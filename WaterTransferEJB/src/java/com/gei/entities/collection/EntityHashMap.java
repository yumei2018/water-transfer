package com.gei.entities.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ymei
 * @param <K, E>
 */
public class EntityHashMap<K extends Object,E extends Object> 
                          extends HashMap<K,E> implements Serializable {

  // <editor-fold defaultstate="collapsed" desc="Private Properties">
  private HashMap<K,E> eHashMap;
  private Class<E> eClass;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor(s)">
  /**
   * Public Constructor  
   */
  public EntityHashMap(Class<E> eClass) {
    super();  
    this.eHashMap = new HashMap();
    this.eClass = eClass;
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Finalize Override">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    this.eHashMap.clear();
    this.eHashMap = null;
    this.eClass = null;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get HashMap">
  public HashMap<K,E> getHashMap() {
    return this.eHashMap;
  }
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc}
   * <p>OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return super.toString();
  }
  
  @Override
  public int size() {
    return this.eHashMap.size();
  }
  
  @Override
  public boolean isEmpty() {
    return this.eHashMap.isEmpty();
  }
  
  @Override
  public boolean containsKey(Object k) {
    return this.eHashMap.containsKey(k);
  }
  
  @Override
  public boolean containsValue(Object v) {
    return this.eHashMap.containsValue(v);
  }
  
  @Override
  public E put(K k, E e) {
    return this.eHashMap.put(k, e);
  }
  
  @Override
  public void putAll(Map<? extends K, ? extends E> m){
    this.eHashMap.putAll(m);
  }
  
  @Override
  public E remove(Object k) {
    return this.eHashMap.remove(k);
  }
  
  @Override
  public E get(Object k) {
    return this.eHashMap.get(k);
  }
  
  @Override
  public void clear() {
    this.eHashMap.clear();
  }
  // </editor-fold>
}
