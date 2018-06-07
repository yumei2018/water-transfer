<%-- 
    Document   : login
    Created on : Jan 28, 2015, 1:34:27 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <base href="${pageContext.request.contextPath}">
        
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wtindex.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/footer.css">
        <link href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/themes/ui-darkness/jquery-ui.min.css" rel="stylesheet">
        
        <script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
        <script type="text/javascript">window['SERVER_ROOT'] = "${pageContext.request.contextPath}";</script> 
        
        <script type="text/javascript">
        var LoginForm = function(id){
            var self = this;
            self.formId=id;
            self.init=function(){
                self.initItems();
                self.initEvents();
            };
            self.initItems=function(){              
                window['LOADMASK'] =
                $("<div></div>")
                .css({
                    backgroundImage: "url('"+window['SERVER_ROOT'] + "/resources/images/loading.gif')"
                    ,padding:"60px 0 0 35px"
                    ,backgroundRepeat:"no-repeat"
                    ,fontSize:"14pt"
                })
                .html("Loading...")
                .dialog({
                    resizable:false
                    ,dialogClass:"loadmask"
                    ,modal:true
                    ,width:120
                    ,height:155
                    ,autoOpen:false
                });

                self.formEl = $("form#" + self.formId);
                self.usernameEl = self.formEl.find("input#username");
                self.passwordEl = self.formEl.find("input#password");
                self.submitBtn = self.formEl.find("input[type=submit]");
                self.usernameEl.on("blur",self.setEmptyText);
                self.passwordEl.on("blur",self.setEmptyText);
                self.usernameEl.on("focus",self.unsetEmptyText);
                self.passwordEl.on("focus",self.unsetEmptyText);
                self.usernameEl.on("keydown",self.unsetEmptyText);
                self.passwordEl.on("keydown",self.unsetEmptyText);
                self.setEmptyText.call(self.usernameEl);
                self.setEmptyText.call(self.passwordEl);
            };
            self.unsetEmptyText=function()
            {
                $(this).removeClass("empty");
                if ($(this).val() == $(this).attr("emptyText"))
                {
                    $(this).val("");
                }
            }
            self.setEmptyText=function()
            {
                if ($(this).val() == $(this).attr("emptyText") || $(this).val() == "")
                {
                    $(this).addClass("empty").val($(this).attr("emptyText"));
                }
                else
                {
                    $(this).removeClass("empty");
                }
            }
            self.initEvents=function(){
                self.submitBtn.on("click",self.submit);
            };
//            self.setPassword=function(rawpass)
//            {
//                self.passwordEl.val($.md5(rawpass));
//            };
    
            self.validate=function()
            {
                var errorMsg = [];
                if (!self.usernameEl.val())
                {
                    errorMsg.push("The username is required!");
                }
        
                if (!self.passwordEl.val())
                {
                    errorMsg.push("The password is required!");
                }
        
                if (errorMsg.length > 0)
                {
                    $("<div></div>")
                    .html(errorMsg)
                    .dialog({
                        title:"Error(s)"
                        ,dialogClass:'alert'
                        ,buttons:{
                         OK:function(){
                                $(this).dialog("close");
                            }
                        }
                    }).parent().addClass("ui-state-error");
            
                    return false;
                }
        
                return true;
            };
    
            self.submit=function()
            {
                if(self.validate())
                {
                    window['LOADMASK'].dialog("open");
            
                    $.ajax({
                        url:self.formEl.attr("action")
                        ,method:"POST"
                        ,data:{password:self.passwordEl.val(),username:self.usernameEl.val()}
                        ,dataType:"json"
                        ,success:function(data,status,jqxhr)
                        {
                            window['LOADMASK'].dialog("close");
                    
                            if (data.success)
                            {
                                location = window['SERVER_ROOT'] + "/";
                            }
                            else
                            {
                                $("<div></div>")
                                .html(data.error || "The username and password did not matched!")
                                .dialog({
                                    title:"Authentication Failed"
                                    ,modal:true
                                });
                            }
                        }
                    });
                }
            };
            self.init();
        }
        $(document).ready(function(){
            new LoginForm("signInForm");
        });
        </script>
    </head>
   
    <body>
        <div id="body-content-wrapper">
        <div id="header" class="corner-top-10">
            <image src="${pageContext.request.contextPath}/resources/images/header_ca.gov.png" class="inline" id="header-seal"/>
            <div class="inline">
                <div id="header-title">Department of Water Resources</div>
                <div id="header-subtitle">Water Transfer Information Application System</div>
                <div></div><p></p><p></p>
            </div>
        </div>
        
        <div id="body-content">
            <div id="framecontent-left">    
                <form id="signInForm" class="" style="" method="POST" action="${pageContext.request.contextPath}/authentication/login" onsubmit="return false;">
                    <div class="form-title">
                        <span class="text">Welcome!</span><br />
                        <span class="subtext">Please sign in to get started.</span>
                    </div>
                    <div class="form-body">
                        <!--<div class="field">
                            <label for="username" class="hidden">Login ID</label><input class="empty" emptyText="Login ID" type="text" id="username" name="username" value="Login ID" />
                        </div>
                        <div class="field">
                            <label for="password" class="hidden">Password</label><input class="empty" emptyText="Password" type="password" id="password" name="password" value="Password" />
                        </div>-->
                        <div class="field">
                            <input class="empty" type="text" id="username" emptyText="Login ID" value="Login ID"/>
                        </div>
                        <div class="field">
                            <input class="empty" type="password" id="password" emptyText="Password"  />
                        </div>
                        <div class="buttons">          
                            <input class="signin-btn" type="submit" id="signin-btn" value="Sign In"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
                
        <div id="dialog" title="Login dialog"></div>
<%@include file="footer.jsp" %>        
       
