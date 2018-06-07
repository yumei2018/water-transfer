<%-- 
    Document   : cropTypeTable
    Created on : Aug 23, 2017, 9:06:07 AM
    Author     : ymei
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
<table class="ci-croptype-table ${hideTable}">
  <thead>
    <tr class="croptype_header" >
      <th style="width:20%;">Type Of Crop</th>
      <th style="width:15%;">Proposed Transfer Acreage by Crop Idling (Acres)</th>
      <th style="width:15%;">Proposed Transfer Acreage by Crop Shifting (Acres)</th>
      <th style="width:15%;">Total Proposed Transfer Acreage (Acres)</th>            
      <th style="width:15%;">Evapotranspiration Rate of Applied Water (ETAW)</th>
      <th style="width:15%;">Amount of Water (acre-feet)</th>
      <th class="">Save</th>
      <th class="">Edit</th>
      <th class="">Remove</th>
    </tr>
  </thead>
  <tbody>
    <tr class="hidden">
      <td>
        <select class="crop_type" style="width:150px;">
          <option value=""></option>
          <c:forEach var="cropType" items="${LookupDataContext.cropType}">
            <option value="${cropType.wtCropTypeId}">${cropType.cropType}</option>
          </c:forEach>
        </select>
      </td>
      <td><input type="text" class="numField" value="2000" name=""/></td>
      <td><input type="text" class="numField" value="1500" name=""/></td>
      <td><input type="text" class="numField" value="3500" name=""/></td>
      <td><input type="text" class="numField" value="1.7" name=""/></td>
      <td><input type="text" class="numField" value="5950" name=""/></td>
      <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Attachment" title="Save Water Rights"/></td>
      <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Water Rights"/></td>
      <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Water Rights"/></td>
      <input type="hidden" value="" name="wtCropTypeId"/>
    </tr>

    <tr>
      <td>
        <select class="crop_type" style="width:150px;">
          <option value=""></option>
          <c:forEach var="cropType" items="${LookupDataContext.cropType}">
            <option value="${cropType.wtCropTypeId}">${cropType.cropType}</option>
          </c:forEach>
        </select>
      </td>
      <td><input type="text" class="numField" value="2000" name=""/></td>
      <td><input type="text" class="numField" value="1500" name=""/></td>
      <td><input type="text" class="numField" value="3500" name=""/></td>
      <td><input type="text" class="numField" value="1.7" name=""/></td>
      <td><input type="text" class="numField" value="5950" name=""/></td>
      <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Attachment" title="Save Water Rights"/></td>
      <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Water Rights"/></td>
      <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Water Rights"/></td>
    </tr>

  </tbody>
</table>
<style>
  table.ci-croptype-table{
    border-collapse: collapse;
    border-spacing: 0px;
    width:100%;
  }
  .croptype_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .ci-croptype-table th{
    background-color: #E3E3E3;
    border: 1px solid #cccccc;
    text-align: center;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  .ci-croptype-table tr{
    border: 1px solid #cccccc;
  }
  .ci-croptype-table td{
    padding: 3px;
    font-size: 10pt;
    text-align: center !important;
    border:1px solid #cccccc;
  }  
  .ci-croptype-table .label{
    text-align: right !important;
  }
  .ci-croptype-table .input{
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
  table.ci-croptype-table tbody .input input:focus{
    border: 1px solid #a6a8a8;
  }
</style>
