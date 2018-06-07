/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAttachment;
import com.gei.entities.WtChecklist;
import com.gei.entities.WtWell;
import java.util.ArrayList;
import java.util.Collection;
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
public class WtWellFacade extends AbstractFacade<WtWellFacade,WtWell> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtWellFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtWellFacade() {
        super(WtWell.class);
    }

    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    public boolean checkAtachmentComplete(List<WtChecklist> records,WtWell well){
        Collection<WtChecklist> attachmentChecklist = new ArrayList<>();
        Collection<WtAttachment> attachmentlist = well.getWtAttachmentCollection();
        if(attachmentlist.size()>0){
            for(WtAttachment attach:attachmentlist){
                for(WtChecklist attachchecklist : attach.getWtChecklistCollection()){
                    if(!attachmentChecklist.contains(attachchecklist)){
                        attachmentChecklist.add(attachchecklist);
                    }
                }
            }
        }
        return records.size() == attachmentChecklist.size();
    }

      /**
     *
     * @param entity
     * @return
     */
    @Transactional
    public boolean checklistItem(WtWell entity){
        List<WtChecklist> records = new ArrayList<>();
         try{
            WtChecklist wc = new WtChecklist();
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(wc.getClass());
            Root<WtChecklist> from = cq.from(wc.getClass());
            Predicate p = null;
            if(entity.getWellTransfer()!= null && entity.getWellTransfer() > 0){
                p = cb.equal(from.get("wellTransfer"), 1);
            }
            if(entity.getWellMonitoring()!=null && entity.getWellMonitoring() > 0){
                p = cb.equal(from.get("wellMonitoring"), 1);
            }
            if(entity.getWellTransfer()!= null && entity.getWellTransfer() > 0
                && entity.getWellMonitoring()!=null && entity.getWellMonitoring() > 0){
               Predicate u = cb.equal(from.get("wellTransfer"), 1);
               p = cb.or(u,cb.equal(from.get("wellMonitoring"), 1));
            }
            if(p != null)
            {
                cq.select(from).where(p).orderBy(cb.desc(from.get("wtChecklistId")));
                TypedQuery<WtChecklist> q = getEntityManager().createQuery(cq);
                records = q.getResultList();
            }

        } catch (Exception ex){
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
         return checkAtachmentComplete(records,entity);
    }
}
