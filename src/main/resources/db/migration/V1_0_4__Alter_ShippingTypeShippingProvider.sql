ALTER TABLE IF EXISTS shipping_type RENAME TO shipping_method;

ALTER TABLE IF EXISTS shipping_method rename constraint shipping_type_pkey to shipping_method_pkey;

ALTER INDEX IF EXISTS shipping_type_pkey RENAME TO shipping_method_pkey;

DROP SEQUENCE IF EXISTS shipping_type_seq;
CREATE SEQUENCE if not exists shipping_method_seq;


DROP TABLE IF EXISTS shipping_address;

create table if not exists shipping_provider
(
    id bigint not null
    constraint shipping_provider_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    name varchar(512),
    price double precision,
    shipping_method_id bigint not null constraint "FK_shipping_method_shipping_provider" references shipping_method
);

CREATE SEQUENCE IF NOT EXISTS shipping_provider_seq;
