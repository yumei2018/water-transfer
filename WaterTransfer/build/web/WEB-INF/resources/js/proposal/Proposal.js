
/*
 * Document   : Proposal.js -- Global functions for all project
 * Author     : ymei
 * Created on : Oct 10, 2016
 */

var Proposal = function(){
  var self = this;
  var commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;

  self.init = function()
  {
    self.initItems();
    self.initButtonItems();
    self.tabsListeners();
    self.initLoadData();
    self.initHandlers();
    self.initListeners();
  };

  //<editor-fold defaultstate="collapsed" desc="Initial Items">
  self.initItems=function()
  {
    self.contentBody = $("#content-ct");
    self.proposalCt = $("#tabs");
    self.proposalTypeCt = $("#typeMenu");
    self.propPDFForm = $("#proPDFform");
    self.uploadFileForm = $("#upload-file-form");
    self.viewChangeLogLink = $('#view_changelog');

    self.proposalType = self.proposalTypeCt.find(".tab-check");
    self.proposalHeader = self.contentBody.find("div.header_tab");
    self.createForm = self.proposalCt.find("#create-form");
    self.processForm = self.proposalCt.find("#process-form");
    self.isReviewer = self.proposalCt.find(".massDisabled");

    self.tabHeader = self.proposalCt.find(".tab_header");
    self.sellerTab = self.proposalCt.find("#seller_tab");
    self.buyerTab = self.proposalCt.find("#buyer_tab");
    self.baseInfoTab = self.proposalCt.find("#baseinfo_tab");
    self.groundwaterTab = self.proposalCt.find("#groundwater_tab");
    self.cropidlingTab = self.proposalCt.find("#cropidling_tab");
    self.reservoirTab = self.proposalCt.find("#reservoir_tab");
    self.conservedTab = self.proposalCt.find("#conserved_tab");
    self.pdfReporTab = self.proposalCt.find("#pdfreport_tab");
    self.changeLogTab = self.proposalCt.find("#changelog_tab");
    self.proposalProcessTab = self.proposalCt.find("#proposalprocess_tab");
    self.statusTrackTab = self.proposalCt.find("#statustrack_tab");

    self.sellerCt = self.sellerTab.find(".agency_list");
    self.buyerCt = self.buyerTab.find(".agency_list");
    self.sellerPanel =  self.sellerTab.find(".accordion");
    self.buyerPanel =  self.buyerTab.find(".accordion");
    self.loadBuyer = self.buyerTab.find(".load_buyer");
    self.loadSeller = self.sellerTab.find(".load_seller");

    self.image = $("<img>", {
      src: window.SERVER_ROOT + "/resources/images/icons/arrow_icon.png"
      , style: 'margin: 3px; 15px 0 0'
      , class: 'expand_arrow'
    });
    self.baseInfoTabItems();
    self.jsonData = new Object();
  };

  self.baseInfoTabItems = function ()
  {
    self.checkboxCt = self.baseInfoTab.find(".checkbox_ct");
    self.faCheckbox = self.checkboxCt.find('input[type=checkbox]');
    self.transferStartDate = self.baseInfoTab.find("input[name=transWinStart]");
    self.transferEndDate = self.baseInfoTab.find("input[name=transWinEnd]");
  };

  self.initButtonItems = function()
  {
    self.buttonCt = $(".button_ct");
    self.attachButtonCt = $(".attach-button");

    self.addSellerBtn = self.sellerTab.find("input[name=seller]");
    self.addBuyerBtn = self.buyerTab.find("input[name=buyer]");
    self.calBtn = self.proposalCt.find("#calBtn");
    self.addAgencyBtn = self.proposalCt.find(".newAgency");
    self.preTransferAddBtn = self.attachButtonCt.find("#pre_transfer_addBtn");
    self.waterRightsAddBtn = self.attachButtonCt.find("#water_rights_addBtn");

    self.nextBtn = self.buttonCt.find("#nextTab");
    self.saveBtn = self.buttonCt.find("#saveProposal");
    self.saveProcessBtn = self.proposalProcessTab.find("#saveProcess");
    self.submitBtn = self.buttonCt.find("#submitProposal");
    self.approveBtn = self.buttonCt.find("#approveProposal");
    self.suspendBtn = self.buttonCt.find("#suspendProposal");
    self.completeBtn = self.buttonCt.find("#completeProposal");
    self.trackBtn = self.buttonCt.find("#trackProposal");

    self.changeLogBtn = self.buttonCt.find("#changeLog");
    self.proposalPDFPreview = self.buttonCt.find("#proposalReportBtn");
    self.proposalPDFLnk = self.buttonCt.find("#proposalReports");
    self.requestBt = self.buttonCt.find(".request_btn");
  };

  self.afterLoadItems=function()
  {
    self.tableEl = $(".agencyContactList");
    self.editIcon = self.tableEl.find(".edit_contact");
    self.editTd = self.tableEl.find(".edit_td");
    self.deleteIcon = self.tableEl.find(".delete_contact");
    self.deleteTd = self.tableEl.find(".delete_td");
    self.accessIcon = self.tableEl.find(".contact_access");
    self.newContactBtn = self.tableEl.nextAll("input.contact_button");
    self.agencyHeaderCt = $(".headerCt");//self.tableEl.parent().prev();
    self.agencyType = $("select.agencyType");
  };
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Tabs Listerners">
  self.tabsListeners = function () {
    var wtTransId = self.createForm.find("#wtTransId").val();
    var activeTabNum = 0;
    if (wtTransId !== ''){
      activeTabNum = 2;
    }
    self.proposalCt.tabs({
      active: activeTabNum
      , activate: function (event, ui) {
        self.selectAgencyListeners(ui.newPanel);
        if (ui.newTab.index() > 1 && ui.newTab.index() < 10)
        {
          if (ui.newTab.index() === 6) {
            self.saveBtn.addClass("hidden");
            self.submitBtn.addClass("hidden");
            self.changeLogBtn.addClass("hidden");
          } else {
            self.saveBtn.removeClass("hidden").show();
            self.submitBtn.removeClass("hidden").show();
            self.changeLogBtn.removeClass("hidden").show();
          }
          self.loadTabs(ui.newPanel);
        }
      }
      , create: function (event, ui) {
        self.selectAgencyListeners(ui.panel);
        self.loadTabs(ui.panel);
      }
    });
  };

  self.selectAgencyListeners = function (panel)
  {
    // Mark Dirty to Buyers Representative change
    panel.find("select.buyersContact_list").on("change", function () {
      self.markDirty(panel.attr('id'));
    });
    panel.find(".removeContact").on("click", function () {
      self.markDirty(panel.attr('id'));
    });

    panel.find("select.agency_list").on("change", function () {
      var el = $(this), isDuplicate = false;
      if ($(this).val() < 1)
      {
        return false;
      }
      if (self.agencyHeaderCt)
      {
        $.each(self.agencyHeaderCt, function () {
          if ($(this).attr("agencyid") === el.val())
          {
            if (!$(this).attr("removed"))
            {
              return isDuplicate = true;
            }
          }
        });
      }
      if (isDuplicate)
      {
        alert("This Agency is already used, Please Select a different Agency.");
        $(this).val(0);
        return isDuplicate = false;
      }
      this.agencyObj = [];
      this.agencyObj.push($(this).val()); //agencyId
      this.agencyObj.push($(this).find("option:selected").text()); //agency Name
      this.agencyObj.push(panel.attr('id')); //tab type
      var accordionDiv = panel.find(".accordion");
      self.buildTabPanel(this.agencyObj, accordionDiv);
      if ($(this).attr('class') === 'agency_list')
      {
        $(this).next().fadeOut(1);
        $(this).val(0).fadeOut(1);
      }
      self.markDirty(panel.attr('id'));
      if (panel.attr('id') === "seller_tab")
      {
        self.addSellerBtn.fadeOut(1);
        self.buildPropsalTitle(this.agencyObj);
      }
    });
  };

  self.buildPropsalTitle = function(agencyObj){
//    alert(agencyObj[1]);
    var transYear = self.proposalHeader.attr("transyear");
    var sellerName = self.proposalHeader.attr("sellername");
    var swpaoNo = self.proposalHeader.attr("swpaono");
    var newSellerName = agencyObj[1];
    var newTitle = newSellerName + " "+ transYear + " Water Transfer Proposal";
    if (swpaoNo !== ""){
      newTitle += " SWPAO # " + swpaoNo;
    }

    self.proposalHeader.attr('sellername', newSellerName);
    self.proposalHeader.html(newTitle);
  };

  self.buildTabPanel = function (agencyObj, divEl)
  {
    var contentHeader = [], percent = '0.0';
    var percentDiv = null;
    var editStatus = self.proposalHeader.attr("editstatus");
    var disableFlag = true;
//    alert(editStatus);
    if (agencyObj[3])
    {
      percent = agencyObj[3];
    }
//    if (self.proposalCt.find("input[name=draft]").length < 1)
    if (editStatus === "")
    {
      disableFlag = false;
      contentHeader.push(
              $("<img/>", {
                src: window.SERVER_ROOT + "/resources/images/icons/close_x.png"
                , class: 'remove_icon_ct inline'
              }).on("click", function () {
        var deleteIcon = $(this);
        var msg = "Are you sure you want to remove this Agency?";
        var title = "Remove Confirmation";
        self.userValidation(title, msg, function (bool) {
          if (!bool)
          {
            return false;
          }
          deleteIcon.parent('h2').attr("removed", true);
          deleteIcon.parent('h2').next("div").remove();
          deleteIcon.parent('h2').remove();
          self.markDirty(agencyObj[2]);
          if (agencyObj[2] === "seller_tab")
          {
            self.addSellerBtn.show();
          }
        });
      }));
    }
    contentHeader.push(
            $("<img>", {
              src: window.SERVER_ROOT + "/resources/images/icons/arrow_icon.png"
              , class: 'expand_arrow'
            }));
    contentHeader.push(
            $("<span/>", {
              html: agencyObj[1]
              , class: 'title inline'
            }));

    if (agencyObj[2] !== "seller_tab")
    {
      var percenHeaderLabel = $("<label/>", {
        html: percent + '%'
        , id: 'percent-header'
        , class: 'percent'
      }).on("click", function () {
        if (self.proposalCt.find("input[name=draft]").length < 1)
        {
//                        $(this).hide();
//                        $(this).prev().show().removeClass("hidden");
//                        $(this).prev().focus();
        }
      });
      contentHeader.push(
              percenHeaderLabel
              );
      contentHeader.push(
              $("<img>", {
                src: window.SERVER_ROOT + "/resources/images/icons/edit-icon.png"
                , class: 'edit_icon hidden'
              }).on("click", function () {
        $(this).prev().hide();
        $(this).prev().prev().show().removeClass("hidden").focus();
      })
              );

      percentDiv = $('<div/>')
              .css({
                padding: '0 0 5px 10px'
                , margin: '0 25px 0 0'
                , display: 'inline-block'
              })
              .html('Percentage of transfer water allocation: ')
              .append(
                      $("<input/>", {
                        name: 'water_percent'
                        , type: 'text'
                        , value: percent
                        , class: 'percent'
                        , disabled: disableFlag
                      }).keyup(function () {
                this.value = this.value.replace(/[^0-9\.]+/g, '');
              }).on("keyup", function () {
                //                $(this).hide();
                //                self.label =  $(this).next().show();
                //                if(self.label.html() !== $(this).val())

                {
                  var msg = false;//"Are you sure you want to update the changes?";
                  var val = $(this).val();
                  if (!val) {
                    val = 0;
                  }
                  if (val > 100)
                  {
                    
                    alert('Cannot Exceed over 100%');
                    $(this).val('100');
                    percenHeaderLabel.text('100%');
                    return false;
                  }
                  if (!isFinite(val))
                  {
                    alert('Wrong input value type');
                    return false;
                  }
                  percenHeaderLabel.text(val + '%');
                  self.markDirty(agencyObj[2]);
                }
              }).on('blur', function () {
                if (!$(this).val())
                  $(this).val(0);
              }))
              .append($("<label>", {
                html: '%'
                , style: 'vertical-align:bottom;padding-left:2px;'
              }));
    }
//    alert(editStatus);
//    if (editStatus === 'disabled'){
//      $('.percent').prop("disabled", true);
//    }
    self.getAgencyContactList(agencyObj[0], function (data) {
      $("<h2>").addClass("headerCt isExpand").attr({agencyid: agencyObj[0]}).html(contentHeader).appendTo(divEl);
      $("<div>", {class: 'contact_panel'})
              .html(data)
              .prepend(percentDiv)
              .appendTo(divEl);
      self.afterLoadItems();
      self.afterLoadListeners();
//            console.log(agencyObj);
    });
  };

  self.getAgencyContactList = function (agencyId, callback)
  {
    $("#content-ct").mask("Loading...");
//    alert(self.getStatusName());
    $.ajax({
      type: 'POST'
      , url: window.SERVER_ROOT + "/proposal/contactListByAgency"
      , data: {wtAgencyId: agencyId, wtTransId: self.getTransId(), statusName:self.getStatusName()}
      , cache: false
      , success: function (data, status, jqx)
      {
        $("#content-ct").unmask();
        callback(data);
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.loadTabs = function (panel) {
    self.attachmentContainer = panel.find(".attachment-container");
    var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
    var typeId = self.attachmentContainer.attr("typeid");
    var mapTypeId = panel.find(".map-attachment-container").attr("typeid");
    if (!wtTransId)
    {
      return false;
    }

    self.preTransferList(typeId, wtTransId, panel);
    self.waterRightsList(typeId, wtTransId, panel);
    self.associateWells(typeId, wtTransId, panel);
    self.attachmentList(typeId, wtTransId, panel);
    self.mapAttachmentList(mapTypeId, wtTransId, panel);
    self.ChangeLog(wtTransId, panel);
//    self.PDFReportList(wtTransId,panel);
//    self.reviewCommentList(typeId,wtTransId,panel);
  };

  self.preTransferList = function (type, wtTransId, panel) {
    if (type !== "BI")
    {
      return false;
    }
    self.preTransferCt = panel.find(".pre_transfer_ct");
    $.ajax({
      type: "POST"
      , url: window['SERVER_ROOT'] + "/proposal/getPreTransferTable"
      , data: {wtTransId: wtTransId}
      , cache: false
      , success: function (data, status, jqxhr) {
        self.preTransferCt.children().remove();
        self.preTransferCt.append(data);
        self.preTransferItems();
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.waterRightsList = function (type, wtTransId, panel) {
    if (type !== "BI")
    {
      return false;
    }
    self.waterRightCt = panel.find(".water_rights_ct");
    $.ajax({
      type: "POST"
      , url: window['SERVER_ROOT'] + "/proposal/getWaterRightsTable"
      , data: {wtTransId: wtTransId}
      , cache: false
      , success: function (data, status, jqxhr) {
        self.waterRightCt.children().remove();
        self.waterRightCt.append(data);
        self.waterRightItems();
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.associateWells = function (type, wtTransId, panel) {
    if (type !== "GW")
    {
      return false;
    }
    $(".proposalReports").unbind("click", self.initPDFReports);
    $(".proposalReports").on("click", self.initPDFReports);
    self.assiocialteWellsCt = panel.find("#listAssociateWellCt");
    $.ajax({
      type: "POST"
      , url: window['SERVER_ROOT'] + "/proposal/getAssociateWells"
      , data: {wtTransId: wtTransId}
      , cache: false
      , success: function (data, status, jqxhr) {
        self.assiocialteWellsCt.children().remove();
        self.assiocialteWellsCt.append(data);
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.attachmentList = function (typeId, wtTransId, panel) {
    self.attachmentContainer = panel.find(".attachment-container");
    var data = {wtTransId: wtTransId, typeId: typeId};
    var url = window.SERVER_ROOT + "/attachment/getAttachmentList";
    if (self.attachmentContainer.length > 0 && !self.attachmentContainer.html())
    {
      $("#content-ct").mask("Loading...");
      $.ajax({
        type: "POST"
        , url: url
        , data: data
        , cache: false
        , success: function (data, status, jqxhr) {
          $("#content-ct").unmask();
          self.attachmentContainer.children().remove();
          self.attachmentContainer.append(data);
          
          // Attachment Control for Reporting Tab
          if(typeId === "TR"){
            self.transreportTab = $("#transreport_tab");
            self.attachmentTable = self.transreportTab.find(".attachmentTb");
            self.tableRow = self.attachmentTable.find("tr");
            var tableRowHeader = self.tableRow.find("th");
            var tableRowCell = self.tableRow.find("td");

            $.each(tableRowHeader,function(){
              $(this).removeClass("hidden").show();
            });

            $.each(tableRowCell,function(){
              $(this).removeClass("hidden").show();
            });
          }
          
          // Attachment Control for Agreement Development Tab
          if(typeId === "PP"){
            self.ppTab = $("#proposalprocess_tab");
            self.attachmentTable = self.ppTab.find(".attachmentTb");
            self.tableRow = self.attachmentTable.find("tr");
            var tableRowHeader = self.tableRow.find("th");
            var tableRowCell = self.tableRow.find("td");

            $.each(tableRowHeader,function(){
              $(this).removeClass("hidden").show();
            });

            $.each(tableRowCell,function(){
              $(this).removeClass("hidden").show();
            });
          }
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };

  self.mapAttachmentList = function (typeId, wtTransId, panel) {
    self.mapAttachCt = panel.find(".map-attachment-container");
    var data = {wtTransId: wtTransId, typeId: typeId};
    var url = window.SERVER_ROOT + "/attachment/getAttachmentList";
    if (self.mapAttachCt.length > 0 && !self.mapAttachCt.html())
    {
//            $("#content-ct").mask("Loading...");
      $.ajax({
        type: "POST"
        , url: url
        , data: data
        , cache: false
        , success: function (data, status, jqxhr) {
//                    $("#content-ct").unmask();
          self.mapAttachCt.children().remove();
          self.mapAttachCt.append(data);
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };

  self.PDFReportList = function (wtTransId, panel) {
    if (panel[0].id === "pdfreport_tab") //Log Page
    {
      $("#content-ct").mask("Loading...");
      $.ajax({
        type: "POST"
        , url: window.SERVER_ROOT + "/proposal/PDFReport/" + wtTransId
        , cache: false
        , success: function (data, status, jqxhr) {
          $("#content-ct").unmask();
          self.pdfReportTab.html(data);
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };

  self.reviewCommentList = function (typeId, wtTransId, panel) {
    self.commentsContainer = panel.find(".comments-container");

    var data = {wtTransId: wtTransId, transType: typeId};
    var url = window.SERVER_ROOT + "/proposal/getReviewComments";
    if (!self.commentsContainer.html())
    {
      $("#content-ct").mask("Loading...");
      $.ajax({
        type: "POST"
        , url: url
        , data: data
        , cache: false
        , success: function (data, status, jqxhr) {
          $("#content-ct").unmask();
          self.commentsContainer.children().remove();
          self.commentsContainer.append(data);
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };


  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Initial Data Loading">
  self.initLoadData=function()
  {
    self.initSideMenu();
//    self.initTechComment();
    self.loadExistSeller();
    self.loadExistBuyer();
    self.proposalTypeSelected();
    self.initTransYr();
    self.initSellerBuyer();
    self.initCalendar();
    self.tabHeader.prepend(self.image);
  };

  self.initSideMenu = function()
  {
//    var left = $("#content-ct").position().left;
    var left = $("#content-ct").offset().left;

    self.sideMenu = $("#sideMenu");
    var offset = 12;
    var pos = left - self.sideMenu.width() + offset + $(window).scrollLeft();
    if ($(window).width()<1080){
      pos-= 1080 - $(window).width();
    }
//    console.log(pos);
//    console.log($(window).width());
    self.sideMenu.css({
      right:pos+'px'
    }).removeClass("hidden");

    $(".fixed").css({
      left: left+'px'
    });
  };

//jQuery('<div/>', {
//    id: 'wrapper',
//    css: {width: '1270px',
//          height:'1px'}
//}).appendTo('#content-ct');

  self.initTechComment = function(){
    self.techComments = self.contentBody.find(".techNote");
    $.each(self.techComments, function(){
      this.style.height = "5px";
      this.style.height = (this.scrollHeight) + "px";
//      alert(this.style.height);
    });
  };

  self.loadExistSeller = function ()
  {
    if (self.loadSeller.length > 0)
    {
      $(self.loadSeller.each(function () {
        this.agencyid = self.loadSeller.attr("agencyid");
        this.agencyObj = [];
        this.agencyObj.push(this.agencyid);
        self.sellerCt.val(this.agencyid);
        this.agencyObj.push(self.sellerCt.find("option:selected").text());
        this.agencyObj.push(self.loadSeller.parent().attr("id"));
        self.buildTabPanel(this.agencyObj, self.loadSeller.prev());
        self.sellerCt.val(0);
      }));
    }
  };

  self.loadExistBuyer = function ()
  {
    if (self.loadBuyer.length > 0)
    {
      $(self.loadBuyer.each(function () {
        this.agencyid = $(this).attr("agencyid");
        this.percent = $(this).attr("percent");
        this.agencyObj = [];
        this.agencyObj.push(this.agencyid);
        self.buyerCt.val(this.agencyid);
        this.agencyObj.push(self.buyerCt.find("option:selected").text());
        this.agencyObj.push($(this).parent().attr("id"));
        this.agencyObj.push(this.percent);
        self.buildTabPanel(this.agencyObj, self.loadBuyer.parent().find(".accordion"));
        self.buyerCt.val(0);
      }));
    }
  };
  self.initSellerBuyer = function()
  {
    if (self.getUrlParameter() === -1)
    {
      self.addSellerBtn.show();
    }
  };

  self.initTransYr = function()
  {
    self.transYear = self.proposalCt.find("#transYear");
    if(self.transYear.val() === ""){
      var currentYear = (new Date).getFullYear();
      var currentMonth = (new Date).getMonth() + 1;
      currentMonth<10? self.transYear.val(currentYear):self.transYear.val(currentYear+1);
    }
  };

  self.initCalendar = function () {
    $(".dateField").datepicker();
    $("#proposedSchedule").datepicker();
    $("#submittedDate").datepicker();
  };
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Initial Handlers">
  self.initHandlers=function()
  {
    self.numberHandler();
    self.initAttachmentUploadForm();
    self.fieldsValidation();
  };

  self.numberHandler = function()
  {
    self.numField = $(".numField");
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
    
    self.intField.each(function () {
//      $(this).val(this.value.replace(commaRegex, "$1,"));  
      if (this.value !== "") {
        var num = parseInt(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString().split('.')[0];
//        this.value = num.toLocaleString('en-US', {
//          minimumFractionDigits: 0,
//          maximumFractionDigits: 0
//        });
      }
    });

    self.intField.unbind("blur").bind("blur", function () {
//      alert("On Blur...");
      if (this.value !== "") {
        var num = parseInt(this.value.replace(/[, ]+/g, ""));
        this.value = num.toLocaleString().split('.')[0]; 
//        this.value = num.toLocaleString('en-US', {
//          minimumFractionDigits: 0,
//          maximumFractionDigits: 0
//        });
//        $(this).val(this.value.replace(commaRegex, "$1,"));
      }
    });

    self.numField.each(function () {
//      $(this).val(this.value.replace(commaRegex, "$1,"));  
      if (this.value !== "") {
        var num = parseFloat(this.value.replace(/[, ]+/g, ""));
//        num = num.toFixed(2);
        this.value = num.toLocaleString('en-US', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        });
      }
    });

    self.numField.unbind("blur").on("blur",function(){
      if (this.value !== "") {
        var num = parseFloat(this.value.replace(/[, ]+/g, ""));
//        num = num.toFixed(2);
        this.value = num.toLocaleString('en-US', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        });
      }
    });
  };

  self.proposalTypeSelected = function () {
    $("#CropIdling,#Reservoir,#GroundWater,#Conserved,#Other").hide();
    var isChecked = false;
    isChecked = self.initCheckboxStatus();
    if (!isChecked)
    {
      self.initDisabledBaseInfo(true);
    }
  };

  self.initCheckboxStatus = function ()
  {
    self.checkboxFlag = false;
    self.proposalTypeCt.find("input").each(function () {
      if ($(this).is(':checked')) {
        self.checkboxFlag = true;
        $(this).val("1");
        $("#" + $(this).attr("tabid")).show();
      }
      else
      {
        $(this).val("");
      }
    });
    return self.checkboxFlag;
  };

  self.initDisabledBaseInfo = function (bool)
  {
    self.baseinfoCt = self.baseInfoTab.find(".sub-title");
    self.baseinfoCt.addClass("disabled");
    self.baseinfoCt.find("input:not(input[name=waterRightsNum])").attr("disabled", bool);
    self.baseinfoCt.find("img").attr("disabled", bool);
    self.baseinfoCt.find("textarea").attr("disabled", bool);
    bool ? self.baseinfoCt.addClass("disabled") : self.baseinfoCt.removeClass("disabled");
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Initial Attachment Upload">
  self.initAttachmentUploadForm = function () {
    self.attachButton = $(".attachButton");
    self.uploadPercent = $('.uploadPercent_ct');
    self.uploadFileContainer = $(".upload-file-container");
    var wtTransId = self.createForm.find("#wtTransId").val();

    self.attachButton.on("click", function () {
      // Require to save proposal before attach files
      if (wtTransId === "") {
        alert("Please save proposal before add attachment.");
        return false;
      }
      var attachType = $(this).attr("typeid");
      var containerId = $(this).attr("containerid");
      self.loadAttachmentUploadForm(wtTransId, attachType, containerId);
    });
  };

  self.loadAttachmentUploadForm = function (wtTransId, attachType, containerId) {
    var uploadFileContainer = $("#uploadFile_ct");
    var url = window.SERVER_ROOT + "/attachment/getAttachmentChecklist?attachType=" + attachType;
    $.ajax({
      type: "POST"
      , url: url
      , cache: false
      , success: function (data, status, jqxhr) {
        uploadFileContainer.children().remove();
        uploadFileContainer.append(data);
        self.uploadFileContainer = $(".upload-file-container");
        self.wtTransId = self.uploadFileContainer.find("#wtTransId");
        self.attachType = self.uploadFileContainer.find("#attachType");
        self.containerId = self.uploadFileContainer.find("#containerId");

        self.wtTransId.attr("value", wtTransId);
        self.attachType.attr("value", attachType);
        self.containerId.attr("value", containerId);
        $(".fieldValue").val("");
        self.uploadFilePopup();
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403)
        {
          location = window.SERVER_ROOT;
        }
        else
        {
          alert(exception + " status:" + xhr.status);
        }
      }
    });
  };

  self.uploadFilePopup = function () {
    self.height = $("div.upload-file-container").height() + 185;
    self.uploadFileContainer.dialog({
      appendTo: "form#upload-file-form",
      modal: true,
      title: "Upload Attachment",
      width: 'auto',
      height: self.height,
      resizable: false,
      close: function (event, ui) {
        $(this).dialog("destroy").remove();
      },
      buttons: [{
          text: "Upload"
          , click: function () {
            if (self.uploadFileForm.valid() === false) {
              return false;
            }
            var filename = $("#file").val();
            if (filename === ''){
              alert("You have not chosen any file to upload yet.");
              return false;
            }
            self.attachmentContainer = $("#" + $("#containerId").val());
            self.fileInput = self.uploadFileForm.find("input[type=file]");            
            var form = new FormData(self.uploadFileForm[0]);
            for (var i = 0; i < self.fileInput[0].files.length; i++) //multiple files handler
            {
              form.append('file', self.fileInput[0].files[0]);
            }
            var url = window.SERVER_ROOT + "/attachment/uploadFile";
            // Call upload file function
            setTimeout(self.callUploadFile(url, form, filename), 0);
            $(this).dialog("destroy").remove();
            self.uploadFileForm.children().remove();
          }
        }, {
          text: "Cancel"
          , click: function () {
            $(this).dialog("destroy").remove();
            self.uploadFileForm.find("#tblImageUpload .subdate").addClass('hidden');
          }
        }]
    }).dialog('open');

    self.uploadValidation();
  };

  self.callUploadFile = function (url, form, filename) {
    var now = new Date();
    var start = now.getTime();

    $.ajax({
      xhr: function () {
        var xhr = new window.XMLHttpRequest();
        xhr.upload.addEventListener("progress", function (evt) {
          if (evt.lengthComputable) {
            var percentComplete = evt.loaded / evt.total;
            percentComplete = parseInt(percentComplete * 100);
            self.attachmentContainer.parent().mask("Loading...." + percentComplete + "%");
            if (percentComplete === 100) {
//                self.uploadPercent.html("File "+ filename +" Uploaded!");
            }
          }
        }, false);

        return xhr;
      },
      type: "POST"
      , url: url
      , data: form
      , cache: false
      , processData: false
      , contentType: false
      , success: function (data, status, jqxhr) {
        var now = new Date();
        var end = now.getTime();
        self.attachmentContainer.parent().unmask();
        self.attachmentContainer.children().remove();
        self.attachmentContainer.append(data);
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403)
        {
          location = window.SERVER_ROOT;
        }
        else
        {
          alert("Upload Failed!");
        }
      }
    });
  };

  self.uploadValidation = function () {
    self.uploadFileForm = $("#upload-file-form");
    self.uploadFileForm.validate({
      rules: {
        file: "required",
        title: "required"
      },
      messages: {
        file: "Please choose a file to be uploaded",
        title: "Please specify the file title"
      }
//        submitHandler: function(form) {
//            form.submit();
//            }
    });
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Initial Validation">
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
  
  self.submitMessage = function (tle, msg, callback)
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
            text: 'Accept'
            , click: function ()
            {
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback(true);
              }
            }
          }, {
            text: 'Decline'
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

  self.fieldsValidation = function () {
    var validateField = {
      number: true
    };
    var labels = {
      required: "<p>This field is required</p>",
      selected: "<p>Please check at least one box</p>",
      number: "<p>Please enter a valid number.</p>",
      range: "<p>Please enter a value between 0 and 99.</p>"
    };

    self.createForm.validate({
      onfocusout: function (element) {
        $(element).valid();
      },
      ignore: [],
      rules: {
        transYear: "required",
        proTransQua: "required",
        surWaterSource: "required",
        majorRiverAttribute: "required",
        transWinStart: "required",
        transWinEnd: "required",
        typeMenuHide: {
          required: {
            depends: function (element) {
              return $('.tab-check:checked').size() === 0;
            }
          }
        },
        wtFuTypeIdHide: {
          required: {
            depends: function (element) {
              return $('.wtFuTypeId:checked').size() === 0;
            }
          }
        },
        envRegComplHide: {
          required: {
            depends: function (element) {
              return $('.envRegCompl:checked').size() === 0;
            }
          }
        },
        proUnitCost: validateField,
        pumpingWellsNumber: validateField,
        monitoringWellsNumber: validateField,
        totalPumping: validateField,
        basePumping: validateField,
        depletionFactor: {
          number: true,
          range: [0, 100]
        },
        waterTransQuaCI: validateField,
        totalTransferAcr: validateField,
        waterTransQuaRV: validateField,
        topAllowStorage: validateField,
        targetStorage: validateField,
        locationLat: validateField,
        locationLong: validateField
      },
      messages: {
        transYear: labels,
        proTransQua: labels,
        surWaterSource: labels,
        majorRiverAttribute: labels,
        transWinStart: labels,
        transWinEnd: labels,
        typeMenuHide: labels['selected'],
        wtFuTypeIdHide: labels['selected'],
        envRegComplHide: labels['selected'],
        proUnitCost: labels,
        pumpingWellsNumber: labels,
        monitoringWellsNumber: labels,
        totalPumping: labels,
        basePumping: labels,
        depletionFactor: labels,
        waterTransQuaCI: labels,
        totalTransferAcr: labels,
        waterTransQuaRV: labels,
        topAllowStorage: labels,
        targetStorage: labels,
        locationLat: labels,
        locationLong: labels
      }
    });
  };

  self.validTabs = function (param) {
    var valid = true;
    var validator = "";
//      if (param === "SAVE"){
//          validator = self.saveValidation();
//      }
    if (param === "SUBMIT") {
      validator = self.createForm.validate();
    }

    //Validate Base info Tab
    var baseInputs = self.baseInfoTab.find(".validField");
    baseInputs.each(function () {
      if (!validator.element(this) && valid) {
        valid = false;
      }
    });
    return valid;
  };

  self.currencyValidation = function (currency)
  {
    var regex = /(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$/;
    var val = currency.match(regex);
    if (val === null)
    {
      self.unitCostField.css({border: '1px solid red'});
      return "NaN";
    }
    else
    {
      self.unitCostField.css({border: '1px solid #a6a8a8'});
      var nonComma = val[0].replace(/[, ]+/g, "").trim();
      var non$ = nonComma.split("$");
      if (non$.length > 1)
      {
        return non$[1];
      }
      else {
        return nonComma;
      }
    }
  };

  self.proposalValidation = function (msg, callback)
  {
    $("<div>", {
      html: '<p>' + msg + '</p>'
    }).dialog({
      title: 'Proposal Validation'
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

  self.submitValidation = function ()
  {
    // Validation for At least have one Seller
    if (!self.getSellerTab().WT_AGENCY_ID)
    {
      alert('Please Select a Seller');
      return false;
    }
//        $.each(self.getBuyerTab(),function(key,val){
//          if(val["SHARE_PERCENT"] === 0)
//          {
//            return isEmpty=true;
//          }
//        });
    // Validation for Buyer Tab Percentage of water allocation
    if (!self.buyerPercentValidation())
    {
      return false;
    }
//        if(isEmpty)
//        {
//          alert("Please specify the amount of water to allocate. See Buyer Tab.");
//          return false;
//        }
//        
    // Validation for at least have one Previous Water Transfer when seller select "Yes"
    if (!self.preWTValidation()) {
      alert("Please add at least one Previous Water Transfer.");
      return false;
    }
    // Validation for Field Proposed Total Transfer Amount match all other Tabs Value
    if (!self.totalTransferValidation()) {
      alert("Proposed Total Transfer Amount (acre-feet) on the General Information page is not matching\n"
          + "the Water Transfer Amount from each water transfer type. Please check the Water Transfer\n"
          + "Amount from each water transfer type and ensure the total matches the Proposed Total Transfer\n"
          + "Amount. You can also click the calculator icon by the Proposed Total Transfer Amount to\n"
          + "automatically sum the Water Transfer Amount from each water transfer type.");
      return false;
    }
    // Validation for at least have one Water Rights
    if (!self.waterRightsValidation()) {
      alert("Please add at least one Water Rights Type.");
      return false;
    }
    // Validation fields in createForm
    if (!self.validTabs("SUBMIT")) {
      alert("Please check for errors ...");
      return false;
    }
    
    // If there is CI Tab, then validate this part
    var cicheckbox = self.baseInfoTab.find("#cropidling-check");
    if (cicheckbox.prop('checked')) {
      if(!self.ciValidation()){
        return false;
      }
    }

    // If there is GW Tab, then validate this part
    var gwcheckbox = self.baseInfoTab.find("#groundwater-check");
    if (gwcheckbox.prop('checked')) {
      if(!self.gwValidation()){
        return false;
      }
    }

    self.msg = "Are you sure you want to submit the proposal?";
    return true;
  };

  self.buyerPercentValidation = function ()
  {
    var isValid = true, percent = 0;
    $.each(self.getBuyerTab(), function (key, val) {
//      if (val["SHARE_PERCENT"] === 0)
//      {
//        return isValid = false;
//      }
      if ($.isNumeric(val['SHARE_PERCENT'])) {
        percent += val["SHARE_PERCENT"];
      }
    });
//    if (!isValid)
//    {
//      alert("Please specify the amount of water to allocate. See Buyer Tab.");
//      return false;
//    }
    if (percent > 100)
    {
      alert("The total water distribution cannot exceed over 100%");
      return false;
    }
    return isValid;
  };
  
  self.preWTValidation = function () {
    var isValid = false;
    self.hasPreTrans = self.baseInfoTab.find("input[name=hasPreTrans]:checked");
//    alert(self.hasPreTrans.val());
    if ("1" === self.hasPreTrans.val()){
      // Check if table exist first
      self.preWTTableRow = self.baseInfoTab.find(".pre_transfer_table tbody tr");
      if (typeof self.preWTTableRow.html() !== 'undefined') {
        isValid = false;
      }
//      alert(self.preWTTableRow.html());
      // Check there is at least one row
      self.preWTTableRow.each(function () {
        if ($(this).find("input:hidden[name=wtPreTransferId]").val() !== "") {
          isValid = true;
        }
      });
    } else {
      return true;
    }   
    
    return isValid;
  };
  
  self.totalTransferValidation = function () {
    var isValid = false;
    
    self.proTransQua = self.baseInfoTab.find("#proTransQua");
    var totalTransferVal = self.proTransQua.val();
    var calTotalTransferVal = self.totalTransferValue();
//    alert(totalTransferVal);
//    alert(calTotalTransferVal);
    if (totalTransferVal === calTotalTransferVal){
      isValid = true;
    }
    
    return isValid;
  };

  self.waterRightsValidation = function () {
    var isValid = false;

    // Check when loading proposal
    self.waterRightsSize = self.sellerTab.find("#waterRightsSize").val();
    if (self.waterRightsSize !== "" && parseInt(self.waterRightsSize) > 0) {
      isValid = true;
    }

    // Check in Water Rights Type and Transfer Information Section
    self.waterRightsTableRow = self.baseInfoTab.find(".water_rights_table tbody tr");
    if (typeof self.waterRightsTableRow.html() !== 'undefined') {
      isValid = false;
    }
    self.waterRightsTableRow.each(function () {
      if ($(this).find("input:hidden[name=wtWaterRightsId]").val() !== "") {
        isValid = true;
      }
    });

    return isValid;
  };
  
  self.ciValidation = function () {
    var isValid = false;
    self.waterTransAmount = self.cropidlingTab.find("input#waterTransQuaCI");
    self.ciMonthlyTable = self.cropidlingTab.find("#ci-monthly-table");
    self.totalTw = self.ciMonthlyTable.find("#totalTw");
    var waterTransAmountVal = self.waterTransAmount.val().replace(/[ ,]+/g, "");
//    alert(waterTransAmountVal);
    var totalTwVal = self.totalTw.text().replace(/[ ,]+/g, "");
//    alert(totalTwVal);
    
    if (waterTransAmountVal === totalTwVal){
      isValid = true;
    } else {
      alert("Water Transfer Amount does not match the monthly total");
    }
    
    return isValid;
  };

  self.gwValidation = function () {
    var isValid = false;
    var depletionVal = self.groundwaterTab.find("#streamDepletion").val().replace(/[ ,]+/g, "");
    var netTransWaterVal = self.groundwaterTab.find("#netTransWater").val().replace(/[ ,]+/g, "");
    var MTdepletionVal = self.groundwaterTab.find("#streamDepletionTotal").val().replace(/[ ,]+/g, "");
    var MTnetTransWaterVal = self.groundwaterTab.find("#netTransWaterTotal").val().replace(/[ ,]+/g, "");

    if (depletionVal === MTdepletionVal && netTransWaterVal === MTnetTransWaterVal) {
      isValid = true;
      return isValid;
    } else if (depletionVal !== MTdepletionVal) {
      alert("Field Streamflow Depletion does not match the monthly total");
    } else if (netTransWaterVal !== MTnetTransWaterVal) {
      alert("Field Net Transfer Water does not match the monthly total");
    }

    return isValid;
  };
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Initial Listeners">
  self.initListeners = function ()
  {
//    $(window).bind('resize', self.initSideMenu);
    $(window).unbind('resize').bind('resize',self.initSideMenu);
    $(window).unbind('scroll').bind('scroll',self.initSideMenu);
    self.nextBtn.unbind('click').bind("click", self.initNextTab);
    self.tabHeader.unbind('click').bind("click",self.headerToggle);
    self.addSellerBtn.unbind('click').bind("click", self.toggleAgencyComboxbox);
    self.addBuyerBtn.unbind('click').bind("click", self.toggleAgencyComboxbox);
    self.addAgencyBtn.unbind('click').bind("click", self.initNewAgency);
    self.proposalType.unbind('click').bind("click", self.checkBoxHandler);
//    self.faCheckbox.unbind('click').bind("click", self.facilitesCheckboxHandler);
    self.preTransferAddBtn.unbind('click').bind("click",self.addPreTransfer);
    self.waterRightsAddBtn.unbind('click').bind("click",self.addWaterRight);

    self.saveBtn.unbind('click').bind("click", self.initSaveProposal);
    self.saveProcessBtn.unbind('click').bind("click", self.initSaveProcess);
    self.submitBtn.unbind('click').bind("click", self.initSubmit);
    self.approveBtn.unbind('click').bind("click", self.initApprove);
    self.suspendBtn.unbind('click').bind("click", self.initSuspend);
    self.completeBtn.unbind('click').bind("click", self.initComplete);
    self.trackBtn.unbind('click').bind("click", self.initTrack);

    self.requestBt.unbind('click').bind("click", self.mileStones);
    self.changeLogBtn.unbind('click').bind("click", self.initChangeLog);
    self.viewChangeLogLink.unbind('click').bind('click', self.showChangeLog);
    self.proposalPDFPreview.unbind('click').bind("click", self.initPDFRenderer);
    self.proposalPDFLnk.unbind('click').bind("click", self.initPDFReports);
  };

  self.afterLoadListeners = function ()
  {
    self.agencyHeaderCt.unbind('click').bind("click", self.headerToggle);
    self.newContactBtn.unbind('click').bind("click", self.addNewContact);
    self.editIcon.unbind('click').bind("click", self.editContact);
    self.deleteIcon.unbind('click').bind("click", self.deleteContact);
    self.accessIcon.unbind('click').bind("click", self.accessContact);
    self.agencyType.unbind('click').bind("change", self.editAgency);

    self.agencyHeaderCt.find("label").on("click", function (e) {
      e.stopPropagation();
    });
    self.agencyHeaderCt.find("input").on("click", function (e) {
      e.stopPropagation();
    });
    self.agencyHeaderCt.find("img.remove_icon_ct").on("click", function (e) {
      e.stopPropagation();
    });
    self.addSellerBtn.hide();
    if (self.sellerPanel.find("h2").length === 0)
    {
      self.addSellerBtn.show();
    }
//    self.newContactBtn.show();
//    self.editTd.show();
//    self.deleteTd.show();
//    self.initDisabledProposalForm();
  };
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Listener Functions">
  self.initNextTab = function ()
  {
    var index = self.proposalCt.tabs('option', 'active');
    if (index < 2) {
      self.proposalCt.tabs("option", "active", index + 1);
    } else {
      alert("You need fill more information by select type of transfer.");
    }
  };

  self.headerToggle = function () {
    if ($(this).hasClass("isExpand"))
    {
      $(this).removeClass("isExpand");
      $(this).find("img").addClass("img_rotate");
//      $(this).find("img:not(.edit_icon)").addClass("img_rotate");
    }
    else
    {
      $(this).addClass("isExpand");
      $(this).find("img").removeClass("img_rotate");
//      $(this).find("img:not(.edit_icon)").removeClass("img_rotate");
    }
    $(this).next().slideToggle("fast");
  };

  self.toggleAgencyComboxbox = function ()
  {
    $(this).next().fadeToggle(1).removeClass("hidden");
    self.toggleEditAddAgencyCt.call(this);
  };

  self.toggleEditAddAgencyCt = function ()
  {
    $(this).next().next().fadeToggle(1).removeClass("hidden");
  };

  self.initNewAgency = function ()
  {
    self.addAgencyDialog = $("<div>");
    self.agencyList = $(this).parent().parent().find("select");
    $.ajax({
      url: window['SERVER_ROOT'] + '/proposal/getNewAgencyForm'
      , type: 'POST'
      , cache: false
      , success: function (data, status, jqxhr) {
        self.addAgencyDialog.append(data);
        self.addAgencyDialog.dialog({
          title: 'New Agency'
          , width: 500
          , modal: true
          , height: 200
          , buttons: [{
              text: 'Add'
              , click: function () {
                $.ajax({
                  url: window['SERVER_ROOT'] + '/proposal/addNewAgency'
                  , type: 'POST'
                  , data: {agencyFullName: $(this).find("input[name=agencyFullName]").val()}
                  , cache: false
                  , success: function (data, status, jqxhr) {
                    if (data === "") {
                      alert("Agency name is duplicate.");
                    } else {
                      alert("New Agency Added.");
                      var jsonData = JSON.parse(data);
                      self.agencyList.append('<option value="' + jsonData['wtAgencyId'] + '">' + jsonData['agencyFullName'] + '</option>');
                    }
                    self.addAgencyDialog.dialog('close');
                  }
                });
              }
            }, {
              text: 'Cancel'
              , click: function () {
                $(this).dialog('close');
              }
            }]
        });
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.checkBoxHandler = function ()
  {
    self.proposalTypeSelected();
    var checkbox = $(this), isChecked = false;
    self.currentTab = $("#" + $(this).attr("tabid"));
    if (this.checked)
    {
      self.currentTab.show();
//      self.calProTransferQua();
      self.initDisabledBaseInfo(false); 
      var maskMsg = "The system is creating a new application for you. Please wait...";
      var afterSaveMsg = "";
      if (self.getTransId())
      {
        maskMsg = "The system is updating your proposal for you. Please wait...";
      }
      self.initSave(maskMsg,afterSaveMsg);
      
      // Remove alert message
//      $(window).unbind('beforeunload');
      
//      var data = self.buildData();
//      var maskString = "The system is creating a new application for you. Please wait...";
//      if (self.getTransId())
//      {
//        maskString = "The system is updating your proposal for you. Please wait...";
//      }
//      $("#content-ct").mask(maskString);
//      var url = window.SERVER_ROOT + "/proposal/saveProposalJson";
//      $.ajax({
//        type: 'POST'
//        , data: {jsondata: JSON.stringify(data)}
//        , url: url
//        , dataType: 'json'
//        , cache: false
//        , success: function (data, status, jqxhr) {
//          try {
//            if (!data.success) {
//              throw data.error || "Unable to remove target storage.";
//            }
//            $("#content-ct").unmask();
//            if (!self.createForm.find("#wtTransId").val())
//            {
//              window.location = window.SERVER_ROOT + "/proposal/edit/" + data['wtTransId'];
//            }
//          } catch (e) {
//            $("#content-ct").unmask();
//            if (data.callback) {
//              var callback = eval(data.callback);
//              if (typeof callback === "function") {
//                callback.call();
//              }
//            } else if (e) {
//              alert(e);
//            }
//          }
//        }
//        , error: function (xhr, errorType, exception) {
//          if (xhr.status === 403) //session ends
//          {
//            location = window.SERVER_ROOT;
//          }
//        }
//      });
    }
    else
    {
      self.tabsCt = $("#" + $(this).attr("tabid"));
      self.tabsContentCt = $(self.tabsCt.find("a").attr("href"));
      var hasVal = false;
      $.each(self.tabsContentCt.find("input,textarea").not(".attachButton"), function () {
        if ($(this).val())
        {
          return hasVal = true;
        }
      });
      if (hasVal)
      {
        var msg = "Are you sure you want to remove " + $(this).attr("txtlabel") + "?";
        self.checkboxDirtyFlag();
        self.proposalValidation(msg, function (bool) {
          if (bool)
          {
            self.currentTab.hide();
            self.initSave("Removing......please wait for complete.","");
//            self.calProTransferQua();
          }
          else
          {
            self.currentTab.show();
            self.initDisabledBaseInfo(false);
            checkbox.prop('checked', true);
            checkbox.val("1");
          }
        });
      } else {
        self.currentTab.hide();
      }
      self.proposalTypeCt.find("input").each(function () {
        if (this.checked)
        {
          isChecked = true;
        }
      });
      if (!isChecked)
      {
        self.initDisabledBaseInfo(true);
      }
    }
    
  };
  
  self.calProTransferQua = function () {  
    self.proTransQua.val(self.totalTransferValue());
  };
    
  self.totalTransferValue = function () {
    self.waterTransQuaCI = self.cropidlingTab.find("#waterTransQuaCI");
    self.waterTransQuaRV = self.reservoirTab.find("#waterTransQuaRV");
    self.netTransWater = self.groundwaterTab.find("#netTransWater");
    self.proTransQua = self.baseInfoTab.find("#proTransQua");
    var cicheckbox = self.baseInfoTab.find("#cropidling-check");
    var gwcheckbox = self.baseInfoTab.find("#groundwater-check");
    var rvcheckbox = self.baseInfoTab.find("#reservoir-check");    
    var waterTransQuaCI = self.waterTransQuaCI.val().replace(/,/g, '');
    var waterTransQuaGW = self.netTransWater.val().replace(/,/g, '');
    var waterTransQuaRV = self.waterTransQuaRV.val().replace(/,/g, '');
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
    
    return totalTransQua.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
  };

  self.mileStones = function()
  {
    window.open(window['SERVER_ROOT']+"/proposal/mileStones",'_blank');
  };
  // </editor-fold>

  // <editor-fold desc="Previous Tansfer Code">
 self.addPreTransfer = function () {
//    alert("Add Pre Transfer");
    if (self.getTransId() === "")
    {
      alert("Please save the proposal before adding the Previous Transfer");
      return false;
    }
    self.preTransferTable = $(this).parent().prev().find("table.pre_transfer_table");
    self.preTransferTable.removeClass("hidden").show();
    self.hiddenRow = self.preTransferTable.find("tbody tr.hidden");
    var row = $("<tr/>").html(self.hiddenRow.html());
    self.preTransferTable.find("tbody tr:last").after(row);
    self.preTransferItems();
  };

  self.preTransferItems = function () {
    self.ptTable = $("table.pre_transfer_table");
    self.typeCheckbox = self.ptTable.find(".transType");
    self.ptSaveBtn = self.ptTable.find("img.save-icon");
    self.ptEditBtn = self.ptSaveBtn.parent().next().find("img.edit-icon");
    self.ptRemoveBtn = self.ptEditBtn.parent().next().find("img.delete-icon");
    self.ptAddBtn = self.ptTable.parent().parent().find("#pre_transfer_addBtn");
    self.ptTableCheckRows();
    self.preTransferListeners();
  };

  self.preTransferListeners = function () {
    self.typeCheckbox.unbind("click", self.ptCheckboxHandler);
    self.typeCheckbox.on("click", self.ptCheckboxHandler);
    self.ptSaveBtn.unbind("click", self.ptSaveHandler);
    self.ptSaveBtn.on("click", self.ptSaveHandler);
    self.ptEditBtn.unbind("click", self.ptEditHandler);
    self.ptEditBtn.on("click", self.ptEditHandler);
    self.ptRemoveBtn.unbind("click", self.ptRemoveHandler);
    self.ptRemoveBtn.on("click", self.ptRemoveHandler);
  };

  self.ptCheckboxHandler = function () {
    if (this.checked) {
      $(this).val("1");
    } else {
      $(this).val("0");
    }
  };

  self.ptSaveHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.swpaonum = self.row.find("input[name=swpaoContractNum]");
    self.recomnum = self.row.find('input[name=recomNum]');
    self.isci = self.row.find('input[name=isTypeCi]');
    self.isrv = self.row.find('input[name=isTypeRv]');
    self.isgw = self.row.find('input[name=isTypeGw]');
    if (!self.ptTableValidation(self.swpaonum, self.recomnum)) {
      return false;
    }
    self.swpaonum.attr("disabled", true);
    self.recomnum.attr("disabled", true);
    self.isci.attr("disabled", true);
    self.isrv.attr("disabled", true);
    self.isgw.attr("disabled", true);
    $(this).hide();
    self.ptId = self.row.find("input[name=wtPreTransferId]");
    self.baseInfoTab.mask("Saving data...");
    $.ajax({
      url: window["SERVER_ROOT"] + "/proposal/savePreTransfer"
      , data: {swpaoContractNum: self.swpaonum.val(), recomNum: self.recomnum.val(), isTypeCi: self.isci.val(),
        isTypeRv: self.isrv.val(), isTypeGw: self.isgw.val(), wtTransId: self.getTransId(), wtPreTransferId: self.ptId.val()}
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr)
      {
        try {
          if (!data.success) {
            throw data.error || "Unable to save this transfer.";
          }
          self.baseInfoTab.unmask();
          alert("Saved Successfully!");
          self.ptId.val(data.wtPreTransferId);
        } catch (e) {
          self.baseInfoTab.unmask();
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
          alert("Saving Failed");
        }
      }
    });
  };

  self.ptEditHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.swpaonum = self.row.find("input[name=swpaoContractNum]");
    self.recomnum = self.row.find('input[name=recomNum]');
    self.isci = self.row.find('input[name=isTypeCi]');
    self.isrv = self.row.find('input[name=isTypeRv]');
    self.isgw = self.row.find('input[name=isTypeGw]');
    self.saveBt = self.row.find(".save-icon");
    self.swpaonum.attr("disabled", false);
    self.recomnum.attr("disabled", false);
    self.isci.attr("disabled", false);
    self.isrv.attr("disabled", false);
    self.isgw.attr("disabled", false);
    self.saveBt.removeClass("hidden").show();
    alert("You can now edit the textfield. Please be sure to save when done.");
  };

  self.ptRemoveHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.ptId = self.row.find("input[name=wtPreTransferId]");
    if (self.ptId.val() === "")
    {
      self.row.remove();
      self.ptTableCheckRows();
      return false;
    }
    self.msg = "Are you sure you want to remove this Transfer?";
    var data = {wtPreTransferId: self.ptId.val()};
    self.proposalValidation(self.msg, function (bool) {
      if (bool) {
        $.ajax({
          url: window["SERVER_ROOT"] + "/proposal/removePreTransfer"
          , data: data
          , cache: false
          , dataType: 'json'
          , success: function (data, status, jqxhr)
          {
            try {
              if (!data.success) {
                throw data.error || "Unable to remove this transfer.";
              }
              self.row.remove();
              self.ptTableCheckRows();
              alert("Removed Successfully!");
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

  self.ptTableCheckRows = function () {
    self.ptRow = self.ptTable.find("tbody tr");
    if (self.ptRow.length === 1)
    {
      self.ptTable.removeClass('hidden').hide();
    }
    if (self.ptRow.length >= 6)
    {
      self.ptAddBtn.removeClass('hidden').hide();
    } else {
      self.ptAddBtn.removeClass('hidden').show();
    }
  };

  self.ptTableValidation = function (swpaonum, recomnum) {
    if (swpaonum.val() === "" && recomnum.val() === "") {
      alert("Please filled in SWAPO Contract No or Recommendation No.");
      return false;
    }
    return true;
  };
  // </editor-fold>

  // <editor-fold desc="Water Rights Code">
  self.addWaterRight = function () {
    if (self.getTransId() === "")
    {
      alert("Please save the proposal before adding the Water Rights Type");
      return false;
    }
    self.waterRightTable = $(this).parent().prev().find("table.water_rights_table");
    self.waterRightTable.removeClass("hidden");
    self.hiddenRow = self.waterRightTable.find("tbody tr.hidden");
    var row = $("<tr/>").html(self.hiddenRow.html());
    self.waterRightTable.find("tbody tr:last").after(row);
    self.waterRightItems();
  };

  self.waterRightItems = function () {
    self.wrTable = $("table.water_rights_table");
    self.wrCombobox = self.wrTable.find("select");
    self.wrSaveBtn = self.wrTable.find("img.save-icon");
    self.wrEditBtn = self.wrSaveBtn.parent().next().find("img.edit-icon");
    self.wrRemoveBtn = self.wrEditBtn.parent().next().find("img.delete-icon");
    self.waterRightListeners();
    self.numberHandler();
  };

  self.waterRightListeners = function () {
    self.wrSaveBtn.unbind("click", self.wrSaveHandler);
    self.wrSaveBtn.on("click", self.wrSaveHandler);
    self.wrEditBtn.unbind("click", self.wrEditHandler);
    self.wrEditBtn.on("click", self.wrEditHandler);
    self.wrRemoveBtn.unbind("click", self.wrRemoveHandler);
    self.wrRemoveBtn.on("click", self.wrRemoveHandler);
    self.wrCombobox.unbind("change", self.wrSelectHandler);
    self.wrCombobox.on("change", self.wrSelectHandler);
  };

  self.wrSelectHandler = function () {
    self.wrLabel = $(this).parent().next().find("label");
    self.wrLabel.html($(this).val() + " #");
  };

  self.wrCheckEmptyTable = function () {
    self.wrRow = self.wrTable.find("tbody tr");
    if (self.wrRow.length === 1)
    {
      self.wrTable.addClass("hidden");
    }
  };

  self.wrSaveHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.type = self.row.find("select");
    self.wrnum = self.row.find('input[name=waterRightsNum]');
    self.volume = self.row.find('input[name=proposedTransVol]');
    if (!self.type.val()) {
      alert("Choose one of the water rights type.");
      return false;
    }
    var icon = this;
    self.wrId = self.row.find("input[name=wtWaterRightsId]");
    self.baseInfoTab.mask("Saving water rights...");
    $.ajax({
      url: window["SERVER_ROOT"] + "/proposal/saveWaterRight"
      , data: {waterRightsType: self.type.val(), waterRightsNum: self.wrnum.val(), proposedTransVol: self.volume.val(), wtTransId: self.getTransId(), wtWaterRightsId: self.wrId.val()}
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr)
      {
        try {
          if (!data.success) {
            throw data.error || "Unable to save water right.";
          }
          self.baseInfoTab.unmask();
          self.type.attr("disabled", true);
          self.wrnum.attr("disabled", true);
          self.volume.attr("disabled", true);
          $(icon).hide();
          alert("Saved Successfully!");
          self.wrId.val(data.wtWaterRightsId);
        } catch (e) {
          self.baseInfoTab.unmask();
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
          alert("Saving Failed");
        }
      }
    });
  };

  self.wrEditHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.type = self.row.find("select");
    self.wrnum = self.row.find('input[name=waterRightsNum]');
    self.volume = self.row.find('input[name=proposedTransVol]');
    self.saveBt = self.row.find(".save-icon");
    self.type.attr("disabled", false);
    self.wrnum.attr("disabled", false);
    self.volume.attr("disabled", false);
    self.saveBt.removeClass("hidden").show();
    alert("You can now edit the textfield. Please be sure to save when done.");
  };

  self.wrRemoveHandler = function () {
    if ($(this).attr("disabled") === "disabled")
    {
      return false;
    }
    self.row = $(this).parent().parent();
    self.combo = self.row.find("select");
    self.type = self.row.find('input[name=waterRightsNum]');
    self.wrId = self.row.find("input[name=wtWaterRightsId]");
    self.volume = self.row.find('input[name=proposedTransVol]');
    if (self.wrId.val() === "")
    {
      self.row.remove();
      self.wrCheckEmptyTable();
      return false;
    }
    self.msg = "Are you sure you want to remove this Water Rights?";
//        var data = {waterRightsType:self.combo.val(),waterRightsNum:self.type.val(),proposedTransVol:self.volume.val(),wtTransId:self.getTransId(),wtWaterRightsId:self.wrId.val()};
    var data = {wtWaterRightsId: self.wrId.val()};
    self.proposalValidation(self.msg, function (bool) {
      if (bool) {
        $.ajax({
          url: window["SERVER_ROOT"] + "/proposal/removeWaterRight"
          , data: data
          , cache: false
          , dataType: 'json'
          , success: function (data, status, jqxhr)
          {
            try {
              if (!data.success) {
                throw data.error || "Unable to remove water right.";
              }
              self.row.remove();
              self.wrCheckEmptyTable();
              alert("Removed Successfully!!!");
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

  // <editor-fold defaultstate="collapsed" desc="Listener Process Functions">
  self.initSaveProposal = function(){
    var maskMsg = "Saving...please wait for complete.";
    var afterSaveMsg = "You have saved the application. Saving does not submit the application for review.";
    self.initSave(maskMsg, afterSaveMsg);
  };
  self.initSave = function (maskmsg, afterSaveMsg)
  {
    var data = self.buildData();
    var maskMessage = "Saving...";
    if(maskmsg !== ""){
      maskMessage = maskmsg;
    }

    // Only save modified data or new enter data
    if (typeof self.accessIcon !== 'undefined') {
      data['accessContact'] = self.getAllAccessContact();
    }
    self.contentBody.mask(maskMessage);
    if (!jQuery.isEmptyObject(data))
    {
      var url = window.SERVER_ROOT + "/proposal/saveProposalJson"; //saveProposalJson
      $.ajax({
        type: 'POST'
        , data: {jsondata: JSON.stringify(data)}
        , url: url
        , cache: false
        , dataType: "json"
        , success: function (jsonData, status, jqxhr) {
          self.contentBody.unmask("Proposal Saved!");
          try {
            if (!jsonData.success) {
              throw jsonData.error || "The system cannot save the proposal!";
            }
            if (afterSaveMsg !== ""){
              alert(afterSaveMsg);
            }
            if (!self.createForm.find("#wtTransId").val()) {
//              $(window).unbind('beforeunload');
              window.location = window.SERVER_ROOT + "/proposal/edit/" + jsonData['wtTransId'];
//              $(window).bind("beforeunload", function(){ return(false); });
            }
          }
          catch (e) {
            if (jsonData.callback) {
              var callback = eval(jsonData.callback);
              if (typeof callback === "function") {
                callback.call();
              }
            }
            else if (e) {
              alert(e);
            }
          }
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) { //session ends
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };

  self.initSubmit = function ()
  {
    var data = self.buildData();
//    var msg = "Are you sure you want to submit the proposal?";
    var msg="Information on the proposed water transfer that has been officially \n\
             submitted to the Department of Water Resources will become a public record and subject to disclosure.";
    var title = "Submit Confirmation";
    if (!self.submitValidation()) {
      return false;
    }

    // wtTransId has to be send
    var wtTransId = self.baseInfoTab.find("#wtTransId").val();
    if (wtTransId === "") {
      alert("There is no proposal to submit.");
      return false;
    }

    self.submitMessage(title, msg, function (bool) {
      if (!bool)
      {
        return false;
      }

      // Remove alert message
//      $(window).unbind('beforeunload');
      self.contentBody.mask("Submitting...please wait");
      var url = window.SERVER_ROOT + "/proposal/submit?wtTransId=" + wtTransId; //submitProposal
      $.ajax({
        type: 'POST'
        , data: {jsondata: JSON.stringify(data)}
        , url: url
        , cache: false
        , dataType: 'json'
        , success: function (data, status, jqxhr) {
          try {
            if (!data.success) {
              throw data.error || "Unable to submit the proposal.";
            }
            self.contentBody.unmask();
            alert("Proposal Submitted!");
            var url = window.SERVER_ROOT + "/proposal/?moduleType=view";
//            $(window).unbind('beforeunload');
            window.location.assign(url);
          } catch (e) {
            self.contentBody.unmask();
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
        }
      });
    });
  };

  self.initSaveProcess = function () {
    var data = self.processForm.serialize();
//  var url =window.SERVER_ROOT + "/proposal/saveProposalProcess?wtTransId="+wtTransId;
    var url = window.SERVER_ROOT + "/proposal/saveProposalProcess";
    $.ajax({
      type: "POST"
      , url: url
      , data: data
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr) {
        try {
          if (!data.success) {
            throw data.error || "Unable to save proposal process.";
          }
          alert("Process Saved!");
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
        if (xhr.status === 403)
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.initApprove = function(){
    var wtTransId = self.createForm.find("#wtTransId").val();
    var url = window.SERVER_ROOT + "/proposal/approve?wtTransId=" + wtTransId;
    var msg = "Are you sure you want to approve this proposal?";
    self.proposalValidation(msg, function (bool) {
      if (bool) {
        $.ajax({
          type: "POST"
          , url: url
          , dataType: 'json'
          , cache: false
          , success: function (data, status, jqxhr) {
            try {
              if (!data.success) {
                throw data.error || "Unable to update the proposal.";
              }
              alert("Proposal Approved!");
              var url = window.SERVER_ROOT + "/proposal/?moduleType=approve";
              window.location.assign(url);
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
            if (xhr.status === 403)
            {
              location = window.SERVER_ROOT;
            }
          }
        });
      }
    });
  };

  self.initSuspend = function(){
    var wtTransId = self.createForm.find("#wtTransId").val();
    var url = window.SERVER_ROOT + "/proposal/suspend?wtTransId=" + wtTransId;
    var msg = "Are you sure you want to suspend this proposal?";
    self.proposalValidation(msg, function (bool) {
      if (bool) {
        $.ajax({
          type: "POST"
          , url: url
          , dataType: 'json'
          , cache: false
          , success: function (data, status, jqxhr) {
            try {
              if (!data.success) {
                throw data.error || "Unable to update the proposal.";
              }
              alert("Proposal Suspend!");
              var url = window.SERVER_ROOT + "/proposal/?moduleType=suspend";
              window.location.assign(url);
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
            if (xhr.status === 403)
            {
              location = window.SERVER_ROOT;
            }
          }
        });
      }
    });
  };

  self.initComplete = function(){
    var wtTransId = self.createForm.find("#wtTransId").val();
    var url = window.SERVER_ROOT + "/proposal/complete?wtTransId=" + wtTransId;
    var msg = "Are you sure this proposal is completed?";
    self.proposalValidation(msg, function (bool) {
      if (bool) {
        $.ajax({
          type: "POST"
          , url: url
          , dataType: 'json'
          , cache: false
          , success: function (data, status, jqxhr) {
            try {
              if (!data.success) {
                throw data.error || "Unable to update the proposal.";
              }
              alert("Proposal is complete!");
              var url = window.SERVER_ROOT + "/proposal/?moduleType=review";
              window.location.assign(url);
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
            if (xhr.status === 403)
            {
              location = window.SERVER_ROOT;
            }
          }
        });
      }
    });
  };

  self.initTrack=function(){
    var wtTransId = self.createForm.find("#wtTransId").val();
    var url = window.SERVER_ROOT + "/proposal/track?wtTransId=" + wtTransId;
    $.ajax({
      type: "POST"
      , url: url
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr) {
        try {
          if (!data.success) {
            throw data.error || "Unable to update the proposal.";
          }
          alert("Proposal is under tracking!");
          var url = window.SERVER_ROOT + "/proposal/?moduleType=track";
          window.location.assign(url);
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
      }
    });
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Listener Reports and Log">
  self.initPDFRenderer = function () {
    self.transId = $(this).attr("wtTransId");
    self.userId = $(this).attr("userId");
    var msg = "In addition to opening the PDF, would you like to save a copy of the PDF to the database?";
    var title = "Save PDF Confirmation";
    self.userValidation(title, msg, function (bool) {
      self.propPDFForm.find("input[name=track]").val(bool);
      self.propPDFForm.submit();
    });
  };

  self.initPDFReport = function () {
    self.contentBody.mask("Generating PDF Report, please wait...");
    var xhr = new XMLHttpRequest();
    xhr.open('GET', self.propPDFForm.attr("action"), true);
    xhr.responseType = 'blob';
    xhr.onload = function (e) {
      self.contentBody.unmask();
      if (this.status === 200) {
        var blob = new Blob([this.response], {type: 'application/pdf'});
        var url = window.URL.createObjectURL(blob);
        var aTag = $("<a/>", {
          href: url
          , download: "detailPDF.pdf"
          , html: 'detailPDF.pdf'
        });
        aTag.appendTo($(".button_ct"));
      }
    };
    xhr.send();
  };

  self.initPDFReports = function () {
    self.form = $("<form method='post' id='PDFReportsform' action='#'>");
    var wtTransId = $(this).attr("wtTransId");
    var reportType = $(this).attr("reportType");
    var data = {reportType: reportType};
    var title = "Groundwater PDF Reports";
    if (reportType === "PR") {
      title = "Proposal PDF Reports";
      data = {
        transYear: self.proposalHeader.attr("transyear")
        , sellerName: self.proposalHeader.attr("sellername")
        , reportType: reportType
      };
    }
    var url = window.SERVER_ROOT + "/proposal/PDFReport/" + wtTransId;
    self.contentBody.mask("Loading PDF Report...");
    $.ajax({
      type: "POST"
      , url: url
      , data: data
      , cache: false
      , scope: this
      , success: function (data, status, jqxhr) {
        self.form.append(data);
        self.form.dialog({
          title: title
          , width: 1000
          , height: 400
          , resizable: true
          , modal: true
        }).dialog('open');
        self.contentBody.unmask();
      }
      , error: function (xhr, errorType, exception) {
        self.contentBody.unmask();
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.initChangeLog = function ()
  {
    var msg="Information on the proposed water transfer that has been officially \n\
              submitted to the Department of Water Resources will become a public record and subject to disclosure.";
    var title = "Submit Confirmation";
    var wtTransId = self.baseInfoTab.find("#wtTransId").val();
    if (!self.submitValidation()) {
      return false;
    }
      
    self.submitMessage(title, msg, function (bool) {
      if (!bool){ return false; }    
      
      self.changeLog = self.changeLogTab.find(".change_log");
      self.changeLogTable = self.changeLog.find("table tbody");
      self.changeLogDialog = $('<div><textarea cols="50" rows="8" id="changeLog" name="changeLog" placeholder="Summarize the changes to the application since the last submittal" style="width:99%; height: 99%;"></textarea></div>');

      self.changeLogDialog.dialog({
        title: 'What changes have you submit?'
        , width: 600
        , modal: true
        , height: 400
        , buttons: [{
            text: 'Submit'
            , click: function () {
              var changeLog = $("textarea#changeLog").val();
              self.changeLogDialog.parent().mask("Resubmitting the proposal...");
              $.ajax({
                url: window.SERVER_ROOT + '/proposal/resubmit'
                , type: 'POST'
                , data: {wtTransId: wtTransId, changeLog: changeLog}
                , cache: false
                , dataType: 'json'
                , success: function (response, status, jqxhr) {
                  try {
                    if (!response.success) {
                      throw response.error || "Unable to set the permission.";
                    }
                    self.changeLogDialog.parent().unmask();
//                    var jsonData = response.data;
//                    var formattedDate = $.datepicker.formatDate('mm/dd/yy', new Date());
//                    var htmlRow = "<tr><td>" + formattedDate + "</td>";
//                    htmlRow += "<td>" + jsonData['changeUser'] + "</td>";
//                    htmlRow += "<td>" + jsonData['changeLog'] + "</td></tr>";
//                    alert("Change Log Saved.");
                    self.changeLogDialog.dialog("destroy").remove();
//                    self.changeLogTable.append(htmlRow);
                    alert("Proposal Submitted!");
                    var url = window.SERVER_ROOT + "/proposal/?moduleType=view";
        //            $(window).unbind('beforeunload');
                    window.location.assign(url);
                  } catch (e) {
                    self.changeLogDialog.parent().unmask();
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
                , error: function (xhr, errorType, exception) {
                  if (xhr.status === 403) //session ends
                  {
                    location = window.SERVER_ROOT;
                  }
                }
              });
            }
          }, {
            text: 'Cancel'
            , click: function () {
              $(this).dialog("destroy").remove();
            }
          }]
      });
    });
  };

  self.showChangeLog = function () {
    var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
    if (!wtTransId)
    {
      return false;
    }

    var successCallBack = function (data, status, jqxhr) {
      $("#content-ct").unmask();
      $('<div/>')
              .html(data)
              .dialog({
                title: 'Submit Log'
                , width: 1075
                , height: 500
              }).show();
    };

    self.ChangeLog(wtTransId, null, true, successCallBack);
  };

  self.ChangeLog = function (wtTransId, panel, force, successfn) {
    if (force || panel[0].id === "changelog_tab") //Log Page
    {
      var success = function (data, status, jqxhr) {
        $("#content-ct").unmask();
        self.changeLogTab.html(data);
      };
      if (typeof successfn === "function") {
        success = successfn;
      }

      $("#content-ct").mask("Loading...");
      $.ajax({
        type: "POST"
        , url: window.SERVER_ROOT + "/proposal/ChangeLog/" + wtTransId
        , cache: false
        , success: success
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    }
  };
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="After Listener Functions">
  self.editAgency = function (e)
  {
    var agencyType = $(this).val();
    var wtAgencyId = $(this).parent().find("input[name=wtAgencyId]").val();
    var data = {wtAgencyId: wtAgencyId, agencyType: agencyType};
    var url = window.SERVER_ROOT + "/proposal/editAgency";
    $.ajax({
      type: "POST"
      , url: url
      , data: data
      , cache: false
      , dataType: 'json'
      , success: function (data, status, jqxhr) {
        try {
          if (!data.success) {
            throw data.error || "Unable to edit agency.";
          }
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
    });
  };

  self.addNewContact = function ()
  {
    self.currentTableEl = $(this).prevAll(".agencyContactList");
    var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
    var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
//        alert($(this).parent().parent().parent().attr('id'));
    var agencyType = "Seller Agency";
    var tabName = $(this).parent().parent().parent().attr('id');
    if (tabName === "buyer_tab") {
      agencyType = "Buyer Agency";
    }
    // Buyers Representative no Agency
    if (typeof agencyid === 'undefined') {
      agencyType = "Buyers";
    }
    var agencyContact = {};
    agencyContact = {wtAgencyId: agencyid, wtTransId: wtTransId, agencyType: agencyType};
    new AgencyContactForm(agencyContact, function (data) {
      self.currentTableEl.parent().html(data);      
      self.afterLoadItems();
      self.afterLoadListeners();
//      self.newContactBtn.show();
//      self.editTd.show();
//      self.deleteTd.show();
    });
  };

  self.editContact = function ()
  {
    var contactid = $(this).parent().parent().parent().attr("contactid");
//    alert($(this).parent().parent().parent().html());
    self.currentTableEl = $(this).parent().parent().parent().parent().parent();
    var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
    var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
    var agencyType = "Seller Agency";
    var tabName = $(this).parent().parent().parent().parent().parent().parent().parent().parent().attr('id');
//    alert($(this).parent().parent().parent().parent().parent().parent().html());
    if (tabName === "buyer_tab") {
      agencyType = "Buyer Agency";
    }
    // Buyers Representative no Agency
    if (typeof agencyid === 'undefined') {
      agencyType = "Buyers";
    }

    var agencyContact = {};
//    alert(agencyType);
//    alert("Edit Contact");
    agencyContact = {wtContactId: contactid, wtAgencyId: agencyid, wtTransId: wtTransId, agencyType: agencyType};
    new AgencyContactForm(agencyContact, function (data) {
//      alert(data);
      self.currentTableEl.parent().html(data);      
      self.afterLoadItems();
      self.afterLoadListeners();
//      self.newContactBtn.show();
//      self.editTd.show();
//      self.deleteTd.show();
    });
  };

  self.deleteContact = function ()
  {
    var tableTr = $(this).parent().parent().parent(),
            msg = "Are you sure you want to remove this Contact?";
    self.currentTableEl = tableTr.parent().parent();
    var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
    var title = "Remove Confirmation";
    self.userValidation(title, msg, function (bool) {
      if (!bool)
      {
        return false;
      }
      var contactData = JSON.parse(tableTr.find("input[name=contactLookup]").val());
      contactData['isActive'] = 0;
      contactData['wtAgencyId'] = agencyid;
      self.contentBody.mask("Removing contact...");
      $.ajax({
        url: window["SERVER_ROOT"] + "/proposal/saveContact"
        , data: contactData
        , cache: false
        , success: function (data, status, jqxhr)
        {
          self.contentBody.unmask();
          tableTr.remove();
          alert("Removed Successfully!");
        }
        , error: function (xhr, errorType, exception) {
          self.contentBody.unmask();
          if (xhr.status === 403) //session ends
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

  self.accessContact = function () {
    var checkbox = this;
    var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
    var contactid = $(this).parent().parent().attr("contactid");
    var linkTrans = "";
    var agencyContact = {};
    linkTrans = "N";
    if (this.checked) {
      linkTrans = "Y";
    }
    agencyContact = {wtContactId: contactid, wtTransId: wtTransId, linkTrans: linkTrans};
    var url = window.SERVER_ROOT + "/proposal/contactAccess";
    if (wtTransId) {
//        $.post(url,agencyContact);
      $.ajax({
        url: url
        , data: agencyContact
        , cache: false
        , dataType: 'json'
        , type: 'post'
        , success: function (data, status, jqxhr)
        {
          try {
            if (!data.success) {
              throw data.error || "Unable to set the permission.";
            }
          } catch (e) {
            $(checkbox).prop("checked", !checkbox.checked);
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
            alert("Saving Failed");
          }
        }
      });
    }
  };

  self.initDisabledProposalForm = function ()
  {
    if (self.isReviewer)
    {
      self.isReviewer.find("input,button,textarea,select").attr("disabled", "disabled");
      self.isReviewer.find("img,label").off("click");
    }
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Gets and Util Functions">
  self.buildData=function(){
    var data = self.getParam();

    var wtTransId = self.createForm.find("#wtTransId").val();
    // Force to send wtTransId
    if (wtTransId !== null || wtTransId !== "") {
      data["wtTransId"] = wtTransId;
    }
    if (self.isDirty_seller)
    {
      data["sellerTab"] = self.getSellerTab();
    }
    if (self.isDirty_buyer)
    {
      data["buyerTab"] = self.getBuyerTab();
    }
    if (self.createForm.find(".isDirty").length === 0 && jQuery.isEmptyObject(data))
    {
      data = new Object();
    }
    if (!jQuery.isEmptyObject(data))
    {
      self.orgData = self.createForm.serializeArray();
      var checkboxFields = self.createForm.find("input:checkbox");
      $.each(self.orgData, function (index, obj) {
        data[obj.name] = obj.value;
      });
      checkboxFields.each(function () {
        if (!this.checked && $(this).hasClass("isDirty")) {
          data[this.name] = this.value;
        }
      });
      self.createForm.find("input").removeClass("isDirty");
      self.createForm.find("textarea").removeClass("isDirty");
    }
    data['countyId'] = self.getCountyIdList().toString();
    data['purposeId'] = self.getPurposeIdList().toString();

    return data;
  };

  self.getAllInputFields = function ()
  {
    var inputs = new Object();
    self.createForm.find("input").each(function (val, key) {
      inputs[$(this).attr("name")] = $(this).val();
    });
    self.createForm.find("textarea").each(function (val, key) {
      inputs[$(this).attr("name")] = $(this).val();
    });
    if (this.getBuyerTab().length > 0)
    {
      inputs['buyerTab'] = self.getBuyerTab();
    }
    if (this.getSellerTab().WT_AGENCY_ID > 0)
    {
      inputs['sellerTab'] = self.getSellerTab();
    }
    return inputs;
  };

  self.getSellerTab = function ()
  {
    var sellerAgencyObj = {};
    this.id = parseInt(self.sellerPanel.find(".headerCt").attr("agencyid"));
    if (this.id)
    {
      sellerAgencyObj = {WT_AGENCY_ID: this.id};
    }
    return sellerAgencyObj;
  };

  self.getBuyerTab = function ()
  {
    self.buyersContactTable = self.buyerTab.find("#buyersContactTable");
    var agencyIdCollection = [];
    $.each(self.buyerPanel.find(".headerCt"), function () {
      var percent = $(this).next().find("input[name=water_percent]").val();
      if (isNaN(percent)) {
        percent = 0;
      }
      agencyIdCollection.push({
        WT_AGENCY_ID: parseInt($(this).attr("agencyid"))
        ,SHARE_PERCENT: parseFloat(percent)
      });
    });

//    var buyersContactId = self.buyersContactTable.find("tbody tr:visible").attr("id");
//    agencyIdCollection.push({WT_CONTACT_ID:parseInt(buyersContactId)});
    self.buyersContactTable.find("tbody tr").each(function () {
      if (!$(this).hasClass("hidden")) {
        var buyersContactId = $(this).attr("id");
        agencyIdCollection.push({WT_CONTACT_ID: parseInt(buyersContactId)});
      }
    });

    return agencyIdCollection;
  };

  self.getAllAccessContact = function () {
    var agencyContactList = [];
    if (self.accessIcon === "" || self.accessIcon === null) {
      return "";
    }
    $.each(self.accessIcon, function () {
      if (this.checked) {
        agencyContactList.push({
          wtContactId: $(this).parent().parent().attr("contactid")
          , linkTrans: 'Y'
        });
      }
    });
    if (agencyContactList.length < 1)
    {
      return "";
    }
    return agencyContactList;
  };

  self.getCountyIdList = function () {
    self.county = self.baseInfoTab.find("input[name=countyId]");
    var result = [];
    $.each(self.county, function () {
      result.push($(this).val());
    });
    return result;
  };

  self.getParam = function () {
    return self.jsonData;
  };

  self.getPurposeIdList = function () {
    self.purposeRes = self.reservoirTab.find("input[name=purposeId]");
    var result = [];
    $.each(self.purposeRes, function () {
      result.push($(this).val());
    });
    return result;
  };

  self.getTransId = function ()
  {
    var id = "";
    var url = window.location.href.split("/");
    if ($.isNumeric(url[url.length - 1]))
    {
      id = url[url.length - 1];
    }
    return id;
  };
  
  self.getStatusName = function ()
  {
    self.statusName = self.sellerTab.find("input[name=statusName]");
    var statusName = self.statusName.val();
    return statusName;
  };

  self.getUrlParameter = function ()
  {
    return location.pathname.indexOf(window.SERVER_ROOT + "/proposal/edit");
  };

  self.phoneNumberFormat = function (phone)
  {
    return phone.replace(/(\d{3})(\d{3})(\d{4})/, "($1) $2-$3");
  };

  self.validateEmail = function (email)
  {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return email.match(re) ? true : false;
  };

  self.checkboxDirtyFlag = function ()
  {
    self.proposalTypeCt.find("input").on("change", function () {
      self.jsonData[$(this).attr("name")] = $(this).val();
      $(this).addClass("isDirty");
    });
  };

  self.checkDirtyFlag = function ()
  {
    self.createForm.find("input").on("change", function (val, key) {
      if ($(this).attr("class") !== "tab-check")
      {
        self.jsonData[$(this).attr("name")] = $(this).val();
        $(this).addClass("isDirty");
      }
    });
    self.createForm.find("textarea").change(function (val, key) {
      self.jsonData[$(this).attr("name")] = $(this).val();
      $(this).addClass("isDirty");
    });
    self.createForm.find("select").change(function (val, key) {
      self.jsonData[$(this).attr("name")] = $(this).val();
      $(this).addClass("isDirty");
    });
  };

  self.resetDirty = function ()
  {
    self.isDirty_seller = false;
    self.isDirty_buyer = false;
  };

  self.markDirty = function (type)
  {
    if (type === "seller_tab")
    {
      self.isDirty_seller = true;
    } else {
      self.isDirty_buyer = true;
    }
  };

  // </editor-fold>

  self.init();
};

