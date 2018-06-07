package gov.ca.water.common.io.timeseries;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>A TimeSpan is used to define time as a number of intervals of a specified
 * {@linkplain TimeUnit}. The definition of a TimeSpan is subjected to the following 
 * constraints:</p><ol>
 *  <li><b>(TimeUnit!=null):</b> - The TimeSpan's TimeUnit must be defined.</li>
 *  <li><b>Min(TimeUnit)=MILLISECONDS:</b> - DateTimes are managed in MilliSeconds - 
 *    thus at this point there seems to be no need for time intervals in units of less 
 *    than milliseconds.</li>
 * </ol>
 * @author kprins
 */
@XmlRootElement()
public class TimeSpan implements Serializable, Comparable<TimeSpan> {  
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * The Time in units of this.timeUnit (Default=0)
   */
  @XmlElement
  private Long interval;
  /**
   * The TimeStep's TimeUnit (Default = Milliseconds)
   */
  @XmlAttribute
  private TimeUnit timeUnit;
  //</editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor - for serialization only
   */
  public TimeSpan() {
    super();    
    this.interval = null;
    this.timeUnit = null;
  }
  
  /**
   * Constructor with a Interval and TimeUnit.
   * @param interval the time interval (must be greater or equal to zero (must &gt; 0 - 
   * set to one (1) if &le; 0)
   * @param timeUnit the TimeUnit (MILLISECONDS or greater)
   */
  public TimeSpan(long interval, TimeUnit timeUnit) {
    this();
    this.setTimeSpan(interval, timeUnit);
  }
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Call to set the TimeSpan's Time, TimeUnit.
   * @param interval the time span
   * @param timeUnit the TimeUnit (MILLISECONDS or greater)
   */
  private void setTimeSpan(long interval, TimeUnit timeUnit) {
    if (timeUnit == null) {
      throw new NullPointerException("The Interval's TimeUnit cannot be unassigned.");
    } else if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
      throw new IllegalArgumentException("TimeUnit[" + timeUnit.toString() + "] is "
              + "not supported. The Interval's TimeUnit must by MILLISECONDS or "
              + "larger.");
    }
    this.interval = interval;
    this.timeUnit = timeUnit;
  }
  
  /**
   * Get the TimeStep's Time
   * @return the assigned Time in this.TimeUnit
   */
  public final Long getInterval() {
    return this.interval;
  }
  
  /**
   * Get the TimeStep's TimeUnit
   * @return
   */
  public final TimeUnit getTimeUnit() {
    return this.timeUnit;
  }

  /**
   * Get the TimeStep in the MilliSeconds
   * @return the TimeStep in MilliSeconds
   */
  public final Long getMilliSeconds() {
    return TimeUnit.MILLISECONDS.convert(this.interval, this.timeUnit);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc} <p>OVERRIDE: Return "this.Time this.TimeUnit" .</p>
   */
  @Override
  public String toString() {
    String sResult = this.interval.toString() + " " + this.timeUnit.toString();
    return sResult;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return a new TimeInterval instance with the same Time 
   * and TimeUnit settings.</p>
   */
  @Override
  public TimeSpan clone()  {
    return new TimeSpan(this.interval, this.timeUnit);
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return true if obj!=null and instanceof TimeStep and
   * its Time and TimeUnit match this instance's values.</p>
   */
  @Override
  public boolean equals(Object obj) {
    boolean bResult = ((obj != null) && (obj instanceof TimeSpan));
    if (bResult) {
      TimeSpan pObj = (TimeSpan) obj;
      bResult = ((this.interval.equals(pObj.interval))
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
    hash = 41 * hash + Objects.hashCode(this.interval);
    hash = 41 * hash + (this.timeUnit != null ? this.timeUnit.hashCode() : 0);
    return hash;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Implement Comparable">
  /**
   * {@inheritDoc} <p>OVERRIDE: Compare this instance's MilliSecond with that of
   * pTimeStep. Return 0 if both are unassigned, -1 of this.milliSeconds=null, 1 if
   * pTimeStep or pOther.milliseconds is unassigned, or the result of {@linkplain
   * Long#compareTo(java.lang.Long) comparing this.milliSeconds} to
   * pOther.milliSeconds.</p>
   */
  @Override
  public int compareTo(TimeSpan pOther) {
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
