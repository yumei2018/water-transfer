package com.gei.controller;

import com.gei.context.ConnectionContext;
import com.gei.context.LookupDataContext;
import com.gei.context.UserContext;
import static com.gei.controller.AuthenticationController.SESSION_KEY_USER;
import com.gei.entities.*;
import com.gei.facades.*;
import com.gei.facades.delegates.QueryDelegate;
import com.gei.util.EntityUtil;
import com.gei.utils.ServletRequestUtil;
import gov.ca.water.transfer.util.WebUtil;
import gov.ca.water.watertransfer.entity.collection.ChecklistCollection;
import gov.ca.water.watertransfer.entity.collection.ReviewNoteCollection;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ymei
 */

@Controller
@RequestMapping("/attachment")
public class AttachmentController extends BaseController{
    @Autowired
    ApplicationContext appContext;
    public final static String CONTENT_TYPE_OCTETSTREAM="application/octet-stream"
                              ,CONTENT_TYPE_PDF="application/pdf";


  // <editor-fold defaultstate="collapsed" desc="Public Methods of Index and List">
  @RequestMapping
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    LoggedInCheck(request, mv);

    return mv;
  }

  @RequestMapping("/getAttachmentList")
  public ModelAndView getAttachmentList(@RequestParam("typeId") String attachType
                                        ,@RequestParam("wtTransId") Integer wtTransId
                                        ,@RequestParam(value="jsonData",required=false) JSONObject jsonData
                                        ,HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = null;
    try {
      LoggedInCheck(request, response);

      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID = ?", EntityUtil.getTableName(WtTrans.class));
      WtTrans transRec = new WtTrans();
      GenericFacade.executeQuery(conn, query, Arrays.asList(wtTransId), new QueryDelegate(transRec) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtTrans proposal = this.getListener();
          if (rs.next()) {
            proposal.Load(rs);
          }
          else {
            throw new Exception("The water transfer proposal is id invalid!");
          }
        }
      });

      Collection<WtChecklist> checklistCollection = null;
      if (attachType.equals("WELL")) {
        mv = new ModelAndView("attachment/AssociateWellAttachmentList");
        WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
        checklistCollection = getChecklistByType(attachType, jsonData.optInt("wellTransfer"), jsonData.optInt("wellMonitoring"));
//        if (jsonData.optInt("wellTransfer") == 1 && jsonData.optInt("wellMonitoring") == 0) {
//          checklistCollection = LookupDataContext.getInstance().getWellTransChecklist(true);
//        } else if (jsonData.optInt("wellTransfer") == 0 && jsonData.optInt("wellMonitoring") == 1) {
//          checklistCollection = LookupDataContext.getInstance().getWellMonitChecklist(true);
//        }
//        WtWell well = LookupDataContext.getInstance().getWells(true).getWell(jsonData.optString("wellNum"));
        wwf.set("wtWellNum", jsonData.optString("wellNum"));
        WtWell well = wwf.find();
        if(well.getWtAttachmentCollection() != null || well.getWtAttachmentCollection().size() > 0){
          for(WtAttachment attachment : well.getWtAttachmentCollection()){
            Collection<WtChecklist> checklists = this.getWellAttachmentChecklist(attachment);
            attachment.setWtChecklistCollection(checklists);
          }
        }
        
        mv = AssociateWellList(mv, well);
        mv.addObject("wellCheckList", checklistCollection);
        
        //<editor-fold defaultstate="collapsed" desc="Querying for Review Notes for select well">
        String sectionKey = jsonData.optString("sKey");
        final ReviewNoteCollection reviewNotes = new ReviewNoteCollection();
        query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_TRANS_ID =? AND SECTION_KEY=? ORDER BY %1$s.NOTE_DATE"
                ,EntityUtil.getTableName(WtReviewNote.class));
        GenericFacade.executeQuery(conn, query, Arrays.asList(wtTransId,sectionKey), new QueryDelegate(reviewNotes) {
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
        mv.addObject("sKey", sectionKey);
        mv.addObject("reviewNotes", reviewNotes);        
        //</editor-fold>
        
        //Get Review Note for select well
//        String sectionKey = jsonData.optString("sKey");
//        WtReviewNote note = new WtReviewNote();
//        note.setWtTransId(wtTransId);
//        note.setSectionKey(sectionKey);
//        note = wrnf.find(note);
//        if(note != null){
//          mv.addObject("sKey", sectionKey);
//          mv.addObject("isComplete", note.getIsComplete());
//          mv.addObject("techNote", note.getNote());
//        }
      }
      else {
        mv = new ModelAndView("attachment/AttachmentFileList");
        checklistCollection = getChecklistByType(attachType);
        
        // Get Attachment Collection depend on Type
        Collection<WtAttachment> wtAttachmentCollection = getAttachmentCollectionByType(transRec, attachType);

        // Get FSA Map# if attachment type is
        if (attachType.equals("CI_MAP") && wtAttachmentCollection.size()>0) {
          Integer fsaMapNum = getFsaMapNum(wtAttachmentCollection);
          mv.addObject("fsaMapNum", fsaMapNum);
        }

        mv.addObject("attachmentCollection", wtAttachmentCollection);
        mv.addObject("checklistCollection", checklistCollection);
      }

      WtStatusFlag statusFlag = new WtStatusFlag();
      query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_STATUS_FLAG_ID = (SELECT %2$s.WT_STATUS_FLAG_ID FROM %2$s WHERE %2$s.WT_TRANS_ID = ?)"
        ,EntityUtil.getTableName(WtStatusFlag.class)
        ,EntityUtil.getTableName(WtTrans.class)
      );
      GenericFacade.executeQuery(conn, query, Arrays.asList(wtTransId), new QueryDelegate(statusFlag) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtStatusFlag status = this.getListener();
          if (rs.next()) {
            status.Load(rs);
          }
          else {
            status = null;
          }
        }
      });
      if (statusFlag != null) {
        mv.addObject("statusFlag", statusFlag);
        mv.addObject("wtTransId", wtTransId);
      }

      mv.addObject("attachType", attachType);
    }
    catch(Exception ex) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    return mv;
  }

  public ModelAndView getTemplateList(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/TemplateList");
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    waf.set("attachmentType", "TEMPLATE");
    waf.set("isActive", 1);

    List<WtAttachment> attachmentList = waf.findAll();
    mv.addObject("templateList", attachmentList);
    return mv;
  }

  public ModelAndView getAttachmentChecklist(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/UploadFile");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    String attachType = requestUtil.getString("attachType");
    int tranId = requestUtil.getInt("wtTransId");
    String welltype = requestUtil.getString("wellType");
    Collection<WtChecklist> wtChecklistCollection = getChecklistByType(attachType);
    if (!welltype.isEmpty()) {
      JSONObject checkType = new JSONObject(welltype);
      attachType = requestUtil.getString("typeId");
      mv.addObject("wellType", checkType);
      wtChecklistCollection = getChecklistByType(attachType, checkType.optInt("wellTransfer"), checkType.optInt("wellMonitoring"));
    }
    mv.addObject("attachType", attachType);
    mv.addObject("wtTranID", tranId);
    mv.addObject("checklistCollection", wtChecklistCollection);
    return mv;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods of Attachment">
  public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException, ParseException, Exception {
    if (!LoggedInCheck(request, response)) {
      return null;
    }
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    AppUser user = UserContext.getInstance().getUser();
    if (ServletFileUpload.isMultipartContent(request)) {
      // Create a factory for disk-based file items
      DiskFileItemFactory factory = new DiskFileItemFactory();

      // Configure a repository
      ServletContext servletContext = this.getServletContext();
//            String dir = servletContext.getInitParameter("FILE_LOAD_TMP_DIR");
//            factory.setRepository(new File(dir));
      File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
      factory.setRepository(repository);

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));
      Iterator<FileItem> iter = items.iterator();
      InputStream iStream = null;
      WtAttachmentWithFile attachment = new WtAttachmentWithFile();
      String filetype = "";
      String attachType = null;
      String wtTransId = null;
      String wtWellNum = null;
      String createdById = null;
      String wellTransfer = null, wellMonitoring = null;
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      List<Integer> intList = new ArrayList<>();
      ArrayList<File> file = new ArrayList<>();
      ArrayList<FileItem> fileItem = new ArrayList<>();
      while (iter.hasNext()) {
        FileItem item = (FileItem) iter.next();
        if (item.isFormField()) {
          String fieldName = item.getFieldName();
          String fieldValue = item.getString();
//          Date dateSubmitted = new Date();
//          if(fieldName.equals("submitdate"))
//          {
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//            dateSubmitted = sdf.parse(fieldValue);
//          }
          if (!StringUtils.isEmpty(fieldValue)) {
            if (null != fieldName) {
              switch (fieldName) {
                case "filetype":
                  filetype = fieldValue;
                  break;
                case "attachType":
                  attachType = fieldValue;
                  break;
                case "wtTransId":
                  wtTransId = fieldValue;
                  break;
                case "wtWellNum":
                  wtWellNum = fieldValue;
                  break;
                case "wellTransfer":
                  wellTransfer = fieldValue;
                  break;
                case "wellMonitoring":
                  wellMonitoring = fieldValue;
                  break;
                case "filename":
                  attachment.setFilename(fieldValue);
                  break;
                case "title":
                  attachment.setTitle(fieldValue);
                  break;
                case "createdById":
                  createdById = fieldValue;
                  attachment.setCreatedById(Integer.parseInt(fieldValue));
                  break;
                case "checklist":
                  intList.add(Integer.parseInt(fieldValue));
                  break;
                case "description":
                  attachment.setDescription(fieldValue);
                  break;
                case "ceqaSubmittedDate":
                  attachment.setCeqaSubmittedDate(sdf.parse(fieldValue));
                  break;
              }
            }
          }
        } else {
          if (filetype.equalsIgnoreCase("ZIP")) {
            iStream = item.getInputStream();
          } else if (filetype.equalsIgnoreCase("TEMPLATE")) {
            File f = new File(item.getName());
            attachment.setFilename(f.getName());
            attachment.setMimeType(item.getContentType());
            attachment.setCreatedById(user.getUserId());
            attachment.setFileLob(item.get());
            attachment.setFileSize(item.getSize());
          } else {
            File f = new File(item.getName());
            if (!file.contains(f)) {
              file.add(f);
              fileItem.add(item);
            }
          }
        }
      }

      if ("TEMPLATE".equalsIgnoreCase(filetype)) {
        attachment.setAttachmentType(filetype);
        waff.create(attachment);
        return mv;
      }

      WtTrans transRec = new WtTrans();
      Collection<WtAttachment> wtAttachmentCollection = new ArrayList();
      transRec = wtf.find(Integer.parseInt(wtTransId));
      if (transRec == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }

      // Deal with the zip file
      if (filetype.equalsIgnoreCase("ZIP")) {
        unzipFile(transRec, attachType, iStream, wtWellNum, createdById);
//            } else if("PP".equalsIgnoreCase(attachType)){ // Deal with DWR uopload files
//                attachment.setAttachmentType(attachType);
//                waf.create(attachment);
//                linkAttachment(attachType,attachment,transRec,wtWellNum);
      } else { // Deal with other type files
        // Create New File
        for (int i = 0; i < file.size(); i++) {
          WtAttachmentWithFile multiAttachment = new WtAttachmentWithFile();
          if (file.size() == 1) {
            multiAttachment = attachment;
          }
          File cFile = file.get(i);
          FileItem cFileItem = fileItem.get(i);
          multiAttachment.setFilename(cFile.getName());
          multiAttachment.setMimeType(cFileItem.getContentType());
          multiAttachment.setCreatedById(user.getUserId());
          multiAttachment.setFileLob(cFileItem.get());
          multiAttachment.setFileSize(cFileItem.getSize());
          if ("PP".equalsIgnoreCase(attachType)) { // Deal with DWR uopload files
            multiAttachment.setAttachmentType(attachType);
          }
          waff.create(multiAttachment);
          linkChecklist(multiAttachment);
          linkAttachment(attachType, multiAttachment.getWtAttachmentId(), transRec, wtWellNum);
        }
      }

      // Get all checklist depend on Project Type
      Collection<WtChecklist> wtChecklistCollection = getChecklistByType(attachType);
      Collection<WtChecklist> checklistCollection = new ArrayList();
      if (intList.size() > 0) {
        for (int i : intList) {
          wclf.set("wtChecklistId", i);
          WtChecklist checklist = wclf.find();
          checklistCollection.add(checklist);
        }
        attachment.setWtChecklistCollection(checklistCollection);
        waff.edit(attachment);
      }
      if ("WELL".equalsIgnoreCase(attachType)) {
        wwf.set("wtWellNum", wtWellNum);
        WtWell well = wwf.find();
        wtChecklistCollection = getChecklistByType(attachType, Integer.parseInt(wellTransfer), Integer.parseInt(wellMonitoring));
        mv = new ModelAndView("attachment/AssociateWellAttachmentList");
        mv = AssociateWellList(mv, well);
        mv.addObject("wellCheckList", wtChecklistCollection);
      }

      // Get attachment collection by attachment Type
      wtAttachmentCollection = getAttachmentCollectionByType(transRec, attachType);
      mv = AttachmentInfomation(mv, wtAttachmentCollection);

      // Get FSA Map# if attachment type is
      if (attachType.equals("CI_MAP") && wtAttachmentCollection.size()>0) {
        Integer fsaMapNum = getFsaMapNum(wtAttachmentCollection);
        mv.addObject("fsaMapNum", fsaMapNum);
      }

      mv.addObject("attachmentCollection", wtAttachmentCollection);
      mv.addObject("checklistCollection", wtChecklistCollection);
      mv.addObject("statusFlag", transRec.getWtStatusFlag());
      mv.addObject("attachType", attachType);
    }

    return mv;
  }

  // Link checklist to attachment
  private void linkChecklist(WtAttachmentWithFile attachment) {
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    List<Integer> intList = new ArrayList<>();

    // Link attachment to checklist table
    Collection<WtChecklist> checklistCollection = new ArrayList();
    if (intList.size() > 0) {
      for (int i : intList) {
        wclf.set("wtChecklistId", i);
        WtChecklist checklist = wclf.find();
        checklistCollection.add(checklist);
      }
      attachment.setWtChecklistCollection(checklistCollection);
      waff.edit(attachment);
    }
  }

  private void unzipFile(WtTrans transRec, String attachType, InputStream fullfilename, String wtWellNum, String createdById) throws FileNotFoundException, IOException, MagicParseException, MagicException {
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fullfilename));
    ZipEntry entry;
    ByteArrayOutputStream baos;
    while ((entry = zis.getNextEntry()) != null) {
//            WtAttachment attachment = new WtAttachment();
      WtAttachmentWithFile attachment = new WtAttachmentWithFile();
      baos = new ByteArrayOutputStream();
      int size;
      byte[] buffer = new byte[1024];
      while ((size = zis.read(buffer)) != -1) {
        baos.write(buffer, 0, size);
      }
      byte[] data = baos.toByteArray();
      if (data.length > 0) {
        String miniType = "application/octet-stream";
        try {
          MagicMatch match = Magic.getMagicMatch(data);
          miniType = match.getMimeType();
        } catch (MagicMatchNotFoundException nf) {
        }
        attachment.setCreatedById(Integer.parseInt(createdById));
        attachment.setFilename(entry.getName());
        attachment.setMimeType(miniType);
        attachment.setFileLob(data);
        attachment.setFileSize((long)data.length);
        baos.flush();
        waff.create(attachment);
        linkAttachment(attachType, attachment.getWtAttachmentId(), transRec, wtWellNum);
      }
    }
  }

//Method not in use, should be removed.
  public ModelAndView uploadZipFile(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException, ParseException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());

    String wtTransId = requestUtil.getString("wtTransId");
    String attachType = requestUtil.getString("attachType");
    String wtWellNum = requestUtil.getString("wtWellNum");
    WtTrans transRec = new WtTrans();
    transRec = wtf.find(Integer.parseInt(wtTransId));
    if (transRec == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    Collection<WtAttachment> wtAttachmentCollection = new ArrayList();

        // Just for test here
//        String FILE_NAME = "C:\\tmp\\GWZipTest.zip";
//        FileInputStream fis = new FileInputStream(FILE_NAME);
    String filename = requestUtil.getString("filename");
    String path = request.getServletContext().getRealPath("upload");
    String fullfilename = path + "/" + filename;
    FileInputStream fis = new FileInputStream(fullfilename);
    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
    ZipEntry entry;

    while ((entry = zis.getNextEntry()) != null) {
      WtAttachmentWithFile attachment = new WtAttachmentWithFile();
      System.out.println("Extracting: " + entry);
      attachment.setFilename(entry.getName());

      int size;
      byte[] buffer = new byte[2048];
      FileOutputStream fos = new FileOutputStream(entry.getName());
      BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
      while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
        bos.write(buffer, 0, size);
      }
      attachment.setFileLob(buffer);
      attachment.setFileSize((long)buffer.length);
      bos.flush();
      bos.close();

      waff.create(attachment);
//            mv = linkAttachment(attachType,attachment,transRec,wtWellNum);
    }

    // Get attachment collection by attachment Type
    wtAttachmentCollection = getAttachmentCollectionByType(transRec, attachType);

    // Get all checklist depend on Project Type
    Collection<WtChecklist> wtChecklistCollection = getChecklistByType(attachType);

    mv.addObject("attachmentCollection", wtAttachmentCollection);
    mv.addObject("checklistCollection", wtChecklistCollection);
    mv.addObject("attachType", attachType);
    return mv;
  }

  // Link Attachment to Projects depend on attachment type
  private void linkAttachment(String attachType, Integer wtAttachmentId, WtTrans transRec, String wtWellNum) {
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
    WtReservoirFacade wrvf = (WtReservoirFacade) appContext.getBean(WtReservoirFacade.class.getSimpleName());
    WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
    WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    Collection<WtAttachment> wtAttachmentCollection = new ArrayList();    
    WtAttachment attachment = waf.find(wtAttachmentId);

    if ("BI".equalsIgnoreCase(attachType) || "PP".equalsIgnoreCase(attachType)) {
      attachment.setWtTrans(transRec);
//        waf.edit(attachment);
    } else if ("TR".equalsIgnoreCase(attachType)) {
      attachment.setWtTransR(transRec);
    } else if ("CI".equalsIgnoreCase(attachType)) {
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      if (cropIdling == null) {
        cropIdling = new WtCropIdling();
        wcif.create(cropIdling);
        cropIdling.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        cropIdling.setWtAttachmentCollection(wtAttachmentCollection);
        wcif.edit(cropIdling);
      }
      attachment.setWtCropIdling(cropIdling);
    } else if ("CI_MAP".equalsIgnoreCase(attachType)) {
      Collection<WtCropIdling> wtCropIdlingCollection = new ArrayList();
      WtCropIdling cropIdling2 = transRec.getWtCropIdling();
      if (cropIdling2 == null) {
        cropIdling2 = new WtCropIdling();
        wcif.create(cropIdling2);
        cropIdling2.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        cropIdling2.setWtAttachmentCollection(wtAttachmentCollection);
        wcif.edit(cropIdling2);
      } 
      wtCropIdlingCollection.add(cropIdling2);
      attachment.setWtCropIdlingCollection(wtCropIdlingCollection);
//      attachment.setWtCropIdling2(cropIdling2);
    } else if ("RV".equalsIgnoreCase(attachType)) {
      WtReservoir reservoir = transRec.getWtReservoir();
      if (reservoir == null) {
        reservoir = new WtReservoir();
        wrvf.create(reservoir);
        reservoir.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        reservoir.setWtAttachmentCollection(wtAttachmentCollection);
        wrvf.edit(reservoir);
      }
      attachment.setWtReservoir(reservoir);
    } else if ("GW".equalsIgnoreCase(attachType)) {
      WtGroundwater groundwater = transRec.getWtGroundwater();
      if (groundwater == null) {
        groundwater = new WtGroundwater();
        wgwf.create(groundwater);
        groundwater.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        groundwater.setWtAttachmentCollection(wtAttachmentCollection);
        wgwf.edit(groundwater);
      }
      attachment.setWtGroundwater(groundwater);
//        waf.edit(attachment);
    } else if ("WELL".equalsIgnoreCase(attachType)) {
      if (!wtWellNum.isEmpty()) {
        wwf.set("wtWellNum", wtWellNum);
        WtWell well = wwf.find();
        attachment.setWtWell(well);
      }
    }
    waf.edit(attachment);
  }

  // Link Attachment to Projects depend on attachment type
  private void linkAttachment(String attachType, WtAttachment attachment, WtTrans transRec, String wtWellNum) {
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
    WtReservoirFacade wrvf = (WtReservoirFacade) appContext.getBean(WtReservoirFacade.class.getSimpleName());
    WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
    WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    Collection<WtAttachment> wtAttachmentCollection = new ArrayList();

    if ("BI".equalsIgnoreCase(attachType) || "PP".equalsIgnoreCase(attachType)) {
      attachment.setWtTrans(transRec);
//        waf.edit(attachment);
    } else if ("TR".equalsIgnoreCase(attachType)) {
      attachment.setWtTransR(transRec);
    } else if ("CI".equalsIgnoreCase(attachType)) {
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      if (cropIdling == null) {
        cropIdling = new WtCropIdling();
        wcif.create(cropIdling);
        cropIdling.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        cropIdling.setWtAttachmentCollection(wtAttachmentCollection);
        wcif.edit(cropIdling);
      }
      attachment.setWtCropIdling(cropIdling);
    } else if ("CI_MAP".equalsIgnoreCase(attachType)) {
      WtCropIdling cropIdling = transRec.getWtCropIdling();
      if (cropIdling == null) {
        cropIdling = new WtCropIdling();
        wcif.create(cropIdling);
        cropIdling.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        cropIdling.setWtMapAttCollection(wtAttachmentCollection);
        wcif.edit(cropIdling);
      }
      attachment.setWtCropIdling(cropIdling);
    } else if ("RV".equalsIgnoreCase(attachType)) {
      WtReservoir reservoir = transRec.getWtReservoir();
      if (reservoir == null) {
        reservoir = new WtReservoir();
        wrvf.create(reservoir);
        reservoir.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        reservoir.setWtAttachmentCollection(wtAttachmentCollection);
        wrvf.edit(reservoir);
      }
      attachment.setWtReservoir(reservoir);
    } else if ("GW".equalsIgnoreCase(attachType)) {
      WtGroundwater groundwater = transRec.getWtGroundwater();
      if (groundwater == null) {
        groundwater = new WtGroundwater();
        wgwf.create(groundwater);
        groundwater.setWtTrans(transRec);
        wtAttachmentCollection.add(attachment);
        groundwater.setWtAttachmentCollection(wtAttachmentCollection);
        wgwf.edit(groundwater);
      }
      attachment.setWtGroundwater(groundwater);
//        waf.edit(attachment);
    } else if ("WELL".equalsIgnoreCase(attachType)) {
      if (!wtWellNum.isEmpty()) {
        wwf.set("wtWellNum", wtWellNum);
        WtWell well = wwf.find();
        attachment.setWtWell(well);
      }
    }
    waf.edit(attachment);
  }

  public void uploadFiles(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
    Iterator<String> itr = request.getFileNames();
    MultipartFile mpf = null;

    // Get each file
    while (itr.hasNext()) {
      mpf = request.getFile(itr.next());
    }
  }

  //Just break link to proposal/wells
  public ModelAndView removeAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
    WtReservoirFacade wrvf = (WtReservoirFacade) appContext.getBean(WtReservoirFacade.class.getSimpleName());
    WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
    WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));

    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachment attachment = waf.find(requestUtil.getInt("wtAttachmentId"));
    String attachType = requestUtil.getString("attachTypeId");
    Collection<WtChecklist> checklistCollection = getChecklistByType(attachType);

    // Remove Template file
    if (attachment != null && "TEMP".equalsIgnoreCase(attachType)) {
//      waf.remove(attachment);
      attachment.setIsActive(0);
      waf.edit(attachment);
    }

    // Remove other files
    if (transRec != null && attachment != null) {
      Collection<WtAttachment> wtAttachmentCollection = new ArrayList();
      if ("BI".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = transRec.getWtAttachmentCollection();
        wtAttachmentCollection.remove(attachment);
//        wtf.edit(transRec);
        attachment.setWtTrans(null);
      }
      if ("PP".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = getPPAttachments(transRec.getWtAttachmentCollection());
        wtAttachmentCollection.remove(attachment);
//        wtf.edit(transRec);
        attachment.setWtTrans(null);
      }
      if ("TR".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = transRec.getWtReportCollection();
        wtAttachmentCollection.remove(attachment);
//        wtf.edit(transRec);
        attachment.setWtTrans(null);
      }
      if ("CI".equalsIgnoreCase(attachType)) {
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        if (cropIdling != null) {
          wtAttachmentCollection = cropIdling.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
//          wcif.edit(cropIdling);
          attachment.setWtCropIdling(null);
        }
      }
      if ("CI_MAP".equalsIgnoreCase(attachType)) {
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        if (cropIdling != null) {
          wtAttachmentCollection = cropIdling.getWtMapAttCollection();
          wtAttachmentCollection.remove(attachment);
//          wcif.edit(cropIdling);
//          attachment.setWtCropIdling2(null);
          Collection<WtCropIdling> wtCropIdlingCollection = attachment.getWtCropIdlingCollection();
          wtCropIdlingCollection.remove(cropIdling);
          attachment.setWtCropIdlingCollection(wtCropIdlingCollection);
        }
      }
      if ("RV".equalsIgnoreCase(attachType)) {
        WtReservoir reservoir = transRec.getWtReservoir();
        if (reservoir != null) {
          wtAttachmentCollection = reservoir.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
//          wrvf.edit(reservoir);
          attachment.setWtReservoir(null);
        }
      }
      if ("GW".equalsIgnoreCase(attachType)) {
        WtGroundwater groundwater = transRec.getWtGroundwater();
        if (groundwater != null) {
          wtAttachmentCollection = groundwater.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
//          wgwf.edit(groundwater);
          attachment.setWtGroundwater(null);
        }
      }

      if ("WELL".equalsIgnoreCase(attachType)) {
        JSONObject jsonData = new JSONObject(requestUtil.getString("wellType"));
        checklistCollection = getChecklistByType(attachType, jsonData.optInt("wellTransfer"), jsonData.optInt("wellMonitoring"));
        mv = new ModelAndView("attachment/AssociateWellAttachmentList");
        wwf.set("wtWellNum", jsonData.optString("wtWellNum"));
        WtWell well = wwf.find();
        attachment.setWtWell(null);
        waf.edit(attachment);
//        attachment.setWtWell(well);
//        waf.remove(attachment);
        mv = AssociateWellList(mv, well);
        mv.addObject("wellCheckList", checklistCollection);
      }

//      waf.remove(attachment);
      attachment.setIsActive(0);
      waf.edit(attachment);

      mv = AttachmentInfomation(mv, wtAttachmentCollection);
      mv.addObject("attachmentCollection", wtAttachmentCollection);
      mv.addObject("checklistCollection", checklistCollection);
      mv.addObject("attachType", attachType);
    }

    return mv;
  }

  // Will delete attachment file from table
  public ModelAndView deleteAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtCropIdlingFacade wcif = (WtCropIdlingFacade) appContext.getBean(WtCropIdlingFacade.class.getSimpleName());
    WtReservoirFacade wrvf = (WtReservoirFacade) appContext.getBean(WtReservoirFacade.class.getSimpleName());
    WtGroundwaterFacade wgwf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));

    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachment attachment = waf.find(requestUtil.getInt("wtAttachmentId"));
    String attachType = requestUtil.getString("attachTypeId");
    Collection<WtChecklist> checklistCollection = getChecklistByType(attachType);

    // Remove Template file
    if (attachment != null && "TEMP".equalsIgnoreCase(attachType)) {
      waf.remove(attachment);
    }

    // Remove other files
    if (transRec != null && attachment != null) {
      Collection<WtAttachment> wtAttachmentCollection = new ArrayList();
      if ("BI".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = transRec.getWtAttachmentCollection();
        wtAttachmentCollection.remove(attachment);
        wtf.edit(transRec);
      }
      if ("PP".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = getPPAttachments(transRec.getWtAttachmentCollection());
        wtAttachmentCollection.remove(attachment);
        wtf.edit(transRec);
      }
      if ("TR".equalsIgnoreCase(attachType)) {
        wtAttachmentCollection = transRec.getWtReportCollection();
        wtAttachmentCollection.remove(attachment);
        wtf.edit(transRec);
      }
      if ("CI".equalsIgnoreCase(attachType)) {
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        if (cropIdling != null) {
          wtAttachmentCollection = cropIdling.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
          wcif.edit(cropIdling);
        }
      }
      if ("CI_MAP".equalsIgnoreCase(attachType)) {
        WtCropIdling cropIdling = transRec.getWtCropIdling();
        if (cropIdling != null) {
          wtAttachmentCollection = cropIdling.getWtMapAttCollection();
          wtAttachmentCollection.remove(attachment);
          wcif.edit(cropIdling);
        }
      }
      if ("RV".equalsIgnoreCase(attachType)) {
        WtReservoir reservoir = transRec.getWtReservoir();
        if (reservoir != null) {
          wtAttachmentCollection = reservoir.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
          wrvf.edit(reservoir);
        }
      }
      if ("GW".equalsIgnoreCase(attachType)) {
        WtGroundwater groundwater = transRec.getWtGroundwater();
        if (groundwater != null) {
          wtAttachmentCollection = groundwater.getWtAttachmentCollection();
          wtAttachmentCollection.remove(attachment);
          wgwf.edit(groundwater);
        }
      }

      if ("WELL".equalsIgnoreCase(attachType)) {
        JSONObject jsonData = new JSONObject(requestUtil.getString("wellType"));
//        checklistCollection = getChecklistByType(attachType, jsonData.optInt("wellTransfer"), jsonData.optInt("wellMonitoring"));
        if (jsonData.optInt("wellTransfer") == 1 && jsonData.optInt("wellMonitoring") == 0) {
          checklistCollection = LookupDataContext.getInstance().getWellTransChecklist();
        } else if (jsonData.optInt("wellTransfer") == 0 && jsonData.optInt("wellMonitoring") == 1) {
          checklistCollection = LookupDataContext.getInstance().getWellMonitChecklist();
        }
        mv = new ModelAndView("attachment/AssociateWellAttachmentList");
        wwf.set("wtWellNum", jsonData.optString("wtWellNum"));
        WtWell well = wwf.find();
        attachment.setWtWell(well);
        waf.remove(attachment);
        mv = AssociateWellList(mv, well);
        mv.addObject("wellCheckList", checklistCollection);
      }

      waf.remove(attachment);
      mv = AttachmentInfomation(mv, wtAttachmentCollection);
      mv.addObject("attachmentCollection", wtAttachmentCollection);
      mv.addObject("checklistCollection", checklistCollection);
      mv.addObject("attachType", attachType);
    }

    return mv;
  }

  public void getAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
//        ModelAndView mv = new ModelAndView("GetAttachment");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtAttachmentFacade wtf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
    WtAttachmentWithFile att = new WtAttachmentWithFile();
    att.setWtAttachmentId(requestUtil.getInt("wtAttachmentId"));
//        WtAttachment attachment = wtf.find(requestUtil.getInt("wtAttachmentId"));
    WtAttachmentWithFile attachment = waff.find(att);
    if (attachment != null) {
      String contentType = attachment.getMimeType();
      if (requestUtil.getString("output").equalsIgnoreCase("file")) {
        contentType = CONTENT_TYPE_OCTETSTREAM;
      }

      if (contentType.equalsIgnoreCase(CONTENT_TYPE_OCTETSTREAM)) {
        response.setHeader("Content-Disposition", "attachment;filename=" + attachment.getFilename());
      }

      response.setContentType(contentType);
      InputStream is = new ByteArrayInputStream(attachment.getFileLob());
      IOUtils.copy(is, response.getOutputStream());
      response.setIntHeader("Refresh", 5);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
//        mv.addObject("attachment", attachment);
//        mv.addObject("attachmentFile", response);
//        return mv;
  }

  @RequestMapping("/view/{attachmentId}")
  public void view(@PathVariable("attachmentId") Integer attachmentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      LoggedInCheck(request, response);
      WtAttachmentWithFileFacade waff = WebUtil.getFacade(WtAttachmentWithFileFacade.class);
      WtAttachmentWithFile attachment = waff.find(new WtAttachmentWithFile(attachmentId));

      if (attachment == null) {
        throw new Exception("File not found!");
      }

      response.setHeader("Content-Disposition", "attachment;filename=" + attachment.getFilename());
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      try (InputStream is = new ByteArrayInputStream(attachment.getFileLob())) {
        IOUtils.copy(is, response.getOutputStream());
      }
    }
    catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  @RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
  public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LoggedInCheck(request, response);
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtAttachmentWithFileFacade waff = (WtAttachmentWithFileFacade) appContext.getBean(WtAttachmentWithFileFacade.class.getSimpleName());
    waff.set("title", requestUtil.getString("title"));
//        waf.set("title", "Other");
    waff.set("attachmentType", "TEMPLATE");
    waff.set("isActive", 1);
    WtAttachmentWithFile attachment = waff.find();

    if (attachment == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    String contentType = attachment.getMimeType();
    String fileName = attachment.getFilename();
    fileName = URLDecoder.decode(fileName, "ISO8859_1");
    response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
//        response.setHeader("Content-Type", contentType);
    response.setContentType(contentType);
//        response.setIntHeader("Refresh", 5);

    InputStream is = new ByteArrayInputStream(attachment.getFileLob());
    IOUtils.copy(is, response.getOutputStream());
    is.close();
    response.flushBuffer();
  }

  @RequestMapping("/edit/{attachmentId}")
  public ModelAndView edit(@PathVariable("attachmentId") Integer attachmentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mv = null;
    try {
      LoggedInCheck(request, response);
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      WtAttachment attachment = new WtAttachment();
      mv = new ModelAndView("attachment/EditFile");
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format("SELECT %1$s.*"
                                    + "\nFROM %1$s "
                                    + "\nWHERE %1$s.WT_ATTACHMENT_ID = ?"
                                    ,EntityUtil.getTableName(WtAttachment.class)
      );
      GenericFacade.executeQuery(conn, query, Arrays.asList(attachmentId), new QueryDelegate(attachment) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          WtAttachment attachment = this.getListener();
          if(rs.next()) {
            attachment.Load(rs);
          }else{
            attachment = null;
          }
        }
      });

      Collection<WtChecklist> checklist = new ArrayList<>();
      query = String.format("SELECT %1$s.*"
                            + "FROM %1$s JOIN %2$s ON %1$s.WT_CHECKLIST_ID = %2$s.WT_CHECKLIST_ID "
                            + "WHERE %2$s.WT_ATTACHMENT_ID = ?"
                            , EntityUtil.getTableName(WtChecklist.class)
                            ,"WT_ATT_CHECKLIST");
      GenericFacade.executeQuery(conn, query, Arrays.asList(attachmentId), new QueryDelegate(checklist) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          Collection<WtChecklist> checklist = this.getListener();
          WtChecklist check = null;
          while(rs.next()) {
            check = new WtChecklist();
            check.Load(rs);
            checklist.add(check);
          }
        }
      });
      attachment.setWtChecklistCollection(checklist);

      String attachType = requestUtil.getString("attachType");
      String wtTransId = requestUtil.getString("wtTransId");
      WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
      Collection<WtChecklist> wtChecklistCollection = getChecklistByType(attachType);

      if (attachType.equals("WELL")) {
        JSONObject jsonData = new JSONObject(requestUtil.getString("wellType"));
        wtChecklistCollection = getChecklistByType(attachType, jsonData.optInt("wellTransfer"), jsonData.optInt("wellMonitoring"));
        mv.addObject("wellType", jsonData);
      }
      if (attachment == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      if (attachType.equals("TEMP")) {
        mv = new ModelAndView("attachment/EditTemplate");
        mv.addObject("attachment", attachment);
        return mv;
      }

      if(null != transRec){
        mv.addObject("statusFlag", transRec.getWtStatusFlag());
      }
      mv.addObject("attachment", attachment);
      mv.addObject("attachType", attachType);
      mv.addObject("wtTansId", wtTransId);      
      mv.addObject("checklistCollection", wtChecklistCollection);
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklistByType(String)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }
    return mv;
  }

//  public ModelAndView edit2(@PathVariable("attachmentId") Integer attachmentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
//    LoggedInCheck(request, response);
//    ModelAndView mv = new ModelAndView("attachment/EditFile");
//    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//    WtAttachmentFacade wtf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
//    WtAttachment attachment = wtf.find(attachmentId);
//    String attachType = requestUtil.getString("attachType");
//    String wtTransId = requestUtil.getString("wtTransId");
//    Collection<WtChecklist> wtChecklistCollection = getChecklistByType(attachType);
//
//    if (attachType.equals("WELL")) {
//      JSONObject jsonData = new JSONObject(requestUtil.getString("wellType"));
//      wtChecklistCollection = getChecklistByType(attachType, jsonData.optInt("wellTransfer"), jsonData.optInt("wellMonitoring"));
//      mv.addObject("wellType", jsonData);
//    }
//    if (attachment == null) {
//      response.sendError(HttpServletResponse.SC_NOT_FOUND);
//    }
//    if (attachType.equals("TEMP")) {
//      mv = new ModelAndView("attachment/EditTemplate");
//      mv.addObject("attachment", attachment);
//      return mv;
//    }
//
//    mv.addObject("attachment", attachment);
//    mv.addObject("attachType", attachType);
//    mv.addObject("wtTansId", wtTransId);
//    mv.addObject("checklistCollection", wtChecklistCollection);
//    return mv;
//  }

  public ModelAndView updateAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
    LoggedInCheck(request, response);
    ModelAndView mv = new ModelAndView("attachment/AttachmentFileList");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtAttachmentFacade waf = (WtAttachmentFacade) appContext.getBean(WtAttachmentFacade.class.getSimpleName());
    WtChecklistFacade wclf = (WtChecklistFacade) appContext.getBean(WtChecklistFacade.class.getSimpleName());
    AppUserFacade auf = (AppUserFacade) appContext.getBean(AppUserFacade.class.getSimpleName());
    WtWellFacade wf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    AppUser user = (AppUser) request.getSession().getAttribute(SESSION_KEY_USER);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    WtAttachment attachment = waf.find(requestUtil.getInt("wtAttachmentId"));
    if (attachment == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    attachment.setTitle(requestUtil.getString("title"));
//        attachment.setAttachmentType(requestUtil.getString("attachmentType"));
    if (requestUtil.getString("ceqaSubmittedDate").isEmpty()) {
      attachment.setCeqaSubmittedDate(null);
    } else {
      attachment.setCeqaSubmittedDate(sdf.parse(requestUtil.getString("ceqaSubmittedDate")));
    }
    attachment.setDescription(requestUtil.getString("description"));
    attachment.setUpdatedById(user.getUserId());

    // Link attachment to checklist table
    String[] checklists = request.getParameterValues("checklist");
    Collection<WtChecklist> wtChecklistCollection = new ArrayList();
    attachment.setWtChecklistCollection(null);
    if (checklists != null) {
      for (String checklist : checklists) {
        wclf.set("wtChecklistId", Integer.parseInt(checklist));
        WtChecklist checklistCollection = wclf.find();
        wtChecklistCollection.add(checklistCollection);
      }
      attachment.setWtChecklistCollection(wtChecklistCollection);
    }

    waf.edit(attachment);

    // Rebuild all attachment list file
    WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
    String attachType = requestUtil.getString("attachType");
    Collection<WtAttachment> attachmentCollection = getAttachmentCollectionByType(transRec, attachType);
    Collection<WtChecklist> checklistCollection = getChecklistByType(attachType);

    if (attachType.equals("WELL")) {
      mv = new ModelAndView("attachment/AssociateWellAttachmentList");
      WtWell wells = new WtWell();
      wells.setWtWellNum(requestUtil.getString("wtWellNum"));
      checklistCollection = getChecklistByType(attachType, requestUtil.getInt("wellTransfer"), requestUtil.getInt("wellMonitoring"));
      mv = AssociateWellList(mv, wf.find(wells));
      mv.addObject("wellCheckList", checklistCollection);
    }
    mv = AttachmentInfomation(mv, attachmentCollection);
    // Get FSA Map# if attachment type is
    if (attachType.equals("CI_MAP") && attachmentCollection.size()>0) {
      Integer fsaMapNum = getFsaMapNum(attachmentCollection);
      mv.addObject("fsaMapNum", fsaMapNum);
    }

    mv.addObject("attachmentCollection", attachmentCollection);
    mv.addObject("checklistCollection", checklistCollection);
    mv.addObject("attachType", attachType);
    mv.addObject("statusFlag", transRec.getWtStatusFlag());
    return mv;
  }

//    public void updateAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
//    {
//        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
//        WtAttachmentFacade wtf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
//        WtChecklistFacade wclf = (WtChecklistFacade)appContext.getBean(WtChecklistFacade.class.getSimpleName());
//        AppUser user = (AppUser)request.getSession().getAttribute(SESSION_KEY_USER);
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//
//        WtAttachment attachment = wtf.find(requestUtil.getInt("wtAttachmentId"));
//        if (attachment == null) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//
//        attachment.setTitle(requestUtil.getString("title"));
////        attachment.setAttachmentType(requestUtil.getString("attachmentType"));
//        if(requestUtil.getString("ceqaSubmittedDate").isEmpty()){
//            attachment.setCeqaSubmittedDate(null);
//        } else {
//            attachment.setCeqaSubmittedDate(sdf.parse(requestUtil.getString("ceqaSubmittedDate")));
//        }
//        attachment.setDescription(requestUtil.getString("description"));
//        attachment.setUpdatedById(user.getUserId());
//
//        // Link attachment to checklist table
//        String[] checklists = request.getParameterValues("checklist");
//        Collection<WtChecklist> wtChecklistCollection = new ArrayList();
//        if(checklists != null){
//            for(String checklist: checklists){
//                wclf.set("wtChecklistId", Integer.parseInt(checklist));
//                WtChecklist checklistCollection = wclf.find();
//                wtChecklistCollection.add(checklistCollection);
//            }
//            attachment.setWtChecklistCollection(wtChecklistCollection);;
//        }
//
//        wtf.edit(attachment);
//    }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Private Methods of Get">
  protected Collection<WtChecklist> getChecklistByType(String attachType) {
//    WtChecklistFacade wclf = WebUtil.getFacade(WtChecklistFacade.class);
//    String checklistField = attachType.toUpperCase() + "_ATTACHMENT";
//    Collection<WtChecklist> checklistCollection = wclf.select("SELECT WT.* FROM WT_CHECKLIST WT WHERE CHECKLIST_FIELD='" + checklistField + "' ORDER BY SORT_ORDER", com.gei.entities.WtChecklist.class);
//
//    return checklistCollection;
    Collection<WtChecklist> result = new ArrayList<>();
    try {
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD = ? ORDER BY %1$s.SORT_ORDER"
        ,EntityUtil.getTableName(WtChecklist.class)
      );
      GenericFacade.executeQuery(conn, query, Arrays.asList(attachType.toUpperCase() + "_ATTACHMENT"), new QueryDelegate(result) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          Collection<WtChecklist> checklists = this.getListener();
          WtChecklist checklist = null;
          while (rs.next()) {
            checklist = new WtChecklist();
            checklist.Load(rs);
            checklists.add(checklist);
          }
        }
      });
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklistByType(String)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }

  private Collection<WtChecklist> getChecklistByType(String attachType, Integer wellTransfer, Integer wellMonitoring) {
    Collection<WtChecklist> result = new ArrayList();
    try {
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.CHECKLIST_FIELD = ?", EntityUtil.getTableName(WtChecklist.class));
      List params = new ArrayList();
      params.add(attachType.toUpperCase() + "_ATTACHMENT");

      if (wellTransfer == 1 && wellMonitoring == 0) {
        query += " AND WELL_TRANSFER = ?";
        params.add(wellTransfer);
      } else if (wellTransfer == 0 && wellMonitoring == 1) {
        query += " AND WELL_MONITORING = ?";
        params.add(wellMonitoring);
      }

      GenericFacade.executeQuery(conn, query, params, new QueryDelegate(result) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          Collection<WtChecklist> checklists = this.getListener();
          WtChecklist checklist = null;
          while (rs.next()) {
            checklist = new WtChecklist();
            checklist.Load(rs);
            checklists.add(checklist);
          }
        }
      });
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getChecklistByType(String,Integer,Integer)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }

  protected Collection<WtAttachment> getAttachmentCollectionByType(WtTrans transRec, String attachType) {
    Collection<WtAttachment> result = new ArrayList<>();
    try {
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format("SELECT %1$s.* FROM ATTACHMENT_VIEW", "ATTACHMENT_VIEW");
      List params = new ArrayList();
      String whereand = "\nWHERE ";
      switch (attachType) {
        case "PP":
          query += String.format(whereand + "  %1$s.ATTACHMENT_TYPE = ?","ATTACHMENT_VIEW");
          params.add("PP");
          whereand = "\nAND ";
        case "BI":
          query += String.format(whereand + " %1$s.WT_ATTACHMENT_ID IN (SELECT %2$s.WT_ATTACHMENT_ID FROM %2$s WHERE %2$s.WT_TRANS_ID = ?)"
            ,"ATTACHMENT_VIEW"
            ,"WT_TRANS_ATTACHMENTS"
          );
          params.add(transRec.getWtTransId());
          break;
        case "TR":
          query += String.format("\nWHERE %1$s.WT_ATTACHMENT_ID IN (SELECT %2$s.WT_ATTACHMENT_ID FROM %2$s WHERE %2$s.WT_TRANS_ID = ?)"
            ,"ATTACHMENT_VIEW"
            ,"WT_TRANS_REPORT"
          );
          params.add(transRec.getWtTransId());
          break;
        case "CI":
          query += String.format("\nJOIN %2$s "
            + "\nJOIN %3$s ON %2$s.WT_CROP_IDLING_ID = %3$s.WT_CROP_IDLING_ID"
            + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID"
            + "\nWHERE %3$s.WT_TRANS_ID = ?"
            ,"ATTACHMENT_VIEW"
            ,"WT_CI_ATTACHMENT"
            ,EntityUtil.getTableName(WtCropIdling.class)
          );
          params.add(transRec.getWtTransId());
          break;
        case "CI_MAP":
          query += String.format("\nJOIN %2$s "
            + "\nJOIN %3$s ON %2$s.WT_CROP_IDLING_ID = %3$s.WT_CROP_IDLING_ID"
            + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID"
            + "\nWHERE %3$s.WT_TRANS_ID = ?"
            ,"ATTACHMENT_VIEW"
            ,"WT_CI_MAP_ATTACHMENT"
            ,EntityUtil.getTableName(WtCropIdling.class)
          );
          params.add(transRec.getWtTransId());
          break;
        case "RV":
          query += String.format("\nJOIN %2$s "
            + "\nJOIN %3$s ON %2$s.WT_RESERVOIR_ID = %3$s.WT_RESERVOIR_ID"
            + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID"
            + "\nWHERE %3$s.WT_TRANS_ID = ?"
            ,"ATTACHMENT_VIEW"
            ,"WT_RV_ATTACHMENT"
            ,EntityUtil.getTableName(WtReservoir.class)
          );
          params.add(transRec.getWtTransId());
          break;
        case "GW":
          query += String.format("\nJOIN %2$s "
            + "\nJOIN %3$s ON %2$s.WT_GROUNDWATER_ID = %3$s.WT_GROUNDWATER_ID"
            + "\nON %1$s.WT_ATTACHMENT_ID = %2$s.WT_ATTACHMENT_ID"
            + "\nWHERE %3$s.WT_TRANS_ID = ?"
            ,"ATTACHMENT_VIEW"
            ,"WT_GW_ATTACHMENT"
            ,EntityUtil.getTableName(WtGroundwater.class)
          );
          params.add(transRec.getWtTransId());
          break;
      }

      GenericFacade.executeQuery(conn, query, params, new QueryDelegate(result) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          ChecklistCollection checklists = LookupDataContext.getInstance().getChecklists(true);
          Map<Integer,WtAttachment> attMap = new LinkedHashMap<>();
          WtAttachment attachment = null;
          Integer attId = null;
          Integer clId = null;
          WtChecklist checklist = null;
          AppUser user = null;
          WtContact contact = null;
          while (rs.next()) {
            attId = rs.getInt("WT_ATTACHMENT_ID");
            if ((attachment = attMap.get(attId)) == null) {
              attachment = new WtAttachment();
              attachment.Load(rs);
              attachment.setWtChecklistCollection(new ArrayList<WtChecklist>());
              attMap.put(attId, attachment);

              user = new AppUser();
              user.Load(rs, EntityUtil.getTableName(AppUser.class) + "_");

              contact = new WtContact();
              contact.Load(rs, EntityUtil.getTableName(WtContact.class) + "_");

              user.setWtContact(contact);
              contact.setUser(user);

              attachment.setCreatedBy(user);
            }
            if (((clId = rs.getInt("WT_CHECKLIST_ID")) != null)
              && ((checklist = checklists.getChecklist(clId))!=null)){
              attachment.getWtChecklistCollection().add(checklist);
            }
          }

          Collection<WtAttachment> attachments = this.getListener();
          attachments.addAll(attMap.values());
        }
      });

      //filter non active attachments
      if(result.size()>0){
        Iterator<WtAttachment> it = result.iterator();
        while(it.hasNext()){
          WtAttachment att = it.next();
          if(att != null){
            if(Objects.equals(att.getIsActive(), 0)){
              it.remove();
            }
          }
        }
      }
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
          ,this.getClass().getName()
          ,"getAttachmentCollectionByType(WtTrans,String)"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }
    return result;
  }

  private Collection<WtAttachment> getPPAttachments(Collection<WtAttachment> wtAttachmentCollection) {
    Collection<WtAttachment> PPAttachmentCollection = new ArrayList();
    for (WtAttachment attachment : wtAttachmentCollection) {
      if (attachment.getAttachmentType() != null && attachment.getAttachmentType().equals("PP")) {
        PPAttachmentCollection.add(attachment);
//          wtAttachmentCollection.remove(this);
      }
    }
    return PPAttachmentCollection;
  }

  private ModelAndView AttachmentInfomation(ModelAndView mv, Collection<WtAttachment> collection) {
//        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
    JSONObject wellData = new JSONObject();
    for (WtAttachment attach : collection) {
      try {
        //Build User Name here
        wellData.put(""+attach.getCreatedById(),buildUserName(attach));

        // Get File Size Info here
//          long bytes = attach.getFileLob().length;
        Integer bytes = attach.getFileSize();
        if (bytes != null || bytes > 0) {
          wellData.put("" + attach.getWtAttachmentId(), sizeFormat(bytes));
        }
      } catch (NullPointerException ex) {
      }
    }
    mv.addObject("filesize", wellData);
    return mv;
  }

  private Integer getFsaMapNum(Collection<WtAttachment> wtAttachmentCollection){
    Integer totalNum = 0;
    for (WtAttachment attachment : wtAttachmentCollection) {
      if(attachment.getIsActive() > 0){
        Collection<WtChecklist> checklist = attachment.getWtChecklistCollection();
        if (checklist.size()>0){
          for(WtChecklist check : checklist){
            if ("CI_MAP_ATTACHMENT".equalsIgnoreCase(check.getChecklistField())
                    && check.getSortOrder()==12){
              totalNum++;
              break;
            }
          }
        }
      }
    }
    return totalNum;
  }

  //<editor-fold defaultstate="collapsed" desc="Get Associated Well Attachment Checklist">
  private Collection<WtChecklist> getWellAttachmentChecklist(WtAttachment attachment) throws Exception{
    Collection<WtChecklist> result = new ArrayList<>();
    try{
      Connection conn = ConnectionContext.getConnection("WtDataSource");
      String query = String.format(("SELECT %1$s.* FROM %1$s\n"
              + "JOIN %2$s ON %1$s.WT_CHECKLIST_ID = %2$s.WT_CHECKLIST_ID\n"
              + "WHERE %2$s.WT_ATTACHMENT_ID = ?")
              , EntityUtil.getTableName(WtChecklist.class)
              , "WT_ATT_CHECKLIST");

      GenericFacade.executeQuery(conn, query, Arrays.asList(attachment.getWtAttachmentId()), new QueryDelegate(result) {
        @Override
        public void handle(ResultSet rs) throws Exception {
          Collection<WtChecklist> checklists = this.getListener();
          WtChecklist checklist = null;
          while (rs.next()) {
            checklist = new WtChecklist();
            checklist.Load(rs);
            checklists.add(checklist);
          }
        }
      });
    }catch(Exception ex){
      throw new IllegalStateException(
        String.format("%s.%s %s:\n%s"
                ,this.getClass().getName()
                ,"getWellAttachmentChecklist(WtAttachment)"
                ,ex.getClass().getName()
                ,ex.getMessage()
        )
      );
    }
    return result;
  }
  //</editor-fold>

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods for Wells">
  public ModelAndView addAssociatedWells(HttpServletRequest request, HttpServletResponse response) throws NullPointerException, ParseException, IOException {
    ModelAndView mv = null;
    try {
      LoggedInCheck(request, response);
      WtWellFacade wwf = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
      WtGroundwaterFacade wgf = (WtGroundwaterFacade) appContext.getBean(WtGroundwaterFacade.class.getSimpleName());
      WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
      ServletRequestUtil requestUtil = new ServletRequestUtil(request);
      int wtTransId = requestUtil.getInt("wtTransId");

      String wellList = requestUtil.getString("wellLists");
      JSONArray wellListObj = new JSONArray(wellList);

      Collection<WtWell> wells = wwf.findAll();
      WtWell newWell = new WtWell();
      wtf.set("wtTransId", wtTransId);
      WtTrans wt = wtf.find();
      // Get Status for permission control          
      WtStatusFlag status = null;
      if(wt != null){
        status = wt.getWtStatusFlag();
      }

      Collection<JSONObject> objwell = new ArrayList<>();
      Collection<WtWell> wellCollection = wt.getWtGroundwater().getWtWellCollection();
      if(wellListObj.length()>0){
        for (int i = 0; i < wellListObj.optJSONArray(0).length(); i++) {
          JSONObject tmp = wellListObj.optJSONArray(0).optJSONObject(i);
          String wellName = tmp.optString("statewellnum");
          String siteId = tmp.optString("sitecode");
          String localWellId = tmp.optString("localwellid");
          int stationId = tmp.optInt("stationid");
          if (!siteId.isEmpty()) {
            boolean isExist = false;
            for (WtWell well : wells) {
              if (siteId.equals(well.getWtWellNum())) {
                isExist = true;
                newWell = well;
                if (!wellCollection.contains(newWell)) {
                  wellCollection.add(newWell);
                }
              }
            }
            if (!isExist) {
              newWell = new WtWell();
              newWell.setStateWellNum(wellName);
              newWell.setWtWellNum(siteId);
              newWell.setLocalWellId(localWellId);
              newWell.setCasgenStationId(stationId);
              wwf.create(newWell);
              wellCollection.add(newWell);
            }
          }
        }
      }
      for (WtWell well : wellCollection) {
        JSONObject wellObj = new JSONObject(well.toMap());
        wellObj.put("lastCalibrateDate", dateFormat(wellObj.optString("lastCalibrateDate")));
        wellObj.optString("wellMonitoring");
        wellObj.optString("wellTransfer");
        if (wellObj.optString("wellMonitoring").isEmpty() && wellObj.optString("wellTransfer").isEmpty()) {
          wellObj.put("checklist", false);
        } else {
          wellObj.put("checklist", wwf.checklistItem(well));
        }
        objwell.add(wellObj);
      }
      wgf.edit(wt.getWtGroundwater());
      mv = new ModelAndView("templates/associateWells");
      mv.addObject("wellList", objwell);
      mv.addObject("status", status);
    }
    catch (Exception ex) {
      response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    
    return mv;
  }

  public ModelAndView filterAssociatedWell(HttpServletRequest request, HttpServletResponse response) throws NullPointerException, ParseException {
    LoggedInCheck(request, response);
    WtTransFacade wtf = (WtTransFacade) appContext.getBean(WtTransFacade.class.getSimpleName());
    WtWellFacade wef = (WtWellFacade) appContext.getBean(WtWellFacade.class.getSimpleName());
    ModelAndView mv = new ModelAndView("templates/associateWells");
    ServletRequestUtil requestUtil = new ServletRequestUtil(request);
    int wtTransId = requestUtil.getInt("wtTransId");
    String welltype = requestUtil.getString("welltype");
    JSONObject welltypeObj = new JSONObject(welltype);
    Collection<JSONObject> filteredWell = new ArrayList<>();
    Collection<WtWell> wellCol = wtf.find(wtTransId).getWtGroundwater().getWtWellCollection();
    for (WtWell well : wellCol) {
      if (welltypeObj.optBoolean("monitoring")) {
        if (well.getWellMonitoring() != null) {
          if (well.getWellMonitoring() > 0) {
            if (!filteredWell.contains(well)) {
              JSONObject wellObj = new JSONObject(well.toMap());
              wellObj.put("lastCalibrateDate", dateFormat(wellObj.optString("lastCalibrateDate")));
              wellObj.put("checklist", wef.checklistItem(well));
              filteredWell.add(wellObj);
            }
          }
        }
      }
      if (welltypeObj.optBoolean("transfer")) {
        if (well.getWellTransfer() != null) {
          if (well.getWellTransfer() > 0) {
            if (!filteredWell.contains(well)) {
              JSONObject wellObj = new JSONObject(well.toMap());
              wellObj.put("lastCalibrateDate", dateFormat(wellObj.optString("lastCalibrateDate")));
              wellObj.put("checklist", wef.checklistItem(well));
              filteredWell.add(wellObj);
            }
          }
        }
      }
      if (!welltypeObj.optBoolean("monitoring") && !welltypeObj.optBoolean("transfer")) {
        if (well.getWellTransfer() == null && well.getWellMonitoring() == null) {
          JSONObject wellObj = new JSONObject(well.toMap());
          wellObj.put("lastCalibrateDate", dateFormat(wellObj.optString("lastCalibrateDate")));
          filteredWell.add(wellObj);
        } else if (well.getWellTransfer() != null && well.getWellMonitoring() != null) {
          if (well.getWellTransfer() < 1 && well.getWellMonitoring() < 1) {
            JSONObject wellObj = new JSONObject(well.toMap());
            wellObj.put("lastCalibrateDate", dateFormat(wellObj.optString("lastCalibrateDate")));
            filteredWell.add(wellObj);
          }
        }
      }
    }
    mv.addObject("wellList", filteredWell);
    return mv;
  }

  public ModelAndView AssociateWellList(ModelAndView mv, WtWell well) {
//        AppUserFacade auf = (AppUserFacade)appContext.getBean(AppUserFacade.class.getSimpleName());
    JSONObject wellData = new JSONObject();
    if (well == null) {
      return mv;
    }
    for (WtAttachment attach : well.getWtAttachmentCollection()) {
      if(Objects.equals(attach.getWtAttachmentId(), null)){
        return mv;
      }
      //Build User Name here
      wellData.put(""+attach.getCreatedById(),buildUserName(attach));

      // Get File Size Info here
//          long bytes = attach.getFileLob().length;
      Integer bytes = attach.getFileSize();
      if (bytes != null) {
        wellData.put("" + attach.getWtAttachmentId(), sizeFormat(bytes));
      }
      attach.getWtChecklistCollection();
    }
    mv.addObject("WtWell", well);
    mv.addObject("size", wellData);
    return mv;
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Utils Methods">
    // Build User Name
    private String buildUserName(WtAttachment attachment){
      String result = null;
      try {
        Connection conn = ConnectionContext.getConnection("WtDataSource");
        String query = String.format("SELECT %1$s.* FROM %1$s WHERE %1$s.WT_CONTACT_ID = (SELECT %2$s.WT_CONTACT_ID FROM %2$s WHERE %2$s.USER_ID = ?)"
          , EntityUtil.getTableName(WtContact.class)
          , EntityUtil.getTableName(AppUser.class)
        );
        WtContact contact = new WtContact();
        GenericFacade.executeQuery(conn, query, Arrays.asList(attachment.getCreatedById()), new QueryDelegate(contact) {
          @Override
          public void handle(ResultSet rs) throws Exception {
            if (rs.next()) {
              WtContact contact = this.getListener();
              contact.Load(rs);
            }
          }
        });

        if (!StringUtils.isEmpty(contact.getFirstName())) {
          result = contact.getFirstName();
          if (!StringUtils.isEmpty(contact.getLastName())) {
            result += " " + contact.getLastName();
          }
        }
        else {
          result = "";
        }
      }
      catch(Exception ex) {
        throw new IllegalStateException(
          String.format("%s.%s %s:\n%s"
            ,this.getClass().getName()
            ,"buildUserName(WtAttachment)"
            ,ex.getClass().getName()
            ,ex.getMessage()
          )
        );
      }
      return result;
    }

    private String sizeFormat(Integer bytes){
      String kbString = "";
      if (bytes.equals(0)){
        kbString = "0 Bytes";;
        return kbString;
      }
      int unit = 1024;
      int exp = (int) (Math.log(bytes) / Math.log(unit));
      if(exp == 0)
      {
        exp = 1;
      }
      String suffix = ("KMGTPE").charAt(exp-1)+"";
      kbString = String.format("%.1f %sB", bytes / Math.pow(unit, exp), suffix);
      if(kbString.equals("0.0 KB"))
      {
        kbString = bytes+" Bytes";
      }

      return kbString;
    }

    public String dateFormat(String date) throws ParseException{
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        if(!date.isEmpty())
        {
            java.util.Date format = dt.parse(date);
            return dt1.format(format);
        }
        else{
            return "";
        }
    }
//    public String parsePathName(String path)
//    {
//        String name[] = path.split("\\\\");
//        if(name.length>0)
//        {
//            return name[name.length-1];
//        }
//        return path;
//    }
    //</editor-fold>
}
