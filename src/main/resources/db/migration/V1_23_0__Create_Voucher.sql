create table voucher
(
    id bigint not null constraint pk_voucher_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    store_id bigint,
    name varchar(512),
    description varchar(4096),
    code varchar(20),
    active boolean,
    type integer,
    discount_amount double precision,
    discount_max_amount double precision,
    discount_percent integer,
    cnd_product_type integer,
    cnd_product_prod_ids varchar(2048),
    cnd_product_cat_ids varchar(2048),
    cnd_order_amount_type integer,
    cnd_order_amount_min_value double precision,
    cnd_quantity_type integer,
    cnd_quantity_max_value integer,
    cnd_customer_type integer,
    cnd_customer_user_ids varchar(2048),
    cnd_customer_location_ids varchar(2048),
    cnd_coupon_quantity_type integer,
    cnd_coupon_quantity_max_value integer,
    cnd_limited_use_type integer,
    cnd_limited_use_max_value integer,
    cnd_valid_from timestamp,
    cnd_valid_to timestamp,
    cnd_shipping_method_type integer,
    cnd_shipping_provider_type integer,
    cnd_shipping_provider_ids varchar(2048),
    cnd_payment_type integer,
    cnd_buy_type integer,
    cnd_apply_multiple_coupons boolean,
    deleted_at timestamp
);
create sequence if not exists voucher_seq;

create table voucher_master_data
(
    id bigint not null constraint pk_voucher_master_data_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    "group" varchar(64),
    name varchar(512),
    "key" varchar(64),
    description varchar(4096),
    priority integer,
    active boolean
);
create sequence if not exists voucher_master_data_seq;

create table voucher_user
(
    id bigint not null constraint pk_voucher_user_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    voucher_id bigint,
    voucher_code varchar(20),
    store_id bigint,
    order_id bigint,
    used_at timestamp,
    discount_amount double precision
);
create sequence if not exists voucher_user_seq;