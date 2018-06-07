/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades.delegates;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public abstract class LoadQueryDelegate<TEntity extends Serializable> implements Serializable {

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private Object listener;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public LoadQueryDelegate(Object listener) {
    super();  
    this.listener = listener;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Private/Protected Methods">
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public Object getListener() {
    return this.listener;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Abstact Methods">
  /**
   * 
   * @param rs
   * @param results
   * @throws SQLException 
   */
  public abstract void loadQuery(ResultSet rs, List<Map> results) throws SQLException;
  // </editor-fold>
}
