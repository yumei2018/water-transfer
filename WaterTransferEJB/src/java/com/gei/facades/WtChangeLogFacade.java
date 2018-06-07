/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtChangeLog;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtChangeLogFacade extends AbstractFacade<WtChangeLogFacade, WtChangeLog>{
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtChangeLogFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }    

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtChangeLogFacade() {
        super(WtChangeLog.class);
    }    
}
