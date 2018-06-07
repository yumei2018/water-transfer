/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ymei
 */
public final class Trans{
    public static final String transYear = "Transfer Year";
    public static final String isShortLong = "One Year or Long Term";
    public static final String proTransQua = "Proposed Transfer Quantity";  
    public static final String proUnitCost = "Proposed Cost of Water";
    public static final String proAgreePaidRange = "Proposed Agreement Amount to be Paid to Seller";
    public static final String surWaterSource = "Surface Water Source";    
    public static final String transWinStart = "Proposed Transfer Start Date";
    public static final String transWinEnd = "Proposed Transfer End Date";
    public static final String reqExpFrom = "Requested Export Window From";
    public static final String reqExpTo= "Requested Export Window To";
    public static final String reqExpFromDate = "Requested Export Window From Date";
    public static final String reqExpToDate= "Requested Export Window To Date";
    public static final String reqStorageExp= "Request for Storage Prior to Export";
    public static final String deltaTransferInd= "Through Delta Transfer";
    public static final String isStateContractor= "State Water Project Contractor";
    public static final String dwrComments= "Comments/Special Conditions";
    public static final String transTypes= "Facilities Used";
    public static final String envRegComp= "Environmental Regulatory Compliance";
    public static final String transReach= "California Aqueduct Reach";
    
    public static final Map<String, String> transFieldMap;
    static {
      Map<String, String> tmpMap = new HashMap<>();
      tmpMap.put("transYear", transYear);
      tmpMap.put("isShortLong", isShortLong);
      tmpMap.put("proTransQua", proTransQua);
      tmpMap.put("proUnitCost", proUnitCost);
      tmpMap.put("proAgreePaidRange", proAgreePaidRange);
      tmpMap.put("surWaterSource", surWaterSource);
      tmpMap.put("transWinStart", transWinStart);
      tmpMap.put("transWinEnd", transWinEnd);
      tmpMap.put("reqExpFrom", reqExpFrom);
      tmpMap.put("reqExpTo", reqExpTo);
      tmpMap.put("reqExpFromDate", reqExpFromDate);
      tmpMap.put("reqExpToDate", reqExpToDate);
      tmpMap.put("reqStorageExp", reqStorageExp);
      tmpMap.put("deltaTransferInd", deltaTransferInd);
      tmpMap.put("isStateContractor", isStateContractor);
      tmpMap.put("dwrComments", dwrComments);
      tmpMap.put("transTypes", transTypes);
      tmpMap.put("envRegComp", envRegComp);
      tmpMap.put("transReach", transReach);
      transFieldMap = Collections.unmodifiableMap(tmpMap);
    }
    
    public static final Map<String, String> transTypeMap;
    static {
      Map<String, String> tmpMap = new HashMap<>();
      tmpMap.put("000", "");
      tmpMap.put("001", "RES");
      tmpMap.put("010", "GW");
      tmpMap.put("100", "CI");
      tmpMap.put("011", "GW,RES");
      tmpMap.put("101", "CI,RES");
      tmpMap.put("110", "CI,GW");
      tmpMap.put("111", "CI,GW,RES");
      transTypeMap = Collections.unmodifiableMap(tmpMap);
    }
    
    public static final Map<String, String> agencyApproval;
    static {
      Map<String, String> tmpMap = new HashMap<>();
      tmpMap.put("00000", "");
      tmpMap.put("00001", "DWR");
      tmpMap.put("00010", "SWRCB");
      tmpMap.put("00100", "USBR");
      tmpMap.put("01000", "Fishery Agencies");
      tmpMap.put("10000", "Other");
      tmpMap.put("00011", "Fishery Agencies/Other");
      tmpMap.put("00101", "USBR/Other");
      tmpMap.put("01001", "SWRCB/Other");
      tmpMap.put("10001", "DWR/Other");
      tmpMap.put("00110", "USBR/Fishery Agencies");
      tmpMap.put("01100", "SWRCB/USBR");
      tmpMap.put("11000", "DWR/SWRCB");
      tmpMap.put("00111", "USBR/Fishery Agencies/Other");
      tmpMap.put("10011", "DWR/Fishery Agencies/Other");
      tmpMap.put("11001", "DWR/SWRCB/Other");
      tmpMap.put("11100", "DWR/SWRCB/USBR");
      tmpMap.put("01110", "SWRCB/USBR/Fishery Agencies");
      tmpMap.put("10110", "DWR/USBR/Fishery Agencies");
      tmpMap.put("11010", "DWR/SWRCB/Fishery Agencies");
      tmpMap.put("01111", "SWRCB/USBR/Fishery Agencies/Other");
      tmpMap.put("10111", "DWR/USBR/Fishery Agencies/Other");
      tmpMap.put("11011", "DWR/SWRCB/Fishery Agencies/Other");
      tmpMap.put("11101", "DWR/SWRCB/USBR/Other");
      tmpMap.put("11110", "DWR/SWRCB/USBR/Fishery Agencies");
      tmpMap.put("11111", "DWR/SWRCB/USBR/Fishery Agencies/Other");
      agencyApproval = Collections.unmodifiableMap(tmpMap);
    }
    
    private Trans(){
        throw new AssertionError();
    }
}
