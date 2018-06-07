<%-- 
    Document   : groundwaterPreview
    Created on : Sep 22, 2017, 8:57:48 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="initPagebreak" value="hidden"/>
<c:if test='${!empty(linebreak.optString("gwinfo"))}'>
  <c:set var="initPagebreak" value='${linebreak.optString("gwinfo")}'/>
</c:if>
<c:set var="groundwater" value=""/>
<c:if test="${proposal.wtGroundwater != null}">
  <c:set var="groundwater" value="${proposal.wtGroundwater}"/>
</c:if>
<div class="section groundwaterCt ${gw_empty}" section='gwinfo'>
  
  <div class="section">
    <h1>Groundwater Information</h1>
    <div>
      <span>Number of Proposed Transfer Wells: <b><fmt:formatNumber type="number" value="${groundwater.pumpingWellsNumber}"/></b></span>
    </div>
    <div>
      <span>Number of Proposed Monitoring Wells: <b><fmt:formatNumber type="number" value="${groundwater.monitoringWellsNumber}"/></b></span>
    </div>
    <div>
      <span>Total Proposed Pumping (acre-feet): <b><fmt:formatNumber type="number" value="${groundwater.totalPumping}"/></b></span>
    </div>
    <div>
      <span>Baseline Pumping (acre-feet): <b><fmt:formatNumber type="number" value="${groundwater.basePumping}"/></b></span>
    </div>
    <div>
      <span>Gross Transfer Pumping (acre-feet): <b><fmt:formatNumber type="number" value="${groundwater.grossTransPumping}"/></b></span>
    </div>
    <div>
      <span>Streamflow Depletion Factor %: <b>${groundwater.depletionFactor}</b></span>
    </div>
    <div>
      <span>Net Transfer Water (acre-feet): <b><fmt:formatNumber type="number" value="${groundwater.netTransWater}"/></b></span>
    </div>
  </div>

  <div class="section">
    <div class="subheader">Estimated Monthly Total Groundwater Pumping (acre-feet)</div>
    <table id="gw-monthly-table">
      <thead>
        <tr style="width:400px;">
          <th style="width:100px;"></th>
          <th>Jan</th>
          <th>Feb</th>
          <th>Mar</th>
          <th>Apr</th>
          <th>May</th>
          <th>June</th>
          <th>July</th>
          <th>Aug</th>
          <th>Sep</th>
          <th>Oct</th>
          <th>Nov</th>
          <th>Dec</th>
          <th>Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Proposed Pumping</td>
          <c:set var="totPP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="proposedPumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="proposedPumping" value="${gwMonthly.proposedPumping}"/>
                  <c:set var="totPP" value="${totPP + proposedPumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${proposedPumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totPP}"/></td>
        </tr>
        <tr>
          <td>Baseline Pumping</td>
          <c:set var="totBP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="baselinePumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="baselinePumping" value="${gwMonthly.baselinePumping}"/>
                  <c:set var="totBP" value="${totBP + baselinePumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${baselinePumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totBP}"/></td>
        </tr>
        <tr>
          <td>Gross Transfer Pumping</td>
          <c:set var="totTP" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="grossTransPumping" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="grossTransPumping" value="${gwMonthly.grossTransPumping}"/>
                  <c:set var="totTP" value="${totTP + grossTransPumping}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${grossTransPumping}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totTP}"/></td>
        </tr>
        <tr>
          <td>Depletions</td>
          <c:set var="totD" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="streamDepletion" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="streamDepletion" value="${gwMonthly.streamDepletion}"/>
                  <c:set var="totD" value="${totD + streamDepletion}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${streamDepletion}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totD}"/></td>
        </tr>
        <tr>
          <td>Net Transfer Water</td>
          <c:set var="totTW" value="0"/>
          <c:forEach var="i" begin="1" end="12">
            <td>
              <c:set var="netTransWater" value=""/>
              <c:forEach var="gwMonthly" items="${groundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="netTransWater" value="${gwMonthly.netTransWater}"/>
                  <c:set var="totTW" value="${totTW + netTransWater}"/>
                </c:if>
              </c:forEach>
              <fmt:formatNumber type="number" value="${netTransWater}"/>
            </td>
          </c:forEach>
          <td><fmt:formatNumber type="number" value="${totTW}"/></td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class='pagebreak'>&#160;</div>

  <div class="section" section='gwchecklist'>
    <div class="subheader">Attachments</div>
    <table id="attachment-table">
      <tr class="attach_header">
        <th>Attached File</th>
        <th>Size</th>
        <th># of Items</th>
        <th>Title</th>
        <th>Description</th>
        <th>Uploaded By</th>
        <th>Upload Date</th>
      </tr>
      <c:forEach var="attachment" items="${groundwater.wtAttachmentCollection}">
        <c:set var="wtAttachmentId">${attachment.wtAttachmentId}</c:set>
        <c:set var="createdById">${attachment.createdById}</c:set>
        <tr>
          <td>${attachment.filename}</td>
          <td>${size.optString(wtAttachmentId)}</td>
          <td>${attachment.wtChecklistCollection.size()}</td>
          <td>${attachment.title}</td>
          <td>${attachment.description}</td>
          <td>${size.optString(createdById)}</td>
          <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.createdDate}" /></td>
        </tr>
      </c:forEach>
    </table>

    <table>
      <thead>
        <tr>
          <th style="width:250px;">Document Check List</th>
          <th>File Name</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="checklist" items="${GWChecklist}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td>${checklist.name}</td>
            <td>
              <c:forEach var="attachment" items="${groundwater.wtAttachmentCollection}">
                <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
                  <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                    <c:set var="isMissing" value="0"/>
                    <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">
                      ${attachment.filename}&#160;
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
  <div class='pagebreak'>&#160;</div>

  <c:if test="${fn:length(groundwater.wtWellCollection) gt 0}">
    <div class="section">
      <div class="subheader">List of Associated Wells</div>
      <table id="associateWellCt">
        <thead>
          <tr>
            <th>Master Side Code</th>
            <th>State Well Number</th>
            <th>Local Well Designation</th>
            <th>Transfer Well?</th>
            <th>Monitoring Well?</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="list" items="${groundwater.wtWellCollection}">
            <tr>
              <td fieldName="wtWellNum">${list.wtWellNum}</td>
              <td fieldName="stateWellNum">${list.stateWellNum}</td>
              <td fieldName="localWellId">${list.localWellId}</td>
              <td fieldName="wellTransfer"><c:choose><c:when test="${list.wellTransfer == 1}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
              <td fieldName="wellMonitoring"><c:choose><c:when test="${list.wellMonitoring == 1}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>
                </tr>
          </c:forEach>
        </tbody>
      </table>
      <div style="padding-top:5px;"><img src="https://maps.googleapis.com/maps/api/staticmap?center=${mapView.getString('center')}&size=400x300&maptype=hybrid&markers=${mapView.getString('points')}" height='300' width='400' /></div>

      <!--<div class="header">Each Associated Well Information</div>-->
      <c:forEach var="well" items="${groundwater.wtWellCollection}" varStatus="count">
        <div class="hidden" section='wellattachment_${count.index}'></div>
        <c:set var="welllinebreak">wellattachment_${count.index}</c:set>
        <div class="pagebreak">&#160;</div>
        <c:set var="hide" value="hidden" />
        <c:if test="${well.wellTransfer == 1}">
          <c:set var="hide" value="" />
        </c:if>
        <div style="margin:10px;">
          <span style="padding-right:60px;">Site Id: <b>${well.wtWellNum}</b></span>
          <span style="padding-right:60px;">State Well Number: <b>${well.stateWellNum}</b></span>
          <span>Local Well Name: <b></b></span>
        </div>
        <div style="margin:10px 10px 10px 10px;">
          <span style="padding-right:60px;">Well Type:<b>
              <c:if test="${well.wellTransfer == 1}">Transfer Well</c:if>
              <c:if test="${well.wellMonitoring == 1&&well.wellTransfer == 1}">/</c:if>
              <c:if test="${well.wellMonitoring == 1}">Monitoring Well</c:if></b>
            </span>
          </div>
          <div style="margin:10px 10px 20px 10px;">
            <span class="${hide}" style="padding-right:60px;">Meter Last Calibrated (Date): <b><fmt:formatDate pattern="MM/dd/yyyy" value="${well.lastCalibrateDate}" /></b></span>
          <span class="${hide}" style="padding-right:60px;">Meter Make: <b>${well.meterMake}</b></span>
          <span class="${hide}" style="padding-right:60px;">Meter Model: <b>${well.meterModel}</b></span>
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${hide}" style="padding-right:60px;">
            Date of Last Flow Meter Installation Certificate:
            <b><fmt:formatDate pattern="MM/dd/yyyy" value="${well.meterLastInstall}" /></b></span>
        </div>
        <div style="margin:10px 10px 20px 10px;">
          <span class="${hide}" style="padding-right:60px;">Attribute Year: <b>${well.attributeYear}</b></span>
          <span class="${hide}" style="padding-right:60px;">Power Source: <b>${well.powerSource}</b></span>
          <span class="${hide}" style="padding-right:60px;">Well Capacity: <b>${well.wellCapacity}</b> ${well.meterUnits}</span>
        </div>
        <table class="associated_wells">
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
            <c:forEach items="${well.wtAttachmentCollection}" var="attach" >
              <c:set var="wtAttachmentId">${attach.wtAttachmentId}</c:set>
              <c:set var="createdById">${attach.createdById}</c:set>
              <tr>
                <td>${attach.filename}</td>
                <td>${size.optString(wtAttachmentId)}</td>
                <td>${attach.title}</td>
                <td>${attach.description}</td>
                <td>${size.optString(createdById)}</td>
                <td><fmt:formatDate pattern="MM/dd/yyyy" value="${attach.modifiedDate}" /></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <table style="margin-top:20px;width:500px;">
          <thead>
            <tr>
              <th>Check List Items</th>
              <th>File Name</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="cl" items="${size.optJSONArray(well.wtWellNum).getArrayList()}">
              <c:set var="isMissing" value="1"/>
              <tr>
                <td>${cl.optString('name')}</td>
                <td>
                  <c:forEach items="${well.wtAttachmentCollection}" var="attach" >
                    <c:forEach items="${attach.wtChecklistCollection}" var="checklist" >
                      <c:if test="${(cl.optString('wtChecklistId')) == checklist.wtChecklistId}">
                        <c:set var="isMissing" value="0"/>
                        <a href="${pageContext.request.contextPath}/attachment/view/${attach.wtAttachmentId}" onClick="" target="_blank">
                          ${attach.filename}&#160;
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
      </c:forEach>
    </div>
  </c:if>
</div>

