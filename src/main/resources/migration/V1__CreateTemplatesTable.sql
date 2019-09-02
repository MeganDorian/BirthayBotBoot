CREATE TABLE bdays.templates
(
    id integer NOT NULL,
    message character varying(200) NOT NULL,
    CONSTRAINT template_pkey PRIMARY KEY (id)
);
insert into bdays.templates (id, message) values
(1, 'Happy Birthday'),
(2, 'Congratulations'),
(3, 'С Днем Рождения'),
(4, 'Поздравляем');
