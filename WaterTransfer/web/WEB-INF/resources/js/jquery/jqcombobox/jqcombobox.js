/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function( $ ) {
  $.widget( "custom.combobox", {
    _create: function() {
      this.wrapper = $( "<span>" )
      .addClass( "custom-combobox" )
      .insertAfter( this.element );
      this.element.hide();
      this._createAutocomplete();
      this._createShowAllButton();
    }
    ,_createAutocomplete: function() {
      var select = this.element;
      var selected = select.children( ":selected" ),
      value = selected.val() ? selected.text() : "";
      this.input = $( "<input>" )
      .appendTo( this.wrapper )
      .val( value )
      .attr( "title", "" )
      .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
      .autocomplete({
        delay: 0,
        minLength: this.options && this.options.minLength || 0,
        source: $.proxy( this, "_source" )
      })
      .tooltip({
        tooltipClass: "ui-state-highlight"
      });
      this._on( this.input, {
        autocompleteselect: function( event, ui ) {
          ui.item.option.selected = true;
          this._trigger( "select", event, {
            item: ui.item.option
          });
        },
        autocompletechange: "_removeIfInvalid"
      });
    }
    ,_createShowAllButton: function() {
      var input = this.input,
      wasOpen = false;
      $( "<a>" )
      .attr( "tabIndex", -1 )
      .attr( "title", "Show All Items" )
      .tooltip()
      .appendTo( this.wrapper )
      .button({
        icons: {
          primary: "ui-icon-triangle-1-s"
        },
        text: false
      })
      .removeClass( "ui-corner-all" )
      .addClass( "custom-combobox-toggle ui-corner-right" )
      .mousedown(function() {
        wasOpen = input.autocomplete( "widget" ).is( ":visible" );
      })
      .click(function() {
        input.focus();
        // Close if already visible
        if ( wasOpen ) {
          return;
        }
        // Pass empty string as value to search for, displaying all results
        input.autocomplete( "search", "" );
      });
    }
    ,_source: function( request, response ) {
      var combobox = this;
      if ((combobox.options.source) //&& (this.element.children( "option" ).length == 0)
          && (!combobox.options.loading)){
        combobox.options.loading = true;
        $.ajax({
          url:combobox.options.source
          ,data:{q:request.term}
          ,dataType:"json"
          ,success:function(res, jqxhr, opts){
            try{
              if (!res.success) {
                throw res.error || "Combobox fetching data error!";
              }
              combobox.element.children().remove();
              if (typeof combobox.options.sourceHandler == "function"){
                combobox.options.sourceHandler.call(combobox.element, res.data);
                setTimeout(function(){
                  combobox._source(request,response);
                  combobox.options.loading = false;
                },100);
              }
            }
            catch(e){
              alert(e);
            }
          }
        })
        return;
      }
      var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
      response( this.element.children( "option" ).map(function() {
        var text = $( this ).text();
        if ( this.value && ( !request.term || matcher.test(text) ) )
          return {
            label: text,
            value: text,
            option: this
          };
        })
      );
    }
    ,_removeIfInvalid: function( event, ui ) {
      // Selected an item, nothing to do
      if ( ui.item ) {
        return;
      }
      // Search for a match (case-insensitive)
      var value = this.input.val(),
      valueLowerCase = value.toLowerCase(),
      valid = false;
      this.element.children( "option" ).each(function() {
        if ( $( this ).text().toLowerCase() === valueLowerCase ) {
          this.selected = valid = true;
          return false;
        }
      });
      // Found a match, nothing to do
      if ( valid ) {
        return;
      }
      // Remove invalid value
//      this.input
//      .val( "" )
//      .attr( "title", value + " didn't match any item" )
//      .tooltip( "open" );
      this.element.val( "" );
      this._delay(function() {
        this.input.tooltip( "close" ).attr( "title", "" );
      }, 2500 );
      this.input.autocomplete( "instance" ).term = "";
    }
    ,_destroy: function() {
      this.wrapper.remove();
      this.element.show();
    }
  });
})( jQuery );
/*
$(function() {
$( "#combobox" ).combobox();
$( "#toggle" ).click(function() {
$( "#combobox" ).toggle();
});
});
/**/