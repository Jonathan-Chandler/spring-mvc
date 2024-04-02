#!/bin/bash

# load database (executed as docker mariadb script)
cd /home/mysql/sql && mariadb < all.sql
