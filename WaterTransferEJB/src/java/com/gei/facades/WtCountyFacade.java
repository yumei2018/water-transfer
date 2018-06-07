package com.gei.facades;

import com.gei.entities.WtCounty;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtCountyFacade extends AbstractFacade<WtCountyFacade,WtCounty> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtCountyFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtCountyFacade() {
        super(WtCounty.class);
    }
    
}
