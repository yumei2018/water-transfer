/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAgency;
import com.gei.entities.WtFuType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ymei
 */
@Stateless
public class WtFuTypeFacade extends AbstractFacade<WtFuTypeFacade,WtFuType> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    public WtFuTypeFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }
//    private EntityManager em;
//
//    @Override
//    protected EntityManager getEntityManager() {
//        return em;
//    }

    public WtFuTypeFacade() {
        super(WtFuType.class);
    }
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
    /**
     * 
     * @param entity
     * @return 
     */
    @Transactional
    public List<WtFuType> searchFuType(WtFuType entity){
        List<WtFuType> records = null;
        try{
            
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(entity.getClass());
            Root<WtFuType> from = cq.from(entity.getClass());
            Predicate p = buildPredicate(cb,cq,from,entity);

            if (p == null){
                cq.select(from).orderBy(cb.desc(from.get("wtFuTypeId")));
            } else {
                cq.select(from).where(p).orderBy(cb.desc(from.get("wtFuTypeId")));
            }
            TypedQuery<WtFuType> q = getEntityManager().createQuery(cq);
            records = q.getResultList();
            
        } catch (Exception ex){
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex); 
        }
        
        return records == null ? new ArrayList<WtFuType>() : records;
    }  
    
//    public static void main(String[] args)
//    {
//        WtFuTypeFacade f = new WtFuTypeFacade();
//        List<WtFuType> wts = f.select("SELECT WT.* FROM WT_FU_TYPE WT",com.gei.entities.WtFuType.class);
//        System.out.println(wts.size());
//    }
    
}
