#!/bin/bash

createValue() {

  local endpoint=$1

  local randomValue=$(openssl rand -base64 12)
  local randomTag=$(openssl rand -base64 12)

  echo -e "---\n"

  curl -X POST "http://${endpoint}/create" \
    -i \
    -H "Content-Type: application/json" \
    -d \
    '{
        "value": "'"${randomValue}"'",
        "tag": "'"${randomTag}"'"
    }'

  echo -e "\n"
  sleep $REQUEST_INTERVAL
}

listValues() {

  local endpoint=$1

  echo -e "---\n"

  curl -X GET "http://${endpoint}/list" \
    -i \
    -H "Content-Type: application/json"

  echo -e "\n"
  sleep $REQUEST_INTERVAL
}

deleteValue() {

  local endpoint=$1

  echo -e "---\n"

  valueId=$(curl -X GET "http://${endpoint}/list?limit=1" \
    -H "Content-Type: application/json" \
    | jq -r .data.values[0].id)

  curl -X DELETE "http://${endpoint}/delete?id=${valueId}" \
    -i \
    -H "Content-Type: application/json"

  echo -e "\n"
  sleep $REQUEST_INTERVAL
}

####################
### SCRIPT START ###
####################

# Set variables
REQUEST_INTERVAL=2

bravoEndpoint="proxy.bravo.svc.cluster.local:8080/persistence"
charlieEndpoint="proxy.charlie.svc.cluster.local:8080/charlie/proxy/persistence"

# Create initial values
for i in {1..2}
do
  createValue $bravoEndpoint
  createValue $charlieEndpoint
done

# Start making requests
while true
do

  # Create value
  for i in {1..3}
  do
    createValue $bravoEndpoint
    createValue $charlieEndpoint
  done

  # List values
  for i in {1..5}
  do
    listValues $bravoEndpoint
    listValues $charlieEndpoint
  done

  # Delete value
  for i in {1..3}
  do
    deleteValue $bravoEndpoint
    deleteValue $charlieEndpoint
  done
done
