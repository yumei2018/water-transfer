/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.entities;

import com.gei.facades.BaseFacade;
import com.gei.utils.ServletRequestUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

/**
 *
 * @author soi
 * @param <T>
 */
public class EntityBase<T extends EntityBase> implements Serializable
{
    protected Boolean mDebugOn=false;
    protected BaseFacade<T> mFacade;
    public BaseFacade<T> getFacade(){return mFacade;}
    
    public List<Method> getAllMethods()
    {
        List<Method> methods = new ArrayList<>();
        for (Class<?> c = this.getClass(); c != null; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods;
    }
    
    public List<Field> getAllFields()
    {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = this.getClass(); c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
    
    private String toCamelName(String name)
    {
        String camelName = "";
        
        if (name.indexOf("_") > -1)
        {
            String[] pieces = name.split("_");
            for (String p : pieces)
            {
                camelName += p.substring(0,1).toUpperCase() + p.substring(1).toLowerCase();
            }
        }
        else
        {
            camelName = name.substring(0,1).toUpperCase()+ name.substring(1);
        }
        
        return camelName;
    }
    
    public Map getMap()
    {
        Map map = new LinkedHashMap();
        Method m = null;
        for (Field f : getAllFields())
        {
            try {
                if (f.isAnnotationPresent(Column.class))
                {
                    m = this.getClass().getMethod("get" + toCamelName(f.getName()));//f.getName().substring(0,1).toUpperCase() + f.getName().substring(1));
                    
                    if (m != null)
                    {
                        map.put(f.getName(), m.invoke(this));
                    }
                }
                else if (f.isAnnotationPresent(JoinColumn.class))
                {
//                    m = this.getClass().getMethod("get" + toCamelName(f.getName()));//f.getName().substring(0,1).toUpperCase() + f.getName().substring(1));
//                    
//                    if (m != null)
//                    {
//                        EntityBase eb = (EntityBase) m.invoke(this);
//                        eb.getClass().getDeclaredFields()
//                        String name = f.getName();//getAnnotation(JoinColumn.class).name();
//                        m = eb.getClass().getMethod("get" + toCamelName(name));//name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase());
//                        if (m != null)
//                        {
//                            map.put(f.getName(), m.invoke(eb));
//                        }
//                    }
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(EntityBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return map;
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s]\n",this.getClass().getName(),getMap().toString());
    }
    
    public T loadProperties(javax.servlet.http.HttpServletRequest request)
    {
        for (Map.Entry<String,String[]> entry : (Set<Map.Entry<String,String[]>>)request.getParameterMap().entrySet())
        {
            initSetMethod(entry.getKey(), new ServletRequestUtil(request));
        }// end of request params loop

        return (T)this;
    }
    
    public T loadProperties(Map map)
    {
//        Method setMethod;
        String methodName,col;

        for (Map.Entry<String,String[]> entry : (Set<Map.Entry<String,String[]>>)map.entrySet())
        {
            initSetMethod(entry.getKey(), map);
        }// end of request params loop

        return (T)this;
    }
    
    private T initSetMethod(String col, Map map)
    {
        String methodName = "set" + col.substring(0,1).toUpperCase() + col.substring(1);
        
        for (Method setMethod : this.getClass().getMethods())
        {
            if (setMethod.getName().equals(methodName))
            {
                for (Class c : setMethod.getParameterTypes())
                {
                    try {
                        if (c.equals(String.class))
                        {
                            setMethod.invoke(this, map.get(col));
                            break;
                        }

                        if (c.equals(Long.class) || c.equals(long.class))
                        {
                            setMethod.invoke(this, Long.parseLong("" + map.get(col)));
                            break;
                        }

                        if (c.equals(Integer.class) || c.equals(int.class))
                        {
                            setMethod.invoke(this, Integer.parseInt("" + map.get(col)));
                            break;
                        }

                        if (c.equals(Double.class) || c.equals(double.class))
                        {
                            setMethod.invoke(this, Double.parseDouble("" + map.get(col)));
                            break;
                        }

                        if (c.equals(Float.class) || c.equals(float.class))
                        {
                            setMethod.invoke(this, Float.parseFloat("" + map.get(col)));
                            break;
                        }

                        if (c.equals(Boolean.class))
                        {
                            setMethod.invoke(this, Boolean.parseBoolean("" + map.get(col)));
                            break;
                        }
                    }catch (Exception ex){}
                }
                break;// break out of look if method found;
            }// end methodname equal
        }// end of class method loop
        return (T)this;
    }
    
    private T initSetMethod(String col, ServletRequestUtil requestUtil)
    {
        String methodName = "set" + col.substring(0,1).toUpperCase() + col.substring(1);
        
        for (Method setMethod : this.getClass().getMethods())
        {
            if (setMethod.getName().equals(methodName))
            {
                for (Class c : setMethod.getParameterTypes())
                {
                    try {
                        if (c.equals(String.class))
                        {
                            setMethod.invoke(this, requestUtil.getString(col));
                            break;
                        }

                        if (c.equals(Long.class) || c.equals(long.class))
                        {
                            if (requestUtil.getLong(col) != requestUtil.INVALID_INTEGER)
                            {
                                setMethod.invoke(this, requestUtil.getLong(col));
                            }
                            break;
                        }

                        if (c.equals(Integer.class) || c.equals(int.class))
                        {
                            if (requestUtil.getLong(col) != requestUtil.INVALID_INTEGER)
                            {
                                setMethod.invoke(this, requestUtil.getInt(col));
                            }
                            break;
                        }

                        if (c.equals(Double.class) || c.equals(double.class))
                        {
                            if (requestUtil.getLong(col) != requestUtil.INVALID_NUMBER)
                            {
                                setMethod.invoke(this, requestUtil.getDouble(col));
                            }
                            break;
                        }

                        if (c.equals(Float.class) || c.equals(float.class))
                        {
                            if (requestUtil.getLong(col) != requestUtil.INVALID_NUMBER)
                            {
                                setMethod.invoke(this, requestUtil.getFloat(col));
                            }
                            break;
                        }

                        if (c.equals(Boolean.class))
                        {
                            setMethod.invoke(this, requestUtil.getBoolean(col));
                            break;
                        }
                    }catch (Exception ex){}
                }
                break;// break out of look if method found;
            }// end methodname equal
        }// end of class method loop
        return (T)this;
    }

    public Boolean getMDebugOn() {
        return mDebugOn;
    }

    public void setMDebugOn(Boolean mDebugOn) {
        this.mDebugOn = mDebugOn;
    }
}
