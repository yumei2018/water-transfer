/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ProposalDev=function() {
    var self = this;
    var commaRegex = /(\d)(?=(\d\d\d)+(?!\d))/g;

    self.initForm=function(){
        self.initItems();
        self.initButtonItems();
        self.initTransYr();
//        self.initGWCalculate();
//        self.initBaseInfoCalculate();
        self.proposalTypeSelected();
        self.initListeners();
        self.initAttachmentUploadForm();
        self.initCalendar();
        self.intHandler();
        self.fieldsValidation();
        self.saveProposalProcess();
        self.approveProposal();
        self.suspendProposal();
        self.completeProposal();
//        self.initfacilitesCheckboxHandler();
        self.checkDirtyFlag();
    };
    self.initItems = function()
    {
        self.proposalCt = $("#tabs");
        self.proposalType = $("#typeMenu");
        self.createForm = self.proposalCt.find("#create-form");
        self.processForm = self.proposalCt.find("#process-form");
        self.groundwaterTab = self.proposalCt.find("#groundwater_tab");
        self.cropidlingTab = self.proposalCt.find("#cropidling_tab");
        self.reservoirTab = self.proposalCt.find("#reservoir_tab");
        self.conservedTab = self.proposalCt.find("#conserved_tab");
        self.pdfReporTab = self.proposalCt.find("#pdfreport_tab");
        self.proposalProcessTab = self.proposalCt.find("#proposalprocess_tab");
        self.statusTrackTab = self.proposalCt.find("#statustrack_tab");

        self.sellerTab = self.proposalCt.find("#seller_tab");
        self.buyerTab = self.proposalCt.find("#buyer_tab");
        self.buyerPanel =  self.buyerTab.find(".accordion");
        self.sellerPanel =  self.sellerTab.find(".accordion");

        self.tabHeader = self.proposalCt.find(".tab_header");
        self.jsonData = new Object();
        self.transYear = self.proposalCt.find("#transYear");
        self.type = $("#attachmentType");
        self.form = $("div#wellListCt");
        self.associateAttachment = self.form.find(".attachCt");

        self.baseInfo = $("#base-container");
        self.tableRow = self.baseInfo.find("tbody tr");
        self.image = $("<img>",{
            src:window.SERVER_ROOT+"/resources/images/icons/arrow_icon.png"
            ,style:'margin: 3px; 15px 0 0'
            ,class:'expand_arrow'

        });
        self.tabHeader.prepend(self.image);
        self.baseInfoTabItems();
    };
    self.baseInfoTabItems = function()
    {
        self.baseInfoTab = self.proposalCt.find("#baseinfo_tab");
        self.checkbox = self.baseInfoTab.find(".checkbox_ct");
        self.transferStartDate = self.baseInfoTab.find("input[name=transWinStart]");
        self.transferEndDate = self.baseInfoTab.find("input[name=transWinEnd]");
    };
    self.initButtonItems = function()
    {
        self.buttonCt = $(".button_ct");
        self.attachButtonCt = $(".attach-button");
        self.requestBt = self.buttonCt.find(".request_btn");
        self.calBtn = self.proposalCt.find("#calBtn");
        self.preTransferAddBtn = self.attachButtonCt.find("#pre_transfer_addBtn");
        self.waterRightsAddBtn = self.attachButtonCt.find("#water_rights_addBtn");
    };
    self.initTransYr = function()
    {
        if(self.transYear.val() === ""){
            var currentYear = (new Date).getFullYear();
            var currentMonth = (new Date).getMonth() + 1;
            currentMonth<10? self.transYear.val(currentYear):self.transYear.val(currentYear+1);
        }
    };
    self.intHandler = function()
    {
        self.numField = $(".numField");
        self.intField = $(".intField");
        self.yearField = $(".yearField");
        self.floatField = $(".floatField");

        self.yearField.keyup(function () {
            this.value = this.value.replace(/[^0-9\.]/g,'');
        });
        self.intField.keyup(function () {
            this.value = this.value.replace(/[^0-9\.]/g,'');
        });
        self.floatField.keyup(function () {
            this.value = this.value.replace(/(?!^-)[^0-9.]/g,'');
        });
        self.numField.keyup(function () {
            this.value = this.value.replace(/[^0-9\.]/g,'');
//            var num = parseFloat(this.value);
//            this.value = num.toFixed(2);
        });

        self.intField.on("blur",function(){
          if($(this).val() !==  ""){
            var num = $(this).val().replace(/[, ]+/g, "");
            if(!$.isNumeric(num)){
              $(this).val("");
            }
            var intVal = Math.round(num);
            $(this).val(intVal);
          }
//            var intVal = num.split('.')[0];
//            var dotVal = num.split('.')[1];
////            alert(intVal+", "+dotVal);
//            if (dotVal){
//                var firstVal = dotVal.substr(0,1);
////                alert(firstVal);
//                if (parseInt(firstVal)>=5){
//                    intVal = (parseInt(intVal)+1).toString();
//                }
////                alert(intVal);
//                $(this).val(intVal);
//            }
//            $(this).val(intVal.replace(commaRegex, "$1,"));
        });

        self.numField.each(function(){
            $(this).val(this.value.replace(commaRegex,"$1,"));
        });

        self.numField.on("blur",function(){
          if(this.value !==  ""){
            var num = parseFloat(this.value.replace(/[, ]+/g, ""));
            this.value=num.toLocaleString('en-US', {
              maximumFractionDigits: 2
            });
          }
//            alert(this.value);            
//            this.value = Number(amt.toFixed(2)).toLocaleString("en");
//            this.value = this.value.replace(commaRegex,"$1,");
        });
    };
    self.checkBoxHandler = function()
    {
        self.proposalTypeSelected();
        var checkbox = $(this),isChecked = false;
        self.currentTab = $("#" + $(this).attr("tabid"));
        if(this.checked)
        {
            self.currentTab.show();
            self.initDisabledBaseInfo(false);
            // Remove alert message
            $(window).unbind('beforeunload');
            var maskString = "The system is creating a new application for you. Please wait...";
            if(self.getTransId())
            {
                maskString="The system is updating your proposal for you. Please wait...";
            }
            $("#content-ct").mask(maskString);
            var url =window.SERVER_ROOT + "/proposal/saveProposalJson";
            $.ajax({
                type:'POST'
                ,data:{jsondata:JSON.stringify(self.getAllInputFields())}
                ,url:url
                ,dataType:'json'
                ,cache:false
                ,success:function(data,status,jqxhr){
                  try{
                    if(!data.success){
                      throw data.error || "Unable to remove target storage.";
                    }
                    $("#content-ct").unmask();
                    if(!self.createForm.find("#wtTransId").val())
                    {
                        window.location = window.SERVER_ROOT+"/proposal/edit/"+data['wtTransId'];
                    }
                  }catch(e){
                    $("#content-ct").unmask();
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
        }
        else
        {
            self.tabsCt = $("#" + $(this).attr("tabid"));
            self.tabsContentCt = $(self.tabsCt.find("a").attr("href"));
            var hasVal = false;
            $.each(self.tabsContentCt.find("input,textarea").not(".attachButton"),function(){
                if($(this).val())
                {
                    return hasVal = true;
                }
            });
            if(hasVal)
            {
                var msg = "Are you sure you want to remove "+$(this).attr("txtlabel")+"?";
                self.checkboxDirtyFlag();
                self.proposalValidation(msg,function(bool){
                    if(bool)
                    {
                        self.currentTab.hide();
                    }
                    else
                    {
                        self.currentTab.show();
                        self.initDisabledBaseInfo(false);
                        checkbox.prop('checked',true);
                        checkbox.val("1");
                    }
                });
            }
            else
            {
                self.currentTab.hide();
            }
            self.proposalType.find("input").each(function(){
                if(this.checked)
                {
                    isChecked=true;
                }
            });
            if(!isChecked)
            {
                self.initDisabledBaseInfo(true);
            }
        }
    };
    self.requestBtnHandler = function()
    {
        self.sellerAgencyId = self.sellerTab.find(".accordion .headerCt").attr("agencyid");
        $.ajax({
            type:"POST"
            ,url:window.SERVER_ROOT + "/proposal/emailLog"
            ,data:{wtAgencyId:self.sellerAgencyId,wtTransId:self.getTransId()}
            ,cache:false
            ,scope:this
            ,success:function(data,status,jqxhr){
                self.divCt = $("<div>").append(data);
                self.divCt.dialog({
                    title:'Email Message Log'
                    ,width: 650
                    ,height: 550
                    ,resizable: false
                    ,modal:true
                    ,buttons:[{
                        text:"Send Email"
                        ,click:function()
                        {
                            self.emailMessageLog.replyBtnHandler();
                        }
                    },{
                        text:"Close"
                        ,click:function()
                        {
                            $(this).dialog("destroy").remove();
                        }
                    }]
                }).css({overflow:'hidden'});
                self.emailMessageLog = new emailMessageLog();
                self.emailMessageLog.init();
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status == 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
            }
        });
    };
    self.initListeners=function(){
//        self.requestBt.on("click",self.requestBtnHandler); //email Window
        self.requestBt.on("click",function(){
            window.open(window['SERVER_ROOT']+"/proposal/mileStones",'_blank');
        });
        self.checkbox.find('input[type=checkbox]').on("click",self.facilitesCheckboxHandler);
        self.type.find("option").on("click",function(){
            if($(this).val()==="CEQA Document"){
                $(this).parent().parent().parent().next().removeClass('hidden');
            } else {
                $(this).parent().parent().parent().next().addClass('hidden');
            }
        });
        self.tabHeader.on("click",self.headerToggle);
        self.preTransferAddBtn.on("click",self.addPreTransfer);
        self.waterRightsAddBtn.on("click",self.addWaterRight);
    };
    self.headerToggle=function(){
        if($(this).hasClass("isExpand"))
        {
            $(this).removeClass("isExpand");
            $(this).find("img").addClass("img_rotate");
        }
        else
        {
            $(this).addClass("isExpand");
             $(this).find("img").removeClass("img_rotate");
        }
        $(this).next().slideToggle("fast");
    };

//    self.initfacilitesCheckboxHandler = function(){
////        self.checkbox.find("input[type=checkbox]").each(function(){
//
//        self.checkbox.find("input.wtFuTypeId").each(function(){
////            alert($(this).attr("name"));
////            alert($(this).val());
//            $(this).val("off");
//            if(this.checked)
//            {
//                $(this).val("on");
//            }
//        });
//
//        if($("input[name=isStateContractor]:checked").val()==="0"){
////          alert($("input[name=Banks]").val());
//          if($("input[name=Banks]").val()==="on"){
//            $(".stateContractor").removeClass('hidden').show();
//          } else {
//            $(".CVP").removeClass('hidden').show();
//          }
//        }
//    };

//    self.facilitesCheckboxHandler=function(){
//        if(this.checked)
//        {
//            $(this).val("on");
//            if($(this).attr("name") === "SWP")
//            {
//                $(this).parent().parent().find(".subSWP").removeClass("hidden").show();
////                alert($(this).parent().parent().find("input[name=Banks]").val());
//                if($(this).parent().parent().find("input[name=Banks]").val()==="on"){
//                  $(this).parent().parent().find(".subBanks").removeClass("hidden").show();
//                }
//            }
//            if($(this).attr("name") === "Banks" && $("input[name=isStateContractor]:checked").val()==="0")
//            {
//                $(this).parent().parent().find(".subBanks").removeClass("hidden").show();
//            }
//            if($(this).attr("name") === "CVP")
//            {
//                $(this).parent().parent().find(".subCVP").removeClass("hidden").show();
//                if($(this).parent().parent().find("input[name=Jones]").val()==="on"){
//                  $(this).parent().parent().find(".subJones").removeClass("hidden").show();
//                }
//            }
//            if($(this).attr("name") === "Jones")
//            {
//                $(this).parent().parent().find(".subJones").removeClass("hidden").show();
//            }
//            if($(this).attr("name") === "OTHER")
//            {
//                $(this).parent().find(".otherText").removeClass("hidden").show();
//            }
//        } else {
//            $(this).val("off");
//            if($(this).attr("name") === "SWP")
//            {
//                $(this).parent().parent().find(".subSWP").removeClass("hidden").hide();
//                $(this).parent().parent().find(".subBanks").removeClass("hidden").hide();
//            }
//            if($(this).attr("name") === "Banks")
//            {
//                $(this).parent().parent().find(".subBanks").removeClass("hidden").hide();
//            }
//            if($(this).attr("name") === "CVP")
//            {
//                $(this).parent().parent().find(".subCVP").removeClass("hidden").hide();
//                $(this).parent().parent().find(".subJones").removeClass("hidden").hide();
//            }
//            if($(this).attr("name") === "Jones")
//            {
//                $(this).parent().parent().find(".subJones").removeClass("hidden").hide();
//            }
//            if($(this).attr("name") === "OTHER")
//            {
//                $(this).parent().find(".otherText").removeClass("hidden").hide();
//            }
//        }
//    };
    self.initDisabledBaseInfo = function(bool)
    {
        self.baseinfoCt = self.baseInfoTab.find(".sub-title");
        self.baseinfoCt.addClass("disabled");
        self.baseinfoCt.find("input:not(input[name=waterRightsNum])").attr("disabled",bool);
        self.baseinfoCt.find("img").attr("disabled",bool);
        self.baseinfoCt.find("textarea").attr("disabled",bool);
        bool ? self.baseinfoCt.addClass("disabled") : self.baseinfoCt.removeClass("disabled");
    };
    self.initAttachmentUploadForm=function(){
        self.attachButton = $(".attachButton");
        self.uploadPercent = $('.uploadPercent_ct');
        self.uploadFileContainer = $(".upload-file-container");
        var wtTransId = self.createForm.find("#wtTransId").val();

        self.attachButton.on("click", function(){
          // Require to save proposal before attach files
          if (wtTransId === ""){
              alert("Please save proposal before add attachment.");
              return false;
          }
          var attachType = $(this).attr("typeid");
          var containerId =  $(this).attr("containerid");
          self.loadAttachmentUploadForm(wtTransId,attachType,containerId);
        });
    };
    self.loadAttachmentUploadForm=function(wtTransId,attachType,containerId){
      var uploadFileContainer = $("#uploadFile_ct");
      var url = window.SERVER_ROOT + "/attachment/getAttachmentChecklist?attachType="+attachType;
      $.ajax({
          type:"POST"
          ,url:url
          ,cache:false
          ,success:function(data,status,jqxhr){
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
          ,error:function(xhr, errorType, exception){
              if(xhr.status === 403)
              {
                  location = window.SERVER_ROOT;
              }
              else
              {
                  alert(exception+" status:"+xhr.status);
              }
            }
        });
    };
    self.uploadFilePopup=function(){
            self.height = $("div.upload-file-container").height()+185;
            self.uploadFileContainer.dialog({
                appendTo: "form#upload-file-form",
                modal: true,
                title: "Upload Attachment",
                width: 'auto',
                height: self.height,
                resizable:false,
                close:function(event,ui){
                    $(this).dialog("destroy").remove();
                },
                buttons:[{
                    text:"Upload"
                    ,click:function(){
                        if (self.uploadFileForm.valid() === false){
                            return false;
                        };
                        var filename = $("#file").val();
                        self.attachmentContainer = $("#"+$("#containerId").val());
                        self.fileInput = self.uploadFileForm.find("input[type=file]");
                        var form = new FormData(self.uploadFileForm[0]);
                        for(var i = 0;i<self.fileInput[0].files.length;i++) //multiple files handler
                        {
                            form.append('file',self.fileInput[0].files[0]);
                        }
                        var url = window.SERVER_ROOT + "/attachment/uploadFile";
                        // Call upload file function
                        setTimeout(self.callUploadFile(url,form,filename),0);
                        $(this).dialog("destroy").remove();
                        self.uploadFileForm.children().remove();
//                        self.uploadFileForm.find("#tblImageUpload .subdate").addClass('hidden');
                    }
                },{
                    text:"Cancel"
                    ,click:function(){
                        $(this).dialog("destroy").remove();
                        self.uploadFileForm.find("#tblImageUpload .subdate").addClass('hidden');
                    }
                }]
                }).dialog('open');

            self.uploadValidation();
//        });
    };

    self.callUploadFile=function(url,form,filename){
      var now = new Date();
      var start = now.getTime();

      $.ajax({
        xhr: function() {
          var xhr = new window.XMLHttpRequest();
          xhr.upload.addEventListener("progress", function(evt) {
            if (evt.lengthComputable) {
              var percentComplete = evt.loaded / evt.total;
              percentComplete = parseInt(percentComplete * 100);
              self.attachmentContainer.parent().mask("Loading...."+percentComplete+"%");
//              console.log(percentComplete);
//              self.uploadPercent.html("File "+ filename +" Uploading..."+percentComplete+"%");
//              self.uploadPercent.dialog({
//                title: "Upload Attachments",
//                width: 480,
//                height: 200,
//                buttons:[{
//                  text:"OK"
//                  ,click:function(){
//                    $(this).dialog('close');
//                    self.uploadPercent.children().remove();
//                  }
//                }]
//              });
              if (percentComplete === 100) {
//                self.uploadPercent.html("File "+ filename +" Uploaded!");
              }
            }
          }, false);

          return xhr;
        },
        type:"POST"
        ,url:url
        ,data:form
        ,cache:false
        ,processData: false
        ,contentType: false
        ,success:function(data,status,jqxhr){
            var now = new Date();
            var end = now.getTime();
            self.attachmentContainer.parent().unmask();
            self.attachmentContainer.children().remove();
            self.attachmentContainer.append(data);
        }
        ,error:function(xhr, errorType, exception){
            if(xhr.status == 403)
            {
                location = window.SERVER_ROOT;
            }
            else
            {
                alert("Upload Failed!");
//                alert("File "+ filename +" Failed Upload!");
            }
        }
      });
    };
    self.uploadValidation=function(){
        self.uploadFileForm = $("#upload-file-form");
        self.uploadFileForm.validate({
            rules:{
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

    self.fieldsValidation=function(){
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
            onfocusout: function(element){
              $(element).valid();
            },
            ignore:[],
            rules:{
                transYear: "required",
                proTransQua: "required",
                surWaterSource: "required",
                majorRiverAttribute: "required",
                transWinStart: "required",
                transWinEnd: "required",
                reqExpFrom: "required",
                reqExpTo: "required",
                typeMenuHide: {
                  required: {
                    depends: function(element){
                      return $('.tab-check:checked').size()===0;
                    }
                  }
                },
                wtFuTypeIdHide: {
                  required: {
                    depends: function(element){
                      return $('.wtFuTypeId:checked').size()===0;
                    }
                  }
                },
                envRegComplHide: {
                  required: {
                    depends: function(element){
                      return $('.envRegCompl:checked').size()===0;
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
                  range: [0,100]
                },
                waterTransQuaCI: validateField,
                totalTransferAcr: validateField,
                proTransferByCI: validateField,
                proTransferByCS: validateField,
                waterTransQuaRV: validateField,
                topAllowStorage: validateField,
                targetStorage: validateField,
                locationLat:validateField,
                locationLong:validateField
            },
            messages: {
                transYear: labels,
                proTransQua: labels,
                surWaterSource: labels,
                majorRiverAttribute: labels,
                transWinStart: labels,
                transWinEnd: labels,
                reqExpFrom: labels,
                reqExpTo: labels,
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
                proTransferByCI: labels,
                proTransferByCS: labels,
                waterTransQuaRV: labels,
                topAllowStorage: labels,
                targetStorage: labels,
                locationLat:labels,
                locationLong:labels
            }
        });
    };

//    self.saveValidation=function(){
//        var validateField = {
//            number: true
//        };
//        var labels = {
//            number: "<p>Please enter a valid number.</p>",
//            range: "<p>Please enter a value between 12 and 100.</p>"
//        };
//
//        var validate = self.createForm.validate({
//            rules:{
//                proTransQua: validateField,
//                proUnitCost: validateField,
//                pumpingWellsNumber: validateField,
//                monitoringWellsNumber: validateField,
//                totalPumping: validateField,
//                basePumping: validateField,
//                depletionFactor: validateField,
//                totalTransferAcr: validateField,
//                proTransferByCI: validateField,
//                proTransferByCS: validateField,
//                topAllowStorage: validateField,
//                targetStorage: validateField,
//                resDownstream: validateField,
//                riverMileStart: validateField,
//                riverMileEnd: validateField
//            },
//            messages: {
//                proTransQua: labels,
//                proUnitCost: labels,
//                pumpingWellsNumber: labels,
//                monitoringWellsNumber: labels,
//                totalPumping: labels,
//                basePumping: labels,
//                depletionFactor: labels,
//                totalTransferAcr: labels,
//                proTransferByCI: labels,
//                proTransferByCS: labels,
//                topAllowStorage: labels,
//                targetStorage: labels,
//                resDownstream: labels,
//                riverMileStart: labels,
//                riverMileEnd: labels
//            }
//        });
//
//        return validate;
//    };

    self.submitValidation=function(){
        var labels = {
            required: "<p>This field is required</p>"
        };
//        var requiredField = {
//            required: true
//        };
        var validate = self.createForm.validate({
            rules:{
//                transYear: "required",
//                proTransQua: "required",
//                surWaterSource: "required",
//                majorRiverAttribute: "required",
//                transWinStart: "required",
//                transWinEnd: "required",
//                reqExpFrom: "required",
//                reqExpTo: "required"
//                SWRCB: {
//                  required: {
//                    depends: function(element){
//                      return $('.envRegCompl:checked').size()===0;
//                    }
//                  }
//                }
            },
            messages: {
//                transYear: labels,
//                proTransQua: labels,
//                surWaterSource: labels,
//                majorRiverAttribute: labels,
//                transWinStart: labels,
//                transWinEnd: labels,
//                reqExpFrom: labels,
//                reqExpTo: labels
//                SWRCB: labels
            }
        });

        return validate;
    };

    self.validTabs=function(param) {
      var valid = true;
      var validator = "";
//      if (param === "SAVE"){
//          validator = self.saveValidation();
//      }
      if (param === "SUBMIT"){
          validator = self.submitValidation();
//          validator = self.fieldsValidation();
      }
      // Validate Other Tabs
//      var tabChecked = self.baseInfoTab.find("#typeMenu input");
//      tabChecked.each(function(){
//        if($(this).is(':checked')){
//            var tabId = $(this).attr("hrefid");
//            var inputs = $("#"+tabId).find("input.validField");
//            inputs.each(function() {
//                if ($(this).val()!=="" && !validator.element(this)) {
//                    valid = false;
//                }
//            });
//        }
//      });
      //Validate Base info Tab
      var baseInputs = self.baseInfoTab.find(".validField");
      baseInputs.each(function() {
          if (!validator.element(this) && valid) {
              valid = false;
          }
      });
      return valid;
    };

    self.initCalendar=function(){
        $(".dateField").datepicker();

//        $("#dwrProApprDate").datepicker();
//        $("#Start").datepicker();
//        $("#transWinEnd").datepicker();
        $("#proposedSchedule").datepicker();
        $("#submittedDate").datepicker();
//        var year = new Date().getFullYear();
//        self.transferStartDate.datepicker({
//            minDate:new Date(year,0,1)
//            ,maxDate:new Date(year,11,31)
//            ,onSelect:function(date){
//                var selectedDate = new Date(date);
//                self.transferEndDate.datepicker("option","minDate",selectedDate);
//                self.transferEndDate.datepicker("option","maxDate",new Date(year,11,31));
//            }
//        });
//        self.transferEndDate.datepicker({
//            minDate:new Date(year,0,1)
//            ,maxDate:new Date(year,11,31)
//            ,onSelect:function(date){
//                var selectedDate = new Date(date);
//                self.transferStartDate.datepicker("option","minDate",new Date(year,0,1));
//                self.transferStartDate.datepicker("option","maxDate",selectedDate);
//            }
//        });
    };

    self.initCheckboxStatus = function()
    {
        self.checkboxFlag = false;
        self.proposalType.find("input").each(function(){
            if($(this).is(':checked')){
                self.checkboxFlag= true;
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

    self.proposalTypeSelected=function(){
        $("#CropIdling,#Reservoir,#GroundWater,#Conserved,#Other").hide();
        var isChecked = false;
        isChecked = self.initCheckboxStatus();
        if(!isChecked)
        {
            self.initDisabledBaseInfo(true);
        }
    };

    self.currencyValidation = function(currency)
    {
        var regex = /(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$/;
        var val = currency.match(regex);
        if(val === null)
        {
            self.unitCostField.css({border:'1px solid red'});
            return "NaN";
        }
        else
        {
            self.unitCostField.css({border:'1px solid #a6a8a8'});
            var nonComma = val[0].replace(/[, ]+/g, "").trim();
            var non$ = nonComma.split("$");
            if(non$.length>1)
            {
                return non$[1];
            }
            else{
                return nonComma;
            }
        }
    };

    self.saveProposalProcess=function(){
        self.saveButton = self.proposalProcessTab.find("#saveProcess");
        self.saveButton.on("click", function() {
//            var wtTransId = self.createForm.find("#wtTransId").val();
            var data = self.processForm.serialize();
//            var url =window.SERVER_ROOT + "/proposal/saveProposalProcess?wtTransId="+wtTransId;
            var url =window.SERVER_ROOT + "/proposal/saveProposalProcess";
            $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,dataType:'json'
                ,success:function(data,status,jqxhr){
                  try{
                    if(!data.success){
                      throw data.error || "Unable to save proposal process.";
                    }
                    alert("Process Saved!");
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
                ,error:function(xhr, errorType, exception){
                    if(xhr.status == 403)
                    {
                        location = window.SERVER_ROOT;
                    }
                }
            });
        });
    };

//    self.reviewProposal=function(){
//        $("#reviewProposal").on("click", function() {
//            var wtTransId = self.createForm.find("#wtTransId").val();
//            var url =window.SERVER_ROOT + "/proposal/review?wtTransId="+wtTransId;
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,success:function(data,status,jqxhr){
//                    alert("Proposal Under Review!");
//                    var url=window.SERVER_ROOT + "/proposal/?moduleType=review";
//                    window.location.assign(url);
//                }
//            });
//        });
//    };

    self.approveProposal=function(){
        $("#approveProposal").on("click", function() {
            var wtTransId = self.createForm.find("#wtTransId").val();
            var url =window.SERVER_ROOT + "/proposal/approve?wtTransId="+wtTransId;
            var msg = "Are you sure you want to approve this proposal?";
            self.proposalValidation(msg,function(bool){
              if(bool){
                $.ajax({
                    type:"POST"
                    ,url:url
                    ,dataType:'json'
                    ,cache:false
                    ,success:function(data,status,jqxhr){
                      try{
                        if(!data.success){
                          throw data.error || "Unable to update the proposal.";
                        }
                        alert("Proposal Approved!");
                        var url=window.SERVER_ROOT + "/proposal/?moduleType=approve";
                        window.location.assign(url);
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
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status == 403)
                        {
                            location = window.SERVER_ROOT;
                        }
                    }
                });
              }
            });
        });
    };
    self.suspendProposal=function(){
        $("#suspendProposal").on("click", function() {
            var wtTransId = self.createForm.find("#wtTransId").val();
            var url =window.SERVER_ROOT + "/proposal/suspend?wtTransId="+wtTransId;
            var msg = "Are you sure you want to suspend this proposal?";
            self.proposalValidation(msg,function(bool){
              if(bool){
                $.ajax({
                    type:"POST"
                    ,url:url
                    ,dataType:'json'
                    ,cache:false
                    ,success:function(data,status,jqxhr){
                      try{
                        if(!data.success){
                          throw data.error || "Unable to update the proposal.";
                        }
                        alert("Proposal Suspend!");
                        var url=window.SERVER_ROOT + "/proposal/?moduleType=suspend";
                        window.location.assign(url);
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
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status == 403)
                        {
                            location = window.SERVER_ROOT;
                        }
                    }
                });
              }
            });
        });
    };

    self.completeProposal=function(){
        $("#completeProposal").on("click", function() {
            var wtTransId = self.createForm.find("#wtTransId").val();
            var url =window.SERVER_ROOT + "/proposal/complete?wtTransId="+wtTransId;
            var msg = "Are you sure this proposal is completed?";
            self.proposalValidation(msg,function(bool){
              if(bool){
                $.ajax({
                    type:"POST"
                    ,url:url
                    ,dataType:'json'
                    ,cache:false
                    ,success:function(data,status,jqxhr){
                      try{
                        if(!data.success){
                          throw data.error || "Unable to update the proposal.";
                        }
                        alert("Proposal is complete!");
                        var url=window.SERVER_ROOT + "/proposal/?moduleType=review";
                        window.location.assign(url);
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
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status == 403)
                        {
                            location = window.SERVER_ROOT;
                        }
                    }
                });
              }
            });
        });
    };

    self.trackProposal=function(){
        $("#trackProposal").on("click", function() {
            var wtTransId = self.createForm.find("#wtTransId").val();
            var url =window.SERVER_ROOT + "/proposal/track?wtTransId="+wtTransId;
            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,dataType:'json'
                ,success:function(data,status,jqxhr){
                  try{
                    if(!data.success){
                      throw data.error || "Unable to update the proposal.";
                    }
                    alert("Proposal is under tracking!");
                    var url=window.SERVER_ROOT + "/proposal/?moduleType=track";
                    window.location.assign(url);
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
                ,error:function(xhr, errorType, exception){
                    if(xhr.status === 403) //session ends
                    {
                        location = window.SERVER_ROOT;
                    }
                }
            });
        });
    };

    // <editor-fold desc="Previous Tansfer Code.">
    self.addPreTransfer=function(){
        if(self.getTransId() === "")
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
    self.preTransferItems=function(){
        self.ptTable = $("table.pre_transfer_table");
        self.typeCheckbox = self.ptTable.find(".transType");
        self.ptSaveBtn = self.ptTable.find("img.save-icon");
        self.ptEditBtn = self.ptSaveBtn.parent().next().find("img.edit-icon");
        self.ptRemoveBtn = self.ptEditBtn.parent().next().find("img.delete-icon");
        self.ptAddBtn = self.ptTable.parent().parent().find("#pre_transfer_addBtn");
        self.ptTableCheckRows();
        self.preTransferListeners();
    };
    self.preTransferListeners=function(){
        self.typeCheckbox.unbind("click",self.ptCheckboxHandler);
        self.typeCheckbox.on("click",self.ptCheckboxHandler);
        self.ptSaveBtn.unbind("click",self.ptSaveHandler);
        self.ptSaveBtn.on("click",self.ptSaveHandler);
        self.ptEditBtn.unbind("click",self.ptEditHandler);
        self.ptEditBtn.on("click",self.ptEditHandler);
        self.ptRemoveBtn.unbind("click",self.ptRemoveHandler);
        self.ptRemoveBtn.on("click",self.ptRemoveHandler);
    };
    self.ptCheckboxHandler=function(){
      if(this.checked){
        $(this).val("1");
      } else {
        $(this).val("0");
      }
    };
    self.ptSaveHandler=function(){
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        self.row = $(this).parent().parent();
        self.swpaonum = self.row.find("input[name=swpaoContractNum]");
        self.recomnum = self.row.find('input[name=recomNum]');
        self.isci = self.row.find('input[name=isTypeCi]');
        self.isrv = self.row.find('input[name=isTypeRv]');
        self.isgw = self.row.find('input[name=isTypeGw]');
        if(!self.ptTableValidation(self.swpaonum,self.recomnum)){
          return false;
        }
//        alert(self.swpaonum.val());
//        if(!self.type.val()){
//          alert("Choose one of the water rights type.");
//          return false;
//        }
        self.swpaonum.attr("disabled",true);
        self.recomnum.attr("disabled",true);
        self.isci.attr("disabled",true);
        self.isrv.attr("disabled",true);
        self.isgw.attr("disabled",true);
        $(this).hide();
        self.ptId = self.row.find("input[name=wtPreTransferId]");
        self.baseInfoTab.mask("Saving data...");
        $.ajax({
            url:window["SERVER_ROOT"]+"/proposal/savePreTransfer"
            ,data:{swpaoContractNum:self.swpaonum.val(),recomNum:self.recomnum.val(),isTypeCi:self.isci.val(),
                    isTypeRv:self.isrv.val(),isTypeGw:self.isgw.val(),wtTransId:self.getTransId(),wtPreTransferId:self.ptId.val()}
            ,cache:false
            ,dataType:'json'
            ,success:function(data,status,jqxhr)
            {
              try{
                if(!data.success){
                  throw data.error || "Unable to save this transfer.";
                }
                self.baseInfoTab.unmask();
                alert("Saved Successfully!");
                self.ptId.val(data.wtPreTransferId);
              }catch(e){
                self.baseInfoTab.unmask();
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
    };
    self.ptEditHandler=function(){
        if($(this).attr("disabled") === "disabled")
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
        self.swpaonum.attr("disabled",false);
        self.recomnum.attr("disabled",false);
        self.isci.attr("disabled",false);
        self.isrv.attr("disabled",false);
        self.isgw.attr("disabled",false);
        self.saveBt.removeClass("hidden").show();
        alert("You can now edit the textfield. Please be sure to save when done.");
    };
    self.ptRemoveHandler=function(){
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        self.row = $(this).parent().parent();
        self.ptId = self.row.find("input[name=wtPreTransferId]");
        if(self.ptId.val() === "")
        {
            self.row.remove();
            self.ptTableCheckRows();
            return false;
        }
        self.msg="Are you sure you want to remove this Transfer?";
        var data = {wtPreTransferId:self.ptId.val()};
        self.proposalValidation(self.msg,function(bool){
            if(bool){
                $.ajax({
                    url:window["SERVER_ROOT"]+"/proposal/removePreTransfer"
                    ,data:data
                    ,cache:false
                    ,dataType:'json'
                    ,success:function(data,status,jqxhr)
                    {
                      try{
                        if(!data.success){
                          throw data.error || "Unable to remove this transfer.";
                        }
                        self.row.remove();
                        self.ptTableCheckRows();
                        alert("Removed Successfully!!!");
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
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status === 403) //session ends
                        {
                            location = window.SERVER_ROOT;
                        }
                        else{
                            alert("Failed to remove");
                        }
                    }
                });
            }
        });
    };
    self.ptTableCheckRows=function(){
        self.ptRow= self.ptTable.find("tbody tr");
        if(self.ptRow.length === 1)
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
    self.ptTableValidation=function(swpaonum,recomnum){
      if(swpaonum.val()==="" && recomnum.val()===""){
        alert("Please filled in SWAPO Contract No or Recommendation No.");
        return false;
      }
      return true;
    };
    // </editor-fold>

    // <editor-fold desc="Water Rights Code">
    self.addWaterRight=function(){
        if(self.getTransId() === "")
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
    self.waterRightItems=function(){
        self.wrTable = $("table.water_rights_table");
        self.wrCombobox = self.wrTable.find("select");
        self.wrSaveBtn = self.wrTable.find("img.save-icon");
        self.wrEditBtn = self.wrSaveBtn.parent().next().find("img.edit-icon");
        self.wrRemoveBtn = self.wrEditBtn.parent().next().find("img.delete-icon");
        self.waterRightListeners();
        self.intHandler();
    };
    self.waterRightListeners=function(){
        self.wrSaveBtn.unbind("click",self.wrSaveHandler);
        self.wrSaveBtn.on("click",self.wrSaveHandler);
        self.wrEditBtn.unbind("click",self.wrEditHandler);
        self.wrEditBtn.on("click",self.wrEditHandler);
        self.wrRemoveBtn.unbind("click",self.wrRemoveHandler);
        self.wrRemoveBtn.on("click",self.wrRemoveHandler);
        self.wrCombobox.unbind("change",self.wrSelectHandler);
        self.wrCombobox.on("change",self.wrSelectHandler);
    };
    self.wrSelectHandler=function(){
        self.wrLabel = $(this).parent().next().find("label");
        self.wrLabel.html($(this).val()+" #");
    };
    self.wrCheckEmptyTable=function(){
        self.wrRow= self.wrTable.find("tbody tr");
        if(self.wrRow.length === 1)
        {
            self.wrTable.addClass("hidden");
        }
    };
    self.wrSaveHandler=function(){
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        self.row = $(this).parent().parent();
        self.type = self.row.find("select");
        self.wrnum = self.row.find('input[name=waterRightsNum]');
        self.volume = self.row.find('input[name=proposedTransVol]');
        if(!self.type.val()){
          alert("Choose one of the water rights type.");
          return false;
        }
//        if(!self.type.val())
//        {
//            return false;
//        }
        var icon = this;
        self.wrId = self.row.find("input[name=wtWaterRightsId]");
        self.baseInfoTab.mask("Saving water rights...");
        $.ajax({
            url:window["SERVER_ROOT"]+"/proposal/saveWaterRight"
            ,data:{waterRightsType:self.type.val(),waterRightsNum:self.wrnum.val(),proposedTransVol:self.volume.val(),wtTransId:self.getTransId(),wtWaterRightsId:self.wrId.val()}
            ,cache:false
            ,dataType:'json'
            ,success:function(data,status,jqxhr)
            {
              try{
                if(!data.success){
                  throw data.error || "Unable to save water right.";
                }
                self.baseInfoTab.unmask();
                self.type.attr("disabled",true);
                self.wrnum.attr("disabled",true);
                self.volume.attr("disabled",true);
                $(icon).hide();
                alert("Saved Successfully!");
                self.wrId.val(data.wtWaterRightsId);
              }catch(e){
                self.baseInfoTab.unmask();
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
    };
    self.wrEditHandler=function(){
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        self.row = $(this).parent().parent();
        self.type = self.row.find("select");
        self.wrnum = self.row.find('input[name=waterRightsNum]');
        self.volume = self.row.find('input[name=proposedTransVol]');
        self.saveBt = self.row.find(".save-icon");
        self.type.attr("disabled",false);
        self.wrnum.attr("disabled",false);
        self.volume.attr("disabled",false);
        self.saveBt.removeClass("hidden").show();
        alert("You can now edit the textfield. Please be sure to save when done.");
    };
    self.wrRemoveHandler=function(){
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        self.row = $(this).parent().parent();
        self.combo = self.row.find("select");
        self.type = self.row.find('input[name=waterRightsNum]');
        self.wrId = self.row.find("input[name=wtWaterRightsId]");
        self.volume = self.row.find('input[name=proposedTransVol]');
        if(self.wrId.val() === "")
        {
            self.row.remove();
            self.wrCheckEmptyTable();
            return false;
        }
        self.msg="Are you sure you want to remove this Water Rights?";
//        var data = {waterRightsType:self.combo.val(),waterRightsNum:self.type.val(),proposedTransVol:self.volume.val(),wtTransId:self.getTransId(),wtWaterRightsId:self.wrId.val()};
        var data = {wtWaterRightsId:self.wrId.val()};
        self.proposalValidation(self.msg,function(bool){
            if(bool){
                $.ajax({
                    url:window["SERVER_ROOT"]+"/proposal/removeWaterRight"
                    ,data:data
                    ,cache:false
                    ,dataType:'json'
                    ,success:function(data,status,jqxhr)
                    {
                      try{
                        if(!data.success){
                          throw data.error || "Unable to remove water right.";
                        }
                        self.row.remove();
                        self.wrCheckEmptyTable();
                        alert("Removed Successfully!!!");
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
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status === 403) //session ends
                        {
                            location = window.SERVER_ROOT;
                        }
                        else{
                            alert("Failed to remove");
                        }
                    }
                });
            }
        });
    };
    // </editor-fold>

    self.proposalValidation = function(msg,callback)
    {
        $("<div>",{
            html:'<p>'+msg+'</p>'
        }).dialog({
            title:'Proposal Validation'
            ,modal:true
            ,width:420
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
                    callback(false);
                    $(this).dialog("destroy").remove();
                }
            }]
        }).dialog("open");
    };

    self.checkboxDirtyFlag = function()
    {
        self.proposalType.find("input").on("change",function(){
            self.jsonData[$(this).attr("name")]=$(this).val();
            $(this).addClass("isDirty");
        });
    };
    self.checkDirtyFlag = function()
    {
        self.createForm.find("input").on("change",function(val,key){
            if($(this).attr("class") !== "tab-check")
            {
                self.jsonData[$(this).attr("name")]=$(this).val();
                $(this).addClass("isDirty");
//                alert($(this).val());
            }
        });
        self.createForm.find("textarea").change(function(val,key){
            self.jsonData[$(this).attr("name")]=$(this).val();
            $(this).addClass("isDirty");
        });
        self.createForm.find("select").change(function(val,key){
            self.jsonData[$(this).attr("name")]=$(this).val();
            $(this).addClass("isDirty");
//            alert($(this).val());
        });
    };
    self.getAllInputFields = function()
    {
        var inputs = new Object();
        self.createForm.find("input").each(function(val,key){
            inputs[$(this).attr("name")]=$(this).val();
        });
        self.createForm.find("textarea").each(function(val,key){
            inputs[$(this).attr("name")]=$(this).val();
        });
        if(this.getBuyerTab().length>0)
        {
            inputs['buyerTab'] = self.getBuyerTab();
        }
        if(this.getSellerTab().WT_AGENCY_ID > 0)
        {
            inputs['sellerTab'] = this.getSellerTab();
        }
        return inputs;
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
          var percent = $(this).next().find("input[name=water_percent]").val();
          if(isNaN(percent)){
            percent = 0;
          }
            agencyIdCollection.push({
                WT_AGENCY_ID:parseInt($(this).attr("agencyid"))
                ,SHARE_PERCENT:parseFloat(percent)
            });
        });
//       var buyersContactId = self.buyersContactTable.find("tbody tr:visible").attr("id");
       self.buyersContactTable.find("tbody tr").each(function(){
         if(!$(this).hasClass("hidden")){
            var buyersContactId = $(this).attr("id");
            agencyIdCollection.push({WT_CONTACT_ID:parseInt(buyersContactId)});
         }
        });

        return agencyIdCollection;
    };
    self.getParam=function(){
        return self.jsonData;
    };
    self.getCountyIdList = function(){
      self.county = self.baseInfoTab.find("input[name=countyId]");
      var result = [];
      $.each(self.county,function(){
        result.push($(this).val());
      });
      return result;
    };
    self.getPurposeIdList = function(){
      self.purposeRes = self.reservoirTab.find("input[name=purposeId]");
      var result = [];
      $.each(self.purposeRes,function(){
        result.push($(this).val());
      });
      return result;
    };
    self.getUrlParameter=function()
    {
        return location.pathname.indexOf(window.SERVER_ROOT+"/proposal/edit");
    };
    self.getTransId = function()
    {
        var id = "";
        var url = window.location.href.split("/");
        if($.isNumeric(url[url.length-1]))
        {
            id = url[url.length-1];
        }
//        try{id=parseInt(/\/wtims\/proposal\/edit\/(\d*)/.exec(location.pathname).pop());}catch(e){}
        return id;
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
};


