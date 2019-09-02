CREATE TABLE bdays.users
(
    id integer NOT NULL,
    user_name character varying(100),
    login character varying(50) NOT NULL UNIQUE,
    password character varying(100) NOT NULL,
    last_activity date,
    active boolean,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)