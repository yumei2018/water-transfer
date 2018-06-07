<%--
    Document   : PDFReport
    Created on : Dec 14, 2015, 9:08:05 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
#pdfreport_ct{
    overflow: hidden;
}
.pdf_report_track table{
    margin-top: 10px;
    width: 100%
}
.pdf_report_track table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    text-align: center;
    font-size: 15px;
    background: none repeat scroll 0 0 #9cc7d4;
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
#pdfreport_ct .pdf_report_track .tracktitle{
  text-align: center;
  font-weight: bold;
  margin: 5px 0;
  font-size: 12pt;
}


</style>
<script type="text/javascript">
    $(document).ready(function(){
        var self = this;
        self.proposalCt = $("#tabs");
        self.createForm = self.proposalCt.find("#create-form");
        self.wtTransId = self.createForm.find("#wtTransId");
//        self.pdfReportTab = $("#pdfreport_tab");
        self.pdfReportCt = $("#pdfreport_ct");
        self.pdfReportTable = $(".pdf_report_track");
        self.refreshBtn = self.pdfReportTable.find(".refresh_icon");
        self.removeBtn = self.pdfReportTable.find(".remove");

        self.removeBtn.on("click",function(){
            var trackId = $(this).attr("trackId");
            var tableRow = $(this).parent().parent();
            $.ajax({
                type:"POST"
                ,url:window.SERVER_ROOT + "/proposal/removeReportTracker"
                ,data:{trackId:trackId}
                ,cache:false
                ,success:function(data,status,jqxhr){
                    tableRow.remove();
                    alert('Removed Successfully');
                }
                ,error:function(xhr, errorType, exception){
                    alert("Failed at Removing the record!");
                }
            });
        });
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

<div id="pdfreport_ct">
  <div class="pdf_report_track">
    <c:if test="${!empty(transYear)}">
      <h1 class="tracktitle">${transYear} Water Transfer Proposal Archived</h1>
      <h2 class="tracktitle">${sellerName}</h2>
    </c:if>
    <table>
      <thead>
        <tr>
          <th style="text-align:center;">File Name</th>
          <th>Status</th>
          <th>Created Date</th>
          <th>Size</th>
          <th>Version Number</th>
          <th>Remove</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${trackFile}" var="pdfFile" >
          <c:set var="wtTrackFileId">${pdfFile.wtTrackFileId}</c:set>
          <c:set var="createdById">${pdfFile.createdById}</c:set>
          <tr>
            <td><a href="${pageContext.request.contextPath}/report/view/${pdfFile.wtTrackFileId}" target="_blank">${pdfFile.filename}</a></td>
            <td>${pdfFile.statusName}</td>
            <td><fmt:formatDate value="${pdfFile.createdDate}" pattern="MM/dd/yyyy hh:mm a"/></td>
            <td>${trackFileInfo.optString(wtTrackFileId)}</td>
            <td>${pdfFile.fileVersion}</td>
            <td><img class="remove" src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" title="Remove PDF" trackId="${pdfFile.wtTrackFileId}"/></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

