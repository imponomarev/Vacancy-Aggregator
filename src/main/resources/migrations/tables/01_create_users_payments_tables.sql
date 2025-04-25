--liquibase formatted sql

--changeset imponomarev:1
create table users
(
    id         bigserial primary key,
    email      varchar(190) not null unique,
    pwd_hash   varchar(255) not null,
    role       varchar(32)  not null, -- "FREE", "PRO"
    created_at timestamp    not null default now()
);

create table payments
(
    id             bigserial primary key,
    user_id        bigint       not null references users (id),
    stripe_session varchar(120) not null unique,
    status         varchar(32)  not null,
    paid_at        timestamp
);
