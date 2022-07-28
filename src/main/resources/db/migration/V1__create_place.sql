CREATE TABLE IF NOT EXISTS public.place
(
    id bigint NOT NULL,
    address text COLLATE pg_catalog."default" NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT place_pkey PRIMARY KEY (id),
    CONSTRAINT uk_b8nbstg9t15unnmk6uod41f47 UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE public.place
    OWNER to postgres;
