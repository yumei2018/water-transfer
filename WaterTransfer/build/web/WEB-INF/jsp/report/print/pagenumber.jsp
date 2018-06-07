<%--
    Document   : pagenumber
    Created on : Apr 10, 2015, 3:23:14 PM
    Author     : clay
--%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style type="text/css">
#pagenumber-ct {
    position: running(footer);
    text-align: center;
    display:none;
    font-size: 8pt;
}
#pageheader{
    position:running(header);
    text-align: center;
    display:none;
    font-size: 8pt;
}
@media print
{
    #pagenumber-ct,#pageheader
    {
        display:block !important;
    }
}
@page {
    @bottom-right {
        content: element(footer);
    }
}
@page {
    @top-right{
         content: element(header);
    }
}

#pagenumber:before {
    content: counter(page);
}

#pagecount:before {
    content: counter(pages);
}
</style>
<div id='pagenumber-ct'>
<!--    <span style="float:left;">Groundwater_proposal260_v1.3</span>-->
<!--    <span style="float:left;">${pdfFileName}_v${version}</span>-->
    <span style="float:left;">Proposal #${proposal.wtTransId}</span>
    Page <span id='pagenumber'></span>
    of <span id='pagecount'></span>
    <span style="float:right;"><fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="<%=new java.util.Date()%>" /></span>
</div>

<div id='pageheader'>
<!--    <span style="float:left;">Groundwater_proposal260.pdf</span>-->
<!--    <span style="float:left;">${pdfFileName}</span>
    <span style="float:right;">Version ${version}</span>-->
    <span style="float:left;">${proposal.wtSellerCollection[0].agencyFullName}</span>
    <span style="float:right;">${proposal.transYear}</span>
</div>
