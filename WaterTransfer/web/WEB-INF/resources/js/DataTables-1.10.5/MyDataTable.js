/**
 *
 * @param {type} cfg
 * @returns {undefined}
 */
var MyDataTable = function(cfg) {
  if (typeof $.fn.DataTable == "undefined") {
    throw "The jQuery DataTable v1.10.10 plugin is not available!";
  }
  var self = this;

  //<editor-fold defaultstate="collapsed" desc="Private Functions">
  /**
   *
   * @param {type} evt
   * @param {type} callback
   * @returns {undefined}
   */
  function empty(o) {
    var result = (o == null)
                  || (typeof o == "undefined")
                  || (((o instanceof jQuery) || (typeof o == "string") || (o instanceof Array)) && (o.length == 0))
                  || ((typeof o == "number") && (isNaN(o)));
    if ((!result) && (typeof o == "object")) {
      result = true;
      for (var i in o) {
        if (o.hasOwnProperty(i)) {
          result = false;
          break;
        }
      }
    }
    return result;
  }

  /**
   *
   * @param {type} cfg
   * @returns {BasinRequestTable.self|BasinRequestTable}
   */
  function actionHandler(evt,callback) {
    try {
      if (empty(callback) || (typeof callback != "function")) {
        throw "The record and callback parameters cannot be unassigned! And the callback must be a function!";
      }
      var record = null;
      var row = null;
      if (((row = $(this).parents('tr')).length == 0)
          || (!(record = self.dataTable.row(row).data()))) {
        throw "Cannot retrieve the row data!";
      }
      var scope = null;
      if (!empty(self.listeners)) {
        scope = self.listeners.scope || self;
      }
      else {
        scope = self;
      }
      callback.call(self, evt, record, row);
    }
    catch(e){
      alert(e);
    }
  }

  /**
   *
   * @returns {BasinRequestTable.buildActions.result}
   */
  function buildActions(){
    var result = null;

    if (!empty(self.listeners)) {
      var nonActions = ["onDraw"];
      var action,actionName,actionLabel;
      var actions=[],iconstyles;
      for (var key in self.listeners) {
        if ((self.listeners.hasOwnProperty(key))
            && (typeof self.listeners[key] == "function")
            && (/on[\w]+/i.test(key))
            && (nonActions.indexOf(key) == -1)) {
          actionName=key.replace(/^on/i,"");
          actionLabel=actionName.match(/[A-Z][a-z]+/g).join(" ");
          if ((self.actionIcons) && (self.actionIcons[key])) {
            iconstyles = "style='background-image: url(\"" + self.actionIcons[key]+"\");"
                    +"background-position: 5px center;"
                    +"background-repeat: no-repeat;"
                    +"background-size: 15px 15px;"
                    +"padding-left: 25px;'";
          }
          else {
            iconstyles = "";
          }
          action="<a " + iconstyles + " href='javascript:void(0);' action='" + actionName.toLowerCase() + "'>";
          action += actionLabel + "</a>";
          actions.push(action);
        }
      }

      if (actions.length > 0) {

        var content = null;
        if (actions.length == 1) {
          content = actions[0];
        }
        else  {
          content = "<ul class='menu'>\
                        <li><span class='actionmenu' style=''></span>\
                          <ul><li>" + actions.join("</li><li>") + "</li></ul>\
                         </li>\
                      </ul>";
        }

        result = {
          data:null
          ,defaultContent:content
          ,orderable:false
        };
      }
    }

    return result;
  }

  /**
   *
   * @returns {BasinRequestTable}
   */
  function initActions(){
    if ((!empty(self.dataTable))
        && (!empty(self.listeners))) {
      var action,actionName;
      for (var key in self.listeners) {
        if ((self.listeners.hasOwnProperty(key))
            && (typeof self.listeners[key] == "function")
            && (/on[\w]+/i.test(key))) {
          actionName=key.replace(/^on/i,"");
          action="a[action='" + actionName.toLowerCase() + "']";
          var callback = (function(key){
            return function(evt) {
              actionHandler.call(this,evt,self.listeners[key]);
            }
          })(key);
          self.dataTable.on("click",action,callback);
        }
      }
    }

    return self;
  }

  /**
   *
   * @returns {MyDataTable}
   */
  function renderHelpTips() {
    if (empty(self.dtConfig) || (empty(self.dtConfig.columns))
     || (!(self.dtConfig.columns instanceof Array))
     || (!empty($("img#help-tip-hover")))) {
      return self;
    }
    var columns = self.dtConfig.columns;
    var colList = "";
    var th;
    for (var i=0;i<columns.length;++i){
      if (columns[i].searchable) {
        th = null;
        if (!empty(th = self.tableEl.find("thead th:nth-child(" + (i+1) + ")"))) {
          colList += "<li>" + th.text() + "</li>";
        }
      }
    }

    if(!empty(colList)){
      var searchEl = null;
      if (empty(searchEl = self.tableEl.parent().find("input[type=search]").parent())) {
        return self;
      }
      var label = $("<img/>")
                  .attr({
                    "class":"help"
                    ,id:"help-tip-hover"
                    ,title:""
                    ,style:"margin-left:5px;"
                    ,src:window.SERVER_ROOT+"/resources/images/icons/help.png"})
                  .insertAfter(searchEl);
      var msg = "<span style='font-size:10pt;'>Search typed keywords in the following columns:</span>"
                +"<ul class='tooltip' style='list-style-type:circle;font-size:10pt;padding-left:15px;font-weight:bold;'>"
                + colList + "</ul>";
      label.tooltip({
        content: msg
      });
    }

    return self;
  }

  /**
   *
   * @returns {MyDataTable}
   */
  function renderActionMenu(){
    self.actionMenu = null;
    if (!empty(self.actionMenu = self.tableEl.find("ul.menu"))) {
      self
      .actionMenu
      .children("li")
      .each(function(){
        $(this)
        .mouseenter(function(){
          $(this).children("ul").show().css({display:"inline-block"});
        })
        .mouseleave(function(){
          $(this).children("ul").hide();
        })
        .children("ul").hide();
      });
    }
    return self;
  }

  /**
   *
   * @returns {MyDataTable}
   */
  function onDraw(){
    renderActionMenu();
    renderHelpTips();
    if (typeof self.listeners.onDraw == "function") {
      self.listeners.onDraw.apply(this, arguments);
    }
    return self;
  }

  /**
   *
   * @param {type} cfg
   * @returns {BasinRequestTable.self|BasinRequestTable}
   */
  function init(cfg){
    if (empty(cfg)) {
      throw "The config in self.init(cfg) parameter cannot be unassigned!";
    }
    if (empty(cfg.id)) {
      throw "The table id cannot be unassigned!";
    }
    if (empty(cfg.url)) {
      throw "The config url cannot be unassigned!";
    }
    if (empty(cfg.columns)) {
      throw "The column configs cannot be unassigned!";
    }
    self.actionIcons = cfg.actionIcons || {};
    delete cfg.actionIcons;
    self.listeners = cfg.listeners || {};
    delete cfg.listeners;

    var columns = cfg.columns;
    var actionColumn = null;
    if ((actionColumn = buildActions())) {
      columns.push(actionColumn);
    }
    delete cfg.columns;

    self.listeners.beforeQuery = function(){};

    if(!empty(cfg.events)) {
      self.listeners.beforeQuery = cfg.events.preInit || cfg.events.preXhr || self.listeners.beforeQuery;

      delete cfg.events;
    }

    self.tableEl = null;

    if ((self.tableEl = $("table#" + cfg.id)).length == 0){
      throw "The table id element does not exists!";
    }
    self.dtConfig = {
      processing: true
      ,serverSide: cfg.serverSide ? true : false
      ,ajax: cfg.url
      ,bRetrieve:true
      ,pageLength:cfg.pageSize || 25
      ,columns: columns
      ,dom: cfg.dom || ""
      ,columnDefs: cfg.columnDefs || []
      ,buttons: cfg.buttons || []
      ,select: cfg.select || {}
      ,paging:typeof cfg.paging == "undefined" || cfg.paging
      ,searching:typeof cfg.searching == "undefined" || cfg.searching
      ,lengthChange:typeof cfg.pageSelect == "undefined" || cfg.paging || cfg.pageSelect
    };

    if ((self.dtConfig.buttons) && (!self.dtConfig.dom)) {
      self.dtConfig.dom="Bfrtip";
    }

    if (!empty(cfg.defaultSort)) {
      self.dtConfig.order = cfg.defaultSort;
      delete cfg.defaultSort;
    }

    delete cfg.buttons;
    delete cfg.url;
    delete cfg.pageSize;
    delete cfg.paging;
    delete cfg.columnDefs;
    delete cfg.searching;

    self.options = cfg;

    $.fn.dataTable.ext.errMode = 'none';
    self.dataTable = self.tableEl.DataTable(self.dtConfig);
    self.dataTable.on("error.dt",self.errorHandler);
    self.dataTable.on("preXhr.dt",self.listeners.beforeQuery);
    self.dataTable.on("preInit.dt",self.listeners.beforeQuery);
    self.dataTable.on("draw.dt",onDraw);

    initActions();

    return self;
  }
  //</editor-fold>


  //<editor-fold defaultstate="collapsed" desc="Public Functions">
  /**
   *
   * @param {type} evt
   * @param {type} settings
   * @param {type} technote
   * @param {type} msg
   */
  self.errorHandler = function(evt,settings,technote,msg){
    // var tableEl = this;
  }

  /**
   *
   * @returns {MyDataTable}
   */
  self.refresh=function(){
    if (!empty(self.dataTable)) {
      self.dataTable.draw(false);
    }
    return self;
  }
  //</editor-fold>


  /**
   * initialize the configs
   */
  init(cfg);
}