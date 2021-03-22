ALTER TABLE location ADD COLUMN latitude double precision;
ALTER TABLE location ADD COLUMN longitude double precision;
ALTER TABLE location DROP COLUMN latlng;
ALTER TABLE location ADD COLUMN target_type varchar(64);