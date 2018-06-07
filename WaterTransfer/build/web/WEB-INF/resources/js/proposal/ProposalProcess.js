var ProposalProcess = function ()
{
  var self = this;
  self.init = function ()
  {    
    self.initItems();  
    self.initListeners();    
  };

  self.initItems = function ()
  {
    self.contentBody = $("#content-ct");
    self.proposalHeader = self.contentBody.find("div.header_tab");
    self.proposalprocessTab = $("#proposalprocess_tab");
    self.helpIcon = self.proposalprocessTab.find(".help-icon");
    self.helpCt = self.proposalprocessTab.find(".help-ct");
    self.commentIcon = self.proposalprocessTab.find(".comment-icon");
    self.swpaoContractNum = self.proposalprocessTab.find("input[name=swpaoContractNum]");
    self.contractAmount = self.proposalprocessTab.find("input[name=contractAmount]");
    self.ciContractAmount = self.proposalprocessTab.find("input[name=ciContractAmount]");
    self.rvContractAmount = self.proposalprocessTab.find("input[name=rvContractAmount]");
    self.gwContractAmount = self.proposalprocessTab.find("input[name=gwContractAmount]");
    self.agencyCheckbox = self.proposalprocessTab.find(".approvalAgency");
    self.assignedReviewer = self.proposalprocessTab.find(".assignedReviewer");
    self.usbrRow = self.proposalprocessTab.find(".usbrRow");
    self.statusMsg = self.proposalprocessTab.find(".statusMsg");
    self.statusId = self.proposalprocessTab.find("#statusId");
    self.banksFinalPercent = self.proposalprocessTab.find("input[name=banksfinalPercent]");
    self.reachLossPercent = self.proposalprocessTab.find("input[name=reachLossPercent]");
    self.swpaoContractNum.inputmask("99-999");
    self.numField = $(".numField span");
    
    self.transferAmountTable = self.proposalprocessTab.find("#transfer-amount-table");
    self.ciProposedAmount = self.transferAmountTable.find("span#ci1");
    self.rvProposedAmount = self.transferAmountTable.find("span#rv1");
    self.gwProposedAmount = self.transferAmountTable.find("span#gw1");
    self.ciContractAmountReport = self.transferAmountTable.find("span#ci2");
    self.rvContractAmountReport = self.transferAmountTable.find("span#rv2");
    self.gwContractAmountReport = self.transferAmountTable.find("span#gw2");
    self.ciActualAmount = self.transferAmountTable.find("span#ci3");
    self.rvActualAmount = self.transferAmountTable.find("span#rv3");
    self.gwActualAmount = self.transferAmountTable.find("span#gw3");
    self.ciExportAmount = self.transferAmountTable.find("input#ci4");
    self.rvExportAmount = self.transferAmountTable.find("input#rv4");
    self.gwExportAmount = self.transferAmountTable.find("input#gw4");
    self.ciExportedAmount = self.transferAmountTable.find("input#ci5");
    self.rvExportedAmount = self.transferAmountTable.find("input#rv5");
    self.gwExportedAmount = self.transferAmountTable.find("input#gw5");
    self.ciDeliveredAmount = self.transferAmountTable.find("input#ci6");
    self.rvDeliveredAmount = self.transferAmountTable.find("input#rv6");
    self.gwDeliveredAmount = self.transferAmountTable.find("input#gw6");
    
    self.cropidlingTab = $("#cropidling_tab");
    self.waterTransQuaCI = self.cropidlingTab.find("input#waterTransQuaCI");
    self.reservoirTab = $("#reservoir_tab");
    self.waterTransQuaRV = self.reservoirTab.find("input#waterTransQuaRV");
    self.groundwaterTab = $("#groundwater_tab");
    self.depletionFactor = self.groundwaterTab.find("#depletionFactor"); 
    self.netTransWater = self.groundwaterTab.find("#netTransWater");
    
    self.proSubmitDate_help = "This date is automated generate by system when seller click submit button. To change the date, click on the proposal status tab and enter the change date using the interface on that tab.";
    self.proCompleteDate_help = "This date represents when all required fields in the proposal have been completed. To change the date, click on the proposal status tab and enter the change date using the interface on that tab.";
    self.condApprovalDate_help = "This date is when conditional approval is granted by DWR. To change the date, click on the proposal status tab and enter the change date using the interface on that tab.";
    self.proApprovedDate_help = "This date is when the proposal is fully approved. To change the date, click on the proposal status tab and enter the change date using the interface on that tab.";
    self.complete_comment = self.proposalprocessTab.find("#completeComment").html();
    self.condApproval_comment = self.proposalprocessTab.find("#condApprovalComment").html();
    self.proApproved_comment = self.proposalprocessTab.find("#proApprovedComment").html();
    
    self.agencyCheckbox.each(function () {
      $(this).val("off");
      if (this.checked)
      {
        $(this).val("on");
        if ($(this).attr("name") === "USBR") {
          self.usbrRow.removeClass("hidden").show();
        }
      }
    });
    
    self.initTransferAmountTable();  
  };
  
  self.initTransferAmountTable = function () {
//    alert("initTransferAmount");
    var ciActualAmountNum = 0;
    var ciExportedAmountNum = 0;
    var rvActualAmountNum = 0;
    var rvExportedAmountNum = 0;
    var gwActualAmountNum = 0;
    var gwExportAmountNum = 0;    
    var gwExportedAmountNum = 0;
    var depletionFactor = 0;
    var banksFinalPercent = 0; 
    var reachLossPercent = 0;
    
    if (self.ciActualAmount.text() !== "") {
      ciActualAmountNum = parseFloat(self.ciActualAmount.text().replace(/[, ]+/g, ""));
    }
    if (self.rvActualAmount.text() !== "") {
      rvActualAmountNum = parseFloat(self.rvActualAmount.text().replace(/[, ]+/g, ""));
    }
    if (self.gwActualAmount.text() !== "") {
      gwActualAmountNum = parseFloat(self.gwActualAmount.text().replace(/[, ]+/g, ""));
    }
    if (self.depletionFactor.val() !== "") {
      depletionFactor = 1-parseFloat(self.depletionFactor.val() / 100);
    }
    if (self.banksFinalPercent.val() !== "") {
      banksFinalPercent = 1-parseFloat(self.banksFinalPercent.val() / 100);
    }
    if (self.reachLossPercent.val() !== "") {
      reachLossPercent = 1-parseFloat(self.reachLossPercent.val() / 100);
    }
//    alert(self.waterTransQuaCI.val());
    self.ciProposedAmount.text(self.waterTransQuaCI.val());
    self.ciContractAmountReport.text(self.ciContractAmount.val());
//    if (self.ciExportAmount.val() === ''){
//      self.ciExportAmount.val(self.ciActualAmount.text());
//    }
//    if(ciActualAmountNum !== 0 && banksFinalPercent !== 0) {
//      ciExportedAmountNum = ciActualAmountNum * banksFinalPercent;
//      self.ciExportedAmount.text(ciExportedAmountNum);
////      self.ciExportedAmount.text(ciExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
//    if(ciExportedAmountNum !== 0 && reachLossPercent !== 0) {
//      var deliveredAmount = ciExportedAmountNum * reachLossPercent;
//      self.ciDeliveredAmount.text(deliveredAmount);
////      self.ciDeliveredAmount.text(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
    
    self.rvProposedAmount.text(self.waterTransQuaRV.val());
    self.rvContractAmountReport.text(self.rvContractAmount.val());
//    self.rvExportAmount.text(self.rvActualAmount.text());
//    if(rvActualAmountNum !== 0 && banksFinalPercent !== 0) {
//      rvExportedAmountNum = rvActualAmountNum * banksFinalPercent;
//      self.rvExportedAmount.text(rvExportedAmountNum);
////      self.rvExportedAmount.text(rvExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
//    if(rvExportedAmountNum !== 0 && reachLossPercent !== 0) {
//      var deliveredAmount = rvExportedAmountNum * reachLossPercent;
//      self.rvDeliveredAmount.text(deliveredAmount);
////      self.rvDeliveredAmount.text(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
    
    self.gwProposedAmount.text(self.netTransWater.val());
    self.gwContractAmountReport.text(self.gwContractAmount.val());
//    if(gwActualAmountNum !== 0 && depletionFactor !== 0) {
//      gwExportAmountNum = gwActualAmountNum * depletionFactor;
//      self.gwExportAmount.text(gwExportAmountNum);
////      self.gwExportAmount.text(gwExportAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
//    if(gwExportAmountNum !== 0 && banksFinalPercent !== 0) {
//      gwExportedAmountNum = gwExportAmountNum * banksFinalPercent;
//      self.gwExportedAmount.text(gwExportedAmountNum);
////      self.gwExportedAmount.text(gwExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
//    if(gwExportedAmountNum !== 0 && reachLossPercent !== 0) {
//      var deliveredAmount = gwExportedAmountNum * reachLossPercent;
//      self.gwDeliveredAmount.text(deliveredAmount);
////      self.gwDeliveredAmount.text(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
//    }
    
    var numFields = self.transferAmountTable.find("td span");
    numFields.each(function () { 
      var thisValue = $(this).text();
//      alert($(this).text());
      if (thisValue !== "") {
        var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
        var adjustedValue = num.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
        $(this).text(adjustedValue);
      }
    });
  };

  self.initListeners = function () {
    self.commentIcon.unbind('click').bind("click", self.initComments);
    self.helpIcon.unbind('click').bind("click", self.initHelpHandler);
    self.agencyCheckbox.unbind('click').bind("click", self.checkboxHandler);
    self.assignedReviewer.unbind('change').bind("change", self.initAssignedReviewer);
  };

  self.initComments = function () {
    var msg = "Comment";
    switch ($(this).attr("id")) {
      case "complete_comment":
        msg = self.complete_comment;
        break;
      case "condApproval_comment":
        msg = self.condApproval_comment;
        break;
      case "proApproved_comment":
        msg = self.proApproved_comment;
        break;
    }

    self.helpCt.html('<p>' + msg + '</p>');
    self.helpCt.dialog({
      title: 'Status Comment'
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

  self.initHelpHandler = function () {
    var msg = "Help";
    switch ($(this).attr("id")) {
      case "proSubmitDate_help":
        msg = self.proSubmitDate_help;
        break;
      case "proCompleteDate_help":
        msg = self.proCompleteDate_help;
        break;
      case "condApprovalDate_help":
        msg = self.condApprovalDate_help;
        break;
      case "proApprovedDate_help":
        msg = self.proApprovedDate_help;
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

  self.checkboxHandler = function () {
    if (this.checked)
    {
      $(this).val("on");
      if ($(this).attr("name") === "USBR") {
//            alert("USBR on");
        self.usbrRow.removeClass("hidden").show();
      }
    } else {
      $(this).val("off");
      if ($(this).attr("name") === "USBR") {
//            alert("USBR off");
        self.usbrRow.removeClass("hidden").hide();
      }
    }
  };

  self.initAssignedReviewer = function () {
    var statusId = parseInt(self.statusId.val());
    if (statusId < 2) {
      self.statusMsg.removeClass("hidden");
    }
  };
  
  self.resetTransferAmountTable = function () {
    var ciExportAmountNum = 0;
    var gwExportAmountNum = 0;
    var rvExportAmountNum = 0; 
    var ciExportedAmountNum = 0;
    var gwExportedAmountNum = 0;
    var rvExportedAmountNum = 0;       
    var banksFinalPercent = 0; 
    var reachLossPercent = 0;
    var depletionFactor = 0;
    var gwActualAmountNum = 0;
    
//    if (!$(this).parent().hasClass("isExpand")){
//      $(this).parent().addClass("isExpand");
//    }
    self.ciExportAmount.val(self.ciActualAmount.text());
    self.rvExportAmount.val(self.rvActualAmount.text());
    if (self.depletionFactor.val() !== "" && self.gwActualAmount.text() !== "") {
      gwActualAmountNum = parseFloat(self.gwActualAmount.text().replace(/[, ]+/g, ""));
      depletionFactor = 1-parseFloat(self.depletionFactor.val() / 100);
      gwExportAmountNum = gwActualAmountNum * depletionFactor;
      self.gwExportAmount.val(gwExportAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    
    if (self.ciExportAmount.val() !== "") {
      ciExportAmountNum = parseFloat(self.ciExportAmount.val().replace(/[, ]+/g, ""));
    }
    if (self.gwExportAmount.val() !== "") {
      gwExportAmountNum = parseFloat(self.gwExportAmount.val().replace(/[, ]+/g, ""));
    }
    if (self.rvExportAmount.val() !== "") {
      rvExportAmountNum = parseFloat(self.rvExportAmount.val().replace(/[, ]+/g, ""));
    }
    if (self.banksFinalPercent.val() !== "") {
      banksFinalPercent = 1-parseFloat(self.banksFinalPercent.val() / 100);
    }
    if (self.reachLossPercent.val() !== "") {
      reachLossPercent = 1-parseFloat(self.reachLossPercent.val() / 100);
    }
    
    if(ciExportAmountNum !== 0 && banksFinalPercent !== 0) {
      ciExportedAmountNum = ciExportAmountNum * banksFinalPercent;
      self.ciExportedAmount.val(ciExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    if(ciExportedAmountNum !== 0 && reachLossPercent !== 0) {
      var deliveredAmount = ciExportedAmountNum * reachLossPercent;
      self.ciDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    if(gwExportAmountNum !== 0 && banksFinalPercent !== 0) {
      gwExportedAmountNum = gwExportAmountNum * banksFinalPercent;
      self.gwExportedAmount.val(gwExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    if(gwExportedAmountNum !== 0 && reachLossPercent !== 0) {
      var deliveredAmount = gwExportedAmountNum * reachLossPercent;
      self.gwDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    if(rvExportAmountNum !== 0 && banksFinalPercent !== 0) {
      rvExportedAmountNum = rvExportAmountNum * banksFinalPercent;
      self.rvExportedAmount.val(rvExportedAmountNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
    if(rvExportedAmountNum !== 0 && reachLossPercent !== 0) {
      var deliveredAmount = rvExportedAmountNum * reachLossPercent;
      self.rvDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
    }
  };

  self.saveProcessField = function (el) {
    var transId = $(el).attr("transId"),
        fieldName = $(el).attr("name"),
        fieldValue = $(el).val();
    if (!transId || !fieldName) {
      throw "The transId and the fieldName are required";
    }
    
    var total = self.calTotalContractAmount();
    self.matchReportTable(fieldName,fieldValue);
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/saveprocessfield'
      , data: {transId: transId, fieldName: fieldName, fieldValue: $(el).val(), contractAmount:total}
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
  
  self.matchReportTable = function (fieldName,fieldValue) {    
    var fieldValueString = "";
    if (fieldValue !== ""){
      var fieldValueNum = parseFloat(fieldValue);
      fieldValueString = fieldValueNum.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
    }
    switch (fieldName) {
      case "ciContractAmount":
        self.ciContractAmountReport.text(fieldValueString);
        break;
      case "rvContractAmount":
        self.rvContractAmountReport.text(fieldValueString);
        break;
      case "gwContractAmount":
        self.gwContractAmountReport.text(fieldValueString);
        break;
    }            
  };
  
  self.calTotalContractAmount = function () {
    var total = 0;
    if (self.ciContractAmount.val() !== ""){
      var ciContractAmount = self.ciContractAmount.val().replace(/[, ]+/g, "");
      total = parseFloat(ciContractAmount) + total;
    }
    if (self.rvContractAmount.val() !== ""){
      var rvContractAmount = self.rvContractAmount.val().replace(/[, ]+/g, "");
      total = parseFloat(rvContractAmount) + total;
    }
    if (self.gwContractAmount.val() !== ""){
      var gwContractAmount = self.gwContractAmount.val().replace(/[, ]+/g, "");
      total = parseFloat(gwContractAmount) + total;
    }
//    alert(total);
    if (total === 0){
      self.contractAmount.val("");
    } else {
      var totalString = total.toLocaleString('en-US', {maximumFractionDigits: 2});
      self.contractAmount.val(totalString);
    }
    
    return total;
  };

  self.saveAgencyApproval = function (el) {
//    alert("saveAgencyApproval");
    var transId = $(el).attr("transId"),
            fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      throw "The transId and the fieldName are required";
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    var fieldValue = $(el).val();
    if ($(el).val() === "off") {
      fieldValue = 1;
    } else if ($(el).val() === "on") {
      fieldValue = 0;
    }
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/saveagencyapproval'
      , data: {transId: transId, fieldName: fieldName, fieldValue: fieldValue}
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

  self.saveWaterLoss = function (el) {
//    alert("saveAgencyApproval");
    var transId = $(el).attr("transId"),
            fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      throw "The transId and the fieldName are required";
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    var fieldValue = $(el).val();
    var data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue};
//    self.initTransferAmountTable();
    // Remove triiger for calculation
//    if (fieldName === "banksfinalPercent" || fieldName === "reachLossPercent"){
//      self.resetTransferAmountTable();
////      alert(self.ciDeliveredAmount.val());
//      data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
//              ciExportedAmount:self.ciExportedAmount.val(), gwExportedAmount:self.gwExportedAmount.val(),
//              rvExportedAmount:self.rvExportedAmount.val(), ciDeliveredAmount:self.ciDeliveredAmount.val(),
//              gwDeliveredAmount:self.gwDeliveredAmount.val(), rvDeliveredAmount:self.rvDeliveredAmount.val()};
//    }
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savewaterloss'
      , data: data
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
  
  self.saveTransferAmountAll = function (el) {
    var transId = $(el).attr("transId");
    if (!transId) {
      throw "The transId are required";
    }
//    alert(transId +": "+ self.ciExportAmount.val());
    var data = {transId: transId 
                ,ciExportAmount: self.ciExportAmount.val(),ciExportedAmount: self.ciExportedAmount.val(),ciDeliveredAmount:self.ciDeliveredAmount.val()
                ,gwExportAmount: self.gwExportAmount.val(),gwExportedAmount: self.gwExportedAmount.val(),gwDeliveredAmount:self.gwDeliveredAmount.val()
                ,rvExportAmount: self.rvExportAmount.val(),rvExportedAmount: self.rvExportedAmount.val(),rvDeliveredAmount:self.rvDeliveredAmount.val()};

    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savetransferamountall'
      , data: data
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Propodal Process. Please contact the site adminstrator to resolve the issue!";
          }
          alert("Transfer Amount Saved!");
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
  
  self.saveTransferAmountReport = function (el) {
    var transId = $(el).attr("transId"),
            fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      throw "The transId and the fieldName are required";
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    var fieldValue = $(el).val();
    var banksFinalPercent = self.banksFinalPercent.val(); 
    var reachLossPercent = self.reachLossPercent.val();
    var exportedAmount = "";
    var deliveredAmount = "";
    if (fieldName === "ciExportAmount" || fieldName === "gwExportAmount" || fieldName === "rvExportAmount"){
      if (fieldValue !== "" && banksFinalPercent !== "") {
        exportedAmount = fieldValue * (1-parseFloat(banksFinalPercent/100));
      }
      if (exportedAmount !== "" && reachLossPercent !== "") {
        deliveredAmount = exportedAmount * (1-parseFloat(reachLossPercent/100));
      }
    }
    if (fieldName === "ciExportedAmount" || fieldName === "gwExportedAmount" || fieldName === "rvExportedAmount"){
      if (fieldValue !== "" && reachLossPercent !== "") {
        deliveredAmount = fieldValue * (1-parseFloat(reachLossPercent/100));
      }
    }
    var data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue};
    switch (fieldName) {
      case "ciExportAmount":
        self.ciExportedAmount.val(exportedAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        self.ciDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                ciExportedAmount: exportedAmount, ciDeliveredAmount: deliveredAmount};
        break;
      case "ciExportedAmount":
        self.ciDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                ciExportedAmount: exportedAmount, ciDeliveredAmount: deliveredAmount};
        break;
      case "gwExportAmount":
        self.gwExportedAmount.val(exportedAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        self.gwDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                gwExportedAmount: exportedAmount, gwDeliveredAmount: deliveredAmount};
        break;
      case "gwExportedAmount":
        self.gwDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                gwExportedAmount: exportedAmount, gwDeliveredAmount: deliveredAmount};
        break;
      case "rvExportAmount":
        self.rvExportedAmount.val(exportedAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        self.rvDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                rvExportedAmount: exportedAmount, rvDeliveredAmount: deliveredAmount};
        break;
      case "rvExportedAmount":
        self.rvDeliveredAmount.val(deliveredAmount.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2}));
        data = {transId: transId, fieldName: fieldName, fieldValue: fieldValue,
                rvExportedAmount: exportedAmount, rvDeliveredAmount: deliveredAmount};
        break;
    }
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savetransferamountreport'
      , data: data
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
  
  self.buildPropsalTitle = function(el){
    var transYear = self.proposalHeader.attr("transyear");
    var sellerName = self.proposalHeader.attr("sellername");
    var swpaoNo = $(el).val();
    var newTitle = sellerName + " "+ transYear + " Water Transfer Proposal";
    if (swpaoNo !== ""){
      newTitle += " SWPAO # " + swpaoNo;
    }
    
    self.proposalHeader.attr('swpaono', swpaoNo);
    self.proposalHeader.html(newTitle);
  };

  self.init();
};


