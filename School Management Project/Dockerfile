
# to build image enter: docker build -t jeancy/School-Management-Project:1.0 
# to build image enter :docker build -t school-management-project:0.0.1 .

# Start from the official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a smaller JRE base image for the final container
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/School-Management-Project-*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

