DELETE FROM WT_RESPONSE;
DELETE FROM WT_REVIEW_COMMENT;

SELECT * FROM WT_TRANS WHERE WT_TRANS_ID NOT IN(SELECT WT_TRANS_ID FROM WT_TRANS_USER) AND WT_TRANS.WT_STATUS_FLAG_ID = 0;
SELECT * FROM WT_TRANS WHERE WT_STATUS_FLAG_ID = 0;

DELETE FROM WT_TRANS WHERE WT_TRANS_ID IN('385','563','581','582','601','721','722','723','724','725','736');
DELETE FROM WT_TRANS WHERE WT_TRANS_ID=421;

