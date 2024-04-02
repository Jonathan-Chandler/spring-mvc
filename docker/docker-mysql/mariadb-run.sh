#!/bin/bash

# 172.19.0.4 port 3306 - mysql
sudo docker container kill sql-backend
sudo docker container rm sql-backend
# sudo docker run -i -t		\
sudo docker run      		\
	--user root:root		\
	--network web_server_network \
	--ip 172.19.0.4			\
	-p 3306:3306			\
	-v $(pwd)/mysql-database:/var/lib/mysql:Z \
	-v $(pwd)/50-server.cnf:/etc/mysql/mariadb.conf.d/50-server.cnf:Z \
	-v $(pwd)/home/mysql:/home/mysql \
	-v $(pwd)/startup-mariadb.sh:/docker-entrypoint-initdb.d/startup-mariadb.sh \
	-w /home/mysql			\
	--name sql-backend		\
	--env MARIADB_ROOT_PASSWORD=root_pass  \
	--detach \
	mariadb:latest

