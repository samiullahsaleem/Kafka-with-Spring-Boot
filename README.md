# Kafka-with-Spring-Boot

A Spring Boot application integrated with **Amazon MSK (Managed Streaming for Kafka)** with RESTful API, Kafka messaging, and SQLite database.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java              # Main application class
│   │   │   ├── config/
│   │   │   │   └── KafkaConfig.java              # Kafka configuration
│   │   │   ├── controller/
│   │   │   │   ├── HelloController.java          # Basic REST endpoints
│   │   │   │   └── KafkaController.java          # Kafka REST endpoints
│   │   │   ├── model/
│   │   │   │   └── Message.java                  # Message model
│   │   │   └── service/
│   │   │       ├── KafkaProducerService.java     # Kafka producer
│   │   │       └── KafkaConsumerService.java     # Kafka consumer
│   │   └── resources/
│   │       └── application.properties            # Configuration file
│   └── test/
├── pom.xml                                        # Maven dependencies
└── database.db                                    # SQLite database (auto-generated)
```

## Technologies Used

- **Spring Boot 3.2.2** - Application framework
- **Spring Web** - RESTful API support
- **Spring Kafka** - Kafka integration
- **Amazon MSK** - Managed Kafka service with IAM authentication
- **Spring Data JPA** - Database access layer
- **SQLite** - Lightweight relational database
- **Maven** - Dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- AWS Account with MSK Serverless cluster configured
- AWS credentials configured (for IAM authentication)
  - Option 1: AWS CLI configured (`aws configure`)
  - Option 2: IAM role attached to EC2/container
  - Option 3: Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)

## How to Run

1. **Configure AWS Credentials** (if not already done):
   ```bash
   aws configure
   # Enter your AWS Access Key ID, Secret Access Key, and region
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will start on `http://localhost:8080`

## API Endpoints

### Basic Endpoints

#### Hello World API
- **URL**: `/api/hello`
- **Method**: GET
- **Response**:
  ```json
  {
    "message": "Hello World!",
    "status": "success"
  }
  ```

#### Health Check API
- **URL**: `/api/health`
- **Method**: GET
- **Response**:
  ```json
  {
    "status": "UP",
    "service": "Kafka Spring Boot Demo"
  }
  ```

### Kafka Endpoints

#### Send Message to Kafka
- **URL**: `/api/kafka/send`
- **Method**: POST
- **Request Body**:
  ```json
  {
    "content": "Your message here",
    "sender": "John Doe"
  }
  ```
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Message sent to Kafka",
    "data": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "content": "Your message here",
      "sender": "John Doe",
      "timestamp": "2026-02-16T10:30:00"
    }
  }
  ```

#### Get Consumed Messages
- **URL**: `/api/kafka/messages`
- **Method**: GET
- **Response**:
  ```json
  {
    "status": "success",
    "count": 2,
    "messages": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "content": "First message",
        "sender": "John Doe",
        "timestamp": "2026-02-16T10:30:00"
      },
      {
        "id": "660f9511-f30c-52e5-b827-557766551111",
        "content": "Second message",
        "sender": "Jane Smith",
        "timestamp": "2026-02-16T10:31:00"
      }
    ]
  }
  ```

#### Clear Consumed Messages
- **URL**: `/api/kafka/messages`
- **Method**: DELETE
- **Response**:
  ```json
  {
    "status": "success",
    "message": "All consumed messages cleared"
  }
  ```

#### Kafka Connection Status
- **URL**: `/api/kafka/status`
- **Method**: GET
- **Response**:
  ```json
  {
    "status": "connected",
    "service": "Kafka MSK Integration",
    "consumedMessageCount": 5
  }
  ```

## Testing the API

### Basic Endpoints
Using curl:
```bash
# Hello World endpoint
curl http://localhost:8080/api/hello

# Health check endpoint
curl http://localhost:8080/api/health
```

### Kafka Endpoints
Using curl:
```bash
# Send a message to Kafka
curl -X POST http://localhost:8080/api/kafka/send \
  -H "Content-Type: application/json" \
  -d '{"content": "Hello Kafka!", "sender": "Test User"}'

# Get all consumed messages
curl http://localhost:8080/api/kafka/messages

# Check Kafka connection status
curl http://localhost:8080/api/kafka/status

# Clear consumed messages
curl -X DELETE http://localhost:8080/api/kafka/messages
```

## Amazon MSK Configuration

### Current Setup
This application is configured to connect to Amazon MSK Serverless with IAM authentication.

**Bootstrap Server**: `boot-hiz3iill.c1.kafka-serverless.us-east-1.amazonaws.com:9098`  
**Topic**: `demo-topic`  
**Authentication**: AWS IAM  
**Security Protocol**: SASL_SSL

### How It Works
1. **Producer**: The `/api/kafka/send` endpoint accepts messages and sends them to the MSK topic
2. **Consumer**: A Kafka listener automatically consumes messages from the topic in the background
3. **Storage**: Consumed messages are stored in-memory and can be retrieved via `/api/kafka/messages`

### Required AWS Permissions
Ensure your AWS credentials have the following permissions for MSK:
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
      "Resource": "*"
    }
  ]
}
```

### Changing Configuration
To connect to a different MSK cluster, update the following in [src/main/resources/application.properties](src/main/resources/application.properties):
- `spring.kafka.bootstrap-servers` - Your MSK bootstrap servers
- `kafka.topic.name` - Your desired topic name

## Database Configuration

The application is configured to use SQLite database. The database file (`database.db`) will be created automatically in the project root directory when the application starts.

Configuration can be found in [src/main/resources/application.properties](src/main/resources/application.properties).

## Troubleshooting

### Kafka Connection Issues
1. **Authentication Errors**: Verify your AWS credentials are configured correctly
2. **Network Issues**: Ensure your security groups allow traffic on port 9098
3. **Permission Issues**: Verify IAM permissions for MSK operations

### Checking Logs
Enable debug logging by adding to application.properties:
```properties
logging.level.org.apache.kafka=DEBUG
logging.level.org.springframework.kafka=DEBUG
```

## Next Steps

- Add persistent storage for consumed messages using SQLite
- Implement message filtering and search
- Add more complex message processing logic
- Set up multiple topics for different message types
- Add monitoring and metrics

## License

This is a learning project for Kafka integration with Spring Boot.
