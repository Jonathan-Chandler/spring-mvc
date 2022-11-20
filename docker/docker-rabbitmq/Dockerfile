FROM rabbitmq:3.8-management

RUN rabbitmq-plugins enable rabbitmq_management rabbitmq_management_agent rabbitmq_mqtt rabbitmq_web_stomp rabbitmq_stomp rabbitmq_web_dispatch rabbitmq_prometheus rabbitmq_auth_backend_http

RUN apt-get update \
	&& apt-get install openssl -y

RUN rm -rf /home/server
RUN mkdir -p /home/server
COPY openssl.cnf /home/openssl.cnf
COPY keygen.sh /home/server/keygen.sh
RUN chmod +x /home/server/keygen.sh
RUN /home/server/keygen.sh
RUN chown -R rabbitmq:rabbitmq /home

# ports: default mgmt stomp ssl_stomp
EXPOSE 5672 15672 61611 61612 61613 61614
RUN ls -al /
RUN ls -al /etc/rabbitmq
COPY ./rabbitmq.config /etc/rabbitmq/conf.d/10-defaults.conf
COPY ./advanced.config /etc/rabbitmq/advanced.conf
RUN mkdir -p /backup
RUN chown -R rabbitmq:rabbitmq /mnt
RUN chown -R rabbitmq:rabbitmq /backup

