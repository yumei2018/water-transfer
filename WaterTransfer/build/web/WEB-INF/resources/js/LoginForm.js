/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var LoginForm=function(cfg){
  var self=$.extend(this,cfg || {});
  self.mask=$("<div>Loading...</div>").dialog({dialogClass:"mask",modal:true}).dialog("close");
  self.showMask=function(){self.mask.dialog("open");return self;};
  self.hideMask=function(){self.mask.dialog("close");return self;};
  self.fieldCssCfg={
    border: "none"
    ,padding:"2%"
    ,fontSize:"12pt"
    ,marginBottom:"10px"
    ,display: 'inline-block'
    ,height: '18px'
    ,width:"96%"
  };
  self.init=function(){
    return self.initItems().initEvents();
  };
  self.initItems=function(){
    return self.initContainer().onInitItems();
  };
  self.initUserIdField=function(){
    self.userIdField = $("<input/>")
                      .attr({
                        name:"username"
                        ,autocomplete:"off"
                        ,type:"text"
                        ,placeholder:"User ID"
                      })
                      .css(self.fieldCssCfg)
                      .appendTo(self.formEl);

    var userId = document.cookie.replace(/(?:(?:^|.*;\s*)userid\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    if (userId){
      self.userIdField.val(userId);
      self.userIdField.blur();
      self.isRemember = true;
    } else {
      self.userIdField.val(self.userIdField.attr("emptyText"));
      self.isRemember = false;
    }
    return self;
  };
  self.initPasswordField=function(){
    self.passwordField = $("<input/>")
                      .attr({
                        name:"password"
                        ,autocomplete:"off"
                        ,class:"empty"
                        ,type:"password"
                        ,emptyText:"Password"
                      })
                      .css(self.fieldCssCfg)
                      .appendTo(self.formEl);
    self.passwordField.val(self.passwordField.attr("emptyText"));
    return self;
  };
  self.initButtons=function(){
    self.submitBtn = $("<button>Login</button>")
        .css({
          float: 'right'
        })
        .addClass('button')
//        .mouseenter(function(){
//          $(this).css({backgroundColor:"#fbad23"});
//        })
//        .mouseout(function(){
//          $(this).css({backgroundColor:"#ddd"});
//        })
        .appendTo(self.formEl);
    return self;
  };
  
  self.initRememberMe = function(){
    self.rememberMe = $('<input/>',{
      type: 'checkbox'
      ,id: 'remember-me'
      ,css: {
        'margin': '25px 5px 0 0',
        height: '13px',
        width: '13px',
        display: 'inline-block'
      }
    });
    self.rememberMe.change(self.rememberMeHandler);
    self.rememberMe.appendTo(self.formEl);
    if (self.isRemember)
      self.rememberMe.prop('checked',true);
    $('<label/>', { 
      for: 'remember-me'
      ,html: 'Remember me'
    }).appendTo(self.formEl);
    
    return self;
  }
  
  self.rememberMeHandler = function(event){
    if (event.currentTarget.checked){
      document.cookie = 'userid='+self.userIdField.val();
    } else {
      document.cookie = 'userid=';
    }
  }
  
  self.initContainer=function(){
    if (!self.id){
      throw "A container id is required!";
    }
    self.container = $("#" + self.id);
    if (self.container.length==0){
      throw "A container with the id '" + self.id + "' must exist!";
    };
    return self.initForm();
  };
  self.initForm=function(){
    self.formEl = $("<form/>").appendTo(self.container);
    return self.initFields();
  };
  self.initFields=function(){
    return self.initUserIdField()
              .initPasswordField()
              .initButtons()
              .initRememberMe();
    return self;;
  }
  self.onInitItems=function(){
    return self;
  }
  self.initFieldToggleEvent=function(){
    var selectors = "input[name=" + self.userIdField.attr("name") + "]";
    selectors += ",input[name=" + self.passwordField.attr("name") + "]";
    $(selectors).on("focus",function(){
      $(this).val($(this).val().replace($(this).attr("emptyText"),"")).removeClass("empty");
    })
    .on("blur",function(){
      if ($(this).val().trim() == ""){
        $(this).val($(this).attr("emptyText")).addClass("empty");
      }
    });
    return self;
  };
  self.initEvents=function(){
    return self.initFieldToggleEvent().initSubmitBtnEvent().onInitEvents();
  };
  self.initSubmitBtnEvent=function(){
    self.submitBtn.click(function(evt){
      evt.preventDefault();
      self.signIn();
    });
    return self;
  };
  self.getVal=function(field){
    var val = field.val();
    return val.replace(field.attr("emptyText"),"");
  }
  self.getUserId=function(){
    return self.getVal(self.userIdField);
  };
  self.getPassword=function(){
    return self.getVal(self.passwordField);
  }
  self.validate=function(){
    var valid=self.getUserId() != "";
    valid = valid && self.getPassword() != "";
    
    if (!valid){
      self.showMsg("Error","All fields are required!");
    }
    
    return valid;
  }
  self.errorHighlight=function(field){
    var errorBorder="1px solid red";
    var origBorder=field[0].style.border;
    field.css({border:errorBorder});
    setTimeout(function(){
      field.css({border:origBorder});
    },2000);
    return self;
  }
  self.errorHighlights=function(){
    if (self.getUserId() == ""){
      self.errorHighlight(self.userIdField);
    }
    if (self.getPassword() == ""){
      self.errorHighlight(self.passwordField);
    }
    return self;
  }
  self.signIn=function(evt){
    if (self.validate()){
      self.showMask("Signing in...");
      $.ajax({
        url:window.SERVER_ROOT + "/authentication/login"
        ,data:{username:self.getUserId(),password:self.getPassword()}
        ,type:"post"
        ,dataType:"json"
        ,success:function(response,status,jqxhr){
          self.hideMask();
          if (!response.success){
            self.showMsg("Authentication Failed", response.error || "Unable to sign in with the user id and password. Please try again.");
            return;
          }
          location = window.SERVER_ROOT + "/";
        }
        ,error:function(XMLHttpRequest,status,err){
          self.hideMask();
//          self.showMsg(status,err);
          if (XMLHttpRequest.readyState === 0) {
            self.showMsg("Network Error");
          } else {
            self.showMsg("Authentication Failed", "Unable to sign in with the user id and password. Please try again.");
          }
//          location.reload(true);
        }
      });
    }
    return self;
  };
  self.showMsg=function(title,msg){
    var DELAY_TWO_SECONDS=2000;
    var d = $("<div>")
            .html(msg)
            .dialog({
              modal:true
              ,title:title
            });

    setTimeout(function(){
      d.dialog("close")
    },DELAY_TWO_SECONDS);
    return self;
  }
  self.onInitEvents=function(){return self;};
  self.init();
};

