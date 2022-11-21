#!/bin/bash
# this script runs on the docker container to start the web and sql services

# launch mariadb as mysql user (port 3306)
cd /home/mysql && mariadbd &

# run as user
su - user <<!
pass

# launch spring boot (port 8080)
cd /home/user/java-spring-backend/ && ./run.sh &

# launch react (port 3000)
cd /home/user/react-frontend/ ./run.sh &

!
