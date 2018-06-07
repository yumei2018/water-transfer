<%-- 
    Document   : prevTransferTable
    Created on : Aug 30, 2016, 9:46:25 AM
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
<table class="pre_transfer_table ${hideTable}">
  <thead>
    <tr>
      <th style="text-align: center;">SWPAO Contract No.</th>
      <th>Contract No.</th>
      <th>Type of Transfer</th>
      <th class="${hideField}">Save</th>
      <th class="${hideField}">Edit</th>
      <th class="${hideField}">Remove</th>
    </tr>
  </thead>
  <tbody>
    <tr class="hidden">
      <td class="input"><input type="text" value="" name="swpaoContractNum" maxlength="8"/></td>
      <td class="input"><input type="text" value="" name="recomNum" maxlength="16"/></td>
      <td class="input">
        <input class="transType" type="checkbox" value="0" name="isTypeCi"/>Crop Idling
        <input class="transType" type="checkbox" value="0" name="isTypeRv"/>Reservoir
        <input class="transType" type="checkbox" value="0" name="isTypeGw"/>Groundwater
      </td>
      <td><img class="save-icon" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Transfer" title="Save Transfer"/></td>
      <td><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Transfer" title="Edit Transfer"/></td>
      <td><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Transfer" title="Remove Transfer"/></td>
      <input type="hidden" value="" name="wtPreTransferId"/>
    </tr>
    <c:forEach items="${preTransferList}" var="preTransfer">
    <tr>
      <td class="input"><input type="text" value="${preTransfer.swpaoContractNum}" name="swpaoContractNum" maxlength="8" disabled/></td>
      <td class="input"><input type="text" value="${preTransfer.recomNum}" name="recomNum" maxlength="16" disabled/></td>
      <td class="input">
        <input class="transType" type="checkbox" value="${preTransfer.isTypeCi}" name="isTypeCi" <c:if test="${preTransfer.isTypeCi==1}">checked="checked"</c:if> disabled/>Crop Idling
        <input class="transType" type="checkbox" value="${preTransfer.isTypeRv}" name="isTypeRv" <c:if test="${preTransfer.isTypeRv==1}">checked="checked"</c:if> disabled/>Reservoir
        <input class="transType" type="checkbox" value="${preTransfer.isTypeGw}" name="isTypeGw" <c:if test="${preTransfer.isTypeGw==1}">checked="checked"</c:if> disabled/>Groundwater
      </td>
      <td class="${hideField}"><img class="save-icon hidden" src="${pageContext.request.contextPath}/resources/images/icons/disk.png" alt="Save Transfer" title="Save Transfer"/></td>
      <td class="${hideField}"><img class="edit-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Transfer" title="Edit Transfer"/></td>
      <td class="${hideField}"><img class="delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Transfer" title="Remove Transfer"/></td>
      <input type="hidden" value="${preTransfer.wtPreTransferId}" name="wtPreTransferId"/>
    </tr> 
    </c:forEach>
  </tbody>
</table>
<script type="text/javascript">
$(document).ready(function(){
  var self = this;
  self.preTransferTable = $(".pre_transfer_table");
  self.typeCheckbox = self.preTransferTable.find(".transType");
  
  self.typeCheckbox.on("click",function(){
    if(this.checked)
    {
      $(this).val("1");
    } else {
      $(this).val("0");
    }
  });
  
});    
</script>
<style>
  table.pre_transfer_table{
    border-collapse: collapse;
    border-spacing: 0px;
    width:100%;
  }
  .pre_transfer_table td,.pre_transfer_ct th{
    padding:5px;
    font-size: 10pt;
    text-align: center;
  }
  .pre_transfer_ct th{
    background-color: #E3E3E3;
    border: 1px solid #cccccc;
    text-align: center;
  }
  .pre_transfer_ct td{
    text-align: center !important;
  }
  .pre_transfer_ct tr{
    border: 1px solid #cccccc;
  }
  .pre_transfer_ct .label{
    text-align: right !important;
  }
  .pre_transfer_ct .input{
    text-align: center !important;
  }
  .save-icon,.edit-icon,.delete-icon{
    cursor: pointer;
  }
  table.pre_transfer_table tbody select:hover{
    cursor: pointer;
  }
  table.pre_transfer_table tbody .input input{
    border: 1px solid #a6a8a8;
    outline: medium none;
  }
  table.pre_transfer_table tbody .input input:focus{
    border: 1px solid #a6a8a8;
  }
</style>
