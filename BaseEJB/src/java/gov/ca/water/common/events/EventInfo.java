package gov.ca.water.common.events;

import java.io.Serializable;

/**
 * A EventInfo is the package of information send to listeners when an event is fired.
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class EventInfo implements Serializable {
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * The EventInfo's isHandled state flag
   */
  private Boolean handled;
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public EventInfo() {
    super();    
    this.handled = null;
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Public Properties">
  /**
   * Get the EventInfo's isHandled state (default = false)
   * @return the current state
   */
  public boolean isHandled() {
    return ((this.handled != null) && (this.handled));
  }
  
  /**
   * Called by inheritors to set the IsHandled State - is can only be turned on
   */
  protected void setHandled() {
    this.handled = true;
  }
  //</editor-fold>
}
