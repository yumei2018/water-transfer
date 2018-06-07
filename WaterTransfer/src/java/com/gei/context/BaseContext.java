package com.gei.context;

import com.gei.entities.AbstractEntity;
import com.gei.facades.AbstractFacade;
import com.gei.util.web.WebUtil;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public abstract class BaseContext<E extends AbstractEntity> {
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private final String SESSION_KEY="ENTITIES"
                        ,ACTIVE_KEY="ACTIVE";
  private String errors="";
  private Class<E> entityClass;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public BaseContext(Class<E> entityClass){
    this.entityClass = entityClass;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Private/Protected Methods">
  protected abstract String buildActiveKey();
  protected <E extends AbstractEntity> String buildActiveKey(Class<E> cls){
    String key = null;
    try {
      key = String.format("%s[%s]",cls.getSimpleName(),ACTIVE_KEY);
    } catch (Exception ex) {
      errors += new java.util.Date() + " -- " + ex.getMessage() + "\n";
    }
    return key;
  }
  
  private Map<Object,E> getMap(){
    Map<Object,E> map=null;
    try {
      if (WebUtil.getRequest() != null){
        if (WebUtil.getRequest().getSession() != null){
          if (WebUtil.getRequest().getSession().getAttribute(SESSION_KEY) == null){
            WebUtil.getRequest().getSession().setAttribute(SESSION_KEY, new LinkedHashMap<Object,E>());
          }
        }
      }
        
      map = (Map<Object, E>) WebUtil.getRequest().getSession().getAttribute(SESSION_KEY);
    }
    catch(Exception ex){
      errors += new java.util.Date() + " -- " + ex.getMessage() + "\n";
    }
    finally{
      return map;
    }
  }
  
  private <BC extends BaseContext> BC add(E entity){
    getMap().put(entity.getId(), entity);
    return (BC) this;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public E find(Integer id){
    E entity = null;
    
    try {
      if (getMap() != null){
        entity = getMap().get(id);
      }
      
      if (entity == null){
        entity = (E) getFacade().find(id);
      }
    }
    catch(Exception ex){
      errors += new java.util.Date() + " -- " + ex.getMessage() + "\n";
    }
    finally {
      return entity;
    }
  }
  
  public <BC extends BaseContext, E extends AbstractEntity> BC setActive(E entity){
    try {
      String formattedKey=buildActiveKey();
      
      WebUtil
      .getRequest()
      .getSession()
      .setAttribute(formattedKey, entity);
    }
    catch(Exception ex){
      errors += new java.util.Date() + " -- " + ex.getMessage() + "\n";
    }
    finally {
      return (BC) this;
    }
  }
  
  public E getActive(){
    E entity = null;
    
    try {
      String formattedKey=buildActiveKey();
      
      entity = (E) WebUtil
                  .getRequest()
                  .getSession()
                  .getAttribute(formattedKey);
    }
    catch(Exception ex){
      
    }
    finally {
      return entity;
    }
  }
  public String getErrors(){return errors;}
  public abstract <F extends AbstractFacade> F getFacade();
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc}
   * <p>OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return super.toString();
  }
  // </editor-fold>
}
