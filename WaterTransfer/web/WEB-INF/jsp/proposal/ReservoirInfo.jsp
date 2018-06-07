<%--
    Document   : ReservoirInfo
    Created on : Apr 1, 2015, 10:12:37 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="reservoir_tab">
  <c:set var="wtReservoirId" value="${proposal.wtReservoir.wtReservoirId}"/>
  <div id="rv-container">
    <table id="create-rv-table">
      <tr class="tableRow" style="display:none;">
        <th>Reservoir ID</th>
        <td><input class="fullRow" type="text" name="wtReservoirId" id="wtReservoirId" value="${proposal.wtReservoir.wtReservoirId}"/></td>
      </tr>
      <tr>
        <th>Dam Location</th>
        <td>
          <label style="font-size: 12px;display:flex;">Latitude </label>
          <input class="floatField" 
                 type="text" 
                 id="locationLat"
                 name="locationLat" 
                 reservoirId='${wtReservoirId}'
                 value="${fn:escapeXml(proposal.wtReservoir.locationLat)}" 
                 maxlength="9" ${disableEdit}
                 onblur="window.reservoirInfo.saveRvField(this);"/>
        </td>
        <td>
          <label style="font-size: 12px;display:flex;">Longitude </label>
          <input class="floatField" 
                 type="text" 
                 id="locationLong"
                 name="locationLong"
                 reservoirId='${wtReservoirId}'
                 value="${fn:escapeXml(proposal.wtReservoir.locationLong)}" 
                 maxlength="9" ${disableEdit}
                 onblur="window.reservoirInfo.saveRvField(this);"/>
        </td>
      </tr>
      <tr class="tableRow">
        <th colspan='1'>Water Transfer Amount (acre-feet)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="waterTransQuaRV" 
                   name="waterTransQuaRV" 
                   reservoirId='${wtReservoirId}'
                   value="${proposal.wtReservoir.waterTransQua}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.reservoirInfo.saveRvField(this);"/></td>
      </tr>
      <tr class="tableRow">
        <th colspan='1'>Top of allowable conservation storage (acre-feet)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="topAllowStorage" 
                   name="topAllowStorage"
                   reservoirId='${wtReservoirId}'
                   value="${proposal.wtReservoir.topAllowStorage}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.reservoirInfo.saveRvField(this);"/></td>
      </tr>
      <tr class="tableRow">
        <th colspan='1'>End-of-Season target storage, if applicable (acre-feet)</th>
        <td><input class="validField numField" 
                   type="text" 
                   id="targetStorage" 
                   name="targetStorage"
                   reservoirId='${wtReservoirId}'
                   value="${proposal.wtReservoir.targetStorage}"
                   maxlength="9" ${disableEdit}
                   onblur="window.reservoirInfo.saveRvField(this);"/></td>
      </tr>

      <tr>
        <th>Purpose of Reservoirs </th>
      </tr>
      <tr class="tableRow purposeResCt">
        <td colspan='3'>
          <div class="${hideField}">
            <select autocomplete="off" class="basincombo" name="combobox" id="combobox" multiple>
              <c:forEach var="purpose" items="${LookupDataContext.purposeReservoir}">
                <option value="${purpose.wtPurposeId}" json='{"wtPurposeId":${purpose.wtPurposeId},"purpose":"${purpose.purpose}"}'>${purpose.purpose}</option>
              </c:forEach>
            </select>
          </div>
          <div id="purResList" class="${isNew}" style="">
            <c:if test="${proposal.wtReservoir.getWtPurposeCollection().size()>0}">
              <c:forEach items="${proposal.wtReservoir.getWtPurposeCollection()}" var="purpose">
                <span class="myButton">
                  <input type="hidden" name="purposeId" autocomplete="off" value="${purpose.wtPurposeId}" />
                  ${purpose.purpose}
                  <c:if test='${hideField == ""}'>
                    <span class="ui-icon ui-icon-circle-close purposeResItem" style="display: inline-block; vertical-align: bottom;"></span>
                  </c:if>
                </span>
              </c:forEach>
            </c:if>
          </div>
        </td>
      </tr>

      <c:set var="isSellerAuth" value="${proposal.wtReservoir.isSellerAuth}" />
      <tr class="tableRow">
        <th colspan="1">Is seller authorized to operate reservoir?</th>
        <td>
          <label for="isSellerAuthNo" style="font-size: 12px;">No</label>
          <input class="non_border"
                 type="radio"
                 id="isSellerAuthNo"
                 name="isSellerAuth"
                 reservoirId='${wtReservoirId}'
                 ${disableEdit}
                 value="0"
                 <c:if test="${isSellerAuth==0}">checked</c:if> 
                 onblur="window.reservoirInfo.saveRvField(this);"/>
          <label for="isSellerAuthYes" style="font-size: 12px;">Yes</label>
          <input class="non_border"
                 type="radio"
                 id="isSellerAuthYes"
                 name="isSellerAuth"
                 reservoirId='${wtReservoirId}'
                 ${disableEdit}
                 value="1"
                 <c:if test="${isSellerAuth!=0}">checked</c:if> 
                 onblur="window.reservoirInfo.saveRvField(this);"/>
          </td>
        </tr>

        <tr class='hidden'>
          <th>Who is authorized to operate reservoir?</th>
          <td><input class="validField"
                     type="text"
                     name="authOperator"
                     id ="authOperator"                     
                     reservoirId='${wtReservoirId}'
                     value="${fn:escapeXml(proposal.wtReservoir.authOperator)}"
                     ${disableEdit}
                     onblur="window.reservoirInfo.saveRvField(this);"/>
        </td>
      </tr>
    </table>
  </div>

  <div class="tab_header isExpand">List any reservoirs located downstream of project reservoir or point of current return</div>
  <div class="contact_panel">
    <div class="targetStorageCt">
      <table class="targetStorageTb">
        <thead>
          <tr>
            <th>Reservoir Name <span style="color:red;">*</span></th>
            <th>Location Latitude</th>
            <th>Location Longitude</th>
            <th>Name of Contact</th>
            <th>Phone Number</th>
            <th>E-mail Address</th>
            <th class="${hideField}">Save</th>
            <th class="${hideField}">Edit</th>
            <th class="${hideField}">Remove</th>
          </tr>
        </thead>
        <tbody>
          <tr class="">
            <td class="hidden"><input type="text" name="wtRvTarstorId" value=""/></td>
            <td><input type="text" name="damName" value="" maxlength="64"/></td>
            <td><input class="floatField" type="text" name="storageLocationLat" value="" /></td>
            <td><input class="floatField" type="text" name="storageLocationLong" value="" /></td>
            <td><input type="text" name="operator" value="" maxlength="64"/></td>
            <td><input type="text" name="phoneNumber" value="" maxlength="20"/></td>
            <td><input type="text" name="email" value="" maxlength="64"/></td>
            <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Attachment" title="Save Water Rights"/></td>
            <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Water Rights"/></td>
            <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Water Rights"/></td>
          </tr>
          <c:forEach items="${proposal.wtReservoir.getWtRvTarstorCollection()}" var="storage">
            <tr>
              <td class="hidden"><input type="text" name="wtRvTarstorId" value="${fn:escapeXml(storage.wtRvTarstorId)}"/></td>
              <td><input type="text" disabled="true" name="damName" value="${fn:escapeXml(storage.damName)}" maxlength="64"/></td>
              <td><input class="floatField" type="text" disabled="true" name="storageLocationLat" value="${fn:escapeXml(storage.locationLat)}" /></td>
              <td><input class="floatField" type="text" disabled="true" name="storageLocationLong" value="${fn:escapeXml(storage.locationLong)}" /></td>
              <td><input type="text" disabled="true" name="operator" value="${fn:escapeXml(storage.operator)}" maxlength="64"/></td>
              <td><input type="text" disabled="true" name="phoneNumber" value="${fn:escapeXml(storage.phoneNumber)}" maxlength="20"/></td>
              <td><input type="text" disabled="true" name="email" value="${fn:escapeXml(storage.email)}" maxlength="64"/></td>
              <td class="${hideField}"><img class="save-icon hidden" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Attachment" title="Save Water Rights"/></td>
              <td class="${hideField}"><img class="edit-icon ${hideField}" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Water Rights"/></td>
              <td class="${hideField}"><img class="delete-icon ${hideField}" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Water Rights"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <span style="color:red;font-size: 11px;padding:0 0 0 5px;" ${hideField}>To save the reservoir to the list, click on the save icon.</span>
    </div>
    <div class="attach-button">
      <input class="add_storage"
             type="button"
             value="Add Reservoir"
             onclick="return false;"
             ${hideField}/>
    </div>
  </div>

  <div class="tab_header isExpand">Attachments</div>
  <div class="contact_panel">
    <div id="typeMenu">
      <div class="head_explain">
        To add your attachments, select the Add Attachment button below. In the pop-up window, select which properties belong to your document and upload.
      </div>
    </div>
    <div class="attachment-container" id="attachment-reservoir-info" typeid="RV"></div>
    <div id="rv_uploadFile_ct" typeid="RV"></div>
    <div class="attach-button ${hideField}">
      <input class="attachButton add_agency" type="button" id="attachReservoir" typeid="RV" containerid="attachment-reservoir-info" attachid="${proposal.wtReservoir.wtReservoirId}" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>
    
  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:set var="bgcolor" value=""/>
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('F').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>      
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('F').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="F"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('F').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments" 
                style="${bgcolor}"
                sectionKey="F"
                transId="${proposal.wtTransId}"
                 ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea>  
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
             type="button"
             sectionKey="F"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('F')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
        </c:forEach>
      </div>-->
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('F')}" var="tNote" varStatus="iterator">
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
      
<script type="text/javascript">
$(document).ready(function(){
  window.reservoirInfo = new ReservoirInfo();
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
  .purposeResCt {
    font-size: 12px;
  }
</style>

