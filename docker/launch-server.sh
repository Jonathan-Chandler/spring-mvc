#!/bin/bash

# remove containers and network if exist
sudo docker container kill java-spring-backend
sudo docker container kill rabbitmq-server
sudo docker container rm java-spring-backend
sudo docker container rm rabbitmq-server
sudo docker network rm web_server_network

sudo docker network create --driver=bridge --subnet=172.18.0.0/24 web_server_network

# build and run rabbitmq server
cd ./docker-rabbitmq 
./build.sh
./rabbitmq-run.sh &
cd ..

# build and run spring/react/mysql server
cd ./docker-spring-react-sql && ./build.sh
./build.sh
./spring-run.sh
cd ..

#./docker-spring-react-sql/spring-run.sh
