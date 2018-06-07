var BaseInfo = function () {
  var self = this;
  self.init = function () {
    self.initItems();
    self.initFields();
    self.initListeners();
    self.helpTipsMsg();
    self.initSelectCombo();
  };
  
  //<editor-fold defaultstate="collapsed" desc="Initial Items and Page">
  self.helpTipsMsg = function () {
    self.commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;
//    self.proTransQua_help = "This is the total water transfer quantity. This value can be entered, or can be calculated using the calculator button to sum the water transfer quantity from other tabs.";
//    self.proTransQua_help = "For groundwater substitution project, this is the total amount of transfer water after stream flow depletion.";    
//    self.proUnitCost_help = "For groundwater substitution project, this is the cost of water per acre-feet after stream flow depletion.";
//    self.proposedAgreement_help = "This is an range of proposed agreement amount to be paid to seller.";
//    self.proposedAgreement_help = "This is the number of proposed agreement amount to be paid to seller.";
//    self.SWRCBDate_help = "Attach SWRCB Order in attachments checklist";
//    self.CEQANumber_help = "Attach CEQA Documents in attachments checklist";

    self.proposalDescription_help = "If more space is needed, please upload your complete proposal description as an attachment.";
    self.county_help = "Select more than one if applicable";
//    self.proTransQua_help = "This field is automatically calculated and cannot be modified.</br></br>";
    self.proTransQua_help = "Crop Idling and Crop Shifting Water Transfer Amount + Reservoirs Water Transfer Amount + Groundwater Net Transfer Water";
    self.surWaterSource_help = "Enter the name of the river or stream that supplies the water being transfered.";
    
    self.fuType_other_help = "Conveyance</br>";
    self.fuType_other_help += "&emsp;&emsp;Byron-Bethany Irrigation District</br>";
    self.fuType_other_help += "&emsp;&emsp;Banta-Carbona Irrigation District</br>";
    self.fuType_other_help += "&emsp;&emsp;Contra Costa Water District</br>";
    self.fuType_other_help += "&emsp;&emsp;East Bay Municipal Utility District</br>";
  };

  self.initItems = function () {
    self.baseinfoTab = $("#baseinfo_tab");
    self.helpIcon = self.baseinfoTab.find(".help-icon");
    self.helpCt = self.baseinfoTab.find(".help-ct");
    self.description = self.baseinfoTab.find("#transDescription");
    self.checkboxCt = self.baseinfoTab.find(".checkbox_ct");
    self.faCheckbox = self.checkboxCt.find('input[type=checkbox]');
    self.hasPreTrans = self.baseinfoTab.find("input[name=hasPreTrans]");
    self.ptAddBtn = self.baseinfoTab.find("#pre_transfer_addBtn");
    self.ptTable = self.baseinfoTab.find(".pre_transfer_table");
    self.stateContInd = self.baseinfoTab.find("input[name=isStateContractor]");
    self.transYear = self.baseinfoTab.find("input[name=transYear]");
    self.deltaTransferInd = self.baseinfoTab.find("input[name=deltaTransferInd]");
    self.exportWinFrom = self.baseinfoTab.find("#reqExpFrom");
    self.exportWinTo = self.baseinfoTab.find("#reqExpTo");
    self.reqExpFromDate = self.baseinfoTab.find("#reqExpFromDate");
    self.reqExpToDate = self.baseinfoTab.find("#reqExpToDate");
    self.envCheckbox = self.baseinfoTab.find(".envRegCompl");
    self.reachCheckbox = self.baseinfoTab.find(".reach");
    self.calBtnPTQ = self.baseinfoTab.find(".calBtn");
    self.proTransQua = self.baseinfoTab.find("#proTransQua");
    self.unitCostField = self.baseinfoTab.find("input[name=proUnitCost]");
    self.agreePaid = self.baseinfoTab.find("input[name=proAgreePaid]");
    self.cropidlingTab = $("#cropidling_tab");
    self.waterTransQuaCI = self.cropidlingTab.find("#waterTransQuaCI");
    self.reservoirTab = $("#reservoir_tab");
    self.waterTransQuaRV = self.reservoirTab.find("#waterTransQuaRV");
    self.groundwaterTab = $("#groundwater_tab");
    self.totalPumping = self.groundwaterTab.find("#totalPumping");
    self.basePumping = self.groundwaterTab.find("#basePumping");
    self.depletionFactor = self.groundwaterTab.find("#depletionFactor");
    self.netTransWater = self.groundwaterTab.find("#netTransWater");
    self.grossTransPumping = self.groundwaterTab.find("#grossTransPumping");
    self.transferStartDate = self.baseinfoTab.find("input[name=transWinStart]");
    self.transferEndDate = self.baseinfoTab.find("input[name=transWinEnd]");
    self.combobox = self.baseinfoTab.find("#countycombo");
    self.countyKeyRemove = self.baseinfoTab.find(".countyItem");
    self.wtcomm = self.baseinfoTab.find("#wtComm");
//      var statusName = self.baseinfoTab.find("input[name=statusName]").val();
  };

  self.initFields = function () {
    self.description.simplyCountable({
      counter: '#counter',
      countable: 'characters',
      maxCount: 2048,
      strictMax: false,
      countDirection: 'down',
      safeClass: 'safe',
      overClass: 'over',
      thousandSeparator: ','
    });
    self.wtcomm.simplyCountable({
      counter: '#comm_counter',
      countable: 'characters',
      maxCount: 2048,
      strictMax: false,
      countDirection: 'down',
      safeClass: 'safe',
      overClass: 'over',
      thousandSeparator: ','
    });
    self.transferStartDate.datepicker({
      onSelect: function (selected) {
        self.transferEndDate.datepicker("option", "minDate", selected);
        self.saveBIField(this);
      }
    });
    self.transferEndDate.datepicker({
      onSelect: function (selected) {
        self.transferStartDate.datepicker("option", "maxDate", selected);
        self.saveBIField(this);
      }
    });
    
    // Set default Requested Export Window Date
    var currentDate = new Date();
    var currentYear = currentDate.getFullYear();
    var defaultReqExpFromDate = "07/01/" + currentYear;
    var defaultReqExpToDate = "09/30/" + currentYear;
//    alert(defaultReqExpFromDate);
    if(self.reqExpFromDate.val()===''){
      self.reqExpFromDate.datepicker()
                         .datepicker("setDate", defaultReqExpFromDate);
    }
    if (self.reqExpToDate.val()===''){
      self.reqExpToDate.datepicker()
                       .datepicker("setDate", defaultReqExpToDate);
    }

    if ($("input[name=hasPreTrans]:checked").val() === "1") {
      $(".preTransfer").removeClass('hidden').show();
    } else {
      $(".preTransfer").removeClass('hidden').hide();
    }

    self.envCheckbox.each(function () {
      $(this).val("off");
      if (this.checked)
      {
        $(this).val("on");
        if ($(this).attr("name") === "SWRCB") {
          self.baseinfoTab.find(".swrcb").removeClass("hidden").show();
        }
        if ($(this).attr("name") === "CEQA") {
          self.baseinfoTab.find(".ceqa").removeClass("hidden").show();
        }
        if ($(this).attr("name") === "NEPA") {
          self.baseinfoTab.find(".nepa").removeClass("hidden").show();
        }
      }
    });

    self.reachCheckbox.each(function () {
      $(this).val("off");
      if (this.checked)
      {
        $(this).val("on");
      }
    });

    self.initfacilitesCheckbox();    
  };

  self.initfacilitesCheckbox = function () {
    self.checkboxCt.find("input.wtFuTypeId").each(function () {
      $(this).val("off");
      if (this.checked)
      {
        $(this).val("on");
      }
    });

    if ($("input[name=isStateContractor]:checked").val() === "0") {
      if ($("input[name=Banks]").val() === "on") {
        $(".stateContractor").removeClass('hidden').show();
      } else {
        $(".CVP").removeClass('hidden').show();
      }
    }
  };

  self.initListeners = function () {
    self.helpIcon.unbind('click').bind("click", self.helpMsgPopup);
    self.countyKeyRemove.click(self.removeCounty);
//    self.unitCostField.on("blur",self.calProAgreePaidAmt);
//    self.proTransQua.on("blur",self.calProAgreePaidAmt);
//    self.calBtnPTQ.unbind('click').bind("click", self.calProTransferQua);
//    self.waterTransQuaCI.on("blur", self.calProTransferQua);
//    self.waterTransQuaCI.on("change", self.calProTransferQua);
//    self.waterTransQuaRV.on("blur", self.calProTransferQua);
//    self.grossTransPumping.on("change", self.calProTransferQua);
//    self.totalPumping.on("blur", self.calProTransferQua);
//    self.basePumping.on("blur", self.calProTransferQua);
//    self.depletionFactor.on("blur", self.calProTransferQua);
    self.hasPreTrans.unbind('click').bind("click", self.preTransferInfo);
    self.stateContInd.unbind('click').bind("click", self.clearFacilities);
    self.deltaTransferInd.unbind('click').bind("click", self.checkDeltaTransferHandler);
    self.reachCheckbox.unbind('click').bind("click", self.setReachValue);
    self.envCheckbox.unbind('click').bind("click", self.envRegComp);
    self.faCheckbox.unbind('click').bind("click", self.facilitesCheckboxHandler);
    self.exportWinFrom.on("change", self.exportFromMsgHandler);
    self.exportWinTo.on("change", self.exportToMsgHandler);
  };

  self.helpMsgPopup = function () {
    var msg = "Help";
    switch ($(this).attr("id")) {
      case "proTransQua_help":
        msg = self.proTransQua_help;
        break;
      case "proposalDescription_help":
        msg = self.proposalDescription_help;
        break;
      case "county_help":
        msg = self.county_help;
        break;
      case "proposedAgreement_help":
        msg = self.proposedAgreement_help;
        break;
      case "surWaterSource_help":
        msg = self.surWaterSource_help;
        break;
      case "fuType_other_help":
        msg = self.fuType_other_help;
        break;
      case "SWRCBDate_help":
        msg = self.SWRCBDate_help;
        break;
      case "CEQANumber_help":
        msg = self.CEQANumber_help;
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
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Facilities Requested Functions">
  self.clearFacilities = function () {     
    self.baseinfoTab.find("input.wtFuTypeId").each(function () {
      $(this).prop("checked", false);
      $(this).val("off");
    });
    self.baseinfoTab.find("input.reach").each(function () {
      $(this).prop("checked", false);
      $(this).val("off");
    });
    self.baseinfoTab.find("input.ftField").each(function () {
      $(this).val("");
    });
    self.baseinfoTab.find(".stateContractor").removeClass('hidden').hide();
    self.baseinfoTab.find(".subSWP").removeClass('hidden').hide();
    self.baseinfoTab.find(".subCVP").removeClass('hidden').hide();

    if ($(this).val() === "0") {
      $(".CVP").removeClass('hidden').show();
    } else if ($(this).val() === "1") {
      $(".CVP").removeClass('hidden').hide();
    }
  };
  
  self.facilitesCheckboxHandler = function () {
    if (this.checked)
    {
      $(this).val("on");
      if ($(this).attr("name") === "SWP")
      {
        $(this).parent().parent().find(".subSWP").removeClass("hidden").show();
        if ($(this).parent().parent().find("input[name=Banks]").val() === "on") {
          $(this).parent().parent().find(".subBanks").removeClass("hidden").show();
        }
      }
      if ($(this).attr("name") === "Banks" && $("input[name=isStateContractor]:checked").val() === "0")
      {
        $(this).parent().parent().find(".subBanks").removeClass("hidden").show();
      }
      if ($(this).attr("name") === "CVP")
      {
        $(this).parent().parent().find(".subCVP").removeClass("hidden").show();
        if ($(this).parent().parent().find("input[name=Jones]").val() === "on") {
          $(this).parent().parent().find(".subJones").removeClass("hidden").show();
        }
      }
      if ($(this).attr("name") === "Jones")
      {
        $(this).parent().parent().find(".subJones").removeClass("hidden").show();
      }
      if ($(this).attr("name") === "OTHER")
      {
        $(this).parent().find(".otherText").removeClass("hidden").show();
      }
    } else {
      $(this).val("off");
      if ($(this).attr("name") === "SWP")
      {
        $(this).parent().parent().find(".subSWP").removeClass("hidden").hide();
        $(this).parent().parent().find(".subBanks").removeClass("hidden").hide();
        $(this).parent().parent().find("input[name=NorthBay]").attr('checked', false);
        $(this).parent().parent().find("input[name=NorthBayV]").val("");
        $(this).parent().parent().find("input[name=Banks]").attr('checked', false);
        $(this).parent().parent().find("input[name=swpIntertie]").attr('checked', false);
        $(this).parent().parent().find("input[name=BanksV]").val("");
        $(this).parent().parent().find(".reach").attr('checked', false);
      }
      if ($(this).attr("name") === "Banks")
      {
        $(this).parent().parent().find(".subBanks").removeClass("hidden").hide();
        $(this).parent().parent().find("input[name=BanksV]").val("");
        $(this).parent().parent().find("input[name=swpIntertie]").attr('checked', false);
        $(this).parent().parent().find(".reach").attr('checked', false);
      }
      if ($(this).attr("name") === "NorthBay")
      {
        $(this).parent().parent().find("input[name=NorthBayV]").val("");
      }
      if ($(this).attr("name") === "CVP")
      {
        $(this).parent().parent().find(".subCVP").removeClass("hidden").hide();
        $(this).parent().parent().find(".subJones").removeClass("hidden").hide();
        $(this).parent().parent().find("input[name=Jones]").attr('checked', false);
        $(this).parent().parent().find("input[name=Forbearance]").attr('checked', false);
        $(this).parent().parent().find("input[name=WarrenAct]").attr('checked', false);
        $(this).parent().parent().find("input[name=JonesV]").val("");
        $(this).parent().parent().find("input[name=ForbearanceV]").val("");
        $(this).parent().parent().find("input[name=WarrenActV]").val("");
      }
      if ($(this).attr("name") === "Jones")
      {
        $(this).parent().parent().find(".subJones").removeClass("hidden").hide();
        $(this).parent().parent().find("input[name=JonesV]").val("");
      }
      if ($(this).attr("name") === "Forbearance")
      {
        $(this).parent().parent().find("input[name=ForbearanceV]").val("");
      }
      if ($(this).attr("name") === "WarrenAct")
      {
        $(this).parent().parent().find("input[name=WarrenActV]").val("");
      }
      if ($(this).attr("name") === "OTHER")
      {
        $(this).parent().find(".otherText").removeClass("hidden").hide();
        $(this).parent().find("input[name=typeDetail]").val("");
        $(this).parent().find("input[name=otherV]").val("");
      }
    }
  };
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="County of Seller Functions">
  self.initSelectCombo = function () {
    self.combobox.combobox({
      create: function () {
        var select = null;
        var comboCt = null;
        var combobox = null;
        if (((select = $(this)).length > 0)
                && ((comboCt = select.next(".custom-combobox")).length > 0)
                && ((combobox = comboCt.children().first(".custom-combobox-input")).length > 0)) {
          combobox.attr("placeholder", "Select...");
          combobox.css("background", "#fff");
          combobox.css("border", "1px solid #a6a8a8");
          if (select.is("[required]")) {
            combobox.attr("required", "required");
          }
        }
      }
      , select: function (event, ui) {
        try {
          var option = ui.item;
          var jsonStr = $(option).attr("json");
          if (jsonStr !== "") {
            var json = JSON.parse(jsonStr);
//              alert(json.name);
            self.addSelectedCounty(json);
            $(this).val(json.name);
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
  };
  
  self.addSelectedCounty = function (json) {
    self.countyCt = $(".countyCt");
    var countyListCt = null;
    if (json && ((countyListCt = self.countyCt.find("#countyList")).length > 0)
            && (self.countyCt.find("input[name=countyId][value=" + json.wtCountyId + "]")).length === 0) {
      var county = $("<input type='hidden' autocomplete='off' name='countyId' />").val(json.wtCountyId);
      var countyKeyRemove = $('<span class="ui-icon ui-icon-circle-close countyItem" style="display: inline-block; vertical-align: bottom;"></span>');
      countyKeyRemove.click(self.removeCounty);
      var countyKeyButton = $("<span class='countyButton' />");
      countyKeyButton.append(county).append(json.name).append(countyKeyRemove);
      countyListCt.append(countyKeyButton);
    }
  };
  
  self.removeCounty = function () {
    var item = this;
    var msg = "Are you sure you want to remove this county?";
    self.confirmationWindow(msg, function (bool) {
      if (!bool) {
        return false;
      }
      $(item).parent().remove();
    });
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Other main Functions">
  self.preTransferInfo = function () {
    if ($(this).val() === "1") {
      $(".preTransfer").removeClass('hidden').show();
    } else if ($(this).val() === "0") {
      $(".preTransfer").removeClass('hidden').hide();
    }
  };

  self.calProTransferQua = function () {
    var cicheckbox = self.baseinfoTab.find("#cropidling-check");
    var gwcheckbox = self.baseinfoTab.find("#groundwater-check");
    var rvcheckbox = self.baseinfoTab.find("#reservoir-check");
    var waterTransQuaRV = self.waterTransQuaRV.val().replace(/,/g, '');
    var waterTransQuaCI = self.waterTransQuaCI.val().replace(/,/g, '');
//      var waterTransQuaGW = self.grossTransPumping.val().replace(/,/g, '');
    var waterTransQuaGW = self.netTransWater.val().replace(/,/g, '');
    var totalTransQua = 0;
    
    if (cicheckbox.prop('checked') && waterTransQuaCI !== '') {
      totalTransQua += parseFloat(waterTransQuaCI);
    }
    if (gwcheckbox.prop('checked') && waterTransQuaGW !== '') {
      totalTransQua += parseFloat(waterTransQuaGW);
    }
    if (rvcheckbox.prop('checked') && waterTransQuaRV !== '') {
      totalTransQua += parseFloat(waterTransQuaRV);
    }
//    alert(totalTransQua);
    self.proTransQua.val(totalTransQua.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2}));

//      self.calProAgreePaidAmt();
  };


  self.checkDeltaTransferHandler = function () {
    if ($(this).val() === "0") {
      $("#exportWindow").removeClass('hidden').hide();
    } else {
      $("#exportWindow").removeClass('hidden').show();
    }
  };

  self.envRegComp = function () {
    if (this.checked)
    {
      $(this).val("on");
      if ($(this).attr("name") === "SWRCB") {
        self.baseinfoTab.find(".swrcb").removeClass("hidden").show();
      }
      if ($(this).attr("name") === "CEQA") {
        self.baseinfoTab.find(".ceqa").removeClass("hidden").show();
      }
      if ($(this).attr("name") === "NEPA") {
        self.baseinfoTab.find(".nepa").removeClass("hidden").show();
      }
    } else {
      $(this).val("off");
      if ($(this).attr("name") === "SWRCB") {
        self.baseinfoTab.find(".swrcb").removeClass("hidden").hide();
      }
      if ($(this).attr("name") === "CEQA") {
        self.baseinfoTab.find(".ceqa").removeClass("hidden").hide();
      }
      if ($(this).attr("name") === "NEPA") {
        self.baseinfoTab.find(".nepa").removeClass("hidden").hide();
      }
    }
  };

  self.setReachValue = function () {
    if (this.checked)
    {
      $(this).val("on");
    } else {
      $(this).val("off");
    }
  };

  self.exportFromMsgHandler = function () {
    if ($(this).val() === "July") {
      $("#exportWindowMsg").removeClass('hidden').hide();
    } else {
      $("#exportWindowMsg").removeClass('hidden').show();
    }
  };

  self.exportToMsgHandler = function () {
    if ($(this).val() === "September") {
      $("#exportWindowMsg").removeClass('hidden').hide();
    } else {
      $("#exportWindowMsg").removeClass('hidden').show();
    }
  };
  
  self.saveBIField = function (el) {
    var transId = $(el).attr("transId"),
        fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      return;
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
//    alert(window.SERVER_ROOT + '/proposal/savebifield');
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savebifield'
      , data: {transId: transId, fieldName: fieldName, fieldValue: $(el).val()}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Proposal base information. Please contact the site adminstrator to resolve the issue!";
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
  
  self.checkEnvRegCompliance = function (el) {
    var transId = $(el).attr("transId"),
        fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      return;
    }
    var fieldValue = $(el).val();
    if (el.checked){
      fieldValue = "on";
    } else {
      fieldValue = "off";
    }
//    alert(transId + ":" + fieldName + ":" + fieldValue);
//    $.ajax({
//      url: window.SERVER_ROOT + '/proposal/checkenvregcomp'
//      , data: {transId: transId, fieldName: fieldName, fieldValue: fieldValue}
//      , method: 'POST'
//      , dataType: 'json'
//      , cache: false
//      , success: function (response, textStatus, jqXhr) {
//        try {
//          if (!response.success) {
//            throw response.error || "Error occurred while updating the Proposal base information. Please contact the site adminstrator to resolve the issue!";
//          }
//        }
//        catch (e) {
//          if (response.callback) {
//            var callback = eval(response.callback);
//            callback.call(self, e);
//          } else {
//            alert(e);
//          }
//        }
//      }
//    });
    
    return this;
  };
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Gets and Util Functions">
  self.confirmationWindow = function (msg, callback) {
    $("<div>", {
      html: '<p>' + msg + '</p>'
    }).dialog({
      title: 'Removal Confirmation'
      , modal: true
      , width: 400
      , height: 180
      , close: function () {
        $(this).dialog("destroy").remove();
      }
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
            $(this).dialog("destroy").remove();
            callback(false);
          }
        }]
    });
  };
  // </editor-fold>

  self.init();
};