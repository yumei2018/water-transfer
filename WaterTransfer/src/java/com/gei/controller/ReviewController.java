
package com.gei.controller;

import com.gei.constants.Status;
import com.gei.context.ConnectionContext;
import com.gei.context.LookupDataContext;
import com.gei.context.UserContext;
import static com.gei.controller.AuthenticationController.SESSION_KEY_USER;
import static com.gei.controller.ReportController.PROPOSAL_REPORT;
import com.gei.entities.*;
import com.gei.exception.MyException;
import com.gei.facades.*;
import com.gei.facades.GenericFacade;
import com.gei.facades.WtTransFacade;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.thread.MailerRunnable;
import com.gei.util.EntityUtil;
import com.gei.utils.ServletRequestUtil;
import gov.ca.water.watertransfer.entity.collection.AgencyCollection;
import gov.ca.water.watertransfer.entity.collection.BuyerCollection;
import gov.ca.water.watertransfer.entity.collection.ChecklistCollection;
import gov.ca.water.watertransfer.entity.collection.ReviewNoteCollection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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
@RequestMapping("/review")
public class ReviewController extends BaseController{
    @Autowired
    ApplicationContext appContext;

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        LoggedInCheckNoAjax(request,response);
        ModelAndView mv = new ModelAndView("review/index");
        return mv;
    }
    
  // <editor-fold defaultstate="collapsed" desc="Proposal Review">
  @RequestMapping("/proposalReview/{transId}")
  public ModelAndView proposalReview(@PathVariable("transId") Integer transId
                                     , HttpServletRequest request
                                     , HttpServletResponse response) throws IOException,Exception {
    ModelAndView mv = null;
    try{ 
      if (!UserContext.getInstance().isLoggedIn()) {
          response.sendRedirect(request.getContextPath());
          return mv;
      }
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtReviewNoteFacade wrnf = (WtReviewNoteFacade)appContext.getBean(WtReviewNoteFacade.class.getSimpleName());
      WtTrans transRec = wtf.find(transId);
      AttachmentController attCtr = new AttachmentController();
      Collection<WtAttachment> attachmentBI = attCtr.getAttachmentCollectionByType(transRec, "BI");
      Collection<WtAttachment> attachmentCI = attCtr.getAttachmentCollectionByType(transRec, "CI");
      Collection<WtAttachment> attachmentCIMap = attCtr.getAttachmentCollectionByType(transRec, "CI_MAP");
      Collection<WtAttachment> attachmentRV = attCtr.getAttachmentCollectionByType(transRec, "RV");
      Collection<WtAttachment> attachmentGW = attCtr.getAttachmentCollectionByType(transRec, "GW");
//      Collection<WtChecklist> checklistBI = attCtr.getChecklistByType("BI");      
//      Collection<WtChecklist> checklistCI = attCtr.getChecklistByType("CI");
//      Collection<WtChecklist> checklistCIMap = attCtr.getChecklistByType("CI_MAP");
//      Collection<WtChecklist> checklistRV = attCtr.getChecklistByType("RV");
      
      Collection<WtChecklist> checklistBI = LookupDataContext.getInstance().getBIChecklist(true);
      Collection<WtChecklist> checklistCI = LookupDataContext.getInstance().getCIChecklist(true);
      Collection<WtChecklist> checklistCIMap = LookupDataContext.getInstance().getCIMapChecklist(true);
      Collection<WtChecklist> checklistRV = LookupDataContext.getInstance().getRVChecklist(true);
      Collection<WtChecklist> checklistGW = LookupDataContext.getInstance().getGWChecklist(true);
      Collection<WtChecklist> checklistWellTrans = LookupDataContext.getInstance().getWellTransChecklist(true);
      Collection<WtChecklist> checklistWellMonit = LookupDataContext.getInstance().getWellMonitChecklist(true);
      final AppUser user = UserContext.getInstance().getUser();
      final Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = null;

//      //<editor-fold defaultstate="collapsed" desc="Querying for a Status Flag Lookup">
//        final HashMap<Integer,WtStatusFlag> statusMap = new HashMap();
//        query = String.format("SELECT %1$s.* FROM %1$s"
//                ,EntityUtil.getTableName(WtStatusFlag.class));
//        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(statusMap) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            HashMap statusFlags = this.getListener();
//            WtStatusFlag statuFlag = null;
//            while (rs.next()) {
//              statuFlag = new WtStatusFlag();
//              statuFlag.Load(rs);
//
//              statusFlags.put(statuFlag.getWtStatusFlagId(),statuFlag);
//            }
//          }
//        });
//        //</editor-fold>
        
//      //<editor-fold defaultstate="collapsed" desc="Query for the Water Transfer Proposal with validation">
//      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?"
//                ,EntityUtil.getTableName(WtTrans.class));
//
//        WtTrans proposal = new WtTrans();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(proposal) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            if (!rs.next()) {
//              throw new Exception("The water transfer id is invalid!");
//            }
//
//            WtTrans proposal = this.getListener();
//            proposal.Load(rs);
//
//            // Load Status Flag Project
//            Integer statusId = null;
//            WtStatusFlag statuFlag = new WtStatusFlag();
//            if (((statusId = rs.getInt("WT_STATUS_FLAG_ID")) != null)
//                && ((statuFlag = statusMap.get(statusId)) != null)){
//                proposal.setWtStatusFlag(statuFlag);
//            }
//
//            String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ? AND %1$s.USER_ID = ?"
//                              ,"WT_TRANS_USER");
//
//            GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId(), user.getUserId()), new QueryDelegate(proposal) {
//              @Override
//              public void handle(ResultSet rs) throws Exception {
//                if (user.isAppAccount() && !rs.next()) {
//                  throw new Exception("You are not authorized to view the water transfer proposal!");
//                }
//              }
//            });
//          }
//        });
//        //</editor-fold>
        
//      //<editor-fold defaultstate="collapsed" desc="Querying for a Agency Lookup">
//        final AgencyCollection agencies = new AgencyCollection();
//        query = String.format("SELECT %1$s.* FROM %1$s"
//                ,EntityUtil.getTableName(WtAgency.class));
//        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencies) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            AgencyCollection agencies = this.getListener();
//            WtAgency agency = null;
//            while (rs.next()) {
//              agency = new WtAgency();
//              agency.Load(rs);
//
//              agencies.add(agency);
//            }
//          }
//        });
//
//        final HashMap<Integer,WtAgency> agencyMap = new HashMap();
//        query = String.format("SELECT %1$s.* FROM %1$s"
//                ,EntityUtil.getTableName(WtAgency.class));
//        GenericFacade.executeQuery(conn, query, new ArrayList(), new QueryDelegate(agencyMap) {
//          @Override
//          public void handle(ResultSet rs) throws Exception {
//            HashMap agencies = this.getListener();
//            WtAgency agency = null;
//            while (rs.next()) {
//              agency = new WtAgency();
//              agency.Load(rs);
//
//              agencies.put(agency.getWtAgencyId(),agency);
//            }
//          }
//        });
//        //</editor-fold>

//      //<editor-fold defaultstate="collapsed" desc="Querying for a Seller">
////
////        AgencyCollection sellers = new AgencyCollection();
////        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?","WT_SELLER");
////        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(sellers) {
////          @Override
////          public void handle(ResultSet rs) throws Exception {
////            AgencyCollection sellers = this.getListener();
////            WtAgency seller = null;
////            Integer agencyId = null;
////            if (rs.next()
////               && ((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
////              && ((seller = agencies.get(agencyId)) != null)) {
////              sellers.add(seller);
////            }
////          }
////        });
//
//        query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_AGENCY_ID = %1$s.WT_AGENCY_ID WHERE %2$s.WT_TRANS_ID = ?"
//                ,EntityUtil.getTableName(WtAgency.class)
//                ,"WT_SELLER");
//        List<WtAgency> wtsellers = new ArrayList<>();
//        GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId())
//          , new QueryDelegate(wtsellers) {
//            @Override
//            public void handle(ResultSet rs) throws Exception {
//              List<WtAgency> sellers = this.getListener();
//              WtAgency p = null;
//              while (rs.next()) {
//                p = new WtAgency();
//                p.Load(rs);
//                sellers.add(p);
//              }
//            }
//          }
//        );
//
//        WtAgency seller = null;
//
//        if (!wtsellers.isEmpty()) {
//          // it's always 1 record if seller exist
//          seller = wtsellers.get(0);
//        }
//        //</editor-fold>

//      //<editor-fold defaultstate="collapsed" desc="Query for Buyers">
//      BuyerCollection buyers = new BuyerCollection();
//      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtBuyer.class));
//      GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(buyers) {
//        @Override
//        public void handle(ResultSet rs) throws Exception {
//          BuyerCollection buyers = this.getListener();
//          Integer agencyId = null;
//          WtBuyer buyer = null;
//          WtAgency agency = null;
//          while (rs.next()) {
//            buyer = new WtBuyer();
//            buyer.setSharePercent(rs.getDouble("SHARE_PERCENT"));
//
//            if (((agencyId = rs.getInt("WT_AGENCY_ID")) != null)
//                    && ((agency = agencyMap.get(agencyId)) != null)) {
//              buyer.setWtAgency(agency);
//            }
//
//            buyers.add(buyer);
//          }
//        }
//      });
//      //</editor-fold>

//      //<editor-fold defaultstate="collapsed" desc="Query for buyer contact">
//      List<WtContact> buyersContactList = LookupDataContext.getInstance().getBuyersContactLookup();
//
//      query = String.format("SELECT %1$s.* FROM %1$s JOIN %2$s ON %2$s.WT_CONTACT_ID = %1$s.WT_CONTACT_ID"
//              + " WHERE %2$s.WT_TRANS_ID = ? AND %1$s.IS_BUYERS_CONTACT = 1 ORDER BY LAST_NAME", EntityUtil.getTableName(WtContact.class), "WT_BUYERS_CONTACT");
//      List<WtContact> buyerContacts = new ArrayList<>();
//      GenericFacade.executeQuery(conn, query, Arrays.asList(proposal.getWtTransId()), new QueryDelegate(buyerContacts) {
//        @Override
//        public void handle(ResultSet rs) throws Exception {
//          List<WtContact> contacts = this.getListener();
//          WtContact p = null;
//          while (rs.next()) {
//            p = new WtContact();
//            p.Load(rs);
//            contacts.add(p);
//          }
//        }
//      }
//      );
//
//      WtContact buyersContact = null;
//
//      if (!buyerContacts.isEmpty()) {
//        // it's always 1 record if buyers contact exist
//        buyersContact = buyerContacts.get(0);
//      }
//        //</editor-fold>

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

      mv = new ModelAndView("review/proposalReview");
      mv.addObject("proposal", transRec);
//      mv.addObject("proposal", proposal);
//      mv.addObject("seller", seller);
//      mv.addObject("buyers", buyers);
//      mv.addObject("buyersContactList", buyersContactList);
//      mv.addObject("buyersContact", buyersContact);  
      mv.addObject("attachmentBI", attachmentBI);
      mv.addObject("checklistBI", checklistBI);
      mv.addObject("attachmentCI", attachmentCI);
      mv.addObject("checklistCI", checklistCI);
      mv.addObject("attachmentCIMap", attachmentCIMap);
      mv.addObject("checklistCIMap", checklistCIMap);
      mv.addObject("attachmentRV", attachmentRV);
      mv.addObject("checklistRV", checklistRV);
      mv.addObject("attachmentGW", attachmentGW);
      mv.addObject("checklistGW", checklistGW);
      mv.addObject("checklistWellTrans", checklistWellTrans);
      mv.addObject("checklistWellMonit", checklistWellMonit);
      mv.addObject("reviewNotes", reviewNotes);
    } catch (Exception ex) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    
    return mv;
  }
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Internal Notes">
  public ModelAndView getComment(HttpServletRequest request, HttpServletResponse response)
  {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("review/reviewComment");
    return mv;
  }
  
  @RequestMapping("/internalnotes")
  public ModelAndView internalNotes(@RequestParam("transId") Integer transId
                                    , @RequestParam("sectionKey") String sectionKey
                                    , HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = null;
    try {
      if (!UserContext.getInstance().isLoggedIn()) {
        response.sendRedirect(request.getContextPath());
        return mv;
      }
      final Connection conn = ConnectionContext.getConnection("WtDataSource");

      //<editor-fold defaultstate="collapsed" desc="Querying for Internal Notes">
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID=? AND SECTION_KEY=? ORDER BY %1$s.NOTE_DATE DESC"
                    , EntityUtil.getTableName(WtInternalNote.class));
      List<WtInternalNote> iNotes = new ArrayList<>();
      GenericFacade.executeQuery(conn, query, Arrays.asList(transId, sectionKey), new QueryDelegate(iNotes) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          List<WtInternalNote> iNotes = this.getListener();
          WtInternalNote note = null;

          while (rs.next()) {
            note = new WtInternalNote();
            note.Load(rs);
            iNotes.add(note);
          }
        }
      });
      //</editor-fold>

      mv = new ModelAndView("review/internalNotes");
      mv.addObject("internalNotes", iNotes);
    } catch (Exception ex) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    return mv;
  }
  
  @RequestMapping("/saveinternalnote")
  public void saveInternalNote(@RequestParam("transId") Integer transId
                               , @RequestParam("sectionKey") String sectionKey
                               , HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try{
      if ((transId == null) || (StringUtils.isEmpty(sectionKey))) {
        throw new Exception("The modification transfer id and section key cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtInternalNoteFacade winf = (WtInternalNoteFacade)appContext.getBean(WtInternalNoteFacade.class.getSimpleName());
      AppUser user = UserContext.getInstance().getUser();
      WtInternalNote node = new WtInternalNote();
      node.setWtTransId(transId);
      node.setSectionKey(sectionKey);
      node.setNote(requestUtil.getString("note"));
      node.setNoteUpdatedBy(user.getUserId());
      node.setNoteDate(Calendar.getInstance().getTime());
      winf.create(node);
      
      jsonResponse = new JSONObject(node.toMap());
      jsonResponse.put("updatedBy", user.getName());
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
  
  @RequestMapping("/deleteinternalnote")
  public void deleteInternalNote(@RequestParam("noteId") Integer noteId
                                 , HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try{
      if ((noteId == null)) {
        throw new Exception("The internal note id cannot be unassigned!");
      }

      WtInternalNoteFacade winf = (WtInternalNoteFacade)appContext.getBean(WtInternalNoteFacade.class.getSimpleName());
      AppUser user = UserContext.getInstance().getUser();
      WtInternalNote node = winf.find(noteId);
      if (node != null){
        winf.remove(node);
      }
      
      jsonResponse.put("updatedBy", user.getName());
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
  
  // <editor-fold defaultstate="collapsed" desc="Review Notes">
  @RequestMapping("/markcomplete")
  public void markComplete(@RequestParam("transId") Integer transId
          ,@RequestParam("sectionKey") String sectionKey
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try{
      if ((transId == null) || (StringUtils.isEmpty(sectionKey))) {
        throw new Exception("The modification transfer id and section key cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtReviewNoteFacade wrnf = (WtReviewNoteFacade)appContext.getBean(WtReviewNoteFacade.class.getSimpleName());
      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
      WtReviewNote node = new WtReviewNote();
      node.setWtTransId(transId);
      node.setSectionKey(sectionKey);
      List<WtReviewNote> notes = wrnf.findAll(node);
      if(notes == null || notes.isEmpty()){
        wrnf.create(node);
        node.setIsComplete(requestUtil.getInt("isComplete"));
        node.setStatusUpdatedBy(user.getUserId());
        node.setStatusDate(Calendar.getInstance().getTime());
        wrnf.edit(node);
      } else {
//        node = wrnf.find(node);        
        for(WtReviewNote note:notes){
          note.setIsComplete(requestUtil.getInt("isComplete"));
          note.setStatusUpdatedBy(user.getUserId());
          note.setStatusDate(Calendar.getInstance().getTime());
          wrnf.edit(note);
        }
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
  
  @RequestMapping("/savetechnicalnote")
  public void saveTechnicalNote(@RequestParam("transId") Integer transId
          ,@RequestParam("sectionKey") String sectionKey
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try{
      if ((transId == null) || (StringUtils.isEmpty(sectionKey))) {
        throw new Exception("The modification transfer id and section key cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtReviewNoteFacade wrnf = (WtReviewNoteFacade)appContext.getBean(WtReviewNoteFacade.class.getSimpleName());
      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
      WtReviewNote node = new WtReviewNote();
      node.setWtTransId(transId);
      node.setSectionKey(sectionKey);
      if(wrnf.find(node) == null){
        wrnf.create(node);
      } else {
        node = wrnf.find(node);
      }
      node.setNote(requestUtil.getString("note"));
      node.setNoteUpdatedBy(user.getUserId());
      node.setNoteDate(Calendar.getInstance().getTime());
      wrnf.edit(node);
      
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
  
  @RequestMapping("/addtechnicalnote")
  public void addTechnicalNote(@RequestParam("transId") Integer transId
          ,@RequestParam("sectionKey") String sectionKey
          ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try{
      if ((transId == null) || (StringUtils.isEmpty(sectionKey))) {
        throw new Exception("The modification transfer id and section key cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtReviewNoteFacade wrnf = (WtReviewNoteFacade)appContext.getBean(WtReviewNoteFacade.class.getSimpleName());
      AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
      WtReviewNote node = new WtReviewNote();
      node.setWtTransId(transId);
      node.setSectionKey(sectionKey);
      node.setNote(requestUtil.getString("note"));
      node.setNoteUpdatedBy(user.getUserId());
      node.setNoteDate(Calendar.getInstance().getTime());
      node.setIsComplete(requestUtil.getInt("isComplete"));
      node.setStatusUpdatedBy(user.getUserId());
      node.setStatusDate(Calendar.getInstance().getTime());
      wrnf.create(node);
      
      jsonResponse = new JSONObject(node.toMap());
      jsonResponse.put("updatedBy", user.getName());
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

  // </editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc="Proposal List">
  @RequestMapping("/proposalList")
  public void getProposalList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade f = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    List<WtTrans> wts = f.findAll();

    // Proposal List return
    if (user.isManager() || user.isReviewer()) {
      wts = f.select("SELECT WT.* FROM WT_TRANS WT WHERE TRANS_YEAR='2015' AND WT.WT_STATUS_FLAG_ID != " + Status.DRAFT, com.gei.entities.WtTrans.class);
    }

    JSONObject jsonResponse = new JSONObject(), tmpJson;
    jsonResponse.put("data", new JSONArray());
    WtBuyer buyer;
    WtAgency seller;
    WtAgency approval;
    String sellerNames, buyerNames, approvalNames, key, comma = "";
    Iterator<WtAgency> sellersIt;
    Iterator<WtBuyer> buyersIt;
    Iterator<WtAgency> approvalIt;
    for (WtTrans wt : wts) {
      tmpJson = new JSONObject(wt.toMap());
      tmpJson.put("sellers", "");
      tmpJson.put("buyers", "");
      tmpJson.put("approvals", "");

      if (wt.getWtSellerCollection() != null) {
        sellersIt = wt.getWtSellerCollection().iterator();
        if (sellersIt != null) {
          comma = "";
          sellerNames = "";
          key = "sellers";
          while (sellersIt.hasNext()) {
            seller = sellersIt.next();
            sellerNames += tmpJson.optString(key) + comma + seller.getAgencyCode();
            comma = "/";
          }//end while
          tmpJson.put(key, sellerNames);
        }//end if
      }//end if

      if (wt.getWtBuyerCollection() != null) {
        buyersIt = wt.getWtBuyerCollection().iterator();
        if (buyersIt != null) {
          comma = "";
          buyerNames = "";
          key = "buyers";
          while (buyersIt.hasNext()) {
            buyer = buyersIt.next();
            buyerNames += tmpJson.optString(key) + comma + buyer.getWtAgency().getAgencyCode();
            comma = "/";
          }//end while
          tmpJson.put(key, buyerNames);
        }//end if
      }//end if

      if (wt.getWtApprovalCollection() != null) {
        approvalIt = wt.getWtApprovalCollection().iterator();
        if (approvalIt != null) {
          comma = "";
          approvalNames = "";
          key = "approvals";
          while (approvalIt.hasNext()) {
            approval = approvalIt.next();
            approvalNames += tmpJson.optString(key) + comma + approval.getAgencyCode();
            comma = "/";
          }//end while
          tmpJson.put(key, approvalNames);
        }//end if
      }//end if

      if (wt.getProTransQua() == null) {
        tmpJson.put("proTransQua", "");
      }
      if (wt.getProReceivedDate() == null) {
        tmpJson.put("proReceivedDate", "");
      } else {
        tmpJson.put("proReceivedDate", df.format(wt.getProReceivedDate()));
      }
      if (wt.getDwrProApprDate() == null) {
        tmpJson.put("dwrProApprDate", "");
      } else {
        tmpJson.put("dwrProApprDate", df.format(wt.getDwrProApprDate()));
      }
      if (wt.getSwrcbAppProcess() == null) {
        tmpJson.put("swrcbAppProcess", "");
      }
      if (wt.getSwrcbApp() == null) {
        tmpJson.put("swrcbApp", "");
      }
      if (wt.getWaterAvlTiming() == null) {
        tmpJson.put("waterAvlTiming", "");
      }
      if (wt.getDeltaTransferInd() == 1) {
        tmpJson.put("deltaTransferInd", "Yes");
      } else {
        tmpJson.put("deltaTransferInd", "No");
      }
      if (wt.getReqExpWin() == null) {
        tmpJson.put("reqExpWin", "");
      }
      jsonResponse.optJSONArray("data").put(tmpJson);
    }//end for
    response.getWriter().write(jsonResponse.toString());
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Send Review Emails">
  @RequestMapping("/checkcompletenotice")
  public void checkCompleteNotice(@RequestParam("transId") Integer transId
                                  , @RequestParam("section") String section
                                  , HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    LoggedInCheck(request, response);
    try {
      if ((transId == null) || (StringUtils.isEmpty(section))) {
        throw new Exception("The modification transfer id and section cannot be unassigned!");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      Date date = new Date();
      String swpaoNum = requestUtil.getString("swpaoNum");
      String swpaoReviewer = requestUtil.getString("swpaoReviewer");
      String sectionName = "";
      
      switch (section) {
        case "BASE":
          sectionName = "buyer, seller, general information, and water rights";
          break;
        case "CI":
          sectionName = "cropland idling and crop shifting";
          break;
        case "RES":
          sectionName = "reservoir release";
          break;
        case "GW":
          sectionName = "groundwater substitution";
          break;
      }
      
      //Send Email for Base Info Complete
      String subject = "Review Completion of SWPAO #"+swpaoNum+" water transfer proposal";
      String content = "On "+df.format(date)+", the review of "+sectionName+" section of SWPAO #"+swpaoNum+" water transfer proposal is complete.";
      sendEmail(swpaoReviewer,subject,content);
      
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
  
  private boolean sendEmail(String emailAddress, String subject, String emailMsg) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setSendPartial(true);
      email.setDebug(true);
//      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      email.addTo(emailConfig.get("manager.address"));
      
      // Build CC Emails include SWPAO review lead, manager, and senior Engineer(s)
      if(!StringUtils.isEmpty(emailAddress)){
        email.addCc(emailAddress.trim());
      }
      email.addCc(emailConfig.get("manager.address"));
      String seniorStr = emailConfig.get("senior.address");
      String[] seniorEmails = seniorStr.split(";");
      if (seniorEmails != null) {
        for (String seniorEmail : seniorEmails) {
          email.addCc(seniorEmail.trim());
        }
      }
      
//      String[] emailAddressList = emailAddress.split(";");
//      for (String aEmailAddress : emailAddressList) {
//        email.addCc(aEmailAddress.trim());
//      }      
//      email.addCc(emailConfig.get("admin.address"));
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
  //</editor-fold>
}
