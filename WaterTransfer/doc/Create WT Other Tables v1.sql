--------------------------------------------------------------------------------
--  DROP ALL TABLES, SEQUENCE AND TRIGGERS
--------------------------------------------------------------------------------
DROP TRIGGER WT_DIV_RIGHT_PKTRIG;
DROP SEQUENCE WT_DIV_RIGHT_PKSEQ;
DROP TABLE WT_DIV_RIGHT;


--------------------------------------------------------------------------------
--  DDL for Table WT_DIV_RIGHT
--------------------------------------------------------------------------------
CREATE TABLE WT_DIV_RIGHT (
  WT_DIV_RIGHT_ID NUMERIC(16,0) PRIMARY KEY
  ,DIV_RIGHT_TYPE VARCHAR2(32)
  ,PERMIT_NUM NUMERIC(16,0)
  ,DIV_HISTORIC VARCHAR2(128)
);
/

CREATE SEQUENCE WT_DIV_RIGHT_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_DIV_RIGHT_PKTRIG
BEFORE INSERT
ON WT_DIV_RIGHT
FOR EACH ROW
BEGIN
  IF :NEW.WT_DIV_RIGHT_ID IS NULL THEN
    SELECT WT_DIV_RIGHT_PKSEQ.NEXTVAL
    INTO :NEW.WT_DIV_RIGHT_ID
    FROM DUAL;
  END IF;
END WT_DIV_RIGHT;
/
















