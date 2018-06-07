package com.gei.facades;

import com.gei.entities.WtPreTransfer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtPreTransferFacade extends AbstractFacade<WtPreTransferFacade,WtPreTransfer> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public WtPreTransferFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }

  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

  public WtPreTransferFacade() {
    super(WtPreTransfer.class);
  }
}
