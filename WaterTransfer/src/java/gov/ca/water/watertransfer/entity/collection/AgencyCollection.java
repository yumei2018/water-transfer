package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtAgency;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author clay
 */
public class AgencyCollection extends EntityCollection<WtAgency>{
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public AgencyCollection() {
    super(WtAgency.class);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Agency">
  public WtAgency getAgency(Integer agencyId) {
    WtAgency result = null;
    
    if (!this.isEmpty() && (agencyId != null)) {
      for (WtAgency a : this) {
        if (Objects.equals(a.getWtAgencyId(), agencyId)) {
          result = a;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
}
