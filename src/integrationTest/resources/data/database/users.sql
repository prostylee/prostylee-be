DELETE FROM user_role;
DELETE FROM user;

INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (1, '2020-12-11 16:09:49', null, '2020-12-11 16:09:49', null, true, true, null, 'test1@prostylee.vn', 'Prostylee 1', 'M', null, '$2a$10$qHzuW4POXF6aOCiuTgr3ZeUVHsiBEPCcK/ArKQefRSB0gdDaZ1NXa', '0900000003', 'test1@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (2, '2020-12-11 16:58:35', null, '2020-12-11 16:58:35', null, true, true, null, 'test2@prostylee.vn', 'Prostylee 2', 'M', null, '$2a$10$suMwHsSpw/yOka2LNAJ.nu89yJN97Sn4XXyePGsTKEYZbkKiMA2om', '0988985000', 'test2@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (3, '2020-12-11 17:12:09', null, '2020-12-11 17:12:09', null, true, true, null, 'test3@prostylee.vn', 'Prostylee 3', 'M', null, '$2a$10$msQ57rnfGHHz8.5uZJL0P.LazXPVsEh5jb/DkOQRpP8JRzlEu/bfW', '0988985000', 'test3@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (4, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test4@prostylee.vn', 'Prostylee 4', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test4@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (5, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test5@prostylee.vn', 'Prostylee 5', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test5@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (6, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test6@prostylee.vn', 'Prostylee 6', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test6@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (7, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test7@prostylee.vn', 'Prostylee 7', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test7@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (8, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test8@prostylee.vn', 'Prostylee 8', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test8@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (9, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test9@prostylee.vn', 'Prostylee 9', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test9@prostylee.vn');
INSERT INTO user (id, created_at, created_by, updated_at, updated_by, active, allow_notification, deleted_at, email, full_name, gender, location_id, password, phone_number, username) VALUES (10, '2020-12-12 07:19:37', null, '2020-12-12 07:19:37', null, true, true, null, 'test10@prostylee.vn', 'Prostylee 10', 'M', null, '$2a$10$8U4jVkvTdq0UqU/2a9j9h.7PuZ71gHKh5JiwI.aFEJZfNHU1Fl.PK', '0988985000', 'test10@prostylee.vn');

INSERT INTO user_role(user_id, role_id) VALUES (1, 1);
INSERT INTO user_role(user_id, role_id) VALUES (2, 3);
INSERT INTO user_role(user_id, role_id) VALUES (4, 2);

ALTER SEQUENCE user_seq RESTART WITH 11;

-- user statistic
DELETE FROM user_statistic;

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (1, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (2, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (3, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (4, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (5, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (6, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (7, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (8, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (9, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO user_statistic (id, created_at, created_by, updated_at, updated_by, number_of_product, number_of_story, number_of_post, number_of_like, number_of_comment, number_of_follower, number_of_following)
VALUES (10, '2021-04-25 15:28:11.000000', 1, '2021-04-25 15:28:13.000000', 1, 0, 0, 0, 0, 0, 0, 0);

ALTER SEQUENCE user_statistic_seq RESTART WITH 11;
