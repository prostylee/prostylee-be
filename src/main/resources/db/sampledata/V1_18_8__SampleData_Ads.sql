INSERT INTO advertisement_group (id, created_at, created_by, updated_at, updated_by, name, description, position, active)
VALUES (1, '2021-05-09 20:25:26.000000', 1, '2021-05-09 20:25:28.000000', 1, 'Quảng cáo 1', 'Black Friday', 'main', true);


INSERT INTO advertisement_banner (id, created_at, created_by, updated_at, updated_by, name, description, advertisement_group_id, banner_image, link, "order")
VALUES (1, '2021-05-09 20:27:04.000000', 1, '2021-05-09 20:27:08.000000', 1, 'Flash sale for clothes', 'Flash sale for clothes including men, woment, children, ...', 1, 1, '/black-friday?sale=clothes', 1);

INSERT INTO advertisement_banner (id, created_at, created_by, updated_at, updated_by, name, description, advertisement_group_id, banner_image, link, "order")
VALUES (2, '2021-05-09 20:27:04.000000', 1, '2021-05-09 20:27:08.000000', 2, 'Flash sale for shoes', 'Flash sale for shoes including men, woment, children, ...', 1, 1, '/black-friday?sale=clothes', 2);


INSERT INTO advertisement_campaign (id, created_at, created_by, updated_at, updated_by, name, description, advertisement_group_id, feature_image, position, from_date, to_date, budget, target_id, target_type, target_from_age, target_to_age, target_location_id, target_user_follower, target_user_like)
VALUES (1, '2021-05-09 20:42:34.000000', 1, '2021-05-09 20:42:36.000000', 1, 'Quảng cáo product 1', 'Sản phầm nỗi bật của cửa hàng', 1, 1, 'top-right', '2021-05-09 20:43:20.000000', '2021-07-09 20:43:21.000000', 5000000, 1, 'PRODUCT', 18, 40, 1, true, true);
