--liquibase formatted sql
--changeset imponomarev:4

create table user_favorites
(
    id           bigserial primary key,
    user_id      bigint        not null references users (id) on delete cascade,
    source       varchar(8)    not null, -- "hh" | "sj" | "avito"
    external_id  varchar(64)   not null,
    title        varchar(512)  not null,
    company      varchar(256),
    city         varchar(128),
    salary_from  int,
    salary_to    int,
    currency     varchar(8),
    published_at timestamp,
    created_at   timestamp default now(),
    url          varchar(1024) not null,
    constraint uq_user_fav unique (user_id, source, external_id)
);
--rollback drop table user_favorites