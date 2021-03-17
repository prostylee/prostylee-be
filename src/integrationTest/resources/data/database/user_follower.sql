-- Can get 8 users, excluding target_id=7 (target_type != 'USER') and target_id=10 (created_at <= 7 days)

DELETE FROM user_follower;

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 1, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 2, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 3, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 4, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 5, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 1, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 1, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 2, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 6, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 7, 'STORE');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 8, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 9, 'USER');

INSERT INTO user_follower(id, created_at, created_by, updated_at, updated_by, target_id, target_type)
VALUES(nextval('user_follower_seq'), CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 10, 'USER');

UPDATE user_follower SET created_at = DATEADD(DAY, 7, created_at) WHERE target_id = 10;