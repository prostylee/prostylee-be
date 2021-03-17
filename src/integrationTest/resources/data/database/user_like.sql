-- Can get 12 users, excluding target_id=17 (target_type != 'USER') and target_id=20 (created_at <= 7 days)
DELETE FROM user_like;

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 11, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 12, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 13, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 14, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 15, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 11, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 11, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 12, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 16, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 17, 'STORE');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 18, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 19, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 20, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 21, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 22, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 23, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 24, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 25, 'USER');

INSERT INTO user_like(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_like_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 25, 'USER');

UPDATE user_like SET created_at = DATEADD(DAY, 7, created_at) WHERE target_id = 20;