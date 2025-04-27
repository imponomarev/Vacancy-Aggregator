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
--rollback drop table users

create table payments
(
    id            bigserial primary key,
    user_id       bigint      not null references users (id),
    yk_payment_id varchar(64) not null,
    amount        integer     not null default 0,
    status        varchar(32) not null,
    paid_at       timestamp
);
--rollback drop table payments

