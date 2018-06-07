
import com.gei.entities.WtAttachment;
import com.gei.entities.WtTrans;
import com.gei.facades.WtAttachmentFacade;
import com.gei.facades.WtAttachmentWithFileFacade;
import com.gei.facades.WtTransFacade;
import com.gei.facades.delegates.LoadQueryDelegate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author clay
 */
public class CrudTest {

  @Test
  public void t1(){
    WtAttachmentFacade f = new WtAttachmentFacade();
    WtAttachment a = new WtAttachment(314);
    a = f.find(a);
    System.out.println(a.toMap());
    System.out.println(a.getReadableFilesize());
  }
  
  @Test
  public void updateAttachmentsFilesize() throws Exception {
    WtAttachmentWithFileFacade f = new WtAttachmentWithFileFacade();
    List<Map> attachments = f.select("SELECT * FROM WT_ATTACHMENT WHERE FILE_SIZE IS NULL",new LoadQueryDelegate(this) {
      @Override
      public void loadQuery(ResultSet rs, List list) throws SQLException {
        Map m = null;
        byte[] bytes = null;
        while (rs.next()) {
          m = new HashMap<>();
          m.put("wtAttachmentId", rs.getInt("WT_ATTACHMENT_ID"));
          bytes = rs.getBytes("FILE_LOB");
          m.put("fileSize", bytes == null ? 0 : bytes.length);
          list.add(m);
        }
      }
    });
    if (attachments != null) {
      Integer fileSize = null;
      Integer attId = null;
      
      for (Map att : attachments) {
        fileSize = (Integer) att.get("fileSize");
        attId = (Integer) att.get("wtAttachmentId");
        f.executeUpdate("UPDATE WT_ATTACHMENT SET FILE_SIZE = ? WHERE WT_ATTACHMENT_ID = ?"
          , new Object[]{fileSize, attId});
      }
    }
  }
  
  @Test
  public void t2(){
    System.out.println(new java.util.Date());
    WtTransFacade f = new WtTransFacade();
    WtTrans wt = f.find(325);
    
    System.out.println(wt.getWtReservoir());
    System.out.println(wt.getWtReservoir().getWtAttachmentCollection().size());
    System.out.println(new java.util.Date());
  }
  
  @Test
  public void t3(){
    System.out.println(new java.util.Date());
    WtTrans wt = new WtTrans(325);
//    System.out.println(wt.getWtStatusFlag().toMap());
    System.out.println(wt.getWtAttachmentCollection().size());
//    System.out.println(wt.getWtGroundwater());
//    System.out.println(wt.getWtGroundwater().getWtAttachmentCollection());
//    System.out.println(wt.getWtReportCollection());
//    System.out.println(wt.getWtCropIdling());
//    System.out.println(wt.getWtCropIdling().getWtAttachmentCollection());
//    System.out.println(wt.getWtReservoir());
//    System.out.println(wt.getWtReservoir().getWtAttachmentCollection().size());
    System.out.println(new java.util.Date());
  }
}
