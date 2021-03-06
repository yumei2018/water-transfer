/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.WtAttachment;
import com.gei.entities.WtAttachmentWithFile;
import java.io.*;
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
import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ymei
 */
@Stateless
public class WtAttachmentWithFileFacade extends AbstractFacade<WtAttachmentWithFileFacade,WtAttachmentWithFile> {
    private File _file;

    @PersistenceContext(unitName = "WaterTransferEJBPU",type = PersistenceContextType.EXTENDED)
    @Override
    public WtAttachmentWithFileFacade setEntityManager(EntityManager em)
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
    public WtAttachmentWithFileFacade() {
        super(WtAttachmentWithFile.class);
    }
    @Override
    public String getPersistenceUnitName() {return "WaterTransferEJBPU";}

    /**
     *
     * @param s
     * @param entity
     */
    public void setFile(java.sql.Blob s, WtAttachment entity)
    {
        try {
            setFile(s.getBinaryStream(), entity);
        }
        catch (Exception ex)
        {
//            errorlog(ex);
        }
//        return this;
    }

    /**
     *
     * @param is
     * @param entity
     * @throws IOException
     */
    public void setFile(InputStream is, WtAttachment entity) throws IOException
    {
        String[] pieces = entity.getFilename().split("\\.");
        _file = File.createTempFile(pieces[0], "." + pieces[1]);
        OutputStream os = new FileOutputStream(_file);
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(os);
//        entity.setFileLob(new FileInputStream(_file));

//        return this;
    }

    /**
     *
     * @param entity
     */
    @Override
    @Transactional
    public WtAttachmentWithFileFacade create(WtAttachmentWithFile entity)
    {
        entity.setModifiedDate(Calendar.getInstance().getTime());
        entity.setCreatedDate(entity.getModifiedDate());
        entity.setIsActive(1);

        super.create(entity);
        return this;
    }
    /**
     *
     * @param entity
     */
    @Override
    @Transactional
    public WtAttachmentWithFileFacade edit(WtAttachmentWithFile entity)
    {
        entity.setModifiedDate(Calendar.getInstance().getTime());
        if (entity.getCreatedDate() == null)
        {
            entity.setCreatedDate(entity.getModifiedDate());
        }
        super.edit(entity);
        return this;
    }

    /**
     *
     * @param entity
     */
//    public void deleteTempFile(WtAttachment entity)
//    {
//        if (entity.getFileLob() != null)
//        {
//            try {
////                entity.getFileLob().close;
//                Files.deleteIfExists(_file.toPath());
//            } catch (IOException ex) {
////                errorlog(ex);
//            }
//        }
//
////        return this;
//    }

    /**
     *
     * @param entity
     * @return
     */
    @Transactional
    public List<WtAttachmentWithFile> searchAttachment(WtAttachmentWithFile entity){
        List<WtAttachmentWithFile> records = null;
        try{

            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(entity.getClass());
            Root<WtAttachmentWithFile> from = cq.from(entity.getClass());
            Predicate p = buildPredicate(cb,cq,from,entity);

            if (p == null){
                cq.select(from).orderBy(cb.desc(from.get("wtAttachmentId")));
            } else {
                cq.select(from).where(p).orderBy(cb.desc(from.get("wtAttachmentId")));
            }
            TypedQuery<WtAttachmentWithFile> q = getEntityManager().createQuery(cq);
            records = q.getResultList();

        } catch (Exception ex){
           Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return records == null ? new ArrayList<WtAttachmentWithFile>() : records;
    }
}
