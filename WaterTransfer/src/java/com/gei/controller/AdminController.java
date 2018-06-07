package com.gei.controller;

import com.gei.constants.Trans;
import com.gei.context.ConnectionContext;
import com.gei.context.LookupDataContext;
import com.gei.context.UserContext;
import static com.gei.controller.AuthenticationController.SESSION_KEY_USER;
import static com.gei.controller.BaseController.md5;
import com.gei.entities.*;
import com.gei.exception.MyException;
import com.gei.facades.*;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.thread.MailerRunnable;
import com.gei.utils.ServletRequestUtil;
import gov.ca.water.watertransfer.entity.collection.TransCollection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
    @Autowired
    ApplicationContext appContext;

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LoggedInCheckNoAjax(request,response);
        ModelAndView mv = new ModelAndView("adminApp/index");
        return mv;
    }

    //<editor-fold defaultstate="collapsed" desc="CONTACT MANAGER MODULE">
//    public ModelAndView createContact(HttpServletRequest request, HttpServletResponse response)
//    {
//        ModelAndView mv = new ModelAndView("CreateContact");
//        return mv;
//    }

    public void saveContact(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        WtContactPhoneFacade wcpf = (WtContactPhoneFacade)appContext.getBean(WtContactPhoneFacade.class.getSimpleName());
        WtContact contact = new WtContact();
        WtContactPhone phone = new WtContactPhone();
        Collection<WtContactPhone> WtContactPhoneCollection = new ArrayList();

        contact.setTitle(requestUtil.getString("title"));
        contact.setFirstName(requestUtil.getString("firstName"));
        contact.setMiddleName(requestUtil.getString("middleName"));
        contact.setLastName(requestUtil.getString("lastName"));
        contact.setEmail(requestUtil.getString("email"));
        wcf.create(contact);
    }

//    public ModelAndView searchContact(HttpServletRequest request, HttpServletResponse response)
//    {
//        ModelAndView mv = new ModelAndView("SearchContact");
//        WtContactFacade wtf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
//        List<WtContact> contactList = wtf.findAll();
//
//        mv.addObject("contactList", contactList);
//        return mv;
//    }

//    public void saveAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
//        JSONObject jsonResponse = new JSONObject();
//        try{
//          if(!AuthenticationController.IsLoggedIn((request))){
//            throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
//          }
//          ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//          AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
//          AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
//          AppUser user = new AppUser();
//          AppGroup group = agf.find(requestUtil.getInt("groupId"));
//          Collection<AppGroup> appGroupCollection = new ArrayList();
//          if(group != null){ appGroupCollection.add(group); }
//
//          user.setUsername(requestUtil.getString("username"));
//          user.setPassword(md5(requestUtil.getString("password")));
//          user.setActive(1);
//          user.setAppGroupCollection(appGroupCollection);
//
//          auf.create(user);
//        }catch(Exception ex){
//          jsonResponse.put("error", ex.getMessage()).put("success",false);
//          if (ex instanceof MyException){
//            jsonResponse.put("callback", ((MyException)ex).getCallback());
//          }
//        }
//    }
//    public ModelAndView searchAccount(HttpServletRequest request, HttpServletResponse response) throws IOException
//    {
//      ModelAndView mv = null;
//      try{
//        if(!UserContext.getInstance().isLoggedIn()){
//          response.sendRedirect(request.getContextPath());
//          return mv;
//        }
//        mv = new ModelAndView("SearchAccount");
//        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
//        List<AppUser> userList = auf.findAll();
//        mv.addObject("userList", userList);
//      }catch(Exception ex){
//        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
//      }
//      return mv;
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ACCOUNT MANAGER MODULE">
    public ModelAndView accountRegister(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("adminApp/template/AccountRegistration");
//        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
//        List<WtAgency> agencyList = waf.select("SELECT * FROM WT_AGENCY ORDER BY AGENCY_FULL_NAME", com.gei.entities.WtAgency.class);
        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
        mv.addObject("agencyList", agencyList);
        return mv;
    }


    /**
     * Registers a user.
     * @param request jsondata: json serialization of {@linkplain UserRegistration}
     * @param response
     * @throws IOException
     * @throws MalformedURLException
     * @throws EmailException
     */
    @RequestMapping("/register/")
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, MalformedURLException, EmailException
    {
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        Map<String,String> emailConfig = getEmailConfigMap();
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
        UserRegistrationFacade urf = (UserRegistrationFacade)appContext.getBean(UserRegistrationFacade.class.getSimpleName());
        UserRegistration ur = new UserRegistration();
        ur.loadProperties(jsonData.getJSONMap());
        ur.setIsRegistered(0);
        urf.edit(ur);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data",ur.toMap());
        String emailMsg = "</div>There is a new request pending for Login ID from "+ur.getFirstName()+"</div>";
        Boolean success = sendEmail(emailMsg,emailConfig.get("admin.address"));
        jsonResponse.put("success", success);
        response.getWriter().write(jsonResponse.toString());
    }


    /**
     * Creates a model view object with list of contacts,
     * registered users, and application groups.
     * @param request no parameters required.
     * @throws IOException
     */
    @RequestMapping("/edit/")
    public ModelAndView account(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        LoggedInCheck(request, response);
        ModelAndView mv = new ModelAndView("adminApp/edit");
        WtContactFacade wtc = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        UserRegistrationFacade urf = (UserRegistrationFacade)appContext.getBean(UserRegistrationFacade.class.getSimpleName());
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
//        List<AppGroup> groupList = agf.findAll();
//        urf.set("isRegistered", 0);
//        Collection<UserRegistration> ur = urf.findAll();
//        Collection<WtContact> wt = wtc.findAll();
        List<WtContact> contactList = wtc.select("SELECT WT.* FROM WT_CONTACT WT ORDER BY WT_CONTACT_ID DESC", com.gei.entities.WtContact.class);
        List<UserRegistration> urList = urf.select("SELECT * FROM USER_REGISTRATION WHERE IS_REGISTERED=0 ORDER BY USER_ID", com.gei.entities.UserRegistration.class);
        List<AppGroup> groupList = agf.select("SELECT * FROM APP_GROUP ORDER BY CODE", com.gei.entities.AppGroup.class);
        JSONArray registerList = new JSONArray();
        for(UserRegistration register : urList)
        {
            registerList.put(new JSONObject(register.toMap()));
        }
//        for(WtContact contact:wt)
//        {
//            contactList.put(new JSONObject(contact.toMap()).put("userApp", contact.getUser().toMap()));
//        }
        mv.addObject("wtcontact",contactList);
        mv.addObject("register",urList);
        mv.addObject("userReg",registerList);
        mv.addObject("groupList", groupList);
//        mv.addObject("contactInfo",contactList);
        mv.addObject("type","account");
        return mv;
    }

    /**
     *
     * Creates an account based on the wt contact id, registered user id,
     * group id, user id, and password.   The password is optional.
     * The AppUser will be edited.
     * If it is not provided then
     *
     * @param request has parameters :
     * <ul>
     *  <li> WtContactId:Integer </li>
     *  <li> regUserId:Integer </li>
     *  <li> groupId:Integer </li>
     *  <li>  userId:Integer </li>
     *  <li>  password:String </li>
     * </ul>
     * @param response data:AppUser
     *
     * @throws IOException
     * @throws MalformedURLException
     * @throws EmailException
     */
    @RequestMapping("/createAccount/")
    public void createAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, MalformedURLException, EmailException
    {
        LoggedInCheck(request,response);
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
        UserRegistrationFacade urf = (UserRegistrationFacade)appContext.getBean(UserRegistrationFacade.class.getSimpleName());
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());

        HttpSession session = request.getSession();
        AppUser user = (AppUser)session.getAttribute(SESSION_KEY_USER);
        if (user == null) {
          throw new IllegalStateException("User is not defined in session");
        }
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
        JSONObject jsonResponse = new JSONObject();
        AppUser appUser = new AppUser();
        WtContact contact = new WtContact();
        boolean success=false;

        String emailMsg = "Thank you for registering with WTIMS. Below you will find your username and temporary password."
                        + " You will be required to change your temporary password on first login.";
        String subject = "Water Transfer Information Management System Notification";

        if(jsonData.optString("WtContactId").isEmpty())
        {
            int userId = jsonData.optInt("regUserId");
            UserRegistration userReg = urf.find(userId);
            if(userReg != null){
              WtAgency agency = waf.find(userReg.getAgencyId());
              contact.loadProperties(userReg.toMap());
              contact = wcf.find(contact);
              if(contact == null){
                contact = new WtContact();
                if(agency != null){
                  contact.setWtAgency(agency);
                }
                contact.loadProperties(userReg.toMap());
                contact.setCreatedById(user.getUserId());
                wcf.create(contact);
              }
              userReg.setIsRegistered(1);
              urf.edit(userReg);
            }
        }
        else
        {
            int contactId = jsonData.optInt("WtContactId");
            contact = wcf.find(contactId);
            if (contact == null) {
              throw new IllegalArgumentException("Invalid contact id : " + contactId);
            }
            contact.loadProperties(jsonData.getJSONMap());
            contact.setUpdatedById(user.getUserId());
            wcf.edit(contact);
        }
        agf.set("groupId", jsonData.optInt("groupId"));
        Collection<AppGroup> agc = agf.findAll();
        appUser.setAppGroupCollection(agc);
        appUser.loadProperties(jsonData.getJSONMap());
        appUser.setWtContact(contact);
        if(jsonData.has("userId"))
        {
          auf.set("userId", jsonData.optInt("userId"));
          appUser.setPassword(auf.find().getPassword());
          appUser.setCreatedDate(auf.find().getCreatedDate());

        }
        if(jsonData.has("password"))
        {
          appUser.setPassword(md5(jsonData.getString("password")));
        }
        appUser.setNeedPasswordReset(1);
        auf.edit(appUser);

        //Send out Email
        if(jsonData.has("password")){
          String username = jsonData.getString("username");
          String password = jsonData.getString("password");
          emailMsg += "</div><br><div>User ID: " + username
                    + "</div><div>Password: " + password + "</div>";
          success = sendEmail(contact.getEmail().trim(),subject,emailMsg);
          jsonResponse.put("success", success);
        }

        jsonResponse.put("data", appUser.toMap());
        response.getWriter().write(jsonResponse.toString());
    }


    /**
     * Checks if the AppUser exists based on the username and the user id.
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/checkUsername/")
    public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws IOException{
      LoggedInCheck(request,response);
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      JSONObject jsonResponse = new JSONObject();
      String username = requestUtil.getString("username");
      int userId = requestUtil.getInt("userId");
      auf.set("username", username);
      AppUser appUser = auf.find();
      if(appUser != null && userId != appUser.getUserId()){
        jsonResponse.put("user","exist");
      } else {
        jsonResponse.put("user","notExist");
      }
      response.getWriter().write(jsonResponse.toString());
    }

    public ModelAndView getAccountInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("adminApp/template/AccountInformation");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        UserRegistrationFacade urf = (UserRegistrationFacade)appContext.getBean(UserRegistrationFacade.class.getSimpleName());
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
//        List<AppGroup> groupList = agf.findAll();
        List<AppGroup> groupList = agf.select("SELECT * FROM APP_GROUP ORDER BY CODE", com.gei.entities.AppGroup.class);
        WtContact contact = null;
        UserRegistration userReg = null;
        try{
            if (!requestUtil.getString("contactId").isEmpty()){
              contact = wcf.find(requestUtil.getInt("contactId"));
              mv.addObject("isActive",contact.getIsActive());
            }
            if (!requestUtil.getString("userId").isEmpty()){
              userReg = urf.find(requestUtil.getInt("userId"));
            }
            if (contact != null){
              mv.addObject("appuser",contact.getUser());
              mv.addObject("isActive",contact.getUser().getActive());
            }
            if (userReg != null){
              mv.addObject("regUser",userReg);
              mv.addObject("isActive",userReg.getIsRegistered());
            }
        }catch(NullPointerException ex){}
        mv.addObject("contact",contact);
        mv.addObject("groupList", groupList);
        return mv;
    }

  private boolean sendEmail(String emailMsg,String emailAddress) throws MalformedURLException, IOException, EmailException  {
    try {
      Map<String,String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }

      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setSendPartial(true);

      String ccStr = emailConfig.get("cc.address");
      String[] ccs = ccStr.split(";");
      email.setSubject("Water Transfer Information Management System Notification");
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      if(ccs != null){
        for (String cc : ccs) {
          email.addCc(cc.trim());
        }
      }
      email.setHtmlMsg(emailMsg);

      new Thread(new MailerRunnable(email)).start();
      return true;
    } catch (EmailException ex) {
      Logger.getLogger(ProposalController.class).log(Logger.Level.WARN, ex.getMessage());
      return false;
    }
  }

  private boolean sendEmail(String emailAddress, String subject, String emailMsg) throws MalformedURLException, IOException, EmailException  {
    try {
      Map<String,String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));

      String ccStr = emailConfig.get("cc.address");
      String[] ccEmails = ccStr.split(";");

      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addTo(emailAddress);
      if(ccEmails != null){
        for (String ccEmail : ccEmails) {
          email.addCc(ccEmail.trim());
        }
      }
      email.setSubject(subject);
      email.setHtmlMsg(emailMsg);

      // email.send();
      new Thread(new MailerRunnable(email)).start();
      return true;
    } catch (EmailException ex) {
      Logger.getLogger(ProposalController.class).log(Logger.Level.WARN, ex.getMessage());
      return false;
    }
  }

  @RequestMapping(value="/reset",method=RequestMethod.POST)
  public void reset(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    JSONObject jsonResponse = new JSONObject();
    boolean success=false;
    try {
      AppUserFacade f = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      f.set("username", username);

      AppUser user = f.find();
      if (user == null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to find the user name '" + username + "'!");
      } else {
        user.setPassword(md5(password));
        user.setNeedPasswordReset(0);
        f.edit(user);
        success = true;
//        request.getSession().setAttribute(SESSION_KEY_USER, user);
        UserContext.getInstance().initSession(user);
      }
    } catch (Exception exp) {
//      ErrorLogContext.getInstance().log(exp);
    }
    finally {
      jsonResponse.put("success",success);
      response.getWriter().write(jsonResponse.toString());
    }
  }
  //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PERMISSION MANAGER MODULE">
    public ModelAndView groupPermission(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("GroupPermission");
        AppPermissionFacade apf = (AppPermissionFacade)appContext.getBean(AppPermissionFacade.class.getSimpleName());
        List<AppPermission> permissionList = apf.findAll();
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        List<AppGroup> groupList = agf.findAll();

        mv.addObject("permissionList", permissionList);
        mv.addObject("groupList", groupList);
        return mv;
    }

    public void saveGroupPermission(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        AppGroup group = agf.find(requestUtil.getInt("groupId"));

        if(group != null){
            // Create new permission collection for edit
            String[] permissionIds = request.getParameterValues("permissionId");
//            AppPermission permission = null;
            Collection<AppPermission> permissionCollection = new ArrayList();
            if(permissionIds!=null && permissionIds.length>0){
                for(String permissionId : permissionIds){
                    AppPermissionFacade apf = (AppPermissionFacade)appContext.getBean(AppPermissionFacade.class.getSimpleName());
                    AppPermission permission = apf.find(Integer.parseInt(permissionId));
                    permissionCollection.add(permission);
                }
            }

            group.setAppPermissionCollection(permissionCollection);
            agf.edit(group);
        }
    }

    public void getUserAccount(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        Collection<AppGroup> appGroup = agf.findAll();
        JSONArray appArray = new JSONArray();
        for(AppGroup group:appGroup)
        {
            for(AppUser user:group.getAppUserCollection())
            {
                JSONObject obj = new JSONObject(user.toMap());
                obj.put("wtContact",user.getWtContact().getWtContactId());
                obj.put("groupId", group.getGroupId());
                obj.put("name", group.getName());
                obj.put("description", group.getDescription());
                obj.put("code", group.getCode());
                appArray.put(obj);
            }
        }
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data", appArray);
        response.getWriter().write(jsonResponse.toString());
    }

    @RequestMapping("/permission")
    public ModelAndView buyersRepPermission(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("adminApp/BuyersRepPermission");
      AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
      WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
      List<WtAgency> agencyList = waf.select("SELECT * FROM WT_AGENCY ORDER BY AGENCY_FULL_NAME", com.gei.entities.WtAgency.class);
//      List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup(true);
//      List<AppGroup> groupList = LookupDataContext.getInstance().getAppGroupLookup(true);
      AppGroup buyersRepGroup = agf.find(6);
      Collection<AppUser> buyersReps = buyersRepGroup.getAppUserCollection();
      TransCollection transCollection = LookupDataContext.getInstance().getTransCollection(true);

      mv.addObject("buyersReps", buyersReps);
      mv.addObject("agencyList", agencyList);
      mv.addObject("transCollection", transCollection);
      return mv;
    }

  @RequestMapping("/proposallist")
  public void proposalList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      UserContext userContext = UserContext.getInstance();
      if (!userContext.isLoggedIn()) {
        throw new Exception("Please sign in first!");
      }

      // Get proposal List
      JSONArray data = new JSONArray();
      String query = "SELECT t1.* FROM WT_LIST_VIEW t1 WHERE t1.WT_TRANS_ID IN(SELECT t2.WT_TRANS_ID FROM WT_TRANS_USER t2)";
      query += " AND t1.IS_ACTIVE = 1 ORDER BY WT_TRANS_ID";
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(data) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          String colName = null;
          ResultSetMetaData rsmd = rs.getMetaData();
          Class type = null;
          JSONObject json = null;
          Object value = null;
          JSONArray data = this.getListener();
          while(rs.next()){
            json = new JSONObject();
            for (int i=1;i<=rsmd.getColumnCount();++i) {
              colName = rsmd.getColumnName(i);
              type = Class.forName(rsmd.getColumnClassName(i));
              value = rs.getObject(colName,type);
              if(value instanceof oracle.sql.TIMESTAMP && (value != null)) {
                String[] ts = value.toString().split("\\.");
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ts[0]);
                value = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(date);
              }
              if ("TRANS_TYPE".equalsIgnoreCase(colName) && value != null){
                value = Trans.transTypeMap.get(value);
              }
              json.put(toCamelCase(colName), value);
            }
            data.put(json);
          }
        }
      });

      jsonResponse.put("data", data).put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

    @RequestMapping("/changepermission")
    public void changeBuyersRepPermission(@RequestParam("agencyId") Integer agencyId
                                          ,@RequestParam("userId") Integer userId
                                          ,@RequestParam("isChecked") Integer isChecked
                                          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((agencyId == null) || (userId == null)) {
          throw new Exception("The modification agency id and user id cannot be unassigned!");
        }
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
        WtAgency agency = waf.find(agencyId);
        AppUser buyerRep = auf.find(userId);
//        Collection<AppUser> buyerReps = agency.getRepUserCollection();
//        Collection<WtAgency> agencyCollection = buyerRep.getRepAgencyCollection();
        Collection<WtTrans> transCollection = new ArrayList();

        // If Agency is the Buyer, then find all related Trans Proposals out
        if (agency != null && agency.isBuyer()){
          Collection<WtBuyer> buyerCollection = agency.getWtBuyerCollection();
          if(buyerCollection.size() > 0){
            for (WtBuyer buyer: buyerCollection){
              WtTrans trans = buyer.getWtTrans();
              transCollection.add(trans);
            }
          }
        }

        // Link Buyer Representative to Buyer Agency
//        if (buyerReps == null){
//          buyerReps = new ArrayList();
//        }
//        if (isChecked == 1 && !agency.hasRepUser(userId)){
//          agencyCollection.add(agency);
//          buyerRep.setRepAgencyCollection(agencyCollection);
//          buyerReps.add(buyerRep);
//          agency.setRepUserCollection(buyerReps);
//          waf.edit(agency);
//
//          // Link Buyer Representative to Trans Proposals
//          if(transCollection.size()>0){
////            buyerRep.setWtTransCollection(transCollection);
//            for (WtTrans trans: transCollection){
//              linkTransUser(trans, buyerRep);
//            }
//          }
//        }
//        if (isChecked == 0 && agency.hasRepUser(userId)){
//          agencyCollection.remove(agency);
//          buyerRep.setRepAgencyCollection(agencyCollection);
//          buyerReps.remove(buyerRep);
//          agency.setRepUserCollection(buyerReps);
//          waf.edit(agency);

          // Unlink Buyer Representative to Trans Proposals
//          if(transCollection.size()>0){
//            for (WtTrans trans: transCollection){
//              unlinkTransUser(trans, userId);
//            }
//          }
//        }

        jsonResponse.put("success", true);
      } catch (Exception exp) {
        jsonResponse.put("success", false).put("error", exp.getMessage());
        if ((exp instanceof MyException)) {
          jsonResponse.put("callback", ((MyException) exp).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/changetransperm")
    public void changeTransPermission(@RequestParam("transId") Integer transId
                                     ,@RequestParam("userId") Integer userId
                                     ,@RequestParam("isChecked") Integer isChecked
                                     ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((transId == null) || (userId == null)) {
          throw new Exception("The modification proposal id and user id cannot be unassigned!");
        }
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans transRec = wtf.find(transId);

        if (isChecked == 1){
          // Link Buyer Representative to Trans
          linkTransUser(transRec, userId);
        } else if (isChecked == 0){
          // Unlink Buyer Representative to Trans Proposals
          unlinkTransUser(transRec, userId);
        }

        jsonResponse.put("success", true);
      } catch (Exception exp) {
        jsonResponse.put("success", false).put("error", exp.getMessage());
        if ((exp instanceof MyException)) {
          jsonResponse.put("callback", ((MyException) exp).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }

    //Link User Account to Proposal
    private void linkTransUser(WtTrans transRec, int userId){
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      Collection<AppUser> appUserCollection = transRec.getAppUserCollection();
      if(appUserCollection == null || appUserCollection.isEmpty()){
        appUserCollection = new ArrayList();
      }

      if(!transRec.hasUser(userId)){
        AppUser user = auf.find(userId);
        if(user != null){
          appUserCollection.add(user);
          transRec.setAppUserCollection(appUserCollection);
          wtf.edit(transRec);
        }
      }
    }

    //Link User Account to Proposal
    private void linkTransUser(WtTrans transRec, AppUser user){
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      Collection<AppUser> appUserCollection = transRec.getAppUserCollection();
      if(appUserCollection == null || appUserCollection.isEmpty()){
        appUserCollection = new ArrayList();
      }

      if(!transRec.hasUser(user.getUserId())){
        appUserCollection.add(user);
        transRec.setAppUserCollection(appUserCollection);
        wtf.edit(transRec);
      }
    }

    //Unlink User Account to Proposal
    private void unlinkTransUser(WtTrans transRec, int userId){
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      Collection<AppUser> appUserCollection = transRec.getAppUserCollection();

      if(!appUserCollection.isEmpty() && transRec.hasUser(userId)){
        AppUser user = auf.find(userId);
        if(user != null){
          appUserCollection.remove(user);
          transRec.setAppUserCollection(appUserCollection);
          wtf.edit(transRec);
        }
      }
    }

//    @RequestMapping("/repagencylist")
//    public void getRepAgencyList(@PathVariable("userId") Integer userId,
//                                         HttpServletRequest request, HttpServletResponse response) throws IOException {
//      JSONObject jsonResponse = new JSONObject();
//      LoggedInCheck(request, response);
//      try{
//        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup(true);
//
//        jsonResponse = new JSONObject(agencyList.toMap());
//      } catch (Exception exp) {
//        jsonResponse.put("success", false).put("error", exp.getMessage());
//        if ((exp instanceof MyException)) {
//          jsonResponse.put("callback", ((MyException) exp).getCallback());
//        }
//      } finally {
//        response.getWriter().write(jsonResponse.toString());
//      }
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AGENCY MANAGER MODULE">
    public ModelAndView getAgencyList(HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("adminApp/AgencyList");
      List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup(true);
      mv.addObject("agencyList", agencyList);
      return mv;
    }

    public ModelAndView getNewAgencyForm(HttpServletRequest request, HttpServletResponse response)
    {
      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("templates/addAgency");
      return mv;
    }

    public void saveAgency(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtAgency agency = new WtAgency();
        String agencyId = requestUtil.getString("wtAgencyId");
        if(!agencyId.isEmpty()){
          agency = waf.find(Integer.parseInt(agencyId));
        }
        agency.loadProperties(request);

        if(agencyId.isEmpty())
        {
          String agencyFullName = requestUtil.getString("agencyFullName");
          Boolean exist = false;
          Collection<WtAgency> agencyCollection = waf.findAll();
          for(WtAgency one:agencyCollection){
            if (agencyFullName.equalsIgnoreCase(one.getAgencyFullName())){
              exist = true;
            }
          }
          if(!exist){
            agency.setAgencyActiveInd(1);
            waf.create(agency);
          }
        }
        else
        {
          waf.edit(agency);
        }
        jsonResponse = new JSONObject(agency.toMap());
        jsonResponse.put("success", true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public void removeAgency(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        String agencyId = requestUtil.getString("wtAgencyId");
        if(!agencyId.isEmpty()){
            WtAgency agency = waf.find(Integer.parseInt(agencyId));
            waf.remove(agency);
            jsonResponse.put("data",agency.toMap());
        }
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public void addNewAgency(HttpServletRequest request, HttpServletResponse response)throws IOException{
      WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//      JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));

      String agencyFullName = requestUtil.getString("agencyFullName");
      Boolean exist = false;
      Collection<WtAgency> agencyCollection = waf.findAll();
      for(WtAgency agency:agencyCollection){
        if (agencyFullName.equalsIgnoreCase(agency.getAgencyFullName())){
          exist = true;
        }
      }

      if(!exist){
        WtAgency agency = new WtAgency();
//        String agencyCode = "";
//        String[] agencyNameArray = agencyFullName.split("\\s+");
//        for(String agencyName:agencyNameArray){
//          agencyCode += agencyName.substring(0,1);
//        }
//        agency.setAgencyCode(agencyCode);
//        agency.setAgencyFullName(agencyFullName);
//        agency.setAgencyType("AG");

        agency.loadProperties(request);
        agency.setAgencyActiveInd(1);
        waf.create(agency);

        JSONObject jsonDataNew = new JSONObject(agency.toMap());
        response.getWriter().write(jsonDataNew.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROPOSAL MANAGER MODULE">
    @RequestMapping("/deletealldraft")
    public void deletealldraft(HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try {
        WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
        List<WtTrans> draftProposalList = wtf.select("SELECT WT.* FROM WT_TRANS WT WHERE WT_STATUS_FLAG_ID=0",com.gei.entities.WtTrans.class);

        for(WtTrans draftProposal:draftProposalList){
          draftProposal.setIsActive(0);
          wtf.edit(draftProposal);
        }
        jsonResponse.put("success", true);
      } catch (Exception exp) {
        jsonResponse.put("success", false).put("error", exp.getMessage());
        if (exp instanceof MyException) {
          jsonResponse.put("callback", ((MyException) exp).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>
}
