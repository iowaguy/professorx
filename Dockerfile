FROM ubuntu:22.04

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk
RUN echo "ulimit -c unlimited" >> /etc/profile
RUN apt-get install -y maven
RUN apt-get install -y swi-prolog

ADD ./ /app/
WORKDIR /app/
EXPOSE 12345

#ENTRYPOINT make test