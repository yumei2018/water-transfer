--------------------------------------------------------------------------------
--  DROP ALL TABLES AND SEQUENCE
--------------------------------------------------------------------------------
DROP TABLE WT_HEAD;

DROP TRIGGER WT_IDLE_PLAN_PKTRIG;
DROP SEQUENCE WT_IDLE_PLAN_PKSEQ;
DROP TABLE WT_IDLE_PLAN;

--------------------------------------------------------------------------------
--  DDL for Table WT_IDLE_PLAN
--------------------------------------------------------------------------------
CREATE TABLE WT_IDLE_PLAN (
  WT_IDLE_PLAN_ID NUMERIC PRIMARY KEY
  ,PLAN_ACR_IDLE NUMBER(10)
  ,ACT_FALL_ACR NUMBER(10)
  ,RES_OPRA_5YEAR_DATA VARCHAR2(400)
);
/

CREATE SEQUENCE WT_IDLE_PLAN_PKSEQ  
  START WITH 1
  INCREMENT BY 1
  CACHE 99999999;
/

CREATE OR REPLACE TRIGGER WT_IDLE_PLAN_PKTRIG
BEFORE INSERT
ON WT_IDLE_PLAN
FOR EACH ROW
BEGIN
    SELECT WT_IDLE_PLAN_PKSEQ.NEXTVAL
    INTO :NEW.WT_IDLE_PLAN_ID
    FROM DUAL;
END WT_IDLE_PLAN_PKTRIG;
/

INSERT INTO WT_IDLE_PLAN ("WT_IDLE_PLAN_ID","PLAN_ACR_IDLE", "ACT_FALL_ACR", "RES_OPRA_5YEAR_DATA")
         VALUES (WT_IDLE_PLAN_PKSEQ.NEXTVAL,'3','4','This is test data');
COMMIT;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_HEAD
--------------------------------------------------------------------------------
CREATE TABLE WT_HEAD (
  WT_TRANS_ID VARCHAR2(8) PRIMARY KEY
  ,WT_IDLE_PLAN_ID REFERENCES WT_IDLE_PLAN(WT_IDLE_PLAN_ID)
  ,TRANS_YEAR NUMBER(4)
  ,WT_AGENCY_ID NUMBER(10)
  ,WT_BUYER_ID NUMBER(10)
);
/

INSERT INTO WT_HEAD ("WT_TRANS_ID","WT_IDLE_PLAN_ID","TRANS_YEAR", "WT_AGENCY_ID", "WT_BUYER_ID")
         VALUES ('100',WT_IDLE_PLAN_PKSEQ.CURRVAL,'3','12','01');
COMMIT;
/


