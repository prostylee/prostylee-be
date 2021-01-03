create table language
(
    id bigint not null
        constraint language_pkey
            primary key,
    code varchar(50),
    name varchar(2048),
     created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint
);