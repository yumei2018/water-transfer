
--------------------------------------------------------------------------------
--  Insert for Table WT_CHECKLIST
--------------------------------------------------------------------------------
--
select WT_CHECKLIST_PKSEQ.nextval from WT_CHECKLIST;
SELECT * FROM WT_CHECKLIST;
TRUNCATE TABLE WT_ATT_CHECKLIST; 
DELETE FROM WT_CHECKLIST;
--TRUNCATE TABLE WT_CHECKLIST;

SET DEFINE OFF;
BEGIN
  reset_seq('WT_CHECKLIST_PKSEQ');
END;
/

-- Insert WELL_ATTACHMENT Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Well Completion Report', 'CHECKBOX', 1, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Well Construction', 'CHECKBOX', 2, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Geologic Log', 'CHECKBOX', 3, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Photo(s) of well site', 'CHECKBOX', 4, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Photo of meter', 'CHECKBOX', 5, 1, 0);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Meter Certification', 'CHECKBOX', 6, 1, 0);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Other', 'CHECKBOX', 7, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'March Readings', 'CHECKBOX', 8, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Monitoring Network', 'CHECKBOX', 9, 0, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Meter readings', 'CHECKBOX', 10, 1, 0);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Groundwater level measurements', 'CHECKBOX', 11, 1, 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Groundwater quality monitoring', 'CHECKBOX', 12, 1, 0);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER,WELL_TRANSFER,WELL_MONITORING) VALUES ('WELL_ATTACHMENT', 'Data evaluation and reporting', 'CHECKBOX', 13, 1, 1);

-- Insert GW_ATTACHMENT Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Baseline Information', 'CHECKBOX', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Historic Operations', 'CHECKBOX', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Justification of a non 13% Depletion Factor', 'CHECKBOX', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Schedule and volume of water to be pumped', 'CHECKBOX', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Other', 'CHECKBOX', 5);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Land subsidence methods', 'CHECKBOX', 6);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Coordination plan', 'CHECKBOX', 7);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Data evaluation and reporting plan', 'CHECKBOX', 8);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Procedure to receive reports of effects', 'CHECKBOX', 9);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Procedure for investigating effects', 'CHECKBOX', 10);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Mitigation Plan and Financial Assurance Plan', 'CHECKBOX', 11);
--INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Mitigation options', 'CHECKBOX', 11);
--INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_ATTACHMENT', 'Financial assurances', 'CHECKBOX', 12);


--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (15, 'GW_ATTACHMENT', 'Baseline Information', 'CHECKBOX');
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (16, 'GW_ATTACHMENT', 'Historic Operations', 'CHECKBOX', 2);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (17, 'GW_ATTACHMENT', 'Justification of a non 12% Depletion Factor', 'CHECKBOX', 3);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (18, 'GW_ATTACHMENT', 'Schedule and volume of water to be pumped', 'CHECKBOX', 4);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (19, 'GW_ATTACHMENT', 'Other', 'CHECKBOX', 5);

--SELECT * FROM WT_CHECKLIST WHERE CHECKLIST_FIELD='BI_ATTACHMENT';
--DELETE FROM WT_CHECKLIST WHERE CHECKLIST_FIELD='BI_ATTACHMENT';
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (15, 'BI_ATTACHMENT', 'Buyer/Seller Agreement', 'CHECKBOX', 1);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (16, 'BI_ATTACHMENT', 'Historic Data', 'CHECKBOX', 2);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (17, 'BI_ATTACHMENT', 'Location Information', 'CHECKBOX', 3);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (18, 'BI_ATTACHMENT', 'Proposal Report', 'CHECKBOX', 4);
--INSERT INTO WT_CHECKLIST(WT_CHECKLIST_ID,CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES (19, 'BI_ATTACHMENT', 'Other', 'CHECKBOX', 5);

-- Insert BI_ATTACHMENT Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('BI_ATTACHMENT', 'Buyer/Seller Agreement', 'CHECKBOX', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('BI_ATTACHMENT', 'Historic Data', 'CHECKBOX', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('BI_ATTACHMENT', 'Location Information', 'CHECKBOX', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('BI_ATTACHMENT', 'Proposal Report', 'CHECKBOX', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('BI_ATTACHMENT', 'Other', 'CHECKBOX', 5);

-- Delete CI_ATTACHMENT Checklist
--DELETE FROM WT_CHECKLIST WHERE CHECKLIST_FIELD='CI_ATTACHMENT';
-- Insert CI_ATTACHMENT Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Field Data Entry Form', 'CHECKBOX', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Compliance with CEQA/SWRCB Approval', 'CHECKBOX', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Mitigation Plan', 'CHECKBOX', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of agency boundary', 'CHECKBOX', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of field boundaries', 'CHECKBOX', 5);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of field identification numbers', 'CHECKBOX', 6);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of fields to be idled as part of water transfer', 'CHECKBOX', 7);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of fields to have crop shift as part of water transfer', 'CHECKBOX', 8);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of current year FSA acreage of each field', 'CHECKBOX', 9);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of areas known to have high seepage', 'CHECKBOX', 10);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of areas adjacent to wildlife refuges or areas managed for habitat', 'CHECKBOX', 11);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of portion of any fields dedicated to non-cropping purposes such as equipment storage', 'CHECKBOX', 12);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of fields currently irrigated', 'CHECKBOX', 13);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of fields routinely irrigated', 'CHECKBOX', 14);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('CI_ATTACHMENT', 'Map of fields routinely not irrigated', 'CHECKBOX', 15);
/

-- Insert RV_ATTACHMENT Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('RV_ATTACHMENT', 'Flood Control Diagram', 'CHECKBOX', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('RV_ATTACHMENT', 'Reservoir Area-Capacity curve', 'CHECKBOX', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('RV_ATTACHMENT', '5-Year Reservoir Operations data spreadsheet', 'CHECKBOX', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('RV_ATTACHMENT', 'Reservoir Reoperation Additional Information form', 'CHECKBOX', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('RV_ATTACHMENT', 'Optional GIS shapefile', 'CHECKBOX', 5);
/

--Drop Constraint SYS_C0016156
ALTER TABLE WT_CHECKLIST DROP CONSTRAINT SYS_C0016156;
--Drop QA DB Constraint SYS_C0041265
ALTER TABLE WT_CHECKLIST DROP CONSTRAINT SYS_C0041265;

-- Insert GW_REQUIRED Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Transfer proposal', 'GW', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Notice and information requirements described in local ordinances', 'GW', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Well ID: owner, owner ID, and district ID number', 'GW', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Well Location: Lat/Long., T,R,S.', 'GW', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Well Location: Well(s) shown on scaled, detailed map showing surface water features within 2 miles of service area boundary. Include wells in monitoring network.', 'GW', 5);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Historic Operations: Volume of water pumped from each well in previous year. Document and identify areas normally irrigated by wells involved in the transfer.', 'GW', 6);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Proposed Operations: Projected operation and beneficial use', 'GW', 7);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Proposed Operations: Verification that totalizing flow meters installed and calibrated', 'GW', 8);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Well Construction', 'GW', 9);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Geologic Log', 'GW', 10);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Estimated Well Capacity and method', 'GW', 11);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Additional Information: Well pump efficiency test, pumping tests, groundwater quality data, site-specific studies', 'GW', 12);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_REQUIRED', 'Pump Power: Verification that pump powered by electric source or offsetting reductions provided elsewhere', 'GW', 13);
/

-- Insert GW_MONITORING Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Monitoring well network', 'GW', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'GW Pumping Measurements', 'GW', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Groundwater Level Measurements', 'GW', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Groundwater quality monitoring', 'GW', 4);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Land subsidence detection or credible analysis', 'GW', 5);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Coordinated data collection and cooperation with other monitoring efforts in the area', 'GW', 6);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MONITORING', 'Data evaluation and reporting', 'GW', 7);
/

-- Insert GW_MITIGATION Checklist
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MITIGATION', 'Procedure for seller to receive reports of purported effects and report information to DWR, Reclamation and local agencies', 'GW', 1);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MITIGATION', 'Procedure for investigating any purported effect with a means to resolve disputes between seller and other parties claiming injury', 'GW', 2);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MITIGATION', 'Development of mitigation options, in cooperation with the affected third parties, for legitimate effects', 'GW', 3);
INSERT INTO WT_CHECKLIST(CHECKLIST_FIELD,NAME,CHECKLIST_TYPE,SORT_ORDER) VALUES ('GW_MITIGATION', 'Assurance that adequate financial resources are available to cover reasonably anticipated mitigation needs', 'GW', 4);
/












