var ProposalAdmin = function()
{
  var self = this;
  self.init = function ()
  {
    self.initItems();
    self.initListeners();
  };
  
  self.initItems = function ()
  {
    
  };
  
  self.initListeners = function () {  
    self.proposalList();
  };
  
  self.proposalList = function(){
    $.ajax({
      url: window.SERVER_ROOT + '/admin/proposallist'
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while get Proposal List. Please contact the site adminstrator to resolve the issue!";
          }
          var transArray = response.data;
          for(var i in transArray){
//            alert(transArray[i]['wtTransId']);
            var transId = transArray[i]['wtTransId'];
            var seller = transArray[i]['seller'];
            var buyers = transArray[i]['buyers'];
            var transType = transArray[i]['transType'];
//            alert(seller);
            $("."+transId).find(".seller").html(seller);
            $("."+transId).find(".buyers").html(buyers);
            $("."+transId).find(".transType").html(transType);
          }
        }
        catch (e) {
          if (response.callback) {
            var callback = eval(response.callback);
            callback.call(self, e);
          } else {
            alert(e);
          }
        }
      }
    });
    return this; 
  };
  
  self.showAgency = function(el){
    self.agencyCt = $(el).parent().parent().find(".agency_ct");
    self.proposalCt = $(el).parent().parent().find(".proposal_ct");
    
    self.agencyCt.removeClass("hidden").show();
    self.proposalCt.removeClass("hidden").hide();
  };
  
  self.showProposal = function(el){
    self.agencyCt = $(el).parent().parent().find(".agency_ct");
    self.proposalCt = $(el).parent().parent().find(".proposal_ct");
    
    self.agencyCt.removeClass("hidden").hide();
    self.proposalCt.removeClass("hidden").show();
  };
  
  self.initAgencyCheckbox = function(el){
    var agencyId = $(el).attr("agencyId"),
        userId = $(el).attr("userId"),
        checkVal = 0;  
    if (!agencyId || !userId) {
      throw "The agencyId and the userId are required";
    }
    if (el.checked) {
      checkVal = 1;
    }    
//    alert(agencyId + "-" + userId + "-" + checkVal);
    var url = window.SERVER_ROOT + '/admin/changepermission';
    $.ajax({
      url: url
      , data: {agencyId: agencyId, userId: userId, isChecked: checkVal}
      , type: 'POST'
      , cache: false
      , dataType: 'json'
      , success: function (response, status, jqxhr) {
//          alert(response.success);
        if (!response.success) {
          throw response.error || "Error occurred while updating the status. Please contact the site adminstrator to resolve the issue!";
        }
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };
  
  self.initTransCheckbox = function(el){
    var transId = $(el).attr("transId"),
        userId = $(el).attr("userId"),
        checkVal = 0;  
    if (!transId || !userId) {
      throw "The transId and the userId are required";
    }
    if (el.checked) {
      checkVal = 1;
    }    
//    alert(transId + "-" + userId + "-" + checkVal);
    var url = window.SERVER_ROOT + '/admin/changetransperm';
    $.ajax({
      url: url
      , data: {transId: transId, userId: userId, isChecked: checkVal}
      , type: 'POST'
      , cache: false
      , dataType: 'json'
      , success: function (response, status, jqxhr) {
//          alert(response.success);
        if (!response.success) {
          throw response.error || "Error occurred while updating the status. Please contact the site adminstrator to resolve the issue!";
        }
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };
  
  self.deleteDraftProposal = function(el){
//    alert("Delete Draft");
    var msg = "Are you sure you want to delete all Draft Proposals?";
    var title = "Delete Confirmation";
    self.userConfirmation(title, msg, function (bool) {
      if(!bool){ return false; }
      $.ajax({
          url:window.SERVER_ROOT+"/admin/deletealldraft"
          ,cache:false
          ,type:'post'
          ,dataType:'json'
          ,success:function(data,status,jqxhr){
            try{
              if(!data.success){
                throw data.error || "Unable to Delete Proposals.";
              }
              alert("All Draft Proposals have been deleted");
              location = window.SERVER_ROOT+"/proposal?moduleType=draft";
            }catch(e){
              if (data.callback) {
                var callback = eval(data.callback);
                if (typeof callback === "function") {
                  callback.call();
                }
              }else if (e){
                alert(e);
              }
            }
          }
          ,error:function(xhr, errorType, exception){
            if(xhr.status === 403) //session ends
            {
              location = window.SERVER_ROOT;
            }
          }
        });
    });
  };
  
  self.userConfirmation = function (tle, msg, callback)
  {
    var title = 'User Confirmation';
    if (tle !== null && tle !== '') {
      title = tle;
    }
    if (msg)
    {
      $("<div>", {
        html: '<p>' + msg + '</p>'
      }).dialog({
        title: title
        , modal: true
        , width: 400
        , height: 200
        , buttons: [{
            text: 'Yes'
            , click: function ()
            {
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback(true);
              }
            }
          }, {
            text: 'No'
            , click: function ()
            {
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback(false);
              }
            }
          }]
      }).dialog("open");
    }
    else {
      callback();
    }
  };
  
  self.init();
};

