/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.entities.EntityBase;
//import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author soi
 */
public class BaseFacade<T>// implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Class<T> entityClass;
    private String DefaultPU="";
    protected boolean DebugOn=false;
            
//    @PersistenceContext
    protected EntityManager em;
    
    public BaseFacade(Class<T> entityClass) {
        setEntityClass(entityClass);
    }

    public BaseFacade(Class<T> entityClass, String unitName) {
        setEntityClass(entityClass);
        setDefaultPU(unitName);
//        initEntityManager();
    }
    
    private void initId(T entity)
    {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Number> cq = qb.createQuery(Number.class);
        Root root = cq.from(entity.getClass());
        cq.select(qb.max(root.get(getIdField(entity))));
        Object e = getEntityManager().createQuery(cq).getSingleResult();
        setId(entity, (Integer) e);
        getEntityManager().clear();
    }
    
    private String getIdField(T entity)
    {
        String field = "";
        
        for (Field f : entity.getClass().getDeclaredFields())
        {
            if (f.isAnnotationPresent(Id.class))
            {
                field = f.getName();
                break;
            }
        }
        
        return field;
    }
    
    private void setId(T entity, Integer i)
    {
        String idField = getIdField(entity);
        try {
            Method m = entity.getClass().getDeclaredMethod("set" + idField.substring(0,1).toUpperCase() + idField.substring(1), Integer.class);
            m.invoke(entity, i);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Transactional
    public void create(T entity) {
//        initDummyId(entity);
//        getEntityManager().getTransaction().begin();
        getEntityManager().persist(entity);
        //getEntityManager().flush();
//        getEntityManager().getTransaction().commit();
//        getEntityManager().detach(entity);
//        initId(entity);
//        getEntityManager().clear();
    }
    
    private void initDummyId(T entity)
    {
        String camelName = "";
        for (Field f : entity.getClass().getDeclaredFields())
        {
            if (f.isAnnotationPresent(Id.class))
            {
                camelName = f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
                try {
                    for (Method setMethod : entity.getClass().getDeclaredMethods())
                    {
                        if (setMethod.getName().equals("set" + camelName))
                        {
                            setMethod.invoke(entity, -9999);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    @Transactional
    public void edit(T entity) {
//        getEntityManager().getTransaction().begin();
        getEntityManager().merge(entity);
        //getEntityManager().flush();
//        getEntityManager().getTransaction().commit();
//        getEntityManager().detach(entity);
//        getEntityManager().clear();
    }

    @Transactional
    public void remove(T entity) {
//        getEntityManager().getTransaction().begin();
//        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().remove(entity);
        //getEntityManager().flush();
//        getEntityManager().getTransaction().commit();
//        getEntityManager().detach(entity);
//        getEntityManager().clear();
    }

    @Transactional
    public T find(T entity) {
        Object o = null;
        if (entity.getClass().isAnnotationPresent(Entity.class))
        {
            try{
                CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
                CriteriaQuery cq = cb.createQuery(entity.getClass());
                Root from = cq.from(entity.getClass());
                initFilter(entity,cb,cq,from);
                TypedQuery<T> q = getEntityManager().createQuery(cq);
                o = q.getSingleResult();
//                getEntityManager().detach(o);
            }
            catch(Exception ex)
            {
                if (DebugOn)
                {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return (T)o;
    }

    @Transactional
    public List<T> findAll() 
    {
        List<T> records = null;
        try
        {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(getEntityClass()));
            records = getEntityManager().createQuery(cq).getResultList();
//            getEntityManager().detach(records);
        }
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return records;
    }
    
    @Transactional
    public List<T> findAll(T entity) {
        List<T> records = null;
        try
        {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(entity.getClass());
            Root<T> from = cq.from(entity.getClass());
            initFilter(entity,cb,cq,from);
            TypedQuery<T> q = getEntityManager().createQuery(cq);
            records = q.getResultList();
//            getEntityManager().detach(records);
        }
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return records;
    }

    @Transactional
    public List<T> findRange(int[] range) {
        
        List<T> records = null;
        try
        {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(getEntityClass()));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
            records = q.getResultList();
//            getEntityManager().detach(records);
        }
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return records;
    }
    
    @Transactional
    public List<T> findRange(T entity, int[] range) {
        List<T> records = null;
        try
        {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(entity.getClass());
            Root<T> from = cq.from(entity.getClass());
            initFilter(entity,cb,cq,from);
            TypedQuery<T> q = getEntityManager().createQuery(cq);
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
            records = q.getResultList();
//            getEntityManager().detach(records);
        }
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return records;
    }

    @Transactional
    public int count(T entity) {
        int total = 0;
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            javax.persistence.criteria.Root<T> rt = cq.from(getEntityClass());
            initFilter(entity,cb,cq,rt);
            cq.select(cb.count(rt));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            total = ((Long) q.getSingleResult()).intValue();
            getEntityManager().clear();
        }
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }
    
    @Transactional
    public int count() {
        int total = 0;
        
        try {
            total = count(getEntityClass().newInstance());
        } 
        catch (Exception ex) 
        {
            if (DebugOn)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return total;
    }
    
    protected Predicate buildPredicates(T entity,CriteriaBuilder cb, CriteriaQuery cq, Root from)
    {
        Predicate p = null;
        String methodName = "";
        List<Field> fields = ((EntityBase)entity).getAllFields();
        List<Method> methods = ((EntityBase)entity).getAllMethods();
        for (Field f : fields)
        {
            if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(JoinColumn.class))
            {
                methodName = "get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
                
                for (Method m : methods)
                {
                    try {
                        if (m.getName().equals(methodName) && m.invoke(entity) != null)
                        {
                            if (p == null)
                            {
                                p = cb.and(cb.equal(from.get(f.getName()),m.invoke(entity)));
                            }
                            else
                            {
                                p = cb.and(p,cb.equal(from.get(f.getName()),m.invoke(entity)));
                            }
                            break;
                        }
                    } 
                    catch (Exception ex) 
                    {
                        if (DebugOn)
                        {
                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        
        return p;
    }
    
    protected T initFilter(T entity,CriteriaBuilder cb, CriteriaQuery cq, Root from)
    {
        Predicate p = buildPredicates(entity, cb, cq, from);
        
        if (p != null)
        {
            cq.where(p);
        }
        
        return (T)this;
    }
    
    public T initEntityManager(){
        if (em == null)
        {
            em = Persistence.createEntityManagerFactory(getDefaultPU()).createEntityManager();
        }
        return (T)this;
    }
    
    // SETTERS
    public T setDefaultPU(String pu){DefaultPU = pu;return (T)this;}
    public T setEntityClass(Class<T> entityClass){
        this.entityClass = entityClass;
        return (T)this;
    }
    public T setDebug(Boolean yesno){DebugOn=yesno;return (T)this;}
    
    // GETTERS
    public Class<T> getEntityClass(){return this.entityClass;}
    public String getDefaultPU(){return DefaultPU;}
    protected EntityManager getEntityManager() {initEntityManager(); return em;}
    public void detach(T entity)
    {
        getEntityManager().detach(entity);
    }
    public void detach(List<T> entities)
    {
        getEntityManager().detach(entities);
    }
    public void clear()
    {
        getEntityManager().clear();
    }
    public void flush()
    {
        getEntityManager().flush();
    }
    public void refresh(T entity)
    {
        getEntityManager().refresh(entity);
    }
    
//    public static void main(String[] args) throws Exception
//    {
//        BaseFacade f = new BaseFacade(com.gei.entities.HydrologyReport.class,"HYDROPU");
//        f.setDebug(true);
//        java.util.Map test = new java.util.HashMap();
////        test.put("summary","test");
//        test.put("hydrologyReportId", 11);
//        com.gei.entities.HydrologyReport r = new com.gei.entities.HydrologyReport();
//        r.loadProperties(test);
////        System.out.printf("%s\n",r);
//        System.out.printf("%s\n",f.find(r));
////        System.out.printf("%s\n", f.findAll());
//    }
}
