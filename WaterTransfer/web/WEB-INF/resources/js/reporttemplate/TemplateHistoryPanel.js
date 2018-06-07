/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var TemplateHistoryPanel=function(){
    var self = this;

    self.init=function(){
        self.initItems();
        self.initEvents();
        self.initPreviewAction();
    }

    self.initItems=function()
    {
        self.historyPanelEl = $("#report-panel .template-history-ct");
        self.gridEl = self.historyPanelEl.find(".grid").removeClass("hidden").hide();
        self.historyBtnEl = this.historyPanelEl.find(".history-btn");
        self.formCtEl = $("#report-panel div.form-ct");
        self.formEl = self.formCtEl.find("form#template-form");
    }

    self.initEvents=function()
    {
        self.historyBtnEl.on("click",self.toggleHistoryPanel);
    }
    self.initPreviewAction=function()
    {
        self.saveBtnEl = self.formEl.find("[name=save-btn]");
        self.saveBtnEl.on("click",function(){

        });
    }

    self.loadRecords=function(){
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
                        self.onRowSelect($(this).attr("reportTemplateId"));
                        $(this).parent().find(".report-row.selected").removeClass("selected");
                        $(this).addClass("selected");
                    });
                }
            });
        }
    }

    self.onRowSelect=function(id){
    }
    self.toggleHistoryPanel=function()
    {
        var gridTableCt = self.gridEl;

        if (gridTableCt.is(":visible"))
        {
            gridTableCt.hide("slide", {direction:"up"},"fast");
        }
        else
        {
            self.loadRecords();
            gridTableCt.show("slide", {direction:"up"},"fast");
        }
    }
    self.test=function(){alert('hi');}
    self.init();
};

