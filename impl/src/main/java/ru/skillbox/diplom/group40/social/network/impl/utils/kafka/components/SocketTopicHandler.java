package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.MessageService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketTopicHandler implements TopicHandler {
    private final NotificationService notificationService;
    private final MessageService messageService;
    @Value("${spring.kafka.topic.socket-message}")
    private String socketTopic;

    @Override
    public Timestamp getLastTimestamp() {
        log.info("SocketTopicHandler: getLastTimestamp() startMethod");
        Timestamp lastTimestampNotification =  notificationService.getLastTimestamp();
        Timestamp lastTimestampMessage =  messageService.getLastTimestamp();
        log.info("SocketTopicHandler: getLastTimestamp() - получен lastTimestampNotification: {}," +
                " lastTimestampMessage: {}", lastTimestampNotification, lastTimestampMessage);
        Timestamp lastTimestamp = lastTimestampNotification.before(lastTimestampMessage) ?
                lastTimestampMessage : lastTimestampNotification;
        log.info("SocketTopicHandler: getLastTimestamp() получен lastTimestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    @Override
    public String getTopic() {
        return socketTopic;
    }

}
