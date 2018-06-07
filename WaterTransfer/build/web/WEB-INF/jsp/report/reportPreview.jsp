<%--
    Document   : reportPreview
    Created on : Sept 22, 2017, 1:46:47 PM
    Author     : ymei
--%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:set var="__Stylesheets">
  ${pageContext.request.contextPath}/resources/css/reset.css
</c:set>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
  ,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
</c:set>
<%@include file="../templates/print/header.jsp" %>
<c:if test="${proposal.wtReservoir == null}">
  <c:set var="rv_empty" value="hidden"/>
</c:if>
<c:if test="${proposal.wtCropIdling == null}">
  <c:set var="ci_empty" value="hidden"/>
</c:if>
<%--<c:if test="${empty(gwPreview)}">
  <c:set var="gw_empty" value="hidden"/>
</c:if>--%>
<c:if test="${proposal.wtGroundwater == null}">
  <c:set var="gw_empty" value="hidden"/>
</c:if>
<div id="report_review_ct">
  <%@include file="print/pagenumber.jsp" %>

  <div class="title">
    ${sellerName}&nbsp;${proposal.transYear} Water Transfer Proposal <c:if test="${!empty(proposal.swpaoContractNum)}">SWPAO # ${proposal.swpaoContractNum}</c:if>
    </div>
    <h1>General Information</h1>
    
    <div class="section">
      <div class="subheader">Seller/Buyer(s) Information</div>
      <div class="sellerCt">
      <c:forEach items="${proposal.wtSellerCollection}" var="seller">
        <span>Seller's Agency: </span><span class="highlight">${seller.agencyFullName}--${seller.agencyType}</span>
        <table>
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Title</th>
              <th>Email</th>
              <th>Phone Number</th>
              <th>Address</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${seller.wtContactCollection}" var="contact">
              <c:if test="${contact.isActive==1}">
                <tr>
                  <td>${contact.firstName}</td>
                  <td>${contact.lastName}</td>
                  <td>${contact.title}</td>
                  <td>${contact.email}</td>
                  <td>${contact.phoneNumber}</td>
                  <td>
                    <c:if test="${not empty contact.address1}">
                      ${contact.address1}<br />${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}
                    </c:if>
                  </td>
                </tr>
              </c:if>
            </c:forEach>
          </tbody>
        </table>
      </c:forEach>
    </div>
    <div class="buyerCt">
      <c:forEach items="${proposal.wtBuyerCollection}" var="buyer">
        <span>Buyer's Agency: </span><span class="highlight">${buyer.wtAgency.agencyFullName}--${buyer.wtAgency.agencyType}--${buyer.sharePercent}%</span>
        <table>
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Title</th>
              <th>Email</th>
              <th>Phone Number</th>
              <th>Address</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${buyer.wtAgency.wtContactCollection}" var="contact">
              <c:if test="${contact.isActive==1}">
                <tr>
                  <td>${contact.firstName}</td>
                  <td>${contact.lastName}</td>
                  <td>${contact.title}</td>
                  <td>${contact.email}</td>
                  <td>${contact.phoneNumber}</td>
                  <td>
                    <c:if test="${not empty contact.address1}">
                      ${contact.address1}<br />${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}
                    </c:if>
                  </td>
                </tr>
              </c:if>
            </c:forEach>
          </tbody>
        </table>
      </c:forEach>
      <span>Buyers Representative: </span>
      <table>
        <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Title</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Address</th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${(proposal.buyersContactCollection.size() eq 1)}">
            <c:forEach items="${proposal.buyersContactCollection}" var="buyersContact">
              <tr>
                <td>${buyersContact.firstName}</td>
                <td>${buyersContact.lastName}</td>
                <td>${buyersContact.title}</td>
                <td>${buyersContact.email}</td>
                <td>${buyersContact.phoneNumber}</td>
                <td>
                  <c:if test="${not empty buyersContact.address1}">
                    ${buyersContact.address1}<br />${buyersContact.cityName},${buyersContact.wtState.shortName}&nbsp;${buyersContact.zipcode}
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
    
  <%@include file="baseInfoPreview.jsp" %>
  <div class='pagebreak'>&#160;</div>

  <%@include file="cropIdlingPreview.jsp" %>
  <div class='pagebreak'>&#160;</div>
  
  <%@include file="groundwaterPreview.jsp" %>
  <div class='pagebreak'>&#160;</div>

  <%@include file="reservoirPreview.jsp" %>
  <div class='pagebreak'>&#160;</div>  
</div>

<!--<div class="groundwaterCt ${gw_empty}">${gwPreview}</div>-->
<%@include file="../templates/print/footer.jsp" %>

<script>
  var reportPreview = function(cfg)
  {
    var self = this;
    self.init=function()
    {
      self.initItems();
      self.initListeners();
      self.initRemoveGWClass();
    };
    self.initItems = function ()
    {
      self.previewCt = $("#" + cfg);
      self.groundwaterCt = self.previewCt.find(".groundwaterCt");
      self.gwReport = self.groundwaterCt.find("#review-gw");
      self.pageBreak = self.previewCt.find(".pagebreak");
      self.pageBreakBtn = self.previewCt.find(".setPageBreak button");
    };
    self.initListeners = function () {

    };
    self.initRemoveGWClass = function () {
      self.gwReport.removeAttr("id");
    };
    self.init();
  };
  $(document).ready(function () {
    window.app = new reportPreview("report_review_ct");
  });
</script>
<style type="text/css">
  h1 {
    margin-bottom:15px;
  }
  div.header {
    font-size: 15px;
  }
  div.section {
    margin-top:20px;
    margin-bottom:15px;
  }
  .section .subheader {
    width:90%;
    border-radius: 2px;
    font-weight: bold;
    margin: 15px 0px;
    padding: 5px;
  }
  #report_review_ct{
    background-color: white;
    box-shadow: 0 0 5px #444;
    margin: 0 auto;
    padding: 20px;
    width: 10.5in;
  }
  #report_review_ct .highlight{
    font-size: 12px;
    font-weight:bold;
    padding-left: 10px;
    padding-right: 10px;
  }
  #report_review_ct .simple{
    font-weight: lighter;
  }
  .checkboxLabel{
    font-weight: bold;
  }
  ul{
    list-style-type: none;
  }
  ul li{
    display: inline-table;
    margin: 0px 10px;
  }
  textarea{
    height: 100px;
    width: 600px;
    resize:none;
  }
  table{
    margin: 5px 0px;
  }
@media print{
  *{font-family: Arial,"Trebuchet MS",Helvetica,sans-serif !important;}
  div.title {
    width: 700px;
    text-align: center;
    font-size:32px;
  }
  h1 {
    color: #0178a3;
    font-size:25px;
  }
  div#report_review_ct{
    border:none;
    background-color:#FFF;
    box-shadow: none;
    padding:0;
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
  table{
    border-collapse: collapse;
    width:700px !important;
  }
  span,table td,table th,ul li{
    font-size: 8pt;
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
  .header {
    color:#000000;
    font-size:18px;
  }
  .section {
    margin-top:20px;
    margin-bottom:15px;
  }
  .pagebreak{
    page-break-after: always;
    border:none;
    float:none;
  }
}
</style>