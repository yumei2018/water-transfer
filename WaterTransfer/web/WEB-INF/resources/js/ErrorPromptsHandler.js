/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var ErrorPromptsHandler = {
  SESSIONTIMEOUT:function(errmsg,callback){
    var password = $("<input/>",{
        name:'password'
        ,type:'password'
        ,placeholder:'***********'
        ,style:'margin:10px 55px;width:250px;'
    });
    var username = $("<input/>",{
        name:'username'
        ,type:'text'
        ,placeholder:'username'
        ,style:'margin:40px 0px 0px 55px;width:250px;'
    });
    $("<div/>",{
        html:'<h2>Your session has timed out. To continue editing please enter your credentials and click "sign in" to reestablish your session. Otherwise, click exit to return to the sign in page.</h2>'
    })
    .append(username)
    .append(password)
    .append('<span style="color: red;">Please click save after logging in to save your work.</span>')
    .dialog({
      modal:true
      ,title:"Sign In or Exit"
      ,width:400
      ,resizable:false
      ,autoOpen:true
      ,create:function(evt,ui){
        var titleBar = $(this).prev(".ui-dialog-titlebar");
        var closeBtn = titleBar.find("button.ui-dialog-titlebar-close");

        if (closeBtn.length > 0){
          closeBtn.remove();
        }
      }
      ,buttons:[{
            text:'SIGN IN'
            ,'style':'background:#0279A4;color:white;'
            ,click:function(){
                var params = {};
                var password =  $(this).find("input[name=password]");
                var username = $(this).find("input[name=username]");
                params[password.attr('name')]=password.val();
                params[username.attr('name')]=username.val();
                params['phaseNo'] = window.PHASENO;
                $.ajax({
                    url:window.SERVER_ROOT+"/authentication/login"
                    ,method:'POST'
                    ,data:params
                    ,dataType:'json'
                    ,success:function(response,status,jqxhr){
                        if(!response.success){
                            alert("The password or username is incorrect.");
                        }
                    }
                    ,error:function(response,status,jqxhr){
                      alert(response);
                    }
                });
                $(this).dialog("close");
            }
      },{
            text:'EXIT'
            ,click:function(){
                $(this).dialog("close");
                location = window.SERVER_ROOT;
            }
      }]
    });
  }
  ,AGENCYACCOUNTEXISTS:function(errmsg,cancelHandler){
    $("<div/>",{
      html:errmsg
    })
    .dialog({
      modal:true
      ,title:"Agency Account Exists"
      ,autoOpen:true
      ,width:400
      ,create:function(evt,ui){
        var titleBar = $(this).prev(".ui-dialog-titlebar");
        var closeBtn = titleBar.find("button.ui-dialog-titlebar-close");
        if (closeBtn.length > 0){
          closeBtn.remove();
        }
      }
      ,buttons:{
        "Login":function(){
          location = window.SERVER_ROOT + "/login";
        }
        ,"Cancel":function(){
          // stays on page
          if (typeof cancelHandler == "function"){
            cancelHandler.call(this);
          }
        }
      }
    });
  }
  ,ACCOUNTEMAILEXISTS:function(errmsg,cancelHandler){
    var dialog = $("<div/>",{
      html:errmsg
    })
    .dialog({
      modal:true
      ,title:"Account Exists"
      ,autoOpen:true
      ,create:function(evt,ui){
        var titleBar = $(this).prev(".ui-dialog-titlebar");
        var closeBtn = titleBar.find("button.ui-dialog-titlebar-close");
        if (closeBtn.length > 0){
          closeBtn.remove();
        }
      }
      ,buttons:{
        "Cancel":function(){
          $(this).parent().remove();
          // stays on page
          if (typeof cancelHandler == "function"){
            cancelHandler.call(this);
          }
        }
      }
    });
  }
  ,AGENCYACCOUNTEXISTSONSAVE:function(msg){
    ErrorPromptsHandler.AGENCYACCOUNTEXISTS.call(this,msg,function(){
      $(this).parent().remove();
    })
  }
}

