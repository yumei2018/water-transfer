--------------------------------------------------------------------------------
--  DROP ALL TABLES, SEQUENCE AND TRIGGERS
--------------------------------------------------------------------------------
DROP TABLE WT_WELL_ATTACHMENT;
DROP TABLE WT_GW_WELL;

DROP TRIGGER WT_GW_MONTHLY_PKTRIG;
DROP SEQUENCE WT_GW_MONTHLY_PKSEQ;
DROP TABLE WT_GW_MONTHLY;

--DROP TRIGGER WT_GW_WELL_PKTRIG;
DROP SEQUENCE WT_WELL_PKSEQ;
DROP TABLE WT_WELL;
--------------------------------------------------------------------------------
--  DDL for Table WT_GW_MONTHLY
--------------------------------------------------------------------------------
CREATE TABLE WT_GW_MONTHLY (
  WT_GW_MONTHLY_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_GROUNDWATER_ID REFERENCES WT_GROUNDWATER(WT_GROUNDWATER_ID)
  ,GW_MONTH NUMBER(4,0)
  ,PROPOSED_PUMPING NUMBER(18,0)
  ,BASELINE_PUMPING NUMBER(18,0)
  ,CROSS_TRANS_PUMPING NUMBER(18,0)
  ,STREAM_DEPLETION NUMBER(18,0)
  ,NET_TRANS_WATER NUMBER(18,0)
);
/

CREATE SEQUENCE WT_GW_MONTHLY_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_GW_MONTHLY_PKTRIG
BEFORE INSERT
ON WT_GW_MONTHLY
FOR EACH ROW
BEGIN
  IF :NEW.WT_GW_MONTHLY_ID IS NULL THEN
    SELECT WT_GW_MONTHLY_PKSEQ.NEXTVAL INTO :NEW.WT_GW_MONTHLY_ID FROM DUAL;
  END IF;
END WT_GW_MONTHLY;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_WELL
--------------------------------------------------------------------------------
CREATE TABLE WT_WELL (
  WT_WELL_NUM VARCHAR2(24)PRIMARY KEY
  ,STATE_WELL_NUM VARCHAR2(16)
  ,GW_CAPACITY NUMBER(18,0)
  ,WELL_USE VARCHAR2(32)
);
/

CREATE SEQUENCE WT_GW_WELL_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

--CREATE OR REPLACE TRIGGER WT_GW_WELL_PKTRIG
--BEFORE INSERT
--ON WT_GW_WELL
--FOR EACH ROW
--BEGIN
--  IF :NEW.WT_GW_WELL_ID IS NULL THEN
--    SELECT WT_GW_WELL_PKSEQ.NEXTVAL INTO :NEW.WT_GW_WELL_ID FROM DUAL;
--  END IF;
--END WT_GW_WELL;
--/

--------------------------------------------------------------------------------
--  DDL for Table WT_GW_WELL
--------------------------------------------------------------------------------
CREATE TABLE WT_GW_WELL (
  WT_GROUNDWATER_ID REFERENCES WT_GROUNDWATER(WT_GROUNDWATER_ID)
  ,WT_WELL_NUM REFERENCES WT_WELL(WT_WELL_NUM)
  ,UNIQUE(WT_GROUNDWATER_ID,WT_WELL_NUM)
);
/

--------------------------------------------------------------------------------
--  DDL for Table WT_WELL_ATTACHMENT
--------------------------------------------------------------------------------
CREATE TABLE WT_WELL_ATTACHMENT (
  WT_WELL_NUM REFERENCES WT_WELL(WT_WELL_NUM)
  ,WT_ATTACHMENT_ID REFERENCES WT_ATTACHMENT(WT_ATTACHMENT_ID) ON DELETE CASCADE
  ,UNIQUE(WT_WELL_NUM,WT_ATTACHMENT_ID)
);
/

--------------------------------------------------------------------------------
--  Add Columns to Table WT_GROUNDWATER
--------------------------------------------------------------------------------
ALTER TABLE WT_GROUNDWATER ADD PUMPING_WELLS_NUMBER NUMBER(8,0);
ALTER TABLE WT_GROUNDWATER ADD MONITORING_WELLS_NUMBER NUMBER(8,0);
ALTER TABLE WT_GROUNDWATER ADD DEPLETION_FACTOR NUMBER(4,0);
/




