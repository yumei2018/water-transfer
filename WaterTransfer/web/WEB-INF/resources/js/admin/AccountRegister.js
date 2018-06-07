/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var AccountRegistration = function()
{
    var self = $(this);
    self.init = function()
    {
        self.initItems();
        self.initListeners();
    };
    self.initItems = function(){
        self.registerFormCt = $("#registerFormCt");
        self.registerLink = $(".registerLink");
        self.addAgencyBtn = $(".newAgency");
    };
    self.initListeners = function(){
        self.registerLink.unbind("click",self.getRegisterForm);
        self.addAgencyBtn.unbind("click",self.initNewAgency);
        self.registerLink.on("click",self.getRegisterForm);
        self.addAgencyBtn.on("click",self.initNewAgency);
    };
    self.getRegisterForm = function()
    {
        $.ajax({
            type:'POST'
            ,url:window['SERVER_ROOT']+'/admin/accountRegister'
            ,cache:false
            ,scope:this
            ,success:function(data,status,hxhr)
            {
                self.regWin(data);
                self.initItems();
                self.initListeners();
            }
        });
    };
    self.initNewAgency = function()
    {
        self.addAgencyDialog = $("<div>");
        self.agencyList = $(this).parent().parent().find("select");
//        alert($(this).parent().parent().find("select").html());
        $.ajax({
            url:window['SERVER_ROOT']+'/proposal/getNewAgencyForm'
            ,type:'POST'
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.addAgencyDialog.append(data);
                self.addAgencyDialog.dialog({
                    title:'New Agency'
                    ,width:500
                    ,modal:true
                    ,height:200
                    ,buttons:[{
                        text:'Add'
                        ,click:function(){
                            console.log($(this).find("form").serializeArray());
                            $.ajax({
                              url:window['SERVER_ROOT']+'/proposal/addNewAgency'
                              ,type:'POST'
                              ,data:{agencyFullName: $(this).find("input[name=agencyFullName]").val()}
                              ,cache:false
                              ,success:function(data,status,jqxhr){
                                if (data === ""){
                                    alert("Agency name is duplicate.");
                                } else {
                                    alert("New Agency Added.");
                                    var jsonData = JSON.parse(data);
                                    self.agencyList.append('<option value="'+jsonData['wtAgencyId']+'">'+jsonData['agencyFullName']+'</option>');
                                }
                                self.addAgencyDialog.dialog('close');
                              }                             
                          });
                        }
                    },{
                        text:'Cancel'
                        ,click:function(){
                            $(this).dialog('close');
                        }
                    }]
                });
            }
        });
    };
    self.regWin = function(formCt){
        $("<div>",{
            html:formCt
        }).dialog({
            title:'Account Registration'
            ,modal:true
            ,width:550
            ,height:478
            ,buttons:[{
                text:'Submit'
                ,click:function(){
                    self.submitForm($(this));
//                    $(this).dialog("destroy").remove();
                }
            },{
                text:'Cancel'
                ,click:function(){
                    $(this).dialog("destroy").remove();
                }
            }]
        }).dialog("open");
    };
//    self.submitValidation=function(){
//      self.registerFormCt.validate({
//        rules:{
//          firstName:"required"
//          ,lastName:"required"
//          ,email:"required"
//          ,email2:"required"
//        },
//        messages: {
//          firstName:"<p>This field is required</p>"
//          ,lastName: "<p>This field is required</p>"
//          ,email:"<p>This field is required</p>"
//          ,email2:"<p>This field is required</p>"
//        }
//      });
//    };
    self.submitForm = function(form)
    {
        if(!self.registerFormCt.valid()){
          return false;
        }
        self.data = self.getFormData();
        $.ajax({
            type:'POST'
            ,url:window['SERVER_ROOT']+'/admin/register'
            ,data:{jsondata:JSON.stringify(self.data)}
            ,cache:false
            ,success:function(data,status,hxhr)
            {
              form.dialog("destroy").remove();
              alert("Your request has been received and is awaiting approval. DWR will reply within two business days.");
            }
        });
    };
    self.getFormData = function()
    {
        var data = new Object();
        $.each(self.registerFormCt.serializeArray(),function(i,field){
            data[field.name] = field.value;
        });
        return data;
    };
    self.init();
};
