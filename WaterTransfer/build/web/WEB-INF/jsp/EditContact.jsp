<%--
    Document   : EditContact
    Created on : Mar 16, 2015, 10:37:56 AM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(document).ready(function(){
        var self = this;
        self.contactForm = $("#contact-form");
        self.saveContact = $("#saveContact");
        self.contactContainer = $(".contact-container");
//        var wtAgencyId = $("input#wtAgencyId").val();
//        self.contactListContainer = $("#contact-list-"+wtAgencyId);
        self.contactListContainer = $('.contact-list-container');

        self.saveContact.on("click", function() {
//            this.contactListContainer = $(this).closest('div').next();
//            alert($("input#wtAgencyId").val());
            var data = self.contactForm.serialize();
            var url =window.SERVER_ROOT + "/proposal/saveContact";

            $.ajax({
                type:"POST"
                ,url:url
                ,cache:false
                ,data:data
                ,success:function(data,status,jqxhr){
                    alert("Contact Saved!");
                    self.contactListContainer.children().remove();
                    self.contactListContainer.append(data);
                }
            });
        });
    });
</script>

            <table id="contact-table">
                <tr class="tableRow">
                    <td><input class="fullRow" type="text" name="wtContactId" id="wtContactId" value="${contact.wtContactId}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Title:</th>
                    <td>
                        <select class="fullRow" name="title" id="title">
                            <option value="Chief Operating Officer" ${contact.title == "Chief Operating Officer"?'selected="selected"':''}>Chief Operating Officer</option>
                            <option value="Engineer and Manager" ${contact.title == "Engineer and Manager"?'selected="selected"':''}>Engineer and Manager</option>
                            <option value="General Manager" ${contact.title == "General Manager"?'selected="selected"':''}>General Manager</option>
                            <option value="Owner" ${contact.title == "Owner"?'selected="selected"':''}>Owner</option>
                        </select>
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>First Name:</th><td><input class="fullRow" type="text" name="firstName" id="firstName" value="${contact.firstName}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Middle Name:</th><td><input class="fullRow" type="text" name="middleName" id="middleName" value="${contact.middleName}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Last Name:</th><td><input class="fullRow" type="text" name="lastName" id="lastName" value="${contact.lastName}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>E-mail:</th><td><input class="fullRow" type="text" name="email" id="email" value="${contact.email}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>Phone Type:</th>
                    <td>
                        <select class="fullRow" name="phoneType" id="phoneType">
                            <option value="Office" ${contact.phoneType == "Office"?'selected="selected"':''}>Office</option>
                            <option value="Mobile" ${contact.phoneType == "Mobile"?'selected="selected"':''}>Mobile</option>
                            <option value="Fax" ${contact.phoneType == "Fax"?'selected="selected"':''}>Fax</option>
                        </select>
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Phone:</th>
                    <td><input class="fullRow" type="text" name="phoneNumber" id="phoneNumber" value="${contact.phoneNumber}"/></td>
                </tr>
                <tr class="lastTableRow">
                    <th>Address:</th><td><input class="fullRow" type="text" name="address1" id="address1" value="${contact.address1}"/></td>
                </tr>
                <tr class="tableRow">
                    <th>City:</th>
                    <td>
                        <select class="fullRow" name="wtCityId" id="wtCityId">
                            <c:forEach var="city" items="${cityList}">
                                <option value="${city.wtCityId}" ${contact.wtCity.wtCityId == city.wtCityId?'selected':''}>${city.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>State:</th>
                    <td>
                        <select class="fullRow" name="wtStateId" id="wtStateId">
                            <c:forEach var="state" items="${stateList}">
                                <option value="${state.wtStateId}" ${contact.wtState.wtStateId == state.wtStateId?'selected':''}>${state.shortName}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr class="tableRow">
                    <th>Zip Code:</th><td><input class="fullRow" type="text" name="zipcode" id="zipcode" value="${contact.zipcode}"/></td>
                </tr>
            </table>
            <div id="contact-form-button">
                <input class="save-button" type="button" id="saveContact" value="Save" onclick="return false;"/>
            </div>