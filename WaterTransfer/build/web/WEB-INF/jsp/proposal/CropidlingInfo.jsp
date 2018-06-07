<%--
    Document   : CropidlingInfo
    Created on : Apr 1, 2015, 10:14:35 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="cropidling_tab">
  <c:set var="wtCropIdlingId" value="${proposal.wtCropIdling.wtCropIdlingId}"/>
  <div id="ci-container">
    <table id="create-ci-table">
      <tr class="tableRow" style="display:none;">
        <th>Crop Idling ID</th>
        <td style="width:100px;">
          <input class="" type="text" name="wtCropIdlingId" id="wtCropIdlingId" value="${proposal.wtCropIdling.wtCropIdlingId}" ${disableEdit}/>
        </td>
      </tr>
      <tr class="tableRow">
        <th>Water Transfer Amount (acre-feet)
<!--          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" 
               class="help-icon"
               id="waterTransQuaCI_help" 
               alt="Help" 
               title="Water Transfer Amount Help"
               onclick="window.cropidlingInfo.helpMsgPopup(this)"/>-->
        </th>
        <td><input class="numField" 
                   type="text" 
                   id="waterTransQuaCI" 
                   name="waterTransQuaCI" 
                   cropIdlingId='${wtCropIdlingId}'
                   value="${proposal.wtCropIdling.waterTransQua}" 
                   maxlength="9"
                   ${disableEdit}
                   onblur="window.cropidlingInfo.ciWtAmountChange(this);"/></td>
      </tr>
<!--      <tr class="tableRow">
        <th>Proposed Transfer Acreage by Crop Idling (Acres)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="proTransferByCI" 
                   name="proTransferByCI"
                   cropIdlingId='${wtCropIdlingId}'
                   value="${proposal.wtCropIdling.proTransferByCI}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.cropidlingInfo.saveCIField(this);"/></td>
      </tr>-->
<!--      <tr class="tableRow">
        <th>Proposed Transfer Acreage by Crop Shift (Acres)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="proTransferByCS" 
                   name="proTransferByCS" 
                   cropIdlingId='${wtCropIdlingId}'
                   value="${proposal.wtCropIdling.proTransferByCS}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.cropidlingInfo.saveCIField(this);"/></td>
      </tr>-->
      <tr class="tableRow">
        <th>Total Proposed Transfer Acreage
<!--          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" 
               class="help-icon"
               id="totalTransferAcr_help" 
               alt="Help" 
               title="Total Proposed Transfer Acreage Help"
               onclick="window.cropidlingInfo.helpMsgPopup(this)"/>-->
        </th>
        <td>
          <input class="numField" 
                 type="text" 
                 id="totalTransferAcr" 
                 name="totalTransferAcr" 
                 ${disableEdit}
                 value="${proposal.wtCropIdling.totalTransferAcr}" />
          <!--<img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="totalProposedAcreage_help" alt="Help" title="Total Proposed Acreage">-->
        </td>
      </tr>
      <tr class="tableRow">
        <th>Current-year Farm Service Agency (Acres)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="currentFsAgency" 
                   name="currentFsAgency" 
                   cropIdlingId='${wtCropIdlingId}'
                   value="${proposal.wtCropIdling.currentFsAgency}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.cropidlingInfo.saveCIField(this);"/></td>
      </tr>
      <tr class="tableRow">
        <th colspan='2'>
          <label style="margin-right:20px;">Does this proposal include reservoir release? </label>
          <label style="margin-left:20px;">
            <input class="non_border" 
                   type="radio" 
                   id="isResReleaseCI" 
                   name="isResReleaseCI" 
                   cropIdlingId='${wtCropIdlingId}'
                   value="0" <c:if test="${proposal.wtCropIdling.isResRelease == 0}">checked="checked"</c:if> ${disableEdit}
                   onblur="window.cropidlingInfo.saveCIField(this);"/>No</label>
          <label style="margin-left:20px;">
            <input class="non_border" 
                   type="radio" 
                   id="isResReleaseCI" 
                   name="isResReleaseCI" 
                   cropIdlingId='${wtCropIdlingId}'
                   value="1" <c:if test="${proposal.wtCropIdling.isResRelease == 1}">checked="checked"</c:if> ${disableEdit}
                   onblur="window.cropidlingInfo.saveCIField(this);"/>Yes</label>
          </th>
        </tr>
        <tr class="tableRow hidden" id="isResReleaseMsgCI">
          <th style="padding-left:50px;" colspan='3'>If yes, provide reservoir release schedule and all contact information for reservoir operator.</th>
        </tr>
      </table>
    </div>
    
    <div class="tab_header isExpand">Water Transfer Amount</div>
    <div class="contact_panel">
      <table id="ci-croptype-table" class="${hideTable}">
        <thead>
          <tr class="croptype_header">
            <th style="width:18%;">Crop: Without Transfer</th>
            <th style="width:18%;">Crop: With Transfer</th>
            <th style="width:17%;">Proposed Transfer Acreage</th>            
            <th style="width:17%;">Evapotranspiration Rate of Applied Water (ETAW) (acre-feet/acre)</th>
            <th style="width:17%;">Water Transfer Amount (acre-feet)</th>
            <th class="${hideField}">Add</th>
            <th class="${hideField}">Edit</th>
            <th class="${hideField}">Remove</th>
          </tr>
        </thead>
        <tbody>
          <tr class="hidden">
            <td>
              <select class="crop_type" style="width:150px;" name="cropWithoutTransfer" onchange="window.cropidlingInfo.selectCroptype(this);">
                <c:forEach var="cropType" items="${LookupDataContext.cropType}">
                <option value="${cropType.cropType}" etaw="${cropType.cropEtaw}">${cropType.cropType}</option>
                </c:forEach>
              </select>
            </td>
            <td>
              <select class="crop_type" style="width:150px;" name="cropWithTransfer" onchange="window.cropidlingInfo.selectCroptype(this);">
                <c:forEach var="cropType" items="${LookupDataContext.cropType}">
                <option value="${cropType.cropType}" etaw="${cropType.cropEtaw}">${cropType.cropType}</option>
                </c:forEach>
              </select>
            </td> 
            <td class="input"><input type="text" class="numField" value="" name="proTransferTotal" maxlength="12"/></td>
            <td class="input"><input type="text" class="numField" value="" name="cropEtaw" maxlength="5"/></td>
            <td class="input"><input type="text" class="numField" value="" name="waterTransferAmount" maxlength="9"/></td>
            <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/add.png" alt="Add Crop Type" title="Add Crop Type"/></td>
            <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Crop Type" title="Edit Crop Type"/></td>
            <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Crop Type" title="Remove Crop Type"/></td>
            <input type="hidden" value="" name="wtCiCroptypeId"/>
          </tr>
          <c:forEach var="ciCroptype" items="${proposal.wtCropIdling.croptypeCollection}"> 
          <tr>
            <td class="crop_without_transfer">${ciCroptype.cropWithoutTransfer}</td>
            <td class="crop_with_transfer">${ciCroptype.cropWithTransfer}</td>
            <td><input type="text" class="numField" value="${ciCroptype.proTransferTotal}" name="proTransferTotal" disabled/></td>
            <td><input type="text" class="numField" value="${ciCroptype.cropEtaw}" name="cropEtaw" disabled/></td>
            <td><input type="text" class="numField" value="${ciCroptype.waterTransferAmount}" name="waterTransferAmount" disabled/></td>
            <td class="${hideField}"><img class="save-icon hidden" src="${pageContext.request.contextPath}/resources/images/icons/add.png" alt="Add Crop Type" title="Add Crop Type"/></td>
            <td class="${hideField}"><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Crop Type" title="Edit Crop Type"/></td>
            <td class="${hideField}"><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Crop Type" title="Remove Crop Type"/></td>
            <input type="hidden" value="${ciCroptype.wtCiCroptypeId}" name="wtCiCroptypeId"/>            
          </tr>
          </c:forEach>
        </tbody>   
      </table>
      
      <div class="attach-button ${hideField}">
        <input class="add_agency " type="button" id="crop_type_addBtn" containerid="" value="Add New Crop Type" onclick="return false;"/>
        <span style="color:red; font-size:10px">Click the add icon for each Crop Type.</span>
      </div>
    </div>
    
    <div class="tab_header_report isExpand">Transfer Water Estimated Schedule
      <img src="${pageContext.request.contextPath}/resources/images/icons/calculator.png" 
               class="calBtn" 
               id="PTQcalBtn" 
               alt="Calculator"
               title="Reset Transfer Water Estmated Schedule"
               onclick="window.cropidlingInfo.resetCiMonthly();">
    </div>
    <div class="contact_panel">
      <c:set var="wtTotal" value="${proposal.wtCropIdling.waterTransQua}"/>
      <c:set var="ciMonthly" value="${proposal.wtCropIdling.wtCiMonthly}"/>
      <c:set var="mayEtaw" value="15"/>
      <c:set var="juneEtaw" value="22"/>
      <c:set var="julyEtaw" value="24"/>
      <c:set var="augustEtaw" value="24"/>
      <c:set var="septemberEtaw" value="15"/>
      <c:if test="${ciMonthly.mayEtaw gt 0}"><c:set var="mayEtaw" value="${ciMonthly.mayEtaw}"/></c:if>
      <c:if test="${ciMonthly.juneEtaw gt 0}"><c:set var="juneEtaw" value="${ciMonthly.juneEtaw}"/></c:if>
      <c:if test="${ciMonthly.julyEtaw gt 0}"><c:set var="julyEtaw" value="${ciMonthly.julyEtaw}"/></c:if>
      <c:if test="${ciMonthly.augustEtaw gt 0}"><c:set var="augustEtaw" value="${ciMonthly.augustEtaw}"/></c:if>
      <c:if test="${ciMonthly.septemberEtaw gt 0}"><c:set var="septemberEtaw" value="${ciMonthly.septemberEtaw}"/></c:if>
      <table id="ci-monthly-table">
        <thead>
          <tr class="monthly_header">
            <th style="width:150px;"></th>
            <th style="width:13%;">May</th>
            <th style="width:13%;">June</th>
            <th style="width:13%;">July</th>
            <th style="width:13%;">August</th>
            <th style="width:13%;">September</th>
            <th style="width:13%;">Total</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>ETAW Pattern (%)</td>
            <td><input class="intField"
                       id="mayEtaw"
                       maxlength="2" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="mayEtaw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${mayEtaw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="intField"
                       id="juneEtaw"
                       maxlength="2" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="juneEtaw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${juneEtaw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="intField"
                       id="julyEtaw"
                       maxlength="2" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="julyEtaw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${julyEtaw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="intField"
                       id="augustEtaw"
                       maxlength="2" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="augustEtaw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${augustEtaw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="intField"
                       id="septemberEtaw"
                       maxlength="2" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="septemberEtaw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${septemberEtaw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td id="totalEtaw">100</td>
          </tr>
          <tr>
            <td>Transfer Water (acre-feet)
<!--              <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" 
                   class="help-icon"
                   id="TWMonthly_help" 
                   alt="Help" 
                   title="Transfer Water Monthly Help"
                   onclick="window.cropidlingInfo.helpMsgPopup(this)"/>-->
            </td>
            <td><input class="numField"
                       id="mayTw"
                       maxlength="12" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="mayTw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${ciMonthly.mayTw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="numField"
                       id="juneTw"
                       maxlength="12" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="juneTw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${ciMonthly.juneTw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="numField"
                       id="julyTw"
                       maxlength="12" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="julyTw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${ciMonthly.julyTw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="numField"
                       id="augustTw"
                       maxlength="12" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="augustTw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${ciMonthly.augustTw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td><input class="numField"
                       id="septemberTw"
                       maxlength="12" ${disableEdit}
                       style="width:60px;"
                       type="text"
                       name="septemberTw"
                       cropIdlingId='${wtCropIdlingId}'
                       value='${ciMonthly.septemberTw}'
                       onblur="window.cropidlingInfo.saveCiMonthly(this);"/></td>
            <td id="totalTw"></td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="fieldInfo">
      The field information for all participating fields must be provided on the Field Data Entry Form, which can be downloaded.<br/><br/>

    <c:set var="title" value="Field Data Entry Form"/>
<!--        <a class="lnkbtn" href="${pageContext.request.contextPath}/attachment/downloadTemplate?title=${title}">Download Template</a>-->
    <span style="text-decoration: underline; color: #0073ea!important; padding-left: 30px; "><a class="downloadImg" href="${pageContext.request.contextPath}/attachment/downloadTemplate?title=${title}"><img width="40" height="40" src="${pageContext.request.contextPath}/resources/images/icons/excel_page.png" alt="Download Template" title="Download Template">
        Download Field Data Entry Form</span></a>
      <br/><br/>
      The completed form will then be uploaded as an attachment.   </div>

  <div class="tab_header isExpand">Attachments</div>
  <div class="contact_panel">
    <div id="typeMenu">
      <div class="head_explain">
        To add your attachments, select the Add Attachment button below. In the pop-up window, select which properties belong to your document and upload.
      </div>
    </div>
    <div class="attachment-container" id="attachment-cropidling-info" typeid="CI"></div>
    <div class="attach-button ${hideField}">
      <input class="attachButton add_agency" type="button" id="attachCropIdling" typeid="CI" containerid="attachment-cropidling-info" attachid="${proposal.wtCropIdling.wtCropIdlingId}" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>

  <div class="tab_header isExpand">Map/Shapefile Attachments</div>
  <div class="contact_panel">
    <div class="map-attachment-container" id="map-attachment-cropidling" typeid="CI_MAP"></div>
    <div class="attach-button ${hideField}">
      <input class="attachButton add_agency" type="button" id="attachCropIdlingMap" typeid="CI_MAP" containerid="map-attachment-cropidling" attachid="${proposal.wtCropIdling.wtCropIdlingId}" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>

  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:set var="bgcolor" value=""/>
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('E').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>      
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('E').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="E"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('E').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments"   
                style="${bgcolor}"
                sectionKey="E"
                transId="${proposal.wtTransId}"
                ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea>     
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
             type="button"
             sectionKey="E"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('E')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
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
      
  <div class="help-ct"></div>
</div>
      
<script type="text/javascript">
$(document).ready(function(){
  window.cropidlingInfo = new CropidlingInfo();
  window.baseInfo = new BaseInfo();
});
</script>
<style type="text/css">
  .link{
    font-size: 15px;
    color: #F00;
    cursor: pointer;
    margin-bottom: 20px;
  }

  .fieldInfo {
    background-color: #cfe9f1;
    margin-bottom: 20px;
    padding: 10px 10px 15px;
    font-size: 14px;
    font-weight: normal;
  }
  #ci-monthly-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #ci-monthly-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #ci-monthly-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }
  .monthly_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  #ci-croptype-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #ci-croptype-table th{
    text-align: center;
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #ci-croptype-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }
  .croptype_header {
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