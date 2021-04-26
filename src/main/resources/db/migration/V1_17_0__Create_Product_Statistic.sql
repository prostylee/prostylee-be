create table if not exists product_statistic
(
    id bigint not null constraint product_statistic_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    number_of_sold bigint,
    number_of_like bigint,
    number_of_comment bigint
);

create sequence product_statistic_seq;