FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openjdk-21-jdk maven swi-prolog
RUN echo "ulimit -c unlimited" >> /etc/profile

ADD ./ /app/
WORKDIR /app/
EXPOSE 12345

ARG VERSION=run24 #default, 2024 release
#ARG RUN_VERSION="run22" # 2022 release
#ARG RUN_VERSION="run20" # 2020 release

ENV VERSION=$VERSION

ENTRYPOINT make ${VERSION}
