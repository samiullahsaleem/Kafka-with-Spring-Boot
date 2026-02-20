# Spring Boot Kafka Integration

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-3.6+-black.svg)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A production-ready Spring Boot microservice demonstrating event-driven architecture using Apache Kafka for real-time message streaming and order processing. Supports both local Kafka development and AWS MSK (Managed Streaming for Kafka) for production deployments.

## 📑 Table of Contents

- [Features](#-features)
- [Use Cases](#-use-cases)
- [Project Structure](#️-project-structure)
- [Technologies](#️-technologies)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [API Endpoints](#-api-endpoints)
- [Configuration](#️-configuration)
- [Architecture](#️-architecture)
- [Security](#-security)
- [Docker Deployment](#-docker-deployment)
- [Troubleshooting](#-troubleshooting)
- [Additional Resources](#-additional-resources)
- [Contributing](#-contributing)
- [License](#-license)

## 🎯 Features

- **Real-time Message Streaming** - Process messages asynchronously using Kafka topics
- **Order Processing System** - Complete order management with event-driven architecture
- **Multiple Kafka Configurations** - Support for local Kafka, AWS MSK with IAM, SASL/SCRAM, and plaintext
- **RESTful API** - Well-documented REST endpoints for producer and consumer operations
- **Auto-configuration** - Conditional bean loading based on Kafka availability
- **Email Notification Service** - Kafka consumer for sending order confirmation emails
- **Docker Support** - Local Kafka setup with Docker Compose including Kafka UI
- **Production Ready** - Configured for both development and production environments

## 📋 Use Cases

This application demonstrates common microservices patterns:

1. **Event-Driven Messaging** - Decouple services using asynchronous message passing
2. **Order Processing Pipeline** - Handle order creation, validation, and notifications
3. **Real-time Data Streaming** - Process and consume data in real-time
4. **Multi-Consumer Pattern** - Multiple consumers processing the same topic for different purposes
5. **Cloud-Native Architecture** - Deploy to AWS with MSK or run locally with Docker

## 🏗️ Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java                      # Application entry point
│   │   │   ├── config/
│   │   │   │   └── KafkaConfig.java                      # Kafka topic configuration
│   │   │   ├── controller/
│   │   │   │   ├── HelloController.java                  # Health check endpoints
│   │   │   │   └── KafkaController.java                  # Kafka messaging & order APIs
│   │   │   ├── model/
│   │   │   │   ├── Message.java                          # Message domain model
│   │   │   │   └── Order.java                            # Order domain model
│   │   │   └── service/
│   │   │       ├── KafkaProducerService.java             # Message producer
│   │   │       ├── KafkaConsumerService.java             # Message consumer
│   │   │       ├── KafkaOrderProducerService.java        # Order producer
│   │   │       ├── KafkaOrderConsumerService.java        # Order consumer
│   │   │       └── KafkaOrderSendEmailService.java       # Email notification service
│   │   └── resources/
│   │       ├── application.properties                     # Default configuration (local)
│   │       ├── application-msk.properties                 # AWS MSK with IAM
│   │       ├── application-sasl.properties                # SASL/SCRAM authentication
│   │       └── application-plaintext.properties           # Unauthenticated access
├── docker-compose.yml                                     # Local Kafka setup
├── start-local-kafka.sh                                   # Quick start script
└── pom.xml                                                # Maven dependencies
```

## 🛠️ Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.2.2 | Application framework |
| Spring Kafka | 3.1.1 | Kafka integration |
| Spring Data JPA | 3.2.2 | Database abstraction |
| Apache Kafka | 3.6+ | Message broker |
| AWS MSK IAM Auth | 2.1.1 | AWS authentication |
| SQLite | 3.45.0 | Embedded database |
| Docker | Latest | Local Kafka deployment |
| Maven | 3.6+ | Build & dependency management |

## 📦 Prerequisites

- **Java Development Kit (JDK)** 17 or higher
- **Maven** 3.6 or higher
- **Docker** (for local Kafka) or **AWS Account** (for MSK)

## 🚀 Quick Start

### Option 1: Local Development (Recommended)

1. **Clone the repository**
   ```bash
   git clone https://github.com/samiullahsaleem/Kafka-with-Spring-Boot.git
   cd Kafka-with-Spring-Boot
   ```

2. **Start local Kafka with Docker**
   ```bash
   ./start-local-kafka.sh
   ```
   This starts Kafka, Zookeeper, and Kafka UI at `http://localhost:8090`

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application: `http://localhost:8080`
   - Kafka UI: `http://localhost:8090`
   - API Documentation: See [API Endpoints](#-api-endpoints) below

### Option 2: AWS MSK Deployment

1. **Configure AWS credentials**
   ```bash
   aws configure
   # Or use IAM roles for EC2/ECS
   ```

2. **Update MSK configuration**
   
   Edit `src/main/resources/application-msk.properties`:
   ```properties
   spring.kafka.bootstrap-servers=your-msk-bootstrap-servers:9098
   ```

3. **Run with MSK profile**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=msk
   ```

See [KAFKA_AUTH_GUIDE.md](KAFKA_AUTH_GUIDE.md) for detailed authentication options.

## 📡 API Endpoints

### Health & Status

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hello` | GET | Application health check |
| `/api/health` | GET | Service status |
| `/api/kafka/status` | GET | Kafka connection status |

### Message Operations

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/kafka/send` | POST | Send custom message to Kafka |
| `/api/kafka/send-dummy` | GET | Send random test message |
| `/api/kafka/send-dummy-batch?count=N` | GET | Send N test messages (1-100) |
| `/api/kafka/consumed` | GET | Retrieve all consumed messages |
| `/api/kafka/messages` | GET | Alias for consumed messages |
| `/api/kafka/consumed` | DELETE | Clear consumed messages |

### Order Processing

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/kafka/send-dummy-order` | GET | Create and send test order |
| `/api/kafka/send-dummy-orders?count=N` | GET | Create N test orders (1-100) |
| `/api/kafka/orders` | GET | Retrieve all consumed orders |
| `/api/kafka/orders` | DELETE | Clear consumed orders |

### Example Usage

**Send a custom message:**
```bash
curl -X POST http://localhost:8080/api/kafka/send \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Order processing started",
    "sender": "Order Service"
  }'
```

**Send test order:**
```bash
curl http://localhost:8080/api/kafka/send-dummy-order
```

**Response:**
```json
{
  "status": "success",
  "message": "Dummy order sent to Kafka successfully!",
  "data": {
    "orderId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "customerName": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1-555-123-4567",
    "city": "New York",
    "location": "123 Main St",
    "stripePaymentId": "pi_1234567890abcdef",
    "paymentDate": "2026-02-20T10:30:00"
  }
}
```

**Retrieve consumed orders:**
```bash
curl http://localhost:8080/api/kafka/orders
```

**Send batch of test messages:**
```bash
curl "http://localhost:8080/api/kafka/send-dummy-batch?count=10"
```

## ⚙️ Configuration

### Environment Profiles

The application supports multiple Kafka configurations through Spring profiles:

| Profile | Authentication | Use Case | Port |
|---------|---------------|----------|------|
| **default** (local) | None | Local development | 9092 |
| **msk** | AWS IAM | AWS MSK Serverless/Provisioned | 9098 |
| **sasl** | SASL/SCRAM | Username/Password | 9096 |
| **plaintext** | None | MSK Provisioned (insecure) | 9092 |

### Switching Profiles

```bash
# Local Kafka (default)
mvn spring-boot:run

# AWS MSK with IAM
mvn spring-boot:run -Dspring-boot.run.profiles=msk

# SASL/SCRAM authentication
mvn spring-boot:run -Dspring-boot.run.profiles=sasl

# Environment variable
export SPRING_PROFILES_ACTIVE=msk
mvn spring-boot:run
```

### Key Configuration Properties

**application.properties** (default - local Kafka):
```properties
kafka.enabled=true
spring.kafka.bootstrap-servers=localhost:9092
kafka.topic.name=demo-topic
```

**application-msk.properties** (AWS MSK):
```properties
kafka.enabled=true
spring.kafka.bootstrap-servers=your-msk-cluster:9098
spring.kafka.properties.security.protocol=SASL_SSL
spring.kafka.properties.sasl.mechanism=AWS_MSK_IAM
```

## 🏛️ Architecture

### Message Flow

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   REST API  │─────▶│ Kafka Topic  │─────▶│  Consumer   │
└─────────────┘      │ (demo-topic) │      └─────────────┘
                     └──────────────┘
                            │
                            ▼
                     ┌──────────────┐
                     │ Kafka Topic  │
                     │  (orders)    │
                     └──────────────┘
                            │
                     ┌──────┴──────┐
                     ▼              ▼
              ┌──────────┐   ┌────────────┐
              │  Order   │   │   Email    │
              │ Consumer │   │  Service   │
              └──────────┘   └────────────┘
```

### Components

1. **Kafka Producer Services**
   - `KafkaProducerService` - Publishes messages to `demo-topic`
   - `KafkaOrderProducerService` - Publishes orders to `orders` topic

2. **Kafka Consumer Services**
   - `KafkaConsumerService` - Consumes messages from `demo-topic`
   - `KafkaOrderConsumerService` - Consumes orders from `orders` topic
   - `KafkaOrderSendEmailService` - Sends email notifications for orders

3. **Auto-configuration**
   - Services are conditionally loaded based on `kafka.enabled` property
   - Gracefully handles Kafka unavailability

## 🔒 Security

### AWS MSK IAM Authentication

Required IAM permissions for MSK access:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "kafka-cluster:Connect",
        "kafka-cluster:DescribeCluster",
        "kafka-cluster:WriteData",
        "kafka-cluster:ReadData",
        "kafka-cluster:CreateTopic",
        "kafka-cluster:DescribeTopic"
      ],
      "Resource": [
        "arn:aws:kafka:region:account-id:cluster/*/*",
        "arn:aws:kafka:region:account-id:topic/*/*/*"
      ]
    }
  ]
}
```

### SASL/SCRAM Configuration

For username/password authentication:

1. Store credentials in AWS Secrets Manager
2. Update `application-sasl.properties`:
   ```properties
   spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="your-username" password="your-password";
   ```

## 🐳 Docker Deployment

### Local Kafka Stack

The included Docker Compose sets up:
- **Apache Kafka** - Message broker (port 9092)
- **Zookeeper** - Kafka coordination (port 2181)
- **Kafka UI** - Web interface (port 8090)

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f kafka

# Stop services
docker-compose down
```

### Containerize the Application

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/kafka-spring-demo-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
mvn clean package
docker build -t kafka-spring-app .
docker run -p 8080:8080 kafka-spring-app
```

## 🔧 Troubleshooting

### Common Issues

**1. Kafka connection timeout**
```bash
# Check if Kafka is running
docker ps | grep kafka

# Restart local Kafka
docker-compose restart kafka
```

**2. AWS MSK authentication errors**
```bash
# Verify AWS credentials
aws sts get-caller-identity

# Check IAM permissions
aws kafka list-clusters --region us-east-1
```

**3. Port already in use**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or change application port in application.properties
server.port=8081
```

**4. Enable debug logging**
```properties
logging.level.org.apache.kafka=DEBUG
logging.level.org.springframework.kafka=DEBUG
logging.level.com.example.demo=DEBUG
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/api/health

# Kafka connection status
curl http://localhost:8080/api/kafka/status

# View Kafka UI
open http://localhost:8090
```

## 📚 Additional Resources

- **[Kafka Authentication Guide](KAFKA_AUTH_GUIDE.md)** - Detailed authentication methods
- **[AWS Setup Guide](AWS_SETUP.md)** - AWS MSK configuration steps
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Reference](https://docs.spring.io/spring-kafka/reference/html/)
- [AWS MSK Developer Guide](https://docs.aws.amazon.com/msk/latest/developerguide/what-is-msk.html)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Samiullah Saleem**
- GitHub: [@samiullahsaleem](https://github.com/samiullahsaleem)

## 🌟 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Kafka community for the robust messaging platform
- AWS for providing managed Kafka services

---

⭐ If you find this project useful, please consider giving it a star!

