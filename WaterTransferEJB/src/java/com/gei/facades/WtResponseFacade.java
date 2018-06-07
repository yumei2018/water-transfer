/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtResponse;
import com.gei.entities.WtTrans;
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
public class WtResponseFacade extends AbstractFacade<WtResponseFacade,WtResponse> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtResponseFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtResponseFacade() {
        super(WtResponse.class);
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
    public WtResponseFacade create(WtResponse entity)
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
    public WtResponseFacade edit(WtResponse entity)
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
