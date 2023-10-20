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

    /** Блок отключения кафки    */
//    /*
    @Bean   // @Рабочее
    public NewTopic getTopicSocket() {
        return TopicBuilder.name("notifications").build();
    }

    @Bean   // @Рабочее
    public NewTopic getTopicDto() {
        return TopicBuilder.name("notificationsdto").build();
    }

    @Bean
    public NewTopic getTopicAccountDto() {
        return TopicBuilder.name("update.account.online").build();
    }

//    @Bean   // @Рабочее
//    public NewTopic getTopicDtoTest() {
//        return TopicBuilder.name("notificationsdtos")
//                .config(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
//                .config(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
//                .build();
//    }


//    */
    /** Конец блока отключения кафки    */


}
