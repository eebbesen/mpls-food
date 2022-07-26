CREATE TABLE IF NOT EXISTS public.deal
(
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    days_of_week character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default" NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    place_id bigint NOT NULL,
    CONSTRAINT deal_pkey PRIMARY KEY (id),
    CONSTRAINT fkk1rh0cs2pfglygo68bv3bw1na FOREIGN KEY (place_id)
        REFERENCES public.place (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.deal
    OWNER to postgres;
