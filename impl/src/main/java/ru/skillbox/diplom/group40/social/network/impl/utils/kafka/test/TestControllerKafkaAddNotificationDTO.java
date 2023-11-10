package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.test;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/notifications/kafka/json/notificationdto")
public class TestControllerKafkaAddNotificationDTO {

    private KafkaTemplate<String, NotificationDTO> kafkaTemplateJSONNotificationDTO;

    public TestControllerKafkaAddNotificationDTO(KafkaTemplate<String, NotificationDTO> kafkaTemplateJSONNotificationDTO) {
        this.kafkaTemplateJSONNotificationDTO = kafkaTemplateJSONNotificationDTO;
    }

    @GetMapping
    public void published(){

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(UUID.randomUUID());
        notificationDTO.setContent("test kafka content");
        notificationDTO.setNotificationType(Type.POST);
        notificationDTO.setSentTime(ZonedDateTime.now());

        kafkaTemplateJSONNotificationDTO.send("notificationsdto", notificationDTO);
    }

    @GetMapping("/withoutdata")
    public void publishedN(){

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setAuthorId(UUID.randomUUID());
        notificationDTO.setContent("test kafka content without LocalDateTime");
        notificationDTO.setNotificationType(Type.POST);

        kafkaTemplateJSONNotificationDTO.send("notificationsdtos", notificationDTO);
    }

}
