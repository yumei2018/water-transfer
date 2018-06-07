/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtContactPhone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtContactPhoneFacade extends AbstractFacade<WtContactPhoneFacade,WtContactPhone> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtContactPhoneFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtContactPhoneFacade() {
        super(WtContactPhone.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
