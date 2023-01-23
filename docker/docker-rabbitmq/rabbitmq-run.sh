#!/bin/bash

sudo docker container kill rabbitmq-server
sudo docker container rm rabbitmq-server
sudo docker run		\
	--network web_server_network \
	--ip 172.18.0.2	\
	-p 5672:5672	\
	-p 15672:15672	\
	-p 61611:61611	\
	-p 61612:61612	\
	--name rabbitmq-server \
	-v $(pwd):/backup rabbitmq-custom

