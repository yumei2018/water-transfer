package gov.ca.water.common.io;

/**
 * A generic Range class. Works with any variable the implements {@linkplain Comparable} 
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class Range<TNum extends Comparable<TNum>> {

  // <editor-fold defaultstate="collapsed" desc="Public Final Fields">
  private TNum min;
  private TNum max;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public Range() {
    super();
    this.min = null;
    this.max = null;
  }
  
  /**
   * Public Constructor  
   */
  public Range(TNum min, TNum max) {
    super();
    this.min = null;
    this.max = null;
    this.grow(min);
    this.grow(max);
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Get the range's minimum value
   * @return the current minimum value
   */
  public TNum getMin() {
    return this.min;
  }
  
  /**
   * Get the range's minimum value
   * @return the current minimum value
   */
  public TNum getMax() {
    return this.max;
  }
  
  /**
   * Check whether <tt>value</tt> is within the range
   * @param value the value to evaluate
   * @return true if the value is in the range; false is null, this.isEmpty, or not in
   * range.
   */
  public boolean inRange(TNum value) {
    boolean result = false;
    if ((value != null) && (!this.isEmpty())) {
      result = ((value.compareTo(this.min) >= 0) &&
                (value.compareTo(this.max) <= 0));
    }
    return result;
  }
  
  /**
   * Grow the limits of the range by the value. If the range is empty then the min and 
   * max value will be set to <tt>value</tt> 
   * @param value the new value - ignored if null.
   */
  public final void grow(TNum value) {
    if (value == null) {
      return;
    }
    
    if (this.isEmpty()) {
      this.min = value;
      this.max = value;
    } else if (value.compareTo(this.min) < 0) {
      this.min = value;
    } else if (value.compareTo(this.max) > 0) {
      this.max = value;
    }
  }
  
  /**
   * Grow the range to include all <tt>values</tt>
   * @param values an array of values
   */
  @SuppressWarnings("unchecked")
  public final void grow(TNum...values) {
    if ((values == null) || (values.length == 0)) {
      return;
    }
    for (TNum value : values) {
      this.grow(value);
    }
  }
  
  /**
   * Get whether the Range has assigned value (can be the same value)
   * @return ((this.min == null) || (this.max == null))
   */
  public final boolean isEmpty() {
    return ((this.min == null) || (this.max == null));
  }
  
  /**
   * Called to reset the range
   */
  public void reset() {
    this.max = null;
    this.min = null;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc}
   * <p>OVERRIDE: </p>
   */
  @Override
  public String toString() {
    return "Range[min = " + this.min + "; max = " + this.max + "]" ;
  }
  // </editor-fold>
}
