<%--
    Document   : BaseInfo
    Created on : Apr 1, 2015, 10:06:30 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="baseinfo_tab">
  <input class="hidden" type="text" id="waterRightsSize" name="waterRightsSize" />

  <c:if test="${!(proposal.wtStatusFlag.statusName eq 'DRAFT')}">
    <c:set var="active" value="disabled"/>
  </c:if>
  <c:if test="${buttonCheck == true}">
    <c:set var="active" value=""/>
  </c:if>

  <div id="typeMenu">
    <div class="head_explain">
      To begin your proposal, select at least one of the following transfer types below to reveal tabs allowing additional information to be added.
    </div>
    <label style="font-weight: bold;">Select Type of Transfer:<span class="asterisk">*</span></label><br>
    <div class="transfer" >
      <input type="checkbox" 
             class="tab-check validField"
             id="cropidling-check"
             txtlabel="Crop Idling" 
             tabid="CropIdling" 
             hrefid="cropidling_tab" 
             name="proAcrIdleInd" 
             value="${proposal.proAcrIdleInd}"
             <c:if test="${proposal.proAcrIdleInd==1}">checked="checked"</c:if> 
             ${disableEdit} /><label for="">Cropland Idling and Crop Shifting</label><br>
      <input type="checkbox" 
             class="tab-check validField" 
             id="groundwater-check" 
             txtlabel="Groundwater" 
             tabid="GroundWater" 
             hrefid="groundwater_tab" 
             name="wellUseNumInd" 
             value="${proposal.wellUseNumInd}"
             <c:if test="${proposal.wellUseNumInd==1}">checked="checked"</c:if> 
             ${disableEdit} /><label for="">Groundwater Substitution</label><br>
      <input type="checkbox" 
             class="tab-check validField" 
             id="reservoir-check" 
             txtlabel="Reservoir" 
             tabid="Reservoir" 
             hrefid="reservoir_tab" 
             name="resReOpInd" 
             value="${proposal.resReOpInd}"
             <c:if test="${proposal.resReOpInd==1}">checked="checked"</c:if> 
             ${disableEdit} /><label for="">Reservoir Release</label><br>
    </div>
  </div>
  <input class="hidden" type="text" id="wtTransId" name="wtTransId" value="${proposal.wtTransId}"/>
  <input class="hidden" type="text" id="statusName" name="statusName" value="${proposal.wtStatusFlag.statusName}"/>

  <div id="base-container" style="margin-top: 20px;">
    <table class="create-base-table" style="width:1000px; ">
      <tr class="tableRow">
        <th style="width:350px;">Does the seller have any previous water transfers?<span class="asterisk">*</span></th>
        <td>
          <input class="non_border" style="width:20px;" type="radio" id="hasPreTrans" name="hasPreTrans" value="0" <c:if test="${proposal.hasPreTrans != 1}">checked="checked"</c:if> ${disableEdit}/><label>No</label>
          <input class="non_border" style="width:20px;" type="radio" id="hasPreTrans" name="hasPreTrans" value="1" <c:if test="${proposal.hasPreTrans == 1}">checked="checked"</c:if> ${disableEdit}/><label>Yes</label>
          </td>
        </tr>
      </table>
      <div class="tab_header isExpand preTransfer" style="margin-top:-10px;">Enter the Agreements for the five most recent water transfers</div>
      <div class="contact_panel preTransfer" style="margin-bottom: 20px;">
        <div class="pre_transfer_ct"></div>
        <div class="attach-button ${hideField}">
        <input class="add_agency" type="button" id="pre_transfer_addBtn" containerid="" value="Add Previous Transfer" onclick="return false;"/>
        <span style="color:red; font-size:11px">Click the save icon for each transfer.</span>
      </div>
    </div>
    <hr style="width:1000px;"></br>

    <table class="create-base-table" style="width:1000px;">
      <tr class="tableRow">
        <th style="width:250px;">Transfer Year<span class="asterisk">*</span></th>
        <td style="width:150px;">
          <input class="validField yearField" 
                 type="text" maxlength="4"
                 id="transYear"
                 transId='${proposal.wtTransId}'
                 name="transYear" 
                 value="${proposal.transYear}"  
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/></td>
        <td colspan='1' style='font-weight: bold; width: 200px;'>
          <label for='isShowLong'>Duration of water transfer: </label>
        </td>
        <td colspan='2'>
          <input class="non_border" 
                 style="width:20px;"
                 type="radio"
                 id="" 
                 transId='${proposal.wtTransId}'
                 name="isShortLong"
                 value="0"
                 <c:if test="${proposal.isShortLong != 1}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>Temporary (One Year)</label>
          <input class="non_border" 
                 style="width:20px;"
                 type="radio"
                 id="" 
                 transId='${proposal.wtTransId}'
                 name="isShortLong"
                 value="1" 
                 <c:if test="${proposal.isShortLong == 1}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>Long-Term</label>
          </td>
        </tr>
        <tr class="tableRow">
          <th style="width:250px;vertical-align: top;">Proposal Description
            <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proposalDescription_help" alt="Help" title="Proposal Description Help">
          </th>
          <td class="fullRow" colspan='3'>
            <textarea cols="50" rows="2" 
                      style="margin-bottom: 5px" maxlength="2048"
                      id="transDescription"
                      transId='${proposal.wtTransId}'
                      name="transDescription" 
                      ${disableEdit}
                      onblur="window.baseInfo.saveBIField(this);">${proposal.transDescription}</textarea>
          <div style="margin-bottom: 20px; color: #565656;">You have <span id="counter"></span> characters left.</div>
        </td>
      </tr>
      <tr class="tableRow countyCt">
        <th>County of Seller</th>
        <td class="${hideField}">
          <select autocomplete="off" class="basincombo" name="combobox" id="countycombo" multiple ${disableEdit}>
            <c:forEach var="county" items="${LookupDataContext.countyLookup}">
              <option value="${county.wtCountyId}" json='{"wtCountyId":${county.wtCountyId},"name":"${county.name}"}'>${county.name}</option>
            </c:forEach>
          </select>
        </td>
        <td class="fullRow" colspan='2'>
          <div id="countyList" class="${isNew}" style="">
            <c:forEach var="county" items="${proposal.wtCountyCollection}">
              <span class="countyButton">
                <input type="hidden" name="countyId" autocomplete="off" value="${county.wtCountyId}"/>
                ${county.name}
                <c:if test='${hideField == ""}'>
                  <span class="ui-icon ui-icon-circle-close countyItem" style="display: inline-block; vertical-align: bottom;"></span>
                </c:if>
              </span>
            </c:forEach>
            <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="county_help" alt="Help" title="County of Seller Help">
          </div>
        </td>
      </tr>
      <tr class="tableRow">
        <th>Proposed Total Transfer Amount (acre-feet)<span class="asterisk">*</span></th>
        <td style="width:180px;">
          <input class="validField numField" 
                 type="text" maxlength="20"
                 id="proTransQua" 
                 transId='${proposal.wtTransId}'
                 name="proTransQua" 
                 value="${proposal.proTransQua}" 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proTransQua_help" alt="Help" title="Proposed Net Transfer Amount Help">
          <img src="${pageContext.request.contextPath}/resources/images/icons/calculator.png" 
               class="calBtn" 
               id="PTQcalBtn" alt="Calculator"
               onclick="window.baseInfo.calProTransferQua();">
        </td>
        <td class="hidden" colspan='3'>
          <span style="font-weight: bold;">Proposed Cost of Net Water per AF ($$/AF) $</span>
          <input class="numField" 
                 type="text" maxlength="9"
                 id="proUnitCost"
                 transId='${proposal.wtTransId}'
                 name="proUnitCost"
                 value="${proposal.proUnitCost}"  
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proUnitCost_help" alt="Help" title="Proposed Cost of Net Water per AF Help">
        </td>
      </tr>
      <tr class="tableRow hidden">
        <th colspan="2">Proposed Maximum Amount for Seller ($$) <span style="float:right;">$</span></th>
        <td>
          <input class="numField" 
                 type="text"
                 id="proAgreePaid"
                 transId='${proposal.wtTransId}'
                 name="proAgreePaid" 
                 value="${proposal.proAgreePaid}" 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/>
<!--          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="proposedAgreement_help" alt="Help" title="Proposed Agreement Amount Help">-->
        </td>
      </tr>
      <tr class="tableRow">
        <th>Surface Water Source/Reservoir<span class="asterisk">*</span></th>
        <td>
          <input class="validField" 
                 type="text" maxlength="128"
                 id="surWaterSource"
                 transId='${proposal.wtTransId}'
                 name="surWaterSource"
                 value="${proposal.surWaterSource}"  
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="surWaterSource_help" alt="Help" title="Surface Water Source Help">
        </td>
      </tr>
      <tr class="tableRow">
        <th>Primary Watersheds<span class="asterisk">*</span></th>
        <td>
          <select name="majorRiverAttribute" 
                  id="majorRiverAttribute"
                  transId='${proposal.wtTransId}'
                  class="validField" 
                  ${disableEdit}
                  onchange="window.baseInfo.saveBIField(this);">
            <option value="">Please select a river attribute</option>
            <option value="American River" ${proposal.majorRiverAttribute == "American River"?'selected="selected"':''}>American River</option>
            <option value="Delta Channel" ${proposal.majorRiverAttribute == "Delta Channel"?'selected="selected"':''}>Delta Channel</option>
            <option value="Feather River" ${proposal.majorRiverAttribute == "Feather River"?'selected="selected"':''}>Feather River</option>
            <option value="Merced River" ${proposal.majorRiverAttribute == "Merced River"?'selected="selected"':''}>Merced River</option>
            <option value="Sacramento River" ${proposal.majorRiverAttribute == "Sacramento River"?'selected="selected"':''}>Sacramento River</option>
            <option value="San Joaquin River" ${proposal.majorRiverAttribute == "San Joaquin River"?'selected="selected"':''}>San Joaquin River</option>
            <option value="Yuba River" ${proposal.majorRiverAttribute == "Yuba River"?'selected="selected"':''}>Yuba River</option>
          </select>
        </td>
      </tr>
      <tr class="tableRow">
        <th>Proposed Transfer Start Date<span class="asterisk">*</span></th>
        <td>
          <input class="validField dateField" 
                 type="text" 
                 id="transWinStart"
                 transId='${proposal.wtTransId}'
                 name="transWinStart" 
                 value='<fmt:formatDate value="${proposal.transWinStart}" pattern="MM/dd/yyyy"/>'
                 ${disableEdit}/>
        </td>
        <th>Proposed Transfer End Date<span class="asterisk">*</span></th>
        <td>
          <input class="validField dateField"
                 type="text"
                 id="transWinEnd"
                 transId='${proposal.wtTransId}'
                 name="transWinEnd"
                 value='<fmt:formatDate value="${proposal.transWinEnd}" pattern="MM/dd/yyyy"/>' 
                 ${disableEdit}/>
        </td>
      </tr>
      <tr><th colspan="5"><hr></th></tr>
      <tr class="tableRow">
        <th>State Water Project Contractor (Buyer)<span class="asterisk">*</span></th>
        <td>
          <input class="non_border" 
                 style="width:20px;" 
                 type="radio" 
                 id="isStateContractor" 
                 transId='${proposal.wtTransId}'
                 name="isStateContractor"
                 value="0" 
                 <c:if test="${proposal.isStateContractor == 0}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>No</label>
          <input class="non_border" 
                 style="width:20px;"
                 type="radio"
                 id="isStateContractor"
                 transId='${proposal.wtTransId}'
                 name="isStateContractor" 
                 value="1" 
                 <c:if test="${proposal.isStateContractor != 0}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>Yes</label>
        </td>
      </tr>

      <tr class="tableRow" style="padding-bottom: 10px;">
        <th style="vertical-align: top;">
            Facilities Requested<span class="asterisk">*</span>
            <input type="checkbox" class="validField hidden" name="wtFuTypeIdHide"/>
          </th>
          <td colspan='5' class="checkbox_ct">
          <c:set var="swpSub" value="hidden"/>
          <c:set var="cvpSub" value="hidden"/>
          <c:set var="otherSub" value="hidden"/>
          <c:set var="cvp" value=""/>
          <c:set var="swp" value=""/>
          <c:set var="Banks" value=""/>
          <c:set var="nb" value=""/>
          <c:set var="Intertie" value=""/>
          <c:forEach var="transType" items="${proposal.wtTransTypeCollection}" varStatus="iterator">
            <c:if test="${transType.wtFuType.fuType eq 'CVP'}">
              <c:set var="cvp" value="checked"/>
              <c:set var="cvpSub" value=""/>
            </c:if>
            <c:if test="${transType.wtFuType.fuType eq 'SWP'}">
              <c:set var="swp" value="checked"/>
              <c:set var="swpSub" value=""/>
            </c:if>
            <c:if test="${transType.wtFuType.fuType eq 'OTHER'}">
              <c:set var="other" value="checked"/>
              <c:set var="otherSub" value=""/>              
              <c:set var="otherV" value="${transType.typeVolume}"/>
              <c:set var="otherD" value="${transType.typeDetail}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuSubType eq 'Jones'}">
              <c:set var="Jones" value="checked"/>
              <c:set var="JonesV" value="${transType.typeVolume}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuSubType eq 'Forbearance'}">
              <c:set var="Forbearance" value="checked"/>
              <c:set var="ForbearanceV" value="${transType.typeVolume}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuSubType eq 'Warren Act'}">
              <c:set var="WarrenAct" value="checked"/>
              <c:set var="WarrenActV" value="${transType.typeVolume}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuSubType eq 'Banks'}">
              <c:set var="Banks" value="checked"/>
              <c:set var="BanksV" value="${transType.typeVolume}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuSubType eq 'North Bay'}">
              <c:set var="nb" value="checked"/>
              <c:set var="NorthBayV" value="${transType.typeVolume}"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuType eq 'SWP'&& transType.wtFuType.fuSubType eq 'SWP/CVP Intertie'}">
              <c:set var="SWPIntertie" value="checked"/>
            </c:if>
            <c:if test="${transType.wtFuType.fuType eq 'CVP'&& transType.wtFuType.fuSubType eq 'SWP/CVP Intertie'}">
              <c:set var="CVPIntertie" value="checked"/>
            </c:if>
          </c:forEach>

          <ul style="width:600px;float:left;">
            <li><input type="checkbox" class="wtFuTypeId non_border validField" ${swp} name="SWP" ${disableEdit}/><label>SWP</label></li>
            <li class="subSWP ${swpSub}"><label style="padding-left:30px; font-weight: bold;">Select sub option(s) for SWP</label></li>
            <li style="padding-left:20px;" class="subSWP ${swpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${Banks} name="Banks" ${disableEdit}/><label>Banks</label>
              <label style="padding-left:32px;padding-right:22px">Volume through Banks (acre-feet) </label>
              <input type="text" name="BanksV" class="subSWPV numField ftField" value="${BanksV}" ${disableEdit}/>
            </li>
            <li style="padding-left:50px;;padding-top:10px;" class="stateContractor hidden subBanks ${swpSub}">
              <label style="padding-right:22px; font-weight: bold">Who will Provide Power</label>
              <select name="powerProvider" id="powerProvider" ${disableEdit}>
                <option value="Reclamation" ${proposal.wtTransReach.powerProvider == "Reclamation"?'selected="selected"':''}>Reclamation</option>
                <option value="DWRPower" ${proposal.wtTransReach.powerProvider == "DWRPower"?'selected="selected"':''}>Buyer paying for DWR power</option>
              </select>
            </li>
            <li style="padding-left:40px;" class="stateContractor hidden subBanks ${swpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${SWPIntertie} name="swpIntertie" ${disableEdit}/><label>SWP/CVP Intertie</label>
            </li>

            <li style="padding-left:50px;padding-bottom:10px;" class="stateContractor hidden subBanks ${swpSub}">
              <label style="padding-right:5px; font-weight: bold;">California Aqueduct Reach</label>
              <c:if test="${proposal.wtTransReach.isReach1 == 1}">
                <c:set var="isReach1" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach2a == 1}">
                <c:set var="isReach2a" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach2b == 1}">
                <c:set var="isReach2b" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach3 == 1}">
                <c:set var="isReach3" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach4 == 1}">
                <c:set var="isReach4" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach5 == 1}">
                <c:set var="isReach5" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach6 == 1}">
                <c:set var="isReach6" value="checked"/>
              </c:if>
              <c:if test="${proposal.wtTransReach.isReach7 == 1}">
                <c:set var="isReach7" value="checked"/>
              </c:if>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach1} name="isReach1" ${disableEdit}/><label>1</label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach2a} name="isReach2a" ${disableEdit}/><label>2A</label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach2b} name="isReach2b" ${disableEdit}/><label>2B</label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach3} name="isReach3" ${disableEdit}/><label>3</label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach4} name="isReach4" ${disableEdit}/><label>4</label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach5} name="isReach5" ${disableEdit}/><label>5 </label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach6} name="isReach6" ${disableEdit}/><label>6 </label>
              <input class="reach non_border" style="width:20px;" type="checkbox" ${isReach7} name="isReach7" ${disableEdit}/><label>7</label>
            </li>

            <li style="padding-left:20px;" class="subSWP ${swpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${nb} name="NorthBay" ${disableEdit}/><label>North Bay</label>
              <label style="padding-left:10px;padding-right:16px">Volume through North Bay (acre-feet) </label>
              <input type="text" name="NorthBayV" class="subSWPV numField ftField" value="${NorthBayV}" ${disableEdit}/>
            </li>
          </ul>
<!--      <div style="width:100px;float:left;"><input type="checkbox" class="wtFuTypeId non_border" name="CVP" ${cvp}/><label>CVP</label></div>-->
          <ul style="width:600px;float:left;" class="stateContractor CVP hidden">
            <li><input type="checkbox" class="wtFuTypeId non_border" ${cvp} name="CVP" ${disableEdit}/><label>CVP</label></li>
            <li class="subCVP ${cvpSub}"><label style="padding-left:30px; font-weight: bold;">Select sub option for CVP</label></li>
            <li style="padding-left:20px;" class="subCVP ${cvpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${Jones} name="Jones" ${disableEdit}/><label>Jones</label>
              <label style="padding-left:104px;padding-right:16px">Volume through Jones (acre-feet) </label>
              <input type="text" name="JonesV" class="subCVPV numField ftField" value="${JonesV}" ${disableEdit}/>
            </li>
<!--            <li style="padding-left:40px;" class="subCVP subJones ${cvpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${CVPIntertie} name="cvpIntertie" ${disableEdit}/><label>SWP/CVP Intertie</label>
            </li>-->
            <li style="padding-left:20px;" class="subCVP ${cvpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${Forbearance} name="Forbearance" ${disableEdit}/><label>Forbearance</label>
              <label style="padding-left:24px;padding-right:16px">Volume through Forbearance (acre-feet) </label>
              <input type="text" name="ForbearanceV" class="subCVPV numField ftField" value="${ForbearanceV}" ${disableEdit}/>
            </li>
            <li style="padding-left:20px;" class="subCVP ${cvpSub}">
              <input type="checkbox" class="wtFuTypeId non_border" ${WarrenAct} name="WarrenAct" ${disableEdit}/><label>Warren Act</label>
              <label style="padding-left:36px;padding-right:22px">Volume through Warren Act (acre-feet) </label>
              <input type="text" name="WarrenActV" class="subCVPV numField ftField" value="${WarrenActV}" ${disableEdit}/>
            </li>
          </ul>
          <div style="width:600px;float:left;padding-bottom:20px">
            <input type="checkbox" class="wtFuTypeId non_border validField" ${other} name="OTHER" ${disableEdit}/><label>OTHER</label>
            <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="fuType_other_help" alt="Help" title="Facilities Requested Type Other Help">
            <input type="text" class="validField ftField" name="typeDetail" value="${otherD}" style="width: 150px;" ${disableEdit}/>
            <label style="padding-left:18px;padding-right:20px">Volume (acre-feet) </label>
            <input type="text" name="otherV" class="numField ftField" value="${otherV}" ${disableEdit}/>
          </div>
        </td>
      </tr>
      <tr><th colspan="5"><hr></th></tr>
      <tr class="tableRow">
        <th>Through Delta Transfer<span class="asterisk">*</span></th>
        <td>
          <input class="non_border" 
                 style="width:20px;" 
                 type="radio" 
                 id="deltaTransferInd"
                 transId='${proposal.wtTransId}'
                 name="deltaTransferInd" 
                 value="0" 
                 <c:if test="${proposal.deltaTransferInd == 0}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>No</label>
          <input class="non_border"
                 style="width:20px;"
                 type="radio"
                 id="deltaTransferInd"
                 transId='${proposal.wtTransId}'
                 name="deltaTransferInd"
                 value="1"
                 <c:if test="${proposal.deltaTransferInd != 0}">checked="checked"</c:if> 
                 ${disableEdit}
                 onblur="window.baseInfo.saveBIField(this);"/><label>Yes</label>
        </td>
      </tr>
      <tr class="tableRow" id="exportWindow">
        <th>Requested Export Window</th>
        <td>From
          <input class="validField dateField" 
                 type="text"
                 id="reqExpFromDate"
                 transId='${proposal.wtTransId}'
                 name="reqExpFromDate" 
                 value='<fmt:formatDate value="${proposal.reqExpFromDate}" pattern="MM/dd/yyyy"/>'                 
                 ${disableEdit}
                 onChange="window.baseInfo.saveBIField(this);"/>
        </td>
        <td>To
          <input class="validField dateField" 
                 type="text" 
                 id="reqExpToDate" 
                 transId='${proposal.wtTransId}'
                 name="reqExpToDate" 
                 value='<fmt:formatDate value="${proposal.reqExpToDate}" pattern="MM/dd/yyyy"/>' 
                 ${disableEdit}
                 onChange="window.baseInfo.saveBIField(this);"/>
        </td>
      </tr>
      <tr class="tableRow hidden" id="exportWindowMsg">
        <th></th>
        <td style="padding-bottom:20px;" colspan='4'>Please explain in Comments if export is requested before July or after September.</br></td>
      </tr>
      <tr class="tableRow">
        <th>Request for Storage Prior to Export</th>
        <td>
          <select name="reqStorageExp" 
                  id="reqStorageExp" 
                  transId='${proposal.wtTransId}'
                  ${disableEdit}
                  onchange="window.baseInfo.saveBIField(this);">
            <option value="N/A" ${proposal.reqStorageExp == "N/A"?'selected="selected"':''}>N/A</option>
            <option value="Shasta" ${proposal.reqStorageExp == "Shasta"?'selected="selected"':''}>Shasta</option>
            <option value="Oroville" ${proposal.reqStorageExp == "Oroville"?'selected="selected"':''}>Oroville</option>
            <option value="Folsom" ${proposal.reqStorageExp == "Folsom"?'selected="selected"':''}>Folsom</option>
            <option value="San Luis" ${proposal.reqStorageExp == "San Luis"?'selected="selected"':''}>San Luis</option>
          </select>
        </td>
      </tr>
      
        <tr class="tableRow">
          <th>Environmental Regulatory Compliance<span class="asterisk">*</span>
            <input type="checkbox" class="validField hidden" name="envRegComplHide" ${disableEdit}/>
        </th>
        <td colspan='3'>
          <c:set var="swrcb" value=""/>
          <c:set var="ceqa" value=""/>
          <c:set var="nepa" value=""/>
          <c:if test="${proposal.wtTransSwrcb ne null}">
            <c:set var="swrcb" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtTransCeqa ne null}">
            <c:set var="ceqa" value="checked"/>
          </c:if>
          <c:if test="${proposal.wtTransNepa ne null}">
            <c:set var="nepa" value="checked"/>
          </c:if>
          <input style="width:20px;" 
                 type="checkbox" 
                 class="envRegCompl non_border validField" ${swrcb} 
                 transId='${proposal.wtTransId}'
                 name="SWRCB" ${disableEdit}
                 onclick="window.baseInfo.checkEnvRegCompliance(this);"/><label>SWRCB</label>
          <input style="width:20px;" 
                 type="checkbox" 
                 class="envRegCompl non_border validField" ${ceqa} 
                 transId='${proposal.wtTransId}'
                 name="CEQA" ${disableEdit}
                 onclick="window.baseInfo.checkEnvRegCompliance(this);"/><label>CEQA</label>
          <input style="width:20px;" 
                 type="checkbox"
                 class="envRegCompl non_border validField" ${nepa}
                 transId='${proposal.wtTransId}'
                 name="NEPA" ${disableEdit}
                 onclick="window.baseInfo.checkEnvRegCompliance(this);"/><label>NEPA</label>
        </td>
      </tr>
      <tr class="tableRow swrcb hidden">
        <th>Petition for Change Submitted to SWRCB Date</th>
        <td>
          <input class="dateField" type="text" id="" name="swrcbPetitionDate" value='<fmt:formatDate value="${proposal.wtTransSwrcb.swrcbPetitionDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>

        <th>SWRCB Order Issued Date</th>
        <td>
          <input class="dateField" type="text" id="" name="swrcbOrderDate" value='<fmt:formatDate value="${proposal.wtTransSwrcb.swrcbOrderDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>
      </tr>
      <tr class="tableRow ceqa hidden">
        <th>CEQA document submitted to OPR Date</th>
        <td>
          <input class="dateField" type="text" id="" name="ceqaSubmittedDate" value='<fmt:formatDate value="${proposal.wtTransCeqa.ceqaSubmittedDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>
        <td>
          <span style="padding-right:10px;">SCH#</span>
          <input class="validField" type="text" id="" name="schNum" value='${proposal.wtTransCeqa.schNum}' maxlength="10" ${disableEdit}/>
        </td>
        <th>CEQA document adopted Date</th>
        <td>
          <input class="dateField" type="text" id="" name="ceqaAdoptedDate" value='<fmt:formatDate value="${proposal.wtTransCeqa.ceqaAdoptedDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>
      </tr>
      <tr class="tableRow nepa hidden">
        <th>NEPA document issued Date</th>
        <td>
          <input class="dateField" type="text" id="" name="nepaIssuedDate" value='<fmt:formatDate value="${proposal.wtTransNepa.nepaIssuedDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>
        <td>
          <span style="padding-right:10px;">NEPA#</span>
          <input class="validField" type="text" id="" name="nepaNum" value='${proposal.wtTransNepa.nepaNum}' maxlength="6" ${disableEdit}/>
        </td>
        <th>NEPA document adopted Date</th>
        <td>
          <input class="dateField" type="text" id="" name="nepaAdoptedDate" value='<fmt:formatDate value="${proposal.wtTransNepa.nepaAdoptedDate}" pattern="MM/dd/yyyy"/>' ${disableEdit}/>
        </td>
      </tr>
      <tr class="tableRow">
        <th style="vertical-align: top;">Comments</th>
        <td class="fullRow" colspan='3'>
          <textarea cols="50" rows="2" 
                    maxlength="2048"
                    id="wtComm"
                    name="wtComm"
                    transId='${proposal.wtTransId}'
                    ${disableEdit}
                    onblur="window.baseInfo.saveBIField(this);">${proposal.wtComm}</textarea>
          <div style="margin-bottom: 20px; color: #565656;">You have <span id="comm_counter"></span> characters left.</div>
        </td>
      </tr>
    </table>
  </div>

  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('B').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>
      <c:set var="bgcolor" value=""/>
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('B').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="B"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('B').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments"  
                style="${bgcolor}"
                sectionKey="B"
                transId="${proposal.wtTransId}"
                ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea> 
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
               type="button"
               sectionKey="B"
               transId="${proposal.wtTransId}"
               value="Save Comment"
               onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('B')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
        </c:forEach>
      </div> -->
      
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

  <div class="tab_header isExpand">Water Rights Type and Transfer Information<span class="asterisk">*</span></div>
  <div class="contact_panel">
    <div class="water_rights_ct"></div>
    <div class="attach-button ${hideField}">
      <input class="add_agency " type="button" id="water_rights_addBtn" containerid="" value="Add Water Rights Type" onclick="return false;"/>
      <span style="color:red; font-size:10px">Click the save icon for each water right.</span>
    </div>
  </div>

  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:set var="bgcolor" value=""/>
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('C').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>      
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('C').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="C"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('C').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments"  
                style="${bgcolor}"
                sectionKey="C"
                transId="${proposal.wtTransId}"
                ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea> 
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
             type="button"
             sectionKey="C"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('C')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
        </c:forEach>
      </div>-->
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

  <div class="tab_header isExpand">Attachments</div>
  <div class="contact_panel">
    <div id="typeMenu">
      <div class="head_explain">
        To add your attachments, select the Add Attachment button below. In the pop-up window, select which properties belong to your document and upload.
      </div>
    </div>
    <div class="attachment-container" id="attachment-base-info" typeid="BI"></div>
    <div class="attach-button ${hideField}">
      <input class="attachButton add_agency" type="button" id="attachBaseInfo" typeid="BI" containerid="attachment-base-info" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>

  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:set var="bgcolor" value=""/>
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('D').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>      
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('D').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="D"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('D').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments" 
                style="${bgcolor}"
                sectionKey="D"
                transId="${proposal.wtTransId}"
                ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea>
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
             type="button"
             sectionKey="D"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('D')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
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

  <div class="help-ct"></div>
</div>

<script type="text/javascript">
  $(document).ready(function () {
    window.baseInfo = new BaseInfo();
  });
</script>
<style type="text/css">
  .head_explain{
    margin-top: 5px;
    margin-bottom: 10px;
    font-size: 16px;

  }
  .isModified{
    background-color: yellow;
  }
  .custom-combobox-input{
    background-color: #fff;
  }
  div.forReviewer {
    width:90%;
    margin-top:20px;
    background: #ffe2b1 none repeat scroll 0 0;
  }
  div.checkBoxCt {
    display: inline;
  }
  div.forReviewer .checkBoxCt label {
    color: red;
    font-weight: bold;
  }
  div.forReviewer .checkBoxCt input[type="checkbox"]:checked ~ label {
    color: green;
    font-weight: normal;
  }
  .input-disabled  {
    background-color:#EBEBE4;
    border:1px solid #ABADB3;
    padding:2px 1px;
  }
  img.calBtn {
    cursor: pointer;
  }
</style>