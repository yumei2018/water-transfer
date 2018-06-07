package gov.ca.water.common.reflection;

import gov.ca.water.common.io.DataEntry;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ReflectionInfo is a utility class for extracting reflection information class 
 * definitions.
 * @author kprins
 */
public class ReflectionInfo {

   /**
   * protected Static Logger object for logging errors, warnings, and info messages.
   */
  protected static final Logger logger = Logger.getLogger("ReflectionInfo");
 
  //<editor-fold defaultstate="collapsed" desc="Static Generic Class Access Methods">
  /**
   * <p>Call to cast a Generic Class to a Class&lt;? extend T&gt;. It is not possible to
   * assign a generic class to class variable. This will not work:</p>
   * {@code 
   *    Class<? extends List<T>> pClass = MyList<T>.class;
   * }
   * <p>Use this method to a cast pClass as a generic class as follows:</p>
   * {@code 
   *    Class<? extends List<T>> pClass = ReflectionInfo.castAsGenericClass(MyList.class);
   * }
   * @param <TBase> the base class in the generic reference
   * @param baseClass the class to cast (without it generic reference)
   * @return pClass cast as Class&lt;? extends TBase&gt;
   */
  @SuppressWarnings({"unchecked","rawtypes","cast"})
  public static <TBase> Class<? extends TBase> castAsGenericClass(Class baseClass) {
    return (Class<? extends TBase>) ((Class) baseClass);
  }
  
  /**
   * <p>Call to cast a Generic Class to a Class&lt;TBase&gt;. It is not possible to
   * assign a generic class to class variable. This will not work:</p>
   * {@code 
   *    Class<List<T>> pClass = MyList<T>.class;
   * }
   * <p>Use this method to a cast pClass as a generic class as follows:</p>
   * {@code 
   *    Class<List<T>> pClass = ReflectionInfo.castAsSpecificGenericClass(MyList.class);
   * }
   * @param <TBase> the specific base class in the generic reference
   * @param baseClass the base class to cast (without it generic reference)
   * @return pClass cast as Class&lt;TBase&gt;
   */
  @SuppressWarnings({"unchecked","rawtypes","cast"})
  public static <TBase> Class<TBase> castAsSpecificGenericClass(Class baseClass) {
    return (Class<TBase>) ((Class) baseClass);
  }
    
  /**
   * Return the Generic Argument class for a Class with one or more generic arguments.
   * @param baseClass the base (generic) class
   * @param childClass the child class for which to get the generic information
   * @param argIdx the index of the generic parameter
   * @return the child class' generic class assignment for Type Argument[ardIdx]
   * @exception Exception - errors are trapped and logged.
   */
  @SuppressWarnings({"unchecked","rawtypes"})
  public static <T> Class<?> getGenericClass(Class<T> baseClass,
          Class<? extends T> childClass, int argIdx) {
    Class result = null;
    try {
      if (baseClass == null) {
        throw new Exception("The Base Class is undefined");
      }
      
      if (childClass == null) {
        throw new Exception("The Parent Class is undefined");
      }
      
      List<Class<?>> typeArgs = ReflectionInfo.getTypeArguments(baseClass, childClass);
      if ((typeArgs != null) && (argIdx < typeArgs.size())) {
        result = typeArgs.get(argIdx);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "ReflectionInfo.getGenericClass Error:\n {0}",
              pExp.getMessage());
    }
    return result;
  }
    
  /**
   * Overload 1. Get the generic casting of the List's Item. It call {@linkplain 
   * #getGenericListItemClass(java.lang.Class) Overlaod 2} and if the class cannot be
   * retrieve it checks the list content and returns the class of the first non-null 
   * list item.
   * @param list the list whose class is to be explore
   * @return the list item class or null if listClass is a generic class and the list 
   * is empty or the list reference is null.
   * @exception Exception - errors are trapped and logged.
   */
  @SuppressWarnings("unchecked")
  public static <TItem> Class<TItem> 
                      getGenericListItemClass(List<TItem> list) {
    Class<TItem> result = null;
    try {
      if (list != null) {
        Class<List<TItem>> listClass = (Class<List<TItem>>) list.getClass();
        result = ReflectionInfo.getGenericListItemClass(listClass);
        if ((result == null) && (!list.isEmpty())) {
          for (TItem item : list) {
            if (item != null) {
              result = (Class<TItem>) item.getClass();
              break;
            }
          }
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "ReflectionInfo.getGenericListItemClass Error:\n {0}",
              pExp.getMessage());
    }
    return result;
  }
    
  /**
   * Overload 2. Get the generic casting of the List's Item.
   * @param listClass the list class to explore
   * @return the list item class or null if listClass is a generic class
   * @exception Exception - errors are trapped and logged.
   */
  @SuppressWarnings({"unchecked","rawtypes"})
  public static <TItem> Class<TItem> 
                      getGenericListItemClass(Class<? extends List<TItem>> listClass) {
    Class<TItem> result = null;
    try {
      if (listClass == null) {
        throw new Exception("The List Class is undefined");
      }
      Class baseClass = List.class;
      
      List<Class<?>> typeArgs = ReflectionInfo.getTypeArguments(baseClass, listClass);
      if ((typeArgs != null) && (!typeArgs.isEmpty())) {
        result = (Class<TItem>) typeArgs.get(0);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "ReflectionInfo.getGenericListItemClass Error:\n {0}",
              pExp.getMessage());
    }
    return result;
  }
  
  //<editor-fold defaultstate="collapsed" desc="Sttaic Classs MapEntryClasses ">
  /**
   * A Static Class for returning the Map entry's key-value classes
   */
  public static class MapEntryClasses<TKey,TValue> {
    /**
     * Placeholder for the Map's Key class
     */
    public Class<TKey >keyClass;
    /**
     * Placeholder for the Map's Value class
     */
    public Class<TValue> valueClass;
    
    /**
     * Check if both classes are defined.
     * @return true id the key and value class are assigned.
     */
    public boolean isDefined() {
      return ((this.keyClass != null) && (this.valueClass != null));
    }
  }
  //</editor-fold>
  
  /**
   * Get the generic casting of the Map's Key.
   * @param map the Map whose class is to be explored
   * @return the {@linkplain MapEntryClasses} with the extracted key and value classes
   * or null if Map class is a generic class
   * @exception Exception - errors are trapped and logged.
   */
  @SuppressWarnings({"unchecked","rawtypes"})
  public static <TKey, TValue> MapEntryClasses<TKey,TValue> 
                                          getGenericMapClasses(Map<TKey,TValue> map) {
    MapEntryClasses<TKey,TValue> result = new MapEntryClasses<>();
    try {
      if (map == null) {
        throw new Exception("The Map is undefined");
      }
      
      Class mapClass = map.getClass();
      MapEntryClasses<TKey,TValue> classes
                                      = ReflectionInfo.getGenericMapClasses(mapClass);
      if ((classes == null) || (!classes.isDefined())) { 
        if (!map.isEmpty()) {
          for (Map.Entry<TKey, TValue> entry : map.entrySet()) {
            if ((entry != null) 
                            && (entry.getKey() != null) && (entry.getValue() != null)) {
              result.keyClass = (Class<TKey>) entry.getKey().getClass();
              result.valueClass = (Class<TValue>) entry.getValue().getClass();
              break;
            }
          }
        }
      } else {
        result = classes;
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "ReflectionInfo.getGenericMapClasses Error:\n {0}",
              pExp.getMessage());
    }
    return result;
  } 
  
  /**
   * Get the generic casting of the Map's Key.
   * @param mapClass the Map class to explore
   * @return the Map key class or null if Map class is a generic class
   * @exception Exception - errors are trapped and logged.
   */
  @SuppressWarnings({"unchecked","rawtypes"})
  public static <TKey, TValue> MapEntryClasses<TKey,TValue> 
                    getGenericMapClasses(Class<? extends Map<TKey,TValue>> mapClass) {
    MapEntryClasses<TKey,TValue> result = new MapEntryClasses<>();
    try {
      if (mapClass == null) {
        throw new Exception("The List Class is undefined");
      }
      Class baseClass = Map.class;
      
      List<Class<?>> typeArgs = ReflectionInfo.getTypeArguments(baseClass, mapClass);
      if ((typeArgs != null) && (typeArgs.size() >= 2)) {
        result.keyClass = (Class<TKey>) typeArgs.get(0);
        result.valueClass = (Class<TValue>) typeArgs.get(1);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "ReflectionInfo.getGenericMapClasses Error:\n {0}",
              pExp.getMessage());
    }
    return result;
  }
  
  /**
   * Get the underlying class for a type, or null if the type is a variable type.
   * @param classType the type to explore
   * @return the class the underlying class or null if the class cannot be resolved.
   */
  public static Class<?> getClass(Type classType) {
    if (classType instanceof Class) {
      return (Class) classType;
    } else if (classType instanceof ParameterizedType) {
      return ReflectionInfo.getClass(((ParameterizedType) classType).getRawType());
    } else if (classType instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) classType).getGenericComponentType();
      Class<?> pCompClass = ReflectionInfo.getClass(componentType);
      if (pCompClass != null ) {
        return Array.newInstance(pCompClass, 0).getClass();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
  
  /**
   * Get the actual type arguments a child class has used to extend a generic base class.
   * @param baseClass the base class
   * @param childClass the child class
   * @return a list of the raw classes for the actual type arguments.
   */
  @SuppressWarnings({"unchecked","rawtypes"})
  public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass,
          Class<? extends T> childClass) {
    Map<Type, Type> resolvedTypes = new HashMap<>();
    Type classType = childClass;
    // start walking up the inheritance hierarchy until we hit baseClass
    if (!childClass.equals(baseClass)) {
      while (true) {
        if (classType instanceof Class) {
          // there is no useful information for us in raw types, so just keep going.
          Type nextType = ((Class) classType).getGenericSuperclass();
          if ((nextType == null) 
                  || (ReflectionInfo.getClass(nextType).equals(Object.class))) {
            break;
          }
          classType = nextType;
        } else {
          ParameterizedType paramType = (ParameterizedType) classType;
          Class<?> rawClass = (Class) paramType.getRawType();

          Type[] actualArgs = paramType.getActualTypeArguments();
          TypeVariable<?>[] typeParams = rawClass.getTypeParameters();
          for (int i = 0; i < actualArgs.length; i++) {
            resolvedTypes.put(typeParams[i], actualArgs[i]);
          }

          if (ReflectionInfo.getClass(classType).equals(baseClass)) {
            break;
          }

          if (!rawClass.equals(baseClass)) {
            Type nextType = rawClass.getGenericSuperclass();
            if ((nextType == null) 
                    || (ReflectionInfo.getClass(nextType).equals(Object.class))) {
              break;
            }
            classType = nextType;
          } else {
            break;
          }
        }
      }
    }
    
    // finally, for each actual type argument provided to baseClass, determine (if possible)
    // the raw class for that type argument.
    List<Class<?>> typeArgsAsClasses = new ArrayList<>();
    if (!resolvedTypes.isEmpty()) {
      Type[] actualTypeArgs;
      if (classType instanceof Class) {
        actualTypeArgs = ((Class) classType).getTypeParameters();
      } else {
        actualTypeArgs = ((ParameterizedType) classType).getActualTypeArguments();
      }

      // resolve types by chasing down type variables.
      for (Type actualType: actualTypeArgs) {
        while (resolvedTypes.containsKey(actualType)) {
          actualType = resolvedTypes.get(actualType);
        }
        Class actualClass = ReflectionInfo.getClass(actualType);
        typeArgsAsClasses.add(actualClass);
      }
    }
    return typeArgsAsClasses;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Entity Field Reflection Methods">
  /**
   * Private Method called by the getGetMethod and getSetMethod methods.
   * @param sFieldName String
   * @param sPrefix String
   * @return String
   */
  private static String getMethodName(String sFieldName, String sPrefix) {
    String sName = null;
    if ((sFieldName != null) && (!sFieldName.trim().equals(""))) {
      sFieldName = sFieldName.trim();
      String sFirstChar = sFieldName.substring(0, 1);
      String sUpperChar = sFirstChar.toUpperCase();
      sName = sFieldName.replaceFirst(sFirstChar, sUpperChar);
      if ((sPrefix != null) && (!sPrefix.trim().equals(""))) {
        sName = sPrefix.trim() + sName;
      }
    }
    return sName;
  }
  
  /**
   * Get the get Method for sField as "get"+(sField with the First Character
   * capitalized). Example: "myField" return "getMyField"
   * @param sField
   * @return String
   */
  public static String getGetMethod(String sField) {
    return ReflectionInfo.getMethodName(sField, "get");
  }
  
  /**
   * Get the get Method for sField as "set"+(sField with the First Character
   * capitalized). Example: "myField" return "setMyField"
   * @param sField
   * @return String
   */
  public static String getSetMethod(String sField) {
    return ReflectionInfo.getMethodName(sField, "set");
  }
  
  /**
   * Overload 1: Check if a Read-And-Write Field[sField] is supported by pEntClass.
   * Call Overload 2 with bReadOnly=false.
   * @param pClass a Serializable class
   * @param sField the Field's Name
   * @return true if the field's GET- and SET-methods are supported.
   */
  public static boolean hasField(Class<? extends Serializable> pClass,
          String sField) {
    return ReflectionInfo.hasField(pClass, sField, false);
  }
  
  /**
   * Overload 2: Check if sField is a valid field for Bean pClass. If bReadOnly=true,
   * it only check if a get-Method exist. Otherwise, it will check whether both a Get-
   * and Set-method exists. It returns false if either pClass or sField is undefined.
   * @param pClass a Serializable class
   * @param sField the Field's Name
   * @param bReadOnly true if the field is readOnly| false=A read and write field
   * @return true if the field GET- (and SET-) method(s) exist.
   */
  public static boolean hasField(Class<? extends Serializable> pClass,
          String sField, boolean bReadOnly) {
    boolean bResult = false;
    try {
      if (pClass == null) {
        throw new Exception("Entity Class is undefined");
      }
      
      sField = DataEntry.cleanString(sField);
      if (sField == null) {
        throw new Exception("The Field name is undefined");
      }
      
      String sGetMethod = ReflectionInfo.getGetMethod(sField);
      bResult = ReflectionInfo.hasMethod(pClass, sGetMethod);
      if ((bResult) && (!bReadOnly)) {
        String sSetMethod = ReflectionInfo.getSetMethod(sField);
        bResult = ReflectionInfo.hasMethod(pClass, sSetMethod);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.hasField Error:\n {1}",
              new Object[]{"ReflectionInfo", pExp.getMessage()});
      bResult = false;
    }
    return bResult;
  }
  
//  public static EntityFieldInfo getFieldInfo(Class<? extends Serializable> pClass,
//                                                                      String sField) {
//    EntityFieldInfo pResult = null;    
//    for (Field pField : pClass.getDeclaredFields()) {
//      pField.
//        if (pMethod.getName().equalsIgnoreCase(sMethod)) {
//          bResult = true;
//          break;
//        }
//      }
//    
//    return pResult;
//  }
  //</editor-fold>
 
  //<editor-fold defaultstate="collapsed" desc="Entity Method Reflection Methods">
  /**
   * Check if Method[sMethod] is supported by pClass.
   * @param pClass a Class that extends Serializable
   * @param sMethod the method name (case in-sensitive search)
   * @return true if the method is supported
   */
  public static boolean hasMethod(Class<? extends Serializable> pClass, String sMethod){
    boolean bResult = false;
    try {
      if (pClass == null) {
        throw new Exception("Entity Class is undefined");
      }
      
      sMethod = DataEntry.cleanString(sMethod);
      if (sMethod == null) {
        throw new Exception("The Method name is undefined");
      }
      
      for (Method pMethod : pClass.getMethods()) {
        if (pMethod.getName().equalsIgnoreCase(sMethod)) {
          bResult = true;
          break;
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.hasMethod Error:\n {1}",
              new Object[]{"ReflectionInfo", pExp.getMessage()});
      bResult = false;
    }
    return bResult;
  }
  
  /**
   * Get the pClass' Method based on a method name only.
   * @param pClass a Class that extends Serializable
   * @param sMethod the method name (case in-sensitive search)
   * @return the Entity Class' Method or null if not supported
   */
  public static Method getMethod(Class<? extends Serializable> pClass, String sMethod){
    Method pResult = null;
    try {
      if (pClass == null) {
        throw new Exception("Entity Class is undefined");
      }
      
      sMethod = DataEntry.cleanString(sMethod);
      if (sMethod == null) {
        throw new Exception("The Method name is undefined");
      }
      
      for (Method pMethod : pClass.getMethods()) {
        if (pMethod.getName().equalsIgnoreCase(sMethod)) {
          pResult = pMethod;
          break;
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.getMethod Error:\n {1}",
              new Object[]{"ReflectionInfo", pExp.getMessage()});
      pResult = null;
    }
    return pResult;
  }
  //</editor-fold>  
}
