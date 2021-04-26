INSERT INTO "order" (id, created_at, created_by, updated_at, updated_by, code, total_money, shipping_address_id, shipping_provider_id, buyer_id, payment_type_id, status)
VALUES (nextval('order_seq'), '2021-04-23 23:59:52.000000', 1, '2021-04-23 23:59:55.000000', 1, '2020-1', 899000, 1, 1, 1, 1, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 1, 499000, 1, 'Ao thun nam 1', null, 'RED', 'XL', null, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 2, 399000, 1, 'Ao thun nam 2', null, 'RED', 'XL', null, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 3, 499000, 1, 'Ao thun nam 3', null, 'RED', 'XL', null, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 4, 399000, 1, 'Ao thun nam 4', null, 'RED', 'XL', null, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 3, 499000, 1, 'Ao thun nam 1', null, 'RED', 'XL', null, 1);

INSERT INTO order_detail (id, created_at, created_by, updated_at, updated_by, store_id, product_id, product_price, amount, product_name, product_image, product_color, product_size, product_data, order_id)
VALUES (nextval('order_detail_seq'), '2021-04-24 00:01:28.000000', 1, '2021-04-24 00:01:30.000000', 1, 1, 2, 399000, 1, 'Ao thun nam 2', null, 'RED', 'XL', null, 1);



