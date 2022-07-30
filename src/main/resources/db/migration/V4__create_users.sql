create table users(
	username varchar(50) not null primary key,
	password varchar(500) not null,
	enabled boolean not null
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;