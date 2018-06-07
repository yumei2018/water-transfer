package gov.ca.water.common.io.timeseries;

import gov.ca.water.common.io.DateTime;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>A TimeInterval is a TimeSpan between TimeSteps in a TimeSeries. It defines the 
 * TimeSteps intervals for a specified {@linkplain TimeUnit}. The definition of a 
 * TimeInterval is subjected to the following constraints:</p><ol>
 *  <li><b>(TimeUnit!=null):</b> - The TimeInetrval's TimeUnit must be defined.</li>
 *  <li><b>Min(TimeUnit)=MILLISECONDS:</b> - DateTimes are managed in MilliSeconds - 
 *    thus at this point there seems to be no need for time increments less than 
 *    milliseconds.</li>
 *  <li><b>(Interval &ge; 1):</b> - The Interval must be defined and must be at least 
 *    one unit of the defined TimeUnit</li>
 *  <li><b>(Interval &le (half the span of the next larger TimeUnit):</b> - see 
 *    {@linkplain #maxInterval(java.util.concurrent.TimeUnit) maxInterval} for more
 *    details.</li>
 *  <li><b>(Offset=null) or (Offset &lt; Interval):</b> - the TimeInterval's start 
 *    Offset is assumed to be 0 if undefined or if it is defined it must be less
 *    than the Interval and is in the same units as the Interval.</li>
 * </ol>
 * <p>TimeInterval is be used to manage {@linkplain TimeStep TimeSteps} of a 
 * {@linkplain TimeSeries} is defined fixed intervals (e.g., Daily, hourly, 6-hourly, 
 * etc.). The TimeStep for a specified Date-Time can be obtained by calling {@linkplain 
 * #getTimeStep(myapp.io.DateTime) getTimeStep()}. The return TimeStep will the for
 * TimeInterval that is &le; the referenced DateTime's {@linkplain 
 * TimeStep#fromDateTime(myapp.io.DateTime, java.util.concurrent.TimeUnit) TimeStep} in 
 * the TimeInterval's TimeUnit. For Example:</p>
 *   For DateTime[PST].now = 2013-01-25T08:58:40-0800<br/>
 *    - TimeInterval[1 DAYS].timeStep = 2013-01-25T00:00:00-0800}<br/>
 *    - TimeInterval[7 DAYS].timeStep = 2013-01-22T00:00:00-0800<br/>
 *    - TimeInterval[1 HOURS].timeStep = 2013-01-25T08:00:00-0800<br/>
 *    - TimeInterval[6 HOURS].timeStep = 2013-01-25T06:00:00-0800<br/>
 *    - TimeInterval[6 HOURS[4]].timeStep = 2013-01-25T04:00:00-0800<br/>
 *    - TimeInterval[1 MINUTES].timeStep = 2013-01-25T08:58:00-0800<br/>
 *    - TimeInterval[15 MINUTES].timeStep = 2013-01-25T08:45:00-0800<br/>
 *    - TimeInterval[15 MINUTES[5]].timeStep = 2013-01-25T08:50:00-0800<br/>
 * </p>
 * <p><b>NOTE:</b> TimeIntervals and Offset must be selected in rounded increments of
 * the next larger TimeUnit span to ensure evenly distributed time intervals. Also, any 
 * interval set to more than half the next larger TimeUnit span could produce unexpected  
 * TimeSteps. For Example: 18-Hour time step will result in 24-hour TimeStep scheduled 
 * for 18:00 every day.</p>
 * @author kprins
 */
@XmlRootElement()
public class TimeInterval extends TimeSpan {
  
  //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
  /**
   * <p>Get the Maximum Allowable Intervals for a specified TimeUnit. It returns half
   * the number of intervals pTimeUnit's next larger TimeUnit' span (e.g. for pTimeUnit
   * = HOURS, return 12. If pTimeUnit=DAYS, it returns 366/2 days (half a year).
   * If the interval &ge; half the larger TimeUnit's span, the interval TimeSteps
   * become irregular. For example a Interval=18 will have TiemSteps at 00:00 and 18:00
   * only.</p>
   * @param timeUnit TimeUnit
   * @return long
   */
  public static long maxInterval(TimeUnit timeUnit) {
    long result = 0;
    if (timeUnit == null) {
      throw new IllegalArgumentException("The TimeUnit is undefined.");
    }
    if (TimeUnit.DAYS.equals(timeUnit)) {
      result = 366/2;
    } else if (TimeUnit.HOURS.equals(timeUnit)) {
      result = 24/2;
    } else if (TimeUnit.MINUTES.equals(timeUnit)) {
      result = 60/2;
    } else if (TimeUnit.SECONDS.equals(timeUnit)) {
      result = 60/2;
    } else if (TimeUnit.MICROSECONDS.equals(timeUnit)) {
      result = 1000/2;
    } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
      result = 1000/2;
    }
    return result;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * The offset (in mpTimeUnit) form the next large TimeUnit's Start, to define the
   * TimeInterval Start.
   */
  @XmlElement
  private Long offset;
  //</editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor - for serialization only
   */
  public TimeInterval() {
    super();    
    this.offset = null;
  }
  
  /**
   * Constructor with a Interval and TimeUnit.
   * @param lInterval the time interval (must be greater or equal to zero (must &gt; 0 - 
   * set to one (1) if &le; 0)
   * @param timeUnit the TimeUnit (MILLISECONDS or greater)
   */
  public TimeInterval(long interval, TimeUnit timeUnit) {
    this(interval, timeUnit, 0);
  }
  
  /**
   * Constructor with a Interval, a TimeUnit, and a Offset. Typically used when the
   * Interval is greater than 1 and the time series interval should start at some
   * offset from the TimeUnit's Zero Increment
   * @param interval the time interval (must be greater or equal to zero (must &gt; 0 - 
   * set to one (1) if &le; 0)
   * @param timeUnit the TimeUnit (MILLISECONDS or greater)
   * @param offset the time interval offset from the base time (must be &le; lInterval)
   */
  public TimeInterval(long interval, TimeUnit timeUnit, long offset) {
    super(TimeInterval.onValidateInterval(interval, timeUnit, offset), timeUnit);
    this.offset = (offset <= 0)? 0: offset;
  }
  
  /**
   * Static validator of the Input before calling the supper constructor
   * @param interval the passed in time interval
   * @param timeUnit the passed in TimeUnit
   * @param offset the passed in time offset
   * @return the validated Time Interval
   * @exception NullPointerException if pTimeUnit=null.
   * @exception IllegalArgumentException if pTimeUnit &lt; MILLISECONDS, lInterval &gt;
   * {@linkplain #maxInterval(java.util.concurrent.TimeUnit) maxInterval}; or lOffset
   * &ge; lInterval.
   */
  protected static long onValidateInterval(long interval, TimeUnit timeUnit, 
          long offset) {
    if (timeUnit == null) {
      throw new NullPointerException("The Interval's TimeUnit cannot be unassigned.");
    } else if (timeUnit.compareTo(TimeUnit.MILLISECONDS) < 0) {
      throw new IllegalArgumentException("TimeUnit[" + timeUnit.toString() + "] is "
              + "not supported. The Interval's TimeUnit must by MILLISECONDS or "
              + "larger.");
    }
    
    interval = (interval <= 0)? 1: interval;
    Long maxTime = TimeInterval.maxInterval(timeUnit);
    if (interval > maxTime) {
      throw new IllegalArgumentException("The Interval[" 
              + Long.toString(interval) + "] exceeds the maximum allowable intervals ("
              + maxTime.toString() + ") for TimeUnit[" + timeUnit.toString() + "].");
    }
    
    offset = (offset <= 0)? 0: offset;
    if (offset >= interval) {
      throw new IllegalArgumentException("The Interval Offset[" 
              + Long.toString(offset) + "] must be smaller than Interval["
              + Long.toBinaryString(interval));
    }
    return interval;
  }
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Methods">  
  /**
   * <p>Get the TimeStep of the closest interval that &le; dateTime's TimeStep. If the
   * (Interval = 1), it returns {@linkplain TimeStep#fromDateTime(myapp.io.DateTime, 
   * java.util.concurrent.TimeUnit) TimeStep.fromDateTime} for dateTime and this
   * TimeUnit.</p>
   * <p>Otherwise, the method resolved the TimeInterval's StartStep from which to 
   * determine subsequent intervals as follows:</p><ul>
   *  <li><b>TimeUnit=DAYS:</b> - the TimeStep for {@linkplain 
   *      #getYearStart(myapp.io.DateTime) this.yearStart} DateTime</li>
   *  <li><b>TimeUnit=HOURS:</b> - the TimeStep for TimeStep(pDate,DAYS)</li>
   *  <li><b>TimeUnit=MINUTES:</b> - the TimeStep for TimeStep(pDate,HOURS)</li>
   *  <li><b>TimeUnit=SECONDS:</b> - the TimeStep for TimeStep(pDate,MINUTES)</li>
   *  <li><b>TimeUnit=MICROSECONDS:</b> - the TimeStep for TimeStep(pDate,SECONDS)</li>
   *  <li><b>TimeUnit=MILLSECONDS:</b> - the TimeStep for TimeStep(pDate,MICROSECONDS)
   *  </li>
   * </ul>
   * <p>If an Offset is defined, it adds the offset to the StartStep. It then determine
   * current TimeStep by incrementing/decrementing the StartTep to the step just smaller 
   * or equal to the dateTime's TimeStep.</p>
   * @param dateTime the reference DateTime
   * @return the TimeInterval's TimeStep that are closest but smaller that dateTime or
   * null if dateTime=null.
   */
  public final TimeStep getTimeStep(DateTime dateTime) {
    TimeStep result = null;
    if (dateTime != null)  {
      TimeUnit timeUnit = this.getTimeUnit();
      long interval = this.getInterval();
      if (interval == 1) {
        result = TimeStep.fromDateTime(dateTime, timeUnit);
      } else {
        TimeZone timeZone = dateTime.getTimeZone();
        TimeStep startStep = null;
        if (TimeUnit.DAYS.equals(timeUnit)) {
          DateTime startDt = this.getYearStart(dateTime);
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        } else if (TimeUnit.HOURS.equals(timeUnit)) {
          TimeStep timeStep = TimeStep.fromDateTime(dateTime, TimeUnit.DAYS);
          DateTime startDt = timeStep.getDateTime();
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        } else if (TimeUnit.MINUTES.equals(timeUnit)) {
          TimeStep timeStep = TimeStep.fromDateTime(dateTime, TimeUnit.HOURS);
          DateTime startDt = timeStep.getDateTime();
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        } else if (TimeUnit.SECONDS.equals(timeUnit)) {
          TimeStep timeStep = TimeStep.fromDateTime(dateTime, TimeUnit.MINUTES);
          DateTime startDt = timeStep.getDateTime();
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        } else if (TimeUnit.MICROSECONDS.equals(timeUnit)) {
          TimeStep timeStep = TimeStep.fromDateTime(dateTime, TimeUnit.SECONDS);
          DateTime startDt = timeStep.getDateTime();
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
          TimeStep timeStep = TimeStep.fromDateTime(dateTime, TimeUnit.MICROSECONDS);
          DateTime startDt = timeStep.getDateTime();
          startStep = TimeStep.fromDateTime(startDt, timeUnit);
        }
        
        if (this.offset > 0) {
          startStep.addTime(this.offset, timeUnit);
        }
        
        TimeStep refStep = TimeStep.fromDateTime(dateTime, timeUnit);
        Long startTime = startStep.getTime();
        Long refTime = refStep.getTime();
        if(startTime.equals(refTime)) {
          result = startStep;
        } else {
          long timeStep = Math.round((1.0d*(refTime - startTime))/interval); 
          TimeStep curStep = startStep.clone();
          curStep.addTime(timeStep*interval, timeUnit);
          if (curStep.isGreaterThan(refStep)) {
            long lStep = -1*interval;
            while (curStep.isGreaterThan(refStep)) {
              curStep.addTime(lStep, timeUnit);
            }
          }
          result = curStep;
        }
      }      
    }  
    return result;
  }

  /**
   * Get the FirstDate of the Year Date
   * @param dateTime the reference date
   * @return return Jan 1 of dateTime.year or null if pDaetTime=null.
   */
  protected DateTime getYearStart(DateTime dateTime) {
    DateTime result = null;
    try {
      if (dateTime != null) {
        Calendar cal = dateTime.getCalendar();
        int year = cal.get(Calendar.YEAR);
        cal.set(year, 0, 1, 00, 0, 0);
        result = new DateTime(cal);
      }
    } catch (Exception pExp) {
      throw new IllegalArgumentException(pExp);
    }
    return result;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc} <p>OVERRIDE: Return "this.Time this.TimeUnit" .</p>
   */
  @Override
  public String toString() {
    String result = super.toString();
    if ((this.offset != null) && (this.offset > 0)) {
      result += "; offset[" + this.offset.toString() + "]";
    }
    return result;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return a new TimeInterval instance with the same Time and
   * TimeUnit settings.</p>
   */
  @Override
  public TimeInterval clone()  {
    return new TimeInterval(this.getInterval(), this.getTimeUnit(), this.offset);
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return true if obj!=null and instanceof TimeStep and
   * its Time and TimeUnit match this instance's values.</p>
   */
  @Override
  public boolean equals(Object obj) {
    boolean bResult = (super.equals(obj) && (obj instanceof TimeInterval));
    if (bResult) {
      TimeInterval pObj = (TimeInterval) obj;
      bResult = (this.offset.equals(pObj.offset));
    }
    return bResult;
  }

  /**
   * {@inheritDoc} <p>OVERRIDE: Return a HashCode using this instance's Time and
   * TimeUnit values</p>
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    hash = 41 * hash + Objects.hashCode(this.offset);
    return hash;
  }
  //</editor-fold>
}
