package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.KafkaConsumerService;
import com.example.demo.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Value("${kafka.enabled:false}")
    private boolean kafkaEnabled;

    @Autowired(required = false)
    private KafkaProducerService producerService;

    @Autowired(required = false)
    private KafkaConsumerService consumerService;

    @GetMapping("/send-dummy")
    public ResponseEntity<Map<String, Object>> sendDummyMessage() {
        if (!kafkaEnabled || producerService == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Kafka is not enabled. Please start Kafka with ./start-local-kafka.sh");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        // Generate random dummy data
        String[] senders = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
        String[] contents = {
            "Hello from Kafka!",
            "This is a test message",
            "Spring Boot + Kafka is awesome!",
            "Learning message streaming",
            "Real-time data processing",
            "Microservices communication"
        };
        
        String randomSender = senders[(int) (Math.random() * senders.length)];
        String randomContent = contents[(int) (Math.random() * contents.length)];

        Message message = new Message(
                UUID.randomUUID().toString(),
                randomContent,
                randomSender,
                LocalDateTime.now()
        );

        producerService.sendMessage(message);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Dummy message sent to Kafka successfully!");
        response.put("data", message);
        response.put("info", "Check /api/kafka/consumed to see this message");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/send-dummy-batch")
    public ResponseEntity<Map<String, Object>> sendDummyBatch(@RequestParam(defaultValue = "5") int count) {
        if (!kafkaEnabled || producerService == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Kafka is not enabled. Please start Kafka with ./start-local-kafka.sh");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        if (count < 1 || count > 100) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Count must be between 1 and 100");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String[] senders = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Henry"};
        String[] contents = {
            "Hello from Kafka!",
            "This is a test message",
            "Spring Boot + Kafka is awesome!",
            "Learning message streaming",
            "Real-time data processing",
            "Microservices communication",
            "Event-driven architecture rocks!",
            "Distributed systems are complex",
            "Kafka handles millions of messages",
            "Asynchronous messaging patterns"
        };

        List<Message> sentMessages = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            String randomSender = senders[(int) (Math.random() * senders.length)];
            String randomContent = contents[(int) (Math.random() * contents.length)];

            Message message = new Message(
                    UUID.randomUUID().toString(),
                    randomContent + " #" + (i + 1),
                    randomSender,
                    LocalDateTime.now()
            );

            producerService.sendMessage(message);
            sentMessages.add(message);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", count + " dummy messages sent to Kafka successfully!");
        response.put("sentCount", count);
        response.put("messages", sentMessages);
        response.put("info", "Check /api/kafka/consumed to see consumed messages");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> request) {
        if (!kafkaEnabled || producerService == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Kafka is not enabled. Please configure AWS credentials and set kafka.enabled=true in application.properties");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        String content = request.get("content");
        String sender = request.getOrDefault("sender", "Anonymous");

        if (content == null || content.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Content cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                sender,
                LocalDateTime.now()
        );

        producerService.sendMessage(message);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Message sent to Kafka");
        response.put("data", message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumed")
    public ResponseEntity<Map<String, Object>> getConsumedMessages() {
        if (!kafkaEnabled || consumerService == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Kafka is not enabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        List<Message> messages = consumerService.getConsumedMessages();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalConsumed", messages.size());
        response.put("messages", messages);
        response.put("info", "These are all messages consumed from Kafka topic");
        response.put("actions", Map.of(
            "sendDummy", "GET /api/kafka/send-dummy",
            "clearMessages", "DELETE /api/kafka/consumed"
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages")
    public ResponseEntity<Map<String, Object>> getMessages() {
        // Alias for /consumed endpoint
        return getConsumedMessages();
    }

    @DeleteMapping("/consumed")
    public ResponseEntity<Map<String, String>> clearConsumedMessages() {
        if (!kafkaEnabled || consumerService == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Kafka is not enabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        int clearedCount = consumerService.getConsumedMessages().size();
        consumerService.clearMessages();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All consumed messages cleared");
        response.put("clearedCount", String.valueOf(clearedCount));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, String>> clearMessages() {
        // Alias for /consumed DELETE endpoint
        return clearConsumedMessages();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getKafkaStatus() {
        Map<String, Object> response = new HashMap<>();
        
        if (!kafkaEnabled || consumerService == null) {
            response.put("status", "disabled");
            response.put("service", "Kafka MSK Integration");
            response.put("message", "Kafka is not enabled. Configure AWS credentials to enable.");
            return ResponseEntity.ok(response);
        }

        response.put("status", "connected");
        response.put("service", "Kafka MSK Integration");
        response.put("consumedMessageCount", consumerService.getConsumedMessages().size());
        return ResponseEntity.ok(response);
    }
}
