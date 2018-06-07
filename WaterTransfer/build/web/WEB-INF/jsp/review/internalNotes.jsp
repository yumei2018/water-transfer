<%-- 
    Document   : internalNotes
    Created on : Dec 21, 2016, 2:14:06 PM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="isManager" value="hidden"/>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="isManager" value=""/>
</c:if>
<form action="" method="post">
  <textarea class="internalNote" placeholder="Enter the Comment" val="" onblur="" style="width: 450px; height: auto;"></textarea>
  
  <div class="notesCt">
    <c:forEach items="${internalNotes}" var="iNote" >
      <div style="color:#003cb3;padding-top:20px;">
        <span style="font-size: 15px;">${iNote.updatedBy.name}</span>
        <span style="padding-left: 10px;">${iNote.noteDate}</span>
        <img class="${isManager}"
              style="cursor:pointer;"
              noteId="${iNote.wtInternalNoteId}"
              src="${pageContext.request.contextPath}/resources/images/icons/crossx.png"
              onclick="window.reviewer.initDeleteNote(this);"/>
      </div>
      <div style="padding-left: 40px;">${iNote.note}</div>
    </c:forEach>
  </div>
</form>
