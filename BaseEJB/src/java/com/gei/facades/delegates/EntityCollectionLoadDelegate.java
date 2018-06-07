package com.gei.facades.delegates;

import com.gei.util.EntityUtil;
import java.sql.ResultSet;
import java.util.Collection;

/**
 *
 * @author clay
 */
public class EntityCollectionLoadDelegate extends QueryDelegate {
  
  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private final Class mClass;
  private final String mColumnPrefix;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  /**
   *
   * @param listener
   */
  public EntityCollectionLoadDelegate(Object listener) {
    super(listener);
    throw new IllegalStateException("Please use the other constructor(Object,CLass)!");
  }
    
  public <L extends Collection> EntityCollectionLoadDelegate(L listener, Class eClass) {
    this(listener,eClass,null);
  }
    
  public <L extends Collection> EntityCollectionLoadDelegate(L listener, Class eClass, String colPrefix) {
    super(listener);
    this.mClass = eClass;
    this.mColumnPrefix = colPrefix;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Query Delegate Implementation">
  @Override
  public void handle(ResultSet rs) throws Exception {
    Collection c = this.getListener();
    Object entity = null;
    while (rs.next()) {
      entity = this.mClass.newInstance();
      EntityUtil.Load(rs, entity, this.mColumnPrefix);
      c.add(entity);
    }
  }
  //</editor-fold>
}
