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
/*        height: 300px;
        width: 500px;*/
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
        padding-bottom: 15px;
    }
    #tblImageUpload th,td{
        font-size: 10pt;
/*        padding-bottom: 20px;*/
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
      rules: {
        filename: "required"
        , title: "required"
        , filetype: "required"
      },
      messages: {
        filename: "<p>Please choose a file to upload</p>"
        , title: "<p>This field is required</p>"
        , filetype: "<p>Please select a file type</p>"
      },
      submitHandler: function (form) {
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
    var fileinput = $(".upload-file-table").find("input[name=file]");
    var textinput = $(".upload-file-table").find("input[id=filename]");
    var filelist = [];
    for (var x = 0; x < fileinput[0].files.length; x++)
    {
      filelist.push(fileinput[0].files[x].name);
    }

    checkFileType(filelist.toString());
    textinput.val(filelist.toString());
    if (fileinput[0].files.length > 1)
    {
      $(".ziphide").hide();
      $(".fileTypeCt").hide();
      $(".multFileInfo").show();
    }
  }
  function checkFileType(type) {
    var fileType = type.split(".");
    $(".ziphide").show();
    $(".fileTypeCt").hide();
    if (fileType[fileType.length - 1] === "zip")
    {
      $(".fileTypeCt").show();
      $(".ziphide").hide();
      $(".filetype").on("change", function () {
        $(".ziphide").hide();
        if ($(this).val() === "REG")
        {
          $(".ziphide").show();
        }
      });
    }
  }
</script>
<form class="form" action="" id="upload-file-form" target="uploadTrg" method="post" enctype="multipart/form-data">
  <div class="upload-file-container hidden">
    <table class="upload-file-table" id="tblImageUpload" style="width:100%">
      <input type="hidden" class="fullRow" type="text" id="wtTransId" name="wtTransId" value="${wtTranID}"/>
      <input type="hidden" class="fullRow" type="text" id="attachType" name="attachType" value="${attachType}"/>
      <input type="hidden" class="fullRow" type="text" id="containerId" name="containerId" value=""/>
      <input type="hidden" class="fullRow" type="text" name="wtWellNum" value="${wellType.optString('wtWellNum')}"/>
      <input type="hidden" class="fullRow" type="text" name="wellTransfer" value="${wellType.optString('wellTransfer')}"/>
      <input type="hidden" class="fullRow" type="text" name="wellMonitoring" value="${wellType.optString('wellMonitoring')}"/>
      <input type="hidden" class="fullRow" type="text" name="createdById" value="${sessionScope.USER.userId}" />


      <tr class="fieldgroup">
        <th align="left">Load File <span style="color:red">*</span></th>
        <td class="fileup_ct">
          <input class="fieldValue" name="filename" style="padding:5px 0px 5px 5px;width:200px;" type="text" id="filename" readonly="true"/>
          <input class="broswerBtn" type="button" value="Browse" onclick="HandleBrowseClick();"/>
        </td>
      </tr>
      <tr class="fieldgroup hidden fileTypeCt">
        <th align="left">Select File Type <span style="color:red">*</span></th>
        <td>
          <!--<input type="radio" class="hidden" name="filetype" value="REG"/>-->
          <label><input class="filetype" type="radio" name="filetype" value="REG"/>Keep as zip file, such as GIS shape file</label><br/>
          <label><input class="filetype" type="radio" name="filetype" value="ZIP"/>Unzip Document after upload</label>
          <input class="fieldValue hidden" type="file" name="file" id="file" onchange="Handlechange();" multiple="multiple"/>
        </td>
      </tr>
      <tr class="fieldgroup hidden multFileInfo">
        <td colspan='3'><br/><br/><br/>
          For multiple files upload. You need to use "Edit" button to edit<br/><br/>
          information for each individual file after all files uploaded.
        </td>
      </tr>
      <tr class="fieldgroup hidden subdate">
        <th align="left">Submitted Date</th>
        <td>
          <input id="submittedDate" type="date" class="fieldValue" name="ceqaSubmittedDate" />
        </td>
      </tr>
      <tr class="fieldgroup ziphide">
        <th align="left">Title <span style="color:red">*</span></th>
        <td><input class="fieldValue" type="text" name="title" id="title" size="40px"/></td>
      </tr>
      <tr class="fieldgroup ziphide">
        <th align="left">File Description</th>
        <td>
          <textarea class="fieldValue" id="description" name="description"></textarea>
        </td>
      </tr>
      <tr class="fieldgroup"><th></th></tr>
          <c:if test="${not empty checklistCollection}">
        <tr class="fieldgroup ziphide" style="height:25px;">
          <td align="left" colspan='2'>Checklist Items (Select applicable item(s) include in the document)</td>
        </tr><tr><td></td></tr>
      </c:if>
      <c:forEach var="checklist" items="${checklistCollection}" varStatus="iterator">
        <tr class="fieldgroup ziphide">
          <td align="left" colspan='3'>
            <div style="padding: 2px 0 2px 0;width:450px;float:left;">
              <input type="checkbox" class="" name="checklist" value="${checklist.wtChecklistId}" id="checklist_${checklist.wtChecklistId}"/>
              <label for="checklist_${checklist.wtChecklistId}">${checklist.name}</label>
            </div>
          </td>
        </tr>
      </c:forEach>
    </table>

    <iframe id="trgID" name="uploadTrg" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
  </div>
</form>

