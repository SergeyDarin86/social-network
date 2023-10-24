package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.accountDTO;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.CustomJsonDeserializer;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.KafkaErrorHandler;

import java.util.HashMap;
import java.util.Map;
@EnableKafka
@Configuration
public class KafkaConsumerConfigAccount {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfigJSONAccountDTO() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomJsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.skillbox.diplom.group40.social.network");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                "ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification");

        return props;
    }

    @Bean
    public ConsumerFactory<String, AccountDtoForNotification> consumerFactoryJSONAccountDTO() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigJSONAccountDTO());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AccountDtoForNotification>> factoryAccountDTO(
            ConsumerFactory<String, AccountDtoForNotification> consumerFactoryJSONAccountDTO) {
        ConcurrentKafkaListenerContainerFactory<String, AccountDtoForNotification> factoryAccountDTO =
                new ConcurrentKafkaListenerContainerFactory<>();
        factoryAccountDTO.setConsumerFactory(consumerFactoryJSONAccountDTO);

//        factoryAccountDTO.setErrorHandler(new KafkaErrorHandler());

        return factoryAccountDTO;
    }

        @Bean
    public KafkaConsumer<String, AccountDtoForNotification> kafkaConsumer() {
//            KafkaConsumer<String, AccountDtoForNotification> consumerAcc =
//                    new KafkaConsumer<String, AccountDtoForNotification>(consumerConfigJSONAccountDTO());

            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomJsonDeserializer.class);

            props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.skillbox.diplom.group40.social.network");
            props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                    "ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupIdAccount");
//            props.put(ConsumerConfig.CLIENT_ID_CONFIG, "groupIdAccount-0-1");

                    KafkaConsumer<String, AccountDtoForNotification> consumerAcc =
                    new KafkaConsumer<String, AccountDtoForNotification>(props);
        return consumerAcc;
    }

}
