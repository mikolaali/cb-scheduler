FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./target/currency-rate-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "currency-rate-0.0.1-SNAPSHOT.jar"]