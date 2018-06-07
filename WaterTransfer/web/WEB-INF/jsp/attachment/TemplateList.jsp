<%--
    Document   : TemplateList
    Created on : Jul 13, 2015, 2:56:01 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/css/wtProposal.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,http://ajax.aspnetcdn.com/ajax/jquery.validate/1.14.0/jquery.validate.min.js
,${pageContext.request.contextPath}/resources/js/proposal/ProposalAdmin.js
</c:set>
<%@include file="../templates/header.jsp" %>
<span class="breadcrumbs"><a href="${pageContext.request.contextPath}/admin">Admin</a> &gt; Template</span>
<c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
  <div style="padding:30px 5px 0;" class="module_type">
    <div id="template-list">
      <table id="template-table" class="display" width="100%" cellspacing="0" cellpadding="0">
        <thead>
          <tr>
            <th colspan="6" style="text-align:left;padding:0px 0px 0px 10px;" class="header_tab">Template Files</th>
          </tr>
          <tr class="template_header">
            <th>File Name</th>
            <th>Title</th>
            <th>Description</th>
            <th>Upload Date</th>
            <th>Edit</th>
            <th>Remove</th>
          </tr>
        </thead>

        <tbody>
          <c:forEach var="attachment" items="${templateList}">
            <c:set var="bg" value="white"/>
            <tr class="attach_row" id='${attachment.wtAttachmentId}' style="background-color:${bg};">
              <td id="filename"><a href="${pageContext.request.contextPath}/attachment/view/${attachment.wtAttachmentId}" onClick="" target="_blank">${attachment.filename}</a></td>
              <td id="title">${attachment.title}</td>
              <td id="">${attachment.description}</td>
              <td id="createdDate"><fmt:formatDate pattern="MM/dd/yyyy" value="${attachment.createdDate}" /></td>
              <td class="${hidden}" id="edit-attachment" style="text-align:center;">
                <img class="picture-edit-icon" id='${attachment.wtAttachmentId}' typeId="TEMP" src="${pageContext.request.contextPath}/resources/images/icons/picture_edit.png" alt="Edit Attachment" title="Edit Attachment">
              </td>
              <td class="${hidden}" id="remove-attachment" style="text-align:center;">
                <img class="picture-delete-icon" id='${attachment.wtAttachmentId}' typeId="TEMP" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Attachment">
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <input class="attachButton" type="button" id="" value="Add Template" onclick="return false;"/>
    </div>
  </div>
  
<!--  <div>
    <input class="attachButton"
           type="button" 
           id="deleteDraftButton" 
           value="Delete All Draft Proposals" 
           onclick="window.admin.deleteDraftProposal(this);return false;"/>
  </div>-->
</c:if>

<%@include file="UploadTemplate.jsp" %>
<form class="form" id="edit-template-form" method="post"></form>
<%@include file="../templates/footer.jsp" %>
<style type="text/css">
#template-list{
  width:1000px;
  top: 20px;
  height: 450px;
  margin: 0.5em 0;
  border:none;
  padding-left: 40px;
  padding-bottom: 20px;
}
#template-table{
  width:980px;
  text-align: center;
  margin-bottom: 10px;
}
.template_header {
  background-color: #8cb3d9;
  font-size: 15px;
  width: 300px;
}
.template_header th{
  border: 1px solid #cccccc;
  padding:10px 0px;
  background: none repeat scroll 0 0 #e3e3e3;
  text-align: center;
}
/*#template-table th{
  border: 1px solid #cccccc;
  padding:10px 0px;
  background: none repeat scroll 0 0 #e3e3e3;
  text-align: center;
}*/
#template-table td{
  text-align: center;
  border:1px solid #cccccc;
  padding: 3px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
  window.admin = new ProposalAdmin();
  var self = this;
  self.templateCt = $("#template-list");
  self.attachmentTable = $("table#template-table");
  self.editBtn = self.attachmentTable.find(".picture-edit-icon");
  self.deleteBtn = self.attachmentTable.find(".picture-delete-icon");
  self.attachButton = self.templateCt.find(".attachButton");
  self.editFileForm = $("#edit-template-form");
  self.uploadFileForm = $("#upload-file-form");
  self.uploadFileContainer = $(".upload-file-container");

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
              callback(false);
              $(this).dialog("destroy").remove();
            }
        }]
      }).dialog("open");
    };

    self.deleteBtn.on("click", function(evt) {
        if($(this).attr("disabled") === "disabled")
        {
            return false;
        }
        var wtAttachmentId = $(this).attr('id');
        var attachTypeId = $(this).attr('typeid');
        var filename = $(this).parent().parent().find("#filename a").html();
        var msg = "Are you sure you want to Remove this File?";
        var data = {wtAttachmentId: wtAttachmentId, attachTypeId: attachTypeId};
        var url = window.SERVER_ROOT + "/attachment/removeAttachment";
        self.removeValidation(msg,function(bool){
          if(bool)
          {
            $.ajax({
              type:"POST"
              ,url:url
              ,data:data
              ,cache:false
              ,success:function(data,status,jqxhr){
                  alert("File "+ filename +" Removed from List!");
                  location.reload(true);
              }
            });
          }
        });
    });

    self.editBtn.on("click", function(evt) {
        var attachmentId = $(this).attr('id');
        var attachType = $(this).attr('typeId');
        var data = {attachType: attachType};
        var url = window.SERVER_ROOT + "/attachment/edit/"+attachmentId;

        $.ajax({
            type:"POST"
            ,url:url
            ,data:data
            ,cache:false
            ,success:function(data,status,jqxhr){
                self.editFileForm.children().remove();
                self.editFileForm.append(data);
                var editFileContainer = $(".edit-template-container");
                editFileContainer.dialog({
                    appendTo: "form#edit-template-form",
                    modal: true,
                    title: "Edit Template Information",
                    width: 480,
                    resizable:false,
                    buttons:[{
                        text:"Update"
                        ,click:function(){
                            if (self.editFileForm.valid() === false){
                               return false;
                            };
                            var url = window.SERVER_ROOT + "/attachment/updateAttachment";
                            var data = self.editFileForm.serialize();
                            $.ajax({
                                type:"POST"
                                ,url:url
                                ,data:data
                                ,cache:false
                                ,success:function(data,status,jqxhr){
                                    alert("Update Successful");
                                    location.reload(true);
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
            }
        });
    });

    self.attachButton.on("click", function(evt) {
      self.uploadFileContainer.dialog({
        appendTo: "form#upload-file-form",
        modal: true,
        title: "Upload Attachments",
        width: 480,
        resizable:false,
        buttons:[{
          text:"Upload"
          ,click:function(){
            if (self.uploadFileForm.valid() === false){
              return false;
            };
            var filename = $("#file").val();
            var form = new FormData($("#upload-file-form")[0]);
            form.append('file',$("input[type=file]")[0].files[0]);
            var url = window.SERVER_ROOT + "/attachment/uploadFile";

            // Call upload file function
            setTimeout(self.callUploadFile(url,form,filename),0);
            $(this).dialog('close');
          }
        },{
            text:"Cancel"
            ,click:function(){
              $(this).dialog('close');
              self.uploadFileForm.find("#tblImageUpload .subdate").addClass('hidden');
            }
        }]
      }).dialog('open');
    });

    self.callUploadFile=function(url,form,filename){
      $.ajax({
        xhr: function() {
          var xhr = new window.XMLHttpRequest();
          xhr.upload.addEventListener("progress", function(evt) {
            if (evt.lengthComputable) {
              var percentComplete = evt.loaded / evt.total;
              percentComplete = parseInt(percentComplete * 100);
//              console.log(percentComplete);
              self.uploadPercent.html("File "+ filename +" Uploading..."+percentComplete+"%");
              self.uploadPercent.dialog({
                title: "Upload Attachments",
                width: 480,
                height: 200,
                buttons:[{
                  text:"OK"
                  ,click:function(){
                    $(this).dialog('close');
                    self.uploadPercent.children().remove();
                  }
                }]
              });
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
            alert("File "+ filename +" Uploaded!");
            location.reload(true);
        }
        ,error:function(xhr, errorType, exception){
            alert("File "+ filename +" Failed Upload!");
        }
      });
    };
});
</script>
