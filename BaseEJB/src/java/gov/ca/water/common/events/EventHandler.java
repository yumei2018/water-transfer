package gov.ca.water.common.events;

/**
 * A Simple EventHandler using the base EventInfo to notify any listener that the event
 * occurred. Since it use the EventInfo class, it sends no additional information, nor
 * can the EventInfo be handled - it is always propagated to all listeners.
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class EventHandler extends EventHandlerBase<EventInfo> {  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public EventHandler() {
    super();    
  }
  // </editor-fold>
}
