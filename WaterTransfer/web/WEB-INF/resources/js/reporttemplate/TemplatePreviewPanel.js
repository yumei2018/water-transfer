/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var TemplatePreviewPanel=function(){
    var self = this;

    self.init=function(){
        self.initItems();
        self.initEvents();
    }

    self.initItems=function()
    {
        self.previewPanelEl = $("#report-panel .template-preview-ct");
        self.gridEl = self.previewPanelEl.find(".grid").removeClass("hidden").hide();
        self.previewBtnEl = this.previewPanelEl.find(".preview-btn");
        self.previewDateCt = self.previewPanelEl.find(".preview-date-ct").hide();
        self.previewDateEl = self.previewDateCt.find("input#preview-date").datepicker({
            dateFormat:"mm/dd/yy"
            ,onSelect:function(){
                self.onRowSelect(self.gridEl.find(".selected").attr("reportTemplateId"));
            }
        });
    }

    self.getPreviewDate=function(){
        return self.previewDateEl.val();
    }

    self.initEvents=function()
    {
        self.previewBtnEl.on("click",self.togglePanel);
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
    self.togglePanel=function()
    {
        var gridTableCt = self.gridEl;
        if (gridTableCt.is(":visible"))
        {
            self.previewDateCt.hide();
            gridTableCt.hide("slide", {direction:"up"},"fast");
        }
        else
        {
            self.loadRecords();
            self.previewDateCt.show();
            gridTableCt.show("slide", {direction:"up"},"fast");
        }
    }

    self.init();
};

