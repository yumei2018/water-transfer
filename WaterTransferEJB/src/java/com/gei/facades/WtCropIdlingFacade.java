/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtContact;
import com.gei.entities.WtCropIdling;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtCropIdlingFacade extends AbstractFacade<WtCropIdlingFacade,WtCropIdling>{
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtCropIdlingFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtCropIdlingFacade() {
        super(WtCropIdling.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
}
