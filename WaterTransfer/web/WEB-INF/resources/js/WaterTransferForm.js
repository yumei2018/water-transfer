/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var WaterTransferForm=function(cfg){
  var self = this;
  $.extend(self,cfg);
  self.init=function(){
    self.initItems();
    self.initEvents();
  };
  self.selectAgencyHandler=function(menuselect,item){
    $(this).find(".selected").removeClass("selected");
    $(item.item).addClass("selected");
  };
  self.initEvents=function(){
    self.appEl.find(".tabs").tabs();
    self.appEl.find("select").selectmenu();
    self.appEl.find(".accordion").accordion({heightStyle: "content"});
    self.appEl.find(".menu").menu({select:self.selectAgencyHandler});
    self.newContactBtn.on("click",function(){
      self.contactForm.dialog({modal:true});
    });
  };
  self.initItems=function(){
    self.appEl = $("#" + self.id);
    self.url="";
    self.contactForm=self.appEl.find("#contactFormDialog").hide();
    self.newContactBtn=self.appEl.find(".newContactBtn");
  };
  
  self.init();
}
