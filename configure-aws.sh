#!/bin/bash

# AWS Configuration Script for MSK Integration
# This script helps you configure AWS credentials and enable Kafka

echo "==================================================="
echo "   AWS Configuration for Kafka MSK Integration"
echo "==================================================="
echo ""

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
    echo "❌ AWS CLI is not installed. Installing now..."
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "/tmp/awscliv2.zip"
    cd /tmp && unzip -q awscliv2.zip && sudo ./aws/install
    cd -
    echo "✅ AWS CLI installed successfully"
else
    echo "✅ AWS CLI is already installed: $(aws --version)"
fi

echo ""
echo "Now, let's configure your AWS credentials."
echo ""
echo "You'll need:"
echo "  1. AWS Access Key ID"
echo "  2. AWS Secret Access Key"
echo ""
echo "Get these from: AWS Console → IAM → Users → Your User → Security Credentials"
echo ""

# Run AWS configure
aws configure

echo ""
echo "==================================================="
echo "Verifying AWS Configuration..."
echo "==================================================="

# Test AWS credentials
if aws sts get-caller-identity &> /dev/null; then
    echo "✅ AWS credentials are configured correctly!"
    echo ""
    aws sts get-caller-identity
    echo ""
    
    # Ask if user wants to enable Kafka
    echo ""
    echo "Do you want to enable Kafka integration now? (y/n)"
    read -r enable_kafka
    
    if [ "$enable_kafka" = "y" ] || [ "$enable_kafka" = "Y" ]; then
        # Update application.properties to enable Kafka
        sed -i 's/kafka.enabled=false/kafka.enabled=true/' src/main/resources/application.properties
        echo "✅ Kafka has been enabled in application.properties"
        echo ""
        echo "Restart your application with:"
        echo "   mvn spring-boot:run"
    else
        echo ""
        echo "ℹ️  To enable Kafka later, change kafka.enabled=true in application.properties"
    fi
else
    echo "❌ AWS credentials verification failed!"
    echo "Please check your Access Key and Secret Key and run 'aws configure' again."
fi

echo ""
echo "==================================================="
echo "Configuration Complete!"
echo "==================================================="
