package com.gei.facades;

import com.gei.entities.WtRvTarstor;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtRvTarstorFacade extends AbstractFacade<WtRvTarstorFacade,WtRvTarstor> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public WtRvTarstorFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }

  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

  public WtRvTarstorFacade() {
    super(WtRvTarstor.class);
  }
}
