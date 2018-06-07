<%-- 
    Document   : UploadFile
    Created on : Jan 15, 2015, 9:01:33 AM
    Author     : ymei
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>New File Upload Form</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/attachment.css">
    <script type="text/javascript">window.SERVER_ROOT = "${pageContext.request.contextPath}";</script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.ajaxfileupload.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.form.js"></script>
    <!--<script src="http://malsup.github.com/jquery.form.js"></script>-->

    <script language="Javascript">
        $(document).ready(function(){
            var url =window.SERVER_ROOT + "/trans/uploadFile";
            
//            $(".upload-button").on("click", function(evt) {
//                var data = $('#fileForm').serialize();
//                var fd = new FormData(); 
//                fd.append('file', $('#file')[0].files[0]);                
//            
//                $.ajax({
//                    type:"POST"
//                    ,url:url
//                    ,cache:false
//                    ,processData: false
//                    ,contentType:false
//                    ,data:fd
//                    ,success:function(data,status,jqxhr){
//                        alert("File upload successful!");
//                    }
//                });
//            });
	
            $(".upload-button").on("click", function(evt) {
                var filename = $(".file").val();
                alert(filename);
                var fileNames = ${fileNames};
//                alert(fileNames[1]);
                $.each(fileNames, function(index, fileName){
//                    alert(index + ": " + fileName);
                });
                
//                $('#fileForm').ajaxForm({
//                    complete:function(data, textStatus, jqXHR) {
//                        window.alert("File upload completed!"); 
//                    }
//                });
                
                alert("File upload completed!");
                window.opener.location.reload();
            });
            
            $(".close-button").on("click", function(evt) {
                window.close();
                window.opener.location.reload();
            });
        });
    </script>

</head>
<body>
   
<!--<form name="fileForm" id="fileForm" action="/WaterTransfer/trans/uploadFile" target="uploadTrg" method="post" enctype="multipart/form-data" onSubmit="return submitPostUsingAjax()">-->
<form name="fileForm" id="fileForm" action="/WaterTransfer/trans/uploadFile" target="uploadTrg" method="post" enctype="multipart/form-data" onSubmit="return false;">
    <div class="form-title">New Attachment Form</div>
    <table border="0" cellpadding="0" cellspacing="0" align="center" id="tblImageUpload">
        <input style="display:none" type="text" name="wtTransId" id="wtTransId" value='${wtTransId}'/>
        <input style="display:none" type="text" name="existFile" id="existFile" value='${existFile}'/>
	<tr>
            <th align="left">Load File</th>
            <td colspan="2"><input class="file" type="file" name="file" id="file"/></td>
        </tr>
        <tr>
            <th align="left">File Description</th>
            <td colspan="4" class="description"><input type="text" size="50" name="description" id="description"/></td>
        </tr>
        <c:forEach var="fileName" items="${fileNames}">
        <!--<tr><td>${fileName}</td></tr>-->
        </c:forEach>
        <tr>
            <td colspan="2" align="center">                
                <input class="upload-button" name="btnUpload" type="submit" id="btnUpload" value="Upload"/>
                <input class="close-button" type="button" id="close" value="Close" onclick="return false;"/>
            </td>
        </tr>	
    </table>
    
    <iframe id="trgID" name="uploadTrg" height="0" width="0" frameborder="0" scrolling="yes"></iframe>    
</form>        								

</body>
</html>
