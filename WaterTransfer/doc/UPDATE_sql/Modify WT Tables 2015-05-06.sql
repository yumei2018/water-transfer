--------------------------------------------------------------------------------
--  Add Columns to Table WT_TRANS
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS ADD PRO_RECEIVED_DATE DATE;
--ALTER TABLE WT_TRANS ADD APPROVED_DATE DATE;
ALTER TABLE WT_TRANS ADD SWRCB_APP VARCHAR2(64);
--ALTER TABLE WT_TRANS DROP COLUMN SWRCB_APP_RPOCESS;
ALTER TABLE WT_TRANS ADD SWRCB_APP_PROCESS VARCHAR2(64);
ALTER TABLE WT_TRANS ADD WATER_AVL_TIMING VARCHAR2(64);
ALTER TABLE WT_TRANS ADD DELTA_TRANSFER_IND NUMBER(1,0) DEFAULT 0;
ALTER TABLE WT_TRANS ADD DELTA_TRANSFER_COMM VARCHAR2(128);
--ALTER TABLE WT_TRANS ADD REQ_EXP_WIN VARCHAR2(64);
ALTER TABLE WT_TRANS ADD FISH_AGENCY_REVIEW VARCHAR2(128);
ALTER TABLE WT_TRANS ADD POTENTIAL_AFFECT VARCHAR2(250);
/

--------------------------------------------------------------------------------
--  Add Columns to Table WT_ATTACHMENT
--------------------------------------------------------------------------------
ALTER TABLE WT_ATTACHMENT ADD CEQA_SUBMITTED_DATE DATE;
/


--------------------------------------------------------------------------------
--  Add Columns to Table WT_FU_TYPE
--------------------------------------------------------------------------------
ALTER TABLE WT_FU_TYPE ADD FU_SUB_TYPE VARCHAR2(64);

Insert into WT_FU_TYPE (WT_FU_TYPE_ID,FU_TYPE,FU_TYPE_DESCRIPTION,FU_SUB_TYPE) values (WT_FU_TYPE_PKSEQ.NEXTVAL,'SWP','State Water Project-Banks','Banks');
Insert into WT_FU_TYPE (WT_FU_TYPE_ID,FU_TYPE,FU_TYPE_DESCRIPTION,FU_SUB_TYPE) values (WT_FU_TYPE_PKSEQ.NEXTVAL,'SWP','State Water Project-North Bay','North Bay');
/

--------------------------------------------------------------------------------
--  DDL for Link Table WT_APP_AGENCY
--------------------------------------------------------------------------------
DROP TABLE WT_APP_AGENCY;

CREATE TABLE WT_APP_AGENCY (
  WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID) ON DELETE CASCADE
  ,WT_AGENCY_ID REFERENCES WT_AGENCY(WT_AGENCY_ID)
  ,PRIMARY KEY (WT_TRANS_ID, WT_AGENCY_ID)
);
/


