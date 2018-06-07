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
        <c:if test="${proposal.hasPreTrans != 1}"><span class="highlight">No</span></c:if>
        <c:if test="${proposal.hasPreTrans == 1}"><span class="highlight">Yes</span></c:if>
      </span>
    </div>
    <c:if test="${proposal.hasPreTrans == 1}">
      <div class="section">
        <div class="highlight">Agreements for the five most recent water transfers</div>
        <table>
          <thead>
            <tr>
              <th style="text-align: center;">SWPAO Contract No</th>
              <th style="text-align: center;">Recommendation No</th>
              <th style="text-align: center;">Type of Transfer</th>
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
      <span>Transfer Year:<span class="highlight">${proposal.transYear}</span></span>
    </div>
    <div>
      <span>Duration of water transfer:
        <c:if test="${proposal.isShortLong == 0}"><span class="highlight">Temporary(One Year)</span></c:if>
        <c:if test="${proposal.isShortLong == 1}"><span class="highlight">Long-Term</span></c:if>
      </span>
    </div>
    <div>
      <span>Proposal Description: <br/>
        <div style="padding-left:20px;font-size:11px;"><span class="highlight">${proposal.transDescription}</span></div></span>
    </div>
    <div>
      <span>County of Seller:
        <c:forEach var="county" items="${proposal.wtCountyCollection}" varStatus="iterator">
          <span class="highlight">${county.name}<c:if test="${!iterator.last}">, </c:if></span>
        </c:forEach></span>
    </div>
    <div>
      <span>Proposed Total Transfer Amount (acre-feet):<span class="highlight"><fmt:formatNumber type="number" value="${proposal.proTransQua}"/></span></span>
    </div>
    <div>
      <span>Surface Water Source/Reservoir:</span><span class="highlight">${proposal.surWaterSource}</span>
    </div>
    <div>
      <span>Primary Watersheds:</span><span class="highlight">${proposal.majorRiverAttribute}</span>
    </div>
    <div>
      <span>Proposed Transfer Start Date:</span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinStart}"/></span>
      <span>Proposed Transfer End Date:</span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinEnd}"/></span>
    </div>
    
    <div>
      <span>State Water Project Contractor (Buyer):
          <c:if test="${proposal.isStateContractor == 1}"><span class="highlight">Yes</span><c:set var="reachShow" value="hidden"/></c:if>
          <c:if test="${proposal.isStateContractor == 0}"><span class="highlight">No</soan><c:set var="reachShow" value=""/></c:if>
      </span>
    </div>
    <div><span>Facilities Requested</span>
      
        <c:set var="otherD" value=""/>
        <c:set var="otherV" value=""/>
        <c:set var="JonesV" value=""/>
        <c:set var="NorthBayV" value=""/>
        <c:set var="ForbearanceV" value=""/>
        <c:set var="BanksV" value=""/>
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
            <c:set var="otherV" value="${type.typeVolume}"/>
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
      <ul style="padding-left:40px;">
        <li>
          <input type="checkbox" disabled="true" ${swp}/>SWP<br/>
        <c:if test="${!empty(swp)}">
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${Banks}/>Banks
          <span>Volume through Banks(acre-feet): </span><span class="highlight">${BanksV}</span><br/>
          <div style="padding-left:20px;" class="${reachShow}">
            <span>Who will Provide Power: </span><span class="highlight">${proposal.wtTransReach.powerProvider}</span>
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
              <input type="checkbox" disabled="true" ${isReach1}/><span class="highlight">1</span>
              <input type="checkbox" disabled="true" ${isReach2a}/><span class="highlight">2A</span>
              <input type="checkbox" disabled="true" ${isReach2b}/><span class="highlight">2B</span>
              <input type="checkbox" disabled="true" ${isReach3}/><span class="highlight">3</span>
              <input type="checkbox" disabled="true" ${isReach4}/><span class="highlight">4</span>
              <input type="checkbox" disabled="true" ${isReach5}/><span class="highlight">5</span>
              <input type="checkbox" disabled="true" ${isReach6}/><span class="highlight">6</span>
              <input type="checkbox" disabled="true" ${isReach7}/><span class="highlight">7</span>
            </span>
          </div>
          <input style="padding-left:10px;" type="checkbox" disabled="true" ${NBay}/>North Bay
          <span>Volume through North Bay(acre-feet): </span><span class="highlight">${NorthBayV}</span>
        </c:if>
        </li>
      </ul>
      <ul style="padding-left:40px;">
        <li class="${reachShow}">
          <input type="checkbox" disabled="true" ${cvp}/>CVP<br/>
          <c:if test="${!empty(cvp)}">
            <input style="padding-left:10px;" type="checkbox" disabled="true" ${Jones}/>Jones
            <span>Volume through Jones(acre-feet): </span><span class="highlight">${JonesV}</span><br/>
  <!--          <input style="padding-left:20px;" type="checkbox" disabled="true" ${CVPIntertie}/>SWP/CVP Intertie<br/>-->
            <input style="padding-left:10px;" type="checkbox" disabled="true" ${Forbearance}/>Forbearance
            <span>Volume through Forbearance(acre-feet): </span><span class="highlight">${ForbearanceV}</span><br/>
            <input style="padding-left:10px;" type="checkbox" disabled="true" ${WarrenAct}/>Warren Act
            <span>Volume through Warren Act(acre-feet): </span><span class="highlight">${WarrenActV}</span><br/>
          </c:if>
        </li>
      </ul>
      <ul style="padding-left:40px;">
        <li>
          <input type="checkbox" disabled="true" ${other}/>OTHER <span class="highlight">${otherD}</span>
          <span>Volume (acre-feet): </span><span class="highlight">${otherV}</span><br/>
        </li>
      </ul>
    </div>
    </br>

    <div>
      <span>Through Delta Transfer: 
          <c:if test="${proposal.deltaTransferInd == 1}"><span class="highlight">Yes</span></c:if>
          <c:if test="${proposal.deltaTransferInd == 0}"><span class="highlight">No</span></c:if>
      </span>
    </div>
    <div>
      <span>Requested Export Window:  From <span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpFromDate}"/></span> To <span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpToDate}"/></span></span>
    </div>
    <div>
      <span>Request for Storage Prior to Export: </span><span class="highlight">${proposal.reqStorageExp}</span>
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
        <input type="checkbox" disabled="true" ${swrcb}/><span class="highlight">SWRCB</span>
        <input type="checkbox" disabled="true" ${ceqa}/><span class="highlight">CEQA</span>
        <input type="checkbox" disabled="true" ${nepa}/><span class="highlight">NEPA</span>
      </span>
    </div>
    <div class="${swrcbShow}">
      <span>Petition for Change Submitted to SWRCB Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbPetitionDate}"/></span>
      <span>SWRCB Order Issued Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbOrderDate}"/></span>
    </div>
    <div class="${ceqaShow}">
      <span>CEQA document submitted to OPR Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaSubmittedDate}"/></span>
      <span>SCH#: </span><span class="highlight">${proposal.wtTransCeqa.schNum}</span>
      <span>CEQA document adopted Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaAdoptedDate}"/></span>
    </div>
    <div class="${nepaShow}">
      <span>NEPA document issued Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaIssuedDate}"/></span>
      <span>NEPA#: </span><span class="highlight">${proposal.wtTransNepa.nepaNum}</span>
      <span>NEPA document adopted Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaAdoptedDate}"/></span>
    </div>
    <div>
      <span>Comments: </span><br/>
      <div style="padding-left:20px;font-size:11px;"><span class="highlight">${proposal.wtComm}</span></div>
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
