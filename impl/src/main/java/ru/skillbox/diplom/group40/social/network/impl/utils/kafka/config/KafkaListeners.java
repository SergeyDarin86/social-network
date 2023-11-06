package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.geo.GeoService;
import ru.skillbox.diplom.group40.social.network.impl.utils.technikalUser.TechnicalUserConfig;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;




import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class KafkaListeners extends AbstractConsumerSeekAware {
    @Autowired
    private GeoService geoService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private  TechnicalUserConfig technicalUserConfig;
    @Autowired
    private  AccountService accountService;

    @Autowired
    private MapperAccount mapperAccount;
    ConcurrentMap<String, Long> offsetsMap= new ConcurrentHashMap();
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;
    @Value("${spring.kafka.topic.socket-message}")
    private String socketTopic;
    @Value("${spring.kafka.topic.event-notifications}")
    private String notificationTopic;

    private ConsumerSeekCallback seekCallback;

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        this.seekCallback = callback;
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

        log.info("KafkaListeners: onPartitionsAssigned startMethod - получен TopicPartition из Map<TopicPartition, " +
                        "Long>: {}", assignments.keySet());

        //TODO: Проверяем имя топика и в соответствии с именем вытаскиваем нужное время
        Timestamp lastTimestamp;

        TopicPartition topicPartitionl = new ArrayList<>(assignments.keySet()).get(0);
        if(topicPartitionl.topic().equals(accountTopic)) {
            log.info("KafkaListeners: onPartitionsAssigned() - получен Topic: {}", accountTopic);
//        Timestamp lastTimestamp = Timestamp.from(accountService.getLastOnlineTime().toInstant());
        };

        if(topicPartitionl.topic().equals(notificationTopic)) {
            log.info("KafkaListeners: onPartitionsAssigned() - получен Topic: {}", notificationTopic);
//        Timestamp lastTimestamp = ;   // TODO: Определяем самое большее время нотификаций
        };

        if(topicPartitionl.topic().equals(socketTopic)) {
            log.info("KafkaListeners: onPartitionsAssigned() - получен Topic: {}", socketTopic);
//        Timestamp lastTimestamp = ;   // TODO: Определяем самое большее время между нотификациями и сообщениями
        };

        lastTimestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(1));   // TODO: Временный Random
        Long timestamp = lastTimestamp.getTime();
        log.info("KafkaListeners: onPartitionsAssigned()- получен Timestamp lastTimestamp: {}, Long timestamp: {}",
                lastTimestamp, timestamp);

        if (timestamp == null) {
            return;
        }
//        callback.seekToTimestamp(new ArrayList<>(assignments.keySet()), timestamp + 1);
        callback.seekToTimestamp(assignments.keySet(), timestamp + 1);
    }


    @KafkaListener(topics = "${spring.kafka.topic.adapter}", groupId = "geoAdapter")
    void geoLoad(String message){
        geoService.loadGeo(message);
    }

    @KafkaListener(topics="${spring.kafka.topic.event-notifications}", groupId = "groupIdDTO",
            containerFactory = "factoryNotificationDTO")
    void listenerNotification(NotificationDTO data) {
        log.info("KafkaListeners: listenerNotification(NotificationDTO data) startMethod - received data: {}", data);
        notificationService.create(data);
    }

    @KafkaListener(id = "socket", topics="${spring.kafka.topic.socket-message}", groupId = "groupId",
            containerFactory = "factoryEventNotification")
    void listener(SocketNotificationDTO data) {
        log.info("KafkaListeners: listener(SocketNotificationDTO data) - received data: {}", data);
        sendToWebsocket(data);
    }

    @KafkaListener(topics="${spring.kafka.topic.account}", groupId = "groupIdAccount",
            containerFactory = "factoryAccountDTO")
    void listener(ConsumerRecord<String, AccountOnlineDto> record, Acknowledgment acknowledgment) {
        AccountOnlineDto data = record.value();
        String key = record.key();
        long offset = record.offset();
        log.info("KafkaListeners: listener(ConsumerRecord<String, AccountOnlineDto> record) - received key: " +
                "{}, offset: {}, header {}, received data: {}", key, offset, record.headers(), data);
        technicalUserConfig.executeByTechnicalUser(
                ()->accountService.update(mapperAccount.AccountDtoFromAccountOnLineDto(record.value())));
        acknowledgment.acknowledge();
        log.info("KafkaListeners: listener(ConsumerRecord<String, AccountOnlineDto> record) - endMethod: ");
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

    private void updateOffsetMap(String topicName, long currentOffset) {
        log.info("KafkaListeners: updateOffsetMap() - startMethod, offsetsMap: {}", offsetsMap);

        long offset= offsetsMap.getOrDefault(topicName, Long.valueOf(-1));

        boolean isNew = false;
        if(offset==-1) {isNew = true;}

        if(isNew) {
            offsetsMap.put(topicName, currentOffset);
        } else {

            if(currentOffset != offset+1) {
                log.info("KafkaListeners: updateOffsetMap() - ошибка сравнений offset: {}, currentOffset: {}",
                        offset, currentOffset);
                kafkaService.setOffset();
            } else {
                offsetsMap.replace(topicName, currentOffset);
                log.info("KafkaListeners: updateOffsetMap() - выполнена корректная перезапись, offset: {}",
                        currentOffset);
            }

        }
        log.info("KafkaListeners: updateOffsetMap() - offsetMap после update: {}", offsetsMap);
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        for(TopicPartition topicPartition : assignments.keySet()){
            if(topicPartition.topic().equals("update.account.online")) {
                System.err.println("This topic is AccountOnlineDTO");
            };
        }
        */

        /*
        Set<TopicPartition> topicPartitionSet= assignments.keySet();
        List<TopicPartition> list = new ArrayList<>();
        list.addAll(topicPartitionSet);
        TopicPartition topicPartition = list.get(0);
        */