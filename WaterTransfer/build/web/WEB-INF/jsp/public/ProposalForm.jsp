<%-- 
    Document   : ProposalForm
    Created on : Aug 7, 2017, 1:59:14 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div style="padding:10px 5px 0;" class="proposal-detail-ct">
  <table class="transfer-detail-table">
    <thead>
      <tr class="transfer-detail-header">
        <th style="width:15%;">Type of Transfer</th>
        <th style="width:13%;">Contract Amount (acre-feet)</th>
        <th style="width:13%;">Actual Amount (acre-feet)</th>
        <th style="width:13%;">Water Delivered (after all losses:acre-feet)</th>
      </tr>
    </thead>
    <tbody>
      <c:if test="${proposal.proAcrIdleInd==1}">
      <tr>
        <td>CI</td>
        <td><span class="numFieldText" id="ci1">${cropIdling.contractAmount}</span></td>
        <td><span class="numFieldText" id="ci2">${cropIdling.actualAmount}</span></td>
        <td><span class="numFieldText" id="ci3">${cropIdling.deliveredAmount}</span></td>
      </tr>
      </c:if>
      <c:if test="${proposal.resReOpInd==1}">
      <tr>
        <td>RES</td>
        <td><span class="numFieldText" id="rv1">${reservoir.contractAmount}</span></td>
        <td><span class="numFieldText" id="rv2">${reservoir.actualAmount}</span></td>
        <td><span class="numFieldText" id="rv3">${reservoir.deliveredAmount}</span></td>
      </tr>
      </c:if>
      <c:if test="${proposal.wellUseNumInd==1}">
      <tr>
        <td>GW</td>
        <td><span class="numFieldText" id="gw1">${groundwater.contractAmount}</span></td>
        <td><span class="numFieldText" id="gw2">${groundwater.actualAmount}</span></td>
        <td><span class="numFieldText" id="gw3">${groundwater.deliveredAmount}</span></td>
      </tr>
      </c:if>
    </tbody>
  </table> 
</div>

<style type="text/css">
/*  #proposalTitle{
    font-size: 20px;
    margin-bottom: 20px;
  }*/
  .transfer-detail-table{
    width:100%;
    text-align: center;
    font-size: 15px;
/*    margin-bottom: 10px;*/
  }
  .transfer-detail-table th{
    border: 1px solid #aaaaaa;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  .transfer-detail-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }  
  .transfer-detail-header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
</style>
