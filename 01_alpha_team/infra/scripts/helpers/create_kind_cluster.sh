#!/bin/bash

# Deploy kind clusters
kind create cluster \
  --name test \
  --config create_kind_cluster-config.yaml \
  --image=kindest/node:v1.24.0
