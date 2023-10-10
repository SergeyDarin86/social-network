package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;

@Slf4j
@RestController
@RequestMapping("api/v1/notifications/kafka/json/")
public class TestControllerKafkaAddSocketNotificationDTO {
    /*
    private KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateJSON;

    public TestControllerKafkaAddSocketNotificationDTO(KafkaTemplate<String, SocketNotificationDTO> kafkaTemplateJSON) {
        this.kafkaTemplateJSON = kafkaTemplateJSON;
    }

    @PostMapping
    public void publishes(@RequestBody SocketNotificationDTO socketNotificationDTO){
        kafkaTemplateJSON.send("notifications", socketNotificationDTO);
        log.info("\nMessageControllerEventNotificationTest: publishes(), - Success load test SocketNotificationDTO: {}",
                socketNotificationDTO);
    }
    */

}
