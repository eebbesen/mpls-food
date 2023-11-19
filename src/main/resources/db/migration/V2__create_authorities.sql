CREATE TABLE authorities (
  id bigint NOT NULL,
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  CONSTRAINT authorities_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE public.authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username);


ALTER TABLE authorities
    OWNER to postgres;
