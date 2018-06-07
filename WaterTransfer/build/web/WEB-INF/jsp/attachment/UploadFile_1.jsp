<%--
    Document   : UploadFile
    Created on : Mar 25, 2015, 10:17:31 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style type="text/css">
    #upload-file-form .fieldgroup label.error{
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
/*    #fileUpload{
        border: none;
        background-color:#dfdfdf;
    }
    #fileUpload:hover{
        background-color:#b3b3b3;
        cursor: pointer;
    }*/
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
        margin-top:1px;
        left:68%;
        top:3%;
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
           ,attachmentType: "required"
           ,title: "required"
        },
        messages: {
            filename: "<p>Please choose a file</p>"
            ,attachmentType:"<p>This field is required</p>"
            ,title:"<p>This field is required</p>"
        }
    });
});
function HandleBrowseClick()
{
    var browseBtn = $(".upload-file-table").find("input[name=file]");
    browseBtn.click();
}
function Handlechange()
{
    var fileinput =$(".upload-file-table").find("input[name=file]");
    var textinput = $(".upload-file-table").find("input[name=filename]");
    textinput.val(fileinput.val());
}
</script>
<form class="form" action="" id="upload-file-form" target="uploadTrg" method="post" enctype="multipart/form-data">
    <div class="upload-file-container">
    <table class="upload-file-table" id="tblImageUpload" style="width:100%">
        <input type="hidden" class="fullRow" type="text" id="wtTransId" name="wtTransId" value=""/>
        <input type="hidden" class="fullRow" type="text" id="attachTypeId" name="attachTypeId" value=""/>
        <input type="hidden" class="fullRow" type="text" id="containerId" name="containerId" value=""/>
	<tr class="fieldgroup">
            <th align="left">Load File <span style="color:red">*</span></th>
            <td class="fileup_ct">
                <input class="fieldValue hidden" type="file" name="file" id="file" onchange="Handlechange();"/>
                <input class="fieldValue" style="padding:5px 0px 5px 5px;width:200px;" type="text" name="filename" id="filename" readonly="true"/>
                <input class="broswerBtn" type="button" value="Browse" onclick="HandleBrowseClick();"/>
            </td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">Type <span style="color:red">*</span></th>
            <td>
                <select class="fieldValue" name="attachmentType" id="attachmentType">
                     <!--<option value="">&nbsp;</option>-->
                    <option value="Buyer/Seller Agreement">Buyer/Seller Agreement</option>
                    <option value="CEQA Document">CEQA Document</option>
                    <option value="Historic Data">Historic Data</option>
                    <option value="Location Infomation">Location Information</option>
                    <option value="Location Map">Location Map</option>
                    <option value="Proposal Report">Proposal Report</option>
                    <option value="Water Rights">Water Rights</option>
                    <option value="WRCB Petitions">WRCB Petitions</option>
                    <option value="Others">Others</option>
                </select>
            </td>
        </tr>
        <tr class="fieldgroup hidden subdate">
            <th align="left">Submitted Date</th>
            <td>
               <input id="submittedDate" type="date" class="fieldValue" name="ceqaSubmittedDate" />
            </td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">Title <span style="color:red">*</span></th>
            <td><input class="fieldValue" type="text" name="title" id="title" size="40px"/></td>
        </tr>
        <tr class="fieldgroup">
            <th align="left">File Description</th>
            <td>
                <textarea class="fieldValue" id="description" name="description"></textarea>
            </td>
        </tr>
    </table>
<!--    <div class="file-buttons">
        <input class="upload-button" name="fileUpload" type="button" id="fileUpload" value="Upload"/>
    </div>-->
    <iframe id="trgID" name="uploadTrg" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
    </div>
</form>