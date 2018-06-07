<%--
    Document   : UploadFile
    Created on : Mar 25, 2015, 10:17:31 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style type="text/css">
    #upload-file-form .fieldgroup label.error{
        color: #FB3A3A;
    }
    #upload-file-form .fileTypeCt label label.error{
        position: absolute;
        top:82px;
        left:130px;
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
    table#tblImageUpload th{
        padding-bottom: 30px;
    }
    #tblImageUpload th,td{
        font-size: 10pt;
    }
    .broswerBtn{
        background-color:#666666;
        border: none;
        padding:5px 20px 5px 20px;
        color:#FFFFFF;
        cursor:pointer;
        position: inherit !important;
        margin-top:1px;
        left:68%;
        top:1.5%;
    }
    .broswerBtn:hover{
        background-color: #333333;
    }
    .IEStyle{
        margin: 2px 0px 0px 10px !important;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
    if(navigator.userAgent.indexOf('MSIE') !== -1 || navigator.userAgent.indexOf('Trident') !== -1)
    {
        $(".broswerBtn").addClass('IEStyle');
    }
    $("#upload-file-form").validate({
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
function HandleBrowseClick()
{
    var browseBtn = $(".upload-file-table").find("input[name=file]")[0];
    browseBtn.click();
}
function Handlechange()
{
    var fileinput =$(".upload-file-table").find("input[name=file]");
    var textinput = $(".upload-file-table").find("input[id=filename]");
    textinput.val(fileinput.val());
}
</script>
<form class="form" action="" id="upload-file-form" target="uploadTrg" method="post" enctype="multipart/form-data">
    <div class="upload-file-container hidden">
      <table class="upload-file-table" id="tblImageUpload" style="width:100%">
        <input type="hidden" class="fullRow" type="text" id="attachType" name="filetype" value="TEMPLATE"/>
	<tr class="fieldgroup">
            <th align="left">Load File <span style="color:red">*</span></th>
            <td class="fileup_ct">
                <input class="fieldValue" style="padding:5px 0px 5px 5px;width:200px;" type="text" id="filename" readonly="true"/>
                <input class="broswerBtn" type="button" value="Browse" onclick="HandleBrowseClick();"/>
                <input class="fieldValue hidden" type="file" name="file" id="file" onchange="Handlechange();"/>
            </td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">Title <span style="color:red">*</span></th>
<!--            <td><input class="fieldValue" type="text" name="title" id="title" size="40px"/></td>-->
            <td>
              <select class="fieldValue" name="title" id="title">
                <option value="Field Data Entry Form">Field Data Entry Form</option>
                <option value="Reservoir Operation Data Entry Form">Reservoir Operation Data Entry Form</option>
                <option value="Reservoir Reoperation Additional Information Form">Reservoir Reoperation Additional Information Form</option>
              </select>
            </td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">File Description</th>
            <td>
                <textarea class="fieldValue" id="description" name="description"></textarea>
            </td>
        </tr>
      </table>
      <iframe id="trgID" name="uploadTrg" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
    </div>
</form>
        
