/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtReviewComment;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ymei
 */
@Stateless
public class WtReviewCommentFacade extends AbstractFacade<WtReviewCommentFacade,WtReviewComment> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtReviewCommentFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtReviewCommentFacade() {
        super(WtReviewComment.class);
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
    public WtReviewCommentFacade create(WtReviewComment entity)
    {
        entity.setUpdatedDate(Calendar.getInstance().getTime());
        entity.setCreatedDate(entity.getUpdatedDate());

        super.create(entity);
        return this;
    }
    
    /**
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public WtReviewCommentFacade edit(WtReviewComment entity)
    {        
        entity.setUpdatedDate(Calendar.getInstance().getTime());
        if (entity.getCreatedDate() == null)
        {
            entity.setCreatedDate(entity.getUpdatedDate());
        }
        super.edit(entity);
        return this;
    }    
}
