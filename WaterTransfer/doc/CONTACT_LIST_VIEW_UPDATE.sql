
  CREATE OR REPLACE FORCE VIEW "CONTACT_LIST_VIEW"  AS 
  SELECT 
  DISTINCT t1.*
  , CASE WHEN t3.USER_ID IS NULL THEN 0 ELSE 1 END AS "HAS_ACCOUNT"
  , t2.AGENCY_TYPE 
  , t5.SHORT_NAME
  , CASE WHEN t4.USER_ID IS NULL THEN 0 ELSE 1 END AS "IS_LINK_TRANS"
from WT_CONTACT t1
  JOIN WT_AGENCY t2
  ON t1.WT_AGENCY_ID = t2.WT_AGENCY_ID
  LEFT JOIN APP_USER t3
    LEFT JOIN WT_TRANS_USER t4
    ON t3.USER_ID = t4.USER_ID
  ON t1.WT_CONTACT_ID = t3.WT_CONTACT_ID 
  LEFT JOIN WT_STATE t5
  ON t1.WT_STATE_ID = t5.WT_STATE_ID;
 
