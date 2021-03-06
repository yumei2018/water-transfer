
--------------------------------------------------------------------------------
--  Modify Table WT_TRANS
--------------------------------------------------------------------------------
-- In Proposal Process Tab
ALTER TABLE WT_TRANS ADD SWPAO_REVIEWER VARCHAR2(64);
ALTER TABLE WT_TRANS ADD REGION_REVIEWER VARCHAR2(64);
ALTER TABLE WT_TRANS ADD USBR_REVIEWER VARCHAR2(64);
ALTER TABLE WT_TRANS ADD SWPAO_CONTRACT_NUM VARCHAR2(8);

-- Base Info Tab
ALTER TABLE WT_TRANS ADD MAJOR_RIVER_ATTRIBUTE VARCHAR2(64);
ALTER TABLE WT_TRANS ADD TRANS_DESCRIPTION VARCHAR2(250);
ALTER TABLE WT_TRANS ADD PRO_AGREE_PAID_RANGE VARCHAR2(32);

-- Reporting Tab
ALTER TABLE WT_TRANS ADD REPORT_COMMENT VARCHAR2(500);
/

--------------------------------------------------------------------------------
--  Modify Table WT_FU_TYPE
--------------------------------------------------------------------------------
Insert into WT_FU_TYPE (WT_FU_TYPE_ID,FU_TYPE,FU_TYPE_DESCRIPTION,FU_SUB_TYPE) values (12,'CVP','Central Valley Project-Jones-SWP/CVP Intertie','SWP/CVP Intertie');

--------------------------------------------------------------------------------
--  Modify Table WT_CONTACT
--------------------------------------------------------------------------------
ALTER TABLE WT_CONTACT ADD IS_BUYERS_CONTACT NUMBER(1,0) DEFAULT 0;
/

--------------------------------------------------------------------------------
--  DDL for Link Table WT_BUYERS_CONTACT
--------------------------------------------------------------------------------
DROP TABLE WT_BUYERS_CONTACT;

CREATE TABLE WT_BUYERS_CONTACT (
  WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,WT_CONTACT_ID REFERENCES WT_CONTACT(WT_CONTACT_ID)
  ,UNIQUE(WT_TRANS_ID,WT_CONTACT_ID)
);
/

--------------------------------------------------------------------------------
--  DDL for Table WT_BUYERS_CONTACT
--------------------------------------------------------------------------------
DROP TRIGGER WT_BUYERS_CONTACT_UPDATETRIG;
DROP TRIGGER WT_BUYERS_CONTACT_PKTRIG;
DROP SEQUENCE WT_BUYERS_CONTACT_PKSEQ;
DROP TABLE WT_BUYERS_CONTACT;

CREATE TABLE WT_BUYERS_CONTACT (
  WT_BUYERS_CONTACT_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,AGENCY_NAME VARCHAR2(128)
  ,TITLE VARCHAR2(32)
  ,LAST_NAME VARCHAR2(32)
  ,MIDDLE_NAME VARCHAR2(32)
  ,FIRST_NAME VARCHAR2(32)  
  ,ADDRESS VARCHAR2(128)  
	,CITY_NAME VARCHAR2(64) 
	,WT_STATE_ID NUMBER(16,0) 
	,ZIPCODE VARCHAR2(10) 
  ,PHONE_TYPE VARCHAR2(16)
	,PHONE_NUMBER VARCHAR2(20) 
  ,EMAIL VARCHAR2(64)
	,CREATED_DATE TIMESTAMP (6) DEFAULT SYSTIMESTAMP
  ,CREATED_BY_ID NUMBER(*,0)
  ,UPDATED_DATE TIMESTAMP (6) DEFAULT SYSTIMESTAMP
  ,UPDATED_BY_ID NUMBER(*,0)
);
/

CREATE SEQUENCE WT_BUYERS_CONTACT_PKSEQ 
  START WITH 1 
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_BUYERS_CONTACT_PKTRIG
  BEFORE INSERT
  ON WT_BUYERS_CONTACT
  FOR EACH ROW
  BEGIN
    IF :NEW.WT_BUYERS_CONTACT_ID IS NULL THEN
      SELECT WT_BUYERS_CONTACT_PKSEQ.NEXTVAL INTO :NEW.WT_BUYERS_CONTACT_ID FROM DUAL;
    END IF;
    SELECT SYSTIMESTAMP INTO :NEW.CREATED_DATE FROM DUAL;
    SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL;
  END WT_BUYERS_CONTACT_PKTRIG;
/

CREATE OR REPLACE TRIGGER WT_BUYERS_CONTACT_UPDATETRIG 
  BEFORE UPDATE
  ON WT_BUYERS_CONTACT
  FOR EACH ROW 
  BEGIN 
    SELECT SYSTIMESTAMP INTO :NEW.UPDATED_DATE FROM DUAL; 
  END WT_BUYERS_CONTACT_UPDATETRIG;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_TRANS_TRACK
--------------------------------------------------------------------------------
DROP TRIGGER WT_TRANS_TRACK_PKTRIG;
DROP SEQUENCE WT_TRANS_TRACK_PKSEQ;
DROP TABLE WT_TRANS_TRACK;

CREATE TABLE WT_TRANS_TRACK (
  WT_TRANS_TRACK_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID NUMBER(16,0)
  ,TRANS_XML CLOB
	,CREATED_DATE TIMESTAMP (6) DEFAULT SYSTIMESTAMP
  ,CREATED_BY VARCHAR2(64)
);
/

CREATE SEQUENCE WT_TRANS_TRACK_PKSEQ 
  START WITH 1 
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_TRANS_TRACK_PKTRIG
  BEFORE INSERT
  ON WT_TRANS_TRACK
  FOR EACH ROW
  BEGIN
    IF :NEW.WT_TRANS_TRACK_ID IS NULL THEN
      SELECT WT_TRANS_TRACK_PKSEQ.NEXTVAL INTO :NEW.WT_TRANS_TRACK_ID FROM DUAL;
    END IF;
    SELECT SYSTIMESTAMP INTO :NEW.CREATED_DATE FROM DUAL;
  END WT_TRANS_TRACK_PKTRIG;
/

--------------------------------------------------------------------------------
--  Modify Table WT_ATTACHMENT
--------------------------------------------------------------------------------
ALTER TABLE WT_ATTACHMENT ADD FILE_SIZE NUMBER(18,0);

--CREATE INDEX attachmenti ON WT_ATTACHMENT(WT_ATTACHMENT_ID);
--ALTER TABLE WT_ATTACHMENT ADD CONSTRAINT SYS_C0013164 PRIMARY KEY (WT_ATTACHMENT_ID) USING INDEX attachmenti;
/

SELECT * FROM WT_ATTACHMENT WHERE FILENAME = 'DWR.pdf';
SELECT * FROM WT_ATTACHMENT WHERE FILENAME = 'drywell_footer.png';
UPDATE WT_ATTACHMENT SET FILE_SIZE = 1984233 WHERE FILENAME = 'DWR.pdf';
UPDATE WT_ATTACHMENT SET FILE_SIZE = 18500 WHERE FILENAME = 'ep.json';
UPDATE WT_ATTACHMENT SET FILE_SIZE = 33000 WHERE FILENAME = 'dwr_ep.json';
UPDATE WT_ATTACHMENT SET FILE_SIZE = 107000 WHERE FILENAME = 'drywell_footer.png';

--------------------------------------------------------------------------------
--  Modify Table WT_TRANS_CEQA
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS_CEQA ADD SCH_NUM VARCHAR2(16);

--------------------------------------------------------------------------------
--  Modify Table WT_TRANS_NEPA
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS_NEPA ADD NEPA_NUM VARCHAR2(8);









