<%--
    Document   : GroundwaterInfo_preview.jsp
    Created on : Jun 19, 2015, 9:30:41 AM
    Author     : ymei
--%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="__Stylesheets">
  ${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
  ,${pageContext.request.contextPath}/resources/css/AgencyContact.css
  ,${pageContext.request.contextPath}/resources/css/jquery.loadmask.css
</c:set>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.cookie.js
  ,${pageContext.request.contextPath}/resources/js/jquery.maskedinput.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.loadmask.min.js
</c:set>
<c:if test="${previewType eq 'pdf'}">
  <%@include file="../templates/print/header.jsp" %>
</c:if>
<c:if test="${empty(previewType)}">
  <%@include file="../templates/header.jsp" %>
</c:if>
<style type="text/css">
  * {font-family: Arial,"Trebuchet MS",Helvetica,sans-serif !important;}
  #review-gw {
    width:10.5in;
    margin:0 auto;
    padding: 20px;
    box-shadow: 0 0 5px #444;
    /*position:relative;*/
    background-color: white;
  }
  h1 {
    color: #0178a3;
    font-size:36px;
  }
  .section .header {
    color:#000000;
    font-size:24px;
    margin-top:20px;
    margin-bottom:15px;
  }
  .content {
    margin-bottom:15px;
  }
  .content label {
    font-weight:bold;
    margin-bottom:10px;
  }
  .content label,.content span {
    display:block;
  }
  #review-gw .highlight{
    font-size: 15px;
    font-weight: bold;
    padding-left: 10px;
    padding-right: 10px;
  }
  table{
    width:100%;
  }
  table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    width: 60px;
    text-align: center;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
  }
  #gw-monthly-table{
    /*width:900px;*/
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #gw-monthly-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #gw-monthly-table td input{
    width:50px;
  }
  #gw-monthly-table tbody tr td:not(:nth-child(1)){
    text-align: center;
  }
  #attachment-table{
    /*width:900px;*/
    width:100%;
    text-align: center;
    margin-bottom: 10px;
  }
  #attachment-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #checklist-table{
    width: 100%;
  }
  #checklist-table th{
    border:0px solid #cccccc;
  }
  #checklist-table td{
    text-align: left;
    border:0px solid #cccccc;
    padding: 3px;
    font-size: 15px;
  }
  .underline{
    text-decoration: underline;
  }
  .associated_wells{
    width:900px;
  }
  .associated_wells thead th{
    background-color:#4F81BD;
    color:white;
    text-align: center;
  }
  .pdfBtn{
    background-color: #ffffff;
    border: 1px solid #666666;
    border-radius: 3px;
    color: #00517a;
    font-size: 12pt !important;
    float:right;
    cursor:pointer;
  }
  .pdfBtn:hover{
    background-color: #0B94C3;
    color: white;
  }
  .hidden{
    display: none;
  }
  .pagebreak{
    border-bottom: 1px dashed #666666;
    width:100%;
    float:left;
  }
  .setPageBreak{
    padding-top: 10px;
    /*    position: relative;
        left:-15%;*/
  }
  .btn{
    background: #B1D4DF;
    border:none;
    cursor:pointer;
    border-radius: 3px;
  }
  .btn:focus{
    outline:none !important;
  }
  .btn:hover{
    background:#0B94C3;
  }
  @media print{
    div#review-gw{
      border:none;
      background-color:#FFF;
      box-shadow: none;
      padding:0;
    }
    table{
      border-collapse: collapse;
      width:70% !important;
    }
    .section input#cmd{
      display:none;
    }
    #review-gw span,#review-gw table td, #review-gw table th{
      font-size: 8pt;
    }
    .pagebreak{
      page-break-after: always;
      border:none;
      float:none;
    }
    .setPageBreak{
      display:none;
    }
  }
</style>
<div id="review-gw">
  <%@include file="../templates/print/pagenumber.jsp" %>
  <c:if test="${empty(previewType)}">
    <input class="pdfBtn" id="savePdfBtn" type="button" style="font-size: 12px;" value="Save PDF" gwId="${id}" userId="${sessionScope.USER.userId}" wtTransId="${groundwater.wtTrans.wtTransId}"/>
    <input class="pdfBtn" id="printPdfBtn" type="button" style="font-size: 12px;" value="Print PDF" gwId="${id}" userId="${sessionScope.USER.userId}" wtTransId="${groundwater.wtTrans.wtTransId}"/>
  </c:if>
  <form action="${pageContext.request.contextPath}/report/print/${id}" target="_blank" id="GWpdfForm" method="post">
    <input type="hidden" name="userId" value="${sessionScope.USER.userId}"/>
    <input type="hidden" name="wtTransId" value="${groundwater.wtTrans.wtTransId}"/>
    <input type="hidden" name="attachType" value="GW"/>
    <input type="hidden" name="track" value=""/>
    <input type="hidden" name="linebreakJson" value="${fn:escapeXml(linebreak)}"/>
  </form>
  <h1>Groundwater Proposal Report</h1>
  <div class="section" section='gwinfo'>
    <div class="header">Groundwater Information</div>
    <div>
      <span>Number of Proposed Transfer Wells: </span>
      <span class="highlight"><fmt:formatNumber type="number" value="${groundwater.pumpingWellsNumber}"/></span>
    </div>
    <div>  
      <span>Number of Proposed Monitoring Wells: </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.monitoringWellsNumber}"/></span>
    </div>
    <div>
      <span>Total Proposed Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.totalPumping}"/></span>
    </div>
    <div>
      <span>Baseline Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.basePumping}"/></span>
    </div>
    <div>
      <span>Gross Transfer Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.grossTransPumping}"/></span>
    </div>
    <div>
      <span>Streamflow Depletion Factor %: </span><span class="highlight">${groundwater.depletionFactor}</span>
    </div>
    <div>
      <span>Streamflow Depletion (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.streamDepletion}"/></span>
    </div>
    <div>
      <span>Net Transfer Water (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.netTransWater}"/></span>
    </div>
  </div>
    
  <c:set var="initPagebreak" value="hidden"/>
  <c:if test='${!empty(linebreak.optString("gwinfo"))}'>
    <c:set var="initPagebreak" value='${linebreak.optString("gwinfo")}'/>
  </c:if>
  <div class='pagebreak ${initPagebreak}'>&#160;</div>
  <div class="setPageBreak"><button class="btn">Insert Page Break</button></div>
  <div class="section" section='pumping'>
    <div class="header">Estimated Monthly Total Groundwater Pumping (acre-feet)</div>
    <table id="gw-monthly-table">
      <thead>
        <tr style="width:400px;">
          <th style="width:100px;"></th>
          <th>Jan</th>
          <th>Feb</th>
          <th>Mar</th>
          <th>Apr</th>
          <th>May</th>
          <th>June</th>
          <th>July</th>
          <th>Aug</th>
          <th>Sep</th>
          <th>Oct</th>
          <th>Nov</th>
          <th>Dec</th>
          <th>Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Proposed Pumping</td>
          <c:set var="totPP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="proposedPumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="proposedPumping" value="${gwMonthly.proposedPumping}"/>
                  <c:set var="totPP" value="${totPP + proposedPumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${proposedPumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totPP}"/></td>
        </tr>
        <tr>
          <td>Baseline Pumping</td>
          <c:set var="totBP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="baselinePumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="baselinePumping" value="${gwMonthly.baselinePumping}"/>
                  <c:set var="totBP" value="${totBP + baselinePumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${baselinePumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totBP}"/></td>
        </tr>
        <tr>
          <td>Gross Transfer Pumping</td>
          <c:set var="totTP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="grossTransPumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="grossTransPumping" value="${gwMonthly.grossTransPumping}"/>
                  <c:set var="totTP" value="${totTP + grossTransPumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${grossTransPumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totTP}"/></td>
        </tr>
        <tr>
          <td>Depletions</td>
          <c:set var="totD" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="streamDepletion" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="streamDepletion" value="${gwMonthly.streamDepletion}"/>
                  <c:set var="totD" value="${totD + streamDepletion}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${streamDepletion}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totD}"/></td>
        </tr>
        <tr>
          <td>Net Transfer Water</td>
          <c:set var="totTW" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="netTransWater" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="netTransWater" value="${gwMonthly.netTransWater}"/>
                  <c:set var="totTW" value="${totTW + netTransWater}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${netTransWater}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totTW}"/></td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class='pagebreak ${linebreak.optString("pumping")}'>&#160;</div>
  <div class="setPageBreak"><button class="btn">Insert Page Break</button></div>
  <div class="section" section='gwchecklist'>
    <div class="header">Attachments</div>
    <table id="attachment-table">
      <tr class="attach_header">
        <th>Attached File</th>
        <th>Size</th>
        <th>Title</th>
        <th>Description</th>
        <th>Uploaded By</th>
        <th>Upload Date</th>
      </tr>
      <c:forEach var="attachment" items="${groundwater.wtAttachmentCollection}">
        <c:set var="wtAttachmentId">${attachment.wtAttachmentId}</c:set>
        <c:set var="createdById">${attachment.createdById}</c:set>
          <tr>
            <td>${attachment.filename}</td>
          <td>${nameSize.optString(wtAttachmentId)}</td>
          <td>${attachment.title}</td>
          <td>${attachment.description}</td>
          <td>${nameSize.optString(createdById)}</td>
          <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.createdDate}" /></td>
        </tr>
      </c:forEach>
    </table>

    <table>
      <thead>
        <tr>
          <th style="width:250px;">Document Check List</th>
          <th>File Name</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="checklist" items="${checklistCollection}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td>${checklist.name}</td>
            <td>
              <c:forEach var="attachment" items="${groundwater.wtAttachmentCollection}">
                <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
                  <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                    <c:set var="isMissing" value="0"/>
                    <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">
                      ${attachment.filename}&#160;
                    </a>
                  </c:if>
                </c:forEach>
              </c:forEach>
              <c:if test="${isMissing == 1}">
                <span style="color:red"></span>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <div class='pagebreak ${linebreak.optString("gwchecklist")}'>&#160;</div>
  <div class="setPageBreak"><button class="btn">Insert Page Break</button></div>

  <c:if test="${fn:length(groundwater.wtWellCollection) gt 0}">
    <div class="section">
      <div class="header">List of Associated Wells</div>
      <table id="associateWellCt">
        <thead>
          <tr>
            <th>Master Side Code</th>
            <th>State Well Number</th>
            <th>Local Well Designation</th>
            <th>Transfer Well?</th>
            <th>Monitoring Well?</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="list" items="${groundwater.wtWellCollection}">
            <tr>
              <td fieldName="wtWellNum">${list.wtWellNum}</td>
              <td fieldName="stateWellNum">${list.stateWellNum}</td>
              <td fieldName="localWellId">${list.localWellId}</td>
              <td fieldName="wellTransfer"><c:choose><c:when test="${list.wellTransfer == 1}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
              <td fieldName="wellMonitoring"><c:choose><c:when test="${list.wellMonitoring == 1}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
                </tr>
          </c:forEach>
        </tbody>
      </table>
<!--      <div style="padding-top:5px;">
        <img src="https://maps.googleapis.com/maps/api/staticmap?key=AIzaSyBlizcrpShX6pzaQeRURCwQjEBg20NSWOc&center=${mapView.getString('center')}&size=400x300&maptype=hybrid&markers=${mapView.getString('points')}" height='300' width='400' />
      </div>-->

      <!--<div class="header">Each Associated Well Information</div>-->
      <c:forEach var="well" items="${groundwater.wtWellCollection}" varStatus="count">
        <div class="hidden" section='wellattachment_${count.index}'></div>
        <c:set var="welllinebreak">wellattachment_${count.index}</c:set>
        <div class="pagebreak ${linebreak.optString(welllinebreak)}">&#160;</div>
        <div class="setPageBreak"><button class="btn">Insert Page Break</button></div>
        <c:set var="isTransfer" value="hidden" />
        <c:set var="isMonitoring" value="hidden" />
        <c:if test="${well.wellTransfer == 1}">
         <c:set var="isTransfer" value="" />
        </c:if>
        <c:if test="${well.wellMonitoring == 1}">
          <c:set var="isMonitoring" value="" />
        </c:if>
          
        <div style="margin:10px;">
          <span style="padding-right:10px;">Site Code: </span><span class="highlight">${well.wtWellNum}</span>
          <span style="padding-right:10px;">State Well Number: </span><span class="highlight">${well.stateWellNum}</span>
          <span style="padding-right:10px;">Local Well Name: </span><span class="highlight">${well.localWellId}</span>
        </div>
        
        <div style="margin:10px 10px 10px 10px;">
          <span style="padding-right:10px;">Well Type:</span><span class="highlight">
            <c:if test="${well.wellTransfer == 1}">Transfer Well</c:if>
            <c:if test="${well.wellMonitoring == 1&&well.wellTransfer == 1}">/</c:if>
            <c:if test="${well.wellMonitoring == 1}">Monitoring Well</c:if></b>
          </span>
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${isMonitoring}" style="padding-right:60px;">Data Collection Agency: </span><span class="highlight">${well.dataCollectAgency}</span>    
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${isTransfer}" style="padding-right:60px;">Meter Last Calibrated (Date): </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${well.lastCalibrateDate}" /></span>
          <span class="${isTransfer}" style="padding-right:60px;">Meter Make: </span><span class="highlight">${well.meterMake}</span>
          <span class="${isTransfer}" style="padding-right:60px;">Meter Model: </span><span class="highlight">${well.meterModel}</span>
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${isTransfer}" style="padding-right:60px;">
            Date of Last Flow Meter Installation Certificate:
            <span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${well.meterLastInstall}" /></span>
          </span>
          <span class="${isTransfer}" style="padding-right:60px;">
            Meter Serial Number:
            <span class="highlight">${well.serialNum}</span>
          </span>
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${isTransfer}" style="padding-right:60px;">Power Source: <span class="highlight">${well.powerSource}</span></span>
          <span class="${isTransfer}" style="padding-right:60px;">Well Capacity: <span class="highlight">${well.wellCapacity} ${well.meterUnits}</span></span>
        </div>
        <table class="associated_wells">
          <thead>
            <tr>
              <th>File Name</th>
              <th>Size</th>
              <th>Title</th>
              <th>Description</th>
              <th>Uploaded By</th>
              <th>Upload Date</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${well.wtAttachmentCollection}" var="attach" >
              <c:set var="wtAttachmentId">${attach.wtAttachmentId}</c:set>
              <c:set var="createdById">${attach.createdById}</c:set>
              <tr>
                <td>${attach.filename}</td>
                <td>${nameSize.optString(wtAttachmentId)}</td>
                <td>${attach.title}</td>
                <td>${attach.description}</td>
                <td>${nameSize.optString(createdById)}</td>
                <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attach.modifiedDate}" /></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <table style="margin-top:20px;width:500px;">
          <thead>
            <tr>
              <th>Check List Items</th>
              <th>File Name</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="cl" items="${nameSize.optJSONArray(well.wtWellNum).getArrayList()}">
              <c:set var="isMissing" value="1"/>
              <tr>
                <td>${cl.optString('name')}</td>
                <td>
                  <c:forEach items="${well.wtAttachmentCollection}" var="attach" >
                    <c:forEach items="${attach.wtChecklistCollection}" var="checklist" >
                      <c:if test="${(cl.optString('wtChecklistId')) == checklist.wtChecklistId}">
                        <c:set var="isMissing" value="0"/>
                        <a href="${pageContext.request.contextPath}/attachment/view/${attach.wtAttachmentId}" onClick="" target="_blank">
                          ${attach.filename}&#160;
                        </a>
                      </c:if>
                    </c:forEach>
                  </c:forEach>
                  <c:if test="${isMissing == 1}">
                    <span style="color:red"></span>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:forEach>
    </div>
  </c:if>
</div>
<div id="editor"></div>
<c:if test="${empty(previewType)}">
  <%@include file="../templates/footer.jsp" %>
</c:if>
<c:if test="${previewType eq 'pdf'}">
  <%@include file="../templates/print/footer.jsp" %>
</c:if>
<script>
//    window.sessionStorage.clear();
//    var _lineBreakStorage = $.cookie("pagebreak-data")==null ? new Object : JSON.parse($.cookie('pagebreak-data'));
  var lineBreak = function () {
    var self = this;
    self.init = function () {
      self.initItems();
      self.initListeners();
      self.updatePageBreakMsg();
    };
    self.initItems = function () {
      self.proposalCt = $("#tabs");
      self.statusTrackTab = self.proposalCt.find("#statustrack_tab");
      self.gwPreviewCt = $("#review-gw");
      self.pageBreak = self.gwPreviewCt.find(".pagebreak");
      self.pageBreakBtn = self.gwPreviewCt.find(".setPageBreak button");
      self.pdfBtn = self.gwPreviewCt.find(".pdfBtn");
      self.printPdfBtn = self.gwPreviewCt.find("#printPdfBtn");
      self.savePdfBtn = self.gwPreviewCt.find("#savePdfBtn");
      self.pdfForm = self.gwPreviewCt.find("#GWpdfForm");
    };
    self.initListeners = function () {
//            self.pdfBtn.on("click",self.initPdfPreview);
      self.printPdfBtn.on("click", self.initPrintPdf);
      self.savePdfBtn.on("click", self.initSavePdf);
      self.pageBreakBtn.on("click", function () {
        $(this).blur();
        self.page = $(this).parent().prev();
        self.section = self.page.prev().attr("section");
        self.linebreakJson = JSON.parse(self.pdfForm.find("input[name=linebreakJson]").val());
        if (self.page.hasClass("hidden"))
        {
          self.page.removeClass("hidden");
          self.isHidden = "notHidden";
          $(this).html("Remove Page Break");
        }
        else
        {
          self.page.addClass("hidden");
          self.isHidden = "hidden";
          $(this).html("Insert Page Break");
        }
        self.linebreakJson[self.section] = self.isHidden;
        self.pdfForm.find("input[name=linebreakJson]").val(JSON.stringify(self.linebreakJson));
        $.cookie(self.section, self.isHidden);
      });
    };
    self.updatePageBreakMsg = function () {
      $.each(self.pageBreakBtn, function () {
        var page = $(this).parent().prev();
        $(this).html("Insert Page Break");
        if (!page.hasClass("hidden")) {
          $(this).html("Remove Page Break");
        }
        ;
      });
    };
    self.initPdfPreview = function () {
      var msg = "Would you like to backup the PDF File to the Database?";
      self.userValidation(msg, function (bool) {
        self.pdfForm.find("input[name=track]").val(bool);
        self.pdfForm.submit();
      });
    };
    self.initPrintPdf = function () {
      self.pdfForm.find("input[name=track]").val(false);
      self.pdfForm.submit();
    };
    self.initSavePdf = function () {
      self.pdfForm.find("input[name=track]").val(true);
//          self.pdfForm.submit();
      var id = $(this).attr("gwId");
      var userId = self.pdfForm.find("input[name=userId]").val();
      var wtTransId = self.pdfForm.find("input[name=wtTransId]").val();
      var attachType = self.pdfForm.find("input[name=attachType]").val();
      var linebreakJson = self.pdfForm.find("input[name=linebreakJson]").val();
//          alert(id);
//          alert(wtTransId);
      var url = window.SERVER_ROOT + "/report/print/" + id;
      self.gwPreviewCt.mask("Saving Groundwater PDF Report...")
      $.ajax({
        url: url
        , data: {userId: userId, wtTransId: wtTransId, attachType: attachType, linebreakJson: linebreakJson, track: true}
        , cache: false
        , success: function (data, status, jqxhr)
        {
          self.gwPreviewCt.unmask();
          alert("PDF file Saved.");
//              opener.location.reload();
        }
        , error: function (xhr, errorType, exception) {
          self.gwPreviewCt.unmask();
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          } else {
            alert("Failed to save PDF file.");
          }
        }
      });
    };
    self.userValidation = function (msg, callback)
    {
      if (msg)
      {
        $("<div>", {
          html: '<p>' + msg + '</p>'
        }).dialog({
          title: 'User Validation'
          , model: true
          , width: 400
          , height: 200
          , buttons: [{
              text: 'Yes'
              , click: function ()
              {
                $(this).dialog("destroy").remove();
                if (callback && typeof (callback) === "function")
                {
                  callback(true);
                }
              }
            }, {
              text: 'No'
              , click: function ()
              {
                $(this).dialog("destroy").remove();
                if (callback && typeof (callback) === "function")
                {
                  callback(false);
                }
              }
            }]
        }).dialog("open");
      }
      else {
        callback();
      }
    };
    self.init();
  };
  $(document).ready(function () {
    window.app = new lineBreak();
  });
</script>