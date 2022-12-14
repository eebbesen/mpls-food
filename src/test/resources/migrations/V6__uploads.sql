CREATE TABLE uploads (
	id bigserial NOT NULL,
	image bytea NULL,
	verified bool NULL,
	deal_id int8 NOT NULL,
	date_created timestamp NULL,
	last_updated timestamp NULL,
	created_by varchar(255) NULL,
	modified_by varchar(255) NULL,
	CONSTRAINT uploads_pkey PRIMARY KEY (id)
);

ALTER TABLE public.uploads ADD CONSTRAINT fk9cwvw8ja1t9bnou3sdx9o6b6w FOREIGN KEY (deal_id) REFERENCES deals(id);
