# Stage 1: Compilazione con Maven e JDK 8
FROM registry.access.redhat.com/ubi8/openjdk-8:1.23-6.1793585664 AS builder

USER root
WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime immagine UBI8 OpenJDK 8
FROM registry.access.redhat.com/ubi8/openjdk-8-runtime:1.23

COPY --from=builder --chown=185:0 /build/target/kafka-springboot-producer-legacy-1.0.0.jar /deployments/app.jar

USER 185
