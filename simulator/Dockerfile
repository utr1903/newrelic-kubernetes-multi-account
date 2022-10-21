FROM debian:stable

RUN apt-get update -y
RUN apt-get install dnsutils -y
RUN apt-get install curl -y
RUN apt-get install vim -y
RUN apt-get install jq -y

RUN mkdir /simulator
COPY simulate.sh /simulator/.

CMD bash /simulator/simulate.sh
