ALTER TABLE shipping_type RENAME TO shipping_method;

create table if not exists shipping_provider
(
    id bigint not null
    constraint shipping_provider_pkey primary key,
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    description varchar(512),
    name varchar(512),
    price double precision
);

alter table shipping_provider owner to postgres;

insert into shipping_provider (id, created_at, description, name, price) values (10010, '2020-12-11 16:09:49', 'Grab Express', 'GRAB', 25000);
insert into shipping_provider (id, created_at, description, name, price) values (20010, '2020-12-11 16:10:49', 'VN Express', 'VNE', 30000);
insert into shipping_provider (id, created_at, description, name, price) values (30010, '2020-12-11 16:11:49', 'Viettel Post', 'VTP', 25000);
insert into shipping_provider (id, created_at, description, name, price) values (40010, '2020-12-11 16:12:49', 'Giao hang tiet kiem', 'GHTK', 25000);
insert into shipping_provider (id, created_at, description, name, price) values (50010, '2020-12-11 16:13:49', 'Ninja van', 'NJV', 27000);








