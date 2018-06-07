
package com.gei.facades;

import com.gei.entities.WtCiMonthly;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtCiMonthlyFacade extends AbstractFacade<WtCiMonthlyFacade,WtCiMonthly> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public WtCiMonthlyFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }

  public WtCiMonthlyFacade() {
    super(WtCiMonthly.class);
  }
  
  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
