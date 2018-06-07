/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var WtTransDev=function() {
    var self = this;

    self.initForm=function(){
        self.link();
        self.initCreateForm();
//        self.repeat();
    };

    self.link=function(){
        self.bodyContent = $("#content-body");
        self.logout = $("#link-logout");
        self.navHome = $("#link-home");
        self.navNew = $("#icon-link-new");
        self.navExist = $("#icon-link-exist");

        self.logout.on("click", function() {
            var url =window.SERVER_ROOT + "/authentication/logout";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    alert("Log out successful.");
                    location.reload(true);
                }
            });
        });

        self.navHome.on("click", function(evt) {
            var url =window.SERVER_ROOT + "/devHome";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContent.children().remove();
                    self.bodyContent.append(data);
                    self.initForm();
                }
            });
        });

        self.navNew.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/newProposal";

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContent.children().remove();
                    self.bodyContent.append(data);
//                    self.initForm();
                    self.initCreateForm();
                }
            });
        });

        self.navExist.on("click", function() {
            var url =window.SERVER_ROOT + "/proposal/newProposalList";
console.log('dd')
            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContent.children().remove();
                    self.bodyContent.append(data);
//                    self.initForm();
                    self.linkProposal();
                }
            });
        });
    };

    self.linkProposal=function(){
        $(".wtTransId").on("click", function() {
            var wtTransId = $(this).html();
//            alert(wtTransId);
            var url =window.SERVER_ROOT + "/proposal/newProposal?wtTransId="+wtTransId;

            $.ajax({
                url:url
                ,cache:false
                ,scope:self
                ,success:function(data,status,jqxhr){
                    self.bodyContent.children().remove();
                    self.bodyContent.append(data);
//                    self.initForm();
                    self.initCreateForm();
                }
            });
        });

        $(".delete-icon").on("click", function() {
            var wtTransId = $(this).attr("id");
            alert(wtTransId);
            var url =window.SERVER_ROOT + "/proposal/deleteProposal?wtTransId="+wtTransId;

            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,success:function(data,status,jqxhr){
                    alert("Proposal "+ wtTransId +" Deleted!");
                    self.bodyContent.children().remove();
                    self.bodyContent.append(data);
                    self.linkProposal();
                }
            });
        });
    };

    self.initCreateForm=function(){
        self.createForm = $("#create-form");

//        var currentYear = (new Date).getFullYear();
//        $("input#transYear").attr("value",currentYear);

        self.attachmentList();
//        self.clickTab();
//        self.agencySelected();
//        self.contactPopup();
        self.uploadPopup();
//        self.agencyRepeat();
        self.calendar();
//        self.proposalTypeSelected();
        self.saveProposal();
//        self.saveDraft();
    };

    self.attachmentList=function(){
        self.attachmentContainer = $("#atachment-base-info");
        var wtTransId = $("#wtTransId").val();
        var typeId = $("#attachBaseInfo").attr("typeid");
        var data = {wtTransId: wtTransId,typeId:typeId};
        var url = window.SERVER_ROOT + "/attachment/getAttachmentList";

        $.ajax({
            type:"POST"
            ,url:url
            ,data:data
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.attachmentContainer.children().remove();
                self.attachmentContainer.append(data);
            }
        });
    };

//    self.clickTab=function(){
//        $("#typeMenu").hide();
//        $("#SellerInfo").addClass("menu-button-click");
//        $("#SellerInfo").removeClass("menu-button");
//        $("#buyer-info,#base-info,#cropidling-info,#reservoir-info,#groundwater-info,#conserved-info").hide();
//
//        $(".menu-button,.menu-button-click").on("click", function() {
//            $(".menu-button-click").addClass("menu-button");
//            $(".menu-button-click").removeClass("menu-button-click");
//            $("#agency-info,#seller-info,#buyer-info,#base-info,#cropidling-info,#reservoir-info,#groundwater-info,#conserved-info").hide();
//
//            $(this).addClass("menu-button-click");
//            $(this).removeClass("menu-button");
//            $("#"+$(this).attr("tabid")).show();
//
//            self.attachmentContainer = $("#atachment-"+$(this).attr("tabid"));
//            self.attchButton = $("#attach"+$(this).attr("id"));
//            var wtTransId = $("#wtTransId").val();
//            var typeId = self.attchButton.attr("typeid");
//            var data = {wtTransId: wtTransId, typeId: typeId};
//            var url = window.SERVER_ROOT + "/attachment/getAttachmentList";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,data:data
//                ,cache:false
//                ,success:function(data,status,jqxhr){
//                    self.attachmentContainer.children().remove();
//                    self.attachmentContainer.append(data)
//                }
//            });
//        });
//
//        $("#AddTypes").on("click", function() {
//            $("#typeMenu").stop().slideToggle(500);
//        });
//    };

//    self.agencySelected=function(){
//        self.selectedAgency = $(".agencyList");
//        self.contactContainer = $(".contact-container");
//
////        self.selectedAgency.change(function() {
//        self.selectedAgency.unbind('change').bind('change', function() {
////            alert($(this).closest('div').attr('id'));
//
//            self.contactListContainer = $(this).closest('div').next();
//            var containerName = $(this).closest('div').attr('id');
//            var wtAgencyId = $(this).val();
//
//            if(wtAgencyId === ''){
//                self.contactListContainer.children().remove();
//                return;
//            }
//
//            // Add id="${agency.wtAgencyId}" to contactListContainer
////            self.contactListContainer.attr("id","contact-list-"+wtAgencyId);
//            // Add wtAgencyId to Contact Form
//            self.contactContainer.html('<div id="contact-wtAgencyId"><input class="fullRow" type="text" name="wtAgencyId" id="wtAgencyId" value="'+wtAgencyId+'"/></div>');
////            alert(self.selectedAgency.val());
//            var url =window.SERVER_ROOT + "/proposal/contactListByAgency?wtAgencyId=" + wtAgencyId;
//
//            $.ajax({
//                url:url
//                ,cache:false
//                ,scope:self
//                ,success:function(data,status,jqxhr){
//                    self.contactListContainer.children().remove();
//                    self.contactListContainer.append(data);
//                    if (containerName==="agency-container"){
//                        self.contactListContainer.find(".delete-icon").show();
//                        self.contactListContainer.find(".edit-icon").show();
//                    }
//                }
//            });
//        });
//    };

//    self.contactPopup=function(){
//        self.contactContainer = $(".contact-container");
//        self.contactForm = $("#contact-form");
//        self.contactButton = $(".contact-button");
//        self.saveContact = $("#saveContact");
//        self.contactContainer.dialog({
//            autoOpen: false
//        });
//
//        self.contactButton.on("click", function(){
//            var url =window.SERVER_ROOT + "/proposal/editContact";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,success:function(data,status,jqxhr){
//                    self.contactContainer.html('<div id="contact-wtAgencyId">'+$("#contact-wtAgencyId").html()+'</div>');
//                    self.contactContainer.append(data);
//                    self.contactContainer.dialog({
//                        appendTo: "form#contact-form",
//                        modal: true,
//                        title: "Contact Form",
//                        width: 400,
//                        height: 360
//                    }).dialog('open');
//                }
//            });
//        });
//    };

    self.uploadPopup=function(){
        self.uploadFileContainer = $(".upload-file-container");
//        self.uploadFileForm = $(".upload-file-form");
        self.attachButton = $(".attachButton");
        self.attachType = $("#attachTypeId");
        self.containerId = $("#containerId");
//        self.saveFile = $(".upload-button");
        self.uploadFileContainer.dialog({
            autoOpen: false
        });

        self.attachButton.on("click", function(){
//            alert("Upload File.");
            var attachTypeId =  $(this).attr("typeid");
            var containerId =  $(this).attr("containerid");
//            var attachId =  $(this).attr("attachid");
            self.attachType.attr("value", attachTypeId);
            self.containerId.attr("value", containerId);
            $(".fieldValue").val("");

            self.uploadFileContainer.dialog({
                appendTo: "form#upload-file-form",
                modal: true,
                title: "Upload file Form",
                width: 400,
                height: 360
                }).dialog('open');
        });
    };

//    self.agencyRepeat=function(){
//        self.addBuyer = $("#add-buyer");
////        self.buyerContainer = $('#buyer-container');
////        var buyerContent = $('#buyer-container').html();
//
////        self.addBuyer.on("click", function() {
//        self.addBuyer.unbind('click').bind('click', function() {
//            self.buyerContainer = $(this).closest('div').next();
//            var buyerContent = self.buyerContainer.html();
//
//            self.buyerContainer.before('<div id="buyer-container">'+buyerContent+'</div><div class="contact-list-container"></div>');
//            self.agencySelected();
//            self.contactPopup();
//        });
//    };

    self.calendar=function(){
        $("#dwrProApprDate").datepicker();
        $("#transWinStart").datepicker();
        $("#transWinEnd").datepicker();
        $("#proposedSchedule").datepicker();
    };

    self.proposalTypeSelected=function(){
        $("#CropIdling,#Reservoir,#GroundWater,#Conserved").hide();

//        if($(".checkbox").is(':checked')){
//            $("input#"  + $(".checkbox").attr("tabid")).show();
//        }
        $(".checkbox").each(function(){
           if($(this).is(':checked')){$("input#"  + $(this).attr("tabid")).show();}
        });

        $("#cropidling-check,#reservoir-check,#groundwater-check,#conserved-check")
        .on("click",function(){
            if (this.checked)
            {
//                $("input#"  + $(this).attr("tabid")).click().show();
                $("input#"  + $(this).attr("tabid")).show();
            } else
            {
                $("input#"  + $(this).attr("tabid")).hide();
                $("div."  + $(this).attr("tabid")).hide();
            }
        });
    };

//    self.saveProposal=function(){
//        $("#saveProposal").on("click", function(evt) {
//            var data = self.createForm.serialize();
//            var url =window.SERVER_ROOT + "/proposal/saveProposal";
//
//            $.ajax({
//                type:"POST"
//                ,url:url
//                ,cache:false
//                ,data:data
//                ,success:function(data,status,jqxhr){
//                    alert("Proposal Saved!");
//                    $("#wtReservoirId").toggle().toggle();
////                    self.sellerContactContainer.children().remove();
////                    self.sellerContactContainer.append(data);
//                }
//            });
//        });
//
//    };

    self.initForm();
};