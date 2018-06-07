/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtTransReach;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ymei
 */
@Stateless
public class WtTransReachFacade extends AbstractFacade<WtTransReachFacade,WtTransReach> {
    @PersistenceContext(unitName = "WaterTransferEJBPU")
    @Override
    public WtTransReachFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtTransReachFacade() {
        super(WtTransReach.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
