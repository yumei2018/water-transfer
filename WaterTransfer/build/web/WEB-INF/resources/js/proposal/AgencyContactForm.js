/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var AgencyContactForm = function(idList,callback){
  var self = this;
  self.agencyContactInfo = idList;
  self.init = function ()
  {
    self.initItems();
    self.saveAgencyForm();
  };
  self.initItems = function ()
  {
    self.form = $("<form method='post' id='agencyform' action='#'>");
    self.isValidated = true;
    self.agencyType = self.agencyContactInfo["agencyType"];
    self.contactTitle = self.agencyType + " Contact";
//    alert(self.contactTitle);
  };
  self.initListener = function ()
  {
    self.contact_ct.find("input[name=phoneNumber]").inputmask("(999) 999-9999");
    self.contact_ct.find("input").on("change", function () {
      if ($(this).attr("name") === "email")
      {
        if (!self.validateEmail($(this).val()))
        {
          self.isValidated = false;
          $(this).addClass("isValid");
        }
        else
        {
          self.isValidated = true;
          $(this).removeClass("isValid");
        }
        if ($(this).val().length < 1)
        {
          self.isValidated = true;
          $(this).removeClass("isValid");
        }
      }
      if ($(this).attr("name") === "phoneNumber"
              || $(this).attr("name") === "firstName"
              || $(this).attr("name") === "lastName")
      {
        if ($(this).val())
        {
          $(this).removeClass("isValid");
        }
      }
    });
  };
  self.existContactListener = function ()
  {
    self.contact_ct = self.form.find("#agency_contact_ct");
    self.inactiveContact = self.contact_ct.find("div:first");
    self.inactiveContact.find("select").on("change", function () {
      if ($(this).val() < 1)
      {
        return false;
      }
      self.form.find("div").css({opacity: '1.0'});
      $(this).parent().fadeOut("slow");
      self.contactData = JSON.parse($(this).val());
      $.each(self.contactData, function (key, value) {
        self.form.find("input[name=" + key + "]").val(value);
      });
      self.form.find("input[name=isActive]").val(1);
    });
    self.contact_ct.find("img").on("click", function () {
      self.form.find("div").css({opacity: '1.0'});
      $(this).fadeOut("slow");
      self.inactiveContact.fadeOut("slow");
    });
    self.initListener();
  };
  self.agencyContactDialog = function (callback)
  {
    $.ajax({
      type: "POST"
      , url: window.SERVER_ROOT + "/proposal/editContact"
      , data: {wtContactId: self.getContactId(), wtAgencyId: self.getAgencyId(), wtTransId: self.getTransId()}
      , cache: false
      , scope: this
      , success: function (data, status, jqxhr) {
        self.form.append(data);
        self.existContactListener();
        self.buildButtons = [];
        if (!self.getContactId())
        {
          self.existContactBtn = {
            text: 'Use Existing Contacts'
            , click: function ()
            {
              self.form.find("div").css({opacity: '0.8'});
              self.availableContact = self.form.find(".hidden");
              self.availableContact.show().css({opacity: '1.0'});
              self.availableContact.find("select").val(0);
              self.form.find("img").show();
            }
          };
//                    self.buildButtons.push(self.existContactBtn); //hide the button for now
        }
        ;
        self.saveButton = {
          text: 'Save'
          , click: function ()
          {
            self.formArray = self.form.serializeArray();
            var msg = false;
            var validationCheck = true;
            if (self.getContactId())
            {
              msg = "Are you sure you want to update the changes?";
            }
            if (self.getAgencyId())
            {
              $(this).find("input[name=wtAgencyId]").val(self.getAgencyId());
            }
            if (self.getTransId())
            {
              $(this).find("input[name=wtTransId]").val(self.getTransId());
            }
            $(this).find("input[name=firstName]").removeClass("isValid");
            $(this).find("input[name=lastName]").removeClass("isValid");
            $(this).find("input[name=phoneNumber]").removeClass("isValid");
            $(this).find("input[name=email]").removeClass("isValid");
            if (!$(this).find("input[name=firstName]").val())
            {
              $(this).find("input[name=firstName]").addClass("isValid");
              validationCheck = false;
            }
            if (!$(this).find("input[name=lastName]").val())
            {
              $(this).find("input[name=lastName]").addClass("isValid");
              validationCheck = false;
            }
            if (!$(this).find("input[name=phoneNumber]").val())
            {
              $(this).find("input[name=phoneNumber]").addClass("isValid");
              validationCheck = false;
            }
            if (!self.validateEmail($(this).find("input[name=email]").val()))
            {
              $(this).find("input[name=email]").addClass("isValid");
              validationCheck = false;
            }
            if (!self.isValidated)
            {
              alert("Please check Input field(s)");
              validationCheck = false;
            }
            if (validationCheck)
            {
              var param = $(this).serialize(), dialogCt = $(this);
              self.contactValidation(msg, function () {
                dialogCt.dialog("destroy").remove();
                if (callback && typeof (callback) === "function")
                {
                  callback(param);
                }
              });
            }
          }
        };
        self.cancelBtn = {
          text: 'Cancel'
          , click: function ()
          {
            $(this).dialog("destroy").remove();
          }
        };
        self.buildButtons.push(self.saveButton);
        self.buildButtons.push(self.cancelBtn);
        self.form.dialog({
          title: self.contactTitle
          , width: 550
          , height: 550
          , resizable: false
          , modal: true
          , buttons: self.buildButtons
        }).dialog('open');
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
        else
        {
          alert("Saving Failed");
        }
      }
    });
  };
  self.saveAgencyForm = function ()
  {
    var url = window.SERVER_ROOT + "/proposal/saveContact";
    // Buyers Representative Contact
    if (self.agencyType === "Buyers") {
      url = window.SERVER_ROOT + "/proposal/saveBuyersContact";
    }
    self.agencyContactDialog(function (param) {
      $.ajax({
        type: 'POST'
        , url: url
        , data: param
        , cache: false
        , success: function (data, status, jqxhr)
        {
          if (!status)
          {
            alert("Error Saving");
            return false;
          }
          if (callback && typeof (callback) === "function")
          {
            callback(data);
          }
        }
        , error: function (xhr, errorType, exception) {
          if (xhr.status === 403) //session ends
          {
            location = window.SERVER_ROOT;
          }
          else
          {
            alert("Saving Failed");
          }
        }
      });
    });
  };
  self.contactValidation = function (msg, callback)
  {
    if (msg)
    {
      $("<div>", {
        html: '<p>' + msg + '</p>'
      }).dialog({
        title: 'User Validation'
        , model: true
        , width: 400
        , frame: true
        , height: 200
        , buttons: [{
            text: 'Yes'
            , click: function ()
            {
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback();
              }
            }
          }, {
            text: 'No'
            , click: function ()
            {
              $(this).dialog("destroy").remove();
            }
          }]
      }).dialog("open");
    }
    else
    {
      callback();
    }

  };
  self.getAgencyId = function ()
  {
    return self.agencyContactInfo["wtAgencyId"];
  };
  self.getContactId = function ()
  {
    return self.agencyContactInfo["wtContactId"];
  };
  self.getTransId = function ()
  {
    return self.agencyContactInfo["wtTransId"];
  };
  self.validateEmail = function (email)
  {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return email.match(re) ? true : false;
  };


  self.init();
};
