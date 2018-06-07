/* 
 * Document   : TransferReport.js
 * Author     : ymei
 */

var TransferReport = function () {
  var self = this;
  self.init = function () {
    self.initItems();
    self.initListeners();
  };
  
  self.initItems = function () {
    self.transreportTab = $("#transreport_tab");
    self.baseinfoTab = $("#baseinfo_tab");
    self.ciMonthlyProdTable = self.transreportTab.find("#ci-monthly-prod-table");
    self.rvMonthlyProdTable = self.transreportTab.find("#rv-monthly-prod-table");
    self.gwMonthlyProdTable = self.transreportTab.find("#gw-monthly-prod-table");
    self.attachmentTable = self.transreportTab.find(".attachmentTb");
    self.saveButton = self.transreportTab.find("#saveComment");     
    
    self.mayTwCi = self.ciMonthlyProdTable.find("#mayTwCi");
    self.juneTwCi = self.ciMonthlyProdTable.find("#juneTwCi");
    self.julyTwCi = self.ciMonthlyProdTable.find("#julyTwCi");
    self.augustTwCi = self.ciMonthlyProdTable.find("#augustTwCi");
    self.septemberTwCi = self.ciMonthlyProdTable.find("#septemberTwCi");
    self.totalTwCi = self.ciMonthlyProdTable.find("#totalTwCi");
    
    self.aprilTwRv = self.rvMonthlyProdTable.find("#aprilTwRv");
    self.mayTwRv = self.rvMonthlyProdTable.find("#mayTwRv");
    self.juneTwRv = self.rvMonthlyProdTable.find("#juneTwRv");
    self.julyTwRv = self.rvMonthlyProdTable.find("#julyTwRv");
    self.augustTwRv = self.rvMonthlyProdTable.find("#augustTwRv");
    self.septemberTwRv = self.rvMonthlyProdTable.find("#septemberTwRv");
    self.octoberTwRv = self.rvMonthlyProdTable.find("#octoberTwRv");
    self.novemberTwRv = self.rvMonthlyProdTable.find("#novemberTwRv");
    self.totalTwRv = self.rvMonthlyProdTable.find("#totalTwRv");
    
    self.aprilTwGw = self.gwMonthlyProdTable.find("#aprilTwGw");
    self.mayTwGw = self.gwMonthlyProdTable.find("#mayTwGw");
    self.juneTwGw = self.gwMonthlyProdTable.find("#juneTwGw");
    self.julyTwGw = self.gwMonthlyProdTable.find("#julyTwGw");
    self.augustTwGw = self.gwMonthlyProdTable.find("#augustTwGw");
    self.septemberTwGw = self.gwMonthlyProdTable.find("#septemberTwGw");
    self.octoberTwGw = self.gwMonthlyProdTable.find("#octoberTwGw");
    self.novemberTwGw = self.gwMonthlyProdTable.find("#novemberTwGw");
    self.totalTwGw = self.gwMonthlyProdTable.find("#totalTwGw");
    
    self.proposalprocessTab = $("#proposalprocess_tab");
    self.transferAmountTable = self.proposalprocessTab.find("#transfer-amount-table");
    self.ciActualAmount = self.transferAmountTable.find("span#ci3");
    self.rvActualAmount = self.transferAmountTable.find("span#rv3");
    self.gwActualAmount = self.transferAmountTable.find("span#gw3");
  };
  
  self.initListeners = function () {
    self.calTotalTwCi();
    self.calTotalTwRv();
    self.calTotalTwGw();
//    self.initAttachmentTable();
  };
  
  //<editor-fold defaultstate="collapsed" desc="Calculate Total Transfer Water">
  self.calTotalTwCi = function(){
    var mayTwCiVal = 0;
    var juneTwCiVal = 0;
    var julyTwCiVal = 0;
    var augustTwCiVal = 0;
    var septemberTwCiVal = 0;
    var totalTwCiCal = 0;
    
    if(self.mayTwCi.val() !== ""){
      mayTwCiVal = parseFloat(self.mayTwCi.val().replace(/[, ]+/g, ""));
      totalTwCiCal +=  mayTwCiVal;
    }
    if(self.juneTwCi.val() !== ""){
      juneTwCiVal = parseFloat(self.juneTwCi.val().replace(/[, ]+/g, ""));
      totalTwCiCal +=  juneTwCiVal;
    }
    if(self.julyTwCi.val() !== ""){
      julyTwCiVal = parseFloat(self.julyTwCi.val().replace(/[, ]+/g, ""));
      totalTwCiCal +=  julyTwCiVal;
    }
    if(self.augustTwCi.val() !== ""){
//      alert(self.augustTw.val());
      augustTwCiVal = parseFloat(self.augustTwCi.val().replace(/[, ]+/g, ""));
      totalTwCiCal +=  augustTwCiVal;
    }
    if(self.septemberTwCi.val() !== ""){
      septemberTwCiVal = parseFloat(self.septemberTwCi.val().replace(/[, ]+/g, ""));
      totalTwCiCal +=  septemberTwCiVal;
    }
    if (totalTwCiCal > 0){
//      totalTwCiCal = totalTwCiCal.toFixed(2);
      var totalTwCiCalString = totalTwCiCal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
      self.totalTwCi.val(totalTwCiCalString);
      self.ciActualAmount.text(totalTwCiCalString);
    }
    
    return totalTwCiCal;
  };
  
  self.calTotalTwRv = function(){
    var aprilTwRvVal = 0;
    var mayTwRvVal = 0;
    var juneTwRvVal = 0;
    var julyTwRvVal = 0;
    var augustTwRvVal = 0;
    var septemberTwRvVal = 0;
    var octoberTwRvVal = 0;
    var novemberTwRvVal = 0;
    var totalTwRvCal = 0;
    
    if(self.aprilTwRv.val() !== ""){
      aprilTwRvVal = parseFloat(self.aprilTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  aprilTwRvVal;
    }
    if(self.mayTwRv.val() !== ""){
      mayTwRvVal = parseFloat(self.mayTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  mayTwRvVal;
    }
    if(self.juneTwRv.val() !== ""){
      juneTwRvVal = parseFloat(self.juneTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  juneTwRvVal;
    }
    if(self.julyTwRv.val() !== ""){
      julyTwRvVal = parseFloat(self.julyTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  julyTwRvVal;
    }
    if(self.augustTwRv.val() !== ""){
      augustTwRvVal = parseFloat(self.augustTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  augustTwRvVal;
    }
    if(self.septemberTwRv.val() !== ""){
      septemberTwRvVal = parseFloat(self.septemberTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  septemberTwRvVal;
//      alert(septemberTwRvVal);
    }
    if(self.octoberTwRv.val() !== ""){
      octoberTwRvVal = parseFloat(self.octoberTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  octoberTwRvVal;
    }
    if(self.novemberTwRv.val() !== ""){
      novemberTwRvVal = parseFloat(self.novemberTwRv.val().replace(/[, ]+/g, ""));
      totalTwRvCal +=  novemberTwRvVal;
    }
//    alert(totalTwRvCal);
//    totalTwCal = mayTwVal + juneTwVal + julyTwVal + augustTwVal + septemberTwVal;
    if (totalTwRvCal > 0){
//      totalTwRvCal = totalTwRvCal.toFixed(2);
      var totalTwRvCalString = totalTwRvCal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
//      alert(totalTwRvCalString);
      self.totalTwRv.val(totalTwRvCalString);
      self.rvActualAmount.text(totalTwRvCalString);
    }
    
    return totalTwRvCal;
  };
  
  self.calTotalTwGw = function(){
    var aprilTwGwVal = 0;
    var mayTwGwVal = 0;
    var juneTwGwVal = 0;
    var julyTwGwVal = 0;
    var augustTwGwVal = 0;
    var septemberTwGwVal = 0;
    var octoberTwGwVal = 0;
    var novemberTwGwVal = 0;
    var totalTwGwCal = 0;
    
    if(self.aprilTwGw.val() !== ""){
      aprilTwGwVal = parseFloat(self.aprilTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  aprilTwGwVal;
    }
    if(self.mayTwGw.val() !== ""){
      mayTwGwVal = parseFloat(self.mayTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  mayTwGwVal;
    }
    if(self.juneTwGw.val() !== ""){
      juneTwGwVal = parseFloat(self.juneTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  juneTwGwVal;
    }
    if(self.julyTwGw.val() !== ""){
      julyTwGwVal = parseFloat(self.julyTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  julyTwGwVal;
    }
    if(self.augustTwGw.val() !== ""){
      augustTwGwVal = parseFloat(self.augustTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  augustTwGwVal;
    }
    if(self.septemberTwGw.val() !== ""){
      septemberTwGwVal = parseFloat(self.septemberTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  septemberTwGwVal;
    }
    if(self.octoberTwGw.val() !== ""){
      octoberTwGwVal = parseFloat(self.octoberTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  octoberTwGwVal;
    }
    if(self.novemberTwGw.val() !== ""){
      novemberTwGwVal = parseFloat(self.novemberTwGw.val().replace(/[, ]+/g, ""));
      totalTwGwCal +=  novemberTwGwVal;
    }
//    alert(totalTwGwCal);
//    totalTwCal = mayTwVal + juneTwVal + julyTwVal + augustTwVal + septemberTwVal;
    if (totalTwGwCal > 0){
      var totalTwGwCalString = totalTwGwCal.toLocaleString('en-US', {minimumFractionDigits: 2,maximumFractionDigits: 2});
      self.totalTwGw.val(totalTwGwCalString);
      self.gwActualAmount.text(totalTwGwCalString);
//      alert(totalTwGwCalString);
    }
    
    return totalTwGwCal;
  };
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Initial Attachment Table">
  self.initAttachmentTable = function () {
    alert(self.attachmentTable.html());
    self.tableRow = self.attachmentTable.find("tr");
    var tableRowHeader = self.tableRow.find("th");
    var tableRowCell = self.tableRow.find("td");
    
    $.each(tableRowHeader,function(){
      $(this).show();
      alert($(this).html());
    });
    
    $.each(tableRowCell,function(){
      $(this).show();
    });
  };
  //</editor-fold>  
  
  //<editor-fold defaultstate="collapsed" desc="Save Field Functions">
  self.saveCiField = function (el) {
//    alert("saveCIField");
    var totalTwCiVal = 0;
    var cropIdlingId = $(el).attr("cropIdlingId"),
        fieldName = $(el).attr("name");
    if (!cropIdlingId || !fieldName) {
      throw "The cropIdlingId and the fieldName are required";
    }
    if (fieldName === 'totalTwCi'){
      totalTwCiVal = $(el).val();
    } else {
      totalTwCiVal = self.calTotalTwCi();
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savecifield'
      , data: {cropIdlingId: cropIdlingId, fieldName: fieldName, fieldValue: $(el).val(), actualAmount: totalTwCiVal}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the Reporting Crop Idling Table. Please contact the site adminstrator to resolve the issue!";
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
  
  self.saveRvField = function (el) {
//    alert("saveRvField");
    var totalTwRvVal = 0;
    var reservoirId = $(el).attr("reservoirId"),
        fieldName = $(el).attr("name");
    if (!reservoirId || !fieldName) {
      throw "The reservoirId and the fieldName are required";
    }
    if (fieldName === 'totalTwRv'){
      totalTwRvVal = $(el).val();
    } else {
      totalTwRvVal = self.calTotalTwRv();
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savervfield'
      , data: {reservoirId: reservoirId, fieldName: fieldName, fieldValue: $(el).val(), actualAmount: totalTwRvVal}
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
  
  self.saveGwField = function (el) {
//    alert("saveGwField");
    var totalTwGwVal = 0;
    var groundwaterId = $(el).attr("groundwaterId"),
        fieldName = $(el).attr("name");
    if (!groundwaterId || !fieldName) {
      throw "The groundwaterId and the fieldName are required";
    }
    if (fieldName === 'totalTwGw'){
      totalTwGwVal = $(el).val();
    } else {
      totalTwGwVal = self.calTotalTwGw();
    }
//    alert(groundwaterId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savegwfield'
      , data: {groundwaterId: groundwaterId, fieldName: fieldName, fieldValue: $(el).val(), actualAmount: totalTwGwVal}
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
  
  self.saveReportComment = function (el){
    var transId = $(el).attr("transId"),
        fieldName = $(el).attr("name");
    if (!transId || !fieldName) {
      throw "The transId and the fieldName are required";
    }
//    alert(transId + ":" + fieldName + ":" + $(el).val());
    $.ajax({
      url: window.SERVER_ROOT + '/proposal/savereportfield'
      , data: {transId: transId, fieldName: fieldName, fieldValue: $(el).val()}
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
  // </editor-fold>
  
  self.init();
};
