ALTER TABLE bdays.users ADD role character varying(100);
UPDATE bdays.users SET role='ADMIN' where id=1;
UPDATE bdays.users SET role='USER' where id=2;
ALTER TABLE bdays.users ALTER COLUMN role SET NOT NULL;