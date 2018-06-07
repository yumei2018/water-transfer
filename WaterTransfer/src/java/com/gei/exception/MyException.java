package com.gei.exception;

public class MyException extends Exception {
  
  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private String mCallback;
  private Integer mHttpResponseError;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Constructors">
  /**
   *
   * @param msg
   */
  public MyException(String msg){
    this(msg,null,null);
  }
  
  /**
   *
   * @param msg
   */
  public MyException(String msg,String callback){
    this(msg,callback,null);
  }
  
  /**
   *
   * @param msg
   * @param callback
   */
  public MyException(String msg, String callback,Integer httperror){
    super(msg);
    this.mCallback = callback;
    this.mHttpResponseError = httperror;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Java Object Override Methods">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    this.mCallback = null;
    this.mHttpResponseError = null;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Methods">
  /**
   *
   * @return
   */
  public Integer getHttpResponseError(){
    return this.mHttpResponseError;
  }
  
  /**
   *
   * @return
   */
  public String getCallback(){
    return this.mCallback;
  }
  //</editor-fold>
}
