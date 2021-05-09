ALTER TABLE attribute DROP COLUMN category_id;

create table category_attribute
(
    category_id bigint not null
        constraint "fk_category_attribute_category"
            references "category",
    attribute_id bigint not null
        constraint "fk_category_attribute_attribute"
            references "attribute",
    constraint category_attribute_pkey
        primary key (category_id, attribute_id)
);