/* 
 * Document   : CropidlingInfo.js
 * Author     : ymei
 */

var CropidlingInfo = function () {
  var self = this;
  self.init = function () {
    self.initItems();
    self.initFieldsValue();
    self.initListeners();
//    self.buildCIData();
  };
  
  //<editor-fold defaultstate="collapsed" desc="Initial Items and Page">
  self.initItems = function () {
    self.commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;
    self.waterTransQuaCI_help = "This field is automatically calculated and cannot be modified.</br>";
    self.totalTransferAcr_help = "This field is automatically calculated and cannot be modified.</br>";
    self.TWMonthly_help = "This field is automatically calculated and cannot be modified.</br>";
    
    self.cropidlingTab = $("#cropidling_tab");
    self.helpCt = self.cropidlingTab.find(".help-ct");
    self.ciContainer = self.cropidlingTab.find("#ci-container");
    self.ciCreateTable = self.cropidlingTab.find("#create-ci-table");
    self.wtCropIdlingId = self.ciCreateTable.find("#wtCropIdlingId");
    self.isResReleaseCI = self.cropidlingTab.find("input[name=isResReleaseCI]");
    self.isResReleaseMsgCI = self.cropidlingTab.find("#isResReleaseMsgCI");
    self.downloadImg = self.cropidlingTab.find(".downloadImg");
    self.totProposalHelpIcon = self.cropidlingTab.find("img#totalProposedAcreage_help");
    self.waterTransQuaCI = self.cropidlingTab.find("input#waterTransQuaCI");
    self.proTransferByCI = self.cropidlingTab.find("input#proTransferByCI");
    self.proTransferByCS = self.cropidlingTab.find("input#proTransferByCS");
    self.totalTransferAcr = self.cropidlingTab.find("input#totalTransferAcr");
    self.cropTypeTable = self.cropidlingTab.find("#ci-croptype-table");
    self.cropTypeAddBtn = self.cropidlingTab.find("#crop_type_addBtn");
    self.ciMonthlyTable = self.cropidlingTab.find("#ci-monthly-table");
    self.mayEtaw = self.ciMonthlyTable.find("#mayEtaw");
    self.mayTw = self.ciMonthlyTable.find("#mayTw");
    self.juneEtaw = self.ciMonthlyTable.find("#juneEtaw");
    self.juneTw = self.ciMonthlyTable.find("#juneTw");
    self.julyEtaw = self.ciMonthlyTable.find("#julyEtaw");
    self.julyTw = self.ciMonthlyTable.find("#julyTw");
    self.augustEtaw = self.ciMonthlyTable.find("#augustEtaw");
    self.augustTw = self.ciMonthlyTable.find("#augustTw");
    self.septemberEtaw = self.ciMonthlyTable.find("#septemberEtaw");
    self.septemberTw = self.ciMonthlyTable.find("#septemberTw");
    self.totalEtaw = self.ciMonthlyTable.find("#totalEtaw");
    self.totalTw = self.ciMonthlyTable.find("#totalTw");
  };
  self.initFieldsValue = function(){
    self.cropTypeItems();
    self.calTotalEtaw();
    self.calTotalTW();
  };
  
  self.initListeners = function () {
//    self.proTransferByCI.on("keyup", self.proposalTotalCal);
//    self.proTransferByCS.on("keyup", self.proposalTotalCal);
    self.cropTypeAddBtn.unbind('click').bind("click",self.addCropType);
//      self.proTransferByCI.on("mousemove",self.proposalTotalCal);
//      self.proTransferByCS.on("mousemove",self.proposalTotalCal);
//    self.downloadImg.mousedown(function () {
//      $(window).unbind('beforeunload');
//    });

//    self.downloadImg.mouseleave(function () {
//      $(window).bind('beforeunload', function () {
//        return 'Are you sure you want to leave? Data you have entered may not be saved.';
//      });
//    });
    self.isResReleaseCI.on("click", function () {
      if (self.cropidlingTab.find("input[name=isResReleaseCI]:checked").val() === "1") {
        self.isResReleaseMsgCI.removeClass("hidden");
      } else {
        self.isResReleaseMsgCI.addClass("hidden");
      }
    });
    self.totProposalHelpIcon.on("click", self.helpIconHandler);
  };
  
  self.helpMsgPopup = function (el) {
    var msg = "Help";
    switch ($(el).attr("id")) {
      case "waterTransQuaCI_help":
        msg = self.waterTransQuaCI_help;
        break;
      case "totalTransferAcr_help":
        msg = self.totalTransferAcr_help;
        break;
      case "TWMonthly_help":
        msg = self.TWMonthly_help;
        break;
    }

    self.helpCt.html('<p>' + msg + '</p>');
    self.helpCt.dialog({
      title: 'Help Information'
      , modal: true
      , width: 400
      , height: 300
      , buttons: [{
          text: "Close"
          , click: function () {
            $(this).dialog("destroy").remove();
          }
        }]
    }).dialog("open");
  };
  
  self.helpIconHandler = function () {
    var msg = "";
    var eq = "";
    switch ($(this).attr("id")) {
      case "totalProposedAcreage_help":
        msg = "Total Proposed Acreage is an automatically calculated value that is \n\
                  calculated by the sum of the Proposed transfer Crop Idling and the Proposed Transfer Crop Shift.";
        eq = "Total Proposed Acreage = Crop Shift + Crop Idling";
        break;
    }
    self.helpTipDialog(msg, eq);
  };
  
  self.helpTipDialog = function (msg, eq) {
    var fs = "";
    if (eq && eq.length > 0) {
      var legend = $("<legend/>").html("Equation:");
      fs = $("<fieldset/>").css({marginTop: '10px'}).append(legend).append($("<p/>").html(eq));
    }
    var helpCt = $("<div/>");
    helpCt.html('<p>' + msg + '</p>').append(fs);
    helpCt.dialog({
      title: 'Help Information'
      , modal: true
      , width: 400
      , height: 300
      , buttons: [{
          text: "Close"
          , click: function () {
            $(this).dialog("destroy").remove();
          }
        }]
    }).dialog("open");
  };
  
  self.proposalTotalCal = function () {
    var proTransferByCI = self.proTransferByCI.val().replace(/[, ]+/g, "");
    var proTransferByCS = self.proTransferByCS.val().replace(/[, ]+/g, "");
//    alert(proTransferByCI);
    var totalTransferAcr = 0;
    if (proTransferByCI !== '') {
      totalTransferAcr += parseFloat(proTransferByCI);
    }
    if (proTransferByCS !== '') {
      totalTransferAcr += parseFloat(proTransferByCS);
    }
    if (totalTransferAcr !== '' && totalTransferAcr !== 0) {
//        totalTransferAcr = Math.round(totalTransferAcr);
      $('#totalTransferAcr').val(totalTransferAcr.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    } else {
      $('#totalTransferAcr').val('');
    }
  };
  // </editor-fold>
  
  // <editor-fold desc="CI Type of Crop Code">
  self.addCropType = function () {
    self.hiddenRow = self.cropTypeTable.find("tbody tr.hidden");
    var row = $("<tr/>").html(self.hiddenRow.html());
    self.cropTypeTable.find("tbody tr:last").after(row);
    self.cropTypeItems();
  };
  
  self.cropTypeItems = function () {
    self.proTransferByCi = self.cropTypeTable.find('input[name=proTransferByCi]');
    self.proTransferByCs = self.cropTypeTable.find('input[name=proTransferByCs]');
    self.proTransfer = self.cropTypeTable.find('input[name=proTransferTotal]');
    self.cropEtaw = self.cropTypeTable.find('input[name=cropEtaw]');
    self.waterTransferAmount = self.cropTypeTable.find('input[name=waterTransferAmount]');
    self.ctSaveBtn = self.cropTypeTable.find("img.save-icon");
    self.ctEditBtn = self.cropTypeTable.find("img.edit-icon");
    self.ctRemoveBtn = self.cropTypeTable.find("img.delete-icon");
    self.cropTypeListeners();
    self.numberHandler();
  };
  
  self.cropTypeListeners = function () {
    self.ctSaveBtn.unbind('click').bind("click",self.saveCropType);
    self.ctEditBtn.unbind('click').bind("click",self.editCropType);
    self.ctRemoveBtn.unbind('click').bind("click",self.removeCropType);
    // Change keyup to blur to fix IE issue--2017.12.26
    self.proTransfer.unbind('blur').on("blur", self.calCroptype);
    self.cropEtaw.unbind('blur').on("blur", self.calCroptype);
//    self.proTransferByCi.on("keyup", self.calCroptype);
//    self.proTransferByCs.on("keyup", self.calCroptype); 
  };
  
  self.selectCroptype = function (el){
//    self.cropType = $(el).find('option:selected');
    var etaw = 0;
    self.row = $(el).parent().parent();
    self.cropWithoutSelect = self.row.find('select[name=cropWithoutTransfer] option:selected');
    self.cropWithSelect = self.row.find('select[name=cropWithTransfer] option:selected');
    var cropWithoutEtaw = self.cropWithoutSelect.attr("etaw");
    var cropWithEtaw = self.cropWithSelect.attr("etaw");
//    alert(cropWithoutEtaw +":"+ cropWithEtaw);
    etaw = parseFloat(cropWithoutEtaw) - parseFloat(cropWithEtaw);
//    alert(etaw);
    if (etaw < 0){
      etaw = 0;
    }
    self.cropEtaw = self.row.find('input[name=cropEtaw]');
    self.cropEtaw.val(etaw.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    
    // If there is value in Proposed Transfer Acreage, then need calculate Water Transfer Amount
    self.proTransfer = self.row.find('input[name=proTransferTotal]');
    self.waterTransferAmount = self.row.find('input[name=waterTransferAmount]');    
    if(self.proTransfer.val() !== ""){
      var proTransfer = parseFloat(self.proTransfer.val().replace(/[, ]+/g, ""));
      var waterTransferAmount = proTransfer*etaw;
      self.waterTransferAmount.val(waterTransferAmount.toLocaleString('en-US', {maximumFractionDigits: 2}));
    }
//    self.calCroptype();
//    alert(self.cropType.val());
//    alert(self.cropType.attr("etaw"));
  };
  
  self.calCroptype = function (){
//    alert("self.calCroptype");
//    var proTransferByCi = 0;
//    var proTransferByCs = 0;
    var proTransfer = 0;
    var cropEtaw = 0;
    var waterTransferAmount = 0;
    self.row = $(this).parent().parent();
//    alert(self.row.find('input[name=proTransferByCi]').val());
//    self.theProTransferByCi = self.row.find('input[name=proTransferByCi]');
//    self.theProTransferByCs = self.row.find('input[name=proTransferByCs]');
    self.theProTransfer = self.row.find('input[name=proTransferTotal]');
    self.theCropEtaw = self.row.find('input[name=cropEtaw]');
    self.theWaterTransferAmount = self.row.find('input[name=waterTransferAmount]');
    
//    if(self.theProTransferByCi.val() !== ""){
//      proTransferByCi = parseFloat(self.theProTransferByCi.val().replace(/[, ]+/g, ""));
//      proTransferTotal += proTransferByCi;
//    }
//    if(self.theProTransferByCs.val() !== ""){
//      proTransferByCs = parseFloat(self.theProTransferByCs.val().replace(/[, ]+/g, ""));
//      proTransferTotal += proTransferByCs;
//    }
    if(self.theProTransfer.val() !== "" && self.theCropEtaw.val()!==""){
//      alert(self.theCropEtaw.val());
      proTransfer = parseFloat(self.theProTransfer.val().replace(/[, ]+/g, ""));
      cropEtaw = parseFloat(self.theCropEtaw.val().replace(/[, ]+/g, ""));
      waterTransferAmount = proTransfer*cropEtaw;
      self.theWaterTransferAmount.val(waterTransferAmount.toLocaleString('en-US', {maximumFractionDigits: 2}));
    }
    
    if(proTransfer > 0){
      self.theProTransfer.val(proTransfer.toLocaleString('en-US', {maximumFractionDigits: 2}));
    }
//    if(waterTransferAmount > 0){
//      self.theWaterTransferAmount.val(waterTransferAmount.toLocaleString('en-US', {maximumFractionDigits: 2}));
//    }    
  };
  
  self.calWTAmountTotal = function (){
    var WTAmountTotal = 0;
    self.waterTransferAmount.each(function () {
      if (this.value !== "") {
        var WTAmount = parseFloat(this.value.replace(/[, ]+/g, ""));
        WTAmountTotal += WTAmount;
//        alert(WTAmountTotal+":"+WTAmount);
        self.waterTransQuaCI.val(WTAmountTotal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
      }
    });
  };
  
  self.calProTransferTotal = function (){
    var proTransferTotal = 0;
    self.proTransfer.each(function () {
      if (this.value !== "") {
        var ProTransfer = parseFloat(this.value.replace(/[, ]+/g, ""));
        proTransferTotal += ProTransfer;
        self.totalTransferAcr.val(proTransferTotal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
      }
    });
  };
  
  self.saveCropType = function (){
    var wtCropIdlingId = self.wtCropIdlingId.val();
    if (wtCropIdlingId ==="" || $(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.select = self.row.find('select');
    self.cropWithoutSelect = self.row.find('select[name=cropWithoutTransfer] option:selected');
    self.cropWithSelect = self.row.find('select[name=cropWithTransfer] option:selected');
    var cropWithoutTransfer = self.cropWithoutSelect.val();
    var cropWithTransfer = self.cropWithSelect.val();
//    self.cropWithoutTransfer = self.row.find('.crop_without_transfer');
//    self.cropWithTransfer = self.row.find('.crop_with_transfer');    

    var icon = this;
//    self.proTransferByCi = self.row.find('input[name=proTransferByCi]');
//    self.proTransferByCs = self.row.find('input[name=proTransferByCs]');
    self.proTransferTotal = self.row.find('input[name=proTransferTotal]');
    self.cropEtaw = self.row.find('input[name=cropEtaw]');
    self.waterTransferAmount = self.row.find('input[name=waterTransferAmount]');
    self.wtCiCroptypeId = self.row.find("input[name=wtCiCroptypeId]");
    
//    var proTransferByCi = self.proTransferByCi.val().replace(/[, ]+/g, ""); 
//    var proTransferByCs = self.proTransferByCs.val().replace(/[, ]+/g, "");
    var proTransferTotal = self.proTransferTotal.val().replace(/[, ]+/g, "");
    var waterTransferAmount = self.waterTransferAmount.val().replace(/[, ]+/g, "");
    
//    alert(self.proTransferByCi.val());
    var data = {wtCropIdlingId:wtCropIdlingId, wtCiCroptypeId:self.wtCiCroptypeId.val(), 
                 cropWithoutTransfer:cropWithoutTransfer, cropWithTransfer:cropWithTransfer,
                  proTransferTotal:proTransferTotal, cropEtaw:self.cropEtaw.val(),
                   waterTransferAmount:waterTransferAmount};
//    alert(self.wtCiCroptypeId.val());
    self.cropidlingTab.mask("Saving Type of Crop..."); 
    $.ajax({
      url: window["SERVER_ROOT"] + "/proposal/savecicroptype"
      , data: data
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr)
      {
        try {
          if (!data.success) {
            throw data.error || "Unable to save Type of Crop.";
          }
          self.select.attr("disabled", true);
          self.cropidlingTab.unmask();
//          self.proTransferByCi.attr("disabled", true);
//          self.proTransferByCs.attr("disabled", true);
          self.proTransferTotal.attr("disabled", true);
          self.cropEtaw.attr("disabled", true);
          self.waterTransferAmount.attr("disabled", true);
          self.wtCiCroptypeId.val(data.wtCiCroptypeId);
          $(icon).hide();
          alert("Saved Successfully!");   
          self.cropTypeItems();
//          self.calWTAmountTotal();
//          self.calProTransferTotal();
//          self.resetCiMonthly();
//          self.calBaseProTransAmount();
        } catch (e) {
          self.cropidlingTab.unmask();
          if (data.callback) {
            var callback = eval(data.callback);
            if (typeof callback === "function") {
              callback.call();
            }
          } else if (e) {
            alert(e);
          }
        }
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
        else
        {
          self.cropidlingTab.unmask();
          alert("Saving Failed");
        }
      }
    });
  }; 
  
  self.editCropType = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.cropTypeItems();
    self.row = $(this).parent().parent();
//    self.proTransferByCi = self.row.find('input[name=proTransferByCi]');
//    self.proTransferByCs = self.row.find('input[name=proTransferByCs]');
    self.proTransferTotal = self.row.find('input[name=proTransferTotal]');
    self.cropEtaw = self.row.find('input[name=cropEtaw]');
    self.waterTransferAmount = self.row.find('input[name=waterTransferAmount]');
    self.saveBt = self.row.find(".save-icon");
    
//    self.proTransferByCi.attr("disabled", false);
//    self.proTransferByCs.attr("disabled", false);
    self.proTransferTotal.attr("disabled", false);
    self.cropEtaw.attr("disabled", false);
    self.waterTransferAmount.attr("disabled", false);
    self.saveBt.removeClass("hidden").show();
  };
  
  self.removeCropType = function () {
//    if ($(this).attr("disabled") === "disabled")
//    {
//      return false;
//    }
    self.row = $(this).parent().parent();
    self.wtCiCroptypeId = self.row.find("input[name=wtCiCroptypeId]");
    if (self.wtCiCroptypeId.val() === "")
    {
      self.row.remove();
      return false;
    }
    self.msg = "Are you sure you want to remove this Crop Type?";
    self.confirmMsg(self.msg, function (bool) {
      if (bool) {
        $.ajax({
          url: window["SERVER_ROOT"] + "/proposal/removecicroptype"
          , data: {wtCiCroptypeId:self.wtCiCroptypeId.val()}
          , cache: false
          , dataType: 'json'
          , success: function (data, status, jqxhr)
          {
            try {
              if (!data.success) {
                throw data.error || "Unable to remove water right.";
              }
              self.row.remove();
              alert("Removed Successfully!!!");
              self.cropTypeItems();
//              self.calWTAmountTotal();
//              self.calProTransferTotal();
//              self.resetCiMonthly();
//              self.calBaseProTransAmount();
            } catch (e) {
              if (data.callback) {
                var callback = eval(data.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              } else if (e) {
                alert(e);
              }
            }
          }
          , error: function (xhr, errorType, exception) {
            if (xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
            }
            else {
              alert("Failed to remove");
            }
          }
        });
      }
    });    
  };
  // </editor-fold>

  // <editor-fold desc="CI Monthly Code">
  self.saveCIField = function (el) {
    var cropIdlingId = $(el).attr("cropIdlingId"),
        fieldName = $(el).attr("name");
    if (!cropIdlingId || !fieldName) {
      throw "The cropIdlingId and the fieldName are required";
    }
    var totalTransferAcr = self.totalTransferAcr.val().replace(/[, ]+/g, ""); 
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savecifield'
      , data: {cropIdlingId: cropIdlingId, fieldName: fieldName, fieldValue: $(el).val(), totalTransferAcr:totalTransferAcr}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Propodal Process. Please contact the site adminstrator to resolve the issue!";
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
  
  self.saveCiMonthly = function (el) {
    self.calTotalEtaw();
    var cropIdlingId = $(el).attr("cropIdlingId"),
        fieldName = $(el).attr("name");
    if (!cropIdlingId || !fieldName) {
      throw "The cropIdlingId and the fieldName are required";
    } 
    var fieldValue = $(el).val();
//    if (self.waterTransQuaCI.val() === ""){
//      alert("There is no Water Transfer Amount to Calculate");
//    }
//    if(self.waterTransQuaCI.val() !== ""){
      var twTotalVal = self.waterTransQuaCI.val().replace(/[, ]+/g, "");      
//      var twVal = twTotalVal * parseFloat(fieldValue / 100);
//      twVal = twVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
//      switch (fieldName){
//        case "mayEtaw":
//          self.mayTw.val(twVal);
//          break;
//        case "juneEtaw":
//          self.juneTw.val(twVal);
//          break;
//        case "julyEtaw":
//          self.julyTw.val(twVal);
//          break;
//        case "augustEtaw":
//          self.augustTw.val(twVal);
//          break;
//        case "septemberEtaw":
//          self.septemberTw.val(twVal);
//          break;
//      }      
//    }
    self.calTotalTW();
    
//    alert(cropIdlingId + ":" + twTotalVal + ":" + fieldName + ":" + fieldValue);
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savecimonthly'
      , data: {cropIdlingId: cropIdlingId, twTotalVal:twTotalVal, fieldName: fieldName, fieldValue: fieldValue}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Crop Idling Monthly Data. Please contact the site adminstrator to resolve the issue!";
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
  
  self.ciWtAmountChange = function (el) {   
//    self.mayTw.val("");
//    self.juneTw.val("");
//    self.julyTw.val("");
//    self.augustTw.val("");
//    self.septemberTw.val("");
//    self.totalTw.html("");
//    self.totalTw.css("background-color","white");
//    self.resetCiMonthly(el);
//    self.calTotalTW();
    self.saveCIField(el);
  };
  
  self.resetCiMonthly = function(){
    var twTotalVal = self.waterTransQuaCI.val().replace(/[, ]+/g, "");
//    alert(twTotalVal);
//    self.mayEtaw.val("15");
//    self.juneEtaw.val("22");
//    self.julyEtaw.val("24");
//    self.augustEtaw.val("24");
//    self.septemberEtaw.val("15");
    
    var mayEtawVal = parseInt(self.mayEtaw.val());
    var juneEtawVal = parseInt(self.juneEtaw.val());
    var julyEtawVal = parseInt(self.julyEtaw.val());
    var augustEtawVal = parseInt(self.augustEtaw.val());
    var septemberEtawVal = parseInt(self.septemberEtaw.val());
//    alert(septemberEtawVal);
    
    var mayTwVal = twTotalVal * parseFloat(mayEtawVal / 100);
    var juneTwVal = twTotalVal * parseFloat(juneEtawVal / 100);
    var julyTwVal = twTotalVal * parseFloat(julyEtawVal / 100);
    var augustTwVal = twTotalVal * parseFloat(augustEtawVal / 100);
    var septemberTwVal = twTotalVal * parseFloat(septemberEtawVal / 100);
//    alert(septemberTwVal);
    
    self.mayTw.val(mayTwVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    self.juneTw.val(juneTwVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    self.julyTw.val(julyTwVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    self.augustTw.val(augustTwVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    self.septemberTw.val(septemberTwVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    
    self.calTotalEtaw();
    self.calTotalTW();
  };
    
  self.calTotalEtaw = function(){
    var mayEtawVal = parseInt(self.mayEtaw.val());
    var juneEtawVal = parseInt(self.juneEtaw.val());
    var julyEtawVal = parseInt(self.julyEtaw.val());
    var augustEtawVal = parseInt(self.augustEtaw.val());
    var septemberEtawVal = parseInt(self.septemberEtaw.val());
    var totalEtawVal = 0;
    totalEtawVal = mayEtawVal+juneEtawVal+julyEtawVal+augustEtawVal+septemberEtawVal;
    if (totalEtawVal > 0){
      self.totalEtaw.html(totalEtawVal);
    }
    if (totalEtawVal === 100){
      self.totalEtaw.css("background-color","white");
    } else {
      self.totalEtaw.css("background-color","red");
    }
  };
  
  self.calTotalTW = function(){
//    alert("calTotalTW");
    var mayTwVal = 0;
    var juneTwVal = 0;
    var julyTwVal = 0;
    var augustTwVal = 0;
    var septemberTwVal = 0;
    var totalTwCal = 0;
    
    if(self.mayTw.val() !== ""){
      mayTwVal = parseFloat(self.mayTw.val().replace(/[, ]+/g, ""));
      totalTwCal +=  mayTwVal;
    }
    if(self.juneTw.val() !== ""){
      juneTwVal = parseFloat(self.juneTw.val().replace(/[, ]+/g, ""));
      totalTwCal +=  juneTwVal;
    }
    if(self.julyTw.val() !== ""){
      julyTwVal = parseFloat(self.julyTw.val().replace(/[, ]+/g, ""));
      totalTwCal +=  julyTwVal;
    }
    if(self.augustTw.val() !== ""){
//      alert(self.augustTw.val());
      augustTwVal = parseFloat(self.augustTw.val().replace(/[, ]+/g, ""));
      totalTwCal +=  augustTwVal;
    }
    if(self.septemberTw.val() !== ""){
      septemberTwVal = parseFloat(self.septemberTw.val().replace(/[, ]+/g, ""));
      totalTwCal +=  septemberTwVal;
    }
//    totalTwCal = mayTwVal + juneTwVal + julyTwVal + augustTwVal + septemberTwVal;
//    if (totalTwCal > 0){
      self.totalTw.html(totalTwCal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
    
    // Control field background color
    totalTwCal = totalTwCal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
    var wtAmountVal = parseFloat(self.waterTransQuaCI.val().replace(/[, ]+/g, ""));
    wtAmountVal = wtAmountVal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
//    alert(totalTwCal+":"+wtAmountVal);    
    if (totalTwCal === wtAmountVal){
      self.totalTw.css("background-color","white");
    } else {
      self.totalTw.css("background-color","red");
    }
  };
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Gets and Util Functions">
  self.numberHandler = function()
  {
    self.numField = $(".numField");
    self.intField = $(".intField");

    self.intField.keyup(function () {
      this.value = this.value.replace(/[^0-9]+/g, '');
    });
    
    // Fixed bug for IE, the old is replace(/[^0-9\.]/g, '')
    self.numField.keyup(function () {
      this.value = this.value.replace(/[^0-9\.]+/g, '');
    });

    // Fixed bug for IE, use num.toLocaleString().split instead
    self.intField.each(function () {
      if (this.value !== "") {
        var num = parseInt(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString().split('.')[0];
      }
    });
    self.intField.on("blur", function () {
      if (this.value !== "") {
        var num = parseInt(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString().split('.')[0];
      }
    });

    self.numField.each(function () {
      if (this.value !== "") {
        var num = parseFloat(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString('en-US', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        });
      }
    });

    self.numField.on("blur",function(){
      if (this.value !== "") {
        var num = parseFloat(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString('en-US', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        });
      }
    });
  };
  
  self.confirmMsg = function (msg, callback)
  {
    $("<div>", {
      html: '<p>' + msg + '</p>'
    }).dialog({
      title: 'Confirmation Message'
      , modal: true
      , width: 420
      , height: 200
      , buttons: [{
          text: 'Yes'
          , click: function ()
          {
            $(this).dialog("destroy").remove();
            if (callback && typeof (callback) === "function")
            {
              callback(true);
            }
          }
        }, {
          text: 'No'
          , click: function ()
          {
            callback(false);
            $(this).dialog("destroy").remove();
          }
        }]
    }).dialog("open");
  };
  
  self.buildCIData = function() {
    var data = new Object();
    var cropIdlingId = self.wtCropIdlingId.val();
    if (cropIdlingId === null || cropIdlingId === "") {
      return;
    }
    data["wtCropIdlingId"] = cropIdlingId;
    //Get all input fields
    self.ciCreateTable.find("input").each(function (val, key) {
      data[$(this).attr("name")] = $(this).val();
    });
    self.ciMonthlyTable.find("input").each(function (val, key) {
      data[$(this).attr("name")] = $(this).val();
    });
//    console.log(data);
    
    return data;
  };
  
  self.calBaseProTransAmount = function () {  
    self.baseinfoTab = $("#baseinfo_tab");
    self.groundwaterTab = $("#groundwater_tab");
    self.reservoirTab = $("#reservoir_tab");
    self.proTransQua = self.baseinfoTab.find("#proTransQua");    
    self.netTransWater = self.groundwaterTab.find("#netTransWater");
    self.waterTransQuaRV = self.reservoirTab.find("#waterTransQuaRV");    
    var waterTransQuaCI = self.waterTransQuaCI.val().replace(/,/g, '');
    var totalTransQua = 0;
    
    if (waterTransQuaCI !== '') {
      totalTransQua += parseFloat(waterTransQuaCI);
    }

    if (typeof self.netTransWater.val() !== 'undefined' && self.netTransWater.val() !== '') {
      totalTransQua += parseFloat(self.netTransWater.val().replace(/,/g, ''));
    }

    if (typeof self.waterTransQuaRV.val() !== 'undefined' && self.waterTransQuaRV.val() !== '') {
      totalTransQua += parseFloat(self.waterTransQuaRV.val().replace(/,/g, ''));
    }
//    alert(totalTransQua);
    self.proTransQua.val(totalTransQua.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2}));
  };
  // </editor-fold>
  
  self.init();
};
