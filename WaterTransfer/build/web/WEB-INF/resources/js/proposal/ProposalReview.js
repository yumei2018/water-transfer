var ProposalReview = function (cfg)
{
  var self = this;
  self.init = function ()
  {
    self.initItems();
    self.initListeners();
    self.initReviewPage();
  };

  self.initItems = function ()
  {
    self.reviewCt = $("#" + cfg);
    self.sellerCt = self.reviewCt.find(".sellerCt");
    self.buyerCt = self.reviewCt.find(".buyerCt");
    self.commentbtn = self.reviewCt.find(".header");
    self.checkBoxCt = self.reviewCt.find(".checkBoxCt");
    self.checkBox = self.checkBoxCt.find("input[type=checkbox]");
    self.techNote = self.reviewCt.find(".techNote");
    self.rightNav = $("#right-nav");
    self.rightNavUl = $("#right-nav").find("ul");
    self.gwCt = $("#review-gw");
    self.associagedWellsTable = self.gwCt.find("#associateWellCt");
    self.approvedTransferNum = self.gwCt.find("#approvedTransferNum");
    self.approvedMonitoringNum = self.gwCt.find("#approvedMonitoringNum");
  };

  self.initListeners = function () {
//      self.commentbtn.unbind('click').bind("click", self.initComment);
//      self.techNote.on("blur", self.initNote);

    $(window).unbind('resize').bind("resize", self.sideMenuResize);
    $(window).unbind('scroll').bind('scroll',self.sideMenuResize);
  };

  // <editor-fold defaultstate="collapsed" desc="Initial Page Load">
  self.sideMenuResize = function () {
    var left = 0;
    if(typeof self.reviewCt !== 'undefined' && typeof self.reviewCt.position() !== 'undefined'){
//      alert("defined");   
      left = self.reviewCt.position().left;
    }
//    var left = 0;
//    if (typeof self.reviewCt.offset()=== "function"){
//      left = self.reviewCt.offset().left;
//    }
    self.sideMenu = $("#right-nav");
    var offset = 3;
    var pos = left - self.sideMenu.width() - offset + $(window).scrollLeft();
    if ($(window).width()<1080){
      pos-= 1080 - $(window).width();
    }
    
    self.sideMenu.css({
      right: pos + 'px'
    }).removeClass("hidden");

    $(".fixed").css({
      left: left + 'px'
    });
  };
//jQuery('<div/>', {
//    id: 'wrapper',
//    css: {width: '1270px',
//          height:'1px'}
//}).appendTo('#content-ct');

  self.auto_grow = function (el) {
    el.style.height = "5px";
    el.style.height = (el.scrollHeight) + "px";
  };

  self.initReviewPage = function ()
  {
    // Initial Right Menu
    var wellHeaders = self.gwCt.find(".wellHeader");
    var checkedImg = self.rightNav.find(".checkboximg").attr("src");
//    alert(checkedImg);
    $.each(wellHeaders, function () {
//        alert($(this).html());
      var sectionKey = $(this).attr("sectionKey");
      var rowNumber = $(this).attr("rowNumber");
      var wellNum = $(this).attr("wellNum");
      var wellHtml = '<li>'
              + '<a href=#section-' + rowNumber + '>'
              + '<span style="margin-left:20px;width:30px;">' + rowNumber + '</span>Well ' + wellNum
              + '<img class="checkboximg hidden" src="'+checkedImg+'" sectionKey=' + sectionKey + '>'
              + '</a>'
              + '</li>';
      self.rightNavUl.append(wellHtml);
    });

    self.initSectionTitle();
  };

  // Initial Section Titles
  self.initSectionTitle = function ()
  {
    var wellChecked = true;
    $.each(self.checkBox, function () {
      var title = $(this).parent().parent().parent().find(".header");
      var sectionKey = $(this).attr("sectionKey");
      var wellNum = $(this).attr("wellNum");
      var completeFlag = self.gwCt.find("#"+wellNum);
      var checkImg = self.rightNav.find("img[sectionKey=" + sectionKey + "]");
//        alert(sectionKey);
//        alert(title.html()+":"+$(this).attr('checked'));
      if ($(this).is(':checked')) {
//          alert(title.html());
        title.css("background-color", "#0190c8");
        checkImg.removeClass("hidden").show();
//        checkFlag.removeClass("hidden").show();
        completeFlag.html("Yes");
      } else {
        title.css("background-color", "red");
        checkImg.removeClass("hidden").hide();
//        checkFlag.removeClass("hidden").hide();
        completeFlag.html("No");
        if (sectionKey.indexOf('H') !== -1) {
          wellChecked = false;
        }
      }
    });

    // Initial Section-H Well List Title
//      alert(wellChecked);
    var wellListHeader = self.gwCt.find(".wellListHeader");
    var wellCheckImg = self.rightNav.find("img[sectionKey=H]");
    if (wellChecked) {
      wellListHeader.css("background-color", "#0190c8");
      wellCheckImg.removeClass("hidden").show();
    } else {
      wellListHeader.css("background-color", "red");
      wellCheckImg.removeClass("hidden").hide();
    }
    
    // Calculate Approved Wells Number 
    self.calApprTranNum();
    self.calApprMoniNum();
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Initial Functions">  
  self.calApprTranNum = function()
  {
    var apprTranNum = 0;
    var wellTypes = self.associagedWellsTable.find(".wellType");
    $.each(wellTypes, function () {
//      alert($(this).html());
      if ($(this).html() === "Transfer"){
        var isApproved = $(this).next().html();
//        alert(isApproved);
        if (isApproved === "Yes"){
          apprTranNum += 1;
        }
      }
    }); 
//    alert(apprTranNum);
    self.approvedTransferNum.html(apprTranNum);
  };
  
  self.calApprMoniNum = function()
  {
    var apprMoniNum = 0;
    var wellTypes = self.associagedWellsTable.find(".wellType");
    $.each(wellTypes, function () {
//      alert($(this).html());
      if ($(this).html() === "Monitoring"){
        var isApproved = $(this).next().html();
//        alert(isApproved);
        if (isApproved === "Yes"){
          apprMoniNum += 1;
        }
      }
    }); 
//    alert(apprMoniNum);
    self.approvedMonitoringNum.html(apprMoniNum);
  };

  self.initNote = function ()
  {
    var title = $(this).parent().parent().parent().prev().find(".header");
    title.css("background-color", "red");
//      alert(title.html());
  };
  
  self.initClean = function (el)
  {
    var techNote = $(el).parent().find(".techNote");
    techNote.val("");
  };
  
  self.initSlideHistory = function (el)
  {    
    var historyCt = $(el).parent().find(".historyCt");
    var thisValue = $(el).val();
      
    historyCt.slideToggle("slow");
    if (thisValue === "Hide History"){
      $(el).val("Show History");
    } else if (thisValue === "Show History"){
      $(el).val("Hide History");
    }      
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Complete Check Functions">
  self.initCompletedCheckbox = function (el) {
    var title = $(el).parent().parent().parent().find(".header");
//      alert(title.html());
    var transId = $(el).attr("transId"),
            sectionKey = $(el).attr("sectionKey"),
            wellNum = $(el).attr("wellNum"),
            checkVal = 0;
    var checkImg = self.rightNav.find("img[sectionKey=" + sectionKey + "]");
    var completeFlag = self.gwCt.find("#"+wellNum);
//    alert(wellNum);
//      alert(transId);
    if (!transId || !sectionKey) {
      throw "The transId and the sectionKey are required";
    }
    if (el.checked) {
      $(el).val("1");
      checkVal = 1;
      title.css("background-color", "#0190c8");
      checkImg.removeClass("hidden").show();
//      checkFlag.removeClass("hidden").show();
      completeFlag.html("Yes");
    } else {
      $(el).val("0");
      checkVal = 0;
      title.css("background-color", "red");
      checkImg.removeClass("hidden").hide();
//      checkFlag.removeClass("hidden").hide();
      completeFlag.html("No");
    }
    var url = window.SERVER_ROOT + '/review/markcomplete';
    $.ajax({
      url: url
      , data: {transId: transId, sectionKey: sectionKey, isComplete: checkVal}
      , type: 'POST'
      , cache: false
      , dataType: 'json'
      , success: function (response, status, jqxhr) {
//          alert(response.success);
        if (!response.success) {
          throw response.error || "Error occurred while updating the status. Please contact the site adminstrator to resolve the issue!";
        }
        self.initSectionTitle();
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };
  
  self.checkReviewCompleted = function (el) {
    var transId = $(el).attr("transId"),
        section = $(el).attr("section"),
        swpaoNum = $(el).attr("swpaoNum"),
        swpaoReviewer = $(el).attr("swpaoReviewer");

//    alert(transId + ":" + section + ":" + swpaoNum);
    var sbChecked = true;
    var baseChecked = true;
    var ciChecked = true;
    var resChecked = true;
    var gwChecked = true;
    $.each(self.checkBox, function () {
      var sectionKey = $(this).attr("sectionKey");
      if (!$(this).is(':checked')){
        if (sectionKey==="A") {
          sbChecked = false;
        } if (sectionKey==="B" || sectionKey==="C" || sectionKey==="D") {
          baseChecked = false;
        } else if (sectionKey==="E"){
          ciChecked = false;
        } else if (sectionKey==="F"){
          resChecked = false;
        } else {
          gwChecked = false;
        }
      }
    });
    
    switch(section){
      case "BASE":
        if (sbChecked === false){
          alert("Buyer, Seller Section is not complete");
        } else if (baseChecked === false){
          alert("General Information Section is not complete");
        } else {
          self.checkCompleteNotice(transId,section,swpaoNum,swpaoReviewer);
        } break;
      case "CI":
        if (ciChecked === false){
          alert("Crop Idling and Crop Shifting Section is not complete");
        } else {
          self.checkCompleteNotice(transId,section,swpaoNum,swpaoReviewer);
        } break;
      case "RES":
        if (resChecked === false){
          alert("Reservoir Release Section is not complete");
        } else {
          self.checkCompleteNotice(transId,section,swpaoNum,swpaoReviewer);
        } break;
      case "GW":
        if (gwChecked === false){
          alert("Groundwater Substitution and Associated Wells Section is not complete");
        } else {
          self.checkCompleteNotice(transId,section,swpaoNum,swpaoReviewer);
        } break;
    }
  };
  
  self.checkCompleteNotice = function (transId,section,swpaoNum,swpaoReviewer){
    var title = "Review Confirmation";
    var msg = "Are you sure you want to submit your review?";
    self.confirmMsg(title, msg, function (bool) {
      if (!bool){ return false; }
      
      $.ajax({
        url: window.SERVER_ROOT + '/review/checkcompletenotice'
        , data: {transId: transId, section: section, swpaoNum: swpaoNum, swpaoReviewer: swpaoReviewer}
        , method: 'POST'
        , dataType: 'json'
        , cache: false
        , success: function (response, textStatus, jqXhr) {
          try {
            if (!response.success) {
              throw response.error || "Error occurred while check complete notice. Please contact the site adminstrator to resolve the issue!";
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
    });
    return this;
  };
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Technical Note Functions">
  self.initTechNoteAdd = function (el) {
    var transId = $(el).attr("transId"),
            sectionKey = $(el).attr("sectionKey");
    var techNote = $(el).parent().find(".techNote");
    var historyCt = $(el).parent().find(".historyCt");
    var lastNoteCt = $(el).parent().find(".lastNoteCt");
    var checkBox = $(el).parent().parent().find("input[type=checkbox]");
    var checkVal = checkBox.val();
    if (!transId || !sectionKey) {
      throw "The transId and the sectionKey are required";
    }
    if (!techNote || techNote.val() === "") {
      alert("The comment is empty.");
      return false;
    }
//    alert(transId + ":" + sectionKey + ":" + techNote + ":" + checkVal);
    $.ajax({
      url: window.SERVER_ROOT + '/review/addtechnicalnote'
      , data: {transId: transId, sectionKey: sectionKey, note: techNote.val(), isComplete: checkVal}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the note. Please contact the site adminstrator to resolve the issue!";
          }
          var note = response.note;
          var updatedBy = response.updatedBy;
          var noteDate = response.noteDate;
//        alert(updatedBy + ":" + noteDate + ":" + note);
          var noteHtml = '<div style="color:#003cb3;padding-top:20px;">'
                       + '<span style="font-size: 15px;">' + updatedBy
                       + '</span><span style="padding-left: 10px;">' + noteDate
                       + '</span></div><div style="padding-left: 40px;">' + note
                       + '</div>';
          if (typeof lastNoteCt.html() !== "undefined"){
            noteHtml = noteHtml + lastNoteCt.html();
            lastNoteCt.html(noteHtml);
          } else {
            $(el).parent().append(noteHtml);
          }
//          alert(noteHtml);          
          techNote.val("");
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

  self.initTechNoteSave = function (el) {
    var transId = $(el).attr("transId"),
            sectionKey = $(el).attr("sectionKey");
    if (!transId || !sectionKey) {
      throw "The transId and the sectionKey are required";
    }
    $.ajax({
      url: window.SERVER_ROOT + '/review/savetechnicalnote'
      , data: {transId: transId, sectionKey: sectionKey, note: $(el).val()}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while updating the note. Please contact the site adminstrator to resolve the issue!";
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

  self.initInternalNotes = function (el)
  {
    self.noteDialog = $("<div>");
    var transId = $(el).attr("transId"),
            sectionKey = $(el).attr("sectionKey");
    if (!transId || !sectionKey) {
      throw "The transId and the sectionKey are required";
    }
    self.reviewCt.mask("Loading Internal Comment...");
//      alert(transId + ":" + sectionKey);
    $.ajax({
      url: window.SERVER_ROOT + '/review/internalnotes'
      , data: {transId: transId, sectionKey: sectionKey}
      , type: 'POST'
      , cache: false
      , success: function (data, status, jqxhr) {
        self.noteDialog.append(data);
        self.noteDialog.dialog({
          title: 'Internal Comment'
          , width: 500
          , modal: true
          , height: 300
          , overflow: true
          , buttons: [{
              text: 'Save'
              , click: function () {
                var iNote = $(this).parent().parent().parent().find(".internalNote").val();
                if (!iNote || iNote === "") {
                  alert("The comment is empty.");
                  return false;
                }
                $.ajax({
                  url: window.SERVER_ROOT + '/review/saveinternalnote'
                  , type: 'POST'
                  , data: {transId: transId, sectionKey: sectionKey, note: iNote}
                  , dataType: 'json'
                  , cache: false
                  , success: function (response, textStatus, jqXhr) {
                    try {
                      if (!response.success) {
                        throw response.error || "Error occurred while save the note. Please contact the site adminstrator to resolve the issue!";
                      }
                      var note = response.note;
                      var updatedBy = response.updatedBy;
                      var noteDate = response.noteDate;
                      var imgSrc = window.SERVER_ROOT + '/resources/images/icons/crossx.png';
                      var imgHtml = '<img class="delete_img" style="cursor:pointer;"'
                                    + ' noteId=' + response.wtInternalNoteId
                                    + ' src=' + imgSrc
                                    + ' onclick="window.reviewer.initDeleteNote(this);"/>';
//                        alert(updatedBy + ":" + noteDate + ":" + note);
                      var noteHtml = '<div style="color:#003cb3;padding-top:20px;">'
                              + '<span style="font-size: 15px;">' + updatedBy
                              + '</span><span style="padding-left: 10px;">' + noteDate
                              + '</span>' + imgHtml 
                              + '</div><div style="padding-left: 40px;">' + note                              
                              + '</div>';
                      $(".notesCt").prepend(noteHtml);
                      $(".internalNote").val("");
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
              }
            }, {
              text: 'Close'
              , click: function () {
                $(this).dialog("destroy").remove();
              }
            }]
        });
        self.reviewCt.unmask();
      }
      , error: function (xhr, errorType, exception) {
        if (xhr.status === 403) //session ends
        {
          location = window.SERVER_ROOT;
        }
      }
    });
  };

  self.initDeleteNote = function (el) {
    var noteId = $(el).attr("noteId");
    if (!noteId) {
      throw "The noteId is required";
    }
    $.ajax({
      url: window.SERVER_ROOT + '/review/deleteinternalnote'
      , data: {noteId: noteId}
      , method: 'POST'
      , dataType: 'json'
      , cache: false
      , success: function (response, textStatus, jqXhr) {
        try {
          if (!response.success) {
            throw response.error || "Error occurred while deleting the note.";
          }
          $(el).parent().hide();
          $(el).parent().next().hide();
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
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Gets and Util Functions">
  self.confirmMsg = function (tle, msg, callback)
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
            , click: function (){
              $(this).dialog("destroy").remove();
              if (callback && typeof (callback) === "function")
              {
                callback(true);
              }
            }
          }, {
            text: 'No'
            , click: function (){
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
  // </editor-fold>
  self.init();
};


