--------------------------------------------------------------------------------
--  Modify Table WT_TRANS
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS MODIFY WT_COMM VARCHAR2(2048);
--ALTER TABLE WT_TRANS MODIFY DWR_COMMENTS VARCHAR2(2048);
/

--------------------------------------------------------------------------------
--  Update Table WT_FU_TYPE
--------------------------------------------------------------------------------
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='Jones' WHERE WT_FU_TYPE_ID = 11;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='SWP/CVP Intertie' WHERE WT_FU_TYPE_ID = 12;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='Forbearance' WHERE WT_FU_TYPE_ID = 13;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='Warren Act' WHERE WT_FU_TYPE_ID = 14;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='Banks' WHERE WT_FU_TYPE_ID = 21;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='North Bay' WHERE WT_FU_TYPE_ID = 22;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION='SWP/CVP Intertie' WHERE WT_FU_TYPE_ID = 23;

--------------------------------------------------------------------------------
--  Modify Table APP_USER/USER_REGISTRATION/WT_CONTACT 2018.02.15
--------------------------------------------------------------------------------
ALTER TABLE APP_USER MODIFY USERNAME VARCHAR2(128);
/

ALTER TABLE USER_REGISTRATION MODIFY EMAIL VARCHAR2(128);
/

ALTER TABLE WT_CONTACT MODIFY EMAIL VARCHAR2(128);
/

--------------------------------------------------------------------------------
--  Modify Table WT_REVIEW_NOTE 2018.05.02
--------------------------------------------------------------------------------
ALTER TABLE WT_REVIEW_NOTE MODIFY SECTION_KEY VARCHAR2(24);
/

--------------------------------------------------------------------------------
--  Modify Table WT_CHECKLIST 2018.05.16
--------------------------------------------------------------------------------
SELECT * FROM WT_CHECKLIST WHERE CHECKLIST_FIELD='BI_ATTACHMENT'; 

UPDATE WT_CHECKLIST SET NAME = 'SWRCB Petition for Change' WHERE NAME = 'SWRCB Order';
UPDATE WT_CHECKLIST SET NAME = 'Compliance with CEQA/SWRCB Order' WHERE NAME = 'Compliance with CEQA/SWRCB Approval';

SELECT WT_TRANS_ID, max(STATUS_DATE) AS MAXDATE FROM WT_STATUS_TRACK WHERE STATUS_NAME = 'SUBMITTED' GROUP BY WT_TRANS_ID;

--------------------------------------------------------------------------------
--  Modify Table WT_FU_TYPE 2018.05.18
--------------------------------------------------------------------------------
SELECT * FROM WT_FU_TYPE;
UPDATE WT_FU_TYPE SET FU_TYPE_DESCRIPTION = 'Other Project' WHERE FU_TYPE = 'OTHER';





























