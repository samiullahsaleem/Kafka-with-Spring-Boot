#!/bin/bash

echo "==================================================="
echo "   Setting Up Local Kafka (No AWS Required!)"
echo "==================================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

echo "✅ Docker is running"
echo ""
echo "Starting Kafka with Docker Compose..."
echo ""

# Start Kafka
docker-compose up -d

echo ""
echo "Waiting for Kafka to be ready..."
sleep 10

# Check if Kafka is running
if docker ps | grep -q kafka; then
    echo ""
    echo "=================================================="
    echo "✅ Local Kafka is running!"
    echo "=================================================="
    echo ""
    echo "Services:"
    echo "  🐋 Kafka Broker: localhost:9092"
    echo "  📊 Kafka UI: http://localhost:8090"
    echo ""
    echo "Now run your Spring Boot app with local profile:"
    echo "  mvn spring-boot:run -Dspring-boot.run.profiles=local"
    echo ""
    echo "Or set environment variable:"
    echo "  export SPRING_PROFILES_ACTIVE=local"
    echo "  mvn spring-boot:run"
    echo ""
    echo "To stop Kafka:"
    echo "  docker-compose down"
    echo ""
else
    echo "❌ Failed to start Kafka"
    echo "Check logs with: docker-compose logs"
fi
