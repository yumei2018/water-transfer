<%-- 
    Document   : BuyersRepPermission
    Created on : Mar 29, 2017, 9:56:37 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/proposal/ProposalAdmin.js
</c:set>
<%@include file="../templates/header.jsp" %>
<span class="breadcrumbs"><a href="${pageContext.request.contextPath}/admin">Admin</a> &gt; Permission</span>
<c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
  <div class="header_title">Buyers Representative Permission</div>
  <div style="padding:20px 25px 0;" class="module_type">
    <table id="permission-table" class="display" width="100%" cellspacing="0" cellpadding="0">
      <thead>
        <tr class="permission_header">
          <th style="width: 160px;">Representative Name</th> 
          <th style="width: 800px;">Permission Control List</th>
        </tr>
      </thead>
      
      <tbody>
        <c:forEach var="buyersRep" items="${buyersReps}">
        <c:set var="userId" value="${buyersRep.userId}"/>
        <tr>
          <td>            
            <!--<div class="buyersRep" id="${buyersRep.userId}">${buyersRep.getName()}</div>-->
            <div class="buyersRep" id="${buyersRep.userId}">${buyersRep.wtContact.lastName}, ${buyersRep.wtContact.firstName}</div> 
            <div class="agencyList" onClick="window.admin.showAgency(this);">Buyers Agency List</div>
            <div class="proposalList" onClick="window.admin.showProposal(this);">Proposal List</div>
          </td>
          <td class="permission_ct">
            <div class="agency_ct">
              <c:forEach var="agency" items="${agencyList}">
                <c:set var="isChecked" value=""/>
                <c:if test="${agency.hasRepUser(userId)}">
                  <c:set var="isChecked" value="checked"/>
                </c:if>              
                <div>
                  <input type="checkbox"
                         agencyId="${agency.wtAgencyId}"
                         userId="${buyersRep.userId}"
                         ${isChecked}
                         onclick="window.admin.initAgencyCheckbox(this);"/> ${agency.agencyFullName} 
                </div>
              </c:forEach>  
            </div>
          
            <div class="proposal_ct hidden">
              <table class="proposal-tb" width="100%" cellspacing="0" cellpadding="0">
                <thead>
                <tr class="tableRow">
                  <th style="width:30px;">Permission</th>
                  <th style="width:30px;">Proposal ID</th>
                  <th style="width:30px;">Year</th>
                  <th style="width:100px;">Seller</th>
                  <th style="width:100px;">Buyers</th>
                  <th style="width:100px;">Transfer Type</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="trans" items="${transCollection}">
                  <c:set var="isChecked" value=""/>
                  <c:if test="${buyersRep.hasTransPermit(trans)}">
                    <c:set var="isChecked" value="checked"/>
                  </c:if>
                  <tr class="tableRow ${trans.wtTransId}">
                    <td style="height:20px;">
                      <input type="checkbox"
                             transId="${trans.wtTransId}"
                             userId="${buyersRep.userId}"
                             ${isChecked}
                             onclick="window.admin.initTransCheckbox(this);"/></td>
                    <td style="height:15px;">${trans.wtTransId}</td>
                    <td style="height:15px;">${trans.transYear}</td>
                    <td style="height:15px;" class="seller"></td>
                    <td style="height:15px;" class="buyers"></td>
                    <td style="height:15px;" class="transType"></td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </td>
        </tr>
        </c:forEach> 
        
      </tbody>
    </table>
  </div>  
</c:if>
<%@include file="../templates/footer.jsp" %>

<style type="text/css">
  div.header_title {
    text-align: center;
    font-size: 20px;
    padding-top: 20px;
    padding-bottom: 20px;
  } 
  #permission-table{
    width:980px;
    text-align: center;
    margin-bottom: 10px;
  }
  .permission_header {
    background-color: #8cb3d9;
    font-size: 15px;
  }
  .permission_header th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #permission-table tr{
    background-color: #F9F9F9;    
  }
  #permission-table td{
    background-color: #F9F9F9;
    border:1px solid #cccccc;
    padding: 3px;
    height: 350px;
    text-align: left;     
  }
  .permission_ct{
    display: block;
    overflow: auto;
  }
  .proposal-tb th{
    background-color: #8cb3d9;
    height:20px;
    text-align: center;
  }
  .proposal-tb td{
    font-size: 12px;
    height: 15px;
  }
  .buyersRep{
    padding:10px 0px;
  }
  .agencyList, .proposalList{
    text-decoration:underline;
    cursor:pointer;
    font-size:12px;
    padding-bottom: 10px;
    color: blue;
  }
</style> 
<script type="text/javascript">
  $(document).ready(function () {
    window.admin = new ProposalAdmin();    
  });  
</script>

