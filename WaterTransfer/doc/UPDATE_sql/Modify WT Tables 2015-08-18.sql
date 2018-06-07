

--------------------------------------------------------------------------------
--  Add Columns to table WT_WELL
--------------------------------------------------------------------------------
ALTER TABLE WT_WELL ADD LOCAL_WELL_ID VARCHAR2(32);
ALTER TABLE WT_WELL ADD CASGEM_STATION_ID NUMBER(10,0);
/

