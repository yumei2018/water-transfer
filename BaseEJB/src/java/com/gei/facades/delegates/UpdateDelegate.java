/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades.delegates;

import java.sql.ResultSet;

/**
 *
 * @author soi
 */
public abstract class UpdateDelegate extends QueryDelegate {

  /**
   * 
   * @param listener 
   */
  public UpdateDelegate(Object listener) {
    super(listener);
  }
  
  /**
   * 
   * @param o
   * @return
   * @throws Exception 
   */
  public abstract void handle(Integer i) throws Exception;
  
  //<editor-fold defaultstate="collapsed" desc="QueryDelegate Implementations">
  public void handler(ResultSet rs) throws Exception {
    throw new Exception("The Update Delegate does not support handling result set!");
  }
  //</editor-fold>
}
