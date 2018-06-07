var GroundwaterInfo = function(){
  var self = this;
  var commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;
  
  self.init = function(){
    self.initItem();
    self.initListeners();
    self.helpMsg();
    self.initGWCalculate();
  };
  self.initItem = function(){
    self.groundwaterTab = $("#groundwater_tab");
    self.helpIcon = self.groundwaterTab.find(".help-icon");
    self.helpCt = self.groundwaterTab.find(".help-ct");
    self.calBtn = self.groundwaterTab.find("#calBtn");
    self.commentIcon = self.groundwaterTab.find(".commentIcon");
    self.isResRelease = self.groundwaterTab.find("input[name=isResRelease]");
    self.isResReleaseMsg = self.groundwaterTab.find("#isResReleaseMsg");
    self.grossTransPumping = self.groundwaterTab.find("#grossTransPumping");
    self.streamDepletion = self.groundwaterTab.find("#streamDepletion");
    self.netTransWater = self.groundwaterTab.find("#netTransWater");
    self.gwMonthlyTable = self.groundwaterTab.find("#gw-monthly-table");
  };
  self.initListeners = function(){
    self.isResRelease.on("click",self.resReleaseHandler);
    self.commentIcon.on("click",self.commentIconHandler);
    self.helpIcon.on("click",self.helpIconHandler);
  };
  self.helpMsg = function(){
    self.totalPumping_help = "This is the amount of water pumped by transfer wells during the transfer window.";
//    self.basePumping_help = "This is the amount of water that is normally pumped by the transfer wells during a year of normal operations that does not include a transfer.";
    self.basePumping_help = "Refer to Section 3.4.1 of the Water Transfer White Paper.";
//    self.grossTransPumping_help = "This is an automatically calcuated value. The Baseline Pumping is subtracted from the Total Proposed pumping to calculate Gross Transfer Pumping.";
    self.grossTransPumping_help = "This field is automatically calculated and cannot be modified.</br></br>";
    self.grossTransPumping_help += "Gross Transfer Pumping = Total Proposed Pumping subtract Baseline Pumping"; 
//    self.depletionFactor_help = "Streamflow Depletion factor is used to determine the amount of pumping that depletes local surface waters, and therefore does not count towards the transfer.";
    self.depletionFactor_help = "Refer to Section 3.4.3 of the Water Transfer White Paper.";
//    self.depletions_help = "Streamflow Depletion  is an automatically calculated value that is calculated by multiplying the depletion factor with the gross transfer pumping.";
    self.depletions_help = "This field is automatically calculated and cannot be modified.</br></br>";
    self.depletions_help += "Streamflow Depletion = Gross Transfer Pumping multiplied by Streamflow Depletion Factor";
//    self.netTransWater_help = "Net Transfer Water is the amount of water credited during the transfer. Streamflow Depletion and Baseline Pumping are subtracted from Gross Pumping to determine the Net Water Transfered.";
    self.netTransWater_help = "This is not amount of Water Delivered Transfer. This field is automatically calculated and cannot be modified.</br></br>";
    self.netTransWater_help += "Net Transfer Water = Gross Transfer Pumping subtract Streamflow Depletion"; 
    self.monthlyTotal_help = "The calculation is based on the monthly total for each pumping.";
  };
  self.resReleaseHandler = function(){
    if(self.isResRelease.filter(':checked').val()==="1"){
      self.isResReleaseMsg.removeClass("hidden");
    } else {
      self.isResReleaseMsg.addClass("hidden");
    }
  };
  self.commentIconHandler = function(){
    $(this).next().fadeToggle(1).removeClass("hidden");
    $(this).next().next().fadeToggle(1).removeClass("hidden");
  };
  self.helpTipDialog = function (msg, eq) {
    var fs = "";
    if (eq && eq.length > 0) {
      var legend = $("<legend/>").html("Equation:");
      fs = $("<fieldset/>").css({marginTop: '10px'}).append(legend).append($("<p/>").html(eq));
    }
    self.helpCt.html('<p>' + msg + '</p>').append(fs);
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
    var msg = "Help";
    var eq = "";
    switch ($(this).attr("id")) {
      case "totalPumping_help":
        msg = self.totalPumping_help;
        break;
      case "basePumping_help":
        msg = self.basePumping_help;
        break;
      case "grossTransPumping_help":
        msg = self.grossTransPumping_help;
        break;
      case "depletionFactor_help":
        msg = self.depletionFactor_help;
        break;
      case "depletions_help":
        msg = self.depletions_help;
        break;
      case "netTransWater_help":
        msg = self.netTransWater_help;
        break;
      case "monthlyTotal_help":
        msg = self.monthlyTotal_help;
        break;
    }
    self.helpTipDialog(msg, eq);
  };
  
  // <editor-fold desc="Groundwater Calculate">
  self.initGWCalculate = function () {
    self.calBtn.on("click", function () {
      // Modified just copy from monthly total--ym 2015.12.01
      $('#totalPumping').val($('#proposedPumpingTotal').val().replace(commaRegex, "$1,"));
      $('#basePumping').val($('#baselinePumpingTotal').val().replace(commaRegex, "$1,"));
      $('#grossTransPumping').val($('#grossTransPumpingTotal').val().replace(commaRegex, "$1,"));
      $('#streamDepletion').val($('#streamDepletionTotal').val().replace(commaRegex, "$1,"));
      $('#netTransWater').val($('#netTransWaterTotal').val().replace(commaRegex, "$1,"));
      
      // Calculate will change the value of Total Transfer Amount in Base Tab
      var baseInfo = new BaseInfo();
      baseInfo.calProTransferQua();
    });

    $('#totalPumping,#basePumping,#depletionFactor').keyup(function () {
      self.GWTotalCalculate();

      // If Streamflow Depletion Factor changed, modify all values in monthly form
      $('.pp').each(function () {
        var i = $(this).attr("index");
        if ($(this).val() !== "") {
          self.GWCalculate(i);
        }
      });
    });

    $('.pp,.bp').keyup(function () {
      var i = $(this).attr("index");
      self.GWCalculate(i);
    });

    $('.gp,.dp').keyup(function () {
      var i = $(this).attr("index");
//            alert(i);
      self.NTWCalculate(i);
    });

    self.monthlyPumpingTotal();
  };
  
  self.GWTotalCalculate = function () {
    var totalProposedPumping = $('#totalPumping').val().replace(/[, ]+/g, "");
    var baselinePumping = $('#basePumping').val().replace(/[, ]+/g, "");
    var grossTransPumping = $('#grossTransPumping').val().replace(/[, ]+/g, "");
    var depletionFactor = $('#depletionFactor').val();

    if (totalProposedPumping !== '' && baselinePumping !== '') {
      totalProposedPumping = parseFloat(totalProposedPumping);
      baselinePumping = parseFloat(baselinePumping);
      grossTransPumping = totalProposedPumping - baselinePumping;
      $('#grossTransPumping').val(grossTransPumping.toLocaleString('en-US', {maximumFractionDigits: 2}));
    } else {
      grossTransPumping = '';
      $('#grossTransPumping').val('');
      $('#streamDepletion').val('');
      $('#netTransWater').val('');
    }
    if (depletionFactor !== '' && grossTransPumping !== '') {
      depletionFactor = parseFloat(depletionFactor / 100);
      var streamDepletions = grossTransPumping * depletionFactor;
      var netTransferWater = grossTransPumping - streamDepletions;
      $('#streamDepletion').val(streamDepletions.toLocaleString('en-US', {maximumFractionDigits: 2}));
      $('#netTransWater').val(netTransferWater.toLocaleString('en-US', {maximumFractionDigits: 2}));
    } else {
      $('#streamDepletion').val('');
      $('#netTransWater').val('');
    }
  };
  
  self.GWCalculate = function (i) {
    var proposedPumpingId = "#proposedPumping" + i;
    var baselinePumpingId = "#baselinePumping" + i;
    var grossTransPumpingId = "#grossTransPumping" + i;
    var streamDepletionId = "#streamDepletion" + i;
    var netTransWaterId = "#netTransWater" + i;
    var depletionFactor = $('#depletionFactor').val();
    var proposedPumping = $(proposedPumpingId).val().replace(/[, ]+/g, "");
    var baselinePumping = $(baselinePumpingId).val().replace(/[, ]+/g, "");
    var grossTransPumping = $(grossTransPumpingId).val().replace(/[, ]+/g, "");

    if (proposedPumping !== '' && baselinePumping !== '') {
      proposedPumping = parseFloat(proposedPumping);
      baselinePumping = parseFloat(baselinePumping);
//          grossTransPumping = Math.round(proposedPumping - baselinePumping);
      grossTransPumping = (proposedPumping - baselinePumping);
//          var grossTransPumpingStr = grossTransPumping.toString().replace(commaRegex,"$1,");
      var grossTransPumpingStr = grossTransPumping.toLocaleString('en-US', {maximumFractionDigits: 2});
      $(grossTransPumpingId).val(grossTransPumpingStr);
    } else {
      grossTransPumping = '';
      $(grossTransPumpingId).val('');
      $(streamDepletionId).val('');
      $(netTransWaterId).val('');
    }
    if (depletionFactor !== '' && grossTransPumping !== '') {
      depletionFactor = parseFloat(depletionFactor / 100);
      var streamDepletion = grossTransPumping * depletionFactor;
      var netTransWater = grossTransPumping - streamDepletion;
//          streamDepletion = streamDepletion.toString().replace(commaRegex,"$1,");
      streamDepletion = streamDepletion.toLocaleString('en-US', {maximumFractionDigits: 2});
      $(streamDepletionId).val(streamDepletion);
      netTransWater = netTransWater.toLocaleString('en-US', {maximumFractionDigits: 2});
      $(netTransWaterId).val(netTransWater);
    } else {
      $(streamDepletionId).val('');
      $(netTransWaterId).val('');
    }

    self.monthlyPumpingTotal();
  }; 
  
  self.NTWCalculate = function (i) {
    var grossTransPumpingId = "#grossTransPumping" + i;
    var streamDepletionId = "#streamDepletion" + i;
    var netTransWaterId = "#netTransWater" + i;
    var grossTransPumping = $(grossTransPumpingId).val().replace(/[, ]+/g, "");
    var streamDepletion = $(streamDepletionId).val().replace(/[, ]+/g, "");

    if (streamDepletion !== '' && grossTransPumping !== '') {
//          var netTransWater = Math.round(grossTransPumping - streamDepletion);
      var netTransWater = grossTransPumping - streamDepletion;
      netTransWater = netTransWater.toLocaleString('en-US', {maximumFractionDigits: 2,minimumFractionDigits: 2});
      $(netTransWaterId).val(netTransWater);
    } else {
      $(netTransWaterId).val('');
    }
    self.monthlyPumpingTotal();
  };
  
  self.monthlyPumpingTotal = function () {
    self.row = $("#gw-monthly-table tbody tr");
    $.each(self.row, function () {
      var total = 0;
      $.each($(this).find("td"), function () {
        var val = $(this).find("input.pp,input.bp,input.gp,input.dp,input.nw").val();
        if (val)
        {
          val = val.toString().replace(/[, ]+/g, "");
          total = parseFloat(val) + total;
        }
      });
//      total = Math.round(total);
//      total = total.toString().replace(commaRegex, "$1,");
      total = total.toLocaleString('en-US', {maximumFractionDigits: 2,minimumFractionDigits: 2});
      $(this).find("td input.total").val(total);
    });
  };
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Save Field Functions">
  self.saveGwField = function (el) {
//    alert("saveGwField");
    var groundwaterId = $(el).attr("groundwaterId"),
        fieldName = $(el).attr("name");
    if (!groundwaterId || !fieldName) {
      throw "The groundwaterId and the fieldName are required";
    }
    var grossTransPumping = self.grossTransPumping.val().replace(/[, ]+/g, "");
    var streamDepletion = self.streamDepletion.val().replace(/[, ]+/g, "");
    var netTransWater = self.netTransWater.val().replace(/[, ]+/g, "");
//    alert(groundwaterId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savegwfield'
      , data: {groundwaterId: groundwaterId, fieldName: fieldName, fieldValue: $(el).val(),
                grossTransPumping: grossTransPumping, streamDepletion: streamDepletion, netTransWater:netTransWater}
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
  
  self.saveGwMonthlyField = function (el) {
//    alert("saveGwMonthlyField");
    var groundwaterId = $(el).attr("groundwaterId"),
        month = $(el).attr("index"),
        fieldName = $(el).attr("fieldname");
    if (!groundwaterId || !fieldName) {
      throw "The groundwaterId and the fieldName are required";
    }
    var grossTransPumping = self.gwMonthlyTable.find("#grossTransPumping"+month).val().replace(/[, ]+/g, "");
    var streamDepletion = self.gwMonthlyTable.find("#streamDepletion"+month).val().replace(/[, ]+/g, "");
    var netTransWater = self.gwMonthlyTable.find("#netTransWater"+month).val().replace(/[, ]+/g, "");
//    alert(groundwaterId + ":" + month + ":" + fieldName + ":" + $(el).val());
//    alert(grossTransPumping + ":" + streamDepletion + ":" + netTransWater);
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savegwmonthly'
      , data: {groundwaterId: groundwaterId, month:month, fieldName: fieldName, fieldValue: $(el).val(),
                grossTransPumping: grossTransPumping, streamDepletion: streamDepletion, netTransWater:netTransWater}
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
  // </editor-fold>
  
  self.init();
};