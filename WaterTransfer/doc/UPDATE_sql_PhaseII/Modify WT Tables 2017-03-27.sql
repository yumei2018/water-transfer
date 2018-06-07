--------------------------------------------------------------------------------
--  Modify Table APP_GROUP
--------------------------------------------------------------------------------
INSERT INTO APP_GROUP (GROUP_ID, NAME, CODE, DESCRIPTION) VALUES (6, 'Buyers Representative', 'BUYER_REP', 'Buyers Representative to Access Buyers Proposals');
INSERT INTO APP_GROUP (GROUP_ID, NAME, CODE, DESCRIPTION) VALUES (7, 'US Bureau of Reclamation', 'USBR', 'Members of USBR to Access Seller Proposals');

--------------------------------------------------------------------------------
--  Modify Table WT_STATUS_FLAG
--------------------------------------------------------------------------------
UPDATE WT_STATUS_FLAG SET STATUS_NAME='INCOMPLETE', STATUS_DESCRIPTION='Proposal Incomplete' WHERE WT_STATUS_FLAG_ID=3;
UPDATE WT_STATUS_FLAG SET STATUS_NAME='PCOMPLETE', STATUS_DESCRIPTION='Proposal Complete' WHERE WT_STATUS_FLAG_ID=4;
/


--------------------------------------------------------------------------------
--  DDL for Link Table WT_REP_AGENCY
--------------------------------------------------------------------------------
--DROP TABLE WT_REP_AGENCY;

--CREATE TABLE WT_REP_AGENCY (
--  USER_ID REFERENCES APP_USER(USER_ID) ON DELETE CASCADE
--  ,WT_AGENCY_ID REFERENCES WT_AGENCY(WT_AGENCY_ID)
--  ,PRIMARY KEY (USER_ID, WT_AGENCY_ID)
--);
--/

--------------------------------------------------------------------------------
--  Update Table WT_CHECKLIST
--------------------------------------------------------------------------------
UPDATE WT_CHECKLIST SET detail='Refer to Section 1.4 of the White Paper' WHERE WT_CHECKLIST_ID = 2;
UPDATE WT_CHECKLIST SET detail='Petition for Change with SWRCB (Refer to Section 1.5 of the White Paper)' WHERE WT_CHECKLIST_ID = 3;
UPDATE WT_CHECKLIST SET detail='Refer to Section 1.5 of the White Paper' WHERE WT_CHECKLIST_ID = 4;
UPDATE WT_CHECKLIST SET detail='Refer to Section 1.5 of the White Paper' WHERE WT_CHECKLIST_ID = 5;

--------------------------------------------------------------------------------
--  Insert Table APP_USER 
--------------------------------------------------------------------------------
--Insert into APP_USER (USER_ID,USERNAME,PASSWORD,ACTIVE,CREATED_DATE,UPDATED_DATE,WT_CONTACT_ID,NEED_PASSWORD_RESET) values (381,'EChapman','7152269b376af51c00cc0322866f61b2',1,to_timestamp('13-DEC-16 02.04.19.245520000 PM','DD-MON-RR HH.MI.SSXFF AM'),to_timestamp('13-DEC-16 02.25.35.867915000 PM','DD-MON-RR HH.MI.SSXFF AM'),102,0);
UPDATE APP_USER SET PASSWORD='7152269b376af51c00cc0322866f61b2' WHERE USER_ID = 381;

UPDATE WT_CONTACT SET EMAIL='vinh.giang@water.ca.gov' WHERE WT_CONTACT_ID = 102;
UPDATE WT_CONTACT SET EMAIL='echapman@swc.org' WHERE WT_CONTACT_ID = 102;













