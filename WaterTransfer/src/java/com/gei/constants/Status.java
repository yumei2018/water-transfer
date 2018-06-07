package com.gei.constants;

/**
 *
 * @author ymei
 */
public final class Status {

  public static final int DRAFT = 0;
  public static final int SUBMITTED = 1;
  public static final int UREVIEW = 2;
  public static final int INCOMPLETE = 3;
  public static final int PCOMPLETE = 4;
  public static final int PAPPROVED = 5;
  public static final int CEXECUTED = 6;
//  public static final int SUSPEND = 7;
  public static final int CANCEL = 8;
  public static final int TPROGRESS = 9;
  public static final int TCOMPLETE = 10;

  public static final int ACTIVE = 1;
  public static final int NONACTIVE = 0;

  private Status() {
    throw new AssertionError();
  }
}
