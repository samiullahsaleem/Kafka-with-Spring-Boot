package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConfig {

    @Value("${kafka.topic.name}")
    private String topicName;

    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .replicas(1)  // Changed to 1 for local Kafka (single broker)
                .build();
    }
}
