#!/bin/bash

sudo docker exec      		\
    -it \
    sql-backend bash

# select user, host from mysql.user;
# USE `application_db`;
# show tables;
