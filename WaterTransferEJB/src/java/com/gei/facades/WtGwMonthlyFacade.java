/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtGwMonthly;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtGwMonthlyFacade extends AbstractFacade<WtGwMonthlyFacade,WtGwMonthly> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtGwMonthlyFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtGwMonthlyFacade() {
        super(WtGwMonthly.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
