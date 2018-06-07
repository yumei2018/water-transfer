<%--
    Document   : cropidlingReview
    Created on : Dec 5, 2016, 4:04:57 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="cropCt section ${ci_empty}">
  <a class="anchor" name="section-E"></a>
  <div class="header">
    E. Crop Idling and Crop Shifting
    <img class="commentimg"
         transId="${proposal.wtTransId}"
         sectionKey="E"
         src="${comment_img}"
         title="Internal Comment"
         onclick="window.reviewer.initInternalNotes(this);"/>
  </div>
  <div class="fieldRow">
    <span>Water Transfer Amount (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${cropIdling.waterTransQua}"/></b>
  </div>
  <!--  <div class="fieldRow">
      <span>Proposed Transfer Acreage by Crop Idling (Acres): </span><b class="highlight"><fmt:formatNumber type="number" value="${cropIdling.proTransferByCI}"/></span>
    </div>
    <div class="fieldRow">
      <span>Proposed Transfer Acreage by Crop Shift (Acres): </span><b class="highlight"><fmt:formatNumber type="number" value="${cropIdling.proTransferByCS}"/></span>
    </div>-->
  <div class="fieldRow">
    <span>Total Proposed Transfer Acreage: </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${cropIdling.totalTransferAcr}"/></span>
  </div>
  <div class="fieldRow">
    <span>Current-year Farm Service Agency (Acres): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${cropIdling.currentFsAgency}"/></span>
  </div>
  <div class="fieldRow">
    <span>Does this proposal include reservoir release? </span>
    <span class="highlight"><c:if test="${cropIdling.isResRelease == 1}">Yes</c:if></span>
    <span class="highlight"><c:if test="${cropIdling.isResRelease == 0}">No</c:if></span>
    </div>

    <div class="section">
      <div class="subheader">Water Transfer Amount</div>
      <table>
        <thead>
          <tr class="monthly_header">
            <th style="width:20%;">Crop: Without Transfer</th>
            <th style="width:20%;">Crop: With Transfer</th>
            <th style="width:20%;">Proposed Transfer Acreage</th>            
            <th style="width:20%;">Evapotranspiration Rate of Applied Water (ETAW)</th>
            <th style="width:20%;">Water Transfer Amount</th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="ciCroptype" items="${cropIdling.croptypeCollection}">
          <tr>
            <td class="boldfield" style="text-align: center;">${ciCroptype.cropWithoutTransfer}</td>
            <td class="boldfield" style="text-align: center;">${ciCroptype.cropWithTransfer}</td>
            <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${ciCroptype.proTransferTotal}"/></td>
            <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${ciCroptype.cropEtaw}"/></td>
            <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${ciCroptype.waterTransferAmount}"/></td>
          </tr>
        </c:forEach>  
      </tbody>
    </table>
  </div>

  <div class="section">
    <div class="subheader">Transfer Water Estimated Schedule (acre-feet)</div>
    <c:set var="ciMonthly" value="${cropIdling.wtCiMonthly}"/>
    <c:set var="mayEtaw" value="${ciMonthly.mayEtaw}"/>
    <c:set var="juneEtaw" value="${ciMonthly.juneEtaw}"/>
    <c:set var="julyEtaw" value="${ciMonthly.julyEtaw}"/>
    <c:set var="augustEtaw" value="${ciMonthly.augustEtaw}"/>
    <c:set var="septemberEtaw" value="${ciMonthly.septemberEtaw}"/>
    <c:set var="totalEtaw" value="${mayEtaw+juneEtaw+julyEtaw+augustEtaw+septemberEtaw}"/>
    <c:set var="mayTw" value="${ciMonthly.mayTw}"/>
    <c:set var="juneTw" value="${ciMonthly.juneTw}"/>
    <c:set var="julyTw" value="${ciMonthly.julyTw}"/>
    <c:set var="augustTw" value="${ciMonthly.augustTw}"/>
    <c:set var="septemberTw" value="${ciMonthly.septemberTw}"/>
    <c:set var="totalTw" value="${mayTw+juneTw+julyTw+augustTw+septemberTw}"/>
    <table>
      <thead>
        <tr class="monthly_header">
          <th style="width:150px;"></th>
          <th style="width:14%;">May</th>
          <th style="width:14%;">June</th>
          <th style="width:14%;">July</th>
          <th style="width:14%;">August</th>
          <th style="width:14%;">September</th>
          <th style="width:14%;">Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td style="text-align: center;">ETAW Pattern (%)</td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${mayEtaw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${juneEtaw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${julyEtaw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${augustEtaw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${septemberEtaw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${totalEtaw}"/></td>
        </tr>
        <tr>
          <td style="text-align: center;">Transfer Water (acre-feet)</td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${mayTw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${juneTw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${julyTw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${augustTw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${septemberTw}"/></td>
          <td class="boldfield" style="text-align: center;"><fmt:formatNumber type="number" value="${totalTw}"/></td>
        </tr>
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
        <c:forEach items="${attachmentCI}" var="attachment">
          <c:set var="wtAttachmentId">${attachment.wtAttachmentId}</c:set>
          <c:set var="createdById">${attachment.createdById}</c:set>
            <tr>
              <td class="boldfield"><a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">${attachment.filename}</a></td>
            <td class="boldfield">${attachment.readableFilesize}</td>
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
        <c:forEach var="checklist" items="${checklistCI}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td class="simple">${checklist.name}</td>
            <td style="width:30%;" class="boldfield">
              <c:forEach var="attachment" items="${attachmentCI}">
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
    </br>

    <div class="subheader">Map/Shapefile Attachments</div>
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
        <c:forEach items="${attachmentCIMap}" var="mapattach">
          <c:set var="wtAttachmentId">${mapattach.wtAttachmentId}</c:set>
          <c:set var="createdById">${mapattach.createdById}</c:set>
            <tr>
              <td class="boldfield"><a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">${mapattach.filename}</a></td>
            <td class="boldfield">${mapattach.readableFilesize}</td>
            <td class="boldfield">${mapattach.title}</td>
            <td class="boldfield">${mapattach.description}</td>
            <td class="boldfield">${mapattach.createdBy.name}</td>
            <td class="boldfield"><fmt:formatDate pattern="MM/dd/yyyy" value="${mapattach.createdDate}" /></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <table>
      <thead>
        <tr>
          <th>Check List Items</th>
          <th>File Name</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="checklist" items="${checklistCIMap}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td class='simple'>${checklist.name}</td>
            <td style="width:30%;" class="boldfield">
              <c:forEach var="attachment" items="${attachmentCIMap}">
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
      <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('E')}"/>
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
             sectionKey="E"
             transId="${proposal.wtTransId}"
             ${isComplete}
             value="${checkVal}"
             onclick="window.reviewer.initCompletedCheckbox(this);"/>
      <label>Check if review of above item is completed</label>
    </div>
    <fieldset class="">
      <textarea class="techNote"
                placeholder="Technical Comments"
                sectionKey="E"
                transId="${proposal.wtTransId}"
                val=""
                onkeyup="window.reviewer.auto_grow(this)"></textarea>
      <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
      <input class="noteBtn"
             type="button"
             sectionKey="E"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
      <!--      <div class="historyCt hidden">
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('E')}" var="tNote" >
        <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
        <div style="padding-left: 40px;">${tNote.note}</div>
      </c:forEach>
    </div>-->
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('E')}" var="tNote" varStatus="iterator">
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
           name="checkCI"
           section="CI"
           type="button" 
           transId="${proposal.wtTransId}"
           swpaoNum="${proposal.swpaoContractNum}"
           swpaoReviewer="${proposal.swpaoReviewer}"
           value="Review Completed"
           onclick="window.reviewer.checkReviewCompleted(this);" />
  
</div>
