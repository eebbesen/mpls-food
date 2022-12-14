CREATE TABLE IF NOT EXISTS public.deals
(
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    description text COLLATE pg_catalog."default" NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    place_id bigint NOT NULL,
    dish text NULL,
    CONSTRAINT deal_pkey PRIMARY KEY (id),
    CONSTRAINT fk_deals_places FOREIGN KEY (place_id)
        REFERENCES public.place (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.deals
    OWNER to postgres;
