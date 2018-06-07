<%-- 
    Document   : reservoirPreview
    Created on : May 22, 2017, 2:24:54 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="reservoirCt ${rv_empty}">
  <h1>Reservoir Information</h1>
  <div>
    <span>Dam Location: </span><span class="highlight">Latitude:${proposal.wtReservoir.locationLat}, Longitude:${proposal.wtReservoir.locationLong}</span>
  </div>
  <div>
    <span>Water Transfer Amount (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${proposal.wtReservoir.waterTransQua}"/></span>
  </div>
  <div>
    <span>Top of allowable conservation storage (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${proposal.wtReservoir.topAllowStorage}"/></span>
  </div>
  <div>
    <span>End-of-Season target storage, if applicable (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" value="${proposal.wtReservoir.targetStorage}"/></span>
  </div>
  <div>
    <span>Purpose of Reservoirs:
      <c:forEach var="purpose" items="${proposal.wtReservoir.wtPurposeCollection}" varStatus="iterator">
        <span class="highlight">${purpose.purpose}<c:if test="${!iterator.last}">, </c:if></span>
      </c:forEach></span>
  </div>
  <div>
    <span>Is Seller authorized to operate reservoir? </span>
    <span class="highlight">
        <c:if test="${proposal.wtReservoir.isSellerAuth==1}">Yes</c:if>
        <c:if test="${proposal.wtReservoir.isSellerAuth==0}">No</c:if>
    </span>
  </div>
  <c:if test="${proposal.wtReservoir.isSellerAuth==0}">
    <div>
      <span>Who is authorized to operate reservoir? </span><span class="highlight">${proposal.wtReservoir.authOperator}</span>
    </div>
  </c:if>
  <div class="section">
    <div class="header">List any reservoirs located downstream of project reservoir or point of current return</div>
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
            <td class="highlight">${storage.damName}</td>
            <td class="highlight">${storage.locationLat}</td>
            <td class="highlight">${storage.locationLong}</td>
            <td class="highlight">${storage.operator}</td>
            <td class="highlight">${storage.phoneNumber}</td>
            <td class="highlight">${storage.email}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <div class="section">
    <div class="header">Attachments</div>
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
        <c:forEach items="${proposal.wtReservoir.wtAttachmentCollection}" var="attach">
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
        <c:forEach var="checklist" items="${RVChecklist}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td>${checklist.name}</td>
            <td>
              <c:forEach var="attachment" items="${proposal.wtReservoir.wtAttachmentCollection}">
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
