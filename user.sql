--- mysql -u root -p
--- CREATE USER 'dbtest'@'localhost' IDENTIFIED BY 'dbtest';
--- GRANT ALL PRIVILEGES ON application_db.* TO 'dbtest'@'localhost';
--- FLUSH PRIVILEGES;
--- source user.sql

CREATE DATABASE IF NOT EXISTS `application_db`;
USE `application_db`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` varchar(50) NOT NULL,
	`email` varchar(100) NOT NULL,
	`password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `user` VALUES
(1,'jonathan_chandler','chandler.jonathan.g@gmail.com','password'),
(2,'jonathan_chandler2','chandler.jonathan.g@gmail.com','password2');

