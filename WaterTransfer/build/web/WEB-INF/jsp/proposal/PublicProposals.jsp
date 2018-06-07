<%-- 
    Document   : PublicProposals
    Created on : Feb 27, 2017, 10:22:26 AM
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
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
  
</style>
<script type="text/javascript">
var ProposalTable = function(){
    var self = this;
    self.init = function(){
      self.initItem();
      self.initListeners();
      self.initDataTable();
      self.searchYearRange();
    };
    self.initItem = function(){
      self.proposalCt = $(".proposal_ct");
      self.proposalTb = $("#proposal-tb");
      self.yearSearch = $(".year_filter");
      self.yearMin = self.yearSearch.find("input[name=min]");
      self.yearMax = self.yearSearch.find("input[name=max]");
      self.entries = self.proposalCt.find(".entries");
    };
    self.initListeners = function(){

      self.proposalTb.on("draw.dt",function(){
        var td = $(this).find("tbody td:not(:last-child)");
        var dt = $(this).DataTable();
        td.on("click",function(){
          var tr = $(this).parent();
          var transId = dt.row(tr[0]).data().wtTransId;
          location = window.SERVER_ROOT + "/proposal/edit/" + transId;
        });
      });

      self.yearMax.on("keyup",function(){
        self.proposalTb.DataTable().draw();
      });
      self.yearMin.on("keyup",function(){
        self.proposalTb.DataTable().draw();
      });

      self.entries.on("change",function(){
        var entry = $(this).find("option:selected").val();
        var dp = self.proposalTb.DataTable();
        dp.page.len(entry).draw();
      });
    };
    self.initDataTable = function(){
      self.myDataTable = new MyDataTable({
        id:self.proposalTb.attr("id")
        ,serverSide:false
        ,paging:true
        ,pageSize:10
        ,searching:true
        ,defaultSort: [ [0,'desc'] ]
        ,url:window.SERVER_ROOT + "/proposalservice/publicproposals"
        ,columns:[
          {
            data:"transYear"
          },{
            data:"seller"
            ,render:function(data){
              return data ? data : "";
            }
//            ,render:function(data,display,row,setting){
//              var html = ""
//              if(data){
//                var link = window.SERVER_ROOT + "/proposal/edit/" + row.wtTransId;
//                html = "<a href='"+link+"'>"+data+"</a>";
//              }
//              return html;
//            }
          }
          ,{
            data:"buyers"
            ,render:function(data){
              return data ? data : "";
            }
          }
          ,{data:"transType"}
          ,{data:"surWaterSource"}
          ,{data:"status"}
          ,{data:"modifyDate"}
        ]
//        ,listeners:{
//          onEdit:function(evt,record,rowEl){
//            location = window.SERVER_ROOT + "/proposal/edit/" + record.wtTransId;
//          }
//          ,onWithdraw:self.widthDrawProposal
//          ,onPrintView:function(evt,record,rowEl){
//            var url = window.SERVER_ROOT + "/report/printReport/" + record.wtTransId +"?track=false&attachType=PR";
//            window.open(url,"_blank");
//          }
//          ,onViewArchivedReport:self.initPDFReports
//        }
      });
    };
    
    self.searchYearRange = function(){
      $.fn.dataTable.ext.search.push(
        function( settings, data, dataIndex ) {
          var min = parseInt( self.yearMin.val(), 10 );
          var max = parseInt( self.yearMax.val(), 10 );

          var year = parseFloat( data[0] ) || 0; // use data for the year column

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
    };
    
    self.init();
  };
  $(document).ready(function(){
    window.proposalTable = new ProposalTable();
  });
</script>
<div style="padding:10px 5px 0;" class="proposal_ct" id="">
  <div class="year_filter">
    <label>From Year: </label><input id="min" name="min" type="text" style="width:100px;">
    <label>To Year: </label><input id="max" name="max" type="text" style="width:100px;">
  </div>
  <div class="entries_filter">
    <label>Show</label>
    <select class="entries">
      <option value="10">10</option>
      <option value="15">15</option>
      <option value="25">25</option>
    </select>
    <label>entries</label>
  </div>
  <table id="proposal-tb" class="display" width="100%" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
        <th colspan="7" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Public Proposal List</th>
      </tr>
      <tr>
        <th>Year</th>
        <th>Seller</th>
        <th>Buyer</th>
        <th>Transfer Type</th>
        <th>Surface Water Source</th>
        <th>Status</th>
        <th>Last Modified D/T</th>
      </tr>
    </thead>
  </table>
</div>
<%@include file="../templates/footer_login.jsp" %>
