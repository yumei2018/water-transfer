<%-- 
    Document   : StatusReport
    Created on : Jul 17, 2017, 11:19:22 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <title>Department of Water Resources</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery.loadmask.css" />
<!--    <script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js"></script>-->
    <script type="text/javascript">window.SERVER_ROOT="${pageContext.request.contextPath}";</script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery/jquery.loadmask.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
  </head>
  <body>
    <c:set var="today" value="<%=new Date()%>"/>
    <div id="status-report-ct">
      <div class="report-title">
        <p class="left-title">
          <fmt:formatDate type="date" value="${today}" pattern="yyyy"/>
           Transfer Requiring Use of DWR Facilities - Temporary Transfer Proposals Status Overview
        </p>
        <p class="right-title">
          <fmt:formatDate type="date" value="${today}" dateStyle = "long"/></p>
      </div>
      <table id="status-report-table">
        <thead>
          <tr>
            <th rowspan="2">Seller</th>
            <th rowspan="2" style="width:12%;">Buyer</th>
            <th rowspan="2">Initial Proposal Submittal Date</th>
            <th colspan="3">Type of Transfer</th>
            <th colspan="4">Proposal Amount (acre-feet)</th>
<!--            <th rowspan="2" style="width:16%;">Agency Approval Required</th>-->
            <th colspan="4">Contract Amount (acre-feet)</th>
            <th rowspan="2">Status</th>
          </tr>
          <tr>
            <th style="width: 5%;">Cropland Idling</th>
            <th style="width: 5%;">Groundwater Substitution</th>
            <th style="width: 5%;">Reservoir Release</th>
            <th style="width: 5%;">Cropland Idling</th>
            <th style="width: 5%;">Groundwater Substitution</th>
            <th style="width: 5%;">Reservoir Release</th>
            <th>Total Transfer</th>
            <th>Cropland Idling</th>
            <th>Groundwater Substitution</th>
            <th>Reservoir Release</th>
            <th>Total Transfer</th>
          </tr>
        </thead>
        <tbody>
          <c:if test="${data.length()>0}">
          <c:forEach begin="0" end="${data.length()-1}" var="index">
            <c:set var="record" value="${data.getJSONObject(index)}"/>
            <c:set var="ciWaterAmount" value="${record.getString('ciWaterAmount')}"/>
            <c:set var="ciWaterAmountTotal" value="${ciWaterAmountTotal+ciWaterAmount}"/>
            <c:set var="gwWaterAmount" value="${record.getString('gwWaterAmount')}"/>
            <c:set var="gwWaterAmountTotal" value="${gwWaterAmountTotal+gwWaterAmount}"/>
            <c:set var="rvWaterAmount" value="${record.getString('rvWaterAmount')}"/>
            <c:set var="rvWaterAmountTotal" value="${rvWaterAmountTotal+rvWaterAmount}"/>
            <c:set var="waterAmount" value="${record.getString('totalWaterAmount')}"/>
            <c:set var="waterAmountTotal" value="${waterAmountTotal+waterAmount}"/>
            
            <c:set var="ciContractAmount" value="${record.getString('ciContractAmount')}"/>
            <c:set var="ciContractAmountTotal" value="${ciContractAmountTotal+ciContractAmount}"/>
            <c:set var="gwContractAmount" value="${record.getString('gwContractAmount')}"/>
            <c:set var="gwContractAmountTotal" value="${gwContractAmountTotal+gwContractAmount}"/>
            <c:set var="rvContractAmount" value="${record.getString('rvContractAmount')}"/>
            <c:set var="rvContractAmountTotal" value="${rvContractAmountTotal+rvContractAmount}"/>
            <c:set var="contractAmount" value="${record.getString('contractAmount')}"/>
            <c:set var="contractAmountTotal" value="${contractAmountTotal+contractAmount}"/>
            <tr>
              <td fieldName="Seller">${record.getString("seller")}</td>
              <td fieldName="Buyer">${record.getString("buyers")}</td>
              <td fieldName="Proposal Received">${record.getString("receivedDate")}</td>
              <td fieldName="Cropland Idling">${record.getString("cropidling")}</td>
              <td fieldName="Groundwater Substitution">${record.getString("groundwater")}</td>
              <td fieldName="Reservoir Release">${record.getString("reservoir")}</td>
              <td fieldName="CI Proposed Amount" class="ciWaterAmount numField"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${ciWaterAmount}" /></td>
              <td fieldName="GW Proposed Amount" class="gwWaterAmount numField">${record.getString("gwWaterAmount")}</td>
              <td fieldName="RV Proposed Amount" class="rvWaterAmount numField">${record.getString("rvWaterAmount")}</td>
              <td fieldName="Total Transfer" class="waterAmount numField">${record.getString("totalWaterAmount")}</td>
<!--              <td fieldName="Agency Approval Required">${record.getString("agencyApproval")}</td>-->
              <td fieldName="CI Contract Amount" class="ciContractAmount numField">${record.getString("ciContractAmount")}</td>
              <td fieldName="GW Contract Amount" class="gwContractAmount numField">${record.getString("gwContractAmount")}</td>
              <td fieldName="RV Contract Amount" class="rvContractAmount numField">${record.getString("rvContractAmount")}</td>
              <td fieldName="Total Contract Amount" class="contractAmount numField">${record.getString("contractAmount")}</td>
              <td fieldName="Status">${record.getString("status")}</td>
            </tr>
          </c:forEach>
          </c:if>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td fieldName="Reservoir Release" colspan="3" class="totalLabel">Total Proposal Temporary Transfer</td>
            <td fieldName="CI Proposed Amount" class="ciWaterAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${ciWaterAmountTotal}" /></td>
            <td fieldName="GW Proposed Amount" class="gwWaterAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${gwWaterAmountTotal}" /></td>
            <td fieldName="RV Proposed Amount" class="rvWaterAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${rvWaterAmountTotal}" /></td>
            <td fieldName="Total Transfer" class="waterAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${waterAmountTotal}" /></td>
<!--            <td fieldName="Agency Approval Required" class="totalLabel">Total Contract Temporary Transfer</td>-->
            <td fieldName="CI Contract Amount" class="ciContractAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${ciContractAmountTotal}" /></td>
            <td fieldName="GW Contract Amount" class="gwContractAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${gwContractAmountTotal}" /></td>
            <td fieldName="RV Contract Amount" class="rvContractAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${rvContractAmountTotal}" /></td>
            <td fieldName="Total Contract Amount" class="contractAmountTotal totalLabel"><fmt:formatNumber type = "number" minFractionDigits = "0" maxFractionDigits = "0" value = "${contractAmountTotal}" /></td>
            <td></td>
          </tr>
        </tbody>
      </table>

      <div style="width: 100%;">
<!--        <a class="linkLine" id="printPDF">Open PDF</a>-->
        <a class="linkLine" id="printCSV">Export CSV</a>
        <a class="linkLine" id="agencyLink">Agency List</a>
      </div>
      <div class="noteHeader">Note:</div>
      <div class="note">Export window is between July 1 and September 30.</div>
      <div class="note">All information is subject to modification and verification.</div>
      <div class="note">All information is non-binding and is not intended to create contractual relationship with Project Agencies</div>
    </div>

    <div id="agency_list_ct" class="hidden">
      <table id="agency-list-table">
        <thead>
        <th>Agency Code</th>
        <th>Agency Full Name</th>
        </thead>      
        <tbody>
          <c:forEach var="agency" items="${agencyList}">
            <tr>
              <td>${agency.agencyCode}</td>
              <td>${agency.agencyFullName}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <form action="${pageContext.request.contextPath}/report/statusReportPDF" target="_blank" method="post" id="statusReportForm"></form>


  </body>
</html>
<style type="text/css">
  * {font-family: Arial,"Trebuchet MS",Helvetica,sans-serif !important;}
  @page { size:landscape;}
/*  @page port { size:portrait; }*/
  div#status-report-ct{
    width: 1280px;
  }
  .hidden{
    display: none;
  }
  .report-title{
    width: 100%;
    padding-top: 10px;
    padding-bottom: 10px;
  } 
  .left-title{  
    font-size: 15px;
    float: left;  
  }
  .right-title{  
    font-size: 10px;
    float: right;
  }
  #status-report-table{
    border-collapse: collapse;
    font-size: 12px; 
    width: 100%;
  }
  #status-report-table th{
    border: 1px solid #999999;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #status-report-table tr td{
    border:1px solid #999999;
    padding: 3px;
    background: #F9F9F9;
    text-align: right;
  }
  .noteHeader{
    padding-top: 30px;
    font-size: 11px;
  }
  .note{
    font-size: 10px; 
  }
  .totalLabel{
    font-weight: bold;
    text-align: right;
  }
  #agency-list-table{
    border-collapse: collapse;
    font-size: 10px; 
  }
  #agency-list-table th{
    border: 1px solid #999999;
    padding:10px 0px;
    background: none repeat scroll 0 0 #e3e3e3;
    text-align: center;
  }
  #agency-list-table tr td{
    border:1px solid #999999;
    padding: 3px;
    background: #F9F9F9;
  }
  .linkLine {
    float:right;
    cursor: pointer;
    color: #0c83ab;
    text-decoration: underline;
    font-size: 12px;
    padding-top:15px;
    padding-right: 10px;
  }
  @media print{
    .report-title{
      padding-top: 10px;
      padding-bottom: 40px;
    }
    div#status-report-ct{
/*      page: land;*/
      width: 950px;
    }
    #status-report-table{
/*      border-collapse: separate;*/
      font-size: 9px; 
      width: 100%;
    }
    .linkLine{
      display: none;
    }
  }
</style>
<script type="text/javascript">
  var statusReport = function ()
  {
    var self = $(this);
    self.init = function ()
    {
      self.initItems();
      self.initListeners();
    };

    self.initItems = function ()
    {
      self.statusReportCt = $("#status-report-ct");
      self.reportTitle = $(".report-title");
      self.leftTitle = $(".left-title");
      self.rightTitle = $(".right-title");
      self.initTitles();
      self.initNumFields();

      self.statusReportTable = $("#status-report-table");
      self.ciWaterAmount = self.statusReportTable.find(".ciWaterAmount");
      self.gwWaterAmount = self.statusReportTable.find(".gwWaterAmount");
      self.rvWaterAmount = self.statusReportTable.find(".rvWaterAmount");
      self.waterAmount = self.statusReportTable.find(".waterAmount");
      self.ciWaterAmountTotal = self.statusReportTable.find(".ciWaterAmountTotal");
      self.gwWaterAmountTotal = self.statusReportTable.find(".gwWaterAmountTotal");
      self.rvWaterAmountTotal = self.statusReportTable.find(".rvWaterAmountTotal");
      self.waterAmountTotal = self.statusReportTable.find(".waterAmountTotal");

      self.ciContractAmount = self.statusReportTable.find(".ciContractAmount");
      self.gwContractAmount = self.statusReportTable.find(".gwContractAmount");
      self.rvContractAmount = self.statusReportTable.find(".rvContractAmount");
      self.contractAmount = self.statusReportTable.find(".contractAmount");
      self.ciContractAmountTotal = self.statusReportTable.find(".ciContractAmountTotal");
      self.gwContractAmountTotal = self.statusReportTable.find(".gwContractAmountTotal");
      self.rvContractAmountTotal = self.statusReportTable.find(".rvContractAmountTotal");
      self.contractAmountTotal = self.statusReportTable.find(".contractAmountTotal");
//      self.calTotal();

      self.agencyLink = $("#agencyLink");
      self.agencyListCt = $("#agency_list_ct");
      self.printPDF = $("#printPDF");
      self.statusReportForm = $("#statusReportForm");
      self.printCSV = $("#printCSV");
    };

    self.initListeners = function () {
      self.agencyLink.unbind('click').bind('click', self.initAgencyList);
      self.printPDF.unbind('click').bind('click', self.initPrintPDF);
      self.printCSV.unbind('click').bind('click', self.initPrintCSV);
    };

    self.initTitles = function ()
    {
      var today = new Date();
      var thisYear = today.getFullYear();
      var formattedDate = $.datepicker.formatDate('MM dd, yy', new Date());
//    alert(formattedDate);

//      self.leftTitle.html(thisYear + " Transfer Requiring Use of DWR Facilities - Temporary Transfer Proposals Status Overview");
//      self.rightTitle.html("Display the date this table is generate " + formattedDate);
    };

    self.initNumFields = function ()
    {
      self.numFields = $(".numField");
//    alert(self.numFields);
      self.numFields.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseInt(thisValue.replace(/[, ]+/g, ""));
//          var num = Math.round(parseFloat(thisValue.replace(/[, ]+/g, "")));
//          var adjustedValue = num.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 2});
          var adjustedValue = num.toLocaleString('en-US');
          $(this).html(adjustedValue);
        }
      });
    };

    self.calTotal = function ()
    {
      var ciWaterAmountTotal = 0;
      var gwWaterAmountTotal = 0;
      var rvWaterAmountTotal = 0;
      var waterAmountTotal = 0;
      var ciContractAmountTotal = 0;
      var gwContractAmountTotal = 0;
      var rvContractAmountTotal = 0;
      var contractAmountTotal = 0;

      self.ciWaterAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          ciWaterAmountTotal += num;
        }
        ciWaterAmountTotal = Math.round(ciWaterAmountTotal);
      });
      self.ciWaterAmountTotal.html(ciWaterAmountTotal.toLocaleString('en-US'));

      self.gwWaterAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          gwWaterAmountTotal += num;
        }
      });
      self.gwWaterAmountTotal.html(gwWaterAmountTotal.toLocaleString('en-US'));

      self.rvWaterAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          rvWaterAmountTotal += num;
        }
      });
      self.rvWaterAmountTotal.html(rvWaterAmountTotal.toLocaleString('en-US'));

      self.waterAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          waterAmountTotal += num;
        }
      });
      self.waterAmountTotal.html(waterAmountTotal.toLocaleString('en-US'));

      self.ciContractAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          ciContractAmountTotal += num;
        }
      });
      self.ciContractAmountTotal.html(ciContractAmountTotal.toLocaleString('en-US'));

      self.gwContractAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          gwContractAmountTotal += num;
        }
      });
      self.gwContractAmountTotal.html(gwContractAmountTotal.toLocaleString('en-US'));

      self.rvContractAmount.each(function () {
        var thisValue = $(this).html();
//      alert(thisValue);
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          rvContractAmountTotal += num;
        }
      });
      self.rvContractAmountTotal.html(rvContractAmountTotal.toLocaleString('en-US'));

      self.contractAmount.each(function () {
        var thisValue = $(this).html();
        if (thisValue !== "") {
          var num = parseFloat(thisValue.replace(/[, ]+/g, ""));
          contractAmountTotal += num;
        }
      });
      self.contractAmountTotal.html(contractAmountTotal.toLocaleString('en-US'));
    };

    self.initAgencyList = function ()
    {
      self.agencyListCt.dialog({
        title: 'Agency List'
        , width: 500
        , modal: true
        , height: 600
        , buttons: [{
            text: 'Close'
            , click: function () {
              $(this).dialog("destroy").remove();
            }
          }]
      });
    };

    self.initPrintPDF = function ()
    {
      alert("Open PDF File");
      self.statusReportForm.submit();
//      self.statusReportCt.unmask();
    };

    self.initPrintCSV = function ()
    {
//      alert("Print CSV");
      var tableArray = [];
      var tableRow = self.statusReportTable.find("tbody tr");
      var filename = "ProposalStatusReport.csv";
      if(tableRow.length<1)
      {
        alert('The table is empty');
        return false;
      }
      self.statusReportCt.mask("Exporting to CSV File...");
      $.each(tableRow,function(){
        self.json = self.buildData($(this).find("td"));
        tableArray.push(self.json);
      });
//      console.log(tableArray);
      var url = window.SERVER_ROOT+"/report/fileExport?name="+filename;      
      if(tableArray.length>0)
      {
//        alert(url);
        $.ajax({
          url:url
          ,data:{jsonData:JSON.stringify(tableArray)}
          ,cache:false
          ,async:false
          ,timeout: 30000
          ,type:'POST'
          ,dataType:'json'
          ,success:function(response,status,jqxhr){
            try{
              if(!response.success){
                throw response.error || "Unable to remove target storage.";
              }
              self.statusReportCt.unmask();
              location = window.SERVER_ROOT+'/report/downloadFile?name='+filename;
            }catch(e){
              self.statusReportCt.unmask();
              if (response.callback) {
                var callback = eval(response.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              }else if (e){
                alert(e);
              }
            }
          }
        });
      }      
    };
    
    self.buildData = function(list){
      var jsonObj = new Object();
      list.each(function(){
        if($(this).attr("fieldName")){
          jsonObj[$(this).attr("fieldName")] = $(this).html();
//          if($(this).attr("fieldName") === "stateWellNum")
//          {
//            jsonObj[$(this).attr("fieldName")] = $(this).html();
//          }
        }
      });
      return jsonObj;
    }; 

    self.init();
  };

  window.statusReort = new statusReport();
</script>
