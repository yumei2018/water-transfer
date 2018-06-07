package gov.ca.water.common.events;

/**
 * An EventInfo extension to handle the canceling of a Cancel Request Event.
 * Used by the {@linkplain CancelEventHandler} when firing an event.
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class CancelEventInfo extends EventInfo {
  
  //<editor-fold defaultstate="collapsed" desc="private Fields">
  /**
   * Placeholder for the cancellation message
   */
  private String cancelMsg;
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public CancelEventInfo() {
    super();    
    this.cancelMsg = null;
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Public methods">
  /**
   * Called to cancel the event's request. If not yet handled, the message will be
   * assigned and this.isHandled will be set.
   * @param cancelMsg the reason for the cancellation.
   */
  public void cancel(String cancelMsg) {
    if (!this.isHandled()) {
      this.cancelMsg = cancelMsg;
      this.setHandled();
    }
  }
  
  /**
   * Get the EventInfo's Canceled state.
   * @return this.isHandled.
   */
  public final boolean isCanceled() {
    return this.isHandled();
  }
  
  /**
   * Get the reason for the cancellation
   * @return the assigned message (or "Canceled for an unknown reason." if unassigned).
   */
  public final String getMessage() {
    return (this.cancelMsg == null)? "Canceled for an unknown reason.": this.cancelMsg;
  }
  //</editor-fold>  
}
