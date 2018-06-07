/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 var emailMessageLog = function(){
    var self = this;
    self.init = function()
    {
        self.initItems();
        self.listeners();
        self.initSelection();
    };
    self.initItems = function()
    {
        self.emailCt = $("#emailCt");
        self.contactCt = self.emailCt.find(".contactCt");
        self.responseCt = self.contactCt.find(".responseCt");
        self.replyBtn = self.emailCt.find(".reply_btn");
        self.emailForm = self.emailCt.find(".email_form");
        self.requireFields = self.emailForm.find(".validField");
        self.contactList = self.emailCt.find(".contactListCt");
        self.headerCt = $("#header");
        self.userId = self.headerCt.find("#header-rcol input[name=userid]").val();
        self.json = new Object();
    };
    self.listeners = function()
    {
        self.contactCt.find(".contact_header").on("click",self.contactHandler);
        self.contactCt.find(".contact_header .reply_btn").on("click",function(e){
            e.stopPropagation();
        });
        self.emailForm.find(".validField").on("blur",function(){
            $(this).val() ? $(this).removeClass("isValid") : $(this).addClass("isValid");
        });
        self.contactList.find("ul li").on("click",self.contactListHandler);
    };
    self.initSelection = function()
    {
        self.firstEl = self.contactList.find("ul li:nth-child(1)");
        self.firstEl.addClass("firstList");
        $.each(self.contactCt,function(){
            $(this).removeClass("hidden").hide();
            if($(this).attr("wtcontactid") === self.firstEl.attr("wtcontactid"))
            {
                $(this).removeClass("hidden").show();
            }
        });
    };
    self.contactListHandler=function()
    {
        self.contact = $(this);
        self.contactId=self.contact.attr("wtcontactid");
        self.contactList.find("ul li").removeClass("firstList");
        self.contact.addClass("firstList");
        $.each(self.contactCt,function(){
            $(this).removeClass("hidden").hide();
            if($(this).attr("wtcontactid") === self.contactId)
            {
                $(this).removeClass("hidden").show();
            }
        });
    };


    self.replyBtnHandler = function()
    {
        self.hightlight = self.contactList.find(".firstList");
        self.contactInfo="";
        $.each(self.contactCt,function(){
            if($(this).attr("wtcontactid") === self.hightlight.attr("wtcontactid"))
            {
                self.contactInfo = $(this);
            }
        });
        var contactJson = JSON.parse(self.contactInfo.find(".contactjson").val());
        self.json['wtContactId']=contactJson['wtContactId'];
        self.json['wtAgencyId']=contactJson['wtAgencyId'];
        self.json['wtTransId'] = self.getTransId();
        self.emailForm.find("input[name=subject]").val((self.contactInfo.find(".contactjson").attr("subject")));
        self.emailForm.find("input[name=toRecip]").val(self.contactInfo.find(".contactjson").attr("email"));
        if(self.responseCt.attr("email"))
        {
            self.emailForm.find("input[name=toRecip]").val(self.responseCt.attr("email"));
        }
        self.emailDialog = $("<div>").append(self.emailForm.show()).dialog({
            title:'Email'
            ,width:400
            ,height:380
            ,modal:true
            ,closeOnEscape: false
            ,resizable:false
            ,buttons:[{
                text:'Send'
                ,click:function()
                {
                    self.sendEmail();
                }
            },{
                text:"Cancel"
                ,click:function()
                {
                    $(this).dialog('destroy').remove();
                }
            }]
        });
    };
    self.contactHandler = function()
    {
        self.contactDivList = $(this).parent().find("div");
        self.contactDivList.slideToggle("fast");
    };
    self.emailValidator = function()
    {
        self.isValid = true;
        $.each(self.requireFields,function(){
            if(!$(this).val())
            {
                $(this).addClass("isValid");
                self.isValid = false;
            }
        });
        return self.isValid;
    };
    self.sendEmail = function()
    {
        self.param = self.emailForm.serializeArray();
        $.each(self.param,function(index,value){
            self.json[value.name]=value.value;
        });
        if(self.emailValidator())
        {
            self.emailDialog.dialog('destroy').remove();
            $.ajax({
              type:'POST'
              ,url:window.SERVER_ROOT + "/proposal/sendEmail"
              ,cache:false
              ,data:{jsondata:JSON.stringify(self.json)}
              ,success:function(data,status,jqxhr){
                var response = JSON.parse(data);
                try{
                    if(!response.success){
                      throw response.error || "Unable to send email.";
                    }
                    $.each(self.requireFields,function(){
                        $(this).val(""); //reset email form
                    });
                  }catch(e){
                    if (response.callback) {
                      var callback = eval(response.callback);
                      if (typeof callback === "function") {
                        callback.call();
                      }
                    }else if (e){
                      alert(e);
                    }
                  }
                }
            });
        }
    };
    self.getTransId = function()
    {
        var id = "";
        try{id=parseInt(/\/WaterTransfer\/proposal\/edit\/(\d*)/.exec(location.pathname).pop());}catch(e){}
        return id ? id : "";
    };
};

