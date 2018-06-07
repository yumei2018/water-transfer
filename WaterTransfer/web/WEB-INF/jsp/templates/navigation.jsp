<%--
    Document   : navigation
    Created on : Mar 23, 2015, 1:15:25 PM
    Author     : clay
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${sessionScope.LOGGED_IN}">
  <div id="navigation">
    <span><a href="${pageContext.request.contextPath}/" class="home_navi">Home</a></span>
    <span><a href="${pageContext.request.contextPath}/proposal" class="proposal_navi">Proposals</a></span>
    <c:if test="${sessionScope.USER.isManager()||sessionScope.USER.isReviewer()}">
      <span><a href="${pageContext.request.contextPath}/proposal/mileStones" target="_blank">Milestone Logs</a></span>
<!--      <span><a href="${pageContext.request.contextPath}/proposal/search" class="search_navi">Search</a></span>-->
    </c:if>
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">      
<!--      <span><a href="${pageContext.request.contextPath}/history" class="history_navi">History</a></span>-->
<!--      <span><a href="${pageContext.request.contextPath}/history/getTransChart" class="chart_navi">Chart</a></span>-->
      <span><a href="${pageContext.request.contextPath}/admin/" class="admin_navi">Admin</a></span>
    </c:if>
  </div>
</c:if>