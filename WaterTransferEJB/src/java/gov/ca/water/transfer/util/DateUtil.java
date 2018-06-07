/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.ca.water.transfer.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.springframework.util.StringUtils;

/**
 *
 * @author Charlie Lay
 */
public class DateUtil implements Serializable {
  
  /**
   * Get today's date without time
   * 
   * @return 
   */
  public final static java.util.Date getToday(){    
    return truncate(getNow());
  }
  
  /**
   * 
   * @return 
   */
  public final static java.util.Date getNow(){
    return new java.util.Date();
  }
  
  /**
   * 
   * @return 
   */
  public final static java.sql.Timestamp getNowAsTimestamp(){
    return toTimestamp(getNow());
  }
  
  /**
   * 
   * @return 
   */
  public final static java.sql.Date getTodayAsSqlDate(){
    return toSqlDate(getToday());
  }
  
  /**
   * 
   * @param d
   * @return 
   */
  public final static <D extends java.util.Date> java.sql.Timestamp toTimestamp(D dt) {
    if (dt == null) {
      return null;
    }
    java.sql.Timestamp result = null;
    if (!(dt instanceof java.sql.Timestamp)){
      result = new java.sql.Timestamp(dt.getTime());
    }
    else {
      result = (java.sql.Timestamp)dt;
    }
    return result;
  }
  
  /**
   * 
   * @param d
   * @return 
   */
  public final static <D extends java.util.Date> java.sql.Date toSqlDate(D dt) {
    if (dt == null) {
      return null;
    }
    java.sql.Date result = null;
    if (!(dt instanceof java.sql.Date)){
      result = new java.sql.Date(dt.getTime());
    }
    else {
      result = (java.sql.Date)dt;
    }
    return result;
  }
  
  /**
   * 
   * @param dt
   * @return 
   */
  public final static <D extends java.util.Date> java.util.Date truncate(D dt) {
    if (dt == null){
      return dt;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
  
  /**
   * Get the past date from date parameter minus the number of days parameter
   * 
   * @param dt java.util.Date cannot be null
   * @param days int absolute negative
   * @return 
   */
  public final static java.util.Date getDaysAgo(java.util.Date dt, int days) {
    return getDateFromTo(dt,(-1)*Math.abs(days));
  }
  
  /**
   * Get the date from date parameter adding the number days parameter
   * 
   * @param dt java.util.Date cannot be null
   * @param days int absolute positive
   * @return 
   */
  public final static java.util.Date getDaysAhead(java.util.Date dt, int days) {
    return getDateFromTo(dt,Math.abs(days));
  }
  
  /**
   * Get the past date from today's date minus the number of days parameter
   * 
   * @param days int absolute negative
   * @return 
   */
  public final static java.util.Date getDaysAgo(int days) {
    return getDaysAgo(getToday(),(-1)*Math.abs(days));
  }
  
  /**
   * Get the future date from today adding the number days parameter
   * 
   * @param days int absolute positive
   * @return 
   */
  public final static java.util.Date getDaysAhead(int days) {
    return getDaysAhead(getToday(),days);
  }
  
  /**
   * 
   * @param hours
   * @return 
   */
  public final static java.util.Date getHoursAhead(int hours) {
    return getHoursAhead(getNow(), hours);
  }
  
  /**
   * 
   * @param <D>
   * @param dt
   * @param hours
   * @return 
   */
  public final static <D extends java.util.Date> D getHoursAhead(D dt, int hours) {
    D result = null;
    
    if (dt != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      cal.add(Calendar.HOUR, Math.abs(hours));
      result = (D) cal.getTime();
    }
    
    return result;
  }
  
  /**
   * 
   * @param hours
   * @return 
   */
  public final static java.util.Date getMinutesAhead(int mins) {
    return getMinutesAhead(getNow(), mins);
  }
  
  /**
   * 
   * @param <D>
   * @param dt
   * @param hours
   * @return 
   */
  public final static <D extends java.util.Date> D getMinutesAhead(D dt, int mins) {
    D result = null;
    
    if (dt != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      cal.add(Calendar.MINUTE, Math.abs(mins));
      result = (D) cal.getTime();
    }
    
    return result;
  }
  
  /**
   * 
   * @param hours
   * @return 
   */
  public final static java.util.Date getHoursAgo(int hours) {
    return getHoursAgo(getNow(), hours);
  }
  
  /**
   * 
   * @param <D>
   * @param dt
   * @param hours
   * @return 
   */
  public final static <D extends java.util.Date> D getHoursAgo(D dt, int hours) {
    D result = null;
    
    if (dt != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      cal.add(Calendar.HOUR, Math.abs(hours) * -1);
      result = (D) cal.getTime();
    }
    
    return result;
  }
  
  /**
   * 
   * @param hours
   * @return 
   */
  public final static java.util.Date getMinutesAgo(int mins) {
    return getMinutesAgo(getNow(), mins);
  }
  
  /**
   * 
   * @param <D>
   * @param dt
   * @param hours
   * @return 
   */
  public final static <D extends java.util.Date> D getMinutesAgo(D dt, int mins) {
    D result = null;
    
    if (dt != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      cal.add(Calendar.MINUTE, Math.abs(mins) * -1);
      result = (D) cal.getTime();
    }
    
    return result;
  }
  
  /**
   * 
   * @param dt
   * @param days
   * @return 
   */
  private final static <D extends java.util.Date> D getDateFromTo(D dt, int days) {
    D result = null;
    
    if (dt != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(truncate(dt));
      cal.add(Calendar.DATE, days);
      result = (D) cal.getTime();
    }
    
    return result;
  }
  
  /**
   * Get number of days between two dates.
   * 
   * @param dt
   * @param dt2
   * @return int Postive means startDt is before endDt, otherwise negative
   */
  public final static Integer dateDiff(java.util.Date startDt, java.util.Date endDt) {
    Integer result = null;
    
    if ((startDt != null) && (endDt != null)){
      startDt = truncate(startDt);
      endDt = truncate(endDt);
      result = 0;
      int direction = startDt.before(endDt) ? 1 : -1;
      Calendar cal = Calendar.getInstance();
      if (endDt.before(startDt)) {
        cal.setTime(endDt);
        endDt = startDt;
      }
      else {
        cal.setTime(startDt);
      }
      java.util.Date runDt = cal.getTime();
      while (runDt.before(endDt)) {
        result++;
        cal.add(Calendar.DATE, 1);
        runDt = cal.getTime();
      }
      result = result * direction;
    }
    
    return result;
  }
  
  /**
   * 
   * @param year
   * @param month
   * @param day
   * @return 
   */
  public final static java.util.Date toDate(int year, int month, int day) {
    return toDate(year,month,day,0,0,0);
  }
  
  /**
   * 
   * @param year int
   * @param month int 0-11; eg. 0 = January
   * @param day int
   * @param hour int
   * @param min int
   * @param second int
   * @return java.util.Date;
   */
  public final static java.util.Date toDate(int year, int month, int day, int hour, int min, int second) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, Math.abs(year));
    cal.set(Calendar.MONTH, Math.abs(month));
    cal.set(Calendar.DATE, Math.abs(day));
    cal.set(Calendar.HOUR, Math.abs(hour));
    cal.set(Calendar.MINUTE, Math.abs(min));
    cal.set(Calendar.SECOND, Math.abs(second));
    cal.set(Calendar.MILLISECOND, 0); //default
    return cal.getTime();
  }
  
  /**
   * 
   * @param dt
   * @param hour
   * @param min
   * @param second
   * @return 
   */
  public final static java.util.Date setTime(java.util.Date dt, int hour, int min, int second) {
    if (dt == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    cal.set(Calendar.HOUR, Math.abs(hour));
    cal.set(Calendar.MINUTE, Math.abs(min));
    cal.set(Calendar.SECOND, Math.abs(second));
    cal.set(Calendar.MILLISECOND, 0); //default
    return cal.getTime();
  }
  
  /**
   * 
   * @param <DT>
   * @param dt
   * @param format
   * @return 
   */
  public final static <DT extends java.util.Date> String format(DT dt, String format) {
    if ((dt == null) || (StringUtils.isEmpty(format))) {
      return null;
    }
    return new SimpleDateFormat(format).format(dt);
  }
  
  /**
   * 
   * @param dateStr
   * @param format
   * @return 
   */
  public final static java.util.Date parse(String dateStr, String format) {
    try {
      return new SimpleDateFormat(format).parse(dateStr);
    }
    catch (Exception exp) {
      throw new IllegalStateException(exp.getMessage());
    }
  }
}