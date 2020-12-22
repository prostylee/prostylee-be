create table app_client
(
    id bigint not null
        constraint app_client_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    active boolean,
    callback_url varchar(2048),
    description varchar(512),
    name varchar(128),
    secret_key varchar(512),
    type varchar(128)
);

create table attachment
(
    id bigint not null
        constraint attachment_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    display_name varchar(512),
    name varchar(512),
    path varchar(2048),
    size_in_kb bigint,
    thumbnail varchar(2048),
    type varchar(512)
);

create table brand
(
    id bigint not null
        constraint brand_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(4096),
    name varchar(512),
    user_id bigint
);

create table category
(
    id bigint not null
        constraint category_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    active boolean,
    deleted_at timestamp,
    description varchar(4096),
    icon varchar(512),
    name varchar(512),
    "order" integer
);

create table attribute
(
    id bigint not null
        constraint attribute_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    key varchar(512),
    label varchar(128),
    "order" integer,
    type integer,
    category_id bigint not null
        constraint "FK8xaeml4v57nyhu2olsqgwg4ak"
            references category
);

create table attribute_option
(
    id bigint not null
        constraint attribute_option_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    label varchar(512),
    name varchar(512),
    value varchar(512),
    attribute_id bigint not null
        constraint "FKolmfrc7trisieb2trwyv6gs5j"
            references attribute
);

create table comment
(
    id bigint not null
        constraint comment_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    content varchar(512),
    parent_id bigint,
    target_id bigint not null,
    target_type varchar(512)
);

create table comment_image
(
    id bigint not null
        constraint comment_image_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    attachment_id bigint,
    "order" integer,
    comment_id bigint not null
        constraint "FKqxojxrgk8n96u5n6iix54563s"
            references comment
);

create table company
(
    id bigint not null
        constraint company_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    active boolean,
    description varchar(4096),
    name varchar(512),
    owner_id bigint
);

create table email_template
(
    id bigint not null
        constraint email_template_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    content varchar(15000),
    template_file varchar(512),
    title varchar(512),
    type varchar(128)
);

create table feature
(
    id bigint not null
        constraint feature_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    api_path varchar(512),
    code varchar(128),
    "group" varchar(128),
    name varchar(128),
    "order" integer
);

create table location
(
    id bigint not null
        constraint location_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    address varchar(512),
    city varchar(512),
    country varchar(512),
    latlng varchar(512),
    state varchar(512),
    zipcode varchar(512)
);

create table metadata
(
    id bigint not null
        constraint metadata_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    code varchar(128),
    content varchar(65556)
);

create table notification
(
    id bigint not null
        constraint notification_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    additional_data varchar(4096),
    content varchar(4096),
    mark_as_read boolean,
    title varchar(512),
    type varchar(128),
    user_id bigint
);

create table "order"
(
    id bigint not null
        constraint order_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    buyer_id varchar(512),
    code varchar(512),
    payment_type varchar(512),
    price varchar(512),
    product_id varchar(512),
    product_price_id varchar(512),
    seller_id bigint,
    shipping_type bigint,
    status varchar(512),
    store_id bigint
);

create table payment_type
(
    id bigint not null
        constraint payment_type_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    name varchar(512)
);

create table product
(
    id bigint not null
        constraint product_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    deleted_at timestamp,
    description varchar(512),
    location_id varchar(512),
    name varchar(512),
    price double precision,
    price_sale double precision,
    published_date timestamp,
    store_id bigint,
    brand_id bigint not null
        constraint "FKh2s69uoa44voy2jsqov3oid7r"
            references brand,
    category_id bigint not null
        constraint "FK7l29ekt1x29jup80y2iigimyy"
            references category
);

create table product_image
(
    id bigint not null
        constraint product_image_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    attachment_id bigint,
    "order" integer,
    product_id bigint not null
        constraint "FK404iut26wg4pq85osw3vn0kwd"
            references product
);

create table product_price
(
    id bigint not null
        constraint product_price_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    name varchar(512),
    price double precision,
    price_sale double precision,
    sku varchar(512),
    product_id bigint not null
        constraint "FKg5vp5ubh91bqfghwmsqmm96qg"
            references product
);

create table product_attribute
(
    id bigint not null
        constraint product_attribute_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    attr_value varchar(512),
    attr_id bigint not null
        constraint "FKdn771njw065udb6ll0uftms38"
            references attribute,
    product_price_id bigint not null
        constraint "FKq41pogw2rb1fkfddhfxip4s5o"
            references product_price
);

create table push_notification_token
(
    id bigint not null
        constraint push_notification_token_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    brand varchar(256),
    device_id varchar(256),
    device_name varchar(256),
    manufacturer varchar(256),
    model_name varchar(256),
    os_name varchar(512),
    os_version varchar(256),
    software varchar(256),
    store_id bigint,
    token varchar(512),
    user_id bigint
);

create table role
(
    id bigint not null
        constraint role_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    code varchar(64),
    name varchar(512)
);

create table feature_role
(
    role_id bigint not null
        constraint "FKecdkmwin2ib8jlfd60a9mr2b5"
            references role,
    feature_id bigint not null
        constraint "FKcknn5ikhe0rq88mumjt0tcouf"
            references feature,
    constraint feature_role_pkey
        primary key (feature_id, role_id)
);

create table shipping_address
(
    id bigint not null
        constraint shipping_address_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    address1 varchar(512),
    address2 varchar(512),
    city varchar(512),
    country varchar(512),
    email varchar(512),
    fullname varchar(512),
    phone_number varchar(512),
    state varchar(512),
    zipcode varchar(512),
    order_id bigint not null
        constraint "FKo6xa2e2q73h0ljij12921k5t7"
            references "order"
);

create table shipping_type
(
    id bigint not null
        constraint shipping_type_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    name varchar(512)
);

create table store
(
    id bigint not null
        constraint store_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    address varchar(512),
    deleted_at timestamp,
    location_id bigint,
    name varchar(512),
    owner_id bigint,
    phone varchar(512),
    status integer,
    website varchar(512),
    company_id bigint not null
        constraint "FKj4vy55rdnnioae3ogbbsmu5g0"
            references company
);

create table "user"
(
    id bigint not null
        constraint user_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    active boolean,
    allow_notification boolean,
    avatar varchar(255),
    dob_date integer,
    deleted_at timestamp,
    email varchar(512)
        constraint "unique_user_email"
            unique,
    full_name varchar(512),
    gender char,
    location_id varchar(512),
    dob_month integer,
    password varchar(128),
    phone_number varchar(32),
    username varchar(128),
    dob_year integer
);

create table user_follower
(
    id bigint not null
        constraint user_follower_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    target_id bigint,
    target_type varchar(512),
    user_id bigint
);

create table user_like
(
    id bigint not null
        constraint user_like_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    product_id bigint,
    user_id bigint
);

create table user_link_account
(
    id bigint not null
        constraint user_link_account_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    provider_id varchar(45),
    provider_name varchar(45),
    user_id bigint not null
        constraint "FKoncs5ymuulwi5v4iipxtovo07"
            references "user"
);

create table user_rating
(
    id bigint not null
        constraint user_rating_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    target_id bigint,
    target_type varchar(512),
    value integer
);

create table user_role
(
    user_id bigint not null
        constraint "FKhjx9nk20h4mo745tdqj8t8n9d"
            references "user",
    role_id bigint not null
        constraint "FKka3w3atry4amefp94rblb52n7"
            references role,
    constraint user_role_pkey
        primary key (role_id, user_id)
);

create table user_store
(
    id bigint not null
        constraint user_store_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    store_id bigint not null,
    user_id bigint not null
);

create table user_temp
(
    id bigint not null
        constraint user_temp_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    expired_at timestamp,
    password varchar(128),
    username varchar(128)
);

create table user_tracking
(
    id bigint not null
        constraint user_tracking_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    category_id bigint,
    product_id bigint,
    search_keyword varchar(512),
    store_id bigint
);




create sequence app_client_seq;

create sequence attachment_seq;

create sequence attribute_option_seq;

create sequence attribute_seq;

create sequence brand_seq;

create sequence category_seq;

create sequence comment_image_seq;

create sequence comment_seq;

create sequence company_seq;

create sequence email_template_seq;

create sequence feature_seq;

create sequence location_seq;

create sequence metadata_seq;

create sequence notification_seq;

create sequence order_seq;

create sequence payment_type_seq;

create sequence product_attribute_seq;

create sequence product_image_seq;

create sequence product_price_seq;

create sequence product_seq;

create sequence push_notification_token_seq;

create sequence role_seq;

create sequence shipping_address_seq;

create sequence shipping_type_seq;

create sequence store_seq;

create sequence user_follower_seq;

create sequence user_like_seq;

create sequence user_link_account_seq;

create sequence user_rating_seq;

create sequence user_seq;

create sequence user_store_seq;

create sequence user_temp_seq;

create sequence user_tracking_seq;

