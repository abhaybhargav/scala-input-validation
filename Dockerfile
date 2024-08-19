# Use OpenJDK 11 as the base image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container
COPY . /app

# Install sbt
RUN apt-get update && \
    apt-get install -y curl && \
    curl -L -o sbt-1.6.2.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-1.6.2.deb && \
    dpkg -i sbt-1.6.2.deb && \
    rm sbt-1.6.2.deb && \
    apt-get update && \
    apt-get install -y sbt

# Build the application
RUN sbt clean assembly

# Expose the port the app runs on
EXPOSE 8880

# Run the app
CMD ["java", "-jar", "target/scala-2.13/scala-validation-demo-assembly-0.1.jar"]