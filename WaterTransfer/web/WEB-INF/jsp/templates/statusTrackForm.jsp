<%--
    Document   : statusTrackForm
    Created on : Apr 17, 2015, 11:43:55 AM
    Author     : ymei
--%>
<%@page import="com.gei.context.LookupDataContext"%>
<%
  if (pageContext.getAttribute("LookupDataContext") == null) {
    pageContext.setAttribute(LookupDataContext.class.getSimpleName(), LookupDataContext.getInstance());
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<form class="form" action="" id="statusTrackForm" method="post">
  <div id="status_track_ct" style="display:none;">
    <input type="hidden" id="wtTransId" name="wtTransId" value="" />
    <div>
      <label style="padding-right:20px;">Change Status</label>
      <select class="fieldValue" name="wtStatusFlagId" id="wtStatusFlagId" style="width:200px;">
        <c:forEach var="status" items="${LookupDataContext.statusFlagLookup}">
          <option value="${status.wtStatusFlagId}">${status.statusDescription}</option>
        </c:forEach>
      </select>
    </div>
    <div style="padding-top: 20px;">
      <label style="padding-right:13px;">Status Date</label>
      <input class="fieldValue dateField" text="text" style="width:200px;" id="statusDate" name="statusDate"/>
    </div>
    <div style="padding-top: 20px;">
      <label>Comments</label><br/>
      <textarea class="fieldValue" style="width:400px;"  id="statusComments" name="statusComments"></textarea>
    </div>
  </div>
</form>
<script type="text/javascript">
  $(document).ready(function () {
    var self = this;
    self.proposalCt = $("#tabs");
    self.statusTrackTab = self.proposalCt.find("#statustrack_tab");
    self.trackStatus = self.statusTrackTab.find(".statusDescription");
    self.statusTrackForm = $("#statusTrackForm");
    self.statusOption = self.statusTrackForm.find("#wtStatusFlagId option");

//    self.statusOption.each(function () {
////            alert($(this).html());
//      var thisStatus = $(this);
//      var thisStatusV = $(this).html();
//      self.trackStatus.each(function () {
////                alert($(this).html());
//        if ($(this).html() === thisStatusV) {
//          thisStatus.addClass('hidden');
//        }
//      });
//    });
  });
</script>