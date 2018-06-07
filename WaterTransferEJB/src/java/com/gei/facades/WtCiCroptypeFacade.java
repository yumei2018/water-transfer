
package com.gei.facades;

import com.gei.entities.WtCiCroptype;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author ymei
 */
@Stateless
public class WtCiCroptypeFacade extends AbstractFacade<WtCiCroptypeFacade,WtCiCroptype> {
  @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
  @Override
  public WtCiCroptypeFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }

  public WtCiCroptypeFacade() {
    super(WtCiCroptype.class);
  }
  
  @Override
  public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
}
