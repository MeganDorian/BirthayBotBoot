CREATE SEQUENCE bdays.templates_id_seq NO MINVALUE NO MAXVALUE NO CYCLE;
ALTER TABLE bdays.templates ALTER COLUMN id SET DEFAULT nextval('bdays.templates_id_seq');
ALTER SEQUENCE bdays.templates_id_seq OWNED BY bdays.templates.id;

CREATE SEQUENCE bdays.users_id_seq NO MINVALUE NO MAXVALUE NO CYCLE;
ALTER TABLE bdays.users ALTER COLUMN id SET DEFAULT nextval('bdays.users_id_seq');
ALTER SEQUENCE bdays.users_id_seq OWNED BY bdays.users.id;