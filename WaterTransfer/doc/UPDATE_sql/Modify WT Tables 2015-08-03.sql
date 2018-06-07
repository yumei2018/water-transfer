

--------------------------------------------------------------------------------
--  Add Columns to table WT_CROP_IDLING
--------------------------------------------------------------------------------

ALTER TABLE WT_CROP_IDLING ADD PRO_TRANSFER_BY_CI NUMBER(18,2);
ALTER TABLE WT_CROP_IDLING ADD PRO_TRANSFER_BY_CS NUMBER(18,2);
/

--------------------------------------------------------------------------------
--  Add Columns to table WT_TRACK_FILE
--------------------------------------------------------------------------------

ALTER TABLE WT_TRACK_FILE ADD FILE_TYPE VARCHAR(32);
ALTER TABLE WT_TRACK_FILE ADD FILE_VERSION NUMBER(8,0);
/

--ALTER TABLE WT_TRACK_FILE MODIFY FILE_TYPE VARCHAR(32);
