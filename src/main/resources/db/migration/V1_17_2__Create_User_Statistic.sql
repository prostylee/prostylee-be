create table if not exists user_statistic
(
    id bigint not null constraint user_statistic_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    number_of_product bigint,
    number_of_story bigint,
    number_of_post bigint,
    number_of_like bigint,
    number_of_comment bigint,
    number_of_follower bigint,
    number_of_following bigint
);

create sequence user_statistic_seq;