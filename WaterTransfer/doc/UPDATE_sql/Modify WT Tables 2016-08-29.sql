--------------------------------------------------------------------------------
--  Modify Table WT_CHANGE_LOG
--------------------------------------------------------------------------------
ALTER TABLE WT_CHANGE_LOG ADD CHANGE_FIELD VARCHAR2(1000);
/
--------------------------------------------------------------------------------
--  Modify Table WT_WELL
--------------------------------------------------------------------------------
ALTER TABLE WT_WELL ADD METER_LAST_INSTALL DATE;
ALTER TABLE WT_WELL ADD POWER_SOURCE VARCHAR2(64);
ALTER TABLE WT_WELL ADD ATTRIBUTE_YEAR NUMBER(8,0);
/

--------------------------------------------------------------------------------
--  Modify Table WT_TRANS
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS ADD HAS_PRE_TRANS NUMBER(1,0) DEFAULT 0;
ALTER TABLE WT_TRANS ADD IS_ACTIVE NUMBER(1,0) DEFAULT 1;
/

--------------------------------------------------------------------------------
--  Add Columns to table WT_CROP_IDLING
--------------------------------------------------------------------------------
ALTER TABLE WT_CROP_IDLING ADD CURRENT_FS_AGENCY NUMBER(18,2);
/

--------------------------------------------------------------------------------
--  Add Columns to table WT_RESERVOIR
--------------------------------------------------------------------------------
ALTER TABLE WT_RESERVOIR ADD LOCATION_LAT NUMBER;
ALTER TABLE WT_RESERVOIR ADD LOCATION_LONG NUMBER;
ALTER TABLE WT_RESERVOIR ADD IS_SELLER_AUTH NUMBER(1,0) DEFAULT 0;
ALTER TABLE WT_RESERVOIR ADD AUTH_OPERATOR VARCHAR2(64);
/

--------------------------------------------------------------------------------
--  DROP ALL TABLES AND SEQUENCE
--------------------------------------------------------------------------------
DROP TRIGGER WT_PRE_TRANSFER_PKTRIG;
DROP TRIGGER WT_PRE_TRANSFER_UPDATETRIG;
DROP SEQUENCE WT_PRE_TRANSFER_PKSEQ;
DROP TABLE WT_PRE_TRANSFER;

DROP TRIGGER WT_RV_TARSTOR_PKTRIG;
DROP TRIGGER WT_RV_TARSTOR_UPDATETRIG;
DROP SEQUENCE WT_RV_TARSTOR_PKSEQ;
DROP TABLE WT_RV_TARSTOR;

--------------------------------------------------------------------------------
--  DDL for Table WT_PRE_TRANSFER
--------------------------------------------------------------------------------
CREATE TABLE WT_PRE_TRANSFER(
  WT_PRE_TRANSFER_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,SWPAO_CONTRACT_NUM VARCHAR2(8)
  ,RECOM_NUM VARCHAR2(16)
  ,IS_TYPE_CI NUMBER(1,0) DEFAULT 0
  ,IS_TYPE_RV NUMBER(1,0) DEFAULT 0
  ,IS_TYPE_GW NUMBER(1,0) DEFAULT 0
  ,CREATED_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP
  ,CREATED_BY_ID NUMBER(*,0) 	
	,UPDATED_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP 
  ,UPDATED_BY_ID NUMBER(*,0)
);
/ 
CREATE SEQUENCE WT_PRE_TRANSFER_PKSEQ START WITH 1;
/
CREATE OR REPLACE TRIGGER WT_PRE_TRANSFER_PKTRIG
BEFORE INSERT
ON WT_PRE_TRANSFER
FOR EACH ROW
BEGIN
  IF :NEW.WT_PRE_TRANSFER_ID IS NULL THEN
    SELECT WT_PRE_TRANSFER_PKSEQ.NEXTVAL INTO :NEW.WT_PRE_TRANSFER_ID FROM DUAL;
  END IF;
  SELECT SYSTIMESTAMP INTO :NEW.CREATED_DATE FROM DUAL;
  SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL;
END WT_PRE_TRANSFER;
/
CREATE OR REPLACE TRIGGER WT_PRE_TRANSFER_UPDATETRIG 
BEFORE UPDATE
ON WT_PRE_TRANSFER
FOR EACH ROW 
BEGIN 
  SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL; 
END WT_PRE_TRANSFER_UPDATETRIG;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_RV_TARSTOR
--------------------------------------------------------------------------------
CREATE TABLE WT_RV_TARSTOR(
  WT_RV_TARSTOR_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_RESERVOIR_ID REFERENCES WT_RESERVOIR(WT_RESERVOIR_ID)
  ,DAM_NAME VARCHAR2(64)
  ,LOCATION_LAT NUMBER
  ,LOCATION_LONG NUMBER
  ,OPERATOR VARCHAR2(64)
  ,PHONE_NUMBER VARCHAR2(20)
  ,EMAIL VARCHAR2(64)
  ,CREATED_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP
  ,CREATED_BY_ID NUMBER(*,0) 	
	,UPDATED_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP 
  ,UPDATED_BY_ID NUMBER(*,0)
);
/ 
CREATE SEQUENCE WT_RV_TARSTOR_PKSEQ START WITH 1;
/
CREATE OR REPLACE TRIGGER WT_RV_TARSTOR_PKTRIG
BEFORE INSERT
ON WT_RV_TARSTOR
FOR EACH ROW
BEGIN
  IF :NEW.WT_RV_TARSTOR_ID IS NULL THEN
    SELECT WT_RV_TARSTOR_PKSEQ.NEXTVAL INTO :NEW.WT_RV_TARSTOR_ID FROM DUAL;
  END IF;
  SELECT SYSTIMESTAMP INTO :NEW.CREATED_DATE FROM DUAL;
  SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL;
END WT_RV_TARSTOR;
/
CREATE OR REPLACE TRIGGER WT_RV_TARSTOR_UPDATETRIG 
BEFORE UPDATE
ON WT_RV_TARSTOR
FOR EACH ROW 
BEGIN 
  SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL; 
END WT_RV_TARSTOR_UPDATETRIG;
/

--------------------------------------------------------------------------------
--  DDL for Link Table WT_TRANS_COUNTY
--------------------------------------------------------------------------------
CREATE TABLE WT_TRANS_COUNTY (
  WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,WT_COUNTY_ID REFERENCES WT_COUNTY(WT_COUNTY_ID)
  ,UNIQUE(WT_TRANS_ID,WT_COUNTY_ID)
);
/

--------------------------------------------------------------------------------
--  DDL for Table WT_PURPOSE
--------------------------------------------------------------------------------
CREATE TABLE WT_PURPOSE(
  WT_PURPOSE_ID NUMERIC(16,0) PRIMARY KEY
  ,PURPOSE VARCHAR2(64) 
);
/
CREATE SEQUENCE WT_PURPOSE_PKSEQ START WITH 1;
/
CREATE OR REPLACE TRIGGER WT_PURPOSE_PKTRIG
BEFORE INSERT
ON WT_PURPOSE
FOR EACH ROW
BEGIN
  IF :NEW.WT_PURPOSE_ID IS NULL THEN
    SELECT WT_PURPOSE_PKSEQ.NEXTVAL INTO :NEW.WT_PURPOSE_ID FROM DUAL;
  END IF;
END WT_PURPOSE;
/

INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Domestic');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Dust Control');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Fire Protection');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Fish and Wildlife Protection and/or Enhancement');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Fish Culture');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Flood');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Frost Protection');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Heat Protection');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Incidental Power');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Industrial');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Irrigation');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Milling');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Mining');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Municipal');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Power Generation');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Recreational');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Snow Making');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Stockwatering');
INSERT INTO WT_PURPOSE(PURPOSE) VALUES ('Other');
/

--------------------------------------------------------------------------------
--  DDL for Link Table WT_RV_PURPOSE
--------------------------------------------------------------------------------
CREATE TABLE WT_RV_PURPOSE (
  WT_RESERVOIR_ID REFERENCES WT_RESERVOIR(WT_RESERVOIR_ID)
  ,WT_PURPOSE_ID REFERENCES WT_PURPOSE(WT_PURPOSE_ID)
  ,UNIQUE(WT_RESERVOIR_ID,WT_PURPOSE_ID)
);
/

--------------------------------------------------------------------------------
--  DDL for Link Table WT_CI_MAP_ATTACHMENT
--------------------------------------------------------------------------------
CREATE TABLE WT_CI_MAP_ATTACHMENT (
  WT_CROP_IDLING_ID REFERENCES WT_CROP_IDLING(WT_CROP_IDLING_ID) 
	,WT_ATTACHMENT_ID REFERENCES WT_ATTACHMENT(WT_ATTACHMENT_ID) 
  ,UNIQUE(WT_CROP_IDLING_ID, WT_ATTACHMENT_ID)
);
/
















