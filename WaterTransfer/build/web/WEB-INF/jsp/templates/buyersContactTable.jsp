<%--
    Document   : buyersContactTable.jsp
    Created on : Mar 28, 2016, 4:02:39 PM
    Author     : ymei
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<input type="button" value="Choose Buyers Contact" name="buyersContact" class="contact_button ${hideField}" />
<select class="buyersContact_list hidden" style="width:250px;">
  <option value="0" style="color:#cccccc;">Please select a Contact...</option>
    <c:forEach var="contact" items="${buyersContactList}">
      <option value="${contact.wtContactId}">${contact.lastName}, ${contact.firstName}</option>
    </c:forEach>
</select>
<lable class="addContact hidden" style="padding-left: 20px;padding-bottom: 20px;font-size: 10pt">
  If you cannot find the contact name on list, Click <a class="newContact" style="cursor:pointer;text-decoration:underline;color:red">here</a> to add a new one.
<!--  If buyer is not listed, please request <a class="" href='mailto:swpwatertransfer@water.ca.gov' style="cursor:pointer;text-decoration:underline;color:red">SWPAO</a> to add buyer.-->
</lable>
<table class="agencyContactList" id="buyersContactTable">
  <thead>
    <tr class="tableRow">
      <th>First Name</th>
      <th>Last Name</th>
      <th>Title</th>
      <th>Email</th>
      <th>Phone Number</th>
      <th>Address</th>
      <th style="width:30px;" class="${hideField}">Edit</th>
      <th style="width:30px;" class="${hideField}">Remove</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="contact" items="${buyersContactList}">
      <c:set var="selectedContact" value="hidden"/>
      <c:if test="${contact.wtContactId == buyersContact.wtContactId}">
        <c:set var="selectedContact" value=""/>
      </c:if>
      <tr class="tableRow contactRow ${selectedContact}" id="${contact.wtContactId}" contactid="${contact.wtContactId}">
        <td>${contact.firstName}</td>
        <td>${contact.lastName}</td>
        <td>${contact.title}</td>
        <td>${contact.email}</td>
        <td>${contact.phoneNumber}</td>
        <td>
          <c:if test="${not empty contact.address1}">
            ${contact.address1}<br />${contact.cityName},${contact.wtState.shortName}&nbsp;${contact.zipcode}
          </c:if>
        </td>
        <td class="${hideField}" style="text-align:center;"><span><img src="${pageContext.request.contextPath}/resources/images/icons/table_edit.png" class="editContact"></span></td>
        <td class="${hideField}" style="text-align:center;"><span><img src="${pageContext.request.contextPath}/resources/images/icons/picture_delete.png" class="removeContact"></span></td>
        <input type="hidden" value="${contact.wtContactId}" name="wtContactId"/>
        <input type="hidden" value="${contact.firstName}" name="firstName"/>
        <input type="hidden" value="${contact.middleName}" name="middleName"/>
        <input type="hidden" value="${contact.lastName}" name="lastName"/>
        <input type="hidden" value="${contact.title}" name="title"/>
        <input type="hidden" value="${contact.address1}" name="address1"/>
        <input type="hidden" value="${contact.cityName}" name="cityName"/>
        <input type="hidden" value="${contact.wtState.wtStateId}" name="wtStateId"/>
        <input type="hidden" value="${contact.wtState.shortName}" name="shortName"/>
        <input type="hidden" value="${contact.zipcode}" name="zipcode"/>
        <input type="hidden" value="${contact.phoneNumber}" name="phoneNumber"/>
        <input type="hidden" value="${contact.phoneType}" name="phoneType"/>
        <input type="hidden" value="${contact.email}" name="email"/>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@include file="contactForm.jsp" %>

<script type="text/javascript">
  $(document).ready(function(){
    var self = this;
    self.proposalCt = $("#tabs");
    self.buyerTab = self.proposalCt.find("#buyer_tab");
    self.contactPanel = self.proposalCt.find(".contact_panel");
    self.contactbt = self.buyerTab.find("input[name=buyersContact]");
    self.buyersContactList = self.buyerTab.find(".buyersContact_list");
    self.buyersContactTable = self.buyerTab.find("#buyersContactTable");
    self.buyersContactRow = self.buyersContactTable.find("tbody tr:last");
    self.newContactBtn = self.buyerTab.find(".newContact");
    self.contactCt = self.buyerTab.find("#contact_ct");
    self.contactForm = self.buyerTab.find("#contactform");
    self.removeBtn = self.buyerTab.find(".removeContact");
    self.editBtn = self.buyerTab.find(".editContact");

//    alert(self.buyersContactTable.find("tbody tr:visible").length);
    if(self.buyersContactTable.find("tbody tr:visible").length===0){
      self.buyersContactTable.addClass("hidden");
    }

    self.contactCt.find("input[name=phoneNumber]").inputmask("(999) 999-9999");
    self.contactForm.validate({
        onfocusout: function(element){
          $(element).valid();
        },
        rules:{
          firstName: "required"
          ,lastName: "required"
          ,phoneNumber:"required"
          ,email:"required"
        },
        messages: {
          filename: "<p>This field is required</p>"
          ,title:"<p>This field is required</p>"
          ,phoneNumber:"<p>This field is required</p>"
          ,email:"<p>This field is required</p>"
        },
        submitHandler: function(form) {
          return false;
        }
    });

    self.newContactBtn.on("click",function(){
//      alert("Add New Contact ...");
//      alert(self.buyersContactTable.find("tbody tr:visible").attr("id"));
      $(".fieldValue").val("");
      self.contactDialog();
    });

    self.editBtn.unbind("click").bind("click",function(){
//      alert($(this).parent().parent().parent().html());
      var thisRow = $(this).parent().parent().parent();
      // Copy values to contact form
      self.contactForm.find("input[name=wtContactId]").val(thisRow.find("input[name=wtContactId]").val());
      self.contactForm.find("input[name=title]").val(thisRow.find("input[name=title]").val());
      self.contactForm.find("input[name=firstName]").val(thisRow.find("input[name=firstName]").val());
      self.contactForm.find("input[name=middleName]").val(thisRow.find("input[name=middleName]").val());
      self.contactForm.find("input[name=lastName]").val(thisRow.find("input[name=lastName]").val());
      self.contactForm.find("input[name=address1]").val(thisRow.find("input[name=address1]").val());
      self.contactForm.find("input[name=cityName]").val(thisRow.find("input[name=cityName]").val());
      self.contactForm.find("select[name=wtStateId]").val(thisRow.find("input[name=wtStateId]").val());
      self.contactForm.find("input[name=zipcode]").val(thisRow.find("input[name=zipcode]").val());
      self.contactForm.find("input[name=phoneNumber]").val(thisRow.find("input[name=phoneNumber]").val());
      self.contactForm.find("select[name=phoneType]").val(thisRow.find("input[name=phoneType]").val());
      self.contactForm.find("input[name=email]").val(thisRow.find("input[name=email]").val());
      self.contactDialog();
    });

    self.contactDialog = function(){
//      alert("Dialog");
      self.contactCt.dialog({
          appendTo: "form#contactform"
         ,title: "Contact Form"
         ,width: 550
         ,height: 550
         ,resizable: false
         ,modal:true
         ,buttons:[{
           text:'Save'
           ,click:function(){
              var url = window.SERVER_ROOT + "/proposal/saveBuyersContact";
              var data = self.contactForm.serialize();
              if (!$("#contactform").valid()){
                return;
              };
              $.ajax({
                type:"POST"
                ,url:url
                ,data:data
                ,cache:false
                ,success:function(data,status,jqxhr){
                  alert("Contact Saved");
                  self.contactPanel.html(data);
                }
                ,error:function(xhr, errorType, exception){
                  if(xhr.status === 403) //session ends
                  {
                    location = window.SERVER_ROOT;
                  }
                 }
              });
//              $(this).dialog("destroy").remove();
              $(this).dialog('close');
           }
         },{
            text:'Cancel'
            ,click:function(){
//              $(this).dialog("destroy").remove();
                $(this).dialog('close');
            }
          }]
      }).dialog('open');
    };

    self.contactbt.on("click",function(){
      $(this).next().fadeToggle(1).removeClass("hidden");
      $(this).next().next().fadeToggle(1).removeClass("hidden");
    });

    self.removeBtn.on("click",function(){
      alert("Please choose another Buyers Contact.");
      self.buyersContactTable.addClass("hidden");
    });

    self.buyersContactList.on("change",function(){
//      alert($(this).val());
      $(".contactRow").addClass("hidden");
      $(this).next().next().removeClass("hidden");
      $("#"+$(this).val()).removeClass("hidden");
//      var selectedRow = $("#"+$(this).val()).html();
//      alert(selectedRow);
//      self.buyersContactRow.html(selectedRow);
    });
  });
</script>