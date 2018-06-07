<%--
    Document   : header
    Created on : Dec 15, 2014, 2:38:06 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=10">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${PageTitle}</title>
        <base href="${pageContext.request.contextPath}">

        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wtindex.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wtadmin.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wthome.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wtnew.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wtexist.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wthistoric.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/scrollableFixedHeaderTable.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/header.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/footer.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/sortHeader.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/theme.default.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/theme.blue.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery.loadmask.css">
        <!--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/multi-select.css">-->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">

        <!--<script src="${pageContext.request.contextPath}/resources/js/tinymce/tinymce.min.js"></script>-->
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.loadmask.min.js"></script>
        <!--<script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.js"></script>-->
        <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.md5.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.cookie.pack.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.dimensions.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.scrollableFixedHeaderTable.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.tablesorter.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.tablesorter.widgets.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/scrolltable.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/highcharts.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.highchartTable.js"></script>
        <!--<script src="${pageContext.request.contextPath}/resources/js/jquery.multi-select.js"></script>-->
        <script src="${pageContext.request.contextPath}/resources/js/WtTrans.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/WtAdmin.js"></script>

<!--        <script src="${pageContext.request.contextPath}/resources/js/proposal/AgencyContact.js"></script>-->
        <!--<script src="http://js.arcgis.com/3.11/init.js"></script>-->
        <!--<script src="http://js.arcgis.com/3.11/jquery.js"></script>-->
        <!--<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>-->
        <script type="text/javascript">window.SERVER_ROOT = "${pageContext.request.contextPath}";</script>
        <script>
            $(document).ready(function(){
                setTimeout(function(){
                    new WtTrans();
                },1);
            });
        </script>
    </head>
    <body>
    <div id="body-content-wrapper">
        <div id="header" class="corner-top-10">
            <image src="${pageContext.request.contextPath}/resources/images/header_ca.gov.png" class="inline" id="header-seal"/>
            <div class="inline">
                <div id="header-title">Department of Water Resources</div>
                <div id="header-subtitle">Water Transfer Information Management System</div>
                <div id="header-nav">
                    <a id="link-home">Home |</a>
                    <a id="link-new">Prepare New Proposal |</a>
                    <a id="link-exist">Exist Proposal |</a>
                    <a id="link-historic">Historic Water Transfer |</a>
                    <a id="link-admin">Admin Page |</a>
                </div>
            </div>
        </div>
