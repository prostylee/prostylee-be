ALTER TABLE "user" ADD COLUMN email_verified boolean;

ALTER TABLE "user" ADD COLUMN phone_number_verified boolean;

ALTER TABLE "user" ADD COLUMN sub varchar(64);