package ru.skillbox.diplom.group40.social.network.impl.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplateNotification;
    private final KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateSocketNotificationDTO;
    private final KafkaTemplate<String, AccountOnlineDto> kafkaTemplateAccountDTO;
    private final KafkaTemplate<String, String> kafkaErrorTemplateAccountDTO;
    private final KafkaConsumer<String, AccountOnlineDto> kafkaConsumer;
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;
    @Value("${spring.kafka.topic.event-notifications}")
    private String eventNotificationsTopic;
    @Value("${spring.kafka.topic.socket-message}")
    private String socketTopic;


    public void sendNotification(NotificationDTO notificationDTO){
        log.info("KafkaService: sendNotification(NotificationDTO notificationDTO) startMethod, notificationDTO = {}",
                notificationDTO);
        kafkaTemplateNotification.send(eventNotificationsTopic, notificationDTO);
    }

    public void sendSocketNotificationDTO(SocketNotificationDTO socketNotificationDTO){
        log.info("KafkaService: sendNotification(SocketNotificationDTO socketNotificationDTO) startMethod, " +
                        "notificationDTO = {}", socketNotificationDTO);
        kafkaTemplateSocketNotificationDTO.send(socketTopic, socketNotificationDTO);
    }

    public void sendListSocketNotificationDTO(List<SocketNotificationDTO> listSocketNotificationDTO){
        log.info("KafkaService: sendListNotification(SocketNotificationDTO socketNotificationDTO) startMethod, " +
                "notificationDTO = {}", listSocketNotificationDTO);
        for(SocketNotificationDTO socketNotificationDTO : listSocketNotificationDTO) {
            kafkaTemplateSocketNotificationDTO.send(socketTopic, socketNotificationDTO);
        }
    }

    public void sendAccountDTO(AccountOnlineDto accountDto){
        log.info("KafkaService: sendAccountDTO(AccountDtoForNotification accountDto) startMethod, " +
                "accountDto = {}", accountDto);
        kafkaTemplateAccountDTO.send(accountTopic, accountDto);
    }

    public void sendAccountErrorDTO(String accountDto){
        log.info("KafkaService: sendERRORAccountDTO(AccountDtoForNotification accountDto) startMethod, " +
                "accountDto = {}", accountDto);
        kafkaErrorTemplateAccountDTO.send(accountTopic, accountDto);
    }

    public void setOffset() {
        TopicPartition topicPartition = new TopicPartition(accountTopic, 0);
        kafkaConsumer.assign(List.of(topicPartition));
        kafkaConsumer.seek(topicPartition, 75);
        log.info("KafkaService: setOffset() - Выполнена установка offset=75 топику update.account.online");
    }

    public void setNewOffset(String topicName, long offset) {
        TopicPartition topicPartition = new TopicPartition(topicName, 0);
        kafkaConsumer.assign(List.of(topicPartition));
        kafkaConsumer.seek(topicPartition, offset);
        log.info("KafkaService: setNewOffset() - Выполнена установка offset={} топику: {}", offset, topicName);
    }
}
