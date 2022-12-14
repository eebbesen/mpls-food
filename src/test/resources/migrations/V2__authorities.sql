CREATE TABLE authorities (
	username varchar(50) NOT NULL,
	authority varchar(50) NOT NULL,
	id bigserial NOT NULL
);

ALTER TABLE public.authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username);
