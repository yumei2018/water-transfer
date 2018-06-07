package com.gei.facades;

import com.gei.entities.WtReviewNote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtReviewNoteFacade extends AbstractFacade<WtReviewNoteFacade,WtReviewNote> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtReviewNoteFacade setEntityManager(EntityManager em) {
        entityManager = em;
        return this;
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public WtReviewNoteFacade() {
        super(WtReviewNote.class);
    }   
}
