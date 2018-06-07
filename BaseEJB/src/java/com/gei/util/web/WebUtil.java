/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.util.web;

import com.gei.facades.AbstractFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class WebUtil {

  /**
   * Public Constructor  
   */
  public WebUtil() {
    super();  
  }
  
  private static ApplicationContext appContext;
  
  public static ApplicationContext getApplicationContext(){
    if (appContext == null){
      HttpServletRequest request = getRequest();
      if (request != null){
        appContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
      }
    }
    return appContext;
  }
  
  public static HttpServletRequest getRequest(){
    HttpServletRequest request = null;
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      request = sra == null ? null : sra.getRequest();
    } catch (Exception exp) {
      Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, null, exp);
    }
    finally {
      return request;
    }
  }
  
  public static HttpSession getSession(){
    HttpSession result = null;
    try {
      if ((result = getRequest().getSession()) == null){
        result = getRequest().getSession(true);
      }
    } catch (Exception exp) {
      Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, exp.getMessage());
    }
    finally {
      return result;
    }
  }
  
  public static <O extends Object> O getSession(Class<O> cls){
    Object result = null;
    try {
      if ((result = getSession().getAttribute(cls.getSimpleName())) == null){
        result = cls.newInstance();
        getSession().setAttribute(cls.getSimpleName(), result);
      }
    } catch (Exception exp) {
      Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, exp.getMessage());
    }
    finally {
      return (O) result;
    }
  }
  
  public static <CTX extends Object> CTX getContext(Class<CTX> ctxCls){
    return (CTX) getSession(ctxCls);
  }
  
  public static <F extends AbstractFacade> F getFacade(Class<F> cls){
    F facade = null;
    if (getApplicationContext() != null){
      try {
        facade = (F)getApplicationContext().getBean(cls.getSimpleName());
      }
      catch(Exception ex){}
    }
    
    if (facade == null){
      try {
        facade = cls.newInstance();
      } 
      catch (Exception ex) {
        Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return facade;
  }
}
