<%@page import="com.gei.context.LookupDataContext"%>
<%
  pageContext.setAttribute(LookupDataContext.class.getSimpleName(), LookupDataContext.getInstance());
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<input type="hidden" name="wtAgencyId" value="${contact.wtAgency.wtAgencyId}" />
<input type="hidden" name="wtContactId" value="${contact.wtContactId}" />
<input type="hidden" name="wtTransId" value="${wtTransId}" />
<c:if test="${contact.wtContactId eq null}">
  <input type="hidden" name="isActive" value="1"/>
</c:if>
  
<style>
  #agency_contact_ct input,
  #agency_contact_ct label,
  #agency_contact_ct select {
    font-size: 10pt;
  }
</style>
<div id="agency_contact_ct" class="">
  <div>
    <label>Title</label><br/>
    <input text="text" style="width:483px;" name="title" value="${contact.title}" maxlength="64"/>
  </div>
  <div>
    <div>
      <label>First Name <span style="color:red;">*</span></label><br/>
      <input text="text" style="width:200px;" name="firstName" value="${contact.firstName}" maxlength="16"/>
    </div>
    <div>
      <label>MI</label><br/>
      <input text="text" style="width:50px;" name="middleName" value="${contact.middleName}" maxlength="10"/>
    </div>
    <div>
      <label>Last Name <span style="color:red;">*</span></label><br/>
      <input text="text" style="width:200px;" name="lastName" value="${contact.lastName}" maxlength="16"/>
    </div>
  </div>
  <div>
    <label>Address</label><br/>
    <input text="text" style="width:483px;" name="address1" value="${contact.address1}"/>
  </div>
  <div>
    <div>
      <label>City</label><br/>
      <input text="text" style="width:200px;" name="cityName" value="${contact.cityName}"/>
    </div>
    <div>
      <label>State</label><br/>
      <c:choose>
        <c:when test="${empty contact.wtState}">
          <c:set var="defaultState" value="CA" />
        </c:when>
        <c:otherwise>
          <c:set var="defaultState" value="${contact.wtState.shortName}" />
        </c:otherwise>
      </c:choose>
      <select name="wtStateId" style="width:63px;">      
        <c:forEach var="state" items="${LookupDataContext.stateLookup}">
          <option value="${state.wtStateId}" ${state.shortName == defaultState?'selected':''}>${state.shortName}</option>
        </c:forEach>
      </select>
    </div>
    <div>
      <label>Zip</label><br/>
      <input text="text" style="width:150px;" name="zipcode" value="${contact.zipcode}"/>
    </div>
  </div>
  <div>
    <div>
      <label>Phone <span style="color:red;">*</span></label><br/>
      <input text="text" style="width:200px;" name="phoneNumber" value="${contact.phoneNumber}"/>
    </div>
    <div>
      <label>Phone Type</label><br/>
      <select name="phoneType" style="width:120px;">
        <option value="Office" ${contact.phoneType == "Office"?'selected="selected"':''}>Office</option>
        <option value="Mobile" ${contact.phoneType == "Mobile"?'selected="selected"':''}>Mobile</option>
      </select>
    </div>
  </div>
  <div>
    <label>Email <span style="color:red;">*</span></label><br/>
    <input text="text" name="email" style="width:483px;" value="${contact.email}"/>
  </div>
  <div>
    Please add General Manager if different information from contact
  </div>
</div>
