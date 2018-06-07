<%-- 
    Document   : reviewComment
    Created on : Dec 2, 2016, 3:53:32 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form action="" method="post">
  <textarea class="techNote" placeholder="Enter the Comment" val="" onblur="" style="height: 65px;"></textarea>
  <input type="hidden" name="createDate" />
  <input type="hidden" name="modifyDate" />
</form>
