ALTER TABLE category ADD COLUMN parent_id bigint not null default 0;
ALTER TABLE category ADD COLUMN attachment_id bigint;