<%--
    Document   : registration
    Created on : May 14, 2015, 1:12:18 PM
    Author     : pheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<form id="registerFormCt" method="post" action="">
    <div>
        <div>
            <label>First Name <span style="color:red;">*</span></label><br/>
            <input text="text" style="width:200px;" name="firstName" maxlength="16"/>
        </div>
        <div>
            <label>MI</label><br/>
            <input text="text" style="width:50px;" name="middleName"/>
        </div>
        <div>
            <label>Last Name <span style="color:red;">*</span></label><br/>
            <input text="text" style="width:150px;" name="lastName" maxlength="16"/>
        </div>
    </div>
<!--            <div class="singleCol">
        <label>Address</label><br/>
        <input text="text" name="address1"/>
    </div>
     <div>
        <div>
            <label>City</label><br/>
            <input type="text" name="city" />
        </div>
        <div>
             <label>State</label><br/>
             <input type="text" style="width:50px;" name="state"/>
        </div>
        <div>
            <label>Zip</label><br/>
            <input text="text" style="width:150px;" name="zipcode" />
        </div>
    </div>
    <div>
        <div>
            <label>Phone <span style="color:red;">*</span></label><br/>
            <input text="text" name="phoneNumber" />
        </div>
        <div>
            <label>Phone Type</label><br/>
            <select name="phoneType" style="width:120px;">
                <option value="Office" selected="selected">Office</option>
                <option value="Mobile" >Mobile</option>
            </select>
        </div>
    </div>-->
    <div class="singleCol">
        <label>E-mail <span style="color:red;">*</span></label><br/>
        <input text="text" name="email" id="email" maxlength="32"/>
    </div>
    <div class="singleCol">
        <label>Confirm E-mail <span style="color:red;">*</span></label><br/>
        <input text="text" name="email2" id="email2" maxlength="32"/>
    </div>
    <div class="singleCol">
      <label>Select an Agency</label><br/>
      <select name="agencyId" class="agency_list" style="width:450px;">
        <option value="0" style="color:#cccccc;">Please select an Agency...</option>
        <c:forEach var="agency" items="${agencyList}">
          <option value="${agency.wtAgencyId}">${agency.agencyFullName}</option>
        </c:forEach>
      </select>
    </div>
    <div class="singleCol">
<!--      If you cannot find the Agency, Click <a class="newAgency" style="cursor:pointer;text-decoration:underline;color:red">here</a> to add a new one.-->
      If agency is not listed, please request <a class="" href='mailto:swpwatertransfer@water.ca.gov' style="cursor:pointer;text-decoration:underline;color:red">administrator</a> to add a new agency.
    </div>
    <input type="hidden" name="isActive" value="1" />
</form>
<style>
    input,select{
        width:200px;
        display:block;
        border: 1px solid #999;
        height: 25px;
        margin-bottom:10px;
/*        -webkit-box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.3);
        -moz-box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.3);
        box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.3);*/
    }
    #registerFormCt{
        width:500px;
    }
    #registerFormCt label.error{
        color: #FB3A3A;
        font-size: 12px;
    }
    #registerFormCt div div{
        float:left;
        margin-right:10px;
    }
    #registerFormCt div {
        overflow: hidden;
    }
    .singleCol input {
        width:80%;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
  $("#registerFormCt").validate({
    rules:{
      firstName:"required"
      ,lastName:"required"
      ,email:{
        required: true,
        email: true
      }
      ,email2:{
        required: true,
        equalTo: "#email"
      }
      ,agencyId:"required" 
    },
    messages: {
      firstName:"First Name is required"
      ,lastName: "Last Name is required"
      ,email: {
        required: "Email is required"
        ,email: "Not a valid email address"
      }
      ,email2: {
        required: "Confirm Email is required"
        ,equalTo: "Does not match above email address"
      }
      ,agencyId: "Agency is required"
    }
//    submitHandler: function(form) {
//      form.submit();
//    }
  });
});
</script>
