create table push_notification_template
(
    id bigint not null constraint pk_push_notification_template_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    title varchar(256),
    content varchar(4096),
    type varchar(128)
);
create sequence if not exists push_notification_template_seq;