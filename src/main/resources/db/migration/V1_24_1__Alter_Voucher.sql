ALTER TABLE voucher ADD COLUMN cnd_shipping_method_ids varchar(2048);

ALTER TABLE voucher DROP COLUMN cnd_customer_location_ids;
ALTER TABLE voucher ADD COLUMN cnd_customer_address_ids varchar(2048);