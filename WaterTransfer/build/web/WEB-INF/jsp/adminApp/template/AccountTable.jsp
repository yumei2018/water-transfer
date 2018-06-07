<%--
    Document   : AccountTable
    Created on : May 14, 2015, 1:15:27 PM
    Author     : pheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${type eq 'permission'}">
    <c:set var="hide" value="hidden"/>
</c:if>
<div id="accordion">
<!--    <ul>
        <li><a href="#registered">Contact with Account</a></li>
        <li class="${hide}"><a href="#contact">Contact without Account</a></li>
        <li class="${hide}"><a href="#pending">Pending</a></li>
    </ul>-->
    <h3>Pending</h3>
    <div id="pending" class="${hide}">
        <table class="contact-table">
            <thead>
                <tr>
                    <th>User Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${userReg.getArrayList()}" var="register">
                    <tr typeId="regUser">
                        <td>${register.optString('userId')}</td>
                        <td>${register.optString('firstName')}</td>
                        <td>${register.optString('lastName')}</td>
                        <td>${register.optString('email')}</td>
                        <input type="hidden" name="registerdata" value="${fn:escapeXml(register)}"/>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <h3>Contacts with Account</h3>
    <div id="registered" class="${hide}">
        <table class="contact-table">
            <thead>
                <tr>
                    <th>Contact Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${wtcontact}" var="contact">
                  <c:if test="${not empty contact.getUser()}">
                    <tr typeId="contact">
                        <td>${contact.wtContactId}</td>
                        <td>${contact.firstName}</td>
                        <td>${contact.lastName}</td>
                        <td>${contact.email}</td>
                        <td>${contact.phoneNumber}</td>
                        <input type="hidden" name="wtcontactid" value="${contact.wtContactId}"/>
                    </tr>
                  </c:if>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <h3>Contacts without Account</h3>
    <div id="contact">
        <table class="contact-table">
            <thead>
                <tr>
                    <th>Contact Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${wtcontact}" var="contact">
                    <tr typeId="contact">
                      <c:if test="${empty contact.getUser()}">
                        <td>${contact.wtContactId}</td>
                        <td>${contact.firstName}</td>
                        <td>${contact.lastName}</td>
                        <td>${contact.email}</td>
                        <td>${contact.phoneNumber}</td>
                        <input type="hidden" name="wtcontactid" value="${contact.wtContactId}"/>
                      </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
</div>
<style>
.contact-table{
    width:100%!important;
    text-align: center;
    margin-bottom: 10px;
}
.contact-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
}
.contact-table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
}
/*  table th{
    background: #ffcc66;
    border: solid 2px;
    border-color: #FFF;
  }  
  #tabs li{
    font-size: 15px;
  }*/
</style>
