--liquibase formatted sql
--changeset imponomarev:6

CREATE TABLE user_resume_experience
(
    favorite_id  BIGINT NOT NULL,
    company      VARCHAR(256),
    exp_position VARCHAR(256),
    start_date   TIMESTAMPTZ,
    end_date     TIMESTAMPTZ,
    description  TEXT,
    CONSTRAINT fk_resume_exp_to_favorite
        FOREIGN KEY (favorite_id)
            REFERENCES user_resume_favorites (id)
            ON DELETE CASCADE
);

--rollback DROP TABLE user_resume_experience;
