/* 
  mysql -u root -p
  CREATE USER 'dbtest'@'localhost' IDENTIFIED BY 'dbtest';
  GRANT ALL PRIVILEGES ON application_db.* TO 'dbtest'@'localhost';
  FLUSH PRIVILEGES;
  source todo.sql
*/

CREATE DATABASE IF NOT EXISTS `application_db`;
USE `application_db`;

DROP TABLE IF EXISTS `todo`;

CREATE TABLE `todo` (
	`id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`description` varchar(50) NOT NULL,
	`done` BOOLEAN,
	`target_date` DATE NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `todo` VALUES
(1,'todo description 1',TRUE,NOW()),
(2,'todo description 2',FALSE,NOW());

