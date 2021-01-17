ALTER TABLE company ADD COLUMN deleted_at timestamp;
ALTER TABLE company ALTER COLUMN active SET DEFAULT true;