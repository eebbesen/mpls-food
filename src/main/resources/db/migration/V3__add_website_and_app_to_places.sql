ALTER TABLE place
ADD COLUMN website text COLLATE pg_catalog."default",
ADD COLUMN app boolean,
ADD COLUMN order_ahead boolean
