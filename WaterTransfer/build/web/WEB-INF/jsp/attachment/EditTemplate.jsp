<%--
    Document   : UploadFile
    Created on : Mar 25, 2015, 10:17:31 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style type="text/css">
    #edit-template-form .fieldgroup label.error{
        color: #FB3A3A;
    }
    table#tblImageUpload{
        /*height: 250px;*/
        width: 400px;
        font-size: 15px;
        text-align: left;
    }
    table#tblImageUpload td{
        text-align: left;
    }
    .file-buttons{
        width: 200px;
        padding-left: 150px;
        margin-top: 50px;
    }
    #description{
        width:306px;
    }
    #tblImageUpload th{
        font-size: 10pt;
        padding-bottom: 30px;
    }
    .broswerBtn{
        background-color:#666666;
        border: none;
        padding:5px 20px 5px 20px;
        color:#FFFFFF;
        cursor:pointer;
        position: absolute;
        left:68%;
        top:3%;
    }
    .broswerBtn:hover{
        background-color: #333333;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
    $("#edit-template-form").validate({
        rules:{
           filename: "required"
           ,title: "required"
        },
        messages: {
            filename: "<p>Please choose a file</p>"
            ,title:"<p>This field is required</p>"
        },
        submitHandler: function(form) {
            form.submit();
        }
    });
});
</script>
<!--<form class="form" id="edit-template-form" method="post">-->
    <div class="edit-template-container" style="display:none;">
      <input class="hidden" type="text" id="wtAttachmentId" name="wtAttachmentId" value="${attachment.wtAttachmentId}"/>
      <input class="hidden" type="text" id="attachType" name="attachType" value="TEMP"/>
      <table class="edit-file-table" id="tblImageUpload" style="width:100%">
	<tr class="fieldgroup">
            <th align="left">File Name: </th>
            <td class="fieldgroup" id="fileName">
                ${attachment.filename}
            </td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">Title <span style="color:red">*</span></th>
            <td><input class="fieldValue" type="text" name="title" id="title" size="40px" value="${attachment.title}"/></td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">File Description</th>
            <td>
                <textarea class="fieldValue" id="description" name="description">${attachment.description}</textarea>
            </td>
        </tr>
      </table>
    </div>
<!--</form>-->