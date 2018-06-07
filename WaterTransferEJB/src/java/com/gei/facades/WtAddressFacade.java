/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAddress;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtAddressFacade extends AbstractFacade<WtAddressFacade,WtAddress> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    public WtAddressFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtAddressFacade() {
        super(WtAddress.class);
    }
    
}
