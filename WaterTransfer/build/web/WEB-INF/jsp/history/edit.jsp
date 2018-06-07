<%--
    Document   : list
    Created on : Mar 23, 2015, 5:36:03 PM
    Author     : clay
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/css/wtProposal.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/jquery.maskedinput.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style>
.mainTable {
    margin-top: 0 !important;
    padding-top: 10px;
    border-collapse: collapse;
    border: 1px;
}
.generalTitle {
    background-color: #014b72 !important;
    border: medium none;
    color: #ffffff !important;
    font-style: normal;
    font-weight: bold;
    padding: 5px 0 5px 8px;
    text-align: left;
}
table th, td {
    text-align: left !important;
    padding-top: 10px;
    padding-bottom: 5px;
/*    border: solid 1px;*/
}
.row1, .row2{
/*    height: 50px;*/
}
.agencyLabel {
    font-weight: bold;
    padding-left: 10px;
    text-align: left;
}
.baseLabel {
    font-weight: bold;
    padding-left: 10px;
    text-align: left;
    width: 250px;
}
.baseLabel, .baseVal {
/*    border: medium none;*/
    color: #000 !important;
}
</style>
<script type="text/javascript">

</script>
<div style="padding:30px 5px 0;" class="">
    <div class="header_tab">Proposal ${proposal.wtTransNum}</div>
    <table class="mainTable" cellspacing="0" align="center" style="width: 1000px;">
        <tr>
            <td class="generalTitle" colspan='3'>Seller/Buyer Information</td>
        </tr>
        <tr class="row1">
            <td class="agencyLabel">Seller: </td>
            <c:forEach var="seller_agency" items="${proposal.wtSellerCollection}">
                <td class="agencyVal">${seller_agency.agencyFullName}</td>
            <tr><tr><td></td>
            </c:forEach>
        </tr>
        <tr class="row2">
            <td class="agencyLabel">Buyer: </td>
            <c:forEach var="buyer_agency" items="${proposal.wtBuyerCollection}">
                <td class="agencyVal">${buyer_agency.wtAgency.agencyFullName}</td>
                <td class="agencyVal">${buyer_agency.sharePercent}</td>
            <tr><tr><td></td>
            </c:forEach>
        </tr>
    </table>

    <%@include file="BaseInfo.jsp" %>

    <c:if test="${not empty proposal.wtCropIdling}">
    <%@include file="CropidlingInfo.jsp" %>
    </c:if>

</div>
<%@include file="../templates/footer.jsp" %>