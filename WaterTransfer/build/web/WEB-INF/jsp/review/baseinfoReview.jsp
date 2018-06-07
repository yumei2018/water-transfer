<%--
    Document   : baseinfoReview
    Created on : Dec 19, 2016, 4:27:26 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="baseCt">
  <div class="section">
    <a class="anchor" name="section-B"></a>
    <div class="header">
      B. General Information
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="B"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>
    <div class="fieldRow">
      <span>Does the seller have previous water transfers?<span class="asterisk">*</span></span>
      <c:if test="${proposal.hasPreTrans != 1}"><span class="highlight">No</span></c:if>
      <c:if test="${proposal.hasPreTrans == 1}"><span class="highlight">Yes</span></c:if>
    </div>
    <c:if test="${proposal.hasPreTrans == 1}">
      <div class="section">
        <span>Enter the Agreements for the five most recent water transfers</span>
        <table>
          <thead>
            <tr>
              <th style="text-align: center; width: 130px;">SWPAO Contract No</th>
              <th style="text-align: center;">Reclamation No</th>
              <th style="text-align: center; width:400px;">Type of Transfer</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${proposal.wtPreTransferCollection}" var="preTransfer">
              <tr>
                <td class="boldfield">${preTransfer.swpaoContractNum}</td>
                <td class="boldfield">${preTransfer.recomNum}</td>
                <td class="boldfield">
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

    <div class="fieldRow">
      <span>Transfer Year<span class="asterisk">*</span>: </span><span class="highlight">${proposal.transYear}</span>
    </div>
    <div class="fieldRow">
      <span>Duration of water transfer: </span>
      <c:if test="${proposal.isShortLong == 0}"><span class="highlight">Temporary(One Year)</span></c:if>
      <c:if test="${proposal.isShortLong == 1}"><span class="highlight">Long-Term</span></c:if>
      </div>
      <div class="fieldRow">
        <span>Proposal Description: </span>
        <span class="highlight" style="padding-left:10px;font-size:16px;">${proposal.transDescription}</span>
    </div>
    <div class="fieldRow">
      <span>County of Seller: </span>
      <c:forEach var="county" items="${proposal.wtCountyCollection}" varStatus="iterator">
        <span class="highlight">${county.name}<c:if test="${!iterator.last}">, </c:if></span>
      </c:forEach>
    </div>
    <div class="fieldRow">
      <span>Proposed Total Transfer Amount (acre-feet)<span class="asterisk">*</span>: </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${proposal.proTransQua}"/></span>
    </div>
<!--    <div class="fieldRow">
      <span>Proposed Cost of Net Water per AF ($$/AF): </span><span class="highlight"><fmt:formatNumber value="${proposal.proUnitCost}" type="currency"/></span>
    </div>
    <div class="fieldRow">
      <span>Proposed Maximum Amount for Seller ($$): </span><span class="highlight"><fmt:formatNumber type="currency" value="${proposal.proAgreePaid}"/></span>
    </div>-->
    <div class="fieldRow">
      <span>Surface Water Source/Reservoir<span class="asterisk">*</span>: </span><span class="highlight">${proposal.surWaterSource}</span>
    </div>
    <div class="fieldRow">
      <span>Primary Watersheds<span class="asterisk">*</span>: </span><span class="highlight">${proposal.majorRiverAttribute}</span>
    </div>
    <div class="fieldRow">
      <span>Proposed Transfer Start Date<span class="asterisk">*</span>: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinStart}"/></span>
      <span>Proposed Transfer End Date<span class="asterisk">*</span>: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.transWinEnd}"/></span>
    </div>
    <div class="fieldRow">
      <span>State Water Project Contractor (Buyer)<span class="asterisk">*</span>: </span><span class="highlight">
        <c:if test="${proposal.isStateContractor == 1}">Yes<c:set var="reachShow" value="hidden"/></c:if>
        <c:if test="${proposal.isStateContractor == 0}">No<c:set var="reachShow" value=""/></c:if></span>
      </div>

      <div class="fieldRow"><span>Facilities Requested<span class="asterisk">*</span>: </span>
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
        <li style="padding-left:120px;">
          <input type="checkbox" onclick="return false;" ${swp}/><span class='checkboxLabel'>SWP</span><br/>
          <c:if test="${!empty(swp)}">
            <div style="padding-left:20px;">
              <input type="checkbox" onclick="return false;" ${Banks}/><span class='checkboxLabel'>Banks</span>
                <span>Volume through Banks(acre-feet): </span><span class="highlight">${BanksV}</span><br/>
            </div>
            <div style="padding-left:40px;" class="${reachShow}">
              <span>Who will Provide Power: </span><span class="highlight">${proposal.wtTransReach.powerProvider}</span>
            </div>
            <div style="padding-left:40px;" class="${reachShow}">
              <input type="checkbox" onclick="return false;" ${SWPIntertie}/><span class='checkboxLabel'>SWP/CVP Intertie</span><br/>
            </div>
            <div style="padding-left:40px;" class="${reachShow}">
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
                <span class="checkboxLabel">
                <input type="checkbox" onclick="return false;" ${isReach1}/>1
                <input type="checkbox" onclick="return false;" ${isReach2a}/>2A
                <input type="checkbox" onclick="return false;" ${isReach2b}/>2B
                <input type="checkbox" onclick="return false;" ${isReach3}/>3
                <input type="checkbox" onclick="return false;" ${isReach4}/>4
                <input type="checkbox" onclick="return false;" ${isReach5}/>5
                <input type="checkbox" onclick="return false;" ${isReach6}/>6
                <input type="checkbox" onclick="return false;" ${isReach7}/>7
                </span>
              </span>
            </div>
            <div style="padding-left:20px;">
              <input type="checkbox" onclick="return false;" ${NBay}/><span class='checkboxLabel'>North Bay</span>
              <span>Volume through North Bay(acre-feet): </span><span class="highlight">${NorthBayV}</span>
            </div>
          </c:if>
        </li>
        <li class="${reachShow}" style="padding-left:120px;">
          <input type="checkbox" onclick="return false;" ${cvp}/>CVP<br/>
          <c:if test="${!empty(cvp)}">
            <div style="padding-left:20px;">
              <input style="padding-left:10px;" type="checkbox" onclick="return false;" ${Jones}/><span class='checkboxLabel'>Jones</span>
              <span>Volume through Jones(acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${JonesV}"/></span><br/>
            </div>
<!--            <div style="padding-left:40px;">
              <input type="checkbox" onclick="return false;" ${CVPIntertie}/><span class='checkboxLabel'>SWP/CVP Intertie</span><br/>
            </div>-->
            <div style="padding-left:20px;">
              <input style="padding-left:10px;" type="checkbox" onclick="return false;" ${Forbearance}/><span class='checkboxLabel'>Forbearance</span>
              <span>Volume through Forbearance(acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${ForbearanceV}"/></span><br/>
            </div>
            <div style="padding-left:20px;">
              <input style="padding-left:10px;" type="checkbox" onclick="return false;" ${WarrenAct}/><span class='checkboxLabel'>Warren Act</span>
              <span>Volume through Warren Act(acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${WarrenActV}"/></span><br/>
            </div>
          </c:if>
        </li>
        <li style="padding-left:120px;">
          <input type="checkbox" onclick="return false;" ${other}/><span class='checkboxLabel'>OTHER</span> <span class="highlight">${otherD}</span>
          <span>Volume (acre-feet): </span><span class="highlight"><fmt:formatNumber type="number" pattern="0.00" value="${otherV}"/></span><br/>
        </li>
      </ul>
    </div>
    </br>

    <div class="fieldRow">
      <c:set var="exportWindowShow" value=""/>
      <c:if test="${proposal.deltaTransferInd == 0}">
        <c:set var="exportWindowShow" value="hidden"/>
      </c:if>
      <span>Through Delta Transfer<span class="asterisk">*</span>: </span><span class="highlight">
      <c:if test="${proposal.deltaTransferInd == 1}">Yes</c:if>
      <c:if test="${proposal.deltaTransferInd == 0}">No</c:if></span>
    </div>
    <div class="fieldRow ${exportWindowShow}">
      <span>Requested Export Window:  From </span>
      <span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpFromDate}"/></span>
      <span>To </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.reqExpToDate}"/></span>
    </div>
    <div class="fieldRow">
      <span>Request for Storage Prior to Export: </span><span class="highlight">${proposal.reqStorageExp}</span>
    </div>

    <div>
      <span>Environmental Regulatory Compliance<span class="asterisk">*</span>: </span>
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
      <span class='checkboxLabel'>
      <input type="checkbox" onclick="return false;" ${swrcb}/>SWRCB
      <input type="checkbox" onclick="return false;" ${ceqa}/>CEQA
      <input type="checkbox" onclick="return false;" ${nepa}/>NEPA
      </span>
    </div>
    <div class="fieldRow ${swrcbShow}">
      <span>Petition for Change Submitted to SWRCB Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbPetitionDate}"/></span>
      <span>SWRCB Order Issued Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransSwrcb.swrcbOrderDate}"/></span>
    </div>
    <div class="fieldRow ${ceqaShow}">
      <span>CEQA document submitted to OPR Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaSubmittedDate}"/></span>
      <span>SCH#: </span><span class="highlight">${proposal.wtTransCeqa.schNum}</span>
      <span>CEQA document adopted Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransCeqa.ceqaAdoptedDate}"/></span>
    </div>
    <div class="fieldRow ${nepaShow}">
      <span>NEPA document issued Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaIssuedDate}"/></span>
      <span>NEPA#: </span><span class="highlight">${proposal.wtTransNepa.nepaNum}</span>
      <span>NEPA document adopted Date: </span><span class="highlight"><fmt:formatDate pattern="MM/dd/yyyy" value="${proposal.wtTransNepa.nepaAdoptedDate}"/></span>
    </div>
    <div class="fieldRow">
      <span>Comments: </span>
      <span style="padding-left:10px;font-size:16px;"><span class="highlight">${proposal.wtComm}</span></span>
    </div>
    <div>
      <span style="color:red;">Note: Fields marked with * must be filled out</span>
    </div>

    <div class="forReviewer">
      <c:set var="isComplete" value=""/>
      <c:set var="checkVal" value="0"/>
      <c:set var="techNote" value=""/>
      <c:set var="note" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('B')}"/>
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
               sectionKey="B"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="B"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="B"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--        <div class="historyCt hidden">
          <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('B')}" var="tNote" >
            <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
            <div style="padding-left: 40px;">${tNote.note}</div>
          </c:forEach>
        </div>-->
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('B')}" var="tNote" varStatus="iterator">
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
  </div>

  <div class="section">
    <a class="anchor" name="section-C"></a>
    <div class="header">
      C. Water Rights Type and Transfer Information<span class="asterisk">*</span>
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="C"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>
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
            <c:set var="type" value="Pre-1914"/>
          </c:if>
          <c:if test="${wtRights.waterRightsType eq 'Decree'}">
            <c:set var="type" value="Adjudicated"/>
          </c:if>
          <c:if test="${wtRights.waterRightsType eq 'Contract'}">
            <c:set var="type" value="Contract"/>
          </c:if>
          <tr>
            <td class="boldfield">${type}</td>
            <td class="boldfield">${wtRights.waterRightsType} # ${wtRights.waterRightsNum}</td>
            <td class="boldfield">${wtRights.proposedTransVol}</td>
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
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('C')}"/>
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
               sectionKey="C"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="C"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="C"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('C')}" var="tNote" varStatus="iterator">
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
  </div>

  <div class="section">
    <a class="anchor" name="section-D"></a>
    <div class="header">
      D. General Info Attachments
      <img class="commentimg"
           transId="${proposal.wtTransId}"
           sectionKey="D"
           src="${comment_img}"
           title="Internal Comment"
           onclick="window.reviewer.initInternalNotes(this);"/>
    </div>
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
        <c:forEach items="${attachmentBI}" var="attachment">
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
        <c:forEach var="checklist" items="${checklistBI}">
          <c:set var="isMissing" value="1"/>
          <tr>
            <td class='simple'>${checklist.name}</td>
            <td style="width:30%;" class="boldfield">
              <c:forEach var="attachment" items="${attachmentBI}">
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
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('D')}"/>
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
               sectionKey="D"
               transId="${proposal.wtTransId}"
               ${isComplete}
               value="${checkVal}"
               onclick="window.reviewer.initCompletedCheckbox(this);"/>
        <label>Check if review of above item is completed</label>
      </div>
      <fieldset class="">
        <textarea class="techNote"
                  placeholder="Technical Comments"
                  sectionKey="D"
                  transId="${proposal.wtTransId}"
                  val=""
                  onkeyup="window.reviewer.auto_grow(this)"></textarea>
        <input class="noteBtn" type="button" value="Show Comment History" onclick="window.reviewer.initSlideHistory(this);"/>
        <input class="noteBtn hidden" type="button" value="Clean" onclick="window.reviewer.initClean(this);"/>
        <input class="noteBtn"
               type="button"
               sectionKey="D"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/><br/>
        <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--        <div class="historyCt hidden">
          <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('D')}" var="tNote" >
            <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
            <div style="padding-left: 40px;">${tNote.note}</div>
          </c:forEach>
        </div>-->
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('D')}" var="tNote" varStatus="iterator">
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
           name="checkBase"
           section="BASE"
           type="button" 
           transId="${proposal.wtTransId}"
           swpaoNum="${proposal.swpaoContractNum}"
           swpaoReviewer="${proposal.swpaoReviewer}"
           value="Review Completed"
           onclick="window.reviewer.checkReviewCompleted(this);" />
  </div>
</div>
