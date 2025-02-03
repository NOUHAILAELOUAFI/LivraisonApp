# Use OpenJDK 21 slim image
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Grant permissions to the mvnw script
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Find the exact jar file name
RUN find /app/target -name "*.jar"

# Use the specific jar file name in the ENTRYPOINT
ENTRYPOINT ["sh", "-c", "java -jar $(find /app/target -name '*.jar')"]