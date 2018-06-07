
var PublicSearch = function(){
  var self = this;
  self.init=function(){
    self.initItems();
    self.numberHandler();
    self.initListeners();
  };
  
  self.initItems = function()
  {
    self.searchContent = $("#public-search-ct");
    self.publicSearchTable = $("#public-search-table");
    self.transYear = self.publicSearchTable.find("input[name=transYear]");
    self.swpaoNum = self.publicSearchTable.find("input[name=swpaoContractNum]");
    self.duration = self.publicSearchTable.find("input[name=isShortLong]");
    self.checkbox = self.publicSearchTable.find("input[type=checkbox]");
    self.seller = self.publicSearchTable.find("select[name=sellerAgency]");
    self.buyer = self.publicSearchTable.find("select[name=buyerAgency]");
    self.county = self.publicSearchTable.find("select[name=sellerCounty]");
    
    self.proposalListCt = $("#proposal-list-ct");
    self.proposalTb = $("#proposal-tb");
    self.proposalDetailCt = $(".proposal-detail-ct");
    self.agencyListCt = $("#agency_list_ct");
  };
  
  self.numberHandler = function()
  {
    self.numField = $(".numField");
    self.numFieldText = $(".numFieldText");
    self.intField = $(".intField");
    self.yearField = $(".yearField");
    self.floatField = $(".floatField");

    self.yearField.keyup(function () {
      this.value = this.value.replace(/[^0-9]+/g, '');
    });
    self.intField.keyup(function () {
      this.value = this.value.replace(/[^0-9]+/g, '');
    });
    self.floatField.keyup(function () {
      this.value = this.value.replace(/(?!^-)[^0-9.]+/g, '');
    });
    self.numField.keyup(function () {
      this.value = this.value.replace(/[^0-9\.]+/g, '');
    });

    self.intField.on("blur", function () {
      if (this.value !== "") {
        var num = parseInt(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString('en-US', {
        });
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
    
    self.numFieldText.each(function () { 
//      alert($(this).text());
      if ($(this).text() !== "") {
        var num = parseFloat($(this).text().replace(/[, ]+/g, ""));
        $(this).text('');
//        $(this).text(num.toLocaleString('en-US'));
        $(this).text(num.toLocaleString('en-US', {
          minimumFractionDigits: 0,
          maximumFractionDigits: 0
        }));
      }
    });
  };
  
  self.initListeners = function(){
        
//    self.proposalListCt.on("draw.dt",function(){
//      var td = $(this).find("tbody td:not(:last-child)");
//      var dt = $(this).DataTable();
//      td.on("click",function(){
//        var tr = $(this).parent();
//        var transId = dt.row(tr[0]).data().wtTransId;
//        alert(transId);
//      });
//    });
  };
  
  self.resetSearch = function(el)
  {
    self.transYear.val("");
    self.swpaoNum.val("");
    self.duration.removeAttr('checked');
    self.checkbox.removeAttr('checked');
    self.seller.val("");
    self.buyer.val("");
    self.county.val("");
  };
  
  self.initSearch = function(el)
  {
    var searchData = self.buildData();

    console.log(searchData);
    if (jQuery.isEmptyObject(searchData)){
      alert("You need put criteria for search.");
      return;
    }
    self.searchContent.mask("Search...");
//    self.proposalTb.find("tbody").empty();
//    self.proposalTb.find("tbody").html("");
    self.initDataTable(searchData);
    self.proposalListCt.show();
    self.searchContent.unmask();
    
    self.proposalListTb = self.proposalListCt.find("#proposal-tb");
    
//    $.ajax({
//      url: window.SERVER_ROOT + '/proposalservice/getpubliclist'
//      , data: {jsondata: JSON.stringify(searchData)}
//      , method: 'POST'
//      , dataType: 'json'
//      , cache: false
//      , success: function (response, textStatus, jqXhr) {
//        self.searchContent.unmask();
//        try {
//          if (!response.success) {
//            throw response.error || "Error occurred while searching. Please contact the site adminstrator to resolve the issue!";
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
  
  self.buildData=function()
  {
    var jsonData = new Object;
    var transYear = self.transYear.val();
    var swpaoNum = self.swpaoNum.val();
    var seller = self.seller.val();
    var buyer = self.buyer.val();
    var county = self.county.val();
    
    if (transYear !== ""){
      jsonData["transYear"] = transYear;
    }
//    if (swpaoNum !== ""){
//      jsonData["swpaoContractNum"] = swpaoNum;
//    }
    self.duration.each(function () {
      if(this.checked){
        jsonData["duration"] = $(this).val();
      }
    });
    self.checkbox.each(function () {
      if (this.checked) {
        jsonData[this.name] = this.value;
      }
    });
    if (seller !== null){
      jsonData["seller"] = seller;
    }
    if (buyer !== null){
      jsonData["buyer"] = buyer;
    }
    if (county !== null){
      jsonData["county"] = county;
    }
    
    return jsonData;
  }; 
  
  self.initDataTable = function (data) {
//    alert("initDataTable");
//    var url = window.SERVER_ROOT + "/proposalservice/getpubliclist";
    var url = window.SERVER_ROOT + "/proposalservice/getpubliclist?data="+JSON.stringify(data);
//    var url = window.SERVER_ROOT + "/proposalservice/getpubliclist";

//    self.myDataTable = new MyDataTable({
    self.proposalTb.DataTable({
      "ajax": {
        url: url
        ,dataType: 'json'
        ,cache: true
      }
      , "bDestroy": true
      , serverSide: false
      , paging: true
      , pageSize: 25
//      , scrollY: 200
      , lengthMenu: [[25, 50, 100, -1], [25, 50, 100, "All"]]      
      , searching: false
      , defaultSort: [[0, 'desc']]
      , columnDefs: [{
          targets: [0]
          , visible: false
        }]
      , columns: [
        {data: "wtTransId"
          , render: function (data, display, row, setting) {
            var html = "";
            if (data) {
              html = "<a href='' onclick='window.publicSearch.dialogProposalDetail(" + row.wtTransId + ");return false'>" + data + "</a>";
            }
            return html;
          }
        }, {data: "transYear"
          , render: function (data, display, row, setting) {
            return data ? data : "";
//            var html = "";
//            if (data) {
//              html = "<a style='cursor:pointer;' onclick='window.publicSearch.dialogProposalDetail("+row.wtTransId+");return false'>" + data + "</a>";
//            }
//            return html; 
          }
        }, {
          data: "swpaoContractNum"
          , render: function (data, display, row, setting) {
            return data ? data : "";
//            var html = "<a href='' onclick='return false'></a>";
//            if (data) {
//              html = "<a href='' onclick='window.publicSearch.expandProposalDetail("+row.wtTransId+",this);return false'>" + data + "</a>";
//            }
//            return html; 
          }
        }
        , { data: "isShortLong"
          , render: function (data) {
            return data === 0 ? "Temporary" : "Long-Term";
          }
        }
        , { data: "seller"
          , render: function (data, display, row, setting) {
            return data ? data : "";
//            var html = "<a href='' onclick='return false'></a>";
//            if (data) {
////              var link = window.SERVER_ROOT + "/report/viewproposal/" + row.wtTransId;
//              html = "<a href='' style='cursor:pointer;' onclick='window.publicSearch.expandProposalDetail("+row.wtTransId+");return false'>" + data + "</a>";
//            }
//            return html;
          }
        }
        , { data: "buyers"
          , render: function (data, display, row, setting) {
            return data ? data : "";
//            var html = "<a href='' onclick='return false'></a>";
//            if (data) {
//              html = "<a href='' style='cursor:pointer;' onclick='window.publicSearch.expandProposalDetail("+row.wtTransId+");return false'>" + data + "</a>";
//            }
//            return html; 
          }
        }
        , { data: "transType"
          , render: function (data, display, row, setting) {
            return data ? data : "";
//            var html = "";
//            if (data) {
//              html = "<a style='cursor:pointer;' onclick='window.publicSearch.dialogProposalDetail("+row.wtTransId+");return false'>" + data + "</a>";
//            }
//            return html; 
          }
        }
        , {data: "status"}
        , {render: function (data, display, row, setting) {
            var transType = row.transType;
            var ciContractAmount = row.ciContractAmount?row.ciContractAmount:"";
            var ciActualAmount = row.ciActualAmount?row.ciActualAmount:"";
            var ciDeliveredAmount = row.ciDeliveredAmount?row.ciDeliveredAmount:"";
            var gwContractAmount = row.gwContractAmount?row.gwContractAmount:"";
            var gwActualAmount = row.gwActualAmount?row.gwActualAmount:"";
            var gwDeliveredAmount = row.gwDeliveredAmount?row.gwDeliveredAmount:"";
            var resContractAmount = row.resContractAmount?row.resContractAmount:"";
            var resActualAmount = row.resActualAmount?row.resActualAmount:"";
            var resDeliveredAmount = row.resDeliveredAmount?row.resDeliveredAmount:"";
            
            var html = "<a href='' onclick='window.publicSearch.expandProposalDetail("+row.wtTransId+",this);return false;'>Details</a>";            
            html += '<div style="padding:10px 5px 0;" class="proposal-detail-ct hidden">'; 
            html += '<table class="transfer-detail-table">'
                    +'<thead>'
                    +  '<tr class="transfer-detail-header">'
                    +   '<th style="width:15%;">Type of Transfer</th>'
                    +   '<th style="width:13%;">Contract Amount (acre-feet)</th>'
                    +   '<th style="width:13%;">Actual Amount (acre-feet)</th>'
                    +   '<th style="width:13%;">Water Delivered (after all losses, acre-feet)</th>'
                    +  '</tr>'
                    +'</thead><tbody>';
//            if (transType.includes("CI")){  -- "includes" function does not work in IE
            if (transType.indexOf('CI')>-1){
              html += '<tr>'
                       +'<td>CI</td>'
                       +'<td><span class="numFieldText" id="ci1">'+ciContractAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="ci2">'+ciActualAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="ci3">'+ciDeliveredAmount+'</span></td>'
                       +'</tr>';
            }
//            if (transType.includes("GW")){
            if (transType.indexOf('GW')>-1){
              html += '<tr>'
                       +'<td>GW</td>'
                       +'<td><span class="numFieldText" id="gw1">'+gwContractAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="gw2">'+gwActualAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="gw3">'+gwDeliveredAmount+'</span></td>'
                       +'</tr>';
            }
//            if (transType.includes("RES")){
            if (transType.indexOf('RES')>-1){
              html += '<tr>'
                       +'<td>RES</td>'
                       +'<td><span class="numFieldText" id="rv1">'+resContractAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="rv2">'+resActualAmount+'</span></td>'
                       +'<td><span class="numFieldText" id="rv3">'+resDeliveredAmount+'</span></td>'
                       +'</tr>';
             }
            html += '</tbody></table>';
            html += '</div>';
            return html;
          }
        }
      ]
    });
  };
  
  self.dialogProposalDetail = function(wtTransId){
//    alert(wtTransId);
    var url = window.SERVER_ROOT + '/report/viewproposal/' + wtTransId;
    $.ajax({
      type:'POST'
      , url: url
      , cache: false
      ,success: function(data,status,jqxhr){
//        alert(data);
        self.form = $("<form method='post' id='proposalform' action='#'>");
        self.form.append(data);
        self.form.dialog({
          title: 'Water Transfer Record Information'
          ,modal:true
          ,width:800
          ,height:400
          ,resizable:true
          ,close:function(){
            $(this).dialog('destroy').remove();
          }
          ,buttons:[{
            text:"Close"
            ,click:function(){
                $(this).dialog("destroy").remove();
            }
          }]
        }).dialog('open');
      }
      , error: function (xhr, errorType, exception) {
        alert("Failed");
      }
    });
  };
  
  self.expandProposalDetail = function(wtTransId,el){
//    alert($(el).parent());
    self.thisrow = $(el).parent().parent();
    var thisProposalDetailCt = self.thisrow.find(".proposal-detail-ct");
    var newrow = "<tr class='expandRow'><td colspan='7'>"+thisProposalDetailCt.html()+"</td></tr>";
//    newrow += '<table class="transfer-detail-table">'
//              +'<thead>'
//              +  '<tr class="transfer-detail-header">'
//              +   '<th style="width:15%;">Type of Transfer</th>'
//              +   '<th style="width:13%;">Contract Amount (acre-feet)</th>'
//              +   '<th style="width:13%;">Actual Amount (acre-feet)</th>'
//              +   '<th style="width:13%;">Water Delivered (after all losses:acre-feet)</th>'
//              +  '</tr>'
//              +'</thead><tbody>';
//    newrow += '</tbody></table>'+thisProposalDetailCt.html()+"</td></tr>";
    var expandRow = self.thisrow.next("tr");
    var expandDetailTable = self.thisrow.next("tr").find(".transfer-detail-table");
    if (expandRow.attr("class")==='expandRow'){
      expandDetailTable.parent().toggle("slow");
    } else {
      self.thisrow.after(newrow);
      self.numberHandler();
    }
    
//    if (typeof thisProposalDetailCt.html() !== 'undefined'){
////      alert(thisProposalDetailCt.html());
//      thisProposalDetailCt.parent().toggle("slow");
//    } else {    
//      var url = window.SERVER_ROOT + '/report/viewproposal/' + wtTransId;
//      $.ajax({
//        type:'POST'
//        , url: url
//        , cache: false
//        ,success: function(data,status,jqxhr){
////          alert(self.thisrow.html());
//          var newrow = "<tr class='expandRow'><td colspan='7'>"+data+"</td></tr>";
//          self.thisrow.after(newrow);
//          self.numberHandler();
//        }
//        , error: function (xhr, errorType, exception) {
//          alert("Failed");
//        }
//      });
//    }
  };
  
  self.initAgencyList = function (el){
    self.agencyListCt.dialog({
      title: 'Agency List'
      , width: 500
      , modal: true
      , height: 600
      , buttons: [{
          text: 'Close'
          , click: function () {
            $(this).dialog("destroy").remove();
          }
        }]
    });
  };
  
  self.initPrintCSV = function (el) {
    var printType = $(el).attr("id");
    var msg = "Do you want to export to CSV?";
    var title = "Export Confirmation";
    self.userValidation(title, msg, function (bool) {
      if (!bool)
      {
        return false;
      }
//    alert(printType);
      self.proposalListTable = $(el).parent().parent().find("#proposal-tb");
      var tableArray = [];
      var tableRows = self.proposalListTable.find("tbody tr");
      var filename = "ProposalListReport.csv";
      if (tableRows.length < 1)
      {
        alert('The proposal list is empty');
        return false;
      }
      if (printType === "simpleCSV"){
        $.each(tableRows, function () {
          self.buildSimpleData($(this),tableArray);
        });
      } else if (printType === "detailCSV"){    
        $.each(tableRows, function () {
          self.buildDetailData($(this),tableArray);
        });
      }
  //    console.log(tableArray);
      var url = window.SERVER_ROOT + "/report/fileExport?name=" + filename;
      if (tableArray.length > 0)
      {
        $.ajax({
          url: url
          , data: {jsonData: JSON.stringify(tableArray)}
          , cache: false
          , async: false
          , timeout: 30000
          , type: 'POST'
          , dataType: 'json'
          , success: function (response, status, jqxhr) {
            try {
              if (!response.success) {
                throw response.error || "Unable to remove target storage.";
              }
              location = window.SERVER_ROOT + '/report/downloadFile?name=' + filename;
            } catch (e) {
              self.statusReportCt.unmask();
              if (response.callback) {
                var callback = eval(response.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              } else if (e) {
                alert(e);
              }
            }
          }
        });
      }
    });
  };
  
  self.buildSimpleData = function(row,tableArray){
    var jsonObj = new Object();
    if(row.hasClass("odd")||row.hasClass("even")){
      jsonObj["Year"] = row.find("td").eq(0).html();
      jsonObj["SWPAO/Contract#"] = row.find("td").eq(1).html();
      jsonObj["Duration"] = row.find("td").eq(2).html();
      jsonObj["Seller"] = row.find("td").eq(3).html();
      jsonObj["Buyer"] = row.find("td").eq(4).html();
      jsonObj["Transfer Type"] = row.find("td").eq(5).html();
      jsonObj["Status"] = row.find("td").eq(6).html();
      tableArray.push(jsonObj);
    }
  };
  
  self.buildDetailData = function(row,tableArray){
    var jsonObj = new Object();
    if(row.hasClass("odd")||row.hasClass("even")){
      jsonObj["Year"] = row.find("td").eq(0).html();
      jsonObj["SWPAO/Contract#"] = row.find("td").eq(1).html();
      jsonObj["Duration"] = row.find("td").eq(2).html();
      jsonObj["Seller"] = row.find("td").eq(3).html();
      jsonObj["Buyer"] = row.find("td").eq(4).html();
      jsonObj["Transfer Type"] = row.find("td").eq(5).html();
      jsonObj["Status"] = row.find("td").eq(6).html();
      tableArray.push(jsonObj);    
    
      jsonObj = new Object();
      jsonObj["SWPAO/Contract#"] = "Type of Transfer";
      jsonObj["Duration"] = "Contract Amount (acre-feet)";
      jsonObj["Seller"] = "Actual Amount (acre-feet)";
      jsonObj["Buyer"] = "Water Delivered (after all losses, acre-feet)";
      tableArray.push(jsonObj);
      var detailTable = row.find(".transfer-detail-table");
//      alert(detailTable.html());
      var tableRows = detailTable.find("tbody tr");
      $.each(tableRows,function(){
        jsonObj = new Object();
        jsonObj["SWPAO/Contract#"] = $(this).find("td").eq(0).html();
        jsonObj["Duration"] = $(this).find("td").eq(1).find("span").text();
        jsonObj["Seller"] = $(this).find("td").eq(2).find("span").text();
        jsonObj["Buyer"] = $(this).find("td").eq(3).find("span").text();
        tableArray.push(jsonObj);
      });
    }    
    
//    if(row.hasClass("expandRow")){
////      alert(row.html());
//      jsonObj = new Object();
//      jsonObj["SWPAO/Contract#"] = "Type of Transfer";
//      jsonObj["Duration"] = "Contract Amount";
//      jsonObj["Seller"] = "Actual Amount";
//      jsonObj["Buyer"] = "Water Delivered";
//      tableArray.push(jsonObj);
//      var expandTable = row.find(".transfer-detail-table");
//      var tableRows = expandTable.find("tbody tr");
//      $.each(tableRows,function(){
//        jsonObj = new Object();
//        jsonObj["SWPAO/Contract#"] = $(this).find("td").eq(0).html();
//        jsonObj["Duration"] = $(this).find("td").eq(1).find("span").text();
////        alert($(this).find("td").eq(1).find("span").text());
//        jsonObj["Seller"] = $(this).find("td").eq(2).find("span").text();
//        jsonObj["Buyer"] = $(this).find("td").eq(3).find("span").text();
//        tableArray.push(jsonObj);
//      });      
//    } 
//    console.log(tableArray);
    
//    return tableArray;
  };
  
  self.userValidation = function (tle, msg, callback)
  {
    var title = 'User Validation';
    if (tle !== null && tle !== '') {
      title = tle;
    }
    if (msg)
    {
      $("<div>", {
        html: '<p>' + msg + '</p>'
      }).dialog({
        title: title
        , modal: true
        , width: 400
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
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback(false);
              }
            }
          }]
      }).dialog("open");
    }
    else {
      callback();
    }
  };  
  
  self.init();
};
