package gov.ca.water.common.io.timeseries;

import gov.ca.water.common.io.DataEntry;
import gov.ca.water.common.io.DateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.*;

/**
 * <p><b>Defining the StartDate:</b> The Start Date can be assigned via the constructor
 * or when the first TimeValue is recorded. The assigned will be converted to 
 * Millisecond and then to the defined Timestep units, which truncates the start and end 
 * times to the lower value of the specified TimeUnit. For example, if the TimeUnits is 
 * HOURS and Start Date = "2012/12/2012 12:15:44.9999" the start date after conversion 
 * will be "2012/12/2012 12:00:00.0000".</p>
 * @author kprins
 */
@XmlRootElement(name="TimeSeries")
public abstract class TimeSeries<TValue extends TimeStepValue> 
                                        implements Serializable, Iterable<TValue> {
  
  //<editor-fold defaultstate="collapsed" desc="Static Logger">
  /**
   * Protected Static Logger object for logging errors, warnings, and info messages.
   */
  protected static final Logger logger = 
                                      Logger.getLogger(TimeSeries.class.getName());
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * Placeholder for the Time Series TimeValues
   */
  @XmlElementWrapper(name="TimeValues")
  @XmlElement(name="timeValue")
  private List<TValue> timeValues;
  /**
   * The Time Series Start DateTime
   */
  @XmlElement
  private TimeStep startStep;
  /**
   * The Time Series End DateTime
   */
  @XmlElement
  private TimeStep endStep;
  /**
   * The TimeStep Intervals
   */
  @XmlElement
  private TimeInterval timeInterval;
  /**
   * Placeholder for the TimeSeries TimeZone (Default=null|{@linkplain 
   * TimeZone#getDefault()} 
   */
  private TimeZone timeZone;
  /**
   * A Transient counter use for managing the TimeSeries' isUpdating state
   */
  @XmlTransient
  private transient int updatingCount = 0;
  //</editor-fold>
 
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor for serializing
   */
  protected TimeSeries() {
    super();    
    this.timeValues = null;
    this.startStep = null;
    this.endStep = null;
    this.timeInterval = null;
  }
    
  /**
   * Public Constructor with a defined Timestep interval and TimeUnit
   * @param interval the Timestep's interval (must be > 0)
   * @param timeUnit the Timestep's TimeUnit (Only MILLISECONDS or larger units are 
   * allowed).
   */
  protected TimeSeries(long interval, TimeUnit timeUnit) {
    this();
    this.onSetTimeInterval(interval, timeUnit);
  }
  
  /**
   * Public Constructor with a defined TimeInterval for managing the TimeSeries' 
   * TimeSteps.
   * @param timeInterval the TimeInterval for managing the TimeSeries' TimeSteps.
   */
  protected TimeSeries(TimeInterval timeInterval) {
    this();    
    this.onSetTimeInterval(timeInterval);
  }
  
  /**
   * <p>Public Constructor with a defined Timestep interval and TimeUnit and start
   * and/or end DateTime of the series. After initiating the TimeSeries' Interval ad
   * TimeUnit, it calls {@linkplain #onSetTimeSeries(myapp.io.DateTime, 
   * myapp.io.DateTime) onSetTimeSeries} to initiate the TimeSeries for the specified
   * pStartDt and pEndDt.</p>
   * @param timeInterval the TimeInterval for managing the TimeSeries' TimeSteps.
   * @param startDt the Time series start Date/Time (can be null)
   * @param endDt the Time series end Date/Time (can be null)
   */
  protected TimeSeries(TimeInterval timeInterval, DateTime startDt, DateTime endDt) {
    this();
    this.onSetTimeInterval(timeInterval);
    this.onSetTimeSeries(startDt, endDt);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: Call supper method before calling {@linkplain #resetTimeSeries() 
   * this.resetTimeSeries}</p>
   */
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); 
    this.resetTimeSeries();
  }
  
  /**
   * Called by {@linkplain #assignTo(gov.ca.water.common.io.timeseries.TimeSeries) 
   * this.assignTo} to reset all timeseries properties before 
   */
  protected final void resetTimeSeries() {  
    this.timeValues = null;
    this.startStep = null;
    this.endStep = null;
    this.timeInterval = null;
    this.onResetTimeSeries();
  }
  
  /**
   * ABSTRACT: Called by {@linkplain #resetTimeSeries() this.resetTimeSeries} to 
   * reset all TimeSeries settings. The base method does nothing.
   */
  protected abstract void onResetTimeSeries();
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

  //<editor-fold defaultstate="collapsed" desc="Protected/Private Methods">
   /**
   * <p>Convert dateTime to a TimeSeries Timestep. First convert get dateTime in 
   * MilliSeconds and convert it to the TimeSeries TimeUnits.</p> 
   * <p><u>If the StartTime is defined</u> then:</p> <ul>
   *  <li>If dateTime is larger (after) the StartTime, the returned value will the 
   *    number of TimeSteps in the Intervals and TimeUnit of this TimeSeries that are 
   *    less than dateTime (e.g., dateTime = 2013-01-02T15:01:00-0800 converts to 
   *    2013-01-02T15:00:00-0800 with StartTime = 2013-01-02T09:00:00-0800 and 6-hourly 
   *    Intervals).</li>
   *  <li>If dateTime is smaller (before) the StartTime, the returned value will the 
   *    number of TimeSteps in the Intervals and TimeUnit of this TimeSeries that are 
   *    less than dateTime (e.g., dateTime = 2013-01-02T02:59:00-0800 converts to 
   *    2013-01-01T21:00:00-0800 and dateTime = 2013-01-02T03:01:00-0800 converts to 
   *    2013-01-02T03:00:00-0800 with StartTime = 2013-01-02T09:00:00-0800 and 
   *    6-hourly Intervals).</li>
   * </ul>
   * <p><u>If the StartTime is NOT defined</u> the returned value will be dateTime in 
   * MilliSeconds rounded to the the TimeSeries' specified TimeUnits (e.g., dateTime =
   * 2013-01-02T09:25:10-0800 converts to 2013-01-02T09:00:00-0800 with TimeUnit=HOURS)
   * </p>
   * @param dateTime the DateTime to convert
   * @return the converted dateTime.milliseconds to a TimeStep relative to
   * the StartDate in this TimeSeries Intervals.
   * @exception NullPointerException if dateTime is undefined.
   */
  protected final TimeStep toTimeStep(DateTime dateTime) {
    TimeStep result = null;
    if (dateTime == null) {
      throw new NullPointerException("The DateTime cannot be unassigned.");
    }
    
    if (this.timeInterval == null) {
      throw new NullPointerException("The TimeSeries' if not properly initiated; The "
              + "TimeInterval is unassigned.");
    }
    
    result = this.timeInterval.getTimeStep(dateTime);
//    if (this.mpInterval != null) {
//      TimeUnit eTimeUnit = this.mpInterval.getTimeUnit();
//      long lDtTime = dateTime.getTotalMilliseconds();
//      long lTime = eTimeUnit.convert(lDtTime, TimeUnit.MILLISECONDS);
//      long lTime2 = TimeUnit.MILLISECONDS.convert(lTime, eTimeUnit);
//      if (this.mpStartStep != null) {
//        long lStartTime = this.mpStartStep.getMilliSeconds();
//        if ((lDtTime < lStartTime) && (lDtTime < lTime2)) {
//          lTime--;
//        }
//
//        result = this.toTimeStep(lTime);
//      } else {
//        result = new TimeStep(lTime, eTimeUnit, this.mpTimeZone);
//      }
//    }
    return result;
  }
  
  /**
   * Convert lTimeStep to a "Clean" or "Valid" TimeStep for the TimeSeries. It calls
   * {@linkplain #getStepIndex(long) getStepIndex} to get the TimeSeries Index and
   * then calculate the TimeStep using the TimeSeries' StartStep and the TimeInterval.
   * @param interval the Time interval to convert to a TimeSeries TimeStep
   * @return the converted value or lTimeStep is the StartTimeStep or TimeInetrval is
   * undefined.
   */
  protected final TimeStep toTimeStep(long interval) {
    TimeStep result = null;
    if ((this.startStep != null) && (this.timeInterval != null)) {
      int lIndex = this.getStepIndex(interval);
      interval = this.startStep.getTime() + (lIndex * this.timeInterval.getInterval());
      result = new TimeStep(interval, this.timeInterval.getTimeUnit(), this.timeZone);
    } else {
      result = new TimeStep(interval, this.timeInterval.getTimeUnit(), this.timeZone);
    }
    return result;
  }
  
  /**
   * Calculate the Index of the TimeStep in the current TimeSeries range. It calculates 
   * the index as the number of Time Intervals from the StartStep.  If lTimeStep is
   * between two intervals it will always return the smaller index.
   * @param pTimeStep the TimeStep for which to calculate the interval
   * @return a negative value if pTimeStep null or pTimeStep is before that 
   * this.StartTimeStep and zero or a positive index if equal to or greater 
   * than this.StartTimeStep.
   */
  protected final int getStepIndex(TimeStep pTimeStep) {
    return (pTimeStep == null)? -1: this.getStepIndex(pTimeStep.getTime());
  }  
  
  /**
   * Calculate the Index of the TimeStep in the current TimeSeries range. It calculates 
   * the index as the number of Time Intervals from the StartStep.  If lTimeStep is
   * between two intervals it will always return the smaller index.
   * @param pTime the TimeStep for which to calculate the interval
   * @return a negative value if lTimeStep is less that this.StartTimeStep and zero or
   * a positive index if equal to or greater than this.StartTimeStep.
   */
  protected final int getStepIndex(long lTime) {
    int lResult = 0;
    if ((this.startStep != null) && (this.timeInterval != null)) {
      Long lStTime = this.startStep.getTime();
      Long lIndex = (lTime - lStTime)/this.timeInterval.getInterval();
      long lTimeStep2 = lStTime + (lIndex * this.timeInterval.getInterval());
      if ((lTime < lStTime) && (lTime < lTimeStep2)) {
        lIndex --;
      }
      lResult = lIndex.intValue();
    }
    return lResult;
  }
  
  /**
   * Get whether the Index is in the range of the defined TimeValues.
   * @param lIndex the index of interest (can be negative)
   * @return true if this.value!=null and (!this.value.isEmpty) and (lIndex greater or 
   * equal to Zero) and (lIndex less than this.values.size).
   */
  protected final boolean isIndexInRange(int lIndex) {
    boolean bResult = false;
    if ((this.timeValues != null) && (!this.timeValues.isEmpty())) {
      bResult = ((lIndex >= 0) && (lIndex < this.timeValues.size()));
    }
    return bResult;
  }
  
  /**
   * Set the TimeSeries TimeZone (Default = {@linkplain TimeZone#getDefault()})
   * @param pTimeZone the new TimeZone (can be null to use default).
   */
  protected void setTimeZone(TimeZone pTimeZone) {
    this.timeZone = pTimeZone;
  }
  
  /**
   * Called by the Constructor (or other Instance Initiation Methods ) to initiate the 
   * the TimeSeries TimeStep Interval and TimeUnit.
   * @param lInterval the TimeStep Interval (must be greater than zero)
   * @param pTimeUnit the TimeUnit for the TimeStep (must be defined).
   * @exception IllegalArgumentException if the Interval less or equal to zero or the
   * TimeUnit is undefined or smaller than MilliSeconds (the smallest TimeUnit for 
   * managing DateTime values).
   */
  protected final void onSetTimeInterval(long lInterval, TimeUnit pTimeUnit) {
    TimeInterval pInterval = new TimeInterval(lInterval, pTimeUnit);    
    if (pInterval == null) {
      throw new NullPointerException("The TimeSeries' Interval is invalid.");
    }
    this.timeInterval = pInterval;
  }
  
  /**
   * Called by the Constructor (or other Instance Initiation Methods ) to initiate the 
   * the TimeSeries TimeStep Interval.
   * @param pInterval a TiemInetrval
   * @exception NullPointerException if pInterval=null.
   */
  protected final void onSetTimeInterval(TimeInterval pInterval) {
    if (pInterval == null) {
      throw new NullPointerException("The TimeSeries' Interval cannot be unassigned");
    }
    this.timeInterval = pInterval;
  }
  
  /**
   * <p>Called by the Constructor to set the Start and End TimeSteps and to initiate the
   * TimeSeries if applicable. It handles the request as follows:</p><ul>
   * <li><b>If (pStartDt=null) and (pEndDt==null):</b> return unhandled.</li>
   * <li><b>If (pStartDt=null) and (pEndDt!=null):</b> set pStartDt=pEndDt; pEndDt=null
   * </li>
   * <li><b>If (pStartDt!=null) and (pEndDt!=null) and (pStartDt.isAfter(pEndDt):</b> 
   *  switch the dates.</li>
   * <li><b>Initiate StartStep:</b> Call {@linkplain #setStartTime(myapp.io.DateTime) 
   *    setStartTime} to set the TimeSteries Start TimeStep.</li>
   * <li>Set lEndStep = (pEndDt==null)? StartStep: Call {@linkplain #toTimeStep(
   *    myapp.io.DateTime) toTimeStep} to convert pEndDt to a TimeStep. 
   *  <li>Call {@linkplain #onSetTimeSeries(long, long) onSetTimeSeries} to initiate
   *    the TimeSeries.</li>
   * </ul>
   * @param startDt the TimeSeries' Start DateTime (can be null).
   * @param endDt the TimeSeries' End DateTime (can be null).
   */
  protected final void onSetTimeSeries(DateTime startDt, DateTime endDt) {
    try {
      if ((startDt == null) && (endDt == null)) {
        return;
      }
      
      if ((startDt == null) && (endDt != null)) {
        startDt = endDt;
        endDt = null;
      } else if ((startDt != null) && (endDt != null) && (startDt.isAfter(endDt))) {
        DateTime tempDt = startDt;
        startDt = endDt;
        endDt = tempDt;
      }
      
      /**
       * Initiate the StartTime
       */
      this.setStartTime(startDt);
      
      /**
       * If pEndDt is not set, set lEndStep=StartStep, else convert pEndDt to a TimeStep
       * and call Overload 2 to initiate the series and set the EndStep.
       */
      TimeStep aEndStep = 
                  (endDt == null)? this.startStep.clone(): this.toTimeStep(endDt);
      this.onSetTimeSeries(this.startStep, aEndStep);
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.onSetTimeSeries Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    }
  }
  
  /**
   * <p>Called to initiate or expand the TimeSeries by adding new values to the series
   * start or end depending on the following:</p><ul>
   *  <li><b>if (lStartStep > lEndStep):</b> - switch the values.</li>
   *  <li><b>if the TimeSeries' StartStep is unassigned:</b> - set lStartStep as the 
   *    TimeSeries' StartStep.</li>
   *  <li>Initiate bPrePend=(lStartStep is less than this.StartStep)</li>
   *  <li>Initiate bAppend and this.EndStep as follows:<ul>
   *    <li>If (lEndStep is greater than this.StartStep): set bAppend=true if 
   *      this.EndStep=null or less than lEndStep</li>
   *    <li>If (pPrepend) and (this.EndStep=null): set pEndStep=this.StartStep and
   *      set bAppend=false.</li>
   *  </ul></li>
   *  <li><b>If (bPrepend or bAppend):</b> add new TimeValue to the start and end of 
   *    the series for each time step.</li>
   *  <li><b>Finally:</b>If (!this.isUpdating) and (!this.values.isEmpty) and 
   *    (bPrepend or bAppend) - Call {@linkplain #validate()) to sort and validate the
   *    the list to ensure that the values are defained and in an increasing Time order. 
   *  </li>
   * </ul>
   * @param startStep the TimeSerie's new Start TimeStep
   * @param endStep the TimeSerie's new End TimeStep
   */
  protected final void onSetTimeSeries(TimeStep startStep, TimeStep endStep) {
    boolean doPrepend = false;
    boolean doAppend = false;
    try {
      if (((startStep == null) && (endStep == null))  
              || (this.timeInterval == null)) {
        return;
      }
      
      if ((startStep == null) && (endStep != null)) {
        startStep = endStep;
        endStep = null;
      } else if ((startStep != null) && (endStep != null) 
              && (startStep.getTime() > endStep.getTime())) {
        TimeStep tempStep = startStep;
        startStep = endStep;
        endStep = tempStep;
      }
      
      if (this.startStep == null) {
        this.startStep = startStep;
      }
      
      doPrepend = (startStep.getTime() < this.startStep.getTime());
      if ((endStep != null) && (endStep.getTime() >= this.startStep.getTime())) {
        if (this.endStep == null) {
          this.endStep = this.startStep;
          doAppend = true;
        } else {
          doAppend = (this.endStep.getTime() < endStep.getTime());
        }
      } else if ((doPrepend) && (this.endStep == null)) {
        this.endStep = this.startStep.clone();
      }
      
      if (((doPrepend) || (doAppend)) && (this.timeValues == null)) {
        this.timeValues = new ArrayList<>();
        TValue timeValue = this.onNewTimeValue(this.startStep);
        this.timeValues.add(timeValue);
        doAppend = (!this.startStep.equals(endStep));
      }
      
      if (doPrepend) {
        long startTime = startStep.getTime();
        long interval = -1*this.timeInterval.getInterval();
        while (this.startStep.getTime() > startTime) {
          TimeStep timeStep = TimeStep.incTimeStep(this.startStep, interval); 
          TValue timeValue = this.onNewTimeValue(timeStep);
          this.timeValues.add(0,timeValue);
          this.startStep = timeStep;
        }
      }
      
      if (doAppend) {
        long endTime = endStep.getTime();
        long interval = this.timeInterval.getInterval();
        while (this.endStep.getTime() < endTime) {
          TimeStep timeStep = TimeStep.incTimeStep(this.endStep, interval); 
          TValue timeValue = this.onNewTimeValue(timeStep);
          this.timeValues.add(timeValue);
          this.endStep = timeStep;
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.onSetTimeSeries Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    } finally {
      if ((!this.isUpdating()) && (this.timeValues != null) 
                                                    && ((doAppend) || (doPrepend))) {
        this.validate();
      }
    }
  }
//  
//  /**
//   * Called to initiate a new TimeValue for the series. Call the abstract 
//   * {@linkplain #onNewTimeValue(long, java.util.concurrent.TimeUnit)} method passing in
//   * lTimeStep and this.timeUnit.
//   * @param pTimeStep the TimeValue's TimeStep in the TimeSeries TimeUnit
//   * @return a new TimeValue instance
//   */
//  protected final TValue onNewTimeValue(TimeStep pTimeStep) {
//    return this.onNewTimeValue(pTimeStep);
//  }  
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Managing the Updating State">
  /**
   * Get the TimeSeries' isUpdatingState 
   * @return true if (this.updatingCount > 0)
   */
  public final boolean isUpdating() {
    return (this.updatingCount > 0);
  }
  
  /**
   * Call to start the Updating of the TimeSeries. It increments this.updatingCount with
   * each call. Must be called in pair with this.endUpdating.
   */
  public final void startUpdating() {
    this.updatingCount = (this.updatingCount < 0)? 0: this.updatingCount;
    this.updatingCount++;
  }
  
  /**
   * Call to end the Updating of the TimeSeries. It decrement this.updatingCount with
   * each call. When the count equals zero, it call {@linkplain #validate()} to sort and
   * validate the time series values.
   */
  public final void endUpdating() {
    if (this.updatingCount > 0) {
      this.updatingCount--;      
    }
    if (this.updatingCount == 0) {
      this.validate();
    }
  }
  
  /**
   * Called to sort and validate the TimeSeries Values. It check that each value in the
   * TimeSeries is initiated ad sort the values. It calls {@linkplain #onValidate()}
   * to handle any custom validation of the series.
   */
  public final void validate() {
    try {
      if ((this.startStep == null) || (this.timeInterval == null)) {
        return;
      }
      
      if (this.endStep == null) {
        this.onSetTimeSeries(this.startStep, this.startStep);
      }
      
      if ((this.timeValues == null) || (this.endStep == null)) {
        throw new Exception("Initiating the TimeSeries failed.");
      }
      
      if (this.timeValues.size() > 1) {
        Collections.sort(this.timeValues, new TimeSeriesComparator());
      }
      
      /* Step through the TimeSeries and validate that all values are initiated.
       */
      int iIdx = 0;
      long lTime = this.startStep.getTime();
      long lEndTime = this.endStep.getTime();
      TimeUnit eTimeUnit = this.timeInterval.getTimeUnit();
      while (lTime <= lEndTime) {
        TValue pValue = null;
        if (iIdx >= this.timeValues.size()) {
          TimeStep pTimeStep = new TimeStep(lTime, eTimeUnit, this.timeZone);
          pValue = this.onNewTimeValue(pTimeStep);
          this.timeValues.add(pValue);
        } else {
          pValue = this.timeValues.get(iIdx);
          TimeStep pTimeStep = (pValue == null)? null: pValue.getTimeStep();
          if (pTimeStep == null) {
            pTimeStep = new TimeStep(lTime, eTimeUnit, this.timeZone);
            pValue = this.onNewTimeValue(pTimeStep);
            this.timeValues.set(iIdx, pValue);
          } else if (pTimeStep.getTime() != lTime) {
            pTimeStep = new TimeStep(lTime, eTimeUnit, this.timeZone);
            pValue = this.onNewTimeValue(pTimeStep);
            this.timeValues.add(iIdx, pValue);
          } else {
            iIdx++;
            lTime += this.timeInterval.getInterval();
            continue;
          }
        }
        Collections.sort(this.timeValues, new TimeSeriesComparator());
      }
      
      /* Call onValidate for custom handling of the request.
       */
      this.onValidate();
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.validate Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    }
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Get the TimeSeries TimeZone (Default = {@linkplain TimeZone#getDefault()})
   * @return the assigned Value (can be null).
   */
  public TimeZone getTimeZone() {
    return timeZone;
  }
  
  /**
   * Convert the specified DateTime its TimeStep Equivalent. It first convert the 
   * DateTime to a TimeStep by calling {@linkplain #toTimeStep(myapp.io.DateTime) 
   * toTimeStep} and the convert the TimeStep back to a DateTime by calling {@linkplain 
   * #toDateTime(long) toDateTime}.
   * @param dateTime the Date/Time of interest
   * @return A DateTime that represents a TimeSeries TimeStep (in the TimeSeries 
   * TimeZone)
   */
  public DateTime toTimeStedateTime(DateTime dateTime) {
    DateTime result = null;
    if (dateTime != null) {
      TimeStep pTimeStep = this.toTimeStep(dateTime);
      result = pTimeStep.getDateTime();
    }
    return result;
  }
  
  /**
   * Get the TimeSeries Start TimeStep
   * @return the assigned TimeStep or null if undefined.
   */
  public TimeStep getStartStep() {
    return this.startStep;
  }
  
  /**
   * Get the TimeSeries StartTime. Convert the start TimeStep to a DateTime by calling
   * the {@linkplain #toDateTime(long) toDateTime} method.
   * @return return the StartTime or null if undefined.
   */
  @XmlTransient
  public DateTime getStartTime() {    
    return (this.startStep == null)? null: this.startStep.getDateTime();
  }
  
  /**
   * Set the TimeSeries' StartTime. Ignored if the StartTime is already set or if 
   * (dateTime=null). Else, calls the {@linkplain #toTimeStep(myapp.io.DateTime) 
   * toTimeStep} method to convert the specified dateTime to TimeStep units.
   * @param dateTime the specified StartTime
   * @throws Exception 
   */
  public void setStartTime(DateTime dateTime) {
    if ((this.startStep == null) && (dateTime != null)) {
      this.startStep = this.toTimeStep(dateTime);      
    }
  }
    
  /**
   * Get the TimeSeries EndTime. Convert the End TimeStep to a DateTime by calling
   * the {@linkplain #toDateTime(long) toDateTime} method.
   * @return return the EndTime or null if undefined.
   */
  @XmlTransient
  public DateTime getEndTime() {    
    return (this.endStep == null)? null: this.endStep.getDateTime();
  }
  
  /**
   * Get the TimeSeries End TimeStep
   * @return the assigned TimeStep or null if undefined.
   */  
  public TimeStep getEndStep() {
    return this.endStep;
  }
  
  /**
   * Get the TimeSeries' TimeInterval 
   * @return the assigned Interval
   */
  public TimeInterval getTimeInterval() {
    return this.timeInterval;
  }
  
  /**
   * Get the TimeSeries' TimeInterval's TimeUnit
   * @return the assigned this.timeInterval.timeUnit or null if this.timeInterval=null
   */
  @XmlTransient
  public TimeUnit getTimeUnit() {
    return (this.timeInterval == null)? null: this.timeInterval.getTimeUnit();
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Manage TimeValues">
  /**
   * Overload 1: Get the TimeValue for the Specified DateTime. It calls {@linkplain 
   * #toTimeStep(myapp.io.DateTime) toTimeStep} to convert the DateTime to a TimeSeries
   * TimeStep and the calls {@linkplain #getTimeValue(long) Overload 2} to get the
   * matching TimeValue.
   * @param dateTime the Date/Time value of interest
   * @return the matching TimeValue if in the TimeSeries Range or null is not in range.
   */
  public final TValue getTimeValue(DateTime dateTime) {
    TValue result = null;
    TimeStep lTimeStep = this.toTimeStep(dateTime);
    result = this.getTimeValue(lTimeStep);
    return result;
  }
  
  /**
   * Overload 2: Get the TimeValue for the Specified TimeStep. It calls {@linkplain 
   * #getStepIndex(long) getStepIndex} to get the Index of the TimeStep and then 
   * retrieve the TimeValue using the Index.
   * @param lTimeStep the TimeStep of Interest
   * @return the matching TimeValue if in the TimeSeries Range or null is not in range.
   */
  public final TValue getTimeValue(TimeStep lTimeStep) {
    TValue result = null;
    int iIdx = this.getStepIndex(lTimeStep);
    if (this.isIndexInRange(iIdx)) {
      result = this.timeValues.get(iIdx);
    }
    return result;
  }
  
  /**
   * Overload 2: Get the TimeValue for the Specified TimeValue Index. It retrieves the 
   * TimeValue for the specified Index is iIndex is in Range[0..this.size-1].
   * @param iIndex the TimeValue's Index
   * @return the TimeValue[iIndex] or null is not in range.
   */
  public final TValue getTimeValue(int iIndex) {
    TValue result = null;
    if (this.isIndexInRange(iIndex)) {
      result = this.timeValues.get(iIndex);
    }
    return result;
  }
  
  /**
   * <p>Assign the values of the Source TimeStepValue to its matching TimeStepValue in
   * this TimeSeries instance. If the Source TimeStepValue's TimeStep is out of the 
   * current TimeSeries range, it will add a new TimeStepValue for the Source's 
   * TimeStep, which will expand the imeSeries rang and then assign the Source's values
   * to the new value. Otherwise, it will locate the matching in-range TimeStepValue 
   * and if found, it will assign the Source's values to the matching value. It calls
   * the {@linkplain TimeStepValue#assignTo(myapp.io.timeseries.TimeValue) 
   * TimeStepValue.assignTo} to transfer the TimeStepValue values.</p>
   * <p><b>NOTE:</b> This call will have no effect if the matching in-range 
   * TimeStepValue could not be retrieved (i.e., this.getTimeValue(lTimeStep) returns 
   * null) or is (sourceValue=null).</p>
   * @param sourceValue the Source TimeStepValue of type TValue
   */
  public final void assignTimeValue(TValue sourceValue) {
    if (sourceValue == null) {
      return;
    }
    try {
      this.startUpdating();
      TimeStep timeStep = sourceValue.getTimeStep();
      if (!this.isInRange(timeStep)) {
        TValue newValue = this.newTimeValue(timeStep);
        sourceValue.assignTo(newValue);
      } else {
        int idx = this.getStepIndex(timeStep);
        if (idx >= 0) {
          TValue curValue = this.getTimeValue(idx);
          sourceValue.assignTo(curValue);
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.assignTimeValue Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    } finally {
      this.endUpdating();
    }
  }
  
  /**
   * Overload 1: Get a the current TimeValue of initiate a new TimeValue for the 
   * specified DateTime. It first convert the DateTime to a TimeStep by calling
   * {@linkplain #toTimeStep(myapp.io.DateTime) toTimeStep}. It then call the 
   * {@linkplain #newTimeValue(long) Overload 2} to initiate the return value.
   * @param dateTime the DateTime for the TimeValue
   * @return an existing TimeValue (if previously initiated) or a new TimeValue 
   * instance after adding it to the series.
   */
  public final TValue newTimeValue(DateTime dateTime) throws Exception {
    TValue result = null;
    if (dateTime == null) {
      throw new Exception("The New TimeValue's DateTime cannot be "
              + "unassigned.");
    }
    TimeStep pTimeStep = this.toTimeStep(dateTime);
    result = this.newTimeValue(pTimeStep);
    return result;
  }
    
  /**
   * <p>Overload 2: Get a the current TimeValue of initiate a new TimeValue for the
   * specified TimeStep. This method will automatically expand the TimeSeries' range to
   * include the TimeStep. It first calculates the Index of the TimeStep (iIdx) and then
   * the "Cleaned" Timestep (lCleanStep) and the handles the request as follows:</p><ul>
   *  <li>if (this.mlStartStep == null), call {@linkplain #onSetTimeSeries(long, long)} 
   *  with lStarStep and lEndStep = lCleanStep. This will initiate the first value in
   *  the time series. It will return this value.</li>
   *  <li>if (this.size() == 0), call onSetTimeSeries with lStartTime=this.StartTime and
   *  lEndTime=lCeanTime. It will recalc the iIdx and return the TimeValue for this 
   *  index.</li>
   *  <li>if (this.isIndexInRange(iIdx)), return the TimeValue for this index.</li>
   *  <li>if (this.mlStartStep less than lCleanStep), call onSetTimeSeries with 
   *  lStartTime=this.StartTime and lEndTime=lCeanTime. It will recalc the iIdx and 
   *  return the TimeValue for this index.</li>
   *  <li>if (this.mlStartStep greater than lCleanStep), call onSetTimeSeries with 
   *  lStartTime=lCeanTime and lEndTime=this.StartTime. It will recalc the iIdx and 
   *  return the TimeValue for this index.</li>
   * </ul>   
   * <p><b>NOTE:</b>This calls this.beginUpdating and this.endUpdating before and after
   * the updating of the TimeSeries as discussed above.</p>
   * @param pTimeStep the TimeValue's timestep in the TimeSeries TimeUnit
   * @return a new TimeValue instance
   * @throws Exception if updating of this.values failed or does not include an item of 
   * the lCleanStep after the TimeSeries has been updated.
   */
  public final TValue newTimeValue(TimeStep pTimeStep) throws Exception {
    TValue result = null;
    if (pTimeStep == null) {
      throw new Exception("The New TimeValue's TimeStep cannot be "
              + "unassigned.");
    }
    int iIdx = this.getStepIndex(pTimeStep);
    long lTime = pTimeStep.getTime();
    TimeStep pCleanStep = this.toTimeStep(lTime);
    try {
      this.startUpdating();
      if (this.startStep == null) {
        this.onSetTimeSeries(pCleanStep, pCleanStep);
        if (this.isEmpty()) {
          throw new Exception("Initiating the TimeSeries Failed.");
        }
        result = this.timeValues.get(0);
      } else if (this.isEmpty()) {
        this.onSetTimeSeries(this.startStep, pCleanStep);
        if (this.isEmpty()) {
          throw new Exception("Initiating the TimeSeries Failed.");
        }
        iIdx = this.getStepIndex(pCleanStep);
        if (!this.isIndexInRange(iIdx)) {
          throw new Exception("Updating the TimeSeries to include the ItemStep failed");
        }
        result = this.timeValues.get(iIdx);
      } else if (this.isIndexInRange(iIdx)) {
        result = this.timeValues.get(iIdx);
      } else if ((this.endStep == null) 
                                || (this.endStep.getTime() < pCleanStep.getTime())) {
        this.onSetTimeSeries(this.startStep, pCleanStep);
        iIdx = this.getStepIndex(pCleanStep);
        if (!this.isIndexInRange(iIdx)) {
          throw new Exception("Updating the TimeSeries to include the ItemStep failed");
        }
        result = this.timeValues.get(iIdx);
      } else if (this.startStep.getTime() > pCleanStep.getTime()) {
        this.onSetTimeSeries(pCleanStep, this.startStep);
        iIdx = this.getStepIndex(pCleanStep);
        if (!this.isIndexInRange(iIdx)) {
          throw new Exception("Updating the TimeSeries to include the ItemStep failed");
        }
        result = this.timeValues.get(iIdx);
      }
    } finally {
      this.endUpdating();
    }
    return result;
  }
  
  /**
   * Overload 1: Check if the DateTime is the Range of the TimeSeries. it calls 
   * {@linkplain #toTimeStep(myapp.io.DateTime) toTimeStep} to convert the DateTime to 
   * a TimeStep and call {@linkplain #isInRange(long) Overload 2} to check the range.
   * @param dateTime the DateTime to check.
   * @return true if TimeSeries index of the DateTime is in the TimeSeries Range. 
   */
  public final boolean isInRange(DateTime dateTime) {
    TimeStep lTimeStep = this.toTimeStep(dateTime);
    return this.isInRange(lTimeStep);
  }
  
  /**
   * Overload 2: Check if the TimeStep is the Range of the TimeSeries. Get the 
   * {@linkplain #getStepIndex(long) this.stepIndex} for lTimeStep) and check if 
   * {@linkplain #isIndexInRange(long) Index is In Range}.
   * @param lTimeStep the TimeStep to check for.
   * @return true if index of the TimeStep is in the TimeSeries Range. 
   */
  public final boolean isInRange(TimeStep pTimeStep) {
    int lIndex = this.getStepIndex(pTimeStep);
    return this.isIndexInRange(lIndex);
  }
   
  /**
   * Get the TimeSeries isEmpty state.
   * @return true is the TimeSeries has no TimeValues
   */
  public final boolean isEmpty() {
    return ((this.timeValues == null) || (this.timeValues.size() == 0));
  }
 
  /**
   * Get the Current Size (number of TimeSteps) in the TimeSeries.
   * @return the size of the Values list or 0 if the Values list is undefined.
   */
  public final int size() {
    return (this.timeValues == null)? 0: this.timeValues.size();
  }
  
  /**
   * Called to clear the TimeSeries values. It reset the Value List and the Start- and
   * EndSteps. It also call {@linkplain #onClear()} for custom handling of the request.
   */
  public final void clear() {
    try {
      if (this.timeValues != null) {
        this.timeValues.clear();
        this.timeValues = null;
      }
      
      this.endStep = null;
      this.startStep = null;
      this.updatingCount = 0;
      
      this.onClear();
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.clear Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    }
  }
  
  /**
   * Overload 1: Called to remove all TimeValue before a Specified DateTime. It calls 
   * {@linkplain #toTimeStep(myapp.io.DateTime) toTimeStep} to convert the DateTime to 
   * a TimeStep and the call {@linkplain #removeBefore(long) Overload 2} to complete the
   * request.
   * @param dateTime the DateTime of the new StartStepTime
   */
  public void removeBefore(DateTime dateTime) {
    TimeStep pTimeStep = this.toTimeStep(dateTime);
    this.removeBefore(pTimeStep);
  }
  
  /**
   * <p>Overload 2: Called to remove all TimeValue before a Specified TimeStep. 
   * If the TimeStep is after this.EndTimeStep, it calls this.clear. Otherwise, 
   * it will remove all TimeValues with TimeStep less then lTimeStep. 
   * This.StartTimeStep will be updated to the TimeStep of the First TimeValue in the 
   * list.</p>
   * <p><b>NOTE:</b> This.Start- and EndUpdating is called before and after the value 
   * List has been updated.</p>
   * @param pTimeStep the new StartStepTime of the TimeSeries.
   */
  public void removeBefore(TimeStep pTimeStep) {
    if ((this.isEmpty()) || (pTimeStep == null) || (this.endStep == null)) {
      return;
    }
    
    long lTime = pTimeStep.getTime();
    try {
      this.startUpdating();
      if ((this.endStep.getTime() < lTime)) {
        this.clear();
      } else if (this.startStep.getTime() > lTime) {
        TValue pTimeValue = this.timeValues.get(0);
        Long lStartTime = ((pTimeValue == null) || (pTimeValue.getTimeStep() == null))?
                           null: pTimeValue.getTimeStep().getTime();
        while ((lStartTime != null) && (lStartTime < lTime)) {
          this.timeValues.remove(0);
          pTimeValue = (this.timeValues.isEmpty())? null: this.timeValues.get(0);
          
          lStartTime = ((pTimeValue == null) || (pTimeValue.getTimeStep() == null))?
                           null: pTimeValue.getTimeStep().getTime();
        }

        if (lStartTime == null) {
          this.clear();
        } else {
          this.startStep = pTimeValue.getTimeStep();
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.removeBefore Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    } finally {
      this.endUpdating();
    }
  }
  
  /**
   * Overload 1: Called to remove all TimeValue after a Specified DateTime. It calls 
   * {@linkplain #toTimeStep(myapp.io.DateTime) toTimeStep} to convert the DateTime to 
   * a TimeStep and the call {@linkplain #removeAfter(long) Overload 2} to complete the
   * request.
   * @param dateTime the DateTime of the new EndStepTime
   */
  public void removeAfter(DateTime dateTime) {
    TimeStep pTimeStep = this.toTimeStep(dateTime);
    this.removeAfter(pTimeStep);
  }
  
  /**
   * <p>Overload 2: Called to remove all TimeValue after a Specified TimeStep. 
   * If the TimeStep is before this.StartTimeStep, it calls this.clear. Otherwise, 
   * it will remove all TimeValues with TimeStep greater then lTimeStep. 
   * This.EndTimeStep will be updated to the TimeStep of the Last TimeValue in the list.
   * </p>
   * <p><b>NOTE:</b> This.Start- and EndUpdating is called before and after the value 
   * List has been updated.</p>
   * @param pTimeStep the new EndStepTime of the TimeSeries.
   */
  public final void removeAfter(TimeStep pTimeStep) {
    if ((this.isEmpty()) || (pTimeStep == null) || (this.endStep == null)) {
      return;
    }
    
    long lTime = pTimeStep.getTime();
    try {   
      this.startUpdating();
      if ((this.startStep != null) && (this.startStep.getTime() < lTime)) {
        this.clear();
      } else if ((this.endStep != null) && (this.endStep.getTime() < lTime)) {
        TValue pTimeValue = this.timeValues.get(this.timeValues.size()-1);
        Long lEndTime = ((pTimeValue == null) || (pTimeValue.getTimeStep() == null))?
                           null: pTimeValue.getTimeStep().getTime();
        while ((lEndTime != null) && (lEndTime > lTime)) {
          this.timeValues.remove(this.timeValues.size()-1);
          pTimeValue = 
             (this.timeValues.isEmpty())? null: this.timeValues.get(this.timeValues.size()-1);
          lEndTime = ((pTimeValue == null) || (pTimeValue.getTimeStep() == null))?
                           null: pTimeValue.getTimeStep().getTime();
        }

        if (lEndTime == null) {
          this.clear();
        } else {
          this.endStep = pTimeValue.getTimeStep();
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.removeAfter Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    } finally {
      this.endUpdating();
    }
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Abstract Methods">
  /**
   * <p>MUST OVERRIDE FOR XML SERIALIZATION: The base method returns the currently 
   * assigned timeValues. The override must call the base method and must add the 
   * following XML Annotations:</p><ul>
   *  <li><b>@{@linkplain XmlElementWrapper}(name="MyTimeValues"):</b></li>
   *  <li><b>@{@linkplain XmlElement}(name="timeValue",type=MyTimeValue.class):</b></li>
   * </ul>
   * @return the TimeSeries' timeValues as an list.
   */
  protected List<TValue> getTimeValues() {
    return this.timeValues;
  }
  
  /**
   * <p>MUST OVERRIDE FOR XML SERIALIZATION: This method is called during XML
   * deserialization to assign the the deserialized timeValue to the timeSeries. The
   * timeValues are assigned between calls to {@linkplain #startUpdating()} and 
   * {@linkplain #endUpdating()} to trigger a call to {@linkplain #validate()}</p>
   * <p><b>NOTE:</b> Inheritors must call the super method, but does not have to do
   * anything else.</p>
   * @param timeValues the deserialized TValue values.
   */
  protected void setTimeValues(List<TValue> timeValues) {
    try {
      this.startUpdating();
      this.timeValues = timeValues;
    } finally {
      this.endUpdating();
    }
  }
  
  /**
   * CAN OVERRIDE: Called by {@linkplain #validate() this.validate} after sorting the 
   * TimeSeries Values and add any missing value to allow custom validation of the 
   * TimeSeries.
   * @throws Exception can be thrown and will be caught an logged.
   */
  protected void onValidate() throws Exception {    
  }
  
  /**
   * CAN OVERRIDE: Called by {@linkplain #clear() this.clear} after clearing the Value 
   * list and resetting the Start- and EndTimeSteps. The base method does nothing.
   * @throws Exception can be thrown and will be caught an logged.
   */
  protected void onClear() throws Exception {    
  }
  
  /**
   * ABSTRACT: Subclasses must override this method to initiate a new missing value 
   * instance of the supported TimeValue.
   * @param pTimeStep the TimeValue's timestep
   * @return a new TimeValue instance
   */
  protected abstract TValue onNewTimeValue(TimeStep pTimeStep);
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="TimeSeries Cloning">  
  /**
   * <p>Call to assign all properties and values of this instance to pTarget. The
   * process is ignored if pTarget= null</p>
   * <p><b>NOTE:</b> The TimeValue's are cloned during this process.</p>
   * @param <TSeries> extends TimeSeries<TValue>
   * @param target
   */
  @SuppressWarnings("unchecked")
  public <TSeries extends TimeSeries<TValue>> void assignTo(TSeries target) {
    try {
      target.resetTimeSeries();
      TimeSeries<TValue> timeSeries = target;
      timeSeries.timeInterval = this.timeInterval.clone();
      timeSeries.startStep = this.startStep.clone();
      timeSeries.endStep = this.endStep.clone();
      timeSeries.timeZone = this.timeZone;
      timeSeries.updatingCount = 0;
      if ((this.timeValues != null) && (!this.timeValues.isEmpty())) {
        timeSeries.timeValues = new ArrayList<>();
        for (TValue value : this.timeValues) {          
          TValue clone = (TValue) ((value == null)? null: value.clone());
          if (clone != null) {
            timeSeries.timeValues.add(clone);
          }
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.assignTo Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
    }
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: It creates and new instance of this class and call
   * {@linkplain #assignTo(myapp.io.timeseries.TimeSeries) this.assignTo) to initiate
   * the field values of the clone before it returns the clone.</p>
   */
  @Override
  @SuppressWarnings("unchecked")
  protected TimeSeries<TValue> clone() throws CloneNotSupportedException {
    TimeSeries<TValue> result = null;
    try {
      try {
        result = this.getClass().newInstance();
      } catch (InstantiationException | IllegalAccessException pInExp) {
        throw new Exception("Cloning[" + this.getClass().getSimpleName()
                + "] failed.", pInExp);
      }
      this.assignTo(result);
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.clone Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
      throw new CloneNotSupportedException(pExp.getMessage());
    }
    return result;
  }

  /**
   * {@inheritDoc} <p>OVERRIDE: Return the TiemSeries and its TimeValues as a String.
   * </p>
   */
  @Override
  public String toString() {
    String result = this.getClass().getSimpleName() 
            + "\ntimeInterval = " + this.timeInterval
            + "\nstartStep = " + this.startStep
            + "\nendStep = " + this.endStep
            + "\ntimeZone = " + this.getTimeZoneId();
    
    String subStr = this.onToString();
    if (subStr != null) {
      result += subStr;
    }
    
    if ((this.timeValues == null) || (this.timeValues.isEmpty())) {
      result += "\ntimeValues = empty";
    } else {
      result += "\ntimeValues:";
      int iCnt = 0;
      for (TValue value : this) {
        result += "\n- timeValues[" + iCnt + "] = " + value.toString();
        iCnt++;
      }
    }
    return result;
  }
  
  /**
   * CAN OVERRIDE: call by toString to append additional TimeSeries Field Values before
   * listing the timeValues. The base method return null.
   * @return a string listing the TimeSeries Properties
   */
  protected String onToString() {
    return null;
  }
  //</editor-fold>  

  //<editor-fold defaultstate="collapsed" desc="Implement Interator<TValue>">
  /**
   * <p>IMPLEMENT: Return this.mpValues' iterator. if this.mpValues=null, initiate an
   * empty list and return its iterator.</p>
   * @return the TimeSeries' TimeValue iterator. Can be empty.
   */
  @Override
  public Iterator<TValue> iterator() {
   List<TValue> valueList = this.timeValues;
    if (valueList == null) {
      valueList = new ArrayList<>();
    }
    return valueList.iterator();
  }
  //</editor-fold>
}
