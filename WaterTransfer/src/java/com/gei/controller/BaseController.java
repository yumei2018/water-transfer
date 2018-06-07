package com.gei.controller;

import com.gei.thread.MailerRunnable;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author clay
 */
public class BaseController extends MultiActionController {

  final static String SESSION_KEY_LOGGEDIN = "LOGGED_IN", SESSION_KEY_USER = "USER";
  
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){

          @Override
	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("localhost")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}

  public void info(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Class cls = this.getClass();
    RequestMapping rm = (RequestMapping) cls.getAnnotation(RequestMapping.class);
    String controllerMapPath = cls.getSimpleName().replaceFirst("Controller", "").toLowerCase();
    if (rm != null) {
      if (rm.value().length > 0) {
        controllerMapPath = rm.value()[0].substring(1);
      }
    }

    String html = "", linkTpl = "<a href='%1$s/%2$s/%3$s'>%1$s/%2$s/%3$s</a>";
    for (Method m : cls.getDeclaredMethods()) {
      rm = m.getAnnotation(RequestMapping.class);
      if (rm == null) {
        if (m.getParameterTypes().length == 2
                && m.getParameterTypes()[0].equals(HttpServletRequest.class)
                && m.getParameterTypes()[1].equals(HttpServletResponse.class)) {
          html += (html.isEmpty() ? "" : "<br />") + String.format(linkTpl, request.getContextPath(), controllerMapPath, m.getName());
        }
      } else {
        for (String rmVal : rm.value()) {
          html += (html.isEmpty() ? "" : "<br />") + String.format(linkTpl, request.getContextPath(), controllerMapPath, rmVal.substring(1));
        }
      }
    }

    response.getWriter().write(html);
  }

  public static String md5(String str) {
    String hashtext = "";

    if (null != str && !str.isEmpty()) {
      MessageDigest m = null;

      try {
        m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(str.getBytes("UTF-8"));

        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        hashtext = bigInt.toString(16);

        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
          hashtext = "0" + hashtext;
        }
      } catch (Exception ex) {
      }
    }

    return hashtext;
  }

  protected boolean LoggedInCheck(HttpServletRequest request, HttpServletResponse response) {
    boolean loggedIn = AuthenticationController.IsLoggedIn(request);
    if (!loggedIn) {
      try {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
      } catch (IOException ex) {
        java.util.logging.Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return loggedIn;
  }

  protected boolean LoggedInCheckNoAjax(HttpServletRequest request, HttpServletResponse response) {
    boolean loggedIn = false;
    try {
      loggedIn = AuthenticationController.IsLoggedIn(request);

      if (!loggedIn) {
        response.sendRedirect(request.getContextPath());
      }
    } catch (Exception ex) {
    }
    return loggedIn;
  }

  protected boolean LoggedInCheck(HttpServletRequest request, ModelAndView mv) {
    boolean loggedIn = false;
    try {
      loggedIn = AuthenticationController.IsLoggedIn(request);
      if (!loggedIn) {
        if (mv != null) {
          mv.setViewName("login");
        } else {
          mv = new ModelAndView("login");
        }
      }
    } catch (Exception ex) {
    }
    return loggedIn;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static String getContents(HttpServletRequest request, String urlStr) {
    Logger.getLogger(BaseController.class).log(Logger.Level.INFO, urlStr);
    if (!urlStr.contains("https://") && urlStr.indexOf("http://") == -1) {
      urlStr = String.format("http://%s%s%s", request.getServerName(), request.getServerPort() == 80 ? "" : ":" + request.getServerPort(), urlStr);
    }    
    if (!urlStr.contains("http://") && urlStr.indexOf("https://") == -1) {
      urlStr = String.format("https://%s%s%s", request.getServerName(), request.getServerPort() == 443 ? "" : ":" + request.getServerPort(), urlStr);
    }
    
    // This code is only used in DWR server
//    if (urlStr.contains("http://")) {
//      urlStr = urlStr.replace("http:", "https:");
//    }

    return getContents(urlStr);
  }
  
  public static String getBasicURL(HttpServletRequest request, String resourcePath) {
    int port = request.getServerPort();
    StringBuilder result = new StringBuilder();
    result.append(request.getScheme())
          .append("://")
          .append(request.getServerName());
 
    if ( (request.getScheme().equals("http") && port != 80) || (request.getScheme().equals("https") && port != 443) ) {
        result.append(':')
              .append(port);
    }
 
//    result.append(request.getContextPath());

    return result.toString();
  }

  public static String getContents(String urlStr) {
    StringBuilder builder = new StringBuilder();
    try {
//    URL url = new URL("http://arcgis03.geiconsultants.com/hydroreport/proxy?" + urlStr);
      Logger.getLogger(BaseController.class).log(Logger.Level.INFO, urlStr);      
      URL url = new URL(urlStr);
      BufferedReader theHTML = null;
      if (urlStr.contains("http://")){
//      BufferedReader theHTML = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        theHTML = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
      } else {
//      if (urlStr.contains("https://")) {   
//        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        Logger.getLogger(BaseController.class).log(Logger.Level.INFO, conn);
        InputStream is = conn.getInputStream();
        Logger.getLogger(BaseController.class).log(Logger.Level.INFO, is);
        InputStreamReader isreader = new InputStreamReader(is);
        theHTML = new BufferedReader(isreader);
        Logger.getLogger(BaseController.class).log(Logger.Level.INFO, theHTML);
      }
      
      String thisLine;
      while ((thisLine = theHTML.readLine()) != null) {
        builder.append(thisLine).append("\n");
      }
      theHTML.close();
    } catch (SSLHandshakeException ex) {
      // Output expected SSLHandshakeExceptions.
      Logger.getLogger(BaseController.class).log(Logger.Level.WARN, ex.getMessage());
    } catch (IOException ex) {
      // Output unexpected InterruptedExceptions and IOExceptions.
      Logger.getLogger(BaseController.class).log(Logger.Level.WARN, ex.getMessage());
    } catch (Exception ex) {
      Logger.getLogger(BaseController.class).log(Logger.Level.WARN, ex.getMessage());
    }
    return builder.toString();
  }

  public Map<String, String> getEmailConfigMap() {
    return (HashMap) this.getApplicationContext().getBean("EmailConfig");
  }

  protected void sendSystemEmail(String subject, String body, String[] tos) throws MalformedURLException, IOException, EmailException {
    try {
      Map<String, String> emailConfig = getEmailConfigMap();
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailConfig.get("host"));
      email.setAuthentication(emailConfig.get("user"), emailConfig.get("password"));
      email.setSSLOnConnect(true);
      email.setSmtpPort(Integer.parseInt(emailConfig.get("port")));
      email.setDebug(Boolean.parseBoolean(emailConfig.get("debug")));

      email.setSubject(subject);
      email.setHtmlMsg(body);
      email.setFrom(emailConfig.get("from.address"), emailConfig.get("from.alias"));
      try {
        email.addTo(tos);
      } catch (Exception ex) {
      }
      try {
        email.addCc(emailConfig.get("cc.address").split(";"));
      } catch (Exception ex) {
      }
      try {
        email.addBcc(emailConfig.get("bcc.address").split(";"));
      } catch (Exception ex) {
      }
      new Thread(new MailerRunnable(email)).start();
    } catch (Exception ex) {
    }
  }
}
