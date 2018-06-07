package com.gei.controller;

import com.gei.context.ConnectionContext;
import com.gei.context.UserContext;
import static com.gei.controller.AttachmentController.CONTENT_TYPE_OCTETSTREAM;
import com.gei.entities.*;
import com.gei.exception.MyException;
import com.gei.facades.*;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.util.EntityUtil;
import com.gei.utils.ServletRequestUtil;
import com.itextpdf.text.DocumentException;
import gov.ca.water.htmtopdf.HtmlToPdfConverter;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController {

  @Autowired
  ApplicationContext appContext;
  public final static String PROPOSAL_REPORT = "PROPOSAL REPORT", GROUNDWATER_REPORT = "GROUNDWATER REPORT";
  @Autowired
  ViewResolver viewResolver;

  @RequestMapping
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("reports");
    return mv;
  }

  @RequestMapping("/public")
  public ModelAndView publicList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = new ModelAndView("proposal/PublicProposals");
    return mv;
  }

  // <editor-fold defaultstate="collapsed" desc="Groundwater Report">
  @RequestMapping("/previewGW/{id}")
  public ModelAndView previewGW(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    String type = requestUtil.getString("type");
    if (type.isEmpty()) {
      LoggedInCheck(request, response);
    }
    ModelAndView mv = new ModelAndView("proposal/GroundwaterInfo_preview");
    WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
    WtGroundwater groundwater = wgwf.find(id);
    WtTrans wtTans = groundwater.getWtTrans();
    int transId = wtTans.getWtTransId();
    String attachType = requestUtil.getString("attachType");
    String pdfFileName = buildFileName(transId, attachType);
    int version = getNextVersion(transId, GROUNDWATER_REPORT);
    Collection<WtChecklist> checklistCollection = getChecklistByType(attachType);
    JSONObject wellData = new JSONObject();
    String linebreakJson = requestUtil.getString("linebreakJson");
    JSONObject linebreak = new JSONObject();
    if (!linebreakJson.isEmpty()) {
      linebreak = new JSONObject(linebreakJson);
    }
    for (WtWell well : groundwater.getWtWellCollection()) {
      AssociateWellList(well, wellData);
    }
    AttachmentInformation(groundwater.getWtAttachmentCollection(), wellData);
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (!cookie.getName().equals("JSESSIONID")) {
          linebreak.put(cookie.getName(), cookie.getValue());
          response.addCookie(cookie);
        }
      }
    }

    JSONObject wellMapPoint = mapLatLngCal(groundwater.getWtWellCollection());
    mv.addObject("mapView", wellMapPoint);
    mv.addObject("linebreak", linebreak);
    mv.addObject("nameSize", wellData);
    mv.addObject("previewType", type);
    mv.addObject("id", id);
    mv.addObject("type", attachType);
    mv.addObject("proposal", wtTans);
    mv.addObject("groundwater", groundwater);
    mv.addObject("checklistCollection", checklistCollection);
    mv.addObject("pdfFileName", pdfFileName);
    mv.addObject("version", version);
    return mv;
  }

  public JSONObject mapLatLngCal(Collection<WtWell> wellCollection) {
    String markers = "";
    double avgLng = 0;
    double avgLat = 0;
    int count = 0;
    for (WtWell well : wellCollection) {
      count++;
      String masterSideCode = well.getWtWellNum();
      String[] latarry = masterSideCode.split("N");
      String[] lngarry = latarry[1].split("W");
      String lng = lngarry[0].subSequence(0, 3) + "." + lngarry[0].substring(3, lngarry[0].length());
      String lat = latarry[0].subSequence(0, 2) + "." + latarry[0].subSequence(2, latarry[0].length());
      avgLng += Double.parseDouble(lng);
      avgLat += Double.parseDouble(lat);
      markers = markers + "|" + lat + ",-" + lng;
    }
    avgLng = avgLng / count;
    avgLat = avgLat / count;
    JSONObject wellMapPoint = new JSONObject();
    wellMapPoint.put("center", avgLat + ",-" + avgLng);
    wellMapPoint.put("points", markers);
    return wellMapPoint;
  }

  @RequestMapping("/print/{id}")
  public void print(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
    LoggedInCheck(request, response);
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    String attachType = requestUtil.getString("attachType");
    int userId = requestUtil.getInt("userId");
    String isTrack = requestUtil.getString("track");
    int transId = requestUtil.getInt("wtTransId");
    HtmlToPdfConverter toPdf = HtmlToPdfConverter.getInstance();
    String serverURL = getFullUrlPath(request) + "/";
    response.setContentType("application/pdf");
    String linebreakJson = requestUtil.getString("linebreakJson");
    String htmlStr = getContents(request, String.format("%s/report/previewGW/%s?attachType=%s&type=%s&linebreakJson=%s", getFullUrlPath(request), id, attachType, "pdf", linebreakJson));
    toPdf.setCreateXhtmlFile(true);
    InputStream is = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8));
    try        
    {
        Thread.sleep(1000);
    } 
    catch(InterruptedException ex) 
    {
        Thread.currentThread().interrupt();
    }    
    if (!toPdf.execute(is, new URL(serverURL), response.getOutputStream())) {
      throw new IOException(toPdf.getErrorMsg());
    }

//        try (InputStream is = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8))) {
//          if(isTrack.equals("true")){
//            if (!toPdf.execute(is,new URL(serverURL))){
//              throw new IOException(toPdf.getErrorMsg());
//            }
//          } else if (!toPdf.execute(is,new URL(serverURL),response.getOutputStream()))
//          {
//            throw new IOException(toPdf.getErrorMsg());
//          }
//        }
    if (isTrack.equals("true")) {
      String fullpath = toPdf.getPdfOutputFile();
      String inputFile = fullpath.substring(0, fullpath.lastIndexOf('.')) + ".xhtml";
      File url = new File(inputFile);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);
        byte[] data = os.toByteArray();
        savePDF(data, transId, userId, attachType);
      } finally {
        os.close();
        os = null;
      }

    }
    response.getOutputStream().close();

  }
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Proposal Report">
  @RequestMapping("/reportPreview/{id}")
  public ModelAndView reportPreview(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mv = new ModelAndView("report/reportPreview");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    JSONObject sizeData = new JSONObject();
    WtTrans wt = wtf.find(id);
    AttachmentInformation(wt.getWtAttachmentCollection(), sizeData);
    Collection<WtChecklist> BIChecklist = getChecklistByType("BI");
    Collection<WtChecklist> CIChecklist = getChecklistByType("CI");
    Collection<WtChecklist> CIMapChecklist = getChecklistByType("CI_MAP");
    Collection<WtChecklist> RVChecklist = getChecklistByType("RV");
    Collection<WtChecklist> GWChecklist = getChecklistByType("GW");

    String htmlStr = "";
    if (wt.getWtCropIdling() != null) {
      AttachmentInformation(wt.getWtCropIdling().getWtAttachmentCollection(), sizeData);
      AttachmentInformation(wt.getWtCropIdling().getWtMapAttCollection(), sizeData);
    }
    if (wt.getWtReservoir() != null) {
      AttachmentInformation(wt.getWtReservoir().getWtAttachmentCollection(), sizeData);
    }
    if (wt.getWtGroundwater() != null) {
      JSONObject wellMapPoint = mapLatLngCal(wt.getWtGroundwater().getWtWellCollection());
      for (WtWell well : wt.getWtGroundwater().getWtWellCollection()) {
        AssociateWellList(well, sizeData);
      }
      AttachmentInformation(wt.getWtGroundwater().getWtAttachmentCollection(), sizeData);
      
      mv.addObject("mapView", wellMapPoint);
    }
//        if(wt.getWtGroundwater() != null)
//        {
//            int gwId = wt.getWtGroundwater().getWtGroundwaterId();
//            htmlStr=getContents(request
//                        ,String.format("%s/report/previewGW/%s?attachType=%s&type=%s"
//                        ,getFullUrlPath(request)
//                        ,gwId
//                        ,"GW"
//                        ,"pdf"));
//        }
//        if(request.getCookies()!=null)
//	{
//	    for(Cookie cookie:request.getCookies())
//	    {
//	        if(!cookie.getName().equals("JSESSIONID"))
//	        {
//	            linebreak.put(cookie.getName(), cookie.getValue());
//	            response.addCookie(cookie);
//	        }
//	    }
//	}
    
    mv.addObject("BIChecklist", BIChecklist);
    mv.addObject("CIChecklist", CIChecklist);
    mv.addObject("CIMapChecklist", CIMapChecklist);
    mv.addObject("RVChecklist", RVChecklist);
    mv.addObject("GWChecklist", GWChecklist);
    mv.addObject("proposal", wt);
    mv.addObject("size", sizeData);
    mv.addObject("gwPreview", htmlStr);
    mv.addObject("pdfFileName", buildFileName(id, "PR"));
    mv.addObject("version", getNextVersion(id, PROPOSAL_REPORT));
//        mv.addObject("linebreak",linebreak);
    return mv;
  }

  @RequestMapping("/printReport/{id}")
  public void printReport(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
    try {
      if (!UserContext.getInstance().isLoggedIn()) {
        response.sendRedirect(request.getContextPath());
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      String attachType = requestUtil.getString("attachType");
      int userId = requestUtil.getInt("userId");
      String isTrack = requestUtil.getString("track");
      String serverURL = getFullUrlPath(request) + "/";
      HtmlToPdfConverter toPdf = HtmlToPdfConverter.getInstance();
      response.setContentType("application/pdf");
      toPdf.setCreateXhtmlFile(true);
      String htmlStr = getContents(request, String.format("%s/report/reportPreview/%s?attachType=%s", getFullUrlPath(request), id, attachType));
      try (InputStream is = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8))) {
        if (!toPdf.execute(is, new URL(serverURL), response.getOutputStream())) {
          throw new IOException(toPdf.getErrorMsg());
        }
      }
      if (isTrack.equals("true")) {
        String fullpath = toPdf.getPdfOutputFile();
        String inputFile = fullpath.substring(0, fullpath.lastIndexOf('.')) + ".xhtml";
        File url = new File(inputFile);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);
        byte[] data = os.toByteArray();
        savePDF(data, id, userId, attachType);
      }
      response.getOutputStream().close();
    } catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Proposal Status Reports">
  @RequestMapping("/statusReportPDF")
  public void statusReportPDF(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
    try {
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      String serverURL = getFullUrlPath(request) + "/";
      HtmlToPdfConverter toPdf = HtmlToPdfConverter.getInstance();
      response.setContentType("application/pdf");
      toPdf.setCreateXhtmlFile(true);
      String htmlStr = getContents(request, String.format("%s/proposalservice/statusreport", getFullUrlPath(request)));
      try (InputStream is = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8))) {
        if (!toPdf.execute(is, new URL(serverURL), response.getOutputStream())) {
          throw new IOException(toPdf.getErrorMsg());
        }
      }
      response.getOutputStream().close();
//      String htmlStr = getContents(request, String.format("%s/proposalservice/statusreport", getFullUrlPath(request)));
//      try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//        WebUtil.toPdf(htmlStr, os);
//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        os.writeTo(response.getOutputStream());
//      }
    } catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Public Proposal Details View">
  @RequestMapping("/viewproposal/{transId}")
  public ModelAndView viewPage(@PathVariable("transId") Integer transId, HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mv = new ModelAndView("public/ProposalForm");
    try {
      final Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = "";

      //<editor-fold defaultstate="collapsed" desc="Query for the Water Transfer Proposal">
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
        }
      });
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="Query for Crop Idling">
      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtCropIdling.class));
      WtCropIdling cropIdling = new WtCropIdling();
      GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(cropIdling) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtCropIdling cropIdling = this.getListener();
          if (rs.next()) {
            cropIdling.Load(rs);
          }
        }
      });
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="Query for Reservoir">
      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtReservoir.class));
      WtReservoir reservoir = new WtReservoir();
      GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(reservoir) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtReservoir reservoir = this.getListener();
          if (rs.next()) {
            reservoir.Load(rs);
          }
        }
      });
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="Query for Groundwater">
      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtGroundwater.class));
      WtGroundwater groundwater = new WtGroundwater();
      GenericFacade.executeQuery(conn, query, Arrays.asList(transId), new QueryDelegate(groundwater) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtGroundwater gw = this.getListener();
          if (rs.next()) {
            gw.Load(rs);
          }
        }
      });
      //</editor-fold> 

      mv.addObject("wtTransId", transId);
      mv.addObject("proposal", proposal);
      mv.addObject("cropIdling", cropIdling);
      mv.addObject("reservoir", reservoir);
      mv.addObject("groundwater", groundwater);
    } catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    return mv;
  }
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Report Util Methods">
  public static String getFullUrlPath(HttpServletRequest request) {
    String url = "http";

    url += request.isSecure() ? "s" : "";
    url += "://" + request.getServerName();
    url += request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
    url += request.getContextPath();

    return url;
  }

  private Collection<WtChecklist> getChecklistByType(String attachType) {
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    String checklistField = attachType.toUpperCase() + "_ATTACHMENT";
    wclf.reset();
    wclf.set("checklistField", checklistField);
    Collection<WtChecklist> checklistCollection = wclf.findAll();

    return checklistCollection;
  }

  private Collection<WtChecklist> getChecklistByType(String attachType, Integer wellTransfer, Integer wellMonitoring) {
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    Collection<WtChecklist> checklistCollection = new ArrayList();
    String checklistField = attachType.toUpperCase() + "_ATTACHMENT";
    wclf.reset();
    wclf.set("checklistField", checklistField);

    if (wellTransfer == 1 && wellMonitoring == 0) {
      wclf.set("wellTransfer", wellTransfer);
    } else if (wellTransfer == 0 && wellMonitoring == 1) {
      wclf.set("wellMonitoring", wellMonitoring);
    }
    checklistCollection = wclf.findAll();
    return checklistCollection;
  }

  public void AssociateWellList(WtWell well, JSONObject wellData) {
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    String wellTransfer = String.valueOf(well.getWellTransfer());
    String wellMonitor = String.valueOf(well.getWellMonitoring());
    if (wellTransfer.equals("null")) {
      wellTransfer = "0";
    }
    if (wellMonitor.equals("null")) {
      wellMonitor = "0";
    }
    Collection<WtChecklist> checkListCollection = getChecklistByType("WELL", Integer.parseInt(wellTransfer), Integer.parseInt(wellMonitor));
    JSONArray checkListArray = new JSONArray();
    for (WtChecklist cl : checkListCollection) {
      JSONObject cljson = new JSONObject(cl.toMap());
      checkListArray.put(cljson);
    }
    wellData.put("" + well.getWtWellNum(), checkListArray);
    for (WtAttachment attach : well.getWtAttachmentCollection()) {
      //Build User Info
      auf.reset();
      if (attach.getCreatedById() != null) {
        auf.set("userId", attach.getCreatedById());
        AppUser app = auf.find();
        wellData.put("" + attach.getCreatedById(), app.getWtContact().getFirstName() + " " + app.getWtContact().getLastName());
      }

      int unit = 1024;
//            long bytes = attach.getFileLob().length;
      Integer bytes = attach.getFileSize();
      if (bytes == null) {
        continue;
      }
      int exp = (int) (Math.log(bytes) / Math.log(unit));
      if (exp == 0) {
        exp = 1;
      }
      String suffix = ("KMGTPE").charAt(exp - 1) + "";
      String kb = String.format("%.1f %sB", bytes / Math.pow(unit, exp), suffix);
      if (kb.equals("0.0 KB")) {
        kb = bytes + " bytes";
      }
      wellData.put("" + attach.getWtAttachmentId(), kb);

    }
  }

  public void AttachmentInformation(Collection<WtAttachment> collection, JSONObject wellData) {
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    for (WtAttachment attach : collection) {
      try {
        //Build User Name
        auf.reset();
        if (attach.getCreatedById() != null) {
//                    auf.set("userId", attach.getCreatedById());
//                    AppUser app = auf.find();
          String name = attach.getCreatedBy().getName();
          wellData.put("" + attach.getCreatedById(), name);
        }

        //Build Size Info
        int unit = 1024;
        String kbString = "";
//                long bytes = attach.getFileLob().length;
        Integer bytes = attach.getFileSize();
        if (bytes == null || bytes.equals(0)) {
          kbString = "0 Bytes";
        } else {
          int exp = (int) (Math.log(bytes) / Math.log(unit));
          if (exp == 0) {
            exp = 1;
          }
          String suffix = ("KMGTPE").charAt(exp - 1) + "";
          kbString = String.format("%.1f %sB", bytes / Math.pow(unit, exp), suffix);
          if (kbString.equals("0.0 KB")) {
            kbString = bytes + " Bytes";
          }
        }
        wellData.put("" + attach.getWtAttachmentId(), kbString);

      } catch (NullPointerException ex) {
      }
    }
  }

  public void savePDF(byte[] data, int transId, int userId, String attachType) throws IOException {
    WtTrackFileFacade waf = (WtTrackFileFacade) appContext.getBean(WtTrackFileFacade.class.getSimpleName());
    WtTransFacade wtt = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtTrackFile wtf = new WtTrackFile();
    WtTrans wt = wtt.find(transId);
    String status = wt.getWtStatusFlag().getStatusName();
//        String filename = "Proposal#"+transId+"_Report";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
//        String versionName = sdf.format(date);
    wtf.setFileLob(data);
    wtf.setMimeType("application/pdf");
    wtf.setWtTrans(wt);
    wtf.setCreatedById(userId);
    wtf.setStatusName(status);

    switch (attachType) {
      case "PR":
        wtf.setFileType(PROPOSAL_REPORT);
        wtf.setFileVersion(getNextVersion(transId, PROPOSAL_REPORT));
        break;
      case "GW":
//            filename = "GroundWater#"+transId+"_Report";
        wtf.setFileType(GROUNDWATER_REPORT);
        wtf.setFileVersion(getNextVersion(transId, GROUNDWATER_REPORT));
        break;
    }
//        wtf.setFilename(filename+".pdf");
    wtf.setFilename(buildFileName(transId, attachType) + ".pdf");
    waf.create(wtf);
  }

  private Integer getNextVersion(int transId, String fileType) {
    Integer lastVersion = 0;
    WtTrackFileFacade waf = (WtTrackFileFacade) appContext.getBean(WtTrackFileFacade.class.getSimpleName());

    List<WtTrackFile> wtf = waf.select("SELECT * FROM WT_TRACK_FILE WHERE WT_TRANS_ID='" + transId + "' AND FILE_TYPE='" + fileType + "' ORDER BY FILE_VERSION", com.gei.entities.WtTrackFile.class);
    if (wtf != null && wtf.size() > 0) {
      lastVersion = wtf.get(wtf.size() - 1).getFileVersion() + 1;
    }

    return lastVersion;
  }

  private String buildFileName(int transId, String attachType) {
    String filename = "";

    switch (attachType) {
      case "PR":
        filename = "Proposal#" + transId + "_Report";
        break;
      case "GW":
        filename = "Proposal#" + transId + "_GroundWater_Report";
        break;
    }

    return filename;
  }
    //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Export and Download">
  @RequestMapping("/view/{attachmentId}")
  public void view(@PathVariable("attachmentId") Integer attachmentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!UserContext.getInstance().isLoggedIn()) {
      response.sendRedirect(request.getContextPath());
    }
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTrackFileFacade waf = (WtTrackFileFacade) appContext.getBean(WtTrackFileFacade.class.getSimpleName());
    WtTrackFile attachment = waf.find(attachmentId);

    if (attachment == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    String contentType = attachment.getMimeType();
    if (requestUtil.getString("output").equalsIgnoreCase("download")) {
      contentType = CONTENT_TYPE_OCTETSTREAM;
    }

    if (contentType.equalsIgnoreCase(CONTENT_TYPE_OCTETSTREAM)) {
      response.setHeader("Content-Disposition", "attachment;filename=" + attachment.getFilename());
    }

    response.setContentType(contentType);
    InputStream is = new ByteArrayInputStream(attachment.getFileLob());
    IOUtils.copy(is, response.getOutputStream());
  }

  public void excelExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      if (!AuthenticationController.IsLoggedIn(request)) {
        throw new MyException("", "ErrorPromptsHandler.SESSIONTIMEOUT");
      }
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      JSONArray dataList = new JSONArray(requestUtil.getString("jsonData"));
      String path = request.getServletContext().getRealPath("WEB-INF");
      File file = new File(path + "/upload/associatedwell.csv");
      String csv = CDL.toString(dataList);
      FileUtils.writeStringToFile(file, csv);
      jsonResponse.put("data", dataList);
      jsonResponse.put("success", file.exists());
      jsonResponse.put("path", file.getPath());
    } catch (Exception ex) {
      jsonResponse.put("success", false).put("error", ex.getMessage());
      if (ex instanceof MyException) {
        jsonResponse.put("callback", ((MyException) ex).getCallback());
      }
    } finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

  @RequestMapping("/fileExport")
  public void fileExport(@RequestParam("name") String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject jsonResponse = new JSONObject();
    try {
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      JSONArray dataList = new JSONArray(requestUtil.getString("jsonData"));
      String path = request.getServletContext().getRealPath("WEB-INF");
      File file = new File(path + "/upload/" + filename);
      String csv = CDL.toString(dataList);
      FileUtils.writeStringToFile(file, csv);
      jsonResponse.put("data", dataList);
      jsonResponse.put("success", file.exists());
      jsonResponse.put("path", file.getPath());
    } catch (JSONException | IOException ex) {
      jsonResponse.put("success", false).put("error", ex.getMessage());
      if (ex instanceof MyException) {
        jsonResponse.put("callback", ((MyException) ex).getCallback());
      }
    } finally {
      response.getWriter().write(jsonResponse.toString());
    }
  }

  public void downloadAssociatedWellExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String path = request.getServletContext().getRealPath("WEB-INF");
    String contentType = CONTENT_TYPE_OCTETSTREAM;
    File file = new File(path + "/upload/associatedwell.csv");
    response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
    InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    response.setContentType(contentType);
    IOUtils.copy(is, response.getOutputStream());
  }

  @RequestMapping("/downloadFile")
  public void downloadExportFile(@RequestParam("name") String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
    String path = request.getServletContext().getRealPath("WEB-INF");
    String contentType = CONTENT_TYPE_OCTETSTREAM;
    File file = new File(path + "/upload/" + filename);
    response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
    InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    response.setContentType(contentType);
    IOUtils.copy(is, response.getOutputStream());
  }
    //</editor-fold>
}
