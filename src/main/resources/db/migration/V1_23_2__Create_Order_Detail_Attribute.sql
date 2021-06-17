create table if not exists order_detail_attribute
(
    id bigint not null
        constraint order_detail_attribute_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    key varchar(512),
    label varchar(128),
    value varchar(512),
    order_detail_id bigint not null CONSTRAINT fk_order_detail_attribute REFERENCES order_detail
);
create sequence if not exists order_detail_attribute_seq;

ALTER TABLE order_detail DROP CONSTRAINT if exists fk_order_detail_store;
ALTER TABLE order_detail DROP CONSTRAINT if exists fk_order_detail_product;
