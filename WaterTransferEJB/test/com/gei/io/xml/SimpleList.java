/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.io.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.gei.io.xml.JaxbListWrapper;

/**
 *
 * @author kprins
 */
@XmlRootElement
public class SimpleList extends JaxbListWrapper<SimpleObject> {
    
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public SimpleList() {
    super();    
  }
  
  public SimpleList(List<SimpleObject> list) {
    super(list);   
  }
  // </editor-fold>

  @XmlElement(name="item", type=SimpleObject.class)
  @Override
  public List<SimpleObject> getList() {
    return super.getList();
  }

  @Override
  protected void setList(List<SimpleObject> wrapList) {
    super.setList(wrapList);
  }
  
  
}
