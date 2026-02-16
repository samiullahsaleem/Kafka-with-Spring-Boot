package com.example.demo.service;

import com.example.demo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final List<Message> consumedMessages = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Message message) {
        logger.info("Consumed message: {}", message);
        consumedMessages.add(message);
    }

    public List<Message> getConsumedMessages() {
        return new ArrayList<>(consumedMessages);
    }

    public void clearMessages() {
        consumedMessages.clear();
        logger.info("Cleared all consumed messages");
    }
}
