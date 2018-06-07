/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.io.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kprins
 */
public class SimpleAdapter extends XmlAdapter<Object, Object> {
 
  @Override
  public Object unmarshal(Object v) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object marshal(Object v) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
