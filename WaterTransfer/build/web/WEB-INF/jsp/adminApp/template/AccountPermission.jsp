<%--
    Document   : AccountPermission
    Created on : May 14, 2015, 1:16:49 PM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<table class="user_perm">
    <thead>
        <tr>
            <th>
                User Permission
            </th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${AppGroup}" var="permission">
        <tr>
            <td>
                ${permission.name}
                <input type="hidden" name="groupid" value="${permission.groupId}"/>
            </td>
        </tr>
        </c:forEach>
    </tbody>
</table>
<div class="addRemoveCt">
    <button class="addBtn"><< Add</button><br/>
    <button class="removeBtn">Remove >></button>
</div>
<table class="aval_perm">
    <thead>
        <tr>
            <th>
                Available Permission
            </th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${AvailGrp}" var="availPerm">
        <tr>
            <td>
                ${availPerm.name}
                <input type="hidden" name="groupid" value="${availPerm.groupId}"/>
            </td>
        </tr>
        </c:forEach>
    </tbody>
</table>
<div class="saveCt">
    <button class="savepermBtn">Save</button>
</div>