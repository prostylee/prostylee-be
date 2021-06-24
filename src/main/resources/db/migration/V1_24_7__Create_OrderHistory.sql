create table order_history
(
    id bigint not null
        constraint order_history_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    deleted_at timestamp,
    status_name varchar(512),
    act_code integer,
    step integer,
    completed_status integer,
    order_id bigint not null
        constraint fk_order_history_order
            references product,
    status_id bigint not null
        constraint fk_order_history_status
            references order_status
);

create sequence order_history_seq;