--- mysql -u root -p
--- application_db
--- CREATE USER 'dbtest'@'localhost' IDENTIFIED BY 'dbtest';
--- GRANT ALL PRIVILEGES ON application_db.* TO 'dbtest'@'localhost';
--- FLUSH PRIVILEGES;
--- pager less -S
--- select * from user;
--- source user.sql
--- CREATE DATABASE IF NOT EXISTS `application_db`;
--- USE `application_db`;

--- DROP TABLE IF EXISTS `tictactoe`;
--- PRIMARY KEY (`player1`,`player2`)
--- show tables;

DROP TABLE IF EXISTS `tictactoe`;
CREATE TABLE `tictactoe` (
	`id` INTEGER NOT NULL AUTO_INCREMENT,
	`player1` INTEGER NOT NULL,
	`player2` INTEGER NOT NULL,
	`active_player` BOOLEAN NOT NULL,
	`realtime_game` BOOLEAN NOT NULL,
	`start_time` DATE NOT NULL,
	`board` char(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

