/**
create or replace TYPE        "T_VARCHAR2_TAB"                                          AS TABLE OF VARCHAR2(4000);

create or replace FUNCTION        "TAB_TO_STRING" (p_varchar2_tab  IN  t_varchar2_tab,
                                          p_delimiter     IN  VARCHAR2 DEFAULT ',') RETURN VARCHAR2 IS
  l_string     VARCHAR2(32767);
BEGIN
  FOR i IN p_varchar2_tab.FIRST .. p_varchar2_tab.LAST LOOP
    IF i != p_varchar2_tab.FIRST THEN
      l_string := l_string || p_delimiter;
    END IF;
    l_string := l_string || p_varchar2_tab(i);
  END LOOP;
  RETURN l_string;
END tab_to_string;
/**/

CREATE OR REPLACE VIEW WT_LIST_VIEW AS
SELECT
  t1.WT_TRANS_ID
  , t1.TRANS_YEAR 
  , t2.STATUS_NAME AS "STATUS"
  , t1.CREATE_DATE
  , t4.SELLER
  , t5.BUYERS
  , t1.WT_STATUS_FLAG_ID
  , t1.CREATED_BY_ID
from WT_TRANS t1
  join WT_STATUS_FLAG t2
  on t1.WT_STATUS_FLAG_ID = t2.WT_STATUS_FLAG_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID ,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB)) AS SELLER
      FROM WT_TRANS t1
        JOIN WT_SELLER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t4
  ON t1.WT_TRANS_ID = t4.WT_TRANS_ID
  LEFT JOIN (SELECT t1.WT_TRANS_ID ,
        TAB_TO_STRING(CAST(COLLECT(t3.AGENCY_CODE) AS T_VARCHAR2_TAB)) AS BUYERS
      FROM WT_TRANS t1
        JOIN WT_BUYER t2
          JOIN WT_AGENCY t3
          ON t2.WT_AGENCY_ID = t3.WT_AGENCY_ID
        ON t1.WT_TRANS_ID = t2.WT_TRANS_ID  
      GROUP BY t1.WT_TRANS_ID) t5
  ON t1.WT_TRANS_ID = t5.WT_TRANS_ID;
  /
  
  CREATE OR REPLACE VIEW CONTACT_LIST_VIEW AS
SELECT 
  DISTINCT t1.* 
  , CASE WHEN t3.USER_ID IS NULL THEN 0 ELSE 1 END AS "HAS_ACCOUNT"
  , t2.AGENCY_TYPE 
  , CASE WHEN t4.USER_ID IS NULL THEN 0 ELSE 1 END AS "IS_LINK_TRANS"
from WT_CONTACT t1
  JOIN WT_AGENCY t2
  ON t1.WT_AGENCY_ID = t2.WT_AGENCY_ID
  LEFT JOIN APP_USER t3
    LEFT JOIN WT_TRANS_USER t4
    ON t3.USER_ID = t4.USER_ID
  ON t1.WT_CONTACT_ID = t3.WT_CONTACT_ID;  
/