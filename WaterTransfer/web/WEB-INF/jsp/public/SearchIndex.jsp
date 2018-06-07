<%-- 
    Document   : SearchIndex
    Created on : Jun 20, 2017, 1:47:26 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/js/DataTables-1.10.5/media/css/jquery.dataTables.min.css
,${pageContext.request.contextPath}/resources/css/jquery.loadmask.css
,${pageContext.request.contextPath}/resources/css/MyDataTable.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/ApplicationLauncher.js
,${pageContext.request.contextPath}/resources/js/DataTables-1.10.5/media/js/jquery.dataTables.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery.loadmask.min.js
,${pageContext.request.contextPath}/resources/js/DataTables-1.10.5/MyDataTable.js
,${pageContext.request.contextPath}/resources/js/proposal/PublicSearch.js
</c:set>
<%@include file="../templates/header.jsp" %>
<div class="sub-title">Water Transfer Records Search</div>

<div id="public-search-ct" style="padding:10px 20px 0px;">
  <table id="public-search-table" style="width:700px;">
    <thead>
      <tr>
        <th colspan='2'>Enter query criteria below and click "Search"</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>Transfer Year:</td>
        <td><input class="yearField" id="transYear" name="transYear" type="text" maxlength="4" style="width:120px;" placeholder="Enter Transfer Year"></td>
      </tr>
<!--      <tr>
        <td>SWPAO/Contract #:</td>
        <td><input id="swpaoContractNum" name="swpaoContractNum" type="text" maxlength="6" style="width:160px;" placeholder="Enter SWPAO Contract #"></td>
      </tr>-->
      <tr>
        <td>Duration of Water Transfer:</td>
        <td>
          <input class="non_border" 
                 style="width:20px;"
                 type="radio"
                 name="isShortLong"
                 value="0"/><label>Temporary (One Year)</label>
          <input class="non_border" 
                 style="width:20px;"
                 type="radio"
                 name="isShortLong"
                 value="1"/><label>Long-Term</label>
        </td>
      </tr>
      <tr>
        <td>Type of Transfer:</td>
        <td>
          <input type="checkbox" 
                 class="type-check"
                 id="typeCropIdling"
                 name="typeCropIdling"/><label> Cropland Idling/Crop Shifting</label><br>
          <input type="checkbox" 
                 class="type-check"
                 id="typeGroundwater"
                 name="typeGroundwater"/><label> Groundwater Substitution</label><br>
          <input type="checkbox" 
                 class="type-check"
                 id="typeReservoir"
                 name="typeReservoir"/><label> Reservoir Release</label><br>
          
        </td>
      </tr>
      <tr>
        <td>Water Right Type:</td>
        <td>
          <input type="checkbox" 
                 class="type-check"
                 id="Statement"
                 name="Statement"/><label> Pre-1914</label><br>
          <input type="checkbox" 
                 class="type-check"
                 id="Application"
                 name="Application"/><label> Post-1914</label><br>
<!--      <input type="checkbox" 
                 class="type-check"
                 id="Decree"
                 name="Decree"/><label> Adjudicated</label><br>
          <input type="checkbox" 
                 class="type-check"
                 id="Contract"
                 name="Contract"/><label> Contract</label><br>-->
        </td>
      </tr>
      <tr>
        <td>Seller:</td>
        <td>
          <select class="agency_list" id="sellerAgency" name="sellerAgency" style="width:480px;" multiple>
          <c:forEach var="agency" items="${agencyList}">
            <option value="${agency.agencyCode}">${agency.agencyFullName}</option>
          </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td>Buyer:</td>
        <td>
          <select class="agency_list" name="buyerAgency" style="width:480px;" multiple>
          <c:forEach var="agency" items="${agencyList}">
            <option value="${agency.agencyCode}">${agency.agencyFullName}</option>
          </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td>County of Seller:</td>
        <td>
          <select class="county_list" name="sellerCounty" style="width:480px;" multiple>
          <c:forEach var="county" items="${countyList}">
            <option value="${county.name}">${county.name}</option>
          </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <th colspan='2'>
          <input class="proposal_btn" 
                 type="button" 
                 id="search_btn" 
                 value="Search" 
                 style="width:100px;" 
                 onclick="window.publicSearch.initSearch(this);"/>
          <input class="proposal_btn" 
                 type="button" 
                 id="resetSearch_btn" 
                 value="Reset Search" 
                 style="width:100px;" 
                 onclick="window.publicSearch.resetSearch(this);"/>
        </th>
      </tr>
    </tbody>
  </table>
</div>

<div style="padding:10px 5px 30px;" id="proposal-list-ct" class="hidden">
<!--  <div class="entries_filter">
    <label>Show</label>
    <select class="entries">
      <option value="10">10</option>
      <option value="15">15</option>
      <option value="25">25</option>
    </select>
    <label>entries</label>
  </div>-->
  <table id="proposal-tb" class="display" width="100%" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
        <th colspan="10" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Water Transfer Records Search Result</th>
      </tr>
      <tr>
        <th>Trans ID</th>
        <th>Year</th>
        <th>SWPAO/Contract #</th>
        <th>Duration</th>
        <th>Seller</th>
        <th>Buyer</th>
        <th>Transfer Type</th>
        <th>Status</th>
        <th>Display</th>
      </tr>
    </thead>
  </table>

  <div style="width: 100%;">    
    <a class="linkLine" id="detailCSV" onclick="window.publicSearch.initPrintCSV(this);return false;">Export CSV with details</a>
    <a class="linkLine" id="simpleCSV" onclick="window.publicSearch.initPrintCSV(this);return false;">Export CSV </a>
    <a class="linkLine" id="agencyLink" onclick="window.publicSearch.initAgencyList(this);return false;">Agency List</a>
  </div>

  <div id="agency_list_ct" class="hidden">
    <table id="agency-list-table">
      <thead>
        <th>Agency Code</th>
        <th>Agency Full Name</th>
      </thead>      
      <tbody>
        <c:forEach var="agency" items="${agencyList}">
          <tr>
            <td>${agency.agencyCode}</td>
            <td>${agency.agencyFullName}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<%@include file="../templates/footer.jsp" %>

<style type="text/css">
  .sub-title{
    padding-top: 10px;
    padding-bottom: 15px;
    font-size: 20px; 
  }
  #public-search-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: left;
  }
  #public-search-table td{
    text-align: left;
    vertical-align: middle;
    font-size: 15px;
    border:1px solid #cccccc;
    padding: 3px;
    background: #F9F9F9;
  }
/*  div.dataTables_length { display: none !important; }*/
  .dataTables_wrapper .dataTables_length {
    float: right;
  }
  #proposal-tb_length {
    position: absolute;
    left: 80%;
    top: 10px;
  }
  #transfer-detail-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #transfer-detail-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #transfer-detail-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }  
  .transfer-detail-header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .linkLine {
    float:right;
    cursor: pointer;
    color: #204d74;
    text-decoration: underline;
    font-size: 12px;
    padding-top:10px;
    padding-right: 15px;
  }
  .transfer-detail-table{
    width:100%;
    text-align: center;
    font-size: 15px;
/*    margin-bottom: 10px;*/
  }
  .transfer-detail-table th{
    border: 1px solid #aaaaaa;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  .transfer-detail-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }  
  .transfer-detail-header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  #agency-list-table{
    border-collapse: collapse;
    font-size: 10px; 
    width: 400px;
  }
  #agency-list-table th{
    border: 1px solid #999999;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #agency-list-table tr td{
    border:1px solid #999999;
    padding: 3px;
    background: #F9F9F9;
  }
</style>
<script type="text/javascript">
  $(document).ready(function () {
    window.publicSearch = new PublicSearch();
  });
</script>