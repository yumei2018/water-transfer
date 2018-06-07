<%@page isErrorPage="true" %>
<%@include file="templates/header.jsp" %>
<div style="padding:20px;">
  <h1 style="color:red;">${errorTitle}</h1>
  <p style="font-weight:bold;">${error}</p>
</div>
<%@include file="templates/footer.jsp" %>