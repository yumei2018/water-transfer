/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var TemplateApp = function(){
    var self = this;
    self.initItems=function()
    {
        self.panelEl = $("#report-panel");
        self.formApp = new TemplateForm();
        self.historyApp = new TemplateHistoryPanel();
        self.historyApp.onRowSelect=function(id){
            self.formApp.loadRecord(id);
            self.previewApp.gridEl.find(".selected").removeClass("selected");
        }
        self.previewApp = new TemplatePreviewPanel();
        self.previewApp.onRowSelect=function(id){
            window['LOADMASK'].dialog("open");
            self.historyApp.gridEl.find(".selected").removeClass("selected");
            var url = window['SERVER_ROOT'] + "/reporttemplate/previewReport?id=" + id + "&previewDate=" + self.previewApp.getPreviewDate();
            var rcol = self.panelEl.find(".rcol");
            rcol.children().remove();
            self.printForm();
//            $("<div></div>").css({textAlign:"right"}).append($("<a href='" + url + "' target=_blank class='big-btn'>Print</a>")).appendTo(rcol);
            var iframe = $("<iframe id='iframe'></iframe>")
            .attr("src", url)
            .width(1000)
            .on("load",function(){
                var iframeWindow;
                iframeWindow = this.contentWindow;

                if (iframeWindow)
                {
//                    $(iframeWindow.document.body).find(".content").width(940);
                    $(this).height(iframeWindow.document.body.scrollHeight+50);
                }
                window['LOADMASK'].dialog("close");
            })
            .appendTo(rcol);
        }
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
    self.printPreview=function()
    {
        var iframeHTML = $('#iframe').contents().find("html").html();
        var html = "<!DOCTYPE html><html>" + iframeHTML + "</html>";
        var form = $('#iframe').contents().find(".submit-html");
        form.children().val(html);
        form.submit();
    }
    self.getFormApp=function(){
        return self.formApp;
    }

    self.getHistoryApp=function(){
        return self.historyApp;
    }

    self.initEvents=function()
    {

    }

    self.init=function()
    {
        self.initItems();
        self.initEvents();
    }

    self.init();
};

