<%-- 
    Document   : cropIdlingPreview
    Created on : May 22, 2017, 2:18:30 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="cropCt section ${ci_empty}" section="ciInfo">
  <div class="section">
    <h1>Crop Idling/Shifting Information</h1>
    <div>
      <span>Water Transfer Amount (acre-feet):<b><fmt:formatNumber type="number" value="${proposal.wtCropIdling.waterTransQua}"/></b></span>
    </div>
    <div>
      <span>Proposed Transfer Acreage by Crop Idling (Acres):<b><fmt:formatNumber type="number" value="${proposal.wtCropIdling.proTransferByCI}"/></b></span>
    </div>
    <div>
      <span>Proposed Transfer Acreage by Crop Shift (Acres):<b><fmt:formatNumber type="number" value="${proposal.wtCropIdling.proTransferByCS}"/></b></span>
    </div>
    <div>
      <span>Total Proposed Acreage for Crop Transfer (Acres):<b><fmt:formatNumber type="number" value="${proposal.wtCropIdling.totalTransferAcr}"/></b></span>
    </div>
    <div>
      <span>Current-year Farm Service Agency (Acres):<b><fmt:formatNumber type="number" value="${proposal.wtCropIdling.currentFsAgency}"/></b></span>
    </div>
    <div>
      <span>Does this proposal include reservoir release? <b>
          <c:if test="${proposal.wtCropIdling.isResRelease == 1}">Yes</c:if>
          <c:if test="${proposal.wtCropIdling.isResRelease == 0}">No</c:if></b>
      </span>
    </div>
  </div>
  
  <div class="section">
    <div class="subheader">Transfer Water Estimated Schedule (acre-feet)</div>
    <c:set var="ciMonthly" value="${proposal.wtCropIdling.wtCiMonthly}"/>
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
          <th># of Items</th>
          <th>Title</th>
          <th>Description</th>
          <th>Uploaded By</th>
          <th>Upload Date</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${proposal.wtCropIdling.wtAttachmentCollection}" var="attach">
        <c:set var="wtAttachmentId">${attach.wtAttachmentId}</c:set>
        <c:set var="createdById">${attach.createdById}</c:set>
        <tr>
          <td>${attach.filename}</td>
          <td>${size.optString(wtAttachmentId)}</td>
          <td>${attach.wtChecklistCollection.size()}</td>
          <td>${attach.title}</td>
          <td>${attach.description}</td>
          <td>${size.optString(createdById)}</td>
          <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attach.createdDate}" /></td>
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
      <c:forEach var="checklist" items="${CIChecklist}">
        <c:set var="isMissing" value="1"/>
        <tr>
          <td>${checklist.name}</td>
          <td>
        <c:forEach var="attachment" items="${proposal.wtCropIdling.wtAttachmentCollection}">
          <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
            <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
              <c:set var="isMissing" value="0"/>
              ${attachment.filename}&nbsp;
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

  <div class="subheader">Map/Shapefile Attachments</div>
  <table>
    <thead>
      <tr>
        <th>File Name</th>
        <th>Size</th>
        <th># of Items</th>
        <th>Title</th>
        <th>Description</th>
        <th>Uploaded By</th>
        <th>Upload Date</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach items="${proposal.wtCropIdling.wtMapAttCollection}" var="mapattach">
      <c:set var="wtAttachmentId">${mapattach.wtAttachmentId}</c:set>
      <c:set var="createdById">${mapattach.createdById}</c:set>
      <tr>
        <td>${mapattach.filename}</td>
        <td>${size.optString(wtAttachmentId)}</td>
        <td>${mapattach.wtChecklistCollection.size()}</td>
        <td>${mapattach.title}</td>
        <td>${mapattach.description}</td>
        <td>${size.optString(createdById)}</td>
        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${mapattach.createdDate}" /></td>
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
    <c:forEach var="checklist" items="${CIMapChecklist}">
      <c:set var="isMissing" value="1"/>
      <tr>
        <td>${checklist.name}</td>
        <td>
      <c:forEach var="attachment" items="${proposal.wtCropIdling.wtMapAttCollection}">
        <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
          <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
            <c:set var="isMissing" value="0"/>
            ${attachment.filename}&nbsp;
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