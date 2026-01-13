CREATE TABLE TENDER (

    id BIGSERIAL PRIMARY KEY,
    external_id varchar(64) NOT NULL,
    title varchar(4096) NOT NULL,
    price varchar(64),
    region varchar(64),
    customer_phone varchar(32),
    customer_email varchar(64),
    customer_name varchar(2048),
    aggregator_url varchar(2048) NOT NULL,
    etp_url varchar(2048),
    published_at timestamp with time zone
);



























