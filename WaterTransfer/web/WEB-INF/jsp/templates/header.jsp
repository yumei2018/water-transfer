<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <title>${empty(__PageTitle) ? "Department of Water Resources" : __PageTitle}</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/dwr.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sitewide.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/header.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/footer.css" />
    <c:forEach items="${__Stylesheets}" var="cssHref">
    <link rel="stylesheet" type="text/css" href="${cssHref}" />
    </c:forEach>
    <script type="text/javascript">window.SERVER_ROOT="${pageContext.request.contextPath}";</script>
    <c:forEach items="${__Javascripts}" var="jsSrc">
    <script type="text/javascript" src="${jsSrc}"></script>
    </c:forEach>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/overrides.css" />
  </head>
  <body onload="${__BodyOnload}">
    <div id="background">&nbsp;</div>
    <!--<a name="header"></a>-->
    <div id="content-ct">
      <div id="header" >
        <div class="" id="header-lcol">
          <img src="${pageContext.request.contextPath}/resources/images/header_ca.gov.png" class="inline" id="header-seal" />
        </div>
        <div class="inline" id="header-rcol">
          <div class="inline" >
            <div id="header-title">Department of Water Resources</div>
            <div id="header-subtitle">Water Transfer Information Management System</div>
          </div>
          <%@include file="welcome.jsp" %>
          <%@include file="navigation.jsp" %>
        </div>
      </div>
      <div id="content-body">
