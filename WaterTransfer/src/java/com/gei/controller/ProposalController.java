package com.gei.controller;

import com.gei.constants.ReportType;
import com.gei.constants.Status;
import com.gei.constants.Trans;
import com.gei.context.ConnectionContext;
import com.gei.context.LookupDataContext;
import com.gei.context.UserContext;
import com.gei.entities.*;
import com.gei.exception.MyException;
import com.gei.facades.*;
import com.gei.facades.delegates.EntityCollectionLoadDelegate;
import com.gei.facades.delegates.EntityLoadDelegate;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.io.xml.JAXBHelper;
import com.gei.thread.MailerRunnable;
import com.gei.util.EntityUtil;
import com.gei.util.StringUtil;
import com.gei.utils.ServletRequestUtil;
import gov.ca.water.transfer.util.WebUtil;
import gov.ca.water.watertransfer.entity.collection.AgencyCollection;
import gov.ca.water.watertransfer.entity.collection.BuyerCollection;
import gov.ca.water.watertransfer.entity.collection.ReviewNoteCollection;
import gov.ca.water.watertransfer.entity.collection.TransTypeCollection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/proposal")
public class ProposalController extends BaseController {
    @Autowired
    ApplicationContext appContext;
    public static final int INVALID_INTEGER = -9999;
    public static final String GROUP_USBR = "USBR";

    //<editor-fold defaultstate="collapsed" desc="Proposal List">
    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException{
      ModelAndView mv = null;
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        if (user.isManager()){
          mv = new ModelAndView("proposal/index_admin");
        } else if (user.isAppAccount()) {
          mv = new ModelAndView("proposal/index");
        } else {
          mv = new ModelAndView("proposal/index_review");
        }

        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        mv.addObject("moduleType", requestUtil.getString("moduleType"));
      }catch(Exception ex){
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
      return mv;
    }

    @RequestMapping("/all")
    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request,response);
      WtTransFacade f = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      List<WtTrans> wts = f.findAll();
      JSONObject jsonResponse = new JSONObject(),tmpJson;
      jsonResponse.put("data",new JSONArray());
      WtBuyer buyer;
      WtAgency seller;
      String buyerNames,sellerNames,key,comma="";
      Iterator<WtBuyer> buyersIt;
      Iterator<WtAgency> sellerIt;
      for (WtTrans wt : wts){
        tmpJson = new JSONObject(wt.toMap());
        tmpJson.put("buyers","");
        tmpJson.put("seller","");
        if (wt.getWtBuyerCollection()!=null){
          buyersIt = wt.getWtBuyerCollection().iterator();
          if (buyersIt != null){
            comma="";
            buyerNames="";
            key="buyers";
            while (buyersIt.hasNext()){
              buyer = buyersIt.next();
              buyerNames = tmpJson.optString(key) + comma + buyer.getWtAgency().getAgencyFullName();
              buyerNames += buyer.getSharePercent() == null ? "" : " (" +buyer.getSharePercent() + "%)";
              comma=",";
            }//end while
            tmpJson.put(key, buyerNames);
          }//end if
        }//end if
        if (wt.getWtSellerCollection()!=null){
            sellerIt = wt.getWtSellerCollection().iterator();
            if (sellerIt != null){
                comma="";
                sellerNames="";
                key="seller";
                while (sellerIt.hasNext()){
                   seller = sellerIt.next();
                   sellerNames += tmpJson.optString(key)+comma + seller.getAgencyFullName();
                   comma=",";
                }
                tmpJson.put(key,sellerNames);
            }
        }//end if
        jsonResponse.optJSONArray("data").put(tmpJson);
      }//end for
      response.getWriter().write(jsonResponse.toString());
    }

    @RequestMapping("/proposalList")
    public void getProposalList(HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request,response);
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade f = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      AppUserFacade auf = WebUtil.getFacade(AppUserFacade.class);//(AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());

      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      Collection<WtTrans> wts = null;
      String moduleType = requestUtil.getString("moduleType");

      // Proposal List return will depend on the user group and module type
      if (user.isReviewer()){
        wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID NOT IN("+Status.DRAFT+","+Status.TCOMPLETE+")",com.gei.entities.WtTrans.class);
//        wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID IN("+Status.SUBMITTED+","+Status.REVIEW+")",com.gei.entities.WtTrans.class);
        if("submit".equalsIgnoreCase(moduleType)){
          wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID NOT IN("+Status.DRAFT+","+Status.TCOMPLETE+")",com.gei.entities.WtTrans.class);
        }
      } else if(user.isManager()) {
        wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID NOT IN("+Status.DRAFT+")",com.gei.entities.WtTrans.class);
        if("submit".equalsIgnoreCase(moduleType)){
          wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID NOT IN("+Status.DRAFT+","+Status.TCOMPLETE+")",com.gei.entities.WtTrans.class);
        }
        else if("historic".equalsIgnoreCase(moduleType)){
          wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID IN("+Status.TCOMPLETE+")",com.gei.entities.WtTrans.class);
        }
        else if("draft".equalsIgnoreCase(moduleType)){
          wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID IN("+Status.DRAFT+")",com.gei.entities.WtTrans.class);
        }
      } else if (user.isAppAccount()) { // For Celler and Buyer there is permission to access proposals
        user = auf.find(user.getUserId());
        wts = user.getWtTransCollection();
      }

      if (wts == null) {
        wts = f.findAll();
      }

      JSONObject jsonResponse = new JSONObject(),tmpJson;
      jsonResponse.put("data",new JSONArray());
      WtBuyer buyer;
      WtAgency seller;
      String buyerNames,sellerNames,key,comma="";
      Iterator<WtBuyer> buyersIt;
        Iterator<WtAgency> sellerIt;
      for (WtTrans wt : wts){
        tmpJson = new JSONObject(wt.toMap());
        tmpJson.put("buyers","");
        tmpJson.put("seller","");
        if (wt.getWtBuyerCollection()!=null){
          buyersIt = wt.getWtBuyerCollection().iterator();
          if (buyersIt != null){
            comma="";
            buyerNames="";
            key="buyers";
            while (buyersIt.hasNext()){
              buyer = buyersIt.next();
              // Use Agency Code
              buyerNames += tmpJson.optString(key) + comma + buyer.getWtAgency().getAgencyCode();
              comma=",";
            }//end while
            tmpJson.put(key, buyerNames);
          }//end if
        }//end if
        if (wt.getWtSellerCollection()!=null){
            sellerIt = wt.getWtSellerCollection().iterator();
            if (sellerIt != null){
                comma="";
                sellerNames="";
                key="seller";
                while (sellerIt.hasNext()){
                   seller = sellerIt.next();
//                   sellerNames += tmpJson.optString(key)+comma + seller.getAgencyFullName();
                   // Use Agency Code
                   sellerNames += tmpJson.optString(key) + comma + seller.getAgencyCode();
                   comma=",";
                }
                tmpJson.put(key,sellerNames);
            }
        }//end if
        if (wt.getWtStatusFlag()!=null){
            tmpJson.put("status", wt.getWtStatusFlag().getStatusName());
        }
        if (wt.getCreateDate() == null){
            tmpJson.put("createDate", "");
        } else {
            tmpJson.put("createDate", df.format(wt.getCreateDate()));
        }
        jsonResponse.optJSONArray("data").put(tmpJson);
      }//end for
      response.getWriter().write(jsonResponse.toString());
    }

//    @RequestMapping("/newProposalList")
//    public ModelAndView newProposalList(HttpServletRequest request, HttpServletResponse response)
//    {
//        ModelAndView mv = new ModelAndView("proposal/ProposalList");
//        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
//
//        WtTrans t = new WtTrans();
//        t.setTransYear(2015);
//        List<WtTrans> records = wtf.findAll(t);
//
//        mv.addObject("proposalList", records);
//        return mv;
//    }

    @RequestMapping("/cancelnotice")
    public void cancelProposalNotice(@RequestParam("wtTransId") Integer transId
                                     , HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try {
        if (transId == null) {
          throw new Exception("The transfer id cannot be unassigned!");
        }
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans proposal = wtf.find(transId);
        if (proposal == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        AppUser user = UserContext.getInstance().getUser();
        String userEmail = "";
        String userPhone = "";
        if (null != user && null != user.getWtContact()){
          userEmail = user.getWtContact().getEmail();
          userPhone = user.getWtContact().getPhoneNumber();
        }
        String query = "SELECT t1.* FROM WT_LIST_VIEW t1 WHERE WT_TRANS_ID="+transId;
        String swpaoNum = "";
        String seller = "";
        String buyers = "";
        HashMap<String,String> dataMap = new HashMap();
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(dataMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap dataMap = this.getListener();
            while (rs.next()) {
              dataMap.put("swpaoNum", rs.getString("SWPAO_CONTRACT_NUM"));
              dataMap.put("seller", rs.getString("SELLERFULL"));
              dataMap.put("buyers", rs.getString("BUYERSFULL"));
            }
          }
        });

        if (null != dataMap.get("swpaoNum")){
          swpaoNum = dataMap.get("swpaoNum");
        }
        if (null != dataMap.get("seller")){
          seller = dataMap.get("seller");
        }
        if (null != dataMap.get("buyers")){
          buyers = dataMap.get("buyers");
        }

        //Build and Send Email
        Map<String, String> emailConfig = getEmailConfigMap();
        String toEmail = emailConfig.get("manager.address");
        List<String> ccEmails = new ArrayList<>();
        ccEmails.add(userEmail);
        ccEmails.add(toEmail);
        ccEmails.addAll(getAgencyEmails(getSellerAgency(proposal)));
        ccEmails.addAll(getSeniorEmails());
        String subject = "Cancellation request: SWPAO #"+swpaoNum;
//        String content = "Seller requested to cancel proposal between "+seller+" and "+buyers+".</br>"
//                         + "Please refer to SWPAO #"+swpaoNum;
        String content = seller + " requested to cancel SWPAO #"+swpaoNum+" water transfer proposal.</br>"
                         + "Phone Number: " + userPhone;
        sendEmail(toEmail,ccEmails,subject,content);

        jsonResponse.put("success", true);
      }
      catch(Exception ex) {
        jsonResponse.put("success", false).put("error",ex.getMessage());
      }
      finally {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/deleteproposal/{transId}")
    public void deleteProposal(@PathVariable("transId") Integer transId
                              ,HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException, MyException, Exception{
      JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
        WtPreTransferFacade wptf = (WtPreTransferFacade)appContext.getBean(WtPreTransferFacade.class.getSimpleName());
        WtWaterRightsFacade wwrf = (WtWaterRightsFacade)appContext.getBean(WtWaterRightsFacade.class.getSimpleName());
        WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCiCroptypeFacade wccf = (WtCiCroptypeFacade) appContext.getBean(WtCiCroptypeFacade.class.getSimpleName());
        WtRvTarstorFacade wrtf = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());

        WtTrans proposal = wtf.find(transId);
        if (proposal == null){
          throw new Exception("Unable to find the record with wtTransId: " + transId);
        }

        // Remove all Attachments from Proposal
        Collection<WtAttachment> attachmentCollection = proposal.getWtAttachmentCollection();
        if(attachmentCollection != null){
          for(WtAttachment attachment:attachmentCollection){
            attachment.setWtTrans(null);
//            waf.remove(attachment);
          }
          proposal.setWtAttachmentCollection(null);
        }

        // Remove all agreements for the five most recent water transfers
        Collection<WtPreTransfer> preTransferCollection = proposal.getWtPreTransferCollection();
        if(preTransferCollection != null){
          for(WtPreTransfer preTransfer:preTransferCollection){
            preTransfer.setWtTrans(null);
            wptf.remove(preTransfer);
          }
          proposal.setWtPreTransferCollection(null);
        }

        // Remove all WaterRights from proposal
        Collection<WtWaterRights> waterRightsCollection = proposal.getWtWaterRightsCollection();
        if(waterRightsCollection != null){
          for(WtWaterRights waterRights:waterRightsCollection){
            waterRights.setWtTrans(null);
            wwrf.remove(waterRights);
          }
          proposal.setWtWaterRightsCollection(null);
        }

        // Proposal have Crop Idling Transfer Type
        WtCropIdling cropIdling = proposal.getWtCropIdling();
        if (null != cropIdling){
          deleteCropIdling(proposal);
        }

        // Proposal have Groundwater Transfer Type
        WtGroundwater groundwater = proposal.getWtGroundwater();
        if (null != groundwater){
          deleteGroundwater(proposal);
        }
        
        // Proposal have Reservoir Transfer Type
        WtReservoir reservoir = proposal.getWtReservoir();
        if (null != reservoir){
          deleteReservoir(proposal);
        }        

        // Propsal after detached have to be refresh again
//        wtf.getEntityManager().refresh(proposal);
        wtf.remove(proposal);  
        
        //Remove CropIdling rest Collection
        if (null != cropIdling){
          deleteCropIdlingPost(cropIdling);
        }
        
        //Remove Groundwater rest Collection
        if (null != groundwater){
          deleteGroundwaterPost(groundwater);
        }
        
        //Remove Reservoir rest Collection
        if (null != reservoir){
          deleteReservoirPost(reservoir);
        }

        // Remove Track Tables
        List<WtStatusTrack> statusList = wstf.select("SELECT WT.* FROM WT_STATUS_TRACK WT WHERE WT_TRANS_ID="+transId, com.gei.entities.WtStatusTrack.class);
        for (WtStatusTrack statusTrack:statusList){
          if (statusTrack != null){
            wstf.remove(statusTrack);
          }
        }

        jsonResponse.put("success",true);
      } catch (Exception ex) {
        jsonResponse.put("error", ex.getMessage()).put("success", false);
         if (ex instanceof MyException) {
          jsonResponse.put("callback", ((MyException) ex).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Proposal Search">
//    @RequestMapping("/search")
//    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        LoggedInCheckNoAjax(request,response);
//        ModelAndView mv = new ModelAndView("proposal/SearchProposal");
////        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
//
////        List<WtAgency> agencyList = waf.findAll();
////        List<WtAgency> agencyList = waf.select("SELECT * FROM WT_AGENCY ORDER BY AGENCY_FULL_NAME", com.gei.entities.WtAgency.class);
//        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
//
//        mv.addObject("agencyList", agencyList);
//        return mv;
//    }

    @RequestMapping("/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws IOException{
      LoggedInCheckNoAjax(request,response);
      ModelAndView mv = new ModelAndView("proposal/SearchIndex");
      List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
      List<WtCounty> countyList = LookupDataContext.getInstance().getCountyLookup();

      mv.addObject("agencyList", agencyList);
      mv.addObject("countyList", countyList);
      return mv;
    }

    @RequestMapping("/searchResult")
    public void searchResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request,response);
      JSONObject jsonResponse = new JSONObject();
      try {
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        JSONObject jsonData = new JSONObject(requestUtil.getString("data"));
        String query = "SELECT t1.* FROM WT_SEARCH_LIST_VIEW t1";
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
        if(!jsonData.optString("swpaoContractNum").isEmpty()){
          conditions += and + " SWPAO_CONTRACT_NUM = '" + jsonData.optString("swpaoContractNum") + "'";
          and = " AND ";
        }
        if(!jsonData.optString("duration").isEmpty()){
          conditions += and + " IS_SHORT_LONG = " + jsonData.optInt("duration");
          and = " AND ";
        }
        // Type of Transfer Query
        if(!jsonData.optString("typeCropIdling").isEmpty()){
          transType += "'100','101','110','111'";
          comma = ",";
        }
        if(!jsonData.optString("typeGroundwater").isEmpty()){
          transType += comma + "'010','110','011','111'";
          comma = ",";
        }
        if(!jsonData.optString("typeReservoir").isEmpty()){
          transType += comma + "'001','011','101','111'";
          comma = ",";
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
        response.getWriter().write(jsonResponse.toString());
      }
    }

//    @RequestMapping("/searchResult")
//    public void searchResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        LoggedInCheck(request,response);
//        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
//        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
////        WtBuyerFacade wbf = (WtBuyerFacade)appContext.getBean(WtBuyerFacade.class.getSimpleName());
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//
//        WtTrans proposal = new WtTrans();
//        WtAgency sellerAgency = null;
//        WtAgency buyerAgency = null;
//        if(!requestUtil.getString("wtTransId").isEmpty()){
//            proposal.setWtTransId(requestUtil.getInt("wtTransId"));
//        }
//        if(!requestUtil.getString("transYear").isEmpty()){
//            proposal.setTransYear(requestUtil.getInt("transYear"));
//        }
//        if(!requestUtil.getString("sellerId").isEmpty()){
//            sellerAgency = waf.find(requestUtil.getInt("sellerId"));
//            Collection<WtAgency> sellerCollection = waf.findAll(sellerAgency);
//            proposal.setWtSellerCollection(sellerCollection);
//        }
//
//
////        List<WtTrans> wts = wtf.findAll(proposal);
////        List<WtTrans> wts = wtf.findAll();
//        List<WtTrans> wts = wtf.searchAll(proposal);
//
//        // Remove proposal with not match buyer
//        Iterator<WtTrans> transIt = wts.iterator();
//        if(!requestUtil.getString("buyerId").isEmpty()){
//            buyerAgency = waf.find(requestUtil.getInt("buyerId"));
//            while(transIt.hasNext()){
//                WtTrans trans = transIt.next();
//                Collection<WtBuyer> buyerCollection = trans.getWtBuyerCollection();
//                if(!buyerAgency.isBuyer(buyerCollection)){
//                    transIt.remove();
//                }
//            }
//        }
//
//        JSONObject jsonResponse = new JSONObject(),tmpJson;
//        jsonResponse.put("data",new JSONArray());
//        WtBuyer buyer;
//        WtAgency seller;
//        String buyerNames,sellerNames,key,comma="";
//        Iterator<WtBuyer> buyersIt;
//        Iterator<WtAgency> sellerIt;
//        for (WtTrans wt : wts){
//            tmpJson = new JSONObject(wt.toMap());
//            tmpJson.put("buyers","");
//            tmpJson.put("seller","");
//            if (wt.getWtBuyerCollection()!=null){
//            buyersIt = wt.getWtBuyerCollection().iterator();
//            if (buyersIt != null){
//                comma="";
//                buyerNames="";
//                key="buyers";
//                while (buyersIt.hasNext()){
//                    buyer = buyersIt.next();
//                    // Use Agency Code
//                    buyerNames += tmpJson.optString(key) + comma + buyer.getWtAgency().getAgencyCode();
//                    comma=",";
//                    }//end while
//                    tmpJson.put(key, buyerNames);
//                }//end if
//            }//end if
//            if (wt.getWtSellerCollection()!=null){
//                sellerIt = wt.getWtSellerCollection().iterator();
//                if (sellerIt != null){
//                    comma="";
//                    sellerNames="";
//                    key="seller";
//                    while (sellerIt.hasNext()){
//                    seller = sellerIt.next();
//                    // Use Agency Code
//                    sellerNames += tmpJson.optString(key) + comma + seller.getAgencyCode();
//                    comma=",";
//                    }
//                    tmpJson.put(key,sellerNames);
//                }
//            }//end if
//            if (wt.getWtStatusFlag()!=null){
//                tmpJson.put("status", wt.getWtStatusFlag().getStatusName());
//            }
//            if (wt.getCreateDate() == null){
//                tmpJson.put("createDate", "");
//            } else {
//             tmpJson.put("createDate", df.format(wt.getCreateDate()));
//            }
//            jsonResponse.optJSONArray("data").put(tmpJson);
//        }//end for
//        response.getWriter().write(jsonResponse.toString());
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Agency and Contact Part">
    public ModelAndView getNewAgencyForm(HttpServletRequest request, HttpServletResponse response)
    {
      // Remove LoggedInCheck here since user can add Agency even do not get account yet
//      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("templates/addAgency");
      return mv;
    }


    /**
     * Adds a new agency based on the agency full name. First checks whether
     * the agency ({@linkplain WtAgency}) already exists by comparing
     * by name with current agency lists.
     * <p>
     * If the agency does not already exist, then a new agency is created and
     * persisted into the database, and the response is set with the json
     * serialization of the agency object.  The new agency will have an
     * agency code created based on the initials in the agency full name.
     * </p>
     * <p>
     * Otherwise, if the agency does already
     * exists, then no action is taken and the response is left empty.
     * </p>
     * @param request agencyFullName:String
     * @param response json-serialized {@linkplain WtAgency}
     * @throws IOException
     */
    @RequestMapping("/addNewAgency/")
    public void addNewAgency(HttpServletRequest request, HttpServletResponse response)throws IOException{
      WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      String agencyFullName = requestUtil.getString("agencyFullName");
      if (agencyFullName != null && !agencyFullName.isEmpty()) {
        Boolean exist = false;
        Collection<WtAgency> agencyCollection = waf.findAll();
        for(WtAgency agency:agencyCollection){
          if (agencyFullName.equalsIgnoreCase(agency.getAgencyFullName())){
            exist = true;
            break;
          }
        }
        if(!exist){
          WtAgency agency = new WtAgency();
          String agencyCode = "";
          String[] agencyNameArray = agencyFullName.split("\\s+");
          for(String agencyName:agencyNameArray){
            agencyCode += agencyName.substring(0,1);
          }
          agency.setAgencyCode(agencyCode);
          agency.setAgencyFullName(agencyFullName);
          agency.setAgencyActiveInd(1);
          waf.create(agency);

          JSONObject jsonData = new JSONObject(agency.toMap());
          response.getWriter().write(jsonData.toString());
        }
      }
    }


    /**
     * Edit the agency type.
     * @param request wtAgencyId: Integer, agencyType:String
     * @param response success:Boolean
     * @throws IOException
     */
    @RequestMapping("/editAgency/")
    public void editAgency(HttpServletRequest request, HttpServletResponse response) throws IOException{
       JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        WtAgency agency = waf.find(requestUtil.getInt("wtAgencyId"));
        agency.setAgencyType(requestUtil.getString("agencyType"));
        waf.edit(agency);
        jsonResponse.put("success",true);
      }
      catch(Exception ex){
        jsonResponse.put("success", false).put("error", ex.getMessage());
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping("/removeAgency/")
    public void removeAgency(HttpServletRequest request, HttpServletResponse response) //test
    {
        WtBuyerFacade bf = (WtBuyerFacade)appContext.getBean(WtBuyerFacade.class.getSimpleName());
        WtBuyer buy = bf.find(24);
        if(buy.getWtBuyerPK().getWtAgencyId()==2 && buy.getWtBuyerPK().getWtTransId() == 24)
        {
            bf.remove(buy);
        }
    }

    @RequestMapping("/contactListByAgency")
    public ModelAndView contactListByAgency(@RequestParam("wtTransId") Integer wtTransId
                                            ,@RequestParam(value="wtAgencyId",required=false) Integer wtAgencyId
                                            ,@RequestParam(value="statusName",required=false) String statusName
                                            ,HttpServletRequest request, HttpServletResponse response) throws IOException{
      ModelAndView mv = null;
      try {
        LoggedInCheck(request,response);
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        List<Map> contacts = new ArrayList<>();
        String query = String.format("SELECT * FROM CONTACT_LIST_VIEW WHERE IS_ACTIVE = 1 AND WT_AGENCY_ID = ?");
        GenericFacade.executeQuery(conn, query, Arrays.asList(wtAgencyId), new QueryDelegate(contacts) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<Map> contacts = this.getListener();
            Map contact = null;
            ResultSetMetaData rsmd = rs.getMetaData();
            String colName = null;
            Class colType = null;
            while (rs.next()) {
              contact = new LinkedHashMap();
              for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                colName = rsmd.getColumnName(i);
                colType = Class.forName(rsmd.getColumnClassName(i));
                contact.put(this.toCamelCase(colName), rs.getObject(colName, colType));
              }
              contacts.add(contact);
            }
          }
        });

        WtAgency agency = LookupDataContext.getInstance().getAgencyLookup(true).getAgency(wtAgencyId);
        if (agency == null) {
          throw new Exception("Cannot find the agency with agency id " + wtAgencyId);
        }

        mv = new ModelAndView("templates/agencyContactTable");
        mv.addObject("contacts", contacts);
        mv.addObject("wtAgencyId",wtAgencyId);
        mv.addObject("statusName",statusName);
        mv.addObject("agencyType",agency.getAgencyType());
      }
      catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }

    @RequestMapping("/removeContact")
    public void removeContact(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtContactFacade wcf = WebUtil.getFacade(WtContactFacade.class);
        WtAgencyFacade waf = WebUtil.getFacade(WtAgencyFacade.class);
      }
      catch(Exception ex){
        jsonResponse.put("success", false).put("error", ex.getMessage());
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/saveContact")
    public ModelAndView saveContact(
      @RequestParam("wtAgencyId") Integer wtAgencyId
      ,@RequestParam(value="wtContactId",required=false) Integer wtContactId
      ,@RequestParam(value="wtStateId",required=false) Integer wtStateId
      ,@RequestParam(value="wtCityId",required=false) Integer wtCityId
      ,@RequestParam(value="address1",required=false) String address1
      ,@RequestParam(value="cityName",required=false) String cityName
      ,@RequestParam(value="email",required=false) String email
      ,@RequestParam(value="firstName",required=false) String firstName
      ,@RequestParam(value="lastName",required=false) String lastName
      ,@RequestParam(value="middleName",required=false) String middleName
      ,@RequestParam(value="phoneNumber",required=false) String phoneNumber
      ,@RequestParam(value="phoneType",required=false) String phoneType
      ,@RequestParam(value="title",required=false) String title
      ,@RequestParam(value="zipcode",required=false) String zipcode
      ,@RequestParam(value="isActive",required=false) Integer isActive
      ,HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
      ModelAndView mv = null;
      try {
        LoggedInCheck(request,response);

        WtContactFacade wcf = WebUtil.getFacade(WtContactFacade.class);
        AppUser user = UserContext.getInstance().getUser();

        // Create New Contact or Update exist Contact
        WtContact contact = null;

        if ((wtContactId == null) || (contact = wcf.find(new WtContact(wtContactId))) == null) {
          contact = new WtContact();
        }

        String agencyType = null;

        if (wtAgencyId != null) {
          WtAgencyFacade waf = WebUtil.getFacade(WtAgencyFacade.class);
          WtAgency contactAgency = null;

          if ((contactAgency = waf.find(wtAgencyId)) != null) {
            contact.setWtAgency(contactAgency);

            agencyType = contactAgency.getAgencyType();
          }
        }

        if (wtStateId != null) {
          WtStateFacade wsf = WebUtil.getFacade(WtStateFacade.class);
          WtState state = null;
          if ((state = wsf.find(wtStateId)) != null) {
            contact.setWtState(state);
          }
        }

        contact.setTitle(title);
        contact.setFirstName(firstName);
        contact.setMiddleName(middleName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhoneType(phoneType);
        contact.setPhoneNumber(phoneNumber);
        contact.setAddress1(address1);
        contact.setCityName(cityName);
        contact.setZipcode(zipcode);
        if (isActive != null) {
          contact.setIsActive(isActive);
        }
        contact.setUpdatedById(user.getUserId());

        // Create New Contact or Update exist Contact
        if(wtContactId == null){
          contact.setCreatedById(user.getUserId());
          wcf.create(contact);
        } else {
            wcf.edit(contact);
        }

        List<Map> contacts = wcf.select("SELECT * FROM CONTACT_LIST_VIEW WHERE IS_ACTIVE = 1 AND WT_AGENCY_ID = " + wtAgencyId);

        mv = new ModelAndView("templates/agencyContactTable");
        mv.addObject("contacts", contacts);
        mv.addObject("wtAgencyId",wtAgencyId);
        mv.addObject("agencyType",agencyType);
      }
      catch(Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }

    public ModelAndView saveBuyersContact(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("templates/buyersContactTable");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        WtStateFacade wsf = (WtStateFacade)appContext.getBean(WtStateFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        WtState state = wsf.find(requestUtil.getInt("wtStateId"));

        // Create New Contact or Update exist Contact
        String contactId = requestUtil.getString("wtContactId");
        WtContact contact = new WtContact();
        if(!StringUtils.isEmpty(contactId)){
            contact = wcf.find(requestUtil.getInt("wtContactId"));
        }

        contact.setTitle(requestUtil.getString("title"));
        contact.setFirstName(requestUtil.getString("firstName"));
        contact.setMiddleName(requestUtil.getString("middleName"));
        contact.setLastName(requestUtil.getString("lastName"));
        contact.setEmail(requestUtil.getString("email"));
        contact.setPhoneType(requestUtil.getString("phoneType"));
        contact.setPhoneNumber(requestUtil.getString("phoneNumber"));
        contact.setAddress1(requestUtil.getString("address1"));
        contact.setCityName(requestUtil.getString("cityName"));
        contact.setWtState(state);
        contact.setZipcode(requestUtil.getString("zipcode"));
        contact.setUpdatedById(user.getUserId());
        contact.setIsBuyersContact(1);
        wcf.edit(contact);

        List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup(true);
        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup(); //wsf.findAll();
        mv.addObject("buyersContactList", buyersContactList);
        mv.addObject("stateList", stateList);
        mv.addObject("buyersContact", contact);
        return mv;
    }

  @RequestMapping("/editContact")
  public ModelAndView editContact(
    @RequestParam(value = "wtContactId", required = false) Integer wtContactId
    , @RequestParam("wtTransId") Integer wtTransId
    , @RequestParam("wtAgencyId") Integer wtAgencyId
    , HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = null;
    try {
      LoggedInCheck(request, response);
      mv = new ModelAndView("templates/agencyContactForm");

      if (wtContactId != null) {
        WtContactFacade wcf = WebUtil.getFacade(WtContactFacade.class);// appContext.getBean(WtContactFacade.class.getSimpleName());
        WtContact contact = wtContactId == null ? null : wcf.find(wtContactId);
        mv.addObject("contact", contact);
      }
      mv.addObject("wtContactId", wtContactId);
      mv.addObject("wtTransId", wtTransId);
    } catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    return mv;
  }

    public void contactAccess(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        WtContact contact = wcf.find(requestUtil.getInt("wtContactId"));

        // Link or unlink Contact to Trans
        WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
        AppUser appUser = contact.getUser();
        String linkTrans = requestUtil.getString("linkTrans");

        if(appUser != null && transRec != null && !linkTrans.isEmpty()){
          int userId = appUser.getUserId();
          if ("Y".equalsIgnoreCase(linkTrans)){
            linkTransUser(transRec,userId);
          } else if ("N".equalsIgnoreCase(linkTrans)){
            unlinkTransUser(transRec,userId);
          }
        }
        jsonResponse.put("success",true);
      }
      catch(Exception ex){
        jsonResponse.put("success", false).put("error", ex.getMessage());
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
    public void contactAccess(JSONObject obj) throws IOException, SQLException{
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        WtContact contact = wcf.find(obj.optInt("wtContactId"));

        // Link or unlink Contact to Trans
        WtTrans transRec = wtf.find(obj.optInt("wtTransId"));
        AppUser appUser = contact.getUser();
        String linkTrans = obj.optString("linkTrans");

        if(appUser != null && transRec != null && !linkTrans.isEmpty()){
          int userId = appUser.getUserId();
          if ("Y".equalsIgnoreCase(linkTrans)){
            linkTransUser(transRec,userId);
          } else if ("N".equalsIgnoreCase(linkTrans)){
            unlinkTransUser(transRec,userId);
          }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="New Proposal Page">
    @RequestMapping(value="/new")
    public ModelAndView newProposal(HttpServletRequest request, HttpServletResponse response) throws IOException {
      ModelAndView mv = null;
      try {
        if (!UserContext.getInstance().isLoggedIn()) {
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();
//        List<WtCity> cityList = LookupDataContext.getInstance().getCityLookup();
//        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup();
        List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup();

        mv = new ModelAndView("proposal/edit");
        mv.addObject("agencies", agencyList);
//        mv.addObject("cityList", cityList);
//        mv.addObject("stateList", stateList);
        mv.addObject("buyersContactList", buyersContactList);
        mv.addObject("buttonCheck",true);
      }
      catch(Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Edit Proposal Page">
    @RequestMapping("/edit/{transId}")
    public ModelAndView editPage(
      @PathVariable("transId") Integer transId
      , HttpServletRequest request
      , HttpServletResponse response) throws Exception {
      ModelAndView mv = null;
      try {
        if (!UserContext.getInstance().isLoggedIn()) {
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        final AppUser user = UserContext.getInstance().getUser();//(AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        final Connection conn = ConnectionContext.getConnection("WtDataSource");
        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup();

        //<editor-fold defaultstate="collapsed" desc="Querying for a Status Flag Lookup">
        final HashMap<Integer,WtStatusFlag> statusMap = new HashMap();
        String query = String.format("SELECT %1$s.* FROM %1$s"
                ,EntityUtil.getTableName(WtStatusFlag.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(statusMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap statusFlags = this.getListener();
            WtStatusFlag statuFlag = null;
            while (rs.next()) {
              statuFlag = new WtStatusFlag();
              statuFlag.Load(rs);

              statusFlags.put(statuFlag.getWtStatusFlagId(),statuFlag);
            }
          }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for the Water Transfer Proposal with validation">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtTrans.class));

        WtTrans proposal = new WtTrans();
        GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(proposal) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            if (!rs.next()) {
              throw new Exception("The water transfer id is invalid!");
            }

            WtTrans proposal = this.getListener();
            proposal.Load(rs);

            // Load Status Flag Project
            Integer statusId = null;
            WtStatusFlag statuFlag = new WtStatusFlag();
            if (((statusId = rs.getInt("WT_STATUS_FLAG_ID")) != null)
                && ((statuFlag = statusMap.get(statusId)) != null)){
                proposal.setWtStatusFlag(statuFlag);
            }

            String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? AND %1$s.USER_ID = ?"
                              ,"WT_TRANS_USER");

            GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId(), user.getUserId()), new QueryDelegate(proposal) {
              @Override
              public void handle(ResultSet rs) throws Exception {
                if (user.isAppAccount() && !rs.next()) {
                  throw new Exception("You are not authorized to view the water transfer proposal!");
                }
              }
            });
          }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for a Agency Lookup">
        final AgencyCollection agencies = new AgencyCollection();
        query = String.format("SELECT %1$s.* FROM %1$s"
                ,EntityUtil.getTableName(WtAgency.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencies) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            AgencyCollection agencies = this.getListener();
            WtAgency agency = null;
            while (rs.next()) {
              agency = new WtAgency();
              agency.Load(rs);

              agencies.add(agency);
            }
          }
        });

        final HashMap<Integer,WtAgency> agencyMap = new HashMap();
        query = String.format("SELECT %1$s.* FROM %1$s"
                ,EntityUtil.getTableName(WtAgency.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencyMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap agencies = this.getListener();
            WtAgency agency = null;
            while (rs.next()) {
              agency = new WtAgency();
              agency.Load(rs);

              agencies.put(agency.getWtAgencyId(),agency);
            }
          }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for a Seller">
//
//        AgencyCollection sellers = new AgencyCollection();
//        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?","WT_SELLER");
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(sellers) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            AgencyCollection sellers = this.getListener();
//            WtAgency seller = null;
//            Integer agencyId = null;
//            if (rs.next()
//               && ((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
//              && ((seller = agencies.get(agencyId)) != null)) {
//              sellers.add(seller);
//            }
//          }
//        });

        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_AGENCY_ID = %1$s.WT_AGENCY_ID WHERE %2$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtAgency.class)
                ,"WT_SELLER");
        List<WtAgency> wtsellers = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(wtsellers) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtAgency> sellers = this.getListener();
              WtAgency p = null;
              while (rs.next()) {
                p = new WtAgency();
                p.Load(rs);
                sellers.add(p);
              }
            }
          }
        );

        WtAgency seller = null;

        if (!wtsellers.isEmpty()) {
          // it's always 1 record if seller exist
          seller = wtsellers.get(0);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Buyers">
        BuyerCollection buyers = new BuyerCollection();
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.WT_AGENCY_ID"
                  ,EntityUtil.getTableName(WtBuyer.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(buyers) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            BuyerCollection buyers = this.getListener();
            Integer agencyId = null;
            WtBuyer buyer = null;
            WtAgency agency = null;
            while (rs.next()) {
              buyer = new WtBuyer();
              buyer.setSharePercent(rs.getDouble("SHARE_PERCENT"));

              if (((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
                && ((agency = agencyMap.get(agencyId)) != null)){
                buyer.setWtAgency(agency);
              }

              buyers.add(buyer);
            }
          }
        });
//        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_AGENCY_ID = %1$s.WT_AGENCY_ID WHERE %2$s.WT_TRANS_ID = ?"
//                ,EntityUtil.getTableName(WtBuyer.class)
//                ,"WT_BUYER");
//        List<WtBuyer> wtbuyers = new ArrayList<>();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
//          , new QueryDelegate(wtbuyers) {
//            @Override
//            public void handle(ResultSet rs) throws Exception {
//              List<WtBuyer> buyers = this.getListener();
//              WtBuyer p = null;
//              while (rs.next()) {
//                p = new WtBuyer();
//                p.Load(rs);
//                buyers.add(p);
//              }
//            }
//          }
//        );

        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for buyer contact">
//        ContactCollection buyerContactes = new ContactCollection();
//        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.IS_BUYERS_CONTACT = 1 ORDER BY LAST_NAME"
//                  ,EntityUtil.getTableName(WtContact.class));
//        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(buyerContacts) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            ContactCollection contacts = this.getListener();
//            Integer agencyId = null;
//            WtContact contact = null;
//            WtAgency agency = null;
//            while (rs.next()) {
//              contact = new WtContact();
//              contact.Load(rs);
//              if (((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
//                && ((agency = agencies.get(agencyId)) != null)){
//                contact.setWtAgency(agency);
//              }
//
//              contacts.add(contact);
//            }
//          }
//        });

        List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup();

        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_CONTACT_ID = %1$s.WT_CONTACT_ID"
                              + " WHERE %2$s.WT_TRANS_ID = ? AND %1$s.IS_BUYERS_CONTACT = 1 ORDER BY LAST_NAME"
                ,EntityUtil.getTableName(WtContact.class)
                ,"WT_BUYERS_CONTACT");
        List<WtContact> buyerContacts = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(buyerContacts) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtContact> contacts = this.getListener();
              WtContact p = null;
              while (rs.next()) {
                p = new WtContact();
                p.Load(rs);
                contacts.add(p);
              }
            }
          }
        );

        WtContact buyersContact = null;

        if (!buyerContacts.isEmpty()) {
          // it's always 1 record if buyers contact exist
          buyersContact = buyerContacts.get(0);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Counties">
//        final CountyCollection counties = new CountyCollection();
//        query = String.format("SELECT %1$s.* FROM %1$s"
//                ,EntityUtil.getTableName(WtCounty.class));
//        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(counties) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            CountyCollection counties = this.getListener();
//            WtCounty county = null;
//            while (rs.next()) {
//              county = new WtCounty();
//              county.Load(rs);
//              counties.add(county);
//            }
//          }
//        });
//
//        CountyCollection wtcounties = new CountyCollection();
//        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.WT_TRANS_ID","WT_TRANS_COUNTY");
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
//          , new QueryDelegate(wtcounties) {
//            @Override
//            public void handle(ResultSet rs) throws Exception {
//              CountyCollection wtcounties = this.getListener();
//              WtCounty county = null;
//              Integer countyId = null;
//
//              while(rs.next()
//                    && ((countyId = rs.getInt("WT_COUNTY_ID")) != null)
//                    && ((county = counties.get(countyId)) != null)) {
//                wtcounties.add(county);
//              }
//            }
//          }
//        );
        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_COUNTY_ID = %1$s.WT_COUNTY_ID WHERE %2$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtCounty.class)
                ,"WT_TRANS_COUNTY");
        List<WtCounty> wtcounties = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(wtcounties) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtCounty> counties = this.getListener();
              WtCounty p = null;
              while (rs.next()) {
                p = new WtCounty();
                p.Load(rs);
                counties.add(p);
              }
            }
          }
        );

        proposal.setWtCountyCollection(wtcounties);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Status Tracks">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.STATUS_DATE"
        ,EntityUtil.getTableName(WtStatusTrack.class));
        List<WtStatusTrack> statusTracks = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(statusTracks) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtStatusTrack> statusTracks = this.getListener();
              WtStatusTrack st = null;

              while(rs.next()) {
                st = new WtStatusTrack();
                st.Load(rs);
                statusTracks.add(st);
              }
            }
          }
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Reviewer Comments">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.CREATED_DATE"
        ,EntityUtil.getTableName(WtReviewerComments.class));
        List<WtReviewerComments> rComments = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(rComments) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtReviewerComments> rComments = this.getListener();
              WtReviewerComments comment = null;

              while(rs.next()) {
                comment = new WtReviewerComments();
                comment.Load(rs);
                rComments.add(comment);
              }
            }
          }
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Crop Idling">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtCropIdling.class));
        WtCropIdling cropIdling = new WtCropIdling();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                    , new QueryDelegate(cropIdling) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              WtCropIdling cropIdling = this.getListener();
              if (rs.next()) {
                cropIdling.Load(rs);
              }
            }
          }
        );
        proposal.setWtCropIdling(cropIdling);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Monthly CI water transfer">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_CROP_IDLING_ID = ?"
                ,EntityUtil.getTableName(WtCiMonthly.class));
        WtCiMonthly ciMonthly = new WtCiMonthly();
        GenericFacade.executeQuery(conn, query, Arrays.asList(cropIdling.getWtCropIdlingId())
                                  , new QueryDelegate(ciMonthly) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              WtCiMonthly ciMonthly = this.getListener();
              while (rs.next()) {
                ciMonthly.Load(rs);
              }
            }
          }
        );

        cropIdling.setWtCiMonthly(ciMonthly);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for CI Crop Type">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_CROP_IDLING_ID = ?"
                ,EntityUtil.getTableName(WtCiCroptype.class));
        List<WtCiCroptype> croptypeList = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(cropIdling.getWtCropIdlingId())
                                  , new QueryDelegate(croptypeList) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtCiCroptype> croptypes = this.getListener();
              WtCiCroptype m = null;
              while (rs.next()) {
                m = new WtCiCroptype();
                m.Load(rs);
                croptypes.add(m);
              }
            }
          }
        );

        cropIdling.setCroptypeCollection(croptypeList);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Reservoir">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtReservoir.class));
        WtReservoir reservoir = new WtReservoir();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(reservoir) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              WtReservoir reservoir = this.getListener();
              if (rs.next()) {
                reservoir.Load(rs);
              }
            }
          }
        );
        proposal.setWtReservoir(reservoir);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Purpose of Reservoirs">
        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_PURPOSE_ID = %1$s.WT_PURPOSE_ID WHERE %2$s.WT_RESERVOIR_ID = ?"
                ,EntityUtil.getTableName(WtPurpose.class)
                ,"WT_RV_PURPOSE");
        List<WtPurpose> wtPurposes = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(reservoir.getWtReservoirId())
          , new QueryDelegate(wtPurposes) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtPurpose> purposes = this.getListener();
              WtPurpose p = null;
              while (rs.next()) {
                p = new WtPurpose();
                p.Load(rs);
                purposes.add(p);
              }
            }
          }
        );
        reservoir.setWtPurposeCollection(wtPurposes);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Groundwater">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtGroundwater.class));
        WtGroundwater groundwater = new WtGroundwater();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(groundwater) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              WtGroundwater gw = this.getListener();
              if (rs.next()) {
                gw.Load(rs);
              }
            }
          }
        );
        proposal.setWtGroundwater(groundwater);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Monthly Groundwater Pumping">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_GROUNDWATER_ID = ?"
                ,EntityUtil.getTableName(WtGwMonthly.class));
        List<WtGwMonthly> monthlyList = new ArrayList<>();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(groundwater.getWtGroundwaterId()), new EntityLoadDelegate(monthlyList));
        GenericFacade.executeQuery(conn, query, Arrays.asList(groundwater.getWtGroundwaterId())
                                  , new QueryDelegate(monthlyList) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtGwMonthly> monthes = this.getListener();
              WtGwMonthly m = null;
              while (rs.next()) {
                m = new WtGwMonthly();
                m.Load(rs);
                monthes.add(m);
              }
            }
          }
        );

        groundwater.setWtGwMonthlyCollection(monthlyList);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Groundwater">
//        query = String.format("SELECT %1$s.* "
//                + ",%2$s.WT_GW_MONTHLY_ID AS T2_WT_GW_MONTHLY_ID\n" +
//                ",%2$s.WT_GROUNDWATER_ID AS T2_WT_GROUNDWATER_ID\n" +
//                ",%2$s.GW_MONTH AS T2_GW_MONTH\n" +
//                ",%2$s.PROPOSED_PUMPING AS T2_PROPOSED_PUMPING\n" +
//                ",%2$s.BASELINE_PUMPING AS T2_BASELINE_PUMPING\n" +
//                ",%2$s.STREAM_DEPLETION AS T2_STREAM_DEPLETION\n" +
//                ",%2$s.NET_TRANS_WATER AS T2_NET_TRANS_WATER\n" +
//                ",%2$s.GROSS_TRANS_PUMPING AS T2_GROSS_TRANS_PUMPING\n" +
//                ",%2$s.MEASUREMENT_DATE AS T2_MEASUREMENT_DATE"
//                + "\nFROM %1$s "
//                + "\nLEFT JOIN %2$s ON %1$s.WT_GROUNDWATER_ID = %2$s.WT_GROUNDWATER_ID"
//                + "\nWHERE %1$s.WT_TRANS_ID = ?"
//          ,EntityUtil.getTableName(WtGroundwater.class)
//          ,EntityUtil.getTableName(WtGwMonthly.class)
//        );
//        WtGroundwater gw = new WtGroundwater();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
//          , new QueryDelegate(gw) {
//            @Override
//            public void handle(ResultSet rs) throws Exception {
//              WtGroundwater gw = this.getListener();
//              WtGwMonthly gm = null;
//              boolean gwLoaded = false;
//              while (rs.next()) {
//                if (!gwLoaded) {
//                gw.Load(rs);
//                  gw.setWtGwMonthlyCollection(new ArrayList<WtGwMonthly>());
//              }
//                gm = new WtGwMonthly();
//                gm.Load(rs, "T2_");
//                gw.getWtGwMonthlyCollection().add(gm);
//            }
//          }
//          }
//        );
//        proposal.setWtGroundwater(gw);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Target Storage">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_RESERVOIR_ID = ?"
                ,EntityUtil.getTableName(WtRvTarstor.class));
        List<WtRvTarstor> targetStorages = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(reservoir.getWtReservoirId())
          , new QueryDelegate(targetStorages) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtRvTarstor> storage = this.getListener();
              WtRvTarstor p = null;
              while (rs.next()) {
                p = new WtRvTarstor();
                p.Load(rs);
                storage.add(p);
              }
            }
          }
        );
        reservoir.setWtRvTarstorCollection(targetStorages);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Agency Approval">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtAgencyApproval.class));
        WtAgencyApproval aa = new WtAgencyApproval();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(aa));
        proposal.setWtAgencyApproval(aa);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for CEQA">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtTransCeqa.class));
        WtTransCeqa ceqa = new WtTransCeqa();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(ceqa));
        if (ceqa.getWtTransCeqaId() != null){
          proposal.setWtTransCeqa(ceqa);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for NEPA">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtTransNepa.class));
        WtTransNepa nepa = new WtTransNepa();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(nepa));
        if (nepa.getWtTransNepaId() != null){
          proposal.setWtTransNepa(nepa);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for SWRCB">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtTransSwrcb.class));
        WtTransSwrcb swrcb = new WtTransSwrcb();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(swrcb));
        if (swrcb.getWtTransSwrcbId() != null){
          proposal.setWtTransSwrcb(swrcb);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for REACH">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtTransReach.class));
        WtTransReach reach = new WtTransReach();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(reach));
        proposal.setWtTransReach(reach);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Report Attachments">
        query = String.format("SELECT %1$s.ATTACHMENT_TYPE\n" +
                                    ",%1$s.CEQA_SUBMITTED_DATE\n" +
                                    ",%1$s.CREATED_BY_ID\n" +
                                    ",%1$s.CREATED_DATE\n" +
                                    ",%1$s.DESCRIPTION\n" +
                                    ",%1$s.FILENAME\n" +
                                    ",%1$s.FILE_SIZE\n" +
                                    ",%1$s.IS_ACTIVE\n" +
                                    ",%1$s.MIME_TYPE\n" +
                                    ",%1$s.MODIFIED_DATE\n" +
                                    ",%1$s.TITLE\n" +
                                    ",%1$s.UPDATED_BY_ID\n" +
                                    ",%1$s.WT_ATTACHMENT_ID "
                                    + "\nFROM %1$s JOIN %2$s "
                                    + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID "
                                    + "\nWHERE %2$s.WT_TRANS_ID = ?"
          ,EntityUtil.getTableName(WtAttachment.class)
          ,"WT_TRANS_REPORT"
        );
        List<WtAttachment> reports = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityCollectionLoadDelegate(reports,WtAttachment.class));
        proposal.setWtReportCollection(reports);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Water Rights">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtWaterRights.class));
        List<WtWaterRights> waterRights = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
          , new QueryDelegate(waterRights) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtWaterRights> rights = this.getListener();
              WtWaterRights p = null;
              while (rs.next()) {
                p = new WtWaterRights();
                p.Load(rs);
                rights.add(p);
              }
            }
          }
        );
        proposal.setWtWaterRightsCollection(waterRights);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Water Loss">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
        ,EntityUtil.getTableName(WtWaterLoss.class));
        WtWaterLoss waterLoss = new WtWaterLoss();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityLoadDelegate(waterLoss));
        proposal.setWtWaterLoss(waterLoss);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for Fu Type Lookup">
        final HashMap<Integer,WtFuType> fuTypeMap = new HashMap();
        query = String.format("SELECT %1$s.* FROM %1$s"
                ,EntityUtil.getTableName(WtFuType.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(fuTypeMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap fuTypes = this.getListener();
            WtFuType fuType = null;
            while (rs.next()) {
              fuType = new WtFuType();
              fuType.Load(rs);

              fuTypes.put(fuType.getWtFuTypeId(),fuType);
            }
          }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Trans Types">
//        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
//        ,EntityUtil.getTableName(WtTransType.class));
//        List<WtTransType> trantypes = new ArrayList<>();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new EntityCollectionLoadDelegate(trantypes, WtTransType.class));
//        proposal.setWtTransTypeCollection(trantypes);

        TransTypeCollection transTypes = new TransTypeCollection();
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                  ,EntityUtil.getTableName(WtTransType.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(transTypes) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            TransTypeCollection types = this.getListener();
            Integer fuTypeId = null;
            WtTransType transType = null;
            WtFuType fuType = null;
            while (rs.next()) {
              transType = new WtTransType();
              String volume = rs.getString("TYPE_VOLUME");
              if (volume != null){
                transType.setTypeVolume(rs.getDouble("TYPE_VOLUME"));
              }
              transType.setTypeDetail(rs.getString("TYPE_DETAIL"));

              if (((fuTypeId = rs.getInt("WT_FU_TYPE_ID")) != null)
                && ((fuType = fuTypeMap.get(fuTypeId)) != null)){
                transType.setWtFuType(fuType);
              }

              types.add(transType);
            }
          }
        });

        proposal.setWtTransTypeCollection(transTypes);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for Review Notes">
        final ReviewNoteCollection reviewNotes = new ReviewNoteCollection();
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                ,EntityUtil.getTableName(WtReviewNote.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(reviewNotes) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            ReviewNoteCollection reviewNotes = this.getListener();
            WtReviewNote note = null;
            while (rs.next()) {
              note = new WtReviewNote();
              note.Load(rs);

              reviewNotes.add(note);
            }
          }
        });
        //</editor-fold>

          mv = new ModelAndView("proposal/edit");
          mv.addObject("proposal", proposal);
          mv.addObject("seller", seller);
          mv.addObject("buyers", buyers);
          mv.addObject("buyersContactList", buyersContactList);
          mv.addObject("stateList", stateList);
          mv.addObject("buyersContact", buyersContact);
          mv.addObject("agencies", agencies);
          mv.addObject("statusTrackList", statusTracks);
          mv.addObject("reviewerComments", rComments);
          mv.addObject("LookupDataContext", LookupDataContext.getInstance());
          mv.addObject("reviewNotes", reviewNotes);
        }
        catch (Exception ex) {
          response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return mv;
      }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save As Proposal Page">
    @RequestMapping("/saveas/{transId}")
    public ModelAndView saveAs(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws Exception {
      ModelAndView mv = null;
      try {
        if (!UserContext.getInstance().isLoggedIn()) {
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        final AppUser user = UserContext.getInstance().getUser();
        final Connection conn = ConnectionContext.getConnection("WtDataSource");
        WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtReservoirFacade wrvf = (WtReservoirFacade) appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade) appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup();

        //<editor-fold defaultstate="collapsed" desc="Querying for a Status Flag Lookup">
        final HashMap<Integer, WtStatusFlag> statusMap = new HashMap();
        String query = String.format("SELECT %1$s.* FROM %1$s", EntityUtil.getTableName(WtStatusFlag.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(statusMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap statusFlags = this.getListener();
            WtStatusFlag statuFlag = null;
            while (rs.next()) {
              statuFlag = new WtStatusFlag();
              statuFlag.Load(rs);

              statusFlags.put(statuFlag.getWtStatusFlagId(), statuFlag);
            }
          }
        });
          //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Create A New Proposal">
        WtStatusFlag statusFlag = statusMap.get(0);
        WtTrans newProposal = new WtTrans();
        newProposal.setTransYear(year);
        newProposal.setCreatedById(user.getUserId());
        newProposal.setUpdatedById(user.getUserId());
        newProposal.setWtStatusFlag(statusFlag);
        newProposal.setIsActive(Status.ACTIVE);
        wtf.create(newProposal);
        final Integer newTransId = newProposal.getWtTransId();
        linkTransUser(newProposal, user.getUserId());
        setStatusTrack(newProposal, user);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for the Water Transfer Proposal with validation">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtTrans.class));

        WtTrans proposal = new WtTrans();
        GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(proposal) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            if (!rs.next()) {
              throw new Exception("The water transfer id is invalid!");
            }

            WtTrans proposal = this.getListener();
            proposal.Load(rs);

            // Load Status Flag Project
            Integer statusId = null;
            WtStatusFlag statuFlag = new WtStatusFlag();
            if (((statusId = rs.getInt("WT_STATUS_FLAG_ID")) != null)
                    && ((statuFlag = statusMap.get(statusId)) != null)) {
              proposal.setWtStatusFlag(statuFlag);
            }

            String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? AND %1$s.USER_ID = ?", "WT_TRANS_USER");

            GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId(), user.getUserId()), new QueryDelegate(proposal) {
              @Override
              public void handle(ResultSet rs) throws Exception {
                if (user.isAppAccount() && !rs.next()) {
                  throw new Exception("You are not authorized to view the water transfer proposal!");
                }
              }
            });
          }
        });
        // Copy Transfer Type to New Proposal
        newProposal.setProAcrIdleInd(proposal.getProAcrIdleInd());
        newProposal.setResReOpInd(proposal.getResReOpInd());
        newProposal.setWellUseNumInd(proposal.getWellUseNumInd());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for a Agency Lookup">
        final AgencyCollection agencies = new AgencyCollection();
        query = String.format("SELECT %1$s.* FROM %1$s"
                              ,EntityUtil.getTableName(WtAgency.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencies) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            AgencyCollection agencies = this.getListener();
            WtAgency agency = null;
            while (rs.next()) {
              agency = new WtAgency();
              agency.Load(rs);

              agencies.add(agency);
            }
          }
        });

        final HashMap<Integer,WtAgency> agencyMap = new HashMap();
        query = String.format("SELECT %1$s.* FROM %1$s"
                              ,EntityUtil.getTableName(WtAgency.class));
        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencyMap) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            HashMap agencies = this.getListener();
            WtAgency agency = null;
            while (rs.next()) {
              agency = new WtAgency();
              agency.Load(rs);

              agencies.put(agency.getWtAgencyId(),agency);
            }
          }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Querying for a Seller">
        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_AGENCY_ID = %1$s.WT_AGENCY_ID WHERE %2$s.WT_TRANS_ID = ?"
                              ,EntityUtil.getTableName(WtAgency.class),"WT_SELLER");
        List<WtAgency> wtsellers = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                  , new QueryDelegate(wtsellers) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtAgency> sellers = this.getListener();
              WtAgency p = null;
              while (rs.next()) {
                p = new WtAgency();
                p.Load(rs);
                sellers.add(p);
              }
            }
          }
        );
        WtAgency seller = null;
        if (!wtsellers.isEmpty()) {
          // it's always 1 record if seller exist
          seller = wtsellers.get(0);
          newProposal.setWtSellerCollection(wtsellers);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Buyers">
        BuyerCollection buyers = new BuyerCollection();
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.WT_AGENCY_ID"
                              ,EntityUtil.getTableName(WtBuyer.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(buyers) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            BuyerCollection buyers = this.getListener();
            Integer agencyId = null;
            WtBuyer buyer = null;
            WtAgency agency = null;
            WtBuyerPK bpk = null;
            while (rs.next()) {
              buyer = new WtBuyer();
              bpk =  new WtBuyerPK();
              bpk.setWtAgencyId(rs.getInt("WT_AGENCY_ID"));
              bpk.setWtTransId(newTransId);
              buyer.setWtBuyerPK(bpk);
              buyer.setSharePercent(rs.getDouble("SHARE_PERCENT"));

              if (((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
                  && ((agency = agencyMap.get(agencyId)) != null)){
                buyer.setWtAgency(agency);
              }

              buyers.add(buyer);
            }
          }
        });
        if (!buyers.isEmpty()) {
          newProposal.setWtBuyerCollection(buyers);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for buyer contact">
        List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup();

        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_CONTACT_ID = %1$s.WT_CONTACT_ID"
                              + " WHERE %2$s.WT_TRANS_ID = ? AND %1$s.IS_BUYERS_CONTACT = 1 ORDER BY LAST_NAME"
                              ,EntityUtil.getTableName(WtContact.class)
                              ,"WT_BUYERS_CONTACT");
        List<WtContact> buyerContacts = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                   , new QueryDelegate(buyerContacts) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtContact> contacts = this.getListener();
            WtContact p = null;
            while (rs.next()) {
              p = new WtContact();
              p.Load(rs);
              contacts.add(p);
            }
          }
        });

        WtContact buyersContact = new WtContact();

        if (!buyerContacts.isEmpty()) {
          // it's always 1 record if buyers contact exist
          buyersContact = buyerContacts.get(0);
          newProposal.setBuyersContactCollection(buyerContacts);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Crop Idling">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                              ,EntityUtil.getTableName(WtCropIdling.class));
        WtCropIdling cropIdling = new WtCropIdling();
        WtCropIdling newCropIdling = new WtCropIdling();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                   , new QueryDelegate(cropIdling) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            WtCropIdling cropIdling = this.getListener();
            if (rs.next()) {
              cropIdling.Load(rs);
            }
          }
        });
        if (cropIdling.getWtCropIdlingId() != null){
          wcif.create(newCropIdling);
          newCropIdling.setWtTrans(newProposal);
          newProposal.setWtCropIdling(newCropIdling);
        }
//        proposal.setWtCropIdling(cropIdling);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for CI Map Attachment">
        query = String.format("SELECT %1$s.WT_ATTACHMENT_ID\n" +
                                    ",%1$s.ATTACHMENT_TYPE\n" +
                                    ",%1$s.TITLE\n" +
                                    ",%1$s.DESCRIPTION\n" +
                                    ",%1$s.FILENAME\n" +
                                    ",%1$s.FILE_SIZE\n" +
                                    ",%1$s.IS_ACTIVE\n"
	                                  + "\nFROM %1$s JOIN %2$s "
	                                  + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID "
	                                  + "\nWHERE %2$s.WT_CROP_IDLING_ID = ?"
                                    ,EntityUtil.getTableName(WtAttachment.class)
                                    ,"WT_CI_MAP_ATTACHMENT");
        List<WtAttachment> mapAttachments = new ArrayList<>();
        if (cropIdling.getWtCropIdlingId() != null){
          GenericFacade.executeQuery(conn, query, Arrays.asList(cropIdling.getWtCropIdlingId()), new EntityCollectionLoadDelegate(mapAttachments,WtAttachment.class));
          linkAttachmentList(newCropIdling,mapAttachments);
//          newCropIdling.setWtAttachmentCollection(mapAttachments);
//          wcif.edit(newCropIdling);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Reservoir">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                              ,EntityUtil.getTableName(WtReservoir.class));
        WtReservoir reservoir = new WtReservoir();
        WtReservoir newReservoir = new WtReservoir();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                  , new QueryDelegate(reservoir) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            WtReservoir reservoir = this.getListener();
            if (rs.next()) {
              reservoir.Load(rs);
            }
          }
        });
        if (reservoir.getWtReservoirId() != null){
          wrvf.create(newReservoir);
          newReservoir.setWtTrans(newProposal);
          newProposal.setWtReservoir(newReservoir);
        }
//        proposal.setWtReservoir(reservoir);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for Groundwater">
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
                              ,EntityUtil.getTableName(WtGroundwater.class));
        WtGroundwater groundwater = new WtGroundwater();
        WtGroundwater newGroundwater = new WtGroundwater();
        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
                                  , new QueryDelegate(groundwater) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            WtGroundwater gw = this.getListener();
            if (rs.next()) {
              gw.Load(rs);
            }
          }
        });
        if (groundwater.getWtGroundwaterId() != null){
          wgwf.create(newGroundwater);
          newGroundwater.setWtTrans(newProposal);
          newProposal.setWtGroundwater(newGroundwater);
        }
//      proposal.setWtGroundwater(groundwater);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Query for AssociateWells">
        query = String.format("SELECT WELL.* FROM %1$s WELL\n" +
                                     "LEFT JOIN %2$s GL ON GL.WT_WELL_ID = well.WT_WELL_ID\n" +
                                     "LEFT JOIN %3$s GW ON GL.WT_GROUNDWATER_ID = GW.WT_GROUNDWATER_ID\n" +
                                     "WHERE GW.WT_GROUNDWATER_ID = ? ORDER BY well.WT_WELL_ID"
                                     ,EntityUtil.getTableName(WtWell.class)
                                     ,"WT_GW_WELL"
                                     ,EntityUtil.getTableName(WtGroundwater.class));

        Collection<WtWell> wellList = new ArrayList<>();
        if (groundwater.getWtGroundwaterId() != null){
          GenericFacade.executeQuery(conn, query, Arrays.asList(groundwater.getWtGroundwaterId())
                                    , new QueryDelegate(wellList) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtWell> wells = this.getListener();
              WtWell well = null;
              while(rs.next()) {
                well = new WtWell();
                EntityUtil.Load(rs, well);
                wells.add(well);
              }
            }
          });
          linkAssociatedWells(newGroundwater,wellList);
          newGroundwater.setWtWellCollection(wellList);
          wgwf.edit(newGroundwater);
        }
        //</editor-fold>

        wtf.edit(newProposal);

        mv = new ModelAndView("proposal/edit");
        mv.addObject("proposal", newProposal);
        mv.addObject("seller", seller);
        mv.addObject("buyers", buyers);
        mv.addObject("buyersContactList", buyersContactList);
  //          mv.addObject("stateList", stateList);
        mv.addObject("buyersContact", buyersContact);
        mv.addObject("agencies", agencies);
        mv.addObject("LookupDataContext", LookupDataContext.getInstance());
      } catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }

    private void linkAttachmentList(WtCropIdling newCropIdling, List<WtAttachment> attachments){
      WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
      Collection<WtCropIdling> wtCropIdlingCollection = new ArrayList();
      for(WtAttachment attachment:attachments){
        WtAttachment attachmentFull = waf.find(attachment.getWtAttachmentId());
        wtCropIdlingCollection = attachmentFull.getWtCropIdlingCollection();
        wtCropIdlingCollection.add(newCropIdling);
        waf.edit(attachmentFull);
      }
    }

    private void linkAssociatedWells(WtGroundwater newGroundwater, Collection<WtWell> wellList){
      WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
      Collection<WtGroundwater> wtGroundwaterCollection = new ArrayList();
      for(WtWell well:wellList){
        WtWell wellFull = wwf.find(well.getWtWellId());
        wtGroundwaterCollection = wellFull.getWtGroundwaterCollection();
        wtGroundwaterCollection.add(newGroundwater);
        wwf.edit(wellFull);
      }
    }

//    private void copyAttachmentList(List<WtAttachment> newAttachments, List<WtAttachment> attachments){
//      WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
//      WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
//
//      for(WtAttachment attachment:attachments){
//        WtAttachmentWithFile attachmentWithFile = waff.find(attachment.getWtAttachmentId());
//        WtAttachmentWithFile newAttachmentWithFile = new WtAttachmentWithFile();
//        newAttachmentWithFile.setAttachmentType(attachmentWithFile.getAttachmentType());
//        newAttachmentWithFile.setFilename(attachmentWithFile.getFilename());
//        newAttachmentWithFile.setTitle(attachmentWithFile.getTitle());
//        newAttachmentWithFile.setMimeType(attachmentWithFile.getMimeType());
//        newAttachmentWithFile.setFileSize(attachmentWithFile.getFileSize());
//        newAttachmentWithFile.setFileLob(attachmentWithFile.getFileLob());
//        newAttachmentWithFile.setIsActive(attachmentWithFile.getIsActive());
//        newAttachmentWithFile.setCreatedById(attachmentWithFile.getCreatedById());
//        waff.create(newAttachmentWithFile);
//        attachment.setWtAttachmentId(newAttachmentWithFile.getWtAttachmentId());
//        newAttachments.add(attachment);
//      }
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Original Edit Proposal Page">
    @RequestMapping("/edit2/{transId}")
    public ModelAndView edit(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
      ModelAndView mv = null;
      try {
        LoggedInCheckNoAjax(request,response);
        AppUser user = UserContext.getInstance().getUser();//(AppUser)request.getSession().getAttribute(SESSION_KEY_USER);

        WtTransFacade f = WebUtil.getFacade(WtTransFacade.class);
        WtTrans proposal = f.find(transId);
        if (proposal == null){
          throw new Exception("The water transfer id is not valid!");
        }
        else if(user!=null && user.isAppAccount() && !proposal.hasUser(user.getUserId())){
          throw new Exception("You are not authorized to view the water transfer proposal!");
        }

        WtStatusTrackFacade wstf = WebUtil.getFacade(WtStatusTrackFacade.class);
        WtReviewerCommentsFacade wrcf = WebUtil.getFacade(WtReviewerCommentsFacade.class);
        List<WtStatusTrack> statusTrackList = wstf.select("SELECT WT.* FROM WT_STATUS_TRACK WT WHERE WT_TRANS_ID="+transId+" ORDER BY STATUS_DATE",com.gei.entities.WtStatusTrack.class);
        List<WtReviewerComments> reviewerComments = wrcf.select("SELECT WT.* FROM WT_REVIEWER_COMMENTS WT WHERE WT_TRANS_ID="+transId+" ORDER BY CREATED_DATE", com.gei.entities.WtReviewerComments.class);

        List<WtAgency> agencyList = LookupDataContext.getInstance().getAgencyLookup();//waf.select("SELECT * FROM WT_AGENCY ORDER BY AGENCY_FULL_NAME", com.gei.entities.WtAgency.class);
        List<WtCity> cityList = LookupDataContext.getInstance().getCityLookup();//wcif.findAll();
        List<WtState> stateList = LookupDataContext.getInstance().getStateLookup();//wsf.findAll();
        List<WtStatusFlag> statusList = LookupDataContext.getInstance().getStatusFlagLookup();//wsff.findAll();
        List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup();

        // Get Buyers Contact
        Collection<WtContact> buyersContactCol = proposal.getBuyersContactCollection();
        WtContact buyersContact = null;
        if(buyersContactCol != null && buyersContactCol.size() == 1){
          buyersContact = buyersContactCol.iterator().next();
        }

        mv = new ModelAndView("proposal/edit");
        mv.addObject("proposal", proposal);
        mv.addObject("agencyList", agencyList);
        mv.addObject("cityList", cityList);
        mv.addObject("stateList", stateList);
        mv.addObject("buyersContactList", buyersContactList);
        mv.addObject("buyersContact", buyersContact);
        mv.addObject("statusList", statusList);
        mv.addObject("statusTrackList", statusTrackList);
        mv.addObject("reviewerComments", reviewerComments);
      }
      catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save Proposal and Related Tables">
    public WtTrans editSellerAgency(WtTrans trans,JSONObject json)
    {
        JSONObject jsonData = json;
        if(jsonData.has("sellerTab"))
        {
            JSONObject sellerAgency = new JSONObject(jsonData.optString("sellerTab"));
            WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
            if(sellerAgency.optString("WT_AGENCY_ID").isEmpty()){
                trans.setWtSellerCollection(null);
                return trans;
            }
            WtAgency agency = waf.find(sellerAgency.optInt("WT_AGENCY_ID"));
            Collection<WtAgency> collect = waf.findAll(agency);
            trans.setWtSellerCollection(collect);
            return trans;
        }
        return trans;
    }


    public WtTrans editBuyerAgency(WtTrans trans,JSONObject json)
    {
       JSONObject jsonData = json;
       if(jsonData.has("buyerTab"))
       {
            WtBuyerFacade bf = (WtBuyerFacade)appContext.getBean(WtBuyerFacade.class.getSimpleName());
            WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
            WtBuyer buyer;
            WtBuyerPK bpk;
            Collection<WtBuyer> coll = new ArrayList();
            Collection<WtBuyer> buyerCollection = trans.getWtBuyerCollection();
            int jsonLength = jsonData.optJSONArray("buyerTab").length();

            // Get Buyers Contact/Representative
//            AppUser repUser =  null;
            if(jsonLength > 0){
              WtContact buyersContact = null;
              JSONObject lastObj = jsonData.optJSONArray("buyerTab").getJSONObject(jsonLength-1);
              if (!lastObj.optString("WT_CONTACT_ID").isEmpty()){
                buyersContact = wcf.find(lastObj.optInt("WT_CONTACT_ID"));
                Collection<WtContact> collect = wcf.findAll(buyersContact);
                trans.setBuyersContactCollection(collect);
                // Link buyers representative to trans
//                repUser = buyersContact.getUser();
//                if ( repUser != null && repUser.getUserId() != null){
//                  linkTransUser(trans, repUser.getUserId());
//                }
              } else {
                trans.setBuyersContactCollection(null);
              }
            }

            // Get Buyer Agency
            for(int i=0;i<jsonLength;i++)
            {
                buyer = new WtBuyer();
                bpk =  new WtBuyerPK();
                JSONObject obj = jsonData.optJSONArray("buyerTab").getJSONObject(i);
                if (!obj.optString("WT_AGENCY_ID").isEmpty()){
                  bpk.setWtAgencyId(obj.optInt("WT_AGENCY_ID"));
                  bpk.setWtTransId(jsonData.optInt("wtTransId"));
                  buyer.setWtBuyerPK(bpk);
                  if(obj.optString("SHARE_PERCENT").isEmpty()){
                    buyer.setSharePercent(null);
                  } else {
                    buyer.setSharePercent(obj.optDouble("SHARE_PERCENT"));
                  }
                  coll.add(buyer);

                  // Link buyer Agency to buyers Representative
//                  Integer agencyId = obj.optInt("WT_AGENCY_ID");
//                  if ( repUser != null && agencyId != null){
//                    linkAgencyRepUser(agencyId,repUser);
//                  }
                }
            }
            if(buyerCollection != null)
            {
                for(WtBuyer buy:buyerCollection)
                {
                    Iterator<WtBuyer> it = coll.iterator();
                    boolean isRemove = true;
                    while(it.hasNext())
                    {

                        if(buy.getWtBuyerPK().getWtAgencyId() == it.next().getWtBuyerPK().getWtAgencyId())
                        {
                            isRemove=false;
                            break;
                        }
                    }
                    if(isRemove)
                    {
                        long agencyId = buy.getWtBuyerPK().getWtAgencyId();
                        Collection<WtBuyer> buyList = bf.findAll(buy);
                        for(WtBuyer removeBuyer:buyList)
                        {
                            if(removeBuyer.getWtBuyerPK().getWtAgencyId()==agencyId)
                            {
                                bf.remove(removeBuyer);
                            }
                        }
                    }
                }
            }
           trans.setWtBuyerCollection(coll);
           return trans;
//           wtf.edit(wtt);
       }
       return trans;
    }

    public void editTransCounty(WtTrans trans,JSONObject jsonData){
      WtCountyFacade wcf = (WtCountyFacade)appContext.getBean(WtCountyFacade.class.getSimpleName());
      String countyIdList[] = StringUtils.commaDelimitedListToStringArray(jsonData.optString("countyId"));
      Collection<WtCounty> countyCollection = trans.getWtCountyCollection();
      if(countyCollection != null){
        for(WtCounty wt: countyCollection){
          Collection<WtTrans> transCollection = wt.getWtTransCollection();
          transCollection.remove(trans);
          wt.setWtTransCollection(transCollection);
          wcf.edit(wt);
        }
      }

      if(countyIdList.length > 0){
        countyCollection = new ArrayList();
        for(String countyId : countyIdList){
          WtCounty county = wcf.find(Integer.parseInt(countyId));
          if(county != null){
            Collection<WtTrans> transCollection = county.getWtTransCollection();
            transCollection.add(trans);
            county.setWtTransCollection(transCollection);
            wcf.edit(county);
            countyCollection.add(county);
          }
        }
        trans.setWtCountyCollection(countyCollection);
      }
//      } else {
//        trans.setWtCountyCollection(null);
//      }
    }

    public void editTransType(WtTrans trans,JSONObject jsonData){
        WtFuTypeFacade wff = (WtFuTypeFacade)appContext.getBean(WtFuTypeFacade.class.getSimpleName());
        AppGroupFacade agf = (AppGroupFacade)appContext.getBean(AppGroupFacade.class.getSimpleName());
        WtTransTypeFacade wttf = (WtTransTypeFacade)appContext.getBean(WtTransTypeFacade.class.getSimpleName());
        // Create new wtTransType Collection for Edit
        Collection<WtTransType> wtTransTypeCollection = new ArrayList();
        AppGroup usbrGroup = agf.getGroupByCode(GROUP_USBR);
        WtTransType transType;
        WtTransTypePK ttpk;

        if(jsonData.has("SWP") && jsonData.optString("SWP").equals("on")){
            wff.set("fuType", "SWP");
            List<WtFuType> fuType = wff.findAll();
            for(WtFuType type: fuType){
                if(type.getFuSubType() != null){
                    if(jsonData.optString("Banks").equals("on") && type.getFuSubType().equals("Banks")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        if(jsonData.optString("BanksV").isEmpty()){
                            transType.setTypeVolume(null);
                        } else {
//                            transType.setTypeVolume(jsonData.optInt("BanksV"));
                            String num = jsonData.optString("BanksV").replaceAll("[,\\s]", "");
                            transType.setTypeVolume(Double.parseDouble(num));
                        }
                        wtTransTypeCollection.add(transType);
                    }
                    if(jsonData.optString("swpIntertie").equals("on") && type.getFuSubType().equals("SWP/CVP Intertie")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        wtTransTypeCollection.add(transType);
                    }
                    if(jsonData.optString("NorthBay").equals("on") && type.getFuSubType().equals("North Bay")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        if(jsonData.optString("NorthBayV").isEmpty()){
                            transType.setTypeVolume(null);
                        } else {
//                          transType.setTypeVolume(jsonData.optInt("NorthBayV"));
                          String num = jsonData.optString("NorthBayV").replaceAll("[,\\s]", "");
                          transType.setTypeVolume(Double.parseDouble(num));
                        }
                        wtTransTypeCollection.add(transType);
                    }
                }
                else
                {
                    transType = new WtTransType();
                    ttpk = new WtTransTypePK();
                    ttpk.setWtTransId(trans.getWtTransId());
                    transType.setWtTrans(trans);
                    ttpk.setWtFuTypeId(type.getWtFuTypeId());
                    transType.setWtTransTypePK(ttpk);
                    wtTransTypeCollection.add(transType);
                }
            }
        }
        if(jsonData.has("CVP") && jsonData.optString("CVP").equals("on")){
            wff.set("fuType", "CVP");
            List<WtFuType> fuType = wff.findAll();
            for(WtFuType type: fuType){
                if(type.getFuSubType() != null){
                    if(jsonData.optString("Jones").equals("on") && type.getFuSubType().equals("Jones")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        if(jsonData.optString("JonesV").isEmpty()){
                            transType.setTypeVolume(null);
                        } else {
                          String num = jsonData.optString("JonesV").replaceAll("[,\\s]", "");
                          transType.setTypeVolume(Double.parseDouble(num));
//                          transType.setTypeVolume(jsonData.optInt("JonesV"));
                        }
                        wtTransTypeCollection.add(transType);
                        linkTransGroup(usbrGroup, trans);
                    }
                    if(jsonData.optString("cvpIntertie").equals("on") && type.getFuSubType().equals("SWP/CVP Intertie")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        wtTransTypeCollection.add(transType);
                    }
                    if(jsonData.optString("Forbearance").equals("on") && type.getFuSubType().equals("Forbearance")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        if(jsonData.optString("ForbearanceV").isEmpty()){
                            transType.setTypeVolume(null);
                        } else {
                          String num = jsonData.optString("ForbearanceV").replaceAll("[,\\s]", "");
                          transType.setTypeVolume(Double.parseDouble(num));
                        }
                        wtTransTypeCollection.add(transType);
                    }
                    if(jsonData.optString("WarrenAct").equals("on") && type.getFuSubType().equals("Warren Act")){
                        transType = new WtTransType();
                        ttpk = new WtTransTypePK();
                        ttpk.setWtTransId(trans.getWtTransId());
                        transType.setWtTrans(trans);
                        ttpk.setWtFuTypeId(type.getWtFuTypeId());
                        transType.setWtTransTypePK(ttpk);
                        if(jsonData.optString("WarrenActV").isEmpty()){
                            transType.setTypeVolume(null);
                        } else {
                          String num = jsonData.optString("WarrenActV").replaceAll("[,\\s]", "");
                          transType.setTypeVolume(Double.parseDouble(num));
                        }
                        wtTransTypeCollection.add(transType);
                    }
                }
                else
                {
                    transType = new WtTransType();
                    ttpk = new WtTransTypePK();
                    ttpk.setWtTransId(trans.getWtTransId());
                    transType.setWtTrans(trans);
                    ttpk.setWtFuTypeId(type.getWtFuTypeId());
                    transType.setWtTransTypePK(ttpk);
                    wtTransTypeCollection.add(transType);
                }
            }
        }
        if(jsonData.has("OTHER")&& jsonData.optString("OTHER").equals("on")){
            transType = new WtTransType();
            ttpk = new WtTransTypePK();
            ttpk.setWtTransId(trans.getWtTransId());
            transType.setWtTrans(trans);
            wff.set("fuType", "OTHER");
            WtFuType type = wff.find();
            ttpk.setWtFuTypeId(type.getWtFuTypeId());
            transType.setWtTransTypePK(ttpk);
            if(jsonData.optString("typeDetail").isEmpty()){
              transType.setTypeDetail(null);
            } else {
              transType.setTypeDetail(jsonData.optString("typeDetail"));
            }
            if(jsonData.optString("otherV").isEmpty()){
              transType.setTypeVolume(null);
            } else {
              String num = jsonData.optString("otherV").replaceAll("[,\\s]", "");
              transType.setTypeVolume(Double.parseDouble(num));
            }
            wtTransTypeCollection.add(transType);
        }

        //Remove unchecked Trans Type
        Collection<WtTransType> oldTransTypeCollection = trans.getWtTransTypeCollection();
        if (oldTransTypeCollection != null){
            for(WtTransType type: oldTransTypeCollection){
                if(!type.isExist(wtTransTypeCollection)){
                    wttf.remove(type);
                    if(type.getWtFuType().getFuType().equals("CVP")
                            || type.getWtFuType().getFuSubType().equals("Jones")){
                      unlinkTransGroup(usbrGroup, trans);
                    }
                }
            }
        }

        trans.setWtTransTypeCollection(wtTransTypeCollection);

    }

    public void editEnvRegCompliance(WtTrans trans,JSONObject jsonData) throws ParseException{
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTransSwrcbFacade wtsf = (WtTransSwrcbFacade)appContext.getBean(WtTransSwrcbFacade.class.getSimpleName());
        WtTransCeqaFacade wtcf = (WtTransCeqaFacade)appContext.getBean(WtTransCeqaFacade.class.getSimpleName());
        WtTransNepaFacade wtnf = (WtTransNepaFacade)appContext.getBean(WtTransNepaFacade.class.getSimpleName());
        WtTransSwrcb swrcb = trans.getWtTransSwrcb();
        WtTransCeqa ceqa = trans.getWtTransCeqa();
        WtTransNepa nepa = trans.getWtTransNepa();

        // SWRCB Information
        if(jsonData.has("SWRCB")&&jsonData.optString("SWRCB").equals("on")){
            if(swrcb == null){
                swrcb = new WtTransSwrcb();
                swrcb.setWtTrans(trans);
                wtsf.create(swrcb);
            }
            if (jsonData.optString("swrcbPetitionDate").isEmpty()){
                swrcb.setSwrcbPetitionDate(null);
            } else {
                swrcb.setSwrcbPetitionDate(df.parse(jsonData.optString("swrcbPetitionDate")));
            }
            if (jsonData.optString("swrcbOrderDate").isEmpty()){
                swrcb.setSwrcbOrderDate(null);
            } else {
                swrcb.setSwrcbOrderDate(df.parse(jsonData.optString("swrcbOrderDate")));
            }
            wtsf.edit(swrcb);
        }
        if(jsonData.optString("SWRCB").equals("off")&&swrcb!=null){
            swrcb.setWtTrans(null);
            trans.setWtTransSwrcb(null);
            wtf.edit(trans);
            wtsf.remove(swrcb);
        }

        // CEQA Information
        if(jsonData.has("CEQA")&&jsonData.optString("CEQA").equals("on")){
            if(ceqa == null){
                ceqa = new WtTransCeqa();
                ceqa.setWtTrans(trans);
                wtcf.create(ceqa);
            }
            if (jsonData.optString("ceqaSubmittedDate").isEmpty()){
                ceqa.setCeqaSubmittedDate(null);
            } else {
                ceqa.setCeqaSubmittedDate(df.parse(jsonData.optString("ceqaSubmittedDate")));
            }
            if (jsonData.optString("ceqaAdoptedDate").isEmpty()){
                ceqa.setCeqaAdoptedDate(null);
            } else {
                ceqa.setCeqaAdoptedDate(df.parse(jsonData.optString("ceqaAdoptedDate")));
            }
            ceqa.setSchNum(jsonData.optString("schNum"));
            wtcf.edit(ceqa);
        }
        if(jsonData.optString("CEQA").equals("off")&&ceqa!=null){
            ceqa.setWtTrans(null);
            trans.setWtTransCeqa(null);
            wtf.edit(trans);
            wtcf.remove(ceqa);
        }

        // NEPA Information
        if(jsonData.has("NEPA")&&jsonData.optString("NEPA").equals("on")){
            if(nepa == null){
                nepa = new WtTransNepa();
                nepa.setWtTrans(trans);
                wtnf.create(nepa);
            }
            if (jsonData.optString("nepaIssuedDate").isEmpty()){
                nepa.setNepaIssuedDate(null);
            } else {
                nepa.setNepaIssuedDate(df.parse(jsonData.optString("nepaIssuedDate")));
            }
            if (jsonData.optString("nepaAdoptedDate").isEmpty()){
                nepa.setNepaAdoptedDate(null);
            } else {
                nepa.setNepaAdoptedDate(df.parse(jsonData.optString("nepaAdoptedDate")));
            }
            nepa.setNepaNum(jsonData.optString("nepaNum"));
            wtnf.edit(nepa);
        }
        if(jsonData.optString("NEPA").equals("off")&&nepa!=null){
            nepa.setWtTrans(null);
            trans.setWtTransNepa(null);
            wtf.edit(trans);
            wtnf.remove(nepa);
        }
    }

    public void eiditTransReach(WtTrans trans,JSONObject jsonData) throws ParseException{
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTransReachFacade wtrf = (WtTransReachFacade)appContext.getBean(WtTransReachFacade.class.getSimpleName());
        WtTransReach reach = trans.getWtTransReach();

        if(trans.getIsStateContractor() == 0){
            if(reach == null){
                reach = new WtTransReach();
                reach.setWtTrans(trans);
                wtrf.create(reach);
            }
            reach.setPowerProvider(jsonData.optString("powerProvider"));
            if(jsonData.optString("isReach1").equals("on")){
                reach.setIsReach1(1);
            } else {
                reach.setIsReach1(0);
            }
            if(jsonData.optString("isReach2a").equals("on")){
                reach.setIsReach2a(1);
            } else {
                reach.setIsReach2a(0);
            }
            if(jsonData.optString("isReach2b").equals("on")){
                reach.setIsReach2b(1);
            } else {
                reach.setIsReach2b(0);
            }
            if(jsonData.optString("isReach3").equals("on")){
                reach.setIsReach3(1);
            } else {
                reach.setIsReach3(0);
            }
            if(jsonData.optString("isReach4").equals("on")){
                reach.setIsReach4(1);
            } else {
                reach.setIsReach4(0);
            }
            if(jsonData.optString("isReach5").equals("on")){
                reach.setIsReach5(1);
            } else {
                reach.setIsReach5(0);
            }
            if(jsonData.optString("isReach6").equals("on")){
                reach.setIsReach6(1);
            } else {
                reach.setIsReach6(0);
            }
            if(jsonData.optString("isReach7").equals("on")){
                reach.setIsReach7(1);
            } else {
                reach.setIsReach7(0);
            }
            wtrf.edit(reach);
        }

        // If Select State Water Project Contractor Yes will need remove REACH record
        if(trans.getIsStateContractor() == 1&&reach!=null){
            reach.setWtTrans(null);
            trans.setWtTransReach(null);
            wtf.edit(trans);
            wtrf.remove(reach);
        }
    }

//    @RequestMapping("/save")
    public void saveProposalJson(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if(user==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        if(jsonData.optString("wtTransId").isEmpty())
        {
            // Create New Proposal
            WtStatusFlag statusFlag = wsff.find(Status.DRAFT);
            WtTrans newProposal = new WtTrans();
            newProposal.setTransYear(year);
            newProposal.setCreatedById(user.getUserId());
            newProposal.setUpdatedById(user.getUserId());
            newProposal.setWtStatusFlag(statusFlag);
            newProposal.setIsActive(Status.ACTIVE);
            wtf.create(newProposal);
            if(newProposal.getWtTransId()!=null){
                linkTransUser(newProposal,user.getUserId());
                jsonData.put("wtTransId", newProposal.getWtTransId());
                if(!jsonData.optString("accessContact").isEmpty())
                {
                    JSONArray accessContact = jsonData.optJSONArray("accessContact");
                    for(int i = 0;i<accessContact.length();i++)
                    {
                        JSONObject permissionContact = accessContact.optJSONObject(i);
                        permissionContact.put("wtTransId", newProposal.getWtTransId());
                        contactAccess(permissionContact);
                    }
                }
            }
            setStatusTrack(newProposal,user);
            //Send Email When created new Proposal
//            String emailMsg = "New proposal #"+newProposal.getWtTransId()+" Created.";
//            sendEmail(emailMsg);
        }
        WtTrans transRec = wtf.find(jsonData.optInt("wtTransId"));

        if(transRec==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        transRec.setUpdatedById(user.getUserId());
//        Map<String,String> map = new HashMap();
//        map = jsonData.getJSONMap();
//        transRec.loadProperties(map);
        if(jsonData.optString("transYear").isEmpty()){
            transRec.setTransYear(year);
        }else{
            transRec.setTransYear(jsonData.optInt("transYear"));
        }
        if(!jsonData.optString("isShortLong").isEmpty()){
            transRec.setIsShortLong(jsonData.optInt("isShortLong"));
        }
        if(!jsonData.optString("hasPreTrans").isEmpty()){
            transRec.setHasPreTrans(jsonData.optInt("hasPreTrans"));
        }
        transRec.setTransDescription(jsonData.optString("transDescription"));
        if(jsonData.has("proTransQua")){
            if(jsonData.optString("proTransQua").isEmpty()){
                transRec.setProTransQua(null);
            }else{
                String num = jsonData.optString("proTransQua").replaceAll("[,\\s]", "");
                transRec.setProTransQua(Double.parseDouble(num));
            }
        }
        if(jsonData.optString("proUnitCost").isEmpty()){
            transRec.setProUnitCost(null);
        }else{
            String paid = jsonData.optString("proUnitCost").replaceAll("[,\\s]", "");
            transRec.setProUnitCost(Double.parseDouble(paid));
        }
        if(jsonData.optString("proAgreePaid").isEmpty()){
            transRec.setProAgreePaid(null);
        }else{
            String paid = jsonData.optString("proAgreePaid").replaceAll("[,\\s]", "");
            transRec.setProAgreePaid(Double.parseDouble(paid));
        }
//        transRec.setProAgreePaidRange(jsonData.optString("proAgreePaidRange"));
        transRec.setSurWaterSource(jsonData.optString("surWaterSource"));
        transRec.setMajorRiverAttribute(jsonData.optString("majorRiverAttribute"));
        if(jsonData.optString("transWinStart").isEmpty()){
            transRec.setTransWinStart(null);
        } else {
            transRec.setTransWinStart(df.parse(jsonData.optString("transWinStart")));
        }
        if(jsonData.optString("transWinEnd").isEmpty()){
            transRec.setTransWinEnd(null);
        } else {
            transRec.setTransWinEnd(df.parse(jsonData.optString("transWinEnd")));
        }
        transRec.setReqExpFrom(jsonData.optString("reqExpFrom"));
        transRec.setReqExpTo(jsonData.optString("reqExpTo"));
        if(jsonData.optString("reqExpFromDate").isEmpty()){
            transRec.setReqExpFromDate(null);
        } else {
            transRec.setReqExpFromDate(df.parse(jsonData.optString("reqExpFromDate")));
        }
        if(jsonData.optString("reqExpToDate").isEmpty()){
            transRec.setReqExpToDate(null);
        } else {
            transRec.setReqExpToDate(df.parse(jsonData.optString("reqExpToDate")));
        }
        transRec.setReqStorageExp(jsonData.optString("reqStorageExp"));
        if(!jsonData.optString("deltaTransferInd").isEmpty()){
            transRec.setDeltaTransferInd(jsonData.optInt("deltaTransferInd"));
        }
        if(!jsonData.optString("isStateContractor").isEmpty()){
            transRec.setIsStateContractor(jsonData.optInt("isStateContractor"));
        }
        transRec.setWtComm(jsonData.optString("wtComm"));

        // Set Proposal Types
        if(jsonData.optString("proAcrIdleInd").isEmpty()){
            transRec.setProAcrIdleInd(0);
            transRec.setWtCropIdling(null);
            deleteCropIdling(transRec);
        } else {
            transRec.setProAcrIdleInd(1);
            saveCropIdling(jsonData,transRec);
        }

        if(jsonData.optString("resReOpInd").isEmpty()){
            transRec.setResReOpInd(0);
            transRec.setWtReservoir(null);
            deleteReservoir(transRec);
        } else{
            transRec.setResReOpInd(1);
            saveReservoir(jsonData,transRec);
        }

        if(jsonData.optString("wellUseNumInd").isEmpty()){
            transRec.setWellUseNumInd(0);
            transRec.setWtGroundwater(null);
            deleteGroundwater(transRec);
        } else {
            transRec.setWellUseNumInd(1);
            saveGroundwater(jsonData,transRec);
        }

//        if(jsonData.optString("consWaterInd").isEmpty()){
//            transRec.setConsWaterInd(0);
//        } else {
//            transRec.setConsWaterInd(1);
//        }

        editTransCounty(transRec,jsonData);
        editTransType(transRec,jsonData);
        editEnvRegCompliance(transRec,jsonData);
        if(transRec.getIsStateContractor() != null){
            eiditTransReach(transRec,jsonData);
        }
        editSellerAgency(transRec,jsonData);
        editBuyerAgency(transRec,jsonData);
        wtf.edit(transRec);

        jsonResponse.put("wtTransId", transRec.getWtTransId())
                    .put("transStatus", transRec.getWtStatusFlag().getStatusName())
                    .put("success",true);
      }
      catch(Exception ex){
        jsonResponse.put("success", false).put("error", ex.getMessage());
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public void saveProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{ //using LoadProperties
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsf = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
        WtTrans transRec = wtf.find(jsonData.optInt("wtTransId"));
        Map<String,String> map = new HashMap();
        map = jsonData.getJSONMap();
        editSellerAgency(transRec,jsonData);
        editBuyerAgency(transRec,jsonData);
        wtf.edit(transRec.loadProperties(map));
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success", false);
        if(ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

//    public void saveReportComment(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{ //using LoadProperties
//      JSONObject jsonResponse = new JSONObject();
//      try{
//        if(!UserContext.getInstance().isLoggedIn()){
//          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
//        }
//        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
//        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
//
//        WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
//        if(transRec==null){
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//        }
//        transRec.setUpdatedById(user.getUserId());
//        transRec.setReportComment(requestUtil.getString("reportComment"));
//
//        wtf.edit(transRec);
//        jsonResponse.put("success", true);
//      }catch(Exception ex){
//        jsonResponse.put("error", ex.getMessage()).put("success",false);
//        if (ex instanceof MyException){
//          jsonResponse.put("callback", ((MyException)ex).getCallback());
//        }
//      }finally{
//        response.getWriter().write(jsonResponse.toString());
//      }
//    }

    @RequestMapping("/savereportfield")
    public void saveReportField(@RequestParam("transId") Integer transId,
                                @RequestParam("fieldName") String fieldName,
                                HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try {
        if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification transfer id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
        AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);

        WtTrans transRec = wtf.find(transId);
        if (transRec == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String textString = requestUtil.getString("fieldValue");
        String numString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        String contractAmount = requestUtil.getString("contractAmount").replaceAll("[,\\s]", "");

        if(null != fieldName)switch (fieldName) {
          case "reportComment":
            transRec.setReportComment(textString);
            break;
        }

        transRec.setUpdatedById(user.getUserId());
        wtf.edit(transRec);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Utils for Link methods">

    //Link App Group to Proposals
    private void linkTransGroup(AppGroup group, WtTrans trans){
      Collection<AppUser> appUserCollection = group.getAppUserCollection();
      if(appUserCollection != null && appUserCollection.size() > 0){
        for(AppUser user: appUserCollection){
          linkTransUser(trans, user.getUserId());
        }
      }
    }

    //Unlink App Group to Proposals
    private void unlinkTransGroup(AppGroup group, WtTrans trans){
      Collection<AppUser> appUserCollection = group.getAppUserCollection();
      if(appUserCollection != null && appUserCollection.size() > 0){
        for(AppUser user: appUserCollection){
          unlinkTransUser(trans, user.getUserId());
        }
      }
    }

    //Link User Account to Proposal
    private void linkTransUser(WtTrans transRec, int userId){
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      Collection<AppUser> appUserCollection = transRec.getAppUserCollection();
//      AppUser user = auf.find(userId);
//      Collection<WtTrans> transCollection = user.getWtTransCollection();
//      if(transCollection == null || transCollection.isEmpty()){
//        transCollection = new ArrayList();
//      }
//      transCollection.add(transRec);
//      auf.edit(user);
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

    //Link User Account to Agency as Buyers Representative
//    private void linkAgencyRepUser(Integer agencyId, AppUser buyerRep){
//      WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
//      WtAgency agency = waf.find(agencyId);
//      if (agency != null){
//        Collection<AppUser> buyerReps = agency.getRepUserCollection();
//
//        if (buyerReps == null){
//          buyerReps = new ArrayList();
//        }
//        if (!agency.hasRepUser(buyerRep.getUserId())){
//          buyerReps.add(buyerRep);
//          agency.setRepUserCollection(buyerReps);
//          waf.edit(agency);
//        }
//      }
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Base Info Part">
    @RequestMapping("/checktransfertype")
    public void checkTransferType(@RequestParam("transId") Integer transId
          ,@RequestParam("fieldName") String fieldName
          ,@RequestParam("fieldValue") Integer fieldValue
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((transId == null) || (StringUtils.isEmpty(fieldName)) || (fieldValue == null)) {
          throw new Exception("The modification transfer Id, field name and field value cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        if(user==null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        WtTrans transRec = wtf.find(transId);
        if(transRec==null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        if (null != fieldName)switch (fieldName) {
          case "cropIdling":
            if (fieldValue == 1){
              transRec.setProAcrIdleInd(1);
              createCropIdling(transRec);
            }else{
              transRec.setProAcrIdleInd(0);
              transRec.setWtCropIdling(null);
              deleteCropIdling(transRec);
            } break;
        }

        wtf.edit(transRec);
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

    @RequestMapping("/savebifield")
    public void saveBaseInfoField(@RequestParam("transId") Integer transId
                                  ,@RequestParam("fieldName") String fieldName
                                  ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification Transfer id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        int year = Calendar.getInstance().get(Calendar.YEAR);

        WtTrans transRec = wtf.find(transId);
        if(transRec==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        String fieldString = requestUtil.getString("fieldValue");
        String fieldValueString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        if (null != fieldName)switch (fieldName) {
          case "hasPreTrans":
            transRec.setHasPreTrans(Integer.parseInt(fieldValueString));
            break;
          case "transYear":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setTransYear(year);
            }else{
              transRec.setTransYear(Integer.parseInt(fieldValueString));
            } break;
          case "isShortLong":
            transRec.setIsShortLong(Integer.parseInt(fieldValueString));
            break;
          case "transDescription":
            transRec.setTransDescription(fieldString);
            break;
          case "proTransQua":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setProTransQua(null);
            }else{
              transRec.setProTransQua(Double.parseDouble(fieldValueString));
            } break;
          case "surWaterSource":
            transRec.setSurWaterSource(fieldString);
            break;
          case "majorRiverAttribute":
            transRec.setMajorRiverAttribute(fieldString);
            break;
          case "transWinStart":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setTransWinStart(null);
            }else{
              transRec.setTransWinStart(df.parse(fieldValueString));
            } break;
          case "transWinEnd":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setTransWinEnd(null);
            }else{
              transRec.setTransWinEnd(df.parse(fieldValueString));
            } break;
          case "isStateContractor":
            transRec.setIsStateContractor(Integer.parseInt(fieldValueString));
            break;
          case "reqExpFromDate":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setReqExpFromDate(null);
            }else{
              transRec.setReqExpFromDate(df.parse(fieldValueString));
            } break;
          case "reqExpToDate":
            if (StringUtils.isEmpty(fieldValueString)){
              transRec.setReqExpToDate(null);
            }else{
              transRec.setReqExpToDate(df.parse(fieldValueString));
            } break;
          case "reqStorageExp":
            transRec.setReqStorageExp(fieldString);
            break;
          case "deltaTransferInd":
            transRec.setDeltaTransferInd(Integer.parseInt(fieldValueString));
            break;
          case "wtComm":
            transRec.setWtComm(fieldString);
            break;
        }

        wtf.edit(transRec);
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

    @RequestMapping("/checkenvregcomp")
    public void checkEnvRegCompliance(@RequestParam("transId") Integer transId
                                      ,@RequestParam("fieldName") String fieldName
                                      ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification transfer Id, field name and field value cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTransSwrcbFacade wtsf = (WtTransSwrcbFacade)appContext.getBean(WtTransSwrcbFacade.class.getSimpleName());
        WtTransCeqaFacade wtcf = (WtTransCeqaFacade)appContext.getBean(WtTransCeqaFacade.class.getSimpleName());
        WtTransNepaFacade wtnf = (WtTransNepaFacade)appContext.getBean(WtTransNepaFacade.class.getSimpleName());

        WtTrans transRec = wtf.find(transId);
        if(transRec==null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        WtTransSwrcb swrcb = transRec.getWtTransSwrcb();
        WtTransCeqa ceqa = transRec.getWtTransCeqa();
        WtTransNepa nepa = transRec.getWtTransNepa();
        String fieldString = requestUtil.getString("fieldValue");
        if (null != fieldName)switch (fieldName) {
          case "SWRCB":
            if (fieldString.equalsIgnoreCase("on")){
              swrcb = new WtTransSwrcb();
              swrcb.setWtTrans(transRec);
              wtsf.create(swrcb);
              transRec.setWtTransSwrcb(swrcb);
            }else{
              swrcb.setWtTrans(null);
              transRec.setWtTransSwrcb(null);
              wtf.edit(transRec);
              wtsf.remove(swrcb);
            } break;
          case "CEQA":
            if (fieldString.equalsIgnoreCase("on")){
              ceqa = new WtTransCeqa();
              ceqa.setWtTrans(transRec);
              wtcf.create(ceqa);
              transRec.setWtTransCeqa(ceqa);
            }else{
              ceqa.setWtTrans(null);
              transRec.setWtTransCeqa(null);
              wtf.edit(transRec);
              wtcf.remove(ceqa);
            } break;
          case "NEPA":
            if (fieldString.equalsIgnoreCase("on")){
              nepa = new WtTransNepa();
              nepa.setWtTrans(transRec);
              wtnf.create(nepa);
              transRec.setWtTransNepa(nepa);
            }else{
              nepa.setWtTrans(null);
              transRec.setWtTransNepa(null);
              wtf.edit(transRec);
              wtnf.remove(nepa);
            } break;
        }

        wtf.edit(transRec);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CropIdling Part">
    private void createCropIdling(WtTrans transRec) throws IOException, SQLException{
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        if(cropIdling == null){
          cropIdling = new WtCropIdling();
          wcif.create(cropIdling);
          cropIdling.setWtTrans(transRec);
          wcif.edit(cropIdling);
          transRec.setWtCropIdling(cropIdling);
        }
    }

    private void saveCropIdling(JSONObject jsonData, WtTrans transRec) throws IOException, SQLException{
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCiMonthlyFacade wcmf = (WtCiMonthlyFacade)appContext.getBean(WtCiMonthlyFacade.class.getSimpleName());
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        WtCiMonthly ciMonthly = null;
        if(cropIdling == null){
          cropIdling = new WtCropIdling();
          wcif.create(cropIdling);
        } else {
          ciMonthly = cropIdling.getWtCiMonthly();
        }

        if(ciMonthly == null){
          ciMonthly = new WtCiMonthly();
          ciMonthly.setWtCropIdling(cropIdling);
          wcmf.create(ciMonthly);
        }

        if(StringUtils.isEmpty(jsonData.optString("waterTransQuaCI"))){
          cropIdling.setWaterTransQua(null);
        }else{
          String num = jsonData.optString("waterTransQuaCI").replaceAll("[,\\s]", "");
          cropIdling.setWaterTransQua(Double.parseDouble(num));
        }
        if(jsonData.optString("totalTransferAcr").isEmpty()){
          cropIdling.setTotalTransferAcr(null);
        }else{
          String num = jsonData.optString("totalTransferAcr").replaceAll("[,\\s]", "");
          cropIdling.setTotalTransferAcr(Double.parseDouble(num));
        }
//        if(jsonData.optString("proTransferByCI").isEmpty()){
//            cropIdling.setProTransferByCI(null);
//        }else{
////            cropIdling.setProTransferByCI(jsonData.optInt("proTransferByCI"));
//            String num = jsonData.optString("proTransferByCI").replaceAll("[,\\s]", "");
//            cropIdling.setProTransferByCI(Double.parseDouble(num));
//        }
//        if(jsonData.optString("proTransferByCS").isEmpty()){
//            cropIdling.setProTransferByCS(null);
//        }else{
////            cropIdling.setProTransferByCS(jsonData.optInt("proTransferByCS"));
//            String num = jsonData.optString("proTransferByCS").replaceAll("[,\\s]", "");
//            cropIdling.setProTransferByCS(Double.parseDouble(num));
//        }
        if(jsonData.optString("currentFsAgency").isEmpty()){
          cropIdling.setCurrentFsAgency(null);
        }else{
          String num = jsonData.optString("currentFsAgency").replaceAll("[,\\s]", "");
          cropIdling.setCurrentFsAgency(Double.parseDouble(num));
        }
        if(jsonData.optString("isResReleaseCI").isEmpty()){
          cropIdling.setIsResRelease(null);
        }else{
          cropIdling.setIsResRelease(jsonData.optInt("isResReleaseCI"));
        }
        cropIdling.setWtTrans(transRec);
        wcif.edit(cropIdling);
        transRec.setWtCropIdling(cropIdling);

        // Save CI Monthly Transfer Water
        if(jsonData.optString("mayEtaw").isEmpty()){
          ciMonthly.setMayEtaw(null);
        } else {
          ciMonthly.setMayEtaw(jsonData.optInt("mayEtaw"));
        }
        if(jsonData.optString("juneEtaw").isEmpty()){
          ciMonthly.setJuneEtaw(null);
        } else {
          ciMonthly.setJuneEtaw(jsonData.optInt("juneEtaw"));
        }
        if(jsonData.optString("julyEtaw").isEmpty()){
          ciMonthly.setJulyEtaw(null);
        } else {
          ciMonthly.setJulyEtaw(jsonData.optInt("julyEtaw"));
        }
        if(jsonData.optString("augustEtaw").isEmpty()){
          ciMonthly.setAugustEtaw(null);
        } else {
          ciMonthly.setAugustEtaw(jsonData.optInt("augustEtaw"));
        }
        if(jsonData.optString("septemberEtaw").isEmpty()){
          ciMonthly.setSeptemberEtaw(null);
        } else {
          ciMonthly.setSeptemberEtaw(jsonData.optInt("septemberEtaw"));
        }
        if(jsonData.optString("mayTw").isEmpty()){
          ciMonthly.setMayTw(null);
        }else{
          String num = jsonData.optString("mayTw").replaceAll("[,\\s]", "");
          ciMonthly.setMayTw(Double.parseDouble(num));
        }
        if(jsonData.optString("juneTw").isEmpty()){
          ciMonthly.setJuneTw(null);
        }else{
          String num = jsonData.optString("juneTw").replaceAll("[,\\s]", "");
          ciMonthly.setJuneTw(Double.parseDouble(num));
        }
        if(jsonData.optString("julyTw").isEmpty()){
          ciMonthly.setJulyTw(null);
        }else{
          String num = jsonData.optString("julyTw").replaceAll("[,\\s]", "");
          ciMonthly.setJulyTw(Double.parseDouble(num));
        }
        if(jsonData.optString("augustTw").isEmpty()){
          ciMonthly.setAugustTw(null);
        }else{
          String num = jsonData.optString("augustTw").replaceAll("[,\\s]", "");
          ciMonthly.setAugustTw(Double.parseDouble(num));
        }
        if(jsonData.optString("septemberTw").isEmpty()){
          ciMonthly.setSeptemberTw(null);
        }else{
          String num = jsonData.optString("septemberTw").replaceAll("[,\\s]", "");
          ciMonthly.setSeptemberTw(Double.parseDouble(num));
        }
        wcmf.edit(ciMonthly);
    }

    @RequestMapping("/savecropidling")
    public void saveCropIdling(@RequestParam("cropIdlingId") Integer cropIdlingId
                               ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if (cropIdlingId == null) {
          throw new Exception("The modification Crop Idling id cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCiMonthlyFacade wcmf = (WtCiMonthlyFacade)appContext.getBean(WtCiMonthlyFacade.class.getSimpleName());
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));

        WtCropIdling cropIdling = wcif.find(cropIdlingId);
        if(cropIdling==null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        WtCiMonthly ciMonthly = cropIdling.getWtCiMonthly();
        if(ciMonthly == null){
          ciMonthly = new WtCiMonthly();
          ciMonthly.setWtCropIdling(cropIdling);
          wcmf.create(ciMonthly);
        }

        if(StringUtils.isEmpty(jsonData.optString("waterTransQuaCI"))){
          cropIdling.setWaterTransQua(null);
        }else{
          String num = jsonData.optString("waterTransQuaCI").replaceAll("[,\\s]", "");
          cropIdling.setWaterTransQua(Double.parseDouble(num));
        }
        if(jsonData.optString("totalTransferAcr").isEmpty()){
          cropIdling.setTotalTransferAcr(null);
        }else{
          String num = jsonData.optString("totalTransferAcr").replaceAll("[,\\s]", "");
          cropIdling.setTotalTransferAcr(Double.parseDouble(num));
        }
        if(jsonData.optString("currentFsAgency").isEmpty()){
          cropIdling.setCurrentFsAgency(null);
        }else{
          String num = jsonData.optString("currentFsAgency").replaceAll("[,\\s]", "");
          cropIdling.setCurrentFsAgency(Double.parseDouble(num));
        }
        if(jsonData.optString("isResReleaseCI").isEmpty()){
          cropIdling.setIsResRelease(null);
        }else{
          cropIdling.setIsResRelease(jsonData.optInt("isResReleaseCI"));
        }
        wcif.edit(cropIdling);

        // Save CI Monthly Transfer Water
        if(jsonData.optString("mayEtaw").isEmpty()){
          ciMonthly.setMayEtaw(null);
        } else {
          ciMonthly.setMayEtaw(jsonData.optInt("mayEtaw"));
        }
        if(jsonData.optString("juneEtaw").isEmpty()){
          ciMonthly.setJuneEtaw(null);
        } else {
          ciMonthly.setJuneEtaw(jsonData.optInt("juneEtaw"));
        }
        if(jsonData.optString("julyEtaw").isEmpty()){
          ciMonthly.setJulyEtaw(null);
        } else {
          ciMonthly.setJulyEtaw(jsonData.optInt("julyEtaw"));
        }
        if(jsonData.optString("augustEtaw").isEmpty()){
          ciMonthly.setAugustEtaw(null);
        } else {
          ciMonthly.setAugustEtaw(jsonData.optInt("augustEtaw"));
        }
        if(jsonData.optString("septemberEtaw").isEmpty()){
          ciMonthly.setSeptemberEtaw(null);
        } else {
          ciMonthly.setSeptemberEtaw(jsonData.optInt("septemberEtaw"));
        }
        if(jsonData.optString("mayTw").isEmpty()){
          ciMonthly.setMayTw(null);
        }else{
          String num = jsonData.optString("mayTw").replaceAll("[,\\s]", "");
          ciMonthly.setMayTw(Double.parseDouble(num));
        }
        if(jsonData.optString("juneTw").isEmpty()){
          ciMonthly.setJuneTw(null);
        }else{
          String num = jsonData.optString("juneTw").replaceAll("[,\\s]", "");
          ciMonthly.setJuneTw(Double.parseDouble(num));
        }
        if(jsonData.optString("julyTw").isEmpty()){
          ciMonthly.setJulyTw(null);
        }else{
          String num = jsonData.optString("julyTw").replaceAll("[,\\s]", "");
          ciMonthly.setJulyTw(Double.parseDouble(num));
        }
        if(jsonData.optString("augustTw").isEmpty()){
          ciMonthly.setAugustTw(null);
        }else{
          String num = jsonData.optString("augustTw").replaceAll("[,\\s]", "");
          ciMonthly.setAugustTw(Double.parseDouble(num));
        }
        if(jsonData.optString("septemberTw").isEmpty()){
          ciMonthly.setSeptemberTw(null);
        }else{
          String num = jsonData.optString("septemberTw").replaceAll("[,\\s]", "");
          ciMonthly.setSeptemberTw(Double.parseDouble(num));
        }
        wcmf.edit(ciMonthly);

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

    @RequestMapping("/savecifield")
    public void saveCropIdlingField(@RequestParam("cropIdlingId") Integer cropIdlingId
                                    ,@RequestParam("fieldName") String fieldName
                                    ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((cropIdlingId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification Crop Idling id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);

        WtCropIdling cropIdling = wcif.find(cropIdlingId);
        if(cropIdling==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        Double actualAmount = null;
        if(!requestUtil.getString("actualAmount").isEmpty()){
          if (requestUtil.getInt("actualAmount") != 0){
            actualAmount = requestUtil.getDouble("actualAmount");
          }
          cropIdling.setActualAmount(actualAmount);
          cropIdling.setExportAmount(actualAmount);
        }

        String fieldValueString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        if (null != fieldName)switch (fieldName) {
          case "waterTransQuaCI":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setWaterTransQua(null);
            }else{
              cropIdling.setWaterTransQua(Double.parseDouble(fieldValueString));
              initCiMonthly(cropIdlingId, Double.parseDouble(fieldValueString));
            } break;
//          case "proTransferByCI":
//            if (StringUtils.isEmpty(fieldValueString)){
//              cropIdling.setProTransferByCI(null);
//            }else{
//              cropIdling.setProTransferByCI(Double.parseDouble(fieldValueString));
//          } break;
//          case "proTransferByCS":
//            if (StringUtils.isEmpty(fieldValueString)){
//              cropIdling.setProTransferByCS(null);
//            }else{
//              cropIdling.setProTransferByCS(Double.parseDouble(fieldValueString));
//          } break;
          case "currentFsAgency":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setCurrentFsAgency(null);
            }else{
              cropIdling.setCurrentFsAgency(Double.parseDouble(fieldValueString));
          } break;
          case "isResReleaseCI":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setIsResRelease(null);
            }else{
              cropIdling.setIsResRelease(requestUtil.getInt("fieldValue"));
            } break;
          case "mayTw":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setMayTw(null);
            }else{
              cropIdling.setMayTw(Double.parseDouble(fieldValueString));
            } break;
          case "juneTw":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setJuneTw(null);
            }else{
              cropIdling.setJuneTw(Double.parseDouble(fieldValueString));
            } break;
          case "julyTw":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setJulyTw(null);
            }else{
              cropIdling.setJulyTw(Double.parseDouble(fieldValueString));
            } break;
          case "augustTw":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setAugustTw(null);
            }else{
              cropIdling.setAugustTw(Double.parseDouble(fieldValueString));
            } break;
          case "septemberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              cropIdling.setSeptemberTw(null);
            }else{
              cropIdling.setSeptemberTw(Double.parseDouble(fieldValueString));
            } break;
        }

        // This field value is calculate auto
//        if ("proTransferByCI".equalsIgnoreCase(fieldName)
//              ||"proTransferByCS".equalsIgnoreCase(fieldName)){
//          if(requestUtil.getString("totalTransferAcr").isEmpty()){
//            cropIdling.setTotalTransferAcr(null);
//          }else{
//            String num = requestUtil.getString("totalTransferAcr").replaceAll("[,\\s]", "");
//            cropIdling.setTotalTransferAcr(Double.parseDouble(num));
//          }
//        }

        wcif.edit(cropIdling);
        jsonResponse.put("success", true);
      } catch (Exception exp) {
        jsonResponse.put("success", false).put("error", exp.getMessage());
        if ((exp instanceof MyException)) {
          jsonResponse.put("callback", ((MyException) exp).getCallback());
        }
      } finally {
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
      }
    }

    private void deleteCropIdling(WtTrans transRec) throws IOException, SQLException {
      WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtCiMonthlyFacade wcmf = (WtCiMonthlyFacade) appContext.getBean(WtCiMonthlyFacade.class.getSimpleName());
      WtCiCroptypeFacade wccf = (WtCiCroptypeFacade) appContext.getBean(WtCiCroptypeFacade.class.getSimpleName());
      WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());

      WtCropIdling cropIdling = transRec.getWtCropIdling();
      if (cropIdling != null) {
        WtCiMonthly ciMonthly = cropIdling.getWtCiMonthly();
        Collection<WtCiCroptype> ciCroptypeCollection = cropIdling.getCroptypeCollection();
        Collection<WtAttachment> attachmentCollection = cropIdling.getWtAttachmentCollection();
        Collection<WtAttachment> mapAttCollection = cropIdling.getWtMapAttCollection();

        if (ciCroptypeCollection.size()>0){
          for (WtCiCroptype ciCroptype:ciCroptypeCollection) {
            ciCroptype.setWtCropIdling(null);
            wccf.edit(ciCroptype);
//            wccf.remove(ciCroptype);
          }
//          cropIdling.setCroptypeCollection(null);
        }
        if(attachmentCollection.size()>0){
          for(WtAttachment attachment:attachmentCollection){
            attachment.setWtCropIdling(null);
            waf.edit(attachment);
          }
        }
        if(mapAttCollection.size()>0){
          for(WtAttachment mapAttachment:mapAttCollection){
            Collection<WtCropIdling> ciCollection = mapAttachment.getWtCropIdlingCollection();
            ciCollection.remove(cropIdling);
            waf.edit(mapAttachment);
          }
        }

        //Edit CropIdling Object
        cropIdling.setCroptypeCollection(null);
        cropIdling.setWtAttachmentCollection(null);
        cropIdling.setWtMapAttCollection(null);
//        cropIdling.setWtTrans(null);
        wcif.edit(cropIdling);
//        wcif.remove(cropIdling);
      }
    }

    private void deleteCropIdlingPost(WtCropIdling cropIdling) throws IOException, SQLException {
      WtCiCroptypeFacade wccf = (WtCiCroptypeFacade) appContext.getBean(WtCiCroptypeFacade.class.getSimpleName());

      if (cropIdling != null) {
        Collection<WtCiCroptype> ciCroptypeCollection = cropIdling.getCroptypeCollection();
        if (null!=ciCroptypeCollection && ciCroptypeCollection.size()>0){
          for (WtCiCroptype ciCroptype:ciCroptypeCollection) {
            ciCroptype.setWtCropIdling(null);
            wccf.remove(ciCroptype);
          }
        }
      }
    }

    /**
     * Save crop type ({@linkplain WtCiCroptype}) based on
     * wtCropIdlingId, wtCiCroptypeId and other properties of WtCiCroptype
     * set in the json request object.
     * <p>
     * If wtCropIdlingId is empty, then a new  entity is created and
     * the WtCropIdling association is set based on the wtCropIdlingId.
     * </p>
     * <p>
     * If wtCropIdlingId is not an empty string, then the entity is found based
     * on wtCiCroptypeId integer value and wtCropIdlingId is not used.
     * </p>
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/savecicroptype")
    public void saveCiCropType(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtCiCroptypeFacade cctf = (WtCiCroptypeFacade)appContext.getBean(WtCiCroptypeFacade.class.getSimpleName());
        WtCropIdlingFacade cif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCiCroptype newCiCropType;
        if(request.getParameter("wtCiCroptypeId").isEmpty()){
          newCiCropType = new WtCiCroptype();
          newCiCropType.setWtCropIdling(cif.find(Integer.parseInt(request.getParameter("wtCropIdlingId"))));
          cctf.create(newCiCropType);
        } else {
          newCiCropType = cctf.find(Integer.parseInt(request.getParameter("wtCiCroptypeId")));
        }
        newCiCropType.loadProperties(request);
//        cct.setCropType(request.getParameter("cropType"));
//        if(request.getParameter("proTransferByCi").isEmpty()){
//          cct.setProTransferByCi(null);
//        } else {
//          String num = request.getParameter("proTransferByCi").replaceAll("[,\\s]", "");
//          cct.setProTransferByCi(Double.parseDouble(num));
//        }
//
        cctf.edit(newCiCropType);
        jsonResponse = new JSONObject(newCiCropType.toMap());
        jsonResponse.put("success", true);
      }catch(MyException | BeansException | NumberFormatException | JSONException ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }


    /**
     * Removes the crop idling crop type ({@linkplan WtCiCroptype}) based
     * on the crop idling crop type id. If the cicroptype does not exists
     * then an Exception is thrown which will produces a success = false
     * value in the response.
     * @param request wtCiCroptypeId: Integer.
     * @param response success:Boolean, error:String.
     * @throws IOException
     */
    @RequestMapping("/removecicroptype")
    public void removeCiCropType(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtCiCroptypeFacade cctf = (WtCiCroptypeFacade)appContext.getBean(WtCiCroptypeFacade.class.getSimpleName());
        if(!request.getParameter("wtCiCroptypeId").isEmpty()){
          WtCiCroptype cct = cctf.find(Integer.parseInt(request.getParameter("wtCiCroptypeId")));
          cctf.remove(cct);
          jsonResponse.put("data",cct.toMap());
        }
        jsonResponse.put("success",true);
      }catch(MyException | BeansException | NumberFormatException | JSONException ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
      finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    /**
     *
     * @param cropIdlingId
     * @param fieldName
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/savecimonthly")
    public void saveCiMonthly(@RequestParam("cropIdlingId") Integer cropIdlingId
                             ,@RequestParam("fieldName") String fieldName
                             ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((cropIdlingId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification cropIdling id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
        WtCiMonthlyFacade wcmf = (WtCiMonthlyFacade)appContext.getBean(WtCiMonthlyFacade.class.getSimpleName());

        WtCropIdling cropIdling = wcif.find(cropIdlingId);
        WtCiMonthly ciMonthly = cropIdling.getWtCiMonthly();
        if(cropIdling==null || ciMonthly == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        Double twTotal = null;
        Double mayTw = null;
        Double juneTw = null;
        Double julyTw = null;
        Double augustTw = null;
        Double septemberTw = null;
        if(!requestUtil.getString("twTotalVal").isEmpty()){
          twTotal = requestUtil.getDouble("twTotalVal");
        }

        if(null != fieldName)switch (fieldName) {
          case "mayEtaw":
            int mayEtaw = requestUtil.getInt("fieldValue");
            ciMonthly.setMayEtaw(mayEtaw);
            if(twTotal != null){
              mayTw = Math.round(twTotal * mayEtaw)/100.0;
              ciMonthly.setMayTw(mayTw);
            } break;
          case "juneEtaw":
            int juneEtaw = requestUtil.getInt("fieldValue");
            ciMonthly.setJuneEtaw(juneEtaw);
            if(twTotal != null){
              juneTw = Math.round(twTotal * juneEtaw)/100.0;
              ciMonthly.setJuneTw(juneTw);
            } break;
          case "julyEtaw":
            int julyEtaw = requestUtil.getInt("fieldValue");
            ciMonthly.setJulyEtaw(julyEtaw);
            if(twTotal != null){
              julyTw = Math.round(twTotal * julyEtaw)/100.0;
              ciMonthly.setJulyTw(julyTw);
          } break;
          case "augustEtaw":
            int augustEtaw = requestUtil.getInt("fieldValue");
            ciMonthly.setAugustEtaw(augustEtaw);
            if(twTotal != null){
              augustTw = Math.round(twTotal * augustEtaw)/100.0;
              ciMonthly.setAugustTw(augustTw);
          } break;
          case "septemberEtaw":
            int septemberEtaw = requestUtil.getInt("fieldValue");
            ciMonthly.setSeptemberEtaw(septemberEtaw);
            if(twTotal != null){
              septemberTw = Math.round(twTotal * septemberEtaw)/100.0;
              ciMonthly.setSeptemberTw(septemberTw);
          } break;
          case "mayTw":
            ciMonthly.setMayTw(requestUtil.getDouble("fieldValue"));
            break;
          case "juneTw":
            ciMonthly.setJuneTw(requestUtil.getDouble("fieldValue"));
            break;
          case "julyTw":
            ciMonthly.setJulyTw(requestUtil.getDouble("fieldValue"));
            break;
          case "augustTw":
            ciMonthly.setAugustTw(requestUtil.getDouble("fieldValue"));
            break;
          case "septemberTw":
            ciMonthly.setSeptemberTw(requestUtil.getDouble("fieldValue"));
            break;
        }

        wcmf.edit(ciMonthly);
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

    public WtCiMonthly initCiMonthly(Integer cropIdlingId, Double wtAmount) throws Exception{
      WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtCiMonthlyFacade wcmf = (WtCiMonthlyFacade)appContext.getBean(WtCiMonthlyFacade.class.getSimpleName());
      Double mayTw = null;
      Double juneTw = null;
      Double julyTw = null;
      Double augustTw = null;
      Double septemberTw = null;

      try{
        WtCropIdling cropIdling = wcif.find(cropIdlingId);
        if(cropIdling==null){
          throw new Exception("The cropIdling does not exist!");
        }
        WtCiMonthly ciMonthly = cropIdling.getWtCiMonthly();
        if(ciMonthly == null){
          ciMonthly = new WtCiMonthly();
          ciMonthly.setWtCropIdling(cropIdling);
          wcmf.create(ciMonthly);
        }

        Integer mayEtaw = 15;
        Integer juneEtaw = 22;
        Integer julyEtaw = 24;
        Integer augustEtaw = 24;
        Integer septemberEtaw = 15;
        if(wtAmount != null){
          mayTw = Math.round(wtAmount * mayEtaw)/100.0;
          juneTw = Math.round(wtAmount * juneEtaw)/100.0;
          julyTw = Math.round(wtAmount * julyEtaw)/100.0;
          augustTw = Math.round(wtAmount * augustEtaw)/100.0;
          septemberTw = Math.round(wtAmount * septemberEtaw)/100.0;
        }

        ciMonthly.setMayEtaw(mayEtaw);
        ciMonthly.setJuneEtaw(juneEtaw);
        ciMonthly.setJulyEtaw(julyEtaw);
        ciMonthly.setAugustEtaw(augustEtaw);
        ciMonthly.setSeptemberEtaw(septemberEtaw);
        ciMonthly.setMayTw(mayTw);
        ciMonthly.setJuneTw(juneTw);
        ciMonthly.setJulyTw(julyTw);
        ciMonthly.setAugustTw(augustTw);
        ciMonthly.setSeptemberTw(septemberTw);

        wcmf.edit(ciMonthly);
        return ciMonthly;
      } catch (Exception exp) {
        throw new Exception("In ProposalController method initCiMonthly!");
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Reservoir Part">
    private void createReservoir(WtTrans transRec) throws IOException, SQLException{
        WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtReservoir reservoir = transRec.getWtReservoir();
        if(reservoir == null){
          reservoir = new WtReservoir();
          wrvf.create(reservoir);
          reservoir.setWtTrans(transRec);
          wrvf.edit(reservoir);
          transRec.setWtReservoir(reservoir);
        }
    }

    private void saveReservoir(JSONObject jsonData, WtTrans transRec) throws IOException, SQLException{
        WtReservoirFacade wrf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtReservoir reservoir = transRec.getWtReservoir();
        if(reservoir == null){
          reservoir = new WtReservoir();
          wrf.create(reservoir);
        }

        if(StringUtils.isEmpty(jsonData.optString("waterTransQuaRV"))){
            reservoir.setWaterTransQua(null);
        }else{
//            reservoir.setWaterTransQua(jsonData.optInt("waterTransQuaRV"));
            String num = jsonData.optString("waterTransQuaRV").replaceAll("[,\\s]", "");
            reservoir.setWaterTransQua(Double.parseDouble(num));
        }
        if(StringUtils.isEmpty(jsonData.optString("topAllowStorage"))){
            reservoir.setTopAllowStorage(null);
        }else{
//            reservoir.setTopAllowStorage(jsonData.optInt("topAllowStorage"));
            String num = jsonData.optString("topAllowStorage").replaceAll("[,\\s]", "");
            reservoir.setTopAllowStorage(Double.parseDouble(num));
        }
        if(jsonData.optString("targetStorage").isEmpty()){
            reservoir.setTargetStorage(null);
        }else{
//            reservoir.setTargetStorage(jsonData.optInt("targetStorage"));
            String num = jsonData.optString("targetStorage").replaceAll("[,\\s]", "");
            reservoir.setTargetStorage(Double.parseDouble(num));
        }
        if(jsonData.optString("locationLat").isEmpty()){
            reservoir.setLocationLat(null);
        }else{
            reservoir.setLocationLat(jsonData.optDouble("locationLat"));
        }
        if(jsonData.optString("locationLong").isEmpty()){
            reservoir.setLocationLong(null);
        }else{
            reservoir.setLocationLong(jsonData.optDouble("locationLong"));
        }
        if(jsonData.optString("authOperator").isEmpty()){
            reservoir.setAuthOperator(null);
        }else{
            reservoir.setAuthOperator(jsonData.optString("authOperator"));
        }
        if(jsonData.optString("isSellerAuth").isEmpty()){
            reservoir.setIsSellerAuth(null);
        }else{
            reservoir.setIsSellerAuth(jsonData.optInt("isSellerAuth"));
        }
        //Saving the Purpose of Reservoir
        WtPurposeFacade pf = (WtPurposeFacade)appContext.getBean(WtPurposeFacade.class.getSimpleName());
        String purposeIdList[] = StringUtils.commaDelimitedListToStringArray(jsonData.optString("purposeId"));

        if(purposeIdList.length > 0){
          for(String p : purposeIdList){
            WtPurpose purpose = pf.find(Integer.parseInt(p));
            if(purpose != null){
              Collection<WtReservoir> reservoirCollection = purpose.getWtReservoirCollection();
              if(!reservoirCollection.contains(reservoir)){
                reservoirCollection.add(reservoir);
                purpose.setWtReservoirCollection(reservoirCollection);
                pf.edit(purpose);
              }
            }
          }
        }
        reservoir.setWtTrans(transRec);
        wrf.edit(reservoir);
        transRec.setWtReservoir(reservoir);
    }

    private void deleteReservoir(WtTrans transRec) throws IOException, SQLException{
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
      WtRvTarstorFacade wrtf = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());
      WtPurposeFacade wpf = (WtPurposeFacade)appContext.getBean(WtPurposeFacade.class.getSimpleName());
      WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());

      WtReservoir reservoir = transRec.getWtReservoir();
      if (reservoir != null){
        Collection<WtRvTarstor> rvTarstorCollection = reservoir.getWtRvTarstorCollection();
        Collection<WtPurpose> purposeCollection = reservoir.getWtPurposeCollection();
        Collection<WtAttachment> attachmentCollection = reservoir.getWtAttachmentCollection();

        if (rvTarstorCollection.size()>0){
          for (WtRvTarstor rvTarstor:rvTarstorCollection) {
            rvTarstor.setWtReservoir(null);
            wrtf.edit(rvTarstor);
//            wrtf.remove(rvTarstor);
          }
        }

        if (purposeCollection.size()>0){
          for (WtPurpose purpose:purposeCollection) {
            Collection<WtReservoir> rvCollection = purpose.getWtReservoirCollection();
            rvCollection.remove(reservoir);
            wpf.edit(purpose);
          }
        }

        if(attachmentCollection.size()>0){
          for(WtAttachment attachment:attachmentCollection){
            attachment.setWtReservoir(null);
            waf.edit(attachment);
          }
        }

        reservoir.setWtPurposeCollection(null);
        reservoir.setWtRvTarstorCollection(null);
        reservoir.setWtAttachmentCollection(null);
//        reservoir.setWtTrans(null);
//        transRec.setWtReservoir(null);
        wrvf.edit(reservoir);
//        wtf.getEntityManager().refresh(transRec);
//        transRec = wtf.find(transRec.getWtTransId());
//        wrvf.remove(reservoir);
//        wtf.edit(transRec);
      }
    }
    
    private void deleteReservoirPost(WtReservoir reservoir) throws IOException, SQLException{
      WtRvTarstorFacade wrtf = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());
      
      //Remove Revior Tarstorage Collection
      Collection<WtRvTarstor> rvTarstorCollection = reservoir.getWtRvTarstorCollection();       
      if (null!=rvTarstorCollection && rvTarstorCollection.size()>0){
        for (WtRvTarstor rvTarstor:rvTarstorCollection) {
          rvTarstor.setWtReservoir(null);
          wrtf.remove(rvTarstor);
        }
      }      
    }

    @RequestMapping("/savervfield")
    public void saveReservoirField(@RequestParam("reservoirId") Integer reservoirId
          ,@RequestParam("fieldName") String fieldName
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((reservoirId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification Reservoir id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());

        WtReservoir reservoir = wrvf.find(reservoirId);
        if(reservoir==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        String fieldValueString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        Double actualAmount = null;
        if(!requestUtil.getString("actualAmount").isEmpty()){
          if (requestUtil.getInt("actualAmount") != 0){
            actualAmount = requestUtil.getDouble("actualAmount");
          }
          reservoir.setActualAmount(actualAmount);
          reservoir.setExportAmount(actualAmount);
        }
        if (null != fieldName)switch (fieldName) {
          case "waterTransQuaRV":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setWaterTransQua(null);
            }else{
              reservoir.setWaterTransQua(Double.parseDouble(fieldValueString));
            } break;
          case "topAllowStorage":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setTopAllowStorage(null);
            }else{
              reservoir.setTopAllowStorage(Double.parseDouble(fieldValueString));
          } break;
          case "targetStorage":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setTargetStorage(null);
            }else{
              reservoir.setTargetStorage(Double.parseDouble(fieldValueString));
          } break;
          case "locationLat":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setLocationLat(null);
            }else{
              reservoir.setLocationLat(Double.parseDouble(fieldValueString));
          } break;
          case "locationLong":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setLocationLong(null);
            }else{
              reservoir.setLocationLong(Double.parseDouble(fieldValueString));
            } break;
          case "isSellerAuth":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setIsSellerAuth(null);
            }else{
              reservoir.setIsSellerAuth(requestUtil.getInt("fieldValue"));
            } break;
          case "authOperator":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setAuthOperator(null);
            }else{
              reservoir.setAuthOperator(fieldValueString);
            } break;
          case "aprilTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setAprilTw(null);
            }else{
              reservoir.setAprilTw(Double.parseDouble(fieldValueString));
            } break;
          case "mayTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setMayTw(null);
            }else{
              reservoir.setMayTw(Double.parseDouble(fieldValueString));
            } break;
          case "juneTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setJuneTw(null);
            }else{
              reservoir.setJuneTw(Double.parseDouble(fieldValueString));
            } break;
          case "julyTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setJulyTw(null);
            }else{
              reservoir.setJulyTw(Double.parseDouble(fieldValueString));
            } break;
          case "augustTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setAugustTw(null);
            }else{
              reservoir.setAugustTw(Double.parseDouble(fieldValueString));
            } break;
          case "septemberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setSeptemberTw(null);
            }else{
              reservoir.setSeptemberTw(Double.parseDouble(fieldValueString));
            } break;
          case "octoberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setOctoberTw(null);
            }else{
              reservoir.setOctoberTw(Double.parseDouble(fieldValueString));
            } break;
          case "novemberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              reservoir.setNovemberTw(null);
            }else{
              reservoir.setNovemberTw(Double.parseDouble(fieldValueString));
            } break;
        }

        wrvf.edit(reservoir);
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

    @RequestMapping("/savervpurpose")
    public void saveReservoirPurpose(@RequestParam("reservoirId") Integer reservoirId
          ,@RequestParam("purposeId") Integer purposeId
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((reservoirId == null) || (purposeId == null)) {
          throw new Exception("The modification Reservoir id and Propurpose id cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtPurposeFacade wpf = (WtPurposeFacade)appContext.getBean(WtPurposeFacade.class.getSimpleName());

        WtReservoir reservoir = wrvf.find(reservoirId);
        WtPurpose purpose = wpf.find(purposeId);
        if(reservoir==null || purpose==null ){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        Collection<WtReservoir> reservoirCollection = purpose.getWtReservoirCollection();
        if(!reservoirCollection.contains(reservoir)){
          reservoirCollection.add(reservoir);
          purpose.setWtReservoirCollection(reservoirCollection);
          wpf.edit(purpose);
        }

//        Collection<WtPurpose> purposeCollection = reservoir.getWtPurposeCollection();
//        String option = requestUtil.getString("option");
//        if (purposeCollection == null){
//          purposeCollection = new ArrayList();
//        }
//        if ("add".equalsIgnoreCase(option)){
//          purposeCollection.add(purpose);
//        } else if("delete".equalsIgnoreCase(option)){
//          purposeCollection.remove(purpose);
//        }
//        reservoir.setWtPurposeCollection(purposeCollection);
        wrvf.edit(reservoir);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get Purpose List">
    @RequestMapping("/purpose")
    public void purposeSearch(@RequestParam(value="q",required=false) String keywords
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      try {
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        List<WtPurpose> c = LookupDataContext.getInstance().getPurposeReservoir();
        JSONArray purposeList = new JSONArray();
        JSONObject temp;
        for(WtPurpose p : c){
          temp = new JSONObject(p.toMap());
          purposeList.put(temp);
        }
        jsonResponse
                .put("data", c.size() < 1 ? "" : purposeList)
                .put("success", true);
      } catch (Exception exp) {
        jsonResponse.put("success",false).put("error",exp.getMessage());
        if (exp instanceof MyException){
          jsonResponse.put("callback", ((MyException)exp).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Remove Proposal">
    @RequestMapping("/removeproposal")
    public void removeProposal(@RequestParam("wtTransId") Integer wtTransId, HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      try {
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("", "ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtTransFacade pf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans trans = pf.find(wtTransId);
        if (trans == null) {
          throw new Exception("Unable to find the record with wtTransId: " + wtTransId);
        }
        trans.setIsActive(0);
        pf.edit(trans);
        jsonResponse.put("success", true).put("data", trans.toMap());
      } catch (Exception ex) {
        jsonResponse.put("error", ex.getMessage()).put("success", false);
        if (ex instanceof MyException) {
          jsonResponse.put("callback", ((MyException) ex).getCallback());
        }
      } finally {
        response.getWriter().write(jsonResponse.toString());
      }
    }
  //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Remove Purpose">
    @RequestMapping("/removepurpose")
    public void removePurpose(@RequestParam("purposeId") Integer purposeId
            , @RequestParam("reservoirId") Integer reservoirId
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      try{
        if (!AuthenticationController.IsLoggedIn(request)) {
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtPurposeFacade pf = (WtPurposeFacade)appContext.getBean(WtPurposeFacade.class.getSimpleName());
        WtReservoirFacade rf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtReservoir reservoir = rf.find(reservoirId);
        WtPurpose purpose = pf.find(purposeId);
        Collection<WtPurpose> purposes = reservoir.getWtPurposeCollection();

        if(purposes.contains(purpose)){
          Collection<WtReservoir> rs = purpose.getWtReservoirCollection();
          if(rs.size()>0){
            for(WtReservoir r : rs){
              if(Objects.equals(r, reservoir)){
                rs.remove(r);
                break;
              }
            }
          }
          purpose.setWtReservoirCollection(rs);
          pf.edit(purpose);
        }

        jsonResponse.put("success", true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success", false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
  //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save Target Storage">
    @RequestMapping("/savestorage")
    public void saveTargetStorage(HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!AuthenticationController.IsLoggedIn((request))){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        Integer reservoirId = requestUtil.getInt("wtReservoirId");
        Integer wtTransId = requestUtil.getInt("wtTransId");
        WtReservoirFacade rf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtTransFacade tf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtReservoir res = rf.find(reservoirId);
        if(res == null){
          WtTrans trans = tf.find(wtTransId);
          res = trans.getWtReservoir();
        }
        WtRvTarstorFacade storageFacade = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());
        WtRvTarstor storage = new WtRvTarstor();
        storage.loadProperties(request);
        if(!requestUtil.getString("storageLocationLat").isEmpty()){
        storage.setLocationLat(requestUtil.getDouble("storageLocationLat"));
        }
        if(!requestUtil.getString("storageLocationLong").isEmpty()){
        storage.setLocationLong(requestUtil.getDouble("storageLocationLong"));
        }
        storage.setWtReservoir(res);
        if(requestUtil.getString("wtRvTarstorId").isEmpty()){
          storageFacade.create(storage);
        }
        storageFacade.edit(storage);

        jsonResponse.put("data",new JSONObject(storage.toMap()));
        jsonResponse.put("success",true);
      }
      catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Remove Target Storage">
    @RequestMapping("/removeStorage")
    public void removeTargetStorage(@RequestParam("wtReservoirId") Integer wtReservoirId
            , @RequestParam("wtRvTarstorId") Integer wtRvTarstorId
            , HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!AuthenticationController.IsLoggedIn((request))){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtReservoirFacade rf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtReservoir res = rf.find(wtReservoirId);
        if(res == null){
          throw new Exception("Unable to retreive the reservoir data");
        }
        WtRvTarstorFacade storageFacade = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());
        WtRvTarstor storage = storageFacade.find(wtRvTarstorId);
        if(!Objects.equals(res, storage.getWtReservoir())){
          throw new Exception("Unable to remove the target storage.");
        }
        storage.setWtReservoir(null);
        storageFacade.remove(storage);

        jsonResponse.put("data",new JSONObject(storage.toMap()));
        jsonResponse.put("success",true);
      }
      catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Edit Target Storage">
    @RequestMapping("/editStorage")
    public void editTargetStorage(@RequestParam("wtReservoirId") Integer wtReservoirId
            , @RequestParam("wtRvTarstorId") Integer wtRvTarstorId
            , HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!AuthenticationController.IsLoggedIn((request))){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReservoirFacade rf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
        WtReservoir res = rf.find(wtReservoirId);
        if(res == null){
          throw new Exception("Unable to retreive the reservoir data");
        }
        WtRvTarstorFacade storageFacade = (WtRvTarstorFacade)appContext.getBean(WtRvTarstorFacade.class.getSimpleName());
        WtRvTarstor storage = storageFacade.find(wtRvTarstorId);
        if(!Objects.equals(res, storage.getWtReservoir())){
          throw new Exception("Unable to remove the target storage.");
        }
        storage.setWtReservoir(res);
        storage.loadProperties(request);
        storage.setLocationLat(requestUtil.getDouble("storageLocationLat"));
        storage.setLocationLong(requestUtil.getDouble("storageLocationLong"));
        storage.setWtReservoir(res);
        storageFacade.edit(storage);

        jsonResponse.put("data",new JSONObject(storage.toMap()));
        jsonResponse.put("success",false);
      }
      catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Groundwater Part">
    public void createGroundwater(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
        WtGroundwaterFacade wgf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtGroundwater gw = new WtGroundwater();
        wtf.set("wtTransId", jsonData.optInt("wtTransId"));
        WtTrans trans = wtf.find();
        gw.loadProperties(jsonData.getJSONMap());
        trans.setWtGroundwater(gw);
        wtf.edit(trans);

        jsonResponse.put("data",trans.getWtGroundwater().getWtGroundwaterId());
        response.getWriter().write(jsonResponse.toString());

      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }
    }

    private void createGroundwater(WtTrans transRec) throws IOException, SQLException{
        WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtGroundwater groundwater = transRec.getWtGroundwater();
        if(groundwater == null){
          groundwater = new WtGroundwater();
          wgwf.create(groundwater);
          groundwater.setWtTrans(transRec);
          wgwf.edit(groundwater);
          transRec.setWtGroundwater(groundwater);
        }
    }

    private void saveGroundwater(JSONObject jsonData, WtTrans transRec) throws IOException, SQLException{
        WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtGroundwater groundwater = transRec.getWtGroundwater();
        if(groundwater == null){
          groundwater = new WtGroundwater();
          wgwf.create(groundwater);
        }

        if(jsonData.optString("pumpingWellsNumber").isEmpty()){
            groundwater.setPumpingWellsNumber(null);
        }else{
            String num = jsonData.optString("pumpingWellsNumber").replaceAll("[,\\s]", "");
            groundwater.setPumpingWellsNumber(Integer.parseInt(num));
        }
        if(jsonData.optString("monitoringWellsNumber").isEmpty()){
            groundwater.setMonitoringWellsNumber(null);
        }else{
            String num = jsonData.optString("monitoringWellsNumber").replaceAll("[,\\s]", "");
            groundwater.setMonitoringWellsNumber(Integer.parseInt(num));
        }
        if(jsonData.optString("totalPumping").isEmpty()){
            groundwater.setTotalPumping(null);
        }else{
            String num = jsonData.optString("totalPumping").replaceAll("[,\\s]", "");
            groundwater.setTotalPumping(Double.parseDouble(num));
        }
        if(jsonData.optString("basePumping").isEmpty()){
            groundwater.setBasePumping(null);
        }else{
            String num = jsonData.optString("basePumping").replaceAll("[,\\s]", "");
            groundwater.setBasePumping(Double.parseDouble(num));
        }
        if(jsonData.optString("grossTransPumping").isEmpty()){
            groundwater.setGrossTransPumping(null);
        }else{
            String num = jsonData.optString("grossTransPumping").replaceAll("[,\\s]", "");
            groundwater.setGrossTransPumping(Double.parseDouble(num));
        }
        if(jsonData.optString("depletionFactor").isEmpty()){
            groundwater.setDepletionFactor(null);
        }else{
            String num = jsonData.optString("depletionFactor").replaceAll("[,\\s]", "");
            groundwater.setDepletionFactor(Double.parseDouble(num));
        }
        if(jsonData.optString("streamDepletion").isEmpty()){
            groundwater.setStreamDepletion(null);
        }else{
            String num = jsonData.optString("streamDepletion").replaceAll("[,\\s]", "");
            groundwater.setStreamDepletion(Double.parseDouble(num));
        }
        if(jsonData.optString("netTransWater").isEmpty()){
            groundwater.setNetTransWater(null);
        }else{
            String num = jsonData.optString("netTransWater").replaceAll("[,\\s]", "");
            groundwater.setNetTransWater(Double.parseDouble(num));
        }
        if(jsonData.optString("isResRelease").isEmpty()){
            groundwater.setIsResRelease(null);
        }else{
            groundwater.setIsResRelease(jsonData.optInt("isResRelease"));
        }
        groundwater.setWtTrans(transRec);
        wgwf.edit(groundwater);
        transRec.setWtGroundwater(groundwater);

        //Create new groundwater monthly records or edit information
        WtGwMonthlyFacade wgmf = (WtGwMonthlyFacade)appContext.getBean(WtGwMonthlyFacade.class.getSimpleName());
//        Collection<WtGwMonthly> gwMonthlyCollection = groundwater.getWtGwMonthlyCollection();
        for(int i=1; i<=12; i++){
            String measurementDateId = "measurementDate"+i;
            String proposedPumpingId = "proposedPumping"+i;
            String baselinePumpingId = "baselinePumping"+i;
            String grossTransPumpingId = "grossTransPumping"+i;
            String streamDepletionId = "streamDepletion"+i;
            String netTransWaterId = "netTransWater"+i;
            String pump = "";

            WtGwMonthly gwm = new WtGwMonthly();
            List<WtGwMonthly> gwmList = wgmf.select("SELECT * from WT_GW_MONTHLY where GW_MONTH="+i+" and WT_GROUNDWATER_ID="+groundwater.getWtGroundwaterId(),com.gei.entities.WtGwMonthly.class);
            if (gwmList.isEmpty()){
                // If there is no propesed pumping, ignore this record
                if(jsonData.optString(proposedPumpingId).isEmpty()){
                    continue;
                }
                gwm.setGwMonth(i);
                wgmf.create(gwm);
                gwm.setWtGroundwater(groundwater);
            } else {
                gwm = gwmList.get(0);
            }
            if(jsonData.optString(measurementDateId).isEmpty()){
                gwm.setMeasurementDate(null);
            } else {
                gwm.setMeasurementDate(jsonData.optString(measurementDateId));
            }
            if(jsonData.optString(proposedPumpingId).isEmpty()){
                gwm.setProposedPumping(null);
            } else {
                pump = jsonData.optString(proposedPumpingId).replaceAll("[,\\s]", "");
                gwm.setProposedPumping(Double.parseDouble(pump));
            }
            if(jsonData.optString(baselinePumpingId).isEmpty()){
                gwm.setBaselinePumping(null);
            } else {
                pump = jsonData.optString(baselinePumpingId).replaceAll("[,\\s]", "");
                gwm.setBaselinePumping(Double.parseDouble(pump));
            }
            if(jsonData.optString(grossTransPumpingId).isEmpty()){
                gwm.setGrossTransPumping(null);
            } else {
                pump = jsonData.optString(grossTransPumpingId).replaceAll("[,\\s]", "");
                gwm.setGrossTransPumping(Double.parseDouble(pump));
            }
            if(jsonData.optString(streamDepletionId).isEmpty()){
                gwm.setStreamDepletion(null);
            } else {
                pump = jsonData.optString(streamDepletionId).replaceAll("[,\\s]", "");
                gwm.setStreamDepletion(Double.parseDouble(pump));
            }
            if(jsonData.optString(netTransWaterId).isEmpty()){
                gwm.setNetTransWater(null);
            } else {
                pump = jsonData.optString(netTransWaterId).replaceAll("[,\\s]", "");
                gwm.setNetTransWater(Double.parseDouble(pump));
            }
            wgmf.edit(gwm);
        }
    }

    @RequestMapping("/savegwfield")
    public void saveGroundwaterField(@RequestParam("groundwaterId") Integer groundwaterId
          ,@RequestParam("fieldName") String fieldName
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((groundwaterId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification Groundwater id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());

        WtGroundwater groundwater = wgwf.find(groundwaterId);
        if(groundwater==null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        String fieldValueString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        Double actualAmount = groundwater.getActualAmount();
        Double exportAmount = groundwater.getExportAmount();
        Double deletionFactor = groundwater.getDepletionFactor();
        if(!requestUtil.getString("actualAmount").isEmpty()){
          if (requestUtil.getInt("actualAmount") != 0){
            actualAmount = requestUtil.getDouble("actualAmount");
          }
          groundwater.setActualAmount(actualAmount);
        }
        if (null != fieldName)switch (fieldName) {
          case "pumpingWellsNumber":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setPumpingWellsNumber(null);
            }else{
              groundwater.setPumpingWellsNumber(Integer.parseInt(fieldValueString));
            } break;
          case "monitoringWellsNumber":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setMonitoringWellsNumber(null);
            }else{
              groundwater.setMonitoringWellsNumber(Integer.parseInt(fieldValueString));
            } break;
          case "totalPumping":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setTotalPumping(null);
            }else{
              groundwater.setTotalPumping(Double.parseDouble(fieldValueString));
            } break;
          case "basePumping":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setBasePumping(null);
            }else{
              groundwater.setBasePumping(Double.parseDouble(fieldValueString));
            } break;
          case "depletionFactor":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setDepletionFactor(null);
            }else{
              deletionFactor = Double.parseDouble(fieldValueString);
              groundwater.setDepletionFactor(Double.parseDouble(fieldValueString));
            } break;
          case "aprilTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setAprilTw(null);
            }else{
              groundwater.setAprilTw(Double.parseDouble(fieldValueString));
            } break;
          case "mayTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setMayTw(null);
            }else{
              groundwater.setMayTw(Double.parseDouble(fieldValueString));
            } break;
          case "juneTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setJuneTw(null);
            }else{
              groundwater.setJuneTw(Double.parseDouble(fieldValueString));
            } break;
          case "julyTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setJulyTw(null);
            }else{
              groundwater.setJulyTw(Double.parseDouble(fieldValueString));
            } break;
          case "augustTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setAugustTw(null);
            }else{
              groundwater.setAugustTw(Double.parseDouble(fieldValueString));
            } break;
          case "septemberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setSeptemberTw(null);
            }else{
              groundwater.setSeptemberTw(Double.parseDouble(fieldValueString));
            } break;
          case "octoberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setOctoberTw(null);
            }else{
              groundwater.setOctoberTw(Double.parseDouble(fieldValueString));
            } break;
          case "novemberTw":
            if (StringUtils.isEmpty(fieldValueString)){
              groundwater.setNovemberTw(null);
            }else{
              groundwater.setNovemberTw(Double.parseDouble(fieldValueString));
            } break;
        }

        // This field value is calculate auto
        if ("totalPumping".equalsIgnoreCase(fieldName)
              ||"basePumping".equalsIgnoreCase(fieldName)
                ||"depletionFactor".equalsIgnoreCase(fieldName)){
          if(requestUtil.getString("grossTransPumping").isEmpty()){
            groundwater.setGrossTransPumping(null);
          }else{
            String num = requestUtil.getString("grossTransPumping").replaceAll("[,\\s]", "");
            groundwater.setGrossTransPumping(Double.parseDouble(num));
          }
          if(requestUtil.getString("streamDepletion").isEmpty()){
            groundwater.setStreamDepletion(null);
          }else{
            String num = requestUtil.getString("streamDepletion").replaceAll("[,\\s]", "");
            groundwater.setStreamDepletion(Double.parseDouble(num));
          }
          if(requestUtil.getString("netTransWater").isEmpty()){
            groundwater.setNetTransWater(null);
          }else{
            String num = requestUtil.getString("netTransWater").replaceAll("[,\\s]", "");
            groundwater.setNetTransWater(Double.parseDouble(num));
          }
        }

        // Calculate Export Amount
        if (actualAmount != null && deletionFactor != null){
          exportAmount = actualAmount * (1-deletionFactor/100);
          groundwater.setExportAmount(exportAmount);
        }

        wgwf.edit(groundwater);
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

    @RequestMapping("/savegwmonthly")
    public void saveGwMonthly(@RequestParam("groundwaterId") Integer groundwaterId
          ,@RequestParam("month") Integer month
          ,@RequestParam("fieldName") String fieldName
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      JSONObject jsonResponse = new JSONObject();
      LoggedInCheck(request, response);
      try{
        if ((groundwaterId == null) || (StringUtils.isEmpty(fieldName))) {
          throw new Exception("The modification groundwater id and field name cannot be unassigned!");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtGwMonthlyFacade wgmf = (WtGwMonthlyFacade)appContext.getBean(WtGwMonthlyFacade.class.getSimpleName());

        WtGroundwater groundwater = wgwf.find(groundwaterId);
        if(groundwater==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        WtGwMonthly gwm = new WtGwMonthly();
        List<WtGwMonthly> gwmList = wgmf.select("SELECT * from WT_GW_MONTHLY where GW_MONTH="+month+" and WT_GROUNDWATER_ID="+groundwater.getWtGroundwaterId(),com.gei.entities.WtGwMonthly.class);
        if (gwmList.isEmpty()){
          gwm.setGwMonth(month);
          wgmf.create(gwm);
          gwm.setWtGroundwater(groundwater);
        } else {
          gwm = gwmList.get(0);
        }

        String fieldValueString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
        if(null != fieldName)switch (fieldName) {
          case "proposedPumping":
            if (StringUtils.isEmpty(fieldValueString)){
              gwm.setProposedPumping(null);
            }else{
              gwm.setProposedPumping(Double.parseDouble(fieldValueString));
            } break;
          case "baselinePumping":
            if (StringUtils.isEmpty(fieldValueString)){
              gwm.setBaselinePumping(null);
            }else{
              gwm.setBaselinePumping(Double.parseDouble(fieldValueString));
            } break;
          case "grossTransPumping":
            if (StringUtils.isEmpty(fieldValueString)){
              gwm.setGrossTransPumping(null);
            }else{
              gwm.setGrossTransPumping(Double.parseDouble(fieldValueString));
            } break;
          case "streamDepletion":
            if (StringUtils.isEmpty(fieldValueString)){
              gwm.setStreamDepletion(null);
            }else{
              gwm.setStreamDepletion(Double.parseDouble(fieldValueString));
            } break;
          case "netTransWater":
            if (StringUtils.isEmpty(fieldValueString)){
              gwm.setNetTransWater(null);
            }else{
              gwm.setNetTransWater(Double.parseDouble(fieldValueString));
            } break;
        }

        // This field value is calculate auto
        if ("proposedPumping".equalsIgnoreCase(fieldName)
              ||"baselinePumping".equalsIgnoreCase(fieldName)){
          if(requestUtil.getString("grossTransPumping").isEmpty()){
            gwm.setGrossTransPumping(null);
          }else{
            String num = requestUtil.getString("grossTransPumping").replaceAll("[,\\s]", "");
            gwm.setGrossTransPumping(Double.parseDouble(num));
          }
          if(requestUtil.getString("streamDepletion").isEmpty()){
            gwm.setStreamDepletion(null);
          }else{
            String num = requestUtil.getString("streamDepletion").replaceAll("[,\\s]", "");
            gwm.setStreamDepletion(Double.parseDouble(num));
          }
          if(requestUtil.getString("netTransWater").isEmpty()){
            gwm.setNetTransWater(null);
          }else{
            String num = requestUtil.getString("netTransWater").replaceAll("[,\\s]", "");
            gwm.setNetTransWater(Double.parseDouble(num));
          }
        }
        else if ("grossTransPumping".equalsIgnoreCase(fieldName)
                  ||"streamDepletion".equalsIgnoreCase(fieldName)){
          if(requestUtil.getString("netTransWater").isEmpty()){
            gwm.setNetTransWater(null);
          }else{
            String num = requestUtil.getString("netTransWater").replaceAll("[,\\s]", "");
            gwm.setNetTransWater(Double.parseDouble(num));
          }
        }

        wgmf.edit(gwm);
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

    private void deleteGroundwater(WtTrans transRec) throws IOException, SQLException{
      WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtGwMonthlyFacade wgmf = (WtGwMonthlyFacade)appContext.getBean(WtGwMonthlyFacade.class.getSimpleName());
      WtWellFacade wwf = (WtWellFacade)appContext.getBean(WtWellFacade.class.getSimpleName());
      WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
      WtGroundwater groundwater = transRec.getWtGroundwater();
      if (groundwater != null){
        Collection<WtGwMonthly> gwMonthlycol = groundwater.getWtGwMonthlyCollection();
        for(WtGwMonthly gwMonthly:gwMonthlycol)
        {
          gwMonthly.setWtGroundwater(null);
          wgmf.edit(gwMonthly);
//          wgmf.remove(gwMonthly);
        }

        //Will not actually delete associated wells and attachment, just break the link
        Collection<WtWell> wellCollection = groundwater.getWtWellCollection();
        for(WtWell well : wellCollection)
        {
          Collection<WtGroundwater> gwCollection = well.getWtGroundwaterCollection();
          gwCollection.remove(groundwater);
          wwf.edit(well);
        }
        Collection<WtAttachment> gwAttachments = groundwater.getWtAttachmentCollection();
        for(WtAttachment attachment:gwAttachments)
        {
          attachment.setWtGroundwater(null);
          waf.edit(attachment);
        }

        groundwater.setWtGwMonthlyCollection(null);
        groundwater.setWtWellCollection(null);
        groundwater.setWtAttachmentCollection(null);
        wgwf.edit(groundwater);
      }
    }
    
    private void deleteGroundwaterPost(WtGroundwater groundwater) throws IOException, SQLException{
      WtGwMonthlyFacade wgmf = (WtGwMonthlyFacade)appContext.getBean(WtGwMonthlyFacade.class.getSimpleName());
      
      if (groundwater != null) {
        Collection<WtGwMonthly> gwMonthlycollection = groundwater.getWtGwMonthlyCollection();
        if (null!=gwMonthlycollection && gwMonthlycollection.size()>0){
        for(WtGwMonthly gwMonthly:gwMonthlycollection)
          {
            gwMonthly.setWtGroundwater(null);
            wgmf.remove(gwMonthly);
          }
        }
      }
    }

    @RequestMapping("/getAssociateWells")
    public ModelAndView getAssociateWells(@RequestParam("wtTransId") Integer transId
                                          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      ModelAndView mv = null;
      try {
        LoggedInCheck(request, response);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        List<JSONObject> wellCollection = new ArrayList();
        Boolean isEmpty = true;
        // Get Status for permission control
        WtTrans wtTrans = wtf.find(transId);
        WtStatusFlag status = null;
        if(wtTrans != null){
          status = wtTrans.getWtStatusFlag();
        }
        if(transId != null) {
          Connection conn = ConnectionContext.getConnection("WtDataSource");
          String query = String.format("SELECT WELL.* FROM %1$s WELL\n" +
                                       "LEFT JOIN %2$s GL ON GL.WT_WELL_ID = well.WT_WELL_ID\n" +
                                       "LEFT JOIN %3$s GW ON GL.WT_GROUNDWATER_ID = GW.WT_GROUNDWATER_ID\n" +
                                       "WHERE GW.WT_TRANS_ID = ? ORDER BY well.WT_WELL_ID"
                                       ,EntityUtil.getTableName(WtWell.class)
                                       ,"WT_GW_WELL"
                                       ,EntityUtil.getTableName(WtGroundwater.class));

          Collection<WtWell> result = new ArrayList<>();
          GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(result) {
            @Override
            public void handle(ResultSet rs) throws Exception {
              List<WtWell> wells = this.getListener();
              WtWell well = null;
              while(rs.next()) {
                well = new WtWell();
                EntityUtil.Load(rs, well);
                wells.add(well);
              }
            }
          });
          if(result.size() > 0)
          {
            for(WtWell well : result)
            {
              JSONObject wellObj = new JSONObject(well.toMap());
              if(!wellObj.optString("lastCalibrateDate").isEmpty()){
                String calDate = dateFormat(wellObj.optString("lastCalibrateDate"),"EEE MMM d HH:mm:ss z yyyy");
                wellObj.put("lastCalibrateDate",calDate);
              }
              if(!wellObj.optString("meterLastInstall").isEmpty()){
                String meterDate = dateFormat(wellObj.optString("meterLastInstall"),"EEE MMM d HH:mm:ss z yyyy");
                wellObj.put("meterLastInstall", meterDate);
              }
//                wellObj.put("checklist", wwf.checklistItem(well));
              wellCollection.add(wellObj);
            }
          }
          isEmpty = wellCollection.isEmpty();
        }
        mv = new ModelAndView("templates/associateWells");
        mv.addObject("isEmpty", isEmpty);
        mv.addObject("wellList",wellCollection);
        mv.addObject("status", status);
      }
      catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Proposal Process Part">
  public void saveProposalProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException { //using LoadProperties
    JSONObject jsonResponse = new JSONObject();
    try {
      if (!UserContext.getInstance().isLoggedIn()) {
        throw new MyException("", "ErrorPromptsHandler.SESSIONTIMEOUT");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtAgencyApprovalFacade waaf = (WtAgencyApprovalFacade) appContext.getBean(WtAgencyApprovalFacade.class.getSimpleName());
      WtWaterLossFacade wwlf = (WtWaterLossFacade) appContext.getBean(WtWaterLossFacade.class.getSimpleName());
      AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);

      WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      transRec.setUpdatedById(user.getUserId());
      transRec.setSwpaoContractNum(requestUtil.getString("swpaoContractNum"));
      if (requestUtil.getString("contractAmount").isEmpty()) {
        transRec.setContractAmount(null);
      } else {
        String num = requestUtil.getString("contractAmount").replaceAll("[,\\s]", "");
        transRec.setContractAmount(Double.parseDouble(num));
      }
//      if (requestUtil.getString("ciContractAmount").isEmpty()) {
//        transRec.setCiContractAmount(null);
//      } else {
//        String num = requestUtil.getString("ciContractAmount").replaceAll("[,\\s]", "");
//        transRec.setCiContractAmount(Double.parseDouble(num));
//      }
//      if (requestUtil.getString("rvContractAmount").isEmpty()) {
//        transRec.setRvContractAmount(null);
//      } else {
//        String num = requestUtil.getString("rvContractAmount").replaceAll("[,\\s]", "");
//        transRec.setRvContractAmount(Double.parseDouble(num));
//      }
//      if (requestUtil.getString("gwContractAmount").isEmpty()) {
//        transRec.setGwContractAmount(null);
//      } else {
//        String num = requestUtil.getString("gwContractAmount").replaceAll("[,\\s]", "");
//        transRec.setGwContractAmount(Double.parseDouble(num));
//      }
      transRec.setCiReviewer(requestUtil.getString("ciReviewer"));
      transRec.setGsReviewer(requestUtil.getString("gsReviewer"));
      transRec.setRrReviewer(requestUtil.getString("rrReviewer"));
      transRec.setSwpaoReviewer(requestUtil.getString("swpaoReviewer"));
      transRec.setRegionReviewer(requestUtil.getString("regionReviewer"));
      transRec.setUsbrReviewer(requestUtil.getString("usbrReviewer"));
      if (requestUtil.getString("isFisheriesReview").isEmpty()) {
        transRec.setIsFisheriesReview(null);
      } else {
        transRec.setIsFisheriesReview(requestUtil.getInt("isFisheriesReview"));
      }
      if (requestUtil.getString("fisheriesApprocalDate").isEmpty()) {
        transRec.setFisheriesApprocalDate(null);
      } else {
        transRec.setFisheriesApprocalDate(requestUtil.getUtilDate("fisheriesApprocalDate", "MM/dd/yyyy"));
      }
      transRec.setDwrComments(requestUtil.getString("dwrComments"));

      //Save Agency Approval Required Info
      WtAgencyApproval agencyApproval = transRec.getWtAgencyApproval();
      if (agencyApproval == null) {
        agencyApproval = new WtAgencyApproval();
        agencyApproval.setWtTrans(transRec);
        agencyApproval.setDwr(0);
        agencyApproval.setSwrcb(0);
        agencyApproval.setUsbr(0);
        agencyApproval.setFishery(0);
        agencyApproval.setOther(0);
        waaf.create(agencyApproval);
      }
      if (requestUtil.getString("DWR").equals("on")) {
        agencyApproval.setDwr(1);
      } else {
        agencyApproval.setDwr(0);
      }
      if (requestUtil.getString("SWRCB").equals("on")) {
        agencyApproval.setSwrcb(1);
      } else {
        agencyApproval.setSwrcb(0);
      }
      if (requestUtil.getString("USBR").equals("on")) {
        agencyApproval.setUsbr(1);
      } else {
        agencyApproval.setUsbr(0);
      }
      if (requestUtil.getString("FISHERY").equals("on")) {
        agencyApproval.setFishery(1);
      } else {
        agencyApproval.setFishery(0);
      }
      if (requestUtil.getString("OTHER").equals("on")) {
        agencyApproval.setOther(1);
      } else {
        agencyApproval.setOther(0);
      }
      agencyApproval.setOtherDetail(requestUtil.getString("otherDetail"));
      waaf.edit(agencyApproval);

      // Save Carriage Water Loss Info
      WtWaterLoss waterLoss = transRec.getWtWaterLoss();
      if (waterLoss == null) {
        waterLoss = new WtWaterLoss();
        waterLoss.setWtTrans(transRec);
        wwlf.create(waterLoss);
      }
      waterLoss.loadProperties(request);
      wwlf.edit(waterLoss);

      wtf.edit(transRec);
      jsonResponse.put("success", true);
    } catch (Exception ex) {
      jsonResponse.put("error", ex.getMessage()).put("success", false);
      if (ex instanceof MyException) {
        jsonResponse.put("callback", ((MyException) ex).getCallback());
      }
    } finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

  @RequestMapping("/saveprocessfield")
  public void saveProcessField(@RequestParam("transId") Integer transId, @RequestParam("fieldName") String fieldName, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
        throw new Exception("The modification transfer id and field name cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
      AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);

      WtTrans transRec = wtf.find(transId);
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      WtGroundwater groundwater = transRec.getWtGroundwater();
      WtReservoir reservoir = transRec.getWtReservoir();

      String textString = requestUtil.getString("fieldValue");
      String numString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
      String contractAmount = requestUtil.getString("contractAmount").replaceAll("[,\\s]", "");

      if(null != fieldName)switch (fieldName) {
        case "swpaoContractNum":
          transRec.setSwpaoContractNum(textString);
          break;
        case "contractAmount":
          if (numString.isEmpty()) {
            transRec.setContractAmount(null);
          } else {
            transRec.setContractAmount(Double.parseDouble(numString));
          } break;
        case "ciReviewer":
          transRec.setCiReviewer(textString);
          break;
        case "gsReviewer":
          transRec.setGsReviewer(textString);
          break;
        case "rrReviewer":
          transRec.setRrReviewer(textString);
          break;
        case "swpaoReviewer":
          transRec.setSwpaoReviewer(textString);
          break;
        case "regionReviewer":
          transRec.setRegionReviewer(textString);
          break;
        case "usbrReviewer":
          transRec.setUsbrReviewer(textString);
          break;
        case "isFisheriesReview":
          transRec.setIsFisheriesReview(requestUtil.getInt("fieldValue"));
          break;
        case "fisheriesApprocalDate":
          transRec.setFisheriesApprocalDate(requestUtil.getUtilDate("fieldValue", "MM/dd/yyyy"));
          break;
        case "dwrComments":
          transRec.setDwrComments(textString);
          break;
        case "reportComment":
          transRec.setReportComment(textString);
          break;
      }

      // For Transfer Amount Report Table
      if (cropIdling != null && cropIdling.getWtCropIdlingId() != null){
        if(null != fieldName)switch (fieldName) {
          case "ciContractAmount":
            if (numString.isEmpty()) {
              cropIdling.setContractAmount(null);
            } else {
              cropIdling.setContractAmount(Double.parseDouble(numString));
            }
            if ("0".equalsIgnoreCase(contractAmount)) {
              transRec.setContractAmount(null);
            } else {
              transRec.setContractAmount(Double.parseDouble(contractAmount));
            }break;
          case "ciExportAmount":
            if (numString.isEmpty()) {
              cropIdling.setExportAmount(null);
            } else {
              cropIdling.setExportAmount(Double.parseDouble(numString));
            } break;
          case "ciExportedAmount":
            if (numString.isEmpty()) {
              cropIdling.setExportedAmount(null);
            } else {
              cropIdling.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "ciDeliveredAmount":
            if (numString.isEmpty()) {
              cropIdling.setDeliveredAmount(null);
            } else {
              cropIdling.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        wcif.edit(cropIdling);
      }
      if (groundwater != null && groundwater.getWtGroundwaterId() != null){
        if(null != fieldName)switch (fieldName) {
          case "gwContractAmount":
            if (numString.isEmpty()) {
              groundwater.setContractAmount(null);
            } else {
              groundwater.setContractAmount(Double.parseDouble(numString));
            }
            if ("0".equalsIgnoreCase(contractAmount)) {
              transRec.setContractAmount(null);
            } else {
              transRec.setContractAmount(Double.parseDouble(contractAmount));
            }break;
          case "gwExportAmount":
            if (numString.isEmpty()) {
              groundwater.setExportAmount(null);
            } else {
              groundwater.setExportAmount(Double.parseDouble(numString));
            } break;
          case "gwExportedAmount":
            if (numString.isEmpty()) {
              groundwater.setExportedAmount(null);
            } else {
              groundwater.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "gwDeliveredAmount":
            if (numString.isEmpty()) {
              groundwater.setDeliveredAmount(null);
            } else {
              groundwater.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        wgwf.edit(groundwater);
      }
      if (reservoir != null && reservoir.getWtReservoirId() != null){
        if(null != fieldName)switch (fieldName) {
          case "rvContractAmount":
            if (numString.isEmpty()) {
              reservoir.setContractAmount(null);
            } else {
              reservoir.setContractAmount(Double.parseDouble(numString));
            }
            if ("0".equalsIgnoreCase(contractAmount)) {
              transRec.setContractAmount(null);
            } else {
              transRec.setContractAmount(Double.parseDouble(contractAmount));
            }break;
          case "rvExportAmount":
            if (numString.isEmpty()) {
              reservoir.setExportAmount(null);
            } else {
              reservoir.setExportAmount(Double.parseDouble(numString));
            } break;
          case "rvExportedAmount":
            if (numString.isEmpty()) {
              reservoir.setExportedAmount(null);
            } else {
              reservoir.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "rvDeliveredAmount":
            if (numString.isEmpty()) {
              reservoir.setDeliveredAmount(null);
            } else {
              reservoir.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        wrvf.edit(reservoir);
      }

      transRec.setUpdatedById(user.getUserId());
      wtf.edit(transRec);
      jsonResponse.put("success", true);
    } catch (Exception exp) {
      jsonResponse.put("success", false).put("error", exp.getMessage());
      if ((exp instanceof MyException)) {
        jsonResponse.put("callback", ((MyException) exp).getCallback());
      }
    } finally {
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }


  /**
   * Edits the agency approval based on the transfer id and a
   * fieldname and fieldvalue pair.
   * <p>
   *  If the transfer record is not found based on the transfer id, then an
   * 404 error reponse is sent through the response object.
   * </p>
   * <p>
   *  If the agency approval entity ({@linkplain{ WtAgencyApproval})
   * does not exist then it is created with default values.
   * </p>
   * <p>
   * Valid field names and field values include :
   * <ul>
   * <li> DWR :Integer (0,1) </li>
   * <li> SWRCB:Integer(0,1) </li>
   * <li> USBR:Integer (0,1)</li>
   * <li> FISHERY:Integer(0,1)</li>
   * <li> OTHER:Integer(0,1)</li>
   * <li> otherDetail:String</li>
   * </ul>
   * </p>
   *
   * @param transId The transaction id.
   * @param fieldName
   * @param request
   * @param response
   * @throws IOException
   */
  @RequestMapping("/saveagencyapproval")
  public void saveAgencyApproval(@RequestParam("transId") Integer transId,
          @RequestParam("fieldName") String fieldName,
          HttpServletRequest request, HttpServletResponse response) throws IOException {

    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
        throw new Exception("The modification transfer id and field name cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtAgencyApprovalFacade waaf = (WtAgencyApprovalFacade) appContext.getBean(WtAgencyApprovalFacade.class.getSimpleName());

      WtTrans transRec = wtf.find(transId);
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      WtAgencyApproval agencyApproval = transRec.getWtAgencyApproval();
      if (agencyApproval == null) {
        agencyApproval = new WtAgencyApproval();
        agencyApproval.setWtTrans(transRec);
        agencyApproval.setDwr(0);
        agencyApproval.setSwrcb(0);
        agencyApproval.setUsbr(0);
        agencyApproval.setFishery(0);
        agencyApproval.setOther(0);
        waaf.create(agencyApproval);
      }
      if(null != fieldName)switch (fieldName) {
        case "DWR":
          agencyApproval.setDwr(requestUtil.getInt("fieldValue"));
          break;
        case "SWRCB":
          agencyApproval.setSwrcb(requestUtil.getInt("fieldValue"));
          break;
        case "USBR":
          agencyApproval.setUsbr(requestUtil.getInt("fieldValue"));
          break;
        case "FISHERY":
          agencyApproval.setFishery(requestUtil.getInt("fieldValue"));
          break;
        case "OTHER":
          agencyApproval.setOther(requestUtil.getInt("fieldValue"));
          break;
        case "otherDetail":
          agencyApproval.setOtherDetail(requestUtil.getString("fieldValue"));
          break;
      }
      waaf.edit(agencyApproval);

      jsonResponse.put("success", true);
    } catch (Exception exp) {
      jsonResponse.put("success", false)
              .put("error", exp.getMessage());
      if ((exp instanceof MyException)) {
        jsonResponse.put("callback", ((MyException) exp).getCallback());
      }
    } finally {
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }

  @RequestMapping("/savewaterloss")
  public void saveWaterLoss(@RequestParam("transId") Integer transId, @RequestParam("fieldName") String fieldName, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
        throw new Exception("The modification transfer id and field name cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtWaterLossFacade wwlf = (WtWaterLossFacade) appContext.getBean(WtWaterLossFacade.class.getSimpleName());
      WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());

      WtTrans transRec = wtf.find(transId);
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      WtWaterLoss waterLoss = transRec.getWtWaterLoss();
      if (waterLoss == null) {
        waterLoss = new WtWaterLoss();
        waterLoss.setWtTrans(transRec);
        wwlf.create(waterLoss);
      }

      WtCropIdling cropIdling = transRec.getWtCropIdling();
      WtGroundwater groundwater = transRec.getWtGroundwater();
      WtReservoir reservoir =  transRec.getWtReservoir();

      // Save Exported and Delivered Amount for CropIdling/Groundwater/Reservoir
      if (cropIdling != null && cropIdling.getWtCropIdlingId() != null){
        String ciExportedAmount = requestUtil.getString("ciExportedAmount").replaceAll("[,\\s]", "");
        String ciDeliveredAmount = requestUtil.getString("ciDeliveredAmount").replaceAll("[,\\s]", "");
        if (ciExportedAmount.isEmpty()){
          cropIdling.setExportedAmount(null);
        } else {
          cropIdling.setExportedAmount(Double.parseDouble(ciExportedAmount));
        }
        if (ciDeliveredAmount.isEmpty()){
          cropIdling.setDeliveredAmount(null);
        } else {
          cropIdling.setDeliveredAmount(Double.parseDouble(ciDeliveredAmount));
        }
        wcif.edit(cropIdling);
      }
      if (groundwater != null && groundwater.getWtGroundwaterId() != null){
        String gwExportedAmount = requestUtil.getString("gwExportedAmount").replaceAll("[,\\s]", "");
        String gwDeliveredAmount = requestUtil.getString("gwDeliveredAmount").replaceAll("[,\\s]", "");
        if (gwExportedAmount.isEmpty()){
          groundwater.setExportedAmount(null);
        } else {
          groundwater.setExportedAmount(Double.parseDouble(gwExportedAmount));
        }
        if (gwDeliveredAmount.isEmpty()){
          groundwater.setDeliveredAmount(null);
        } else {
          groundwater.setDeliveredAmount(Double.parseDouble(gwDeliveredAmount));
        }
        wgwf.edit(groundwater);
      }
      if (reservoir != null && reservoir.getWtReservoirId() != null){
        String rvExportedAmount = requestUtil.getString("rvExportedAmount").replaceAll("[,\\s]", "");
        String rvDeliveredAmount = requestUtil.getString("rvDeliveredAmount").replaceAll("[,\\s]", "");
        if (rvExportedAmount.isEmpty()){
          reservoir.setExportedAmount(null);
        } else {
          reservoir.setExportedAmount(Double.parseDouble(rvExportedAmount));
        }
        if (rvDeliveredAmount.isEmpty()){
          reservoir.setDeliveredAmount(null);
        } else {
          reservoir.setDeliveredAmount(Double.parseDouble(rvDeliveredAmount));
        }
        wrvf.edit(reservoir);
      }

      String numString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");
      if(null != fieldName)switch (fieldName) {
        case "banksInitialPercent":
          if (numString.isEmpty()) {
            waterLoss.setBanksInitialPercent(null);
          } else {
            waterLoss.setBanksInitialPercent(Integer.parseInt(numString));
          } break;
        case "banksfinalPercent":
          if (numString.isEmpty()) {
            waterLoss.setBanksfinalPercent(null);
          } else {
            waterLoss.setBanksfinalPercent(Integer.parseInt(numString));
          } break;
        case "nbaInitialPercent":
          if (numString.isEmpty()) {
            waterLoss.setNbaInitialPercent(null);
          } else {
            waterLoss.setNbaInitialPercent(Integer.parseInt(numString));
          } break;
        case "nbafinalPercent":
          if (numString.isEmpty()) {
            waterLoss.setNbafinalPercent(null);
          } else {
            waterLoss.setNbafinalPercent(Integer.parseInt(numString));
          } break;
        case "mercedInitialPercent":
          if (numString.isEmpty()) {
            waterLoss.setMercedInitialPercent(null);
          } else {
            waterLoss.setMercedInitialPercent(Integer.parseInt(numString));
          } break;
        case "mercedfinalPercent":
          if (numString.isEmpty()) {
            waterLoss.setMercedfinalPercent(null);
          } else {
            waterLoss.setMercedfinalPercent(Integer.parseInt(numString));
          } break;
        case "reachLossPercent":
          if (numString.isEmpty()) {
            waterLoss.setReachLossPercent(null);
          } else {
            waterLoss.setReachLossPercent(Integer.parseInt(numString));
          } break;
      }
      wwlf.edit(waterLoss);
      transRec.setWtWaterLoss(waterLoss);

      jsonResponse.put("success", true);
    } catch (Exception exp) {
      jsonResponse.put("success", false).put("error", exp.getMessage());
      if ((exp instanceof MyException)) {
        jsonResponse.put("callback", ((MyException) exp).getCallback());
      }
    } finally {
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }

    @RequestMapping("/savetransferamountreport")
    public void saveTransferAmountReport(@RequestParam("transId") Integer transId,
                                         @RequestParam("fieldName") String fieldName,
                                         HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null) || (StringUtils.isEmpty(fieldName))) {
        throw new Exception("The modification transfer id and field name cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
      AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);

      WtTrans transRec = wtf.find(transId);
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      WtGroundwater groundwater = transRec.getWtGroundwater();
      WtReservoir reservoir = transRec.getWtReservoir();

      String textString = requestUtil.getString("fieldValue");
      String numString = requestUtil.getString("fieldValue").replaceAll("[,\\s]", "");

      // For Transfer Amount Report Table
      if (cropIdling != null && cropIdling.getWtCropIdlingId() != null){
        if(null != fieldName)switch (fieldName) {
          case "ciExportAmount":
            if (numString.isEmpty()) {
              cropIdling.setExportAmount(null);
            } else {
              cropIdling.setExportAmount(Double.parseDouble(numString));
            } break;
          case "ciExportedAmount":
            if (numString.isEmpty()) {
              cropIdling.setExportedAmount(null);
            } else {
              cropIdling.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "ciDeliveredAmount":
            if (numString.isEmpty()) {
              cropIdling.setDeliveredAmount(null);
            } else {
              cropIdling.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        String ciExportedAmount = requestUtil.getString("ciExportedAmount");
        String ciDeliveredAmount = requestUtil.getString("ciDeliveredAmount");
        if (!ciExportedAmount.isEmpty()){
          cropIdling.setExportedAmount(Double.parseDouble(ciExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!ciDeliveredAmount.isEmpty()){
          cropIdling.setDeliveredAmount(Double.parseDouble(ciDeliveredAmount.replaceAll("[,\\s]", "")));
        }
        wcif.edit(cropIdling);
      }
      if (groundwater != null && groundwater.getWtGroundwaterId() != null){
        if(null != fieldName)switch (fieldName) {
          case "gwExportAmount":
            if (numString.isEmpty()) {
              groundwater.setExportAmount(null);
            } else {
              groundwater.setExportAmount(Double.parseDouble(numString));
            } break;
          case "gwExportedAmount":
            if (numString.isEmpty()) {
              groundwater.setExportedAmount(null);
            } else {
              groundwater.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "gwDeliveredAmount":
            if (numString.isEmpty()) {
              groundwater.setDeliveredAmount(null);
            } else {
              groundwater.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        String gwExportedAmount = requestUtil.getString("gwExportedAmount");
        String gwDeliveredAmount = requestUtil.getString("gwDeliveredAmount");
        if (!gwExportedAmount.isEmpty()){
          groundwater.setExportedAmount(Double.parseDouble(gwExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!gwDeliveredAmount.isEmpty()){
          groundwater.setDeliveredAmount(Double.parseDouble(gwDeliveredAmount.replaceAll("[,\\s]", "")));
        }

        wgwf.edit(groundwater);
      }
      if (reservoir != null && reservoir.getWtReservoirId() != null){
        if(null != fieldName)switch (fieldName) {
          case "rvExportAmount":
            if (numString.isEmpty()) {
              reservoir.setExportAmount(null);
            } else {
              reservoir.setExportAmount(Double.parseDouble(numString));
            } break;
          case "rvExportedAmount":
            if (numString.isEmpty()) {
              reservoir.setExportedAmount(null);
            } else {
              reservoir.setExportedAmount(Double.parseDouble(numString));
            } break;
          case "rvDeliveredAmount":
            if (numString.isEmpty()) {
              reservoir.setDeliveredAmount(null);
            } else {
              reservoir.setDeliveredAmount(Double.parseDouble(numString));
            } break;
        }
        String rvExportedAmount = requestUtil.getString("rvExportedAmount");
        String rvDeliveredAmount = requestUtil.getString("rvDeliveredAmount");
        if (!rvExportedAmount.isEmpty()){
          reservoir.setExportedAmount(Double.parseDouble(rvExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!rvDeliveredAmount.isEmpty()){
          reservoir.setDeliveredAmount(Double.parseDouble(rvDeliveredAmount.replaceAll("[,\\s]", "")));
        }
        wrvf.edit(reservoir);
      }
      transRec.setUpdatedById(user.getUserId());
      wtf.edit(transRec);
      jsonResponse.put("success", true);
    } catch (Exception exp) {
      jsonResponse.put("success", false).put("error", exp.getMessage());
      if ((exp instanceof MyException)) {
        jsonResponse.put("callback", ((MyException) exp).getCallback());
      }
    } finally {
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }

  @RequestMapping("/savetransferamountall")
  public void saveTransferAmountAll(@RequestParam("transId") Integer transId,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null)) {
        throw new Exception("The modification transfer id cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtCropIdlingFacade wcif = (WtCropIdlingFacade)appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
      WtGroundwaterFacade wgwf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtReservoirFacade wrvf = (WtReservoirFacade)appContext.getBean(WtReservoirFacade.class.getSimpleName());
      AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);

      WtTrans transRec = wtf.find(transId);
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      WtGroundwater groundwater = transRec.getWtGroundwater();
      WtReservoir reservoir = transRec.getWtReservoir();

      // For Transfer Amount Report Table
      if (cropIdling != null && cropIdling.getWtCropIdlingId() != null){
        String ciExportAmount = requestUtil.getString("ciExportAmount");
        String ciExportedAmount = requestUtil.getString("ciExportedAmount");
        String ciDeliveredAmount = requestUtil.getString("ciDeliveredAmount");
        if (!ciExportAmount.isEmpty()){
          cropIdling.setExportAmount(Double.parseDouble(ciExportAmount.replaceAll("[,\\s]", "")));
        }
        if (!ciExportedAmount.isEmpty()){
          cropIdling.setExportedAmount(Double.parseDouble(ciExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!ciDeliveredAmount.isEmpty()){
          cropIdling.setDeliveredAmount(Double.parseDouble(ciDeliveredAmount.replaceAll("[,\\s]", "")));
        }
        wcif.edit(cropIdling);
      }
      if (groundwater != null && groundwater.getWtGroundwaterId() != null){
        String gwExportAmount = requestUtil.getString("gwExportAmount");
        String gwExportedAmount = requestUtil.getString("gwExportedAmount");
        String gwDeliveredAmount = requestUtil.getString("gwDeliveredAmount");
        if (!gwExportAmount.isEmpty()){
          groundwater.setExportAmount(Double.parseDouble(gwExportAmount.replaceAll("[,\\s]", "")));
        }
        if (!gwExportedAmount.isEmpty()){
          groundwater.setExportedAmount(Double.parseDouble(gwExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!gwDeliveredAmount.isEmpty()){
          groundwater.setDeliveredAmount(Double.parseDouble(gwDeliveredAmount.replaceAll("[,\\s]", "")));
        }

        wgwf.edit(groundwater);
      }
      if (reservoir != null && reservoir.getWtReservoirId() != null){
        String rvExportAmount = requestUtil.getString("rvExportAmount");
        String rvExportedAmount = requestUtil.getString("rvExportedAmount");
        String rvDeliveredAmount = requestUtil.getString("rvDeliveredAmount");
        if (!rvExportAmount.isEmpty()){
          reservoir.setExportAmount(Double.parseDouble(rvExportAmount.replaceAll("[,\\s]", "")));
        }
        if (!rvExportedAmount.isEmpty()){
          reservoir.setExportedAmount(Double.parseDouble(rvExportedAmount.replaceAll("[,\\s]", "")));
        }
        if (!rvDeliveredAmount.isEmpty()){
          reservoir.setDeliveredAmount(Double.parseDouble(rvDeliveredAmount.replaceAll("[,\\s]", "")));
        }
        wrvf.edit(reservoir);
      }
      transRec.setUpdatedById(user.getUserId());
      wtf.edit(transRec);
      jsonResponse.put("success", true);
    } catch (Exception exp) {
      jsonResponse.put("success", false).put("error", exp.getMessage());
      if ((exp instanceof MyException)) {
        jsonResponse.put("callback", ((MyException) exp).getCallback());
      }
    } finally {
      response.setContentType("application/json");
      response.getWriter().write(jsonResponse.toString());
    }
  }
  //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Loads and Edit Prevoius Transfer Table">
    @RequestMapping("/getPreTransferTable")
    public ModelAndView getPreTransferTable(@RequestParam("wtTransId") Integer wtTransId
                                            ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      ModelAndView mv = null;
      try {
        LoggedInCheck(request, response);
        // Get Status for permission control
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans wtTrans = wtf.find(wtTransId);
        WtStatusFlag status = null;
        if(wtTrans != null){
          status = wtTrans.getWtStatusFlag();
        }

        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.SWPAO_CONTRACT_NUM", EntityUtil.getTableName(WtPreTransfer.class));
        List<WtPreTransfer> preTransferList = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(wtTransId), new QueryDelegate(preTransferList) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtPreTransfer> transfers = this.getListener();
            WtPreTransfer transfer = null;
            while (rs.next()) {
              transfer = new WtPreTransfer();
              transfer.Load(rs);
              transfers.add(transfer);
            }
          }
        });

        mv = new ModelAndView("templates/prevTransferTable");
        mv.addObject("preTransferList", preTransferList);
        mv.addObject("status", status);
        mv.addObject("hasData", (preTransferList != null && !preTransferList.isEmpty()));
      }
      catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }

    public void savePreTransfer(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtPreTransferFacade ptf = (WtPreTransferFacade)appContext.getBean(WtPreTransferFacade.class.getSimpleName());
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtPreTransfer wpt = new WtPreTransfer();
        if(!request.getParameter("wtPreTransferId").isEmpty()){
          wpt = ptf.find(Integer.parseInt(request.getParameter("wtPreTransferId")));
        }
        wpt.setWtTrans(wtf.find(Integer.parseInt(request.getParameter("wtTransId"))));
        wpt.loadProperties(request);
//        wpt.setIsTypeCi(Integer.parseInt(request.getParameter("isTypeCi")));
//        wpt.setIsTypeRv(Integer.parseInt(request.getParameter("isTypeRv")));
//        wpt.setIsTypeGw(Integer.parseInt(request.getParameter("isTypeGw")));
        if(request.getParameter("wtPreTransferId").isEmpty())
        {
          ptf.create(wpt);
        }
        else
        {
          ptf.edit(wpt);
        }
        jsonResponse = new JSONObject(wpt.toMap());
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

    public void removePreTransfer(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtPreTransferFacade ptf = (WtPreTransferFacade)appContext.getBean(WtPreTransferFacade.class.getSimpleName());
        if(!request.getParameter("wtPreTransferId").isEmpty()){
            WtPreTransfer wpt = ptf.find(Integer.parseInt(request.getParameter("wtPreTransferId")));
            ptf.remove(wpt);
            jsonResponse.put("data",wpt.toMap());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Loads and Edit Proposal Other Parts">
    public void editAssociateWells(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        JSONObject jsondata = new JSONObject(requestUtil.getString("jsonData"));
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        WtWellFacade wf = (WtWellFacade)appContext.getBean(WtWellFacade.class.getSimpleName());

        WtWell well = new WtWell();
        if(!jsondata.optString("wtWellNum").isEmpty()){
          well = wf.set("wtWellNum", jsondata.optString("wtWellNum")).find();
        } else if(!jsondata.optString("stateWellNum").isEmpty()){
          well = wf.set("stateWellNum", jsondata.optString("stateWellNum")).find();
        }
        well.loadProperties(jsondata.getJSONMap());
        if(!jsondata.optString("lastCalibrateDate").isEmpty())
        {
            well.setLastCalibrateDate(df.parse(jsondata.optString("lastCalibrateDate")));
        }
        if(!jsondata.optString("meterLastInstall").isEmpty())
        {
            well.setMeterLastInstall(df.parse(jsondata.optString("meterLastInstall")));
        }
        wf.edit(well);
        boolean isComplete = wf.checklistItem(well);
        jsonResponse.put("data",well.toMap());
        jsonResponse.put("isComplete",isComplete);
        jsonResponse.put("success", true);
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
    public void removeAssociateWells(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!AuthenticationController.IsLoggedIn((request))){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtWellFacade wwf = (WtWellFacade)appContext.getBean(WtWellFacade.class.getSimpleName());
        WtGroundwaterFacade wgf = (WtGroundwaterFacade)appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        int wtTransId = requestUtil.getInt("wtTransId");
        WtTrans trans = wtf.find(wtTransId);
        Collection<WtWell> wellCollection = trans.getWtGroundwater().getWtWellCollection();
        WtWell well = wwf.find(requestUtil.getInt("wtWellId"));
        if(wellCollection.contains(well))
        {
            wellCollection.remove(well);
        }
        trans.getWtGroundwater().setWtWellCollection(wellCollection);
        wgf.edit(trans.getWtGroundwater());
        jsonResponse = new JSONObject(well.toMap());
        jsonResponse.put("success", true);
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

    @RequestMapping("/getWaterRightsTable")
    public ModelAndView getWaterRightsTable(@RequestParam("wtTransId") Integer wtTransId
      ,HttpServletRequest request, HttpServletResponse response) throws IOException {
      ModelAndView mv = null;
      try {
        LoggedInCheck(request, response);
//        WtWaterRightsFacade facade = WebUtil.getFacade(WtWaterRightsFacade.class);
//        List<WtWaterRights> waterRights = facade.select("SELECT * FROM WT_WATER_RIGHTS WHERE WT_TRANS_ID = " + wtTransId,WtWaterRights.class);
        // Get Status for permission control
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans wtTrans = wtf.find(wtTransId);
        WtStatusFlag status = null;
        if(wtTrans != null){
          status = wtTrans.getWtStatusFlag();
        }

        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtWaterRights.class));
        List<WtWaterRights> waterRights = new ArrayList<>();
        GenericFacade.executeQuery(conn, query, Arrays.asList(wtTransId), new QueryDelegate(waterRights) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtWaterRights> rights = this.getListener();
            WtWaterRights right = null;
            while (rs.next()) {
              right = new WtWaterRights();
              right.Load(rs);
              rights.add(right);
            }
          }
        });
        mv = new ModelAndView("templates/waterRightsTable");
        mv.addObject("waterRights", waterRights);
        mv.addObject("status", status);
        mv.addObject("hasData", (waterRights != null && !waterRights.isEmpty()));
      }
      catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
      return mv;
    }
    public void saveWaterRight(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtWaterRightsFacade wrf = (WtWaterRightsFacade)appContext.getBean(WtWaterRightsFacade.class.getSimpleName());
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtWaterRights wwr = new WtWaterRights();
        if(!request.getParameter("wtWaterRightsId").isEmpty()){
            wwr = wrf.find(Integer.parseInt(request.getParameter("wtWaterRightsId")));
        }
        wwr.setWtTrans(wtf.find(Integer.parseInt(request.getParameter("wtTransId"))));
        wwr.loadProperties(request);
        if(!request.getParameter("proposedTransVol").isEmpty()){
          String num = request.getParameter("proposedTransVol").replaceAll("[,\\s]", "");
          wwr.setProposedTransVol(Double.parseDouble(num));
        }
        if(request.getParameter("wtWaterRightsId").isEmpty())
        {
            wrf.create(wwr);
        }
        else
        {
            wrf.edit(wwr);
        }
        jsonResponse = new JSONObject(wwr.toMap());
        jsonResponse.put("success", true);
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
    public void removeWaterRight(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtWaterRightsFacade wrf = (WtWaterRightsFacade)appContext.getBean(WtWaterRightsFacade.class.getSimpleName());
        if(!request.getParameter("wtWaterRightsId").isEmpty()){
            WtWaterRights wwr = wrf.find(Integer.parseInt(request.getParameter("wtWaterRightsId")));
            wrf.remove(wwr);
            jsonResponse = new JSONObject(wwr.toMap());
        }
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
    public ModelAndView getTrackFileInformation(ModelAndView mv,Collection<WtTrackFile> collection)
    {
        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
        JSONObject wellData = new JSONObject();
        for(WtTrackFile attach:collection)
        {
            try{
                int unit = 1024;
                long bytes = attach.getFileLob().length;
                int exp = (int) (Math.log(bytes) / Math.log(unit));
                if(exp == 0)
                {
                    exp = 1;
                }
                String suffix = ("kMGTPE").charAt(exp-1)+"";
                String kb = String.format("%.1f %sB", bytes / Math.pow(unit, exp), suffix);
                if(kb.equals("0.0 KB"))
                {
                    kb = bytes+" bytes";
                }
                wellData.put(""+attach.getWtTrackFileId(), kb);
                auf.reset();
                if(attach.getCreatedById() != null)
                {
//                  WtContact contact = LookupDataContext.getInstance().getContact(attach);
//                    auf.set("userId", attach.getCreatedById());
//                    AppUser app = auf.find();
//                    wellData.put(""+attach.getCreatedById(),attach.getCreatedBy().getName());
                }
            }catch(NullPointerException ex){}
        }
        mv.addObject("trackFileInfo",wellData);
        return mv;
    }

    public ModelAndView getReviewComments(HttpServletRequest request, HttpServletResponse response) throws IOException{
      ModelAndView mv = null;
      try{
        LoggedInCheck(request, response);
        mv = new ModelAndView("templates/groundwaterChecklist");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
        Integer transId = requestUtil.getInt("wtTransId");
        String transType = requestUtil.getString("transType");

        List<WtReviewComment> reviewCommentList = wrcf.select("SELECT WT.* FROM WT_REVIEW_COMMENT WT WHERE WT_TRANS_ID="+transId+" AND TRANS_TYPE='"+transType+"' ORDER BY WT_CHECKLIST_ID",com.gei.entities.WtReviewComment.class);

        mv.addObject("reviewCommentList", reviewCommentList);
      }catch(Exception ex){
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
      return mv;
    }

    public ModelAndView editReviewComments(HttpServletRequest request, HttpServletResponse response) throws IOException{
      ModelAndView mv = null;
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        mv = new ModelAndView("templates/editReviewComment");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
        Integer wtReviewCommentId = requestUtil.getInt("wtReviewCommentId");
        WtReviewComment reviewComment = wrcf.find(wtReviewCommentId);

        mv.addObject("reviewComment", reviewComment);
      }catch(Exception ex){
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
      return mv;
    }

    public void saveReviewComments(HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        WtReviewComment reviewComment = wrcf.find(Integer.parseInt(request.getParameter("wtReviewCommentId")));
        reviewComment.loadProperties(request);
        reviewComment.setUpdatedById(user.getUserId());
        wrcf.edit(reviewComment);
        jsonResponse = new JSONObject(reviewComment.toMap());
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public ModelAndView editReviewResponse(HttpServletRequest request, HttpServletResponse response) throws IOException{
      LoggedInCheck(request,response);
      ModelAndView mv = new ModelAndView("templates/editReviewResponse");
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
      WtResponseFacade wrf = (WtResponseFacade)appContext.getBean(WtResponseFacade.class.getSimpleName());
      AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
      WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
      WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
      Integer wtReviewCommentId = requestUtil.getInt("wtReviewCommentId");
      WtReviewComment reviewComment = wrcf.find(wtReviewCommentId);
//      wrf.set("wtReviewComment", reviewComment);
//      List<WtResponse> responseCommentList = wrf.findAll();
      List<WtResponse> responseCommentList = wrf.select("SELECT WT.* FROM WT_RESPONSE WT WHERE WT_REVIEW_COMMENT_ID="+wtReviewCommentId,com.gei.entities.WtResponse.class);
      JSONObject userData = new JSONObject();
      JSONObject agencyData = new JSONObject();
      for(WtResponse responseComment:responseCommentList){
        if(responseComment.getCreatedById() != null){
          Integer userId = responseComment.getCreatedById();
          auf.set("userId", userId);
          AppUser app = auf.find();
          userData.put(""+userId,app.getWtContact().getFirstName()+" "+app.getWtContact().getLastName());

          Integer contactId = app.getWtContact().getWtContactId();
          wcf.set("wtContactId", contactId);
          WtContact contact = wcf.find();
          if(contact != null && contact.getWtAgency() != null){
            WtAgency agency = waf.find(contact.getWtAgency().getWtAgencyId());
            agencyData.put(""+userId,agency.getAgencyCode());
          }
        }
      }

      mv.addObject("reviewComment", reviewComment);
      mv.addObject("responseCommentList", responseCommentList);
      mv.addObject("userData", userData);
      mv.addObject("agencyData", agencyData);
      return mv;
    }

    public void saveReviewResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
        WtResponseFacade wrf = (WtResponseFacade)appContext.getBean(WtResponseFacade.class.getSimpleName());
        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
        WtContactFacade wcf = (WtContactFacade)appContext.getBean(WtContactFacade.class.getSimpleName());
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);

        WtResponse reviewResponse = new WtResponse();
        Integer wtReviewCommentId = requestUtil.getInt("wtReviewCommentId");
        WtReviewComment reviewComment = wrcf.find(wtReviewCommentId);
        reviewResponse.setWtReviewComment(reviewComment);
        reviewResponse.loadProperties(request);
        reviewResponse.setCreatedById(user.getUserId());
        wrf.create(reviewResponse);
        jsonResponse = new JSONObject(reviewResponse.toMap());
        jsonResponse.put("createdDate", dateFormat(jsonResponse.optString("createdDate"),"MMM dd, yyyy HH:mm a"));

        // Add user and agency information
        Integer userId = user.getUserId();
        auf.set("userId", userId);
        AppUser app = auf.find();
        jsonResponse.put("userName",app.getWtContact().getFirstName()+" "+app.getWtContact().getLastName());

        Integer contactId = app.getWtContact().getWtContactId();
        wcf.set("wtContactId", contactId);
        WtContact contact = wcf.find();
        if(contact != null && contact.getWtAgency() != null){
          WtAgency agency = waf.find(contact.getWtAgency().getWtAgencyId());
          jsonResponse.put("agencyCode",agency.getAgencyCode());
        }
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public void loadCommentChecklist(WtTrans transRec, String transType){
      WtReviewCommentFacade wrcf = (WtReviewCommentFacade)appContext.getBean(WtReviewCommentFacade.class.getSimpleName());
      WtChecklistFacade wclf = (WtChecklistFacade)appContext.getBean(WtChecklistFacade.class.getSimpleName());
      wclf.set("checklistType", transType);
      Collection<WtChecklist> checklists = wclf.findAll();
      for(WtChecklist checklist: checklists){
        WtReviewComment reviewComment = new WtReviewComment();
        reviewComment.setWtTrans(transRec);
        reviewComment.setWtChecklist(checklist);
        reviewComment.setTransType(transType);
        reviewComment.setIssueStatus("NEW");
        wrcf.create(reviewComment);
      }
    }

    @RequestMapping("/ChangeLog/{transId}")
    public ModelAndView getChangeLog(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException {
      ModelAndView mv = null;
      try {
        LoggedInCheck(request, response);
//        WtChangeLogFacade wclf = (WtChangeLogFacade)appContext.getBean(WtChangeLogFacade.class.getSimpleName());
//        Collection<WtChangeLog> changeLog = wclf.select("SELECT WT.* FROM WT_CHANGE_LOG WT WHERE WT_TRANS_ID="+transId+" ORDER BY CREATED_DATE",com.gei.entities.WtChangeLog.class);
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        List<WtChangeLog> logs = new ArrayList<>();
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? ORDER BY %1$s.CREATED_DATE DESC", EntityUtil.getTableName(WtChangeLog.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(logs) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            List<WtChangeLog> logs = this.getListener();
            WtChangeLog log = null;
            while(rs.next()) {
              log = new WtChangeLog();
              log.Load(rs);
              logs.add(log);
            }
          }
        });

        mv = new ModelAndView("proposal/ChangeLog");
        mv.addObject("changeLog", logs);
      }
      catch(Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }

      return mv;
    }

    @RequestMapping("/PDFReport/{transId}")
    public ModelAndView getPDFReport(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException {
      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("proposal/PDFReport");
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTrackFileFacade wtff = (WtTrackFileFacade)appContext.getBean(WtTrackFileFacade.class.getSimpleName());
      Collection<WtTrackFile> trackFile = null;

      String reportType = requestUtil.getString("reportType");
      if (!reportType.isEmpty()){
        if("PR".equalsIgnoreCase(reportType)){
          trackFile = wtff.select("SELECT WT.* FROM WT_TRACK_FILE WT WHERE WT_TRANS_ID="+transId+" AND FILE_TYPE='"+ReportType.PR+"' ORDER BY CREATED_DATE", com.gei.entities.WtTrackFile.class);
        }
        else if ("GWR".equalsIgnoreCase(reportType)) {
          trackFile = wtff.select("SELECT WT.* FROM WT_TRACK_FILE WT WHERE WT_TRANS_ID="+transId+" AND FILE_TYPE='"+ReportType.GWR+"' ORDER BY CREATED_DATE", com.gei.entities.WtTrackFile.class);
        }
      }

      if(trackFile != null && trackFile.size() > 0) {
          mv.addObject("trackFile",trackFile);
          getTrackFileInformation(mv,trackFile);
      }
      if(!requestUtil.getString("transYear").isEmpty()){
        mv.addObject("transYear",requestUtil.getString("transYear"));
      }
      if(!requestUtil.getString("sellerName").isEmpty()){
        mv.addObject("sellerName",requestUtil.getString("sellerName"));
      }
      return mv;
    }

    public void saveReviewerComments(HttpServletRequest request, HttpServletResponse response) throws IOException, NullPointerException{
        JSONObject jsonResponse = new JSONObject();
        try{
          if(!UserContext.getInstance().isLoggedIn()){
            throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
          }
          ServletRequestUtil requestUtil = new ServletRequestUtil(request);
          WtReviewerCommentsFacade wrcf = (WtReviewerCommentsFacade)appContext.getBean(WtReviewerCommentsFacade.class.getSimpleName());
          AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
          String userName = buildUserName(user);

          WtReviewerComments reviewerComments = new WtReviewerComments();
          reviewerComments.setWtTransId(requestUtil.getInt("wtTransId"));
          reviewerComments.setReviewerName(userName);
          reviewerComments.setReviewerComments(requestUtil.getString("reviewerComments"));
          reviewerComments.setCreatedById(user.getUserId());
          wrcf.create(reviewerComments);

          jsonResponse = new JSONObject(reviewerComments.toMap());
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

    public ModelAndView mileStones(HttpServletRequest request, HttpServletResponse response)
    {
        LoggedInCheckNoAjax(request,response);
        ModelAndView mv = new ModelAndView("templates/milestoneLogs");
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Collection<WtTrans> col = wtf.select("SELECT * from WT_TRANS where WT_STATUS_FLAG_ID>0 and WT_STATUS_FLAG_ID<9 ORDER BY WT_TRANS_ID",com.gei.entities.WtTrans.class);

        List<WtStatusTrack> wsts = wstf.select("SELECT * from WT_STATUS_TRACK where STATUS_NAME IN('SUBMITTED','PCOMPLETE','PAPPROVED')",com.gei.entities.WtStatusTrack.class);;
//        JSONArray jsonArray = new JSONArray();
        JSONObject tmpJson = new JSONObject();
        for(WtStatusTrack wst : wsts){
//            JSONObject tmpJson = new JSONObject(wst.toMap());
//            jsonArray.put(tmpJson);
            String transId = wst.getWtTransId().toString();
            if(wst.getStatusName().equalsIgnoreCase("SUBMITTED")){
                tmpJson.put(transId+"SM", df.format(wst.getStatusDate()));
            }
            if(wst.getStatusName().equalsIgnoreCase("PCOMPLETE")){
                tmpJson.put(transId+"PC", df.format(wst.getStatusDate()));
            }
            if(wst.getStatusName().equalsIgnoreCase("CAPPROVAL")){
                tmpJson.put(transId+"CP", df.format(wst.getStatusDate()));
            }
            if(wst.getStatusName().equalsIgnoreCase("PAPPROVED")){
                tmpJson.put(transId+"PP", df.format(wst.getStatusDate()));
            }
        }

        mv.addObject("WtTrans",col);
        mv.addObject("statusTrackList",tmpJson);
        return mv;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Proposal Submit and reSubmit">
    @RequestMapping("/submit")
    public void submitProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException, NullPointerException, MalformedURLException, EmailException, Exception{
        LoggedInCheck(request,response);
        JSONObject jsonResponse = new JSONObject();
        try{
          if(!UserContext.getInstance().isLoggedIn()){
            throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
          }
          ServletRequestUtil requestUtil = new ServletRequestUtil(request);
          WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
          WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
          WtStatusFlag statusFlag = wsff.find(Status.SUBMITTED);
          AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
          int userId = INVALID_INTEGER;
          if(user != null) { userId =  user.getUserId(); }
          WtTrans proposal = new WtTrans();

          JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
          if(jsonData.length()>0){
              saveProposalJson(request,response);
          }
          proposal = wtf.find(requestUtil.getInt("wtTransId"));
          proposal.setUpdatedById(userId);
          proposal.setWtStatusFlag(statusFlag);
          proposal.setWtTransNum(buildTransNum(proposal));

          wtf.edit(proposal);
          setStatusTrack(proposal,user);
          createTransTrack(proposal,user);

          //Send Email
          Map<String, String> emailConfig = getEmailConfigMap();
          String toEmail = emailConfig.get("manager.address");
          String adminEmail = emailConfig.get("admin.address");
          String ownerEmail = getOwnerEmail(proposal);
          // cc Email incude proposal owner, all seller Emails and DWR senior Emails
          List<String> ccEmails = new ArrayList<>();
          ccEmails.addAll(getSeniorEmails());
          // Add manager Email to CC -- 2018.05.17
          ccEmails.add(toEmail);
          ccEmails.add(adminEmail);
          ccEmails.add(ownerEmail);
          ccEmails.addAll(getAgencyEmails(getSellerAgency(proposal)));         
          
          String subject = getSellerName(proposal) +" submitted a water transfer proposal";
          sendEmail(toEmail,ccEmails,subject,buildSubmitMsg(proposal));
        }catch(Exception ex){
          jsonResponse.put("error", ex.getMessage()).put("success",false);
          if (ex instanceof MyException){
            jsonResponse.put("callback", ((MyException)ex).getCallback());
          }
        }finally{
//        response.getWriter().write(jsonResponse.toString());
        }
    }

    //Build Submit Email Msg
//    private String buildSubmitMsg(WtTrans proposal){
//      StringBuilder emailMsg = new StringBuilder();
//      emailMsg.append("New proposal #").append(proposal.getWtTransId());
//      emailMsg.append(" submitted by ");
//      emailMsg.append(getSellerName(proposal)).append(".<br />");
//      emailMsg.append("Buyers are ").append(getBuyerName(proposal)).append(".<br />");
//      emailMsg.append("Transfer Type(s) are ").append(getTransferType(proposal)).append(".<br />");
//
//      return emailMsg.toString();
//    }

    private String buildSubmitMsg(WtTrans proposal){
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      Date date = new Date();
      StringBuilder emailMsg = new StringBuilder();
      emailMsg.append(getSellerName(proposal)).append(" on ");
      emailMsg.append(df.format(date)).append(" submitted a water transfer proposal for ");
      emailMsg.append(getTransferType(proposal)).append(" using ");
      emailMsg.append(getFacilities(proposal)).append(" facilities. </br>");

      return emailMsg.toString();
    }

    private String getBuyerName(WtTrans wt){
      String comma,buyerNames="";
      Iterator<WtBuyer> buyersIt;

      if (wt.getWtSellerCollection()!=null){
        WtBuyer buyer= null;
        buyersIt = wt.getWtBuyerCollection().iterator();
          if (buyersIt != null){
            comma="";
            buyerNames="";
            while (buyersIt.hasNext()){
              buyer = buyersIt.next();
              // Use Agency Code
              buyerNames = buyerNames + comma + buyer.getWtAgency().getAgencyCode();
              comma=",";
            }
          }
        }//end if
      return buyerNames;
    }

    // Build Tansfer Type(s)
    private String getTransferType(WtTrans wt){
      String comma="",transTypes="";
//      if(wt.getWtCropIdling() != null){
      if(wt.getProAcrIdleInd() != null && wt.getProAcrIdleInd() == 1){
        transTypes = "Cropland Idling and Crop Shifting";
        comma = ", ";
      }
//      if(wt.getWtGroundwater() != null){
      if(wt.getWellUseNumInd() != null && wt.getWellUseNumInd() == 1){
        transTypes = transTypes + comma + "Groundwater Substitution";
        comma = ", ";
      }
//      if(wt.getWtReservoir() != null){
      if(wt.getResReOpInd() != null && wt.getResReOpInd() == 1){
        transTypes = transTypes + comma + "Reservoir Release";
        comma = ", ";
      }

      return transTypes;
    }

    // Build Facilities
    private String getFacilities(WtTrans wt){
      WtFuTypeFacade wftf = (WtFuTypeFacade)appContext.getBean(WtFuTypeFacade.class.getSimpleName());
      String comma="",fuTypes="";
      String SWPDelimiter ="", CVPDelimiter ="";
      String SWPTypes="", CVPTypes="", OtherTypes="";
//      Iterator<WtTransType> fuIt;

      Collection<WtTransType> transTypeCollection = wt.getWtTransTypeCollection();
//      if (transTypeCollection != null){
//        for(WtTransType type: transTypeCollection){
//          WtFuType futype = wftf.find(type.getWtTransTypePK().getWtFuTypeId());
//          fuTypes=fuTypes + comma + futype.getFuTypeDescription();
//          comma=",";
//        }
//      }//end if
      
     if (transTypeCollection != null){
        for(WtTransType type: transTypeCollection){
          Integer fuTypeId = type.getWtTransTypePK().getWtFuTypeId();          
          if(null != fuTypeId){
            WtFuType futype = wftf.find(type.getWtTransTypePK().getWtFuTypeId());

            switch (fuTypeId) {
              case 1:
                CVPTypes = futype.getFuTypeDescription();
                CVPDelimiter = " (";
                break;
              case 2:
                SWPTypes = futype.getFuTypeDescription();
                SWPDelimiter = " (";
                break;
              case 3:
                OtherTypes = futype.getFuTypeDescription()+ " ("+type.getTypeDetail()+")";
                break;
              case 11:
                CVPTypes = CVPTypes + CVPDelimiter + futype.getFuTypeDescription();
                CVPDelimiter = ", ";
                break;
              case 13:
                CVPTypes = CVPTypes + CVPDelimiter + futype.getFuTypeDescription();
                CVPDelimiter = ", ";
                break;
              case 14:
                CVPTypes = CVPTypes + CVPDelimiter + futype.getFuTypeDescription();
                CVPDelimiter = ", ";
                break;
              case 21:
                SWPTypes = SWPTypes + SWPDelimiter + futype.getFuTypeDescription();
                SWPDelimiter = ", ";
                break;
              case 22:
                SWPTypes = SWPTypes + SWPDelimiter + futype.getFuTypeDescription();
                SWPDelimiter = ", ";
                break;
            }
          }
        }        
      }//end if
     
      if(!CVPTypes.isEmpty() && CVPTypes.contains("(")){
        CVPTypes = CVPTypes + ")";
      }
      if(!SWPTypes.isEmpty() && SWPTypes.contains("(")){
        SWPTypes = SWPTypes + ")";
      }
      if (!CVPTypes.isEmpty() && !SWPTypes.isEmpty() && !OtherTypes.isEmpty()){
        fuTypes = SWPTypes + ", " + CVPTypes + ", and " + OtherTypes;
      } else if (!CVPTypes.isEmpty() && !SWPTypes.isEmpty()) {
        fuTypes = SWPTypes + " and " + CVPTypes;
      } else if (!CVPTypes.isEmpty() && !OtherTypes.isEmpty()) {
        fuTypes = CVPTypes + " and " + OtherTypes;
      } else if (!SWPTypes.isEmpty() && !OtherTypes.isEmpty()) {
        fuTypes = SWPTypes + " and " + OtherTypes;
      } else if (!SWPTypes.isEmpty()) {
        fuTypes = SWPTypes;
      } else if (!CVPTypes.isEmpty()) {
        fuTypes = CVPTypes;
      } else if (!OtherTypes.isEmpty()) {
        fuTypes = OtherTypes;
      }

      return fuTypes;
    }

    //Generate Trans Number
    private String buildTransNum(WtTrans proposal) {
        WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
        String sellerNames="",buyerNames="",split="",transNum="";
        Iterator<WtAgency> sellersIt;
        Iterator<WtBuyer> buyersIt;
        WtAgency seller;
        WtBuyer buyer;
        if(proposal.getWtSellerCollection()!=null){
            sellersIt = proposal.getWtSellerCollection().iterator();
            if (sellersIt != null){
                split="";
                sellerNames="";
                while (sellersIt.hasNext()){
                    seller = sellersIt.next();
                    if(seller.getAgencyCode()!=null){
                        sellerNames = sellerNames + split + seller.getAgencyCode();
                        split="/";
                    }
                }
            }
        }
        if(proposal.getWtBuyerCollection()!=null){
            buyersIt = proposal.getWtBuyerCollection().iterator();
            if (buyersIt != null){
                split="";
                buyerNames="";
                while (buyersIt.hasNext()){
                    buyer = buyersIt.next();
                    if(buyer.getWtAgency() == null){
                        if(buyer.getWtBuyerPK()!=null&&buyer.getWtBuyerPK().getWtAgencyId()!=null){
                            WtAgency agency = waf.find(buyer.getWtBuyerPK().getWtAgencyId());
                            buyer.setWtAgency(agency);
                        }
                    }
                    if(buyer.getWtAgency() != null){
                        if(buyer.getWtAgency().getAgencyCode()!= null){
                            buyerNames = buyerNames + split + buyer.getWtAgency().getAgencyCode();
                            split="/";
                        }
                    }
                }
            }
        }
        transNum = sellerNames+"-"+buyerNames+"-"+proposal.getTransYear();
        return transNum;
    }

    public void resubmit(HttpServletRequest request, HttpServletResponse response) throws IOException, NullPointerException, Exception{

        JSONObject jsonResponse = new JSONObject();
        try{
          if(!UserContext.getInstance().isLoggedIn()){
            throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
          }
          ServletRequestUtil requestUtil = new ServletRequestUtil(request);
          WtChangeLogFacade wclf = (WtChangeLogFacade)appContext.getBean(WtChangeLogFacade.class.getSimpleName());
          WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
          WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
          AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
          String userName = buildUserName(user);
          WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));

          if (proposal == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
          }

          // Change Status back to Submit
          proposal.setUpdatedById(user.getUserId());
          proposal.setWtStatusFlag(wsff.find(Status.SUBMITTED));
          wtf.edit(proposal);

        // User input for Change Log
//        WtChangeLog changeLog = new WtChangeLog();
//        changeLog.setWtTransId(requestUtil.getInt("wtTransId"));
//        changeLog.setChangeUser(userName);
//        changeLog.setChangeLog(requestUtil.getString("changeLog"));
//        changeLog.setCreatedById(user.getUserId());
//        wclf.create(changeLog);

        // Combine user change log and System record
          WtChangeLog changeLog = new WtChangeLog();
//          String changeStr = "User Submit Notes:<br />";
          String changeStr = "<br />";
          changeStr += requestUtil.getString("changeLog");
//          changeStr += "<br /><br />";
//          changeStr += "Changes of Fields:<br />";
//
//          String lastChange = compareLastSubmit(proposal);
//          changeStr +=lastChange;

          changeLog.setWtTransId(requestUtil.getInt("wtTransId"));
          changeLog.setChangeUser(userName);
          changeLog.setChangeLog(requestUtil.getString("changeLog"));
          changeLog.setCreatedById(user.getUserId());
//          changeLog.setChangeField(lastChange);
          wclf.create(changeLog);
          setStatusTrack(proposal,user);

          //Send Email when re-submit
          Map<String, String> emailConfig = getEmailConfigMap();
          String toEmail = emailConfig.get("manager.address");
          String adminEmail = emailConfig.get("admin.address");
          String ownerEmail = getOwnerEmail(proposal);
          List<String> ccEmails = new ArrayList<>();
          ccEmails.addAll(getSeniorEmails());          
          ccEmails.add(toEmail);
          ccEmails.add(adminEmail);
          ccEmails.add(ownerEmail);
          ccEmails.addAll(getAgencyEmails(getSellerAgency(proposal)));          
          String subject = getSellerName(proposal) +" re-submitted a water transfer proposal";
          sendEmail(toEmail,ccEmails,subject,buildResubmitMsg(proposal,changeStr));
//          buildResubmitMsg(proposal,changeStr);

          createTransTrack(proposal, user);

          jsonResponse.put("data",changeLog.toMap()).put("success",true);
        }catch(Exception ex){
          jsonResponse.put("success", false).put("error", ex.getMessage());
          if (ex instanceof MyException){
            jsonResponse.put("callback", ((MyException)ex).getCallback());
          }
        }
        finally{
          response.getWriter().write(jsonResponse.toString());
        }
    }

    //Build Resubmit Email Msg
    private String buildResubmitMsg(WtTrans proposal, String changeStr){
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = new Date();
      StringBuilder emailMsg = new StringBuilder();
//      emailMsg.append("Proposal #").append(proposal.getWtTransId());
//      emailMsg.append(" submitted by ");
//      emailMsg.append(getSellerName(proposal)).append(".<br />");
//      emailMsg.append("Buyers are ").append(getBuyerName(proposal)).append(".<br />");
//      emailMsg.append("Transfer Type(s) are ").append(getTransferType(proposal)).append(".<br /><br />");
//      emailMsg.append("Seller/Buyer modified fields as below:<br />");

      emailMsg.append(getSellerName(proposal)).append(" on ");
	    emailMsg.append(df.format(date)).append(" re-submitted a water transfer proposal for ");
	    emailMsg.append(getTransferType(proposal)).append(" using ");
	    emailMsg.append(getFacilities(proposal)).append(" facilities.  Edits were made in these section: </br>");

      emailMsg.append(changeStr);

      return emailMsg.toString();
    }

    // System record for Change Log
    private void systemSaveChangeLog(Integer transId, Integer userId, String changeStr) throws Exception{
      WtChangeLogFacade wclf = (WtChangeLogFacade)appContext.getBean(WtChangeLogFacade.class.getSimpleName());
      WtChangeLog systemLog = new WtChangeLog();
      systemLog.setWtTransId(transId);
      systemLog.setChangeUser("System");
      systemLog.setCreatedById(userId);
      systemLog.setChangeLog(changeStr);
      wclf.create(systemLog);
    }

    private String compareLastSubmit(WtTrans proposal) throws Exception{
      WtTransTrackFacade wttf = (WtTransTrackFacade)appContext.getBean(WtTransTrackFacade.class.getSimpleName());
      Map<String,String> resultMap = new HashMap<>();
      String resultStr = "";

      WtTransTrack transTrack = new WtTransTrack();
      transTrack.setWtTransId(proposal.getWtTransId());
      List<WtTransTrack> transTrackList = wttf.findAll(transTrack);
      int length = transTrackList.size();
      if (length > 0){
        WtTransTrack lastTrack = transTrackList.get(length-1);
        String xmlStr = lastTrack.getTransXml();
        WtTrans lastTrans = (WtTrans)JAXBHelper.parseXMLContext(xmlStr, WtTrans.class);
        if(lastTrans != null){
          resultMap = proposal.getCompareMap(lastTrans);
        }
      }

      for(Map.Entry entry: resultMap.entrySet()){
        String fieldName = (String) entry.getKey();
        resultStr += Trans.transFieldMap.get(fieldName) + ": "+entry.getValue()+"<br />\n";
      }

      return resultStr;
    }

    private void createTransTrack(WtTrans proposal, AppUser user) throws Exception{
      WtTransTrackFacade wttf = (WtTransTrackFacade)appContext.getBean(WtTransTrackFacade.class.getSimpleName());
      String userName = buildUserName(user);

      String xmlStr = proposal.xmlSerialization();
      WtTransTrack transTrack = new WtTransTrack();
      transTrack.setWtTransId(proposal.getWtTransId());
      transTrack.setTransXml(xmlStr);
      transTrack.setCreatedBy(userName);
      wttf.create(transTrack);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Proposal Change Status">
    @RequestMapping("/review")
    public void reviewProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.UREVIEW);
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        int userId = INVALID_INTEGER;
        if(user != null) { userId =  user.getUserId(); }

        WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));
        if (proposal == null || statusFlag == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        proposal.setUpdatedById(userId);
        proposal.setWtStatusFlag(statusFlag);

        wtf.edit(proposal);
        setStatusTrack(request, response);
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

    @RequestMapping("/complete")
    public void completeProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.TCOMPLETE);
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        int userId = INVALID_INTEGER;
        if(user != null) { userId =  user.getUserId(); }

        WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));
        if (proposal == null || statusFlag == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        proposal.setUpdatedById(userId);
        proposal.setWtStatusFlag(statusFlag);

        wtf.edit(proposal);
        setStatusTrack(request, response);
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/approve")
    public void approveProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.PAPPROVED);
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        int userId = INVALID_INTEGER;
        if(user != null) { userId =  user.getUserId(); }

        WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));
        if (proposal == null || statusFlag == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        proposal.setUpdatedById(userId);
        proposal.setWtStatusFlag(statusFlag);

        wtf.edit(proposal);
        setStatusTrack(request, response);
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

//    @RequestMapping("/suspend")
//    public void suspendProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException
//    {
//      JSONObject jsonResponse = new JSONObject();
//      try{
//        if(!UserContext.getInstance().isLoggedIn()){
//          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
//        }
//        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
//        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
//        WtStatusFlag statusFlag = wsff.find(Status.SUSPEND);
//        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
//        int userId = INVALID_INTEGER;
//        if(user != null) { userId =  user.getUserId(); }
//
//        WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));
//        if (proposal == null || statusFlag == null){
//          response.sendError(HttpServletResponse.SC_NOT_FOUND);
//        }
//
//        proposal.setUpdatedById(userId);
//        proposal.setWtStatusFlag(statusFlag);
//
//        wtf.edit(proposal);
//        setStatusTrack(request, response);
//        jsonResponse.put("success",true);
//      }catch(Exception ex){
//        jsonResponse.put("error", ex.getMessage()).put("success",false);
//        if (ex instanceof MyException){
//          jsonResponse.put("callback", ((MyException)ex).getCallback());
//        }
//      }finally{
//        response.getWriter().write(jsonResponse.toString());
//      }
//    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Status Track Methods">
    @RequestMapping("/track")
    public void trackProposal(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.TPROGRESS);
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        int userId = INVALID_INTEGER;
        if(user != null) { userId =  user.getUserId(); }

        WtTrans proposal = wtf.find(requestUtil.getInt("wtTransId"));
        if (proposal == null || statusFlag == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        proposal.setUpdatedById(userId);
        proposal.setWtStatusFlag(statusFlag);

        wtf.edit(proposal);
        setStatusTrack(request, response);
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    private void  setStatusTrack(WtTrans proposal, AppUser user){
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        WtStatusTrack statusTrack = new WtStatusTrack();
        Integer transId = proposal.getWtTransId();
        String userName = buildUserName(user);

        statusTrack.setWtTransId(transId);
        statusTrack.setStatusName(proposal.getWtStatusFlag().getStatusName());
        statusTrack.setStatusDescription(proposal.getWtStatusFlag().getStatusDescription());
        statusTrack.setStatusDate(Calendar.getInstance().getTime());
        statusTrack.setStatusComments("System Record");
        statusTrack.setUsername(userName);
        statusTrack.setCreatedById(user.getUserId());

        wstf.create(statusTrack);
    }

    @RequestMapping("/statusTrack")
    public ModelAndView setStatusTrack(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ParseException, MalformedURLException, EmailException{
      LoggedInCheck(request, response);
      ModelAndView mv = new ModelAndView("proposal/StatusTrack");
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
      WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
      WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
      WtTrackFileFacade wtff = (WtTrackFileFacade)appContext.getBean(WtTrackFileFacade.class.getSimpleName());
      WtReviewerCommentsFacade wrcf = (WtReviewerCommentsFacade)appContext.getBean(WtReviewerCommentsFacade.class.getSimpleName());
      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      Integer transId = requestUtil.getInt("wtTransId");
      WtTrans proposal = wtf.find(transId);
      String userName = buildUserName(user);
      if (proposal == null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      WtStatusTrack statusTrack = new WtStatusTrack();
      statusTrack.setWtTransId(transId);
      statusTrack.setUsername(userName);
      statusTrack.setCreatedById(user.getUserId());
      if(requestUtil.getString("wtStatusFlagId").isEmpty()){
          statusTrack.setStatusName(proposal.getWtStatusFlag().getStatusName());
          statusTrack.setStatusDescription(proposal.getWtStatusFlag().getStatusDescription());
      } else {
          wsff.set("wtStatusFlagId", requestUtil.getInt("wtStatusFlagId"));
          WtStatusFlag statusFlag = wsff.find();
          proposal.setWtStatusFlag(statusFlag);
          statusTrack.setStatusName(statusFlag.getStatusName());
          statusTrack.setStatusDescription(statusFlag.getStatusDescription());
          wtf.edit(proposal);
      }
//        statusTrack.setSubStatus(requestUtil.getString("subStatus"));
      if(requestUtil.getString("statusDate").isEmpty()){
          statusTrack.setStatusDate(Calendar.getInstance().getTime());
      } else {
          statusTrack.setStatusDate(df.parse(requestUtil.getString("statusDate")));
      }
      if(requestUtil.getString("statusComments").isEmpty()){
          statusTrack.setStatusComments("System Record");
      } else {
          statusTrack.setStatusComments(requestUtil.getString("statusComments"));
      }

      wstf.create(statusTrack);

      // If Status Change to Under Review, Send Email to reviewers
      String swpaoReviewer = proposal.getSwpaoReviewer();
//      String regionReviewer = proposal.getRegionReviewer();
      String ciReviewer = proposal.getCiReviewer();
      String gsReviewer = proposal.getGsReviewer();
      String rrReviewer = proposal.getRrReviewer();
//      String usbrReviewer = proposal.getUsbrReviewer();
      String subject = "Water Transfer Proposal";
      String emailMsg = "A new or update proposal has submitted to the Department. Please log in to WTIMS to review the proposal.";
      List<String> reviewerEmailList = new ArrayList<>();
      String emailSuccess = "false";
      if (!StringUtil.isEmpty(swpaoReviewer)){
        reviewerEmailList.add(swpaoReviewer.trim());
      }
      if (!StringUtil.isEmpty(ciReviewer)){
        reviewerEmailList.add(ciReviewer.trim());
      }
      if (!StringUtil.isEmpty(gsReviewer)){
        reviewerEmailList.add(gsReviewer.trim());
      }
      if (!StringUtil.isEmpty(rrReviewer)){
        reviewerEmailList.add(rrReviewer.trim());
      }
//      if (!StringUtil.isEmpty(usbrReviewer)){
//        reviewerEmailList.add(usbrReviewer.trim());
//      }
      if (requestUtil.getInt("wtStatusFlagId") == (Status.UREVIEW) && (reviewerEmailList.size()>0)){
        if(sendEmail(reviewerEmailList,subject,emailMsg)){
          emailSuccess = "true";
        }
      }

//        wstf.set("wtTransId", transId);
//        List<WtStatusTrack> statusTrackList = wstf.findAll();
      List<WtStatusTrack> statusTrackList = wstf.select("SELECT WT.* FROM WT_STATUS_TRACK WT WHERE WT_TRANS_ID="+transId+" ORDER BY STATUS_DATE",com.gei.entities.WtStatusTrack.class);
//        List<WtTrackFile> trackFile = wtff.select("SELECT WT.* FROM WT_TRACK_FILE WT WHERE WT_TRANS_ID="+transId+" ORDER BY CREATED_DATE", com.gei.entities.WtTrackFile.class);
      List<WtReviewerComments> reviewerComments = wrcf.select("SELECT WT.* FROM WT_REVIEWER_COMMENTS WT WHERE WT_TRANS_ID="+transId+" ORDER BY CREATED_DATE", com.gei.entities.WtReviewerComments.class);
//        if(proposal.getWtTrackFileCollection() != null)
//        {
//            mv.addObject("trackFile",trackFile);
//            getTrackFileInformation(mv,proposal.getWtTrackFileCollection());
//        }
      response.getWriter().write(emailSuccess);
      mv.addObject("statusTrackList", statusTrackList);
      mv.addObject("reviewerComments", reviewerComments);
      return mv;
    }

    @RequestMapping("/changeStatus")
    public void addStatusTrack(HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Integer transId = requestUtil.getInt("wtTransId");
        WtTrans proposal = wtf.find(transId);
        String userName = buildUserName(user);
        if (proposal == null){
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        WtStatusTrack statusTrack = new WtStatusTrack();
        statusTrack.setWtTransId(transId);
        statusTrack.setUsername(userName);
        statusTrack.setCreatedById(user.getUserId());
        if(requestUtil.getString("wtStatusFlagId").isEmpty()){
          statusTrack.setStatusName(proposal.getWtStatusFlag().getStatusName());
          statusTrack.setStatusDescription(proposal.getWtStatusFlag().getStatusDescription());
        } else {
          wsff.set("wtStatusFlagId", requestUtil.getInt("wtStatusFlagId"));
          WtStatusFlag statusFlag = wsff.find();
          proposal.setWtStatusFlag(statusFlag);
          statusTrack.setStatusName(statusFlag.getStatusName());
          statusTrack.setStatusDescription(statusFlag.getStatusDescription());
          wtf.edit(proposal);
        }

        if(requestUtil.getString("statusDate").isEmpty()){
          statusTrack.setStatusDate(Calendar.getInstance().getTime());
        } else {
          statusTrack.setStatusDate(df.parse(requestUtil.getString("statusDate")));
        }
        if(requestUtil.getString("statusComments").isEmpty()){
          statusTrack.setStatusComments("System Record");
        } else {
          statusTrack.setStatusComments(requestUtil.getString("statusComments"));
        }

        wstf.create(statusTrack);

        // If Status Change to Under Review/incomplete/complete, Send Email to reviewers
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        int noOfDays = 14;
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date next2Week = calendar.getTime();
        String swpaoNum = proposal.getSwpaoContractNum();
        String sellerName = getSellerName(proposal);
        String ownerEmail = getOwnerEmail(proposal);
        Map<String, String> emailConfig = getEmailConfigMap();
        String adminEmail = emailConfig.get("admin.address");
        String managerEmail = emailConfig.get("manager.address");        
        String swpaoReviewer = proposal.getSwpaoReviewer();
        String ciReviewer = proposal.getCiReviewer();
        String gsReviewer = proposal.getGsReviewer();
        String rrReviewer = proposal.getRrReviewer();
//        String usbrReviewer = proposal.getUsbrReviewer();
        
        // Build reviewer Email List
        List<String> reviewerEmails = new ArrayList<>();
        if (swpaoNum == null){ swpaoNum = ""; }
        String baseSubject = "SWPAO #"+swpaoNum+" water transfer proposal";
        String reviewerMsg = "";
        String emailSuccess = "false";
        if (!StringUtil.isEmpty(swpaoReviewer)){
          reviewerEmails.add(swpaoReviewer.trim());
          reviewerMsg += "<li>Seller/Buyers/General Information - "+swpaoReviewer.trim()+"</li>";
        }
        if (!StringUtil.isEmpty(ciReviewer)){
          reviewerEmails.add(ciReviewer.trim());
          reviewerMsg += "<li>Cropland idling and crop shifting - "+ciReviewer.trim()+"</li>";
        }
        if (!StringUtil.isEmpty(gsReviewer)){
          reviewerEmails.add(gsReviewer.trim());
          reviewerMsg += "<li>Groundwater substitution - "+gsReviewer.trim()+"</li>";
        }
        if (!StringUtil.isEmpty(rrReviewer)){
          reviewerEmails.add(rrReviewer.trim());
          reviewerMsg += "<li>Reservoir release - "+rrReviewer.trim()+"</li>";
        }
//        if (!StringUtil.isEmpty(usbrReviewer)){
//          reviewerEmails.add(usbrReviewer.trim());
//          reviewerMsg += "<li>USBR - "+usbrReviewer.trim()+"</li>";
//        }

        // Build CC include all Seller Email List
        List<String> sellerEmails = getAgencyEmails(getSellerAgency(proposal));
        sellerEmails.add(ownerEmail);
        
        if(reviewerEmails.size()>0){
          if (requestUtil.getInt("wtStatusFlagId") == (Status.UREVIEW)){
            String subject = "Start review of "+baseSubject+" from "+sellerName;
            String emailMsg = "All,</br></br>";
            emailMsg += "Please complete the review of SWPAO #"+swpaoNum+" water transfer proposal from "
                      + sellerName + " no later than " + df.format(next2Week) + ".</br></br><ul>";
            emailMsg += reviewerMsg;
            emailMsg += "</ul></br>Thank you very much.</br></br>";
            emailMsg += "Anna</br>";
//            sellerEmails.add(ownerEmail);
            sellerEmails.add(managerEmail);
            sellerEmails.add(adminEmail);
            sellerEmails.addAll(getSeniorEmails());
            if(sendEmail(reviewerEmails,sellerEmails,subject,emailMsg)){
              emailSuccess = "true";
            }
          } else if (requestUtil.getInt("wtStatusFlagId") == (Status.INCOMPLETE)){
            String subject = baseSubject+" is incomplete";
            String emailMsg = "Your SWPAO #"+swpaoNum+" water transfer proposal has been reviewed and determined  " +
                              "that additional information is needed. Please login WTIMS to read our comments, " +
                              "make edits, and re-submit. If you have any questions, please don't hesitate to contact " +
                               swpaoReviewer + ".</br></br>";
//            sellerEmails.add(ownerEmail);
            reviewerEmails.add(managerEmail);
            reviewerEmails.add(adminEmail);
            reviewerEmails.addAll(getSeniorEmails());
            if(sendEmail2(sellerEmails,reviewerEmails,subject,emailMsg)){
              emailSuccess = "true";
            }
          } else if (requestUtil.getInt("wtStatusFlagId") == (Status.PCOMPLETE)){
            String subject = baseSubject+" is complete";
            String emailMsg = "DWR reviewed your SWPAO #"+swpaoNum+" water transfer proposal and your proposal is complete. " +
                              "DWR is developing the draft water transfer agreement.</br></br>";
//            sellerEmails.add(ownerEmail);
            reviewerEmails.add(managerEmail);
            reviewerEmails.add(adminEmail);
            reviewerEmails.addAll(getSeniorEmails());
            if(sendEmail2(sellerEmails,reviewerEmails,subject,emailMsg)){
              emailSuccess = "true";
            }
          } else if (requestUtil.getInt("wtStatusFlagId") == (Status.CEXECUTED)){
            String subject = baseSubject+" is executed";
            String emailMsg = "SWPAO #"+swpaoNum+" water transfer agreement is executed. " +
                              "Please provide the notice to process from your buyers to DWR.</br></br>";
//            sellerEmails.add(ownerEmail);
            reviewerEmails.clear();
            reviewerEmails.add(managerEmail);
            reviewerEmails.add(adminEmail);
            reviewerEmails.addAll(getSeniorEmails());
            if(sendEmail2(sellerEmails,reviewerEmails,subject,emailMsg)){
              emailSuccess = "true";
            }
          }
        }

        jsonResponse = new JSONObject(statusTrack.toMap());
        jsonResponse.put("success", true);
        jsonResponse.put("emailSuccess", emailSuccess);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/proposalStatus/{transId}")
    public ModelAndView getProposalStatus(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      ModelAndView mv = null;
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          response.sendRedirect(request.getContextPath());
          return mv;
        }
        mv = new ModelAndView("proposal/StatusTrack");
        WtStatusFlagFacade wstf = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtTrackFileFacade wtff = (WtTrackFileFacade)appContext.getBean(WtTrackFileFacade.class.getSimpleName());
        Collection<WtStatusTrack> statusTrackList = wstf.select("SELECT WT.* FROM WT_STATUS_TRACK WT WHERE WT_TRANS_ID="+transId+" ORDER BY STATUS_DATE",com.gei.entities.WtStatusTrack.class);
        Collection<WtTrackFile> trackFile = wtff.select("SELECT WT.* FROM WT_TRACK_FILE WT WHERE WT_TRANS_ID="+transId+" ORDER BY CREATED_DATE", com.gei.entities.WtTrackFile.class);
        if(trackFile.size()>0)
        {
            mv.addObject("trackFile",trackFile);
            getTrackFileInformation(mv,trackFile);
        }
        mv.addObject("statusTrackList", statusTrackList);
      }catch(Exception ex){
       response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
      }
      return mv;
    }

    public void saveStatusTrack(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        if(!requestUtil.getString("wtStatusTrackId").isEmpty()){
            WtStatusTrack statusTrack = wstf.find(requestUtil.getInt("wtStatusTrackId"));
            if(!requestUtil.getString("statusDate").isEmpty()){
                statusTrack.setStatusDate(df.parse(requestUtil.getString("statusDate")));
            }
            statusTrack.setStatusComments(requestUtil.getString("statusComments"));
            wstf.edit(statusTrack);
        }
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    public void removeReportTracker(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        int trackFileId = requestUtil.getInt("trackId");
        WtTrackFileFacade wtff = (WtTrackFileFacade)appContext.getBean(WtTrackFileFacade.class.getSimpleName());
        wtff.set("wtTrackFileId", trackFileId);
        WtTrackFile wtf = wtff.find();
        wtff.remove(wtf);
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }

    @RequestMapping("/statusTrack/{transId}")
    public void getStatusTrack(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws IOException{
      JSONObject jsonResponse = new JSONObject();
      try{
        if(!UserContext.getInstance().isLoggedIn()){
          throw new MyException("","ErrorPromptsHandler.SESSIONTIMEOUT");
        }
        WtStatusTrackFacade wstf = (WtStatusTrackFacade)appContext.getBean(WtStatusTrackFacade.class.getSimpleName());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        wstf.set("wtTransId", transId);
        List<WtStatusTrack> wsts = wstf.findAll();

        JSONObject tmpJson = new JSONObject();
        jsonResponse.put("data",new JSONArray());
        for(WtStatusTrack wst : wsts){
            tmpJson = new JSONObject(wst.toMap());
            tmpJson.put("statusDate", df.format(wst.getStatusDate()));
            jsonResponse.optJSONArray("data").put(tmpJson);
        }
        jsonResponse.put("success",true);
      }catch(Exception ex){
        jsonResponse.put("error", ex.getMessage()).put("success",false);
        if (ex instanceof MyException){
          jsonResponse.put("callback", ((MyException)ex).getCallback());
        }
      }finally{
        response.getWriter().write(jsonResponse.toString());
      }
    }
    //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Send Email and other Utils">
  public void sendEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      if (!AuthenticationController.IsLoggedIn(request)) {
        throw new MyException("", "ErrorPromptsHandler.SESSIONTIMEOUT");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtMessageFacade wmf = (WtMessageFacade) appContext.getBean(WtMessageFacade.class.getSimpleName());
      JSONObject jsonData = new JSONObject(requestUtil.getString("jsondata"));
      WtMessage msg = new WtMessage();
      wmf.edit(msg.loadProperties(jsonData.getJSONMap()));
      jsonResponse.put("success", true);
    } catch (Exception ex) {
      jsonResponse.put("success", false).put("error", ex.getMessage());
      if (ex instanceof MyException) {
        jsonResponse.put("callback", ((MyException) ex).getCallback());
      }
    } finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

  private boolean sendEmail(String subject, String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }

      HtmlEmail email = new HtmlEmail();
      String ccStr = emailConfig.get("cc.address");
      String[] ccs = ccStr.split(";");
      email.setHostName(emailConfig.get("host"));
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setSendPartial(true);
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addTo(emailConfig.get("manager.address"));
      if (ccs != null) {
        for (String cc : ccs) {
          email.addCc(cc.trim());
        }
      }
      if (emailConfig.get("bcc.address") != null && !emailConfig.get("bcc.address").isEmpty()) {
        email.addBcc(emailConfig.get("bcc.address"));
      }
      email.setSubject(subject);
      email.setHtmlMsg(emailMsg);

      new Thread(new MailerRunnable(email)).start();
      return true;
    } catch (EmailException ex) {
      Logger.getLogger(ProposalController.class).log(Logger.Level.WARN, ex.getMessage());
      return false;
    }
  }

  private boolean sendEmail(List<String> toEmailList, String subject, String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
//      email.setDebug(true);
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      if (toEmailList.size()>0){
        for (String toEmail : toEmailList) {
          email.addTo(toEmail.trim());
        }
      }
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addCc(emailConfig.get("admin.address"));
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

  private boolean sendEmail(String toEmail, String subject, String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
      email.setDebug(true);
//      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addTo(emailConfig.get("manager.address"));
      email.addCc(toEmail.trim());
      email.addCc(emailConfig.get("admin.address"));
      email.setSubject(subject);
      email.setHtmlMsg(emailMsg);

      new Thread(new MailerRunnable(email)).start();
      return true;
    } catch (EmailException ex) {
      Logger.getLogger(ProposalController.class).log(Logger.Level.WARN, ex.getMessage());
      return false;
    }
  }

  private boolean sendEmail(List<String> toEmailList,List<String> ccEmailList,String subject,String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
//      email.setDebug(true);
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setFrom(emailConfig.get("manager.address"));
      if (null != toEmailList && toEmailList.size()>0){
        for (String toEmail : toEmailList) {
          email.addTo(toEmail.trim());
        }
      }
      // CC to Owner, all Seller Contacts and Administrator
//      email.addCc(ccEmail);
//      email.addCc(emailConfig.get("admin.address"));
      if (null != ccEmailList && ccEmailList.size()>0){
        for (String aCcEmail : ccEmailList) {
          email.addCc(aCcEmail.trim());
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

  private boolean sendEmail(String toEmail,List<String> ccEmailList,String subject,String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
//      email.setDebug(true);
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addTo(toEmail);
      if (null != ccEmailList && ccEmailList.size()>0){
        for (String ccEmail : ccEmailList) {
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

  private boolean sendEmail2(List<String> toEmailList,List<String> ccEmailList,String subject,String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      String isAutomatedTestString = emailConfig.get("isAutomatedTest");
      if (isAutomatedTestString != null && Boolean.parseBoolean(isAutomatedTestString)){
        return true;
      }
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
//      email.setDebug(true);
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      if (null != toEmailList && toEmailList.size()>0){
        for (String toEmail : toEmailList) {
          email.addTo(toEmail.trim());
        }
      }
      if (null != ccEmailList && ccEmailList.size()>0){
        for (String ccEmail : ccEmailList) {
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

  private String buildUserName(AppUser user) {
    String userName = "";
    if (user.getUsername() == null) {
      return userName;
    }
    if (user.getWtContact() == null) {
      return user.getUsername();
    }
    if (user.getWtContact().getFirstName() != null) {
      userName += user.getWtContact().getFirstName() + " ";
    }
    if (user.getWtContact().getLastName() != null) {
      userName += user.getWtContact().getLastName();
    }
    if (user.getWtContact().getFirstName() == null && user.getWtContact().getLastName() == null && user.getUsername() != null) {
      userName += user.getUsername();
    }

    return userName;
  }

  public ModelAndView emailLog(HttpServletRequest request, HttpServletResponse response) {
    LoggedInCheck(request, response);
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    ModelAndView mv = new ModelAndView("templates/emailLog");
    WtAgencyFacade waf = (WtAgencyFacade) appContext.getBean(WtAgencyFacade.class.getSimpleName());
    WtMessageFacade wmf = (WtMessageFacade) appContext.getBean(WtMessageFacade.class.getSimpleName());
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    JSONObject contactMessageLog;
    JSONArray messageLogArray = new JSONArray();
    Collection<WtContact> contactList = new ArrayList();
    try {
      contactList = waf.select("SELECT * from WT_CONTACT where IS_ACTIVE = 1 and WT_AGENCY_ID=" + requestUtil.getString("wtAgencyId"), com.gei.entities.WtContact.class);
    } catch (Exception ex) {
    }
    for (WtContact contact : contactList) {
      List<WtMessage> colMsg = wmf.select("SELECT * from WT_MESSAGE where WT_CONTACT_ID=" + contact.getWtContactId() + " and WT_TRANS_ID = " + requestUtil.getInt("wtTransId") + "ORDER BY CREATED_DATE ASC", com.gei.entities.WtMessage.class);
      contactMessageLog = new JSONObject(contact.toMap());
      JSONArray appuser = new JSONArray();
      for (WtMessage messages : colMsg) {
        int createby = messages.getCreatedBy();
        AppUser user = new AppUser();
        user.setUserId(createby);
        user = auf.find(user);
        JSONObject msgLog = new JSONObject(messages.toMap());
        msgLog.put("firstName", user.getWtContact().getFirstName());
        msgLog.put("lastName", user.getWtContact().getLastName());
        msgLog.put("email", user.getWtContact().getEmail());
        msgLog.put("createdDate", dateFormatter(msgLog.optString("createdDate")));
        appuser.put(msgLog);
      }
      contactMessageLog.put("MESSAGE_LOG", appuser);
      if (colMsg.iterator().hasNext()) {
        contactMessageLog.put("subject", colMsg.iterator().next().getSubject());
      }
      contactMessageLog.put("wtAgencyId", contact.getWtAgency().getWtAgencyId());
      messageLogArray.put(contactMessageLog);
    }
    mv.addObject("jsonarray", messageLogArray);
    return mv;
  }

  public String dateFormatter(String date) {
    String newDate = "";
    try {
      Date df = new SimpleDateFormat("yyyy-MM-dd k:mm:ss").parse(date);
      newDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(df);
    } catch (ParseException e) {
    }
    return newDate;
  }

  public String dateFormat(String date, String style) throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat(style);
    SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
    if (!date.isEmpty()) {
      java.util.Date formatDate = df.parse(date);
      return dt1.format(formatDate);
    } else {
      return "";
    }
  }

  public String dateFormat(String date) throws ParseException {
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
    if (!date.isEmpty()) {
      java.util.Date format = dt.parse(date);
      return dt1.format(format);
    } else {
      return "";
    }
  }

  private WtAgency getSellerAgency(WtTrans wt) {
    WtAgency sellerAgency = null;
    Iterator<WtAgency> sellerIt;

    if (wt.getWtSellerCollection() != null) {
      sellerIt = wt.getWtSellerCollection().iterator();
      if (sellerIt != null) {
        while (sellerIt.hasNext()) {
          sellerAgency = sellerIt.next();
        }
      }
    }//end if
    return sellerAgency;
  }

  private String getSellerName(WtTrans wt) {
    String comma, sellerName = "";
    Iterator<WtAgency> sellerIt;

    if (wt.getWtSellerCollection() != null) {
      WtAgency seller = null;
      sellerIt = wt.getWtSellerCollection().iterator();
      if (sellerIt != null) {
        comma = "";
        sellerName = "";
        while (sellerIt.hasNext()) {
          seller = sellerIt.next();
          // Use Agency Name
          sellerName = sellerName + comma + seller.getAgencyFullName();
          comma = ",";
        }
      }
    }//end if
    return sellerName;
  }

  private List<String> getAgencyEmails(WtAgency agency) throws Exception {
    Integer wtAgencyId = agency.getWtAgencyId();
    List<String> agencyEmails = new ArrayList<>();

    Connection conn = ConnectionContext.getConnection("WtDataSource");
    String query = String.format("SELECT * FROM CONTACT_LIST_VIEW WHERE IS_ACTIVE = 1 AND WT_AGENCY_ID = ?");
    GenericFacade.executeQuery(conn, query, Arrays.asList(wtAgencyId), new QueryDelegate(agencyEmails) {
      @Override
      public void handle(ResultSet rs) throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<String> agencyEmails = this.getListener();
        String colName = null;
        Class colType = null;
        Object value = null;
        while (rs.next()) {
          String contactEmail = "";
          for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
            colName = rsmd.getColumnName(i);
            colType = Class.forName(rsmd.getColumnClassName(i));
            value = rs.getObject(colName,colType);
            if ("EMAIL".equalsIgnoreCase(colName) && value != null) {
              contactEmail = value.toString();
              agencyEmails.add(contactEmail);
            }
          }
        }
      }
    });

    return agencyEmails;
  }

  private List<String> getSeniorEmails(){
    List<String> seniorEmailList = new ArrayList<>();
    Map<String, String> emailConfig = getEmailConfigMap();
    String seniorEmailString = emailConfig.get("senior.address");
    String[] seniorEmails = seniorEmailString.split(";");
    if (null != seniorEmails){
      seniorEmailList.addAll(Arrays.asList(seniorEmails));
    }

    return seniorEmailList;
  }

  private String getOwnerEmail(WtTrans wt) {
    String ownerEmail = "";
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    AppUser user = auf.find(wt.getCreatedById());
    if (user != null) {
      ownerEmail = user.getWtContact().getEmail();
    }
    return ownerEmail == null ? "" : ownerEmail;
  }

  private String getOwnerPhone(WtTrans wt) {
    String ownerPhoneNum = "";
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    AppUser user = auf.find(wt.getCreatedById());
    if (user != null) {
      ownerPhoneNum = user.getWtContact().getPhoneNumber();
    }
    return ownerPhoneNum == null ? "" : ownerPhoneNum;
  }

//    public void checkduplicate(HttpServletRequest request, HttpServletResponse response) throws IOException
//    {
//        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        String agencyName = requestUtil.getString("agencyName");
//
//        JSONObject jsonresponse = new JSONObject();
//        jsonresponse.put("isDuplicate",true);
//        response.getWriter().write(jsonresponse.toString());
//    }
    //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Main Function">
//    public static void main(String[] args)
//    {
//        WtTransFacade f = new WtTransFacade();
//        WtStatusFlagFacade wsff = new WtStatusFlagFacade();
//        WtStatusFlag statusFlag = wsff.find(Status.COMPLETE);
//        WtAgencyFacade waf = new WtAgencyFacade();
//        Collection<WtContact> inactiveContact = waf.select("SELECT WT.* FROM WT_CONTACT WT WHERE IS_ACTIVE=0",com.gei.entities.WtContact.class);
//
//        WtTrans t = new WtTrans();
//        t.setTransYear(2015);
//        List<WtTrans> trans = f.select("SELECT WT.* FROM WT_TRANS WT WHERE TRANS_YEAR='2015'",com.gei.entities.WtTrans.class);
//        t.setWtStatusFlag(statusFlag);
//        List<WtTrans> transRecords = f.findAll();
//        transRecords = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID="+Status.COMPLETE,com.gei.entities.WtTrans.class);
//        List<WtTrans> transRecords = f.findAll();
//        List<WtTrans> transRecords = f.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID="+Status.DONE+" ORDER BY TRANS_YEAR",com.gei.entities.WtTrans.class);
//        s.out.println(transRecords);
//        f.findAll();
//        System.out.println(f.find(15));
//    }
    //</editor-fold>
}
