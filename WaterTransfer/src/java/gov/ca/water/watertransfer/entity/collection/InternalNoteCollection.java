package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtInternalNote;
import com.gei.entities.collection.EntityCollection;
import java.util.List;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 *
 * @author ymei
 */
public class InternalNoteCollection extends EntityCollection<WtInternalNote>{
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public InternalNoteCollection() {
    super(WtInternalNote.class);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get WtReviewNote">
  public List<WtInternalNote> getWtInternalNotesBySectionKey(String key)
  {
    List<WtInternalNote> result = null;
  
    if(this.size()>0 && !StringUtils.isEmpty(key)){
      for (WtInternalNote note : this) {
        if (Objects.equals(note.getSectionKey(), key)) {
          result.add(note);
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
}
