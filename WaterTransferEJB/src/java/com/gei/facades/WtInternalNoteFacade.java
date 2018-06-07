package com.gei.facades;

import com.gei.entities.WtInternalNote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtInternalNoteFacade extends AbstractFacade<WtInternalNoteFacade,WtInternalNote> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtInternalNoteFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtInternalNoteFacade() {
        super(WtInternalNote.class);
    } 
}
