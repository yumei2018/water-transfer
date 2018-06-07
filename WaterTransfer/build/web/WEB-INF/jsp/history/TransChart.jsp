<%--
    Document   : TransChart
    Created on : Dec 17, 2014, 9:31:36 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:set var="__Stylesheets">
${pageContext.request.contextPath}/resources/css/jquery-ui-1.11.3.css
,${pageContext.request.contextPath}/resources/css/AgencyContact.css
</c:set>
<c:set var="__Javascripts">
${pageContext.request.contextPath}/resources/js/jquery/jquery-1.11.2.min.js
,${pageContext.request.contextPath}/resources/js/jquery/jquery-ui-1.11.3.min.js
,${pageContext.request.contextPath}/resources/js/highcharts.js
,${pageContext.request.contextPath}/resources/js/jquery.highchartTable.js
</c:set>
<%@include file="../templates/header.jsp" %>
<style type="text/css">
/*.chart_button {
    background-color:#039;
    color:#000000;
}
#chart_button_ct > *:hover {
    background-color:#f9f9f9;
    color:#000000;
}
#chart_button_ct :visited {
    background-color:#f9f9f9;
    color:#000000;
}*/
.graphcontainer{
    width: 1000px;
}
.chart_navi{
    background-color:#fbad23;
}
</style>
<script type="text/javascript">
    $(document).ready(function(){
//        setTimeout(function(){
//            new WtTrans();
//        },1);
        $( "#tabs" ).tabs();
//        var self = this;
//        self.chartBarButton = $("#barChart");
//        self.chartAreaButton = $("#areaChart");
//        self.chartLineButton = $("#lineChart");

        $('table.areachart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
            $.each(highChartConfig.yAxis, function (index, value)   {
                value.title.text = value.title.text || 'Quantity (AF)';
            });
        }).highchartTable();

        $('table.barchart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
            highChartConfig.colors = ['#AA4C4C', '#44AACC'];
            $.each(highChartConfig.yAxis, function (index, value)   {
                value.title.text = value.title.text || 'Quantity (AF)';
            });
        }).highchartTable();

        $('table.linechart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
            $.each(highChartConfig.yAxis, function (index, value)   {
                value.title.text = value.title.text || 'Quantity (AF)';
            });
        }).highchartTable();

//        self.chartBarButton.on("click", function(evt) {
//            $(".chart_button").css('background-color','#039');
//            $(this).css('background-color','#f9f9f9');
////            $('table.highchart').highchartTable();
//            $('table.barchart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
//                highChartConfig.colors = ['#AA4C4C', '#44AACC'];
//                $.each(highChartConfig.yAxis, function (index, value)   {
//                    value.title.text = value.title.text || 'Quantity (AF)';
//                });
//            }).highchartTable();
//        });
//
//        self.chartAreaButton.on("click", function(evt) {
//            $(".chart_button").css('background-color','#039');
//            $(this).css('background-color','#f9f9f9');
////            $('table.areachart').highchartTable();
//            $('table.areachart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
//                $.each(highChartConfig.yAxis, function (index, value)   {
//                    value.title.text = value.title.text || 'Quantity (AF)';
//                });
//            }).highchartTable();
//        });
//
//        self.chartLineButton.on("click", function(evt) {
//            $(".chart_button").css('background-color','#039');
//            $(this).css('background-color','#f9f9f9');
////            $('table.linechart').highchartTable();
//            $('table.linechart').bind('highchartTable.beforeRender', function(event, highChartConfig) {
//                $.each(highChartConfig.yAxis, function (index, value)   {
//                    value.title.text = value.title.text || 'Quantity (AF)';
//                });
//            }).highchartTable();
//        });

    });
</script>
<!--<div id="chart-form">-->
<div style="padding:30px 5px 0;" class="">
    <div class="header_tab">Historic Chart</div>
    <div id="tabs">
        <ul>
            <li><a href="#barchart_tab">Bar Chart</a></li>
            <li><a href="#linechart_tab">Line Chart</a></li>
            <li><a href="#areachart_tab">Area Chart</a></li>
        </ul>

        <div id="linechart_tab" class="tab_ct">
            <table class="linechart" data-graph-container="#linechart_graphcontainer" data-graph-type="line" style="display:none" data-graph-xaxis-title-text="Transfer Year">
                <thead>
                    <tr class="header">
                        <th>Transfer Year</th>
                        <th>Proposed Transfer Quantity</th>
                        <th>Actual Transfer Quantity</th>
                        <th>Average Proposed Transfer Quantity</th>
                        <th>Average Actual Transfer Quantity</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="record" items="${chartRecords}">
                    <tr class="body">
                        <td>${record.transYear}</td>
                        <td>${record.proTransQua}</td>
                        <td>${record.actTransQua}</td>
                        <td>${aveProTransQua}</td>
                        <td>${aveActTransQua}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="graphcontainer" id="linechart_graphcontainer"></div>
        </div>

        <div id="barchart_tab" class="tab_ct">
            <table class="barchart" data-graph-container="#barchart_graphcontainer" data-graph-type="column" style="display:none" data-graph-xaxis-title-text="Transfer Year">
                <thead>
                    <tr class="header">
                        <th>Transfer Year</th>
                        <th>Proposed Transfer Quantity</th>
                        <th>Actual Transfer Quantity</th>
                        <th data-graph-type="line">Average Proposed Transfer Quantity</th>
                        <th data-graph-type="line">Average Actual Transfer Quantity</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="record" items="${chartRecords}">
                    <tr class="body">
                        <td>${record.transYear}</td>
                        <td>${record.proTransQua}</td>
                        <td>${record.actTransQua}</td>
                        <td>${aveProTransQua}</td>
                        <td>${aveActTransQua}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="graphcontainer" id="barchart_graphcontainer"></div>
        </div>

        <div id="areachart_tab" class="tab_ct">
            <table class="areachart" data-graph-container="#areachart_graphcontainer" data-graph-type="area" style="display:none" data-graph-xaxis-title-text="Transfer Year">
                <thead>
                    <tr class="header">
                        <th>Transfer Year</th>
                        <th>Proposed Transfer Quantity</th>
                        <th>Actual Transfer Quantity</th>
                    </tr>
                </thead>

                <tbody>
                <c:forEach var="record" items="${chartRecords}">
                    <tr class="body">
                        <td>${record.transYear}</td>
                        <td>${record.proTransQua}</td>
                        <td>${record.actTransQua}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="graphcontainer" id="areachart_graphcontainer"></div>
        </div>
    </div>

<!--<div id="chart_button_ct">
    <input class="chart_button" type="button" id="barChart" value="Bar Chart" onclick="return false;"/>
    <input class="chart_button" type="button" id="areaChart" value="Area Chart" onclick="return false;"/>
    <input class="chart_button" type="button" id="lineChart" value="Line Chart" onclick="return false;"/>
</div>

<div id="graphcontainer"></div>-->


</div>
<%@include file="../templates/footer.jsp" %>

