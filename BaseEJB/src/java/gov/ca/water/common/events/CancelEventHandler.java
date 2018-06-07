package gov.ca.water.common.events;

/**
 * A base Cancel request EventHandler used send a request to listener for permission to
 * proceed with process. It uses the {@linkplain CancelEventInfo}, which allows listeners
 * to cancel the request and provide a reason for the cancellation.
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class CancelEventHandler extends EventHandlerBase<CancelEventInfo>{
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public CancelEventHandler() {
    super();    
  }
  // </editor-fold>
}
