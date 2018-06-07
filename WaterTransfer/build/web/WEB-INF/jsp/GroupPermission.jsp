<%-- 
    Document   : GroupPermission
    Created on : Feb 9, 2015, 2:54:19 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form class="form" id="group-permission-form">
    <table class="group-permission-table">
        <tr><td>
            <div class="form-top">Group List</div>
            <div class="group-container" value="${groupList}">
                <select class="groupList" multiple='group' name="groupId" id="groupId">
                    <c:forEach var="group" items="${groupList}">
                        <option value="${group.groupId}">${group.name}</option>
                    </c:forEach>    
                </select>
            </div>
        </td></tr>
        <tr><td>
            <div class="form-top">Group Permission</div>
            <div class="selected-group-container">        
                <c:forEach var="group" items="${groupList}">
                    <select id="${group.groupId}" class="groupPermission" multiple='group' name="permissionId">
                        <c:forEach var="permission" items="${group.appPermissionCollection}">
                            <option value="${permission.permissionId}">${permission.name}</option>
                        </c:forEach>
                    </select>
                </c:forEach>
            </div>
        </td>
        <td>
            <input class="add-remove-button" type="button" id="add-button" value="<<ADD" onclick="return false;"/>
            <div class="space"></div>
            <input class="add-remove-button" type="button" id="remove-button" value="REMOVE>>" onclick="return false;"/>
        </td>
        <td>
            <div class="form-top">Available Group Permission</div>
            <div class="permission-container">                
                <select class="permissionList" multiple='multiple' name="permissionId" id="permissionId">
                    <c:forEach var="permission" items="${permissionList}">
                        <option value="${permission.permissionId}">${permission.name}</option>
                    </c:forEach>    
                </select>
            </div>
        </td></tr>
    </table>
                
    <div id="form-button">
        <input class="save-button" type="button" id="save-permission" value="Save" onclick="return false;"/>
    </div>
</form>
