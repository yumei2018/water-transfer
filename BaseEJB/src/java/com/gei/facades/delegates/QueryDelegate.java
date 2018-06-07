package com.gei.facades.delegates;

import java.sql.ResultSet;

/**
 *
 * @author clay
 */
public abstract class QueryDelegate {
  
  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private final Object mListener;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public QueryDelegate(Object listener) {
    this.mListener = listener;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Abstract Handler">
  public abstract void handle(ResultSet rs) throws Exception;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Listener">
  public <O extends Object> O getListener() {
    return (O)this.mListener;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="To Camel Case">
  public String toCamelCase(String name) {
    String camelName = "";

    for (String n : name.split("_"))
    {
      if (camelName.isEmpty())
      {
        camelName = n.toLowerCase();
      }
      else
      {
        camelName += n.substring(0,1).toUpperCase() + n.substring(1).toLowerCase();
      }
    }

    return camelName;
  }
  //</editor-fold>
}
