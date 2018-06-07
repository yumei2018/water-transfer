<%-- 
    Document   : editReviewComment
    Created on : Sep 28, 2015, 1:39:52 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form action="" method="post">
  <textarea cols="60" rows="3" name="reviewerComments" placeholder="Enter comments here">${reviewComment.reviewerComments}</textarea>
  <input type="hidden" name="wtReviewCommentId" value="${reviewComment.wtReviewCommentId}"/>
</form>
