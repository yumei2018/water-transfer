package com.gei.context;

import com.gei.controller.BaseController;
import com.gei.entities.AppGroup;
import com.gei.entities.AppPermission;
import com.gei.entities.AppUser;
import com.gei.entities.UserProfile;
import com.gei.entities.WtContact;
import com.gei.facades.GenericFacade;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.util.EntityUtil;
import gov.ca.water.transfer.util.WebUtil;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author soi
 */
public class UserContext implements Serializable{

  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  final static String SESSION_KEY_LOGGEDIN = "LOGGED_IN";
  final static String SESSION_KEY_USER = "USER";

  @Transient
  private AppUser mUser;

  @Autowired(required = true)
  public oracle.jdbc.pool.OracleDataSource wtDataSource;

  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public UserContext(){

  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Finalize Override">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    this.mUser = null;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Static Get Instance">
  public static UserContext getInstance() {
    return WebUtil.getContext(UserContext.class);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get User">
  @Transient
  public AppUser getUser() {
    return this.mUser;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Logged In Check">
  public Boolean isLoggedIn() {
    return (this.mUser != null);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Authenticate">
  public boolean authenticate(String username, final String rawpassword) {
    try {
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.USERNAME = ?", EntityUtil.getTableName(AppUser.class));
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      GenericFacade.executeQuery(conn, query, Arrays.asList(username), new QueryDelegate(this) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          if (!rs.next()) {
            throw new Exception("The user id and password did not match!");
          }

          AppUser user = new AppUser();
          user.Load(rs);
          if (!Objects.equals(user.getPassword(), BaseController.md5(rawpassword))) {
            throw new Exception("The user id and password did not match!");
          }

          if (!user.isActive()) {
            throw new Exception("The user is not active!");
          }

          UserContext ctx = this.getListener();
          ctx.initSession(user);
        }
      });
    }
    catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    return true;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Initialize User Session">
  public void initSession(AppUser user) {
    try {

      this.mUser = user;
      this.mUser.setPassword(null);

      Connection conn = ConnectionContext.getConnection("WtDataSource");

      //<editor-fold defaultstate="collapsed" desc="Load User Profile ">
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.USER_ID = ?", EntityUtil.getTableName(UserProfile.class));
      GenericFacade.executeQuery(conn, query, Arrays.asList(user.getUserId()), new QueryDelegate(UserContext.getInstance()) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          if (rs.next()) {
            UserContext ctx = this.getListener();
            UserProfile profile = new UserProfile();
            profile.Load(rs);
            profile.setAppUser(ctx.getUser());
            ctx.getUser().setUserProfile(profile);
          }
        }
      });
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="Load App Group and Permissions">
      query = String.format("SELECT %1$s.*"
        + "\n,%4$s.PERMISSION_ID AS PERMISSION_PERMISSION_ID"
        + "\n,%4$s.NAME AS PERMISSION_NAME"
        + "\n,%4$s.CODE AS PERMISSION_CODE"
        + "\n,%4$s.DESCRIPTION AS PERMISSION_DESCRIPTION"
        + "\n,%4$s.CREATED_DATE AS PERMISSION_CREATED_DATE"
        + "\n,%4$s.UPDATED_DATE AS PERMISSION_UPDATED_DATE"
        + "\nFROM %1$s "
        + "\nLEFT JOIN %2$s ON %1$s.GROUP_ID = %2$s.GROUP_ID "
        + "\nLEFT JOIN %3$s "
        + "\nLEFT JOIN %4$s ON %3$s.PERMISSION_ID = %4$s.PERMISSION_ID"
        + "\nON %1$s.GROUP_ID = %3$s.GROUP_ID"
        + "\nWHERE %2$s.USER_ID = ?"
        ,EntityUtil.getTableName(AppGroup.class)
        , "USER_GROUP"
        , "GROUP_PERMISSION"
        , EntityUtil.getTableName(AppPermission.class));
      GenericFacade.executeQuery(conn, query, Arrays.asList(user.getUserId()), new QueryDelegate(this) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          Map<Integer,AppGroup> groups = new LinkedHashMap<>();
          AppGroup group = null;
          Integer groupId = null;
          AppPermission permission = null;
          UserContext ctx = this.getListener();
          while (rs.next()) {
            groupId = rs.getInt("GROUP_ID");
            if ((group = groups.get(groupId)) == null) {
              group =new AppGroup();
              group.Load(rs);
              group.setAppPermissionCollection(new ArrayList<AppPermission>());
              group.setAppUserCollection(new ArrayList<AppUser>());
              group.getAppUserCollection().add(ctx.getUser());
              groups.put(groupId, group);
            }

            permission = new AppPermission();
            permission.Load(rs, "PERMISSION_");
            permission.setAppGroupCollection(new ArrayList<AppGroup>());
            permission.getAppGroupCollection().add(group);
            group.getAppPermissionCollection().add(permission);
          }

          ctx.getUser().setAppGroupCollection(new ArrayList(groups.values()));
        }
      });
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="Load Contact ">
      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_CONTACT_ID = (SELECT %2$s.WT_CONTACT_ID FROM %2$s WHERE %2$s.USER_ID = ?)"
        , EntityUtil.getTableName(WtContact.class)
        , EntityUtil.getTableName(AppUser.class)
      );
      GenericFacade.executeQuery(conn, query, Arrays.asList(user.getUserId()), new QueryDelegate(UserContext.getInstance()) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          if (rs.next()) {
            UserContext ctx = this.getListener();
            WtContact contact = new WtContact();
            contact.Load(rs);
            contact.setUser(ctx.getUser());
            ctx.getUser().setWtContact(contact);
          }
        }
      });
      //</editor-fold>

      HttpSession session = null;
      if ((session = WebUtil.getSession()) != null){
        user.setPassword(null);
        session.setAttribute(SESSION_KEY_LOGGEDIN, true);
        session.setAttribute(SESSION_KEY_USER, this.getUser());
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,UserContext.class.getName()
          ,"initSession(AppUser)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Kill Session">
  public boolean killSession() {
    boolean result = false;
    HttpSession session = null;
    if ((session = WebUtil.getSession()) != null){
      session.removeAttribute(SESSION_KEY_LOGGEDIN);
      session.removeAttribute(SESSION_KEY_USER);
      session.invalidate();
      result = true;
    }
    return result;
  }
  //</editor-fold>
}