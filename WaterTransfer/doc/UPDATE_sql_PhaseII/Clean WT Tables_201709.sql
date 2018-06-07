--------------------------------------------------------------------------------
--  Clean Account Tables
--------------------------------------------------------------------------------
TRUNCATE TABLE USER_REGISTRATION;
EXEC reset_seq('USER_REGISTER_PKSEQ');

--------------------------------------------------------------------------------
--  Clean Trans and related Tables
--------------------------------------------------------------------------------
TRUNCATE TABLE WT_STATUS_TRACK;
EXEC reset_seq('WT_STATUS_TRACK_PKSEQ');
TRUNCATE TABLE WT_TRACK_FILE;
EXEC reset_seq('WT_TRACK_FILE_PKSEQ');



