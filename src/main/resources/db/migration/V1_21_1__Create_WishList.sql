create table user_wish_list
(
    id bigint not null
        constraint user_wish_list_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    deleted_at timestamp,
    product_id bigint
);
create sequence if not exists user_wish_list_seq;
