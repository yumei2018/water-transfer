/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.util;

import com.gei.entities.AbstractEntity;
import com.gei.facades.AbstractFacade;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author clay
 */
public class EntityManagerUtils {
  private static EntityManagerFactory emfactory;
  private static ApplicationContext appContext;
  
  public static <F extends AbstractFacade, E extends AbstractEntity> F getFacade(Class<F> facadeClass){
    String className = facadeClass.getSimpleName();
    
    if (!getFacadeMap().containsKey(className)){
      F facade = null;
      if (getApplicationContext().containsBean(className)){
        facade = (F) getApplicationContext().getBean(className);
      }
      else {
        try {
          facade = facadeClass.newInstance();
          facade.setEntityManager(getEMFactory().createEntityManager());
        } catch (Exception ex) {
          Logger.getLogger(EntityManagerUtils.class.getName()).log(Level.SEVERE, null, ex);
        } 
      }
      
      if (facade != null){
        getFacadeMap().put(className, facade);
      }
    }
      
    return (F) getFacadeMap().get(className);
  }
  
  private static EntityManagerFactory getEMFactory(){
    if (emfactory == null){
      emfactory = (EntityManagerFactory) getApplicationContext().getBean(org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.class);
    }
    
    return emfactory;
  }
  
  private static ApplicationContext getApplicationContext(){
    if (appContext == null){
      appContext = WebApplicationContextUtils.getWebApplicationContext(getRequest().getServletContext());
    }
    return appContext;
  }
  
  private static HttpServletRequest getRequest(){
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }
  
  private static LinkedHashMap getFacadeMap(){
    return (LinkedHashMap)getApplicationContext().getBean("FacadeMap");
  }
}
