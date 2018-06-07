/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.controller;

import com.gei.constants.Status;
import com.gei.entities.WtAgency;
import com.gei.entities.WtAttachment;
import com.gei.entities.WtContact;
import com.gei.entities.WtFuType;
import com.gei.entities.WtStatusFlag;
import com.gei.entities.WtTrans;
import com.gei.entities.WtWell;
import com.gei.facades.WtAgencyFacade;
import com.gei.facades.WtAttachmentFacade;
import com.gei.facades.WtFuTypeFacade;
import com.gei.facades.WtStatusFlagFacade;
import com.gei.facades.WtTransFacade;
import com.gei.facades.WtWellFacade;
import com.gei.utils.ServletRequestUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
//import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author ymei
 */
@Controller
@RequestMapping("/trans")
public class TransController extends BaseController{
    @Autowired
    ApplicationContext appContext;
    public final static String CONTENT_TYPE_OCTETSTREAM="application/octet-stream"
                                ,CONTENT_TYPE_PDF="application/pdf";

//    @RequestMapping(value="/",method=RequestMethod.GET)
//    public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
//    {
//        ModelAndView mv = new ModelAndView("index");
//
//        return mv;
//    }

    public ModelAndView searchMain(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("SearchTrans");
        mv.addObject("bodyMessage", "Search Water Transfer Records");

        return mv;
    }

    public ModelAndView searchTrans(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("TransRecTable");
        mv.addObject("getDataUrl", request.getContextPath() + "/trans/getDataTrans");
        mv.addObject("updateDataUrl", request.getContextPath() + "/trans/updateDataTrans");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans req = new WtTrans();

        String wtTransNum = requestUtil.getString("wtTransNum");
        if(!StringUtils.isEmpty(wtTransNum)) { req.setWtTransNum(wtTransNum); }
        String transYear = requestUtil.getString("transYear");
        if(!StringUtils.isEmpty(transYear)) { req.setTransYear(requestUtil.getInt("transYear")); }
        String transYearFromS = requestUtil.getString("transYearFrom");
        Integer transYearFrom = null;
        if(!StringUtils.isEmpty(transYearFromS)) { transYearFrom = requestUtil.getInt("transYearFrom"); }
        String transYearToS = requestUtil.getString("transYearTo");
        Integer transYearTo = null;
        if(!StringUtils.isEmpty(transYearToS)) { transYearTo = requestUtil.getInt("transYearTo"); }

        // Search by Seller Code
        String sellerCode = requestUtil.getString("sellerCode").toUpperCase();
        WtAgency seller = null;
        if(!StringUtils.isEmpty(sellerCode)) {
            seller = new WtAgency();
            seller.setAgencyCode(sellerCode);
            WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
            List<WtAgency> sellers = waf.searchAgency(seller);
            if (sellers == null || sellers.isEmpty()) {
                seller = null;
            } else {
                seller = sellers.get(0);
            }
        }
        // search by Buyer Code
        String buyerCode = requestUtil.getString("buyerCode").toUpperCase();
        WtAgency buyer = null;
        if(!StringUtils.isEmpty(buyerCode)) {
            buyer = new WtAgency();
            buyer.setAgencyCode(buyerCode);
            WtAgencyFacade waf = (WtAgencyFacade)appContext.getBean(WtAgencyFacade.class.getSimpleName());
            List<WtAgency> buyers = waf.searchAgency(buyer);
            if (buyers == null || buyers.isEmpty()){
                buyer = null;
            } else {
                buyer = buyers.get(0);
            }
        }
        // search by Buyer Code
        String fuTypeCode = requestUtil.getString("fuTypeCode").toUpperCase();
        WtFuType fuType = null;
        if(!StringUtils.isEmpty(fuTypeCode)) {
            fuType = new WtFuType();
            fuType.setFuType(fuTypeCode);
            WtFuTypeFacade wff = (WtFuTypeFacade)appContext.getBean(WtFuTypeFacade.class.getSimpleName());
            List<WtFuType> fuTypes = wff.searchFuType(fuType);
            if (fuTypes == null || fuTypes.isEmpty()){
                fuType = null;
            } else {
                fuType = fuTypes.get(0);
            }
        }

        List<WtTrans> records = wtf.searchTrans(req,transYearFrom,transYearTo,seller,buyer,fuType);
//        for(int i=0; i<records.size(); i++){
//            WtTrans rec = records.get(i);
//            Collection<WtAttachmentNoFile> attachRecs = rec.getAttachmentCollection();
//            for(WtAttachmentNoFile attachRec : attachRecs){
//                String filename = attachRec.getFilename();
//                System.out.print(filename);
//            }
//        }
        mv.addObject("transRecords", records);
        return mv;
    }

    public ModelAndView getTransChart(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LoggedInCheck(request,response);
        ModelAndView mv = new ModelAndView("historic/TransChart");
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtStatusFlagFacade wsff = (WtStatusFlagFacade)appContext.getBean(WtStatusFlagFacade.class.getSimpleName());
        WtStatusFlag statusFlag = wsff.find(Status.TCOMPLETE);

//        WtTrans trans = new WtTrans();
//        trans.setWtStatusFlag(statusFlag);
        List<WtTrans> transRecords = wtf.findAll();
        transRecords = wtf.select("SELECT WT.* FROM WT_TRANS WT WHERE WT.WT_STATUS_FLAG_ID="+Status.TCOMPLETE+" ORDER BY TRANS_YEAR",com.gei.entities.WtTrans.class);
        List<WtTrans> chartRecords = new ArrayList();
        Integer totalProTransQua = 0;
        Integer totalActTransQua = 0;
        Integer aveProTransQua = 0;
        Integer aveActTransQua = 0;

        if(transRecords == null || transRecords.isEmpty()){
            return mv;
        }
        for (WtTrans t : transRecords){
            Integer transYear = t.getTransYear();
//            if (t.getProTransQua() != null){
//                totalProTransQua = totalProTransQua + t.getProTransQua();
//            }
            if(t.getActTransQua() != null){
                totalActTransQua = totalActTransQua + t.getActTransQua();
            }

            if(chartRecords.isEmpty()){
                chartRecords.add(getNewTrans(t));
            } else {
                for (WtTrans c : chartRecords){
                    if (c.getTransYear().equals(t.getTransYear())){
                        transYear = null;
                        if(t.getProTransQua() != null){
                            c.setProTransQua(c.getProTransQua()+t.getProTransQua());
                        }
                        if(t.getActTransQua() != null){
                            c.setActTransQua(c.getActTransQua()+t.getActTransQua());
                        }
                    }
                }
                if (transYear != null){
                    chartRecords.add(getNewTrans(t));
                }
            }
        }

        Integer size = chartRecords.size();
        if (!size.equals(0)) {
            aveProTransQua = totalProTransQua/size;
            aveActTransQua = totalActTransQua/size;
        }

        mv.addObject("aveProTransQua", aveProTransQua);
        mv.addObject("aveActTransQua", aveActTransQua);
        mv.addObject("chartRecords", chartRecords);
        return mv;
    }

    private WtTrans getNewTrans(WtTrans t){
        WtTrans cr = new WtTrans();
        cr.setTransYear(t.getTransYear());
        if(t.getProTransQua() == null){
//            cr.setProTransQua(0);
        } else {
            cr.setProTransQua(t.getProTransQua());
        }
        if(t.getActTransQua() == null){
            cr.setActTransQua(0);
        } else {
            cr.setActTransQua(t.getActTransQua());
        }

        return cr;
    }

    public ModelAndView getDataTrans(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("GetDataTrans");
        mv.addObject("bodyMessage", "Water Transfer Record");
        mv.addObject("getAttUrl", request.getContextPath() + "/trans/getAttachment");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans req = new WtTrans();
        req.setWtTransId(requestUtil.getInt("wtTransId"));
//        WtTrans transRec = wtf.searchTrans(req).get(0);
        WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));

        mv.addObject("transRecord", transRec);
        return mv;
    }

    public void getAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
//        ModelAndView mv = new ModelAndView("GetAttachment");
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtAttachmentFacade wtf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
        WtAttachment att = new WtAttachment();
        att.setWtAttachmentId(requestUtil.getInt("wtAttachmentId"));
        WtAttachment attachment = wtf.find(requestUtil.getInt("wtAttachmentId"));
        if (attachment != null) {
            String contentType = attachment.getMimeType();
            if (requestUtil.getString("output").equalsIgnoreCase("file"))
            {
                contentType=CONTENT_TYPE_OCTETSTREAM;
            }

            if (contentType.equalsIgnoreCase(CONTENT_TYPE_OCTETSTREAM))
            {
                response.setHeader("Content-Disposition", "attachment;filename=" + attachment.getFilename());
            }

            response.setContentType(contentType);
//            InputStream is = new ByteArrayInputStream(attachment.getFileLob());
//            IOUtils.copy(is, response.getOutputStream());
//            response.setIntHeader("Refresh", 5);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
//        mv.addObject("attachment", attachment);
//        mv.addObject("attachmentFile", response);
//        return mv;
    }

    public void updateTrans(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException
    {
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());

        // Get updated information for record
        WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));
//        String wtTransNum = requestUtil.getString("wtTransNum");
//        if(!StringUtils.isEmpty(wtTransNum)) { transRec.setWtTransNum(wtTransNum); }
//        String transYear = requestUtil.getString("transYear");
//        if(!StringUtils.isEmpty(transYear)) { transRec.setTransYear(requestUtil.getInt("transYear")); }
        if (transRec != null) {
            String wtComm= requestUtil.getString("wtComm");
            if(!StringUtils.isEmpty(wtComm)) { transRec.setWtComm(wtComm); }

            // Create new wtFuType Collection for Edit
            String[] fuTypeCodes = request.getParameterValues("wtFuType");
            WtFuType fuType = null;
            Collection<WtFuType> wtFuTypeCollection = new ArrayList();
            if(fuTypeCodes!=null && fuTypeCodes.length > 0) {
                for(String fuTypeCode : fuTypeCodes){
                    fuType = new WtFuType();
                    fuType.setFuType(fuTypeCode);
                    WtFuTypeFacade wff = (WtFuTypeFacade)appContext.getBean(WtFuTypeFacade.class.getSimpleName());
                    List<WtFuType> fuTypes = wff.searchFuType(fuType);
                    if (fuTypes != null && fuTypes.size() == 1){
                        fuType = fuTypes.get(0);
                        wtFuTypeCollection.add(fuType);
                    }
                }
            }
//            transRec.setWtFuTypeCollection(wtFuTypeCollection);

            //Upload Files
//            uploadFiles(request, response);

            wtf.edit(transRec);
        }
    }

    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException{
        ModelAndView mv = new ModelAndView("UploadFile");
        Boolean existFile = false;

        if(ServletFileUpload.isMultipartContent(request))
        {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Configure a repository
            ServletContext servletContext = this.getServletContext();
//            String dir = servletContext.getInitParameter("FILE_LOAD_TMP_DIR");
//            factory.setRepository(new File(dir));
            File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository );

           // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
//          FileItemIterator iter = upload.getItemIterator(request);
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));
            Iterator<FileItem> iter = items.iterator();
            WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
            WtTrans transRec = new WtTrans();
            WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
            WtAttachment attachment = new WtAttachment();

            while (iter.hasNext()) {

//              FileItemStream item = iter.next();
//              InputStream stream = item.getInputStream();;
                FileItem item = iter.next();
                if (item.isFormField())
                {
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    if ("description".equals(fieldName) && !StringUtils.isEmpty(fieldValue )){
                        attachment.setDescription(fieldValue);
                    }
                    if ("wtTransId".equals(fieldName) && !StringUtils.isEmpty(fieldValue )){
                        transRec = wtf.find(Integer.parseInt(fieldValue));
                    }
                } else {
                    attachment.setFilename(item.getName());
                    List<WtAttachment> attRecs = waf.searchAttachment(attachment);
                    if (attRecs != null && attRecs.size()>0){
                        existFile = true;
                    } else {
//                        attachment.setMimeType(item.getContentType());
//                        attachment.setFileLob(item.get());
                    }
                }
            }

//            if (existFile){
//                response.setContentType("text/plain");
//                response.getWriter().println("File upload failed.");
//            }

            // Create New File
            if (!existFile){
                waf.create(attachment);
            }

            // Link Attachment to Trans Record
            if(transRec != null && attachment.getWtAttachmentId() != null){
                Collection<WtAttachment> wtAttachmentCollection = transRec.getWtAttachmentCollection();
                wtAttachmentCollection.add(attachment);

                wtf.edit(transRec);
            }
        }

//        mv.addObject("existFile", existFile);
    }

    public void uploadFiles(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException
    {
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;

        // Get each file
        while(itr.hasNext()){
            mpf = request.getFile(itr.next());
        }
    }

    public void removeFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans transRec = wtf.find(requestUtil.getInt("wtTransId"));

        WtAttachmentFacade waf = (WtAttachmentFacade)appContext.getBean(WtAttachmentFacade.class.getSimpleName());
        WtAttachment attachment = waf.find(requestUtil.getInt("wtAttachmentId"));

        if(transRec != null && attachment != null){
            Collection<WtAttachment> wtAttachmentCollection = transRec.getWtAttachmentCollection();
            wtAttachmentCollection.remove(attachment);
            wtf.edit(transRec);
        }
    }

    public void saveTrans(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        ServletRequestUtil requestUtil = new ServletRequestUtil(request);
        Integer wtTransId = 0;

        WtTransFacade wtf = (WtTransFacade)appContext.getBean(WtTransFacade.class.getSimpleName());
        WtTrans req = new WtTrans();

        req.setWtTransNum(requestUtil.getString("wtTransNum"));
        req.setTransYear(requestUtil.getInt("transYear"));
//        req.setProTransQua(requestUtil.getInt("proTransQua"));
        req.setActTransQua(requestUtil.getInt("actTransQua"));
        req.setDwrProApprDate(requestUtil.getSQLDate("dwrProApprDate", "mm/dd/yyyy"));
        req.setTransWinStart(requestUtil.getSQLDate("transWinStart", "mm/dd/yyyy"));
        req.setTransWinEnd(requestUtil.getSQLDate("transWinEnd", "mm/dd/yyyy"));
//        req.setProAgreePaid(requestUtil.getDouble("ProAgreePaid"));
        String proAgreePaid = requestUtil.getString("proAgreePaid");
        if(!StringUtils.isEmpty(proAgreePaid)) { req.setProAgreePaid(requestUtil.getDouble("proAgreePaid")); }
//        req.setProAgreePaid(new BigDecimal(requestUtil.getString("proAgreePaid")));
        req.setActAmtPaid(requestUtil.getDouble("actAmtPaid"));
        req.setCalAmtPaid(requestUtil.getDouble("calAmtPaid"));
        req.setProUnitCost(requestUtil.getDouble("proUnitCost"));
        req.setCalUnitCost(requestUtil.getDouble("calUnitCost"));

        req.setProAcrIdle(requestUtil.getInt("proAcrIdle"));
        req.setProAcrIdleInd(requestUtil.getInt("proAcrIdleInd"));
        req.setActFallAcr(requestUtil.getInt("actFallAcr"));
        req.setActFallAcrInd(requestUtil.getInt("actFallAcrInd"));
        req.setResReOpInd(requestUtil.getInt("resReOpInd"));
        req.setConsWaterInd(requestUtil.getInt("consWaterInd"));
        req.setWellUseNum(requestUtil.getInt("wellUseNum"));
        req.setWellUseNumInd(requestUtil.getInt("wellUseNumInd"));
        req.setWtComm(requestUtil.getString("wtComm"));

        wtf.create(req);

        wtTransId = req.getWtTransId();
        if(wtTransId != null){

            String wtFuTypeIds = requestUtil.getString("wtFuTypeIds");

        }
    }
}
