--------------------------------------------------------------------------------
--  DROP ALL TABLES AND SEQUENCE
--------------------------------------------------------------------------------
DROP TRIGGER WT_TRANS_TYPE_PKTRIG;
DROP SEQUENCE WT_TRANS_TYPE_PKSEQ;
DROP TABLE WT_TRANS_TYPE;

DROP TRIGGER WT_FU_TYPE_PKTRIG;
DROP SEQUENCE WT_FU_TYPE_PKSEQ;
DROP TABLE WT_FU_TYPE;

--DROP TRIGGER WT_SELLER_BUYER_PKTRIG;
--DROP SEQUENCE WT_SELLER_BUYER_PKSEQ;
--DROP TABLE WT_SELLER_BUYER;

DROP TRIGGER WT_BUYER_PKTRIG;
DROP SEQUENCE WT_BUYER_PKSEQ;
DROP TABLE WT_BUYER;

DROP TRIGGER WT_SELLER_PKTRIG;
DROP SEQUENCE WT_SELLER_PKSEQ;
DROP TABLE WT_SELLER;

DROP TRIGGER WT_TRANS_PKTRIG;
DROP SEQUENCE WT_TRANS_PKSEQ;
DROP TABLE WT_TRANS;

DROP TRIGGER WT_AGENCY_ADDRESS_PKTRIG;
DROP SEQUENCE WT_AGENCY_ADDRESS_PKSEQ;
DROP TABLE WT_AGENCY_ADDRESS;

DROP TRIGGER WT_AGENCY_PKTRIG;
DROP SEQUENCE WT_AGENCY_PKSEQ;
DROP TABLE WT_AGENCY;

DROP TRIGGER WT_ADDRESS_PKTRIG;
DROP SEQUENCE WT_ADDRESS_PKSEQ;
DROP TABLE WT_ADDRESS;

DROP TRIGGER WT_STATE_PKTRIG;
DROP SEQUENCE WT_STATE_PKSEQ;
DROP TABLE WT_STATE;

DROP TRIGGER WT_COUNTY_PKTRIG;
DROP SEQUENCE WT_COUNTY_PKSEQ;
DROP TABLE WT_COUNTY;

DROP TRIGGER WT_CITY_PKTRIG;
DROP SEQUENCE WT_CITY_PKSEQ;
DROP TABLE WT_CITY;

--------------------------------------------------------------------------------
--  DDL for Table WT_CITY
--------------------------------------------------------------------------------
CREATE TABLE WT_CITY (
  WT_CITY_ID NUMERIC(16,0) PRIMARY KEY
  ,NAME VARCHAR2(64) NOT NULL ENABLE
  ,SHORT_NAME VARCHAR2(16)
);
/

CREATE SEQUENCE WT_CITY_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_CITY_PKTRIG
BEFORE INSERT
ON WT_CITY
FOR EACH ROW
BEGIN
  IF :NEW.WT_CITY_ID IS NULL THEN
    SELECT WT_CITY_PKSEQ.NEXTVAL
    INTO :NEW.WT_CITY_ID
    FROM DUAL;
  END IF;
END WT_CITY;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_COUNTY
--------------------------------------------------------------------------------
CREATE TABLE WT_COUNTY (
  WT_COUNTY_ID NUMERIC(16,0) PRIMARY KEY
  ,NAME VARCHAR2(64) NOT NULL ENABLE
  ,SHORT_NAME VARCHAR2(16)
);
/

CREATE SEQUENCE WT_COUNTY_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_COUNTY_PKTRIG
BEFORE INSERT
ON WT_COUNTY
FOR EACH ROW
BEGIN
  IF :NEW.WT_COUNTY_ID IS NULL THEN
    SELECT WT_COUNTY_PKSEQ.NEXTVAL
    INTO :NEW.WT_COUNTY_ID
    FROM DUAL;
  END IF;
END WT_COUNTY;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_STATE
--------------------------------------------------------------------------------
CREATE TABLE WT_STATE (
  WT_STATE_ID NUMERIC(16,0) PRIMARY KEY
  ,NAME VARCHAR2(64) NOT NULL ENABLE
  ,SHORT_NAME VARCHAR2(16)
);
/

CREATE SEQUENCE WT_STATE_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_STATE_PKTRIG
BEFORE INSERT
ON WT_STATE
FOR EACH ROW
BEGIN
  IF :NEW.WT_STATE_ID IS NULL THEN
    SELECT WT_STATE_PKSEQ.NEXTVAL
    INTO :NEW.WT_STATE_ID
    FROM DUAL;
  END IF;
END WT_STATE;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_ADDRESS
--------------------------------------------------------------------------------
CREATE TABLE WT_ADDRESS (
  WT_ADDRESS_ID NUMERIC(16,0) PRIMARY KEY
  ,ADDRESS1 VARCHAR2(64) NOT NULL ENABLE
  ,ADDRESS2 VARCHAR2(64) 
  ,WT_CITY_ID REFERENCES WT_CITY(WT_CITY_ID)
  ,WT_COUNTY_ID REFERENCES WT_COUNTY(WT_COUNTY_ID)
  ,WT_STATE_ID REFERENCES WT_STATE(WT_STATE_ID)
  ,ZIPCODE NUMBER(5,0)
  ,ZIP_EXT NUMBER(5,0)
);
/

CREATE SEQUENCE WT_ADDRESS_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_ADDRESS_PKTRIG
BEFORE INSERT
ON WT_ADDRESS
FOR EACH ROW
BEGIN
  IF :NEW.WT_ADDRESS_ID IS NULL THEN
    SELECT WT_ADDRESS_PKSEQ.NEXTVAL
    INTO :NEW.WT_ADDRESS_ID
    FROM DUAL;
  END IF;
END WT_ADDRESS;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_AGENCY
--------------------------------------------------------------------------------
CREATE TABLE WT_AGENCY (
  WT_AGENCY_ID NUMERIC(16,0) PRIMARY KEY
  ,AGENCY_CODE VARCHAR2(16) NOT NULL ENABLE
  ,AGENCY_FULL_NAME VARCHAR2(128) 
  ,AGENCY_ACTIVE_IND VARCHAR2(1) DEFAULT 'Y' NOT NULL
);
/

CREATE SEQUENCE WT_AGENCY_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_AGENCY_PKTRIG
BEFORE INSERT
ON WT_AGENCY
FOR EACH ROW
BEGIN
  IF :NEW.WT_AGENCY_ID IS NULL THEN
    SELECT WT_AGENCY_PKSEQ.NEXTVAL
    INTO :NEW.WT_AGENCY_ID
    FROM DUAL;
  END IF;
END WT_AGENCY;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_AGENCY_ADDRESS
--------------------------------------------------------------------------------
CREATE TABLE WT_AGENCY_ADDRESS (
  WT_AGENCY_ADDRESS_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_AGENCY_ID REFERENCES WT_AGENCY(WT_AGENCY_ID)
  ,WT_ADDRESS_ID REFERENCES WT_ADDRESS(WT_ADDRESS_ID)
);
/

CREATE SEQUENCE WT_AGENCY_ADDRESS_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_AGENCY_ADDRESS_PKTRIG
BEFORE INSERT
ON WT_AGENCY_ADDRESS
FOR EACH ROW
BEGIN
  IF :NEW.WT_AGENCY_ADDRESS_ID IS NULL THEN
    SELECT WT_AGENCY_ADDRESS_PKSEQ.NEXTVAL
    INTO :NEW.WT_AGENCY_ADDRESS_ID
    FROM DUAL;
  END IF;
END WT_AGENCY_ADDRESS;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_TRANS
--------------------------------------------------------------------------------
CREATE TABLE WT_TRANS (
  WT_TRANS_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_NUM VARCHAR2(32) NOT NULL  
  ,TRANS_YEAR NUMBER(14,0) NOT NULL
  ,PRO_TRANS_QUA NUMBER(32,0)
  ,ACT_TRANS_QUA NUMBER(32,0)
  ,DWR_PRO_APPR_DATE DATE
  ,TRANS_WIN_START DATE
  ,TRANS_WIN_END DATE
  ,PRO_AGREE_PAID NUMBER(18,2)
  ,ACT_AMT_PAID NUMBER(18,2)
  ,CAL_AMT_PAID NUMBER(18,2)
  ,PRO_UNIT_COST NUMBER(18,2)
  ,CAL_UNIT_COST NUMBER(18,2)
  ,PRO_ACR_IDLE NUMBER(32,0)
  ,PRO_ACR_IDLE_IND VARCHAR2(1) DEFAULT 'N' NOT NULL
  ,ACT_FALL_ACR NUMBER(32,0)
  ,ACT_FALL_ACR_IND VARCHAR2(1) DEFAULT 'N' NOT NULL 
  ,RES_RE_OP_IND VARCHAR2(1) DEFAULT 'N' NOT NULL 
  ,CONS_WATER_IND VARCHAR2(1) DEFAULT 'N' NOT NULL
  ,WELL_USE_NUM NUMBER(16,0)
  ,WELL_USE_NUM_IND VARCHAR2(1) DEFAULT 'N' NOT NULL
  ,WT_COMM VARCHAR2(250)  
  ,CREATE_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP
  ,CREATED_BY_ID NUMBER(*,0) 	
	,MODIFY_DATE TIMESTAMP(6) DEFAULT SYSTIMESTAMP 
  ,UPDATED_BY_ID NUMBER(*,0) 
);
/

CREATE SEQUENCE WT_TRANS_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_TRANS_PKTRIG
BEFORE INSERT
ON WT_TRANS
FOR EACH ROW
BEGIN
  IF :NEW.WT_TRANS_ID IS NULL THEN
    SELECT WT_TRANS_PKSEQ.NEXTVAL
    INTO :NEW.WT_TRANS_ID
    FROM DUAL;
  END IF;
END WT_TRANS;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_SELLER
--------------------------------------------------------------------------------
CREATE TABLE WT_SELLER (
  WT_SELLER_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,WT_AGENCY_ID REFERENCES WT_AGENCY(WT_AGENCY_ID)
);
/

CREATE SEQUENCE WT_SELLER_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_SELLER_PKTRIG
BEFORE INSERT
ON WT_SELLER
FOR EACH ROW
BEGIN
  IF :NEW.WT_SELLER_ID IS NULL THEN
    SELECT WT_SELLER_PKSEQ.NEXTVAL
    INTO :NEW.WT_SELLER_ID
    FROM DUAL;
  END IF;
END WT_SELLER;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_BUYER
--------------------------------------------------------------------------------
CREATE TABLE WT_BUYER (
  WT_BUYER_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,WT_AGENCY_ID REFERENCES WT_AGENCY(WT_AGENCY_ID)
);
/

CREATE SEQUENCE WT_BUYER_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_BUYER_PKTRIG
BEFORE INSERT
ON WT_BUYER
FOR EACH ROW
BEGIN
  IF :NEW.WT_BUYER_ID IS NULL THEN
    SELECT WT_BUYER_PKSEQ.NEXTVAL
    INTO :NEW.WT_BUYER_ID
    FROM DUAL;
  END IF;
END WT_BUYER;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_FU_TYPE
--------------------------------------------------------------------------------
CREATE TABLE WT_FU_TYPE (
  WT_FU_TYPE_ID NUMERIC(16,0) PRIMARY KEY
  ,FU_TYPE VARCHAR2(16) NOT NULL ENABLE
  ,FU_TYPE_DESCRIPTION  VARCHAR2(128) 
);
/

CREATE SEQUENCE WT_FU_TYPE_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_FU_TYPE_PKTRIG
BEFORE INSERT
ON WT_FU_TYPE
FOR EACH ROW
BEGIN
  IF :NEW.WT_FU_TYPE_ID IS NULL THEN
    SELECT WT_FU_TYPE_PKSEQ.NEXTVAL
    INTO :NEW.WT_FU_TYPE_ID
    FROM DUAL;
  END IF;
END WT_FU_TYPE;
/

--------------------------------------------------------------------------------
--  DDL for Table WT_TRANS_TYPE
--------------------------------------------------------------------------------
CREATE TABLE WT_TRANS_TYPE (
  WT_TRANS_TYPE_ID NUMERIC(16,0) PRIMARY KEY
  ,WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,WT_FU_TYPE_ID REFERENCES WT_FU_TYPE(WT_FU_TYPE_ID)
);
/

CREATE SEQUENCE WT_TRANS_TYPE_PKSEQ  
  START WITH 1
  INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER WT_TRANS_TYPE_PKTRIG
BEFORE INSERT
ON WT_TRANS_TYPE
FOR EACH ROW
BEGIN
  IF :NEW.WT_TRANS_TYPE_ID IS NULL THEN
    SELECT WT_TRANS_TYPE_PKSEQ.NEXTVAL
    INTO :NEW.WT_TRANS_TYPE_ID
    FROM DUAL;
  END IF;
END WT_TRANS_TYPE;
/

