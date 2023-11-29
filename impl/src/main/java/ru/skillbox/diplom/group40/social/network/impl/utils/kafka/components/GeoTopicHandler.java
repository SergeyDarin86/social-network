package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeoTopicHandler implements TopicHandler {
    @Value("${spring.kafka.topic.adapter}")
    private String adapterTopic;

    @Override
    public Timestamp getLastTimestamp() {
        log.info("GeoTopicHandler: getLastTimestamp() startMethod");
        Timestamp lastTimestamp = new Timestamp(System.currentTimeMillis());
        log.info("GeoTopicHandler: getLastTimestamp() получен lastTimestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    @Override
    public String getTopic() {
        return adapterTopic;
    }

    }
