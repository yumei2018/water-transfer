<%--
    Document   : milestoneLogs
    Created on : May 8, 2015, 11:16:04 AM
    Author     : pheng
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
        <table id="milestone">
            <thead>
                <tr>
                    <th>Seller</th>
                    <th>Buyer</th>
                    <th>Maximum Quantity (AF)</th>
                    <th>Type of Transfer</th>
                    <th>Proposal Received</th>
                    <th>Agreement Executed/Proposal Approved</th>
                    <th>Agency Approval Required</th>
                    <th>Process for SWRCB Approval</th>
                    <th>SWRCB Approval</th>
                    <th>Timing of Water Availability</th>
                    <th>Through Delta Transfer</th>
                    <th>Requested Export Window</th>
                    <th>Fisheries Agency Review for Expanded Export Window(If Applicable)</th>
                    <th>Potential Affected by Water Right Curtailment</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td colspan="14" class="title">Proposals Submitted</td>
                </tr>
                <tr>
                    <td colspan="14" class="subtitle">Temporary Transfer Proposal</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
<!--                <tr class="rec_data">
                    <td>DWR/USBR Consolidated Place of Use</td>
                    <td>SWP/CVP and Contractors</td>
                    <td>up to 335,560</td>
                    <td>Project Water Exchanges</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>SWP - 1/20/15</td>
                    <td>Project Delivery Schedules</td>
                    <td>No</td>
                    <td>N/A</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr class="rec_data">
                    <td>CCWD</td>
                    <td>ACWD & ACFCWCD, Zone 7</td>
                    <td>5000</td>
                    <td>Exchange of SWP/CVP water</td>
                    <td>10/1/2014</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>WC 1725</td>
                    <td>Petition Filed 3/25/15</td>
                    <td>Winter/Spring to CCWD</td>
                    <td>No</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr class="rec_data">
                    <td>Foresthill PUD</td>
                    <td>SCVWD</td>
                    <td>2000</td>
                    <td>Reservoir Release</td>
                    <td>12/19/2014</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>WC 1725</td>
                    <td>Petition Filed 3/5/15</td>
                    <td>Folsom Release Schedule</td>
                    <td>No</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>No - Release of previously stored water</td>
                </tr>
                <tr class="rec_data">
                    <td>Garden Highway MWC</td>
                    <td>SWP Contractors</td>
                    <td>7500</td>
                    <td>GW Substitution</td>
                    <td>3/27/2015</td>
                    <td>&nbsp;</td>
                    <td>DWR/SWRCB</td>
                    <td>WC 1725</td>
                    <td>Petition Noticed 4/6/15</td>
                    <td>May-Oct</td>
                    <td>Yes</td>
                    <td>May-Oct</td>
                    <td>&nbsp;</td>
                    <td>Water Under Settlement Agreement will be used if Water Right is curtailed</td>
                </tr>
                <tr class="rec_data">
                    <td>Plumas MWC</td>
                    <td>SWP Contractors</td>
                    <td>4828</td>
                    <td>GW Substitution</td>
                    <td>3/27/2015</td>
                    <td>&nbsp;</td>
                    <td>DWR/SWRCB</td>
                    <td>WC 1725</td>
                    <td>Petition Noticed 4/6/15</td>
                    <td>May-Oct</td>
                    <td>Yes</td>
                    <td>May-Oct</td>
                    <td>&nbsp;</td>
                    <td>Water Under Settlement Agreement will be used if Water Right is curtailed</td>
                </tr>-->
                <tr>
                    <td colspan="2" class="total">Subtotal</td>
                    <td colspan="1" class="tmpTransPro_subtot subtotal_proSubmit"></td>
                </tr>
                <tr>
                    <td colspan="14" class="subtitle">Long-Term Transfers</td>
                </tr>

                 <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="lgnTermTrans_maxqty">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr class="rec_data">
                    <td rowspan="2">Yuba County Water Agency</td>
                    <td rowspan="2">SWP/CVP and Contractors</td>
                    <td class="lgnTermTrans_maxqty">53,000</td>
                    <td>Reservoir Release</td>
                    <td rowspan="2"></td>
                    <td rowspan="2"></td>
                    <td rowspan="2"></td>
                    <td rowspan="2"></td>
                    <td rowspan="2"></td>
                    <td>Res Release Sched</td>
                    <td rowspan="2">Yes</td>
                    <td rowspan="2">Juy-Sep</td>
                    <td rowspan="2"></td>
                    <td rowspan="2"></td>
                </tr>
                <tr class="rec_data">
                    <td class="lgnTermTrans_maxqty">30,000</td>
                    <td>GW Substitution</td>
                    <td>Res Release Sched</td>
                </tr>
                <tr class="rec_data">
                    <td>Browns Valley Irrigation District</td>
                    <td>SCVWD</td>
                    <td class="lgnTermTrans_maxqty">3,100</td>
                    <td>Conservation</td>
                    <td></td>
                    <td>7/12/2013</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>Res Release Sched</td>
                    <td>Yes</td>
                    <td>Jul-Sep</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="rec_data">
                    <td>South Sutter Water District</td>
                    <td>DWR</td>
                    <td class="lgnTermTrans_maxqty">4,400</td>
                    <td>Reservoir Release</td>
                    <td></td>
                    <td>2/14/2000</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>Res Release Sched</td>
                    <td>Yes</td>
                    <td>Jul-Sep</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="2" class="total">Subtotal</td>
                    <td colspan="1" class="lgnTermTrans_subtot subtotal_proSubmit">94,500</td>
                </tr>
                <tr>
                    <td colspan="2" class="total">Total</td>
                    <td colspan="1" class="proSubmit_total">110,328</td>
                </tr>
                <tr>
                    <td colspan="14" class="title">Potential Proposals to DWR</td>
                </tr>
                <tr>
                    <td colspan="14" class="subtitle">Proposals Being Developed/Environmental or SWRCB Requirements Initiated</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="proDev_maxQty">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr class="rec_data">
                    <td>Cordua ID</td>
                    <td>SWP Contractors</td>
                    <td class="proDev_maxQty">6000</td>
                    <td>Reservoir ReOp/GW Sub</td>
                    <td>4/10/2015</td>
                    <td></td>
                    <td>YCWA</td>
                    <td>Pre-1914</td>
                    <td></td>
                    <td>May-Sep</td>
                    <td>Yes</td>
                    <td>Jul-Sep</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="rec_data">
                    <td>South Sutter Water District</td>
                    <td>SWP Contractors</td>
                    <td class="proDev_maxQty">6000</td>
                    <td>GW Sub/Reservoir ReOp</td>
                    <td></td>
                    <td></td>
                    <td>DWR/SWRCB</td>
                    <td>Post-1914</td>
                    <td></td>
                    <td>Jul-Sep</td>
                    <td>Yes</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="2" class="total">Total</td>
                    <td colspan="1" class="proDev_total"></td>
                </tr>
                <tr>
                    <td colspan="14" class="subtitle">Other Potential Proposals</td>
                </tr>
                 <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr class="rec_data">
                    <td>South Feather WPA</td>
                    <td>SWP Contractors</td>
                    <td></td>
                    <td>Reseroir Reoperation</td>
                    <td></td>
                    <td></td>
                    <td>SWRCB</td>
                    <td>WC 1725</td>
                    <td></td>
                    <td></td>
                    <td>Yes</td>
                    <td>Jul-Sep</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="rec_data">
                    <td>BBID</td>
                    <td>ACFCWCD, Zone 7</td>
                    <td>2,000</td>
                    <td>Land Conversion</td>
                    <td></td>
                    <td></td>
                    <td>DWR/USBR</td>
                    <td>Pre-1914</td>
                    <td></td>
                    <td></td>
                    <td>No</td>
                    <td></td>
                    <td></td>
                    <td>Yes if Pre-14 rights are curtailed</td>
                </tr>
                <tr>
                    <td colspan="14" class="subtitle">Potential Transfers Conveyed Through non-Project Facilities</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr class="rec_data">
                    <td>Yuba County Water Agency</td>
                    <td>Dublin San Ramon SD</td>
                    <td>1,500</td>
                    <td>Reservoir Release</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>No</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="14" class="title">Proposals Submitted to Bureau of Reclamation</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="14" class="title">Potential Proposals to Reclamation</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="14" class="title">Proposals Submitted to SWRCB not Listed Above</td>
                </tr>
                <c:forEach items="${WtTrans}" var="trans">
                    <c:set var="deltaTrans" value="No"/>
                    <c:if test="${trans.deltaTransferInd eq 1}">
                        <c:set var="deltaTrans" value="Yes"/>
                    </c:if>
                    <tr class="rec_data">
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
                        <td class="">${trans.proTransQua}</td>
                        <td>${trans.wtTransId}</td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.proReceivedDate}" /></td>
                        <td><fmt:formatDate pattern="MM/dd/yyyy" value="${trans.dwrProApprDate}" /></td>
                        <td>
                            <c:forEach items="${trans.wtApprovalCollection}" var="appCol" varStatus="status">
                                <c:set var="separator" value=""/>
                                <c:if test="${!(status.last)}">
                                    <c:set var="separator" value="/"/>
                                </c:if>
                                ${appCol.agencyCode}${separator}
                            </c:forEach>
                        </td>
                        <td>${trans.swrcbAppProcess}</td>
                        <td>${trans.swrcbApp}</td>
                        <td>${trans.waterAvlTiming}</td>
                        <td>${deltaTrans}</td>
                        <td>${trans.reqExpWin}</td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
<style>
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
            self.calTmpTransProSub();
            self.calLngTermTransSub();
            self.proSubmitTot();
            self.proDevTotal();
        };
        self.initItems=function()
        {
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