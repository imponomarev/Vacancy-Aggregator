--liquibase formatted sql
--changeset imponomarev:5

CREATE TABLE user_resume_favorites
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    source            VARCHAR(16) NOT NULL,
    external_id       VARCHAR(64) NOT NULL,
    first_name        VARCHAR(128),
    last_name         VARCHAR(128),
    position          VARCHAR(256),
    salary            INT,
    currency          VARCHAR(8),
    city              VARCHAR(128),
    updated_at        TIMESTAMPTZ,
    url               TEXT,
    age               INT,
    experience_months INT,
    gender            TEXT,
    education_level   TEXT,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, source, external_id)
);
--rollback DROP TABLE user_resumes;