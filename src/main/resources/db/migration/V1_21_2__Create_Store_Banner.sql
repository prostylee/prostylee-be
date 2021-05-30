create table store_banner
(
    id bigint not null constraint pk_store_banner_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    name varchar(512),
    description varchar(4096),
    banner_image bigint, -- attachment_id
    link varchar(2048),
    "order" integer,
	store_id bigint not null CONSTRAINT fk_store_banner REFERENCES store
);

create sequence if not exists store_banner_seq;