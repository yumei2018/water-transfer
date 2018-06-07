/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.WtCropType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtCropTypeFacade extends AbstractFacade<WtCropTypeFacade,WtCropType> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public WtCropTypeFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }

  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

  public WtCropTypeFacade() {
    super(WtCropType.class);
  }
}
