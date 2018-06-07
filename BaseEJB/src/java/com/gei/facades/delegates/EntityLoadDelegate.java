package com.gei.facades.delegates;

import com.gei.util.EntityUtil;
import java.sql.ResultSet;

/**
 *
 * @author clay
 */
public class EntityLoadDelegate extends QueryDelegate {

  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private final String mColumnPrefix;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public EntityLoadDelegate(Object listener) {
    this(listener,null);
  }
  
  public EntityLoadDelegate(Object listener, String colPrefix) {
    super(listener);
    this.mColumnPrefix = colPrefix;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Query Delegate Implementations">
  @Override
  public void handle(ResultSet rs) throws Exception {
    if (rs.next()) {
      EntityUtil.Load(rs, this.getListener(), this.mColumnPrefix);
    }
  }  
  //</editor-fold>
}
