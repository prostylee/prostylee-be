DELETE FROM location;
ALTER SEQUENCE location_seq RESTART WITH 1;

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, '18, Nguyễn văn Mại', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.806406363857086, 106.6634168400805);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, '25/23 Cửu Long', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.811611452194661, 106.66657518425933);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Sunflowers Hotel', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.80904881516809, 106.66690810737569);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Căn hộ Sky Center', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.806465209193, 106.6662538133757);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Tòa Nhà Waseco', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.805578294531028, 106.66616221218972);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Hansol Restaurant', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.807917686910232, 106.66164104064985);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Chidori - Coffee in Bed', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.810218500104792, 106.6668034203138);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Nhà hàng Vườn Phố Tân Bình', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.801561438098435, 106.66635850039737);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'Phúc Long', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.80064237377746, 106.66069885723054);

INSERT INTO location (id, created_at, created_by, updated_at, updated_by, address, city, country, state, zipcode, latitude, longitude)
VALUES (nextval('location_seq'), '2021-02-04 00:06:05.000000', 1, '2021-02-04 00:06:08.000000', 1, 'E. Town 3, Cộng Hòa', 'Tân Bình', 'Việt Nam', 'HCM', '700000', 10.801192016736037, 106.64137768959131);

UPDATE location SET target_type='STORE';