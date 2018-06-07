<%--
    Document   : StatusTrack
    Created on : Apr 17, 2015, 9:08:05 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
  #statustrack_tab{
    overflow: hidden;
  }
  #statusBtn{
    height: 80px;
  }
  #table{
    width: 1007px;
    text-align: center;
    margin-bottom: 20px;
  }
  #statusTrack-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #statusTrack-table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
  }
  .status_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .status_row {
    color: #000000;
    font-size: 15px;
    width: 300px;
  }
  #pdf_status_track{
    margin-top: 70px;
  }
  #pdf_status_track table{
    margin-top: 10px;
    width: 100%
  }
  #pdf_status_track table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    font-size: 15px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #pdf_status_track table td{
    font-size: 15px;
    border: 1px solid #cccccc;
    padding: 5px;
  }
  .refresh_icon,.remove{
    cursor:pointer;
    opacity: 0.7;
  }
  .refresh_icon:hover{
    opacity: 1;
  }
  #pdf_status_track table tr td:last-child{
    text-align: center;
  }
  #communication_log{
    margin-top: 50px;
  }
  .comm_log table{
    margin-top: 10px;
    width: 100%
  }
  .comm_log table th{
    text-align: center;
    border: 1px solid #cccccc;
    padding:10px 0px;
    font-size: 15px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  .comm_log table td{
    font-size: 15px;
    border: 1px solid #cccccc;
    padding: 5px;
  }
</style>
<script type="text/javascript">
  $(document).ready(function () {
    var self = $(this);
    self.init = function ()
    {
      self.initItems();
      self.initListeners();
    };
    self.initItems = function ()
    {
      self.proposalCt = $("#tabs");
      self.statusTrackTab = self.proposalCt.find("#statustrack_tab");
      self.statusTrackTable = self.statusTrackTab.find("#statusTrack-table");
      self.proposalprocessTab = self.proposalCt.find("#proposalprocess_tab");
      self.createForm = self.proposalCt.find("#create-form");
      self.statusTrackForm = $("#statusTrackForm");
      self.statusTrackContainer = $("#status_track_ct");
      self.statusButton = $("#changeStatus");
      self.wtTransId = self.statusTrackContainer.find("#wtTransId");
      self.statusSaveBtn = self.statusTrackTab.find("img.save-icon");
      self.statusEditBtn = self.statusTrackTab.find("img.edit-icon");
      self.communicationLog = self.statusTrackTab.find("#communication_log");
      self.addCommentsBtn = self.communicationLog.find("#addComments");

      self.eidtStatusTrackContainer = $("#edit_status_track");
      self.statusDescription = self.eidtStatusTrackContainer.find("#statusDescription");
      self.statusDate = self.eidtStatusTrackContainer.find("input[name=statusDate]");
      self.statusComments = self.eidtStatusTrackContainer.find("textarea[name=statusComments]");

      self.proposalprocessTab = self.proposalCt.find("#proposalprocess_tab");
      self.proCompleteDate = self.proposalprocessTab.find("#proCompleteDate");
      self.condApprovalDate = self.proposalprocessTab.find("#condApprovalDate");
      self.proApprovedDate = self.proposalprocessTab.find("#proApprovedDate");
    };
    self.initListeners = function () {
      self.statusEditBtn.unbind('click').bind("click", self.initStatusEdit);
      self.statusSaveBtn.unbind('click').bind("click", self.initStatusSave);
//      self.statusButton.unbind('click').bind("click", self.initStatusTrack);
      self.statusButton.unbind('click').bind("click", self.initChangeStatus);
      self.addCommentsBtn.unbind('click').bind("click", self.initAddComments);
    };

    self.initStatusEdit = function () {
      var thisRow = $(this).parent().parent();
      var wtTransId = self.createForm.find("#wtTransId").val();
      var wtStatusTrackId = thisRow.find('input[name=wtStatusTrackId]').val();
      var statusDescription = thisRow.find('.statusDescription').html();
      var statusDate = thisRow.find('input[name=statusDate]').val();
      var statusComments = thisRow.find('input[name=statusComments]').val();
      self.wtTransId.attr("value", wtTransId);
      self.statusDescription.html(statusDescription);
      self.statusDate.val(statusDate);
//            self.statusDate.on("blur", function(e) { $(this).datepicker(); });
//            self.statusDate.on("blur", function(e) { $(this).datepicker("hide"); });
      self.statusComments.val(statusComments);
//            self.statusComments.focus();
//            alert(statusComments);
//            self.statusComments.val("value");
//            alert(statusDate);

      self.eidtStatusTrackContainer.dialog({
        title: 'Status Edit Form'
        , modal: true
        , width: 480
        , height: 380
        , resizable: false
        , buttons: [{
            text: "Save"
            , click: function () {
              var dialog = this;
              var newStatusDate = self.eidtStatusTrackContainer.find("input[name=statusDate]").val();
              var newStatusComments = self.eidtStatusTrackContainer.find("textarea[name=statusComments]").val();
              var url = window.SERVER_ROOT + "/proposal/saveStatusTrack";
              $.ajax({
                type: "POST"
                , url: url
                , data: {wtStatusTrackId: wtStatusTrackId, statusDate: newStatusDate, statusComments: newStatusComments}
                , cache: false
                , dataType: 'json'
                , success: function (data, status, jqxhr) {
                  try {
                    if (!data.success) {
                      throw data.error || "Unable to save status track.";
                    }
                    alert("Saved Successfully!");
                    thisRow.find('input[name=statusDate]').val(newStatusDate);
                    thisRow.find('input[name=statusComments]').val(newStatusComments);
                    if (statusDescription === "Proposal Complete") {
                      self.proCompleteDate.val(newStatusDate);
                    } else if (statusDescription === "Conditional Approval") {
                      self.condApprovalDate.val(newStatusDate);
                    } else if (statusDescription === "Proposal Approved") {
                      self.proApprovedDate.val(newStatusDate);
                    }
                    $(dialog).dialog("close");
                  } catch (e) {
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
                  alert("Failed change status!");
                }
              });
//                        $(this).dialog("close");
//                        $(this).dialog("destroy").remove();
            }
          }, {
            text: "Cancel"
            , click: function () {
              $(this).dialog("close");
//                        $(this).dialog("destroy").remove();
            }
          }]
      }).dialog('open');
    };

//        self.statusEditBtn.on("click", function(){
//            if($(this).attr("disabled") === "disabled")
//            {
//                return false;
//            }
//            var thisRow = $(this).parent().parent();
//            var statusDate = thisRow.find('input[name=statusDate]');
//            var statusComments = thisRow.find('input[name=statusComments]');
//            statusDate.attr("disabled",false);
//            statusComments.attr("disabled",false);
//            alert("You can now edit the textfield. Please be sure to save when done.");
//        });

    self.initStatusSave = function ()
    {
      if ($(this).attr("disabled") === "disabled")
      {
        return false;
      }
      var thisRow = $(this).parent().parent();
      var statusDate = thisRow.find('input[name=statusDate]');
      var statusComments = thisRow.find('input[name=statusComments]');
      var wtStatusTrackId = thisRow.find('input[name=wtStatusTrackId]');
      statusDate.attr("disabled", true);
      statusComments.attr("disabled", true);
//            alert("Save here");
      $.ajax({
        url: window["SERVER_ROOT"] + "/proposal/saveStatusTrack"
        , data: {wtStatusTrackId: wtStatusTrackId.val(), statusDate: statusDate.val(), statusComments: statusComments.val()}
        , cache: false
        , success: function (data, status, jqxhr)
        {
          alert("Saved Successfully!");
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
          else
          {
            alert("Saving Failed");
          }
        }
      });
    };

    self.initStatusTrack = function () {
//            alert("Reject...");
      self.statusTrackContainer.find(".fieldValue").val("");
      self.swpaoReviewer = self.proposalprocessTab.find("#swpaoReviewer");
      self.regionReviewer = self.proposalprocessTab.find("#regionReviewer");
      self.usbrReviewer = self.proposalprocessTab.find("#usbrReviewer");
      var wtTransId = self.createForm.find("#wtTransId").val();
      self.wtTransId.attr("value", wtTransId);

      self.statusTrackContainer.dialog({
        appendTo: "form#statusTrackForm",
        modal: true,
        title: "Status Change Form",
        width: 480,
        height: 380,
        resizable: false,
        buttons: [{
            text: "OK"
            , click: function () {
              var data = self.statusTrackForm.serialize();
              var swpaoReviewer = self.swpaoReviewer.val();
              var regionReviewer = self.regionReviewer.val();
              var wtStatusFlagId = self.statusTrackForm.find("#wtStatusFlagId").val();
              var newStatusDate = self.statusTrackForm.find("input[name=statusDate]").val();
//              alert(swpaoReviewer + ":" + regionReviewer);
              // If Status change to Under Review, check there is Email address
              if (wtStatusFlagId === '2') {
                if (swpaoReviewer === '' && regionReviewer === '') {
                  alert("Please enter email addressed for SWPAO/Region/Reclamation Reviewer");
                  return;
                }
              }
              var url = window.SERVER_ROOT + "/proposal/statusTrack";
              $.ajax({
                type: "POST"
                , url: url
                , data: data
                , cache: false
                , success: function (data, response, status, jqxhr) {
                  alert("Status Changed!");
                  self.statusTrackTab.html(data);
//                                alert(wtStatusFlagId+": "+newStatusDate);
                  if (wtStatusFlagId === "3") {
                    self.proCompleteDate.val(newStatusDate);
                  } else if (wtStatusFlagId === "4") {
                    self.condApprovalDate.val(newStatusDate);
                  } else if (wtStatusFlagId === "5") {
                    self.proApprovedDate.val(newStatusDate);
                  }
//                                self.init();
                }
                , error: function (xhr, errorType, exception) {
                  if (xhr.status === 403) //session ends
                  {
                    location = window.SERVER_ROOT;
                  }
                }
              });
              $(this).dialog("close");
//                        $(this).dialog("destroy").remove();
            }
          }, {
            text: "Cancel"
            , click: function () {
              $(this).dialog("close");
//                        $(this).dialog("destroy").remove();
            }
          }]
      }).dialog('open');
    };
    
    self.initChangeStatus = function () {
      self.statusTrackContainer.find(".fieldValue").val("");
      self.swpaoReviewer = self.proposalprocessTab.find("#swpaoReviewer");
      self.regionReviewer = self.proposalprocessTab.find("#regionReviewer");
      self.usbrReviewer = self.proposalprocessTab.find("#usbrReviewer");
      var wtTransId = self.createForm.find("#wtTransId").val();
      self.wtTransId.attr("value", wtTransId);

      self.statusTrackContainer.dialog({
        appendTo: "form#statusTrackForm",
        modal: true,
        title: "Status Change Form",
        width: 480,
        height: 380,
        resizable: false,
        buttons: [{
            text: "OK"
            , click: function () {
//              var data = self.statusTrackForm.serialize();
              var swpaoReviewer = self.swpaoReviewer.val();
              var regionReviewer = self.regionReviewer.val();              
//              alert(swpaoReviewer + ":" + regionReviewer);
              // If Status change to Under Review, check there is Email address
              if (wtStatusFlagId === '2') {
                if (swpaoReviewer === '' && regionReviewer === '') {
                  alert("Please enter email addressed for SWPAO/Region/Reclamation Reviewer");
                  return;
                }
              }
              var wtStatusFlagId = self.statusTrackForm.find("#wtStatusFlagId").val();
              var newStatusDate = self.statusTrackForm.find("input[name=statusDate]").val();
              var newStatusComments = self.statusTrackForm.find("textarea[name=statusComments]").val();
//              alert(wtStatusFlagId+":"+newStatusDate+":"+newStatusComments);
              var url = window.SERVER_ROOT + "/proposal/changeStatus";
              self.statusTrackTab.mask("Changing Status ...");
              $.ajax({
                type: "POST"
                , url: url
                , data: {wtTransId: wtTransId, wtStatusFlagId: wtStatusFlagId, statusDate: newStatusDate, statusComments: newStatusComments}
                , cache: false
                , dataType: 'json'
                , success: function (response, status, jqxhr) {
                  try {
                    if (!response.success) {
                      throw response.error || "Unable to change Status.";
                    }
                    var jsonData = response;
                    var formattedDate = $.datepicker.formatDate('mm/dd/yy', new Date(jsonData['statusDate']));
                    self.newRow = self.statusTrackTable.find("tbody tr:last");
                    var htmlRow = $("<tr class='status_row hidden'/>").html(self.newRow.html());
                    self.newRow.find("#wtTransId").html(jsonData["wtTransId"]);
                    self.newRow.find("#statusDescription").html(jsonData["statusDescription"]);
                    self.newRow.find("input[name=statusDate]").attr('value',formattedDate);
                    self.newRow.find("input[name=statusComments]").attr('value',jsonData["statusComments"]);
                    self.newRow.find("#username").html(jsonData["username"]);
                    self.newRow.find("input[name=wtStatusTrackId]").val(jsonData["wtStatusTrackId"]);                    
                    self.newRow.removeClass("hidden").show();
//                    alert(self.newRow.html());                    
                    alert("Status Changed!");
                    self.statusTrackTab.unmask();
                    self.statusTrackTable.find("tbody tr:last").after(htmlRow);
                    self.statusEditBtn = self.statusTrackTab.find("img.edit-icon");
                    self.statusEditBtn.unbind('click').bind("click", self.initStatusEdit);
//                  self.statusTrackTab.html(data);
                    if (wtStatusFlagId === "3") {
                      self.proCompleteDate.val(newStatusDate);
                    } else if (wtStatusFlagId === "4") {
                      self.condApprovalDate.val(newStatusDate);
                    } else if (wtStatusFlagId === "5") {
                      self.proApprovedDate.val(newStatusDate);
                    }
                  } catch (e) {
                    self.statusTrackTab.unmask();
                    if (response.callback) {
                      var callback = eval(response.callback);
                      if (typeof callback === "function") {
                        callback.call();
                      }
                    } else if (e) {
                      alert(e);
                    }
                  }
                }
                , error: function (xhr, errorType, exception) {
                  self.statusTrackTab.unmask();
                  if (xhr.status === 403) //session ends
                  {
                    location = window.SERVER_ROOT;
                  }
                }
              });
              $(this).dialog("close");
//              $(this).dialog("destroy").remove();
            }
          }, {
            text: "Cancel"
            , click: function () {
//              $(this).dialog("close");
              $(this).dialog("destroy").remove();
            }
          }]
      }).dialog('open');
    };

    self.initAddComments = function () {
//            alert("Comments....");
      var wtTransId = self.createForm.find("#wtTransId").val();
      var communicationTable = self.communicationLog.find("table tbody");
      var commentsDialog = $('<div><textarea cols="50" rows="8" id="reviewerComments" name="reviewerComments" placeholder="Enter the Comment Here" style="margin-top:20px;margin-left:20px;"></textarea></div>');

      commentsDialog.dialog({
        title: 'Internal Communication Log'
        , width: 600
        , modal: true
        , height: 400
        , buttons: [{
            text: 'Save'
            , click: function () {
              var reviewerComments = $("textarea#reviewerComments").val();
//                        alert(reviewerComments);
//                        alert(wtTransId);
              $.ajax({
                url: window.SERVER_ROOT + '/proposal/saveReviewerComments'
                , type: 'POST'
                , data: {wtTransId: wtTransId, reviewerComments: reviewerComments}
                , cache: false
                , dataType: 'json'
                , success: function (response, status, jqxhr) {
                  try {
                    if (!response.success) {
                      throw response.error || "Unable to remove target storage.";
                    }
                    var jsonData = response;
                    var formattedDate = $.datepicker.formatDate('mm/dd/yy', new Date());
                    var htmlRow = "<tr><td>" + formattedDate + "</td>";
                    htmlRow += "<td>" + jsonData['reviewerName'] + "</td>";
                    htmlRow += "<td>" + jsonData['reviewerComments'] + "</td></tr>";
                    alert("Comments Saved.");
                    commentsDialog.dialog("destroy").remove();
                    communicationTable.append(htmlRow);
                  } catch (e) {
                    if (response.callback) {
                      var callback = eval(response.callback);
                      if (typeof callback === "function") {
                        callback.call();
                      }
                    } else if (e) {
                      alert(e);
                    }
                  }
                }
                , error: function (xhr, errorType, exception) {
                  if (xhr.status === 403) //session ends
                  {
                    location = window.SERVER_ROOT;
                  }
                }
              });
            }
          }, {
            text: 'Cancel'
            , click: function () {
//                        $(this).dialog('close');
              $(this).dialog("destroy").remove();
            }
          }]
      });
    };

    self.init();
  });
</script>
<c:if test="${empty(statusTrackList)}">
  <c:set var="hideTable" value="hidden"/>
</c:if>
<div id="statustrack_tab">&nbsp;
  <div id="status_track">
    <h1>Proposal Status Tracker</h1><br/>
    <table id="statusTrack-table" class="${hideTable}" width="100%" cellspacing="0" cellpadding="0">
      <thead>
        <tr class="status_header">
          <th>Proposal ID</th>
          <th>Status</th>
          <th>Status Date</th>
          <th>Comments</th>
          <th>Changed By</th>
          <!--                <th>Save</th>-->
          <th>Edit</th>
        </tr>
      </thead>
      <tbody>        
        <c:forEach var="statusTrack" items="${statusTrackList}">
          <tr class="status_row">
            <td id="wtTransId">${statusTrack.wtTransId}</td>
            <td id="statusDescription">${statusTrack.statusDescription}</td>
            <td><input type="text" name="statusDate" class="dateField" value="<fmt:formatDate value="${statusTrack.statusDate}" pattern="MM/dd/yyyy"/>" disabled/></td>
            <td><input type="text" name="statusComments" value="${statusTrack.statusComments}" style="width:200px;" disabled/></td>
            <td id="username">${statusTrack.username}</td>
<!--        <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Status" title="Save Status"/></td>-->
            <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Status" title="Edit Status"/></td>
            <input type="hidden" value="${statusTrack.wtStatusTrackId}" name="wtStatusTrackId"/>
          </tr>
        </c:forEach>
        <tr class="status_row hidden">
          <td id="wtTransId"></td>
          <td id="statusDescription"></td>
          <td><input type="text" name="statusDate" class="dateField" value="" disabled/></td>
          <td><input type="text" name="statusComments" value="" style="width:200px;" disabled/></td>
          <td id="username"></td>
          <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Status" title="Edit Status"/></td>
          <input type="hidden" value="" name="wtStatusTrackId"/>
        </tr>
      </tbody>
    </table>
    <button class="proposal_btn ${newProposal}" id="changeStatus" style="width:145px;margin-top: 10px;">Change Status</button>
  </div>

  <div id="edit_status_track" style="display:none;">
    <div>
      <label style="padding-right:20px;">Status:</label>
      <label class="fieldValue" id="statusDescription" name="statusDescription"></label>
    </div>
    <div style="padding-top: 20px;">
      <label>Comments</label><br/>
      <textarea class="fieldValue" style="width:400px;" name="statusComments"></textarea>
    </div>
    <div style="padding-top: 20px;">
      <label style="padding-right:13px;">Status Date</label>
      <input class="fieldValue dateField" text="text" style="width:200px;" name="statusDate"/>
    </div>
  </div>

  <div id="communication_log">
    <div class="tab_header isExpand">Internal Communication Log</div>
    <div class="contact_panel comm_log">
      <table>
        <thead>
          <tr>
            <th style="width:150px;text-align:center;">Date</th>
            <th style="width:150px;">Reviewer</th>
            <th>Comments</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${reviewerComments}" var="comment" >
            <tr>
              <td><fmt:formatDate value="${comment.createdDate}" pattern="MM/dd/yyyy"/></td>
              <td>${comment.reviewerName}</td>
              <td>${comment.reviewerComments}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <button class="proposal_btn ${newProposal}" id="addComments" style="width:145px;margin-top:20px;">Add Comments</button>
    </div>
  </div>
</div>

