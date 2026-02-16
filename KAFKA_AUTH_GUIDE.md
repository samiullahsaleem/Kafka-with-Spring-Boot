# Kafka Authentication Options Guide

This guide shows all the ways you can connect to Kafka, from easiest to most complex.

## 🎯 Quick Comparison

| Method | Difficulty | Use Case | AWS Required |
|--------|-----------|----------|--------------|
| **Local Kafka** | ⭐ Easiest | Development | ❌ No |
| **PLAINTEXT** | ⭐⭐ Easy | MSK Provisioned only | ✅ Yes |
| **SASL/SCRAM** | ⭐⭐⭐ Medium | Username/Password | ✅ Yes |
| **IAM** | ⭐⭐⭐⭐ Complex | MSK Serverless | ✅ Yes |

---

## 1️⃣ Local Kafka (Recommended for Development) ✨

**No AWS, no credentials, no hassle!**

### Setup

```bash
# Start local Kafka with Docker
./start-local-kafka.sh
```

Or manually:
```bash
docker-compose up -d
```

### Run Your App

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Features
- ✅ Kafka at `localhost:9092`
- ✅ Kafka UI at `http://localhost:8090`
- ✅ No authentication needed
- ✅ Perfect for development

### Stop Kafka
```bash
docker-compose down
```

---

## 2️⃣ MSK Provisioned with PLAINTEXT (Easiest AWS Option)

**Requirements:**
- MSK Provisioned cluster (not Serverless)
- Unauthenticated access enabled
- Port 9092

### Setup

1. In AWS Console, create/modify your MSK cluster to allow unauthenticated access
2. Get your bootstrap servers (port 9092)
3. Update `application-plaintext.properties`:
   ```properties
   spring.kafka.bootstrap-servers=your-broker:9092
   ```

### Run Your App
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=plaintext
```

⚠️ **Note:** MSK Serverless does NOT support this method.

---

## 3️⃣ MSK with SASL/SCRAM (Username/Password)

**Requirements:**
- MSK Provisioned cluster
- SASL/SCRAM authentication enabled in MSK
- Username/password stored in AWS Secrets Manager
- Port 9096

### Setup

1. Enable SASL/SCRAM in your MSK cluster settings
2. Create username/password in AWS Secrets Manager
3. Update `application-sasl.properties`:
   ```properties
   spring.kafka.bootstrap-servers=your-broker:9096
   spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="myuser" password="mypassword";
   ```

### Run Your App
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=sasl
```

---

## 4️⃣ MSK with IAM Authentication (Your Current Setup)

**Requirements:**
- MSK Serverless or Provisioned with IAM
- AWS credentials configured
- Port 9098

### Setup

1. Configure AWS credentials:
   ```bash
   aws configure
   # Or use: ./configure-aws.sh
   ```

2. Enable Kafka in `application.properties`:
   ```properties
   kafka.enabled=true
   ```

3. Run your app:
   ```bash
   mvn spring-boot:run
   ```

---

## 🔄 Switching Between Configurations

### Method 1: Use Spring Profiles

```bash
# Local Kafka
mvn spring-boot:run -Dspring-boot.run.profiles=local

# PLAINTEXT
mvn spring-boot:run -Dspring-boot.run.profiles=plaintext

# SASL/SCRAM
mvn spring-boot:run -Dspring-boot.run.profiles=sasl

# IAM (default)
mvn spring-boot:run
```

### Method 2: Environment Variable

```bash
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

---

## 📊 My Recommendation

**For Development:**
- Use **Local Kafka** (easiest, no AWS needed)
- Run: `./start-local-kafka.sh`
- Then: `mvn spring-boot:run -Dspring-boot.run.profiles=local`

**For Production:**
- If using MSK Serverless → Must use **IAM**
- If using MSK Provisioned → Use **SASL/SCRAM** (simpler than IAM)
- Avoid PLAINTEXT in production (security risk)

---

## 🚀 Quick Start (Easiest Path)

```bash
# 1. Start local Kafka
./start-local-kafka.sh

# 2. Wait for it to start (about 10 seconds)

# 3. Run your app with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# 4. Test it
curl -X POST http://localhost:8080/api/kafka/send \
  -H "Content-Type: application/json" \
  -d '{"content": "Hello Local Kafka!", "sender": "Me"}'

# 5. View messages
curl http://localhost:8080/api/kafka/messages

# 6. View Kafka UI in browser
# Open: http://localhost:8090
```

---

## ❓ FAQ

**Q: Can I use MSK Serverless without IAM?**  
A: No, MSK Serverless only supports IAM authentication.

**Q: What's the easiest for development?**  
A: Local Kafka with Docker (no AWS needed).

**Q: What if I don't have Docker?**  
A: You'll need to install Docker, or set up MSK with IAM authentication.

**Q: Which is most secure?**  
A: IAM > SASL/SCRAM > PLAINTEXT (never use PLAINTEXT in production)
