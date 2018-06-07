package com.gei.io.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Typical Class for XML Serialization - used by the unit test
 * @author kprins
 */
@XmlRootElement(name="second")
public class SecondClass extends SimpleObject{
  
  @XmlElement(name="city") 
  private String objCity;
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public SecondClass() {
    super();    
    this.objCity = null;
  }
  
  public SecondClass(String sName, String city) {
    super(sName);
    this.objCity = city;
  }
  // </editor-fold>

  public String getObjCity() {
    return objCity;
  }

  @Override
  public String toString() {
    String result = super.toString();
    result += ";" + this.objCity;
    return result;
  }
  
  
}
