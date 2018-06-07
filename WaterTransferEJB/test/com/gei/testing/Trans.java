/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.testing;

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
    public static final String reqStorageExp= "Request for Storage Prior to Export";
    public static final String deltaTransferInd= "Through Delta Transfer";
    public static final String isStateContractor= "State Water Project Contractor";
    public static final String dwrComments= "Comments/Special Conditions";
    public static final String transTypes= "Facilities Used";
    public static final String envRegComp= "Environmental Regulatory Compliance";
    public static final String transReach= "California Aqueduct Reach";
    
    public static final Map<String, String> transFieldMap;
    static {
      Map<String, String> tmpMap = new HashMap<String, String>();
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
      tmpMap.put("reqStorageExp", reqStorageExp);
      tmpMap.put("deltaTransferInd", deltaTransferInd);
      tmpMap.put("isStateContractor", isStateContractor);
      tmpMap.put("dwrComments", dwrComments);
      tmpMap.put("transTypes", transTypes);
      tmpMap.put("envRegComp", envRegComp);
      tmpMap.put("transReach", transReach);
      transFieldMap = Collections.unmodifiableMap(tmpMap);
    }
    
    private Trans(){
        throw new AssertionError();
    }
}
