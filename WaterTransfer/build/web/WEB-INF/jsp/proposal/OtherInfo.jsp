<%--
    Document   : OherInfo
    Created on : Apr 27, 2015, 9:33:13 AM
    Author     : ymei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
    <div id="other_tab">
        <!--<div class="sub-title">Other Information</div>-->
        <div id="ot-container">
            <table id="create-ot-table">
                <tr class="tableRow" style="display:none;">
                    <th>Other ID</th>
                    <td><input class="fullRow" type="text" name="wtOtherId" id="wtOtherId" value=""/></td>
                </tr>
                <tr class="tableRow">
                    <th>Identify the surface water rights<span class="asterisk">*</span></th>
                    <td><input class="fullRow" type="text" name="waterRightId" id="waterRightsId"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Water Rights Description</th>
                    <td colspan='3'>
                        <select class="" name="cvWaterRightsDesc" id="cvWaterRightsDesc">
                            <option value="Pre-1914">Pre-1914</option>
                            <option value="Post-1914">Post-1914</option>
                            <option value="WC 1725">WC 1725</option>
                        </select>
                    </td>
<!--                    <td class="fullRow" colspan='3'>
                        <textarea cols="50" rows="2" id="waterRightsDesc" name="waterRightDesc"></textarea>
                    </td>-->
                </tr>
            </table>
        </div>

        <div class="tab_header isExpand">Attachments</div>
        <div class="contact_panel">
          <div class="attachment-container" id="attachment-conserved-info" typeid="cs"></div>
          <c:if test="${proposal.wtStatusFlag.statusName eq 'DRAFT' || buttonCheck == true}">
          <div class="attach-button">
              <input class="attachButton add_agency" type="button" id="attachConserved" typeid="cs" containerid="attachment-conserved-info" value="Add Attachment" onclick="return false;"/>
          </div>
          </c:if>
        </div>
    </div>
