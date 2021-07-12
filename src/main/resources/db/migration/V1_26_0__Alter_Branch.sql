ALTER TABLE "branch" ADD COLUMN city_code varchar(20) not null;
ALTER TABLE "branch" ADD COLUMN district_code varchar(20) not null;
ALTER TABLE "branch" ADD COLUMN ward_code varchar(20) not null;
ALTER TABLE "branch" ADD COLUMN address varchar(512) not null;
ALTER TABLE "branch" ADD COLUMN full_address varchar(512) not null;
