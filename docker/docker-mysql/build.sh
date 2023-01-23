#!/bin/bash

# docker build -p 5672:5672 -p 15672:15672 --name rabbitmq-broker rabbitmq:3-management
sudo docker build -t sql-server-container .

