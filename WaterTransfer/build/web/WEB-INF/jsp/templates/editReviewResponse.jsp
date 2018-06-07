<%-- 
    Document   : editReviewComment
    Created on : Sep 28, 2015, 1:39:52 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form action="" method="post">
  <div class="response-ct">
  <c:forEach var="responseComment" items="${responseCommentList}">
    <c:set var="createdById">${responseComment.createdById}</c:set>
    <div>${responseComment.responseBy}</div>
    <div>Response By: ${userData.optString(createdById)}, ${agencyData.optString(createdById)}, <fmt:formatDate pattern="MMM dd, yyyy HH:mm a" value="${responseComment.createdDate}" /></div>
    <div style="margin-left:50px;background-color:#CCC;margin-bottom: 20px;">${responseComment.responseComments}</div>
  </c:forEach>
  </div>
  <div>
    <select name="responseBy">
      <option value="Response to Information Requested">Response to Information Requested</option>
      <option value="Reclamation Responses to Additional Information Provided">Reclamation Responses to Additional Information Provided</option>
      <option value="Consultant Response to Information Requested">Consultant Response to Information Requested</option>
      <option value="Agency Responses to Information Requested">Agency Responses to Information Requested</option>
    </select>
  </div>
  <div style="margin-top: 10px;">
    <textarea cols="60" rows="3" name="responseComments" placeholder="Enter response here"></textarea>
  </div>
  <input type="hidden" name="wtReviewCommentId" value="${reviewComment.wtReviewCommentId}"/>
</form>
<script type="text/javascript">
  $(document).ready(function(){
    var self = this;
    self.responseBy = $("select[name=responseBy]");
    self.responseComments = $("textarea[name=responseComments]");
    self.responseBy.on("change", function(){
      self.responseComments.val("");
    });
  });
</script>