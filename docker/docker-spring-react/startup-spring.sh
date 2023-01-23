#!/bin/bash

# turn on bash's job control
set -m
  
# launch spring boot (port 8080)
cd /home/user/java-spring-backend/ && ./run.sh &

# launch react (port 3000)
cd /home/user/react-frontend/ && ./run.sh &
  
# bring process back into the foreground
fg %1

