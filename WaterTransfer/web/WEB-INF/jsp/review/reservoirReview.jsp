<%--
    Document   : reservoirReview
    Created on : Dec 5, 2016, 3:45:07 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="reservoirCt section ${rv_empty}">
  <a class="anchor" name="section-F"></a>
  <div class="header">
    F. Reservoir Release
    <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="F"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
  </div>
  <div class="fieldRow">
    <span>Dam Location: </span><b class="highlight">Latitude:${proposal.wtReservoir.locationLat}, Longitude:${proposal.wtReservoir.locationLong}</b>
  </div>
  <div class="fieldRow">
    <span>Water Transfer Amount (acre-feet): </span><b class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${proposal.wtReservoir.waterTransQua}"/></b>
  </div>
  <div class="fieldRow">
    <span>Top of allowable conservation storage (acre-feet): </span><b class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${proposal.wtReservoir.topAllowStorage}"/></b>
  </div>
  <div class="fieldRow">
    <span>End-of-Season target storage, if applicable (acre-feet): </span><b class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${proposal.wtReservoir.targetStorage}"/></b>
  </div>
  <div class="fieldRow">
    <span>Purpose of Reservoirs: </span>
      <c:forEach var="purpose" items="${proposal.wtReservoir.wtPurposeCollection}" varStatus="iterator">
        <b class="highlight">${purpose.purpose}<c:if test="${!iterator.last}">, </c:if></b>
      </c:forEach>
  </div>
  <div class="fieldRow">
    <span>Is Seller authorized to operate reservoir? </span>
    <b><c:if test="${proposal.wtReservoir.isSellerAuth==1}"><span class="highlight">Yes</span></c:if></b>
    <b><c:if test="${proposal.wtReservoir.isSellerAuth==0}"><span class="highlight">No</span></c:if></b>
  </div>
  <c:if test="${proposal.wtReservoir.isSellerAuth==0}">
    <div>
      <span>Who is authorized to operate reservoir? </span><b class="highlight">${proposal.wtReservoir.authOperator}</b>
    </div>
  </c:if>
  <div class="section">
    <div class="subheader">List any reservoirs located downstream of project reservoir or point of current return</div>
    <table>
      <thead>
        <tr>
          <th style="text-align: center;">Reservoir Name</th>
          <th>Location Latitude</th>
          <th>Location Longitude</th>
          <th>Name of Contact</th>
          <th>Phone Number</th>
          <th>Email Address</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${proposal.wtReservoir.wtRvTarstorCollection}" var="storage">
        <tr>
          <td class="boldfield">${storage.damName}</td>
          <td class="boldfield">${storage.locationLat}</td>
          <td class="boldfield">${storage.locationLong}</td>
          <td class="boldfield">${storage.operator}</td>
          <td class="boldfield">${storage.phoneNumber}</td>
          <td class="boldfield">${storage.email}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <div class="section">
    <div class="subheader">Attachments</div>
    <table>
      <thead>
        <tr>
          <th>File Name</th>
          <th>Size</th>
          <th>Title</th>
          <th>Description</th>
          <th>Uploaded By</th>
          <th>Upload Date</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${attachmentRV}" var="attachment">
        <c:set var="wtAttachmentId">${attachment.wtAttachmentId}</c:set>
        <c:set var="createdById">${attachment.createdById}</c:set>
        <tr>
          <td class="boldfield"><a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">${attachment.filename}</a></td>
          <td class="boldfield">${size.optString(wtAttachmentId)}</td>
          <td class="boldfield">${attachment.title}</td>
          <td class="boldfield">${attachment.description}</td>
          <td class="boldfield">${attachment.createdBy.name}</td>
          <td class="boldfield"><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.createdDate}" /></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <table>
      <thead>
        <tr>
          <th>Attachments Checklist</th>
          <th>File Name</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach var="checklist" items="${checklistRV}">
        <c:set var="isMissing" value="1"/>
        <tr>
          <td class="simple">${checklist.name}</td>
          <td style="width:30%;" class="boldfield">
          <c:forEach var="attachment" items="${attachmentRV}">
            <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
              <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                <c:set var="isMissing" value="0"/>
                <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">
                  ${attachment.filename}&nbsp;
                </a>
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
  </div>

  <div class="forReviewer">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="techNote" value=""/>
    <c:set var="note" value=""/>
    <c:if test="${!empty(reviewNotes)}">
      <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('F')}"/>
    </c:if>
    <c:if test="${techNote!='' && techNote.isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>
    </c:if>
    <c:if test="${techNote!='' && techNote.note != ''}">
      <c:set var="note" value="${techNote.note}"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="F"
             transId="${proposal.wtTransId}"
             ${isComplete}
             value="${checkVal}"
             onclick="window.reviewer.initCompletedCheckbox(this);"/>
      <label>Check if review of above item is completed</label>
    </div>
    <fieldset class="">
      <textarea class="techNote"
                placeholder="Technical Comments"
                sectionKey="F"
                transId="${proposal.wtTransId}"
                val=""
                onkeyup="window.reviewer.auto_grow(this)"></textarea>
      <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
      <input class="noteBtn"
             type="button"
             sectionKey="F"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('F')}" var="tNote" varStatus="iterator">
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
             
  <input class="review_button"
         name="checkRES"
         section="RES"
         type="button" 
         transId="${proposal.wtTransId}"
         swpaoNum="${proposal.swpaoContractNum}"
         swpaoReviewer="${proposal.swpaoReviewer}"
         value="Review Completed"
         onclick="window.reviewer.checkReviewCompleted(this);" />
</div>
