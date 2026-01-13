CREATE TABLE subscriptions (
                               id BIGSERIAL PRIMARY KEY,
                               chat_id BIGINT NOT NULL UNIQUE,
                               is_active BOOLEAN NOT NULL DEFAULT true
);