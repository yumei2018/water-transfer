package com.gei.io.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.*;
import com.gei.io.DataEntry;

/**
 *
 * @author kprins
 */
@XmlRootElement(name="MapOwner")
public class TestMapOwner implements Serializable {
  
  //<editor-fold defaultstate="collapsed" desc="Static Class TestMapEntry">
  /**
   * Public static Class for XML Serialization
   */
  public static class TestMapEntry {
    @XmlAttribute
    public String key;
    @XmlElement
    public SecondClass value;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  private HashMap<String,SecondClass> objMap;
  private String objName;
  private SimpleObject objInst;
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public TestMapOwner() {
    this.objName = null;
    this.objMap = null;
    this.objInst = null;
  }
  
  @SuppressWarnings("unchecked")
  public TestMapOwner(String objName) {
    this();
    if (objName == null) {
      throw new NullPointerException("The Element's Object Name cannot be unassigned");
    }
    this.objMap = null;    
    this.objName = objName;
    this.objInst = null;
  }
  // </editor-fold>
  
    /**
   * Get the wrapped map as a list of JaxbMapEntries
   * @return the JaxbMapEntry list
   */
  @XmlElementWrapper(name="ObjMapEntries")
  @XmlElement(name="entry", type=TestMapOwner.TestMapEntry.class)
  public List<TestMapOwner.TestMapEntry> getMapEntries() {
    List<TestMapOwner.TestMapEntry> result = new ArrayList<>();
    if ((this.objMap != null) && (!this.objMap.isEmpty())) {
      for (Map.Entry<String, SecondClass> entry : this.objMap.entrySet()) {
        TestMapOwner.TestMapEntry mapEntry = new TestMapOwner.TestMapEntry();
        mapEntry.key = entry.getKey();
        mapEntry.value = entry.getValue();
        result.add(mapEntry);
      }
    }
    return result;
   }
  
  /**
   * Assign the wrapped map's entries
   * @param mapEntries the new list to JaxbMapEntries.
   */
  protected void setMapEntries(List<TestMapOwner.TestMapEntry> mapEntries) {
    this.objMap = null;
    if ((mapEntries != null) && (!mapEntries.isEmpty())) {
      this.objMap = new HashMap<>();
      for (TestMapOwner.TestMapEntry mapEntry : mapEntries) {
        if (mapEntry != null) {
          SecondClass value = (mapEntry.value == null)? null: (SecondClass)mapEntry.value;
          this.objMap.put(mapEntry.key, value);
        }
      }
    }
  }

  //<editor-fold defaultstate="collapsed" desc="HashMap Delegates">
  public int size() {
    return (this.objMap == null)? 0: this.objMap.size();
  }
  
  public SecondClass get(String key) {
    return ((this.objMap == null) || (!this.objMap.containsKey(key)))? null:
            objMap.get(key);
  }
  
  public SecondClass put(String key, SecondClass value) {
    SecondClass result = null;
    key = DataEntry.cleanString(key);
    if (key != null) {
      if (value == null) {
        if ((this.objMap != null) && (this.objMap.containsKey(key))) {
          this.objMap.remove(key);
        }
      } else {
        if (this.objMap == null) {
          this.objMap = new HashMap<>();
        }
        result = this.objMap.put(key, value);
      }
    }
    return result;
  }
  
  public Set<String> keySet() {
    return (this.objMap == null)? null: objMap.keySet();
  }
  
  public Collection<SecondClass> values() {
    return (this.objMap == null)? null: objMap.values();
  }
  
  public void clear() {
    if (this.objMap == null) {
      this.objMap.clear();
      this.objMap = null;
    }
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Properties">
  @XmlElement()
  public String getObjName() {
    return objName;
  }
  
  public void setObjName(String objName) {
    this.objName = objName;
  }
  
  public SimpleObject getObjInst() {
    return objInst;
  }
  
  public void setObjInst(SimpleObject objInst) {
    this.objInst = objInst;
  }
  //</editor-fold>

  @Override
  public String toString() {
    String result = "Name = " + this.objName + "\n";
    result += "Simple = " + this.objInst.toString()+ "\n";
    result += "MapEntries = " + this.objMap.toString();
    return result;
  }
}
