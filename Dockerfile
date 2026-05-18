# Build stage
FROM amazoncorretto:25 AS build
WORKDIR /app

# Install tar and gzip required by Maven Wrapper
RUN yum install -y tar gzip

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Make sure mvnw is executable
RUN chmod +x ./mvnw

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline. 
# This is a separate step so the dependencies will be cached unless the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Run stage
FROM amazoncorretto:25
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
