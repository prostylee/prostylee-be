create table product_payment_type
(
    id bigint not null
        constraint product_payment_type_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    product_id bigint not null constraint "FK_product_product_payment_type" references product,
    payment_type_id bigint not null constraint "FK_payment_type_product_payment_type" references payment_type
);

create sequence if not exists product_payment_type_seq;
