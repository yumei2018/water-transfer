<%--
    Document   : CropidlingInfo
    Created on : Apr 1, 2015, 10:14:35 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

    <table class="mainTable" cellspacing="0" align="center" style="width: 1000px;">
        <tr>
            <td class="generalTitle" colspan='2'>Crop Idling Information</td>
        </tr>
        <tr class="row1">
            <td class="baseLabel" style="width: 400px;">Water Transfer Amount (AF): </td>
            <td class="baseVal">${proposal.wtCropIdling.waterTransQua}</td>
        </tr>
        <tr class="row2">
            <td class="baseLabel">Total Proposed Amount for Crop Transfer (AF): </td>
            <td class="baseVal">${proposal.wtCropIdling.totalTransferAcr}</td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Proposed Transfer Amount by crop Idling (AF): </td>
            <td class="baseVal">${proposal.wtCropIdling.proTransferByCI}</td>
        </tr>
        <tr class="row2">
            <td class="baseLabel">Proposed Transfer Amount by Crop Shift (AF): </td>
            <td class="baseVal">${proposal.wtCropIdling.proTransferByCS}</td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Does this proposal include reservoir release?: </td>
            <td class="baseVal">
                <c:if test="${proposal.wtCropIdling.isResRelease == 0}">No</c:if>
                <c:if test="${proposal.wtCropIdling.isResRelease == 1}">Yes</c:if>
            </td>
        </tr>
    </table>

