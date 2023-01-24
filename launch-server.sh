#!/bin/bash

# remove containers and network if exist
sudo docker container kill java-spring-backend
sudo docker container kill rabbitmq-server
sudo docker container kill sql-backend
sudo docker container rm java-spring-backend
sudo docker container rm rabbitmq-server
sudo docker container rm sql-backend
sudo docker network rm web_server_network

# create docker network bridge subnet 172.18.0.1
sudo docker network create --driver=bridge --subnet=172.18.0.0/24 web_server_network
cd ./docker

# build rabbitmq server
cd ./docker-rabbitmq
./build.sh
cd ..

# build mysql server
cd ./docker-mysql 
./build.sh
cd ..

# build spring/react server
cd ./docker-spring-react
./build.sh
cd ..

# run rabbitmq and wait for initialize
cd ./docker-rabbitmq
./rabbitmq-run.sh &
cd ..
sleep 5

# run mariadbd and wait for initialize
cd ./docker-mysql
./mariadb-run.sh &
cd ..
sleep 6

# run spring/react
cd ./docker-spring-react
./spring-run.sh &
cd ..

cd ..
# sudo docker container ls
