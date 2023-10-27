package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.webSocketDTO;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.CustomJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigWebsocket {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfigJSON() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, SocketNotificationDTO> producerFactoryJSON() {
        return new DefaultKafkaProducerFactory<>(producerConfigJSON());
    }

    @Bean
    public KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateJSON(ProducerFactory<String,
            SocketNotificationDTO> producerFactoryJSON) {
        return new KafkaTemplate<>(producerFactoryJSON);
    }

}
