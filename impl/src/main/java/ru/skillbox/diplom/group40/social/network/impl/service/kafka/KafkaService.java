package ru.skillbox.diplom.group40.social.network.impl.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplateNotification;
    private final KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateSocketNotificationDTO;
    private final KafkaTemplate<String, AccountDtoForNotification> kafkaTemplateAccountDTO;
    private final KafkaConsumer<String, AccountDtoForNotification> kafkaConsumer;

    public void sendNotification(NotificationDTO notificationDTO){
        log.info("\nKafkaService: sendNotification(NotificationDTO notificationDTO) startMethod, notificationDTO = {}",
                notificationDTO);
        kafkaTemplateNotification.send("notificationsdto", notificationDTO);
    }

    public void sendSocketNotificationDTO(SocketNotificationDTO socketNotificationDTO){
        log.info("\nKafkaService: sendNotification(SocketNotificationDTO socketNotificationDTO) startMethod, " +
                        "notificationDTO = {}", socketNotificationDTO);
        kafkaTemplateSocketNotificationDTO.send("notifications", socketNotificationDTO);
    }

    public void sendAccountDTO(AccountDtoForNotification accountDto){
        log.info("\n\n\nKafkaService: sendAccountDTO(AccountDtoForNotification accountDto) startMethod, " +
                "accountDto = {}\n\n\n", accountDto);
        kafkaTemplateAccountDTO.send("update.account.online", accountDto);
    }

    public void setOffset() {
        TopicPartition topicPartition = new TopicPartition("update.account.online", 0);
        kafkaConsumer.assign(List.of(topicPartition));
        kafkaConsumer.seek(topicPartition, 75);
        log.info("\nKafkaService: setOffset() - Выполнена установка offset=75 топику update.account.online");
    }

    public void setNewOffset(String topicName, long offset) {
        TopicPartition topicPartition = new TopicPartition(topicName, 0);
        kafkaConsumer.assign(List.of(topicPartition));
        kafkaConsumer.seek(topicPartition, offset);
        log.info("\nKafkaService: setNewOffset() - Выполнена установка offset={} топику: {}", offset, topicName);
    }
}
