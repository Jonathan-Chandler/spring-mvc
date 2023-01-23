#!/bin/bash
# this script runs in the docker container to start mariadb

# turn on bash's job control
set -m

# launch mariadb (port 3306)
mariadbd &

# wait for service to start
sleep 10

cd sql && mariadb < all.sql
#spawn "mariadbd &"

# now we bring the primary process back into the foreground
fg %1

