package gov.ca.water.common.io.timeseries;

import gov.ca.water.common.io.DataEntry;
import gov.ca.water.common.io.DateTime;
import java.io.Serializable;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A TimeStep represents the intervals of {@linkplain TimeSeries}, which store it 
 * {@linkplain DateTime} representation as a Time (long), a {@linkplain TimeUnit} and a
 * {@linkplain TimeZone}
 * @author kprins
 */
//@XmlType(propOrder={"time","timeUnit","timeZone"})
public class TimeStep implements Serializable, Comparable<TimeStep> {
  
  //<editor-fold defaultstate="collapsed" desc="Static Methods">
  /**
   * Call to create a new TimeStep with a Time interval added to base TimeStep assuming
   * the Time interval's TimeUnit is the same a the base TimeStep
   * @param pTimeStep the base TimeStep.
   * @param lTime the time interval to add.
   * @return a clone of pTimeStep with lTime added.
   * @throws Exception if the base TimeStep is null.
   */
  public static TimeStep incTimeStep(TimeStep pTimeStep, long lTime) throws Exception  {
    return TimeStep.incTimeStep(pTimeStep, lTime, null);
  }
  
  /**
   * Call to create a new TimeStep with a Time interval added to base TimeStep.
   * @param pTimeStep the base TimeStep.
   * @param lTime the time interval to add.
   * @param pTimeUnit the TimeUnit of the interval (or use pTimeStep.timeUnit is null)
   * @return a clone of pTimeStep with lTime added.
   * @throws Exception if the base TimeStep is null.
   */
  public static TimeStep incTimeStep(TimeStep pTimeStep, long lTime,
          TimeUnit pTimeUnit) throws Exception {
    TimeStep pResult = null;
    if (pTimeStep == null) {
      throw new Exception("The base TimeStep is unassigned.");
    }
    
    pTimeUnit = (pTimeUnit == null)? pTimeStep.getTimeUnit(): pTimeUnit;
    pResult = pTimeStep.clone();
    pResult.addTime(lTime, pTimeUnit);
    return pResult;
  }
  
  /**
   * <p>Called to convert a DateTime to TimeStep of a specified TimeUnit. The returned
   * TimeStep's Time will be in the specified TimeUnit and the TimeStep's TimeZone will
   * be the DateTime's TimeZone.</p>
   * <p>It uses the DateTime's TimeZone to calculate a {@linkplain 
   * #getTimeZoneOffset(java.util.TimeZone, java.util.concurrent.TimeUnit) TimeZone
   * Offset} to be added to the DateTime.totalMilliSecond (in UTC time) before 
   * converting the milliseconds to the requested TimeUnit for creating the TimeStep.
   * </p>
   * @param pDateTime the specified DateTime
   * @param pTimeUnit the TimeStep's required TimeStep
   * @return the converted TimeStep or null if pDateTime=null.
   */  
  public static TimeStep fromDateTime(DateTime pDateTime, TimeUnit pTimeUnit) {
    TimeStep pResult = null;
    pTimeUnit = (pTimeUnit == null)? TimeUnit.MILLISECONDS: pTimeUnit;
    if (pDateTime != null) {
      TimeZone pTz = pDateTime.getTimeZone();
      long lOffset = TimeStep.getTimeZoneOffset(pTz, pTimeUnit);
      long lMillSec = pDateTime.getTotalMilliseconds();
      lMillSec += lOffset;
      long lTime = pTimeUnit.convert(lMillSec, TimeUnit.MILLISECONDS);
      pResult = new TimeStep(lTime, pTimeUnit, pTz);
    }
    return pResult;
  }
  
  /**
   * <p>Get the TimeZone Offset (in milliseconds) to be added to a DataTime's UTC
   * Milliseconds before converting the milliseconds to the specified time unit. Since
   * raw TimeZone offsets are in hours and minutes, the result for TimeUnits smaller or
   * equal to minutes will be zero.</p>
   * <p>The TimeZone's NetOffset is calculated by converting the {@linkplain 
   * TimeZone#getRawOffset() TimeZone.rawOffset} to the specified TimeUnit and back to
   * milliseconds. The returned value if the difference between the TimeZone.rawOffset
   * and the calculated NetOffset.</p>
   * @param pTz the TimeZone of interest 
   * @param pTimeUnit the TimeUnit to convert to
   * @return the calculated time offset in milliseconds or 0 if the TimeZone or
   * TimeUnit is undefined.
   */
  public static long getTimeZoneOffset(TimeZone pTz, TimeUnit pTimeUnit) {
    long lResult = 0;
    if ((pTz != null) && (pTimeUnit != null) && 
                          (pTimeUnit.compareTo(TimeUnit.MINUTES) > 0)) {
      long lRawOffset = pTz.getRawOffset();
      long lTuOffset = pTimeUnit.convert(lRawOffset, TimeUnit.MILLISECONDS);
      long lNetOffset = TimeUnit.MILLISECONDS.convert(lTuOffset, pTimeUnit);
      lResult = (lRawOffset - lNetOffset);
    }
    return lResult; 
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * The Time in // of this.timeUnit (Default=0)
   */
  @XmlAttribute
  private Long time;
  /**
   * The TimeStep's TimeUnit (Default = Milliseconds)
   */
  @XmlAttribute
  private TimeUnit timeUnit;
  /**
   * The TimeStep's TimeZone (Default={@linkplain TimeZone#getDefault() }
   */
  
  private TimeZone timeZone;
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public TimeStep() {
    super(); 
    this.time = null;
    this.timeUnit = null;
    this.timeZone = null;
  }
  
  /**
   * Constructor with a Time, TimeUnit, and TimeZone value.
   * @param lTime the time stime (must be greater or equal to zero (set to zero if 
   * negative)
   * @param pTimeUnit the TimeUnit (MILLISECONDS or greater)
   * @param pTimeZone the timezone reference (can be null to use the default).
   */
  public TimeStep(long lTime, TimeUnit pTimeUnit, TimeZone pTimeZone) {
    this();
    this.setTimeStep(lTime, pTimeUnit, pTimeZone);
  }
  // </editor-fold>
    
  //<editor-fold defaultstate="collapsed" desc="Private Methods for XML Serialization">
  /**
   * Get the TimeZoneId (for XML serializing)
   * @return the current timeZone's ID (or null if this.timeZone=null)
   */
  @XmlAttribute(name="timeZone")
  private String getTimeZoneId() {
    return (this.timeZone == null)? null: this.timeZone.getID();
  }
  /**
   * Get the TimeZoneId (for XML serializing). Initiate the time zone using the ID 
   * @param zoneId the time zone ID (can be null)
   */
  private void setTimeZoneId(String zoneId) {
    zoneId = DataEntry.cleanString(zoneId);
    this.timeZone = (zoneId == null)? null: TimeZone.getTimeZone(zoneId);
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Call to set the TimeStep's Time, TimeUnit, and TimeZone value.
   * @param lTime the time stime (must be greater or equal to zero (set to zero if
   * negative)
   * @param pTimeUnit the TimeUnit (MILLISECONDS or greater)
   * @param pTimeZone the timezone reference (can be null to use the default).
   */
  public final void setTimeStep(long lTime, TimeUnit pTimeUnit, TimeZone pTimeZone) {
    this.time = (lTime < 0)? 0: lTime;
    if (pTimeUnit == null) {
      throw new NullPointerException("The Timestep's TimeUnit cannot be unassigned.");
    } else if (pTimeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
      throw new IllegalArgumentException("TimeUnit[" + pTimeUnit.toString() + "] is "
              + "not supported. The Timestep's TimeUnit must by MILLISECONDS or "
              + "larger.");
    }
    this.timeUnit = pTimeUnit;
    this.timeZone = pTimeZone;
  }
  
  /**
   * Get the TimeStep's Time
   * @return the assigned Time in this.TimeUnit
   */
  public final Long getTime() {
    return this.time;
  }
  
  /**
   * Get the TimeStep's TimeUnit
   * @return
   */
  public final TimeUnit getTimeUnit() {
    return this.timeUnit;
  }
  
  /**
   * Get the TimeStep's TimeZone
   * @return get the assign TimeZone or {@linkplain TimeZone#getDefault() } if
   * unassigned.
   */
  public final TimeZone getTimeZone() {
    return this.timeZone;
  }
  
  /**
   * Get the TimeStep's DateTime using {@linkplain #getMilliSeconds() this.milliSeconds}
   * and this.timeZone. 
   * @return a DateTime in the specified (or Default) TimeZone
   */
  public final DateTime getDateTime() {
    TimeZone pTz = this.getTimeZone();
    long lMilSec = this.getMilliSeconds();
    return new DateTime(lMilSec,pTz);
  }

  /**
   * Get the TimeStep's Time in the UTC MilliSeconds for this.timeZone. It converts 
   * this.time to MilliSeconds and deduct the {@linkplain 
   * TimeStep#getTimeZoneOffset(java.util.TimeZone, java.util.concurrent.TimeUnit) 
   * TimeZone Offset} for this.TimeUnit. 
   * @return the TimeStep in MilliSeconds
   */
  public final Long getMilliSeconds() {    
    long lResult = TimeUnit.MILLISECONDS.convert(this.time, this.timeUnit);
    TimeZone pTz = this.getTimeZone();
    long lOffset = TimeStep.getTimeZoneOffset(pTz, this.timeUnit);
    lResult -= lOffset;
    return lResult;
  }
  
  /**
   * Add a time interval to this TimeStep. If the interval is negative and the resulting
   * time is less than zero the TimeStep.time will be set to zero.
   * @param lTime this time interval to add (in the specified TimeUnit)
   * @param pTimeUnit the TimeUit of the time interval.
   */
  public final void addTime(long lTime, TimeUnit pTimeUnit) {
    if (pTimeUnit == null) {
      throw new NullPointerException("The incremental time's TimeUnit cannot be "
              + "unassigned.");
    }
    
    long lDTime = this.timeUnit.convert(lTime, pTimeUnit);
    this.time += lDTime;
    if (this.time < 0) {
      this.time = 0l;
    }
  }
  
  /**
   * Get whether this TimeStep is greater than (later than) pTimeStep
   * @param pTimeStep the other TimeStep
   * @return return (this.compareTo(pTimeStep) &gt; 0)
   */
  public final boolean isGreaterThan(TimeStep pTimeStep) {
    return (this.compareTo(pTimeStep) > 0);
  }
  
  /**
   * Get whether this TimeStep is less than (earlier than) pTimeStep
   * @param pTimeStep the other TimeStep
   * @return return (this.compareTo(pTimeStep) &lt; 0)
   */
  public final boolean isLessThan(TimeStep pTimeStep) {
    return (this.compareTo(pTimeStep) < 0);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc} <p>OVERRIDE: Return "this.Time this.TimeUnit (this.TimeZone.ID)" or
   * "this.Time this.TimeUnit" if the TiemZone is not specified.</p>
   */
  @Override
  public String toString() {
    String sResult = this.time.toString() + " " + this.timeUnit.toString();
    if (this.timeZone != null) {
      sResult += " (" + this.timeZone.getID() + ")";
    }
    return sResult;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return a new TimeStep instance with the same Time,
   * TimeUnit, and TimeZone settings.</p>
   */
  @Override
  public TimeStep clone()  {
    return new TimeStep(this.time, this.timeUnit, this.timeZone);
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return true if obj!=null and instanceof TimeStep and
   * its Time and TimeUnit match this instance's values.</p>
   */
  @Override
  public boolean equals(Object obj) {
    boolean bResult = ((obj != null) && (obj instanceof TimeStep));
    if (bResult) {
      TimeStep pObj = (TimeStep) obj;
      bResult = ((this.time.equals(pObj.time))
              && (this.timeUnit.equals(pObj.timeUnit)));
    }
    return bResult;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return a HashCode using this instance's Time and
   * TimeUnit values</p>
   */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 41 * hash + Objects.hashCode(this.time);
    hash = 41 * hash + (this.timeUnit != null ? this.timeUnit.hashCode() : 0);
    return hash;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Implement Comparable">
  /**
   * {@inheritDoc} <p>Compare this instance's MilliSecond with that of
   * pTimeStep. Return 0 if both are unassigned, -1 of this.milliSeconds=null, 1 if
   * pTimeStep or pOther.milliseconds is unassigned, or the result of {@linkplain 
   * Long#compareTo(java.lang.Long) comparing this.milliSeconds} to 
   * pOther.milliSeconds.</p>
   */
  @Override
  public int compareTo(TimeStep pOther) {
    int iResult = 0;
    Long iMilSec1 = this.getMilliSeconds();
    Long iMilSec2 = (pOther == null)? null: pOther.getMilliSeconds();
    if ((iMilSec1 == null) && (iMilSec2 != null)) {
      iResult = -1;
    } else if ((iMilSec1 != null) && (iMilSec2 == null)) {
      iResult = 1;
    } else if ((iMilSec1 != null) && (iMilSec2 != null)) {
      iResult = iMilSec1.compareTo(iMilSec2);
    }
    return iResult;
  }
  //</editor-fold>
}
