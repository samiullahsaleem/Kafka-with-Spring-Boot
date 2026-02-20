package com.example.demo.service;

import com.example.demo.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaOrderProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaOrderProducerService.class);

    private static final String ORDERS_TOPIC = "orders";

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public KafkaOrderProducerService(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(Order order) {
        logger.info("Sending order to topic {}: {}", ORDERS_TOPIC, order.getOrderId());
        
        CompletableFuture<SendResult<String, Order>> future = kafkaTemplate.send(ORDERS_TOPIC, order.getOrderId(), order);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Order sent successfully: [{}] with offset [{}]", 
                    order.getOrderId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send order: [{}] due to: {}", order.getOrderId(), ex.getMessage());
            }
        });
    }
}
