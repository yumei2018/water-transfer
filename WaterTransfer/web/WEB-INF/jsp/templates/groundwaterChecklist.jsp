<%-- 
    Document   : groundwaterChecklist
    Created on : Sep 25, 2015, 2:46:49 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>Checklist of Required Information</div>
<table id="required-checklist-table" class="comment-table">
  <thead>
    <tr class="attach_header">
      <th>Required Information</th>
      <th>Meets Requirement(s)</th>
      <th>Comments</th>
      <th>Response</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="reviewComment" items="${reviewCommentList}" varStatus="ct">
      <c:if test="${ct.index < 13}">
      <tr class="attach_row">
        <td width="250">${reviewComment.wtChecklist.name}</td>
        <td width="70">
            <select name="issueStatus">
              <option value="NEW" <c:if test="${reviewComment.issueStatus eq 'NEW'}">selected</c:if>>NEW</option>
              <option value="INCOMPLETE" <c:if test="${reviewComment.issueStatus eq 'INCOMPLETE'}">selected</c:if>>INCOMPLETE</option>
              <option value="COMPLETE" <c:if test="${reviewComment.issueStatus eq 'COMPLETE'}">selected</c:if>>COMPLETE</option>
            </select>
        </td>
        <td><span>${reviewComment.reviewerComments}</span><input type="button" value="Edit" name="editComment" class="eidt_comment"/></td>
        <td width="60"><input type="button" value="View" name="viewResponse" class="view_response"/></td>
        <input type="hidden" value="${reviewComment.wtReviewCommentId}" name="wtReviewCommentId"/>
      </tr>
      </c:if>
    </c:forEach>
  </tbody>
</table>

<div style="margin-top:20px;">Groundwater Substitution Transfer Monitoring Plan</div>
<table id="monitoring-checklist-table" class="comment-table">
  <thead>
    <tr class="attach_header">
      <th>Required Elements of Plan</th>
      <th>Meets Requirement(s)</th>
      <th>Comments</th>
      <th>Response</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="reviewComment" items="${reviewCommentList}" varStatus="ct">
      <c:if test="${ct.index > 12 && ct.index < 20}">
      <tr class="attach_row">
        <td width="250">${reviewComment.wtChecklist.name}</td>
        <td width="70">
            <select name="issueStatus">
              <option value="NEW" <c:if test="${reviewComment.issueStatus eq 'NEW'}">selected</c:if>>NEW</option>
              <option value="INCOMPLETE" <c:if test="${reviewComment.issueStatus eq 'INCOMPLETE'}">selected</c:if>>INCOMPLETE</option>
              <option value="COMPLETE" <c:if test="${reviewComment.issueStatus eq 'COMPLETE'}">selected</c:if>>COMPLETE</option>
            </select>
        </td>
        <td><span>${reviewComment.reviewerComments}</span><input type="button" value="Edit" name="editComment" class="eidt_comment"/></td>
        <td width="60"><input type="button" value="View" name="viewResponse" class="view_response"/></td>
        <input type="hidden" value="${reviewComment.wtReviewCommentId}" name="wtReviewCommentId"/>
      </tr>
      </c:if>
    </c:forEach>
  </tbody>
</table>

<div style="margin-top:20px;">Groundwater Substitution Transfer Mitigation Plan</div>
<table id="mitigation-checklist-table" class="comment-table">
  <thead>
    <tr class="attach_header">
      <th>Required Elements of Plan</th>
      <th>Meets Requirement(s)</th>
      <th>Comments</th>
      <th>Response</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="reviewComment" items="${reviewCommentList}" varStatus="ct">
      <c:if test="${ct.index > 19 && ct.index < 24}">
      <tr class="attach_row">
        <td width="250">${reviewComment.wtChecklist.name}</td>
        <td width="70">
            <select name="issueStatus">
              <option value="NEW" <c:if test="${reviewComment.issueStatus eq 'NEW'}">selected</c:if>>NEW</option>
              <option value="INCOMPLETE" <c:if test="${reviewComment.issueStatus eq 'INCOMPLETE'}">selected</c:if>>INCOMPLETE</option>
              <option value="COMPLETE" <c:if test="${reviewComment.issueStatus eq 'COMPLETE'}">selected</c:if>>COMPLETE</option>
            </select>
        </td>
        <td><span>${reviewComment.reviewerComments}</span><input type="button" value="Edit" name="editComment" class="eidt_comment"/></td>
        <td width="60"><input type="button" value="View" name="viewResponse" class="view_response"/></td>
        <input type="hidden" value="${reviewComment.wtReviewCommentId}" name="wtReviewCommentId"/>
      </tr>
      </c:if>
    </c:forEach>
  </tbody>
</table>

<style type="text/css">
.comment-table{
    width:980px;
    text-align: center;
    margin-bottom: 10px;
}
.comment-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
}
.comment-table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
}
.attach_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
}
.attach_row {
    color: #000000;
    font-size: 15px;
    width: 300px;
}
</style>
<script type="text/javascript">
  var groundwaterChecklist=function(){
    var self = this;
    self.init=function(){
      self.initItems();
      self.initListeners();
    };
    self.initItems=function(){
      self.commentTable = $(".comment-table");
      self.requiredChecklistTable = $("#required-checklist-table");
      self.issueStatus = self.commentTable.find("select[name=issueStatus]");
      self.editCommentBtn = self.commentTable.find(".eidt_comment");
      self.viewResponseBtn = self.commentTable.find(".view_response");
    };
    self.initListeners=function(){
      self.issueStatus.on("change",self.initChangeStatus);
      self.editCommentBtn.on("click",self.initEditComment);
      self.viewResponseBtn.on("click",self.initViewResponse);
    };
    self.initViewResponse=function(){           
      self.viewResponseDialog = $("<div>");
      var wtReviewCommentId = $(this).parent().parent().find("input[name=wtReviewCommentId]").val();
//      alert(wtReviewCommentId);
         
      $.ajax({
        url:window['SERVER_ROOT']+'/proposal/editReviewResponse'
        ,type:'POST'
        ,data:{wtReviewCommentId: wtReviewCommentId}
        ,cache:false
        ,success:function(data,status,jqxhr){
//          alert(data);
          self.viewResponseDialog.append(data);
          self.viewResponseDialog.dialog({
            title:'View/Add Response'
            ,width:700
            ,modal:true
            ,height:'auto'
            ,buttons:[{
              text:'Add Response'
              ,click:function(){
                console.log($(this).find("form").serializeArray());
                self.responseCt = $(this).find(".response-ct");
                var responseBy = $(this).find("select[name=responseBy]").val();
                var responseComments = $(this).find("textarea[name=responseComments]").val();
//                alert(responseComments);
                $.ajax({
                  url:window['SERVER_ROOT']+'/proposal/saveReviewResponse'
                  ,type:'POST'
                  ,data:{wtReviewCommentId: wtReviewCommentId,responseBy:responseBy,responseComments:responseComments}
                  ,cache:false
                  ,success:function(data,status,jqxhr){
//                    alert("Response saved.");
                    var jsonData = JSON.parse(data);
//                    console.log(jsonData);
                    self.responseCt.append('<div>'+jsonData['responseBy']+'</div>');
                    self.responseCt.append('<div>Response By: '+jsonData['userName']+', '+jsonData['agencyCode']+', '+jsonData['createdDate']+'</div>');
                    self.responseCt.append('<div style="margin-left:50px;background-color:#CCC;margin-bottom: 20px;">'+jsonData['responseComments']+'</div>');
                  }
                });
              }
            },{
              text:'Cancel'
              ,click:function(){
                $(this).dialog('close');
              }
            }]
          });
        }
        ,error:function(xhr, errorType, exception){
          if(xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    };
    self.initEditComment=function(){           
//      var self = this;
      self.editCommentDialog = $("<div>");
      self.reviewComment = $(this).parent().find("span");
      var wtReviewCommentId = $(this).parent().parent().find("input[name=wtReviewCommentId]").val();
//      alert(wtReviewCommentId);
         
      $.ajax({
        url:window['SERVER_ROOT']+'/proposal/editReviewComments'
        ,type:'POST'
        ,data:{wtReviewCommentId: wtReviewCommentId}
        ,cache:false
        ,success:function(data,status,jqxhr){
//          alert(data);
          self.editCommentDialog.append(data);
          self.editCommentDialog.dialog({
            title:'Add/Edit Comments'
            ,width:600
            ,modal:true
            ,height:300
            ,buttons:[{
              text:'Save'
              ,click:function(){
                console.log($(this).find("form").serializeArray());
                var reviewerComments = $(this).find("textarea[name=reviewerComments]").val();
//                alert(reviewerComments);
                $.ajax({
                  url:window['SERVER_ROOT']+'/proposal/saveReviewComments'
                  ,type:'POST'
                  ,data:{wtReviewCommentId: wtReviewCommentId,reviewerComments:reviewerComments}
                  ,cache:false
                  ,success:function(data,status,jqxhr){
//                    alert("Comments saved.");
                    var jsonData = JSON.parse(data);
                    console.log(jsonData);
                    self.reviewComment.html(jsonData['reviewerComments']);
                    self.editCommentDialog.dialog('close');
                  }
                });
              }
            },{
              text:'Cancel'
              ,click:function(){
                $(this).dialog('close');
              }
            }]
          });
        }
        ,error:function(xhr, errorType, exception){
          if(xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });
    };
    self.initChangeStatus=function(){ 
      var issueStatus = $(this).val();
      var wtReviewCommentId = $(this).parent().parent().find("input[name=wtReviewCommentId]").val();
//      alert(issueStatus+","+wtReviewCommentId);
      $.ajax({
        url:window['SERVER_ROOT']+'/proposal/saveReviewComments'
        ,type:'POST'
        ,data:{wtReviewCommentId: wtReviewCommentId,issueStatus:issueStatus}
        ,cache:false
        ,success:function(data,status,jqxhr){
        }
        ,error:function(xhr, errorType, exception){
          if(xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
        }
      });      
    };
        
    self.init();
  };
        
  $(document).ready(function(){
    new groundwaterChecklist();
  });
</script>  