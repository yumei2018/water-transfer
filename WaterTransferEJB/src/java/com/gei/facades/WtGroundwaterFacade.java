/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtGroundwater;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtGroundwaterFacade extends AbstractFacade<WtGroundwaterFacade,WtGroundwater> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtGroundwaterFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtGroundwaterFacade() {
        super(WtGroundwater.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
//    public static void main(String[] args)
//    {
//        WtGroundwaterFacade f = new WtGroundwaterFacade();
//        WtGroundwater gw = f.find(1);
//        System.out.println(gw.getGrossTransPumping());
//        System.out.println(f.findAll());
//    }
    
}
