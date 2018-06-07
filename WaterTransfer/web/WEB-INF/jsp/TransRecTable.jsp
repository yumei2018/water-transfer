<%-- 
    Document   : TransRecTable
    Created on : Dec 5, 2014, 1:03:30 PM
    Author     : ymei
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<table id="transTable" class="table_trans_list tablesorter scrollableFixedHeaderTable" width="1000px" cellspacing="1">
    <thead>
    <tr class="header">
        <th class="wtTransId">Transfer ID</th>
        <th class="wtTransNum">Transfer Numuber</th>
        <th class="transYear">Transfer Year</th>
        <th class="wtSeller">Seller</th>
        <th class="wtBuyer">Buyer</th>        
        <th class="proTransQua">Proposed Trans Qty (AF)</th>
        <th class="actTransQua">Actual Trans Qty (AF)</th> 
        <th class="dwrProApprDate">DWR Proposed Approved Date</th>
        <th class="transWinStart">Start Transfer Window</th>
        <th class="transWinEnd">End Transfer Window</th>
        <th class="proAgreePaid">Proposed Amt Paid</th>
        <th class="actAmtPaid">Actual Amt Paid</th>
        <th class="calAmtPaid">Calculated Amt Paid</th>
        <th class="proUnitCost">Proposed Unit Cost ($/AF)</th>
        <th class="calUnitCost">Calculated Unit Cost ($/AF)</th>
        <th class="fuType">Facilities Used</th>
        <th class="proAcrIdle">Proposed Idled Acres</th>
        <th class="actFallAcr">Actual Fallowed Acres</th>
        <th class="resReOpInd">Reservoir Re-Op</th>
        <th class="consWaterInd">Conserved Water</th>
        <th class="wellUseNum">Wells Used#</th>
        <th class="wtComm">Comments</th>
        <th class="attachment">Attachments</th>
    </tr>  
    </thead>

    <tbody>
    <c:forEach var="record" items="${transRecords}">
        <tr class="id" recordId="${record.wtTransId}">
            <td class="wtTransId" id="wtTransId">
                <a href="${getDataUrl}?wtTransId=${record.wtTransId}" target="_blank">${record.wtTransId}&nbsp;</a>
                <!--<a id="link-trans">${record.wtTransId}</a>-->
            </td>
            <td class="wtTransNum">${record.wtTransNum}&nbsp;</td>
            <td class="transYear">${record.transYear}&nbsp;</td>
            <td class="wtSeller">
                <c:forEach var="wtSeller" items="${record.wtSellerCollection}" varStatus="iterator">
                    <c:choose>
                        <c:when test="${iterator.last}">
                            ${wtSeller.agencyCode}
                        </c:when>
                        <c:otherwise>
                            ${wtSeller.agencyCode}/
                        </c:otherwise>
                    </c:choose> 

                    <%--<c:if test="${wtSeller.agencyCode eq agencyCode}">
                        ${wtSeller.agencyCode}/
                    </c:if>--%>
                </c:forEach>
            </td>
            <td class="wtBuyer">
                <c:forEach var="wtBuyer" items="${record.wtBuyerCollection}" varStatus="iterator">
                    <c:choose>
                        <c:when test="${iterator.last}">
                            ${wtBuyer.agencyCode}
                        </c:when>
                        <c:otherwise>
                            ${wtBuyer.agencyCode}/
                        </c:otherwise>
                    </c:choose>                    
                </c:forEach>
            </td>            
            <td class="proTransQua">${record.proTransQua}&nbsp;</td>
            <td class="actTransQua">${record.actTransQua}&nbsp;</td>
            <td class="dwrProApprDate"><fmt:formatDate value="${record.dwrProApprDate}" pattern="MM/dd/yyyy"/></td>
            <td class="transWinStart"><fmt:formatDate value="${record.transWinStart}" pattern="MM/dd/yyyy"/></td>
            <td class="transWinEnd"><fmt:formatDate value="${record.transWinEnd}" pattern="MM/dd/yyyy"/></td>
            <td class="proAgreePaid"><fmt:formatNumber value="${record.proAgreePaid}" type="currency"/></td>
            <td class="actAmtPaid"><fmt:formatNumber value="${record.actAmtPaid}" type="currency"/></td>
            <td class="calAmtPaid"><fmt:formatNumber value="${record.calAmtPaid}" type="currency"/></td>
            <td class="proUnitCost"><fmt:formatNumber value="${record.proUnitCost}" type="currency"/></td>
            <td class="calUnitCost"><fmt:formatNumber value="${record.calUnitCost}" type="currency"/></td>
            <td class="fuType">
                <c:forEach var="fuType" items="${record.wtFuTypeCollection}" varStatus="iterator">
                    <c:choose>
                        <c:when test="${iterator.last}">
                            ${fuType.fuType}
                        </c:when>
                        <c:otherwise>
                            ${fuType.fuType}/
                        </c:otherwise>
                    </c:choose> 
                </c:forEach>
            </td>
            <td class="proAcrIdle">
                <c:choose>
                    <c:when test="${!empty(record.proAcrIdle)}">
                        ${record.proAcrIdle}
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${record.proAcrIdleInd}">x</c:when>
                            <c:otherwise>n/a</c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="actFallAcr">
                <c:choose>
                    <c:when test="${!empty(record.actFallAcr)}">
                        ${record.actFallAcr}
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${record.actFallAcrInd}">x</c:when>
                            <c:otherwise>n/a</c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="resReOpInd">
                <c:choose>
                    <c:when test="${record.resReOpInd}">x</c:when>
                    <c:otherwise>n/a</c:otherwise>
                </c:choose>
            </td>
            <td class="consWaterInd">
                <c:choose>
                    <c:when test="${record.consWaterInd}">x</c:when>
                    <c:otherwise>n/a</c:otherwise>
                </c:choose>
            </td>
            <td class="wellUseNum">
                <c:choose>
                    <c:when test="${!empty(record.wellUseNum)}">
                        ${record.wellUseNum}
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${record.wellUseNumInd}">x</c:when>
                            <c:otherwise>n/a</c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="wtComm">${record.wtComm}&nbsp;</td>
            <td class="attachment">
                <c:forEach var="attachment" items="${record.wtAttachmentCollection}">
                    ${attachment.filename} <br>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table><p></p>

<div class="hightchart-container"></div>

<div class="highchart">
<table class="highchart" data-graph-container=".hightchart-container" data-graph-type="column" style="display:none" data-graph-xaxis-title-text="Transfer ID">
    <thead>
        <tr class="header">
            <th>Transfer ID</th>     
            <th>Proposed Transfer Quantity</th>
            <th>Actual Transfer Quantity</th> 
        </tr>  
    </thead>
    
    <tbody>
    <c:forEach var="record" items="${transRecords}">
        <tr class="body">
            <td>${record.wtTransId}</td>
            <td>${record.proTransQua}</td>
            <td>${record.actTransQua}</td>
        </tr>
    </c:forEach>   
    </tbody>    
</table>
</div>
