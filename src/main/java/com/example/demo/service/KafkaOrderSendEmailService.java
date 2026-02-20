package com.example.demo.service;

import com.example.demo.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaOrderSendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaOrderSendEmailService.class);

    @KafkaListener(topics = "orders", groupId = "kafka-spring-demo-email-group")
    public void consumeOrderAndSendEmail(Order order) {
        logger.info("Processing order: {}", order.getOrderId());
        
        // Send email to customer
        String customerName = order.getCustomerName();
        String customerEmail = order.getEmail();
        
        logger.info("Email sent to customer: {} ({})", customerName, customerEmail);
        
        // Add your actual email sending logic here
    }
}
