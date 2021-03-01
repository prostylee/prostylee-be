create table post
(
    id bigint not null
        constraint post_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(4096),
    store_id bigint
);

create table post_image
(
    id bigint not null
        constraint post_image_pkey
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    attachment_id bigint,
    "order" integer,
    post_id bigint not null
        constraint "FK_post_post_image"
            references post
);

create sequence if not exists post;

create sequence if not exists post_image;