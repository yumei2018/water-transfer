<%-- 
    Document   : AgencyList
    Created on : Nov 7, 2016, 11:04:12 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/css/wtProposal.css
,${pageContext.request.contextPath}/resources/css/agencyList.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery.tablesorter.min.js
,http://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<span class="breadcrumbs"><a href="${pageContext.request.contextPath}/admin">Admin</a> &gt; Agency</span>
<c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
  <div style="padding:30px 5px 0;" class="module_type">
    <div id="agency-list">
      <table id="agencyTable" class="tablesorter">
        <thead>
<!--          <tr>
            <th colspan="6" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Agency List</th>
          </tr>-->
          <tr class="agency_header">
            <th style="width: 160px;">Agency ID</th>
            <th style="width: 160px;">Agency Code</th>
            <th style="width: 440px;">Agency Name</th>
            <th style="width: 100px;">Agency Type</th>
            <th style="width: 50px;">Save</th>
            <th style="width: 50px;">Edit</th>
            <th style="width: 50px;">Remove</th>
          </tr>
        </thead>
        <tbody>
          <tr class="hidden">
            <td class="input"><input type="text" value="" name="wtAgencyId" maxlength="16" disabled/></td>
            <td class="input"><input type="text" value="" name="agencyCode" maxlength="16"/></td>
            <td class="input"><input style="width: 400px;" type="text" value="" name="agencyFullName" maxlength="128"/></td>
            <td class="input" style="width: 100px;">
              <select name="agencyType">
                <option value="AG">AG</option>
                <option value="URBAN">URBAN</option>
              </select>
            </td>
            <td style="width: 50px;"><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Transfer" title="Save Transfer"/></td>
            <td style="width: 50px;"><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Transfer" title="Edit Transfer"/></td>
            <td style="width: 50px;"><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Transfer" title="Remove Transfer"/></td>
          </tr>
          <c:forEach var="agency" items="${agencyList}">
            <tr class="agency_row">
              <td class="input"><span class="hidden">${agency.wtAgencyId}</span>
                <input type="text" value="${agency.wtAgencyId}" name="wtAgencyId" maxlength="16" disabled/>
              </td>
              <td class="input"><span class="hidden">${agency.agencyCode}</span>
                <input type="text" value="${agency.agencyCode}" name="agencyCode" maxlength="16" disabled/>
              </td>
              <td class="input"><span class="hidden">${agency.agencyFullName}</span>
                <input type="text" style="width: 400px;" value="${agency.agencyFullName}" name="agencyFullName" maxlength="16" disabled/>
              </td>
              <td class="input" style="width: 100px;">
                <select name="agencyType" class="agencyType" disabled>
                  <option value="AG" ${agency.agencyType == "AG" ? 'selected="selected"' : ''}>AG</option>
                  <option value="URBAN" ${agency.agencyType == "URBAN" ? 'selected="selected"' : ''}>URBAN</option>
                </select>
              </td>
              <td style="width: 50px;"><img class="save-icon hidden" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Agency" title="Save Agency"/></td>
              <td style="width: 50px;"><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Agency" title="Edit Agency"/></td>
              <td style="width: 50px;"><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Agency" title="Remove Agency"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <input class="addButton" type="button" id="" value="Add New Agency" onclick="return false;"/>
    </div>
  </div>
</c:if>
<%@include file="../templates/footer.jsp" %>

<script type="text/javascript">
$(document).ready(function(){
  var self = this;
  
  self.init = function() {
    self.initItems();
    self.initListeners();
  };  
  self.initItems = function()
  {
    self.agencyList = $("#agency-list");
    self.agencyTable = $("table#agencyTable");
    self.hiddenRow = self.agencyTable.find("tbody tr.hidden");
    self.addBtn = self.agencyList.find(".addButton");
    self.saveBtn = self.agencyTable.find("img.save-icon");
    self.editBtn = self.saveBtn.parent().next().find("img.edit-icon");
    self.removeBtn = self.editBtn.parent().next().find("img.delete-icon");
    self.agencyTable.tablesorter({
      headers: {
        3: {sorter: false },
        4: {sorter: false },
        5: {sorter: false },
        6: {sorter: false }
      }
    });
  };
  self.initListeners=function(){
  //  self.addBtn.on('click', self.initNewAgency);
    self.addBtn.unbind('click').bind("click",self.addNewAgency);
    self.saveBtn.unbind('click').bind("click",self.saveHandler);
    self.editBtn.unbind('click').bind("click",self.editHandler);
    self.removeBtn.unbind('click').bind("click",self.removeHandler);
  };
  
  self.addNewAgency = function () {
//    alert("Add New Agency");
    var row = $("<tr/>").html(self.hiddenRow.html());
//    alert(self.hiddenRow.html());
    self.agencyTable.find("tbody tr:last").after(row);
    self.initItems();
    self.initListeners();
//    self.preTransferItems();
  };
  
  self.saveHandler = function () {
    alert("Save New Agency");
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.agencycode = self.row.find("input[name=agencyCode]");
    self.agencyname = self.row.find("input[name=agencyFullName]");
    self.agencytype = self.row.find("select[name=agencyType]");
    
    self.agencycode.attr("disabled", true);
    self.agencyname.attr("disabled", true);
    self.agencytype.attr("disabled", true);
    $(this).hide();
    self.agencyId = self.row.find("input[name=wtAgencyId]");
//    alert(self.agencycode.val());
//    self.agencyList.mask("Saving data...");
    
    $.ajax({
      url: window["SERVER_ROOT"] + '/admin/saveAgency'
      , data: {agencyCode: self.agencycode.val(), agencyFullName: self.agencyname.val(), 
                agencyType: self.agencytype.val(), wtAgencyId: self.agencyId.val()}
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr)
      {
        try {
          if (!data.success) {
            throw data.error || "Unable to save this new agency.";
          }
//          self.agencyList.unmask();
          alert("Saved Successfully!");
          self.agencyId.val(data.wtAgencyId);
        } catch (e) {
//          self.agencyList.unmask();
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
  
  self.editHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.agencycode = self.row.find("input[name=agencyCode]");
    self.agencyname = self.row.find("input[name=agencyFullName]");
    self.agencytype = self.row.find("select[name=agencyType]");
    self.saveBt = self.row.find(".save-icon");
    self.agencycode.attr("disabled", false);
    self.agencyname.attr("disabled", false);
    self.agencytype.attr("disabled", false);
    self.saveBt.removeClass("hidden").show();
    alert("You can now edit the textfield. Please be sure to save when done.");
  };
  
  self.removeHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.agencyId = self.row.find("input[name=wtAgencyId]");
    self.agencyname = self.row.find("input[name=agencyFullName]");
    if (self.agencyId.val() === "")
    {
      self.row.remove();
      return false;
    }
    self.msg = "Are you sure you want to remove agency "+self.agencyname.val()+"?";
    var data = {wtAgencyId: self.agencyId.val()};
    self.agencyValidation(self.msg, function (bool) {
      if (bool) {
        $.ajax({
          url: window["SERVER_ROOT"] + "/admin/removeAgency"
          , data: data
          , cache: false
          , dataType: 'json'
          , success: function (data, status, jqxhr)
          {
            try {
              if (!data.success) {
                throw data.error || "Unable to remove this agency.";
              }
              self.row.remove();
              alert("Removed Successfully!!!");
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
            if (xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
            }
            else {
              alert("Failed to remove");
            }
          }
        });
      }
    });
  };
  
  self.agencyValidation = function (msg, callback)
  {
    $("<div>", {
      html: '<p>' + msg + '</p>'
    }).dialog({
      title: 'Remove Validation'
      , modal: true
      , width: 420
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
            callback(false);
            $(this).dialog("destroy").remove();
          }
        }]
    }).dialog("open");
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
