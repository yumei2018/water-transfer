<%--
    Document   : list
    Created on : Mar 23, 2015, 5:36:03 PM
    Author     : clay
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
,${pageContext.request.contextPath}/resources/js/proposal/ProposalAdmin.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
.dataTable tr {
  cursor:pointer;
}
.proposal_navi{
  background-color:#fbad23;
}
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
    self.initDataTable = function () {
      self.myDataTable = new MyDataTable({
        id: self.proposalTb.attr("id")
        , serverSide: false
        , paging: true
        , pageSize: 10
        , searching: true
        , defaultSort: [[0, 'desc']]
        , url: window.SERVER_ROOT + "/proposalservice/getproposals?moduleType=" + $(".module_type").attr('id')
        , columns: [
          {
            data: "wtTransId"
          }, {
            data: "transYear"
          }, {
            data: "seller"
            , render: function (data, display, row, setting) {
              var html = "";
              if (data) {
                var link = window.SERVER_ROOT + "/proposal/edit/" + row.wtTransId;
                html = "<a href='" + link + "'>" + data + "</a>";
              }
              return html;
            }
          }
          , {
            data: "buyers"
            , render: function (data) {
              return data ? data : "";
            }
          }
          , {data: "transType"}
          , {data: "status"}
          , {data: "modifyDate"}
        ]
        , listeners: {
          onEdit: function (evt, record, rowEl) {
            location = window.SERVER_ROOT + "/proposal/edit/" + record.wtTransId;
          }
          , onWithdraw: self.widthDrawProposal
          , onPrintView: function (evt, record, rowEl) {
            var url = window.SERVER_ROOT + "/report/printReport/" + record.wtTransId + "?track=false&attachType=PR";
            window.open(url, "_blank");
          }
          , onViewArchivedReport: self.initPDFReports
          , onDelete: self.deleteProposal
        }
      });
    };
    self.widthDrawProposal = function (evt, record, rowEl) {
      self.removeConfirmation(function (bool) {
        if (!bool) {
          return false;
        }
        self.proposalTb.mask("Removing Proposal Report...");
        $.ajax({
          url: window.SERVER_ROOT + "/proposal/removeproposal"
          , data: {wtTransId: record.wtTransId}
          , cache: false
          , type: 'post'
          , dataType: 'json'
          , success: function (data, status, jqxhr) {
            try {
              if (!data.success) {
                throw data.error || "Unable to remove the proposal.";
              }
              self.proposalTb.unmask();
              self.proposalTb.DataTable()
                      .row(rowEl)
                      .remove()
                      .draw(false);
            } catch (e) {
              self.proposalTb.unmask();
              if (data.callback) {
                var callback = eval(data.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              } else if (e) {
                alert(e);
              }
            }
          }
          , error: function (xhr, errorType, exception) {
            self.proposalTb.unmask();
            if (xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
            }
          }
        });
      });
    };
    self.initPDFReports = function (evt, record, rowEl) {
      self.form = $("<form method='post' id='PDFReportsform' action='#'>");
      var title = "Proposal PDF Reports";
      var data = {
        reportType: "PR"
        , transYear: record.transYear
        , sellerName: record.agencyFullName
      };
      var url = window.SERVER_ROOT + "/proposal/PDFReport/" + record.wtTransId;
      self.proposalTb.mask("Loading PDF Report...");
      $.ajax({
        type: "POST"
        , url: url
        , data: data
        , cache: false
        , scope: this
        , success: function (data, status, jqxhr) {
          self.form.append(data);
          self.form.dialog({
            title: title
            , width: 1000
            , height: 400
            , resizable: true
            , modal: true
          }).dialog('open');
          self.proposalTb.unmask();
        }
        , error: function (xhr, errorType, exception) {
          self.proposalTb.unmask();
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    };
    self.deleteProposal = function (evt, record, rowEl) {
      self.removeConfirmation(function (bool) {
        if (!bool) {
          return false;
        }
        var url = window.SERVER_ROOT + "/proposal/deleteproposal/" + record.wtTransId;
        self.proposalTb.mask("Deleting Proposal ...");
        $.ajax({
          type: "POST"
          , url: url
          , cache: false
          , dataType: 'json'
          , success: function (data, status, jqxhr) {
            try {
              if (!data.success) {
                throw data.error || "Unable to delete the proposal.";
              }
              self.proposalTb.unmask();
              self.proposalTb.DataTable()
                      .row(rowEl)
                      .remove()
                      .draw(false);
            } catch (e) {
              self.proposalTb.unmask();
              if (data.callback) {
                var callback = eval(data.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              } else if (e) {
                alert(e);
              }
            }
          }
          , error: function (xhr, errorType, exception) {
            self.proposalTb.unmask();
            if (xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
            }
          }
        });
      });
    };
    self.searchYearRange = function () {
      $.fn.dataTable.ext.search.push(
        function (settings, data, dataIndex) {
          var min = parseInt(self.yearMin.val(), 10);
          var max = parseInt(self.yearMax.val(), 10);

          var year = parseFloat(data[0]) || 0; // use data for the year column

          if ((isNaN(min) && isNaN(max)) ||
                (isNaN(min) && year <= max) ||
                (min <= year && isNaN(max)) ||
                (min <= year && year <= max))
          {
            return true;
          }
          return false;
        }
      );
    };
    self.removeConfirmation = function (callback) {
      var msg = "You will no longer have access to view or to edit if you withdraw this proposal. \n\
                  Do you wish to proceed?";
      $("<div>", {
        html: '<p>' + msg + '</p>'
      }).dialog({
        title: "Remove Confirmation"
        , modal: true
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
    };
    self.init();
  };
  $(document).ready(function () {
    window.proposalTable = new ProposalTable();
    window.admin = new ProposalAdmin();
  });
</script>
<div style="padding:10px 5px 0;" class="proposal_ct module_type" id="${moduleType}">
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
        <th colspan="8" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Proposal Table</th>
      </tr>
      <tr>
        <th>ID</th>
        <th>Year</th>
        <th>Seller</th>
        <th>Buyer</th>
        <th>Transfer Type</th>
        <th>Status</th>
        <th>Last Modified D/T</th>
        <th>Actions</th>
      </tr>
    </thead>
  </table>
</div>

  <div>
    <input class="attachButton"
           type="button" 
           id="deleteDraftButton" 
           value="Delete All Draft Proposals" 
           onclick="window.admin.deleteDraftProposal(this);return false;"/>
  </div>
<%@include file="../templates/footer.jsp" %>