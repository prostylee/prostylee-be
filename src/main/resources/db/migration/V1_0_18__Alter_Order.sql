create table if not exists "order_discount"
(
    id bigint not null
        constraint order_discount_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    voucher_id bigint,
    amount double precision,
    description varchar(512),
    order_id bigint not null
        constraint "fk_discount_order"
            references "order"
);

create table if not exists "order_detail"
(
    id bigint not null
        constraint order_detail_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    store_id bigint not null
        constraint "fk_order_detail_store"
            references "store",
    product_id bigint not null
        constraint "fk_order_detail_product"
            references "product",
    product_price double precision,
    amount int,
    product_name varchar(512),
    product_image varchar(512),
    product_color varchar(512),
    product_size varchar(512),
    product_data varchar(4096),
    order_id bigint not null
        constraint "fk_order_detail_order"
            references "order"
);

create table if not exists "shipping_address"
(
    id bigint not null
        constraint shipping_address_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    fullname varchar(512),
    email varchar(512),
    phone_number varchar(512),
    address1 varchar(512),
    address2 varchar(512),
    state varchar(512),
    city varchar(512),
    country varchar(512),
    zipcode varchar(512)
);

create sequence if not exists order_discount_seq;

create sequence if not exists order_detail_seq;

create sequence if not exists shipping_address_seq;

ALTER TABLE "order" DROP COLUMN price;
ALTER TABLE "order" DROP COLUMN product_id;
ALTER TABLE "order" DROP COLUMN product_price_id;
ALTER TABLE "order" DROP COLUMN seller_id;
ALTER TABLE "order" DROP COLUMN store_id;
ALTER TABLE "order" DROP COLUMN shipping_type;
ALTER TABLE "order" ADD COLUMN total_money double precision;
ALTER TABLE "order" ADD COLUMN shipping_address_id bigint;
ALTER TABLE "order" ADD COLUMN shipping_provider_id bigint;
ALTER TABLE "order" RENAME COLUMN payment_type TO payment_type_id;
ALTER TABLE "order" ALTER COLUMN buyer_id TYPE bigint;
ALTER TABLE "order" ALTER COLUMN payment_type_id TYPE bigint;
