package gov.ca.water.common.io.timeseries;

import java.util.Comparator;

/**
 * A Comparator for sorting {@linkplain TimeValue} entities by their TimeStep in 
 * MilliSeconds.
 * @author kprins
 */
public class TimeSeriesComparator implements Comparator<TimeStepValue> {
  
  //<editor-fold defaultstate="collapsed" desc="Private Fields">
  /**
   * Flag controlling the sort Order (default=true|null)
   */
  private Boolean mbAscending;
  //</editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public TimeSeriesComparator() {
    super();    
    this.mbAscending = null;
  }
  
  /**
   * Public Constructor
   */
  public TimeSeriesComparator(boolean bAscending) {
    this();    
    this.mbAscending = (bAscending)? null: bAscending;
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   * Get the Comparator isAscendign sort order setting.
   * @return true if sorting in ascending order.
   */
  public boolean isAscending() {
    return ((this.mbAscending == null) || (this.mbAscending));
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Implement Comparator">
  /**
   * {@inheritDoc} <p>IMPLEMENT: return 0 if both values are null. Return -1 if
   * pValue1=null, or 1 if pValue2=null. Otherwise, get the values TiemStep in
   * MilliSeconds and return the 1, 0, or -1 if the Timestep for pValue1 is greater
   * than, equal to, or less than that of pValue2, respectively. if the comparison is 
   * not equal (0) and (!this.isAscending), switch the returned value form positive to
   * negative and vise versa.
   * </p>
   */
  @Override
  public int compare(TimeStepValue value1, TimeStepValue value2) {
    int iResult = 0;
    if ((value1 != null) && (value2 != null)) {
      if (value1 == null) {
        iResult = -1;
      } else if (value2 == null) {
        iResult = 1;
      } else {
        Long lTime1 = value1.getMilliSeconds();
        Long lTime2 = value2.getMilliSeconds();
        iResult = lTime1.compareTo(lTime2);
      }
    }
    
    if ((iResult != 0) && (!this.isAscending())) {
      iResult = -1*iResult;
    }
    
    return iResult;
  }
  //</editor-fold>
}
