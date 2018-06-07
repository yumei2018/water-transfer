CREATE OR REPLACE VIEW WT_PUBLIC_STATUS_VIEW AS
SELECT
  t1.WT_TRANS_ID
  , t1.TRANS_YEAR 
  , t4.SELLER
  , t5.BUYERS   
  , t3.MINDATE AS RECEIVED_DATE
  , t1.PRO_ACR_IDLE_IND AS CROPIDLING
  , t1.RES_RE_OP_IND AS RESERVOIR
  , t1.WELL_USE_NUM_IND AS GROUNDWATER
  , t6.WATER_TRANS_QUA AS CI_WATER_AMOUNT
  , t7.NET_TRANS_WATER AS GW_WATER_AMOUNT
  , t8.WATER_TRANS_QUA AS RV_WATER_AMOUNT
  , t1.PRO_TRANS_QUA AS TOTAL_WATER_AMOUNT
  , t9.AGENCY_APPROVAL
  , t1.CI_CONTRACT_AMOUNT
  , t1.GW_CONTRACT_AMOUNT
  , t1.RV_CONTRACT_AMOUNT
  , t1.CONTRACT_AMOUNT
  , t2.STATUS_DESCRIPTION AS STATUS 
  , t1.IS_ACTIVE
FROM WT_TRANS t1
  JOIN WT_STATUS_FLAG t2
  ON t1.WT_STATUS_FLAG_ID = t2.WT_STATUS_FLAG_ID
  AND t1.WT_STATUS_FLAG_ID>0 AND t1.IS_ACTIVE=1
  LEFT JOIN (SELECT WT_TRANS_ID, min(STATUS_DATE) AS MINDATE 
              FROM WT_STATUS_TRACK WHERE STATUS_NAME IN('SUBMITTED') GROUP BY WT_TRANS_ID)t3
  ON t1.WT_TRANS_ID = t3.WT_TRANS_ID 
  LEFT JOIN (SELECT t1.WT_TRANS_ID,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB)) AS SELLER
      FROM WT_TRANS t1
        JOIN WT_SELLER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t4
  ON t1.WT_TRANS_ID = t4.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB),'/') AS BUYERS
      FROM WT_TRANS t1
        JOIN WT_BUYER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t5
  ON t1.WT_TRANS_ID = t5.WT_TRANS_ID
  LEFT JOIN WT_CROP_IDLING t6 
  ON t1.WT_TRANS_ID = t6.WT_TRANS_ID AND t1.PRO_ACR_IDLE_IND = 1
  LEFT JOIN WT_GROUNDWATER t7
  ON t1.WT_TRANS_ID = t7.WT_TRANS_ID AND t1.WELL_USE_NUM_IND = 1
  LEFT JOIN WT_RESERVOIR t8
  ON t1.WT_TRANS_ID = t8.WT_TRANS_ID AND t1.RES_RE_OP_IND = 1
  LEFT JOIN (SELECT t1.WT_TRANS_ID  ,
        t2.DWR||t2.SWRCB||t2.USBR||t2.FISHERY||t2.OTHER as AGENCY_APPROVAL
      FROM WT_TRANS t1
        JOIN WT_AGENCY_APPROVAL t2
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID) t9
  ON t1.WT_TRANS_ID = t9.WT_TRANS_ID
  WHERE t1.TRANS_YEAR = extract(year from sysdate);































