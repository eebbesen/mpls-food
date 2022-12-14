CREATE TABLE users (
	username varchar(50) NOT NULL,
	"password" varchar(500) NOT NULL,
	enabled bool NOT NULL,
	id bigserial NOT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (username)
);
