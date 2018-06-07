var AssociateWells = function(){
  var self = this;
  self.init=function(){
      self.initItems();
      self.initListeners();
      self.checkComplete();
  };
  self.initItems=function(){
      self.transItems();
      self.wellListTableItems();
      self.wellFormItems();
      self.wellsMapIframeItems();
  };
  self.transItems = function(){
      self.proposalCt = $("#tabs");
      self.createForm = self.proposalCt.find("#create-form");
      self.uploadFileContainer = $("#uploadFile_ct");
      self.jsonData = new Object();
  };
  self.wellsMapIframeItems = function()
  {
      self.associatedTabCt = $("#listAssociateWellCt");
      self.iframe = $("#associated_well_iframe");
//      self.iframe = self.associatedTabCt.parent().find("#associated_well_iframe");
      self.wellAddBtn = self.associatedTabCt.find(".attach-button input.addWell");
      self.exportExcelBtn = self.associatedTabCt.find(".attach-button input.excel_export");
  };
  self.wellListTableItems=function(){
      self.table = $("table#associateWellCt");
      self.editBtn = self.table.find(".editWells");
      self.removeBtn = self.table.find(".removeWells");
      self.filterWellCt = self.table.prev();
      self.transferCbx = self.table.find("#transfer");
      self.monitoringCbx = self.table.find("#monitoring");
      self.transferCbx.prop('checked', false);
      self.monitoringCbx.prop('checked', false);
      self.table.tablesorter({
        headers: {
          3: {sorter: false },
          4: {sorter: false },
          5: {sorter: false },
          6: {sorter: false },
          7: {sorter: false }
        }
      });
  };
  self.wellFormItems = function(){
      self.attachmentContainer = $("div.associateWellTableCt");
      self.form = $("div#wellListCt");
      self.radiobtn = self.form.find("input[name=welltype]");
      self.backBtn = self.form.find("input[name=back]");
      self.saveBtn = self.form.find("input[name=save]");
      self.attachBtn = self.form.find("input[name=attachFile]");
      self.cancelBtn = self.form.find("input[name=cancel]");
      self.attachment = self.form.find(".attachCt");
      self.isTransferWell = self.form.find("span.isTransfer");
      self.isMonitoringWell = self.form.find("span.isMonitoring");
      self.wellCapacity = self.form.find("input[name=wellCapacity]");
      self.wellCapacity.val(0);
      self.wellCapacityHelp = self.form.find("img");
      self.checkListTable = $("table#checkListCt");
      self.wellCapacityUnits = self.form.find("input[name=meterUnits]");
      self.latitude = self.form.find("input[name=latitude]");
      self.longitude = self.form.find("input[name=longitude]");
      self.lastCalibrateDate = self.form.find("input[name=lastCalibrateDate]");
      self.meterLastInstallDate = self.form.find("input[name=meterLastInstall]");
      self.attributeYear = self.form.find("input[name=attributeYear]");
      self.meterMake = self.form.find("input[name=meterMake]");
  };
  self.wellsAttachmentItems = function(){
      self.attachmentTable = $("table#attachmentCt");
      self.attachEdit = self.attachmentTable.find("tbody tr td img.editAttach");
      self.attachRemove = self.attachmentTable.find("tbody tr td img.removeAttach");
  };
  self.initAfterRender = function(){
      self.wellsAttachmentItems();
      self.afterRenderListener();
  };
  self.initAfterRenderIframe = function()
  {
      self.checkComplete();
      self.wellListTableItems();
      self.editBtn.on("click",self.initEdit);
      self.removeBtn.on("click",self.initRemove);
      self.transferCbx.on('click',self.filterWells);
      self.monitoringCbx.on('click',self.filterWells);
//      self.attachBtn.on("click",self.initAttachFile);
  };
  self.afterRenderListener=function(){
    self.attachEdit.on("click",self.initEditWindow);
    self.attachRemove.on("click",self.initRemoveWindow);
    setTimeout(function(){
      if(self.getCheckList().wellTransfer < 1)
      {
        self.isTransferWell.hide();
      }
      if(self.getCheckList().wellMonitoring < 1)
      {
        self.isMonitoringWell.hide();
      }
    },100);
  };
  self.initListeners=function(){
    self.editBtn.on("click",self.initEdit);
    self.removeBtn.on("click",self.initRemove);
    self.saveBtn.on("click",self.initSave);
//    self.attachBtn.on("click",self.initAttachFile);
    self.radiobtn.on("click",self.initCheckListener);
//    self.wellAddBtn.on("click",self.initIframeMapDialog);
    self.wellCapacity.on("blur",self.initUnitCheck);
    self.wellCapacityHelp.on("click",self.wellCapHelpTip);
    self.wellCapacityUnits.on("click",self.initUnitCheck);
    self.lastCalibrateDate.on("change",self.initCalibratedDateCheck);
    self.cancelBtn.click(function(){
        self.table.removeClass("hidden");
        self.form.addClass("hidden");
        self.wellAddBtn.removeClass("hidden");
    });
    self.filterWellCt.find("input[type=radio]").on('click',self.filterWellType);
    self.transferCbx.on('click',self.filterWells);
    self.monitoringCbx.on('click',self.filterWells);
    self.exportExcelBtn.on("click",self.initExportData);

    self.attributeYear.on("keyup",function(){
      this.value = this.value.replace(/[^0-9\.]+/g,'');
    });
    self.initDatepicker();
  };
  self.initDatepicker = function(){
    self.lastCalibrateDate.datepicker({
      onSelect: function() {
        self.meterMake.focus();
      }
    });
    self.meterLastInstallDate.datepicker({
      onSelect: function() {
        self.attributeYear.focus();
      }
    });
  };
  self.filterWells=function(){
    var tableRow = self.table.find("tr").not(":first");
    $.each(tableRow,function(){
      var transferVal = $(this).find("td[fieldname=wellTransfer]").html();
      var monitoringVal = $(this).find("td[fieldname=wellMonitoring]").html();
      if(self.transferCbx.is(':checked') && self.monitoringCbx.is(':checked')){
        if(transferVal === "Yes" && monitoringVal === "Yes"){
          $(this).show();
        } else {
          $(this).hide();
        }
      } else if(self.transferCbx.is(':checked')){
        if(transferVal === "No")
          $(this).hide();
        else
          $(this).show();
      } else if(self.monitoringCbx.is(':checked')){
        if(monitoringVal === "No")
          $(this).hide();
        else
          $(this).show();
      } else {
        $(this).show();
      }
    });
  };
  self.initExportData = function(){
      var tableArry = [];
      var tableRow = self.table.find("tr").not(":first");
      if(tableRow.length<1)
      {
          alert('The table is empty');
          return false;
      }
      self.associatedTabCt.mask("Exporting to Excel...");
      $.each(tableRow,function(){
          self.json = self.buildData($(this).find("td"));
          tableArry.push(self.json);
      });
      if(tableArry.length>0)
      {
        $.ajax({
          url:window.SERVER_ROOT+"/report/excelExport"
          ,data:{jsonData:JSON.stringify(tableArry)}
          ,cache:false
          ,async:false
          ,timeout: 30000
          ,type:'POST'
          ,dataType:'json'
          ,success:function(response,status,jqxhr){
            try{
              if(!response.success){
                throw response.error || "Unable to remove target storage.";
              }
              self.associatedTabCt.unmask();
//              $(window).unbind('beforeunload');
              location = window.SERVER_ROOT+'/report/downloadAssociatedWellExport';
//              $(window).bind("beforeunload", function(){ return(false); });
            }catch(e){
              self.associatedTabCt.unmask();
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
          ,error:function(xhr,errorType,exception){
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
          }
        });
      }
  };
  self.initNextWell = function(){
    var tablerow = self.table.find("tr").not(":first");
    $.each(tablerow,function(){
      console.log($(this).html());
      var wellType = $(this).find("td[fieldname=wellType]").html();
//      alert(wellType);
      if (wellType === ""){
        var wellData = $(this).find("td");
//        console.log(self.tableData);
        self.dialogWellForm(wellData);
        return false;
      }
    });
  };
  self.checkComplete = function(){
    var tablerow = self.table.find("tr").not(":first");
    var requireFields = ["lastCalibrateDate","meterMake","meterModel","wellCapacity","wellTransfer","wellMonitoring"];
    $.each(tablerow,function(){
      var tabledata = $(this).find('td');
      var hasData = true;
      var checklist = $(this).find("td.checklist");
      if(checklist.html() === "false")
      {
          hasData = false;
//                    return this;
      }
      if($(this).find("td[fieldname=wellTransfer]").html() === "No" && $(this).find("td[fieldname=wellMonitoring]").html() === "No"){
        hasData = false;
      }
      if($(this).find("td[fieldname=wellTransfer]").html() === "Yes")
      {
          $.each(tabledata,function(){
              var data = $(this).attr("fieldname");
              if($.inArray(data,requireFields) > -1)
              {
                  if(!$(this).html())
                  {
                      hasData = false;
                  }
              }
          });
      }
      if(hasData)
      {
        $(this).find('.complete').html('<img class="editWells" src="${pageContext.request.contextPath}/resources/images/icons/tick.png"/>');
      } else {
        $(this).find('.complete').html("");
      }
    });
  };
  self.filterWellType = function(){
    var type = new Object();
    $.each($(this).parent().find("input[type=radio]"),function(i){
      type[$(this).attr("name")] = this.checked;
      if(this.checked)
      {
          type[$(this).attr("name")] = this.checked;
      }
    });
    self.associatedTabCt.mask("Loading...");
    $.ajax({
      url:window.SERVER_ROOT+"/attachment/filterAssociatedWell"
      ,data:{wtTransId:self.getTransId(),welltype:JSON.stringify(type)}
      ,type:'POST'
      ,cache:false
      ,success:function(data,status,jqxhr){
        self.associatedTabCt.unmask();
        var table = $(data)[0];
        self.table.removeClass("hidden");
        self.table.children().remove();
        self.table.append($(table).html());
        self.initAfterRenderIframe();
      }
      ,error:function(xhr, errorType, exception){
        if(xhr.status === 403) //session ends
        {
            location = window.SERVER_ROOT;
        }
        else
        {
             alert("Exception:"+exception.toString());
        }
      }
    });
  };
  self.wellCapHelpTip=function(){
    var msg = "Meter Units CFS is cubic feet per second and GPM is gallons per minute";
    self.helpCt = $("<div/>");
    self.helpCt.html('<p>'+msg+'</p>');
    self.helpCt.dialog({
        title:'Help Information'
        ,modal:true
        ,width:400
        ,height:300
        ,buttons:[{
            text:"Close"
            ,click:function(){
                  $(this).dialog('close');
            }
        }]
    }).dialog("open");
  };
  self.initIframeMapDialog=function(){
      self.iframe.removeClass('hidden');
      var src = self.iframe.attr("srcpath");
      self.iframe.attr("src",src);
      self.iframe.dialog({
        width:1075
        ,height:800
        ,resizable:true
        ,autoFocus:false
        ,modal:true
        ,open: function(event, ui) {
              $("button").blur();
           }
        ,beforeClose:function(event,ui){
          
            var wellNameList = document.getElementById(self.iframe.attr("id")).contentWindow.MapLite._wellNameList;
            document.getElementById(self.iframe.attr("id")).contentWindow.MapLite.openSearchWellsWindow();
            document.getElementById(self.iframe.attr("id")).contentWindow.MapLite.clearSearchResults();
            if(wellNameList !== "" && wellNameList.length > 0)
            {
              self.initAddWellList(wellNameList);
              document.getElementById(self.iframe.attr("id")).contentWindow.MapLite._wellNameList=[];
            }
        }
      });
      var isExpand = true;
      self.iframe.prev().append("<button type='button' class='ui-widget-header ui-button ui-widget expandBtn'><span class='ui-icon ui-icon-arrowthick-2-ne-sw'></span></button>");
      self.iframe.prev().find("button.expandBtn").on("click",function(){
      if(isExpand)
      {
        self.iframe.dialog("option","height",$(window).height()+17);
        self.iframe.parent().css({
            'left': '0px',
            'top':'0px',
            'width':$(window).width()-12,
            'height':$(window).height()-10
        });
        $(window).scrollTop(0);
        isExpand = false;
      }
      else
      {
        self.iframe.dialog("option","width","1000");
        self.iframe.dialog("option","height","800");
        self.iframe.parent().css({
            'left': ($(window).width()*.2)+'px',
            'top':($(window).height()*.1)+'px'
        });
        isExpand = true;
      }
    });
  };
  self.initAddWellList = function(jsonObj)
  {
    self.associatedTabCt.mask("Loading...");
    $.ajax({
      url:window.SERVER_ROOT+"/attachment/addAssociatedWells"
      ,data:{wtTransId:self.getTransId(),wellLists:JSON.stringify(jsonObj)}
      ,type:'POST'
      ,cache:false
      ,success:function(data,status,jqxhr){
        self.associatedTabCt.unmask();
        var table = $(data)[0];
        self.table.removeClass("hidden");
        self.table.children().remove();
//        alert($(table).html());
        self.table.append($(table).html());
        self.checkNewWells(jsonObj);
        self.initAfterRenderIframe();
      }
      ,error:function(xhr, errorType, exception){
        if(xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
        else
        {
          alert("Exception:"+exception.toString());
        }
      }
    });
  };

  self.checkNewWells = function(jsonArray){
    self.existWells = self.table.find("tbody tr");
    $.each(self.existWells,function(){
      self.tr = $(this);
      self.existWellId = $(this).find("td[fieldname=casgenStationId]").html();
      $.each(jsonArray[0],function(i){
        self.stationId = jsonArray[0][i].stationid;
        if(self.stationId === self.existWellId)
        {
            self.tr.addClass(("newWellsAdded"));
        }
      });
    });
  };
  self.initEditWindow=function(){
    var attachmentid = $(this).parent().parent().find("td input[name=attachmentId]").val();
    var wtTransId = self.getTransId();
    var attachTypeId = "WELL";
    self.wellCheckList = self.getCheckList();
    self.wellCheckList['wtWellNum']=self.getWellNum();
    var url = window.SERVER_ROOT + "/attachment/edit/"+attachmentid;
    $.ajax({
      type:"POST"
      ,url:url
      ,data:{attachType:attachTypeId,wtTransId:wtTransId,wellType:JSON.stringify(self.wellCheckList)}
      ,cache:false
      ,error:function(xhr, errorType, exception){
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
        }
      ,success:function(data,status,jqxhr){
        $("<div>").addClass("well_mask")
            .appendTo(self.form.parent())
            .css({
                width:self.form.parent().width()+"px"
                ,height:self.form.parent().height()+"px"
        });
        self.uploadFileContainer.children().remove();
        self.uploadFileContainer.append(data);
        self.loadAttachDialog(function(el){
            var url = window.SERVER_ROOT + "/attachment/updateAttachment";
            var data = $(el).serializeArray();
            self.form.parent().mask("Saving...");
            $.ajax({
              type:"POST"
              ,url:url
              ,data:data
              ,cache:false
              ,success:function(data,status,jqxhr){
                  self.form.parent().unmask();
                  alert("Saved Successfully");
                  self.attachmentContainer.children().remove();
                  self.attachmentContainer.append(data);
                  self.initAfterRender();
              }
              ,error:function(xhr, errorType, exception){
                  alert("Error while saving the attachment");
              }
            });
          });
        }
        ,error:function(xhr, errorType, exception){
            alert(errorType+" Exception:"+exception);
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
        }
    });
  };
  self.initRemoveWindow=function(){
    var theRow = $(this).parent().parent();
    var attachmentId = theRow.find("td input[name=attachmentId]").val();
    self.wellCheckList = self.getCheckList();
    self.wellCheckList['wtWellNum']=self.getWellNum();
    var msg = "Are you sure you want to Remove this Attachment?";
    self.removeValidation('Removal Validation',msg,function(bool){
      if(bool)
      {
        self.form.parent().mask("Removing file...");
        $.ajax({
            type:"POST"
            ,url:window['SERVER_ROOT']+"/attachment/removeAttachment"
            ,data:{wtTransId:self.getTransId(),wtAttachmentId:attachmentId,attachTypeId:"WELL",wellType:JSON.stringify(self.wellCheckList)}
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.form.parent().unmask();
                alert("Removed Successfully");
                theRow.hide();
//                self.attachmentContainer.children().remove();
//                self.attachmentContainer.append(data);
                self.initAfterRender();
            }
            ,error:function(xhr, errorType, exception){
                if(xhr.status === 403) //session ends
                {
                    location = window.SERVER_ROOT;
                }
                else{
                    alert(errorType+" Exception:"+exception);
                }
            }
        });
      }
    });
  };
  self.initCheckListener = function(){
      self.getAttachmentTable();
  };
  self.buildForm=function(el){
      self.AttachForm = $("<form/>",{
          class:'form'
          ,id:'associateWellForm'
          ,target:"uploadTrg"
          ,method:"post"
          ,enctype:"multipart/form-data"
      }).html(el);
      return self.AttachForm;
  },
  self.loadAttachDialog = function(callback){
      self.formTable = self.uploadFileContainer.find("div.edit-file-container");
      self.header = "Edit Attachment Information";
      self.isEdit =true;
      self.append = "#edit-file-form";
      self.buttonType = "Update";
      if(self.formTable.length<1)
      {
        self.isEdit=false;
        self.buttonType = "Upload";
        self.append = "#upload-file-form";
        self.header = "Upload Attachment";
        self.formTable = self.uploadFileContainer.find("div.upload-file-container");
      }
      self.formTable.dialog({
          appendTo: self.append
          ,modal: true
          ,title: self.header
          ,width: 'auto'
          ,height: 'auto'
          ,resizable:false
          ,close:function(event,ui){
              $("div.well_mask").remove();
              $(this).dialog("destroy").remove();
          }
          ,buttons:[{
            text:self.buttonType
            ,click:function(){
                if (self.uploadFileForm.valid() === false){
                    return false;
                };
                $("div.well_mask").remove();
                if(self.isEdit)
                {
                    if(typeof callback === 'function'){
                        callback(self.append);
                    }
                    $(this).dialog("destroy").remove();
                    return false;
                }
                var formCt = self.formTable.parent().parent();
                var inputFile = formCt.find("input[type=file]");
                var formData = new FormData(formCt[0]);
                formData.append('file',inputFile[0].files[0]);
                var url = window.SERVER_ROOT + "/attachment/uploadFile";
                $.ajax({
                    xhr: function() {
                        var xhr = new window.XMLHttpRequest();
                        xhr.upload.addEventListener("progress", function(evt) {
                          if (evt.lengthComputable) {
                            var percentComplete = evt.loaded / evt.total;
                            percentComplete = parseInt(percentComplete * 100);
                            self.form.parent().mask("Uploading...."+percentComplete+"%");
                            }
                        }, false);
                        return xhr;
                    }
                    ,type:"POST"
                    ,url:url
                    ,data:formData
                    ,cache:false
                    ,processData: false
                    ,contentType: false
                    ,success:function(data,status,jqxhr){
                        self.form.parent().unmask();
//                                alert("File "+ filename +" Upload!");
                        self.attachmentContainer.children().remove();
                        self.attachmentContainer.append(data);
                        self.initAfterRender();
                    }
                    ,error:function(xhr, errorType, exception){
                        if(xhr.status === 403) //session ends
                        {
                            location = window.SERVER_ROOT;
                        }
                        else{
                            alert("Upload Failed!");
//                                    alert("File "+ filename +" Failed Upload!");
                        }
                    }
                });
                $(this).dialog("destroy").remove();
              }
          },{
              text:"Cancel"
              ,click:function(){
                  $("div.well_mask").remove();
                  $(this).dialog("destroy").remove();
              }
          }]
      });
      self.formTable.parent().css({'z-index':'1000'});
      self.uploadValidation();
  };
  self.getCheckList = function(){
      var checklist = new Object();
      self.inputs = self.form.find("input[name=welltype]");
      self.checked = self.form.find("input:checked");
      $.each(self.checked,function(){
          checklist[$(this).attr('name')]=1;
      });
      $.each(self.inputs,function(){
          checklist[$(this).attr('wellname')]=0;
          if(this.checked){
            checklist[$(this).attr('wellname')]=1;
          }
      });
      self.isTransferWell.hide();
      self.isMonitoringWell.hide();
      self.attachmentContainer.hide();
      self.attachBtn.hide();
      if(checklist.wellTransfer>0)
      {
        self.isTransferWell.show();
      }
      if(checklist.wellMonitoring>0)
      {
        self.isMonitoringWell.show();          
      }
      self.attachmentContainer.show();
      self.attachBtn.show();
      return checklist;
  };
  self.initAttachFile=function(){
//    alert("initAttachFile");
      var wtTransId = self.getTransId();
      var attachTypeId = "WELL";
      self.wellCheckList = self.getCheckList();
      self.wellCheckList['wtWellNum']=self.getWellNum();
      var url = window.SERVER_ROOT + "/attachment/getAttachmentChecklist";
      $.ajax({
          type:"POST"
          ,url:url
          ,data:{typeId:attachTypeId,wtTransId:wtTransId,wellType:JSON.stringify(self.wellCheckList)}
          ,cache:false
          ,scope:this
          ,success:function(data,status,jqxhr){
              self.uploadFileContainer.children().remove();
              self.uploadFileContainer.append(data);
              $("<div>").addClass("well_mask")
                  .appendTo(self.form.parent())
                  .css({
                      width:self.form.parent().width()+"px"
                      ,height:self.form.parent().height()+"px"
              });
              self.loadAttachDialog();
          }
          ,error:function(xhr, errorType, exception){
              if(xhr.status === 403) //session ends
              {
                  location = window.SERVER_ROOT;
              }
              else{
                  alert(errorType+" Exception:"+exception);
              }
          }
      });
  };
  self.initSave=function(wellData){
      self.jsonData = new Object();
      self.inputs = self.form.find("input[type=checkbox],input[type = text],input[type=number],input[type=date], input[type=radio]:checked");
      $.each(self.inputs,function(){
          self.jsonData[$(this).attr("name")] = $(this).val();
          if($(this).attr("type") === "checkbox")
          {
              self.jsonData[$(this).attr("name")] = 0;
          }
      });
      self.checked = self.form.find("input[type=checkbox]:checked");
      self.checked.each(function(){
         self.jsonData[$(this).attr("name")] = 1;
      });
      self.wellType = self.form.find("input[name=welltype]");
      self.wellType.each(function(){
        self.jsonData[$(this).attr("wellname")] = 0;
        if(this.checked){
          self.jsonData[$(this).attr("wellname")] = 1;
        }
      });
      self.psSelect = self.form.find("select[name=powerSource]");
      self.jsonData[self.psSelect.attr("name")] = self.psSelect.val();
      self.dcaSelect = self.form.find("select[name=dataCollectAgency]");
      self.jsonData[self.dcaSelect.attr("name")] = self.dcaSelect.val();
      if(self.jsonData["wellMonitoring"] === 1 && self.dcaSelect.val() === ""){
        alert('Please choose the Data Colletion Agency.');
        return false;
      }
      self.form.parent().mask("Saving...");
      $.ajax({
          type:'POST'
          ,url:window['SERVER_ROOT']+'/proposal/editAssociateWells'
          ,data:{jsonData:JSON.stringify(self.jsonData)}
          ,cache:false
          ,scope:this
          ,success:function(data,status,hxhr)
          {
            var response = JSON.parse(data);
            try{
                if(!response.success){
                  throw response.error || "Unable to edit associate wells.";
                }
                self.form.parent().unmask();
//                var isComplete = response.isComplete?"true":"false";
//                self.tableData.parent().find(".checklist").html(isComplete);
                var jsonData = response.data;
                if(jsonData['wellMonitoring'] === 1){
                  jsonData['wellType']="Monitoring";
                }
                if(jsonData['wellTransfer'] === 1){
                  jsonData['wellType']="Transfer";
                }
//                self.tableData.parent().find("td[fieldName=wellTransfer]").html("No");
//                self.tableData.parent().find("td[fieldName=wellMonitoring]").html("No");
                $.each(jsonData,function(k,v){
//                  alert(jsonData['wellType']);
                  if(k === 'stateWellNum')
                  {
                      wellData.parent().find("td[fieldName=stateWellNum] span").html(v);
                  }
                  else if(k === "lastCalibrateDate" && v !=="")
                  {
                      var dateformat = $.datepicker.formatDate("mm/dd/yy",new Date(v));
                      wellData.parent().find("td[fieldName="+k+"]").html(dateformat);
                  }
                  else if(k === "meterLastInstall" && v !=="")
                  {
                      var dateformat = $.datepicker.formatDate("mm/dd/yy",new Date(v));
                      wellData.parent().find("td[fieldName="+k+"]").html(dateformat);
                  }
                  else if(k === "powerSource" && jsonData['wellType']==="Monitoring")
                  {
                      wellData.parent().find("td[fieldName="+k+"]").html("N/A");
                  }
                  else if(k === "dataCollectAgency" && jsonData['wellType']==="Transfer")
                  {
                      wellData.parent().find("td[fieldName="+k+"]").html("N/A");
                  }
                  else
                  {
                      wellData.parent().find("td[fieldName="+k+"]").html(v);
                  }
                });
//                self.checkComplete();
                if(wellData.parent().hasClass("newWellsAdded")){
                  wellData.parent().removeClass("newWellsAdded");
                }
                alert("Well Info Saved");
              }catch(e){
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
              else{
                  alert("Saving Failed");
              }
          }
      });
  };
  self.initEdit=function(){
//    if($(this).parent().parent().hasClass("newWellsAdded")){
//      $(this).parent().parent().removeClass("newWellsAdded");
//    }
    self.tableData = $(this).parent().parent().find("td");
    self.dialogWellForm(self.tableData);
  };
  self.dialogWellForm = function(wellData){
    self.form.dialog({
      title: 'Associated Well Information'
      ,modal:true
      ,width:1075
      ,height:750
      ,resizable:true
      ,close:function(event,ui){
        self.table.removeClass("hidden");
        self.form.addClass("hidden");
        self.wellAddBtn.removeClass("hidden");
        $(this).dialog('destroy');
      }
      ,buttons:[{
        text:"Save"
        ,click:function(){
          self.initSave(wellData);
        }
      },{
//        text:"Next Well"
//        ,click:function(){
//          self.form.addClass("hidden");
//          $(this).dialog('destroy');
//          self.initNextWell();
//        }
//      },{
        text:"Close"
        ,click:function(){
          self.table.removeClass("hidden");
          self.form.addClass("hidden");
          self.wellAddBtn.removeClass("hidden");
          $(this).dialog('destroy');
        }
      }]
    ,open:function(){      
      self.json = self.buildData(wellData);
      self.loadForm(self.json);
      self.fetchContourData(wellData);
      self.form.removeClass("hidden");
      self.getAttachmentTable();
    }
    }).dialog('open');
  };

  self.getAttachmentTable = function(){
    self.params = self.getCheckList();
    self.params["wellNum"]=self.getWellNum();
    self.params['wellId']=self.getWellId();
    self.params['sKey']=self.getSectionKey();
    var wtTransId = self.createForm.find("#wtTransId").val();
    var typeId = "WELL";
    console.log(self.params);
    self.form.parent().mask("Well Loading...");
    $.ajax({
        type:"POST"
        ,url:window.SERVER_ROOT + "/attachment/getAttachmentList"
        ,data:{wtTransId: wtTransId,typeId:typeId,jsonData:JSON.stringify(self.params)}
        ,cache:false
        ,success:function(data,status,jqxhr){
            self.form.parent().unmask();
            self.attachmentContainer.children().remove();
            self.attachmentContainer.append(data);
            self.initAfterRender();
        }
        ,error:function(xhr, errorType, exception){
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
            else{
                alert(errorType+" Exception:"+exception);
            }
        }
    });
  };
  self.getAssociateWellMap = function(){
    var latitude = self.form.find("input[name=latitude]").val();
    var longitude = self.form.find("input[name=longitude]").val();
    var point = latitude+","+longitude;
    // This for wtims project Staticmap Key
    var API_KEY = 'AIzaSyBlizcrpShX6pzaQeRURCwQjEBg20NSWOc';
    // This for WaterTransfer Project Staticmap Key
//    var API_KEY = 'AIzaSyChd-NTZTOJ8Kd8O6Rxxj7DcO-435LFBno';
//    var url = "https://maps.googleapis.com/maps/api/staticmap?center="+point+"&size=400x300&maptype=hybrid&markers="+point;
    var url = "https://maps.googleapis.com/maps/api/staticmap?key="+API_KEY+"&center="+point+"&size=400x300&maptype=hybrid&markers="+point;
    self.associateWellMapCt = self.form.find(".associateWellMapCt");
    self.associateWellMapCt.html("");
    self.associateWellMapCt.append("<div><img src="+ url +" width='320' height='240'/></div>");
  };
  self.fetchContourData = function(list){
    list.each(function(){
      if($(this).attr("fieldName") === "casgenStationId"){
        var stationId = $(this).html();
        var host = self.iframe.attr("srcpath");
        var url = host+"contour/getWellById?wellid="+stationId;
        $.ajax({
          url:url
          ,cache:false
          ,dataType:'json'
          ,timeout: 30000
          ,type:'POST'
          ,success:function(data,status,jqxhr){
            var jsonData = data[0];
            self.json['totalDepth'] = jsonData['totaldepth'];
            self.json['topPerforation'] = jsonData['high'];
            self.json['bottomPerforation'] = jsonData['low'];
            self.json['firstMeasureDate'] = jsonData['firstmeasuredate'];
            self.json['lastMeasureDate'] = jsonData['lastmeasuredate'];
            self.loadForm(self.json);
            self.getAssociateWellMap();
          }
        });
      }
    });
  };
  self.buildData = function(list){
    var jsonObj = new Object();
    list.each(function(){
      if($(this).attr("fieldName")){
        jsonObj[$(this).attr("fieldName")] = $(this).html();
        if($(this).attr("fieldName") === "stateWellNum")
        {
            jsonObj[$(this).attr("fieldName")] = $(this).find("span").html();
        }
        if($(this).attr("fieldName") === "wtWellNum")
        {
          var masterSiteCode = $(this).html();
          if (masterSiteCode !== "" && masterSiteCode !== "-"){
            self.calculateLatLng(masterSiteCode,function(lat,lng){
                jsonObj['latitude']=lat;
                jsonObj['longitude']=lng;
            });
          }
        }
//        if($(this).attr("fieldName") === "casgenStationId"){
//          var stationId = $(this).html();
//          var host = self.iframe.attr("srcpath");
//          var url = host+"contour/getWellById?wellid="+stationId;
//          $.ajax({
//            url:url
//            ,cache:false
////            ,async:false
//            ,dataType:'json'
//            ,timeout: 30000
//            ,type:'POST'
//            ,success:function(data,status,jqxhr){
//              var jsonData = data[0];
//              jsonObj['totalDepth'] = jsonData['totaldepth'];
//              jsonObj['topPerforation'] = jsonData['high'];
//              jsonObj['bottomPerforation'] = jsonData['low'];
//              jsonObj['firstMeasureDate'] = jsonData['firstmeasuredate'];
//              jsonObj['lastMeasureDate'] = jsonData['lastmeasuredate'];
//              self.loadForm(jsonObj);
//              self.getAssociateWellMap();
//            }
//          });
//        }
      }
    });
    return jsonObj;
  };
  self.sleep=function(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
      if ((new Date().getTime() - start) > milliseconds){
        break;
      }
    }
  };
  self.loadForm = function(json){
    console.log(json);
    self.input = self.form.find("input");
    self.loadingDataForm(json);
    $(self.radiobtn).prop('checked',false);
    if(json.wellType === "Transfer"){
      $(self.radiobtn[0]).prop('checked',true);
    }else if(json.wellType === "Monitoring"){
      $(self.radiobtn[1]).prop('checked',true);
    }
  };
  self.calculateLatLng = function(masterSiteCode,callback)
  {
    var latArray = masterSiteCode.split('N');
    var longArray = latArray[1].split('W');
    var lat = latArray[0].substr(0,2)+"."+ latArray[0].substr(2,latArray[0].length);
    var long = "-"+longArray[0].substr(0,3)+"."+longArray[0].substr(3,longArray[0].length);
    callback(lat,long);
  };
  self.loadingDataForm=function(json){
    $.each(json,function(k,v){
      self.input = self.form.find("input[name="+k+"]");
      self.select = self.form.find("select[name="+k+"]");
      if(k === 'meterUnits')
      {
        self.input[0].checked = true;
        var unit = json['meterUnits'];
        if(unit.toUpperCase() === 'GPM')
        {
            self.input[1].checked = true;
        }
      }
      else if(k === 'wellCapacity')
      {
        self.input.val(v);
        if(v<1)
        {
          self.input.val(0);
        }
      }
      else if(k === 'powerSource' || k === 'dataCollectAgency')
      {
        self.select.val(v);
      }
      else
      {
        self.input.val(v);
      }
    });
  };
  self.initRemove=function(){
    self.tableData = $(this).parent().parent().find("td");
    var wtWellId = $(this).parent().parent().find("td[fieldname=wtWellId]").html();
    var msg = "Are you sure you want to Remove this Well?";
    self.removeValidation('Removal Validation',msg,function(bool){
      if(bool)
      {
        $.ajax({
          type:'POST'
          ,url:window['SERVER_ROOT']+'/proposal/removeAssociateWells'
          ,data:{wtWellId:wtWellId,wtTransId:self.getTransId()}
          ,cache:false
          ,scope:this
          ,dataType:'json'
          ,success:function(response,status,hxhr)
          {
            try{
              if(!response.success){
                throw response.error || "Unable to remove target storage.";
              }
              self.tableData.addClass("hidden");
            }catch(e){
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
            else{
                alert(errorType+" Exception:"+exception);
            }
          }
        });
      }
    });
  };
  self.initUnitCheck = function(){
    var el = $(this);
    if(el.val() === "GPM" || el.val() === "CFS")
    {
       el =  $(this).parent().find("input[name=wellCapacity]");
    }
    self.val = el.val();
    self.unit = el.parent().find("input[name=meterUnits]:checked").val();
    if(self.val !== ""){
      if((self.val > 10 || self.val < 1) && self.unit === 'CFS')
      {
          alert("Are you sure this is the correct number?\n The Valid well capacity is between 1 and 10 CFS");
      }
      else if((self.val < 500 || self.val > 5000) && self.unit === 'GPM')
      {
          alert("Are you sure this is the correct number?\n The Valid well capacity is between 500 and 5000 GPM");
      }
    }
  };
  self.initCalibratedDateCheck = function(){
    var calibratedDate = $(this).val();
    var date1 = new Date(calibratedDate);
    var date2 = new Date();
    var dateDiff = date2.getTime() - date1.getTime();
    if (dateDiff > 63072000000){
      alert("The date is more than 2 years from now.");
    }
  };
  self.removeValidation = function(title,msg,callback)
  {
    $("<div>",{
        html:'<p>'+msg+'</p>'
    }).dialog({
        title:title//'Removal Validation'
        ,modal:true
        ,width:400
        ,height:180
        ,close:function(){
          callback(false);
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
              callback(false);
              $(this).dialog("destroy").remove();
          }
        }]
    }).dialog("open");
  };
  self.fieldsValidation=function(){
    self.form.validate({
      onfocusout: function(element){
        $(element).valid();
      },
      rules:{
        lastCalibrateDate: "required"
      },
      messages:{
        lastCalibrateDate: "<p>The date is more than 2 years.</p>"
      }
    });
  };
  self.uploadValidation=function(){
    self.uploadFileForm = self.formTable.parent().parent();
    self.uploadFileForm.validate({
      rules:{
          file: "required",
          title: "required"
      },
      messages: {
          file: "<p>Please choose a file to be uploaded</p>",
          title: "<p>Please specify the file title</p>"
      },
      submitHandler: function(form) {
          form.submit();
      }
    });
  };
  self.getWellId = function(){
    return self.json.wtWellId;
  };
  self.getWellNum = function(){
    return self.json.wtWellNum;
  };
  self.getTransId = function(){
    return self.createForm.find("#wtTransId").val();
  };
  self.getGroundwaterId = function(){
    return self.createForm.find("#wtGroundwaterId").val();
  };
  self.getSectionKey = function(){
    return self.json.sKey;
  };
  
  self.init();
};