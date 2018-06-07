/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.facades;

import com.gei.entities.WtAttachment;
import com.gei.entities.WtContact;
import com.gei.entities.WtLocation;
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
public class WtLocationFacade extends AbstractFacade<WtLocationFacade,WtLocation> {
    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtLocationFacade setEntityManager(EntityManager em)
    {
        entityManager = em;
        return this;
    }

    public WtLocationFacade() {
        super(WtLocation.class);
    }
    
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}
    
    /**
     *
     * @param entity
     */
    @Override
    @Transactional
    public WtLocationFacade create(WtLocation entity)
    {
        entity.setUpdatedDate(Calendar.getInstance().getTime());
        entity.setCreatedDate(entity.getUpdatedDate());

        super.create(entity);
        return this;
    }
    /**
     *
     * @param entity
     */
    @Override
    @Transactional
    public WtLocationFacade edit(WtLocation entity)
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
