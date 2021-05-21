ALTER TABLE user_rating ADD COLUMN content varchar(4096);

create table user_rating_attachment
(
    id bigint not null
        constraint pk_user_rating_attachment_id
            primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    user_rating_id bigint,
    attachment_id bigint
);

create sequence if not exists user_rating_attachment_seq;