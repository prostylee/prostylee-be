create table used_status
(
    id bigint not null
        constraint used_status_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    name varchar (512)
);

ALTER TABLE product ADD COLUMN used_status_id bigint;

create sequence if not exists used_status_seq;
