/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
if (typeof GEI == 'undefined')
{
    var GEI = {};
}

GEI.Print=function(opts){
    var self = this;
    var configs={};
    configs.editable = !!opts&&!!opts.editable;
    self.init=function(){
        self.repositionHelpDiv();
        self.initTinyMCE();
        self.initReviewApproveButtons();
        self.htmlContent();
        $(window).resize(self.repositionHelpDiv);
        return self;
    }

    self.initReviewApproveButtons=function(){
        self.reviewBtn = $("#review-btn").on("click",self.review);
        self.approveBtn = $("#approve-btn").on("click",self.approve);
        self.disapproveBtn = $("#disapprove-btn").on("click",self.disapprove);
    }

    self.initTinyMCE=function(){
        if (configs.editable && typeof tinymce == "object"){
            var tmeditorCfg = {
                selector:".editable"
                ,width:"100%"
                ,resize:false
                ,plugins:["paste","table","spellchecker"]
                ,paste_auto_cleanup_on_paste:true
                ,paste_remove_styles:true
                ,statusbar: false
                ,toolbar_item_size: "small"
                ,formats:false
                ,menubar:false
                ,forced_root_block: false
    //            ,toolbar:"undo redo | bold italic underline | bullist outdent indent "
    //            ,fontsize_formats:"8 8.5pt 9pt 9.5pt 10pt 10.5pt 11pt 11.5pt 12pt 12.5pt 13pt 13.5pt 14pt 18pt 24pt 36pt"
                ,toolbar:["undo redo"
                            ,"alignleft aligncenter alignright alignjustify"
                            ,"bold italic underline"
                            ,"table"
                            ,"bullist numlist outdent indent"
                            ,"spellchecker"].join(" | ")
    //            ,browser_spellcheck : true
                ,paste_as_text: true
                ,bullist_active:true
                ,inline:true
                ,spellchecker_rpc_url:window["SERVER_ROOT"]+"/req/actionplan/spellcheck"
                ,spellchecker_languages: 'English=en'
                ,setup:function(ed)
                {
                    ed.on('keydown', function(e)
                    {
                        if(e.keyCode===10/* TAB */)
                        {
                            if (e.shiftKey)
                            {
                                this.buttons.outdent.onclick();
                            }
                            else
                            {
                                this.buttons.indent.onclick();

                            }
                            e.preventDefault();
                        }
                    });

                    ed.on("blur",function(evt){
                        window['MASK'].close();
                        if ($(".mce-btn.mce-active[aria-label=Spellcheck]").length > 0)
                        {
                            ed.buttons.spellchecker.onclick();
                        }
                        self.save.call(this,evt);
                    });
                    ed.on("focus",function(evt){
                        window['MASK'].show();
                        setTimeout(function(){
                            var tmed = ed; //tinymce.get(this.id);
                            var tbarEl = tmed.theme.panel.getEl();
                            var edEl = tmed.getElement();

                            if (tbarEl.offsetWidth < edEl.offsetWidth)
                            {
                                $(tbarEl).width(edEl.offsetWidth-2);
                            }
                            else
                            {
                                $(edEl).width(tbarEl.offsetWidth-20);
                            }

                            if ($(".mce-btn.mce-active[aria-label=Spellcheck]").length == 0)
                            {
                                ed.buttons.spellchecker.onclick();
                            }
                        },10);
                    });
                }
                ,init_instance_callback:function(ed){
//                    if (ed.getContent() == "")
//                    {
//                        ed.getBody().innerHTML = "&nbsp;";
//                    }
                }
            };

            tinymce.init(tmeditorCfg);
        }

        return self;
    }// end of initTinyMCE

    self.save = function(evt){
        var editor = this;
        if (!editor.isDirty()){return;}
        try
        {
            var el = editor.getElement();
            var jsonData = {};
            jsonData[el.getAttribute("name")] = editor.getContent();
            var idfields = el.getAttribute("id_field").split(",");
            for (var i=0;i<idfields.length;++i)
            {
                jsonData[idfields[i]] = el.getAttribute(idfields[i]);
            }
            jsonData.jsonData = JSON.stringify(jsonData);
            $.ajax({
                url:el.getAttribute("url")
                ,data:jsonData
                ,dataType:"json"
                ,type:"post"
                ,success:function(data,status,ajax){}
            });
        }
        catch(e){
            if (self.debug)
            {
                alert(e);
            }
        }
    }

    self.showMessage=function(title,msg,callback){
        self.dialog = $("<div></div>")
        .html(msg)
        .dialog({
          modal:true
          ,title:title || "Notice(s)"
          ,position:['middle',150]
          ,buttons: [{
            text: "OK"
            ,click: function() {
              $( this ).remove();//.dialog( "close" );
              if (typeof callback == "function")
              {
                  callback.call(this);
              }
            }
          }]
        });
    };
    self.closeMessage=function(){
      try{self.dialog.dialog("close")}catch(e){}
    };
    self.getAttributes=function(el){
        var params = {},attribute;
        var unwantedAttributes = ["style","id","class","disabled","readonly","checked","src","href","url","approveUrl","idField","validationUrl"];
        for (var i=0; i<el.attributes.length;++i)
        {
            attribute=el.attributes[i];
            if (unwantedAttributes.indexOf(attribute.name.toLowerCase()) == -1)
            {
                params[attribute.name.toUpperCase()] = attribute.value;
            }
        }
        return params;
    }

    self.review=function(evt){
        try
        {
            var btn = self.reviewBtn;
            var url = btn.attr("url");
            var params = self.getAttributes(btn[0]);
            self.doReviewApprove(url,params,function(){
                self.showMessage("Success","The report is reviewed!",function(){
                    location.reload(true);
                })
            });
        }
        catch(e){
            if (location.search.indexOf("debug=true") > -1)
            {
                alert(e);
            }
        }
    }

    self.processApprove=function(url,params){
      var d = new Date();
      params.APPROVED_DATE = [d.getFullYear(),d.getMonth()+1,d.getDate()].join("-") + " " + [d.getHours(),d.getMinutes(),d.getSeconds()].join(":");
      self.doReviewApprove(url,params,function(){
          self.showMessage("Success","The report is approved!",function(){
              var approveCallbackUrl = self.approveBtn.attr("approveCallbackUrl");
              var id = self.approveBtn.attr(self.approveBtn.attr("idField"));

              if (approveCallbackUrl && id)
              {
                  $.ajax({
                      url:approveCallbackUrl
                      ,data:{id:id}
                      ,dataType:"json"
                      ,type:"post"
                      ,success:function(data,status,ajax){
                          location.reload(true);
                      }
                  });
              }
              else
              {
                  location.reload(true);
              }
          })
      });
    }
    self.approve=function(evt){
        try
        {
          self.showMessage("Processing...", "Please wait...");
          var url = self.approveBtn.attr("url");
          var validationUrl = self.approveBtn.attr("validationUrl");
          var params = self.getAttributes(self.approveBtn[0]);
          if (validationUrl)
          {
            self.doReviewApprove(validationUrl,params,function(response,status,jqxhr){
              if (response.data.approvable)
              {
                self.processApprove(url,params);
              }
              else
              {
                self.closeMessage();
                
                $("<div/>")
                .html("The next operational period must be set before the Action Plan report can be approved. Do you want to set the next operational period now?")
                .dialog({
                  title:"Error"
                  ,modal:true
                  ,autoShow:true
                  ,buttons:{
                    yes:function(){
                      window.open(window.SERVER_ROOT + "/operationperiod/edit.jsp?id=" + response.data.FC_OPERATION_PERIOD_ID,"_blank");
                      $(this).dialog("close");
                    }
                    ,no:function(){
                      $(this).dialog("close");
                    }
                  }
                });
              }
            });
          }
          else
          {
            self.processApprove(url,params);
          }
        }
        catch(e){
          self.closeMessage();
          if (location.search.indexOf("debug=true") > -1)
          {
              alert(e);
          }
        }
    }

    self.disapprove=function(evt){
        try
        {
            var btn = self.disapproveBtn;
            var url = btn.attr("url");
            var params = self.getAttributes(btn[0]);
            params.APPROVED_DATE="";
            self.doReviewApprove(url,params,function(){
                self.showMessage("Success","The report approval is reverted!",function(){
                    location.reload(true);
                })
            });
        }
        catch(e){
            if (location.search.indexOf("debug=true") > -1)
            {
                alert(e);
            }
        }
    }

    self.doReviewApprove=function(url,params,callback)
    {
        $.ajax({
            url:url
            ,data:{jsonData:JSON.stringify(params)}
            ,dataType:"json"
            ,type:"post"
            ,success:function(data,status,ajax){
                if (typeof callback == "function")
                {
                    callback.call(this,data,status,ajax);
                }
            }
        });
    }

    self.repositionHelpDiv = function()
    {
        var w = window.innerWidth
        var w2 = $(".print-area").outerWidth();//document.getElementById("content-wrapper").offsetWidth;
        var w3 = w-w2;
        var w4 = w3/2;
        var left = w2+w4 + 10;
        var div = document.getElementById("help-div");
        div.style.left = left + "px";

        return self;
    }

     self.htmlContent = function(){
//        $(".print-area").css("padding","0in");
//        $("body").css("background-color","#fff");
//        var html = "<!DOCTYPE html><html>" + document.getElementsByTagName("html")[0].innerHTML + "</html>";
//        var elem = document.getElementById("htmlStr");
//        elem.value = html;
//        $(".print-area").css("padding","0.2in 0.5in");
//        $("body").css("background-color","#ddd");
    }
    self.init();

}

function ViewerPanel(cfg)
{
    var self = this;
    self.getViewersUrl=cfg.url;
    self.resultPanelId=cfg.ctId;
    self.currentViewerId=cfg.viewerId;
    self.initialLoad=true;
    self.title="This report is also being viewed by others listed below";
    self.active=true;

    self.getParam = function(key)
    {
        if (!self.queryParams)
        {
            self.queryParams = {};
            location.search.substring(1).replace(
                    new RegExp("([^?=&]+)(=([^&]*))?", "g"),
                    function($0, $1, $2, $3) { self.queryParams[$1] = $3; }
            );
        }

        return self.queryParams[key];
    }

    self.init = function()
    {
      if (location.hostname.indexOf("localhost") == -1 
          || location.search.indexOf("usercheck=true") > -1)
      {
        self.resultPanelEl = document.getElementById(self.resultPanelId);
        self.ti = setInterval(self.fetchViewers,10000);
        self.fetchViewers();
      }
      
      $(window).on("blur focus",self.initActive)
    }
    self.initActive=function(e){
      self.active = (e.type == "focus");
    };
    self.fetchViewers=function()
    {
      if (/*location.host.indexOf("localhost") > -1 || /**/!self.active){return;}
        
        $.ajax({
            url:self.getViewersUrl
            ,data:{id:self.getParam("id")}
            ,dataType:"json"
            ,type:"post"
            ,success:function(response)
            {
                self.initViewers(response.data);
            }
        });
    };

    self.initViewers=function(data)
    {
        if (self.resultPanelEl)
        {
            var viewers = [],viewerNames=[];

            for (var i in data)
            {
                if (data.hasOwnProperty(i) && i != self.currentViewerId)
                {
                    viewers.push("<span class='user'>" + data[i].name + "</span>");
                    viewerNames.push(data[i].name);
                }
            }

            $(self.resultPanelEl).html("<div style='font-weight:bold;margin-bottom:5px;'>" + self.title + "</div>" + viewers.join(""));
            $(self.resultPanelEl).removeClass("hidden");
            if (viewers.length == 0)
            {
                $(self.resultPanelEl).addClass("hidden");
            }
            else if (self.initialLoad)
            {
                var names="";
                
                if (viewerNames.length > 2)
                {
                    names += viewerNames.splice(0, viewerNames.length-1).join (" ,") + " and " + viewerNames[viewerNames.length];
                }
                else if (viewerNames.length == 2)
                {
                    names += viewerNames.join(" and ");
                }
                else
                {
                    names += viewerNames[0];
                }
                
                $("<div>" + names + (viewerNames.length > 1 ? " are " : " is ") + " also viewing the same report. Do you want to continue to view the report?</div>")
                .dialog({
                    title:"Warning"
                    ,modal:true
                    ,buttons:{
                        yes:function(){
                            $(this).remove();
                        }
                        ,no:function(){
                            window.close();
                        }
                    }
                });
            }
            self.initialLoad=false;
        }
    }
}