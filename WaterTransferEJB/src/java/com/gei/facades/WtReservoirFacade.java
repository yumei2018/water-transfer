/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtReservoir;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtReservoirFacade extends AbstractFacade<WtReservoirFacade,WtReservoir> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtReservoirFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtReservoirFacade() {
        super(WtReservoir.class);
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

}

