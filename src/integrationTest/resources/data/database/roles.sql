DELETE FROM role;

insert into role (id, created_at, created_by, updated_at, updated_by, code, name) values (1, '2021-01-04 23:27:38.106000', null, '2021-01-04 23:27:38.106000', null, 'SUPER_ADMIN', 'Administrator');
insert into role (id, created_at, created_by, updated_at, updated_by, code, name) values (2, '2021-01-04 23:27:39.106000', null, '2021-01-04 23:27:39.106000', null, 'JUNIOR_ADMIN', 'Junior Administrator');
insert into role (id, created_at, created_by, updated_at, updated_by, code, name) values (3, '2021-01-04 23:27:40.106000', null, '2021-01-04 23:27:40.106000', null, 'MIDDLE_ADMIN', 'Middle Administrator');
insert into role (id, created_at, created_by, updated_at, updated_by, code, name) values (4, '2021-01-04 23:27:41.106000', null, '2021-01-04 23:27:41.106000', null, 'SENIOR_ADMIN', 'Senior Administrator');
insert into role (id, created_at, created_by, updated_at, updated_by, code, name) values (5, '2021-01-04 23:27:42.106000', null, '2021-01-04 23:27:42.106000', null, 'EMPLOYEE', 'Employee');

ALTER SEQUENCE role_seq RESTART WITH 6;