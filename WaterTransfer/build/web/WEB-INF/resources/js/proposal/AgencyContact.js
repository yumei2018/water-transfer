/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global moment */

var AgencyContact = function(){
    var self = this;
    var commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;

    self.init = function()
    {
        self.proposalDev = new ProposalDev();
        self.proposalDev.initForm();
        self.initItems();
        self.buttonItems();
        self.tabsListeners();
        self.loadExistSeller();
        self.loadExistBuyer();
        self.initListeners();
    };
    self.initItems=function()
    {
        self.contentBody = $("#content-ct");
        self.proposalCt = $("#tabs");
        self.buttonCt = $(".button_ct");
        self.uploadFileForm = $("#upload-file-form");
        self.buyerTab = self.proposalCt.find("#buyer_tab");
        self.sellerTab = self.proposalCt.find("#seller_tab");
        self.baseInfoTab = self.proposalCt.find("#baseinfo_tab");
        self.groundwaterTab = self.proposalCt.find("#groundwater_tab");
        self.pdfReportTab = self.proposalCt.find("#pdfreport_tab");
        self.changeLogTab = self.proposalCt.find("#changelog_tab");
        self.viewChangeLogLink = $('#view_changelog');
        self.sellerCt = self.sellerTab.find(".agency_list");
        self.buyerCt = self.buyerTab.find(".agency_list");
        self.buyerPanel =  self.buyerTab.find(".accordion");
        self.sellerPanel =  self.sellerTab.find(".accordion");
        self.loadBuyer = self.buyerTab.find(".load_buyer");
        self.loadSeller = self.sellerTab.find(".load_seller");
        self.isReviewer = self.proposalCt.find(".massDisabled");
        self.createForm = self.proposalCt.find("#create-form");
        self.propPDFForm = $("#proPDFform");
        self.proposalHeader = self.contentBody.find("div.header_tab");
    };
    self.buttonItems = function(){
        self.saveBtn = self.buttonCt.find("#saveProposal");
        self.submitBtn = self.buttonCt.find("#submitProposal");
        self.nextBtn = self.buttonCt.find("#nextTab");
        self.changeLogBtn = self.buttonCt.find("#changeLog");
        self.addSellerBtn = self.sellerTab.find("input[name=seller]");
        self.addBuyerBtn = self.buyerTab.find("input[name=buyer]");
        self.proposalPDFPreview = self.buttonCt.find(".proposalReportBtn");
        self.proposalPDFLnk = self.buttonCt.find(".proposalReports");
//        self.addAgencyBtn = self.proposalCt.find("input[name=newAgency]");
        self.addAgencyBtn = self.proposalCt.find(".newAgency");
//        self.newContactLnk = self.buyerTab.find(".newContact");
    };
    self.afterLoadItems=function()
    {
        self.tableEl = $(".agencyContactList");
        self.editIcon = self.tableEl.find(".edit_contact");
        self.deleteIcon = self.tableEl.find(".delete_contact");
        self.accessIcon = self.tableEl.find(".contact_access");
        self.newContactBtn = self.tableEl.nextAll("input.contact_button");
//        self.newContactLnk = self.buyerTab.find(".newContact");
        self.agencyHeaderCt = $(".headerCt");//self.tableEl.parent().prev();
        self.agencyType = $("select.agencyType");
    };
    self.afterLoadListeners=function()
    {
        self.newContactBtn.unbind("click",self.addNewContact);
//        self.newContactLnk.unbind("click",self.addNewContact);
        self.editIcon.unbind("click",self.editContact);
        self.deleteIcon.unbind("click",self.deleteContact);
        self.accessIcon.unbind("click",self.accessContact);
        self.agencyType.unbind("change",self.editAgency);
        self.newContactBtn.on("click",self.addNewContact);
//        self.newContactLnk.on("click",self.addNewContact);
        self.editIcon.on("click",self.editContact);
        self.deleteIcon.on("click",self.deleteContact);
        self.accessIcon.on("click",self.accessContact);
        self.agencyType.on("change",self.editAgency);

        self.agencyHeaderCt.unbind("click",self.headerToggle);
        self.agencyHeaderCt.on("click",self.headerToggle);
        self.agencyHeaderCt.find("label").on("click",function(e){
            e.stopPropagation();
        });
        self.agencyHeaderCt.find("input").on("click",function(e){
            e.stopPropagation();
        });
        self.agencyHeaderCt.find("img.remove_icon_ct").on("click",function(e){
            e.stopPropagation();
        });
        self.addSellerBtn.hide();
        if(self.sellerPanel.find("h2").length ===0)
        {
            self.addSellerBtn.show();
        }
        self.initDisabledProposalForm();
    };
    self.editAgency=function(e)
    {
       var agencyType = $(this).val();
       var wtAgencyId = $(this).parent().find("input[name=wtAgencyId]").val();
       var data = {wtAgencyId: wtAgencyId, agencyType: agencyType};
       var url = window.SERVER_ROOT + "/proposal/editAgency";
       $.ajax({
            type:"POST"
            ,url:url
            ,data:data
            ,cache:false
            ,dataType:'json'
            ,success:function(data,status,jqxhr){
              try{
                if(!data.success){
                  throw data.error || "Unable to edit agency.";
                }
              }catch(e){
                if (data.callback) {
                  var callback = eval(data.callback);
                  if (typeof callback === "function") {
                    callback.call();
                  }
                }else if (e){
                  alert(e);
                }
              }
            }
        });
    };
    self.headerToggle=function(e)
    {
        if($(this).hasClass("isExpand"))
        {
            $(this).removeClass("isExpand");
            $(this).find("img:not(.edit_icon)").addClass("img_rotate");
        }
        else
        {
            $(this).addClass("isExpand");
            $(this).find("img:not(.edit_icon)").removeClass("img_rotate");
        }
        $(this).next().slideToggle("fast");
    };
    self.initListeners=function()
    {
        self.proposalDev.proposalType.find("input").on("click",self.proposalDev.checkBoxHandler);
        self.addSellerBtn.on("click",self.toggleAgencyComboxbox);
        self.addBuyerBtn.on("click",self.toggleAgencyComboxbox);
        self.saveBtn.on("click",self.initSave);
        self.submitBtn.on("click",self.initSubmit);
        self.nextBtn.on("click",self.initNextTab);
        self.changeLogBtn.on("click",self.initChangeLog);
        self.viewChangeLogLink.on('click',self.showChangeLog);
        self.proposalPDFPreview.on("click",self.initPDFRenderer);

        self.proposalPDFLnk.unbind("click",self.initPDFReports);
        self.proposalPDFLnk.on("click",self.initPDFReports);

        self.addAgencyBtn.on("click",self.initNewAgency);
//        self.newContactLnk.unbind("click",self.addNewContact);
//        self.newContactLnk.on("click",self.addNewContact);
        if(self.proposalDev.getUrlParameter() ===-1)
        {
            self.addSellerBtn.show();
        }
    };

    self.initNextTab = function()
    {
      var index = self.proposalCt.tabs('option', 'active');
      if (index < 2){
        self.proposalCt.tabs("option", "active", index+1);
      } else {
        alert("You need fill more information by select type of transfer.");
      }
    };

    self.initNewAgency = function()
    {
        self.addAgencyDialog = $("<div>");
        self.agencyList = $(this).parent().parent().find("select");
//        alert($(this).parent().parent().find("select").html());
        $.ajax({
            url:window['SERVER_ROOT']+'/proposal/getNewAgencyForm'
            ,type:'POST'
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.addAgencyDialog.append(data);
                self.addAgencyDialog.dialog({
                    title:'New Agency'
                    ,width:500
                    ,modal:true
                    ,height:200
                    ,buttons:[{
                        text:'Add'
                        ,click:function(){
                            $.ajax({
                              url:window['SERVER_ROOT']+'/proposal/addNewAgency'
                              ,type:'POST'
                              ,data:{agencyFullName: $(this).find("input[name=agencyFullName]").val()}
                              ,cache:false
                              ,success:function(data,status,jqxhr){
                                if (data === ""){
                                    alert("Agency name is duplicate.");
                                } else {
                                    alert("New Agency Added.");
                                    var jsonData = JSON.parse(data);
    //                                console.log(jsonData);
    //                                console.log(jsonData['wtAgencyId']+", "+jsonData['agencyFullName']);
                                    self.agencyList.append('<option value="'+jsonData['wtAgencyId']+'">'+jsonData['agencyFullName']+'</option>');
                                }
                                self.addAgencyDialog.dialog('close');
                              }
                          });
                        }
                    },{
                        text:'Cancel'
                        ,click:function(){
                            $(this).dialog('close');
                        }
                    }]
                });
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.initPDFRenderer = function(){
        self.transId = $(this).attr("wtTransId");
        self.userId = $(this).attr("userId");
        var msg = "In addition to opening the PDF, would you like to save a copy of the PDF to the database?";
        var title = "Save PDF Confirmation";
        self.userValidation(title,msg,function(bool){
            self.propPDFForm.find("input[name=track]").val(bool);
//            self.initPDFReport();
            self.propPDFForm.submit();
//            self.url =window['SERVER_ROOT']+"/report/printReport/"+self.transId+"?userId="+self.userId+"&track="+bool+"&attachType=PR";
//            window.open(self.url,'_blank');
        });
    };

    self.initPDFReport = function(){
      self.contentBody.mask("Generating PDF Report, please wait...");
      var xhr = new XMLHttpRequest();
      xhr.open('GET', self.propPDFForm.attr("action"), true);
      xhr.responseType = 'blob';
      xhr.onload = function(e) {
        self.contentBody.unmask();
        if (this.status == 200) {
          var blob = new Blob([this.response], {type: 'application/pdf'});
          var url = window.URL.createObjectURL(blob);
          var aTag = $("<a/>",{
            href:url
            ,download:"detailPDF.pdf"
            ,html:'detailPDF.pdf'
          });
          aTag.appendTo($(".button_ct"));
//          a.click();
//          window.open(url);
        }
      };
      xhr.send();

//      return false;
//      $.ajax({
//        url:self.propPDFForm.attr("action")
//        ,cache:false
//        ,success:function(data,status,jqxhr){
//          var blob=new Blob([data]);
//          var url = window.URL.createObjectURL(blob);
//          console.log(url)
//          window.open(url);
//          self.contentBody.unmask();
//        }
//        ,error:function(xhr, errorType, exception){
//          self.contentBody.unmask();
//          if(xhr.status === 403) //session ends
//          {
//            location = window.SERVER_ROOT;
//          }
//        }
//      });
    };
    self.initPDFReports = function(){
      self.form = $("<form method='post' id='PDFReportsform' action='#'>");
      var wtTransId = $(this).attr("wtTransId");
      var reportType = $(this).attr("reportType");
      var data = {reportType: reportType};
      var title = "Groundwater PDF Reports";
      if (reportType === "PR"){
        title = "Proposal PDF Reports";
        data = {
          transYear:self.proposalHeader.attr("transyear")
          ,sellerName:self.proposalHeader.attr("sellername")
          ,reportType: reportType
        };
      }
      var url = window.SERVER_ROOT + "/proposal/PDFReport/"+wtTransId;
      self.contentBody.mask("Loading PDF Report...");
      $.ajax({
        type:"POST"
        ,url: url
        ,data:data
        ,cache:false
        ,scope:this
        ,success:function(data,status,jqxhr){
          self.form.append(data);
          self.form.dialog({
              title: title
              ,width: 1000
              ,height: 400
              ,resizable: true
              ,modal:true
          }).dialog('open');
          self.contentBody.unmask();
        }
        ,error:function(xhr, errorType, exception){
          self.contentBody.unmask();
          if(xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
          }
        }
      });
    };
    self.buildData=function(){
        var data = self.proposalDev.getParam();

        var wtTransId = self.createForm.find("#wtTransId").val();
        // Force to send wtTransId
        if (wtTransId !== null || wtTransId !== ""){
//            alert(wtTransId);
            data["wtTransId"] = wtTransId;
        }
        if(self.isDirty_seller)
        {
            data["sellerTab"] = self.getSellerTab();
        }
        if(self.isDirty_buyer)
        {
            data["buyerTab"] = self.getBuyerTab();
        }
        if(self.createForm.find(".isDirty").length===0 && jQuery.isEmptyObject(data))
        {
            data = new Object();;
        }
        if(!jQuery.isEmptyObject(data))
        {
            self.orgData = self.createForm.serializeArray();
            var checkboxFields = self.createForm.find("input:checkbox");
            $.each(self.orgData, function(index, obj){
//                if(self.createForm.find("input[name="+obj.name+"]").hasClass("isDirty")){
                    data[obj.name] = obj.value;
//                }
            });
            checkboxFields.each(function(){
                if(!this.checked && $(this).hasClass("isDirty")){
//                    alert(this.name+"="+this.value);
                    data[this.name] = this.value;
                }
            });
            self.createForm.find("input").removeClass("isDirty");
            self.createForm.find("textarea").removeClass("isDirty");
        }
        data['countyId'] = self.proposalDev.getCountyIdList().toString();
        data['purposeId'] = self.proposalDev.getPurposeIdList().toString();

        return data;
    };

    self.initSave=function()
    {
        var data = self.buildData();

//        alert(JSON.stringify(data));
//        if(!self.waterPercentValidation())
//        {
//            return false;
//        }
        // Validation fields in createForm
//        if (!self.proposalDev.validTabs("SAVE")){
//            alert("Please check each tab for errors...");
//            return false;
//        }

        // If there is GW Tab, then validate this part
//        var gwcheckbox = self.baseInfoTab.find("#groundwater-check");
////        alert(gwcheckbox.val());
//        if(gwcheckbox.prop('checked')){
//          self.gwValidation();
//        }
        // Only save modified data or new enter data
        if (typeof self.accessIcon !== 'undefined'){
          data['accessContact']=self.findAllAccessContact();
        }
        self.contentBody.mask("Saving...please wait");
        if(!jQuery.isEmptyObject(data))
        {
            var url =window.SERVER_ROOT + "/proposal/saveProposalJson"; //saveProposalJson
            $.ajax({
                type:'POST'
                ,data:{jsondata:JSON.stringify(data)}
                ,url:url
                ,cache:false
                ,dataType:"json"
                ,success:function(jsonData,status,jqxhr){
                  self.contentBody.unmask("Proposal Saved!");
                  try {
                    if (!jsonData.success) {
                      throw jsonData.error || "The system cannot save the proposal!";
                    }
//                    if(jsonData['transStatus'] === "DRAFT"){
                    alert("You have saved the application. Saving does not submit the application for review.");
//                    } else {
//                      alert("Proposal Saved!");
//                    }
                    if(!self.createForm.find("#wtTransId").val()){
                      window.location = window.SERVER_ROOT+"/proposal/edit/"+jsonData['wtTransId'];
                    }
                  }
                  catch(e) {
                    if (jsonData.callback) {
                      var callback = eval(jsonData.callback);
                      if (typeof callback === "function") {
                        callback.call();
                      }
                    }
                    else if (e){
                      alert(e);
                    }
                  }
                }
                ,error:function(xhr, errorType, exception){
                  if(xhr.status === 403){ //session ends
                    location = window.SERVER_ROOT;
                  }
                }
            });
        }
    };

    self.submitValidation=function()
    {
//        var isEmpty=false;
        // Validation for At least have one Seller
        if(!self.getSellerTab().WT_AGENCY_ID)
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
        if(!self.waterPercentValidation())
        {
          return false;
        }
//        if(isEmpty)
//        {
//          alert("Please specify the amount of water to allocate. See Buyer Tab.");
//          return false;
//        }
        // Validation for at least have one Water Rights
        if(!self.waterRightsValidation()){
          alert("Please add at least one Water Rights Type.");
          return false;
        }
        // Validation fields in createForm
        if (!self.proposalDev.validTabs("SUBMIT")){
          alert("Please check for errors ...");
          return false;
        }

        // If there is GW Tab, then validate this part
        var gwcheckbox = self.baseInfoTab.find("#groundwater-check");
        if(gwcheckbox.prop('checked')){
          self.gwValidation();
        }

        self.msg="Are you sure you want to submit the proposal?";
        return true;
    };

    self.initSubmit=function()
    {
        var data = self.buildData();
//        var msg="Are you sure you want to submit the proposal?";
        var msg="Information on the proposed water transfer that has been officially \n\
                  submitted to the Department of Water Resources will become a public record and subject to disclosure.";
        var title = "Submit Confirmation";
        if(!self.submitValidation()){
          return false;
        }

        // wtTransId has to be send
        var wtTransId = self.baseInfoTab.find("#wtTransId").val();
        if(wtTransId === ""){
            alert("There is no proposal to submit.");
            return false;
        }

        self.userValidation(title,msg,function(bool){
            if(!bool)
            {
                return false;
            }

            // Remove alert message
            $(window).unbind('beforeunload');
            self.contentBody.mask("Submitting...please wait");
            var url =window.SERVER_ROOT + "/proposal/submit?wtTransId="+wtTransId; //submitProposal
            $.ajax({
                type:'POST'
                ,data:{jsondata:JSON.stringify(data)}
                ,url:url
                ,cache:false
                ,dataType:'json'
                ,success:function(data,status,jqxhr){
                  try{
                      if(!data.success){
                        throw data.error || "Unable to submit the proposal.";
                      }
                      self.contentBody.unmask();
                      alert("Proposal Submitted!");
                      var url=window.SERVER_ROOT + "/proposal/?moduleType=view";
                      $(window).unbind('beforeunload');
                      window.location.assign(url);
                    }catch(e){
                      self.contentBody.unmask();
                      if (data.callback) {
                        var callback = eval(data.callback);
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
                }
            });
        });
    };

    self.initChangeLog=function()
    {
        var wtTransId = self.baseInfoTab.find("#wtTransId").val();
//        alert("Change Log ..."+wtTransId);
        if(!self.submitValidation()){
          return false;
        }
        self.changeLog = self.changeLogTab.find(".change_log");
//        alert(self.changeLog.html());
        self.changeLogTable = self.changeLog.find("table tbody");
//        alert(self.changeLogTable.html());
        self.changeLogDialog = $('<div><textarea cols="50" rows="8" id="changeLog" name="changeLog" placeholder="Summarize the changes to the application since the last submittal" style="width:99%; height: 99%;"></textarea></div>');

        self.changeLogDialog.dialog({
            title:'What changes have you submit?'
            ,width:600
            ,modal:true
            ,height:400
            ,buttons:[{
                text:'Submit'
                ,click:function(){
                    var changeLog = $("textarea#changeLog").val();
                    self.changeLogDialog.parent().mask("Submitting the proposal report...");
                    $.ajax({
                        url:window.SERVER_ROOT + '/proposal/resubmit'
                        ,type:'POST'
                        ,data:{wtTransId:wtTransId,changeLog:changeLog}
                        ,cache:false
                        ,dataType:'json'
                        ,success:function(response,status,jqxhr){
                            try{
                              if(!response.success){
                                throw response.error || "Unable to set the permission.";
                              }
                              self.changeLogDialog.parent().unmask();
                              var jsonData = response.data;
                              var formattedDate = $.datepicker.formatDate('mm/dd/yy',new Date());
                              var htmlRow = "<tr><td>"+formattedDate+"</td>";
                              htmlRow += "<td>"+jsonData['changeUser']+"</td>";
                              htmlRow += "<td>"+jsonData['changeLog']+"</td></tr>";
                              alert("Change Log Saved.");
                              self.changeLogDialog.dialog("destroy").remove();
                              self.changeLogTable.append(htmlRow);
                            }catch(e){
                              self.changeLogDialog.parent().unmask();
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
                        }
                    });
                }
            },{
                text:'Cancel'
                ,click:function(){
//                    $(this).dialog('close');
                    $(this).dialog("destroy").remove();
                }
            }]
        });
    };

    self.showChangeLog = function(){
      var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
      if(!wtTransId)
      {
          return false;
      }

      var successCallBack = function(data,status,jqxhr){
                  $("#content-ct").unmask();
                  $('<div/>')
                    .html(data)
                    .dialog({
                      title: 'Submit Log'
                      ,width: 1075
                      ,height: 500
                    }).show();
              };

      self.ChangeLog(wtTransId,null, true, successCallBack);

    }
    self.toggleAgencyComboxbox = function()
    {
        $(this).next().fadeToggle(1).removeClass("hidden");
        self.toggleEditAddAgencyCt.call(this);
    };
    self.toggleEditAddAgencyCt = function()
    {
        $(this).next().next().fadeToggle(1).removeClass("hidden");
    };
    self.addNewContact = function()
    {
        self.currentTableEl = $(this).prevAll(".agencyContactList");
        var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
        var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
//        alert($(this).parent().parent().parent().attr('id'));
        var agencyType = "Seller Agency";
        var tabName = $(this).parent().parent().parent().attr('id');
        if (tabName === "buyer_tab"){
          agencyType = "Buyer Agency";
        }
        // Buyers Representative no Agency
        if (typeof agencyid === 'undefined'){
          agencyType = "Buyers";
        }
        var agencyContact = {};
        agencyContact = {wtAgencyId:agencyid,wtTransId:wtTransId,agencyType:agencyType};
        new AgencyContactForm(agencyContact,function(data){
            self.currentTableEl.parent().html(data);
            self.afterLoadItems();
            self.afterLoadListeners();
        });
    };
    self.editContact = function()
    {
//        alert("editContact");
        var contactid = $(this).parent().parent().parent().attr("contactid");
        self.currentTableEl = $(this).parent().parent().parent().parent().parent();
        var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
        var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
        var agencyType = "Seller Agency";
        var tabName = $(this).parent().parent().parent().parent().parent().parent().parent().parent().attr('id');
//        alert($(this).parent().parent().parent().parent().parent().parent().parent().parent().html());
        if (tabName === "buyer_tab"){
          agencyType = "Buyer Agency";
        }
        // Buyers Representative no Agency
        if (typeof agencyid === 'undefined'){
          agencyType = "Buyers";
        }
//        alert(agencyid+":"+agencyType);
        var agencyContact = {};
        agencyContact = {wtContactId:contactid,wtAgencyId:agencyid,wtTransId:wtTransId,agencyType:agencyType};
        new AgencyContactForm(agencyContact,function(data){
            self.currentTableEl.parent().html(data);
            self.afterLoadItems();
            self.afterLoadListeners();
        });
    };
    self.deleteContact = function()
    {
        var tableTr = $(this).parent().parent().parent(),
        msg = "Are you sure you want to remove this Contact?";
        self.currentTableEl= tableTr.parent().parent();
        var agencyid = self.currentTableEl.find("input[name=wtAgencyId]").val();
        var title = "Remove Confirmation";
        self.userValidation(title,msg,function(bool){
            if(!bool)
            {
                return false;
            }
            var contactData = JSON.parse(tableTr.find("input[name=contactLookup]").val());
            contactData['isActive']=0;
            contactData['wtAgencyId']=agencyid;
            self.contentBody.mask("Removing contact...");
            $.ajax({
             url:window["SERVER_ROOT"]+"/proposal/saveContact"
             ,data:contactData
             ,cache:false
             ,success:function(data,status,jqxhr)
             {
               self.contentBody.unmask();
               tableTr.remove();
               alert("Removed Successfully!");
             }
             ,error:function(xhr, errorType, exception){
               self.contentBody.unmask();
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
    self.accessContact=function(){
      var checkbox = this;
      var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
      var contactid = $(this).parent().parent().attr("contactid");
      var linkTrans = "";
      var agencyContact = {};
      linkTrans = "N";
      if(this.checked) {
        linkTrans = "Y";
      }
      agencyContact = {wtContactId:contactid,wtTransId:wtTransId,linkTrans:linkTrans};
      var url = window.SERVER_ROOT + "/proposal/contactAccess";
      if(wtTransId){
//        $.post(url,agencyContact);
        $.ajax({
            url:url
            ,data:agencyContact
            ,cache:false
            ,dataType:'json'
            ,type:'post'
            ,success:function(data,status,jqxhr)
            {
              try{
                if(!data.success){
                  throw data.error || "Unable to set the permission.";
                }
              }catch(e){
                $(checkbox).prop("checked",!checkbox.checked);
                if (data.callback) {
                  var callback = eval(data.callback);
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
      }
    };
    self.findAllAccessContact = function(){
        var agencyContactList=[];
        if(self.accessIcon === "" || self.accessIcon === null){
            return "";
        }
        $.each(self.accessIcon,function(){
            if(this.checked){
                agencyContactList.push({
                    wtContactId:$(this).parent().parent().attr("contactid")
                    ,linkTrans:'Y'
                });
            }
        });
        if(agencyContactList.length<1)
        {
            return "";
        }
        return agencyContactList;
    };
    self.tabsListeners=function(){
        self.proposalCt.tabs({
            active:0
            ,activate: function( event, ui ) {
                self.selectAgencyListeners(ui.newPanel);
                if(ui.newTab.index() > 1 && ui.newTab.index() < 10)
                {
//                    alert(ui.newPanel);
                    if(ui.newTab.index() === 6){
//                      alert(ui.newPanel);
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
            ,create: function( event, ui ) {
                self.selectAgencyListeners(ui.panel);
            }
        });
    };

    self.loadTabs=function(panel){
        self.attachmentContainer = panel.find(".attachment-container");
        var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
        var typeId = self.attachmentContainer.attr("typeid");
        var mapTypeId = panel.find(".map-attachment-container").attr("typeid");
        if(!wtTransId)
        {
            return false;
        }

        self.preTransferList(typeId,wtTransId,panel);
        self.waterRightsList(typeId,wtTransId,panel);
        self.associateWells(typeId,wtTransId,panel);
        self.attachmentList(typeId,wtTransId,panel);
        self.mapAttachmentList(mapTypeId,wtTransId,panel);
        self.ChangeLog(wtTransId,panel);
//        self.PDFReport(wtTransId,panel);
//        self.reviewCommentList(typeId,wtTransId,panel);
    };
    self.attachmentList=function(typeId,wtTransId,panel){
        self.attachmentContainer = panel.find(".attachment-container");
        var data = {wtTransId: wtTransId,typeId:typeId};
        var url = window.SERVER_ROOT + "/attachment/getAttachmentList";
        if(self.attachmentContainer.length > 0 && !self.attachmentContainer.html())
        {
            $("#content-ct").mask("Loading...");
            $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,success:function(data,status,jqxhr){
                    $("#content-ct").unmask();
                    self.attachmentContainer.children().remove();
                    self.attachmentContainer.append(data);
                }
                ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
                }
            });
        }
    };
    self.mapAttachmentList=function(typeId,wtTransId,panel){
        self.mapAttachCt = panel.find(".map-attachment-container");
        var data = {wtTransId: wtTransId,typeId:typeId};
        var url = window.SERVER_ROOT + "/attachment/getAttachmentList";
        if(self.mapAttachCt.length > 0 && !self.mapAttachCt.html())
        {
//            $("#content-ct").mask("Loading...");
            $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,success:function(data,status,jqxhr){
//                    $("#content-ct").unmask();
                    self.mapAttachCt.children().remove();
                    self.mapAttachCt.append(data);
                }
                ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
                }
            });
        }
    };
    self.preTransferList = function(type,wtTransId,panel){
        if(type !== "BI")
        {
            return false;
        }
        self.preTransferCt = panel.find(".pre_transfer_ct");
        $.ajax({
            type:"POST"
            ,url:window['SERVER_ROOT']+"/proposal/getPreTransferTable"
            ,data:{wtTransId:wtTransId}
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.preTransferCt.children().remove();
                self.preTransferCt.append(data);
                self.proposalDev.preTransferItems();
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.waterRightsList = function(type,wtTransId,panel){
        if(type !== "BI")
        {
            return false;
        }
        self.waterRightCt = panel.find(".water_rights_ct");
        $.ajax({
            type:"POST"
            ,url:window['SERVER_ROOT']+"/proposal/getWaterRightsTable"
            ,data:{wtTransId:wtTransId}
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.waterRightCt.children().remove();
                self.waterRightCt.append(data);
                self.proposalDev.waterRightItems();
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.associateWells = function(type,wtTransId,panel){
        if(type !== "GW")
        {
            return false;
        }
        $(".proposalReports").unbind("click",self.initPDFReports);
        $(".proposalReports").on("click",self.initPDFReports);
        self.assiocialteWellsCt = panel.find("#listAssociateWellCt");
        $.ajax({
            type:"POST"
            ,url:window['SERVER_ROOT']+"/proposal/getAssociateWells"
            ,data:{wtTransId:wtTransId}
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.assiocialteWellsCt.children().remove();
                self.assiocialteWellsCt.append(data);
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                  location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.ChangeLog = function(wtTransId,panel, force, successfn){
        if(force || panel[0].id === "changelog_tab") //Log Page
        {
          var success = function(data,status,jqxhr){
                  $("#content-ct").unmask();
                  self.changeLogTab.html(data);
              };
          if(typeof successfn === "function"){
            success = successfn;
          }

          $("#content-ct").mask("Loading...");
          $.ajax({
              type:"POST"
              ,url:window.SERVER_ROOT + "/proposal/ChangeLog/"+wtTransId
              ,cache:false
              ,success: success
              ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
              }
          });
        }
    };
    self.PDFReport = function(wtTransId,panel){
        if(panel[0].id === "pdfreport_tab") //Log Page
        {
          $("#content-ct").mask("Loading...");
          $.ajax({
              type:"POST"
              ,url:window.SERVER_ROOT + "/proposal/PDFReport/"+wtTransId
              ,cache:false
              ,success:function(data,status,jqxhr){
                  $("#content-ct").unmask();
                  self.pdfReportTab.html(data);
              }
              ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
              }
          });
        }
    };
    self.reviewCommentList=function(typeId,wtTransId,panel){
        self.commentsContainer = panel.find(".comments-container");
//        var wtTransId = self.baseInfoTab.find("input[name=wtTransId]").val();
//        var typeId = self.attachmentContainer.attr("typeid");

        var data = {wtTransId: wtTransId,transType:typeId};
        var url = window.SERVER_ROOT + "/proposal/getReviewComments";
        if(!self.commentsContainer.html())
        {
            $("#content-ct").mask("Loading...");
            $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,success:function(data,status,jqxhr){
                    $("#content-ct").unmask();
                    self.commentsContainer.children().remove();
                    self.commentsContainer.append(data);
                }
                ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
                }
            });
        }
    };
    self.selectAgencyListeners = function(panel)
    {
        // Mark Dirty to Buyers Representative change
        panel.find("select.buyersContact_list").on("change",function(){
          self.markDirty(panel.attr('id'));
        });
        panel.find(".removeContact").on("click",function(){
          self.markDirty(panel.attr('id'));
        });

        panel.find("select.agency_list").on("change",function(){
            var el = $(this),isDuplicate=false;
            if($(this).val()<1)
            {
                return false;
            }
            if(self.agencyHeaderCt)
            {
                $.each(self.agencyHeaderCt,function(){
                    if($(this).attr("agencyid") === el.val())
                    {
                        if(!$(this).attr("removed"))
                        {
                            return isDuplicate = true;
                        }
                    }
                });
            }
            if(isDuplicate)
            {
                alert("This Agency is already used, Please Select a different Agency.");
                $(this).val(0);
                return isDuplicate=false;
            }
            this.agencyObj = [];
            this.agencyObj.push($(this).val()); //agencyId
            this.agencyObj.push($(this).find("option:selected").text()); //agency Name
            this.agencyObj.push(panel.attr('id')); //tab type
            var accordionDiv = panel.find(".accordion");
            self.buildTabPanel(this.agencyObj,accordionDiv);
            if($(this).attr('class')=='agency_list')
            {
                $(this).next().fadeOut(1);
                $(this).val(0).fadeOut(1);
            }
            self.markDirty(panel.attr('id'));
            if(panel.attr('id') === "seller_tab")
            {
                self.addSellerBtn.fadeOut(1);
            }
        });
    };
    self.buildTabPanel=function(agencyObj,divEl)
    {
        var contentHeader = [],percent = '0.0';
        var percentDiv = null;
        if(agencyObj[3])
        {
            percent = agencyObj[3];
        }
        if(self.proposalCt.find("input[name=draft]").length<1)
        {
            contentHeader.push(
            $("<img/>",{
                src:window.SERVER_ROOT+"/resources/images/icons/close_x.png"
                ,class:'remove_icon_ct inline'
            }).on("click",function(){
                var deleteIcon = $(this);
                var msg = "Are you sure you want to remove this Agency?";
                var title = "Remove Confirmation";
                self.userValidation(title,msg,function(bool){
                    if(!bool)
                    {
                        return false;
                    }
                    deleteIcon.parent('h2').attr("removed",true);
                    deleteIcon.parent('h2').next("div").remove();
                    deleteIcon.parent('h2').remove();
                    self.markDirty(agencyObj[2]);
                    if(agencyObj[2] === "seller_tab")
                    {
                        self.addSellerBtn.show();
                    }
                });
            }));
        }
        contentHeader.push(
            $("<img>",{
                src:window.SERVER_ROOT+"/resources/images/icons/arrow_icon.png"
                ,class:'expand_arrow'
            }));
        contentHeader.push(
            $("<span/>",{
                html:agencyObj[1]
                ,class:'title inline'
            }));

        if(agencyObj[2] !== "seller_tab")
        {

//            contentHeader.push(
//                $("<input/>",{
//                            name:'water_percent'
//                            ,type:'text'
//                            ,value:percent
//                            ,class:'percent'
//                        }).keyup(function () {
//                            this.value = this.value.replace(/[^0-9\.]/g,'');
//                        }).on("blur",function(){
//            //                $(this).hide();
//            //                self.label =  $(this).next().show();
//            //                if(self.label.html() !== $(this).val())
//                            {
//                                var msg = false;//"Are you sure you want to update the changes?";
//                                var val = $(this).val();
//                                if(val >100)
//                                {
//                                    alert('Cannot Exceed over 100%');
//                                    return false;
//                                }
//                                if(!isFinite(val))
//                                {
//                                    alert('Wrong input value type');
//                                    return false;
//                                }
//            //                    self.userValidation(msg,function(bool){
//            //                        if(!bool)
//            //                        {
//            //                            return false;
//            //                        }
//            //                        self.label.html(val+"%");
//                                    self.markDirty(agencyObj[2]);
//            //                    });
//                            }
//                        })
//            );
//            contentHeader.push(
//                $("<label>",{
//                            html:'%'
//                            ,style:'vertical-align:bottom;padding-left:2px;'
//                        })
//                );
            var percenHeaderLabel = $("<label/>",{
                    html:percent+'%'
                    ,id: 'percent-header'
                    ,class:'percent'
                }).on("click",function(){
                    if(self.proposalCt.find("input[name=draft]").length<1)
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
                $("<img>",{
                    src:window.SERVER_ROOT+"/resources/images/icons/edit-icon.png"
                    ,class:'edit_icon hidden'
                }).on("click",function(){
                    $(this).prev().hide();
                    $(this).prev().prev().show().removeClass("hidden").focus();
                })
            );

            percentDiv = $('<div/>')
                      .css({
                        padding: '0 0 5px 10px'
                        ,margin: '0 25px 0 0'
                        ,display: 'inline-block'
                      })
                      .html('Percentage of transfer water allocation: ')
                      .append(
                        $("<input/>",{
                            name:'water_percent'
                            ,type:'text'
                            ,value:percent
                            ,class:'percent'
                        }).keyup(function () {
                            this.value = this.value.replace(/[^0-9\.]+/g,'');
                        }).on("keyup",function(){
            //                $(this).hide();
            //                self.label =  $(this).next().show();
            //                if(self.label.html() !== $(this).val())

                            {
                                var msg = false;//"Are you sure you want to update the changes?";
                                var val = $(this).val();
                                if (!val){
                                  val = 0;
                                }
                                if(val >100)
                                {
                                    alert('Cannot Exceed over 100%');
                                    return false;
                                }
                                if(!isFinite(val))
                                {
                                    alert('Wrong input value type');
                                    return false;
                                }
            //                    self.userValidation(msg,function(bool){
            //                        if(!bool)
            //                        {
            //                            return false;
            //                        }
            //                        self.label.html(val+"%");
                                    percenHeaderLabel.text(val + '%');
                                    self.markDirty(agencyObj[2]);
            //                    });
                            }
                        }).on('blur',function(){
                          if (!$(this).val())
                            $(this).val(0);
                        }))
                        .append($("<label>",{
                            html:'%'
                            ,style:'vertical-align:bottom;padding-left:2px;'
                        }))
        }
        self.getAgencyContactList(agencyObj[0],function(data){
//            if(agencyObj[2] === "seller_tab")
//            {
//                divEl.find("h2").remove();
//                divEl.find("div").remove();
//            }
            $("<h2>").addClass("headerCt isExpand").attr({agencyid:agencyObj[0]}).html(contentHeader).appendTo(divEl);
            $("<div>",{class:'contact_panel'})
                .html(data)
                .prepend(percentDiv)
                .appendTo(divEl);
            self.afterLoadItems();
            self.afterLoadListeners();
//            console.log(agencyObj);
        });
    };
    self.getAgencyContactList = function(agencyId,callback)
    {
         $("#content-ct").mask("Loading...");
        $.ajax({
            type:'POST'
            ,url:window.SERVER_ROOT + "/proposal/contactListByAgency"
            ,data:{wtAgencyId:agencyId,wtTransId:self.proposalDev.getTransId()}
            ,cache:false
            ,success:function(data,status,jqx)
            {
                 $("#content-ct").unmask();
                callback(data);
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.loadExistSeller = function()
    {
        if(self.loadSeller.length>0)
        {
            $(self.loadSeller.each(function(){
                this.agencyid = self.loadSeller.attr("agencyid");
                this.agencyObj = [];
                this.agencyObj.push(this.agencyid);
                self.sellerCt.val(this.agencyid);
                this.agencyObj.push(self.sellerCt.find("option:selected").text());
                this.agencyObj.push(self.loadSeller.parent().attr("id"));
                self.buildTabPanel(this.agencyObj,self.loadSeller.prev());
                self.sellerCt.val(0);
            }));
        }
    };
    self.loadExistBuyer = function()
    {
        if(self.loadBuyer.length>0)
        {
            $(self.loadBuyer.each(function(){
//                console.log($(this).attr("agencyid"));
                this.agencyid = $(this).attr("agencyid");
                this.percent = $(this).attr("percent");
                this.agencyObj = [];
                this.agencyObj.push(this.agencyid);
                self.buyerCt.val(this.agencyid);
                this.agencyObj.push(self.buyerCt.find("option:selected").text());
                this.agencyObj.push($(this).parent().attr("id"));
                this.agencyObj.push(this.percent);
                self.buildTabPanel(this.agencyObj,self.loadBuyer.parent().find(".accordion"));
                self.buyerCt.val(0);
            }));
        }
    };
    self.userValidation = function(tle,msg,callback)
    {
      var title = 'User Validation';
      if (tle !== null && tle !== ''){
        title = tle;
      }
        if(msg)
        {
            $("<div>",{
            html:'<p>'+msg+'</p>'
                }).dialog({
                title:title
                ,modal:true
                ,width:400
                ,height:200
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
                        if(callback && typeof(callback)==="function")
                        {
                            callback(false);
                        }
                    }
                }]
            }).dialog("open");
        }
        else{
            callback();
        }
    };
    self.getSellerTab = function()
    {
        var sellerAgencyObj = {};
        this.id = parseInt(self.sellerPanel.find(".headerCt").attr("agencyid"));
        if(this.id)
        {
            sellerAgencyObj={WT_AGENCY_ID:this.id};
        }
        return sellerAgencyObj;
    };
    self.getBuyerTab = function()
    {
        self.buyersContactTable = self.buyerTab.find("#buyersContactTable");
        var agencyIdCollection = [];
        $.each(self.buyerPanel.find(".headerCt"),function(){
//          var percent = $(this).next().siblings('.contact_panel').find("input[name=water_percent]").val();
          var percent = $(this).next().find("input[name=water_percent]").val();
//          alert(percent);
          if(isNaN(percent)){
            percent = 0;
          }
            agencyIdCollection.push({
                WT_AGENCY_ID:parseInt($(this).attr("agencyid"))
                ,SHARE_PERCENT:parseFloat(percent)
            });
        });
        // Added Buyers contact ID
//        alert(self.buyersContactTable.html());
//        alert(self.buyersContactTable.find("tbody tr:visible").attr("id"));
        var buyersContactId = self.buyersContactTable.find("tbody tr:visible").attr("id");
        agencyIdCollection.push({WT_CONTACT_ID:parseInt(buyersContactId)});

        return agencyIdCollection;
    };
    self.resetDirty=function()
    {
        self.isDirty_seller = false;
        self.isDirty_buyer = false;
    };
    self.markDirty=function(type)
    {
        if(type === "seller_tab")
        {
            self.isDirty_seller = true;
        }
        else
        {
            self.isDirty_buyer = true;
        }
    };
    self.waterPercentValidation=function()
    {
        var isValid=true,percent = 0;
        $.each(self.getBuyerTab(),function(key,val){
//            if(val["SHARE_PERCENT"] === 0)
//            {
//                return isValid=false;
//            }
            if($.isNumeric(val['SHARE_PERCENT'])){
              percent+=val["SHARE_PERCENT"];
            }
//            alert(percent);
        });
//        if(!isValid)
//        {
//            alert("Please specify the amount of water to allocate. See Buyer Tab.");
//            return false;
//        }
        if(percent>100)
        {
            alert("The total water distribution cannot exceed over 100%");
            return false;
        }
//        self.msg="Are you sure you want to submit the proposal?";
        return isValid;
    };
    self.waterRightsValidation=function(){
      var isValid = false;

      // Check when loading proposal
      self.waterRightsSize = self.sellerTab.find("#waterRightsSize").val();
//      alert(self.waterRightsSize);
      if(self.waterRightsSize !== "" && parseInt(self.waterRightsSize) > 0){
        isValid = true;
      }

      // Check in Water Rights Type and Transfer Information Section
      self.waterRightsTableRow = self.baseInfoTab.find(".water_rights_table tbody tr");
      if (typeof self.waterRightsTableRow.html() !== 'undefined'){
//        alert(self.waterRightsTableRow.html());
        isValid = false;
      }
      self.waterRightsTableRow.each(function(){
        if($(this).find("input:hidden[name=wtWaterRightsId]").val() !== ""){
          isValid = true;
        }
      });

      return isValid;
    };

    self.gwValidation=function(){
      var isValid=false;
      var depletionVal = self.groundwaterTab.find("#streamDepletion").val().replace(/[ ,]+/g, "");
      var netTransWaterVal = self.groundwaterTab.find("#netTransWater").val().replace(/[ ,]+/g, "");
      var MTdepletionVal = self.groundwaterTab.find("#streamDepletionTotal").val().replace(/[ ,]+/g, "");
      var MTnetTransWaterVal = self.groundwaterTab.find("#netTransWaterTotal").val().replace(/[ ,]+/g, "");

      if(depletionVal === MTdepletionVal && netTransWaterVal === MTnetTransWaterVal){
        isValid = true;
        return isValid;
      } else if (depletionVal !== MTdepletionVal){
        alert("Field Streamflow Depletion does not match the monthly total");
      } else if (netTransWaterVal !== MTnetTransWaterVal){
        alert("Field Net Transfer Water does not match the monthly total");
      }

//      alert(depletionVal + ":" + netTransWaterVal);
      return isValid;
    };

    self.initDisabledProposalForm = function()
    {
        if(self.isReviewer)
        {
            self.isReviewer.find("input,button,textarea,select").attr("disabled","disabled");
            self.isReviewer.find("img,label").off("click");
        }
    };
    self.init();
};

