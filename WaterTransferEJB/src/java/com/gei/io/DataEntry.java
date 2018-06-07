package com.gei.io;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
//import myapp.io.enums.UrlProtocolEnums;
//import myapp.io.url.UrlValidator;

/**
 *
 * @author kprins
 */
public class DataEntry {

  //<editor-fold defaultstate="collapsed" desc="Static Public Fields">
  /**
   * The String representing a 'hard' space.
   */
  public static final String nbsp = "&nbsp;";

  /**
   * Text Trim Enums
   */
  public static final int TEXT_TRIMLEFT = 1;
  public static final int TEXT_TRIMRIGHT = 2;
  public static final int TEXT_TRIMALL = 3;
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Static Logger">
  /**
   * Static Error Logger for the Facade Class
   */
  private static final Logger logger = Logger.getLogger(DataEntry.class.getSimpleName());
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="String Conversions/Test">
  /**
   * Check if sInsTr string is an empty string (""|null).
   * @param sInStr the string to check
   * @return true if cleanString(sInstr) == null
   */
  public static boolean isEmpty(String sInStr) {
    return (DataEntry.cleanString(sInStr) == null);
  }

  /**
   * OVERLOAD 1: Returns a trimmed string or null if the string is empty.
   * @param sInStr
   * @return String
   */
  public static String cleanString(String sInStr) {
    return ((sInStr == null) || (sInStr.trim().equals(""))) ? null
            : sInStr.trim();
  }

  /**
   * OVERLOAD 2: Returns a trimmed string or null if the string is empty.
   * If the length of the trimmed exceeds iMaxLength (for iMaxLength > 0), it will
   * return the first iMaxLength of the trimmed string.  Use this call for preparing
   * limited length string for assigning to the database records.
   * @param sInStr String
   * @param iMaxLength int
   * @return String
   */
  public static String cleanString(String sInStr, int iMaxLength) {
    String sResult = ((sInStr == null) || (sInStr.trim().equals(""))) ? null
            : sInStr.trim();
    if ((sResult != null) && (iMaxLength > 0) && (sResult.length() > iMaxLength)) {
      sResult = sResult.substring(0, iMaxLength);
    }
    return sResult;
  }
  /**
   * OVERLOAD 3: Clean the string by trimming it according to eTextTrim(TEXT_TRIMLEFT |
   * TEXT_TRIMRIGHT | TEXT_TRIMALL) and truncate the string to iMaxLength if
   * (iMaxLength>0) and the string's length exceeds iMaxLength.  Use this call for
   * preparing limited length string for assigning to the database records and
   * preserving either leading or trailing blanks.
   * @param sInStr the input String
   * @param iMaxLength the maximum sting length
   * @return the cleaned string null if the string is empty.
   */
  public static String cleanString(String sInStr, int iMaxLength, int eTextTrim) {
    String sResult = null;
    if (sInStr != null) {
      switch (eTextTrim) {
        case DataEntry.TEXT_TRIMLEFT:
          sResult = sInStr + "_x";
          sResult = sResult.trim();
          if (sResult.equals("_x")) {
            sResult = null;
          } else {
            sResult = sResult.substring(0, (sResult.length()-2));
          }
          break;
        case DataEntry.TEXT_TRIMRIGHT:
          sResult = "x_" + sInStr;
          sResult = sResult.trim();
          if (sResult.equals("x_")) {
            sResult = null;
          } else {
            sResult = sResult.substring(2);
          }
          break;
        default:
          sResult = sInStr.trim();
          break;
      }
    }

    if ((sResult != null) && (iMaxLength > 0) && (sResult.length() > iMaxLength)) {
      sResult = sResult.substring(0, iMaxLength);
    }
    return sResult;
  }

  /**
   * OVERLOAD 4: Called to "clean" sValue (i.e., removing trailing blanks and delimiters
   * as defined by sDelimiters.
   * @param sValue String
   * @param sDelimiters String
   * @return String
   */
  public static String cleanString(String sValue, String sDelimiters) {
    String sResult = DataEntry.cleanString(sValue);
    sDelimiters = DataEntry.cleanString(sDelimiters);
    if (sDelimiters != null) {
      while (sResult != null) {
        String sLast = sResult.substring(sResult.length()-1);
        if (sLast.matches(sDelimiters)) {
          sResult = DataEntry.cleanString(sResult.substring(0, sResult.length()-1));
        } else {
          break;
        }
      }
    }
    return sResult;
  }

  /**
   * Split the input string into a list of substring using the specified Delimiter to
   * split the string.
   * Return empty values as "" and return an empty string id sStringList = null|""
   * @param inputStr the input string to parse into sub-strings
   * @param delimiter the delimiter use to split the list (default=" ").
   * @return the list if sub-string or an empty list if the input string is empty.
   */
  public static List<String> splitString(String inputStr, String delimiter) {
    List<String> result = new ArrayList<String>();
    inputStr = DataEntry.cleanString(inputStr);
    delimiter = ((delimiter == null) || (delimiter.equals(""))) ? " " : delimiter;
    if (inputStr != null) {
      if (inputStr.equals(delimiter)) {
        result.add("");
      } else {
        int iLast = 0;
        int iPos = inputStr.indexOf(delimiter);
        if (iPos < 0) {
          result.add(inputStr);
        } else {
          while (iPos > 0) {
            String subStr = DataEntry.cleanString(inputStr.substring(iLast, iPos));
            if (subStr != null) {
              result.add(subStr);
            } else {
              result.add("");
            }
            iLast = iPos + delimiter.length();
            iPos = inputStr.indexOf(delimiter, iLast);
          }
          int len = inputStr.length();
          if (iLast < len) {
            String subStr = DataEntry.cleanString(inputStr.substring(iLast));
            if (subStr != null) {
              result.add(subStr);
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * Return the Proper Case of the String.
   * @param sInStr String
   * @return String
   */
  public static String toProper(String sInStr) {
    String sResult = null;
    sInStr = (sInStr == null)? null: sInStr.trim().toLowerCase();
    if ((sInStr != null) && (sInStr.length() > 0)) {
      List<String> pWords = DataEntry.splitString(sInStr, " ");
      if (pWords.size() > 0) {
        for (String sSubStr: pWords) {
          sSubStr = sSubStr.trim();
          if (sSubStr.equals("")) {
            continue;
          }

          String sCh1 = sSubStr.substring(0, 1);
          String sCh2 = sCh1.toUpperCase();
          sSubStr = sSubStr.replaceFirst(sCh1, sCh2);
          if (sResult == null) {
            sResult = sSubStr;
          } else {
            sResult += (" " + sSubStr);
          }
        }
      }
    }
    return sResult;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="String Tests">
  /**
   * Check if sNewValue matches sCurValue. First clean sNewValue by calling the
   * cleanString method and then compare them - ignoring case is bIngnoreCase.
   * Return true if both strings are empty or match.
   * Note: sCurvalue is not "cleaned"
   * @param sCurValue the current value
   * @param sNewValue the new input value
   * @param bIgnoreCase if true, do a case insensitive comparison
   * @return boolean
   */
  public static boolean isEqual(String sCurValue, String sNewValue,
          boolean bIgnoreCase) {
    sNewValue = DataEntry.cleanString(sNewValue);
    return ((sCurValue == null) ? (sNewValue == null)
            : ((bIgnoreCase)? sCurValue.equalsIgnoreCase(sNewValue):
            sCurValue.equals(sNewValue)));
  }

  /**
   * Check if sNewValue matches sCurValue. If (!bSkipTrim), first clean sNewValue by
   * calling the cleanString method and then compare them - ignoring case if
   * bIngnoreCase=true. Return true if both strings are empty or match.
   * Note: sCurvalue is not "cleaned"
   * @param sCurValue the current value
   * @param sNewValue the new input value
   * @param bIgnoreCase if true, do a case insensitive comparison
   * @param bSkipTrim if true, sNewValue is not trimmed to remove leading and/or
   * trailing spaces.
   * @return boolean
   */
  public static boolean isEqual(String sCurValue, String sNewValue,
          boolean bIgnoreCase, boolean bSkipTrim) {
    sNewValue = (bSkipTrim)? sNewValue: DataEntry.cleanString(sNewValue);
    return ((sCurValue == null) ? (sNewValue == null)
            : ((bIgnoreCase)? sCurValue.equalsIgnoreCase(sNewValue):
            sCurValue.equals(sNewValue)));
  }

  /**
   * Check if sNewValue matches sCurValue. First clean sNewValue by calling the
   * cleanString with iMaxLength set method and then compare them -
   * ignoring case is bIngnorCase.Return true if both strings are empty or match.
   * Note: sCurvalue is not "cleaned"
   * @param sCurValue String
   * @param sNewValue String
   * @param bIgnoreCase boolean
   * @param iMaxLength int
   * @return boolean
   */
  public static boolean isEqual(String sCurValue, String sNewValue,
          boolean bIgnoreCase, int iMaxLength) {
    sNewValue = DataEntry.cleanString(sNewValue, iMaxLength);
    return ((sCurValue == null) ? (sNewValue == null)
            : ((bIgnoreCase)? sCurValue.equalsIgnoreCase(sNewValue):
            sCurValue.equals(sNewValue)));
  }

  /**
   * Check if a value (sValue) is equal to a value in the array pValue of possible
   * values. Return false if sValue=""|null or if pValue=null|empty. Matchings is not
   * case sensitive.
   * @param sValue string to check
   * @param pValues array of string to match
   * @return true if a match is found.
   */
  public static boolean inList(String sValue, String[] pValues) {
    boolean bResult = false;
    try {
      sValue = DataEntry.cleanString(sValue);
      if ((sValue != null) || (pValues != null) && (pValues.length > 0)) {
        for (String sItem : pValues) {
          if (sValue.equalsIgnoreCase(sItem)) {
            bResult = true;
            break;
          }
        }
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.inList Error:\n {1}",
              new Object[]{"DataEntry", pExp.getMessage()});
    }
    return bResult;
  }

  /**
   * Called to left Pad the sInstr with sPadStr until the sInt.Length is >= iLength.
   * if (sInstr = null), set sInstr = "" before padding. if sPadStr = empty or null,
   * set sPadStr = " ".
   * @param sInStr String
   * @param sPadStr String
   * @param iLength int
   * @return String
   */
  public static String leftPadString(String sInStr, String sPadStr, int iLength) {
    String sResult = (sInStr == null) ? "" : sInStr;
    sPadStr = ((sPadStr == null) || (sPadStr.equals(""))) ? " " : sPadStr;
    while (sResult.length() < iLength) {
      sResult = sPadStr + sResult;
    }
    return sResult;
  }

  /**
   * Trim all leading and ending spaces and if the input string is not empty strip out
   * any non-numeric characters
   * @param sInStr
   * @param bIsDecimal
   * @return String
   */
  public static String cleanNumericString(String sInStr) {
    String sResult = null;
    sInStr = DataEntry.cleanString(sInStr);
    if (sInStr != null) {
      String sRegExp = "^[\\D]*[ ]*g$";
      sResult = sInStr.replaceAll(sRegExp, "");
    }
    return sResult;
  }

  /**
   * Format a numeric string by adding a delimiter (e.g., sDelimiter="-" - not an empty
   * string, but it could be a space or spaces) at the character spacing defined in
   * pSpacing after sInStr is trimmed from any leading or ending spaces. For example
   * formating sInstr="0000000000" using pSpacing={3,3,4} using sDelimiter="-" will
   * return "000-000-0000". If sInstr.length exceeds the sum of pSpacing, the trailing
   * characters will be ignored.  if sInstr.length less than the sum of pSpacing, the
   * String will be right padded with "0". The trimmed sInstr will be returned if
   * (pSpacing=null|empty) or (sDelimiter=null|"").
   * @param sInStr String
   * @param bIsDecimal
   * @return String
   */
  public static String formatNumericString(String sInStr, String sDelimiter,
            int[] pSpacings) {
    String sResult = null;
    sInStr = DataEntry.cleanString(sInStr);
    if ((sDelimiter == null) || (sDelimiter.equals("")) ||
            (pSpacings == null) || (pSpacings.length == 0)) {
      sResult = sInStr;
    } else if (sInStr != null) {
      int iSumChars = 0;
      for (int iChars : pSpacings) {
        iSumChars += iChars;
      }

      if (sInStr.length() < iSumChars) {
        sInStr = DataEntry.rightPadString(sInStr, "0", iSumChars);
      }

      int iLast = 0;
      List<String> pSubStrings = new ArrayList<String>();
      for (int iChars : pSpacings) {
        if (iChars <= 0) {
          continue;
        }
        int iNext = iLast + iChars;
        if (iNext >= (sInStr.length()-1)) {
          pSubStrings.add(sInStr.substring(iLast));
          iLast = sInStr.length();
          break;
        } else {
          pSubStrings.add(sInStr.substring(iLast, iNext));
          iLast = iNext;
        }
      }

      if (pSubStrings.size() <= 1) {
        sResult = sInStr;
      } else {
        for (String sSubStr : pSubStrings) {
          if (sResult == null) {
            sResult = sSubStr;
          } else {
            sResult += sDelimiter;
            sResult += sSubStr;
          }
        }
      }
    }
    return sResult;
  }

  /**
   * Called to Pad right the sInstr with sPadStr until the sInt.Length is >= iLength.
   * if (sInstr = null), set sInstr = "" before padding. if sPadStr = empty or null,
   * set sPadStr = " ".
   * @param sInStr String
   * @param sPadStr String
   * @param iLength int
   * @return String
   */
  public static String rightPadString(String sInStr, String sPadStr, int iLength) {
    String sResult = (sInStr == null) ? "" : sInStr;
    sPadStr = ((sPadStr == null) || (sPadStr.equals(""))) ? " " : sPadStr;
    while (sResult.length() < iLength) {
      sResult += sPadStr;
    }
    return sResult;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Number/Boolean Test/Conversions">
  /**
   * Return a "clean" integer value or null if the value is unassigned or invalid.
   * if iLBound or iUBound (i.e., the Lower or upper value range boundaries) are set
   * and iVal falls outside the boundaries return a null value.
   * @param iVal Integer
   * @param iLBound Integer
   * @param iUBound Integer
   * @return Integer
   */
  public static Integer cleanInteger(Integer iVal, Integer iLBound, Integer iUBound) {
    Integer iResult = iVal;
    if (iVal != null) {
      if ((iLBound != null) &&  (iVal < iLBound)) {
        iResult = null;
      } else if ((iUBound != null) && (iVal > iUBound)) {
        iResult = null;
      }
    }
    return iResult;
  }

  /**
   * Return a "clean" integer value or null if the value is unassigned or invalid.
   * if iLBound or iUBound (i.e., the Lower or upper value range boundaries) are set
   * and iVal falls outside the boundaries return a null value.
   * @param iVal Long
   * @param iLBound Long
   * @param iUBound Long
   * @return Long
   */
  public static Long cleanInteger(Long iVal, Long iLBound, Long iUBound) {
    Long iResult = iVal;
    if (iVal != null) {
      if ((iLBound != null) &&  (iVal < iLBound)) {
        iResult = null;
      } else if ((iUBound != null) && (iVal > iUBound)) {
        iResult = null;
      }
    }
    return iResult;
  }

  /**
   * Check if iNewValue matches iCurValue.
   * Return true if both values are null or match.
   * @param iCurValue the current/base Integer Value
   * @param iNewValue the new Integer value
   * @return true if both is null or equal.
   */
  public static boolean isEqual(Integer iCurValue, Integer iNewValue) {
    return (iCurValue == null) ? (iNewValue == null) : iCurValue.equals(iNewValue);
  }

  /**
   * Check if iNewValue  complies to range set limits and matches iCurValue.
   * Call cleanInteger method on the iNewValue before comparing the two values.
   * Return true if both values are null or match.
   * @param iCurValue the current/base Integer Value
   * @param iNewValue the new Integer value
   * @param bIgnoreCase boolean
   * @return true if both is null or equal and in bounds
   */
  public static boolean isEqual(Integer iCurValue, Integer iNewValue,
          Integer iLBound, Integer iUBound) {
    iNewValue = DataEntry.cleanInteger(iNewValue, iLBound, iUBound);
    return (iCurValue == null) ? (iNewValue == null): iCurValue.equals(iNewValue);
  }

  /**
   * Check if iNewValue matches iCurValue.
   * Return true if both values are null or match.
   * @param iCurValue the current/base Long Value
   * @param iNewValue the new Long value
   * @return true if both is null or equal.
   */
  public static boolean isEqual(Long iCurValue, Long iNewValue) {
    return (iCurValue == null) ? (iNewValue == null) : iCurValue.equals(iNewValue);
  }

  /**
   * Check if iNewValue  complies to range set limits and matches iCurValue.
   * Call cleanInteger method on the iNewValue before comparing the two values.
   * Return true if both values are null or match.
   * @param iCurValue the current/base Integer Value
   * @param iNewValue the new Integer value
   * @param bIgnoreCase boolean
   * @return true if both is null or equal and in bounds
   */
  public static boolean isEqual(Long iCurValue, Long iNewValue,
          Long iLBound, Long iUBound) {
    iNewValue = DataEntry.cleanInteger(iNewValue, iLBound, iUBound);
    return (iCurValue == null) ? (iNewValue == null): iCurValue.equals(iNewValue);
  }

  /**
   * Check if iNewValue matches iCurValue.
   * Return true if both values are null or match.
   * @param iCurValue the current/base Short Value
   * @param iNewValue the new Short value
   * @return true if both is null or equal.
   */
  public static boolean isEqual(Short iCurValue, Short iNewValue) {
    return (iCurValue == null) ? (iNewValue == null) : iCurValue.equals(iNewValue);
  }

  /**
   * Check if iNewValue matches iCurValue.
   * Return true if both values are null or match.
   * @param dCurValue Double
   * @param dNewValue Double
   * @return boolean
   */
  public static boolean isEqual(Double dCurValue, Double dNewValue) {
    return (dCurValue == null) ? (dNewValue == null) : dCurValue.equals(dNewValue);
  }

  /**
   * Check if bNewValue matches bCurValue.
   * Return true if both values are null or match.
   * @param dCurValue current Boolean value
   * @param dNewValue new Boolean value
   * @return true if both is null or equal.
   */
  public static boolean isEqual(Boolean dCurValue, Boolean dNewValue) {
    return (dCurValue == null) ? (dNewValue == null) : dCurValue.equals(dNewValue);
  }

  /**
   * Check if pNewValue matches pCurValue.
   * Return true if both values are null or match.
   * @param pCurValue Object
   * @param pNewValue Object
   * @return boolean
   */
  public static boolean isEqual(Object pCurValue, Object pNewValue) {
    return (pCurValue == null) ? (pNewValue == null) : pCurValue.equals(pNewValue);
  }

 /**
   * Return the Parsed integer value represented by sInval. If it contains a period (.)
   * it will return the integer value of the string left of the period. It will remove
   * all "," separators from the string. If the string is empty or the conversion fails
   * it returns iDefault.
   * @param sInVal String
   * @param iDefault int
   * @return int
   */
  public static int parseInt(String sInVal, int iDefault) {
    Integer iResult = null;
    try {
      sInVal = DataEntry.cleanString(sInVal);
      if (sInVal != null) {
        int iIdx = sInVal.indexOf(".");
        if (iIdx > 0) {
          sInVal = sInVal.substring(0,iIdx);
        }
        sInVal.replaceAll(",", "");
        if (!sInVal.equals("")) {
          iResult = Integer.parseInt(sInVal);
        }
      }
    } catch (Exception pExp) {
      iResult = null;
    }
    return (iResult == null)? iDefault: iResult;
  }

  /**
   * Return the Parsed double value represented by sInval. It will remove
   * all "," separators from the string. If the string is empty or the conversion fails
   * it returns dDefault.
   * @param sInVal String
   * @param dDefault double
   * @return double
   */
  public static double parseDouble(String sInVal, double dDefault) {
    Double iResult = null;
    try {
      sInVal = DataEntry.cleanString(sInVal);
      if (sInVal != null) {
        sInVal.replaceAll(",", "");
        if (!sInVal.equals("")) {
          iResult = Double.parseDouble(sInVal);
        }
      }
    } catch (Exception pExp) {
      iResult = null;
    }
    return (iResult == null)? dDefault: iResult;
  }

  /**
   * Return iNumber as an alpha character String consisting of upper case values A..Z.
   * Valid iNumber values are greater than 0.  Returns "" if iNumber is zero, negative
   * or null. For numbers greater than 25, a "X" will prepend for the number of times
   * the number is larger the 25. Example: iNumber=26 returns "XA", and iNumber=51
   * returns "XXA".
   * @param iNumber Integer
   * @return String
   */
  public static String toAlpha(Integer iNumber) {
    String sAlpha = "";
    if ((iNumber != null) && (iNumber > 0)) {
      Integer iBase = 64;
      Integer iNumChars = 26;
      Integer iRemain = iNumber;
      while (iRemain > 0) {
        if (iRemain >= iNumChars) {
          sAlpha += "X";
        } else {
          char pChar = (char)(iBase+iRemain);
          sAlpha += String.valueOf(pChar);
        }
        iRemain -= iNumChars;
      }
    }
    return sAlpha;
  }

  /**
   * Return a List with the non-null elements of pArray.
   * @param <T>
   * @param pArray T[]
   * @return List<T>
   */
  public static <T> List<T> newAsList(T[] pArray) {
    List<T> pResults = new ArrayList<T>();
    if ((pArray != null) && (pArray.length > 0)) {
      for (T pObj : pArray) {
        if (pObj != null) {
          pResults.add(pObj);
        }
      }
    }
    return pResults;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Input Validations">
  /**
   * Check is the sInput complies with the set format of a parameter name
   * consisting of only alphanumeric characters or underscore and starting with
   * a letter. No limit in the length.
   * @param sInput
   * @return boolean
   */
  public static boolean isValidParameter(String sInput) {
    boolean bIsValid = false;
    String sRegExp = "^[a-zA-Z][a-zA-Z0-9_]*$";
    sInput = DataEntry.cleanString(sInput);
    bIsValid = (sInput == null)? false : sInput.matches(sRegExp);
    return bIsValid;
  }

  /**
   * Validate that the e-mail entry format is correct. Use InternetAddress.parse() to
   * check if it is valid.
   * @param sInput String
   * @return boolean
   */
  public static boolean isValidEMail(String sInput) {
    boolean bIsValid = false;
    try {
      sInput = DataEntry.cleanString(sInput);
      if (sInput != null) {
        String sRegExp = "(^[a-z]([a-z0-9_.-]*)@([a-z]([a-z0-9_.-]*))"
                + "([.][a-z]{3})$)|"
                + "(^[a-z]([a-z0-9_.-]*)@([a-z]([a-z0-9_.-]*))"
                + "(.[a-z]{3})(.[a-z]{2})*$)";
        bIsValid = sInput.matches(sRegExp);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "DataEntry.isValidEMail Error:\n {1}",
              new Object[]{pExp.getMessage()});
    }
    return bIsValid;
  }

  /**
   * Check is the username complies with the set format consisting of 5-16
   * characters, and only alphanumeric characters or underscore. The username
   * must start with a letter.
   * @param sInput
   * @return boolean
   */
  public static boolean isValidUsername(String sInput) {
    boolean bIsValid = false;
    String sRegExp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    bIsValid = ((sInput == null) || (sInput.equals(""))) ? false
            : sInput.matches(sRegExp);
    return bIsValid;
  }

  public static String UsernameHint = "A valid Username has 5-16 alpha-numeric "
          + "characters or underscores. No spaces are allowed.";

  /**
   * Check if the sInput is a valid 10-digit telephone number. Call
   * getTelNoasNumber to strip all non-numeric characters and return true
   * if the length = 10 and the first digit is not 0.
   * @param sInput String
   * @return boolean
   */
  public static boolean isValidTelNo(String sInput) {
    boolean bIsValid = false;

    String sNumber = DataEntry.getTelNoasNumber(sInput);
    bIsValid = (sNumber == null) ? false
            : ((sNumber.length() == 10) && (!sNumber.startsWith("0")));
    return bIsValid;
  }

  /**
   * Strip the any spaces, brackets, dashes, etc. and return only the digits
   * of the sInput telephone number
   * @param sInput String
   * @return String
   */
  public static String getTelNoasNumber(String sInput) {
    String sNumber = "";
    if ((sInput != null) && (!sInput.trim().equals(""))) {
      sNumber = sInput.replaceAll("[ ()-./]*", "");
    }
    return DataEntry.cleanString(sNumber.trim());
  }

  /**
   * Convert sInput to the standard "(000) 000-0000" format. return an empty
   * string is the number is invalid
   * @param sInput String
   * @return String
   */
  public static String getTelNoAsString(String sInput) {
    String sOutStr = "";
    String sNumber = getTelNoasNumber(sInput);
    if ((sNumber != null) && (sNumber.length() == 10)
            && (!sNumber.startsWith("0"))) {
      sOutStr = "(" + sNumber.substring(0, 3) + ")";
      sOutStr += " " + sNumber.substring(3, 6) + "-";
      sOutStr += sNumber.substring(6);
    }
    return DataEntry.cleanString(sOutStr);
  }

  /**
   * Check if the sInput is a valid web site Address. It initiates a UrlValidator and
   * if sInput is not empty, call the validator's isValidInput method to validate the
   * sInput.
   * @param sInput the input to validate
   * @return false if sInput=""|null or UrlValidator.isValidInput(sInput)
   */
  public static boolean isValidWebAddress(String sInput) {
    boolean bResult = false;
    try {
      sInput = DataEntry.cleanString(sInput);
      if (sInput != null) {
//        int[] pProtocols = new int[]{UrlProtocolEnums.HTTP, UrlProtocolEnums.HTTPS};
//        UrlValidator pValidator = new UrlValidator();
//        pValidator.setUrlProtocols(pProtocols, UrlProtocolEnums.HTTP);

//        bResult = pValidator.isValidInput(sInput);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.isValidWebAddress Error:\n {1}",
              new Object[]{"DataEntry", pExp.getMessage()});
    }
    return bResult;
  }

  /**
   * Clean the sInput and return a fully resolved web site address. For example:
   * sInput="www.mysite.com" will return "http://www.mysite.com"
   * @param sInput the input to clean
   * @return a fully resolved web site address or null if sInput is invalid.
   */
  public static String cleanWebAddress(String sInput) {
    String sResult = null;
    try {
      sInput = DataEntry.cleanString(sInput);
      if (sInput != null) {
//        int[] pProtocols = new int[]{UrlProtocolEnums.HTTP, UrlProtocolEnums.HTTPS};
//        UrlValidator pValidator = new UrlValidator();
//        pValidator.setUrlProtocols(pProtocols, UrlProtocolEnums.HTTP);
//
//        sResult = pValidator.toString(sInput);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.getCleanWebAddress Error:\n {1}",
              new Object[]{"DataEntry", pExp.getMessage()});
    }
    return sResult;
  }

  /**
   * Check if the sInput is a valid ftp site Address. It initiates a UrlValidator and
   * if sInput is not empty, call the validator's isValidInput method to validate the
   * sInput.
   * @param sInput the input to validate
   * @return false if sInput=""|null or UrlValidator.isValidInput(sInput)
   */
  public static boolean isValidFtpAddress(String sInput) {
    boolean bResult = false;
    try {
      sInput = DataEntry.cleanString(sInput);
      if (sInput != null) {
//        int[] pProtocols = new int[]{ UrlProtocolEnums.FTP};
//        UrlValidator pValidator = new UrlValidator();
//        pValidator.setUrlProtocols(pProtocols, UrlProtocolEnums.FTP);
//
//        bResult = pValidator.isValidInput(sInput);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.isValidFtpAddress Error:\n {1}",
              new Object[]{"DataEntry", pExp.getMessage()});
    }
    return bResult;
  }

  /**
   * Clean the sInput and return a fully resolved ftp site address. For example:
   * sInput="ftp.mysite.com" will return "ftp://ftp.mysite.com"
   * @param sInput the input to clean
   * @return a fully resolved ftp site address or null if sInput is invalid.
   */
  public static String cleanFtpAddress(String sInput) {
    String sResult = null;
    try {
      sInput = DataEntry.cleanString(sInput);
      if (sInput != null) {
//        int[] pProtocols = new int[]{UrlProtocolEnums.FTP};
//        UrlValidator pValidator = new UrlValidator();
//        pValidator.setUrlProtocols(pProtocols, UrlProtocolEnums.FTP);
//
//        sResult = pValidator.toString(sInput);
      }
    } catch (Exception pExp) {
      logger.log(Level.WARNING, "{0}.getCleanWebAddress Error:\n {1}",
              new Object[]{"DataEntry", pExp.getMessage()});
    }
    return sResult;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Password Validation/Conversions">
  /**
   * Check whether Pw1==Pw2. Pw1 must be a valid password and Pw2 will be
   * matched against pw1 - ignoring case for iPwType<2
   * @param sPw1 String
   * @param sPw2 String
   * @param iPwType Integer
   * @return boolean
   */
  public static boolean isPasswordEqual(String sPw1, String sPw2,
          Integer iPwType) {
    boolean bIsMatch = false;
    iPwType = (iPwType == null) ? 0 : ((iPwType > 2) ? 2 : iPwType);
    sPw1 = DataEntry.cleanString(sPw1);
    sPw2 = DataEntry.cleanString(sPw2);
    if (isValidPassword(sPw1, iPwType)) {
      bIsMatch = (iPwType == 2) ? sPw1.equals(sPw2) : sPw1.equalsIgnoreCase(sPw2);
    }
    return bIsMatch;
  }

  /**
   * Check if the Password complies with the required password format for the
   * specified type with iPwType=0..2. Length 6-16
   * 0=Simple Alpha-numeric password
   * 1=Medium Strong
   * 2=Strong Password
   * @param sInput String
   * @param iPwType Integer
   * @return boolean
   */
  public static boolean isValidPassword(String sInput, Integer iPwType) {
    boolean bIsValid = false;
    iPwType = (iPwType == null) ? 0 : ((iPwType > 2) ? 2 : iPwType);
    String sRegExp = "^[a-zA-Z][a-zA-Z0-9]{5,15}$";
    if (iPwType == 1) {
      sRegExp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?!.*\\s).{6,16}$";
    } else if (iPwType == 2) {
      sRegExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{6,16}$";
    }

    bIsValid = ((sInput == null) || (sInput.equals(""))) ? false
            : sInput.matches(sRegExp);
    return bIsValid;
  }

  /**
   * An description of the valid format for a Password by iPwType=0..2, width:
   * 0=Simple Alpha-numeric password
   * 1=Medium Strong
   * 2=Strong Password
   * @param iPwType Integer
   * @return String
   */
  public static String ValidPasswordFormat(Integer iPwType) {
    iPwType = (iPwType == null) ? 0 : ((iPwType > 2) ? 2 : iPwType);
    String sFormat = "The password's first character must be a letter, it "
            + "must contain 6-16 characters and no characters other than"
            + " letters and numbers may be used";
    if (iPwType == 1) {
      sFormat = "Password expression that requires one letter, one "
              + "digit, 6-16 characters, and no spaces";
    } else if (iPwType == 2) {
      sFormat = "Password expression that requires one lower case letter, one "
              + "upper case letter, one digit, 6-16 characters, and no spaces";
    }
    return sFormat;
  }

  /**
   * Generate a Random Password consisting of 10 Characters. Th Password will comply
   * with the format of iPwType
   * @return String
   */
  public static String getRandomPassword(Integer iPwType) {
    String sPW = "";
    int iPWLen = 10;
    String sUCaseStr = "ABCDEFGHIJKLMNPQRSTVWXYZ";
    String sLCaseStr = "abcdefghijkmnopqrstvwxyz";
    String sNumStr = "0123456789";
    String sSrcStr = sLCaseStr;
    int iUCaseLen = sUCaseStr.length();
    int iLCaseLen = sLCaseStr.length();
    int iNumLen = sNumStr.length();

    int iChar1 = -1;
    String sChar1 = "";
    int iChar2 = -1;
    String sChar2 = "";
    if (iPwType == 0) {
      sSrcStr += sNumStr;
    } else if (iPwType == 1) {
      sSrcStr += sNumStr;
      float dFract = (float) Math.random();
      iChar1 = 1 + Math.round(dFract * 9);
      int iChar = Math.round(dFract * iNumLen);
      iChar = (iChar > 0) ? iChar - 1 : iChar;
      sChar1 = sNumStr.substring(iChar, iChar + 1);
    } else if (iPwType == 2) {
      sSrcStr += sNumStr;
      sSrcStr += sUCaseStr;
      float dFract = (float) Math.random();
      iChar2 = 1 + Math.round(dFract * 9);
      iChar2 = (iChar2 != iChar1) ? iChar2 : ((iChar2 == 10) ? iChar2 - 1 : iChar2 + 1);
      int iChar = Math.round(dFract * iUCaseLen);
      iChar = (iChar > 0) ? iChar - 1 : iChar;
      sChar2 = sUCaseStr.substring(iChar, iChar + 1);
    }
    int iSrcLen = sSrcStr.length();

    int iCurLen = 0;
    while (iCurLen < iPWLen) {
      int iLen = 0;
      int iChar = 0;
      String sSrc = "";
      float dFract = (float) Math.random();
      if (iCurLen == 0) {
        iLen = iLCaseLen;
        sSrc = sLCaseStr;
        iChar = Math.round(dFract * iLen);
        sPW += sSrc.substring(iChar, iChar + 1);
      } else if (iCurLen == iChar1) {
        sPW += sChar1;
      } else if (iCurLen == iChar2) {
        sPW += sChar2;
      } else {
        iLen = iSrcLen;
        sSrc = sSrcStr;
        iChar = Math.round(dFract * iLen);
        sPW += sSrc.substring(iChar, iChar + 1);
      }
      iCurLen = sPW.length();
    }
    return sPW;
  }

    /**
   * Encrypts a password using a specified Salt string (Optional) and return a
   * 32-character hexadecimal string.
   * @param sPassword String
   * @param sSalt String
   * @return String
   * @throws Exception
   */
  public static String hashPassword(String sPassword, String sSalt) throws Exception {
    String sHash = null;
    try {
      sPassword = DataEntry.cleanString(sPassword);
      if (sPassword == null) {
        throw new Exception("The Password is empty or undefined");
      }
      byte[] pBytes = null;
      MessageDigest pDigest = MessageDigest.getInstance("MD5");
      pDigest.reset();
      sSalt = DataEntry.cleanString(sSalt);
      if (sSalt == null) {
        pDigest.update(sPassword.getBytes());
        pBytes = pDigest.digest();
      } else {
        pDigest.update(sSalt.getBytes());
        pBytes = pDigest.digest(sPassword.getBytes());
      }
      BigInteger hash = new BigInteger(1, pBytes);
      sHash = hash.toString(16);
      while (sHash.length() < 32) {
        sHash = "0" + sHash;
      }
    } catch (Exception pExp) {
      throw new Exception("DataEntry.hashPassword Error: \n" + pExp.getMessage());
    }
    return sHash;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Unique IDs">
  /**
   * Generate a new random hash Salt value (32 characters)
   * @return
   */
  public static String newHashSalt() {
    Random pRand = new Random();
    byte[] pSalt = new byte[12];
    pRand.nextBytes(pSalt);
    BigInteger hash = new BigInteger(1, pSalt);
    String sSalt = hash.toString(16);
    while (sSalt.length() < 32) {
      sSalt = "0" + sSalt;
    }
    return sSalt;
  }

  /**
   * Generate a new unique record ID starting with today's date and a random hash Salt
   * The return value is a 32-character string.
   * @return String
   */
  public static String newUniqueID() throws Exception { //DataEntryUniqueIdException
    String sBaseStr = null;
    String sSalt = null;
//    DateTime pToday = DateTime.getNow(null);
    java.util.Date pToday = new java.util.Date();
    sBaseStr = pToday.toString();
    sSalt = DataEntry.newHashSalt();
    return DataEntry.newUniqueId(sBaseStr, sSalt);
  }

  /**
   * Generate a new unique record ID starting with sBaseStr and using sSalt. The return
   * value a 32-character string.
   * @param sBaseStr String
   * @param sSalt String
   * @return String
   * @throws Exception
   */
  public static String newUniqueId(String sBaseStr, String sSalt)
          throws Exception { //DataEntryUniqueIdException
    String sHash = null;
    try {
      sBaseStr = DataEntry.cleanString(sBaseStr);
      if (sBaseStr == null) {
        throw new Exception("The UniqueId's Base String cannot be null"); //DataEntryUniqueIdException
      }

      sSalt = DataEntry.cleanString(sSalt);
      byte[] pBytes = null;
      MessageDigest pDigest = MessageDigest.getInstance("MD5");
      pDigest.reset();
      if (sSalt == null) {
        pDigest.update(sBaseStr.getBytes());
        pBytes = pDigest.digest();
      } else {
        pDigest.update(sSalt.getBytes());
        pBytes = pDigest.digest(sBaseStr.getBytes());
      }
      BigInteger hash = new BigInteger(1, pBytes);
      sHash = hash.toString(16);
      while (sHash.length() < 32) {
        sHash = "0" + sHash;
      }
    } catch (Exception pExp) {
      throw new Exception("DataEntry.newUniqueID Error: \n" + //DataEntryUniqueIdException
              pExp.getMessage());
    }
    return sHash;
  }

  /**
   * Generate a new UniqueID with no salt and without throwing an exception. Instead
   * exceptions are logged.
   * @param sBaseStr String
   * @return String
   */
  public static String newUniqueId(String sBaseStr) {
    String sHash = null;
    try {
      sHash = DataEntry.newUniqueId(sBaseStr, null);
    } catch (Exception pExp) {//DataEntryUniqueIdException
      logger.log(Level.WARNING,
              "DataEntry.newUniqueId Error:\n {0}", pExp.getMessage());
    }
    return sHash;
  }
  //</editor-fold>
}
