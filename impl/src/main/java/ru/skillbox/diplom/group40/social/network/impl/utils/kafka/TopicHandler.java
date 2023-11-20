package ru.skillbox.diplom.group40.social.network.impl.utils.kafka;

import java.sql.Timestamp;

public interface TopicHandler {

    Timestamp getLastTimestamp();

    String getTopic();

}
