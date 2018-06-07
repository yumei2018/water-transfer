/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtTransNepa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtTransNepaFacade extends AbstractFacade<WtTransNepaFacade,WtTransNepa> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtTransNepaFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtTransNepaFacade() {
        super(WtTransNepa.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
