create table if not exists post_statistic
(
    id bigint not null constraint post_statistic_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    number_of_like bigint,
    number_of_comment bigint
);
