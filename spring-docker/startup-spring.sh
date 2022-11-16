#!/bin/bash

# sqldb (port 3306)
mariadbd &

# launch spring boot (port 8080)
/home/user/run.sh &

# launch react (port 3000)
/home/user/react-main/run.sh

