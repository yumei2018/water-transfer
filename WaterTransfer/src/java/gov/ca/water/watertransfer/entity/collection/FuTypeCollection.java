package gov.ca.water.watertransfer.entity.collection;

import com.gei.collection.BaseCollection;
import com.gei.entities.WtFuType;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author clay
 */
public class FuTypeCollection extends EntityCollection<WtFuType> {
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public FuTypeCollection() {
    super(WtFuType.class);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get WtFuType">
  public WtFuType getWtFuType(Integer wtFuTypeId) {
    WtFuType result = null;
  
    if (!this.isEmpty() && (wtFuTypeId != null)) {
      for (WtFuType ft : this) {
        if (Objects.equals(ft.getWtFuTypeId(), wtFuTypeId)) {
          result = ft;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
  
}
