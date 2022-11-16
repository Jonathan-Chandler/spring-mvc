FROM archlinux

#VOLUME /home/jonathan/work/java/spring-mvc/spring-docker:/home/user

VOLUME $(pwd)/../:/home/user

RUN pacman -Syu --noconfirm
# RUN useradd -m -G wheel -s /bin/bash jonathan
# RUN echo "%wheel	ALL = (ALL) NOPASSWD: ALL" >> /etc/sudoers

# language
RUN echo "en_US.UTF-8 UTF-8" > /etc/locale.gen
RUN locale-gen
RUN echo "LANG=en_US.UTF-8" > /etc/locale.conf

# react / mariadb / spring
EXPOSE 3000 3306 8080

RUN pacman -Sy --noconfirm \
	jre11-openjdk \
	ant \
	maven \
	mariadb \
	npm

# ENTRYPOINT /home/user/spring-docker/startup-spring.sh 

#RUN echo "loopback_users.guest = false" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "listeners.tcp.default = 5672" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "management.tcp.port = 15672" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "management.cors.allow_origins.1 = *" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "stomp.listeners.tcp.1 = 127.0.0.1:61613" >> /etc/rabbitmq/rabbitmq.conf

#RUN cat /etc/rabbitmq/rabbitmq.config

#RUN mkdir -p /home/server


# sudo docker exec -u 0 -it spring-mvc /bin/bash

# sudo docker container ls
# sudo docker exec -u 0 -it 2add55196ded /bin/bash

# RUN rabbitmq-plugins enable rabbitmq_management
# RUN rabbitmq-plugins enable rabbitmq_management_agent
# RUN rabbitmq-plugins enable rabbitmq_mqtt
# RUN rabbitmq-plugins enable rabbitmq_stomp
# RUN rabbitmq-plugins enable rabbitmq_federation_management

#VOLUME $(pwd)/..:/home/user
#
#RUN rabbitmq-plugins enable rabbitmq_management rabbitmq_management_agent rabbitmq_mqtt rabbitmq_web_stomp rabbitmq_stomp rabbitmq_web_dispatch rabbitmq_prometheus rabbitmq_auth_backend_http
#
#RUN apt-get update \
#	&& apt-get install openssl -y
#
#RUN rm -rf /home/server
#RUN mkdir -p /home/server
#COPY openssl.cnf /home/openssl.cnf
#COPY keygen.sh /home/server/keygen.sh
#RUN chmod +x /home/server/keygen.sh
#RUN /home/server/keygen.sh
#RUN chown -R rabbitmq:rabbitmq /home
##COPY certificates /home/server/
#
## ports: default mgmt stomp ssl_stomp
#EXPOSE 5672 15672 61611 61612 61613 61614
#RUN ls -al /
#RUN ls -al /etc/rabbitmq
#COPY ./rabbitmq.config /etc/rabbitmq/conf.d/10-defaults.conf
#COPY ./advanced.config /etc/rabbitmq/advanced.conf
#RUN mkdir -p /backup
#RUN chown -R rabbitmq:rabbitmq /mnt
#RUN chown -R rabbitmq:rabbitmq /backup
## rabbitmqctl authenticate_user "test_user123" "password123"
#
##RUN rabbitmqctl add_user "guest123" "guest123"
#
##RUN /home/server/keygen.sh
#
## sudo docker exec -u 0 -it <hash> /bin/bash
#
## RUN rabbitmq-plugins enable rabbitmq_management
## RUN rabbitmq-plugins enable rabbitmq_management_agent
## RUN rabbitmq-plugins enable rabbitmq_mqtt
## RUN rabbitmq-plugins enable rabbitmq_stomp
## RUN rabbitmq-plugins enable rabbitmq_federation_management
#
##RUN echo "loopback_users.guest = false" >> /etc/rabbitmq/rabbitmq.conf
##RUN echo "listeners.tcp.default = 5672" >> /etc/rabbitmq/rabbitmq.conf
##RUN echo "management.tcp.port = 15672" >> /etc/rabbitmq/rabbitmq.conf
##RUN echo "management.cors.allow_origins.1 = *" >> /etc/rabbitmq/rabbitmq.conf
##RUN echo "stomp.listeners.tcp.1 = 127.0.0.1:61613" >> /etc/rabbitmq/rabbitmq.conf
#
##RUN cat /etc/rabbitmq/rabbitmq.config
#
##RUN mkdir -p /home/server
