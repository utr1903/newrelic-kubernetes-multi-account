#!/bin/bash

#################
### App Setup ###
#################

### Set variables

# Namespaces
namespaceBravo="bravo"

# Mongo
declare -A mongo
mongo["name"]="mongo"
mongo["image"]="mongo"
mongo["port"]=27017
mongo["replicas"]=1
mongo["nodePoolName"]="storage"

# Redis
declare -A redis
redis["name"]="redis"
redis["image"]="redis"
redis["port"]=6379
redis["replicas"]=1
redis["nodePoolName"]="storage"

# Persistence
declare -A persistence
persistence["name"]="persistence"
persistence["imageName"]="bravo-persistence-service"
persistence["appName"]="bravo-persistence-service"
persistence["port"]=8080
persistence["replicas"]=1
persistence["nodePoolName"]="general"

# Input Processor
declare -A proxy
proxy["name"]="proxy"
proxy["imageName"]="bravo-proxy-service"
proxy["appName"]="bravo-proxy-service"
proxy["port"]=8080
proxy["replicas"]=1
proxy["nodePoolName"]="general"

####################
### Build & Push ###
####################

# --platform linux/amd64 \

### Persistence
docker build \
  --platform linux/amd64 \
  --tag "${DOCKERHUB_NAME}/${persistence[imageName]}" \
  "../../apps/bravo-persistence-service/."
docker push "${DOCKERHUB_NAME}/${persistence[imageName]}"

### Proxy
docker build \
  --platform linux/amd64 \
  --build-arg newRelicAppName=${proxy[appName]} \
  --build-arg newRelicLicenseKey=$NEWRELIC_LICENSE_KEY_BRAVO \
  --tag "${DOCKERHUB_NAME}/${proxy[imageName]}" \
  "../../apps/bravo-proxy-service/bravo-proxy-service/."
docker push "${DOCKERHUB_NAME}/${proxy[imageName]}"
#########

##################
### Deploy K8s ###
##################

# ### Redis ###
# helm upgrade redis \
#   --install \
#   --wait \
#   --debug \
#   --namespace $namespaceBravo \
#   --set name=${redis[name]} \
#   --set namespace=$namespaceBravo \
#   --set image=${redis[image]} \
#   --set port=${redis[port]} \
#   --set replicas=${redis[replicas]} \
#   --set nodePoolName=${redis[nodePoolName]} \
#   "../charts/redis"

# ### Mongo ###
# helm upgrade mongo \
#   --install \
#   --wait \
#   --debug \
#   --namespace $namespaceBravo \
#   --set name=${mongo[name]} \
#   --set namespace=$namespaceBravo \
#   --set image=${mongo[image]} \
#   --set port=${mongo[port]} \
#   --set replicas=${mongo[replicas]} \
#   --set nodePoolName=${mongo[nodePoolName]} \
#   "../charts/mongo"

### Mongo (Bitnami) ###
helm repo add bitnami https://charts.bitnami.com/bitnami
helm upgrade ${mongo[name]} \
  --install \
  --wait \
  --debug \
  --namespace $namespaceBravo \
  --set namespace=$namespaceBravo \
  --set auth.enabled=false \
  --set livenessProbe.enabled=true \
  --set livenessProbe.initialDelaySeconds=30 \
  --set readinessProbe.enabled=true \
  --set readinessProbe.initialDelaySeconds=30 \
  --set architecture=standalone \
  --set replicaCount=1 \
  --set nodeSelector.nodePoolName="storage" \
  --set arbiter.nodeSelector.nodePoolName="storage" \
  "bitnami/mongodb"

### Persistence ###
helm upgrade ${persistence[name]} \
  --install \
  --wait \
  --debug \
  --set dockerhubName=$DOCKERHUB_NAME \
  --namespace $namespaceBravo \
  --set name=${persistence[name]} \
  --set namespace=$namespaceBravo \
  --set imageName=${persistence[imageName]} \
  --set port=${persistence[port]} \
  --set nodePoolName=${persistence[nodePoolName]} \
  --set newRelicAppName=${persistence[appName]} \
  --set newRelicLicenseKey=$NEWRELIC_LICENSE_KEY_BRAVO \
  "../charts/bravo-persistence-service"

### Proxy ###
helm upgrade ${proxy[name]} \
  --install \
  --wait \
  --debug \
  --set dockerhubName=$DOCKERHUB_NAME \
  --namespace $namespaceBravo \
  --set name=${proxy[name]} \
  --set namespace=$namespaceBravo \
  --set imageName=${proxy[imageName]} \
  --set nodePoolName=${proxy[nodePoolName]} \
  --set port=${proxy[port]} \
  "../charts/bravo-proxy-service"
#########