DROP USER IF EXISTS 'username'@'java-spring-backend.web_server_network';
CREATE USER 'username'@'java-spring-backend.web_server_network' IDENTIFIED BY 'password1';

source user.sql
source employee.sql
source tictactoe.sql

grant all on * to 'username'@'java-spring-backend.web_server_network';

