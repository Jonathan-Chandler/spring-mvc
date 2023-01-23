#!/bin/bash

# 3000 - react
# 8080 - java spring
sudo docker container kill sql-backend
sudo docker container rm sql-backend
sudo docker run -i -t		\
	--user mysql:mysql		\
	--network web_server_network \
	--ip 172.18.0.4			\
	-p 3306:3306			\
	-v $(pwd)/home/mysql:/home/mysql \
	-v $(pwd)/mysql-database:/var/lib/mysql \
	-w /home/mysql			\
	--name sql-backend		\
	sql-server-container
	
sudo docker container ls

#	-p 3306:3306			\
#	-p 8080:8080 			\

# mariadbd as root

#docker run -d -p 5672:5672 -p 15672:15672 -p 61613:61613 --name rabbitmq-broker rabbitmq:3-management
