package com.gei.entities;

import com.gei.reflection.ReflectionHelper;
import com.gei.util.EntityUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import org.springframework.util.StringUtils;

/**
 *
 * @author clay
 */
public class AbstractEntity<T> 
{
  private Boolean debug=false;
  public T debugOn(){debug=true;return (T)this;}
  public T debugOff(){debug=false;return (T)this;}
  public Boolean debug(){return debug;}
  
  private List<Exception> exceptionErrors;
  public T errorlog(Exception ex){
    if (exceptionErrors == null){
      exceptionErrors = new ArrayList<>();
    }
    exceptionErrors.add(ex);
    return (T)this;
  }
  
  public String getErrorLogs(){
    String errors = "",del="";
    for (Exception ex : exceptionErrors){
      errors += del + ex.getMessage();
      del = "\n";
    }
    return errors;
  }
  
    public T loadProperties(java.util.Map map)
    {
        for (Map.Entry<String,Object> entry : (Set<Map.Entry>)map.entrySet())
        {
            invokeSetMethod(entry.getKey(),entry.getValue());
        }
        
        return (T) this;
    }
    
    public T loadProperties(org.json.JSONObject json){
      for (Map.Entry<String,Object> entry : (Set<Map.Entry>)json.getJSONMap().entrySet()){
        invokeSetMethod(entry.getKey(),entry.getValue());
      }

      return (T) this;
    }
    
    public T loadProperties(javax.servlet.http.HttpServletRequest request){
      for (Map.Entry<String,String[]> entry : request.getParameterMap().entrySet()){
        for (String s : entry.getValue()){
          invokeSetMethod(entry.getKey(), entry.getValue()[0]);
        }
      }

      return (T) this;
    }
    
    private T invokeSetMethod(String col, Object value)
    {
        String methodName = "set" + col.substring(0,1).toUpperCase() + col.substring(1);
        
        List<Method> methods = Arrays.asList(this.getClass().getMethods());
        
        for (Method setMethod : methods){
            if (setMethod.getName().equalsIgnoreCase(methodName)){
              try {                                
                if (value != null)
                {
                  Class ptype = setMethod.getParameterTypes()[0];

                  if (ptype.equals(Integer.class) || ptype.equals(int.class))
                  {
                    value = Integer.parseInt(value.toString());
                  }
                  else if (ptype.equals(Short.class) || ptype.equals(short.class))
                  {
                    value = Short.parseShort(value.toString());
                  }
                  else if (ptype.equals(Long.class) || ptype.equals(long.class))
                  {
                    value = Long.parseLong(value.toString());
                  }
                  else if (ptype.equals(Float.class) || ptype.equals(float.class) )
                  {
                    value = Float.parseFloat(value.toString());
                  }
                  else if (ptype.equals(Double.class) || ptype.equals(double.class) )
                  {
                    value = Double.parseDouble(value.toString());
                  }
                  else if (ptype.equals(Boolean.class) || ptype.equals(boolean.class) )
                  {
                    value = Boolean.parseBoolean(value.toString());
                  }
                  else if (ptype.equals(Byte.class) || ptype.equals(byte.class) )
                  {
                    value = Byte.parseByte(value.toString());
                  }
                  else if (ptype.equals(char.class))
                  {
                    value = value.toString().charAt(0);
                  }
                }                
                setMethod.invoke(this, value);
              } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                if (debug){
                  Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
                  errorlog(ex);
                }
              }
              break;// break out of look if method found;
            }        
        }// end of class method loop
        return (T)this;
    }
    
    public Map toMap(){
      LinkedHashMap map = new LinkedHashMap();
      String methodName;
      Method m;
      Object val;
      for (Field f : this.getClass().getDeclaredFields()){
        if (f.isAnnotationPresent(Column.class)){
          methodName = "get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1);
          try {
            m = this.getClass().getDeclaredMethod(methodName);
            val = m.invoke(this);
            if (val instanceof AbstractEntity){
              map.put(f.getName(), ((AbstractEntity)val).toMap());
            }
            else if (val instanceof Collection) {
              
            }
            else {
              val = val == null ? "" : val;
              map.put(f.getName(), val);
            }
          } 
          catch (Exception ex) {}
        }
      }

      return map;
    }
    
    public Object getId()
    {
      Object o = null;
      for (Field f : this.getClass().getDeclaredFields())
      {
        if (f.isAnnotationPresent(Column.class) && f.isAnnotationPresent(Id.class))
        {
          try {
            Method m = this.getClass().getDeclaredMethod("get" + f.getName().substring(0,1).toUpperCase() + f.getName().substring(1));
            o = m.invoke(this);
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
          }
          break;
        }
      }
      
      return o;
    }
    
    public static <E extends AbstractEntity> String getColumn(Class<E> entityClass, String propName){
      String colName = null;
      try {
        Field f = ReflectionHelper.getField(entityClass, propName);
        if (f != null){
          Column col = f.getAnnotation(Column.class);
          if (col != null){
            colName = col.name();
          }
        }
      }
      catch(Exception ex){
        Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
      }
      finally {
        return colName;
      }
    }
    
  //<editor-fold defaultstate="collapsed" desc="Load(ResultSet)">
  public <E extends AbstractEntity> E Load(ResultSet rs) {
    return this.Load(rs, null);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Load(ResultSet)">
  public <E extends AbstractEntity> E Load(ResultSet rs, String colPrefix) {
    try {
      Field[] fields = null;
      if ((fields = EntityUtil.getFields(this.getClass())) != null) {
        if (StringUtils.isEmpty(colPrefix)) {
          colPrefix = "";
        }
        Column col = null;
        String colName = null;
        Object value = null;
        for (Field f : fields) {
          if (!f.isAnnotationPresent(Column.class)) {
            continue;
          }
          col = f.getAnnotation(Column.class);
          colName = colPrefix + col.name();
          
          value = rs.getObject(colName, f.getType());
          
          if (!rs.wasNull()) {            
            f.setAccessible(true);
            f.set(this, value);
          }
        }
      }
    } 
    catch (Exception e) {
      throw new IllegalStateException(
        String.format("%s.Load(ResultSet) %s:\n%s"
          ,this.getClass().getName()
          ,e.getClass().getName()
          ,e.getMessage()
        )
      );
    }
  
    return (E)this;
  }
  //</editor-fold>
}
