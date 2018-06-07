--------------------------------------------------------------------------------
--  DROP ALL TABLES, SEQUENCE AND TRIGGERS
--------------------------------------------------------------------------------
DROP TABLE WT_WELL_ATTACHMENT;
DROP TABLE WT_GW_WELL;

DROP TRIGGER WT_WELL_PKTRIG;
DROP SEQUENCE WT_WELL_PKSEQ;
DROP TABLE WT_WELL;

--------------------------------------------------------------------------------
--  DDL for Table WT_WELL
--------------------------------------------------------------------------------
CREATE TABLE WT_WELL (
  WT_WELL_ID NUMBER(16,0) PRIMARY KEY  
  ,WT_WELL_NUM VARCHAR2(24)
  ,STATE_WELL_NUM VARCHAR2(16)
  ,LOCAL_WELL_ID VARCHAR2(32)
  ,WELL_TRANSFER NUMBER(1,0) DEFAULT 0
  ,WELL_MONITORING NUMBER(1,0) DEFAULT 0  
  ,LAST_CALIBRATE_DATE DATE
  ,METER_MAKE VARCHAR2(32)
  ,METER_MODEL VARCHAR2(32)  
  ,WELL_CAPACITY NUMBER(18,0)
  ,METER_UNITS VARCHAR2(8)
  ,CASGEM_STATION_ID NUMBER(10,0)
);
/

CREATE SEQUENCE WT_WELL_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_WELL_PKTRIG
BEFORE INSERT
ON WT_WELL
FOR EACH ROW
BEGIN
  IF :NEW.WT_WELL_ID IS NULL THEN
    SELECT WT_WELL_PKSEQ.NEXTVAL INTO :NEW.WT_WELL_ID FROM DUAL;
  END IF;
END WT_WELL;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_GW_WELL
--------------------------------------------------------------------------------
CREATE TABLE WT_GW_WELL (
  WT_GROUNDWATER_ID REFERENCES WT_GROUNDWATER(WT_GROUNDWATER_ID)
  ,WT_WELL_ID REFERENCES WT_WELL(WT_WELL_ID)
  ,UNIQUE(WT_GROUNDWATER_ID,WT_WELL_ID)
);
/

--------------------------------------------------------------------------------
--  DDL for Table WT_WELL_ATTACHMENT
--------------------------------------------------------------------------------
CREATE TABLE WT_WELL_ATTACHMENT (
  WT_WELL_ID REFERENCES WT_WELL(WT_WELL_ID)
  ,WT_ATTACHMENT_ID REFERENCES WT_ATTACHMENT(WT_ATTACHMENT_ID) ON DELETE CASCADE
  ,UNIQUE(WT_WELL_ID,WT_ATTACHMENT_ID)
);
/


--------------------------------------------------------------------------------
--  Modify Table WT_REVIEW_COMMENT
--------------------------------------------------------------------------------
ALTER TABLE WT_REVIEW_COMMENT DROP COLUMN WT_WELL_NUM;
ALTER TABLE WT_REVIEW_COMMENT ADD WT_WELL_ID NUMBER(16,0);
/











