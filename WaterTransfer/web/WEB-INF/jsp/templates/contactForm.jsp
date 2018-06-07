<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style type="text/css">
#contact_ct div{
  float:left;
}

#contact_ct > div{
  padding:5px;
}
#contact_ct  > div >div{
  padding-right:10px;
}
#contact_ct label{
  color:#666666;
  font-size: 10pt;
} 
#contact_ct input{
  border: 1px solid #a6a8a8;
  font-size: 10pt;
  color: #333333;
  padding: 3px;
}
#contact_ct label.error{
  color: #FB3A3A;
  font-size: 10px;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
    $("#contactform").validate({
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
            form.submit();
        }
    });
});  
</script>

<form class="form" id="contactform" method="post">  
  <div id="contact_ct" class="hidden">
    <input type="hidden" name="wtContactId" value="" />
      <div>
          <label>Titles</label><br/>
          <input class="fieldValue" text="text" style="width:483px;" name="title" value="${contact.title}" maxlength="32"/>
      </div>
      <div>
          <div>
              <label>First Name <span style="color:red;">*</span></label><br/>
              <input class="fieldValue" text="text" style="width:200px;" name="firstName" value="${contact.firstName}" maxlength="16"/>
          </div>
          <div>
              <label>MI</label><br/>
              <input class="fieldValue" text="text" style="width:50px;" name="middleName" value="${contact.middleName}" maxlength="10"/>
          </div>
          <div>
              <label>Last Name <span style="color:red;">*</span></label><br/>
              <input class="fieldValue" text="text" style="width:200px;" name="lastName" value="${contact.lastName}" maxlength="16"/>
          </div>
      </div>
      <div>
          <label>Address</label><br/>
          <input class="fieldValue" text="text" style="width:483px;" name="address1" value="${contact.address1}"/>
      </div>
       <div>
          <div>
              <label>City</label><br/>
              <input class="fieldValue" text="text" style="width:200px;" name="cityName" value="${contact.cityName}"/>
          </div>
          <div>
            <label>State</label><br/>
            <c:choose>
              <c:when test="${empty contact.wtState}">
                <c:set var="defaultState" value="CA" />
              </c:when>
              <c:otherwise>
                <c:set var="defaultState" value="${contact.wtState.shortName}" />
              </c:otherwise>
            </c:choose>
            <select name="wtStateId" style="width:63px;">      
              <c:forEach var="state" items="${LookupDataContext.stateLookup}">
                <option value="${state.wtStateId}" ${state.shortName == defaultState?'selected':''}>${state.shortName}</option>
              </c:forEach>
            </select>
          </div>
<!--          <div>
               <label>State</label><br/>
               <select name="wtStateId" style="width:63px;" class="fieldValue">
                  <c:if test="${empty contact.wtState}">
                    <c:forEach var="state" items="${stateList}">
                      <option value="${state.wtStateId}" ${state.shortName == 'CA'?'selected':''}>${state.shortName}</option>
                    </c:forEach>
                  </c:if>
                  <c:if test="${not empty contact.wtState}">
                    <c:forEach var="state" items="${stateList}">
                      <option value="${state.wtStateId}" ${contact.wtState.wtStateId == state.wtStateId?'selected':''}>${state.shortName}</option>
                    </c:forEach>
                  </c:if>
              </select>
          </div>-->
          <div>
              <label>Zip</label><br/>
              <input class="fieldValue" text="text" style="width:150px;" name="zipcode" value="${contact.zipcode}"/>
          </div>
      </div>
      <div>
          <div>
              <label>Phone <span style="color:red;">*</span></label><br/>
              <input class="fieldValue" text="text" style="width:200px;" name="phoneNumber" value="${contact.phoneNumber}"/>
          </div>
          <div>
              <label>Phone Type</label><br/>
              <select class="fieldValue" name="phoneType" style="width:120px;">
                  <option value="Office" ${contact.phoneType == "Office"?'selected="selected"':''}>Office</option>
                  <option value="Mobile" ${contact.phoneType == "Mobile"?'selected="selected"':''}>Mobile</option>
              </select>
          </div>
      </div>
      <div>
          <label>Email <span style="color:red;">*</span></label><br/>
          <input class="fieldValue" text="text" name="email" style="width:483px;" value="${contact.email}"/>
      </div>
  </div>
</form>