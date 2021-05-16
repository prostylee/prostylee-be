create table branch
(
    id bigint not null constraint pk_branch_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    deleted_at timestamp,
    name varchar(512),
    description varchar(4096),
    location_id bigint,
    active boolean default true,
    store_id bigint not null
        constraint "fk_branch_store_id"
            references store
);
create sequence if not exists branch_seq;

ALTER table order_detail ADD COLUMN branch_id bigint;