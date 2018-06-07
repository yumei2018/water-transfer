<%--
    Document   : associateWells
    Created on : Jun 22, 2015, 9:55:54 AM
    Author     : pheng
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="hideTable" value=""/>
<c:set var="hideWell" value="hidden"/>
<c:set var="hideField" value="hidden"/>
<c:set var="disableEdit" value="disabled"/>
<c:if test="${isEmpty}">
  <c:set value="hideTable" var="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="hideWell" value=""/>
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()}">
  <c:if test="${(status.statusName eq 'DRAFT')||(status.statusName eq 'INCOMPLETE')}">
    <c:set var="disableEdit" value=""/>
    <c:set var="hideField" value=""/>
  </c:if>
  <c:if test="${(status.statusName eq 'DRAFT') || (status.statusName eq 'SUBMITTED')
                || (status.statusName eq 'UREVIEW') || (status.statusName eq 'INCOMPLETE')
                  || (status.statusName eq 'PCOMPLETE')||(status.statusName eq 'PAPPROVED')}">
    <c:set var="hideWell" value=""/>
  </c:if>
</c:if>
  
<%--<c:if test="${isEmpty}">
  <c:set value="hidden" var="hide"/>
</c:if>
<c:if test="${sessionScope.USER.isReviewer()}">
  <c:set var="isReviewer" value="hidden"/>
  <c:set var="disableEdit" value="disabled"/>
</c:if>
<c:if test="${sessionScope.USER.isBuyersRepresentative()||sessionScope.USER.isUSBR()}">
  <c:set var="isReviewer" value="hidden"/>
  <c:set var="disableEdit" value="disabled"/>
</c:if>--%>
<table id="associateWellCt" class="tablesorter ${hideTable}">
  <thead>
    <tr>
      <th>Site Code</th>
      <th>State Well Number</th>
      <th>Local Well Name</th>
      <th>Well Type</th>
      <th>Power Source</th>
      <th>Data Collection Agency</th>
      <th class="${hideWell}">Edit</th>
      <th class="${hideWell}">Remove</th>
      <!--            <th>Complete</th>-->
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${wellList.iterator()}" var="list" varStatus="count">
      <tr>
        <c:set var="wellMonitoring" value="No"/>
        <c:set var="wellTransfer" value="No"/>
        <c:set var="wellType" value=""/>
        <c:set var="powerSource" value='' />
        <c:set var="dataCollectAgency" value='' />
        <c:if test='${list.optString("wellMonitoring") eq 1}'>
          <c:set var="wellType" value="Monitoring"/>
          <c:set var="wellMonitoring" value="Yes"/>
          <c:set var="powerSource" value='N/A' />
          <c:set var="dataCollectAgency" value='${list.optString("dataCollectAgency")}' />
        </c:if>
        <c:if test='${list.optString("wellTransfer") eq 1}'>
          <c:set var="wellType" value="Transfer"/>
          <c:set var="wellTransfer" value="Yes"/>
          <c:set var="powerSource" value='${list.optString("powerSource")}' />
          <c:set var="dataCollectAgency" value='N/A' />
        </c:if>
        <td fieldName="wtWellNum">${list.optString("wtWellNum")}</td>
        <td class="stationNum" fieldName="stateWellNum">
          <a href='http://www.water.ca.gov/waterdatalibrary/groundwater/hydrographs/brr_hydro.cfm?CFGRIDKEY=${list.optString("casgenStationId")}' onClick="" target="_blank">
            <span class="stateWellNum">${list.optString("stateWellNum")}</span>
          </a>
        </td>
        <td fieldName="localWellId">${list.optString("localWellId")}</td>
        <td fieldName="wellType">${wellType}</td>
        <td fieldName="powerSource">${powerSource}</td>
        <td fieldName="dataCollectAgency">${dataCollectAgency}</td>
        <td class="${hideWell}"><img class="editWells" src="${pageContext.request.contextPath}/resources/images/icons/table_edit.png"/></td>
        <td class="${hideWell}"><img class="removeWells" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png"/></td>
        <td class="hidden" fieldName="wellTransfer">${wellTransfer}</td>
        <td class="hidden" fieldName="wellMonitoring">${wellMonitoring}</td>
        <td class="hidden" fieldName="wellCapacity">${list.optString("wellCapacity")}</td>
        <td class="hidden" fieldName="meterModel">${list.optString("meterModel")}</td>
        <td class="hidden" fieldName="meterMake">${list.optString("meterMake")}</td>
        <td class="hidden" fieldName="lastCalibrateDate">${list.optString("lastCalibrateDate")}</td>
        <td class="hidden" fieldName="meterUnits">${list.optString("meterUnits")}</td>
        <td class="hidden" fieldName="firstMeasureDate">${firstMeasureDate}</td>
        <td class="hidden" fieldName="lastMeasureDate">${lastMeasureDate}</td>
        <td class="hidden" fieldName="serialNum">${list.optString("serialNum")}</td>
        <td class="hidden" fieldName="meterLastInstall">${list.optString("meterLastInstall")}</td>
        <td class="hidden" fieldName="wtWellId">${list.optString("wtWellId")}</td>
        <td class="hidden" fieldName="casgenStationId">${list.optString("casgenStationId")}</td>
        <td class="hidden" fieldName="sKey">${list.optString("wtWellNum")}</td>
        <td class="hidden" fieldName="rowNumber">H${count.index+1}</td>
        <td class="hidden checklist">${list.optBoolean("checklist")}</td>
        <c:set var="lat" value='${list.optString("wtWellNum")}lat'/>
        <c:set var="lng" value='${list.optString("wtWellNum")}lng'/>
      </tr>
    </c:forEach>
  </tbody>
</table>
<div class="attach-button ${hideField}">
  <input type="button" 
         class="add_agency addWell"
         attachid=""
         value="Select Well from WDL"
         onclick="window.associatedWell.initIframeMapDialog();"/>
  <label style="padding-left:15px;font-size:12px;padding-right:103px" class="">If you cannot find the Well, Click <a href="https://www.casgem.water.ca.gov" style="cursor:pointer;text-decoration:underline;color:red" target="_blank">here</a> to submit a new well to CASGEM</label>
  <input type="button" onclick="return false;" value="Export to Excel" class="add_agency excel_export"/>
</div>

<div id="wellListCt" class="hidden">
  <div id="well-info-left">
    <div id="well-info-ct">
      <div style="margin:10px;" class="non-editable-row">
        <input class="hidden" type="text" name="casgenStationId" value="" disabled/>
        <span>Site Code: <input type="text" name="wtWellNum" value="" disabled /></span>
        <span>State Well Number: <input type="text" name="stateWellNum" value="" disabled/></span>
        <span>Local Well Name: <input type="text" name="localWellId" value="" disabled/></span>
      </div>
      <div style="margin:10px;" class="non-editable-row">
        <span>Well Depth (feet bgs): <input type="text" name="totalDepth" value="" disabled /></span>
        <span>Top Perforation (feet bgs): <input type="text" name="topPerforation" value="" disabled /></span>
        <span>Bottom Perforation (feet bgs) <input type="text" name="bottomPerforation" value="" disabled/></span>
      </div>
      <div style="margin:10px;" class="non-editable-row">
        <span>First Measure Date: <input type="text" name="firstMeasureDate" value="" disabled/></span>
        <span>Last Measure Date: <input type="text" name="lastMeasureDate" value="" disabled/></span>
      </div>
      <div style="margin:10px;" class="non-editable-row">
        <span>Latitude: <input type="text" name="latitude" disabled/></span>
        <span>Longitude: <input type="text" name="longitude" disabled/></span>
      </div>
    </div>

    <div style="margin:10px 10px 20px 10px;">
      <fieldset style="margin:10px 0px;">
        <legend>Well Type:</legend>
        <input class="non_border" type="radio" wellname="wellTransfer" name="welltype" id="wellTransfer" value=""/>
        <label style="padding-right:60px;" for="wellTransfer">Transfer Well</label>
        <input class="non_border" type="radio" wellname="wellMonitoring" name=welltype id="wellMonitoring" value=""/>
        <label for="wellMonitoring">Monitoring Well</label>
      </fieldset>
      <span class="isTransfer">Meter Last Calibrated Date: <input class="dateField" type="text" name="lastCalibrateDate" style="width:120px;"/></span>
      <span class="isTransfer">Meter Make: <input type="text" name="meterMake" style="width:115px;"/></span>
      <span class="isTransfer">Meter Model: <input type="text" name="meterModel" style="width:80px;"/></span>
      <div style="margin-top: 15px;">
        <span class="isTransfer">Date of Last Flow Meter Installation Certification:
          <input type="text" name="meterLastInstall" style="width:110px;"/>
        </span>
        <span class="isTransfer">
          <label for="serialNum">Meter Serial Number:</label>
          <input class="" type="text" maxlength="10" name="serialNum" id="serialNum" style="width:100px;"/>
        </span>
      </div>

      <div style="margin-top: 15px;">
        <span class="isTransfer">Power Source:
          <select style="width:150px;" name="powerSource">
            <option value="Electric">Electric</option>
            <option value="Diesel">Diesel</option>
            <option value="Natural Gas">Natural Gas</option>
            <option value="Other">Other</option>
          </select>
        </span>
        <span class="isTransfer" style="margin-top: 15px;">Well Capacity:
          <input type="number" name="wellCapacity" style="width:150px;"/>
          <input class="non_border" type="radio" name="meterUnits" value="CFS"/>CFS
          <input class="non_border" type="radio" name="meterUnits" value="GPM"/>GPM
          <img class='tooltipIcon' title="Pumping rate of the well" alt="Help" src="${pageContext.request.contextPath}/resources/images/icons/help.png">
        </span>
      </div>
      <span class="isMonitoring">Data Collection Agency:
        <select style="width:150px;" name="dataCollectAgency">
          <option value="">Please select...</option>
          <option value="Seller">Seller</option>
          <option value="DWR">DWR</option>
          <option value="USGS">USGS</option>
          <option value="Other">Other</option>
        </select>
      </span>
    </div>

  </div><div id="well-info-right">
    <div class="associateWellMapCt"></div>
  </div>

  <div class="wellButtonsCt">
    <div style="float:left;">
      <input class="add_agency" 
             style="margin: 3px;" 
             type="button" 
             name="attachFile" 
             value="Add Attachment" 
             onclick="window.associatedWell.initAttachFile();"/>
    </div>
  </div>       

  <!--load the well table here-->
  <div class="associateWellTableCt"></div>        

  <!--load the form dialog window-->
  <!--<div id="dialogCt"></div>-->
</div>

<script>
  $(document).ready(function() {
    window.associatedWell = new AssociateWells();
  });
</script>
