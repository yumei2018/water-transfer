<%-- 
    Document   : GetDataTrans
    Created on : Dec 16, 2014, 10:26:49 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <base href="${pageContext.request.contextPath}">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/wthome.css">
        <script src="${pageContext.request.contextPath}/resources/js/WtTrans.js"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
        
        <script type="text/javascript">window.SERVER_ROOT = "${pageContext.request.contextPath}";</script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.1.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.MultiFile.js"></script>
        <script>
            $(document).ready(function(){              
                setTimeout(function(){
                    new WtTrans();
                },1);
                
                $('.addFile-button').click(function(){
                    var url =window.SERVER_ROOT + "/uploadFile?wtTransId="+$('#wtTransId').val();
                    window.open(url,'_blank','width=500,height=200,resizable=no');
                });
                
                $('.picture-delete-icon').click(function(){
//                    alert($(this).parents("tr").find("td:first").text());
                    var url =window.SERVER_ROOT + "/trans/removeFile?wtTransId="+$('#wtTransId').val()+"&wtAttachmentId="+$(this).closest('tr').attr('id');
                 
                    $.ajax({
                        url:url
                        ,cache:false
                        ,scope:self
                        ,success:function(data,status,jqxhr){
                            $(this).closest('tr').remove();
                            alert('Attached file removed.');
                        }
                    }); 
                });
                
//                $('.addFile-button').click(function(){
//                    var url =window.SERVER_ROOT + "/uploadFile";
//                    $( "#dialog-modal" ).dialog({
//                        height: 200,
//                        width: 500,
//                        modal: true
//                    });
//                    $.ajax({
//                        url: url
//                        ,success: function(data){
//                            $('#dialog-modal p').html(data);
//                        }
//                    });
//                });
//                $('.multi-pt').MultiFile({ 
//                    accept:'pdf|gif|jpg', 
//                    max:3, 
//                    STRING: { 
//                        remove:'Remover', 
//                        selected:'Selecionado: $file', 
//                        denied:'Invalid file type $ext!', 
//                        duplicate:'This file already be selected:\n$file!' 
//                    } 
//                });
            });
        </script>
    </head>

    <body>
        <form class="form" id="update-form">
            <div id="title-top">${bodyMessage}</div>
            
            <table id="">
                <tr class="tableRow">
                    <th>Transfer ID</th><td><input class="readonlyField" type="text" name="wtTransId" id="wtTransId" value="${transRecord.wtTransId}" readonly="true"/></td>
                    <th>Transfer Num</th><td><input class="readonlyField" type="text" name="wtTransNum" id="wtTransNum" value="${transRecord.wtTransNum}" readonly="true"/></td>
                    <th>Transfer Year</th><td><input class="readonlyField" type="text" name="transYear" id="transYear" value="${transRecord.transYear}" readonly="true"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Seller</th>            
                    <c:forEach var="wtSeller" items="${transRecord.wtSellerCollection}" varStatus="iterator">
                        <td class="rowCell">
                           <input class="readonlyField" type="checkbox" name="wtSeller" id="wtSeller" value="${wtSeller.agencyCode}" checked="checked" disabled/>${wtSeller.agencyCode}</td>
                        </td>
                    </c:forEach>            
                    <th>Buyer</th>            
                    <c:forEach var="wtBuyer" items="${transRecord.wtBuyerCollection}" varStatus="iterator">
                        <td class="rowCell">
                           <input class="readonlyField" type="checkbox" name="wtBuyer" id="wtBuyer" value="${wtBuyer.agencyCode}" checked="checked" disabled/>${wtBuyer.agencyCode}</td>
                        </td>
                    </c:forEach>            
                </tr>
                <tr class="tableRow">
                    <th>Proposed Transfer Quantity</th><td><input class="readonlyField" type="number" name="proTransQua" id="proTransQua" value="${transRecord.proTransQua}" readonly="true"/></td>
                    <th>Actual Transfer Quantity</th><td><input class="readonlyField" type="number" name="actTransQua" id="actTransQua" value="${transRecord.actTransQua}" readonly="true"/></td>
                </tr>
                <tr class="tableRow">
                    <th>DWR Proposal Approved Date</th><td><input class="readonlyField" type="date" id="dwrProApprDate" name="dwrProApprDate" value='<fmt:formatDate value="${transRecord.dwrProApprDate}" pattern="MM/dd/yyyy"/>' readonly="true"/></td>
                    <th>Start Transfer Window</th><td><input class="readonlyField" type="date" id="transWinStart" name="transWinStart" value='<fmt:formatDate value="${transRecord.transWinStart}" pattern="MM/dd/yyyy"/>' readonly="true"/></td>
                    <th>End Transfer Window</th><td><input class="readonlyField" type="date" id="transWinEnd" name="transWinEnd" value='<fmt:formatDate value="${transRecord.transWinEnd}" pattern="MM/dd/yyyy"/>' readonly="true"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Proposed Agreement Amt Paid to Seller $</th><td><input class="readonlyField" type="number" id="proAgreePaid" name="proAgreePaid" value="${transRecord.proAgreePaid}" readonly="true"/></td>
                    <th>Actual Amt Paid to Seller $</th><td><input class="readonlyField" type="number" id="actAmtPaid" name="actAmtPaid" value="${transRecord.actAmtPaid}" readonly="true"/></td>
                    <th>Calculated Amt Paid to Seller $</th><td><input class="readonlyField" type="number" id="calAmtPaid" name="calAmtPaid" value="${transRecord.calAmtPaid}" readonly="true"/></td>
                </tr> 
                <tr class="tableRow">
                    <th>Proposed Unit Cost of Water $</th><td><input class="readonlyField" type="number" id="proUnitCost" name="proUnitCost" value="${transRecord.proUnitCost}" readonly="true"/></td>
                    <th>Calculated Unit Cost of Water $</th><td><input class="readonlyField" type="number" id="calUnitCost" name="calUnitCost" value="${transRecord.calUnitCost}" readonly="true"/></td>
                    <th>Facilities Used</th>
                    <td colspan='2'>
                        <input class="readonlyField" type="checkbox" id="wtFuTypeId" name="wtFuType" value="CVP" 
                        <c:forEach var="fuType" items="${transRecord.wtFuTypeCollection}" varStatus="iterator">
                            <c:if test="${fuType.fuType eq 'CVP'}">checked="checked"</c:if>
                        </c:forEach>
                        />CVP
                        <input class="readonlyField" type="checkbox" id="wtFuTypeId" name="wtFuType" value="SWP" 
                        <c:forEach var="fuType" items="${transRecord.wtFuTypeCollection}" varStatus="iterator">
                            <c:if test="${fuType.fuType eq 'SWP'}">checked="checked" </c:if>
                        </c:forEach>
                        />SWP
                        <input class="readonlyField" type="checkbox" id="wtFuTypeId" name="wtFuType" value="OTHER" 
                        <c:forEach var="fuType" items="${transRecord.wtFuTypeCollection}" varStatus="iterator">
                            <c:if test="${fuType.fuType eq 'OTHER'}">checked="checked"</c:if>
                        </c:forEach>
                        />OTHER
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Proposed Acreage to be Idled</th><td><input class="readonlyField" type="number" id="proAcrIdle" name="proAcrIdle" value="${transRecord.proAcrIdle}" readonly="true"/></td>
                    <th>Is transfer involves fallowing?</th>
                    <td>
                        <input type="radio" id="proAcrIdleInd" name="proAcrIdleInd" value="false" disabled <c:if test="${not fuType.proAcrIdleInd}">checked="checked"</c:if>/>No
                        <input type="radio" id="proAcrIdleInd" name="proAcrIdleInd" value="true" disabled <c:if test="${fuType.proAcrIdleInd}">checked="checked"</c:if>/>Yes
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Actual Fallowed Acreage</th><td><input class="readonlyField" type="number" id="actFallAcr" name="actFallAcr" value="${transRecord.actFallAcr}" readonly="true"/></td>
                    <th>Is transfer involves fallowing?</th>
                    <td>
                        <input type="radio" id="actFallAcrInd" name="actFallAcrInd" value="false" disabled <c:if test="${not fuType.actFallAcrInd}">checked="checked"</c:if>/>No
                        <input type="radio" id="actFallAcreInd" name="actFallAcrInd" value="true" disabled <c:if test="${fuType.actFallAcrInd}">checked="checked"</c:if>/>Yes
                    </td>
                </tr>
                <tr class="tableRow">
                <th></th><td></td>
                    <th>Is Reservoir Re-Op?</th>
                    <td>
                        <input type="radio" id="resReOpInd" name="resReOpInd" value="false" disabled <c:if test="${not fuType.resReOpInd}">checked="checked"</c:if>/>No
                        <input type="radio" id="resReOpInd" name="resReOpInd" value="true" disabled <c:if test="${fuType.resReOpInd}">checked="checked"</c:if>/>Yes
                    </td>
                </tr>
                <tr class="tableRow">
                    <th></th><td></td>
                    <th>Is Conserved Water?</th>
                    <td>
                        <input type="radio" id="consWaterInd" name="consWaterInd" value="false" disabled <c:if test="${not fuType.consWaterInd}">checked="checked"</c:if>/>No
                        <input type="radio" id="consWaterInd" name="consWaterInd" value="true" disabled <c:if test="${fuType.consWaterInd}">checked="checked"</c:if>/>Yes
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Number of Wells Used</th><td><input class="readonlyField" type="number" id="wellUseNum" name="wellUseNum" value="${transRecord.wellUseNum}" readonly="true"/></td>
                    <th>Is transfer involves fallowing?</th>
                    <td>
                        <input type="radio" id="wellUseNumInd" name="wellUseNumInd" value="false" disabled <c:if test="${not fuType.wellUseNumInd}">checked="checked"</c:if>/>No
                        <input type="radio" id="wellUseNumInd" name="wellUseNumInd" value="true" disabled <c:if test="${fuType.wellUseNumInd}">checked="checked"</c:if>/>Yes
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Comments</th><td colspan='3'><textarea cols="50" rows="3" id="wtComm" name="wtComm">${transRecord.wtComm}</textarea></td>
                </tr>
            </table>
        
            <div id="add-attachment">
                <input class="addFile-button" type="button" id="addFile" value="Add New Attachment" onclick="return false;"/>
                <!--Add Attachment Here:<input type="file" class="multi-pt" id="multiFile" name="files[]" multiple/>--> 
            </div>
            <div style="display:none">
                <div id="dialog-modal" title="Add New Attachment">
                <p>Loading</p>
                </div>
            </div>
            <table id="attachment-form">
                <tr class="attach_header">
                    <th>Attachment ID</th>
                    <th>Attachment File</th>
                    <th>Description</th>
                    <th>View Attachment</th>
                    <th>Download Attachment</th>
                    <th>Remove Attachment</th>
                </tr>
                <c:forEach var="attachment" items="${transRecord.wtAttachmentCollection}">
                    <tr class="attach_row" id='${attachment.wtAttachmentId}'>  
                        <td name="wtAttachmentId" id="wtAttachmentId">${attachment.wtAttachmentId}</td>
                        <td>${attachment.filename}</td>
                        <td name="description" id="description">${attachment.description}</td>
                        <td id="link-attachment">
                            <a href="${getAttUrl}?wtAttachmentId=${attachment.wtAttachmentId}" onClick="document.location.reload(true)" target="_blank">
                                <img src="${pageContext.request.contextPath}/resources/images/icons/link_go.png" alt="Open Attachment" title="Open Attachment">
                            </a>
                        </td>
                        <td id="download-attachment">
                            <a href="${getAttUrl}?wtAttachmentId=${attachment.wtAttachmentId}&output=file">
                                <img src="${pageContext.request.contextPath}/resources/images/icons/circle_down_arrow.png" alt="Save Attachment" title="Save Attachment">
                            </a>
                        </td>
                        <td id="remove-attachment">
                            <img class="picture-delete-icon" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" alt="Remove Attachment" title="Remove Attachment">                          
                        </td>
                    </tr>
                </c:forEach>
            </table>
        
            <div id="form-button">
                <input class="update-button" type="button" id="update" value="Update" onclick="return false;"/>
                <input class="close-button" type="button" id="close" value="Close" onclick="return false;"/>
            </div>
        </form>
        
    </body>
</html>