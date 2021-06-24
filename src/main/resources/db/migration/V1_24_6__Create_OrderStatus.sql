create table order_status
(
    id bigint not null
        constraint order_status_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    active boolean,
    deleted_at timestamp,
    description varchar(4096),
    name varchar(512),
    act_code integer,
    step integer,
    language_code varchar(50) default 'vi',
    "group" varchar(64)
);

create sequence order_status_seq;