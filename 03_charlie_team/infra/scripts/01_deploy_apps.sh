#!/bin/bash

#################
### App Setup ###
#################

### Set variables

# Namespaces
namespaceCharlie="charlie"

# Mysql
declare -A mysql
mysql["name"]="mysql"
mysql["image"]="mysql:8.0"
mysql["port"]=3306
mysql["replicas"]=1
mysql["nodePoolName"]="storage"

# Persistence
declare -A persistence
persistence["name"]="persistence"
persistence["imageName"]="charlie-persistence-service"
persistence["appName"]="charlie-persistence-service"
persistence["port"]=8080
persistence["nodePoolName"]="general"

# Proxy
declare -A proxy
proxy["name"]="proxy"
proxy["imageName"]="charlie-proxy-service"
proxy["appName"]="charlie-proxy-service"
proxy["port"]=8080
proxy["nodePoolName"]="general"

####################
### Build & Push ###
####################

# --platform linux/amd64 \

# Persistence
docker build \
  --platform linux/amd64 \
  --build-arg newRelicAppName=${persistence[appName]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY_CHARLIE \
  --tag "${DOCKERHUB_NAME}/${persistence[imageName]}" \
  "../../apps/charlie-persistence-service/."
docker push "${DOCKERHUB_NAME}/${persistence[imageName]}"

# Proxy
docker build \
  --platform linux/amd64 \
  --build-arg newRelicAppName=${proxy[appName]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY_CHARLIE \
  --tag "${DOCKERHUB_NAME}/${proxy[imageName]}" \
  "../../apps/charlie-proxy-service/."
docker push "${DOCKERHUB_NAME}/${proxy[imageName]}"

##################
### Deploy K8s ###
##################

### Mysql ###
helm upgrade mysql \
  --install \
  --wait \
  --debug \
  --namespace $namespaceCharlie \
  --set name=${mysql[name]} \
  --set namespace=$namespaceCharlie \
  --set image=${mysql[image]} \
  --set port=${mysql[port]} \
  --set replicas=${mysql[replicas]} \
  --set nodePoolName=${mysql[nodePoolName]} \
  "../charts/mysql"

### Persistence ###
helm upgrade ${persistence[name]} \
  --install \
  --wait \
  --debug \
  --namespace $namespaceCharlie \
  --set name=${persistence[name]} \
  --set namespace=$namespaceCharlie \
  --set imageName=${persistence[imageName]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set port=${persistence[port]} \
  --set nodePoolName=${persistence[nodePoolName]} \
  "../charts/charlie-persistence-service"

### Proxy ###
helm upgrade ${proxy[name]} \
  --install \
  --wait \
  --debug \
  --namespace $namespaceCharlie \
  --set name=${proxy[name]} \
  --set namespace=$namespaceCharlie \
  --set imageName=${proxy[imageName]} \
  --set dockerhubName=$DOCKERHUB_NAME \
  --set port=${proxy[port]} \
  --set nodePoolName=${proxy[nodePoolName]} \
  "../charts/charlie-proxy-service"
