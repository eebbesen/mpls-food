#!/bin/bash

docker rm  mpls-food-web-1  mpls-food-db-1
docker image rm mpls-food
rm -rf volumes/db/*
gradle build --no-scan -PexcludeTests='**/endtoend*'
docker build -f Dockerfile -t mpls-food .
