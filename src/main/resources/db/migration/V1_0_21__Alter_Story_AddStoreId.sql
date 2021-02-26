ALTER TABLE "story" ADD COLUMN store_id bigint;

UPDATE "story" SET store_id = 1 WHERE target_type = 'user';

