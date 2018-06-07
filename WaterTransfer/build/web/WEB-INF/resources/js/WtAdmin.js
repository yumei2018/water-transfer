/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function($) {
    $.fn.initAdmin = function() {
        var self = this;
//        alert("test");
        linkAdmin();

        function linkAdmin(){
            self.bodyContentMain = $("#framecontent-main");
            self.navCreateContact = $("#create-cont-btn");
            self.navSearchContact = $("#search-cont-btn");
            self.navCreateAccount = $("#create-acct-btn");
            self.navSearchAccount = $("#search-acct-btn");
            self.navGroupPermission = $("#group-permission-btn");

            self.navCreateContact.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/admin/createContact";

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentMain.children().remove();
                        self.bodyContentMain.append(data);
                        reset();
                        saveContact();
                    }
                });
            });

            self.navSearchContact.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/admin/searchContact";

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentMain.children().remove();
                        self.bodyContentMain.append(data);
                        tablesorter();
                    }
                });
            });

            self.navCreateAccount.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/admin/createAccount";

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentMain.children().remove();
                        self.bodyContentMain.append(data);
                        reset();
                        saveAccount();
                    }
                });
            });

            self.navSearchAccount.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/admin/searchAccount";

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentMain.children().remove();
                        self.bodyContentMain.append(data);
                        tablesorter();
                    }
                });
            });

            self.navGroupPermission.on("click", function(evt) {
                var url =window.SERVER_ROOT + "/admin/groupPermission";

                $.ajax({
                    url:url
                    ,cache:false
                    ,scope:self
                    ,success:function(data,status,jqxhr){
                        self.bodyContentMain.children().remove();
                        self.bodyContentMain.append(data);
                        initGroupPermission();
                    }
                });
            });
        };

        function reset(){
            self.resetButton = $(".reset-button");

            self.resetButton.on("click", function(evt) {
                $('.fullRow').val("");
            });
        };

        function tablesorter(){
            $("#searchAccountTable").tablesorter({
                theme: 'blue',
                widgets: ["zebra", "filter"],
                widgetOptions : {
                    filter_external : '.search',
                    filter_columnFilters: true,
                    filter_placeholder: { search : 'Search...' },
                    filter_saveFilters : true,
                    filter_reset: '.reset'
                }
            });

            $("#searchContactTable").tablesorter({
                theme: 'blue',
                widgets: ["zebra", "filter"],
                widgetOptions : {
                    filter_external : '.search',
                    filter_columnFilters: true,
                    filter_placeholder: { search : 'Search...' },
                    filter_saveFilters : true,
                    filter_reset: '.reset'
                }
            });
        };

        function saveContact(){
            self.createForm = $("#create-contact-form");
            self.saveButton = self.createForm.find(".save-button");
            self.saveButton.on("click", function(evt) {
                var data = self.createForm.serialize();
                var url =window.SERVER_ROOT + "/admin/saveContact";

                $.ajax({
                    type:"POST"
                    ,url:url
                    ,cache:false
                    ,data:data
                    ,success:function(data,status,jqxhr){
                        alert("Contact Record Submitted!");
                    }
                });
            });
        };

        function saveAccount(){
            self.createForm = $("#create-account-form");
            self.saveButton = self.createForm.find(".save-button");
            self.saveButton.on("click", function(evt) {
                var data = self.createForm.serialize();
                var url =window.SERVER_ROOT + "/admin/saveAccount";

                $.ajax({
                    type:"POST"
                    ,url:url
                    ,cache:false
                    ,data:data
                    ,success:function(response,status,jqxhr){
                      try{
                        alert("New account created!");
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
                });
            });
        };

        // Initial Group Permission functions
        function initGroupPermission(){
            self.selectedContainer = $(".selected-group-container");
            self.selectedGroup = $(".groupList");
            self.permissionList = $(".permissionList");
            self.selectedPermission = $("#permissionId");
            self.addButton = $("#add-button");
            self.removeButton = $("#remove-button");

            groupSelected();
            addPermission();
            removePermission();
            saveGroupPermission();
        };

        function groupSelected(){
            self.selectedGroup.change(function() {
//              alert(self.selectedGroup.val());
                $(".groupPermission").hide();
                $('#'+$(this).val()).show();
                // Hide Permission value if it is belong to group
                var values = $("#"+self.selectedGroup.val()+">option").map(function(){return $(this).val();});
                $("#permissionId>option").show();
                for(var i=0; i<values.length; i++){
                    $("#permissionId option[value="+values[i]+"]").hide();
                }
            }).change();
        };

        function addPermission(){
            self.addButton.on("click", function() {
                var pValue = self.selectedPermission.val();
                $('#'+self.selectedGroup.val()).append("<option value="+pValue+">"+$("#permissionId option:selected").text()+"</option>");
                $("#permissionId option:selected").hide();
            });
        };

        function removePermission(){
            self.removeButton.on("click", function() {
                var pValue = $("#"+self.selectedGroup.val()+" option:selected").val();

//              $("#"+self.selectedGroup.val()+" option:selected").hide();
                $("#"+self.selectedGroup.val()+" option:selected").remove();
                $("#permissionId option[value="+pValue+"]").show();
            });
        };

        function saveGroupPermission(){
            self.saveButton = $("#save-permission");
            self.groupPermissionForm = $("#selected-permission-form");

            self.saveButton.on("click", function() {
                var gValue = self.selectedGroup.val();
                var values = $("#"+self.selectedGroup.val()+">option").map(function(){return $(this).val();});
                var data = "groupId="+gValue;
                for(var i=0; i<values.length; i++){
                    data += "&permissionId="+values[i];
                }

                var url =window.SERVER_ROOT + "/admin/saveGroupPermission";

                $.ajax({
                    type:"POST"
                    ,url:url
                    ,cache:false
                    ,data:data
                    ,success:function(data,status,jqxhr){
                        alert("Group Permission Saved!");
                    }
                });
            });
        };
    };

})(jQuery);