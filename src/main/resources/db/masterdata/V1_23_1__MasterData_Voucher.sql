-- Nhóm sản phẩm/ Sản phẩm áp dụng
INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_product', 'Tất cả sản phẩm trong đơn hàng', 'all', 'Cài mã giảm giá cho toàn bộ sản phẩm của gian hàng. ', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_product', 'Sản phẩm cụ thể cả sản phẩm trong đơn hàng', 'specific_product', 'Cài mã giảm giá cho các sản phẩm Nhà bán muốn giảm giá. Nhà Bán có thể tuỳ chọn sản phẩm, hoặc tải danh sách sản phẩm áp dụng bằng cách: Chọn sản phẩm cụ thể', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_product', 'Nhóm sản phẩm cụ thể', 'specific_category', 'Cài mã giảm giá cho sản phẩm cụ thể, chẳng hạn quần áo, giày dép, ...', 3, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_product', 'Không áp dụng', 'none', 'Không áp dụng mã giảm giá này cho sản phẩm hoặc nhóm sản phẩm.', 4, true);

-- Số lượng sản phẩm

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_quantity', 'Không giới hạn', 'all', 'Không giới hạn số lượng sản phẩm được giảm giá.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_quantity', 'Giới hạn tối đa', 'limit_max', 'Giảm giá tối đa số lượng sản phẩm cố định, chẳng hạn chỉ áp dụng cho 2 sản phẩm đầu tiên.', 2, true);

-- Giá trị đơn hàng tối thiểu

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_order', 'Không giới hạn', 'all', 'Áp dụng Mã giảm giá cho tất cả giá trị đơn hàng.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_order', 'Giá trị đơn hàng tối thiểu', 'limit_min', 'Quy định mức giá đơn hàng được áp dụng mã giảm giá.', 2, true);

-- Nhóm Khách hàng áp dụng

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Tất cả khách hàng', 'all', 'Áp dụng cho tất cả Khách hàng.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Địa chỉ khách hàng', 'limit_location', 'Chỉ áp dụng cho Khách hàng ở khu vực cố định, chẳng hạn TP. HCM.', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Khách hàng thân thiết', 'limit_member', 'Chỉ áp dụng cho khách hàng thân thiết.', 3, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Khách hàng tiềm năng', 'limit_potential_customer', 'Chỉ áp dụng cho khách hàng tiềm năng.', 4, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Khách hàng mới', 'limit_new_customer', 'Chỉ áp dụng cho khách hàng mới.', 5, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_customer', 'Khách hàng cụ thể', 'specific_customer', 'Chỉ áp dụng cho một hoặc một vài khách hàng cụ thể.', 6, true);

-- Số lượng Mã giảm giá

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_coupon', 'Không giới hạn', 'all', 'Không giới hạn số lượng mã giảm giá trong suốt thời gian hiệu lực.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_coupon', 'Giới hạn số lần sử dụng tối đa', 'limit_max', 'Nhà bán nhập số lần sử dụng tối đa cho phép của mã giảm giá.', 2, true);

-- Giới hạn lượt sử dụng mỗi Khách hàng

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_limited_use', 'Không giới hạn', 'all', 'Một Khách hàng có thể dùng bao nhiêu lượt cho mã giảm giá cũng được, không giới hạn.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_limited_use', 'Giới hạn số lần sử dụng mỗi Khách hàng', 'limit_max', 'Nhà bán nhập số lần cho phép một Khách hàng sử dụng mã giảm giá.', 2, true);

-- Phương thức vận chuyển

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_method', 'Không áp dụng', 'none', 'Không áp dụng mã giảm giá cho giảm giá vận chuyển.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_method', 'Tất cả phương thức vận chuyển', 'all', 'Áp dụng mã giảm giá cho giảm giá vận chuyển.', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_method', 'Tự đến lấy tại cửa hàng', 'limit_no_shipping', 'Áp dụng mã giảm giá khi khách hàng tự đến nhận hàng tại cửa hàng.', 3, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_method', 'Giao hàng tận nơi', 'limit_shipping_method', 'Áp dụng mã giảm giá khi khách hàng chọn giao hàng tận nơi.', 4, true);

-- Đơn vị vận chuyển

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_provider', 'Không áp dụng', 'none', 'Không áp dụng mã giảm giá cho giảm giá vận chuyển.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_provider', 'Tất cả đơn vị vận chuyển', 'all', 'Áp dụng mã giảm giá cho giảm giá vận chuyển.', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_shipping_provider', 'Đơn vị vận chuyển xác định', 'limit_shipping_provider', 'Áp dụng mã giảm giá khi khách hàng chọn một hoặc nhiều đơn vị vận chuyển xác định.', 3, true);

-- Hình thức thanh toán

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_payment', 'Tất cả hình thức thanh toán', 'all', 'Áp dụng cho tất cả hình thức thanh toán.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_payment', 'Thánh toán online', 'limit_online', 'Áp dụng mã giảm giá khi khách hàng thanh toán trực tuyến (online).', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_payment', 'Than toán offline (tại cửa hàng)', 'limit_offline', 'Áp dụng mã giảm giá khi khách hàng thanh toán tại cửa hàng (offline).', 3, true);

-- Nơi mua hàng

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_buy', 'Tất cả', 'all', 'Áp dụng cho tất cả hình thức mua hàng bao gồm qua website, qua ứng dụng hay tại cửa hàng.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_buy', 'Mua hàng tại cửa hàng', 'limit_at_store', 'Áp dụng khi mua hàng trực tiếp tại cửa hàng.', 2, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_buy', 'Mua hàng qua website', 'limit_on_website', 'Áp dụng khi mua hàng trực tuyến thông qua wesite.', 3, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_buy', 'Mua hàng qua ứng dụng', 'limit_on_mobile_app', 'Áp dụng khi mua hàng trực tuyến thông qua ứng dụng điện thoại.', 4, true);

-- Áp dụng nhiều mã giảm giá trong một đơn hàng

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_apply_multiple_coupons', 'Không cho phép', 'no', 'Không cho phép áp dụng đồng thời nhiều mã giảm giá trong cùng một đơn hàng.', 1, true);

INSERT INTO voucher_master_data (id, created_at, created_by, updated_at, updated_by, "group", name, key, description, priority, active)
VALUES (nextval('voucher_master_data_seq'), '2021-06-13 10:11:23.000000', 1, '2021-06-13 10:11:25.000000', 1, 'cnd_apply_multiple_coupons', 'Cho phép', 'yes', 'Cho phép áp dụng đồng thời nhiều mã giảm giá trong cùng một đơn hàng.', 2, true);
