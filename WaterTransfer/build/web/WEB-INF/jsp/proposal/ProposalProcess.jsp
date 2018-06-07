<%--
    Document   : ProposalProcess
    Created on : May 7, 2015, 8:54:37 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/proposal/ProposalProcess.js
</c:set>
  
<c:if test="${sessionScope.USER.isManager() || sessionScope.USER.isReviewer()}">
  <c:set var="hideField" value=""/>
</c:if>

<div id="proposalprocess_tab">
  <!--<div class="sub-title">Proposal Process</div>-->
  <c:set var="submittedDate" value=""/>
  <c:set var="completeDate" value=""/>
  <c:set var="completeComment" value=""/>
  <c:set var="condApprovalDate" value=""/>
  <c:set var="condApprovalComment" value=""/>
  <c:set var="proApprovedDate" value=""/>
  <c:set var="proApprovedComment" value=""/>
  <c:forEach var="statusTrack" items="${statusTrackList}">
    <c:if test='${statusTrack.statusName eq "SUBMITTED"}'>
      <c:set var="submittedDate" value="${statusTrack.statusDate}"/>
    </c:if>
    <c:if test='${statusTrack.statusName eq "PCOMPLETE"}'>
      <c:set var="completeDate" value="${statusTrack.statusDate}"/>
      <c:set var="completeComment" value="${statusTrack.statusComments}"/>
    </c:if>
    <c:if test='${statusTrack.statusName eq "PAPPROVED"}'>
      <c:set var="proApprovedDate" value="${statusTrack.statusDate}"/>
      <c:set var="proApprovedComment" value="${statusTrack.statusComments}"/>
    </c:if>
    <c:if test='${statusTrack.statusName eq "UREVIEW"}'>
      <c:set var="statusName" value="REVIEWER"/>
    </c:if>
  </c:forEach>
  
  <c:set var="transferTypeCI" value="hidden"/>
  <c:set var="transferTypeGW" value="hidden"/>
  <c:set var="transferTypeRV" value="hidden"/>  
  <c:if test="${proposal.proAcrIdleInd==1}"><c:set var="transferTypeCI" value=""/></c:if>
  <c:if test="${proposal.wellUseNumInd==1}"><c:set var="transferTypeGW" value=""/></c:if>
  <c:if test="${proposal.resReOpInd==1}"><c:set var="transferTypeRV" value=""/></c:if>

  <form class="form" id="process-form" method="POST">
    <input class="hidden" type="text" id="wtTransId" name="wtTransId" value="${proposal.wtTransId}"/>
    <input class="hidden" type="text" id="statusId" name="statusId" value="${proposal.wtStatusFlag.wtStatusFlagId}"/>
    <table id="proposal-process-table" style="width:900px;">
      <tr class="tableRow">
        <td style="width:250px">SWPAO Contract #</td>
        <td style="width:180px">
          <input class="validField" type="text" 
                 maxlength="6" id="swpaoContractNum" 
                 transId='${proposal.wtTransId}'
                 name="swpaoContractNum" 
                 value='${proposal.swpaoContractNum}'
                 onblur="window.proposalProcess.saveProcessField(this);"
                 onchange="window.proposalProcess.buildPropsalTitle(this)"/>
        </td>
        <td style="width:220px">Total Contract Amount (acre-feet)</td>
        <td style="width:100px">
          <input class="validField numField" type="text" 
                 maxlength="6" id="contractAmount" 
                 transId='${proposal.wtTransId}'
                 name="contractAmount" 
                 value='${proposal.contractAmount}'
                 disabled
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
      </tr>
      <tr class="tableRow ${transferTypeCI}">
        <td>Cropland Idling Contract Amount (acre-feet)</td>
        <td>
          <input class="validField numField" type="text" 
                 maxlength="6" id="ciContractAmount" 
                 transId='${proposal.wtTransId}'
                 name="ciContractAmount" 
                 value='${proposal.wtCropIdling.contractAmount}'
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
        <td>Cropland Idling Reviewer</td>
        <td>
          <input class="validField ciReviewer" 
                 type="text" id="ciReviewer"
                 transId='${proposal.wtTransId}'
                 name="ciReviewer" 
                 value='${proposal.ciReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/><br/>  
        </td>
      </tr>
      <tr class="tableRow ${transferTypeGW}">
        <td>Groundwater Substitution Contract Amount (acre-feet)</td>
        <td>
          <input class="validField numField" type="text" 
                 maxlength="6" id="gwContractAmount" 
                 transId='${proposal.wtTransId}'
                 name="gwContractAmount" 
                 value='${proposal.wtGroundwater.contractAmount}'
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
        <td>Groundwater Substitution Reviewer</td>
        <td>
          <input class="validField gsReviewer" 
                 type="text" id="gsReviewer"
                 transId='${proposal.wtTransId}'
                 name="gsReviewer" 
                 value='${proposal.gsReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/><br/>  
        </td>
      </tr>
      <tr class="tableRow ${transferTypeRV}">
        <td>Reservoir Release Contract Amount (acre-feet)</td>
        <td>
          <input class="validField numField" type="text" 
                 maxlength="6" id="rvContractAmount" 
                 transId='${proposal.wtTransId}'
                 name="rvContractAmount" 
                 value='${proposal.wtReservoir.contractAmount}'
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
        <td>Reservoir Release Reviewer</td>
        <td>
          <input class="validField rrReviewer" 
                 type="text" id="rrReviewer"
                 transId='${proposal.wtTransId}'
                 name="rrReviewer" 
                 value='${proposal.rrReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/><br/>  
        </td>
      </tr>      
      <tr class="tableRow">
        <td>Proposal Submitted Date</td>
        <td>
          <input class="validField" type="text" id="proSubmitDate" name="proSubmitDate" value='<fmt:formatDate value="${submittedDate}" pattern="MM/dd/yyyy"/>' disabled/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proSubmitDate_help" alt="Help" title="Proposal Submitted Date Help">
        </td>
        <td>SWPAO Reviewer</td>                
        <td>
          <input class="validField assignedReviewer" 
                 type="text" id="swpaoReviewer"
                 transId='${proposal.wtTransId}'
                 name="swpaoReviewer"
                 value='${proposal.swpaoReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/>                         
        </td>
      </tr>
      <tr class="tableRow">
        <td>Proposal Complete Date</td>
        <td>
          <input class="validField dateField" type="text" id="proCompleteDate" name="proCompleteDate" value='<fmt:formatDate value="${completeDate}" pattern="MM/dd/yyyy"/>' disabled/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proCompleteDate_help" alt="Help" title="Proposal Complete Date Help">
          <img src="${pageContext.request.contextPath}/resources/images/icons/comment2.png" class="comment-icon" id="complete_comment" alt="Comment" title="Proposal Complete Comment" height="12" width="12">
          <label class="hidden" id="completeComment">${completeComment}</label>
        </td>
<!--        <td>Region Reviewer</td>
        <td>
          <input class="validField assignedReviewer" 
                 type="text" id="regionReviewer"
                 transId='${proposal.wtTransId}'
                 name="regionReviewer" 
                 value='${proposal.regionReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/><br/>
          <label style="color:red;font-size:11px" class="statusMsg hidden">Please change status in Proposal Status Tab.</label>   
        </td>-->
      <tr class="tableRow">
        <td>Proposal Approved Date</td>
        <td>
          <input class="validField dateField" type="text" id="proApprovedDate" name="proApprovedDate" value='<fmt:formatDate value="${proApprovedDate}" pattern="MM/dd/yyyy"/>' disabled/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proApprovedDate_help" alt="Help" title="Proposal Approved Date">
          <img src="${pageContext.request.contextPath}/resources/images/icons/comment2.png" class="comment-icon" id="proApproved_comment" alt="Comment" title="Proposal Approved Comment" height="12" width="12">
          <label class="hidden" id="proApprovedComment">${proApprovedComment}</label>
        </td>
        <td class="usbrRow hidden">USBR Reviewer</td>
        <td class="usbrRow hidden">
          <input class="validField" 
                 type="text" id="usbrReviewer"
                 name="usbrReviewer"
                 transId='${proposal.wtTransId}'
                 value='${proposal.usbrReviewer}'
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
      </tr>
      <tr class="tableRow">
        <td>Agency Approval Required</td>
        <td colspan='3'>
          <c:set var="dwr" value=""/>
          <c:set var="swrcb" value=""/>
          <c:set var="usbr" value=""/>
          <c:set var="fishery" value=""/>
          <c:set var="other" value=""/>
          <c:if test="${proposal.wtAgencyApproval.dwr == 1}">
            <c:set var="dwr" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtAgencyApproval.swrcb == 1}">
            <c:set var="swrcb" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtAgencyApproval.usbr == 1}">
            <c:set var="usbr" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtAgencyApproval.fishery == 1}">
            <c:set var="fishery" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtAgencyApproval.other == 1}">
            <c:set var="other" value="checked"/>
          </c:if>
          <input type="checkbox" 
                 class="approvalAgency non_border" 
                 name="DWR" ${dwr}
                 transId='${proposal.wtTransId}'
                 onclick="window.proposalProcess.saveAgencyApproval(this);"/><label>DWR</label>
          <input type="checkbox"                  
                 class="approvalAgency non_border" 
                 name="SWRCB" ${swrcb}
                 transId='${proposal.wtTransId}'
                 onclick="window.proposalProcess.saveAgencyApproval(this);"/><label>SWRCB</label>
          <input type="checkbox" 
                 class="approvalAgency non_border" 
                 name="USBR" ${usbr}
                 transId='${proposal.wtTransId}'
                 onclick="window.proposalProcess.saveAgencyApproval(this);"/><label>USBR</label>
          <input type="checkbox" 
                 class="approvalAgency non_border" 
                 name="FISHERY" ${fishery}
                 transId='${proposal.wtTransId}'
                 onclick="window.proposalProcess.saveAgencyApproval(this);"/><label>Fishery Agencies</label>
          <input type="checkbox" 
                 class="approvalAgency non_border" 
                 name="OTHER" ${other}
                 transId='${proposal.wtTransId}'
                 onclick="window.proposalProcess.saveAgencyApproval(this);"/><label>Other</label>
          <input class="validField" 
                 type="text"
                 name="otherDetail" 
                 value="${proposal.wtAgencyApproval.otherDetail}" maxlength="32"
                 transId='${proposal.wtTransId}'
                 onchange="window.proposalProcess.saveAgencyApproval(this);"/>
        </td>
      </tr>
      <tr class="tableRow">
        <td>Fisheries Agency Review</td>
        <td>
          <input style="width:60px;" class="wtFuTypeId non_border" 
                 type="radio" id="isFisheriesReview" 
                 name="isFisheriesReview" 
                 transId='${proposal.wtTransId}'
                 value="0" <c:if test="${proposal.isFisheriesReview == 0}">checked="checked"</c:if>
                   onclick="window.proposalProcess.saveProcessField(this);"/>
          <label>No</label>
          <input style="width:60px;" class="wtFuTypeId non_border"
                 type="radio" id="isFisheriesReview"
                 name="isFisheriesReview" 
                 transId='${proposal.wtTransId}'
                 value="1" <c:if test="${proposal.isFisheriesReview == 1}">checked="checked"</c:if>
                 onclick="window.proposalProcess.saveProcessField(this);"/>
          <label>Yes</label>
        </td>
      </tr>
      <tr class="tableRow">
        <td colspan='2'>Fisheries Agency Approval Date, if applicable</td>
        <td>
          <input class="validField dateField" 
                 type="date" id="fisheriesApprocalDate"
                 name="fisheriesApprocalDate" 
                 transId='${proposal.wtTransId}'
                 value='<fmt:formatDate value="${proposal.fisheriesApprocalDate}" pattern="MM/dd/yyyy"/>'
                 onchange="window.proposalProcess.saveProcessField(this);"/>
        </td>
      </tr>
      <tr class="tableRow">
        <td>Carriage Water Loss Percentage</td>            
      </tr>
      <tr class="tableRow">
        <td style="text-align:right;">Banks/Jones Initial </td>
        <td><input class="intField" maxlength="2"
                   type="text" 
                   name="banksInitialPercent" 
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.banksInitialPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
        <td style="text-align:right;">Final </td>
        <td><input class="intField" maxlength="2" 
                   type="text" id=""
                   name="banksfinalPercent"
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.banksfinalPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
      </tr>
      <tr class="tableRow">
        <td style="text-align:right;">NBA Initial </td>
        <td><input class="intField" maxlength="2"
                   type="text" id=""
                   name="nbaInitialPercent"
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.nbaInitialPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
        <td style="text-align:right;">Final </td>
        <td><input class="intField" maxlength="2" 
                   type="text" id="" 
                   name="nbafinalPercent" 
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.nbafinalPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
      </tr>
      <tr class="tableRow">  
        <td style="text-align:right;">San Joaquin Initial </td>
        <td><input class="intField" maxlength="2" 
                   type="text" id="" 
                   name="mercedInitialPercent"
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.mercedInitialPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
        <td style="text-align:right;">Final </td>
        <td><input class="intField" maxlength="2" 
                   type="text" id="" 
                   name="mercedfinalPercent"
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.mercedfinalPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
      </tr>
      <tr class="tableRow">  
        <td style="text-align:right;">Reach Loss Percentage </td>
        <td><input class="intField" maxlength="2" 
                   accept=""type="text" id="" 
                   name="reachLossPercent"
                   transId='${proposal.wtTransId}'
                   value='${proposal.wtWaterLoss.reachLossPercent}'
                   onchange="window.proposalProcess.saveWaterLoss(this);"/></td>
        <td style="text-align:right;"></td>
        <td></td>
      </tr>
      <tr class="tableRow"">
        <td>Comments</td>
        <td class="fullRow" colspan='2'>
          <textarea cols="50" rows="2" 
                    id="dwrComments"
                    name="dwrComments"
                    transId='${proposal.wtTransId}'
                    onchange="window.proposalProcess.saveProcessField(this);">${proposal.dwrComments}</textarea>
        </td>
      </tr>
    </table>
    
    <div class="tab_header_report isExpand">Transfer Amount Report 
      <img src="${pageContext.request.contextPath}/resources/images/icons/calculator.png" 
           transId='${proposal.wtTransId}' 
           id="calBtn_transAmount" 
           alt="Calculation Button of Transfer Amount Report"
           style="cursor:pointer;"
           onclick="window.proposalProcess.resetTransferAmountTable();"/>
      <img class="save-icon" 
           src="${pageContext.request.contextPath}/resources/images/icons/disk.png" 
           transId='${proposal.wtTransId}' 
           alt="Save Transfer Amount Report" 
           title="Save Transfer Amount Report"
           style="cursor:pointer;"
           onclick="window.proposalProcess.saveTransferAmountAll(this);"/>
    </div>
    <div class="contact_panel">   
      <table id="transfer-amount-table">
        <thead>
          <tr class="transfer-amount-header">
            <th style="width:15%;">Type of Transfer</th>
            <th style="width:13%;">Proposed Amount</th>
            <th style="width:13%;">Contract Amount</th>
            <th style="width:13%;">Actual Amount</th>
            <th style="width:13%;">Amount for Export(after streamflow depletion)</th>
            <th style="width:13%;">Exported Amount(after carriage water losses)</th>
            <th style="width:13%;">Water Delivered(after all losses)</th>
          </tr>
        </thead>
        <tbody>
          <tr class="${transferTypeCI}">
            <td>Crop Idling</td>
            <td><span id="ci1"></span></td>
            <td><span id="ci2"></span></td>
            <td><span id="ci3"></span></td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="ci4" 
                     transId='${proposal.wtTransId}'
                     name="ciExportAmount" 
                     value='${proposal.wtCropIdling.exportAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="ci5" 
                     transId='${proposal.wtTransId}'
                     name="ciExportedAmount" 
                     value='${proposal.wtCropIdling.exportedAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="ci6" 
                     transId='${proposal.wtTransId}'
                     name="ciDeliveredAmount" 
                     value='${proposal.wtCropIdling.deliveredAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
          </tr>
          <tr class="${transferTypeGW}">
            <td>Groundwater</td>
            <td><span id="gw1"></span></td>
            <td><span id="gw2"></span></td>
            <td><span id="gw3"></span></td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="gw4" 
                     transId='${proposal.wtTransId}'
                     name="gwExportAmount" 
                     value='${proposal.wtGroundwater.exportAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="gw5" 
                     transId='${proposal.wtTransId}'
                     name="gwExportedAmount" 
                     value='${proposal.wtGroundwater.exportedAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="gw6" 
                     transId='${proposal.wtTransId}'
                     name="gwDeliveredAmount" 
                     value='${proposal.wtGroundwater.deliveredAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
          </tr>
          <tr class="${transferTypeRV}">
            <td>Reservoir</td>
            <td><span id="rv1"></span></td>
            <td><span id="rv2"></span></td>
            <td><span id="rv3"></span></td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="rv4" 
                     transId='${proposal.wtTransId}'
                     name="rvExportAmount" 
                     value='${proposal.wtReservoir.exportAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="rv5" 
                     transId='${proposal.wtTransId}'
                     name="rvExportedAmount" 
                     value='${proposal.wtReservoir.exportedAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
            <td>
              <input class="validField numField" 
                     type="text" 
                     maxlength="16" 
                     style="width:60px;"
                     id="rv6" 
                     transId='${proposal.wtTransId}'
                     name="rvDeliveredAmount" 
                     value='${proposal.wtReservoir.deliveredAmount}'
                     onchange="window.proposalProcess.saveTransferAmountReport(this);"/>
            </td>
          </tr>          
        </tbody>
      </table>
    </div>
  </form>

  <div class="tab_header isExpand">Attachments</div>
  <div class="contact_panel">
    <div class="attachment-container" id="attachment-process-info" typeid="PP"></div>
    <div class="attach-button">
      <input class="attachButton add_agency" type="button" id="attachProcessInfo" typeid="PP" containerid="attachment-process-info" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>
  <!--
      <div class="button_ct">
          <button class="proposal_btn ${newProposal}" id="saveProcess" style="width:120px;">Save</button>
      </div>-->

  <div class="help-ct"></div>
</div>
<script type="text/javascript">
  $(document).ready(function () {
    window.proposalProcess = new ProposalProcess();
  });
</script>
<style type="text/css">
  #transfer-amount-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #transfer-amount-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #transfer-amount-table td{
    text-align: center;
    font-size: 13px;
    border:1px solid #cccccc;
    padding: 3px;
  }  
  .transfer-amount-header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .tab_header_report {
    background-color: #9cc7d4;
    border-radius: 2px 2px 0 0;
    margin-top: 20px;
    padding: 10px;
    cursor: default;
    font-size: 16px;
 }
</style>