var AttachmentList = function(){
    var self = this;
    self.init = function(){
      self.initItems();
      self.initListeners();
    };

    self.initItems = function(){
      self.baseinfoTab = $("#baseinfo_tab");
      self.attachmentTable = $("table#attachment-table");
      self.editBtn = self.attachmentTable.find(".picture-edit-icon");
      self.deleteBtn = self.attachmentTable.find(".picture-delete-icon");
      self.editFileForm = $("#edit-file-form");
      self.editFileContainer = $(".edit-file-container");
      self.contactPane = self.attachmentTable.parent().parent();
      self.checklistTable = $("table#checklist-table");
      self.helpIcon = self.checklistTable.find(".help-icon");
      self.helpCt = $("#attachment-help-ct");
    };
    self.initListeners = function(){
      self.deleteBtn.unbind('click').bind("click",self.deleteHandler);
      self.editBtn.unbind('click').bind('click', self.initAttachmentDialog);
      self.helpIcon.unbind('click').on("click", self.helpIconDialog);
    };

    self.helpIconDialog = function(){
      var msg = $(this).next('div.detail').text();
      self.helpCt.html('<p>'+msg+'</p>');
      self.helpCt.dialog({
        title:'Help Information'
        ,modal:true
        ,width:400
        ,height:300
        ,buttons:[{
            text:"Close"
            ,click:function(){
                $(this).dialog("destroy").remove();
            }
        }]
      }).dialog("open");
    };
    self.editValidation=function(){
      self.editFileForm.validate({
        rules:{
          title: "required"
        },
        messages: {
          title: "Please specify the file title"
        }
      });
    };

    self.removeValidation = function(msg,callback)
    {
      $("<div>",{
        html:'<p>'+msg+'</p>'
        }).dialog({
          title:'Removal Validation'
         ,modal:true
         ,width:400
         ,height:180
         ,close:function(){
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
                $(this).dialog("destroy").remove();
                if(callback && typeof(callback)==="function")
                {
                  callback(false);
                }
              }
          }]
      });
    };
    self.deleteHandler = function(){
      if($(this).attr("disabled") === "disabled")
      {
        return false;
      }
      var theRow = $(this).parent().parent();
      var wtTransId = $("#wtTransId").val();
      var wtAttachmentId = $(this).attr('id');
      var attachTypeId = $(this).parent().closest('div').attr('typeid');
      var attachmentContainer = $(this).parent().closest('div');
      var filename = $(this).parent().parent().find("#filename a").html();
      var msg = "Are you sure you want to Remove this File?";
      var data = {wtTransId: wtTransId, wtAttachmentId: wtAttachmentId, attachTypeId: attachTypeId};
      var url = window.SERVER_ROOT + "/attachment/removeAttachment";
      self.removeValidation(msg,function(bool){
        if(bool)
        {
          self.contactPane.mask("Removing file...");
          $.ajax({
            type:"POST"
            ,url:url
            ,data:data
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.contactPane.unmask();
                alert("Removed Successfully");
                theRow.hide();
//                  alert("File "+ filename +" Deleted!");
//                attachmentContainer.children().remove();
//                attachmentContainer.append(data);
            }
            ,error:function(xhr, errorType, exception){
              if(xhr.status === 403) //session ends
              {
                location = window.SERVER_ROOT;
              }
            }
          });
        }
      });
    };

    self.initAttachmentDialog = function(){
      if($(this).attr("disabled") === "disabled")
      {
        return false;
      }
      var evt = this;
//      var height = $("div.edit-file-container").height()+150;
      var attachmentContainer = $(this).parent().closest('div');
      var attachmentContext = $("<div/>").dialog({
        appendTo: self.editFileForm
        ,modal: true
        ,title: "Edit Attachment Information"
        ,width: 480
        ,height: 600
        ,resizable:false
        ,close:function(event,ui){
            $(this).dialog("destroy").remove();
        }
        ,open:function(){
          self.fetchAttacmentData(this,evt,function(data){
            attachmentContext.append(data);
            var editFileContainer = attachmentContext.find('div.edit-file-container');
            var height = editFileContainer.height()+150;
            editFileContainer.show();
            self.editValidation();
            attachmentContext.dialog("option",'height',height);
          });
        }
        ,buttons:[{
          text:"Update"
          ,click:function(){
              var url = window.SERVER_ROOT + "/attachment/updateAttachment";
              var title = self.editFileForm.find("#title").val();
              if (title === ''){
                alert("You can not leave the title as blank.");
                return false;
              }
              var data = self.editFileForm.serialize();
              var dia = this;
              $(dia).parent().mask("Updating attachment...");
              $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,success:function(data,status,jqxhr){
                  $(dia).parent().unmask();
                  alert("Update Successful");
                  attachmentContainer.children().remove();
                  attachmentContainer.append(data);
                  $(dia).dialog("close");
                }
                ,error:function(xhr, errorType, exception){
                  $(dia).parent().unmask();
                  if(xhr.status === 403) //session ends
                  {
                      location = window.SERVER_ROOT;
                  }
                }
              });
            }
        },{
            text:"Cancel"
            ,click:function(){
                $(this).dialog("destroy").remove();
            }
        }]
      }).dialog('open');
    };
    self.fetchAttacmentData = function(attEl,evtEl,callback){
      var attachmentId = $(evtEl).attr('id');
      var wtTransId = $("#wtTransId").val();
      var attachType = $(evtEl).attr('typeId');
      var url = window.SERVER_ROOT + "/attachment/edit/"+attachmentId;
      var data = {wtTransId: wtTransId, attachType: attachType};
      $(attEl).mask("Loading...");
      $.ajax({
          type:"POST"
          ,url:url
          ,data:data
          ,cache:false
          ,error:function(xhr, errorType, exception){
            $(attEl).unmask();
            if(xhr.status === 403) //session ends
            {
                location = window.SERVER_ROOT;
            }
          }
          ,success:function(data,status,jqxhr){
            $(attEl).unmask();
            if(typeof callback == "function" || callback){
              callback(data);
            }
          }
      });
    };
    self.editHandler = function(){
      if($(this).attr("disabled") === "disabled")
      {
          return false;
      }
      var attachmentId = $(this).attr('id');
      var wtTransId = $("#wtTransId").val();
      var attachType = $(this).attr('typeId');
      var attachmentContainer = $(this).parent().closest('div');
      var url = window.SERVER_ROOT + "/attachment/edit/"+attachmentId;
      var data = {wtTransId: wtTransId, attachType: attachType};

      $.ajax({
          type:"POST"
          ,url:url
          ,data:data
          ,cache:false
          ,error:function(xhr, errorType, exception){
              if(xhr.status === 403) //session ends
              {
                  location = window.SERVER_ROOT;
              }
          }
          ,success:function(data,status,jqxhr){
              self.editFileForm.children().remove();
              self.editFileForm.append(data);
              var height = $("div.edit-file-container").height()+150;
              $(".edit-file-container").dialog({
                appendTo: "form#edit-file-form",
                modal: true,
                title: "Edit Attachment Information",
                width: 480,
                height: height,
                resizable:false,
                close:function(event,ui){
                    $(this).dialog("destroy").remove();
                },
                buttons:[{
                    text:"Update"
                    ,click:function(){
                        var url = window.SERVER_ROOT + "/attachment/updateAttachment";
                        var title = self.editFileForm.find("#title").val();
//                        alert("title="+title);
                        if (title === ''){
                          alert("You can not leave the title as blank.");
                          return false;
                        }
                        var data = self.editFileForm.serialize();
                        $.ajax({
                            type:"POST"
                            ,url:url
                            ,data:data
                            ,cache:false
                            ,success:function(data,status,jqxhr){
                                alert("Update Successful");
                                attachmentContainer.children().remove();
                                attachmentContainer.append(data);
                            }
                            ,error:function(xhr, errorType, exception){
                                if(xhr.status === 403) //session ends
                                {
                                    location = window.SERVER_ROOT;
                                }
                            }
                        });
                        $(this).dialog("destroy").remove();
                    }
                },{
                    text:"Cancel"
                    ,click:function(){
                        $(this).dialog("destroy").remove();
                    }
                }]
            }).dialog('open');
            self.editValidation();
          }
      });
    };
    self.init();
  };