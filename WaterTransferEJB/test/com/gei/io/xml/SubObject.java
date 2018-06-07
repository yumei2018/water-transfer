/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.io.xml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kprins
 */
//@XmlRootElement
public class SubObject implements Serializable {
  
  private String name;
  private int length;
  private double value;
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public SubObject() {
    super();    
    this.name = null;
    this.length = 0;
    this.value = Double.NaN;
  }
  // </editor-fold>

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.name + ";" + this.length + ";" + this.value;
  }
  
  
}
