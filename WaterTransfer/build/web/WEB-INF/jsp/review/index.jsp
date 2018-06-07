<%--
    Document   : index
    Created on : May 8, 2015, 9:41:19 AM
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
  font-size: 12px;
}
.asterisk {
  color: red;
  font-weight: bold;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
  $("#proposal-tb")
  .on('click', 'tr', function () {
      var transId = $('td', this).eq(0).text();
      if (transId){
        location = window.SERVER_ROOT + "/proposal/edit/" + transId;
      }
  })
  .dataTable({
    "ajax":window.SERVER_ROOT + "/review/proposalList"
    ,columnDefs:[{
        targets:[0]
        ,visible:true
    }]
    ,columns:[
      {data:"wtTransId"}
      ,{data:"sellers"}
      ,{data:"buyers"}
      ,{data:"proTransQua"}
      ,{data:"proReceivedDate"}
      ,{data:"dwrProApprDate"}
      ,{data:"approvals"}
      ,{data:"swrcbAppProcess"}
      ,{data:"swrcbApp"}
      ,{data:"waterAvlTiming"}
      ,{data:"deltaTransferInd"}
      ,{data:"reqExpWin"}]

  });

//  $("#proposal1-tb")
//    .dataTable({
//    "footerCallback": function( tfoot, data, start, end, display ) {
//        var api = this.api();
//        $( api.column( 4 ).footer() ).html(
//            api.column( 4 ).data().reduce( function ( a, b ) {
//                return a + b;
//            })
//        );
//    }
//  });
})
</script>
<div style="padding:10px 5px 0;" class="module_type" id="${moduleType}">
    <div class="year_filter"></div>
    <table id="proposal-tb" class="display" width="100%" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th colspan="20" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Proposal Table</th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Seller</th>
                <th>Buyer</th>
                <th>Maximum Quantity(AF)</th>
                <th>Proposal Received</th>
                <th>Agreement Executed/ Proposal Approved</th>
                <th>Agency Approval Required</th>
                <th>Process for SWRCB</th>
                <th>SWRCB Approval</th>
                <th>Timing of Water Availability</th>
                <th>Through Delta Transfer</th>
                <th>Requested Export Window</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>ID</th>
                <th>Seller</th>
                <th>Buyer</th>
                <th>Maximum Quantity(AF)</th>
                <th>Proposal Received</th>
                <th>Agreement Executed/ Proposal Approved</th>
                <th>Agency Approval Required</th>
                <th>Process for SWRCB</th>
                <th>SWRCB Approval</th>
                <th>Timing of Water Availability</th>
                <th>Through Delta Transfer</th>
                <th>Requested Export Window</th>
            </tr>
        </tfoot>
    </table>
</div>
<%@include file="../templates/footer.jsp" %>
