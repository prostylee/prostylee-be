ALTER TABLE category ADD COLUMN language_code varchar(50) default 'vi';
ALTER TABLE category ALTER COLUMN active SET DEFAULT true;

ALTER TABLE attribute ADD COLUMN language_code varchar(50) default 'vi';
ALTER TABLE attribute_option ADD COLUMN language_code varchar(50) default 'vi';