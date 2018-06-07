<%--
    Document   : agencyContactTable
    Created on : Mar 23, 2015, 3:50:22 PM
    Author     : pheng
--%>
<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="emptyRecord" value="hidden"/>
<c:set var="hideTable" value=""/>
<c:set var="isReviewer" value="hidden"/>
<c:set var="hideField" value=""/>
<c:set var="disableEdit" value=""/>
<c:set var="isCreater" value=""/>
<c:if test="${empty(contacts)}">
  <c:set var="emptyRecord" value=""/>
  <c:set var="hideTable" value="hidden"/>
</c:if>
<%--<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="hideTable" value=""/>
</c:if>--%>
<c:if test="${sessionScope.USER.isReviewer()}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<%--<c:if test="${sessionScope.USER.isAppAccount()||sessionScope.USER.isManager()}">
  <c:set var="hideTable" value="hidden"/>
</c:if>--%>
<%--<c:if test="${sessionScope.USER.isAppAccount()&&(statusName eq 'DRAFT')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&(statusName eq 'INCOMPLETE')}">
  <c:set var="disableEdit" value=""/>
  <c:set var="hideField" value=""/>
</c:if>--%>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'SUBMITTED'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'UREVIEW'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'PCOMPLETE'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'PAPPROVED'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'CEXECUTED'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'CANCEL'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'TPROGRESS'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>
<c:if test="${sessionScope.USER.isAppAccount()&&statusName eq 'TCOMPLETE'}">
  <c:set var="disableEdit" value="disabled"/>
  <c:set var="hideField" value="hidden"/>
</c:if>

<c:if test="${empty isCreater}">
  <c:set var="isCreater" value="hidden"/>
</c:if>
<table class="agencyContactList ${hideTable}">
    <input type="hidden" name="wtAgencyId" value="${wtAgencyId}" />
    <label style="padding-left:10px;">Is Agency primarily an Ag or Urban agency? </label>
    <select name="agencyType" class="agencyType" ${disableEdit}>
        <option value="AG" ${agencyType == "AG" ? 'selected="selected"' : ''}>AG</option>
        <option value="URBAN" ${agencyType == "URBAN" ? 'selected="selected"' : ''}>URBAN</option>
    </select></br> 
    
    <thead>
    <tr class="tableRow">
        <th>First Name</th>
        <th>Last Name</th>
        <th>Title</th>
        <th>Email</th>
        <th>Phone Number</th>
        <th>Address</th>
        <th style="width:30px;" class="edit_td ${hideField}">Edit</th>        
        <th style="width:30px;" class="${isCreater}">Permission</th>
        <th style="width:30px;" class="delete_td ${hideField}">Remove</th>
<!--        <th style="width:50px;">Status</th>-->
    </tr>
    </thead>
    <c:forEach var="contact" items="${contacts}">
        <c:set var="isActive" value="Inactive"/>
        <c:if test="${contact.isActive eq 1}">
            <c:set var="isActive" value="Active"/>
        </c:if>
        <c:set var="hasAccount" value=""/>
        <c:if test="${contact.hasAccount eq 0}">
            <c:set var="hasAccount" value="disabled"/>
        </c:if>
        <c:set var="isLinkTrans" value=""/>
        <c:if test="${contact.isLinkTrans eq 1}">
            <c:set var="isLinkTrans" value="checked"/>
        </c:if>
        <tr class="tableRow" contactid="${contact.wtContactId}" >
            <td>${contact.firstName}</td>
            <td>${contact.lastName}</td>
            <td>${contact.title}</td>
            <td>${contact.email}</td>
            <td>${contact.phoneNumber}</td>
            <td>
              <c:if test="${not empty contact.address1}">
                ${contact.address1}<br />${contact.cityName},${contact.shortName}&nbsp;${contact.zipcode}
              </c:if>
            </td>
<!--            <td class="edit_contact ${isReviewer}" style="text-align:center;"><span><img src="${pageContext.request.contextPath}/resources/images/icons/table_edit.png"></span></td>  -->
            <td class="edit_td ${hideField}" style="text-align:center;"><span><img src="${pageContext.request.contextPath}/resources/images/icons/table_edit.png" class="edit_contact"></span></td>
<!--            <td style="text-align:left;"><label>${isActive}</label></td>-->
            <td class="${isCreater}" style="text-align:center;">
              <input type="checkbox" class="contact_access" name="linkTrans" ${isLinkTrans} value='Y' ${hasAccount}/>
            </td>
            <td class="delete_td ${hideField}" style="text-align:center;"><span><img src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" class="delete_contact"></span></td>
            <%
              pageContext.setAttribute("contactJson", new JSONObject(((java.util.Map)pageContext.getAttribute("contact"))));
            %>
            <input type="hidden" name="contactLookup" value="${fn:escapeXml(contactJson)}"/>
        </tr>
    </c:forEach>
</table>
<!--<h2 class="${emptyRecord}">No contact found. Click the "Add New Contact" button below to add new contact.</h2>-->

<input type="button" class="contact_button ${hideField}" value="Add New Contact" />


<style type="text/css">
  .agencyContactList th{
    text-align: center;
    font-size: 15px;
  }
  .agencyContactList tr{
    font-size: 14px;
  }
</style>

