package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtChecklist;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author soi
 */
public class ChecklistCollection extends EntityCollection<WtChecklist> {
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public ChecklistCollection() {
    super(WtChecklist.class);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Checklist">
  public WtChecklist getChecklist(Integer checklistId) {
    WtChecklist result = null;
    
    if (!this.isEmpty()) {
      for (WtChecklist cl : this) {
        if (Objects.equals(cl.getWtChecklistId(), checklistId)) {
          result = cl;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
}
