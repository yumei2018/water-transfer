/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var TemplateForm=function(){
    var self = this;
    self.init=function(){
        self.formCtEl = $("#report-panel div.form-ct");
        self.formEl = self.formCtEl.find("form#template-form");
        self.reportTemplateIdEl = self.formEl.find("input[name=reportTemplateId]");
        self.initCheckboxes();
        self.initSaveAction();
        self.initPreviewAction();
    }

    self.initPreviewAction=function(){
        self.previewBtn = self.formEl.find("button[name=preview-btn]");
        self.previewBtn.on("click",self.preview);
    }

    self.preview=function(){
        var id = self.formEl.find("input[name=reportTemplateId]").val();
        if (id)
        {
            $("<div></div>")
            .html("Please select a preview date for the report. <br/><label for='preview-date'>Choose</label> <input id='preview-date' type='date' />")
            .dialog({
                title:"Preview Option"
                ,modal:true
                ,autoOpen:true
//                ,buttons:[ { text: "Ok", click: function() { $( this ).dialog( "close" ); } } ]
                ,open:function(evt,ui)
                {
                    $(this)
                    .find("input[type=date]")
                    .datepicker({
                        dateFormat:"mm/dd/yy"
                        ,buttonImageOnly:true
                        ,buttonImage:window["SERVER_ROOT"] + "/resources/images/calendar.gif"
                        ,onSelect:function()
                        {
                            window.open(window["SERVER_ROOT"] + "/reporttemplate/previewReport?id=" + id + "&previewDate=" + $(this).val(), "_blank");
                        }
                    });
                }
            });
        }
    }

    self.initSaveAction=function(){
        self.saveBtnEl = self.formEl.find("[name=save-btn]");
        self.saveBtnEl.on("click",function(){
            self.submitForm();
        });
    }

    self.onCheckboxClick=function()
    {
        var idx = self.selectedTypes.indexOf(this.value);
        if (this.checked)
        {
            if (idx == -1)
            {
                self.selectedTypes.push(this.value);
            }
        }
        else
        {
            if (idx !== -1)
            {
                delete self.selectedTypes[idx];

                self.selectedTypes = self.selectedTypes.filter(String);
            }
        }
    }

    self.initCheckboxes=function()
    {
        self.selectedTypes = [];

        try
        {
            self.selectedTypes = JSON.parse(self.formEl.find("input#selectedTypes").val());
        }catch(e){
            self.selectedTypes = [];
        }

        self.checkBoxesEl = self.formEl.find("input[type=checkbox]");
        self.checkBoxesEl.on("click",self.onCheckboxClick);
    }

    self.getTemplateName=function(){
        return self.formEl.find("input[name=name]").val().trim();
    }

    self.beforeNewForm=function(){}
    self.newForm = function()
    {
        $("<div></div>")
        .html("You are creating a new report template any unsaved data on the current report template will not be saved. Are you sure?")
        .dialog({
            resizable: false
            ,dialogClass:'alert'
            ,modal: true
            ,title:'New Report Template Confirmation'
            ,buttons: {
                YES: function() {
                    var dialogEl = $(this);
                    if (self.formEl.parent().length > 0)
                    {
                        self.formEl.find("input").not(self.checkBoxesEl).val("");
                        self.checkBoxesEl.attr("checked",false);
                        self.selectedTypes = [];
                        self.previewBtn.addClass("hidden");
                        self.formEl.find(".update-label").html("");
                    }
                    else
                    {
                        self.loadRecord(-1);
                    }

                    dialogEl.dialog( "close" );
              },
              NO: function() {
                  $( this ).dialog( "close" );
              }
            }
        });
    }

    self.submitForm = function(callback)
    {
        if (!self.getTemplateName())
        {
            alert("A template name is required!");
            return;
        }

        if (self.selectedTypes.length ==0)
        {
            alert("A hydrology section type must be selected!");
            return;
        }

        if (self.formEl.attr("action"))
        {
            if (!callback)
            {
                callback = self.onFormSubmit;
            }

            var params = self.formEl.serialize();
            params += "&types=" + encodeURIComponent(self.selectedTypes);
            window['LOADMASK'].dialog("open");
            $.ajax({
                url:self.formEl.attr("action")
                ,cache:false
                ,data:params
                ,type:"POST"
                ,success:function(data,status,jqxhr){
                    setTimeout(function(){
                        window['LOADMASK'].dialog("close");
                        alert("Report Template Saved!");
                    },500);
                    callback.call(this,data,status,jqxhr);
                    self.loadRecords();
                    self.loadHomeReport(data);
                }
            });
        }
    }

    self.loadHomeReport=function(data)
    {
        var newTemplate = true;
        var home = $("#hydroreports-ct").find(".report-row");
        for(var i =0;i<home.length;i++)
        {
            var reportTemplate = $(".report-row")[i];
            if(data.reportTemplate.reportTemplateId==reportTemplate.getAttribute("reporttemplateid"))
            {
                reportTemplate.innerHTML=data.reportTemplate.name;
                newTemplate=false;
            }
        }
        if(newTemplate)
        {
            var calendarEl = $("#calendar-info");
            var dp = $("<input>").attr("type","date").datepicker({
                dateFormat:"mm/dd/yy"
            });
            dp.datepicker("setDate", new Date(Date.parse(calendarEl.html())));
            var dateStr = dp.datepicker().val();
            var div = $("<div></div>").html('&raquo').insertAfter(".hydro-title");
            $("<a/>",{
                html:data.reportTemplate.name
                ,addClass:'report-row'
                ,href:window["SERVER_ROOT"] + "/reporttemplate/previewReport?id="+data.reportTemplate.reportTemplateId+"&previewDate="+dateStr
                ,target:'_blank'
            }).attr('reporttemplateid',data.reportTemplate.reportTemplateId).appendTo(div);
        }
    }

    self.onFormSubmit=function(data,status,jqxhr)
    {
        if (data.reportTemplate && !isNaN(parseInt(data.reportTemplate.reportTemplateId)))
        {
            self.setReportTemplateId(data.reportTemplate.reportTemplateId);
            self.previewBtn.removeClass("hidden");
        }
    }

    self.setReportTemplateId=function(id)
    {
        self.reportTemplateIdEl.val(id);
    }

    self.loadRecord=function(id){
        if (self.formEl.attr("url"))
        {
            window['LOADMASK'].dialog("open");
            $.ajax({
                url:self.formEl.attr("url") + "?id=" + id
                ,cache:false
                ,success:function(data,status,jqxhr){
                    self.formCtEl.children().remove();
                    $(data).appendTo(self.formCtEl);
                    self.init();
                    setTimeout(function(){
                        window['LOADMASK'].dialog("close");
                    },500);
                }
            });
        }
    }
    self.loadRecords=function(){
        self.previewPanelEl = $("#report-panel");
        self.gridEl = self.previewPanelEl.find(".grid").removeClass("hidden").show();
        var gridTableCt = self.gridEl;
        if (gridTableCt.length > 0 && gridTableCt.attr("url"))
        {
            $.ajax({
                url:gridTableCt.attr("url")
                ,cache:false
                ,success:function(data,status,jqxhr){
                    gridTableCt.children().remove();
                    $(data).appendTo(gridTableCt);
                    gridTableCt.find(".report-row").on("click",function(){
                        var className = $(this).parent().parent().parent().parent().parent().attr("class");
                        if(className == "template-history-ct")
                        {
                            self.onRowSelectHistory($(this).attr("reportTemplateId"));
                        }
                        else
                        {
                            self.onRowSelectPreview($(this).attr("reportTemplateId"));
                        }
                        $(this).parent().find(".report-row.selected").removeClass("selected");
                        $(this).addClass("selected");
                    });
                }
            });
        }
    }

    self.onRowSelectHistory = function(id)
    {
        self.loadRecord(id);
        self.previewPanelEl = $("#report-panel .template-preview-ct");
        self.gridEl = self.previewPanelEl.find(".grid").removeClass("hidden").show();
        self.gridEl.find(".selected").removeClass("selected");
    }
    self.onRowSelectPreview=function(id){
        self.panelEl = $("#report-panel");
        self.historyPanelEl = $("#report-panel .template-history-ct");
        self.gridEl = self.historyPanelEl.find(".grid").removeClass("hidden").show();
        window['LOADMASK'].dialog("open");
            var calendarEl = $("#calendar-info");
            var dp = $("<input>").attr("type","date").datepicker({
                dateFormat:"mm/dd/yy"
            });
            dp.datepicker("setDate", new Date(Date.parse(calendarEl.html())));
            var dateStr = dp.datepicker().val();
            self.gridEl.find(".selected").removeClass("selected");
            var url = window['SERVER_ROOT'] + "/reporttemplate/previewReport?id=" + id + "&previewDate=" + dateStr;
            var rcol = self.panelEl.find(".rcol");
            rcol.children().remove();
            self.printForm();
            $("<iframe id='iframe'></iframe>")
            .attr("src", url)
            .width(1000)
            .on("load",function(){
                var iframeWindow;
                iframeWindow = this.contentWindow;

                if (iframeWindow)
                {
                    $(this).height(iframeWindow.document.body.scrollHeight+50);
                }
                window['LOADMASK'].dialog("close");
            })
            .appendTo(rcol);
    }
    self.printForm=function(){
        var rcol = self.panelEl.find(".rcol");
        var button = $("<a/>",{
            addClass:'big-btn'
            ,style:'float:right;cursor:pointer;'
        }).appendTo(rcol);
        button.html("PDF Preview").click(function(){
            self.printPreview();
            $(".selected").click();
        });
    }
    self.init();
};

