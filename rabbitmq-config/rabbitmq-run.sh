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
# sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 --network host rabbitmq-custom
#sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 -v $(pwd):/backup --network host rabbitmq-custom
sudo docker run -p 1883:1883 -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61611:61611 -p 61612:61612 -p 61613:61613 -p 61614:61614 -v $(pwd):/backup rabbitmq-custom
#docker run -d -p 5672:5672 -p 15672:15672 -p 61613:61613 --name rabbitmq-broker rabbitmq:3-management
