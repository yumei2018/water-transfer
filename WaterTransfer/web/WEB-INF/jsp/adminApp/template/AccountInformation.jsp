<%--
    Document   : AccountInformation
    Created on : May 14, 2015, 1:15:57 PM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="__Javascripts">
  http://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.min.js
</c:set>
<!DOCTYPE html>
<form id="target" method="post" action="${pageContext.request.contextPath}/admin/createAccount">
    <c:if test="${not empty contact.wtContactId}">
      <div>
        <label>Email</label><br/><input type="text" name="email" value="${contact.email}"/>
      </div>
      <div>
        <label>Phone</label><br/><input type="text" name="phoneNumber" value="${contact.phoneNumber}"/>
      </div>
    </c:if>
    <div>
      <label>
        <c:choose>
          <c:when test="${not empty contact.getUser()}">Reset Username</c:when>
          <c:otherwise>Create Username<span class="asterisk">*</span></c:otherwise>
        </c:choose>
      </label><br/><input type="text" name="username" id="" value="${appuser.username}"/>
    </div>
    <div>
      <label>
        <c:choose>
          <c:when test="${not empty contact.getUser()}">Reset Password</c:when>
          <c:otherwise>Create Password<span class="asterisk">*</span></c:otherwise>
        </c:choose>
          <input type="password" class="hidden"/>
          </label><br/><input type="password" name="password" id="" <c:if test="${not empty appuser.password}">placeholder="********"</c:if>/>        
    </div>
    <div>
      <label>Assign to Group</label><br/>
      <select class="groupList" name="groupId" id="groupId">
        <c:set var="appGroup" value="${appuser.appGroupCollection[0].groupId}"></c:set>
        <c:forEach var="group" items="${groupList}">
          <option value="${group.groupId}" <c:if test="${appGroup==group.groupId}">selected</c:if>>${group.name}</option>
        </c:forEach>
      </select>
    </div>
    <div>
        <input type="radio" name="active" value="1"
        <c:if test="${isActive eq 1}">
                checked
            </c:if>
        ><label>Active</label>
        <input type="radio" name="active" value="0"
        <c:if test="${isActive eq 0}">
                checked
            </c:if>
        ><label>Inactive</label>
    </div>

    <input type="hidden" name="firstname" value="${contact.firstName}"/>
    <input type="hidden" name="lastname" value="${contact.lastName}"/>
<!--    <input type="hidden" name="email" value="${contact.email}"/>-->
    <input type="hidden" name="userId" value="${appuser.userId}"/>
    <input type="hidden" name="WtContactId" value="${contact.wtContactId}"/>
    <input type="hidden" name="createdDate" value="${appuser.createdDate}"/>
    <input type="hidden" name="regUserId" value="${regUser.userId}"/>
    <input type="submit" name="submit" value="Submit" onclick="return false;"/>
</form>
<script type="text/javascript">
    var accountInfo=function(){
      var self = this;
      self.init=function(){
        self.accountForm = $("#target");
        self.username = self.accountForm.find("input[name=username]");
        self.userId = self.accountForm.find("input[name=userId]");
        self.submitBtn = self.accountForm.find("input[name=submit]");
        self.initListeners();
      };
      self.initListeners=function(){
        self.username.on("blur",self.duplicateValidation);
//        self.submitValidation();
      };
//      self.submitValidation=function(){
//        self.accountForm.validate({
//          rules:{
//            username: "required"
//            ,password: "required"
//          },
//          messages:{
//            username: "<p>Username Required.</p>"
//            ,password: "<p>Password Required.</p>"
//          },
//          submitHandler: function(form) {
//            form.submit();
//          }
//        });
//      };
      self.duplicateValidation=function(){
//        alert("Validation Here");
        var username = $(this).val();
        var userId = self.userId.val();
//        alert(userId);
        $.ajax({
          type:"POST"
          ,url:window['SERVER_ROOT']+"/admin/checkUsername"
          ,data: {username:username,userId:userId}
          ,cache:false
          ,dataType: "json"
          ,success:function(data,status,jqxhr){
//            alert(data.user);
            if(data.user === 'exist'){
              self.username.val('');
              self.userId.val('');
              alert('Username exist, choose another one!');              
            }
          }
          ,error:function(xhr, errorType, exception){
            alert(errorType+" Exception:"+exception);
          }
        });        
      };
      self.init();
    };
  
    $(document).ready(function(){
        new accountInfo();
    });
      
</script>
<style>
  div input,select{
    margin-bottom: 10px;
  }
  .asterisk{
    color: red;
  }
</style>