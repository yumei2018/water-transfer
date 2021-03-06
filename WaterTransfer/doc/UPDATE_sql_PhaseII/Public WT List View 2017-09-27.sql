CREATE OR REPLACE VIEW WT_PUBLIC_LIST_VIEW AS
SELECT
  t1.WT_TRANS_ID
  , t1.TRANS_YEAR 
  , t1.SWPAO_CONTRACT_NUM
  , t1.IS_SHORT_LONG 
  , t1.PRO_ACR_IDLE_IND||t1.WELL_USE_NUM_IND||t1.RES_RE_OP_IND as TRANS_TYPE
  , t10.WATER_RIGHTS
  , t4.SELLER
  , t5.BUYERS   
  , t6.COUNTIES    
  , t1.WT_STATUS_FLAG_ID
  , t2.STATUS_DESCRIPTION AS "STATUS"  
  , t1.IS_ACTIVE
  , t7.CI_CONTRACT_AMOUNT
  , t7.CI_ACTUAL_AMOUNT
  , t7.CI_DELIVERED_AMOUNT
  , t8.GW_CONTRACT_AMOUNT
  , t8.GW_ACTUAL_AMOUNT
  , t8.GW_DELIVERED_AMOUNT
  , t9.RES_CONTRACT_AMOUNT
  , t9.RES_ACTUAL_AMOUNT
  , t9.RES_DELIVERED_AMOUNT
FROM WT_TRANS t1
  JOIN WT_STATUS_FLAG t2
  ON t1.WT_STATUS_FLAG_ID = t2.WT_STATUS_FLAG_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID ,t3.AGENCY_FULL_NAME,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB)) AS SELLER
      FROM WT_TRANS t1
        JOIN WT_SELLER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID,t3.AGENCY_FULL_NAME) t4
  ON t1.WT_TRANS_ID = t4.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID  ,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB)) AS BUYERS
      FROM WT_TRANS t1
        JOIN WT_BUYER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t5
  ON t1.WT_TRANS_ID = t5.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID  ,
        TAB_TO_STRING(CAST(COLLECT(t3.NAME) AS T_VARCHAR2_TAB)) AS COUNTIES
      FROM WT_TRANS t1
        JOIN WT_TRANS_COUNTY t2
          JOIN WT_COUNTY t3
          ON t2.WT_COUNTY_ID = t3.WT_COUNTY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t6
  ON t1.WT_TRANS_ID = t6.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID,
                    t2.CONTRACT_AMOUNT AS CI_CONTRACT_AMOUNT, 
                    t2.ACTUAL_AMOUNT AS CI_ACTUAL_AMOUNT, 
                    t2.DELIVERED_AMOUNT AS CI_DELIVERED_AMOUNT
      FROM WT_TRANS t1
        JOIN WT_CROP_IDLING t2
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID) t7
  ON t1.WT_TRANS_ID = t7.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID,
                    t2.CONTRACT_AMOUNT AS GW_CONTRACT_AMOUNT, 
                    t2.ACTUAL_AMOUNT AS GW_ACTUAL_AMOUNT, 
                    t2.DELIVERED_AMOUNT AS GW_DELIVERED_AMOUNT
      FROM WT_TRANS t1
        JOIN WT_GROUNDWATER t2
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID) t8
  ON t1.WT_TRANS_ID = t8.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID,
                    t2.CONTRACT_AMOUNT AS RES_CONTRACT_AMOUNT, 
                    t2.ACTUAL_AMOUNT AS RES_ACTUAL_AMOUNT, 
                    t2.DELIVERED_AMOUNT AS RES_DELIVERED_AMOUNT
      FROM WT_TRANS t1
        JOIN WT_RESERVOIR t2
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID) t9
  ON t1.WT_TRANS_ID = t9.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID  ,
        TAB_TO_STRING(CAST(COLLECT(t2.WATER_RIGHTS_TYPE) AS T_VARCHAR2_TAB)) AS WATER_RIGHTS
      FROM WT_TRANS t1
        JOIN WT_WATER_RIGHTS t2
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t10
  ON t1.WT_TRANS_ID = t10.WT_TRANS_ID
  WHERE t1.WT_STATUS_FLAG_ID > 1 AND t1.IS_ACTIVE=1;
--  WHERE t1.WT_STATUS_FLAG_ID > 4;
  
  
  
  
  
  
  
  
  
  