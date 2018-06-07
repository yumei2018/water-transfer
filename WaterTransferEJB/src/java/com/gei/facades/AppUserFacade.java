/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.AppUser;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author clay
 */
@Stateless
public class AppUserFacade extends AbstractFacade<AppUserFacade,AppUser> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public AppUserFacade setEntityManager(EntityManager em) {
	entityManager = em;
        return this;
    }
    
    public AppUserFacade() {
        super(AppUser.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
  
//  public static void main(String[] args)
//  {
//    AppUserFacade f = new AppUserFacade();
//    AppUser user = new AppUser();
//    user.setUsername("seller");
//    f.set("username", "seller");
//    user = f.find(user);
//    System.out.println(user.getWtTransCollection());
//  }
}
