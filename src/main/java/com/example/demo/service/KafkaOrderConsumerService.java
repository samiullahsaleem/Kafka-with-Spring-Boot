package com.example.demo.service;

import com.example.demo.model.Order;
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
public class KafkaOrderConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaOrderConsumerService.class);
    
    private final List<Order> consumedOrders = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "orders", groupId = "kafka-spring-demo-orders-group")
    public void consumeOrder(Order order) {
        logger.info("Consumed order: {} from customer: {}", order.getOrderId(), order.getCustomerName());
        consumedOrders.add(order);
    }

    public List<Order> getConsumedOrders() {
        return new ArrayList<>(consumedOrders);
    }

    public void clearOrders() {
        consumedOrders.clear();
        logger.info("Cleared all consumed orders");
    }
}
