<%--
    Document   : PDFReport
    Created on : Dec 14, 2015, 9:08:05 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
#pdfreport_tab{
    overflow: hidden;
}
.change_log table{
    margin-top: 10px;
    width: 100%
}
.change_log table th{
    text-align: center;
    border: 1px solid #cccccc;
    padding:10px 0px;
    font-size: 15px;
    background: none repeat scroll 0 0 #e3e3e3;
}
.change_log table td{
    font-size: 15px;
    border: 1px solid #cccccc;
    padding: 5px;
}

.pdf_report_track table{
    margin-top: 10px;
    width: 100%
}
.pdf_report_track table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    font-size: 15px;
    background: none repeat scroll 0 0 #e3e3e3;
}
.pdf_report_track table td{
    font-size: 15px;
    border: 1px solid #cccccc;
    padding: 5px;
}
.pdf_report_track table tr td:last-child{
    text-align: center;
}
.refresh_icon,.remove{
    cursor:pointer;
    opacity: 0.7;
}
.refresh_icon:hover{
    opacity: 1;
}

</style>
<script type="text/javascript">
    $(document).ready(function(){
        var self = this;
        self.proposalCt = $("#tabs");
        self.changeLogTab = $("#changelog_tab");
        self.createForm = self.proposalCt.find("#create-form");
        self.wtTransId = self.createForm.find("#wtTransId");
//        self.refreshBtn = self.pdfReportTab.find(".refresh_icon");
//        self.removeBtn = self.pdfReportTab.find(".remove");

//        self.removeBtn.on("click",function(){
//            var trackId = $(this).attr("trackId");
//            var tableRow = $(this).parent().parent();
//            $.ajax({
//                type:"POST"
//                ,url:window.SERVER_ROOT + "/proposal/removeReportTracker"
//                ,data:{trackId:trackId}
//                ,cache:false
//                ,success:function(data,status,jqxhr){
//                    tableRow.remove();
//                    alert('Removed Successfully');
//                }
//                ,error:function(xhr, errorType, exception){
//                    alert("Failed at Removing the record!");
//                }
//            });
//        });
//        self.refreshBtn.on("click",function(){
//            var wtTransId = self.createForm.find("#wtTransId").val();
//            self.pdfReportTab.mask("Refreshing...");
//            $.ajax({
//                type:"POST"
//                ,url:window.SERVER_ROOT + "/proposal/PDFReport/"+wtTransId
//                ,cache:false
//                ,success:function(data,status,jqxhr){
//                    self.pdfReportTab.unmask();
//                    self.pdfReportTab.html(data);
//                }
//                ,error:function(xhr, errorType, exception){
//                    alert("Failed refresh Page!");
//                }
//            });
//        });
    });
</script>
<c:if test="${empty(statusTrackList)}">
  <c:set var="hideTable" value="hidden"/>
</c:if>

<div id="changelog_tab">
<!--    <div class="head_explain">
    This page provides a log of edits made to the Proposal Form. Once edits have been made to the Proposal Form, <br/>
    please click Change Log to provide comments and generate a new PDF report. If no edits have been made, click <br/>
    on a filename to view a PDF report.”
    </div>-->
    <!--<div class="tab_header isExpand">Change Log</div>-->
    <div class="contact_panel change_log">
        <table>
            <thead>
                <tr>
                   <th style="width:120px;padding-left:5px;">Submitted Date</th>
                   <th style="width:150px;">Submitted By</th>
                   <th>Submitted Comments</th>
                   <th>Change of fields</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${changeLog}" var="changeLog" >
                <tr>
                    <td><fmt:formatDate value="${changeLog.createdDate}" pattern="MM/dd/yyyy"/></td>
                    <td>${changeLog.changeUser}</td>
                    <td>${changeLog.changeLog}</td>
                    <td>${changeLog.changeField}</td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

