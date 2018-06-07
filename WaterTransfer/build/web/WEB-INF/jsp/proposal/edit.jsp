<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Stylesheets">
  ${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
  ,${pageContext.request.contextPath}/resources/css/AgencyContact.css
  ,${pageContext.request.contextPath}/resources/css/wtProposal.css
  ,${pageContext.request.contextPath}/resources/css/jquery.loadmask.css
  ,${pageContext.request.contextPath}/resources/js/jquery/jqcombobox/jqcombobox.css
  ,${pageContext.request.contextPath}/resources/css/associatedWells.css
</c:set>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/ui.dropdownchecklist-1.4-min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.standard_fonts_metrics.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.split_text_to_size.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.from_html.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.addimage.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.table.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.cell.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jspdf.plugin.autoprint.js
  ,${pageContext.request.contextPath}/resources/js/jquery/adler32cs.js
  ,${pageContext.request.contextPath}/resources/js/jquery/deflate.js
  ,${pageContext.request.contextPath}/resources/js/jquery/FileSaver.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/html2canvas.js
  ,${pageContext.request.contextPath}/resources/js/jquery.maskedinput.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.loadmask.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.tablesorter.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.simplyCountable.js
  ,${pageContext.request.contextPath}/resources/js/ApplicationLauncher.js
  ,${pageContext.request.contextPath}/resources/js/proposal/AgencyContactForm.js
  ,${pageContext.request.contextPath}/resources/js/proposal/AgencyContactEmail.js
  ,${pageContext.request.contextPath}/resources/js/ErrorPromptsHandler.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jqcombobox/jqcombobox.js
  ,${pageContext.request.contextPath}/resources/js/proposal/AssociatedWells.js
  ,${pageContext.request.contextPath}/resources/js/proposal/AttachmentFileList.js
  ,${pageContext.request.contextPath}/resources/js/proposal/BaseInfo.js
  ,${pageContext.request.contextPath}/resources/js/proposal/CropidlingInfo.js
  ,${pageContext.request.contextPath}/resources/js/proposal/ReservoirInfo.js
  ,${pageContext.request.contextPath}/resources/js/proposal/GroundwaterInfo.js
  ,${pageContext.request.contextPath}/resources/js/proposal/Proposal.js
  ,${pageContext.request.contextPath}/resources/js/proposal/ProposalReview.js
  ,${pageContext.request.contextPath}/resources/js/proposal/ProposalProcess.js
  ,${pageContext.request.contextPath}/resources/js/proposal/TransferReport.js
  ,https://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
  div#content-body{
    box-shadow: 0 0 5px #999;
    box-sizing: border-box;
    margin: 0 auto;
  }
  .ui-state-default{
    font-size: 14px;
  }
  .proposal_navi{
    background-color:#fbad23;
  }
/*  .proposalReports,.proposalReview,#view_changelog {
    position: relative;
    font-size: 15px;
    margin-left:15px;
        margin-top:30px;
    bottom: -15px;
    text-decoration:underline;
    cursor:pointer;
  }*/
  .proposal_btn{
    width: 180px;
  }
  a.proposal_btn{
    width: 180px;
    background-color: #0190c8;
    text-align: center;
    text-decoration: none;
  }
  #typeMenu {
    margin-bottom: 20px;
    background-color: #cfe9f1;
    padding: 10px 10px 15px 10px;
  }
  * > #sideMenu {
    position: fixed;
  }
  #sideMenu {
    position: fixed;
    box-sizing: border-box;   
    font-size: 10pt; 
    color: white;
    top: 20%;
    width: 18em;
/*    width: 280px;*/
  }
  #sideMenu ul {
    display: block;
    list-style-type: none;
    padding: 0px 0px;
    position: relative;
  }
  #sideMenu ul li {
    padding: 0px 20px;
  }
  #sideMenu li {
    background-color: #0190c8;
    border-radius: 3px;    
    margin: 5px 0 5px;
  }
  #sideMenu ul li button{
    display: inline-block;
    margin-top: 10px;
  }
  #sideMenu ul li button:hover{
    background-color:#FBAE24;
    color: black;
  }
  #sideMenu ul li a{
    display: inline-block;
    margin-top: 10px;
  }
  #sideMenu ul li a:hover{
    background-color:#FBAE24;
    color: black;
  }
  textarea.techNote {
    background-color: #ddd;
    display: block;
    min-height: 100%;
    overflow: auto;
    height: auto;
    width: 99%; 
    resize: none;
  }
  .noteBtn {
    /*    width: 40px;*/
    height: 20px;
    float: right;
    cursor: pointer;
    padding-bottom: 20px;
  }
  .commentNote {
    float:right;
    font-size:11px;
    color:red;
  }
</style>
<script type="text/javascript">
//  var newContactHandler = new ApplicationLauncher({
//    javascripts: [window.SERVER_ROOT + "/resources/js/proposal/AgencyContact.js"]
//    , callback: function () {
//      new AgencyContact();
//    }
//  });
  var newContactHandler = new ApplicationLauncher({
    javascripts: [window.SERVER_ROOT + "/resources/js/proposal/Proposal.js"]
    , callback: function () {
      new Proposal();
    }
  });
  $(document).ready(function () {
    window.app = newContactHandler.launch();
    window.reviewer = new ProposalReview("proposal_review_ct");

//    $(window).bind('beforeunload', function () {
//      return 'Are you sure you want to leave? Data you have entered may not be saved.';
//    });
    
  });
</script>
<c:set var='newProposal' value=""/>
<c:set var="hideTab" value="hidden"/>
<c:set var="reportTab" value="hidden"/>
<c:set var="reviewNote" value="hidden"/>
<c:set var="isReviewer" value="hidden"/>
<c:set var="hideField" value="hidden"/>
<c:set var="disableEdit" value="disabled"/>
<c:set var="disableEditWell" value="disabled"/>
<c:if test="${buttonCheck == true}">
  <c:set var='newProposal' value="hidden"/>
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideTab" value=""/>
  <c:set var="hideField" value=""/>
  <c:set var="reportTab" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(proposal.wtStatusFlag.statusName eq 'DRAFT')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(proposal.wtStatusFlag.statusName eq 'INCOMPLETE')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
  <c:set var="reviewNote" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()||sessionScope.USER.isManager()}">
  <c:set var="isReviewer" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isReviewer()||sessionScope.USER.isManager()}">
  <c:set var="hideTab" value=""/>
</c:if>
<c:if test="${proposal.wtStatusFlag.statusName eq 'TPROGRESS'||proposal.wtStatusFlag.statusName eq 'CEXECUTED'}">
  <c:set var="reportTab" value=""/>
</c:if>
<c:if test="${(proposal.wtStatusFlag.statusName eq 'UREVIEW') && sessionScope.USER.isAppAccount()}">
  <c:set var="reviewNote" value=""/>
</c:if>
<c:set var="agency_options" />
<c:forEach var="agency" items="${agencies}">
  <c:set var="agency_options">
    ${agency_options}
    <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
  </c:set>
</c:forEach>
<div id="content-inside">
  <div class="header_tab" id="proposalTitle" editstatus="${disableEdit}" sellername="${seller.agencyFullName}" transyear="${proposal.transYear}" swpaono="${proposal.swpaoContractNum}">
    ${seller.agencyFullName}&nbsp${proposal.transYear} Water Transfer Proposal <c:if test="${!empty(proposal.swpaoContractNum)}">SWPAO # ${proposal.swpaoContractNum}</c:if>
  </div>
  <div id="tabs">
    <ul>
      <li><a href="#seller_tab">Seller</a></li>
      <li><a href="#buyer_tab">Buyer</a></li>
      <li><a href="#baseinfo_tab">General Information</a></li>
      <li id="CropIdling" class="secondary-dynamic-tabs"><a href="#cropidling_tab">Cropland Idling and Crop Shifting</a></li>      
      <li id="GroundWater" class="secondary-dynamic-tabs"><a href="#groundwater_tab">Groundwater Substitution</a></li>
      <li id="Reservoir" class="secondary-dynamic-tabs"><a href="#reservoir_tab">Reservoir Release</a></li>
      <li id="TransReport" class="${reportTab} secondary-dynamic-tabs"><a href="#transreport_tab" >Reporting</a></li>
      <li id="ProposalProcess" class="${hideTab} secondary-dynamic-tabs"><a href="#proposalprocess_tab">Agreement Development</a></li>
      <li id="ProposalStatus" class="${hideTab} secondary-dynamic-tabs"><a href="#statustrack_tab">Status</a></li>
    </ul>

    <div id="seller_tab" class="tab_ct">
      <div class="accordion"></div>
      <c:if test="${!empty(proposal) && !empty(seller)}">
        <div class="load_seller hidden" agencyid="${seller.wtAgencyId}"></div>
      </c:if>
      <input type="button" value="Choose Seller Agency" name="seller" class="add_agency ${isReviewer}"/>
      <select class="agency_list hidden" style="width:480px;" ${disableEdit}>
        <option value="0" style="color:#cccccc;">Please select a Seller...</option>
        ${agency_options}
      </select>
      <div class="addEditAgency hidden" style="padding-left: 230px;font-size: 10pt">
        <!--          <p>If you cannot find the Agency, Click <a class="newAgency" style="cursor:pointer;text-decoration:underline;color:red">here</a> to add a new one.</p>-->
        If seller is not listed, please request <a class="" href='mailto:swpwatertransfer@water.ca.gov' style="cursor:pointer;text-decoration:underline;color:red">SWPAO</a> to add seller.
      </div>
      <input class="hidden" type="text" id="waterRightsSize" name="waterRightsSize" value="${proposal.wtWaterRightsCollection.size()}"/>
      <input class="hidden" type="text" id="statusName" name="statusName" value="${proposal.wtStatusFlag.statusName}"/>

      <div class="forReviewer ${reviewNote}">
        <c:set var="isComplete" value=""/>
        <c:set var="checkVal" value="0"/>
        <c:set var="addComment" value="disabled"/>  
        <c:set var="showAdd" value="hidden"/> 
        <c:set var="bgcolor" value=""/>
        <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('A1').isComplete==1}">
          <c:set var="isComplete" value="checked"/>
          <c:set var="checkVal" value="1"/> 
        </c:if>
        <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('A1').isComplete!=1)}">
          <c:set var="addComment" value=""/>      
          <c:set var="showAdd" value=""/>
          <c:set var="bgcolor" value="background-color: #fff;"/>
        </c:if>
        <div class="checkBoxCt">
          <input type="checkbox"
                 sectionKey="A1"
                 transId="${proposal.wtTransId}"
                 onclick="return false;"
                 ${isComplete}
                 value="${checkVal}"/>
          <label>Above section is completed if checked</label>
        </div>
        <fieldset class="">
          <c:set var="techNote" value=""/>
          <c:if test="${!empty(reviewNotes)}">
            <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('A1').note}"/>
          </c:if>
          <textarea class="techNote" 
                    placeholder="Technical Comments"  
                    style="${bgcolor}"
                    sectionKey="A1"
                    transId="${proposal.wtTransId}"
                    ${addComment}
                    onkeyup="window.reviewer.auto_grow(this)"></textarea> 
          <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
          <input class="noteBtn ${showAdd}"
                 type="button"
                 sectionKey="A1"
                 transId="${proposal.wtTransId}"
                 value="Save Comment"
                 onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
          <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--          <div class="historyCt hidden">
            <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('A1')}" var="tNote" >
              <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
              <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
            </c:forEach>
          </div>-->

          <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('A1')}" var="tNote" varStatus="iterator">
            <c:if test="${iterator.first}">
              <div class="lastNoteCt">
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>
              </div>
            </c:if>
            <c:if test="${!iterator.first}">
              <div class="historyCt hidden">
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>
              </div>
            </c:if>
          </c:forEach>
        </fieldset>
      </div>    
    </div>

    <div id="buyer_tab" class="tab_ct">
      <div class="accordion">
        Transfer water allocating may be revised at any time
      </div>
      <c:if test="${!empty(buyers)}">
        <c:forEach var="buyer" items="${buyers}">
          <c:set var="buyer_agency" value="${buyer.wtAgency}" />
          <div class="load_buyer hidden" agencyid="${buyer_agency.wtAgencyId}" percent="${buyer.sharePercent}"></div>
        </c:forEach>
      </c:if>
      <input type="button" value="Add Buyer Agency" name="buyer" class="${draft} add_agency ${hideField}" />
      <select class="agency_list hidden" style="width:500px;">
        <option value="0" style="color:#cccccc;">Please select a Buyer...</option>
        ${agency_options}
      </select>
      <div class="addEditAgency hidden" style="padding-left: 220px;font-size: 10pt; margin-top: 2px;">
        <!--         If you cannot find the Agency, Click <a class="newAgency" style="cursor:pointer;text-decoration:underline;color:red">here</a> to add a new one.-->
        If buyer is not listed, please request <a class="" href='mailto:swpwatertransfer@water.ca.gov' style="cursor:pointer;text-decoration:underline;color:red">SWPAO</a> to add buyer.
      </div>

      <hr style="margin-top:30px;">
      <h2 class="tab_header isExpand" style="margin-bottom:4px;">Buyers Representative</h2>
      <div class="contact_panel"><%@include file="../templates/buyersContactTable.jsp" %></div>

      <div class="forReviewer ${reviewNote}">
        <c:set var="isComplete" value=""/>
        <c:set var="checkVal" value="0"/>
        <c:set var="addComment" value="disabled"/>  
        <c:set var="showAdd" value="hidden"/> 
        <c:set var="bgcolor" value=""/>
        <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('A2').isComplete==1}">
          <c:set var="isComplete" value="checked"/>
          <c:set var="checkVal" value="1"/> 
        </c:if>
        <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('A2').isComplete!=1)}">
          <c:set var="addComment" value=""/>      
          <c:set var="showAdd" value=""/>
          <c:set var="bgcolor" value="background-color: #fff;"/>
        </c:if>
        <div class="checkBoxCt">
          <input type="checkbox"
                 sectionKey="A2"
                 transId="${proposal.wtTransId}"
                 onclick="return false;"
                 ${isComplete}
                 value="${checkVal}"/>
          <label>Above section is completed if checked</label>
        </div>
        <fieldset class="">
          <c:set var="techNote" value=""/>
          <c:if test="${!empty(reviewNotes)}">
            <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('A2').note}"/>
          </c:if>
          <textarea class="techNote" 
                    placeholder="Technical Comments"  
                    style="${bgcolor}"
                    sectionKey="A2"
                    transId="${proposal.wtTransId}"
                    ${addComment}
                    onkeyup="window.reviewer.auto_grow(this)"></textarea> 
          <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
          <input class="noteBtn ${showAdd}"
                 type="button"
                 sectionKey="A2"
                 transId="${proposal.wtTransId}"
                 value="Save Comment"
                 onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
          <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--          <div class="historyCt hidden">
            <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('A2')}" var="tNote" >
              <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
              <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
            </c:forEach>
          </div>-->

          <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('A2')}" var="tNote" varStatus="iterator">
            <c:if test="${iterator.first}">
              <div class="lastNoteCt">
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>
              </div>
            </c:if>
            <c:if test="${!iterator.first}">
              <div class="historyCt hidden">
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>
              </div>
            </c:if>
          </c:forEach>
        </fieldset>
      </div>
    </div>

    <form class="form" id="create-form" method="POST">
      <%@include file="BaseInfo.jsp" %>
      <%@include file="CropidlingInfo.jsp" %>
      <%@include file="GroundwaterInfo.jsp" %>
      <%@include file="ReservoirInfo.jsp" %>
    </form>

    <%--<%@include file="ChangeLog.jsp" %>--%>
    <%@include file="TransferReport.jsp" %>
    <%@include file="ProposalProcess.jsp" %>
    <%@include file="StatusTrack.jsp" %>

  </div>

  <!-- Popup Forms -->
  <div id="uploadFile_ct" typeid=""></div>
  <div class="uploadPercent_ct"></div >
  <form class="form" id="edit-file-form" method="post"></form>
  <%@include file="../templates/statusTrackForm.jsp" %>
  <!-- End Popup Forms -->

<!--  <div class="button_ct">
    <c:if test="${proposal.wtStatusFlag.statusName eq 'DRAFT'}">
      <c:set var='draftProposal' value="hidden"/>
    </c:if>    
    <button class="proposal_btn ${newProposal} proposalReportBtn" style="width:175px;float:left;" wtTransId="${proposal.wtTransId}" userId="${sessionScope.USER.userId}">Generate PDF Report</button>
    <a class="proposalReports ${newProposal}" wtTransId="${proposal.wtTransId}" reportType="PR">View Saved PDF Reports</a>
    <a class="${newProposal}" id="view_changelog">View Submit Log</a>
    <c:if test="${(sessionScope.USER.isReviewer()||sessionScope.USER.isManager())}">
      <a class ="proposalReview" target="_blank" href="${pageContext.request.contextPath}/review/proposalReview/${proposal.wtTransId}">Proposal Review</a>
    </c:if>
    <c:if test="${sessionScope.USER.isAppAccount()&&proposal.wtStatusFlag.statusName eq 'DRAFT'}">
      <button class="proposal_btn" id="submitProposal">Submit</button>
    </c:if>
    <c:if test="${sessionScope.USER.isAppAccount()
                  && proposal.wtStatusFlag.statusName ne 'DRAFT'
                  &&(proposal.wtTransId > 0)}">

      <button class="proposal_btn" id="changeLog" ${newProposal}>Resubmit</button>
    </c:if>
    <c:if test="${buttonCheck == true}">
      <button class="proposal_btn" id="nextTab">Next Step</button>
    </c:if>
    <button class="proposal_btn" id="saveProposal" ${appUser}>Save</button>   

    <c:if test="${(sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager())
                  && (proposal.wtStatusFlag.statusName eq 'REVIEW')}">
    </c:if>
    <c:if test="${(sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager())
                  && (proposal.wtStatusFlag.statusName eq 'APPROVED')}">
    </c:if>
  </div>-->
  <form action="${pageContext.request.contextPath}/report/printReport/${proposal.wtTransId}" target="_blank" method="post" id="proPDFform">
    <input type="hidden" name="userId" value="${sessionScope.USER.userId}"/>
    <input type="hidden" name="track" value=""/>
    <input type="hidden" name="attachType" value="PR"/>
  </form>
</div>

<div id="sideMenu" class="button_ct" style="right: 90px;">
  <c:set var='showSave' value="hidden" />
  <c:set var='reviewUser' value="hidden" />
  <c:set var='showSubmit' value="hidden" />
  <c:set var='showResubmit' value="hidden" /> 
  <c:set var='createProposal' value="hidden" />
  <c:set var='editProposal' value="" />
  <c:if test="${sessionScope.USER.isAppAccount()
                && (proposal.wtStatusFlag.statusName eq 'DRAFT' || proposal.wtStatusFlag.statusName eq 'INCOMPLETE')}">
    <c:set var='showSave' value="" />
  </c:if>
  <c:if test="${(sessionScope.USER.isReviewer()||sessionScope.USER.isManager())}">
    <c:set var='reviewUser' value="" />
  </c:if>
  <c:if test="${sessionScope.USER.isAppAccount()&&proposal.wtStatusFlag.statusName eq 'DRAFT'}">
    <c:set var='showSubmit' value=""/>
  </c:if>
  <c:if test="${buttonCheck == true}">
    <c:set var='createProposal' value="" />
    <c:set var='editProposal' value="hidden" />
  </c:if>
  <c:if test="${sessionScope.USER.isUSBR()}">
    <c:set var='editProposal' value="hidden" />
  </c:if>
  <c:if test="${sessionScope.USER.isAppAccount()
                && proposal.wtStatusFlag.statusName eq 'INCOMPLETE'
                && proposal.wtTransId > 0}">
    <c:set var='showResubmit' value="" />    
  </c:if>
  <ul>
    <li class="${showSave}">
      <button class="proposal_btn" id="saveProposal" >Save</button>
    </li>
    <li class="${createProposal}">
      <button class="proposal_btn" id="nextTab">Next Step</button>
    </li>
    <li class="${showSubmit}">
      <button class="proposal_btn" id="submitProposal">Submit</button>
    </li>
    <li class="${showResubmit}">
      <button class="proposal_btn" id="changeLog">Resubmit</button>
    </li>
    <li class="${editProposal}">
      <button class="proposal_btn" id="proposalReportBtn" wtTransId="${proposal.wtTransId}" userId="${sessionScope.USER.userId}">Generate PDF Report</button>
    </li>
    <li class="${editProposal}">
      <button class="proposal_btn" id="proposalReports" wtTransId="${proposal.wtTransId}" reportType="PR">View Saved PDF Reports</button>
    </li>
    <li class="${editProposal}">
      <button class="proposal_btn" id="view_changelog">View Submit Log</button>
    </li>
<!--    <li class="${reviewUser}">
      <button class="proposal_btn" formtarget="_blank" onClick="location.href='${pageContext.request.contextPath}/review/proposalReview/${proposal.wtTransId}'">Proposal Review</button>
    </li>-->
    <li class="${reviewUser}">
      <a class="proposal_btn" 
         href="${pageContext.request.contextPath}/review/proposalReview/${proposal.wtTransId}" 
         onclick="$(window).unbind('beforeunload');window.reviewer.sideMenuResize();">Proposal Review</a>
    </li>
  </ul>      
</div>
        
<%@include file="../templates/footer.jsp" %>