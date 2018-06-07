

--------------------------------------------------------------------------------
--  Add Columns to table APP_USER
--------------------------------------------------------------------------------
ALTER TABLE APP_USER ADD NEED_PASSWORD_RESET NUMBER(1,0) DEFAULT 1 CHECK("NEED_PASSWORD_RESET" IN (NULL, 0, 1));
/

--------------------------------------------------------------------------------
--  Create Link Table WT_TRANS_USER
--------------------------------------------------------------------------------
DROP TABLE WT_TRANS_USER;

CREATE TABLE WT_TRANS_USER (
  WT_TRANS_ID REFERENCES WT_TRANS(WT_TRANS_ID)
  ,USER_ID REFERENCES APP_USER(USER_ID)
  ,PRIMARY KEY(WT_TRANS_ID,USER_ID)
);
/

INSERT INTO WT_TRANS_USER (WT_TRANS_ID,USER_ID) SELECT WT_TRANS_ID, CREATED_BY_ID FROM WT_TRANS WHERE CREATED_BY_ID IS NOT NULL;
/