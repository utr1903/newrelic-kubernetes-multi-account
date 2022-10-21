FROM ubuntu:latest

RUN mkdir /app
COPY bashlogger.sh /app

RUN apt-get update
RUN apt-get install -y openssl

ENTRYPOINT ["bash", "/app/bashlogger.sh"]
