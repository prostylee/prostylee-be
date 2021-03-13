insert into shipping_method (id, created_at, description, name)
values (1, '2020-12-11 16:09:49', 'Giao hàng tận nơi thông qua đơn vị vận chuyển', 'Giao hàng tận nơi');

insert into shipping_method (id, created_at, description, name)
values (2, '2020-12-11 16:09:49', 'Quý khách sẽ đến nhận tại cửa hàng', 'Nhận tại cửa hàng');

ALTER SEQUENCE shipping_method_seq RESTART WITH 3;
