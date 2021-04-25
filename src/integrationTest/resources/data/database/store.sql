DELETE FROM store;
DELETE FROM company;

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(1, 'Prostylee Company 1', 'This is a description', 1, 1, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(2, 'Prostylee Company 2', 'This is a description', 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (1, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 01', 'This is a description', '01 Ly Tu Trong', 'https://prostylee01.vn', '0988000111', 1, 1, 1, '2020-12-12 07:19:37');

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (2, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 02', 'This is a description', '01 Ly Tu Trong', 'https://prostylee02.vn', '0988000111', 1, 1, 1, '2020-12-12 07:19:37');

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (3, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 04', 'This is a description', '01 Ly Tu Trong', 'https://prostylee03.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (4, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 04', 'This is a description', '01 Ly Tu Trong', 'https://prostylee04.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (5, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 000005', 'This is a description', '01 Ly Tu Trong', 'https://prostylee05.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (6, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 000006', 'This is a description', '01 Ly Tu Trong', 'https://prostylee06.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (7, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 00007', 'This is a description', '01 Ly Tu Trong', 'https://prostylee07.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (8, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 2, 'Prostylee 00008', 'This is a description', '01 Ly Tu Trong', 'https://prostylee08.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (9, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 00009', 'This is a description', '01 Ly Tu Trong', 'https://prostylee09.vn', '0988000111', 1, 1, 1, null);

INSERT INTO store (id, created_at, created_by, updated_at, updated_by, company_id, name, description, address, website, phone, owner_id, location_id, status, deleted_at)
VALUES (10, '2020-12-11 16:09:49', 1, '2020-12-11 16:09:49', 1, 1, 'Prostylee 00010', 'This is a description', '01 Ly Tu Trong', 'https://prostylee10.vn', '0988000111', 1, 1, 1, null);

ALTER SEQUENCE store_seq RESTART WITH 11;

-- store Statistic
INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (1, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (2, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (3, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (4, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (5, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (6, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (7, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (8, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (9, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

INSERT INTO store_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (10, '2021-04-25 14:06:40.000000', 1, '2021-04-25 14:06:44.000000', 1, 0, 0, 0, 0, 0);

ALTER SEQUENCE store_statistic_seq RESTART WITH 11;