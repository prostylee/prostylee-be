create table advertisement_group
(
    id bigint not null constraint pk_advertisement_banner_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    name varchar(512),
    description varchar(4096),
    position varchar(128),
    active boolean
);
create sequence if not exists advertisement_banner_seq;


create table advertisement_banner
(
    id bigint not null constraint pk_advertisement_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    name varchar(512),
    description varchar(4096),
    advertisement_group_id bigint,
    banner_image bigint, -- attachment_id
    link varchar(2048),
    "order" integer
);
create sequence if not exists advertisement_seq;


create table advertisement_campaign
(
    id bigint not null constraint pk_advertisement_campaign_id primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    name varchar(512),
    description varchar(4096),
    advertisement_group_id bigint,
    feature_image bigint, -- attachment_id
    position varchar(128),
    from_date timestamp,
    to_date timestamp,
    budget double precision,
    target_id bigint,
    target_type varchar(512),
    target_from_age integer,
    target_to_age integer,
    target_location_id bigint,
    target_user_follower boolean,
    target_user_like boolean
);
create sequence if not exists advertisement_campaign_seq;