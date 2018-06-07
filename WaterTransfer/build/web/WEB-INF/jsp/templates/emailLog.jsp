<%--
    Document   : MessageLog
    Created on : Apr 17, 2015, 10:00:01 AM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    .contact_header{
        background: #6699ff;
        height: 50px;
        line-height: 50px;
        padding-left: 5px;
        cursor: pointer;
        overflow: hidden;
    }
    .email_form input, .email_form textarea{
        margin-bottom: 15px;
    }
    .contactCt{
       overflow: hidden;
    }
    .reviewer_tag{
        width: 425px;
        overflow: auto;
        position: absolute;
        top:0;
        left:34.6%;
        height: 412px;
        padding-top:5px;
    }
    .seller_tag{
        overflow: auto;
        position: absolute;
        top:0;
        height: 410px;
        padding-top:5px;
    }
    .reply_btn{
        float:right;
        margin-top: 10px;
        margin-right: 5px;
    }
    .responseCt,.replyCt{
        width: 255px;
    }
    .sellerAcct{
        width:350px !important;
    }
    .replyCt{
        /*background-color: #ccccff;*/
        border: 1px solid #ccccff;
        border-radius: 2px;
        box-shadow: 2px 2px 5px #cccccc;
        float:right;
        padding:10px;
    }
    .responseCt{
        /*background-color: #cccccc;*/
        border: 1px solid #cccccc;
        border-radius: 2px;
        box-shadow: 2px 2px 5px #cccccc;
        float:left;
        padding:10px;
    }
    .msgHeaderCt{
        margin-top: 10px;
    }
    .responseCt_msgHeaderCt{
        background-color: #cccccc;
        border: 1px solid #cccccc;
        border-radius: 2px;
        box-shadow: 2px 2px 5px #cccccc;
        float:left;
        padding:10px;
    }
    .replyCt_msgHeaderCt{
        background-color:#6699ff;
        margin-right:10px;
        border: 1px solid #ccccff;
        border-radius: 2px;
        box-shadow: 2px 2px 5px #cccccc;
        float:right;
        padding:10px;
    }
    .msgCt{
        margin-bottom: 20px;
        margin-right:10px;
    }
    .responder{
        font-size: 9pt;
        float:left;
        margin-top: 10px;
    }
    .timestamp{
        font-size: 9pt;
        float:right;
        margin-top: 10px;
    }
    .msg{
        float:left;
    }
    .contactListCt{
        float:left;
        width:180px;
        height: 405px;
        border: 1px solid #00A8EA;
        border-radius: 2px;
        box-shadow: 0px 0px 3px #cccccc;
        position: absolute;
        top:5px;
        left:5px;
    }
    .messageLogsCt{
        float:right;
        width:fit-content;
    }
    .contactListCt ul{
        padding: 5px;
    }
    .contactListCt ul li:nth-child(odd){
        background: #F8F8F8;
    }
    .contactListCt ul li{
        cursor: pointer;
        padding: 5px;
        margin: 5px;
    }
    .contactListCt ul li:hover,.firstList{
        border-left: 2px solid #FFCC66;
        color:black !important;
    }
    .contactListCt h2{
        text-align: center;
        background-color: #00A8EA;
        padding: 5px 0px;
    }
</style>
<div id="emailCt">
    <c:if test="${!(sessionScope.USER.isAppAccount())}">
        <div class="contactListCt"><h2>Contacts</h2>
            <ul>
                <c:forEach items="${jsonarray.getArrayList()}" var="contact">
                    <li wtContactId="${contact.optString("wtContactId")}">${contact.optString("firstName")}&nbsp;${contact.optString("lastName")}</li>
                </c:forEach>
            </ul>
        </div>
        <div class="messageLogsCt">
            <c:forEach items="${jsonarray.getArrayList()}" var="contact">
                <!--<div class="subject_header">Subject: ${contact.optString("subject")}</div>-->
                <div class="contactCt reviewer_tag hidden" wtContactId="${contact.optString("wtContactId")}">
                    <input class="contactjson" type="hidden" name="contactjson" value="${fn:escapeXml(contact)}" email="${contact.optString("email")}" subject="${contact.optString('subject')}"/>
                    <c:forEach var="emailLogs" items="${contact.optJSONArray('MESSAGE_LOG').getArrayList()}">
                        <c:set var="userType" value="responseCt"/>
                        <c:if test="${emailLogs.optInt('createdBy') eq sessionScope.USER.userId}">
                            <c:set var="userType" value="replyCt"/>
                        </c:if>
                        <h2 class="${userType}_msgHeaderCt ${userType}">Subject: ${contact.optString("subject")}</h2>
                        <div class="${userType} msgCt">
                            <label class="msg">${emailLogs.optString("message")}</label><br/>
                            <label class="responder">${emailLogs.optString("firstName")}&nbsp;${emailLogs.optString("lastName")}</label>
                            <label class="timestamp">${emailLogs.optString('createdDate')}</label>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
        <%--<c:forEach items="${jsonarray.getArrayList()}" var="contact">--%>
<!--            <div class="contactCt">
                <input type="hidden" name="contactjson" value="${fn:escapeXml(contact)}"/>
                <h1 class="contact_header">${contact.optString("firstName")}&nbsp;${contact.optString("lastName")}: ${contact.optString("subject")}
                    <button class="reply_btn" email="${contact.optString("email")}" subject="${contact.optString('subject')}">Send Email</button>
                </h1>-->
                <%--<c:forEach var="emailLogs" items="${contact.optJSONArray('MESSAGE_LOG').getArrayList()}">--%>
                    <%--<c:set var="userType" value="responseCt"/>--%>
                    <%--<c:if test="${emailLogs.optInt('createdBy') eq sessionScope.USER.userId}">--%>
                        <%--<c:set var="userType" value="replyCt"/>--%>
                    <%--</c:if>--%>
<!--                    <div class="${userType}">
                        <label class="msg">${emailLogs.optString("message")}</label><br/>
                        <label class="responder">${emailLogs.optString("firstName")}&nbsp;${emailLogs.optString("lastName")}</label>
                        <label class="timestamp">${emailLogs.optString("createdDate")}</label>
                    </div>-->
                <%--</c:forEach>--%>
            <!--</div>-->
        <%--</c:forEach>--%>
    </c:if>

    <c:if test="${(sessionScope.USER.isAppAccount())}">
        <c:forEach items="${jsonarray.getArrayList()}" var="contact">
            <c:if test="${sessionScope.USER.wtContact.wtContactId eq contact.optInt('wtContactId')}">
                <div class="contactCt seller_tag">
                    <input class="contactjson" type="hidden" name="contactjson" value="${fn:escapeXml(contact)}" email="${contact.optString("email")}" subject="${contact.optString('subject')}"/>
                    <c:forEach var="emailLogs" items="${contact.optJSONArray('MESSAGE_LOG').getArrayList()}">
                        <c:set var="userType" value="responseCt"/>
                        <c:if test="${emailLogs.optInt('createdBy') eq sessionScope.USER.userId}">
                            <c:set var="userType" value="replyCt"/>
                        </c:if>
                        <h2 class="${userType}_msgHeaderCt sellerAcct">Subject: ${contact.optString("subject")}</h2>
                        <div class="${userType} sellerAcct msgCt" email="${emailLogs.optString("email")}">
                            <label class="msg">${emailLogs.optString("message")}</label><br/>
                            <label class="responder">${emailLogs.optString("firstName")}&nbsp;${emailLogs.optString("lastName")}</label>
                            <label class="timestamp">${emailLogs.optString("createdDate")}</label>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
    <form class="email_form hidden">
        <input type="hidden" name="createdBy" value="${sessionScope.USER.userId}"/>
        <label>To<span style="color:red;">*</span></label><input class="validField" type="text" name="toRecip" style="width: 350px;" />
        <label>Subject<span style="color:red;">*</span></label><input class="validField" type="text" name="subject" style="width: 350px;" />
        <label>Messages<span style="color:red;">*</span></label><textarea class="validField" type="textarea" name="message" style="width: 350px;"></textarea>
    </form>
</div>
