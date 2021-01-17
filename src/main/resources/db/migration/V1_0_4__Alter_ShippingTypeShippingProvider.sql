ALTER TABLE IF EXISTS shipping_type RENAME TO shipping_method;
alter table IF EXISTS shipping_method rename constraint shipping_type_pkey to shipping_method_pkey;
alter index IF EXISTS shipping_type_pkey rename to shipping_method_pkey;
DROP TABLE IF EXISTS shipping_address;
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
    price double precision,
    shipping_method_id bigint not null constraint "FK_shipping_method_shipping_provider" references shipping_method
);

insert into shipping_method (id, created_at, description, name) values (1, '2020-12-11 16:09:49', 'Grab Express', 'GRAB');

insert into shipping_provider (id, created_at, description, name, price, shipping_method_id) values (10010, '2020-12-11 16:09:49', 'Grab Express', 'GRAB', 25000, 1);
insert into shipping_provider (id, created_at, description, name, price, shipping_method_id) values (20010, '2020-12-11 16:10:49', 'VN Express', 'VNE', 30000, 1);
insert into shipping_provider (id, created_at, description, name, price, shipping_method_id) values (30010, '2020-12-11 16:11:49', 'Viettel Post', 'VTP', 25000, 1);
insert into shipping_provider (id, created_at, description, name, price, shipping_method_id) values (40010, '2020-12-11 16:12:49', 'Giao hang tiet kiem', 'GHTK', 25000 , 1) ;
insert into shipping_provider (id, created_at, description, name, price, shipping_method_id) values (50010, '2020-12-11 16:13:49', 'Ninja van', 'NJV', 27000, 1);








