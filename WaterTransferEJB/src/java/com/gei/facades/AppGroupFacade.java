/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.AppGroup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author clay
 */
@Stateless
public class AppGroupFacade extends AbstractFacade<AppGroupFacade,AppGroup> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public AppGroupFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }
  
  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
  
  public AppGroupFacade() {
    super(AppGroup.class);
  }
  
  @Transactional
  public AppGroup getGroupByCode(String code){
    AppGroup group  = new AppGroup();
    group.setCode(code);
    group = this.find(group);
    
    return group;
  }
//  public static void main(String[] args)
//  {
//    AppGroupFacade f = new AppGroupFacade();
////    java.util.List results = f.select("SELECT AU.* FROM APP_USER AU");
//    java.util.List results = f.select("SELECT AU.* FROM APP_USER AU",com.gei.entities.AppUser.class);
//    System.out.println(results);
//  }
}
