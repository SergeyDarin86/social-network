package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.notificationDTO;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.CustomJsonDeserializer;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.KafkaErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfigNotification {

    @Value("${spring.kafka.bootstrap-service}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfigJSONNotificationDTO() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, /*JsonDeserializer.class*/ CustomJsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.skillbox.diplom.group40.social.network");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                "ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO");

        return props;
    }

    @Bean
    public ConsumerFactory<String, NotificationDTO> consumerFactoryJSONNotificationDTO() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigJSONNotificationDTO());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, NotificationDTO>> factoryNotificationDTO(
            ConsumerFactory<String, NotificationDTO/*String*/> consumerFactoryJSONNotificationDTO) {
        ConcurrentKafkaListenerContainerFactory<String, NotificationDTO> factoryNotificationDTO =
                new ConcurrentKafkaListenerContainerFactory<>();
        factoryNotificationDTO.setConsumerFactory(consumerFactoryJSONNotificationDTO);

        factoryNotificationDTO.setErrorHandler(new KafkaErrorHandler());

        return factoryNotificationDTO;
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS