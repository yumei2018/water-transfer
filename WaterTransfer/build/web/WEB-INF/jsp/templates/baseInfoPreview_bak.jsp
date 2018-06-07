<%-- 
    Document   : baseInfoPreview
    Created on : May 22, 2017, 2:30:00 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="baseCt">
  <div class="section">
    <div class="subheader">General Information</div>

    <div>
      <span>Does the seller have previous water transfer?
        <c:if test="${proposal.hasPreTrans != 1}"><b>No</b></c:if>
        <c:if test="${proposal.isShortLong == 1}"><b>Yes</b></c:if>
      </span>
    </div>
    <c:if test="${proposal.isShortLong == 1}">
      <div class="section">
        <div class="subheader">Enter the Agreements for the five most recent water transfers</div>
        <table>
          <thead>
            <tr>
              <th style="text-align: center;">SWPAO Contract No</th>
              <th>Recommendation No</th>
              <th>Type of Transfer</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${proposal.wtPreTransferCollection}" var="preTransfer">
            <tr>
              <td>${preTransfer.swpaoContractNum}</td>
              <td>${preTransfer.recomNum}</td>
              <td>
                <input type="checkbox" disabled="true" <c:if test="${preTransfer.isTypeCi==1}">checked="checked"</c:if>/>Cropland Idling            
                <input type="checkbox" disabled="true" <c:if test="${preTransfer.isTypeGw==1}">checked="checked"</c:if>/>Groundwater Substitution
                <input type="checkbox" disabled="true" <c:if test="${preTransfer.isTypeRv==1}">checked="checked"</c:if>/>Reservoir Release
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </c:if>

    <div>
      <span>Transfer Year:<b>${proposal.transYear}</b></span>
    </div>
    <div>
      <span>Short or Long Term:
        <c:if test="${proposal.isShortLong == 0}"><b>Temporary(One Year)</b></c:if>
        <c:if test="${proposal.isShortLong == 1}"><b>Long-Term</b></c:if>
      </span>
    </div>
    <div>
      <span>Proposal Description: <br/>
        <div style="padding-left:20px;font-size:11px;"><b>${proposal.transDescription}</b></div></span>
    </div>
    <div>
      <span>County of Seller:
        <c:forEach var="county" items="${proposal.wtCountyCollection}" varStatus="iterator">
          <b>${county.name}<c:if test="${!iterator.last}">, </c:if></b>
        </c:forEach></span>
    </div>
    <div>
      <span>Proposed Total Transfer Amount (acre-feet):<b><fmt:formatNumber type="number" value="${proposal.proTransQua}"/></b></span>
    </div>
    <div>
      <span>Surface Water Source/Reservoir:<b>${proposal.surWaterSource}</b></span>
    </div>
    <div>
      <span>Primary Watersheds:<b>${proposal.majorRiverAttribute}</b></span>
    </div>
    <div>
      <span>Proposed Transfer Start Date:<b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinStart}"/></b></span>
      <span>Proposed Transfer End Date:<b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinEnd}"/></b></span>
    </div>
    <div>
      <span>State Water Project Contractor (Buyer): <b>
          <c:if test="${proposal.isStateContractor == 1}">Yes<c:set var="reachShow" value="hidden"/></c:if>
          <c:if test="${proposal.isStateContractor == 0}">No<c:set var="reachShow" value=""/></c:if></b>
      </span>
    </div>

    <div><span>Facilities Requested</span>
      <ul>
        <c:forEach items="${proposal.wtTransTypeCollection}" var="type">
          <c:if test="${type.wtFuType.fuType eq 'CVP'}">
            <c:set var="cvp" value="checked='true'"/>
          </c:if>
          <c:if test="${type.wtFuType.fuType eq 'SWP'}">
            <c:set var="swp" value="checked='true'"/>
          </c:if>
          <c:if test="${type.wtFuType.fuType eq 'OTHER'}">
            <c:set var="other" value="checked='true'"/>
            <c:set var="otherD" value="${type.typeDetail}"/>
          </c:if>
          <c:if test="${type.wtFuType.fuSubType eq 'Jones'}">
            <c:set var="Jones" value="checked='true'"/>
            <c:set var="JonesV" value="${type.typeVolume}"/>
          </c:if>
          <c:if test="${type.wtFuType.fuSubType eq 'Forbearance'}">
            <c:set var="Forbearance" value="checked"/>
            <c:set var="ForbearanceV" value="${transType.typeVolume}"/>
          </c:if>
          <c:if test="${type.wtFuType.fuSubType eq 'Warren Act'}">
            <c:set var="WarrenAct" value="checked"/>
            <c:set var="WarrenActV" value="${transType.typeVolume}"/>
          </c:if>
          <c:if test="${type.wtFuType.fuSubType eq 'Banks'}">
            <c:set var="Banks" value="checked='true'"/>
            <c:set var="BanksV" value="${type.typeVolume}"/>
          </c:if>
          <c:if test="${type.wtFuType.fuSubType eq 'North Bay'}">
            <c:set var="NBay" value="checked='true'"/>
            <c:set var="NorthBayV" value="${type.typeVolume}"/>
          </c:if>
          <c:if test="${transType.wtFuType.fuType eq 'SWP'&& type.wtFuType.fuSubType eq 'SWP/CVP Intertie'}">
            <c:set var="SWPIntertie" value="checked='true'"/>
          </c:if>
          <c:if test="${transType.wtFuType.fuType eq 'CVP'&& type.wtFuType.fuSubType eq 'SWP/CVP Intertie'}">
            <c:set var="CVPIntertie" value="checked='true'"/>
          </c:if>
        </c:forEach>
        <li style="padding-left:40px;">
          <input type="checkbox" disabled="true" ${swp}/>SWP<br/>
        <c:if test="${!empty(swp)}">
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${Banks}/>Banks
          <span>Volume through Banks(acre-feet): <b>${BanksV}</b></span><br/>
          <div style="padding-left:20px;" class="${reachShow}">
            <span>Who will Provide Power: <b>${proposal.wtTransReach.powerProvider}</b></span>
          </div>
          <div style="padding-left:20px;" class="${reachShow}">
            <input type="checkbox" disabled="true" ${SWPIntertie}/>SWP/CVP Intertie<br/>
          </div>
          <div style="padding-left:20px;" class="${reachShow}">
            <span>California Aqueduct Reach:
              <c:if test="${proposal.wtTransReach.isReach1 == 1}">
                <c:set var="isReach1" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach2a == 1}">
                <c:set var="isReach2a" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach2b == 1}">
                <c:set var="isReach2b" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach3 == 1}">
                <c:set var="isReach3" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach4 == 1}">
                <c:set var="isReach4" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach5 == 1}">
                <c:set var="isReach5" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach6 == 1}">
                <c:set var="isReach6" value="checked='true'"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach7 == 1}">
                <c:set var="isReach7" value="checked='true'"/>
              </c:if>
              <input type="checkbox" disabled="true" ${isReach1}/><b>1</b>
              <input type="checkbox" disabled="true" ${isReach2a}/><b>2A</b>
              <input type="checkbox" disabled="true" ${isReach2b}/><b>2B</b>
              <input type="checkbox" disabled="true" ${isReach3}/><b>3</b>
              <input type="checkbox" disabled="true" ${isReach4}/><b>4</b>
              <input type="checkbox" disabled="true" ${isReach5}/><b>5</b>
              <input type="checkbox" disabled="true" ${isReach6}/><b>6</b>
              <input type="checkbox" disabled="true" ${isReach7}/><b>7</b>
            </span>
          </div>
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${NBay}/>North Bay
          <span>Volume through North Bay(acre-feet): <b>${NorthBayV}</b></span>
        </c:if>
        </li>
        <li class="${reachShow}">
          <input type="checkbox" disabled="true" ${cvp}/>CVP<br/>
        <c:if test="${!empty(cvp)}">
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${Jones}/>Jones
          <span>Volume through Jones(acre-feet): <b>${JonesV}</b></span><br/>
<!--          <input style="padding-left:20px;" type="checkbox" disabled="true" ${CVPIntertie}/>SWP/CVP Intertie<br/>-->
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${Forbearance}/>Forbearance
          <span>Volume through Forbearance(acre-feet): <b>${ForbearanceV}</b></span><br/>
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${WarrenAct}/>Warren Act
          <span>Volume through Warren Act(acre-feet): <b>${WarrenActV}</b></span><br/>
        </c:if>
        </li>
        <li>
          <input type="checkbox" disabled="true" ${other}/>OTHER <b>${otherD}</b>
        </li>
      </ul>
    </div>
    </br>

    <div>
      <span>Through Delta Transfer: <b>
          <c:if test="${proposal.deltaTransferInd == 1}">Yes</c:if>
          <c:if test="${proposal.deltaTransferInd == 0}">No</c:if></b>
      </span>
    </div>
    <div>
      <span>Requested Export Window:  From <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpFromDate}"/></b> To <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpToDate}"/></b></span>
    </div>
    <div>
      <span>Request for Storage Prior to Export: <b>${proposal.reqStorageExp}</b></span>
    </div>

    <div>
      <span>Environmental Regulatory Compliance:
        <c:set var="swrcbShow" value="hidden"/>
        <c:set var="ceqaShow" value="hidden"/>
        <c:set var="nepaShow" value="hidden"/>
        <c:if test="${proposal.wtTransSwrcb ne null}">
          <c:set var="swrcb" value="checked='true'"/>
          <c:set var="swrcbShow" value=""/>
        </c:if>
        <c:if test="${proposal.wtTransCeqa ne null}">
          <c:set var="ceqa" value="checked='true'"/>
          <c:set var="ceqaShow" value=""/>
        </c:if>
        <c:if test="${proposal.wtTransNepa ne null}">
          <c:set var="nepa" value="checked='true'"/>
          <c:set var="nepaShow" value=""/>
        </c:if>
        <input type="checkbox" disabled="true" ${swrcb}/><b>SWRCB</b>
        <input type="checkbox" disabled="true" ${ceqa}/><b>CEQA</b>
        <input type="checkbox" disabled="true" ${nepa}/><b>NEPA</b>
      </span>
    </div>
    <div class="${swrcbShow}">
      <span>Petition for Change Submitted to SWRCB Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbPetitionDate}"/></b></span>
      <span>SWRCB Order Issued Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbOrderDate}"/></b></span>
    </div>
    <div class="${ceqaShow}">
      <span>CEQA document submitted to OPR Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaSubmittedDate}"/></b></span>
      <span>CEQA document adopted Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaAdoptedDate}"/></b></span>
    </div>
    <div class="${nepaShow}">
      <span>NEPA document issued Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaIssuedDate}"/></b></span>
      <span>NEPA document adopted Date: <b><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaAdoptedDate}"/></b></span>
    </div>
    <div>
      <span>Comments/Special Condition: </span><br/>
      <div style="padding-left:20px;font-size:11px;"><b>${proposal.wtComm}</b></div>
    </div>
  </div>
  <div class='pagebreak'>&#160;</div>

  <div class="section">
    <div class="subheader">Water Rights Type and Transfer Information</div>
    <table>
      <thead>
        <tr>
          <th style="text-align: center;">Water Rights Type</th>
          <th>Governing Document</th>
          <th>Proposed Volume for transfer (acre-feet)</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${proposal.wtWaterRightsCollection}" var="wtRights">
        <c:if test="${wtRights.waterRightsType eq 'Application'}">
          <c:set var="type" value="Post-1914"/>
        </c:if>
        <c:if test="${wtRights.waterRightsType eq 'Statement'}">
          <c:set var="type" value="Pre-1914 Appropriative Right"/>
        </c:if>
        <c:if test="${wtRights.waterRightsType eq 'Decree'}">
          <c:set var="type" value="Adjudicated"/>
        </c:if>
        <c:if test="${wtRights.waterRightsType eq 'Contract'}">
          <c:set var="type" value="Contract"/>
        </c:if>
        <tr>
          <td>${type}</td>
          <td>${wtRights.waterRightsType} # ${wtRights.waterRightsNum}</td>
          <td>${wtRights.proposedTransVol}</td>
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
          <th># of Items</th>
          <th>Title</th>
          <th>Description</th>
          <th>Uploaded By</th>
          <th>Upload Date</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${proposal.wtAttachmentCollection}" var="attach">
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
      <c:forEach var="checklist" items="${BIChecklist}">
        <c:set var="isMissing" value="1"/>
        <tr>
          <td>${checklist.name}</td>
          <td>
        <c:forEach var="attachment" items="${proposal.wtAttachmentCollection}">
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
</div>
