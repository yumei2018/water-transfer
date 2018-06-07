/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soi
 */
public final class ReflectionHelper {
  public static Object invoke(Object scope, String methodName, Object... params){
    Object rtnVal=null;
    Method m = getMethod(scope, methodName);
    if (m != null){
      if (m.getName().equalsIgnoreCase(methodName)){
        try {
          rtnVal = m.invoke(scope, params);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(ReflectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
          Logger.getLogger(ReflectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
          Logger.getLogger(ReflectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return rtnVal;
  }
  
  public static Method getMethod(Object scope, String methodName){
    Method method = null;
    try {
      for (Method m : scope.getClass().getDeclaredMethods()){
        if (m.getName().equalsIgnoreCase(methodName)){
          method = m;
          break;
        }
      }
      for (Method m : scope.getClass().getMethods()){
        if (m.getName().equalsIgnoreCase(methodName)){
          method = m;
          break;
        }
      }
    }
    catch(Exception ex){
      
    }
    finally {
      return method;
    }
  }
  
  public static Field getField(Object scope, String fieldName){
    return getField(scope.getClass(),fieldName);
  }
  
  public static Field getField(Class scope, String fieldName){
    Field f = null;
    try {
      f = scope.getDeclaredField(fieldName);
    }
    catch(Exception ex){
      try {
        f = scope.getField(fieldName);
      }
      catch(Exception ex2){
        
      }
    }
    finally {
      return f;
    }
  }
}
