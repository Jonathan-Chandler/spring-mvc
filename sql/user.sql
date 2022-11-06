--- mysql -u root -p
--- use application_db;
--- CREATE USER 'dbtest'@'localhost' IDENTIFIED BY 'dbtest';
--- GRANT ALL PRIVILEGES ON application_db.* TO 'dbtest'@'localhost';
--- FLUSH PRIVILEGES;
--- pager less -S
--- select * from user;
--- source user.sql

CREATE DATABASE IF NOT EXISTS `application_db`;
USE `application_db`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
	`username` varchar(50) NOT NULL,
	`email` varchar(100) NOT NULL,
	`password` TEXT NOT NULL,
	`active` BOOLEAN,
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

