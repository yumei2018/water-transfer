<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <meta content="IE=9,IE=edge" http-equiv="X-UA-Compatible"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>${empty(__PageTitle) ? "Water Tranfer Report System" : __PageTitle}</title>
    <c:forEach items="${__Stylesheets}" var="cssHref">
    <link rel="stylesheet" type="text/css" href="${cssHref}" />
    </c:forEach>
    <c:forEach items="${__Javascripts}" var="jsSrc">
    <script type="text/javascript" src="${jsSrc}"></script>
    </c:forEach>
  </head>
  <body onload="${__BodyOnload}">
    <div id="body-ct">