package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;
    @Value("${spring.kafka.topic.event-notifications}")
    private String eventNotificationsTopic;
    @Value("${spring.kafka.topic.socket-message}")
    private String socketTopic;

    @Bean
    public NewTopic getTopicSocket() {
        return TopicBuilder.name(socketTopic).build();
    }

    @Bean
    public NewTopic getTopicDto() {
        return TopicBuilder.name(eventNotificationsTopic).build();
    }

    @Bean
    public NewTopic getTopicAccountDto() {
        return TopicBuilder.name(accountTopic).build();
    }

}
