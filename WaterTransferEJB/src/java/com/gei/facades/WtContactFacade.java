package com.gei.facades;

import com.gei.entities.WtContact;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtContactFacade extends AbstractFacade<WtContactFacade,WtContact> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtContactFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtContactFacade() {
        super(WtContact.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
