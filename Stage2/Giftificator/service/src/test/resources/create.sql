CREATE TABLE `gift_certificates` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `price` INT NOT NULL,
  `duration` SMALLINT NOT NULL,
  `create_date` VARCHAR(23) NOT NULL,
  `last_update_date` VARCHAR(23) NOT NULL);

CREATE TABLE `tags` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL);

CREATE TABLE `certs_tags` (
  `cert_id` INT NOT NULL,
  `tag_id`  INT NOT NULL);