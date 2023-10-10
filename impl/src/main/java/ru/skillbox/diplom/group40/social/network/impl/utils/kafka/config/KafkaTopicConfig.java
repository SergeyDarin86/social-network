package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    /** Блок отключения кафки    */
    /*
    @Bean   // @Рабочее
    public NewTopic getTopicSocket() {
        return TopicBuilder.name("notifications").build();
    }

    @Bean   // @Рабочее
    public NewTopic getTopicDto() {
        return TopicBuilder.name("notificationsdto").build();
    }

    @Bean   // @Рабочее
    public NewTopic getTopicDtoTest() {
        return TopicBuilder.name("notificationsdtos").build();
    }
    */
    /** Конец блока отключения кафки    */


}
