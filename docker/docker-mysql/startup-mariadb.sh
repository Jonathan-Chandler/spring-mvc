#!/usr/bin/expect
# #!/bin/bash
# this script runs on the docker container to start the web and sql services

#set -m

# launch mariadb as mysql user (port 3306)
spawn cd /home/mysql
spawn "mariadbd &"

# run as user

spawn su - user
expect "Password:"
send "pass\r"

# launch spring boot (port 8080)
cd /home/user/java-spring-backend/ && ./run.sh &

# launch react (port 3000)
cd /home/user/react-frontend/ && ./run.sh &

!
