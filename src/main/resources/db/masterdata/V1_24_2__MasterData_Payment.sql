DELETE FROM payment_type WHERE 1 = 1;

INSERT INTO payment_type (id, created_at, created_by, updated_at, updated_by, description, name)
VALUES (1, '2021-02-08 16:53:22.000000', 1, '2021-02-08 16:53:23.000000', 1, 'Thanh toán trực tuyến', 'ONLINE');

INSERT INTO payment_type (id, created_at, created_by, updated_at, updated_by, description, name)
VALUES (2, '2021-02-08 16:53:22.000000', 1, '2021-02-08 16:53:23.000000', 1, 'Thanh toán khi nhận hàng', 'OFFLINE');

ALTER SEQUENCE shipping_method_seq RESTART WITH 3;