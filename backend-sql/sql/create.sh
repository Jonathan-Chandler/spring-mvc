create database if not exists application_db;
use application_db;

CREATE USER 'dbtest'@'localhost' IDENTIFIED BY 'dbtest';
GRANT ALL PRIVILEGES ON application_db.* TO 'dbtest'@'localhost';
FLUSH PRIVILEGES;
pager less -S
# select * from user;
source user.sql
source employee.sql
source tictactoe.sql
source todo.sql

