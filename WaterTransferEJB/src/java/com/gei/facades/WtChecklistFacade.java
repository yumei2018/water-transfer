/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtChecklist;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtChecklistFacade extends AbstractFacade<WtChecklistFacade,WtChecklist> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtChecklistFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtChecklistFacade() {
        super(WtChecklist.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
//    public static void main(String[] args)
//    {
//        WtChecklistFacade wcf = new WtChecklistFacade();
//        wcf.set("checklistField", "BI_ATTACHMENT");
//        System.out.println(wcf.findAll());
//    }
}
