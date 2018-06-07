/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.util;

/**
 *
 * @author Charlie Lay
 */
public class StringUtil {
  
  public static boolean isEmpty(String o){
    return o == null || o.trim().isEmpty();
  }
  
  public static boolean equals(String s, String s2){
    return (isEmpty(s) && isEmpty(s2))
            || (!isEmpty(s) && !isEmpty(s2) && s.equals(s2));
  }
  
  public static boolean equalsIgnoreCase(String s, String s2){
    return (isEmpty(s) && isEmpty(s2))
            || (!isEmpty(s) && !isEmpty(s2) && s.equalsIgnoreCase(s2));
  }
  
  public static String clean(String s){
    return isEmpty(s) ? s : s.replaceAll("^\\s*|\\s*$","");
  }
  
  public static String join(String[] words,String del){
    String result="";
    if (words != null && del != null){
      for (String word : words){
        if (!isEmpty(clean(word))) {
          result += del + clean(word);
        }
      }
      result = result.trim().replaceAll("^" + del + "|" + del + "$","");
    }
    return result;
  }
}
