<%--
    Document   : BaseInfo
    Created on : Apr 1, 2015, 10:06:30 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

    <table class="mainTable" cellspacing="0" align="center" style="width: 1000px;">
        <tr>
            <td class="generalTitle" colspan='4'>General Information</td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Transfer Year: </td>
            <td class="baseVal">${proposal.transYear}</td>
        </tr>
<!--        <tr class="row2">
            <td class="baseLabel">Proposal Description: </td>
            <td class="baseVal" colspan='3'>${proposal.description}</td>
        </tr>-->
        <tr class="row1">
            <td class="baseLabel">Proposed Transfer Quantity: </td>
            <td class="baseVal">${proposal.proTransQua}</td>
            <td class="baseLabel">Actual Transfer Quantity: </td>
            <td class="baseVal">${proposal.actTransQua}</td>
        </tr>
        <tr class="row2">
<!--            <td class="baseLabel">Requested Export Window: </td>
            <td class="baseVal">${proposal.reqExpWin}</td>-->
            <td class="baseLabel">DWR Proposed Approve Date: </td>
            <td class="baseVal"><fmt:formatDate value="${proposal.dwrProApprDate}" pattern="MM/dd/yyyy"/></td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Start Transfer Window: </td>
            <td class="baseVal"><fmt:formatDate value="${proposal.transWinStart}" pattern="MM/dd/yyyy"/></td>
            <td class="baseLabel">End Transfer Window: </td>
            <td class="baseVal"><fmt:formatDate value="${proposal.transWinEnd}" pattern="MM/dd/yyyy"/></td>
        </tr>
        <tr class="row2">
            <td class="baseLabel">Proposed Agreement Amt Paid to Seller: </td>
            <td class="baseVal">$<fmt:formatNumber type="number" maxFractionDigits="3" value="${proposal.proAgreePaid}"/></td>
            <td class="baseLabel">Proposed Unit Cost of Water: </td>
            <td class="baseVal">$<fmt:formatNumber type="number" maxFractionDigits="3" value="${proposal.proUnitCost}"/></td>
        </tr>
        <tr class="row1">    
            <td class="baseLabel">Calculated Amt Paid to Seller: </td>
            <td class="baseVal">$<fmt:formatNumber type="number" maxFractionDigits="3" value="${proposal.calAmtPaid}"/></td>
            <td class="baseLabel">Calculated Unit Cost of Water: </td>
            <td class="baseVal">$<fmt:formatNumber type="number" maxFractionDigits="3" value="${proposal.calUnitCost}"/></td>
        </tr>
        <tr class="row2">
            <td class="baseLabel">Actual Amt Paid to Seller: </td>
            <td class="baseVal">$<fmt:formatNumber type="number" maxFractionDigits="3" value="${proposal.actAmtPaid}"/></td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Facilities Used: </td>
            <td class="baseVal">
                <c:forEach var="fuType" items="${proposal.wtTransTypeCollection}" varStatus="iterator">
                    ${fuType.wtFuType.fuType}
                    <c:if test="${!iterator.last}">/</c:if>
                </c:forEach>
            </td>
        </tr>
        <tr class="row2">
            <td class="baseLabel">Comments: </td>
            <td class="baseVal" colspan='3'>${proposal.wtComm}</td>
        </tr>
        <tr class="row1">
            <td class="baseLabel">Document(s): </td>
            <c:forEach var="attachment" items="${proposal.wtAttachmentCollection}">
                <td class="baseVal" colspan='3'>
                    <a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">
                        ${attachment.filename}
                    </a>
                </td>
            <tr><tr><td></td>
            </c:forEach>            
        </tr>
    </table>


