
INSERT INTO APP_USER ("USERNAME","PASSWORD") VALUES ('test','098f6bcd4621d373cade4e832627b4f6'); --'test'
INSERT INTO USER_GROUP (USER_ID,GROUP_ID) VALUES ((SELECT USER_ID FROM APP_USER WHERE USERNAME = 'test'),(SELECT GROUP_ID FROM APP_GROUP WHERE CODE = 'SUPER_ADMIN'));
COMMIT;

INSERT INTO APP_USER ("USERNAME","PASSWORD") VALUES ('seller','64c9ac2bb5fe46c3ac32844bb97be6bc'); --'seller'
INSERT INTO USER_GROUP (USER_ID,GROUP_ID) VALUES ((SELECT USER_ID FROM APP_USER WHERE USERNAME = 'seller'),(SELECT GROUP_ID FROM APP_GROUP WHERE CODE = 'APP_ACCT'));
/

INSERT INTO APP_USER ("USERNAME","PASSWORD") VALUES ('reviewer','7ba917e4e5158c8a9ed6eda08a6ec572'); --'reviewer'
INSERT INTO USER_GROUP (USER_ID,GROUP_ID) VALUES ((SELECT USER_ID FROM APP_USER WHERE USERNAME = 'reviewer'),(SELECT GROUP_ID FROM APP_GROUP WHERE CODE = 'DWR_REVIEWER'));
/

INSERT INTO APP_USER ("USERNAME","PASSWORD") VALUES ('manager','1d0258c2440a8d19e716292b231e3190'); --'manager'
INSERT INTO USER_GROUP (USER_ID,GROUP_ID) VALUES ((SELECT USER_ID FROM APP_USER WHERE USERNAME = 'manager'),(SELECT GROUP_ID FROM APP_GROUP WHERE CODE = 'DWR_MANAGER'));
/

INSERT INTO APP_USER ("USERNAME","PASSWORD") VALUES ('admin','21232f297a57a5a743894a0e4a801fc3'); --'admin'
INSERT INTO USER_GROUP (USER_ID,GROUP_ID) VALUES ((SELECT USER_ID FROM APP_USER WHERE USERNAME = 'admin'),(SELECT GROUP_ID FROM APP_GROUP WHERE CODE = 'SYS_ADMIN'));
/














