package com.gei.facades;

import com.gei.facades.delegates.QueryDelegate;
import com.gei.facades.delegates.UpdateDelegate;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.util.StringUtils;

/**
 *
 * @author clay
 */
public class GenericFacade {
  
  //<editor-fold defaultstate="collapsed" desc="Private Static Properties">
  private static Boolean mAutoCommit;
  private static Logger mLogger = Logger.getLogger(GenericFacade.class.getName());
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Override Methods">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    GenericFacade.mAutoCommit = null;
    GenericFacade.mLogger = null;
  }
//</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Query Methods">
  /**
   * 
   */
  public static void beginTransaction(){
    GenericFacade.mAutoCommit = false;
  }
  
  /**
   * 
   * @param <F>
   * @param pstmt
   * @param objects
   * @return
   * @throws SQLException 
   */
  private static void fillStatement(PreparedStatement pstmt,Object... objects) throws SQLException {
    if ((objects != null) && (objects.length > 0) && (pstmt != null)) {
      int i=1;
      Integer sqlType = null;
      for (Object o : objects){
        if (o == null){
          try {
            sqlType = pstmt.getParameterMetaData().getParameterType(i);
          } catch (Exception e) {
            sqlType = Types.VARCHAR;
          }          
          pstmt.setNull(i++, sqlType);
        }
        else {
          pstmt.setObject(i++, o);
        }
      }
    }
  }
  
  /**
   * 
   * @param conn
   * @param query
   * @param values
   * @return 
   */
  public static Boolean executeQuery(Connection conn, String query, List values, QueryDelegate rsh) throws Exception {
    if ((conn == null) || (conn.isClosed())) {
      throw new IllegalArgumentException("The connection parameter cannot be unassigned or closed!");
    }
    if (StringUtils.isEmpty(query)) {
      throw new IllegalArgumentException("The query parameter cannot be unassigned!");
    }
    if (rsh == null){
      throw new IllegalArgumentException("The result set handler parameter cannot be unassigned!");
    }
//    mLogger.log(Level.INFO, query);
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
      if ((values != null) && (values.size() > 0)) {
        GenericFacade.fillStatement(pstmt,values.toArray());
      }
      try(ResultSet rs = pstmt.executeQuery()) {
        rsh.handle(rs);
      }
    }
    
    return true;
  }
  
  /**
   * 
   * @param conn
   * @param query
   * @param values
   * @param rsh
   * @return 
   */
  public static Boolean executeUpdate(Connection conn, String query, List values, UpdateDelegate euh) throws Exception {
    return executeUpdate(conn,query,values,euh,null);
  }
  
  /**
   * 
   * @param conn
   * @param query
   * @param values
   * @param euh
   * @param indexes
   * @param rsh
   * @return 
   */
  public static Boolean executeUpdate(Connection conn, String query, List values
          , UpdateDelegate euh,QueryDelegate delegate,String...indexes) throws Exception {
    Boolean result = false;
    try {
      if ((conn == null) || (conn.isClosed())) {
        throw new IllegalArgumentException("The connection parameter cannot be unassigned or closed!");
      }
      if (StringUtils.isEmpty(query)) {
        throw new IllegalArgumentException("The query parameter cannot be unassigned!");
      }
      if (euh == null){
        throw new IllegalArgumentException("The result set handler parameter cannot be unassigned!");
      }
//      mLogger.log(Level.INFO, query);
      conn.setAutoCommit(false);
      if ((delegate == null) || (indexes == null) || (indexes.length == 0)) {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
          if ((values != null) && (values.size() > 0)) {
            GenericFacade.fillStatement(pstmt,values.toArray());
          }
          euh.handle(pstmt.executeUpdate());
        }
      }
      else {
        if (delegate == null) {
          throw new IllegalArgumentException("The delegate parameter cannot be unassigned!");
        }
        try (PreparedStatement pstmt = conn.prepareStatement(query,indexes)) {
          if ((values != null) && (values.size() > 0)) {
            GenericFacade.fillStatement(pstmt,values.toArray());
          }
          euh.handle(pstmt.executeUpdate());
          try (ResultSet rs = pstmt.getGeneratedKeys()) {
            delegate.handle(rs);
          }
        }
      }
      
      if ((GenericFacade.mAutoCommit == null) 
          || (GenericFacade.mAutoCommit)) {
        conn.commit();
      }
      result = true;
    } catch (Exception e) {
      if (conn != null) {
        conn.rollback();
      }
      throw e;
    }
    
    return result;
  }
  
  public static void resetAutoCommit(){
    GenericFacade.mAutoCommit = true;
  }
  //</editor-fold>
}
