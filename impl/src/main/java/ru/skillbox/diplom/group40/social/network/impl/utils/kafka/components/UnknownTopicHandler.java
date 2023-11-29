package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnknownTopicHandler implements TopicHandler {

    @Override
    public Timestamp getLastTimestamp() {
        log.info("UnknownTopicHandler: getLastTimestamp() startMethod");
        Timestamp lastTimestamp = new Timestamp(System.currentTimeMillis());
        log.info("UnknownTopicHandler: getLastTimestamp() получен lastTimestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    @Override
    public String getTopic() {
        return "unknownTopic";
    }

}
