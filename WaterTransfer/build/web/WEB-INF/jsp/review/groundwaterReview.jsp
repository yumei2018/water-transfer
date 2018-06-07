<%--
    Document   : groundwaterReview
    Created on : Dec 6, 2016, 10:03:03 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="review-gw" class="gwCt section ${gw_empty}">
  <div class="section" section='gwinfo'>
    <a class="anchor" name="section-G"></a>
    <div class="header">
      G. Groundwater Substitution
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="G"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>
    <div class="fieldRow">
      <span>Number of Proposed Transfer Wells: </span> <span class="highlight"><fmt:formatNumber type="number" value="${groundwater.pumpingWellsNumber}"/></span>
    </div>
<!--    <div class="fieldRow">
      <span>Number of Approved Transfer Wells: </span> <span class="highlight" id="approvedTransferNum">0</span>
    </div>-->
    <div class="fieldRow">
      <span>Number of Proposed Monitoring Wells: </span><span class="highlight"><fmt:formatNumber type="number" value="${groundwater.monitoringWellsNumber}"/></span>
    </div>
<!--    <div class="fieldRow">
      <span>Number of Approved Monitoring Wells: </span> <span class="highlight" id="approvedMonitoringNum">0</span>
    </div>-->
    <div class="fieldRow">
      <span>Total Proposed Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${groundwater.totalPumping}"/></span>
    </div>
    <div class="fieldRow">
      <span>Baseline Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${groundwater.basePumping}"/></span>
    </div>
    <div class="fieldRow">
      <span>Gross Transfer Pumping (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${groundwater.grossTransPumping}"/></span>
    </div>
    <div class="fieldRow">
      <span>Streamflow Depletion Factor<span class="asterisk">*</span> %: </span><span class="highlight">${groundwater.depletionFactor}</span>
    </div>
    <div class="fieldRow">
      <span>Streamflow Depletion (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${groundwater.streamDepletion}"/></span>
    </div>
    <div class="fieldRow">
      <span>Net Transfer Water (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${groundwater.netTransWater}"/></span>
    </div>
    <div>
      <span style="color:red;">Note: Fields marked with * must be filled out</span>
    </div>

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
            <td class="boldfield">
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
      <td class="boldfield"><fmt:formatNumber type="number" value="${totPP}"/></td>
      </tr>
      <tr>
        <td class="simple">Baseline Pumping</td>
        <c:set var="totBP" value="0"/>
        <c:forEach var="i" begin="1" end="12">
          <td class="boldfield">
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
      <td class="boldfield"><fmt:formatNumber type="number" value="${totBP}"/></td>
      </tr>
      <tr>
        <td class="simple">Gross Transfer Pumping</td>
        <c:set var="totTP" value="0"/>
        <c:forEach var="i" begin="1" end="12">
          <td class="boldfield">
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
      <td class="boldfield"><fmt:formatNumber type="number" value="${totTP}"/></td>
      </tr>
      <tr>
        <td class="simple">Depletions</td>
        <c:set var="totD" value="0"/>
        <c:forEach var="i" begin="1" end="12">
          <td class="boldfield">
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
      <td class="boldfield"><fmt:formatNumber type="number" value="${totD}"/></td>
      </tr>
      <tr>
        <td class="simple">Net Transfer Water</td>
        <c:set var="totTW" value="0"/>
        <c:forEach var="i" begin="1" end="12">
          <td class="boldfield">
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
      <td class="boldfield"><fmt:formatNumber type="number" value="${totTW}"/></td>
      </tr>
      </tbody>
    </table>

    <div class="subheader">Attachments</div>
    <table id="attachment-table">
      <tr class="attach_header">
        <th>Attached File</th>
        <th>Size</th>
        <th>Title</th>
        <th>Description</th>
        <th>Uploaded By</th>
        <th>Upload Date</th>
      </tr>
      <c:forEach var="attachment" items="${attachmentGW}">
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
    </table>

    <table>
      <thead>
        <tr>
          <th>Attachments Checklist</th>
          <th>File Name</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="checklist" items="${checklistGW}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td class="simple">${checklist.name}</td>
            <td style="width:30%;" class="boldfield">
              <c:forEach var="attachment" items="${attachmentGW}">
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
    
    <div class="forReviewer">
      <c:set var="isComplete" value=""/>
      <c:set var="checkVal" value="0"/>
      <c:set var="techNote" value=""/>
      <c:set var="note" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('G')}"/>
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
               sectionKey="G"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="G"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="G"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--        <div class="historyCt hidden">
          <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('G')}" var="tNote" >
            <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
            <div style="padding-left: 40px;">${tNote.note}</div>
          </c:forEach>
        </div>-->
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('G')}" var="tNote" varStatus="iterator">
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
           name="checkGW"
           section="GW"
           type="button" 
           transId="${proposal.wtTransId}"
           swpaoNum="${proposal.swpaoContractNum}"
           swpaoReviewer="${proposal.swpaoReviewer}"
           value="Review Completed"
           onclick="window.reviewer.checkReviewCompleted(this);" />
  </div>

  <c:if test="${fn:length(groundwater.wtWellCollection) gt 0}">
    <c:set var="checked_img" value="${pageContext.request.contextPath}/resources/images/icons/checkbox.png"/>
    <div class="section">
      <a class="anchor" name="section-H"></a>
      <div class="header wellListHeader" sectionKey="H">
        H. List of Associated Wells
        <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="H"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
      </div>
      <table id="associateWellCt">
        <thead>
          <tr>
            <th style="width:80px;">Master Side Code</th>
            <th>State Well Number</th>
            <th>Local Well Designation</th>
            <th>Well Type</th>
<!--            <th>Power Source</th>-->
<!--            <th style="width:100px;">Data Collection Agency</th>-->
            <th>Review Complete</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="list" items="${groundwater.wtWellCollection}" varStatus="count">
            <c:set var="sKey">${list.wtWellNum}</c:set>
            <c:set var="rowNum">H${count.index+1}</c:set>
            <c:set var="wellType" value=""/>
            <c:if test="${list.wellTransfer == 1}">
              <c:set var="wellType" value="Transfer"/>
            </c:if>
            <c:if test="${list.wellMonitoring == 1}">
              <c:set var="wellType" value="Monitoring"/>
            </c:if>
            <tr onclick="window.document.location='#section-${rowNum}';" style="cursor: pointer;">
              <td class="boldfield" fieldName="wtWellNum"><a href="#section-${rowNum}">${list.wtWellNum}</a></td>
<!--              <td class="boldfield" fieldName="wtWellNum"><a href="#${list.wtWellNum}">${list.wtWellNum}</a></td>-->
              <td class="boldfield" fieldName="stateWellNum">${list.stateWellNum}</td>
              <td class="boldfield" fieldName="localWellId">${list.localWellId}</td>
              <td class="boldfield wellType" fieldName="wellType">${wellType}</td>
<!--              <td class="boldfield" fieldName="localWellId">${list.powerSource}</td>-->
<!--              <td class="boldfield" fieldName="localWellId">${list.dataCollectAgency}</td>-->
<!--              <td class="boldfield" fieldName="wellMonitoring"><c:choose><c:when test="${list.wellMonitoring == 1}">Yes</c:when><c:otherwise>No</c:otherwise></c:choose></td>-->
<!--              <td><img id="${list.wtWellNum}" class="checkboximg hidden" src="${checked_img}"></td>-->
              <td class="boldfield isApproved" id="${list.wtWellNum}" fieldName="completed">No</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!--<div class="header">Each Associated Well Information</div>-->
    <c:forEach var="well" items="${groundwater.wtWellCollection}" varStatus="count">
      <c:set var="sKey">${well.wtWellNum}</c:set>
      <c:set var="rowNum">H${count.index+1}</c:set>
      <div class="section">
        <a class="anchor" name="section-${rowNum}"></a>
        <a class="anchor" name="${well.wtWellNum}"></a>
        <div class="header wellHeader" sectionKey="${sKey}" rowNumber="${rowNum}" wellNum="${well.wtWellNum}">
          ${rowNum}. Well ${well.wtWellNum}
          <img class="commentimg"
               transId="${proposal.wtTransId}"
               sectionKey="${sKey}"
               rowNumber="${rowNum}"
               src="${comment_img}"
               title="Internal Comment"
               onclick="window.reviewer.initInternalNotes(this);"/>
        </div>
        <div class="hidden" section='wellattachment_${count.index}'></div>
        <c:set var="transfer" value="hidden" />
        <c:set var="monitoring" value="hidden" />
        <c:if test="${well.wellTransfer == 1}">
          <c:set var="transfer" value="" />
        </c:if>
        <c:if test="${well.wellMonitoring == 1}">
          <c:set var="monitoring" value="" />
        </c:if>
        <div style="margin:10px;">
          <span style="padding-right:20px;">Site Code: </span><span class="highlight">${well.wtWellNum}</span>
          <span style="padding-right:20px;">State Well Number: </span><span class="highlight">${well.stateWellNum}</span>
          <span>Local Well Designation: </span><span class="highlight">${well.localWellId}</span>
        </div>
        <div style="margin:10px 10px 10px 10px;">
          <span style="padding-right:20px;">Well Type: </span><span class="highlight">
            <c:if test="${well.wellTransfer == 1}">Transfer Well</c:if>
            <c:if test="${well.wellMonitoring == 1&&well.wellTransfer == 1}">/</c:if>
            <c:if test="${well.wellMonitoring == 1}">Monitoring Well</c:if></span>
        </div>
        <div style="margin:10px 10px 20px 10px;" class="${monitoring}">
          <span style="padding-right:20px;">Data Collection Agency: </span><span class="highlight">${well.dataCollectAgency}</span>
        </div>
        <div style="margin:10px 10px 20px 10px;" class="${transfer}">
          <span style="padding-right:20px;">Meter Last Calibrated Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${well.lastCalibrateDate}" /></span>
          <span style="padding-right:20px;">Meter Make: </span><span class="highlight">${well.meterMake}</span>
          <span style="padding-right:20px;">Meter Model: </span><span class="highlight">${well.meterModel}</span>
        </div>
        <div style="margin:10px 10px 20px 10px;" class="${transfer}">
          <span style="padding-right:20px;">Date of Last Flow Meter Installation Certificate: </span>
          <span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${well.meterLastInstall}" /></span>
          <span style="padding-right:20px;">Meter Serial Number: </span><span class="highlight">${well.serialNum}</span>
        </div>
        <div style="margin:10px 10px 20px 10px;" class="${transfer}">
          <span style="padding-right:20px;">Power Source: </span><span class="highlight">${well.powerSource}</span>
          <span style="padding-right:20px;">Well Capacity: </span><span class="highlight">${well.wellCapacity}</span><span class="highlight"> ${well.meterUnits}</span>
        </div>
        <table class="associated_wells">
          <thead>
            <tr>
              <th>File Name</th>
              <th>Size</th>
              <th>Title</th>
              <th>Description</th>
              <th>Uploaded By</th>
              <th>Uploaded Date</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${well.wtAttachmentCollection}" var="attach" varStatus="iterator">
            <c:set var="wtAttachmentId">${attach.wtAttachmentId}</c:set>
            <c:set var="createdById">${attach.createdById}</c:set>
            <tr>
              <td class="boldfield"><a href="${pageContext.request.contextPath}/attachment/view/${attach.wtAttachmentId}" onClick="" target="_blank">${attach.filename}</a></td>
              <td class="boldfield">${attach.readableFilesize}</td>
              <td class="boldfield">${attach.title}</td>
              <td class="boldfield">${attach.description}</td>
              <td class="boldfield">${attach.createdBy.name}</td>
              <td class="boldfield"><fmt:formatDate pattern="MM/dd/yyyy" value="${attach.createdDate}" /></td>
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
            <c:set var="wellCheckList" value="" />
            <c:if test="${well.wellTransfer == 1}">
              <c:set var="wellCheckList" value="${checklistWellTrans}" />
            </c:if>
            <c:if test="${well.wellMonitoring == 1}">
              <c:set var="wellCheckList" value="${checklistWellMonit}" />
            </c:if>
            <c:forEach var="cl" items="${wellCheckList}">
              <c:set var="isMissing" value="1"/>
              <tr>
                <td>${cl.name}</td>
                <td class="boldfield">
                  <c:forEach items="${well.wtAttachmentCollection}" var="attach" >
                    <c:forEach items="${attach.wtChecklistCollection}" var="checklist" >
                      <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                        <c:set var="isMissing" value="0"/>
                        <a href="${pageContext.request.contextPath}/attachment/view/${attach.wtAttachmentId}" onClick="" target="_blank">
                          ${attach.filename}&nbsp;
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

        <div class="forReviewer">
          <c:set var="isComplete" value=""/>
          <c:set var="checkVal" value="0"/>
          <c:set var="techNote" value=""/>
          <c:set var="note" value=""/>
          <c:if test="${!empty(reviewNotes)}">
            <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey(sKey)}"/>
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
                   sectionKey=${sKey}
                   transId="${proposal.wtTransId}"
                   wellNum="${well.wtWellNum}"
                   ${isComplete}
                   value="${checkVal}"
                   onclick="window.reviewer.initCompletedCheckbox(this);"/>
            <label>Check if review of above item is completed</label>
          </div>
          <fieldset class="">
            <textarea class="techNote"
                      placeholder="Technical Comments"
                      sectionKey=${sKey}
                      transId="${proposal.wtTransId}"
                      val=""
                      onkeyup="window.reviewer.auto_grow(this)"></textarea>
            <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
            <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
            <input class="noteBtn"
                   type="button"
                   sectionKey=${sKey}
                   transId="${proposal.wtTransId}"
                   value="Save Comment"
                   onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
            <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--            <div class="historyCt hidden">
              <c:forEach items="${reviewNotes.getReviewNotesBySectionKey(sKey)}" var="tNote" >
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>
              </c:forEach>
            </div>-->
            <c:forEach items="${reviewNotes.getReviewNotesBySectionKey(sKey)}" var="tNote" varStatus="iterator">
              <div class="lastNoteCt">
              <c:if test="${iterator.first}">                
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>                
              </c:if>
              </div>
              <div class="historyCt hidden">
              <c:if test="${!iterator.first}">                
                <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
                <div style="padding-left: 40px;">${tNote.note}</div>                
              </c:if>
              </div>
            </c:forEach>
          </fieldset>
        </div>
      </div>
    </c:forEach>
  </c:if>
</div>

