#!/bin/bash

simulatorName="simulator"

# Build & Push
docker build \
  --platform linux/amd64 \
  --tag "${DOCKERHUB_NAME}/${simulatorName}" \
  "."
docker push "${DOCKERHUB_NAME}/${simulatorName}"

kubectl run --image="${DOCKERHUB_NAME}/${simulatorName}" "${simulatorName}"
