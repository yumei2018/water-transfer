<%--
    Document   : list
    Created on : Mar 23, 2015, 5:36:03 PM
    Author     : clay
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
,${pageContext.request.contextPath}/resources/css/wtProposal.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/jquery.maskedinput.js
,${pageContext.request.contextPath}/resources/js/ApplicationLauncher.js
,${pageContext.request.contextPath}/resources/js/proposal/AgencyContactForm.js
,${pageContext.request.contextPath}/resources/js/proposal/ProposalDev.js
,http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style>
    .proposal_navi{
        background-color:#fbad23;
    }
</style>
<script type="text/javascript">
    var newContactHandler =  new ApplicationLauncher({
        javascripts:[window.SERVER_ROOT+"/resources/js/proposal/AgencyContact.js"]
        ,callback:function(){
          new AgencyContact();
        }
    });
    $(document).ready(function(){
        window.app = newContactHandler.launch();
    });
</script>
<span class="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> &gt; <a href="${pageContext.request.contextPath}/proposal">Proposal</a> &gt; Form</span>
<div style="padding:30px 5px 0;" class="">
    <div class="header_tab">Proposal Form</div>
    <div id="tabs">
        <c:if test="${proposal.wtStatusFlag.statusName eq 'SUBMIT'}">
            <%--<c:set var="draft" value="hidden"/>--%>
            <!--<input name="draft" type="hidden" value="0"/>-->
        </c:if>
        <c:if test="${sessionScope.USER.isAppAccount()}">
            <c:set var="showTable" value="hidden"/>
        </c:if>
        <ul>
            <li><a href="#seller_tab">Seller</a></li>
            <li><a href="#buyer_tab">Buyer</a></li>
            <li><a href="#baseinfo_tab">Base Info</a></li>
            <li id="CropIdling"><a href="#cropidling_tab">Crop Idling</a></li>
            <li id="Reservoir"><a href="#reservoir_tab" >Reservoir</a></li>
            <li id="GroundWater"><a href="#groundwater_tab" >Groundwater</a></li>
            <li id="Conserved"><a href="#conserved_tab" >Conserved</a></li>
            <li><a href="#statustrack_tab" class="${showTable}">Proposal Status</a></li>
            <!--<li class="list_btn"><input type="button" value="Add Tab" class="add_tab_button"/></li>-->
        </ul>
        <div id="seller_tab" class="tab_ct">
            <div class="accordion"></div>
            <c:if test="${!empty(proposal)}">
                <c:forEach var="seller_agency" items="${proposal.wtSellerCollection}">
                    <div class="load_seller hidden" agencyid="${seller_agency.wtAgencyId}"></div>
                </c:forEach>
            </c:if>
            <input type="button" value="Choose Seller Agency" name="seller" class="add_agency" />
            <select class="agency_list hidden">
                <option value="0" style="color:#cccccc;">Please select a Seller...</option>
                <c:forEach var="agency" items="${agencyList}">
                    <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
                </c:forEach>
            </select>
        </div>

        <div id="buyer_tab" class="tab_ct">
            <div class="accordion"></div>
            <c:if test="${!empty(proposal)}">
                <c:forEach var="buyer_agency" items="${proposal.wtBuyerCollection}">
                    <div class="load_buyer hidden" agencyid="${buyer_agency.wtBuyerPK.wtAgencyId}" percent="${buyer_agency.sharePercent}"></div>
                </c:forEach>
            </c:if>
            <input type="button" value="Add Buyer Agency" name="buyer" class="add_agency ${draft}" />
            <select class="agency_list hidden">
                <option value="0" style="color:#cccccc;">Please select a Buyer...</option>
                <c:forEach var="agency" items="${agencyList}">
                    <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
                </c:forEach>
            </select>
        </div>

        <form class="form" id="create-form" method="POST">
        <%@include file="BaseInfo.jsp" %>
        <%@include file="CropidlingInfo.jsp" %>
        <%@include file="GroundwaterInfo.jsp" %>
        <%@include file="ReservoirInfo.jsp" %>
        <%@include file="ConservedInfo.jsp" %>
        </form>

        <%@include file="StatusTrack.jsp" %>
    </div>

    <!-- Popup Forms -->
    <%@include file="../attachment/UploadFile.jsp" %>
    <%@include file="../templates/statusTrackForm.jsp" %>
    <!-- End Popup Forms -->

    <div class="button_ct">
        <c:if test="${buttonCheck == true}">
            <c:set var='newProposal' value="hidden"/>
        </c:if>
        <c:if test="${(sessionScope.USER.isSuperAdmin()||sessionScope.USER.isReviewer())
                      && (proposal.wtStatusFlag.statusName eq 'SUBMITTED' || proposal.wtStatusFlag.statusName eq 'REVIEW')}">
<!--            <button class="proposal_btn ${newProposal}" id="reviewProposal">Review</button>
            <button class="proposal_btn ${newProposal}" id="completeProposal">Complete</button>            -->
            <button class="proposal_btn request_btn">Request</button>
        </c:if>
        <c:if test="${(sessionScope.USER.isSuperAdmin()||sessionScope.USER.isManager())
                      && (proposal.wtStatusFlag.statusName eq 'COMPLETE')}">
            <button class="proposal_btn ${newProposal}" id="approveProposal">Approve</button>
        </c:if>

        <c:if test="${proposal.wtStatusFlag.statusName eq 'DRAFT'}">
            <button class="proposal_btn" id="submitProposal">Submit</button>
        </c:if>
        <c:if test="${proposal.wtStatusFlag.statusName eq 'DRAFT' || buttonCheck == true}">
            <button class="proposal_btn" id="saveProposal">Save</button>
        </c:if>
<!--        <button class="proposal_btn ${newProposal}" id="changeStatus">Status</button>-->
    </div>
</div>
<%@include file="../templates/footer.jsp" %>