package com.gei.util;

import gov.ca.water.htmtopdf.HtmlToPdfConverter;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.*;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 *
 * @author Charlie Lay
 */
public class WebUtil implements Serializable {
  private static Logger mLogger = Logger.getLogger(WebUtil.class.getName());
  private static ApplicationContext mAppContext;
  public final static String DISPATCHER_SERVLET_NAME
                  = "org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher";

  /**
   * Public Constructor
   */
  public WebUtil() {
    super();
  }


    /**
   *
   * @param appContext
   */
  public static void setAppContext(ApplicationContext appContext) {
    WebUtil.mAppContext = appContext;
  }

  /**
   *
   * @return
   */
  public final static ApplicationContext getApplicationContext(){
    HttpServletRequest request = null;
    ServletContext context = null;
    if ((mAppContext == null) && ((request = getRequest()) != null)
                              && ((context = request.getServletContext()) != null)) {
      mAppContext = WebApplicationContextUtils.getWebApplicationContext(context);
    }
    return mAppContext;
  }

  /**
   *
   * @return
   */
  public final static HttpServletRequest getRequest(){
    HttpServletRequest result = null;
    try {
      RequestAttributes reqAttr = null;
      if ( ((reqAttr = RequestContextHolder.getRequestAttributes()) != null)
          && (reqAttr instanceof ServletRequestAttributes)) {
        result = ((ServletRequestAttributes)reqAttr).getRequest();
      }
    } catch (Exception exp) {
      mLogger.log(Level.SEVERE, null, exp);
    }
    finally {
      return result;
    }
  }

  /**
   * Create or get current session
   *
   * @return HttpSession
   */
  public final static HttpSession getSession(){
    HttpSession result = null;
    try {
      HttpServletRequest request = null;
      if (((request = getRequest()) != null)
          && ((result = request.getSession()) == null)){
        result = request.getSession(true);
      }
    } catch (Exception exp) {
      mLogger.log(Level.SEVERE, exp.getMessage());
    }
    finally {
      return result;
    }
  }

  /**
   *
   * @param <O>
   * @param cls
   * @return
   */
  public final static <O extends Object> O getSession(Class<O> cls){
    return getSession(cls,true);
  }

  /**
   * Create or get the session instance of the class object
   *
   * @param <O>
   * @param cls
   * @return
   */
  public final static <O extends Object> O getSession(Class<O> cls,boolean autoCreate){
    O result = null;
    try {
      HttpSession session = null;
      if (((session = getSession()) != null)
          && ((result = (O) session.getAttribute(cls.getSimpleName())) == null)) {
        if (autoCreate) {
          result = cls.newInstance();
          session.setAttribute(cls.getSimpleName(), result);
        }
      }
    } catch (Exception exp) {
      mLogger.log(Level.SEVERE, exp.getMessage());
    }
    finally {
      return result;
    }
  }

  /**
   * Create or get the session instance of the class object
   *
   * @param <O>
   * @param cls
   * @return
   */
  public final static <O extends Object> O getSession(String sessionKey){
    O result = null;
    try {
      HttpSession session = null;
      if (((session = getSession()) != null)) {
        result = (O) session.getAttribute(sessionKey);
      }
    } catch (Exception exp) {
      mLogger.log(Level.SEVERE, exp.getMessage());
    }
    finally {
      return result;
    }
  }

  /**
   *
   * @param <CTX>
   * @param ctxCls
   * @return
   */
  public final static <CTX extends Object> CTX getContext(Class<CTX> ctxCls){
    return (CTX) getSession(ctxCls);
  }

  /**
   *
   * @param url
   * @return
   */
  public final static String getContents(String url) {
    return getContents(url, "UTF-8");
  }

  /**
   *
   * @param url
   * @return
   */
  public final static String getContents(URL url) {
    return getContents(url, "UTF-8");
  }

  /**
   *
   * @param u
   * @param encodeType
   * @return
   */
  public final static String getContents(URL u, String encodeType) {
    StringBuilder builder = new StringBuilder();

    try {
      BufferedReader theHTML = new BufferedReader(new InputStreamReader(u.openStream(), encodeType));
      String thisLine;
      while ((thisLine = theHTML.readLine()) != null) {
        builder.append(thisLine).append("\n");
      }
    } catch (Exception e) {
      System.err.println(e);
      builder.append(e);
    }

    return builder.toString();
  }

  /**
   *
   * @param url
   * @param encodeType
   * @return
   */
  public final static String getContents(String url, String encodeType) {
    String contentStr = "";
    try {
      contentStr = getContents(new URL(url), encodeType);
    } catch (Exception e) {
      mLogger.log(Level.WARNING,e.getMessage());
    }

    return contentStr;
  }

  /**
   *
   * @param file
   * @return
   */
  public final static String getContents(File file) {
    FileReader fr = null;
    StringBuilder content = new StringBuilder();
    try {

      fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line = "";

      while ((line = br.readLine()) != null) {
        if (0 < content.length()) {
          content.append("\n");
        }

        content.append(line);
      }
    } catch (IOException ex) {
      content.append(ex.getMessage());
    } finally {
      try {
        if (null != fr) {
          fr.close();
        }
      } catch (IOException ex) {
        content.append(ex.getMessage());
      }
    }
    return content.toString();
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    mLogger = null;
  }


  public final static String getServerHost(){
    String result = null;
    HttpServletRequest request = null;
    if ((request = WebUtil.getRequest()) != null) {
      result = "http";

      result += request.isSecure() ? "s" : "";
      result += "://" + request.getServerName();
      result += request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
    }

    return result;
  }

  public final static String getServerContext(){
    String result = null;
    HttpServletRequest request = null;
    if ((request = WebUtil.getRequest()) != null) {
      result = request.getContextPath();
    }

    return result;
  }

  public final static String getServerRoot(){
    return WebUtil.getServerHost() + WebUtil.getServerContext();
  }

  /**
   *
   * @return
   */
  public final static String getServerUrl(){
    return WebUtil.getServerRoot();
  }

  /**
   *
   * @return
   */
  public final static ViewResolver getViewResolver(){
    return WebUtil.getViewResolver(null);
  }

  /**
   *
   * @param beanId
   * @return
   */
  public final static ViewResolver getViewResolver(String beanId){
    ViewResolver result = null;
    ApplicationContext dispatcherCtx = null;
    if ((dispatcherCtx = WebUtil.getApplicationContext(DISPATCHER_SERVLET_NAME)) != null){
      if (StringUtils.isEmpty(beanId)){
        result = dispatcherCtx.getBean(ViewResolver.class);
      }
      else {
        result = dispatcherCtx.getBean(beanId, ViewResolver.class);
      }
    }

    return result;
  }

  /**
   *
   * @param attr
   * @return
   */
  public final static ApplicationContext getApplicationContext(String attr){
    ApplicationContext result = null;

    try {
      HttpServletRequest request = null;
      Object appCtx = null;
      if (((request = WebUtil.getRequest()) != null)
          && ((appCtx = request.getServletContext().getAttribute(attr)) != null)) {
        result = (ApplicationContext) appCtx;
      }
    }
    catch (Exception exp) {
      Logger
      .getLogger(WebUtil.class.getName())
        .log(Level.WARNING
          ,"{0}.getApplicationContext Error:\n {1}"
          ,new Object[]{WebUtil.class.getSimpleName(), exp.getMessage()});
    }

    return result;
  }

  /**
   *
   * @param html
   * @param os
   * @throws Exception
   */
  public final static void toPdf(String html, OutputStream os) throws Exception {
    try(InputStream is = new ByteArrayInputStream(html.getBytes())){
      HtmlToPdfConverter converter = HtmlToPdfConverter.getInstance();
      converter.setCreateXhtmlFile(true);
      String url = WebUtil.getServerUrl();
      if (!converter.execute(is, new URL(url + "/"), os)) {
        throw new Exception(converter.getErrorMsg());
      }
    }
  }

  /**
   *
   * @param str
   * @return
   */
  public final static String md5(String str) {
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

  public static String getViewHtml(String viewName, ModelMap mmap) {
    String result = null;
    try {
      MockHttpServletResponse mockResp = new MockHttpServletResponse();
      View myView = WebUtil.getViewResolver().resolveViewName(viewName, Locale.US);
      myView.render(mmap, WebUtil.getRequest(), mockResp);
      result = mockResp.getContentAsString();
    } catch (Exception ex) {
      throw new IllegalStateException(ex.getMessage());
    }

    return result;
  }

  //<editor-fold defaultstate="collapsed" desc="Static Get Hostname">
  public final static String getHostname() {
    String result = null;
    HttpServletRequest request = null;
    if ((request = WebUtil.getRequest()) != null) {
//      result = "";
//
//      result += request.isSecure() ? "s" : "";
//      result += "://" + request.getServerName();
      result = request.getServerName();
      result += request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Parse Domain">
  public static URL toUrl(String url) {
    URL result = null;

    try {
      result = new URL(url);
    }
    catch(Exception ex) {
      throw new IllegalStateException(
        String.format(""
          ,WebUtil.class.getName()
          ,"toUrl"
          ,ex.getClass().getName()
          ,ex.getMessage()
        )
      );
    }

    return result;
  }

  public static String parseDomain(String url) {
    return WebUtil.parseDomain(WebUtil.toUrl(url));
  }

  public static String parseDomain(URL url) {
    String result = null;

    if (url != null) {
      result = url.getHost();
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Get Bean">
  public static <O extends Object> O getBean(String beanId) {
    O result = null;

    if (WebUtil.getApplicationContext() != null) {
      result = (O) WebUtil.getApplicationContext().getBean(beanId);
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Is Local Host">
  public final static Boolean isLocalHost() {
    return Objects.equals(WebUtil.getRequest().getServerName(), "localhost");
  }
  //</editor-fold>
}