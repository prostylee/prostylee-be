INSERT INTO category (id, created_at, created_by, updated_at, updated_by, active, deleted_at, description, icon, name, "order", language_code, parent_id, attachment_id) VALUES (2, '2021-02-04 00:27:54.000000', 1, '2021-02-04 00:27:56.000000', 1, true, null, 'Description for shirt', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/qmmwhyelq5qegzvh8qu4/air-monarch-iv-training-shoe-cc3XbD.jpg', 'Shoe', 1, 'vi', 0, null);
ALTER SEQUENCE attribute_seq RESTART WITH 2;
/** Color */
INSERT INTO attribute (id, created_at, created_by, updated_at, updated_by, description, key, label, "order", type, category_id, language_code) VALUES (nextval('attribute_seq'), '2021-02-28 00:57:13.000000', null, '2021-02-28 00:57:17.000000', null, 'Color', 'color', 'Color', 1, 0, 2, 'vi');
/** Size */
INSERT INTO attribute (id, created_at, created_by, updated_at, updated_by, description, key, label, "order", type, category_id, language_code) VALUES (nextval('attribute_seq'), '2021-02-28 00:57:13.000000', null, '2021-02-28 00:57:17.000000', null, 'Size', 'size', 'Size', 2, 0, 2, 'vi');
/** Status */
INSERT INTO attribute (id, created_at, created_by, updated_at, updated_by, description, key, label, "order", type, category_id, language_code) VALUES (nextval('attribute_seq'), '2021-02-28 00:57:13.000000', null, '2021-02-28 00:57:17.000000', null, 'Status', 'status', 'Status', 3, 0, 2, 'vi');
/** Material */
INSERT INTO attribute (id, created_at, created_by, updated_at, updated_by, description, key, label, "order", type, category_id, language_code) VALUES (nextval('attribute_seq'), '2021-02-28 00:57:13.000000', null, '2021-02-28 00:57:17.000000', null, 'Material', 'material', 'Material', 4, 0, 2, 'vi');
/** Style */
INSERT INTO attribute (id, created_at, created_by, updated_at, updated_by, description, key, label, "order", type, category_id, language_code) VALUES (nextval('attribute_seq'), '2021-02-28 00:57:13.000000', null, '2021-02-28 00:57:17.000000', null, 'Style', 'style', 'Style', 5, 0, 2, 'vi');
