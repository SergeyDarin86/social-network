package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.countryDTO;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfigGeo {


    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;


    private String kafkaGroupId="geoAdapter";

    @Bean
    public Map<String, Object> consumerConfigsGeo() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactoryGeo() {
        ConcurrentKafkaListenerContainerFactory<String, CountryDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGeo());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CountryDto> consumerFactoryGeo() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigsGeo());
    }


}
