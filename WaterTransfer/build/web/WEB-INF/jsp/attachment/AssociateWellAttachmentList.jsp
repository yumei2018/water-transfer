<%--
    Document   : AssociateWellAttachmentList
    Created on : Jun 24, 2015, 11:22:06 AM
    Author     : pheng
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="typeMenu">
  <div class="head_explain">
    To add your attachments, select the Add Attachment button. In the pop-up window, select which properties belong to your document and upload.
  </div>
</div>

<table id="attachmentCt">
  <thead>
    <tr class="attach_header">
      <th>File Name</th>
      <th>Size</th>
      <th># of Items</th>
      <th>Title</th>
      <th>Uploaded By</th>
      <th>Uploaded Date</th>
      <th>Edit</th>
      <th>Remove</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${WtWell.wtAttachmentCollection}" var="attachFile" >
      <c:set var="bg" value="white"/>
      <c:set var="wtAttachmentId">${attachFile.wtAttachmentId}</c:set>
      <c:set var="createdById">${attachFile.createdById}</c:set>
      <c:if test="${(attachFile.wtChecklistCollection.size() eq 0)}">
        <c:set var="bg" value="#F6727F"/>
      </c:if>
      <tr>
        <td><a href="${pageContext.request.contextPath}/attachment/view/${attachFile.wtAttachmentId}" target="_blank">${attachFile.filename}</a></td>
        <td>${attachFile.readableFilesize}<%--${size.optString(wtAttachmentId)}--%></td>
        <td style="background-color:${bg};">${attachFile.wtChecklistCollection.size()}</td>
        <td>${attachFile.title}</td>
        <td>${attachFile.createdBy.name}<%--${size.optString(createdById)}--%></td>
        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attachFile.createdDate}" type="date" dateStyle="short"/></td>
        <td><img class="editAttach" src="${pageContext.request.contextPath}/resources/images/icons/table_edit.png"/></td>
        <td><img class="removeAttach" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png"/></td>
        <td class="hidden"><input type="hidden" name="attachmentId" value="${attachFile.wtAttachmentId}"/></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<table id="checkListCt">
  <thead>
    <tr>
      <th>Check List Items</th>
      <th>File Name</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${wellCheckList}" var='checklist'>
      <c:set var="isMissing" value="1"/>
      <tr>
        <td>${checklist.name}</td>
        <td>
          <c:forEach var="attachment" items="${WtWell.wtAttachmentCollection}">
            <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
              <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                <c:set var="isMissing" value="0"/>
                <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" target="_blank">${attachment.filename}</a>&nbsp;
              </c:if>
            </c:forEach>
          </c:forEach>
          <c:if test="${isMissing == 1}">
            <span style="color:red"></span>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>

<c:set var="reviewNote" value="hidden"/>
<c:if test="${statusFlag.statusName eq 'UREVIEW'|| statusFlag.statusName eq 'INCOMPLETE'}">
  <c:set var="reviewNote" value=""/>
</c:if>
<div class="forReviewer ${reviewNote}">
  <c:set var="isComplete" value=""/>
  <c:set var="checkVal" value="0"/>
  <c:set var="addComment" value="disabled"/>  
  <c:set var="showAdd" value="hidden"/> 
  <c:if test="${reviewNotes.getLastReviewNoteBySectionKey(sKey).isComplete==1}">
    <c:set var="isComplete" value="checked"/>
    <c:set var="checkVal" value="1"/>
    <c:set var="bgcolor" value=""/>
  </c:if>
  <c:if test="${(statusFlag.statusName eq 'INCOMPLETE') 
                  && (reviewNotes.getLastReviewNoteBySectionKey(sKey).isComplete!=1)}">
    <c:set var="addComment" value=""/>      
    <c:set var="showAdd" value=""/>
    <c:set var="bgcolor" value="background-color: #fff;"/>
  </c:if>
  <div class="checkBoxCt">
    <input type="checkbox"
           sectionKey="${sKey}"
           transId="${wtTransId}"
           onclick="return false;"
           ${isComplete}
           value="${checkVal}"/>
    <label>Above section is completed if checked</label>
  </div>
  <fieldset class="">
    <c:set var="techNote" value=""/>
    <c:if test="${!empty(reviewNotes)}">
      <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey(sKey).note}"/>
    </c:if>
    <textarea class="techNote" 
              placeholder="Technical Comments" 
              style="${bgcolor}"
              sectionKey="${sKey}"
              transId="${wtTransId}"
              ${addComment}
              onkeyup="window.reviewer.auto_grow(this)"></textarea>
    <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
    <input class="noteBtn ${showAdd}"
           type="button"
           sectionKey="${sKey}"
           transId="${wtTransId}"
           value="Save Comment"
           onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
    <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--    <div class="historyCt hidden">
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey(sKey)}" var="tNote" >
        <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
        <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
      </c:forEach>
    </div>-->

    <c:forEach items="${reviewNotes.getReviewNotesBySectionKey(sKey)}" var="tNote" varStatus="iterator">
      <c:if test="${iterator.first}">
        <div class="lastNoteCt">
          <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left: 40px;">${tNote.note}</div>
        </div>
      </c:if>
      <c:if test="${!iterator.first}">
        <div class="historyCt hidden">
          <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left: 40px;">${tNote.note}</div>
        </div>
      </c:if>
    </c:forEach>
  </fieldset>
</div>

