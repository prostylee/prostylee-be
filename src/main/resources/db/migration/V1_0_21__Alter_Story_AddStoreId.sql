ALTER TABLE "story" ADD COLUMN store_id bigint;

UPDATE "story" SET store_id = 1 WHERE target_type = 'user';

insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-02-03 00:10:24.288000', 1, '2021-01-27 22:50:24.288000', null, 2, 1, 1);
insert into story_image (id, created_at, created_by, updated_at, updated_by, attachment_id, "order", story_id) values (nextval('story_image_seq'), '2021-02-03 00:10:24.288000', 1, '2021-01-27 22:50:24.288000', null, 1, 2, 2);