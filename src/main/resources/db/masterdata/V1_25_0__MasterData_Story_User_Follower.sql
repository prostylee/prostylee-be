--Update user_follower --
UPDATE user_follower
SET target_type = 'USER' WHERE target_type = 'user';

UPDATE user_follower
SET target_type = 'USER' WHERE target_type = 'user';

--Update story --
UPDATE story
SET target_type = 'STORE' WHERE target_type = 'store';

UPDATE story
SET target_type = 'USER' WHERE target_type = 'user';