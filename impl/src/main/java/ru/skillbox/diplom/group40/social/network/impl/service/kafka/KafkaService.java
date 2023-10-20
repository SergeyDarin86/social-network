package ru.skillbox.diplom.group40.social.network.impl.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplateNotification;
    private final KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateSocketNotificationDTO;
    private final KafkaTemplate<String, AccountDtoForNotification> kafkaTemplateAccountDTO;

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
}
