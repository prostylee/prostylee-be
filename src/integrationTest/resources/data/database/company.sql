DELETE FROM company;

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(1, 'Prostylee Company 1', 'This is a description', 1, 1, '2020-12-12 07:19:37');

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(2, 'Prostylee Company 2', 'This is a description', 1, 1, '2020-12-12 07:19:37');

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(3, 'Prostylee Company 3', 'This is a description', 1, 1, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(4, 'Prostylee Company 4', 'This is a description', 1, 1, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(5, 'Prostylee Company 5', 'This is a description', 1, 1, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(6, 'Prostylee Company 00006', 'This is a description', 1, 1, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(7, 'Prostylee Company 00007', 'This is a description', 1, 0, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(8, 'Prostylee Company 00008', 'This is a description', 1, 0, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(9, 'Prostylee Company 00009', 'This is a description', 1, 0, null);

INSERT INTO company(id, name, description, owner_id, active, deleted_at)
VALUES(10, 'Prostylee Company 00010', 'This is a description', 1, 1, null);

ALTER SEQUENCE company_seq RESTART WITH 11;