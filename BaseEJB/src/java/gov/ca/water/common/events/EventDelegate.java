package gov.ca.water.common.events;

/**
 * An EventDelegate for the base EventInfo
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public abstract class EventDelegate extends EventDelegateBase<EventInfo>{
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public EventDelegate(Object listener) {
    super(listener);  
  }
  // </editor-fold>
}
