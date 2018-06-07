--------------------------------------------------------------------------------
--  Add Columns GROSS_TRANS_PUMPING to Table WT_GW_MONTHLY and WT_GROUNDWATER
--------------------------------------------------------------------------------
ALTER TABLE WT_GW_MONTHLY ADD GROSS_TRANS_PUMPING NUMBER(18,0);
ALTER TABLE WT_GROUNDWATER ADD GROSS_TRANS_PUMPING NUMBER(18,0);

UPDATE WT_GW_MONTHLY SET GROSS_TRANS_PUMPING=CROSS_TRANS_PUMPING;
UPDATE WT_GROUNDWATER SET GROSS_TRANS_PUMPING=CROSS_TRANS_PUMPING;
/

--------------------------------------------------------------------------------
--  Delete Columns CROSS_TRANS_PUMPING to Table WT_GW_MONTHLY and WT_GROUNDWATER
--------------------------------------------------------------------------------
UPDATE WT_GW_MONTHLY SET CROSS_TRANS_PUMPING=NULL;
UPDATE WT_GROUNDWATER SET CROSS_TRANS_PUMPING=NULL;

ALTER TABLE WT_GW_MONTHLY DROP COLUMN CROSS_TRANS_PUMPING;
ALTER TABLE WT_GROUNDWATER DROP COLUMN CROSS_TRANS_PUMPING;
/

--------------------------------------------------------------------------------
--  Add Columns to table WT_WELL
--------------------------------------------------------------------------------
ALTER TABLE WT_WELL ADD WELL_MAKE VARCHAR2(32);
ALTER TABLE WT_WELL ADD WELL_MODEL VARCHAR2(32);
ALTER TABLE WT_WELL ADD LAST_CALIBRATE_DATE DATE;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_CHECKLIST
--------------------------------------------------------------------------------
CREATE TABLE WT_CHECKLIST(
  WT_CHECKLIST_ID NUMERIC(16,0) PRIMARY KEY
  ,CHECKLIST_FIELD VARCHAR(32)
  ,NAME VARCHAR(256) NOT NULL
  ,CHECKLIST_TYPE VARCHAR(8) DEFAULT 'RADIO' CHECK ("CHECKLIST_TYPE" IN ('RADIO','CHECKBOX'))
  ,SORT_ORDER NUMBER(3,0)
);
/
CREATE SEQUENCE WT_CHECKLIST_PKSEQ START WITH 1;
/
CREATE OR REPLACE TRIGGER WT_CHECKLIST_PKTRIG
BEFORE INSERT
ON WT_CHECKLIST
FOR EACH ROW
BEGIN
  IF :NEW.WT_CHECKLIST_ID IS NULL THEN
    SELECT WT_CHECKLIST_PKSEQ.NEXTVAL INTO :NEW.WT_CHECKLIST_ID FROM DUAL;
  END IF;
END WT_CHECKLIST;
/
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Well Completion Report', 'CHECKBOX', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Well Construction', 'CHECKBOX', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Geologic Log', 'CHECKBOX', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Photo of site', 'CHECKBOX', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Photo of meter', 'CHECKBOX', 5);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Certification of meter', 'CHECKBOX', 6);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Baseline Information', 'CHECKBOX', 7);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('WELL_ATTACHMENT', 'Historic Operations', 'CHECKBOX', 8);

--------------------------------------------------------------------------------
--  DDL for Table WT_ATT_CHECKLIST
--------------------------------------------------------------------------------
CREATE TABLE WT_ATT_CHECKLIST (
  WT_ATTACHMENT_ID REFERENCES WT_ATTACHMENT(WT_ATTACHMENT_ID)
  ,WT_CHECKLIST_ID REFERENCES WT_CHECKLIST(WT_CHECKLIST_ID)
  ,UNIQUE(WT_ATTACHMENT_ID,WT_CHECKLIST_ID)
);
/

