ALTER TABLE tender
    ALTER COLUMN region TYPE VARCHAR(1024);

ALTER TABLE tender
    ADD COLUMN tender_stage varchar(128)