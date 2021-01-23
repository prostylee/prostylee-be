create table if not exists story
(
    id bigint not null constraint story_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    deleted_at timestamp,
    target_id bigint not null,
    target_type  varchar(512) not null,
    product_id bigint
);

create table if not exists story_image
(
    id bigint not null constraint story_image_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    attachment_id bigint not null,
    "order" integer,
    story_id bigint not null constraint "FK_story_story_image" references story
);