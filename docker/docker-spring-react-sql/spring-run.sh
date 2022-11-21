#!/bin/bash
# -d detach
# -e ENV_VAR=value -> environment variable
# --hostname container_host_name
# --name container_name
# -p exposed port
#	 5672 - client connection
#	15672 - management port (http://localhost:15672 / http://localhost:15672/api/index.html)
#			mgmt roles: administrator > monitoring > policymaker > management

# plugins: rabbitmq_federation_management, rabbitmq_management, rabbitmq_mqtt, rabbitmq_stomp

# default user
# docker run 
#	-d \
# 	--hostname my-rabbit \
# 	--name some-rabbit \
# 	-e RABBITMQ_DEFAULT_USER=user \
# 	-e RABBITMQ_DEFAULT_PASS=password \
#	rabbitmq:3-management

# without management plugin
# docker run -d -p 5672:5672 --name rabbitmq-broker rabbitmq:latest

# with management plugin
# rabbitmq-plugins enable rabbitmq_management
# docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq-broker rabbitmq:3-management

# docker container stomp @61613
#sudo docker run -p 5672:5672 -p 15672:15672 -p 61613:61613 -t rabbitmq-custom



# sudo docker container ls
# sudo docker exec -u 0 -it <hash> /bin/bash
# apt-get install iproute2 iputils-ping

# sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 --network host rabbitmq-custom
#sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 -v $(pwd):/backup --network host rabbitmq-custom

#sudo docker run -p 3000:3000 -p 3306:3306 -p 8080:8080 -v $(pwd)/..:/home/user spring-mvc

# expose ports for react / mariadb / spring, also mount the volume that contains web app binaries
# 	--user user:user		\

# 3000 - react
# 8080 - java spring
sudo docker run -i -t		\
	--user mysql:mysql		\
	--ip 172.17.0.2			\
	-p 3000:3000			\
	-p 8080:8080			\
	-v $(pwd)/../..:/home/user \
	-v $(pwd)/../../../mysql-database/mysql:/var/lib/mysql \
	-w /home/user			\
	spring-mvc 

#	-p 3306:3306			\
#	-p 8080:8080 			\

# mariadbd as root

#docker run -d -p 5672:5672 -p 15672:15672 -p 61613:61613 --name rabbitmq-broker rabbitmq:3-management
