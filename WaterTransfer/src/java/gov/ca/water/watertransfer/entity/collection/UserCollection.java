package gov.ca.water.watertransfer.entity.collection;

import com.gei.entities.AppUser;
import com.gei.entities.collection.EntityCollection;
import java.util.Objects;

/**
 *
 * @author ymei
 */
public class UserCollection extends EntityCollection<AppUser>{

  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public UserCollection() {
    super(AppUser.class);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get App User">
  public AppUser getAppUser(Integer userId) {
    AppUser result = null;
    
    if (!this.isEmpty() && (userId != null)) {
      for (AppUser a : this) {
        if (Objects.equals(a.getUserId(), userId)) {
          result = a;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
}
