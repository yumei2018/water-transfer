<%--
    Document   : NewProposalList
    Created on : Feb 20, 2015, 3:27:00 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style type="text/css">
    #main-content-wrapper
    {
        width:1000px;
        margin:0 auto;
    }
    #proposal-list{
        top: 20px;
        height: 450px;
        background-color: #AFE8FF;
        margin: 0.5em 0;
        border:none;
        padding-left: 80px;
        padding-bottom: 20px;
    }
    #proposal-list-table{
        width: 800px;
        font-size: 15px;
    }
    table#proposal-list-table th{
        background-color: #8dbdd8
    }
    .delete-icon{
        cursor:pointer;
    }
    .edit-icon{
        cursor:pointer;
    }
    .wtTransId{
        cursor:pointer;
        text-decoration: underline;
    }
</style>

<div id="main-content-wrapper">
<div id="proposal-list">
    <table id="proposal-list-table">
        <tr class="tableRow">
            <th>Transfer ID</th>
            <th>Transfer Year</th>
            <th>Status</th>
            <th>Last Updated Time</th>
            <th>Last Updated By</th>
            <th></th>
        </tr>

        <c:forEach var="proposal" items="${proposalList}">
            <tr class="tableRow">
                <td class="wtTransId">${proposal.wtTransId}</td>
                <td>${proposal.transYear}</td>
                <td>${proposal.wtStatusFlag.statusName}</td>
                <td><fmt:formatDate value="${proposal.modifyDate}" pattern="MM/dd/yyyy"/></td>
                <td>${proposal.updatedById}</td>
                <td>
                    <a class="delete-icon" id='${proposal.wtTransId}'>
                        <img src="${pageContext.request.contextPath}/resources/images/icons/crossx.png" alt="Delete Proposal" title="Delete Proposal">
                    </a>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>
</div>
