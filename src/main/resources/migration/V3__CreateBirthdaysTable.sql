CREATE TABLE bdays.birthdays
(
    id integer NOT NULL,
    date_of_birth date check (date_of_birth<=CURRENT_DATE),
    id_template integer,
    user_name varchar (100),
    CONSTRAINT birthdays_pkey PRIMARY KEY (id),
    CONSTRAINT birthdays_fk FOREIGN KEY (id_template)
        REFERENCES bdays.templates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE bdays.birthdays_id_seq NO MINVALUE NO MAXVALUE NO CYCLE;
ALTER TABLE bdays.birthdays ALTER COLUMN id SET DEFAULT nextval('bdays.birthdays_id_seq');
ALTER SEQUENCE bdays.birthdays_id_seq OWNED BY bdays.birthdays.id;

INSERT INTO bdays.birthdays (date_of_birth, id_template, user_name) VALUES
('1980-03-14',2,'megan'),
('2010-08-25',1,'alex'),
('1999-12-05',4,'ann'),
('2001-06-27',3,'dorian'),
('1996-04-19',3,'meh'),
(CURRENT_DATE , 1, 'melhdor'),
(current_date , 3, 'detel');