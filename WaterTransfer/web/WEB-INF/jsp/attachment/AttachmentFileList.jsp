<%--
    Document   : AttachmentFileList
    Created on : Mar 25, 2015, 1:44:51 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="hideTable" value="hidden"/>
<c:set var="hideField" value="hidden"/>
<c:set var='hideReportItem' value="hidden"/>
<c:set var="disableEdit" value="disabled"/>
<c:if test="${fn:length(attachmentCollection) gt 0}">
  <c:set var="hideTable" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
  <c:set var="hideReportItem" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(statusFlag.statusName eq 'DRAFT')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(statusFlag.statusName eq 'INCOMPLETE')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>

<%--<c:if test="${fn:length(attachmentCollection) gt 0}">
  <c:set var="isReviewer" value="hidden"/>
  <c:set var="disableEdit" value="disabled"/>
  <c:set var='hideReportItem' value="hidden"/>
  <c:if test="${!(statusFlag.statusName eq 'DRAFT')&&!empty(statusFlag.statusName)}">
    <c:set var="hidden" value="hidden"/>
  </c:if>
  <c:if test="${sessionScope.USER.isAppAccount()||sessionScope.USER.isManager()}">
    <c:set var="isReviewer" value=""/>
    <c:set var="disableEdit" value=""/>
    <c:set var='hideReportItem' value=""/>
  </c:if>--%>

<c:if test="${!empty(fsaMapNum)}">
  <div class="mapLabel">FSA Map Total# is ${fsaMapNum}</div>
</c:if>
<table class="attachmentTb ${hideTable}" id="attachment-table" wtTransId="${wtTransId}">
  <tr class="attach_header">
    <th>File Names</th>
    <th>Size</th>
    <th># of Items</th>
    <th>Title</th>
    <th>Uploaded By</th>
    <th>Upload Date</th>
    <th class="${hideField}">Edit</th>
    <th class="${hideField}">Remove</th>
  </tr>
  <c:forEach var="attachment" items="${attachmentCollection}">
    <c:set var="bg" value="white"/>
    <c:set var="wtAttachmentId">${attachment.wtAttachmentId}</c:set>
    <c:set var="createdById">${attachment.createdById}</c:set>
    <c:if test="${(attachment.wtChecklistCollection.size() eq 0)}">
      <c:set var="bg" value="#F6727F"/>
    </c:if>
    <tr class="attach_row" id='${attachment.wtAttachmentId}'>
      <td id="filename"><a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">${attachment.filename}</a></td>
      <td>${attachment.readableFilesize}<%--${filesize.optString(wtAttachmentId)}--%></td>
      <td style="background-color:${bg};">${attachment.wtChecklistCollection.size()}</td>
      <td id="title">${attachment.title}</td>
      <td>${attachment.createdBy.name}</td>
      <td id="createdDate"><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.createdDate}" /></td>
      <td class="${hideField}" id="edit-attachment" style="text-align:center;">
        <img class="picture-edit-icon" id='${attachment.wtAttachmentId}' typeId="${attachType}" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Attachment">
      </td>
      <td class="${hideField}" id="remove-attachment" style="text-align:center;">
        <img class="picture-delete-icon" id='${attachment.wtAttachmentId}' src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Attachment">
      </td>
    </tr>
  </c:forEach>
</table>
<%--</c:if>--%>

<table id="checklist-table" class="">
  <thead>
    <c:if test="${not empty checklistCollection}">
      <tr class="checklist_header">
        <th>Attachments Checklist</th>
        <th>File Name</th>
      </tr>
    </c:if>
  </thead>
  <tbody>
    <c:forEach var="checklist" items="${checklistCollection}">
      <c:set var="isMissing" value="1"/>
      <tr>
        <td>${checklist.name}&nbsp;
          <c:if test="${checklist.detail != null}">
            <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" alt="Help" title="Checklist Help">
            <div class="detail hidden">${checklist.detail}</div>
          </c:if>
        </td>
        <td>
          <c:forEach var="attachment" items="${attachmentCollection}">
            <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
              <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                <c:set var="isMissing" value="0"/>
                <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">
                  ${attachment.filename}
                </a>&nbsp;
              </c:if>
            </c:forEach>
          </c:forEach>
          <c:if test="${isMissing == 1}">
            <span style="color:red"> </span>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>

<div id="attachment-help-ct"></div>

<script type="text/javascript">
$(document).ready(function(){
  window.AttachmentFileList = new AttachmentList();
});
</script>
<style type="text/css">
div.attachment-container{
  margin-top: 0px;
}
div.mapLabel{

}
#attachment-table{
  /*width: 800px;*/
  width:100%;
  text-align: center;
  margin-bottom: 10px;
}
#attachment-table th{
  border: 1px solid #cccccc;
  padding:10px 0px;
  background: none repeat scroll 0 0 #e3e3e3;
  text-align: center;
}
#attachment-table td{
  text-align: center;
  border:1px solid #cccccc;
  padding: 3px;
}
.attach_header {
  background-color: #8cb3d9;
  font-size: 14px;
  width: 300px;
}
.attach_row {
  color: #000000;
  font-size: 12px;
  width: 300px;
}
.picture-delete-icon{
  cursor: pointer;
}
.picture-edit-icon{
  cursor: pointer;
}
#checklist-table{
  width:100%;
}
#checklist-table th{
  border: 1px solid #ffffff;
  background-color:#dddddd;
  color:#333;
  text-align: left;
  padding:7px ;
  font-size: 14px;
}
#checklist-table tr:nth-child(odd){
  background-color: #dddddd;
}
#checklist-table td{
  text-align: left;
  border:0px solid #cccccc;
  padding: 7px;
  font-size: 12px;
}
#checklist-table tbody tr:nth-child(2n+1)
{
  background-color:#EEEEEE;
}
a:hover{
  color:blue;
}
</style>

