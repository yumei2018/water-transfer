/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var ApplicationLauncher=function(cfg){
  var self = $.extend(this,cfg || {});
  self.javascripts=self.javascripts || [];
  self.stylesheets=self.stylesheets || [];
  self.mask=$("<div>Loading...</div>").dialog({dialogClass:"mask",modal:true}).dialog("close");
  self.showMask=function(){self.mask.dialog("open");return self;};
  self.hideMask=function(){self.mask.dialog("close");return self;};
  self.launch=function(){return self.showMask().loadCss().loadJs();};
  self._callback=function(){self.callback();return self.hideMask();};
  self.callback=self.callback || function(){};
  self.callbackDelay=1;
  self.loadJs=function(){
    var manualLaunch=true,last=self.javascripts.length-1;
    for (var i=0;i<self.javascripts.length;++i){
      if ($("script[src*='" + self.javascripts[i] + "']").length == 0){
        var el = document.createElement("script");
        el.type="text/javascript";
        el.src=self.javascripts[i];
        if (el.src.indexOf("?") == -1){
          el.src += "?";
        }
        el.src += "_dc=" + Math.random();
        if (i==last){
          // assign the callback after the last js is loaded
          el.onload=function(){
            setTimeout(function(){
              self._callback();
            },self.callbackDelay);
          }
        }
        document.body.appendChild(el);
        manualLaunch=false;
      }
    }
    if (manualLaunch){
      setTimeout(function(){
        self._callback();
      },self.callbackDelay);
    }
    return self;
  };//end of loadJs
  self.loadCss=function(){
    for (var i=0;i<self.stylesheets.length;++i){
      if ($("link[href*='" + self.stylesheets[i] + "']").length == 0){
        var el = document.createElement("link");
        el.type="text/css";
        el.rel="stylesheet";
        el.href=self.stylesheets[i];
        document.body.appendChild(el);
      }
    }
    return self;
  };//end of loadCss
}

