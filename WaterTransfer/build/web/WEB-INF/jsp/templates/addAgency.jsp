<%--
    Document   : addEditAgency
    Created on : Sep 21, 2015, 9:46:42 AM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form action="" method="post">
    <input type="text" value="" name="agencyFullName" placeholder="Enter the Agency Name" style="width:100%;"/>
    <input type="hidden" name="agencyCode"/>
    <input type="hidden" name="createDate" />
    <input type="hidden" name="modifyDate" />
    <input type="hidden" name="agencyActiveInd" value="1"/>
</form>