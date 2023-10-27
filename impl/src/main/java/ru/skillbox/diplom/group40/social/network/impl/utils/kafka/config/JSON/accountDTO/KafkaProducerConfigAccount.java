package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.accountDTO;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.CustomJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducerConfigAccount {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfigJSONAccountDTO() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, AccountOnlineDto> producerFactoryJSONAccountDTO() {
        return new DefaultKafkaProducerFactory<>(producerConfigJSONAccountDTO());
    }

    @Bean
    public KafkaTemplate<String, AccountOnlineDto> kafkaTemplateJSONAccountDTO(
            ProducerFactory<String, AccountOnlineDto> producerFactoryJSONAccountDTO) {
        return new KafkaTemplate<>(producerFactoryJSONAccountDTO);
    }

}
