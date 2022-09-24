--- mysql -u root -p
--- application_db
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
	`username` varchar(50) NOT NULL,
	`email` varchar(100) NOT NULL,
	`password` TEXT NOT NULL,
	`active` BOOLEAN,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `user` VALUES
('abcdef','chandler.jonathan.g@gmail.com','password',false),
('asonetuh','chandler.jonathan.g@gmail.com','password',true),
('asone','chandler.jonathan.g@gmail.com','password2',true);

