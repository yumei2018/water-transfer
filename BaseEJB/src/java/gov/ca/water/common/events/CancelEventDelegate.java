package gov.ca.water.common.events;

/**
 * An abstract class for handling Cancel events
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public abstract class CancelEventDelegate extends EventDelegateBase<CancelEventInfo> {

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  protected CancelEventDelegate(Object listener) {
    super(listener);  
  }
  // </editor-fold>
}
