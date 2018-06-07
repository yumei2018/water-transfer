<%-- 
    Document   : SearchTrans
    Created on : Dec 4, 2014, 2:25:18 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
          
    <form class="form" id="search-form">
        <div class="form-top">${bodyMessage}</div>
            
        <table id="">
            <tr class="tableRow">
                <th>Transfer ID</th><td><input class="fullRow" type="text" name="wtTransNum" id="wtTransNum" placeholder=""/></td>
                <th>Transfer Year</th>
                <td>
                    <select class="fullRow" name="transYear" id="transYear">
                        <option value=""></option>
                        <option value="2014">2014</option>
                        <option value="2013">2013</option>
                        <option value="2012">2012</option>
                        <option value="2011">2011</option>
                        <option value="2010">2010</option>
                        <option value="2009">2009</option>
                        <option value="2008">2008</option>
                        <option value="2007">2007</option>
                        <option value="2006">2006</option>
                        <option value="2005">2005</option>
                        <option value="2004">2004</option>
                        <option value="2003">2003</option>
                        <option value="2002">2002</option>
                        <option value="2001">2001</option>
                        <option value="2000">2000</option>
                        <option value="1999">1999</option>
                    </select>
                </td>
                <th>Transfer Year From</th>
                <td>
                    <select class="fullRow" name="transYearFrom" id="transYearFrom">
                        <option value=""></option>
                        <option value="2014">2014</option>
                        <option value="2013">2013</option>
                        <option value="2012">2012</option>
                        <option value="2011">2011</option>
                        <option value="2010">2010</option>
                        <option value="2009">2009</option>
                        <option value="2008">2008</option>
                        <option value="2007">2007</option>
                        <option value="2006">2006</option>
                        <option value="2005">2005</option>
                        <option value="2004">2004</option>
                        <option value="2003">2003</option>
                        <option value="2002">2002</option>
                        <option value="2001">2001</option>
                        <option value="2000">2000</option>
                        <option value="1999">1999</option>
                    </select>
                </td>
                <th>Transfer Year To</th>
                <td>
                    <select class="fullRow" name="transYearTo" id="transYearTo">
                        <option value=""></option>
                        <option value="2014">2014</option>
                        <option value="2013">2013</option>
                        <option value="2012">2012</option>
                        <option value="2011">2011</option>
                        <option value="2010">2010</option>
                        <option value="2009">2009</option>
                        <option value="2008">2008</option>
                        <option value="2007">2007</option>
                        <option value="2006">2006</option>
                        <option value="2005">2005</option>
                        <option value="2004">2004</option>
                        <option value="2003">2003</option>
                        <option value="2002">2002</option>
                        <option value="2001">2001</option>
                        <option value="2000">2000</option>
                        <option value="1999">1999</option>
                    </select>
                </td>
            </tr>
            <tr class="tableRow">
                <th>Seller</th><td><input class="fullRow" type="text" name="sellerCode" id="sellerCode" placeholder=""/></td>
                <th>Buyer</th><td><input class="fullRow" type="text" name="buyerCode" id="buyerCode" placeholder=""/></td>
                <th>Facilities Used</th>
                <td>
                    <select class="fullRow" name="fuTypeCode" id="fuTypeCode">
                        <option value=""></option>
                        <option value="CVP">CVP</option>
                        <option value="SWP">SWP</option>
                        <option value="OTHER">OTHER</option>
                    </select>
                </td>
            </tr>
        </table><p></p>
        
        <div id="form-button">
            <input class="search-button" type="button" id="search" value="Search" onclick="return false;"/>
            <input class="clear-button" type="button" id="clear" value="Clear" onclick="return false;"/>
            <input class="print-button" type="button" id="print" value="Print" onclick="return false;"/>
            <input class="chart-button" type="button" id="Chart" value="Chart" onclick="return false;"/>
        </div><p></p><p></p>
            
        <div class="table_hidden" id="table_print">   
            <%@include file="TransRecTable.jsp" %>
        </div>
    </form>
                

