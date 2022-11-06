FROM rabbitmq:3.8-management

RUN rabbitmq-plugins enable rabbitmq_management rabbitmq_management_agent rabbitmq_mqtt rabbitmq_web_stomp rabbitmq_stomp rabbitmq_web_dispatch rabbitmq_prometheus

RUN apt-get update \
	&& apt-get install openssl -y

RUN rm -rf /home/server
RUN mkdir -p /home/server
COPY openssl.cnf /home/openssl.cnf
COPY keygen.sh /home/server/keygen.sh
RUN chmod +x /home/server/keygen.sh
RUN /home/server/keygen.sh
RUN chown -R rabbitmq:rabbitmq /home
#COPY certificates /home/server/

# ports: default mgmt stomp ssl_stomp
EXPOSE 5672 15672 61611 61612 61613 61614
RUN ls -al /
RUN ls -al /etc/rabbitmq
COPY ./rabbitmq.config /etc/rabbitmq/conf.d/10-defaults.conf
RUN mkdir -p /backup
RUN chown -R rabbitmq:rabbitmq /mnt
RUN chown -R rabbitmq:rabbitmq /backup

#RUN rabbitmqctl add_user "guest123" "guest123"

#RUN /home/server/keygen.sh

# sudo docker exec -u 0 -it <hash> /bin/bash

# RUN rabbitmq-plugins enable rabbitmq_management
# RUN rabbitmq-plugins enable rabbitmq_management_agent
# RUN rabbitmq-plugins enable rabbitmq_mqtt
# RUN rabbitmq-plugins enable rabbitmq_stomp
# RUN rabbitmq-plugins enable rabbitmq_federation_management

#RUN echo "loopback_users.guest = false" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "listeners.tcp.default = 5672" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "management.tcp.port = 15672" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "management.cors.allow_origins.1 = *" >> /etc/rabbitmq/rabbitmq.conf
#RUN echo "stomp.listeners.tcp.1 = 127.0.0.1:61613" >> /etc/rabbitmq/rabbitmq.conf

#RUN cat /etc/rabbitmq/rabbitmq.config

#RUN mkdir -p /home/server
