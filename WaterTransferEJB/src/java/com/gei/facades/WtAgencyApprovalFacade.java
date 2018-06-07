/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAgencyApproval;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtAgencyApprovalFacade extends AbstractFacade<WtAgencyApprovalFacade,WtAgencyApproval> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtAgencyApprovalFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtAgencyApprovalFacade() {
        super(WtAgencyApproval.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
}
