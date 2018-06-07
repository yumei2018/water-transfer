--------------------------------------------------------------------------------
--  Add Columns to Table WT_RESERVOIR
--------------------------------------------------------------------------------
ALTER TABLE WT_RESERVOIR ADD RIVER_MILE_START NUMBER;
ALTER TABLE WT_RESERVOIR ADD RIVER_MILE_END NUMBER;
ALTER TABLE WT_RESERVOIR ADD IS_OPR_OBLIGATION NUMBER(1,0);
ALTER TABLE WT_RESERVOIR ADD IS_REG_OBLIGATION NUMBER(1,0);
/


