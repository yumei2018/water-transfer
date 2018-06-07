package com.gei.facades;

import com.gei.entities.WtAgency;
import com.gei.entities.WtFuType;
import com.gei.entities.WtTrans;
import com.gei.facades.delegates.LoadQueryDelegate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ymei
 */
@Stateless
public class WtTransFacade extends AbstractFacade<WtTransFacade, WtTrans> {

  @PersistenceContext(unitName = "WaterTransferEJBPU", type = PersistenceContextType.EXTENDED)
  @Override
  public WtTransFacade setEntityManager(EntityManager em) {
    entityManager = em;
    return this;
  }
//    private EntityManager em;
//
//    @Override
//    protected EntityManager getEntityManager() {
//        return em;
//    }

  public WtTransFacade() {
    super(WtTrans.class);
  }

  @Override
  public String getPersistenceUnitName() {
    return "WaterTransferEJBPU";
  }

  /**
   *
   * @param entity
   */
  @Override
  @Transactional
  public WtTransFacade create(WtTrans entity) {
    entity.setModifyDate(Calendar.getInstance().getTime());
    entity.setCreateDate(entity.getModifyDate());

    super.create(entity);
    return this;
  }

  /**
   *
   * @param entity
   */
  @Override
  @Transactional
  public WtTransFacade edit(WtTrans entity) {
    entity.setModifyDate(Calendar.getInstance().getTime());
    if (entity.getCreateDate() == null) {
      entity.setCreateDate(entity.getModifyDate());
    }
    super.edit(entity);
    return this;
  }

  /**
   *
   * @param entity
   * @return
   */
  @Transactional
  public List<WtTrans> searchTrans(WtTrans entity) {
    return searchTrans(entity, null, null, null, null, null);
  }

  /**
   *
   * @param entity
   * @param transYearFrom
   * @param transYearTo
   * @param seller
   * @param buyer
   * @param fuType
   * @return
   */
  @Transactional
  public List<WtTrans> searchTrans(WtTrans entity, Integer transYearFrom, Integer transYearTo, WtAgency seller, WtAgency buyer, WtFuType fuType) {
    List<WtTrans> records = null;
    try {

      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery(entity.getClass());
      Root<WtTrans> from = cq.from(entity.getClass());
      Predicate p = buildPredicate(cb, cq, from, entity);
      // Search From and To Trans Year
      if (transYearFrom != null || transYearTo != null) {
        Calendar cal = Calendar.getInstance();
        Integer thisYear = cal.get(Calendar.YEAR);
        if (transYearFrom == null) {
          transYearFrom = 0;
        }
        if (transYearTo == null) {
          transYearTo = thisYear;
        }
        Expression<Integer> transYear = from.get("transYear");
        if (p == null) {
          p = cb.between(transYear, transYearFrom, transYearTo);
        } else {
          p = cb.and(p, cb.between(transYear, transYearFrom, transYearTo));
        }
      }
      if (seller != null) {
        Expression<Collection<WtAgency>> WtAgencies = from.get("wtSellerCollection");
        if (p == null) {
          p = cb.isMember(seller, WtAgencies);
        } else {
          p = cb.and(p, cb.isMember(seller, WtAgencies));
        }
      }
      if (buyer != null) {
        Expression<Collection<WtAgency>> WtAgencies = from.get("wtBuyerCollection");
        if (p == null) {
          p = cb.isMember(buyer, WtAgencies);
        } else {
          p = cb.and(p, cb.isMember(buyer, WtAgencies));
        }
      }
      if (fuType != null) {
        Expression<Collection<WtFuType>> WtFuTypes = from.get("wtFuTypeCollection");
        if (p == null) {
          p = cb.isMember(fuType, WtFuTypes);
        } else {
          p = cb.and(p, cb.isMember(fuType, WtFuTypes));
        }
      }

      // If there is no any condition, then select all records
      if (p == null) {
        cq.select(from).orderBy(cb.asc(from.get("wtTransId")));
      } else {
        cq.select(from).where(p).orderBy(cb.asc(from.get("wtTransId")));
      }
      TypedQuery<WtTrans> q = getEntityManager().createQuery(cq);
      records = q.getResultList();

    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }

    return records == null ? new ArrayList<WtTrans>() : records;
  }

  @Transactional
  public List<WtTrans> searchAll(WtTrans entity) {
    List<WtTrans> records = null;
    try {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery(entity.getClass());
      Root<WtTrans> from = cq.from(entity.getClass());
      Predicate p = buildPredicate(cb, cq, from, entity);

      // Search by seller
      WtAgency seller = null;
      Iterator<WtAgency> sellersIt;
      if (entity.getWtSellerCollection() != null) {
        sellersIt = entity.getWtSellerCollection().iterator();
        seller = sellersIt.next();

        Expression<Collection<WtAgency>> WtAgencies = from.get("wtSellerCollection");
        if (p == null) {
          p = cb.isMember(seller, WtAgencies);
        } else {
          p = cb.and(p, cb.isMember(seller, WtAgencies));
        }
      }

      // If there is no any condition, then select all records
      if (p == null) {
        cq.select(from).orderBy(cb.asc(from.get("wtTransId")));
      } else {
        cq.select(from).where(p).orderBy(cb.asc(from.get("wtTransId")));
      }
      TypedQuery<WtTrans> q = getEntityManager().createQuery(cq);
      records = q.getResultList();

    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }

    return records == null ? new ArrayList<WtTrans>() : records;
  }

  public boolean exists(WtTrans wt) {
    return this.exists(wt.getWtTransId());
  }

  private Boolean mRecordExist;

  public boolean exists(final Integer wtTransId) {
    this.mRecordExist = false;// reset

    if (wtTransId != null) {
      this.select("SELECT WT_TRANS_ID FROM WT_TRANS WHERE WT_TRANS_ID = " + wtTransId, new LoadQueryDelegate(this) {

        @Override
        public void loadQuery(ResultSet rs, List list) throws SQLException {
          WtTransFacade listener = (WtTransFacade) this.getListener();
          if (rs.next()) {
            listener.mRecordExist = Objects.equals(rs.getInt("WT_TRANS_ID"), wtTransId);
          }
        }
      });
    }

    return this.mRecordExist;
  }
}
