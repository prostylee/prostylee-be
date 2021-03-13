insert into shipping_provider (id, created_at, description, name, price, shipping_method_id)
values (10010, '2020-12-11 16:09:49', 'Grab Express', 'GRAB', 25000, 2);

insert into shipping_provider (id, created_at, description, name, price, shipping_method_id)
values (20010, '2020-12-11 16:10:49', 'VN Express', 'VNE', 30000, 2);

insert into shipping_provider (id, created_at, description, name, price, shipping_method_id)
values (30010, '2020-12-11 16:11:49', 'Viettel Post', 'VTP', 25000, 2);

insert into shipping_provider (id, created_at, description, name, price, shipping_method_id)
values (40010, '2020-12-11 16:12:49', 'Giao hang tiet kiem', 'GHTK', 25000 , 2) ;

insert into shipping_provider (id, created_at, description, name, price, shipping_method_id)
values (50010, '2020-12-11 16:13:49', 'Ninja van', 'NJV', 27000, 2);

ALTER SEQUENCE shipping_provider_seq RESTART WITH 60010;






