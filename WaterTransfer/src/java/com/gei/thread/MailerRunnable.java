/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.thread;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author J.G. "Koos" Prins, D.Eng. PE.
 */
public class MailerRunnable implements Runnable {

  /**
   * 
   */
  private HtmlEmail emailer;
  
  /**
   * Public Constructor  
   */
  public MailerRunnable(HtmlEmail emailer) {
    this.emailer = emailer;
  }
  
  /**
   * 
   */
  @Override
  public void run(){
    try {
      this.emailer.send();
    } catch (Exception exp) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exp);
    }
  }
}
