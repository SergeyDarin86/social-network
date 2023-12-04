package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
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
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.TopicHandler;
import ru.skillbox.diplom.group40.social.network.impl.utils.technikalUser.TechnicalUserConfig;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaListeners extends AbstractConsumerSeekAware {
    private final NotificationService notificationService;
    private final GeoService geoService;
    private final AccountService accountService;

    private final List<TopicHandler> topicHandlerList;
    private final Map<String, TopicHandler> topicHandlersMap;
    private final WebSocketHandler webSocketHandler;
    private final NotificationsMapper notificationsMapper;
    private final MapperAccount mapperAccount;
    private final TechnicalUserConfig technicalUserConfig;

    ConcurrentMap<String, Long> offsetsMap= new ConcurrentHashMap();
    @Value("${spring.kafka.topic.account}")
    private String accountTopic;
    @Value("${spring.kafka.topic.socket-message}")
    private String socketTopic;
    @Value("${spring.kafka.topic.event-notifications}")
    private String notificationTopic;

    private ConsumerSeekCallback seekCallback;

    public KafkaListeners(
            List<TopicHandler> topicHandlerList,
            WebSocketHandler webSocketHandler,
            NotificationService notificationService,
            GeoService geoService,
            AccountService accountService,
            NotificationsMapper notificationsMapper,
            MapperAccount mapperAccount,
            TechnicalUserConfig technicalUserConfig
    ) {
        this.topicHandlerList = topicHandlerList;
        this.topicHandlersMap = this.topicHandlerList.stream().collect(Collectors.toMap(TopicHandler::getTopic,
                topicHandler -> topicHandler));
        this.webSocketHandler = webSocketHandler;
        this.notificationService = notificationService;
        this.geoService = geoService;
        this.accountService = accountService;
        this.notificationsMapper = notificationsMapper;
        this.mapperAccount = mapperAccount;
        this.technicalUserConfig = technicalUserConfig;
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        this.seekCallback = callback;
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

        log.info("KafkaListeners: onPartitionsAssigned startMethod - получен TopicPartition из " +
                "Map<TopicPartition, Long>: {}", assignments.keySet());

        setTimeTopic(assignments, callback,
                topicHandlersMap.getOrDefault(assignments.keySet().stream().findFirst().get().topic(),
                        topicHandlersMap.get("unknownTopic")).getLastTimestamp());
    }

    @KafkaListener(topics = "${spring.kafka.topic.adapter}", groupId = "geoAdapter")
    void geoLoad(String message){
        geoService.loadGeo(message);
    }

    @KafkaListener(topics="${spring.kafka.topic.event-notifications}", groupId = "groupIdDTO",
            containerFactory = "factoryNotificationDTO")
    void listenerNotification(NotificationDTO data, Acknowledgment acknowledgment) {
        log.info("KafkaListeners: listenerNotification(NotificationDTO data) startMethod - received data: {}", data);
        acknowledgment.acknowledge();
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

    private void setTimeTopic(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback, Timestamp lastTimestamp) {
        log.info("KafkaListeners: setTimeTopic startMethod- получена Map<TopicPartition, Long> assignments: {}," +
                " и время к которому переходим: {}", assignments, lastTimestamp);
        Long timestamp = lastTimestamp.getTime();
        callback.seekToTimestamp(assignments.keySet(), timestamp + 100);
        log.info("KafkaListeners: setTimeTopic endMethod- для Map<TopicPartition, Long> assignments: {} " +
                "выполнен переход на Long timestamp: {}", assignments, timestamp);
    }

    private boolean sendToWebsocket(SocketNotificationDTO socketNotificationDTO) {
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