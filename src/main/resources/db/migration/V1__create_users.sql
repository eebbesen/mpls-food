CREATE TABLE users (
	id bigint NOT NULL,
	username varchar(50) NOT NULL,
	"password" varchar(500) NOT NULL,
	enabled bool NOT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (username)
);

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;
