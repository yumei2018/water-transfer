
--------------------------------------------------------------------------------
--  Add Columns to table WT_WELL
--------------------------------------------------------------------------------
ALTER TABLE WT_WELL ADD WELL_TRANSFER NUMBER(1,0) DEFAULT 0 CHECK (WELL_TRANSFER IN (NULL,0,1));
ALTER TABLE WT_WELL ADD WELL_MONITORING NUMBER(1,0) DEFAULT 0 CHECK (WELL_MONITORING IN (NULL,0,1));
/

--------------------------------------------------------------------------------
--  Add Columns to table WT_CHECKLIST
--------------------------------------------------------------------------------
ALTER TABLE WT_CHECKLIST ADD WELL_TRANSFER NUMBER(1,0) DEFAULT 0 CHECK (WELL_TRANSFER IN (NULL,0,1));
ALTER TABLE WT_CHECKLIST ADD WELL_MONITORING NUMBER(1,0) DEFAULT 0 CHECK (WELL_MONITORING IN (NULL,0,1));
/


