#!/bin/bash
# this script runs on the docker container to start the web and sql services

# launch sql (port 3306)
mariadbd &

su - user <<!
pass

# launch spring boot (port 8080)
/home/user/java-spring-backend/run.sh &

# launch react (port 3000)
/home/user/react-frontend/run.sh

!
