package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForNotification;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.CustomJsonDeserializer;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.accountDTO.KafkaConsumerConfigAccount;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
//@EnableKafka
@Slf4j
@Component
public class KafkaListeners {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private AccountService accountService;
    ConcurrentMap<String, Long> offsetsMap= new ConcurrentHashMap();
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;

    /** Блок отключения кафки    */
//    /*

    @KafkaListener(topics="notificationsdto", groupId = "groupIdDTO", containerFactory = "factoryNotificationDTO")
    void listenerNotification(NotificationDTO data) {
        log.info("\nKafkaListeners: listenerNotification(NotificationDTO data) startMethod - received data: {}", data);
        notificationService.create(data);
    }

    @KafkaListener(id = "socket", topics="notifications", groupId = "groupId", containerFactory = "factoryEventNotification")
    void listener(SocketNotificationDTO data) {
        log.info("\nKafkaListeners: listener(SocketNotificationDTO data) - received data: {}", data);
        sendToWebsocket(data);
    }

    @KafkaListener(topics="update.account.online", groupId = "groupIdAccount", containerFactory = "factoryAccountDTO"
//            , partitionOffsets = @PartitionOffset(partition = "2",initialOffset = "100")
    )
    void listener(/*AccountDtoForNotification data*/ ConsumerRecord<String, AccountDtoForNotification> record) {

        AccountDtoForNotification data = record.value();
        String key = record.key();
        long offset = record.offset();
        log.info("\n\n\nKafkaListeners: listener(AccountDtoForNotification data) - received key: {}, offset: {}, " +
                "header {}, received data: {}\n\n\n", key, offset, record.headers(), data);

        updateOffsetMap(accountTopic, offset);

        /** Место для логики обработки полученного из топика сообщения*/
        /**                                                           */

    }

//    */
    /** Конец блока отключения кафки    */

    private void updateOffsetMap(String topicName, long currentOffset) {
        log.info("KafkaListeners: updateOffsetMap() - startMethod, offsetsMap: {}", offsetsMap);

        long offset= offsetsMap.getOrDefault(topicName, Long.valueOf(-1));

        // Переделать
        boolean isNew = false;
        if(offset==-1) {isNew = true;}
        //

        if(isNew) {
            offsetsMap.put(topicName, currentOffset);
        } else {

            if(currentOffset != offset+1) {
                log.info("KafkaListeners: updateOffsetMap() - ошибка сравнений offset: {}, currentOffset: {}",
                        offset, currentOffset);
            } else {
                offsetsMap.replace(topicName, currentOffset);
                log.info("KafkaListeners: updateOffsetMap() - выполнена корректная перезапись, offset: {}", currentOffset);
            }

        }
        log.info("KafkaListeners: updateOffsetMap() - offsetMap после update: {}", offsetsMap);
    }

    public boolean sendToWebsocket(SocketNotificationDTO socketNotificationDTO) {
        log.info("\nKafkaListeners: sendToWebsocket(SocketNotificationDTO) - received socketNotificationDTO: {}",
                socketNotificationDTO);

        List<WebSocketSession> sendingList = webSocketHandler.getSessionMap()
                .getOrDefault(socketNotificationDTO.getRecipientId(), new ArrayList<>());

        if (sendingList.isEmpty()) {return false;}

        try {
            webSocketHandler.handleTextMessage(sendingList.get(0),
                    new TextMessage(notificationsMapper.getJSON(socketNotificationDTO)));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}