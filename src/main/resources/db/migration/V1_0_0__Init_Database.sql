create table app_client
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	active bit null,
	callback_url varchar(2048) null,
	description varchar(512) null,
	name varchar(128) null,
	secret_key varchar(512) null,
	type varchar(128) null
)
;

create table attachement
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	display_name varchar(512) null,
	name varchar(512) null,
	path varchar(2048) null,
	size_in_kb bigint null,
	thumbnail varchar(2048) null,
	type varchar(512) null
)
;

create table attribute
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	description varchar(512) null,
	`key` varchar(512) null,
	label varchar(128) null,
	`order` int null,
	type int null,
	category_id bigint not null
)
;

create index FK8xaeml4v57nyhu2olsqgwg4ak
	on attribute (category_id);

create table attribute_option
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	label varchar(512) null,
	name varchar(512) null,
	value varchar(512) null,
	attribute_id bigint not null
)
;

create index FKolmfrc7trisieb2trwyv6gs5j
	on attribute_option (attribute_id);

create table brand
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	description varchar(4096) null,
	name varchar(512) null,
	user_id bigint null
)
;

create table category
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	active bit null,
	deleted_at datetime null,
	description varchar(4096) null,
	icon varchar(512) null,
	name varchar(512) null,
	`order` int null
)
;

create table comment
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	content varchar(512) null,
	parent_id bigint null,
	target_id bigint not null,
	target_type varchar(512) null
)
;

create table comment_image
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	attachment_id bigint null,
	`order` int null,
	comment_id bigint not null
)
;

create index FKqxojxrgk8n96u5n6iix54563s
	on comment_image (comment_id);

create table company
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	active bit null,
	description varchar(4096) null,
	name varchar(512) null,
	owner_id bigint null
)
;

create table email_template
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	content varchar(15000) null,
	template_file varchar(512) null,
	title varchar(512) null,
	type varchar(128) null
)
;

create table feature
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	api_path varchar(512) null,
	code varchar(128) null,
	`group` varchar(128) null,
	name varchar(128) null,
	`order` int null
)
;

create table feature_role
(
	role_id bigint not null,
	feature_id bigint not null,
	primary key (feature_id, role_id)
)
;

create index FKecdkmwin2ib8jlfd60a9mr2b5
	on feature_role (role_id);

create table location
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	address varchar(512) null,
	city varchar(512) null,
	country varchar(512) null,
	latlng varchar(512) null,
	state varchar(512) null,
	zipcode varchar(512) null
)
;

create table metadata
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	code varchar(128) null,
	content longtext null
)
;

create table notification
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	additional_data varchar(4096) null,
	content varchar(4096) null,
	mark_as_read bit null,
	`template 5col` varchar(512) null,
	title varchar(512) null,
	type varchar(128) null,
	user_id bigint null
)
;

create table `order`
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	buyer_id varchar(512) null,
	code varchar(512) null,
	payment_type varchar(512) null,
	price varchar(512) null,
	product_id varchar(512) null,
	product_price_id varchar(512) null,
	seller_id bigint null,
	shipping_type bigint null,
	status varchar(512) null,
	store_id bigint null
)
;

create table payment_type
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	description varchar(512) null,
	name varchar(512) null
)
;

create table product
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	deleted_at datetime null,
	description varchar(512) null,
	location_id varchar(512) null,
	name varchar(512) null,
	price double null,
	price_sale double null,
	published_date datetime null,
	store_id bigint null,
	brand_id bigint not null,
	category_id bigint not null
)
;

create index FK7l29ekt1x29jup80y2iigimyy
	on product (category_id);

create index FKh2s69uoa44voy2jsqov3oid7r
	on product (brand_id);

create table product_attribute
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	attr_value varchar(512) null,
	attr_id bigint not null,
	product_price_id bigint not null
)
;

create index FKdn771njw065udb6ll0uftms38
	on product_attribute (attr_id);

create index FKq41pogw2rb1fkfddhfxip4s5o
	on product_attribute (product_price_id);

create table product_image
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	attachment_id bigint null,
	`order` int null,
	product_id bigint not null
)
;

create index FK404iut26wg4pq85osw3vn0kwd
	on product_image (product_id);

create table product_price
(
	id bigint not null
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	name varchar(512) null,
	price double null,
	price_sale double null,
	sku varchar(512) null,
	product_id bigint not null
)
;

create index FKg5vp5ubh91bqfghwmsqmm96qg
	on product_price (product_id);

create table push_notification_token
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	brand varchar(256) null,
	device_id varchar(256) null,
	device_name varchar(256) null,
	manufacturer varchar(256) null,
	model_name varchar(256) null,
	os_name varchar(512) null,
	os_version varchar(256) null,
	software varchar(256) null,
	store_id bigint null,
	token varchar(512) null,
	user_id bigint null
)
;

create table role
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	code varchar(64) null,
	name varchar(512) null
)
;

create table shipping_address
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	address1 varchar(512) null,
	address2 varchar(512) null,
	city varchar(512) null,
	country varchar(512) null,
	email varchar(512) null,
	fullname varchar(512) null,
	phone_number varchar(512) null,
	state varchar(512) null,
	zipcode varchar(512) null,
	order_id bigint not null
)
;

create index FKo6xa2e2q73h0ljij12921k5t7
	on shipping_address (order_id);

create table shipping_type
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	description varchar(512) null,
	name varchar(512) null
)
;

create table store
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	address varchar(512) null,
	deleted_at datetime null,
	location_id bigint null,
	name varchar(512) null,
	owner_id bigint null,
	phone varchar(512) null,
	status int null,
	website varchar(512) null,
	company_id bigint not null
)
;

create index FKj4vy55rdnnioae3ogbbsmu5g0
	on store (company_id);

create table user
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	active bit null,
	allow_notification bit null,
	deleted_at datetime null,
	email varchar(512) null,
	full_name varchar(512) null,
	gender char null,
	location_id varchar(512) null,
	password varchar(128) null,
	phone_number varchar(32) null,
	username varchar(128) null
)
;

create table user_follower
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	target_id bigint null,
	target_type varchar(512) null,
	user_id bigint null
)
;

create table user_like
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	product_id bigint null,
	user_id bigint null
)
;

create table user_link_account
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	provider_id varchar(45) null,
	provider_name varchar(45) null,
	user_id bigint not null
)
;

create index FKoncs5ymuulwi5v4iipxtovo07
	on user_link_account (user_id);

create table user_role
(
	user_id bigint not null,
	role_id bigint not null,
	primary key (role_id, user_id)
)
;

create index FKhjx9nk20h4mo745tdqj8t8n9d
	on user_role (user_id);

create table user_star
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	target_id bigint null,
	target_type varchar(512) null,
	value int null
)
;

create table user_store
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	store_id bigint not null,
	user_id bigint not null
)
;

create table user_temp
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	expired_at datetime null,
	password varchar(128) null,
	username varchar(128) null
)
;

create table user_tracking
(
	id bigint auto_increment
		primary key,
	created_at datetime null,
	created_by bigint null,
	updated_at datetime null,
	updated_by bigint null,
	category_id bigint null,
	product_id bigint null,
	search_keyword varchar(512) null,
	store_id bigint null
)
;

