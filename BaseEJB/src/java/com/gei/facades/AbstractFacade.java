/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.facades;

import com.gei.facades.delegates.LoadQueryDelegate;
import com.gei.facades.delegates.QueryDelegate;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author clay
 */
public abstract class AbstractFacade<F,E extends Serializable> {
    private Class<E> entityClass;
    private E entity;
    protected EntityManager entityManager;
    protected Boolean _DEBUG_ON = false;
    
    public AbstractFacade(Class<E> entityClass) {
        this.entityClass = entityClass;
      try {
        this.entity = this.entityClass.newInstance();
      } catch (InstantiationException | IllegalAccessException ex) {
        if (_DEBUG_ON)
        {
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    public abstract String getPersistenceUnitName();
    public abstract F setEntityManager(EntityManager em);
    
    public EntityManager getEntityManager(){
      if (entityManager == null){
        setEntityManager(Persistence.createEntityManagerFactory(getPersistenceUnitName()).createEntityManager());
      }
      return entityManager;
    }

    @Transactional
    public F create(E entity) {
        getEntityManager().persist(entity);
        return (F)this;
    }

    @Transactional
    public F edit(E entity) {
        getEntityManager().merge(entity);
        return (F)this;
    }

    @Transactional
    public F remove(E entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        return (F)this;
    }

    @Transactional
    public E find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }
    
    @Transactional
    public E find()
    {
      return find(getEntity());
    }
    
    @Transactional
    public E find(E entity)
    {
      try
      {
        return createQuery(entity).getSingleResult();
      }catch(NoResultException ex){}
      
      return null;
    }

    @Transactional
    public List<E> findAll() {
      return findAll(getEntity());
    }
    
    @Transactional
    public List<E> findAll(E entity)
    {
        return createQuery(entity).getResultList();
    }

    @Transactional
    public List<E> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Transactional
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<E> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    protected Predicate buildPredicate(CriteriaBuilder cb, CriteriaQuery cq, E entity)
    {
      return buildPredicate(cb, cq, cq.from(entityClass), entity);
    }
    
    protected Predicate buildPredicate(CriteriaBuilder cb, CriteriaQuery cq, Root<E> from, E entity)
    {
//        Root from = cq.from(entityClass);
        Predicate p = null;

        try
        {
            for (Field f : entityClass.getDeclaredFields())
            {
                if (f.isAnnotationPresent(Column.class))
                {
                    Method m = entityClass.getDeclaredMethod("get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1));
                    if (m != null && m.invoke(entity) != null)
                    {
                        if (p == null)
                        {
                            p = cb.and(cb.equal(from.get(f.getName()), m.invoke(entity)));
                        }
                        else
                        {
                            p = cb.and(p, cb.equal(from.get(f.getName()), m.invoke(entity)));
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
          if (_DEBUG_ON)
          {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
          }
        }
        
        return p;
    }
    
    protected Predicate buildLikePredicate(CriteriaBuilder cb, CriteriaQuery cq, E entity)
    {
        Root from = cq.from(entityClass);
        Predicate p = null;

        try
        {
            String value = "";
            
            for (Field f : entityClass.getDeclaredFields())
            {
                if (f.isAnnotationPresent(Column.class))
                {
                    Method m = entityClass.getDeclaredMethod("get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1));
                    if (m != null && m.invoke(entity) != null)
                    {
                        value = (m.invoke(entity) + "").toUpperCase();
                        
                        if (p == null)
                        {
                            p = cb.and(cb.like(cb.upper(cb.toString(from.get(f.getName()))), value));
                        }
                        else
                        {
                            p = cb.and(p, cb.like(cb.upper(cb.toString(from.<String>get(f.getName()))), value));
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
          if (_DEBUG_ON)
          {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
          }
        }
        
        return p;
    }
    
    protected TypedQuery<E> createLikeQuery(E entity)
    {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(entityClass);
        Predicate p = buildLikePredicate(cb,cq,entity);
        if (p != null)
        {
            cq.where(p);
        }
        TypedQuery<E> q = getEntityManager().createQuery(cq);
        
        return q;
    }
    
    protected TypedQuery<E> createQuery(E entity)
    {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(entityClass);
        Predicate p = buildPredicate(cb,cq,entity);
        if (p != null)
        {
            cq.where(p);
        }
        TypedQuery<E> q = getEntityManager().createQuery(cq);
        
        return q;
    }
    
    public F set(String prop, Object val)
    {
      String methodName = "set" + prop.substring(0,1).toUpperCase() + prop.substring(1);
      for (Method m : getEntity().getClass().getDeclaredMethods())
      {
        if(m.getName().equals(methodName))
        {
          try{
            m.invoke(getEntity(), val);
          }
          catch(Exception ex)
          {
            if (_DEBUG_ON)
            {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
          }
          break;
        }
      }
      return (F) this;
    }
    
    public Object get(String prop)
    {
      Object val = null;
      String methodName = "get" + prop.substring(0,1).toUpperCase() + prop.substring(1);
      for (Method m : getEntity().getClass().getDeclaredMethods())
      {
        if(m.getName().equals(methodName))
        {
          try{
            val = m.invoke(getEntity());
          }
          catch(Exception ex)
          {
            if (_DEBUG_ON)
            {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
          }
          break;
        }
      }
      
      return val;
    }
    
    public F loadProperties(JSONObject json){
      ((com.gei.entities.AbstractEntity)this.getEntity()).loadProperties(json);
      return (F)this;
    }
    
    public F loadProperties(java.util.Map map)
    {
      ((com.gei.entities.AbstractEntity<E>)getEntity()).loadProperties(map);
      
      return (F) this;
    }
    
    public F loadProperties(javax.servlet.http.HttpServletRequest request)
    {
      ((com.gei.entities.AbstractEntity<E>)getEntity()).loadProperties(request);
      return (F) this;
    }
    
    public F reset()
    {
      try {
        setEntity(entityClass.newInstance());
      } catch (InstantiationException | IllegalAccessException ex) {
        if (_DEBUG_ON)
        {
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
      }
      return (F) this;
    }
    
    public F debugOn(){_DEBUG_ON=true;return (F) this;}
    public F debugOff(){_DEBUG_ON=false;return (F) this;}
    
    @Transactional
    public <ET extends com.gei.entities.AbstractEntity> List<ET> select(String query, Class<ET> type)
    {
      return getEntityManager().createNativeQuery(query,type).getResultList();
    }
    
    public String toCamelCase(String name) {
      String camelName = "";

      for (String n : name.split("_"))
      {
        if (camelName.isEmpty())
        {
          camelName = n.toLowerCase();
        }
        else
        {
          camelName += n.substring(0,1).toUpperCase() + n.substring(1).toLowerCase();
        }
      }

      return camelName;
    }
    
    public F defaultSelectCallback(java.sql.ResultSet rs,List<Map> results) throws SQLException
    {
      java.sql.ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next())
      {
        results.add(new java.util.LinkedHashMap());
        for (int i=1;i<=rsmd.getColumnCount();++i)
        {
          results.get(results.size()-1).put(toCamelCase(rsmd.getColumnName(i)), rs.getObject(i));
        }
      }
      
      return (F)this;
    }
    
    @Transactional
    public <LQD extends LoadQueryDelegate> List select(final String query, final LQD delegate)
    {
      if (delegate == null) {
        throw new NullPointerException("The LaodQueryDelegate is not defined");
      }
      
      final List results = new ArrayList();
      getEntityManager()
      .unwrap(org.hibernate.Session.class)
      .doWork(new org.hibernate.jdbc.Work() {
        @Override
        public void execute(Connection conn) throws SQLException {
          try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
              delegate.loadQuery(rs, results);
            } catch (Exception ex) {
              if (_DEBUG_ON)
              {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
              }
            }            
          } catch (Exception ex) {
            if (_DEBUG_ON)
            {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
          } 
        }
      });      
      return results;
    }
    
    @Transactional
    public List select(final String query, final String methodName, final Object methodScope)
    {
      final List results = new ArrayList();
      getEntityManager()
      .unwrap(org.hibernate.Session.class)
      .doWork(new org.hibernate.jdbc.Work() {
        @Override
        public void execute(Connection conn) throws SQLException {
          try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
              Method m = methodScope.getClass().getMethod(methodName, java.sql.ResultSet.class,List.class);
              m.invoke(methodScope,rs,results);
            } catch (Exception ex) {
              if (_DEBUG_ON)
              {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
              }
            }            
          } catch (Exception ex) {
            if (_DEBUG_ON)
            {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
          }
//          finally {
//            conn.close();
//          }
        }
      });      
      return results;
    }
    
    public List select(final String query, final String methodName)
    {
      return select(query,methodName,this);
    }
    
    
    @Transactional
    public <QD extends QueryDelegate> List select(final String query, final List params, final QD delegate) {
      if (StringUtils.isEmpty(query)) {
        throw new NullPointerException("The query parameter cannot be unassigned!");
      }
      if (delegate == null) {
        throw new NullPointerException("The query delegate cannot be unassigned");
      }
      final List results = new ArrayList();
      getEntityManager()
      .unwrap(org.hibernate.Session.class)
      .doWork(new org.hibernate.jdbc.Work() {
        @Override
        public void execute(Connection conn) throws SQLException {
          try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (params != null) {
              int i=1;
              Integer sqlType = null;
              for (Object o : params.toArray()){
                if (o == null){
                  try {
                    sqlType = pstmt.getParameterMetaData().getParameterType(i);
                  } catch (Exception e) {
                    sqlType = Types.VARCHAR;
                  }          
                  pstmt.setNull(i++, sqlType);
                }
                else {
                  pstmt.setObject(i++, o);
                }
              }
            }
            
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
              delegate.handle(rs);
            }
          } 
          catch (Exception ex) {
            throw new IllegalStateException(
              String.format("%s.%s %s:\n%s"
                ,this.getClass().getName()
                ,"select(String,List,QueryDelegate)"
                ,ex.getClass().getName()
                ,ex.getMessage()
              )
            );
          } 
        }
      });      
      return results;
    }
    
    
//  @Transactional
//  public boolean executeUpdate(final String query){
//    return executeUpdate(query,null);
//  }

  @Transactional
  public boolean executeUpdate(final String query,final Object... values){
    boolean success = true;
    try {
      getEntityManager()
      .unwrap(org.hibernate.Session.class)
      .doWork(new org.hibernate.jdbc.Work() {
        @Override
        public void execute(Connection conn) throws SQLException {
          try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            fillStatement(pstmt,values);
            pstmt.executeUpdate();
          } catch (Exception ex) {
            throw ex;
          } 
          finally {
            conn.close();
          }
        }
      });
    }
    catch(Exception ex){
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
      success = false;
    }
    finally {
      return success;
    }
  }
  
//  @Transactional
//  public boolean executeQuery(final String query){
//    return executeUpdate(query,null);
//  }

  @Transactional
  public List<Map> executeQuery(final String query,final Object... values){
    final List<Map> results = new ArrayList<>();
    try {
      getEntityManager()
      .unwrap(org.hibernate.Session.class)
      .doWork(new org.hibernate.jdbc.Work() {
        @Override
        public void execute(Connection conn) throws SQLException {
          try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            fillStatement(pstmt,values);
            try(ResultSet rs = pstmt.executeQuery()){
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              Map map;
              String column;
              Object value;
              while (rs.next()){
                map = new LinkedHashMap<>();
                for (int i=1;i<=columnCount;++i){
                  column = rsmd.getColumnName(i);
                  value = rs.getObject(column);
                  map.put(column, value);
                }
                results.add(map);
              }
            }
          } catch (Exception ex) {
            throw ex;
          } 
        }
      });
    }
    catch(Exception ex){
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
    finally {
      return results;
    }
  }
  
  /**
   * 
   * @param <F>
   * @param pstmt
   * @param objects
   * @return
   * @throws SQLException 
   */
  private F fillStatement(PreparedStatement pstmt,Object... objects) throws SQLException{
    if (objects != null && objects.length > 0){
      int i=1;
      for (Object o : objects){
        if (o == null){
          int sqlType = Types.VARCHAR;
          sqlType = pstmt.getParameterMetaData().getParameterType(i);
          pstmt.setNull(i++, sqlType);
        }
        else {
          pstmt.setObject(i++, o);
        }
      }
    }
    return (F)this;
  }
    
    @Transactional
    public List<Map> select(final String query)
    {
      return select(query,"defaultSelectCallback");
    }
    
    public F defaultSelectArrayCallback(java.sql.ResultSet rs, List<List> results) throws SQLException
    {
      java.sql.ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next())
      {
        results.add(new java.util.ArrayList());
        for (int i=1;i<=rsmd.getColumnCount();++i)
        {
          results.get(results.size()-1).add(rs.getObject(i));
        }
      }          
      
      return (F)this;
    }
    
    @Transactional
    public List<List> selectArray(final String query)
    {
      return select(query,"defaultSelectArrayCallback");
    }
    
    public static String buildBeans(String facadesPath)
    {
      String beans="";
      
      facadesPath = facadesPath != null && !facadesPath.trim().isEmpty() ? facadesPath : "./src/java/com/gei/facades/";
      java.io.File path = new java.io.File(facadesPath);
      String beanTpl = "<bean id=\"%1$s\" class=\"com.gei.facades.%1$s\" scope=\"%2$s\" />\n";
      for (String f : path.list())
      {
        if (f.indexOf(".") > -1)
        {
          beans += String.format(beanTpl, f.split("\\.")[0], "request");
        }
      }
      
      return beans;
    }

  /**
   * @return the entity
   */
  public E getEntity() {
    if (this.entity == null){
      try {
        this.entity = this.entityClass.newInstance();
      } 
      catch (Exception ex) {
        if (_DEBUG_ON)
        {
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return entity;
  }

  /**
   * @param entity the entity to set
   */
  public F setEntity(E entity) {
    this.entity = entity;
    return (F)this;
  }
  
  @Transactional
  public F save(){
    return this.save(entity);
  }
  
  @Transactional
  public F save(E entity){
    if (((com.gei.entities.AbstractEntity)entity).getId() == null){
      this.create(entity);
    }
    else {
      this.edit(entity);
    }
    return (F)this;
  }
  
  protected EntityTransaction entityTransaction;
  public F beginTransaction(){
    if (entityTransaction == null || !entityTransaction.isActive()){
      entityTransaction = this.getEntityManager().getTransaction();
    }
    
    entityTransaction.begin();
    return (F) this;
  }
  
  public F endTransaction(){
    if (entityTransaction != null && entityTransaction.isActive()){
      entityTransaction.commit();
    }
    return (F)this;
  }
//    /* Bean Generator */
//    public static void main(String[] args)
//    {
//      String facadesPath = "./src/java/com/gei/facades/";
//      java.io.File path = new java.io.File(facadesPath);
//      String beanTpl = "<bean id=\"%1$s\" class=\"com.gei.facades.%1$s\" scope=\"%2$s\" />\n";
//      for (String f : path.list())
//      {
//        if (f.indexOf(".") > -1)
//        {
//          System.out.printf(beanTpl, f.split("\\.")[0], "request");
//        }
//      }
//    }
/**
 *
Entity find/replace
===========================
import .*Named.*\r\n|@NamedQueries.*\r\n|@NamedQuery.*\r\n

public class (.*) implements
public class $1 extends AbstractEntity<$1> implements

(private\s|public\s|\()Serializable
$1Double

import .*Size;$\r\n|^\s.*@Size.*$\r\n


Facade find/replace
=============================
AbstractFacade<(.*)>
AbstractFacade<$1Facade,$1>

@PersistenceContext\((.*)\)
@PersistenceContext\($1,type = PersistenceContextType.EXTENDED\)

(import .*Context;)
$1\r\nimport javax.persistence.PersistenceContextType;

private EntityManager em;\r\n\r\n.*@Override\r\n.*EntityManager.*\r\n.*return.*\r\n.*
public Facade setEntityManager\(EntityManager em\) \{\r\n\t\tentityManager = em;\r\n\t\treturn this;\r\n\t\}

(public class )(.*)( extends .*\r\n.*\r\n.*public )(Facade)
$1$2$3$2 

^\}$
\t@Override\r\n\tpublic String getPersistenceUnitName\(\)\{return "";\}\r\n\}
 /**/
}
