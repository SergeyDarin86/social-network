package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.ReadStatus;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/websocket/add")
public class TestControllerWS {
    ObjectMapper mapper = new ObjectMapper();

    private final NotificationsMapper notificationsMapper;
    private final NotificationService notificationService;
    private final KafkaService kafkaService;

    @PostMapping
    public void publishedTest(@RequestBody SocketNotificationDTO socketNotificationDTO) throws Exception {

        if(socketNotificationDTO.getType().equals("NOTIFICATION")) {
            log.info("\nПолучен POST запрос с type=NOTIFICATION: {}", socketNotificationDTO);
            notificationService.addNotification(notificationsMapper.getEventNotificationDTO(socketNotificationDTO));
            kafkaService.sendSocketNotificationDTO(socketNotificationDTO);
        }

        if(socketNotificationDTO.getType().equals("MESSAGE")) {
            log.info("\nПолучен POST запрос с type=MESSAGE: {}", socketNotificationDTO);
            /**
            Для Антона хвост для тестового запроса HTTP
            */
        }

    }

    @GetMapping
    public void testDtoMessage() throws Exception {
        kafkaService.sendSocketNotificationDTO(getWebsocketDtoMessage());
    }

    public String getJSONEvNotDTO() {

        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(Type.LIKE.toString());
        evNotificationDTO.setContent("test");
        evNotificationDTO.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));
        evNotificationDTO.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));

        String jsonDTOString = null;
        try {
            jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(jsonDTOString);
        return jsonDTOString;
    }

    public SocketNotificationDTO getWebsocketDtoMessage() {

        MessageDto messageDto = new MessageDto();
        messageDto.setTime(LocalDateTime.now());
        messageDto.setConversationPartner1(UUID.fromString("65918f1f-3e1e-4833-8d85-4b83389d6a10"));
        messageDto.setConversationPartner2(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));
        messageDto.setMessageText("test DTO with Message");
        messageDto.setReadStatus(ReadStatus.SENT);
        messageDto.setDialogId(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));

        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData(messageDto);
        socketNotificationDTO.setType("MESSAGE");
        socketNotificationDTO.setRecipientId(UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9"));

        log.info("\nСформирована Notification DTO с с type=Message: {}", socketNotificationDTO);
        return socketNotificationDTO;
    }

}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////








/*
package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

        import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.web.bind.annotation.*;
        import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
        import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
        import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
        import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
        import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/websocket/add")
public class TestControllerWS {
    ObjectMapper mapper = new ObjectMapper();

    //    /*
//    3
    private final NotificationsMapper notificationsMapper;
    private final NotificationService notificationService;
    private final KafkaService kafkaService;

    @PostMapping
    public void publishedTest(@RequestBody SocketNotificationDTO socketNotificationDTO) throws Exception {

        if(socketNotificationDTO.getType().equals("NOTIFICATION")) {
            log.info("\nПолучен POST запрос с type=NOTIFICATION: {}", socketNotificationDTO);
            notificationService.addNotification(notificationsMapper.getEventNotificationDTO(socketNotificationDTO));
            kafkaService.sendSocketNotificationDTO(socketNotificationDTO);
        }

        if(socketNotificationDTO.getType().equals("MESSAGE")) {
            log.info("\nПолучен POST запрос с type=MESSAGE: {}", socketNotificationDTO);

        }

        //        webSocketHandler.handleTextMessage(webSocketHandler.getSessionList().get(0), new TextMessage(getJSONEvNotDTO()));


    }

*/

    //3
//    */


    /*
    //1
    private final WebSocketHandler webSocketHandler;

//    private final WebSocketSession session;
//    private final Session session;


    @GetMapping
    public void published() throws Exception {

//        EventNotification eventNotification = new EventNotification();  eventNotification.setId( UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c") ); eventNotification.setIsDeleted( false );    eventNotification.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133")); eventNotification.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));   eventNotification.setNotificationType(Type.LIKE);   eventNotification.setStatus(Status.SEND);   eventNotification.setContent("test content"); String jsonString = mapper.writeValueAsString(eventNotification);   webSocketHandler.handleTextMessage(webSocketHandler.getWebSocketSession(), new TextMessage(jsonString) );
//        List<EventNotification> eventNotificationList = new ArrayList<>();    eventNotificationList.add(eventNotification);   String jsonListString = mapper.writeValueAsString(eventNotificationList);   webSocketHandler.handleTextMessage(webSocketHandler.getWebSocketSession(), new TextMessage(jsonListString) );

//        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();evNotificationDTO.setNotificationType(Type.LIKE.toString());evNotificationDTO.setContent("test");evNotificationDTO.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));evNotificationDTO.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();socketNotificationDTO.setData(evNotificationDTO);socketNotificationDTO.setType("NOTIFICATION");socketNotificationDTO.setRecipientId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));String jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);System.out.println(jsonDTOString);

        webSocketHandler.handleTextMessage(webSocketHandler.getWebSocketSession(), new TextMessage(jsonDTOString) );

        }
    //1
    */



            /*
            //2
            @Autowired
            WebSocketSessionManager webSocketSessionManager;


            @GetMapping
            public void published() throws Exception {
            WebSocketHandler webSocketHandler = new WebSocketHandler(this.webSocketSessionManager);

            EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
            evNotificationDTO.setNotificationType(Type.LIKE.toString());
            evNotificationDTO.setContent("test");
            evNotificationDTO.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));
            evNotificationDTO.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
            SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
            socketNotificationDTO.setData( evNotificationDTO);
            socketNotificationDTO.setType("NOTIFICATION");
            socketNotificationDTO.setRecipientId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
            String jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
            System.out.println(jsonDTOString);

            for (WebSocketSession webSocketSession: webSocketSessionManager.getWebSocketSessions()) {
                webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(jsonDTOString));
            }
            }
            //2
            */


/*
    public String getJSONEvNotDTO() {

        EvNotificationDTO evNotificationDTO = new EvNotificationDTO();
        evNotificationDTO.setNotificationType(Type.LIKE.toString());
        evNotificationDTO.setContent("test");
        evNotificationDTO.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));
        evNotificationDTO.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        SocketNotificationDTO socketNotificationDTO = new SocketNotificationDTO();
        socketNotificationDTO.setData( evNotificationDTO);
        socketNotificationDTO.setType("NOTIFICATION");
        socketNotificationDTO.setRecipientId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));

        String jsonDTOString = null;
        try {
            jsonDTOString = mapper.writeValueAsString(socketNotificationDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(jsonDTOString);
        return jsonDTOString;
    }

}
*/























    /*
    //3
    private SimpMessagingTemplate simpMessagingTemplate;


    public TestControllerWS(SimpMessagingTemplate template) {
        this.simpMessagingTemplate = template;
    }

    @GetMapping
    public void published() throws Exception {
        this.simpMessagingTemplate.convertAndSend(getJSONEvNotDTO());
    }
    //3
    */



    /*
    @PostConstruct
    public void connect() {
        try {


        } catch (Exception e) {
            System.out.println("Exception while accessing websockets: " + e);
        }
    }*/



//    @GetMapping
//    public void published() throws Exception {


//        webSocketHandler.handleTextMessage(webSocketHandler.getSessionList().get(0), new TextMessage(getJSONEvNotDTO()));

//        webSocketHandler.handleTextMessage(webSocketHandler.getSessionList().get(0), new TextMessage(getJSONEvNotDTO()));

//        WebSocketClient webSocketClient = new StandardWebSocketClient();
//        WebSocketSession webSocketSession = webSocketClient.doHandshake(webSocketHandler,
//                new WebSocketHttpHeaders(), URI.create("ws://localhost:8099/api/v1/streaming/ws")).get();

//        WebSocket.Builder biulder = HttpClient.newHttpClient().newWebSocketBuilder();
//    }