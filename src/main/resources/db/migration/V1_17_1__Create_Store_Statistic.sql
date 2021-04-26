create table if not exists store_statistic
(
    id bigint not null constraint store_statistic_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    number_of_product bigint,
    number_of_like bigint,
    number_of_comment bigint,
    number_of_follower bigint,
    number_of_following bigint
);

create sequence store_statistic_seq;