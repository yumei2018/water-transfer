package Testing.Package;

import com.sun.mail.smtp.SMTPAddressFailedException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;

/**
 *
 * @author ymei
 */
public class MyTest {
  @Test
  public void emailTest(){
    try {
      HtmlEmail email = new HtmlEmail();
      
//      email.addTo("geideveloper@geiconsultants.com");      
//      email.setHostName("geiconsultants-com.mail.protection.outlook.com");
      email.setHostName("us-smtp-outbound-1.mimecast.com");	
      email.setSendPartial(true);
      email.setDebug(true);
      
//      email.setFrom("no-reply@water.ca.gov");
      email.setFrom("no-reply@geiconsultants.com");
      email.addTo("ymei@geiconsultants.com");
      email.addCc("maywhite@yahoo.com");
      email.setSubject("Hello,Test");
      email.setHtmlMsg("world!");
//      email.setFrom("geideveloper@geiconsultants.com");      
      
      email.send();
    } catch (EmailException ex) {
      if ((ex.getCause() == null)
        || !(ex.getCause().getCause() instanceof SMTPAddressFailedException)) {
        ex.printStackTrace();
      }
    }
  }
}
