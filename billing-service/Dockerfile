# Use OpenJDK 21 slim image
FROM openjdk:21-slim

# Install required dependencies
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    libfontconfig1

# Rest of your Dockerfile remains the same
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
RUN find /app/target -name "*.jar"
ENTRYPOINT ["sh", "-c", "java -jar $(find /app/target -name '*.jar')"]