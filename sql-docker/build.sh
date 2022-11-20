#!/bin/bash

# docker build -p 5672:5672 -p 15672:15672 --name rabbitmq-broker rabbitmq:3-management
sudo docker build -t sql-backend .

# -d detach
# -p exposed port
#	 5672 - client connection
#	15672 - management port (http://localhost:15672 / http://localhost:15672/api/index.html)
# roles: administrator > monitoring > policymaker > management

# without management plugin
# docker run -d -p 5672:5672 --name rabbitmq-broker rabbitmq:latest

# with management plugin
# rabbitmq-plugins enable rabbitmq_management
# docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq-broker rabbitmq:3-management
