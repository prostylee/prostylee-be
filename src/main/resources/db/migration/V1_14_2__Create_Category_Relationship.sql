create table category_relationship
(
    id bigint not null
        constraint category_relationship_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    category_id_1 bigint,
    category_id_2 bigint
);
create sequence if not exists category_relationship_seq;