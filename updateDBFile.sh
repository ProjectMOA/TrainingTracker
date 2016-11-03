#!/usr/bin/env bash
##This script updates the deploying file "deployDB.sql"

cat ./src/main/resources/* >deployDB.sql
