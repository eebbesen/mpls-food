CREATE TABLE deal_log (
	id bigserial NOT NULL,
	created_by varchar(255) NULL,
	date_created timestamp NULL,
	last_updated timestamp NULL,
	modified_by varchar(255) NULL,
	deal_type int4 NULL,
	description text NOT NULL,
	redeemed bool NULL,
	redemption_date date NULL,
	deal_id int8 NULL,
	place_id int8 NULL,
	CONSTRAINT deal_log_pkey PRIMARY KEY (id)
);

ALTER TABLE public.deal_log ADD CONSTRAINT fk_deal_log_deals FOREIGN KEY (deal_id) REFERENCES deals(id);
ALTER TABLE public.deal_log ADD CONSTRAINT fk_deal_log_places FOREIGN KEY (place_id) REFERENCES places(id);