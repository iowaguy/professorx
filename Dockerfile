FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openjdk-21-jdk maven swi-prolog
RUN echo "ulimit -c unlimited" >> /etc/profile

ADD ./ /app/
WORKDIR /app/
EXPOSE 12345

ENTRYPOINT make newest