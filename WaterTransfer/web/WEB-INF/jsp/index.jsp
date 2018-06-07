<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="templates/header.jsp" %>
<style type="text/css">
div#apps-ct .app-btn
{
  display:inline-block;
  padding:20px;
  width:80px;
  text-decoration: none;
  border-radius:10px;
  border:1px solid #aaa;
  color:#e9e9e9;
  margin-right:20px;
  
  background-image: linear-gradient(#0b94c3, #0c83ab 80%);

  //background: rgba(73,155,234,1);
  //background: -moz-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  //background: -webkit-gradient(radial, center center, 0px, center center, 100%, color-stop(0%, rgba(73,155,234,1)), color-stop(100%, rgba(1,75,114,1)));
  //background: -webkit-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  //background: -o-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  //background: -ms-radial-gradient(center, ellipse cover, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  //background: radial-gradient(ellipse at center, rgba(73,155,234,1) 0%, rgba(1,75,114,1) 100%);
  //filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#499bea', endColorstr='#014b72', GradientType=1 );
}

div#apps-ct .app-btn:hover {
  background: #0c83ab;
  //background: -moz-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  //background: -webkit-gradient(radial, center center, 0px, center center, 100%, color-stop(0%, rgba(255,175,75,1)), color-stop(100%, rgba(255,146,10,1)));
  //background: -webkit-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  //background: -o-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  //background: -ms-radial-gradient(center, ellipse cover, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  //background: radial-gradient(ellipse at center, rgba(255,175,75,1) 0%, rgba(255,146,10,1) 100%);
  //filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffaf4b', endColorstr='#ff920a', GradientType=1 );
}
.home_navi{
    background-color:#fbad23;
}
</style>
<div id="login-subheader">
  
</div>
<div class="welcome" style="text-align:center;">
  <h2>Welcome to the Water Transfer Information Management System</h2>
  <div id="apps-ct" style="width:500px;margin:20px auto 0;">
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isAppAccount()}">
      <a href="${pageContext.request.contextPath}/proposal/new" class="app-btn">Create A New Proposal</a>
    </c:if>
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isAppAccount()
                  ||sessionScope.USER.isBuyersRepresentative()||sessionScope.USER.isUSBR()}">
      <a href="${pageContext.request.contextPath}/proposal?moduleType=view" class="app-btn">View/Edit </br></br> Proposals</a>
    </c:if>
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isReviewer()}">
      <a href="${pageContext.request.contextPath}/proposal?moduleType=current" class="app-btn">Current Proposals</a>
      <a href="${pageContext.request.contextPath}/proposal?moduleType=archived" class="app-btn">Archived Proposals</a>
<!--  <a href="${pageContext.request.contextPath}/proposal?moduleType=submit" class="app-btn">Submitted Proposals</a>
      <a href="${pageContext.request.contextPath}/proposal?moduleType=historic" class="app-btn">Historic Proposals</a>-->
<!--  <a href="${pageContext.request.contextPath}/proposal?moduleType=review" class="app-btn">Review Proposals</a>-->
    </c:if>
    <c:if test="${sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager()}">
<!--  <a href="${pageContext.request.contextPath}/proposal?moduleType=approve" class="app-btn">Approve Proposals</a>
      <a href="${pageContext.request.contextPath}/proposal?moduleType=track" class="app-btn">Track Proposals</a>-->
      <a href="${pageContext.request.contextPath}/proposal?moduleType=draft" class="app-btn">Draft Proposals</a>
      <a href="${pageContext.request.contextPath}/proposal?moduleType=submit" class="app-btn">Submitted Proposals</a>
      <a href="${pageContext.request.contextPath}/proposal?moduleType=historic" class="app-btn">Historic Proposals</a>      
    </c:if>
  </div>
</div>
<%@include file="templates/footer.jsp" %>
