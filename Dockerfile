FROM eclipse-temurin:17-jdk-jammy

RUN mkdir /app

WORKDIR /app

ADD entrypoint /app/entrypoint
ADD target/*-SNAPSHOT.jar /app

ENTRYPOINT ["/bin/bash", "/app/entrypoint"]
