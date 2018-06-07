<%--
    Document   : index
    Created on : May 13, 2015, 8:56:00 AM
    Author     : pheng
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<div class="" style="text-align:center;">
  <h2>Welcome to the Water Transfer Application System</h2>
  <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
    <div id="apps-ct" style="width:800px;margin:20px auto 0;">
      <a href="${pageContext.request.contextPath}/admin/edit/" class="app-btn">Account Manager</a>
<!--      <a href="${pageContext.request.contextPath}/admin/permission/" class="app-btn">Permission Manager</a>-->
      <a href="${pageContext.request.contextPath}/attachment/getTemplateList" class="app-btn">Template Manager</a>
      <a href="${pageContext.request.contextPath}/admin/getAgencyList" class="app-btn">Agency Manager</a>
    </div>
  </c:if>
</div>
<%@include file="../templates/footer.jsp" %>
<style type="text/css">
div#apps-ct .app-btn
{
  display:inline-block;
  padding:20px;
  width:80px;
  text-decoration: none;
  border-radius:10px;
  border:1px solid #aaa;
  color:#e9e9e9;
  margin-right:20px;

  background: rgba(73,155,234,1);
  background: -moz-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  background: -webkit-gradient(radial, center center, 0px, center center, 100%, color-stop(0%, rgba(73,155,234,1)), color-stop(100%, rgba(1,75,114,1)));
  background: -webkit-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  background: -o-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  background: -ms-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  background: radial-gradient(ellipse at center, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#499bea', endColorstr='#014b72', GradientType=1 );
}

div#apps-ct .app-btn:hover {
  background: rgba(255,175,75,1);
  background: -moz-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  background: -webkit-gradient(radial, center center, 0px, center center, 100%, color-stop(0%, rgba(255,175,75,1)), color-stop(100%, rgba(255,146,10,1)));
  background: -webkit-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  background: -o-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  background: -ms-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  background: radial-gradient(ellipse at center, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffaf4b', endColorstr='#ff920a', GradientType=1 );
}
.admin_navi{
    background-color:#fbad23;
}
</style>