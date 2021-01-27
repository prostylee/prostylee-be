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

create sequence story_seq;

create sequence story_image_seq;

insert into story (id, created_at, created_by, updated_at, updated_by, deleted_at, target_id, target_type, product_id) values (nextval('story_seq'), '2021-01-27 22:49:16.995000', 1, '2021-01-27 22:49:16.995000', 1, null, 1, 'user', 1);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:49:17.021000', 1, '2021-01-27 22:49:17.021000', 1, 2, 1, 1);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:49:17.025000', 1, '2021-01-27 22:49:17.025000', 1, 1, 2, 1);

insert into story (id, created_at, created_by, updated_at, updated_by, deleted_at, target_id, target_type, product_id) values (nextval('story_seq'), '2021-01-27 22:50:01.177000', 1, '2021-01-27 22:50:01.177000', 1, null, 2, 'user', 1);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:50:01.180000', 1, '2021-01-27 22:50:01.180000', 1, 3, 1, 2);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:50:01.182000', 1, '2021-01-27 22:50:01.182000', 1, 4, 2, 2);

insert into story (id, created_at, created_by, updated_at, updated_by, deleted_at, target_id, target_type, product_id) values (nextval('story_seq'), '2021-01-27 22:50:24.283000', 1, '2021-01-27 22:50:24.283000', 1, null, 2, 'store', 1);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:50:24.286000', 1, '2021-01-27 22:50:24.286000', 1, 3, 1, 3);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-01-27 22:50:24.288000', 1, '2021-01-27 22:50:24.288000', 1, 4, 2, 3);



