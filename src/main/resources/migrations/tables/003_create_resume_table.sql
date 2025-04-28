--liquibase formatted sql
--changeset imponomarev:4
CREATE TABLE user_resumes
(
    id          bigserial PRIMARY KEY,
    user_id     bigint       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    source      varchar(8)   NOT NULL, -- hh | sj | avito
    external_id varchar(64)  NOT NULL,
    title       varchar(512) NOT NULL,
    city        varchar(128),
    updated_at  timestamp,
    created_at  timestamp DEFAULT now(),
    CONSTRAINT uq_user_resumes UNIQUE (user_id, source, external_id)
);
--rollback DROP TABLE user_resumes;