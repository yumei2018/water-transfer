<%--
    Document   : waterRightsTable
    Created on : Jul 7, 2015, 10:01:00 AM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="hideTable" value="hidden"/>
<c:set var="hideField" value="hidden"/>
<c:set var="disableEdit" value="disabled"/>
<c:if test="${hasData}">
  <c:set var="hideTable" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(status.statusName eq 'DRAFT')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(status.statusName eq 'INCOMPLETE')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<table class="water_rights_table ${hideTable}">
  <thead>
    <tr class="" >
      <th style="text-align: center;">Water Rights Type</th>
      <th colspan='2'>Governing Document</th>
      <th>Proposed Volume for transfer (acre-feet)</th>
      <th class="${hideField}">Save</th>
      <th class="${hideField}">Edit</th>
      <th class="${hideField}">Remove</th>
    </tr>
  </thead>
  <tbody>
    <tr class="hidden">
      <td>
        <select name="waterRights">
          <option value=""></option>
          <option value="Statement">Pre-1914</option>
          <option value="Application">Post-1914</option>
<!--          <option value="Decree">Adjudicated</option>
          <option value="Contract">Contract</option>-->
        </select>
      </td>
      <td class="label"><label> #</label></td>
      <td class="input"><input type="text" value="" name="waterRightsNum" maxlength="32"/></td>
      <td class="input"><input class="numField" type="text" value="" name="proposedTransVol" maxlength="9"/></td>
      <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Attachment" title="Save Water Rights"/></td>
      <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Water Rights"/></td>
      <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Water Rights"/></td>
    <input type="hidden" value="" name="wtWaterRightsId"/>
  </tr>
  <c:forEach items="${waterRights}" var="waterRight">
    <tr>
      <td>
        <select name="waterRights" disabled>
          <option value="Statement" <c:if test="${waterRight.waterRightsType eq 'Statement'}">selected</c:if> >Pre-1914</option>
          <option value="Application" <c:if test="${waterRight.waterRightsType eq 'Application'}">selected</c:if>>Post-1914</option>
<!--      <option value="Decree" <c:if test="${waterRight.waterRightsType eq 'Decree'}">selected</c:if>>Adjudicated</option>
          <option value="Contract" <c:if test="${waterRight.waterRightsType eq 'Contract'}">selected</c:if>>Contract</option>-->
        </select>
      </td>
      <td class="label"><label>${waterRight.waterRightsType} #</label></td>
      <td class="input"><input type="text" value="${waterRight.waterRightsNum}" name="waterRightsNum" disabled/></td>
      <td class="input"><input type="text" class="numField" value="${waterRight.proposedTransVol}" name="proposedTransVol" maxlength="9" disabled/></td>
      <td class="${hideField}"><img class="save-icon hidden" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Water Rights" title="Save Water Rights"/></td>
      <td class="${hideField}"><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Water Rights" title="Edit Attachment"/></td>
      <td class="${hideField}"><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Attachment"/></td>
      <input type="hidden" value="${waterRight.wtWaterRightsId}" name="wtWaterRightsId"/>
    </tr>
  </c:forEach>
  </tbody>
</table>
<style>
  table.water_rights_table{
    border-collapse: collapse;
    border-spacing: 0px;
    width:100%;
  }
  .water_rights_table td,.water_rights_ct th{
    padding:5px;
    font-size: 10pt;
    text-align: center;
  }
  .water_rights_ct th{
    background-color: #E3E3E3;
    border: 1px solid #cccccc;
    text-align: center;
  }
  .water_rights_ct td{
    text-align: center !important;
  }
  .water_rights_ct tr{
    border: 1px solid #cccccc;
  }
  .water_rights_ct .label{
    text-align: right !important;
  }
  .water_rights_ct .input{
    text-align: center !important;
  }
  .save-icon,.edit-icon,.delete-icon{
    cursor: pointer;
  }
  table.water_rights_table tbody select:hover{
    cursor: pointer;
  }
  table.water_rights_table tbody .input input{
    border: 1px solid #a6a8a8;
    outline: medium none;
  }
  table.water_rights_table tbody .input input:focus{
    border: 1px solid #a6a8a8;
  }
</style>