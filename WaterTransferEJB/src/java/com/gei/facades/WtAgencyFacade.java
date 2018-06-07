/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAgency;
import java.util.ArrayList;
import java.util.Calendar;
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
public class WtAgencyFacade extends AbstractFacade<WtAgencyFacade,WtAgency> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtAgencyFacade setEntityManager(EntityManager em)
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

    public WtAgencyFacade() {
        super(WtAgency.class);
    }
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}    
    
    /**
     *
     * @param entity
     * @return 
     */
    @Override
    @Transactional
    public WtAgencyFacade create(WtAgency entity)
    {
        entity.setModifyDate(Calendar.getInstance().getTime());
        entity.setCreateDate(entity.getModifyDate());

        super.create(entity);
        return this;
    }
    
    /**
     * 
     * @param entity
     * @return 
     */
    @Transactional
    public List<WtAgency> searchAgency(WtAgency entity){
        List<WtAgency> records = null;
        try{
            
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(entity.getClass());
            Root<WtAgency> from = cq.from(entity.getClass());
            Predicate p = buildPredicate(cb,cq,from,entity);

            if (p == null){
                cq.select(from).orderBy(cb.desc(from.get("wtAgencyId")));
            } else {
                cq.select(from).where(p).orderBy(cb.desc(from.get("wtAgencyId")));
            }
            TypedQuery<WtAgency> q = getEntityManager().createQuery(cq);
            records = q.getResultList();
            
        } catch (Exception ex){
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex); 
        }
        
        return records == null ? new ArrayList<WtAgency>() : records;
    }
    
//    public static void main(String[] args)
//    {
//        WtAgencyFacade f = new WtAgencyFacade();
//        List<WtAgency> wts = f.select("SELECT WT.* FROM WT_AGENCY WT",com.gei.entities.WtAgency.class);
//        System.out.println(wts.size());
//        f.setEntityManager(Persistence.createEntityManagerFactory("WaterTransferEJBPU").createEntityManager());
//        f.getEntityManager().getTransaction().begin();
//        WtAgency a = f.find(2);
//        System.out.println(a.getWtTransCollection());
//        System.out.println(a.getWtTransCollection1());
//        f.getEntityManager().getTransaction().commit();
//    }

}