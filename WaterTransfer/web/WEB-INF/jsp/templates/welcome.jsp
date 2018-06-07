<%--
    Document   : welcome
    Created on : Mar 23, 2015, 1:45:51 PM
    Author     : clay
--%>
<%@page import="com.gei.context.UserContext"%>
<%
  pageContext.setAttribute("UserContext", UserContext.getInstance());
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${UserContext.isLoggedIn()}">
  <div class="inline" style="color: #000; background-color: #e9e9e9;border-radius: 5px;float: right;font-size: 10pt;margin-top: 10px;opacity: 0.75;padding: 10px 15px;">
    Welcome, ${UserContext.user.name}!
    <a style="color: rgb(0,91,138);font-size:8pt;text-decoration:none;" href="${pageContext.request.contextPath}/authentication/logout">Sign Out</a>
  </div>
    <input id="userjson" type="hidden" name="userjson" value="${UserContext.user.toMap()}"/>
</c:if>
