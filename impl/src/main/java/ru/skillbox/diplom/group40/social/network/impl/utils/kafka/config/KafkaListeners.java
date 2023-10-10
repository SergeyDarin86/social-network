package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KafkaListeners {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private NotificationsMapper notificationsMapper;

    // Блок отключения кафки
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
    // Конец блока отключения кафки

    //  TODO: Может сделать сервис вебсокет и вынести в него метод, чтобы не подвешивать в случае ошибок сокет и он не долбил повторно тем же сообщением которое нет возможности обработать?
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
    //

}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        //1
        /*
        try {
            webSocketHandler.handleTextMessage(webSocketHandler.getSessionList().get(0),
                    new TextMessage(notificationsMapper.getJSON(socketNotificationDTO)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */
        //1





    /*
    @KafkaListener(topics="notificationsdtos", groupId = "groupIdDTOs"
            , containerFactory = "factoryNotificationDTO"
    )
    void listenerdtotest(NotificationDTO data) {
        log.info("\nKafkaListeners: listenerdtos(NotificationDTO data) without LOCALTIME- received data: {}", data);
    }
    */




//    /*
//    */


//        Gson g = new Gson();
//        NotificationDTO notificationDTO = g.fromJson(data, NotificationDTO.class);
//        log.info("Listener received data(NotificationDTO notificationDTO): {}", notificationDTO);


//        Gson g = new Gson();
//        EventNotification eNot = g.fromJson(data, EventNotification.class);
//        log.info("Listener received data(EventNotification eNot): {}", eNot);


//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
//                .create();
//        NotificationDTO notificationDTOgson = gson.fromJson(data, NotificationDTO.class);
//        log.info("Listener received data(NotificationDTO notificationDTO): {}", notificationDTOgson);



//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, EventNotification> kafkaListenerContainerFactoryEN() {
//        ConcurrentKafkaListenerContainerFactory<String, EventNotification> factoryEN = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryEN.setConsumerFactory(dataConsumerFactory());
//        return factoryEN;
//    }