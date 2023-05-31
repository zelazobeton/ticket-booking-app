#!/bin/bash

echo "Building the Maven project..."
mvn clean package

# Check if the build was successful
if [ $? -ne 0 ]; then
  echo "Maven build failed. Exiting..."
  exit 1
fi

echo "Running the Spring Boot application..."
java -jar ./target/ticket-booking-0.0.1-SNAPSHOT.jar
