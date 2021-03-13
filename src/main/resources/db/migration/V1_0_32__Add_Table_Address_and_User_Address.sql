create table address
(
    id bigint not null constraint address_pkey primary key,
    code varchar(20) not null,
    parent_code varchar(20) null,
    name varchar(512) not null,
    "order" integer default 0,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint
);

create table user_address
(
    id bigint not null constraint user_address_pkey primary key,
    city_code varchar(20) not null,
    district_code varchar(20) not null,
    ward_code varchar(20) not null,
    address varchar(512) not null,
    full_address varchar(512) not null,
    priority boolean,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    user_id bigint not null constraint "fk_user_address_user" references "user"
);

ALTER TABLE "user" ADD COLUMN bio varchar(512);

create sequence address_seq;
create sequence user_address_seq;