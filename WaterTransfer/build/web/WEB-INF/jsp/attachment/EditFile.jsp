<%--
    Document   : UploadFile
    Created on : Mar 25, 2015, 10:17:31 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style type="text/css">
    #edit-file-form .fieldgroup label.error{
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
        padding: 8px;
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
    $("#eidt-file-form").validate({
        rules:{
           filename: "required"
           ,title: "required"
        },
        messages: {
            filename: "<p>Please choose a file</p>"
            ,title:"<p>This field is required</p>"
        }
    });

    $(".dateField").datepicker();
    if($("#editAttachmentType").val()==="CEQA Document"){
        $(".subdate").removeClass('hidden');
    }

    $("#editAttachmentType").on("change",function(){
//        alert($(this).val());
        if($(this).val()==="CEQA Document"){
//            $("#subdate").removeClass('hidden');
            $(this).parent().parent().next().removeClass('hidden');
        } else {
//            $("#subdate").addClass('hidden');
            $(this).parent().parent().next().addClass('hidden');
        }
    });
});
</script>
<!--<form class="form" id="edit-file-form" method="post">-->
    <div class="edit-file-container" style="display:none;">
    <input class="hidden" type="text" id="wtTransId" name="wtTransId" value="${wtTansId}"/>
    <input class="hidden" type="text" id="wtAttachmentId" name="wtAttachmentId" value="${attachment.wtAttachmentId}"/>
    <input class="hidden" type="text" id="attachType" name="attachType" value="${attachType}"/>
    <input type="hidden" class="fullRow" type="text" name="wtWellNum" value="${wellType.optString('wtWellNum')}"/>
    <input type="hidden" class="fullRow" type="text" name="wellTransfer" value="${wellType.optString('wellTransfer')}"/>
    <input type="hidden" class="fullRow" type="text" name="wellMonitoring" value="${wellType.optString('wellMonitoring')}"/>
    <table class="edit-file-table" id="tblImageUpload" style="width:100%">
	<tr class="fieldgroup">
            <th align="left">File Name: </th>
            <td class="fieldgroup" id="fileName">
                ${attachment.filename}
            </td>
        </tr>
        <tr class="fieldgroup hidden subdate">
            <th align="left">Submitted Date</th>
            <td>
               <input id="submittedDate" type="date" class="fieldValue dateField" name="ceqaSubmittedDate" value='<fmt:formatDate value="${attachment.ceqaSubmittedDate}" pattern="MM/dd/yyyy"/>'/>
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
        <tr class="fieldgroup"><th></th></tr>
        <c:if test="${fn:length(checklistCollection) gt 0}">
            <tr class="fieldgroup" hidden>
                <td align="left" colspan='2'>Type Checklist</td>
            </tr>
        </c:if>
        <c:forEach var="checklist" items="${checklistCollection}" varStatus="iterator">
          <tr class="fieldgroup"><td></td>
            <td><div style="width:350px;float:left;">
                <input style="margin: 6px 6px 6px 0;"
                       type="checkbox" class=""
                       name="checklist"
                       value="${checklist.wtChecklistId}"
                       id="checklist_${checklist.wtChecklistId}"
                <c:forEach var="cl" items="${attachment.wtChecklistCollection}">
                  <c:if test="${cl.wtChecklistId == checklist.wtChecklistId}">
                    checked
                  </c:if>
                </c:forEach>
                />
                <label for="checklist_${checklist.wtChecklistId}">${checklist.name}</label>
            </div></td>
          </tr>
        </c:forEach>
    </table>
    </div>
<!--</form>-->