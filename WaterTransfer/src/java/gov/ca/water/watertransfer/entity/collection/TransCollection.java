package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtTrans;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author ymei
 */
public class TransCollection extends EntityCollection<WtTrans>{

  // <editor-fold defaultstate="collapsed" desc="Constructor(s)">
  /**
   * Public Constructor  
   */
  public TransCollection() {
    super(WtTrans.class);  
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Get Trans">
  public WtTrans getTrans(Integer transId) {
    WtTrans result = null;
    
    if (!this.isEmpty() && (transId != null)) {
      for (WtTrans wt : this) {
        if (Objects.equals(wt.getWtTransId(), transId)) {
          result = wt;
          break;
        }
      }
    }
    
    return result;
  }
  // </editor-fold>

}
