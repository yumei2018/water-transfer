<%--
    Document   : proposalReview
    Created on : Dec 1, 2016, 9:51:19 AM
    Author     : ymei
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="__Stylesheets">
  ${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
  ,${pageContext.request.contextPath}/resources/css/reset.css
  ,${pageContext.request.contextPath}/resources/css/jquery.loadmask.css
</c:set>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery.loadmask.min.js
  ,${pageContext.request.contextPath}/resources/js/proposal/ProposalReview.js
</c:set>
<%@include file="../templates/header.jsp" %>
<c:set var="ci_empty" value=""/>
<c:set var="rv_empty" value=""/>
<c:set var="gw_empty" value=""/>
<c:set var="well_empty" value=""/>
<c:if test="${proposal.wtCropIdling == null}">
  <c:set var="ci_empty" value="hidden"/>
</c:if>
<c:if test="${proposal.wtCropIdling != null}">
  <c:set var="cropIdling" value="${proposal.wtCropIdling}"/>
</c:if>
<c:if test="${proposal.wtReservoir == null}">
  <c:set var="rv_empty" value="hidden"/>
</c:if>
<c:if test="${proposal.wtReservoir != null}">
  <c:set var="reservoir" value="${proposal.wtReservoir}"/>
</c:if>
<c:if test="${proposal.wtGroundwater == null}">
  <c:set var="gw_empty" value="hidden"/>
  <c:set var="well_empty" value="hidden"/>
</c:if>
<c:if test="${proposal.wtGroundwater != null}">
  <c:set var="groundwater" value="${proposal.wtGroundwater}"/>
  <c:if test="${empty(groundwater.wtWellCollection)}">
    <c:set var="well_empty" value="hidden"/>
  </c:if>
</c:if>

<div id="proposal_review_ct">
  <%@include file="../templates/print/pagenumber.jsp" %>

  <c:forEach items="${proposal.wtSellerCollection}" var="seller">
    <c:set var="sellerName" value="${seller.agencyFullName}"/>
  </c:forEach>
  <div class="title">
    ${sellerName}&nbsp${proposal.transYear} Water Transfer Proposal <c:if test="${!empty(proposal.swpaoContractNum)}">SWPAO # ${proposal.swpaoContractNum}</c:if>
  </div>
  <c:set var="comment_img" value="${pageContext.request.contextPath}/resources/images/icons/report_icon.png"/>
  
  <div class="section">
    <a class="anchor" name="section-A1"></a>
    <div class="header" id="comment-A1">
      A1. Seller Information
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="A1"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>
    <div class="sellerCt">
      <c:forEach items="${proposal.wtSellerCollection}" var="seller">
        <span style="font-size:18px;color:#265a88">Seller's Agency: </span>${seller.agencyFullName}--${seller.agencyType}<br />
        <c:forEach items="${seller.wtContactCollection}" var="contact">
          <c:if test="${contact != null && contact.isActive==1}">
            <span>${contact.firstName}&nbsp;${contact.lastName}, ${contact.title}</span><br />
            <c:if test="${not empty contact.address1}">
              <span>${contact.address1}</span><br />
              <span>${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}</span><br />
            </c:if>
            <span>${contact.phoneNumber}</span><br />
            <span>${contact.email}</span><br />
          </c:if>
        </c:forEach>
      </c:forEach>
    </div>

    <div class="forReviewer">
      <c:set var="isComplete" value=""/>
      <c:set var="checkVal" value="0"/>
      <c:set var="techNote" value=""/>
      <c:set var="note" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('A1')}"/>
      </c:if>
      <c:if test="${techNote!='' && techNote.isComplete==1}">
        <c:set var="isComplete" value="checked"/>
        <c:set var="checkVal" value="1"/>
      </c:if>
      <c:if test="${techNote!='' && techNote.note != ''}">
        <c:set var="note" value="${techNote.note}"/>
      </c:if>
      <div class="checkBoxCt">
        <input type="checkbox"
               sectionKey="A1"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="A1"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="A1"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
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
               
  <div class="section">
    <a class="anchor" name="section-A2"></a>
    <div class="header" id="comment-A2">
      A2. Buyer(s) Information
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="A2"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>

    <div class="buyerCt">
      <c:forEach items="${proposal.wtBuyerCollection}" var="buyer">
        <span style="font-size:18px;color:#265a88">Buyer's Agency: </span>${buyer.wtAgency.agencyFullName}--${buyer.wtAgency.agencyType}--${buyer.sharePercent}%<br />
        <c:forEach items="${buyer.wtAgency.wtContactCollection}" var="contact">
          <c:if test="${contact != null && contact.isActive==1}">            
              <span>${contact.firstName}&nbsp;${contact.lastName}, ${contact.title}</span><br />
            <c:if test="${not empty contact.address1}">
              <span>${contact.address1}</span><br />
              <span>${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}</span><br />
            </c:if>
            <span>${contact.phoneNumber}</span><br />
            <span>${contact.email}</span><br />
          </c:if><br />
        </c:forEach>
      </c:forEach>
    </div>
    
    <div class="buyerCt">
      <span style="font-size:18px;color:#265a88">Buyers Representative: </span><br />
      <c:if test="${(proposal.buyersContactCollection.size() eq 1)}">
        <c:forEach items="${proposal.buyersContactCollection}" var="contact">
          <c:if test="${contact != null && contact.isActive==1}">
            <span>${contact.firstName}&nbsp;${contact.lastName}, ${contact.title}</span><br />
            <c:if test="${not empty contact.address1}">
              <span>${contact.address1}</span><br />
              <span>${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}</span><br />
            </c:if>
            <span>${contact.phoneNumber}</span><br />
            <span>${contact.email}</span><br />
          </c:if>
        </c:forEach>
      </c:if>
    </div>
    <div class="forReviewer">
      <c:set var="isComplete" value=""/>
      <c:set var="checkVal" value="0"/>
      <c:set var="techNote" value=""/>
      <c:set var="note" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('A2')}"/>
      </c:if>
      <c:if test="${techNote!='' && techNote.isComplete==1}">
        <c:set var="isComplete" value="checked"/>
        <c:set var="checkVal" value="1"/>
      </c:if>
      <c:if test="${techNote!='' && techNote.note != ''}">
        <c:set var="note" value="${techNote.note}"/>
      </c:if>
      <div class="checkBoxCt">
        <input type="checkbox"
               sectionKey="A2"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="A2"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="A2"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
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

  <%@include file="baseinfoReview.jsp" %>
  <%@include file="cropidlingReview.jsp" %>
  <%@include file="reservoirReview.jsp" %>
  <%@include file="groundwaterReview.jsp" %>
</div>

<c:set var="checked_img" value="${pageContext.request.contextPath}/resources/images/icons/checkbox.png"/>
<div id="right-nav" class="no_print" style="right: 40px;">
  <ul>
    <li>
      <a href="#section-A1">
        <span>A1</span> Seller Information
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="A1">
      </a>
    </li>
    <li>
      <a href="#section-A2">
        <span>A2</span> Buyer(s) Information
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="A2">
      </a>
    </li>
    <li>
      <a href="#section-B">
        <span>B</span> General Information
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="B">
      </a>
    </li>
    <li>
      <a href="#section-C">
        <span>C</span> Water Rights
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="C">
      </a>
    </li>
    <li>
      <a href="#section-D">
        <span>D</span> General Info Attachments
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="D">
      </a>
    </li>
    <li class="${ci_empty}">
      <a href="#section-E">
        <span>E</span> Crop Idling and Crop Shifting
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="E">
      </a>
    </li>
    <li class="${rv_empty}">
      <a href="#section-F">
        <span>F</span> Reservoir Release
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="F">
      </a>
    </li>
    <li class="${gw_empty}">
      <a href="#section-G">
        <span>G</span> Groundwater Substitution
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="G">
      </a>
    </li>
    <li class="${well_empty}">
      <a href="#section-H">
        <span>H</span> Associated Wells
        <img class="checkboximg hidden" src="${checked_img}" sectionKey="H">
      </a>
    </li>
    <li></li>
  </ul>
</div>

<%@include file="../templates/footer.jsp" %>

<script type="text/javascript">
  $(document).ready(function () {
    window.reviewer = new ProposalReview("proposal_review_ct");    
  });
</script>
<style type="text/css">
  *{font-family: Arial,"Trebuchet MS",Helvetica,sans-serif !important;}
  div#proposal_review_ct{
    width:10.5in;
    margin:0 auto;
    padding: 20px;
    box-shadow: 0 0 5px #444;
    /*position:relative;*/
    background-color: white;
  }
  #proposal_review_ct .userInputTd{
/*    font-weight:bold;*/
  }
  #proposal_review_ct .boldfield{
    font-weight:bold;
  }
  #proposal_review_ct .highlight{
    font-size: 15px;
    background-color: #b2e6ff;
    box-shadow: 2px 2px  #888888;
    font-weight:bold;
    padding-left: 10px;
    padding-right: 10px;
  }
  #proposal_review_ct .simple{
    font-weight: lighter;
  }
  .checkboxLabel{
    font-weight: bold;
  }
  .review_button{
    position: relative;
    left: 40%;
    background-color: #005b8a;
    color:#fff;
    font-size:12px !important;
    border:1px solid #797979;
    border-radius: 2px;
    margin-top: 20px;
/*    margin-left: 350px;*/
    height:30px;
  }
  .review_button:hover{
    cursor:pointer;
    color:#333333;
    background-color:#FBAE24;
  }
  div.title {
    width: 900px;
    text-align: center;
    font-size:20px;
    font-weight:bold;
    color: #005b8a;
  }
  div.buyerCt {
    margin-top: 15px;
  }
  div.buyerCt table {
    margin-bottom: 15px;
  }
  h1 {
    color: #0178a3;
    font-size:25px;
  }
  div.fieldRow {
    margin-top: 10px;
    margin-bottom: 10px;
  }
  span {
    font-size: 10pt;
  }
  table{
    border-collapse: collapse;
    width:90%;
    /*    width:700px !important;*/
  }
  table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    width: 60px;
    text-align: center;
    background: none repeat scroll 0 0 #e3e3e3;
    font-size: 15px;
  }
  table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
    font-size: 15px;
  }
  table td,table th,ul li{
    /*    font-size: 10pt;*/
  }
  textarea{
    border:1px solid #D6D6D6;
  }
  ul{
    margin:0px;
    padding:0px;
  }
  .hidden{
    display:none;
  }
  /*  .section .header {
      color:#000000;
      font-size:20px;
      margin-top:20px;
      margin-bottom:15px;
    }*/
  .section .header {
    /*    background-color: #0190c8;*/
    background-color: red;
    width:90%;
    border-radius: 2px;
    font-weight: bold;
    color: #fff;
    margin: 15px 0px;
    padding: 5px;
  }
  .section .subheader {
    width:90%;
    border-radius: 2px;
    font-weight: bold;
    margin: 15px 0px;
    padding: 5px;
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
  .pagebreak{
    page-break-after: always;
    border:none;
    float:none;
  }
  div.forReviewer {
    width:90%;
    margin-top:20px;
    margin-bottom:20px;
    background: #ffe2b1 none repeat scroll 0 0;
  }
  div.checkBoxCt {
    display: inline;
  }
  div.forReviewer .checkBoxCt label {
    color: red;
    font-weight: bold;
  }
  div.forReviewer .checkBoxCt input[type="checkbox"]:checked ~ label {
    color: green;
    font-weight: normal;
  }
  textarea.techNote {
    color: #333333;
/*    min-height: 30px;
    max-height: 200px;*/
    height: auto;
    overflow: hidden;
    padding: 3px;
    resize: none;
    border: 1px solid #999;
    width: 99%;
  }
  #right-nav {
    box-sizing: border-box;
    position: fixed;
    font-size: 9pt;
    top: 100px;
    width: 270px;
  }
  #right-nav ul {
    display: block;
    list-style-type: none;
  }
  #right-nav ul li {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    border-color: #0190c8 #0190c8 -moz-use-text-color;
    border-image: none;
    border-style: solid solid none;
    border-width: 1px 1px medium;
    margin: 0;
    padding-right: 0px;
  }
  #right-nav ul li:last-of-type {
    border-bottom:1px solid #0190c8;
  }
  #right-nav li {
    background-color: white;
    border: 2px solid #0190c8;
    margin: 0 0 5px;
  }
  #right-nav ul li a, #right-nav ul li div {
    cursor: pointer;
    padding: 0;
  }
  #right-nav ul li a, #right-nav ul li div {
    color: #333;
    display: inline-block;
    padding: 5px;
    text-decoration: none;
  }
  #right-nav ul li a, #reviewerCt ul li div {
    width: 92%;
  }
  #right-nav ul li a span, #right-nav ul li div span {
    padding: 1px 0 0 5px;
    background-color: #0190c8;
    box-sizing: border-box;
    width: 25px;
    height: 20px;
    color: #f9f9f9;
    border-radius: 5px;
    margin: 5px 1px 5px 5px;
  }
  #right-nav ul li span {
    display: inline-block;
    padding-right: 3px;
  }
  #right-nav ul li:hover{
    background-color:#FBAE24;
  }
  #right-nav img.checkboximg {
    float: right;
    padding-top: 4px;
  }
  img.commentimg {
    width: 20px;
    height: 20px;
    cursor: pointer;
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
  .asterisk {
    color: red;
    font-weight: bold;
    font-size: 20px;
  }
  
  @media print{
    #right-nav {
      display: none;
    }
  }
</style>
