--------------------------------------------------------------------------------
--  Modify Table WT_TRANS
--------------------------------------------------------------------------------
ALTER TABLE WT_TRANS MODIFY TRANS_DESCRIPTION VARCHAR2(2048);
ALTER TABLE WT_TRANS MODIFY DESCRIPTION VARCHAR2(2048);
/

--------------------------------------------------------------------------------
--  Modify Table WT_CONTACT
--------------------------------------------------------------------------------
ALTER TABLE WT_CONTACT MODIFY TITLE VARCHAR2(64);
/



