<%--
    Document   : AccountTable
    Created on : May 14, 2015, 1:15:27 PM
    Author     : pheng
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery.validate.min.js
<!--,https://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.min.js-->
</c:set>
<%@include file="../templates/header.jsp" %>
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
    <div id="admin_manager" style="overflow: hidden;">
        <div id="account_list" style="float:center;">
            <%@include file="template/AccountTable.jsp" %>
        </div>
        <input type="hidden" name="type" value="${type}" />
            <!--<h2>Account Manager</h2>-->
        <div id="acctManager"></div>
    </div>
  </c:if>
<%@include file="../templates/footer.jsp" %>
<script>
    var AdminManager = function(){
        var self = $(this);
        self.init=function()
        {
            self.initItems();
            self.initListeners();
            self.accordionListeners();
        };
        self.initItems=function(){
            self.adminManager = $("#admin_manager");
//            self.accordionCt = self.adminManager.find("#account_list #tabs");
            self.accordionCt = self.adminManager.find("#account_list #accordion");
            self.manageAccount = self.adminManager.find("#acctManager");

            self.manageAccountForm = self.manageAccount.find("form");
            self.submitBtn = self.manageAccountForm.find("input[name=submit]");
//            self.accountTableCt = self.accordionCt.find("#registered table");
            self.accountTableCt = self.accordionCt.find("table");
            self.tableBody = self.accountTableCt.find("tbody");
            self.type = self.adminManager.find("input[name=type]");
            self.userPermission = self.manageAccount.find(".user_perm");
            self.avalPermission = self.manageAccount.find(".aval_perm");

            self.addBtn = self.manageAccount.find(".addRemoveCt .addBtn");
            self.removeBtn = self.manageAccount.find(".addRemoveCt .removeBtn");
            self.savePermBtn = self.manageAccount.find(".saveCt button");
        };
        self.initListeners=function(){
            self.tableBody.find("tr").on("click",self.tableSelection);
        };
        self.accordionListeners = function(){
            self.accordionCt.accordion({
                animate:200
                ,heightStyle:"content"
                ,collapsible:true
//                ,active:false
                }
            )
        }
        self.initAfterRenderListeners = function()
        {
            self.submitBtn.on("click",self.submit);
            self.userPermission.find("tbody tr").on("click",self.permTableSelection);
            self.avalPermission.find("tbody tr").on("click",self.permTableSelection);
            self.addBtn.on("click",self.addPermissionHandler);
            self.removeBtn.on("click",self.removePermissionHandler);
            self.savePermBtn.on("click",self.savePermissionHandler)
        };
        self.getSubmitData=function(){
            var obj =new Object();
            $.each(self.manageAccountForm.serializeArray(),function(i,field){
                if(field.value)
                {
                    obj[field.name]=field.value;
                }
            });
            return obj;
        };
        self.submit = function()
        {
            if(self.isValid())
            {
               return false;
            }
            self.url = self.manageAccountForm.attr("action");
            self.data = self.getSubmitData();
            $.ajax({
                type:'POST'
                ,url:self.url
                ,data:{jsondata:JSON.stringify(self.data)}
                ,cache:false
                ,scope:this
                ,dataType: "json"
                ,success:function(data,status,jxhr)
                {
//                    self.emailNotification();
//                    alert(data);          
                    if(data.success === true){
                      alert("Email Notification send out!");
                    } else if(data.success === false){
                      alert("Send out Email failed!");
                    } else {
                      alert("Saving Successfull!");
                    }
                    location.reload(true);
                    self.manageAccount.dialog("close");
                }
                ,error:function(){
                    alert("error sending email");
                    self.manageAccount.dialog("close");
                }
            });
        };
        self.emailNotification = function(){
            $("<div>",{
                html:'Saving Successfull!<br/>Would you like to send an Email Nofitication?'
            }).dialog({
                title:'Email Notification'
                ,modal:true
                ,width:400
                ,height:200
                ,buttons:[{
                    text:'Yes'
                    ,click:function(){
                        $(this).dialog("destroy").remove();
                        self.data = self.getSubmitData();
                        $.ajax({
                            type:'POST'
                            ,url:window['SERVER_ROOT']+'/admin/sendMail'
                            ,cache:false
                            ,data:{jsondata:JSON.stringify(self.data)}
                            ,success:function(data,status,jxhr)
                            {
                                location.reload(true);
                            }
                        });
                    }
                },{
                    text:'No'
                    ,click:function(){
                        location.reload(true);
                        $(this).dialog("destroy").remove();
                    }
                }]
            }).dialog("open");
        };
        self.isValid=function()
        {
            var isEmpty = false;
            self.userId = self.manageAccountForm.find("input[name=userId]").val();
            self.userName = self.manageAccountForm.find("input[name=username]").val();
            self.password = self.manageAccountForm.find("input[name=password]").val();
            
            if(!self.userId){
              if(self.userName === "" || self.password === "") 
              {
                alert("You need fill all required field(s).");
                isEmpty = true;
              }
            }
//            if(!self.userId)
//            {
//                $.each(self.manageAccountForm.find("input[type=text]"),function(){
//                    if($(this).val() === "")
//                    {
//                        return isEmpty = true;
//                    }
//                });
//            }
            return isEmpty;
        };
        self.tableSelection = function()
        {
//            alert("selected");
            self.tableBody.find("tr").removeClass("selected");
            $(this).addClass("selected");
            self.selectedId = $(this).find("td:first-child").html();
            self.selectedIdType = $(this).closest('tr').attr("typeId");
            var data = null;
//            alert(self.selectedId);
//            alert(self.selectedIdType);
            if (self.selectedIdType === "regUser"){
              data = {userId:self.selectedId};
            } else if (self.selectedIdType === "contact"){
              data = {contactId:self.selectedId};
            }
            $.ajax({
               type:'POST'
               ,url:self.getType()
               ,data:data
               ,cache:false
               ,scope:this
               ,success:function(data,status,jxhr){
//                    self.manageAccount.children().remove();
                    self.manageAccount.append(data);
                    self.initItems();
                    self.initAfterRenderListeners();                            
                    self.manageAccount.dialog({
                        title:'Account Manager'
                       ,width:500
                       ,height: 500
                       ,resizable: false
                       ,modal:true
                       ,autoOpen:false
                       ,close:function(){
                           self.manageAccount.dialog("destroy");
                           self.manageAccount.children().remove();
                       }
                    }).dialog('open');                    
               }
            });
        };
        self.permTableSelection = function(){
            self.userPermission.find("tr").removeClass("selected");
            self.avalPermission.find("tr").removeClass("selected");
            $(this).addClass("selected");
        };
        self.findDuplicatePermission = function()
        {
            var isDuplicate = false;
            self.userPerm = self.userPermission.find('tbody tr td input[name=groupid]');
            $.each(self.userPerm,function(){
                if($(this).val()===self.getSelectedAvalGroupId())
                {
                    return isDuplicate = true;
                }
            });
            return isDuplicate;
        };
        self.findDuplicateAvalPermission = function()
        {
            var isDuplicate = false;
            self.avalPerm = self.avalPermission.find('tbody tr td input[name=groupid]');
            $.each(self.avalPerm,function(){
                if($(this).val()===self.getSelectedUserGroupdId())
                {
                    return isDuplicate = true;
                }
            });
            return isDuplicate;
        };
        self.addPermissionHandler = function()
        {
            self.selectedAvalPermission = self.avalPermission.find('tbody .selected');
            if(!self.selectedAvalPermission || self.findDuplicatePermission())
            {
                return false;
            }
            self.userPermission.find("tbody:last").append(self.selectedAvalPermission);
        };
        self.removePermissionHandler = function()
        {
            self.selectedUserPermission = self.userPermission.find('tbody .selected');
            if(!self.selectedUserPermission)
            {
                return false;
            }
            self.findDuplicateAvalPermission() ? self.selectedUserPermission.remove()
                :self.avalPermission.find("tbody:last").append(self.selectedUserPermission);
        };
        self.savePermissionHandler = function()
        {
            self.grpIdArray = [];
            self.input = self.userPermission.find("tbody tr input[name=groupid]");
            $.each(self.input,function(i){
                self.grpIdArray.push($(this).val());
            });
            $.ajax({
                type:'POST'
                ,url:window['SERVER_ROOT']+'/permission/editPermission'
                ,data:{contactId:self.getSelectedWtContactId(),groupId:self.grpIdArray.toString()}
                ,cache:false
                ,scope:this
                ,success:function(data,status,jxhr)
                {
                    location.reload(true);
                }
            });
        };
        self.getType = function()
        {
            if(self.type.val() == "permission")
            {
                return window['SERVER_ROOT']+"/permission/getPermissionInfo";
            }
            if(self.type.val() == "account")
            {
                return window['SERVER_ROOT']+"/admin/getAccountInfo";
            }
        };
        self.getSelectedWtContactId = function()
        {
            self.selectedContact = self.accordionCt.find("table .selected");
            self.contactid = self.selectedContact.find("input[name=wtcontactid]").val();
            return self.contactid;
        };
        self.getSelectedAvalGroupId = function()
        {
            self.selectedPermission = self.avalPermission.find('.selected');
            self.groupid = self.selectedPermission.find("input[name=groupid]").val();
            return self.groupid;
        };
        self.getSelectedUserGroupdId = function()
        {
            self.selectedPermission = self.userPermission.find('.selected');
            self.groupid = self.selectedPermission.find("input[name=groupid]").val();
            return self.groupid;
        };
        self.init();
    };
    new AdminManager();
</script>
<style>
    #account_list table tbody tr:hover{
        background:#ccffff;

    }
    #account_list table tbody tr{
        cursor:pointer;
    }
    .selected{
        background:#ccffff;
    }
    table.user_perm, table.aval_perm{
        border-collapse: collapse;
    }
    table.user_perm,table.aval_perm,.addRemoveCt{
        float:left;
    }
    table.user_perm th, table.aval_perm th{
        background: #cccccc;
        font-weight: bold;
        border: 1px solid black;
    }
    table.user_perm tbody, table.aval_perm tbody{
        border: 1px solid black;
    }
    table.user_perm td, table.aval_perm td{
        cursor: pointer;
    }
    table.user_perm tr:hover, table.aval_perm tr:hover{
        background:#ccffff;
    }
    .saveCt{
        clear:both;
        float:right;
        margin-top:10px;
    }
    button{
        cursor: pointer;
    }
    .hidden{
        display: none;
    }
    h2 {
        margin-top: 10px;
        margin-left: 600px;
    }
</style>