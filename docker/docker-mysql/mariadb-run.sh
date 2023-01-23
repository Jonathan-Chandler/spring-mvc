#!/bin/bash

# 172.18.0.4 port 3306 - mysql
sudo docker container kill sql-backend
sudo docker container rm sql-backend
# sudo docker run -i -t		\
sudo docker run      		\
	--user mysql:mysql		\
	--network web_server_network \
	--ip 172.18.0.4			\
	-p 3306:3306			\
	-v $(pwd)/home/mysql:/home/mysql \
	-v $(pwd)/mysql-database:/var/lib/mysql \
	-w /home/mysql			\
	--name sql-backend		\
	sql-server-container
	
#sudo docker container ls

