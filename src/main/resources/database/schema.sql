-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  `full_name` VARCHAR(512) NULL,
  `gender` TINYINT(1) NULL,
  `phone_number` VARCHAR(32) NULL,
  `email` VARCHAR(512) NULL,
  `location_id` VARCHAR(512) NULL,
  `allow_notification` TINYINT(1) NULL,
  `active` TINYINT(1) NULL DEFAULT 1 COMMENT '1: active\n0: inactive',
  `deleted_at` TIMESTAMP NULL,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `code` VARCHAR(64) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  INDEX `fk_user_role_user1_idx` (`user_id` ASC),
  INDEX `fk_user_role_role1_idx` (`role_id` ASC),
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_role_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_link_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_link_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `provider_name` VARCHAR(45) NULL,
  `provider_id` VARCHAR(45) NULL,
  `user_id` BIGINT NOT NULL,
  INDEX `fk_user_link_account_user1_idx` (`user_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_link_account_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `company` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(4096) NULL,
  `owner_id` BIGINT NULL,
  `active` TINYINT(1) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `store` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `address` VARCHAR(512) NULL,
  `website` VARCHAR(512) NULL,
  `phone` VARCHAR(512) NULL,
  `owner_id` BIGINT NULL,
  `company_id` BIGINT NOT NULL,
  `location_id` BIGINT NULL,
  `status` INT NULL COMMENT '0: inactive\n1: active\n2: in-progress',
  `deleted_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_store_company1_idx` (`company_id` ASC),
  CONSTRAINT `fk_store_company1`
    FOREIGN KEY (`company_id`)
    REFERENCES `company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `brand`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `brand` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(4096) NULL,
  `user_id` BIGINT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(4096) NULL,
  `icon` VARCHAR(512) NULL,
  `order` INT NULL,
  `deleted_at` TIMESTAMP NULL,
  `active` TINYINT(1) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(512) NULL,
  `category_id` BIGINT NOT NULL,
  `brand_id` BIGINT NOT NULL,
  `location_id` VARCHAR(512) NULL,
  `store_id` BIGINT NULL,
  `price` DOUBLE NULL,
  `price_sale` DOUBLE NULL,
  `published_date` TIMESTAMP NULL,
  `deleted_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_product_category1_idx` (`category_id` ASC),
  INDEX `fk_product_brand1_idx` (`brand_id` ASC),
  CONSTRAINT `fk_product_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `brand` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `attachement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `attachement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `type` VARCHAR(512) NULL,
  `path` VARCHAR(2048) NULL,
  `display_name` VARCHAR(512) NULL,
  `thumbnail` VARCHAR(2048) NULL,
  `size_in_kb` BIGINT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `product_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `attachment_id` BIGINT NULL,
  `order` INT NULL,
  `product_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_product_image_product1_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_image_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `attribute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(512) NULL,
  `order` INT NULL,
  `label` VARCHAR(128) NULL,
  `description` VARCHAR(512) NULL,
  `type` INT NULL COMMENT '1. TEXT\n2. NUMBER\n3. SELECT_BOX\n4. CHECKBOX\n5. RADIO',
  `category_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_attribute_category1_idx` (`category_id` ASC),
  CONSTRAINT `fk_attribute_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `product_price`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_price` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(512) NULL,
  `product_id` BIGINT NOT NULL,
  `sku` VARCHAR(512) NULL,
  `price` DOUBLE NULL,
  `price_sale` DOUBLE NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_product_group_attribute_product1_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_group_attribute_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `product_attribute`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_attribute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `attr_value` VARCHAR(512) NULL,
  `attr_id` BIGINT NOT NULL,
  `product_price_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_product_attribute_attribute1_idx` (`attr_id` ASC),
  INDEX `fk_product_attribute_product_price1_idx` (`product_price_id` ASC),
  CONSTRAINT `fk_product_attribute_attribute1`
    FOREIGN KEY (`attr_id`)
    REFERENCES `attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_attribute_product_price1`
    FOREIGN KEY (`product_price_id`)
    REFERENCES `product_price` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `attribute_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `attribute_option` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `value` VARCHAR(512) NULL,
  `label` VARCHAR(512) NULL,
  `attribute_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_attribute_option_attribute1_idx` (`attribute_id` ASC),
  CONSTRAINT `fk_attribute_option_attribute1`
    FOREIGN KEY (`attribute_id`)
    REFERENCES `attribute` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `product_id` VARCHAR(512) NULL,
  `code` VARCHAR(512) NULL,
  `price` VARCHAR(512) NULL,
  `product_price_id` VARCHAR(512) NULL,
  `status` VARCHAR(512) NULL,
  `shipping_type` BIGINT NULL,
  `payment_type` VARCHAR(512) NULL,
  `buyer_id` VARCHAR(512) NULL,
  `seller_id` BIGINT NULL,
  `store_id` BIGINT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `location` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `address` VARCHAR(512) NULL,
  `latlng` VARCHAR(512) NULL,
  `state` VARCHAR(512) NULL,
  `city` VARCHAR(512) NULL,
  `country` VARCHAR(512) NULL,
  `zipcode` VARCHAR(512) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `shipping_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shipping_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(512) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `payment_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payment_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(512) NULL,
  `description` VARCHAR(512) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `shipping_address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shipping_address` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `fullname` VARCHAR(512) NULL,
  `email` VARCHAR(512) NULL,
  `phone_number` VARCHAR(512) NULL,
  `address1` VARCHAR(512) NULL,
  `address2` VARCHAR(512) NULL,
  `state` VARCHAR(512) NULL,
  `city` VARCHAR(512) NULL,
  `country` VARCHAR(512) NULL,
  `zipcode` VARCHAR(512) NULL,
  `order_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_shipping_address_order1_idx` (`order_id` ASC),
  CONSTRAINT `fk_shipping_address_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `user_follower`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_follower` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `user_id` BIGINT NULL,
  `target_id` BIGINT NULL,
  `target_type` VARCHAR(512) NULL COMMENT 'USER, STORE, PRODUCT',
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `user_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `user_id` BIGINT NULL,
  `product_id` BIGINT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `content` VARCHAR(512) NULL,
  `parent_id` BIGINT NULL,
  `target_id` BIGINT NOT NULL,
  `target_type` VARCHAR(512) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `comment_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comment_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `attachment_id` BIGINT NULL,
  `order` INT NULL,
  `comment_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_comment_image_comment1_idx` (`comment_id` ASC),
  CONSTRAINT `fk_comment_image_comment1`
    FOREIGN KEY (`comment_id`)
    REFERENCES `comment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_star`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_star` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `target_id` BIGINT NULL,
  `target_type` VARCHAR(512) NULL COMMENT 'USER, STORE, PRODUCT',
  `value` INT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `user_tracking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_tracking` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `store_id` BIGINT NULL,
  `product_id` BIGINT NULL,
  `category_id` BIGINT NULL,
  `search_keyword` VARCHAR(512) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `feature`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `feature` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(128) NULL,
  `api_path` VARCHAR(512) NULL,
  `code` VARCHAR(128) NULL,
  `group` VARCHAR(128) NULL,
  `order` INT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `feature_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `feature_role` (
  `role_id` BIGINT NOT NULL,
  `feature_id` BIGINT NOT NULL,
  INDEX `fk_timestamps_role1_idx` (`role_id` ASC),
  INDEX `fk_timestamps_feature1_idx` (`feature_id` ASC),
  PRIMARY KEY (`role_id`, `feature_id`),
  CONSTRAINT `fk_timestamps_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_timestamps_feature1`
    FOREIGN KEY (`feature_id`)
    REFERENCES `feature` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `user_temp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_temp` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `username` VARCHAR(128) NULL,
  `password` VARCHAR(128) NULL,
  `expired_at` TIMESTAMP NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `app_client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `app_client` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `name` VARCHAR(128) NULL,
  `secret_key` VARCHAR(512) NULL,
  `callback_url` VARCHAR(2048) NULL,
  `description` VARCHAR(512) NULL,
  `type` VARCHAR(128) NULL,
  `active` TINYINT(1) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `email_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `email_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `title` VARCHAR(512) NULL,
  `content` VARCHAR(15000) NULL,
  `template_file` VARCHAR(512) NULL,
  `type` VARCHAR(128) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `template 5col` VARCHAR(512) NULL,
  `user_id` BIGINT NULL,
  `title` VARCHAR(512) NULL,
  `content` VARCHAR(4096) NULL,
  `type` VARCHAR(128) NULL,
  `additional_data` VARCHAR(4096) NULL,
  `mark_as_read` TINYINT(1) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `push_notification_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `push_notification_token` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `user_id` BIGINT NULL,
  `token` VARCHAR(512) NULL,
  `device_name` VARCHAR(256) NULL,
  `device_id` VARCHAR(256) NULL,
  `software` VARCHAR(256) NULL,
  `os_version` VARCHAR(256) NULL,
  `os_name` VARCHAR(512) NULL,
  `brand` VARCHAR(256) NULL,
  `manufacturer` VARCHAR(256) NULL,
  `model_name` VARCHAR(256) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `user_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_store` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_by` BIGINT NULL,
  `created_at` TIMESTAMP NULL,
  `updated_by` BIGINT NULL,
  `updated_at` TIMESTAMP NULL,
  `user_id` BIGINT NOT NULL,
  `store_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
