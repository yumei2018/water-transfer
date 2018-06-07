package com.gei.util;

import com.gei.entities.AbstractEntity;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Table;
import org.springframework.util.StringUtils;

/**
 *
 * @author clay
 */
public class EntityUtil {
  
  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private static Map<Class<? extends AbstractEntity>,Table> mEntityTables;
  private static Map<Class<? extends AbstractEntity>,Field[]> mEntityFields;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Initialize Static Properties">
  static {
    EntityUtil.mEntityFields = new LinkedHashMap<>();
    EntityUtil.mEntityTables = new LinkedHashMap<>();
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Class Initialization">
  private static <E extends AbstractEntity> void initClass(Class<E> cls) {
    Table table = null;
    if (!EntityUtil.mEntityTables.containsKey(cls) && (table = cls.getAnnotation(Table.class)) != null) {
      EntityUtil.mEntityTables.put(cls, table);
    }
    
    if (!EntityUtil.mEntityFields.containsKey(cls)) {
      List<Field> allfields = new ArrayList<>();
      Field[] fields = null;
      if ((fields = cls.getDeclaredFields()) != null) {
        allfields.addAll(Arrays.asList(fields));
      }
      if ((fields = cls.getFields()) != null) {
        allfields.addAll(Arrays.asList(fields));
      }
      EntityUtil.mEntityFields.put(cls, allfields.toArray(new Field[0]));
    }
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Table Name">
  public static String getTableName(Class cls) {
    if (!EntityUtil.mEntityTables.containsKey(cls)) {
      EntityUtil.initClass(cls);
    }
    
    String result = null;
    Table table = null;
    if ((table = EntityUtil.mEntityTables.get(cls)) != null) {
      result = table.name();
    }
  
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Class Fields">
  public static Field[] getFields(Class cls) {
    if (!EntityUtil.mEntityFields.containsKey(cls)) {
      EntityUtil.initClass(cls);
    }
    
    return EntityUtil.mEntityFields.get(cls);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Columns">
  public static Column[] getColumns(Class cls) {
    Column[] result = null;
    Field[] fields = null;
    
    if ((fields = EntityUtil.getFields(cls)) != null) {
      List<Column> cols = new ArrayList<>();
      for (Field f : fields) {
        if (f.isAnnotationPresent(Column.class)) {
          cols.add(f.getAnnotation(Column.class));
        }
      }
      
      if (!cols.isEmpty()) {
        result = cols.toArray(new Column[0]);
      }
    }
    
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Field">
  public static Field getField(Class cls, String fieldName) {
    Field result = null;
    Field[] fields = null;
    if ((fields = EntityUtil.getFields(cls)) != null) {
      for (Field f : fields) {
        if (Objects.equals(f.getName(), fieldName)){
          result = f;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Column">
  public static Column getColumn(Class cls, String fieldName) {
    Column result = null;
    Field field = null;
    if ((field = EntityUtil.getField(cls, fieldName)) != null) {
      result = field.getAnnotation(Column.class);
    }
  
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Field">
  public static Field getField(Class cls, Column col) {
    Field result = null;
    Field[] fields = null;
    if ((fields = EntityUtil.getFields(cls)) != null) {
      Column fCol = null;
      for (Field f : fields) {
        if (((fCol = f.getAnnotation(Column.class)) != null)
          && (Objects.equals(fCol, col))){
          result = f;
          break;
        }
      }
    }
    
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Column Name">
  public static String getColumnName(Class cls, String fieldName) {
    String result = null;
    Column col = null;
    if ((col = EntityUtil.getColumn(cls,fieldName)) != null) {
      result = col.name();
    }
  
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Static Get Field Name">
  public static String getFieldName(Class cls, Column col) {
    Field field = null;
    String result = null;
    
    if ((field = EntityUtil.getField(cls, col)) != null) {
      result = field.getName();
    }
  
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Load(ResultSet, Object)">
  public static void Load(ResultSet rs, Object o) throws Exception {
    EntityUtil.Load(rs, o, null);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Load(ResultSet, Object, String)">
  public static void Load(ResultSet rs, Object o, String colPrefix) throws Exception {
    try {
      if (StringUtils.isEmpty(colPrefix)) {
        colPrefix = "";
      }
      Field[] fields = EntityUtil.getFields(o.getClass());
      Column col = null;
      String colName = null;
      Object value = null;
      for (Field f : fields) {
        if (!f.isAnnotationPresent(Column.class) 
          || ((col = f.getAnnotation(Column.class)) == null) 
          || (StringUtils.isEmpty(col.name()))) {
          continue;
        }
        
        colName = colPrefix + col.name();
        
        value = rs.getObject(colName, f.getType());
        
        if (!rs.wasNull()) {
          f.setAccessible(true);
          f.set(o, value);
        }
      }
    }
    catch(Exception ex) {
      String.format("%s.%s %s:\n%s"
        ,EntityUtil.class.getName()
        ,""
        ,ex.getClass().getName()
        ,ex.getMessage()
      );
    }
  }
  //</editor-fold>
}
