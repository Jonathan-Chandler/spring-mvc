#!/bin/bash

# ip addr 172.18.0.3
# 3000 - react
# 8080 - java spring
sudo docker container kill java-spring-backend
sudo docker container rm java-spring-backend
# sudo docker run -i -t		\
sudo docker run      		\
	--user user:user		\
	--network web_server_network \
	--ip 172.18.0.3			\
	-p 3000:3000			\
	-p 8080:8080			\
	-v $(pwd)/home/user:/home/user \
	-v $(pwd)/../../java-spring-backend:/home/user/java-spring-backend \
	-v $(pwd)/../../react-frontend:/home/user/react-frontend \
	-v $(pwd)/../docker-spring-react:/home/user/docker-spring-react \
	-w /home/user			\
	--name java-spring-backend \
	spring-react-container
sudo docker container ls

