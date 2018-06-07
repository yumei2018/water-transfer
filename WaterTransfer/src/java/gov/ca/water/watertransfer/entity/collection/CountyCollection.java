package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtCounty;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author ymei
 */
public class CountyCollection extends EntityCollection<WtCounty>{
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public CountyCollection() {
    super(WtCounty.class);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get County">
  public WtCounty getCounty(Integer countyId) {
    WtCounty result = null;
    
    if (!this.isEmpty()) {
      for (WtCounty ct : this) {
        if (Objects.equals(ct.getWtCountyId(), countyId)) {
          result = ct;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
}
