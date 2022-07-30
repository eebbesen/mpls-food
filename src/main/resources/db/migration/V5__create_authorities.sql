create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
)

TABLESPACE pg_default;

ALTER TABLE public.authorities
    OWNER to postgres;