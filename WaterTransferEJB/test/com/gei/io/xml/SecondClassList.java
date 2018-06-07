/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.io.xml;

import java.util.List;

/**
 *
 * @author kprins
 */
//@XmlRootElement
public class SecondClassList extends JaxbListWrapper<SecondClass> {
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public SecondClassList() {
    super();    
  }
  
  public SecondClassList(List<SecondClass> list) {
    super(list);   
  }
  // </editor-fold> 

  //@XmlElement(name="item", type=SecondClass.class)
//  @Override
//  public List<SecondClass> getList() {
//    return super.getList();
//  }

//  @Override
//  public void setList(List<SecondClass> list) {
//    super.setList(list);
//  }
}
