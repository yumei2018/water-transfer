<%--
    Document   : TransferReport
    Created on : Apr 1, 2015, 10:14:35 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="__Javascripts">
  ${pageContext.request.contextPath}/resources/js/proposal/TransferReport.js
</c:set>

<c:set var="disableEdit" value="disabled"/>
<c:set var='hideReportItem' value="hidden"/>
<c:set var="hideField" value="hidden"/>
<c:set var="ciMonthly" value="hidden"/>
<c:set var="rvMonthly" value="hidden"/>
<c:set var="gwMonthly" value="hidden"/>
<c:if test="${sessionScope.USER.isAppAccount()}">
  <c:set var='hideReportItem' value=""/>
  <c:set var="hideField" value=""/>
</c:if>
<c:if test="${sessionScope.USER.isReviewer()}">
  <c:set var="disableEdit" value=""/> 
</c:if>
<c:if test="${sessionScope.USER.isManager()}">
  <c:set var="disableEdit" value=""/>
  <c:set var='hideReportItem' value=""/>
</c:if>
<c:if test="${!empty(proposal.wtCropIdling)&&!empty(proposal.wtCropIdling.wtCropIdlingId)}">
  <c:set var="ciMonthly" value=""/>
</c:if>
<c:if test="${!empty(proposal.wtReservoir)&&!empty(proposal.wtReservoir.wtReservoirId)}">
  <c:set var="rvMonthly" value=""/>
</c:if>
<c:if test="${!empty(proposal.wtGroundwater)&&!empty(proposal.wtGroundwater.wtGroundwaterId)}">
  <c:set var="gwMonthly" value=""/>
</c:if>
<div id="transreport_tab">
  <div style="font-size: 18px;">
    The page is for seller fill and upload execute the water transfer reports.
  </div>  
  
  <div class="tab_header isExpand ${ciMonthly}">Crop Idling Transfer Water Production Schedule (acre-feet)</div>
  <div class="contact_panel ${ciMonthly}">
    <c:set var="cropIdling" value="${proposal.wtCropIdling}"/>
    <c:set var="wtCropIdlingId" value="${proposal.wtCropIdling.wtCropIdlingId}"/>
    <table id="ci-monthly-prod-table">
      <thead>
        <tr class="monthly_header">
          <th style="width:20%;"></th>
          <th style="width:13%;">May</th>
          <th style="width:13%;">June</th>
          <th style="width:13%;">July</th>
          <th style="width:13%;">August</th>
          <th style="width:13%;">September</th>
          <th style="width:13%;">Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Transfer Water (acre-feet)</td>
          <td><input class="numField"
                     id="mayTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="mayTw"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.mayTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
          <td><input class="numField"
                     id="juneTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="juneTw"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.juneTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
          <td><input class="numField"
                     id="julyTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="julyTw"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.julyTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
          <td><input class="numField"
                     id="augustTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="augustTw"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.augustTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
          <td><input class="numField"
                     id="septemberTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="septemberTw"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.septemberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
          <td><input class="numField"
                     id="totalTwCi"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="totalTwCi"
                     cropIdlingId='${wtCropIdlingId}'
                     value='${cropIdling.actualAmount}'
                     ${disableEdit}
                     onchange="window.transferReport.saveCiField(this);"/></td>
<!--          <td><span id="totalTwCi"></span></td>-->
        </tr>
      </tbody>
    </table>
  </div>

  <div class="tab_header isExpand ${gwMonthly}">Groundwater Transfer Water Production Schedule (acre-feet)</div>
  <div class="contact_panel ${gwMonthly}">
    <c:set var="groundwater" value="${proposal.wtGroundwater}"/>
    <c:set var="wtGroundwaterId" value="${proposal.wtGroundwater.wtGroundwaterId}"/>
    <table id="gw-monthly-prod-table">
      <thead>
        <tr class="monthly_header">
          <th style="width:18%;"></th>
          <th style="width:9%;">April</th>
          <th style="width:9%;">May</th>
          <th style="width:9%;">June</th>
          <th style="width:9%;">July</th>
          <th style="width:9%;">August</th>
          <th style="width:9%;">September</th>
          <th style="width:9%;">October</th>
          <th style="width:9%;">November</th>
          <th style="width:11%;">Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Transfer Water (acre-feet)</td>
          <td><input class="numField"
                     id="aprilTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="aprilTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.aprilTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="mayTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="mayTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.mayTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="juneTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="juneTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.juneTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="julyTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="julyTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.julyTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="augustTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="augustTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.augustTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="septemberTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="septemberTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.septemberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="octoberTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="octoberTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.octoberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="novemberTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="novemberTw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.novemberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
          <td><input class="numField"
                     id="totalTwGw"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="totalTwGw"
                     groundwaterId='${wtGroundwaterId}'
                     value='${groundwater.actualAmount}'
                     ${disableEdit}
                     onchange="window.transferReport.saveGwField(this);"/></td>
<!--          <td><span id="totalTwGw"></span></td>-->
        </tr>
      </tbody>
    </table>
  </div>
  
  <div class="tab_header isExpand ${rvMonthly}">Reservoir Transfer Water Production Schedule (acre-feet)</div>
  <div class="contact_panel ${rvMonthly}">
    <c:set var="reservoir" value="${proposal.wtReservoir}"/>
    <c:set var="wtReservoirId" value="${proposal.wtReservoir.wtReservoirId}"/>
    <table id="rv-monthly-prod-table">
      <thead>
        <tr class="monthly_header">
          <th style="width:18%;"></th>
          <th style="width:9%;">April</th>
          <th style="width:9%;">May</th>
          <th style="width:9%;">June</th>
          <th style="width:9%;">July</th>
          <th style="width:9%;">August</th>
          <th style="width:9%;">September</th>
          <th style="width:9%;">October</th>
          <th style="width:9%;">November</th>
          <th style="width:11%;">Total</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Transfer Water (acre-feet)</td>
          <td><input class="numField"
                     id="aprilTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="aprilTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.aprilTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="mayTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="mayTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.mayTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="juneTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="juneTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.juneTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="julyTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="julyTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.julyTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="augustTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="augustTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.augustTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="septemberTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="septemberTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.septemberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="octoberTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="octoberTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.octoberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="novemberTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="novemberTw"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.novemberTw}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
          <td><input class="numField"
                     id="totalTwRv"
                     maxlength="12"
                     style="width:60px;"
                     type="text"
                     name="totalTwRv"
                     reservoirId='${wtReservoirId}'
                     value='${reservoir.actualAmount}'
                     ${disableEdit}
                     onchange="window.transferReport.saveRvField(this);"/></td>
<!--          <td><span id="totalTwRv"></span></td>-->
        </tr>
      </tbody>
    </table>
  </div>                     
                     
  <div class="tab_header isExpand">Attachments</div>
  <div class="contact_panel">
    <div class="attachment-container" id="attachment-transreport-info" typeid="TR"></div>
    <div class="attach-button">
      <input class="attachButton add_agency" 
             type="button" 
             id="attachTransReport" 
             typeid="TR" 
             containerid="attachment-transreport-info" 
             attachid="" 
             value="Add Attachment" 
             onclick="return false;"/>
    </div>
<!--    <div class="comment-label">Comments:</div>
    <textarea cols="150" rows="3"
              class="comment-content" 
              id="reportComment" 
              name="reportComment" 
              transId='${proposal.wtTransId}'
              maxlength="500"
              ${disableEdit}
              onchange="window.transferReport.saveReportComment(this);">${proposal.reportComment}</textarea>
    <div class="button_ct hidden">
      <button class="proposal_btn" id="saveComment" style="width:120px;">Save</button>
    </div>-->
  </div>
</div>
    
<script type="text/javascript">
  $(document).ready(function(){
    window.transferReport = new TransferReport();
  });
</script>
<style type="text/css">
  #ci-monthly-prod-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #ci-monthly-prod-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #ci-monthly-prod-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }
  #rv-monthly-prod-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #rv-monthly-prod-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #rv-monthly-prod-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }
  #gw-monthly-prod-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  #gw-monthly-prod-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #gw-monthly-prod-table td{
    text-align: center;
    border:1px solid #cccccc;
    padding: 3px;
  }
  .monthly_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .comment-label{
    margin-top: 40px;
    margin-bottom: 20px;
  }
  .comment-content{
    margin-left: 100px;
    border: 1px solid #a6a8a8;
  }
</style>