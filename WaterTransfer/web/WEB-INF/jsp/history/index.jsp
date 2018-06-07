<%--
    Document   : index
    Created on : Apr 14, 2015, 10:15:43 AM
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
.history_navi{
    background-color:#fbad23;
}
.dataTable tr {
  cursor:pointer;
  font-size: 12px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
  $.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var min = parseInt( $('#min').val(), 10 );
        var max = parseInt( $('#max').val(), 10 );
        var year = parseFloat( data[2] ) || 0; // use data for the year column

        if ( ( isNaN( min ) && isNaN( max ) ) ||
             ( isNaN( min ) && year <= max ) ||
             ( min <= year   && isNaN( max ) ) ||
             ( min <= year   && year <= max ) )
        {
            return true;
        }
        return false;
    }
  );

  var table =
  $("#proposal-tb")
  .on('click', 'tr', function () {
      var transId = $('td', this).eq(0).text();
      if (transId){
        location = window.SERVER_ROOT + "/history/edit/" + transId;
      }
  })
  .DataTable({
    "ajax":window.SERVER_ROOT + "/history/proposalList"
    ,columnDefs:[{
        targets:[0]
        ,visible:true
    }]
    ,columns:[
      {data:"wtTransId"}
      ,{data:"wtTransNum"}
      ,{data:"transYear"}
      ,{data:"sellers"}
      ,{data:"buyers"}
      ,{data:"proTransQua"}
      ,{data:"actTransQua"}
      ,{data:"dwrProApprDate"}
      ,{data:"transWinStart"}
      ,{data:"transWinEnd"}]
  });

  $('#min, #max').keyup( function() {
        table.draw();
  });
})
</script>
<div style="padding:10px 5px 0;" class="module_type" id="${moduleType}">
    <div class="year_filter">
        <label>From Year: </label><input id="min" name="min" type="text" style="width:100px;">
        <label>To Year: </label><input id="max" name="max" type="text" style="width:100px;">
    </div>

    <table id="proposal-tb" class="display" width="100%" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th>ID</th>
                <th>Transfer Num</th>
                <th>Year</th>
                <th>Seller</th>
                <th>Buyer</th>
                <th>Proposed Transfer Quantity(AF)</th>
                <th>Actual Transfer Quantity(AF)</th>
                <th>DWR Proposal Approved Date</th>
                <th>Start Transfer Window</th>
                <th>End Transfer Window</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>ID</th>
                <th>Transfer Num</th>
                <th>Year</th>
                <th>Seller</th>
                <th>Buyer</th>
                <th>Proposed Transfer Quantity(AF)</th>
                <th>Actual Transfer Quantity(AF)</th>
                <th>DWR Proposal Approved Date</th>
                <th>Start Transfer Window</th>
                <th>End Transfer Window</th>
            </tr>
        </tfoot>
    </table>
</div>
<%@include file="../templates/footer.jsp" %>
