/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ReservoirInfo = function(){
    var self = this;
    self.init=function(){
      self.initItems();
      self.initListeners();
      initSelectCombo();
      self.targetStorageCheck();
      self.targetStorageShowTable();
    };
    self.initItems = function(){
      self.reservoirTab = $("#reservoir_tab");
      self.reservoirTable = self.reservoirTab.find("#create-rv-table");
      self.downloadImg = self.reservoirTab.find(".downloadImg");
      self.purposeReservoir = self.reservoirTab.find(".purposeResItem");
      self.isSellerAuth = self.reservoirTab.find("input[name=isSellerAuth]");
      self.targetStorageItems();
    };
    self.targetStorageItems = function(){
      self.targetStorageTb = self.reservoirTab.find("table.targetStorageTb");
      self.attachStorageBtn = self.reservoirTab.find("input.add_storage");
      self.storageSaveBtn = self.targetStorageTb.find("img.save-icon");
      self.storageEditBtn = self.targetStorageTb.find('img.edit-icon');
      self.storageRemoveBtn = self.targetStorageTb.find("img.delete-icon");
      self.phoneNumber = self.targetStorageTb.find("input[name=phoneNumber]");
      self.emailAddy = self.targetStorageTb.find("input[name=email]");
      self.floatField = self.targetStorageTb.find("input.floatField");
      self.targetStorageFieldFormat();
    };
    self.initListeners = function(){
//      self.downloadImg.mousedown(function() {
//        $(window).unbind('beforeunload');
//      });
//      self.downloadImg.mouseleave(function() {
//        $(window).bind('beforeunload', function(){
//          return 'Are you sure you want to leave? Data you have entered may not be saved.';
//        });
//      });
      self.purposeReservoir.on("click",self.removePurposeReservoir);
      self.targetStorageListeners();
      self.initSellerAuth();
    };

    self.targetStorageCheck = function(){
      var tr = self.targetStorageTb.find('tbody tr');
      if(tr.length>1){
        $(tr[0]).addClass("hidden");
      }
    };
    self.targetStorageUnbindListeners = function(){
      self.isSellerAuth.unbind("click",self.sellerAuthLogic);
      self.attachStorageBtn.unbind('click',self.addTargetStorage);
      self.storageSaveBtn.unbind("click",self.saveTargetStorage);
      self.storageRemoveBtn.unbind("click",self.removeTargetStorage);
      self.storageEditBtn.unbind("click",self.editTargetStorage);
      self.emailAddy.unbind("blur",self.emailVerfication);
    };

    self.targetStorageListeners = function(){
      self.targetStorageUnbindListeners();

      self.isSellerAuth.on("click",self.sellerAuthLogic);
      self.attachStorageBtn.on('click',self.addTargetStorage);
      self.storageSaveBtn.on("click",self.saveTargetStorage);
      self.storageRemoveBtn.on("click",self.removeTargetStorage);
      self.storageEditBtn.on("click",self.editTargetStorage);
      self.emailAddy.on("blur",self.emailVerfication);
      self.floatField.keyup(function(){
        this.value = this.value.replace(/(?!^-)[^0-9.]/g,'');
      });
    };

    self.editTargetStorage = function(){
      var td = $(this).parent().prev();
      td.find("img").removeClass("hidden");
      var inputs = td.parent().find("input");
      $.each(inputs,function(){
        $(this).prop("disabled",false);
      });
      alert("You can now edit the textfield. Please be sure to save when done");
    };

    self.targetStorageShowTable = function(){
      var trs = self.targetStorageTb.find("tbody tr");
      self.targetStorageTb.hide();
      var input = trs.find("td:first-child input");
      $.each(input,function(){
        if($(this).val()){
          self.targetStorageTb.show();
          return;
        }
      });
    };

    self.removeTargetStorage = function(){
      var tr = $(this).closest("tr");
      var trs = self.targetStorageTb.find("tbody tr");
      var input = tr.find("td:first-child input");
      var msg = "Are you sure you want to remove this target storage?";
      if(!input.val()){
        tr.hide();
        if(trs.length > 1){
          tr.remove();
        }
        return;
      }
      self.confirmationWindow(msg,function(bool){
        if(!bool){return false;}
        self.reservoirTab.mask("Please wait...");
        $.ajax({
          url:window.SERVER_ROOT+"/proposal/removeStorage"
          ,data:{
            wtRvTarstorId:input.val()
            ,wtReservoirId:self.getReservoirId()
          }
          ,type:'POST'
          ,dataType:'json'
          ,cache:false
          ,success:function(response,status,jqhxr){
            try{
              if(!response.success){
                throw response.error || "Unable to remove target storage.";
              }
              tr.hide();
              if(trs.length > 1){
                tr.remove();
              }
              self.reservoirTab.unmask();
            }catch(e){
              self.reservoirTab.unmask();
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
      });
    };

    self.saveTargetStorage = function(){
      var img = this;
      var tr = $(img).closest("tr");
      var inputs = tr.find("input");
      var data = {};
      $.each(inputs,function(){
        data[$(this).attr("name")] = $(this).val();
      });
      data['wtReservoirId'] = self.getReservoirId();
      data['wtTransId'] = self.getwtTrans();
      if(!self.validationCheck(data)){
        return;
      }
      self.reservoirTab.mask("Saving data, please wait...");
      $.ajax({
        url:window.SERVER_ROOT+"/proposal/savestorage"
        ,data:data
        ,type:'POST'
        ,dataType:'json'
        ,cache:false
        ,scope:this
        ,success:function(response,status,jqhxr){
          try{
            if(!response.success){
              throw response.error || "Unable to save target storage.";
            }
            var data = response.data;
            var input = tr.find("td:first-child input");
            input.val(data.wtRvTarstorId);
            self.reservoirTab.unmask();
            inputs.prop("disabled",true);
            $(img).addClass("hidden");
            alert("Saved Successfully!");
          }
          catch(e){
            self.reservoirTab.unmask();
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
        ,error:function(xhr, errorType, exception){
          if(xhr.status === 403) //session ends
          {
              location = window.SERVER_ROOT;
          }
          else
          {
              alert("Saving Failed");
          }
        }
      });
    };

    self.addTargetStorage = function(){
      var firstRow = self.targetStorageTb.find("tbody tr:first-child");
      var newTr = $("<tr/>").html(firstRow.html());
      $.each(newTr.find("input"),function(){
        $(this).prop("disabled",false);
      });
      if(self.targetStorageTb.is(":visible")){
        newTr.find(".save-icon").removeClass("hidden");
        self.targetStorageTb.find("tbody tr:last-child").after(newTr);
      }else{
        self.targetStorageTb.show();
      }
      self.targetStorageItems();
      self.targetStorageListeners();
    };

    self.initSellerAuth = function(){
//      alert(self.isSellerAuth.filter(':checked').val());
      if(self.isSellerAuth.filter(':checked').val() === '0'){
        self.isSellerAuth.closest("tr").next().removeClass("hidden").show();
      }
    };

    self.sellerAuthLogic = function(){
      var authCt = $(this).closest("tr").next();
      authCt.addClass("hidden").hide();
      if($(this).val() !== '1'){
        authCt.removeClass("hidden").show();
      }
    };

    self.removePurposeReservoir = function(){
      var msg = "Are you sure you want to remove this reservoir?";
      var purRes = this;
      self.confirmationWindow(msg,function(bool){
        if(!bool){return false;}
        self.reservoirTab.mask("Removing reservoir...");
        $(purRes).parent().hide();
        $.ajax({
          url:window.SERVER_ROOT+"/proposal/removepurpose"
          ,data:{
            reservoirId:self.getReservoirId()
            ,purposeId:$(purRes).prev().val()
          }
          ,type:"POST"
          ,cache:false
          ,dataType:'json'
          ,success:function(response,status,jqxhr){
            try{
              if(!response.success){
                throw response.error || "Unable to remove reservoir.";
              }
              self.reservoirTab.unmask();
              $(purRes).parent().remove();
            }catch(e){
              self.reservoirTab.unmask();
              $(purRes).parent().show();
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
          ,error:function(xhr, errorType, exception){
            self.reservoirTab.unmask();
            $(purRes).parent().show();
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
            else
            {
                alert("Saving Failed");
            }
          }
        });
      });
    };

    self.addSelectedReservoir = function(json) {
      self.purposeResCt = $(".purposeResCt");
      var purResListCt = null;
      if (json && ((purResListCt = self.purposeResCt.find("#purResList")).length > 0)
              && (self.purposeResCt.find("input[name=purposeId][value=" + json.wtPurposeId + "]")).length === 0) {
        var purRes = $("<input type='hidden' autocomplete='off' name='purposeId' />").val(json.wtPurposeId);
        var basinKeyRemove = $('<span class="ui-icon ui-icon-circle-close purposeResItem" style="display: inline-block; vertical-align: bottom;"></span>');
        basinKeyRemove.click(self.removePurposeReservoir);
        var basinKeyButton = $("<span class='myButton' />");
        basinKeyButton.append(purRes).append(json.purpose).append(basinKeyRemove);
        purResListCt.append(basinKeyButton);
      }
      self.saveRvPurpose(self.getReservoirId(),json.wtPurposeId);
    };

    self.emailVerfication = function(){
      var val = $(this).val();
      if(!val){return;}
      if(!self.validateEmail(val)){
        alert("Invalid email, please check your email address.");
      }
    };
    self.getReservoirId = function(){
      return self.reservoirTab.find("input[name=wtReservoirId]").val();
    };
    self.getwtTrans = function(){
      return $("#wtTransId").val();
    };
    self.targetStorageFieldFormat = function(){
      self.phoneNumber.inputmask("(999) 999-9999");
    };

    self.phoneNumberFormat = function(phone)
    {
      return phone.replace(/(\d{3})(\d{3})(\d{4})/, "($1) $2-$3");
    };
    self.validateEmail=function(email)
    {
      var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return email.match(re) ? true:false;
    };
    self.validNumber = function(value){
      return $.isNumeric(value);
    };
    self.validationCheck = function(data){
      var result = true;
      if(data.email && !self.validateEmail(data.email)){
        alert("Unable to save. Invalid email address.");
        return !result;
      }
      if(data.storageLocationLat && !self.validNumber(data.storageLocationLat)){
        alert("Invalid input. Please check your Latitude.");
        return !result;
      }
      if(data.storageLocationLong && !self.validNumber(data.storageLocationLong)){
        alert("Invalid input. Please check your Longitude.");
        return !result;
      }
      if(!data.damName){
        alert("Reservoir name is required.");
        return !result;
      }
      return result;
    };
    self.confirmationWindow = function(msg,callback){
      $("<div>",{
        html:'<p>'+msg+'</p>'
        }).dialog({
          title:'Removal Confirmation'
         ,modal:true
         ,width:400
         ,height:180
         ,close:function(){
           $(this).dialog("destroy").remove();
         }
         ,buttons:[{
            text:'Yes'
            ,click:function()
            {
              $(this).dialog("destroy").remove();
              if(callback && typeof(callback)==="function")
              {
                callback(true);
              }
            }
          },{
              text:'No'
              ,click:function()
              {
                $(this).dialog("destroy").remove();
                callback(false);
              }
          }]
      });
    };
    
  self.saveRvField = function (el) {
//    alert("saveRvField");
    var reservoirId = $(el).attr("reservoirId"),
        fieldName = $(el).attr("name");
    if (!reservoirId || !fieldName) {
      throw "The reservoirId and the fieldName are required";
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savervfield'
      , data: {reservoirId: reservoirId, fieldName: fieldName, fieldValue: $(el).val()}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Reporting Reservoir Table. Please contact the site adminstrator to resolve the issue!";
          }
        }
        catch (e) {
          if (response.callback) {
            var callback = eval(response.callback);
            callback.call(self, e);
          } else {
            alert(e);
          }
        }
      }
    });
    return this;
  };  
  
  self.saveRvPurpose = function (reservoirId,purposeId) {
//    alert("saveRvPurpose");
    if (!reservoirId || !purposeId) {
      throw "The reservoirId and the purposeId are required";
    }
//    alert(reservoirId + ":" + purposeId);
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savervpurpose'
      , data: {reservoirId: reservoirId, purposeId: purposeId}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Reservoir Purpose. Please contact the site adminstrator to resolve the issue!";
          }
        }
        catch (e) {
          if (response.callback) {
            var callback = eval(response.callback);
            callback.call(self, e);
          } else {
            alert(e);
          }
        }
      }
    });
    return this;
  };

//##################Private Fnt########################
    /**
     *
     * @returns {undefined}
     */
    function initSelectCombo() {
      $("#combobox").combobox({
//        source: window.SERVER_ROOT + "/proposal/purpose"
//        ,sourceHandler: function(data) {
//          var options = [];
//          for (var i = 0; i < data.length; ++i) {
//            if ($(this).children("option[value='" + data[i].wtPurposeId + "']").length == 0){
//              options.push($("<option/>")
//                            .attr({
//                              value:data[i].wtPurposeId
//                              ,json:JSON.stringify(data[i])
//                            })
//                            .html(data[i].purpose));
//            }
//          }
//          $(this).append(options);
//        },
        create: function () {
          var select = null;
          var comboCt = null;
          var combobox = null;
          if (((select = $(this)).length > 0)
                  && ((comboCt = select.next(".custom-combobox")).length > 0)
                  && ((combobox = comboCt.children().first(".custom-combobox-input")).length > 0)) {
            combobox.attr("placeholder", "Select...");
            if (select.is("[required]")) {
              combobox.attr("required", "required");
            }
          }
        }
        , select: function (event, ui) {
          try {
            var option = null;
            var jsonStr = null;
            var json = null;
            if ((option = ui.item)
                    && (jsonStr = $(option).attr("json"))
                    && (json = JSON.parse(jsonStr))
              ) {
              self.addSelectedReservoir(json);
              $(this).val(json.purpose);
              var select = null;
              var comboCt = null;
              var combobox = null;
              if (((select = $(this)).length > 0)
                      && ((comboCt = select.next(".custom-combobox")).length > 0)
                      && ((combobox = comboCt.children().first(".custom-combobox-input")).length > 0)) {
                setTimeout(function () {
                  combobox.val("");
                }, 500);
              }
            }
          }
          catch (e) {
            alert(e);
          }
        }
      }).change(function () {
        var select = null;
        var comboCt = null;
        var combobox = null;
        var option = null;
        if (((select = $(this)).length > 0)
                && ((comboCt = select.next(".custom-combobox")).length > 0)
                && ((combobox = comboCt.children().first(".custom-combobox-input")).length > 0)
                && ((option = select.children(":selected")).length > 0)) {
          var ui = {item: {label: option.val(), option: option, value: option.val()}};
          combobox.val($(this).children(':selected').text()).trigger("autocompleteselect", ui);
        }
      });
    }
    self.init();
  };