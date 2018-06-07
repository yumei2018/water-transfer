package com.gei.controller.service;

import com.gei.constants.Trans;
import com.gei.context.ConnectionContext;
import com.gei.context.LookupDataContext;
import com.gei.context.UserContext;
import com.gei.entities.*;
import com.gei.facades.GenericFacade;
import com.gei.facades.WtStatusTrackFacade;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.util.EntityUtil;
import com.gei.utils.ServletRequestUtil;
import gov.ca.water.transfer.util.WebUtil;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author clay
 */
@Controller
@RequestMapping("/proposalservice")
public class ProposalService implements Serializable {

  //<editor-fold defaultstate="collapsed" desc="Public Proposal Search Functions">
  @RequestMapping("/publicsearch")
  public ModelAndView publicSearch(HttpServletRequest request, HttpServletResponse response) throws IOException{
    ModelAndView mv = new ModelAndView("public/SearchIndex");
    List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
    List<WtCounty> countyList = LookupDataContext.getInstance().getCountyLookup();
    
    mv.addObject("agencyList", agencyList);
    mv.addObject("countyList", countyList);
    return mv;
  }
    
  @RequestMapping("/getpubliclist")
  public void getPublicList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      JSONObject jsonData = new JSONObject(requestUtil.getString("data"));
      String query = "SELECT t1.* FROM WT_PUBLIC_LIST_VIEW t1";
      String conditions = "";
      String and = "";
      String transType = "";
      String waterRights = "";
      String comma = "";
      String or = "";
      
      if(!jsonData.optString("transYear").isEmpty()){
        conditions += and + " TRANS_YEAR = " + jsonData.optInt("transYear");
        and = " AND ";
      }
//      if(!jsonData.optString("swpaoContractNum").isEmpty()){
//        conditions += and + " SWPAO_CONTRACT_NUM = '" + jsonData.optString("swpaoContractNum") + "'";
//        and = " AND ";
//      }
      if(!jsonData.optString("duration").isEmpty()){
        conditions += and + " IS_SHORT_LONG = " + jsonData.optInt("duration");
        and = " AND ";
      }
      // Type of Transfer Query
      if(!jsonData.optString("typeCropIdling").isEmpty()){
        transType += "'100','101','110','111'";
        comma = ",";
//        transType += "1";
//      } else {
//        transType += "0";
      }
      if(!jsonData.optString("typeGroundwater").isEmpty()){
        transType += comma + "'010','110','011','111'";
        comma = ",";
//        transType += "1";
//      } else {
//        transType += "0";
      }
      if(!jsonData.optString("typeReservoir").isEmpty()){
        transType += comma + "'001','011','101','111'";
        comma = ",";
//        transType += "1";
//      } else {
//        transType += "0";
      }      
      if(!"".equals(transType) && !"000".equals(transType)){
        conditions += and + " TRANS_TYPE IN(" + transType + ")";
        and = " AND ";
      }

      // Water Rights Type Query
      if(!jsonData.optString("Statement").isEmpty()||!jsonData.optString("Application").isEmpty()){
        conditions += and;
        or = "";
      
        if(!jsonData.optString("Statement").isEmpty()){
          conditions += " WATER_RIGHTS LIKE '%Statement%'";   
          or = " OR ";
        }
        if(!jsonData.optString("Application").isEmpty()){
          conditions += or + " WATER_RIGHTS LIKE '%Application%'";          
        }         
        and = " AND ";
      }
//      if(!jsonData.optString("Decree").isEmpty()){
//        conditions += or + " WATER_RIGHTS LIKE '%Decree%'";
//        or = " OR ";
//        and = " AND ";
//      } 
//      if(!jsonData.optString("Contract").isEmpty()){
//        conditions += or + " WATER_RIGHTS LIKE '%Contract%'";
//        and = " AND ";
//      }
      
      // Seller Query      
      if(!jsonData.optString("seller").isEmpty()){
        conditions += and;
        or = "";
        JSONArray sellerArray = jsonData.getJSONArray("seller");        
        for(int i=0; i<sellerArray.length(); i++ ){
          String seller = sellerArray.getString(i);
          conditions += or + " SELLER LIKE '%" + seller + "%'";
          or = " OR ";          
        }
        and = " AND ";
      }   
      
      // Buyers Query      
      if(!jsonData.optString("buyer").isEmpty()){
        conditions += and;
        or = "";
        JSONArray buyerArray = jsonData.getJSONArray("buyer");        
        for(int i=0; i<buyerArray.length(); i++ ){
          String buyer = buyerArray.getString(i);
          conditions += or + " BUYERS LIKE '%" + buyer + "%'";
          or = " OR ";          
        }
        and = " AND ";
      } 
      
      // Counties Query      
      if(!jsonData.optString("county").isEmpty()){
        conditions += and;
        or = "";
        JSONArray countyArray = jsonData.getJSONArray("county");        
        for(int i=0; i<countyArray.length(); i++ ){
          String county = countyArray.getString(i);
          conditions += or + " COUNTIES LIKE '%" + county + "%'";
          or = " OR ";          
        }
        and = " AND ";
      } 

      JSONArray data = new JSONArray();
      query += StringUtils.isEmpty(conditions) ? "\nWHERE " : "\nWHERE " + conditions;
      query += and+"t1.IS_ACTIVE = 1 ORDER BY t1.WT_TRANS_ID DESC";
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
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }
  
  @RequestMapping("/publicproposals")
  public void publicProposals(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      String query = "SELECT t1.* FROM WT_LIST_VIEW t1";
      String conditions = "";
      String and = "";
      WtStatusFlag approvedStatus = null;
      
      if ((approvedStatus = LookupDataContext.getInstance().findStatusFlag("PAPPROVED")) == null) {
        throw new Exception("Cannot find the approved status!");
      }
      
      conditions += and + "WT_STATUS_FLAG_ID IN (" + approvedStatus.getWtStatusFlagId() + ")";
      and = " AND ";

      JSONArray data = new JSONArray();
      query += StringUtils.isEmpty(conditions) ? "\nWHERE " : "\nWHERE " + conditions;
      query += and+"t1.IS_ACTIVE = 1 ORDER BY t1.MODIFY_DATE DESC";
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
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Proposal Reports">
  @RequestMapping("/statusreport")
  public ModelAndView statusReport(HttpServletRequest request, HttpServletResponse response) throws IOException{
    ModelAndView mv = null;
    try {      
      List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
      String query = "SELECT t1.* FROM WT_PUBLIC_STATUS_VIEW t1";
      
      JSONArray data = new JSONArray();
      query += " WHERE t1.IS_ACTIVE = 1 ORDER BY t1.RECEIVED_DATE DESC";
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
              if("RECEIVED_DATE".equalsIgnoreCase(colName) && (value != null)) {
                String[] ts = value.toString().split("\\.");
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ts[0]);
                value = new SimpleDateFormat("MM/dd/yyyy").format(date);
              }
              if ("CROPIDLING".equalsIgnoreCase(colName) 
                    || "RESERVOIR".equalsIgnoreCase(colName)
                      || "GROUNDWATER".equalsIgnoreCase(colName)){
//                value = Trans.transTypeMap.get(value);
                if ("1".equalsIgnoreCase(value.toString())){
                  value = "Yes";
                } else {
                  value = "No";
                }
              }
              if ("AGENCY_APPROVAL".equalsIgnoreCase(colName)) {
                value = Trans.agencyApproval.get(value);
              }
              if (value == null){
                json.put(toCamelCase(colName), "");
              } else {
                json.put(toCamelCase(colName), value.toString());
              }
            }
            data.put(json);
          }
        }
      });

      mv = new ModelAndView("public/StatusReport");
      mv.addObject("agencyList", agencyList);
      mv.addObject("data", data);
    }
    catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }    
    return mv;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Proposals">
  @RequestMapping("/getproposals")
  public void getProposals(@RequestParam(value="moduleType",required=false) String moduleType
                           ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      if (!UserContext.getInstance().isLoggedIn()) {
        throw new Exception("Please sign in first!");
      }

      AppUser user = UserContext.getInstance().getUser();
      String query = "SELECT t1.* FROM WT_LIST_VIEW t1";
      String conditions = "";
      String and = "";
      Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
      WtStatusFlag draftStatus = null;
      WtStatusFlag approvedStatus = null;
//      WtStatusFlag suspendStatus = null;
      WtStatusFlag cancelStatus = null;
      WtStatusFlag completeStatus = null;

      if ((draftStatus = LookupDataContext.getInstance().findStatusFlag("DRAFT")) == null) {
        throw new Exception("Cannot find the draft status!");
      }
      if ((approvedStatus = LookupDataContext.getInstance().findStatusFlag("PAPPROVED")) == null) {
        throw new Exception("Cannot find the approved status!");
      }
//      if ((suspendStatus = LookupDataContext.getInstance().findStatusFlag("SUSPEND")) == null) {
//        throw new Exception("Cannot find the suspend status!");
//      }
      if ((cancelStatus = LookupDataContext.getInstance().findStatusFlag("CANCEL")) == null) {
        throw new Exception("Cannot find the draft status!");
      }
      if ((completeStatus = LookupDataContext.getInstance().findStatusFlag("TCOMPLETE")) == null) {
        throw new Exception("Cannot find the transfer complete status!");
      }

      if (user.isAppAccount()||user.isUSBR()) { // For Seller and Buyer there is permission to access proposals
        query += "\nLEFT JOIN WT_TRANS_USER t2\n" +
                  "  ON t1.WT_TRANS_ID = t2.WT_TRANS_ID";
        conditions += and + " t2.USER_ID = " + user.getUserId();
        and = " AND ";
        if (user.isUSBR()){
          conditions += and + "WT_STATUS_FLAG_ID NOT IN ("
                          + draftStatus.getWtStatusFlagId()
                          + "," + cancelStatus.getWtStatusFlagId()
                          + "," + completeStatus.getWtStatusFlagId() + ")";
          and = " AND ";
        }
      }
      else {
        if (user.isReviewer()){
          if("current".equalsIgnoreCase(moduleType)){
            conditions += and + "TRANS_YEAR >= " + currentYear
                          + " AND WT_STATUS_FLAG_ID NOT IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("archived".equalsIgnoreCase(moduleType)){
            conditions += and + "TRANS_YEAR < " + currentYear
                          + " AND WT_STATUS_FLAG_ID NOT IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("submit".equalsIgnoreCase(moduleType)){
            conditions += and + "WT_STATUS_FLAG_ID NOT IN ("
                            + draftStatus.getWtStatusFlagId()
                            + "," + cancelStatus.getWtStatusFlagId()
                            + "," + completeStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if ("historic".equalsIgnoreCase(moduleType)) {
            conditions += and + "WT_STATUS_FLAG_ID IN (" + completeStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else { // Reviewer should not see the DRAFT Proposals
            conditions += and + "WT_STATUS_FLAG_ID NOT IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          }
        } else if(user.isManager()) {
          if("draft".equalsIgnoreCase(moduleType)){
            conditions += and + "WT_STATUS_FLAG_ID IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("current".equalsIgnoreCase(moduleType)){
            conditions += and + "TRANS_YEAR >= " + currentYear
                          + " AND WT_STATUS_FLAG_ID NOT IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("archived".equalsIgnoreCase(moduleType)){
            conditions += and + "TRANS_YEAR < " + currentYear
                          + " AND WT_STATUS_FLAG_ID NOT IN (" + draftStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("submit".equalsIgnoreCase(moduleType)){
            conditions += and + "WT_STATUS_FLAG_ID NOT IN ("
                          + draftStatus.getWtStatusFlagId()
                          + "," + completeStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          } else if("historic".equalsIgnoreCase(moduleType)){
            conditions += and + "WT_STATUS_FLAG_ID IN (" + completeStatus.getWtStatusFlagId() + ")";
            and = " AND ";
          }          
        }
      }

      JSONArray data = new JSONArray();
      query += StringUtils.isEmpty(conditions) ? "\nWHERE " : "\nWHERE " + conditions;
      query += and+"t1.IS_ACTIVE = 1 ORDER BY t1.MODIFY_DATE DESC";
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
//                value = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(date);
                value = new SimpleDateFormat("MM/dd/yyyy").format(date);
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
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Crop Idling Data">
  public void getCropIdling(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Reservoir Data">
  public void getReservoir(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Groundwater Data">
  public void getGroundWater(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Proposal Logs">
  public void getProposalLogs(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {

//      List<WtReviewerComments> reviewerComments = new ArrayList<>();
//      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY CREATED_DATE"
//              ,EntityUtil.getTableName(WtReviewerComments.class));
//      f.select(query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(reviewerComments) {
//        @Override
//        public void handle(ResultSet rs) throws Exception {
//          List<WtReviewerComments> comments = this.getListener();
//          WtReviewerComments comment = null;
//          while (rs.next()) {
//            comment = new WtReviewerComments();
//            comment.Load(rs);
//            comments.add(comment);
//          }
//        }
//      });

      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Proposal Reporting Data">
  public void getProposalReporting(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Proposal Process">
  public void getProposalProcesses(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Proposal Statuses">
  public void getProposalStatuses(
    @RequestParam("wtTransId") Integer transId
    ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {

      // logged in check

      WtStatusTrackFacade f = WebUtil.getFacade(WtStatusTrackFacade.class);
      List<WtStatusTrack> statusTrackList = new ArrayList();
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY STATUS_DATE"
                ,EntityUtil.getTableName(WtStatusTrack.class));
      f.select(query, Arrays.asList(transId), new QueryDelegate(statusTrackList) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          List<WtStatusTrack> statuses = this.getListener();
          WtStatusTrack status = null;
          while (rs.next()) {
            status = new WtStatusTrack();
            status.Load(rs);
            statuses.add(status);
          }
        }
      });

      jsonResponse.put("data", "").put("success", true);
    }
    catch(Exception ex) {
      jsonResponse.put("success", false).put("error",ex.getMessage());
    }
    finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }
  // </editor-fold>
}
