ALTER TABLE "user" DROP COLUMN IF EXISTS location_id;
ALTER TABLE "user" ADD COLUMN location_id bigint;

ALTER TABLE product DROP COLUMN IF EXISTS location_id;
ALTER TABLE product ADD COLUMN location_id bigint;