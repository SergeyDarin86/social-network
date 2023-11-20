package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationTopicHandler implements TopicHandler {
    private final NotificationService notificationService;
    @Value("${spring.kafka.topic.event-notifications}")
    private String notificationTopic;

    @Override
    public Timestamp getLastTimestamp() {
        log.info("NotificationTopicHandler: getLastTimestamp() startMethod");
        Timestamp lastTimestamp =  notificationService.getLastTimestamp();
        log.info("NotificationTopicHandler: getLastTimestamp() получен lastTimestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    @Override
    public String getTopic() {
        return notificationTopic;
    }

}
