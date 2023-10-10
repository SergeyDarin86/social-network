package ru.skillbox.diplom.group40.social.network.impl.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplateNotification;
    private final KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateSocketNotificationDTO;

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
}
