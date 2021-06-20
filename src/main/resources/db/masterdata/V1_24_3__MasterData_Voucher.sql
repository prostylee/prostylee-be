DELETE FROM voucher_master_data v
WHERE v.key IN ('limit_no_shipping', 'limit_shipping_method');

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_method', 'Phương thức vận chuyển xác định', 'limit_shipping_method', 'Áp dụng mã giảm giá khi khách hàng chọn một hoặc nhiều phương thức vận chuyển xác định.', 3, true);
