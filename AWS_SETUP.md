# AWS MSK Setup Guide

## Problem: Application failing to start with "No resolvable bootstrap urls" error

This error occurs because AWS credentials are not configured in your environment, which are required for IAM authentication with Amazon MSK.

## Solution Options

### Option 1: Configure AWS Credentials (Recommended for production)

1. **Install AWS CLI** (if not already installed):
   ```bash
   curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
   unzip awscliv2.zip
   sudo ./aws/install
   ```

2. **Configure AWS credentials:**
   ```bash
   aws configure
   ```
   
   Enter the following when prompted:
   - AWS Access Key ID: `[Your Access Key]`
   - AWS Secret Access Key: `[Your Secret Key]`
   - Default region name: `us-east-1`
   - Default output format: `json`

3. **Verify credentials:**
   ```bash
   aws sts get-caller-identity
   ```

4. **Make sure kafka.enabled is set to true** in `application.properties`:
   ```properties
   kafka.enabled=true
   ```

5. **Restart the application:**
   ```bash
   mvn spring-boot:run
   ```

### Option 2: Use Environment Variables

Set AWS credentials as environment variables:

```bash
export AWS_ACCESS_KEY_ID="your-access-key"
export AWS_SECRET_ACCESS_KEY="your-secret-key"
export AWS_SESSION_TOKEN="your-session-token"  # if using temporary credentials
export AWS_REGION="us-east-1"

# Run the application
mvn spring-boot:run
```

### Option 3: Run without Kafka (Development Only)

If you want to test the application without Kafka:

1. **Disable Kafka** in `src/main/resources/application.properties`:
   ```properties
   kafka.enabled=false
   ```

2. **Restart the application:**
   ```bash
   mvn spring-boot:run
   ```

The application will start successfully, but Kafka endpoints will return a "service unavailable" message.

## Testing After Setup

Once AWS credentials are configured and Kafka is enabled:

1. **Check Kafka status:**
   ```bash
   curl http://localhost:8080/api/kafka/status
   ```

2. **Send a test message:**
   ```bash
   curl -X POST http://localhost:8080/api/kafka/send \
     -H "Content-Type: application/json" \
     -d '{"content": "Test message", "sender": "Developer"}'
   ```

3. **Retrieve consumed messages:**
   ```bash
   curl http://localhost:8080/api/kafka/messages
   ```

## Required AWS IAM Permissions

Your AWS credentials need the following permissions:

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
        "arn:aws:kafka:us-east-1:*:cluster/*/*",
        "arn:aws:kafka:us-east-1:*:topic/*/*"
      ]
    }
  ]
}
```

## Network Connectivity

Ensure that:
1. Your MSK cluster security group allows inbound traffic on port 9098
2. Your application can reach the MSK bootstrap servers
3. If using VPN or private networking, ensure proper routing is configured

## Troubleshooting

### Still getting connection errors after configuring credentials?

1. **Verify credentials are working:**
   ```bash
   aws kafka list-clusters --region us-east-1
   ```

2. **Check if you can reach the MSK endpoint:**
   ```bash
   telnet boot-hiz3iill.c1.kafka-serverless.us-east-1.amazonaws.com 9098
   ```

3. **Enable debug logging** in `application.properties`:
   ```properties
   logging.level.org.apache.kafka=DEBUG
   logging.level.software.amazon.msk=DEBUG
   ```

4. **Check AWS credentials location:**
   - Default location: `~/.aws/credentials`
   - Verify the file exists and has correct permissions

### Using IAM Role (EC2/Container)

If running on EC2 or in a container with an IAM role attached:
- No need to configure credentials manually
- Ensure the IAM role has the required MSK permissions
- The application will automatically use the instance profile
