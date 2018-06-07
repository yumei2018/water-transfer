package com.gei.context;

import com.gei.facades.AbstractFacade;
import com.gei.util.WebUtil;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author clay
 */
public class ConnectionContext {

  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private Connection mConn;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Finalize Override">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    try {
      this.mConn.rollback();
    }
    catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.WARNING
        ,String.format("%s.finalize() %s:\n%s"
          ,this.getClass().getName()
          ,e.getClass().getName()
          ,e.getMessage()
        )
      );
    }
    finally {
      this.mConn = null;
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Static Get Instance">
  public static ConnectionContext getInstance() {
    return WebUtil.getContext(ConnectionContext.class);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Static Get Connection">
  public static <F extends AbstractFacade> Connection getConnection(String dataSourceBeanId) {
    return ConnectionContext.getInstance()._getConnection(dataSourceBeanId);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Private Get Connection">
  private <F extends AbstractFacade> Connection _getConnection(String dataSourceBeanId) {
    try {
      if ((this.mConn == null) || (this.mConn.isClosed())) {
        ApplicationContext appCtx = null;
        if ((appCtx = WebUtil.getApplicationContext()) == null) {
          throw new Exception("The system cannot get the application context!");
        }
        Object bean = null;
        if ((bean = appCtx.getBean(dataSourceBeanId)) == null) {
          throw new Exception("The system cannot get the bean \"" + dataSourceBeanId + "\".");
        }

        if (!(bean instanceof DataSource)) {
          throw new Exception("The bean is not a datasource bean!");
        }

        DataSource ds = (DataSource)bean;

        this.mConn = ds.getConnection();
      }
    }
    catch (Exception ex) {
      throw new IllegalStateException(
        String.format("%s.getConnection(Class<AbstractFacade>) %s:\n%s"
          ,this.getClass().getName()
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return this.mConn;
  }
  //</editor-fold>
}
