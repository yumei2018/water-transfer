
package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.WtReviewNote;
import com.gei.entities.collection.EntityCollection;
import java.util.*;
import org.springframework.util.StringUtils;

/**
 *
 * @author ymei
 */
public class ReviewNoteCollection extends EntityCollection<WtReviewNote>{

  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public ReviewNoteCollection() {
    super(WtReviewNote.class);
  }
  //</editor-fold>

  //  //<editor-fold defaultstate="collapsed" desc="Get WtReviewNote">
//  public WtReviewNote getReviewNoteBySectionKey(String key)
//  {
//    WtReviewNote result = null;
//  
//    if(this.size()>0 && !StringUtils.isEmpty(key)){
//      for (WtReviewNote note : this) {
//        if (Objects.equals(note.getSectionKey(), key)) {
//          result = note;
//          break;
//        }
//      }
//    }
//    
//    return result;
//  }
//  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Last WtReviewNote">
  public WtReviewNote getLastReviewNoteBySectionKey(String key)
  {
    WtReviewNote result = null;
  
    if(this.size()>0 && !StringUtils.isEmpty(key)){
      for (WtReviewNote note : this) {
        if (Objects.equals(note.getSectionKey(), key) 
                && (result == null || note.getWtReviewNoteId()>result.getWtReviewNoteId())) {
          result = note;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get WtReviewNotes">
  public List<WtReviewNote> getReviewNotesBySectionKey(String key)
  {
    List<WtReviewNote> result = new ArrayList<>();
  
    if(this.size()>0 && !StringUtils.isEmpty(key)){
      for (WtReviewNote note : this) {
        if (Objects.equals(note.getSectionKey(), key)) {
          result.add(note);
        }
      }
    }
    
    Comparator<WtReviewNote> comparator = new Comparator<WtReviewNote>() {
      @Override
      public int compare(WtReviewNote left, WtReviewNote right) {
        return right.getWtReviewNoteId() - left.getWtReviewNoteId(); // Compare ID
      }
    };
    
    if (result.size() > 1){
      Collections.sort(result, comparator);
    }
    
    return result;
  }
  //</editor-fold>
}
