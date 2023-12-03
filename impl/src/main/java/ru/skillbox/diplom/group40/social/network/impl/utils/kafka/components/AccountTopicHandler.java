package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTopicHandler implements TopicHandler {
    private final AccountService accountService;
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;

    @Override
    public Timestamp getLastTimestamp() {
        log.info("AccountTopicHandler: getLastTimestamp() startMethod");
        Timestamp lastTimestamp = accountService.getLastOnlineTime();
        log.info("AccountTopicHandler: getLastTimestamp() получен lastTimestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    @Override
    public String getTopic() {
        return accountTopic;
    }

}
