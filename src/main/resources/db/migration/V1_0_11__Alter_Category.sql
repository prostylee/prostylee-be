ALTER TABLE category ADD COLUMN language_code varchar(50) default 'vi';
ALTER TABLE category ALTER COLUMN active SET DEFAULT true;

ALTER TABLE category ADD COLUMN parent_id bigint not null default 0;
ALTER TABLE category ADD COLUMN attachment_id bigint;