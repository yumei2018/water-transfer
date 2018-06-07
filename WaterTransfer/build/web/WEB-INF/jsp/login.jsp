<%--
    Document   : login
    Created on : Mar 23, 2015, 11:54:04 AM
    Author     : clay
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/login.css
,${pageContext.request.contextPath}/resources/css/sitewide.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/ApplicationLauncher.js
,${pageContext.request.contextPath}/resources/js/admin/AccountRegister.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery.validate.min.js
,${pageContext.request.contextPath}/resources/js/jquery/additional-methods.min.js
</c:set>
<%@include file="templates/header.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
  new ApplicationLauncher({
    javascripts:[window.SERVER_ROOT + "/resources/js/LoginForm.js"]
    ,callback:function(){
      new LoginForm({
        id:"form-ct"
      });
    }
  }).launch();
   new AccountRegistration();
});
</script>
<c:set var="noticeMsg" value="hidden"/>
<c:if test="${networkFlag == true}">
  <c:set var="noticeMsg" value=""/>
</c:if>
<div id="login-subheader">
  
</div>
<div id="content-ct">
  <div class="inline" id="loginform-ct" >
    <div id="loginform-wrapper">
      <h2 id="welcome-header">Welcome</h2>
      <div class="form-title">
        <span class="subtext" style="font-size: 9pt">To login, enter your username and password</span>
      </div>
      <div id="form-ct"></div>
<!--      <div style="margin-top: 15px; margin-bottom: 30px;">
        <input type="checkbox" id="agree-check" value="" /> I Agree <span style="color:red;">*</span>
      </div>-->
      <div style="width:100%;">
        <span style="float: left;" class="loginLink registerLink">Request a Login ID</span>
        <a style="float: right;" class="loginLink" href='mailto:Vinh.Giang@water.ca.gov
                                                         &cc=Devinder.Dhillon@water.ca.gov;Ahrash.Zamanian@water.ca.gov;ymei@geiconsultants.com
                                                         &subject=Water Transfer Information Application System - Request Password Reset'>Password Reset</a>
      </div>
<!--
      <div style="width:100%;margin-bottom: 70px;">
        <a style="float: right;" class="loginLink" href='proposalservice/publicsearch' target="_blank">Water Transfer Records Search</a>
      </div>
-->

      <c:set var="today" value="<%=new Date()%>"/>
      <div class="${noticeMsg}" style="width:100%;color:red;background-color:white;margin-top:50px;">
        WTIMS is unavailable <fmt:formatDate type="date" value="${today}" dateStyle = "long"/> due to maintenance and upgrades. Thank you for your patience.
      </div>
    </div>
  </div>
  
  <div class="inline content" id="login-content">
    <div id="">
      <p>The Water Transfer Information Management System (WTIMS) is an online web application
      developed by the State of California Department of Water Resources (DWR) to help facilitate
      preparation of water transfer proposals that will be submitted to DWR for consideration. A
      water transfer is a voluntary sale of water proposed and initiated by willing sellers who have
      legal rights to a supply of water to an interested buyer. The Seller must take specific actions
      within the Seller’s service area to make water available to the buyer that would not be water
      available in the watercourse absent the transfer, "new water". Please refer to DWR’s 
      <a target="_blank" href="https://www.water.ca.gov/Programs/State-Water-Project/Management/Water-Transfers">water transfer webpage</a> and the
      <a target="_blank" href="https://www.water.ca.gov/watertransfers/docs/2016_Water_Transfer_White_Paper.pdf">White Paper</a>
      for additional information:</p><br>
    
      <p>In order to access the system, Seller must establish a secure User ID and Password by 
        clicking request a Login ID in the home page of WTIMS.</p><br>      
    </div>
    <div style="width:100%;">
      <a style="float: left;" class="loginLink" href='proposalservice/statusreport' target="_blank">Current Temporary Water Transfer Proposal Status Overview</a>
    </div>
  </div>
</div>

<%@include file="templates/footer_login.jsp" %>