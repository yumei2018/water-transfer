/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.io.xml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlType;

/**
 * Typical Class for XML Serialization - used by the unit test
 * @author kprins
 */
@XmlRootElement(name="Simple")
//@XmlType(name="Simple")
public class SimpleObject implements Serializable {
  
  @XmlElement(name="name")
  private String name;
  private int length;
  private double value;
  private SubObject subObject;
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public SimpleObject() {
    super();    
    this.name = null;
    this.length = 0;
    this.value = Double.NaN;
    this.subObject = null;
  }
  
  public SimpleObject(String name) {
    this();
    this.name = name;
  }
  // </editor-fold>

//  public String getName() {
//    return name;
//  }

//  public void setName(String name) {
//    this.name = name;
//  }

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

  public SubObject getSubObject() {
    return subObject;
  }

  public void setSubObject(SubObject subObject) {
    this.subObject = subObject;
  }

  @Override
  public String toString() {
    String result = this.name + ";" + this.length + ";" + this.value;
    if (this.subObject != null) {
      result += ";" + this.subObject.toString();
    }
    return result;
  }
  
  
}
