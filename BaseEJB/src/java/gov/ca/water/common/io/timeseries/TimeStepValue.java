package gov.ca.water.common.io.timeseries;

import gov.ca.water.common.io.DateTime;
import java.io.Serializable;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;

/**
 * An object for storing a single time step's value in a timeseries recordset. 
 * @author kprins
 */
public abstract class TimeStepValue implements Serializable {
  
  //<editor-fold defaultstate="collapsed" desc="Static Logger">
  /**
   * Protected Static Logger object for logging errors, warnings, and info messages.
   */
  protected static final Logger logger 
                                    = Logger.getLogger(TimeStepValue.class.getName());
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * Placeholder of the value's TimeStep in the owner TimeSeries' TimeUnits.
   */
  @XmlElement
  private TimeStep timeStep;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Parameterless Constructor for serialization. Set the Datetime and the value as null
   * (i.e {@linkplain #isNullValue()} = true.
   */
  public TimeStepValue() {
    this.timeStep = null;
  }
  
  /**
   * Constructor with a TimeStep and Value
   * @param timeStep the TimeStep (not null)
   */
  public TimeStepValue(TimeStep timeStep) {
    if (timeStep == null) {
      throw new NullPointerException("The TimeValue's TimeStep cannot be unassigned");
    }

    this.timeStep = timeStep;
  }  
  
  /**
   * Constructor with a TimeStep, Value, TimeUnit and TimeZone
   * @param time the TimeStep in the specified TimeUnits
   * @param timeUnit The TimeStep's TimeUnit.
   * @param timeZone the new TimeZone (can be null to use default).
   */
  public TimeStepValue(Long time, TimeUnit timeUnit, TimeZone timeZone) {
    if (time == null) {
      throw new NullPointerException("The TimeValue's Time cannot be unassigned");
    }
    if (timeUnit == null) {
      throw new NullPointerException("The TimeValue's TimeUnit cannot be unassigned");
    }

    this.timeStep = new TimeStep(time, timeUnit, timeZone);
  }  
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Proteced Methods">
  /**
   * Get the TimeStep's TimeZone
   * @return the assigned TimeZone or null if using the Default TimeZone.
   */
  protected TimeZone getTimeZone() {
    return (this.timeStep == null)? null: this.timeStep.getTimeZone();
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Get the TimeStep's DateTime. 
   * @return the {@linkplain TimeStep#getDateTime()} or null if the TimeStep=null.
   */
  public final DateTime getDateTime() {
    return (this.timeStep == null)? null: this.timeStep.getDateTime();
  }

  /**
   * Get the TimeStep in the MilliSeconds
   * @return the {@linkplain TimeStep#getMilliSeconds()} or null if the TimeStep=null.
   */
  public final Long getMilliSeconds() {
    return (this.timeStep == null)? null: this.timeStep.getMilliSeconds();
  }

  /**
   * Get the timestep's in the value's TimeUnit.
   * @return the assigned TimeStep
   */
  public final TimeStep getTimeStep() {
    return this.timeStep;
  }
  
  /**
   * Check is this timestep value was properly initiated 
   * @return true if this.timeStep = null.
   */
  public boolean isNullValue() {
    return (this.timeStep == null);
  }

  /**
   * Check if the record has a missing value.
   * @return true if (this.value=null)
   */
  public abstract boolean isMissingValue();
  
  /**
   * Get whether TimeStepValue's value(s) matches this instance's value(s)
   * @param <TValue> - extends TimeStepValue
   * @param target a target TimeValue to compare to.
   * @return true if the values match (but not necessarily the timeSteps.
   */
  public abstract <TValue extends TimeStepValue> boolean isValue(TValue target);
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc} <p>OVERRIDE: Check if obj matches this instance by comparing the
   * TimeStep, TimeUnit, and values. Returns true if obj != null and a instance of 
   * TimeValue and the TimeStep, TimeUnit, and Value match.</p>
   */
  @Override
  public boolean equals(Object obj) {
    boolean bResult = ((obj != null) && (obj instanceof TimeStepValue));
    if (bResult) {
      TimeStepValue pTrg = (TimeStepValue) obj;
      bResult = ((this.timeStep != null) && (this.timeStep.equals(pTrg.timeStep)));
    }
    return bResult;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return a hash code using the DateTime and value</p>
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.timeStep);
    return hash;
  }
    
  /**
   * CAN OVERRIDE: Called to assign the values of this instance to that of pTarget. 
   * Skipped if pTarget is unassigned.
   * @param <TValue> extends TimeValue
   * @param target The target to assign to.
   */
  protected <TValue extends TimeStepValue> void assignTo(TValue target) {
    if (target != null) {
      TimeStepValue pValue = target;
      pValue.timeStep = this.timeStep.clone();
    }
  }

  /**
   * {@inheritDoc} <p>OVERRIDE: It creates and new instance of this class and call
   * {@linkplain #assignTo(myapp.io.timeseries.TimeValue) this.assignTo) to initiate
   * the field values of the clone before it returns the clone.</p>
   */
  @Override
  protected TimeStepValue clone() throws CloneNotSupportedException {
    TimeStepValue pResult = null;
    try {
      try {
        pResult = this.getClass().newInstance();
      } catch (InstantiationException | IllegalAccessException pInExp) {
        throw new Exception("Cloning[" + this.getClass().getSimpleName()
                + "] failed.", pInExp);
      }
      this.assignTo(pResult);
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.clone Error:\n {1}",
              new Object[]{this.getClass().getSimpleName(), pExp.getMessage()});
      throw new CloneNotSupportedException(pExp.getMessage());
    }
    return pResult;
  }
  
  /**
   * {@inheritDoc} <p>OVERRIDE: Return TimeValue[DateTime; Value]</p>
   */
  @Override
  public String toString() {
    String sResult = this.getClass().getSimpleName() +": timeStep["; 
    sResult += (this.timeStep == null)? "-" : (this.timeStep.toString());
    sResult += "]";
    return sResult;
  }
  //</editor-fold>
}
