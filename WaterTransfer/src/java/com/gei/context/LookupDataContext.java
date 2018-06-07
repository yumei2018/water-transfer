package com.gei.context;

import com.gei.entities.*;
import com.gei.facades.*;
import com.gei.facades.delegates.EntityCollectionLoadDelegate;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.util.EntityUtil;
import gov.ca.water.transfer.util.WebUtil;
import gov.ca.water.watertransfer.entity.collection.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Charlie Lay
 */
public class LookupDataContext implements Serializable {

  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private Map mLookupData;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public LookupDataContext(){
    this.mLookupData = new LinkedHashMap<>();
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Finalize Override">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    this.mLookupData.clear();
    this.mLookupData = null;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Private Autocast Get Lookup Data">
  private <O extends Object> O getLookupData(String key) {
    O result = null;
    if (this.mLookupData.containsKey(key)) {
      result = (O) this.mLookupData.get(key);
    }
    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Static Get Instance">
  public static LookupDataContext getInstance(){
    return WebUtil.getContext(LookupDataContext.class);
  }
  //</editor-fold>

  //<editor-fold defaultstate="expanded" desc="Lookup Data Logic Methods">
  /**
   *
   * @param requery
   * @return
   */
  public List<WtCity> getCityLookup() {
    return this.getCityLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtCity> getCityLookup(boolean requery) {
    List<WtCity> result = null;
    String key = WtCity.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.\"NAME\"",EntityUtil.getTableName(WtCity.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtCity> cities = this.getListener();
            WtCity city = null;
            while(rs.next()) {
              city = new WtCity();
              EntityUtil.Load(rs, city);
              cities.add(city);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getCityLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }


  /**
   *
   * @param requery
   * @return
   */
  public List<WtState> getStateLookup() {
    return this.getStateLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtState> getStateLookup(boolean requery) {
    List<WtState> result = null;
    String key = WtState.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.\"NAME\"", EntityUtil.getTableName(WtState.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtState> states = this.getListener();
            WtState state = null;
            while(rs.next()) {
              state = new WtState();
              EntityUtil.Load(rs, state);
              states.add(state);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getStateLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtStatusFlag> getStatusFlagLookup() {
    return this.getStatusFlagLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtStatusFlag> getStatusFlagLookup(boolean requery) {
    List<WtStatusFlag> result = null;

    String key = WtStatusFlag.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.WT_STATUS_FLAG_ID", EntityUtil.getTableName(WtStatusFlag.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtStatusFlag> statuses = this.getListener();
            WtStatusFlag status = null;
            while(rs.next()) {
              status = new WtStatusFlag();
              status.Load(rs);
              statuses.add(status);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getStatusFlagLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }

  /**
   *
   * @param requery
   * @return
   */
  public AgencyCollection getAgencyLookup() {
    return this.getAgencyLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public AgencyCollection getAgencyLookup(boolean requery) {
    AgencyCollection result = null;
    String key = WtAgency.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.AGENCY_FULL_NAME", EntityUtil.getTableName(WtAgency.class));
        result = new AgencyCollection();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            AgencyCollection agencies = this.getListener();
            WtAgency agency = null;
            while(rs.next()) {
              agency = new WtAgency();
              agency.Load(rs);
              agencies.add(agency);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getAgencyLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }
  
  /**
   *
   * @param requery
   * @return
   */
  public List<WtContact> getAgencyContactsLookup() {
    return getBuyersContactLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtContact> getAgencyContactsLookup(boolean requery) {
    List<WtContact> result = null;
    String key = WtContact.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
//        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_AGENCY_ID IS NOT NULL ORDER BY %1$s.LAST_NAME", EntityUtil.getTableName(WtContact.class));
        String query = String.format("SELECT t2.*,t1.* FROM %1$s t1 JOIN %2$s t2 ON t2.WT_STATE_ID = t1.WT_STATE_ID WHERE t1.WT_AGENCY_ID IS NOT NULL ORDER BY t1.LAST_NAME"
                                      , EntityUtil.getTableName(WtContact.class)
                                      , "WT_STATE");

        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtContact> contacts = this.getListener();
            WtContact contact = null;
            WtState state = null;
            while(rs.next()) {
              state = new WtState();
              state.Load(rs);
              
              contact = new WtContact();
              contact.Load(rs);
              contact.setWtState(state);
              
              contacts.add(contact);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getBuyersContactLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }
    return result;
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtContact> getBuyersContactLookup() {
    return getBuyersContactLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<WtContact> getBuyersContactLookup(boolean requery) {
    List<WtContact> result = null;
    String key = WtContact.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
//        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.IS_BUYERS_CONTACT = 1 ORDER BY %1$s.LAST_NAME", EntityUtil.getTableName(WtContact.class));
        String query = String.format("SELECT t2.*,t1.* FROM %1$s t1 LEFT JOIN %2$s t2 ON t2.WT_STATE_ID = t1.WT_STATE_ID WHERE t1.IS_BUYERS_CONTACT = 1 ORDER BY t1.LAST_NAME"
                                      , EntityUtil.getTableName(WtContact.class)
                                      , "WT_STATE");
//        String query = String.format("SELECT t1.* FROM %1$s t1 WHERE t1.IS_BUYERS_CONTACT = 1 ORDER BY t1.LAST_NAME"
//                                      , EntityUtil.getTableName(WtContact.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtContact> contacts = this.getListener();
            WtContact contact = null;
            WtState state = null;
            while(rs.next()) {
              state = new WtState();
              state.Load(rs);
              
              contact = new WtContact();
              contact.Load(rs);
              contact.setWtState(state);
              
              contacts.add(contact);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getBuyersContactLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }
    return result;
  }
  
    /**
   *
   * @param requery
   * @return
   */
  public List<AppGroup> getAppGroupLookup() {
    return this.getAppGroupLookup(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public List<AppGroup> getAppGroupLookup(boolean requery) {
    List<AppGroup> result = null;

    String key = WtStatusFlag.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.GROUP_ID", EntityUtil.getTableName(AppGroup.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<AppGroup> groups = this.getListener();
            AppGroup group = null;
            while(rs.next()) {
              group = new AppGroup();
              group.Load(rs);
              groups.add(group);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getAppGroupLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Find State Methods">
  public WtState findState(Integer id) {
    WtState result = null;
    if ((this.getStateLookup() == null) || (this.getStateLookup().isEmpty())) {
      this.getStateLookup(true);
    }

    if (this.getStateLookup() != null) {
      for (WtState state : this.getStateLookup()) {
        if (Objects.equals(state.getWtStateId(), id)) {
          result = state;
          break;
        }
      }
    }

    return result;
  }

  public WtState findState(String code) {
    WtState result = null;
    if ((this.getStateLookup() == null) || (this.getStateLookup().isEmpty())) {
      this.getStateLookup(true);
    }

    if (this.getStateLookup() != null) {
      for (WtState state : this.getStateLookup()) {
        if (Objects.equals(state.getShortName(), code)) {
          result = state;
          break;
        }
      }
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Find City">
  public WtCity findCity(Integer id) {
    WtCity result = null;
    if ((this.getCityLookup()== null) || (this.getCityLookup().isEmpty())) {
      this.getCityLookup(true);
    }

    if (this.getCityLookup() != null) {
      for (WtCity city : this.getCityLookup()) {
        if (Objects.equals(city.getWtCityId(), id)) {
          result = city;
          break;
        }
      }
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Find Agency">
  public WtAgency findAgency(Integer id) {
    WtAgency result = null;
    if ((this.getAgencyLookup()== null) || (this.getAgencyLookup().isEmpty())) {
      this.getAgencyLookup(true);
    }

    if (this.getAgencyLookup() != null) {
      for (WtAgency agency : this.getAgencyLookup()) {
        if (Objects.equals(agency.getWtAgencyId(), id)) {
          result = agency;
          break;
        }
      }
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Find Status Flag">
  public WtStatusFlag findStatusFlag(String name) {
    WtStatusFlag result = null;
    if ((this.getStatusFlagLookup() == null) || (this.getStatusFlagLookup().isEmpty())) {
      this.getStatusFlagLookup(true);
    }

    if (this.getStatusFlagLookup() != null) {
      for (WtStatusFlag status : this.getStatusFlagLookup()) {
        if (Objects.equals(status.getStatusName(), name)) {
          result = status;
          break;
        }
      }
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get County Lookup">
  public List<WtCounty> getCountyLookup(){
    return getCountyLookup(false);
  }
  /**
   *
   * @param requery
   * @return
   */
  public List<WtCounty> getCountyLookup(boolean requery){
    CountyCollection result = null;
    try {
      String key = WtCounty.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new CountyCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.NAME"
          ,EntityUtil.getTableName(WtCounty.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            CountyCollection counties = this.getListener();
            WtCounty county = null;
            while (rs.next()) {
              county = new WtCounty();
              county.Load(rs);
              counties.add(county);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getCountyLookup(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Wells">
  /**
   *
   * @param requery
   * @return
   */
  public WellCollection getWells() {
    return this.getWells(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public WellCollection getWells(boolean requery) {
    WellCollection result = null;
    String key = WtWell.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT \n" +
                                 "  t1.*\n" +
                                 "  ,t2.*\n" +
                                 "\nFROM %1$s t1\n" +
                                 "\nLEFT JOIN %2$s t3\n" +
                                 "\nJOIN %3$s t2\n" +
                                 "\nON t2.WT_ATTACHMENT_ID  = t3.WT_ATTACHMENT_ID\n" +
                                 "\nON t1.WT_WELL_ID = t3.WT_WELL_ID ORDER BY t1.WT_WELL_ID"
                                 , EntityUtil.getTableName(WtWell.class)
                                 , "WT_WELL_ATTACHMENT"
                                 , "ATTACHMENT_VIEW");
        result = new WellCollection();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            WellCollection wells = this.getListener();
            Map<Integer,WtWell> wellmap = new LinkedHashMap<>();
            WtWell well = null;
            WtAttachment attachment = null;
            Integer wellId = null;
            AppUser user = null;
            WtContact contact = null;
            while(rs.next()) {
              wellId = rs.getInt("WT_WELL_ID");
              if ((well = wellmap.get(wellId)) == null) {
                well = new WtWell();
                well.Load(rs);
                well.setWtAttachmentCollection(new ArrayList<WtAttachment>());
                wellmap.put(wellId, well);
              }
              user = new AppUser();
              user.Load(rs, EntityUtil.getTableName(AppUser.class) + "_");

              contact = new WtContact();
              contact.Load(rs, EntityUtil.getTableName(WtContact.class) + "_");

              user.setWtContact(contact);
              contact.setUser(user);

              attachment = new WtAttachment();
              attachment.Load(rs);

              attachment.setCreatedBy(user);
              if(!well.getWtAttachmentCollection().contains(attachment)){
                well.getWtAttachmentCollection().add(attachment);
              }
              wells.add(well);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getWells(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }


    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Checklists">
  public ChecklistCollection getChecklists() {
    return this.getChecklists(false);
  }

  public ChecklistCollection getChecklists(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.CHECKLIST_FIELD, %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getBIChecklist() {
    return this.getBIChecklist(false);
  }

  public ChecklistCollection getBIChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'BI_ATTACHMENT\' ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getCIChecklist() {
    return this.getCIChecklist(false);
  }

  public ChecklistCollection getCIChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'CI_ATTACHMENT\' ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getCIMapChecklist() {
    return this.getCIMapChecklist(false);
  }

  public ChecklistCollection getCIMapChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'CI_MAP_ATTACHMENT\' ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getRVChecklist() {
    return this.getRVChecklist(false);
  }

  public ChecklistCollection getRVChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'RV_ATTACHMENT\' ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getGWChecklist() {
    return this.getGWChecklist(false);
  }

  public ChecklistCollection getGWChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'GW_ATTACHMENT\' ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getWellTransChecklist() {
    return this.getWellTransChecklist(false);
  }

  public ChecklistCollection getWellTransChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'WELL_ATTACHMENT\'"
                                      + " AND %1$s.WELL_TRANSFER=1 ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  
  public ChecklistCollection getWellMonitChecklist() {
    return this.getWellMonitChecklist(false);
  }

  public ChecklistCollection getWellMonitChecklist(boolean requery) {
    ChecklistCollection result = null;
    try {
      String key = WtChecklist.class.getSimpleName();
      if (requery || ((result = this.getLookupData(key)) == null)) {
        result = new ChecklistCollection();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD=\'WELL_ATTACHMENT\'"
                                      + " AND %1$s.WELL_MONITORING=1 ORDER BY %1$s.SORT_ORDER"
          ,EntityUtil.getTableName(WtChecklist.class)
        );

        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ChecklistCollection checklists = this.getListener();
            WtChecklist checklist = null;
            while (rs.next()) {
              checklist = new WtChecklist();
              checklist.Load(rs);
              checklists.add(checklist);
            }
          }
        });

        this.mLookupData.put(key, result);
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklists(boolean)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Purpose Reservoirs">
  public List<WtPurpose> getPurposeReservoir(){
    return getPurposeReservoir(false);
  }
  /**
   *
   * @param requery
   * @return
   */
  public List<WtPurpose> getPurposeReservoir(boolean requery) {
    List<WtPurpose> result = null;
    String key = WtPurpose.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.PURPOSE ", EntityUtil.getTableName(WtPurpose.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtPurpose> purposes = this.getListener();
            WtPurpose purpose = null;
            while(rs.next()) {
              purpose = new WtPurpose();
              purpose.Load(rs);
              purposes.add(purpose);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getBuyersContactLookup(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }
    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Fu Types">
  public FuTypeCollection getFuTypes() {
    FuTypeCollection result = null;
    String key = WtFuType.class.getSimpleName();
    if ((result = this.getLookupData(key)) == null) {
      result = new FuTypeCollection();
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s",EntityUtil.getTableName(WtFuType.class));
        GenericFacade.executeQuery(conn, query, null, new EntityCollectionLoadDelegate(result,WtFuType.class));
        this.mLookupData.put(key, result);
      }
      catch (Exception e) {
        throw new IllegalStateException(
          String.format("%s.getFuTypes() %s:\n%s"
            ,this.getClass().getName()
            ,e.getClass().getName()
            ,e.getMessage()
          )
        );
      }
    }
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Trans">
  /**
   *
   * @param requery
   * @return
   */
  public TransCollection getTransCollection() {
    return this.getTransCollection(false);
  }

  /**
   *
   * @param requery
   * @return
   */
  public TransCollection getTransCollection(boolean requery) {
    TransCollection result = null;
    String key = WtTrans.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT \n" +
                                 "  t1.*\n" +
                                 "\nFROM %1$s t1\n" +
                                 "\nWHERE t1.WT_TRANS_ID IN(SELECT t2.WT_TRANS_ID FROM %2$s t2) ORDER BY t1.WT_TRANS_ID"
                                 , EntityUtil.getTableName(WtTrans.class)
                                 , "WT_TRANS_USER");
//        String query = String.format("SELECT \n" +
//                                 "  t1.*\n" +
//                                 "  ,t2.*\n" +
//                                 "\nFROM %1$s t1\n" +
//                                 "\nLEFT JOIN %2$s t3\n" +
//                                 "\nJOIN %3$s t2\n" +
//                                 "\nON t2.WT_TRANS_ID = t3.WT_TRANS_ID\n" +
//                                 "\nON t3.USER_ID = t1.USER_ID ORDER BY t3.WT_TRANS_ID"
//                                 , EntityUtil.getTableName(AppUser.class)
//                                 , "WT_TRANS_USER"
//                                 , "WT_LIST_VIEW");
        result = new TransCollection();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            TransCollection transCollection = this.getListener();
            WtTrans trans = null;
            AppUser user = null;
            while(rs.next()) {
              trans = new WtTrans();
              trans.Load(rs);
              
//              user = new AppUser();
//              user.Load(rs);
//              
//              if(!trans.getAppUserCollection().contains(user)){
//                trans.getAppUserCollection().add(user);
//              }
              
              transCollection.add(trans);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getTransCollection(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }

    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Crop Type">
  public List<WtCropType> getCropType(){
    return getCropType(false);
  }
  /**
   *
   * @param requery
   * @return
   */
  public List<WtCropType> getCropType(boolean requery) {
    List<WtCropType> result = null;
    String key = WtCropType.class.getSimpleName();
    if (requery || ((result = this.getLookupData(key)) == null)) {
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s ORDER BY %1$s.WT_CROP_TYPE_ID ", EntityUtil.getTableName(WtCropType.class));
        result = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(result) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtCropType> cropTypeList = this.getListener();
            WtCropType cropType = null;
            while(rs.next()) {
              cropType = new WtCropType();
              cropType.Load(rs);
              cropTypeList.add(cropType);
            }
          }
        });
        this.mLookupData.put(key, result);
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"getCropType(boolean)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
    }
    return result;
  }
  //</editor-fold>
}
