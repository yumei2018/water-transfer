<%--
    Document   : GroundwaterInfo
    Created on : Apr 1, 2015, 10:10:41 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="status" value="${proposal.wtStatusFlag.statusName}"/>

<div id="groundwater_tab">
  <c:set var="wtGroundwaterId" value="${proposal.wtGroundwater.wtGroundwaterId}"/>
  <div style="overflow: hidden;" class="${hideField}">
    <a style="float:right;display:inline-block;font-size:10px;bottom:-5px;margin-right:10px;text-decoration:underline;" class="proposalReports" wtTransId="${proposal.wtTransId}" reportType="GWR">View Saved PDFs</a>
    <a style="float:right;display:inline-block;width:120px;" target="_blank" class="lnkbtn" href="${pageContext.request.contextPath}/report/previewGW/${proposal.wtGroundwater.wtGroundwaterId}?attachType=GW">Create GW Tab PDF</a>
  </div>
  <div id="gw-container">
    <table id="create-gw-table">
      <tr class="tableRow" style="display:none;">
        <th>Groundwater ID</th>
        <td><input class="" type="text" name="wtGroundwaterId" id="wtGroundwaterId" value="${proposal.wtGroundwater.wtGroundwaterId}"/></td>
      </tr>
      <tr class="tableRow">
        <th>Number of Proposed Transfer Wells</th>
        <td><input class="validField intField" 
                   type="text" 
                   id="pumpingWellsNumber" 
                   name="pumpingWellsNumber" 
                   groundwaterId='${wtGroundwaterId}'
                   value="${proposal.wtGroundwater.pumpingWellsNumber}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.groundwaterInfo.saveGwField(this);"/></td>
        <th>Number of Proposed Monitoring Wells</th>
        <td><input class="validField intField" 
                   type="text" 
                   id="monitoringWellsNumber" 
                   name="monitoringWellsNumber"
                   groundwaterId='${wtGroundwaterId}'
                   value="${proposal.wtGroundwater.monitoringWellsNumber}" 
                   maxlength="9" ${disableEdit}
                   onblur="window.groundwaterInfo.saveGwField(this);"/></td>
      </tr>
      <tr class="tableRow">
        <th>Total Proposed Pumping (acre-feet)</th>
        <td>
          <input class="validField numField" 
                 type="text" id="totalPumping"
                 name="totalPumping" 
                 groundwaterId='${wtGroundwaterId}'
                 value="${proposal.wtGroundwater.totalPumping}" 
                 maxlength="9" ${disableEdit}
                 onblur="window.groundwaterInfo.saveGwField(this);"/>
<!--          <img src="${pageContext.request.contextPath}/resources/images/icons/calculator.png" class="" id="calBtn" alt="">-->
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="totalPumping_help" alt="Help" title="Total Proposed Pumping Help">
        </td>
        <th>Baseline Pumping (acre-feet)</th>
        <td>
          <input class="validField numField" 
                 type="text" 
                 id="basePumping" 
                 name="basePumping"
                 groundwaterId='${wtGroundwaterId}'
                 value="${proposal.wtGroundwater.basePumping}" 
                 maxlength="9" ${disableEdit}
                 onblur="window.groundwaterInfo.saveGwField(this);"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="basePumping_help" alt="Help" title="Baseline Pumping Help">
        </td>
      </tr>
      <tr class="tableRow">
        <th>Gross Transfer Pumping (acre-feet)</th>
        <td>
          <input class="input-disabled numField" type="text" id="grossTransPumping" name="grossTransPumping" value="${proposal.wtGroundwater.grossTransPumping}" readonly="true"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="grossTransPumping_help" alt="Help" title="Gross Transfer Pumping Help">
        </td>
        <th>Streamflow Depletion Factor<span class="asterisk">*</span> %</th>
        <td>
          <input class="validField numField" 
                 type="text"
                 id="depletionFactor"
                 name="depletionFactor"
                 groundwaterId='${wtGroundwaterId}'
                 value="${proposal.wtGroundwater.depletionFactor}"
                 maxlength="5" ${disableEdit}
                 onblur="window.groundwaterInfo.saveGwField(this);"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="depletionFactor_help" alt="Help" title="Streamflow Depletion Factor Help">
        </td>
      </tr>
      <tr class="tableRow">
        <th>Streamflow Depletion (acre-feet)</th>
        <td>
          <input class="input-disabled numField" type="text" id="streamDepletion" name="streamDepletion" value="${proposal.wtGroundwater.streamDepletion}" readonly="true" />
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="depletions_help" alt="Help" title="Streamflow Depletion Help">
        </td>
        <th>Net Transfer Water (acre-feet)</th>
        <td>
          <input class="input-disabled numField" type="text" id="netTransWater" name="netTransWater" value="${proposal.wtGroundwater.netTransWater}" readonly="true"/>
          <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="netTransWater_help" alt="Help" title="Net Transfer Water Help">
        </td>
      </tr>
    </table>

    <div id="gw_monthly_tab" class="tab_ct">
      <div class="tab_header isExpand">Estimated Monthly Total Groundwater Pumping (acre-feet)</div>
      <div class="contact_panel">
        <table id="gw-monthly-table">
          <thead>
            <tr class="monthly_header">
              <th style="width:100px;"></th>
              <th>Jan</th>
              <th>Feb</th>
              <th>Mar</th>
              <th>Apr</th>
              <th>May</th>
              <th>June</th>
              <th>July</th>
              <th>Aug</th>
              <th>Sep</th>
              <th>Oct</th>
              <th>Nov</th>
              <th>Dec</th>
              <th>
                Total
                <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="monthlyTotal_help" alt="Help" title="Monthly Total">
              </th>
            </tr>
          </thead>
          <tbody>
            <!--                <tr>
                                <td>Proposed Measurement Date (1-31)</td>
            <c:forEach var="i" begin="1" end="12">
                <td>
              <c:set var="measurementDate" value=""/>
              <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                <c:if test="${gwMonthly.gwMonth eq i}">
                  <c:set var="measurementDate" value="${gwMonthly.measurementDate}"/>
                </c:if>
              </c:forEach>
              <input type="text" class="md intField" index="${i}" id="measurementDate${i}" name="measurementDate${i}" value="${measurementDate}" placeholder="" maxlength="4"/>
              </td>
            </c:forEach>
                <td></td>
        </tr>-->
            <tr>
              <td>Proposed Pumping</td>
              <c:forEach var="i" begin="1" end="12">
                <td>
                  <c:set var="proposedPumping" value=""/>
                  <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                    <c:if test="${gwMonthly.gwMonth eq i}">
                      <c:set var="proposedPumping" value="${gwMonthly.proposedPumping}"/>
                    </c:if>
                  </c:forEach>
                  <input type="text" 
                         class="pp numField" 
                         index="${i}" 
                         id="proposedPumping${i}" 
                         groundwaterId='${wtGroundwaterId}'
                         name="proposedPumping${i}"
                         fieldname="proposedPumping"                         
                         value="${proposedPumping}" 
                         maxlength="9" ${disableEdit}
                         onblur="window.groundwaterInfo.saveGwMonthlyField(this);"/>
                </td>
              </c:forEach>
              <td><input class="total numField" id="proposedPumpingTotal" type="text" name="total" value="" disabled="true"/></td>
            </tr>
            <tr>
              <td>Baseline Pumping</td>
              <c:forEach var="i" begin="1" end="12">
                <td>
                  <c:set var="baselinePumping" value=""/>
                  <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                    <c:if test="${gwMonthly.gwMonth eq i}">
                      <c:set var="baselinePumping" value="${gwMonthly.baselinePumping}"/>
                    </c:if>
                  </c:forEach>
                  <input type="text" 
                         class="bp numField"
                         index="${i}" 
                         id="baselinePumping${i}" 
                         groundwaterId='${wtGroundwaterId}'
                         name="baselinePumping${i}"
                         fieldname="baselinePumping"
                         value="${baselinePumping}" 
                         maxlength="9" ${disableEdit}
                         onblur="window.groundwaterInfo.saveGwMonthlyField(this);"/>
                </td>
              </c:forEach>
              <td><input class="total numField" id="baselinePumpingTotal" type="text" name="total" value="" disabled="true"/></td>
            </tr>
            <tr>
              <td>Gross Transfer Pumping</td>
              <c:forEach var="i" begin="1" end="12">
                <td>
                  <c:set var="grossTransPumping" value=""/>
                  <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                    <c:if test="${gwMonthly.gwMonth eq i}">
                      <c:set var="grossTransPumping" value="${gwMonthly.grossTransPumping}"/>
                    </c:if>
                  </c:forEach>
                  <input type="text" 
                         class="gp numField" 
                         index="${i}"
                         id="grossTransPumping${i}"
                         groundwaterId='${wtGroundwaterId}'
                         name="grossTransPumping${i}"
                         fieldname="grossTransPumping"
                         value="${grossTransPumping}" ${disableEdit}
                         onblur="window.groundwaterInfo.saveGwMonthlyField(this);"/>
                </td>
              </c:forEach>
              <td><input class="total numField" id="grossTransPumpingTotal" type="text" name="total" value="" disabled="true"/></td>
            </tr>
            <tr>
              <td>Depletion</td>
              <c:forEach var="i" begin="1" end="12">
                <td>
                  <c:set var="streamDepletion" value=""/>
                  <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                    <c:if test="${gwMonthly.gwMonth eq i}">
                      <c:set var="streamDepletion" value="${gwMonthly.streamDepletion}"/>
                    </c:if>
                  </c:forEach>
                  <input type="text" 
                         class="dp numField"
                         index="${i}"
                         id="streamDepletion${i}"
                         groundwaterId='${wtGroundwaterId}'
                         name="streamDepletion${i}"
                         fieldname="streamDepletion"
                         value="${streamDepletion}" ${disableEdit}
                         onblur="window.groundwaterInfo.saveGwMonthlyField(this);"/>
                </td>
              </c:forEach>
              <td><input class="total numField" id="streamDepletionTotal" type="text" name="total" value="" disabled="true"/></td>
            </tr>
            <tr>
              <td>
                Net Transfer Water
                <img src="${pageContext.request.contextPath}/resources/images/icons/help.png" class="help-icon" id="netTransWater_help" alt="Help" title="Net Transfer Water">
              </td>
              <c:forEach var="i" begin="1" end="12">
                <td>
                  <c:set var="netTransWater" value=""/>
                  <c:forEach var="gwMonthly" items="${proposal.wtGroundwater.wtGwMonthlyCollection}" varStatus="iterator">
                    <c:if test="${gwMonthly.gwMonth eq i}">
                      <c:set var="netTransWater" value="${gwMonthly.netTransWater}"/>
                    </c:if>
                  </c:forEach>
                  <input type="text" 
                         class="nw numField input-disabled" 
                         index="${i}"
                         id="netTransWater${i}"
                         groundwaterId='${wtGroundwaterId}'
                         name="netTransWater${i}" 
                         value="${netTransWater}"
                         readonly="true" ${disableEdit}/>
                </td>
              </c:forEach>
              <td><input class="total numField" id="netTransWaterTotal" type="text" name="total" value="" disabled="true"/></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div id="listwells">
      <div class="tab_header isExpand">List of Associated Wells  
        <span class="commentNote ${reviewNote}">Please click Edit to view comments.</span>
      </div>
      <div class="contact_panel" id="listAssociateWellCt"></div>
<!--    <iframe id="associated_well_iframe" class="hidden" srcpath="http://<%=request.getServerName() + ":" + request.getServerPort()%>/wellsearch/" frameBorder="0"></iframe>-->
      <!-- DWR Server Use -->
<!--      <iframe id="associated_well_iframe" class="hidden" srcpath="https://<%=request.getServerName() + ":443"%>/wellsearch/?hidenearbywells=true" frameBorder="0"></iframe>-->
      <iframe id="associated_well_iframe" class="hidden" srcpath="<%=request.getScheme()+"://"+request.getServerName() + ":" + request.getServerPort()%>/wellsearch/?hidenearbywells=true" frameBorder="0"></iframe>
    </div>

  </div>

  <div class="tab_header isExpand">Attachment</div>
  <div class="contact_panel">
    <div id="typeMenu">
      <div class="head_explain">
        To add your attachments, select the Add Attachment button below. In the pop-up window, select which properties belong to your document and upload.
      </div>
    </div>
    <div class="attachment-container" id="attachment-groundwater-info" typeid="GW"></div>
    <div class="attach-button ${hideField}">
      <input class="attachButton add_agency" type="button" id="attachGroundWater" typeid="GW" containerid="attachment-groundwater-info" attachid="${proposal.wtGroundwater.wtGroundwaterId}" value="Add Attachment" onclick="return false;"/>
    </div>
  </div>
    
  <div class="forReviewer ${reviewNote}">
    <c:set var="isComplete" value=""/>
    <c:set var="checkVal" value="0"/>
    <c:set var="addComment" value="disabled"/>  
    <c:set var="showAdd" value="hidden"/> 
    <c:set var="bgcolor" value=""/>
    <c:if test="${reviewNotes.getLastReviewNoteBySectionKey('G').isComplete==1}">
      <c:set var="isComplete" value="checked"/>
      <c:set var="checkVal" value="1"/>      
    </c:if>
    <c:if test="${(proposal.wtStatusFlag.statusName eq 'INCOMPLETE') 
                    && (reviewNotes.getLastReviewNoteBySectionKey('G').isComplete!=1)}">
      <c:set var="addComment" value=""/>      
      <c:set var="showAdd" value=""/>
      <c:set var="bgcolor" value="background-color: #fff;"/>
    </c:if>
    <div class="checkBoxCt">
      <input type="checkbox"
             sectionKey="G"
             transId="${proposal.wtTransId}"
             onclick="return false;"
             ${isComplete}
             value="${checkVal}"/>
      <label>Above section is completed if checked</label>
    </div>
    <fieldset class="">
      <c:set var="techNote" value=""/>
      <c:if test="${!empty(reviewNotes)}">
        <c:set var="techNote" value="${reviewNotes.getLastReviewNoteBySectionKey('G').note}"/>
      </c:if>
      <textarea class="techNote" 
                placeholder="Technical Comments"   
                style="${bgcolor}"
                sectionKey="G"
                transId="${proposal.wtTransId}"
                ${addComment}
                onkeyup="window.reviewer.auto_grow(this)"></textarea>
      <input class="noteBtn" type="button" value="Show History" onclick="window.reviewer.initSlideHistory(this);"/>
      <input class="noteBtn ${showAdd}"
             type="button"
             sectionKey="G"
             transId="${proposal.wtTransId}"
             value="Save Comment"
             onclick="window.reviewer.initTechNoteAdd(this);"/><br/><br/>
      <div class="commentNote">Please note that once a comment is saved, it cannot be modified.</div>
<!--      <div class="historyCt hidden">
        <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('G')}" var="tNote" >
          <div style="color:#003cb3;padding-top:10px;font-size:12px;"><span>${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
          <div style="padding-left:40px;font-size:15px;">${tNote.note}</div>
        </c:forEach>
      </div>-->
      <c:forEach items="${reviewNotes.getReviewNotesBySectionKey('G')}" var="tNote" varStatus="iterator">
        <c:if test="${iterator.first}">
          <div class="lastNoteCt">
            <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
            <div style="padding-left: 40px;">${tNote.note}</div>
          </div>
        </c:if>
        <c:if test="${!iterator.first}">
          <div class="historyCt hidden">
            <div style="color:#003cb3;padding-top:10px;"><span style="font-size: 15px;">${tNote.updatedBy.name}</span><span style="padding-left: 10px;">${tNote.noteDate}</span></div>
            <div style="padding-left: 40px;">${tNote.note}</div>
          </div>
        </c:if>
      </c:forEach>
    </fieldset>
  </div>

  <div class="help-ct"></div>
</div>
      
<script type="text/javascript">
$(document).ready(function(){
  window.groundwaterInfo = new GroundwaterInfo();
  window.baseInfo = new BaseInfo();
});
</script>
<style type="text/css">
  .tab_header {
    background-color: #e3e3e3;
    border-radius: 2px 2px 0 0;
    margin-top: 20px;
    padding: 10px;
    cursor: default;
    font-size: 16px
  }
  .attach-button {
    margin-left: 10px;
  }
  .tab_header:hover {
    background-color:#86c0d1;
  }
  .isExpand{
    background-color: #9cc7d4;
  }
  .contact_panel {
    border-radius: 0 0 3px 3px;
    padding: 0px 0px 10px 0px!important;
    margin-bottom: 10px!important;
  }
  #gw-monthly-table{
    width:100%;
    text-align: center;
    font-size: 15px;
    margin-bottom: 10px;
  }
  .linkline:hover{
    cursor:pointer;
    color:#0B94C3;
  }
  #gw-monthly-table th{
    border: 1px solid #cccccc;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
  }
  #gw-monthly-table td{
    text-align: left;
    border:1px solid #cccccc;
    padding: 3px;
  }
  #gw-monthly-table td input{
    width:50px;
  }
  .monthly_header {
    background-color: #8cb3d9;
    font-size: 15px;
    width: 300px;
  }
  .input-disabled  {
    background-color:#EBEBE4;
    border:1px solid #ABADB3;
    padding:2px 1px;
  }
  .ui-dialog {
    /*font-size: 0.8em;*/
  }
  .ui-button-text {
    /*font-size: 10px;*/
  }
  #listAssociateWellCt{
    background-color:#F2F8FE;
  }
  .lnkbtn {
    background-color: #797979;
    border: none;
    color: #fff!important;
    border-radius: 2px;
    width: 100px;
  }
  img.help-icon{
    cursor:pointer;
  }
</style>

