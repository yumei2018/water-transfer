<%--
    Document   : SearchProposal
    Created on : May 18, 2015, 11:26:16 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/js/DataTables-1.10.5/media/css/jquery.dataTables.min.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/ApplicationLauncher.js
,${pageContext.request.contextPath}/resources/js/DataTables-1.10.5/media/js/jquery.dataTables.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
.dataTable tr {
    cursor:pointer;
}
.search_navi{
    background-color:#fbad23;
}
#proposal-tb_length{
    position: relative;
    left:85%;
    top:20px;
}
#proposal-tb_filter{
    display: none;
}
.sub-title{
    padding-top: 10px;
    padding-bottom: 15px;
}
#search-table{
    padding-left: 100px;
}
.searchRow{
    border:none;
    font-size: 12pt;
    border: 0px solid #A6A8A8;
    padding: 3px;
    color:#333333;
    height: 40px;
    text-align:left;
}

</style>
<script type="text/javascript">
$(document).ready(function(){
//    var data = $("#search-form").serializeArray();
    var url =window.SERVER_ROOT + "/proposal/searchResult";

    $("#search_btn").on('click', function(){

        $("#proposal-tb").attr('class','display')
        .DataTable({
            "ajax":{
                "url": url,
                "data": {"wtTransId": $("#wtTransId").val()
                         ,"transYear": $("#transYear").val()
                         ,"sellerId": $("#sellerId").val()
                         ,"buyerId": $("#buyerId").val()}
            }
            ,"bDestroy": true
            ,columnDefs:[{
                targets:[0]
                ,visible:true
            }]
            ,columns:[
            {data:"wtTransId"}
            ,{data:"transYear"}
            ,{data:"seller"}
            ,{data:"buyers"}
            ,{data:"status"}
            ,{data:"createDate"}]
        });
    })
  $("#proposal-tb")
  .on('click', 'tr', function () {
      var transId = $('td', this).eq(0).text();
      if (transId){
        location = window.SERVER_ROOT + "/proposal/edit/" + transId;
      }
  })
//  .DataTable({
//    "ajax":window.SERVER_ROOT + "/proposal/proposalList?moduleType="+$(".module_type").attr('id')
//    ,columnDefs:[{
//        targets:[0]
//        ,visible:true
//    }]
//    ,columns:[
//      {data:"wtTransId"}
//      ,{data:"transYear"}
//      ,{data:"seller"}
//      ,{data:"buyers"}
//      ,{data:"status"}
//      ,{data:"createDate"}]
//  });
})
</script>

<div class="sub-title">Search By Attributes</div>
<!--<form class="form" id="search-form">-->
<div style="padding:10px 20px 0px;">
<table id="search-table" style="width:600px;">
    <tr class="searchRow">
        <th>Transfer ID: </th>
        <td><input id="wtTransId" name="wtTransId" type="text" style="width:120px;" placeholder="Enter Transfer ID"></td>
    </tr>
    <tr class="searchRow">
        <th>Transfer Year: </th>
        <td><input id="transYear" name="transYear" type="text" style="width:120px;" placeholder="Enter Transfer Year"></td>
    </tr>
    <tr class="searchRow">
        <th>Seller: </th>
        <td>
            <select class="agency_list" id="sellerId">
                <option value="" style="color:#cccccc;">Please select a Seller...</option>
                <c:forEach var="agency" items="${agencyList}">
                    <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
                </c:forEach>
            </select>
        </td>
    </tr>
    <tr class="searchRow">
        <th>Buyer: </th>
        <td>
            <select class="agency_list" id="buyerId">
                <option value="" style="color:#cccccc;">Please select a Buyer...</option>
                <c:forEach var="agency" items="${agencyList}">
                    <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
                </c:forEach>
            </select>
        </td>
    </tr>
</table>
</div>
<!--</form>-->
<div class="button_ct">
    <input class="proposal_btn" type="button" id="search_btn" value="Search" style="width:100px;" onclick="return false;"/>
</div>

<div style="padding:10px 5px 5px;" class="" id="">
<!--    <div class="year_filter">
        <label>From Year: </label><input id="min" name="min" type="text" style="width:100px;">
        <label>To Year: </label><input id="max" name="max" type="text" style="width:100px;">
    </div>-->


  <table id="proposal-tb" class="hidden" width="100%" cellspacing="0" cellpadding="0">
    <thead>
        <tr>
            <th colspan="6" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Proposal Table</th>
        </tr>
    <tr>
      <th>ID</th>
      <th>Year</th>
      <th>Seller</th>
      <th>Buyer</th>
      <th>Status</th>
      <th>Created Date</th>
    </tr>
    </thead>
    <tfoot>
    <tr>
      <th>ID</th>
      <th>Year</th>
      <th>Seller</th>
      <th>Buyer</th>
      <th>Status</th>
      <th>Created Date</th>
    </tr>
    </tfoot>
  </table>
</div>
<%@include file="../templates/footer.jsp" %>
