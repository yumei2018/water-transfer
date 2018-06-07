package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtWell;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author soi
 */
public class WellCollection extends EntityCollection<WtWell> {

  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public WellCollection() {
    super(WtWell.class);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Well">
  public WtWell getWell(String wellNum) {
    WtWell result = null;

    if (!this.isEmpty()) {
      for (WtWell w : this) {
        if (Objects.equals(w.getWtWellNum(), wellNum)) {
          result = w;
          break;
        }
      }
    }

    return result;
  }
  //</editor-fold>

}
