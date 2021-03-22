ALTER TABLE user_like DROP COLUMN IF EXISTS product_id;
ALTER TABLE user_like DROP COLUMN IF EXISTS user_id;
ALTER TABLE user_like ADD COLUMN target_id bigint;
ALTER TABLE user_like ADD COLUMN target_type varchar(512);