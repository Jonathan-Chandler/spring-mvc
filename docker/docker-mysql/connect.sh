#!/bin/bash

sudo docker container ls
sudo docker exec -u 0 -it <hash> /bin/bash
# apt-get install iproute2 iputils-ping

# sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 --network host rabbitmq-custom
#sudo docker run -p 5672:5672 -p 15672:15672 -p 25672:25672 -p 61613:61613 -v $(pwd):/backup --network host rabbitmq-custom
# sudo docker run -p 8080:8080 -v $(pwd)/..:/home/user spring-mvc

#docker run -d -p 5672:5672 -p 15672:15672 -p 61613:61613 --name rabbitmq-broker rabbitmq:3-management
