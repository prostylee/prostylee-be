INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (10, '2021-02-27 12:53:25.000000', 1, null, null, 'Just like it sounds. A brand-new item', 'NEW');

INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (50, '2021-02-27 12:53:25.000000', 1, null, null, 'A pre-owned product that was inspected', 'RENEWED');

INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (100, '2021-02-08 12:53:25.000000', 1, null, null, 'Like New or Open Box', 'USED');

INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (110, '2021-02-08 12:53:25.000000', 1, null, null, 'Like New or Open Box', 'USED_LIKE_NEW');

INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (120, '2021-02-08 12:53:25.000000', 1, null, null, 'Like New or Open Box', 'USED_ACCEPTABLE');

INSERT INTO used_status (id, created_at, created_by, updated_at, updated_by, description, name)
    VALUES (1000, '2021-02-08 12:53:25.000000', 1, null, null, 'A product that was inspected and graded by a qualified supplier', 'RENTAL');

ALTER SEQUENCE used_status_seq RESTART WITH 1001;
