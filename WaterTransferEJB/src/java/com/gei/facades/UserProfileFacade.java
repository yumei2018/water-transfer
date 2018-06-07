/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.UserProfile;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author clay
 */
@Stateless
public class UserProfileFacade extends AbstractFacade<UserProfileFacade,UserProfile> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  public UserProfileFacade setEntityManager(EntityManager em) {
		entityManager = em;
    return this;
	}
  public UserProfileFacade() {
    super(UserProfile.class);
  }
  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
