
--------------------------------------------------------------------------------
--  Add Columns to Table WT_ATTACHMENT
--------------------------------------------------------------------------------

ALTER TABLE WT_ATTACHMENT ADD ATTACHMENT_TYPE VARCHAR2(64);
ALTER TABLE WT_ATTACHMENT ADD TITLE VARCHAR2(128);
ALTER TABLE WT_ATTACHMENT ADD CREATED_BY_ID NUMBER(*,0);
ALTER TABLE WT_ATTACHMENT ADD UPDATED_BY_ID NUMBER(*,0);
/



