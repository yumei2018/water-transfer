<%-- 
    Document   : reset
    Created on : Aug 26, 2015, 3:44:03 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery.validate.min.js
,${pageContext.request.contextPath}/resources/js/jquery/additional-methods.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
#reset-form{
    background-color: #f0f0f0;
    width: 100%;
    font: 15px arial;
    margin-left: 50px;
    margin-bottom: 50px;
    border: none;
}
.form-title{
    background-color: #0190c8;
    border-radius: 3px 3px 0 0;
    bottom: 0;
    color: #f9f9f9;
    font-size: 24px;
    padding: 10px 20px;
}
.field-row{
    height: 60px;
}
.field-label{
    margin-bottom: 0.5em;
    margin-left: 20px;
}
input[type="text"], input[type="password"]{
    border: 1px solid #e3e3e3;
    color: #555;
    font: 12px arial;
    height: 16px;
    padding: 6px 3px;
    margin-left: 20px;
    margin-bottom: 1.5em;
}
.button-row{
    margin-left: 150px;
}
label.error{
    color: #FB3A3A;
    font-size: 12px;
}
.hidden {
    display: none;
}
#resetbtn, Button {
    border-radius: 20px;
    background-color: #448ccb;
    padding: 10px 20px !important;
    border:medium none!important;
    cursor:pointer;
    margin:15px auto;
    color:#e9e9e9;
    text-transform: uppercase;
    font-weight: bold;
    margin-left: 10px;
}    
#resetbtn:hover  {
    background-color:#FAAD21;
}
ul {
    margin: 16px;
}
.reset {
    border: none;
    padding-left: 20px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
    var self = this;
    self.resetForm = $("#reset-form");
    self.resetBtn = $("#resetbtn");
    self.agreeCheck = $("#agree-check");
    
    var labels = {
        required: "This field is required"
        ,minlength: "Please enter at least 8 characters"
        ,pattern: "Invalid format"
        ,equalTo: "Please enter the same value as password"
    };
      
    self.resetForm.validate({
        rules:{
            password: {
                required: true
                ,minlength: 8
                ,pattern: /^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).*$/
            }
            ,password_confirm: {
                required: true
                ,equalTo: "#password"
            }
        },
        messages: {
            password: labels
            ,password_confirm: labels
        }
    });
    
    self.agreeCheck.on("click",function(){
      if(this.checked)
      {
        $(this).val("on");
      } else {
        $(this).val("off");
      }
    });
    
    self.resetBtn.on("click", function(){      
        if (self.resetForm.valid() === false){
            return false;
        } 
        
        if(self.agreeCheck.val() !== "on"){
          alert("You need agree with the disclosure statement for process."); 
          return false;                   
        }
        
//        var data = self.resetForm.serialize();
        var username = self.resetForm.find("#username").val();
        var password = self.resetForm.find("#password").val();
//        alert(username);
//        alert(password);
        var url = window.SERVER_ROOT + "/admin/reset";
        $.ajax({
            type:"POST"
            ,url:url
            ,data:{username:username,password:password}
            ,dataType:"json"
            ,success:function(data,status,jqxhr){
                alert("Reset Password Successful!");
                var url=window.SERVER_ROOT+"/authentication/logout";
//                var url=window.SERVER_ROOT+"/authentication/login";
//                window.location.assign(url);
                var url=window.SERVER_ROOT;
                window.location.assign(url);
            }
            ,error:function(xhr,errorType,exception){
                alert("Failed Reset!");
            }
        });
    });
});
</script>
<!--<label class="breadcrumbs">Home</label>--><br /><br />

<div id="apps-ct" style="width:600px;margin:20px auto 0;">
<form id="reset-form" method="POST" title="Reset Password Form">
    <div style="padding:10px 5px 0;" class="form-title">Reset Password</div>
    <fieldset class="reset"> 
      <div>
        <label class="field-label">
          <ul>
            <li>Passwords must be at least 8 characters</li>
            <li>At least 1 number, 1 lowercase, 1 uppercase letter</li>
            <li>At least 1 special character from @#$%&</li>
          <ul>
        </label>
      </div>
      <div>
        <label class="field-label" for="Username">User Name</label><br>
        <input type="text" class="validField" style="width:280px;" name="user name" id="username" value="${sessionScope.USER.username}" readonly="true"/>
      </div>
        <label class="field-label" for="Password">New Password<span style="color:red;">*</span></label><br>
        <input type="password" class="validField" style="width:280px;" name="password" id="password"/>
      <div>
        <label class="field-label" for="Password">Re-enter Password<span style="color:red;">*</span></label><br>
        <input type="password" class="validField" style="width:280px;" name="password_confirm" id="password_confirm"/>
      </div>
<!--      <div class="statement">        
        Information on the proposed water transfer inputted and saved on the Water Transfer<br>
        Information Management System will not be publicly disclosed until it is officially<br>
        submitted to DWR at which time it will become a public record subject to disclosure.<br>
        Applications not submitted to DWR by November 30 of each transfer year will be deleted.<br><br>
      </div>-->
      <div class="statement">  
        Information on the proposed water transfer that has been officially submitted to <br>
        the Department of Water Resources will become a public record and subject to <br> 
        disclosure. Applications not submitted to DWR by November 30th of each year <br>
        will be deleted.<br><br>
      </div>
      <div>
        <input type="checkbox" id="agree-check" value="" /> I Agree <span style="color:red;">*</span>
      </div>
      <div class="button-row">
        <input type="button" id="resetbtn" value="Reset Password" />
      </div>    
    </fieldset>
</form>
</div>

<%@include file="../templates/footer_login.jsp" %>
