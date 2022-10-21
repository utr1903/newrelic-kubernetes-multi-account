#!/bin/bash

# Logging service account of team1
# -> on pods of namespace "team1"
# -> should be YES
kubectl auth can-i get pods -n team1 --as system:serviceaccount:team1:nri-logging-newrelic-logging

# Logging service account of team1
# -> on pods of namespace kube-system
# -> should be NO
kubectl auth can-i get pods -n kube-system --as system:serviceaccount:team1:nri-logging-newrelic-logging
