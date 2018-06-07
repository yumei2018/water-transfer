/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var WtTrans=function() {
    var self = this;

    self.initForm=function(){
        self.bodyContentPanel = $("#body-content");
        self.bodyContentMain = $("#framecontent-main");

        self.createFormIP = $("#create-form.form");
        self.searchFormIP = $("#search-form.form");
        self.updateFormIP = $("#update-form.form");

        self.link();
        self.linkNewProposal();
        self.calendar();
        self.reset();
        self.close();
        self.clear();
        self.cancel();
        self.save();
        self.update();
        self.search();
        self.printDiv();
        self.chartDiv();
    };

    self.link=function(){
        self.navHome = $("#link-home");
        self.navNew = $("#link-new");
        self.navHistoric = $("#link-historic");
        self.navExist = $("#link-exist");
        self.navAdmin = $("#link-admin");
        var resultFormCt = self.searchFormIP.find(".table_hidden");
        resultFormCt.hide();

        self.navHome.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/home";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentPanel.children().remove();
                    self.bodyContentPanel.append(data);
                    self.initForm();
                }
            });
        });

        self.navNew.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/newProposal";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentPanel.children().remove();
                    self.bodyContentPanel.append(data);
                    self.initForm();
                }
            });
        });

        self.navExist.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/existProposal";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentPanel.children().remove();
                    self.bodyContentPanel.append(data);
                    self.initForm();
                    self.linkExistProposal();
                }
            });
        });

        self.navHistoric.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/historicTrans";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentPanel.children().remove();
                    self.bodyContentPanel.append(data);
                    self.initForm();
                    self.linkHistoticTrans();
                }
            });
        });

//        self.navSearch.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/trans/searchMain";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initForm();
//                }
//            });
//        });

//        self.navCreate.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/trans/createTrans";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initForm();
//                    self.selectPropType();
//                }
//            });
//        });

//        self.navChart.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/trans/getTransChart";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initForm();
//                }
//            });
//        });

        self.navAdmin.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/admin";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentPanel.children().remove();
                    self.bodyContentPanel.append(data);
                    self.bodyContentPanel.initAdmin();
//                    self.linkAdmin();
                }
            });
        });
    };

    self.linkNewProposal=function(){
        self.navCreateNew = $("#create-new-btn");
        self.navViewNew = $("#view-new-btn");

        self.navCreateNew.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/createProposal";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.selectPropType();
                }
            });
        });

        self.navViewNew.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/newProposalList";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.editProposal();
                }
            });
        });
    };

    self.linkExistProposal=function(){
        self.navExistReview = $("#exist-review-btn");
        self.navExistApprove = $("#exist-approve-btn");
        self.navExistTrack = $("#exist-track-btn");

        self.navExistReview.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/reviewList";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.editProposal();
                }
            });
        });

        self.navExistApprove.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/approveList";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.editProposal();
                }
            });
        });

        self.navExistTrack.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/trackList";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.editProposal();
                }
            });
        });
    };

    self.editProposal=function(){
        self.linkProposal = $("#link-a-proposal");

        self.linkProposal.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/editProposal";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.selectPropType();
                }
            });
        });
    };

    self.selectPropType=function(){
        $(".CropIdling").hide();
        $(".Reservoir").hide();
        $(".GroundWater").hide();

        $(".select-ci-button").on("click", function() {
            $(".select-ci-button").css({backgroundColor:"#555599"});
            $(".select-rv-button").css({backgroundColor:""});
            $(".select-gw-button").css({backgroundColor:""});

            $(".CropIdling").show();
            $(".Reservoir").hide();
            $(".GroundWater").hide();
        });

        $(".select-rv-button").on("click", function() {
            $(".select-ci-button").css({backgroundColor:""});
            $(".select-rv-button").css({backgroundColor:"#555599"});
            $(".select-gw-button").css({backgroundColor:""});

            $(".CropIdling").hide();
            $(".Reservoir").show();
            $(".GroundWater").hide();
        });

        $(".select-gw-button").on("click", function() {
            $(".select-ci-button").css({backgroundColor:""});
            $(".select-rv-button").css({backgroundColor:""});
            $(".select-gw-button").css({backgroundColor:"#555599"});

            $(".CropIdling").hide();
            $(".Reservoir").hide();
            $(".GroundWater").show();
        });
    };

    self.linkHistoticTrans=function(){
        self.navHistoricSearch = $("#historic-search-btn");
        self.navHistoricChart = $("#historic-chart-btn");

        self.navHistoricSearch.on("click", function() {
            var url =window.SERVER_ROOT + "/trans/searchMain";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.selectPropType();
                }
            });
        });

        self.navHistoricChart.on("click", function() {
            var url =window.SERVER_ROOT + "/trans/getTransChart";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContentMain.children().remove();
                    self.bodyContentMain.append(data);
                    self.initForm();
                    self.editProposal();
                }
            });
        });
    };

//    self.linkAdmin=function(){
//        self.bodyContentMain = $("#framecontent-main");
//        self.navCreateContact = $("#create-cont-btn");
//        self.navSearchContact = $("#search-cont-btn");
//        self.navCreateAccount = $("#create-acct-btn");
//        self.navSearchAccount = $("#search-acct-btn");
//        self.navGroupPermission = $("#group-permission-btn");
//
//        self.navCreateContact.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/admin/createContact";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initAdminForm();
//                    self.saveContact();
//                }
//            });
//        });
//
//        self.navSearchContact.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/admin/searchContact";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initAdminForm();
//                    self.tablesorter();
//                }
//            });
//        });
//
//        self.navCreateAccount.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/admin/createAccount";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initAdminForm();
//                }
//            });
//        });
//
//        self.navSearchAccount.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/admin/searchAccount";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initAdminForm();
//                    self.tablesorter();
//                }
//            });
//        });
//
//        self.navGroupPermission.on("click", function(evt) {
//            var url =window.SERVER_ROOT + "/admin/groupPermission";
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.bodyContentMain.children().remove();
//                    self.bodyContentMain.append(data);
//                    self.initAdminForm();
//                    self.initGroupPermission();
//                }
//            });
//        });
//    };

    // Initial Group Permission functions
//    self.initGroupPermission=function(){
//        $(".selected-group-container").initGroupPermission();
//        self.selectedContainer = $(".selected-group-container");
//        self.selectedGroup = $(".groupList");
//        self.permissionList = $(".permissionList");
//        self.selectedPermission = $("#permissionId");
//        self.addButton = $("#add-button");
//        self.removeButton = $("#remove-button");
//
//        self.groupSelected();
//        self.addPermission();
//        self.removePermission();
//        self.saveGroupPermission();
////      self.multiSelect();
//    };

//    self.tablesorter=function(){
//        $("#searchAccountTable").tablesorter({
//            theme: 'blue',
//            widgets: ["zebra", "filter"],
//            widgetOptions : {
//                filter_external : '.search',
//                filter_columnFilters: true,
//                filter_placeholder: { search : 'Search...' },
//                filter_saveFilters : true,
//                filter_reset: '.reset'
//            }
//        });
//
//        $("#searchContactTable").tablesorter({
//            theme: 'blue',
//            widgets: ["zebra", "filter"],
//            widgetOptions : {
//                filter_external : '.search',
//                filter_columnFilters: true,
//                filter_placeholder: { search : 'Search...' },
//                filter_saveFilters : true,
//                filter_reset: '.reset'
//            }
//        });
//    };

//    self.groupSelected=function(){
//        self.selectedGroup.change(function() {
////            alert(self.selectedGroup.val());
//            $(".groupPermission").hide();
//            $('#'+$(this).val()).show();
//            // Hide Permission value if it is belong to group
//            var values = $("#"+self.selectedGroup.val()+">option").map(function(){return $(this).val();});
//            $("#permissionId>option").show();
//            for(var i=0; i<values.length; i++){
//                $("#permissionId option[value="+values[i]+"]").hide();
//            }
//        }).change();
//    };

//    self.addPermission=function(){
//        self.addButton.on("click", function() {
//            var pValue = self.selectedPermission.val();
////            alert('#'+self.selectedGroup.val());
////            alert(pValue);
////            alert($("#permissionId option:selected").text());
//            $('#'+self.selectedGroup.val()).append("<option value="+pValue+">"+$("#permissionId option:selected").text()+"</option>");
//            $("#permissionId option:selected").hide();
//        });
//    };

//    self.removePermission=function(){
//        self.removeButton.on("click", function() {
//            var pValue = $("#"+self.selectedGroup.val()+" option:selected").val();
//
////            $("#"+self.selectedGroup.val()+" option:selected").hide();
//            $("#"+self.selectedGroup.val()+" option:selected").remove();
//            $("#permissionId option[value="+pValue+"]").show();
//        });
//    };

//    self.saveGroupPermission=function(){
//        self.saveButton = $("#save-permission");
//        self.groupPermissionForm = $("#selected-permission-form");
//
//        self.saveButton.on("click", function() {
//            var gValue = self.selectedGroup.val();
//            var pValue = self.selectedPermission.val();
//            var values = $("#"+self.selectedGroup.val()+">option").map(function(){return $(this).val();});
////            var values = $(".groupPermission>option").map(function(){return $(this).val();});
////            alert(gValue);
////            alert(values.length);
//            var data = "groupId="+gValue;
//            for(var i=0; i<values.length; i++){
//                data += "&permissionId="+values[i];
//            }
//
////            var data = self.groupPermissionForm.serialize();
////            var data = $("#group-permission-form").serialize();
//            var url =window.SERVER_ROOT + "/admin/saveGroupPermission";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,data:data
//                ,success:function(data,status,jqxhr){
//                    alert("Group Permission Saved!");
//                }
//            });
//        });
//    };

//    self.multiSelect=function(){
//        $(".permissionList").multiSelect({
//            selectableHeader: "<div class='custom-header'>Selectable Permissions</div>",
//            selectionHeader: "<div class='custom-header'>Selected Permissions</div>"
//        });
//    };

    self.calendar=function(){
        $("#dwrProApprDate").datepicker();
        $("#transWinStart").datepicker();
        $("#transWinEnd").datepicker();
    };

    self.clear=function(){
        self.resetButton = self.searchFormIP.find(".clear-button");
        self.resetButton.on("click", function(evt) {
            $('.fullRow').val("");
        });
    };

    self.close=function(){
        self.closeButton = $(".close-button");
        self.closeButton.on("click", function(evt) {
            window.close();
        });
    };

    self.reset=function(){
        self.resetButton = $(".reset-button");

        self.resetButton.on("click", function(evt) {
            $('.fullRow').val("");
        });
    };

    self.cancel=function(){
        self.cancelButton = $(".cancel-button");

        self.cancelButton.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/trans/";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.initForm();
                }
            });
        });
    };

    self.save=function(){
        self.saveButton = self.createFormIP.find(".save-button");
        self.saveButton.on("click", function(evt) {
            var data = self.createFormIP.serialize();
            var url =window.SERVER_ROOT + "/trans/saveTrans";

            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,data:data
                ,success:function(data,status,jqxhr){
                    alert("Trans Record Submitted!");
                }
            });
        });
    };

//    self.saveContact=function(){
//        self.createForm = $("#create-contact-form");
//        self.saveButton = self.createForm.find(".save-button");
//        self.saveButton.on("click", function(evt) {
//            var data = self.createForm.serialize();
//            var url =window.SERVER_ROOT + "/admin/saveContact";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,data:data
//                ,success:function(data,status,jqxhr){
//                    alert("Contact Record Submitted!");
//                }
//            });
//        });
//    };

//    self.saveAccount=function(){
//        self.createForm = $("#create-account-form");
//        self.saveButton = self.createForm.find(".save-button");
//        self.saveButton.on("click", function(evt) {
//            var data = self.createForm.serialize();
//            var url =window.SERVER_ROOT + "/admin/saveAccount";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,data:data
//                ,success:function(data,status,jqxhr){
//                    alert("New account created!");
//                }
//            });
//        });
//    };

    self.update=function(){
        self.updateButton = self.updateFormIP.find(".update-button");
        self.updateButton.on("click", function(evt) {
            var data = self.updateFormIP.serialize();
            var url =window.SERVER_ROOT + "/trans/updateTrans";

            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,data:data
                ,success:function(data,status,jqxhr){
                    alert("Trans Record Updated!");
                }
            });
        });
    };

    self.search=function(){
        var resultFormCt = self.searchFormIP.find(".table_hidden");
        var scrollTable = self.searchFormIP.find(".table_trans_list");
        self.searchButton = self.searchFormIP.find(".search-button");
        self.searchButton.on("click", function(evt) {
//            $('#body-content').css({'height': '400px'});
            var data = self.searchFormIP.serialize();
            var url =window.SERVER_ROOT + "/trans/searchTrans";

            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,data:data
                ,callback:self.onSearchForm
                ,success:function(data,status,jqxhr){
                    resultFormCt.children().remove();
                    $(data).appendTo(resultFormCt);
                    resultFormCt.show();
                    self.scrollTable();
                    self.linkTrans();
//                    alert("Trans Records Found!");
                }
            });
        });
    };

    self.linkTrans=function(){
        var $trueTR = $('.sfhtData tbody tr');
        $trueTR.each(function(index){
            var $linkTrans = $($trueTR[index]).find("#link-trans");
            var wtTransId = $linkTrans.html();

            $linkTrans.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/trans/getDataTrans?wtTransId=" + wtTransId;

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentPanel.children().remove();
                        self.bodyContentPanel.append(data);

//                        self.initForm();
                    }
                });
            });
        });
    };

    self.scrollTable=function(){
        $('#transTable').scrollableFixedHeaderTable(1000,300,'true','src_table_trans_list');

        $('.sfhtHeader').hide();
        $('#transTable').tablesorter();

//        $('#transTable').tablesorter().bind('sortEnd', function(){
//            var $cloneTH = $('.sfhtHeader thead th');
//            var $trueTH = $('.sfhtData thead th');
//            $cloneTH.each(function(index){
//                $(this).attr('class', $($trueTH[index]).attr('class'));
//            });
//        });

        /* synchronize sortable and scrollable */
//        $('.sfhtHeader thead th').each(function(index){
//            var $cloneTH = $(this);
//            var $trueTH = $($('.sfhtData thead th')[index]);
//            $cloneTH.attr('class', $trueTH.attr('class'));
//            $cloneTH.click(function(){
//                $trueTH.click();
//            });
//        });
    };

    self.printDiv=function(){
        self.printButton = self.searchFormIP.find(".print-button");

        self.printButton.on("click", function(evt) {
            var divContents = $('.sfhtData').html();
            var printWindow = window.open('');
//            printWindow.document.write('<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/print.css"/>');
            printWindow.document.write('<head><style>');
            printWindow.document.write('table {border-collapse:collapse;}');
            printWindow.document.write('table td, table th{ border: 1px solid #aaaaaa; text-align:left;}');
            printWindow.document.write('</style></head>');
            printWindow.document.write('<body >');
            printWindow.document.write(divContents);
            printWindow.document.write('</body></html>');
            printWindow.print();
            printWindow.close();
        });
    };

    self.uploadFile=function(){
        $('#multiFile').MultiFile({
            accept:'gif|jpg',
            max:3,
            STRING: {
                remove:'Remover',
                selected:'Selecionado: $file',
                denied:'Invalido arquivo de tipo $ext!',
                duplicate:'Arquivo ja selecionado:\n$file!'
            }
        });
    };

    self.getAttachment=function(){
        self.attachmentForm = $("#attachment-form");
        self.navAttachment = $("#link-attachment");
        self.navAttachment.on("click", function(evt) {
//            var divContents = $("#attachment-file").html();
//            var attWindow = window.open('');
//
//            attWindow.document.write(divContents);
//            location.reload(true);
            location.reload(true);
        });
    };

    self.chartDiv=function(){
        self.chartButton = $(".chart-button");
        self.chartBarButton = $(".chart-bar-button");
        self.chartAreaButton = $(".chart-area-button");
        self.chartLineButton = $(".chart-line-button");

        self.chartButton.on("click", function(evt) {
//           $('#framecontent-main').css({'height': '800px'});
//           $('table.highchart').highchartTable();

           $('table.highchart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
                $.each(highChartConfig.yAxis, function (index, value)   {
                    value.title.text = value.title.text || 'Quantity (AF)';
                });
            }).highchartTable();
        });

        self.chartBarButton.on("click", function(evt) {
//            $('table.highchart').highchartTable();
            $('table.barchart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
                highChartConfig.colors = ['#AA4C4C', '#44AACC'];
                $.each(highChartConfig.yAxis, function (index, value)   {
                    value.title.text = value.title.text || 'Quantity (AF)';
                });
            }).highchartTable();
        });

        self.chartAreaButton.on("click", function(evt) {
//            $('table.areachart').highchartTable();
            $('table.areachart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
                $.each(highChartConfig.yAxis, function (index, value)   {
                    value.title.text = value.title.text || 'Quantity (AF)';
                });
            }).highchartTable();
        });

        self.chartLineButton.on("click", function(evt) {
//            $('table.linechart').highchartTable();
            $('table.linechart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
                $.each(highChartConfig.yAxis, function (index, value)   {
                    value.title.text = value.title.text || 'Quantity (AF)';
                });
            }).highchartTable();
        });
    };

    self.onSearchForm=function(data,status,jqxhr){/*PLACE HOLDER/**/};

    self.initForm();
};
