<%--
    Document   : milestoneLogs
    Created on : Jan 12, 2016, 11:16:04 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js"></script>
    </head>
    <body>
        <div class="main_title">Water Transfer Application Status</div>
        <div class="sub_title">Total Number ${WtTrans.size()} Generated on <label class="today_date"></label></div>
        <table id="milestone">
            <thead>
                <tr>
                    <th>Trans ID</th>
                    <th>Status</th>
                    <th>Seller</th>
                    <th>Buyer</th>
                    <th>Maximum Quantity (AF)</th>
                    <th>Proposal Submitted Date</th>
                    <th>Proposal Complete Date</th>
                    <th>Conditional Approval Date</th>
                    <th>Proposal Approved Date</th>
                    <th>Agency Approval Required</th>
                    <th>Fisheries Agency Review</th>
                    <th>Fisheries Agency Approval Date</th>
                    <th>Banks/Jones Initial%</th>
                    <th>Banks/Jones Final%</th>
                    <th>NBA Initial%</th>
                    <th>NBA Final%</th>
                    <th>Merced Initial%</th>
                    <th>Merced Final%</th>
                    <th>Comments</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${WtTrans}" var="trans">    
                    <c:set var="transIdSM">${trans.wtTransId}SM</c:set>
                    <c:set var="transIdPC">${trans.wtTransId}PC</c:set>
                    <c:set var="transIdCP">${trans.wtTransId}CP</c:set>
                    <c:set var="transIdPP">${trans.wtTransId}PP</c:set>
                    <tr class="rec_data">
                        <td>${trans.wtTransId}</td>
                        <td>${trans.wtStatusFlag.statusName}</td>
                        <td>
                            <c:forEach items="${trans.wtSellerCollection}" var="seller">
                                ${seller.agencyCode}
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${trans.wtBuyerCollection}" var="buyer" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value=", "/>
                                </c:if>
                                ${buyer.wtAgency.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td class="tmpTransPro_maxqty">${trans.proTransQua}</td>
                        <td>${statusTrackList.optString(transIdSM)}</td>
                        <td>${statusTrackList.optString(transIdPC)}</td>
                        <td>${statusTrackList.optString(transIdCP)}</td>
                        <td>${statusTrackList.optString(transIdPP)}</td>
                        <td>
                            <c:if test="${trans.wtAgencyApproval.dwr == 1}">DWR/</c:if>
                            <c:if test="${trans.wtAgencyApproval.swrcb == 1}">SWRCB/</c:if>
                            <c:if test="${trans.wtAgencyApproval.usbr == 1}">USBR/</c:if>
                            <c:if test="${trans.wtAgencyApproval.fishery == 1}">Fishery Agencies/</c:if>
                            <c:if test="${trans.wtAgencyApproval.other == 1}">Other</c:if>
                        </td>
                        <td>
                            <c:if test="${trans.isFisheriesReview == 0}">No</c:if>
                            <c:if test="${trans.isFisheriesReview == 1}">Yes</c:if>
                        </td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.fisheriesApprocalDate}" /></td>
                        <td>${trans.wtWaterLoss.banksInitialPercent}</td>
                        <td>${trans.wtWaterLoss.banksfinalPercent}</td>
                        <td>${trans.wtWaterLoss.nbaInitialPercent}</td>
                        <td>${trans.wtWaterLoss.nbafinalPercent}</td>
                        <td>${trans.wtWaterLoss.mercedInitialPercent}</td>
                        <td>${trans.wtWaterLoss.mercedfinalPercent}</td>
                        <td>${proposal.dwrComments}</td>
                    </tr>
                </c:forEach>   
                 
            </tbody>
        </table>
<!--        <div>${statusTrackList}</div>-->
    </body>
</html>
<style>
    .main_title{
        width: 1200px;
        text-align: center;
        font-size: 36px;
    }
    .sub_title{
        width: 1200px;
        text-align: center;
        font-size: 20px;
        margin-bottom: 30px;
    }
    table{
        border-collapse: collapse;
    }
    table tr th{
        background-color: #f0f0f0;
        border:1px solid #000;
    }
    table .rec_data td{
        border:1px solid #000;
        text-align: center;
    }
    table tr .title{
        font-weight: bold;
        font-size: 14pt;
    }
    table tr .subtitle{
        font-weight: bold;
    }
    table td,table th{
        font-size:10pt;
    }
    table tr .total{
        text-align: right;
        font-weight: bold;
    }
    table tr .total:first-of-type + td{
        text-align: center;
        font-weight: bold;
    }

</style>
<script>
    var milestoneLogs =  function()
    {
        var self = $(this);
        self.init = function()
        {
            self.initItems();
            self.initListeners();
            self.initTitles();
            self.calTmpTransProSub();
            self.calLngTermTransSub();
            self.proSubmitTot();
            self.proDevTotal();
        };
        self.initItems=function()
        {
            self.mainTitle = $(".main_title");
            self.subTitle = $(".sub_title");
            self.todayDate = $(".today_date");
            
            self.milestonetable = $("#milestone");
            self.maxqty = self.milestonetable.find(".tmpTransPro_maxqty");
            self.lngTermTransMaxqty = self.milestonetable.find(".lgnTermTrans_maxqty");
            self.proDevMaxqty = self.milestonetable.find(".proDev_maxQty");

            self.tmptransProSubtot = self.milestonetable.find(".tmpTransPro_subtot");
            self.lngTermTransSubtot = self.milestonetable.find(".lgnTermTrans_subtot");

            self.proSubmitSubTot = self.milestonetable.find(".subtotal_proSubmit");
            self.proSubmitTotal = self.milestonetable.find(".proSubmit_total");
            self.proDevTot =self.milestonetable.find(".proDev_total");
        };
        self.initListeners=function(){};
        
        self.initTitles = function()
        {
            var today = new Date();
            var thisYear = today.getFullYear();
            var thisMonth = today.getMonth()+1;
            var thisDay = today.getDate();
            var formatToday = thisMonth+'/'+thisDay+'/'+thisYear;
            
            self.mainTitle.html("Water Transfer Application Status " + thisYear);
            self.todayDate.html(formatToday);
        };

        self.calTmpTransProSub = function()
        {
            self.total = self.calculateMaxQty.call(self.maxqty);
            self.tmptransProSubtot.html(self.comma(self.total.toString()));
            self.addComma.call(self.maxqty);
        };
        self.calLngTermTransSub = function()
        {
            self.total = self.calculateMaxQty.call(self.lngTermTransMaxqty);
            self.lngTermTransSubtot.html(self.comma(self.total.toString()));
            self.addComma.call(self.lngTermTransMaxqty);
        };
        self.proSubmitTot = function()
        {
            self.total = self.calculateMaxQty.call(self.proSubmitSubTot);
            self.proSubmitTotal.html(self.comma(self.total.toString()));
            self.addComma.call(self.proSubmitSubTot);
        };
        self.proDevTotal = function()
        {
            self.total = self.calculateMaxQty.call(self.proDevMaxqty);
            self.proDevTot.html(self.comma(self.total.toString()));
            self.addComma.call(self.proDevMaxqty);
        };
        self.calculateMaxQty = function()
        {
            var sub=0;
            $.each(this,function()
            {
                sub += parseFloat($(this).html().replace(",",""));
            });
            return sub;
        };
        self.addComma = function()
        {
            $.each(this,function()
            {
                if($(this).html().indexOf(',') === -1)
                {
                    self.val = self.comma($(this).html());
                    $(this).html(self.val);
                }
            });
        }
        self.comma = function(dt)
        {
            return dt.replace(/(.)(?=(.{3})+$)/g,"$1,");
        };

        self.init();
    };
    new milestoneLogs();
</script>