/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var WtApp=function() {
    var self = this;
    
    self.initForm=function(){
        $('div.form-top').addClass('highlight');
        self.formIP = $("#create-form.form");
        self.reset();
        self.save();
    };
    
    self.reset=function(){
        self.resetButton = self.formIP.find(".reset-button");
        
        self.resetButton.on("click", function(evt) {
            var table = $("#table_idelPlanData")[0];
//            for (var i = table.rows.length - 1; i > 0; i--) {
//              table.deleteRow(i);
                $('.fullRow').val("");
//           }
        });
    };
    
    self.save=function(){
        self.saveButton = self.formIP.find(".save-button");   
        self.saveButton.on("click", function(evt) {
            var data = self.formIP.serialize();
            var url =window.SERVER_ROOT + "/requestIdledPlanInfo/saveRecord";
            
            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,dataType:"json"
                ,data:data
                ,success:function(data,status,jqxhr){
                    alert("Idled Plan Record Submitted!");
                }
            }); 

        });
    };
    
    self.onSubmitForm=function(data,status,jqxhr){/*PLACE HOLDER/**/};
    
    self.initForm();
};