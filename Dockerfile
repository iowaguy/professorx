FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk
RUN apt-get install -y maven
RUN apt-get install -y swi-prolog

ADD ../ /app/
WORKDIR /app/

ENTRYPOINT make new