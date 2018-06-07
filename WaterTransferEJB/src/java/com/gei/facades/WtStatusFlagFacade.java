/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtStatusFlag;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtStatusFlagFacade extends AbstractFacade<WtStatusFlagFacade,WtStatusFlag> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtStatusFlagFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtStatusFlagFacade() {
        super(WtStatusFlag.class);
    }   
    
//    public static void main(String[] args)
//    {
//        WtStatusFlagFacade f = new WtStatusFlagFacade();
//        List<WtStatusFlag> wts = f.select("SELECT WT.* FROM WT_STATUS_FLAG WT",com.gei.entities.WtStatusFlag.class);
//        System.out.println(wts.size());
//    }
}