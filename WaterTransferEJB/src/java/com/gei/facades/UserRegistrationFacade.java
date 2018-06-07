/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.UserRegistration;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author pheng
 */
@Stateless
public class UserRegistrationFacade extends AbstractFacade<UserRegistrationFacade,UserRegistration> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public UserRegistrationFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }
  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

  public UserRegistrationFacade() {
    super(UserRegistration.class);
  }

}
