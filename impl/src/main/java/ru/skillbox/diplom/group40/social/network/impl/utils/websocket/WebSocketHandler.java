package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.DialogService;

import java.util.*;

@Data
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //4
//  /*
    Map<UUID, List<WebSocketSession>> sessionMap = new HashMap<UUID, List<WebSocketSession>>();
    public static final String TYPE_NOTIFICATION = "NOTIFICATION";
    public static final String TYPE_MESSAGE = "MESSAGE";
    @Autowired
    private NotificationsMapper notificationsMapper;
//    @Autowired
//    private final DialogService dialogService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("\nWebSocketHandler: afterConnectionEstablished() startMethod: sessionId: {}, sessionMap: {}",
                session.getId(), sessionMap);

        UUID uuid = getId(session);                                                                                     /*JwtAuthenticationToken token = (JwtAuthenticationToken) session.getPrincipal();Jwt jwt = (Jwt) token.getCredentials();UUID uuid = UUID.fromString(jwt.getClaim("user_id"));*/
        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));

        //A Вынести в отдельный метод
        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());

        boolean isNew = false;
        if(list.isEmpty()) {isNew = true;}

        list.add(session);

        if(isNew) {
            sessionMap.put(uuid, list);
        } else {
            sessionMap.remove(uuid, list);
        }

//        isNew == true ? sessionMap.put(uuid, list) : sessionMap.remove(uuid, list);

        //A

        log.info("\nWebSocketHandler: afterConnectionEstablished(): итоговый для id: {} sessionMap: {}",
                session.getId(), sessionMap);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {     // private
        log.info("\nWebSocketHandler: handleTextMessage() startMethod: получен TextMessage: {}", message.getPayload());

        if (isNotification(message)) {
            sendTextMessage(getId(session), message);
        };

        if (isMessage(message)) {
            log.info("\nWebSocketHandler: handleTextMessage() : получен TextMessage c type Message: {}",
                    message.getPayload());
            /**
             Для Антона хвост;
             */
        };
                                                                                                                         /*List<WebSocketSession> sendingList = sessionMap.getOrDefault(getId(session), new ArrayList<>()); for(WebSocketSession registerSession: sendingList) {registerSession.sendMessage(message);}*/
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("\nWebSocketHandler: afterConnectionClosed() startMethod: status: {}, для id: {}, sessionMap: {}",
                status.toString(), session.getId(), sessionMap);

        UUID uuid = getId(session);
        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());
        list.remove(session);                                                                                           //        sessionList.remove(session);
        sessionMap.remove(uuid, list);
        log.info("\nWebSocketHandler: afterConnectionClosed(): итоговый для id: {} sessionMap: {}", uuid, sessionMap);


    }

    public UUID getId(WebSocketSession session) throws Exception {
        JwtAuthenticationToken token = (JwtAuthenticationToken) session.getPrincipal();
        Jwt jwt = (Jwt) token.getCredentials();
        UUID uuid = UUID.fromString(jwt.getClaim("user_id"));
        log.info("\nWebSocketHandler: getId() id: {}", uuid);
        return uuid;
    }

    public void sendTextMessage(UUID id, TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: sendMessage(): получен к отправке в WS message: {}", message.getPayload());
        List<WebSocketSession> sendingList = sessionMap.getOrDefault(id, new ArrayList<>());
        for(WebSocketSession registerSession: sendingList) {registerSession.sendMessage(message);}
    }

    public boolean isNotification(TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: isNotification(): проверка типа TextMessage: {}", message.getPayload());
        return TYPE_NOTIFICATION.equals(notificationsMapper.getSocketNotificationDTO(message.getPayload()).getType());
    }

    public boolean isMessage(TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: isMessage(): проверка типа TextMessage: {}", message.getPayload());
        return TYPE_MESSAGE.equals(notificationsMapper.getSocketNotificationDTO(message.getPayload()).getType());
    }



    /*@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: WS соединение установлено, для sessionId: {},\nsessionList: {}", session.getHandshakeHeaders(), sessionMap);
        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));

        System.err.println(session.getHandshakeHeaders().get("cookie"));
        System.err.println("getPrincipal: " + session.getPrincipal().getName());    // user;
        accountService.get(session.getPrincipal().getName());
        cnt++;
        sessionMap.put(String.valueOf(cnt), session);
    }*/

    //4

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //3 @Рабочее
      /*
    List<WebSocketSession> sessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: WS соединение установлено, для sessionId: {},\nsessionList: {}", session.getId(), sessionList);
        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));
        sessionList.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {     // private
        log.info("WebSocketHandler: handleTextMessage() startMethod: получен message: {}", message.getPayload());
        for(WebSocketSession registerSession: sessionList) {registerSession.sendMessage(message);}
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        log.info("WebSocketHandler: afterConnectionClosed() startMethod: WS соединение завершено, status: {}, для sessionId: {},\nsessionList: {}", status.toString(), session.getId(), sessionList);
    }
      */
    //3

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //1
    /*
    WebSocketSession webSocketSession;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: WS соединение установлено, session: {}",
                session);
        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));

        webSocketSession = session;

        // For test:
        EventNotification eventNotification = new EventNotification();
        eventNotification.setId( UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c") );
        eventNotification.setIsDeleted( false );
        eventNotification.setAuthorId(UUID.fromString("7df96082-f67f-42c8-b5e7-11b2c2878133"));
        eventNotification.setReceiverId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        eventNotification.setNotificationType(Type.LIKE);
        eventNotification.setStatus(Status.SEND);
        eventNotification.setContent("test content");
        //
//        Gson g = new Gson();
//        EventNotification eventNotifications = g.fromJson(String.valueOf(eventNotification), EventNotification.class);

        String jsonString = mapper.writeValueAsString(eventNotification);
        System.out.println(jsonString);
        session.sendMessage(new TextMessage(jsonString));

        List<EventNotification> eventNotificationList = new ArrayList<>();
        eventNotificationList.add(eventNotification);
        String jsonListString = mapper.writeValueAsString(eventNotificationList);
        session.sendMessage(new TextMessage(jsonListString));


        EventNotification[] myArray = eventNotificationList.toArray(new EventNotification[0]);
        String jsonArrayString = mapper.writeValueAsString(myArray);
        session.sendMessage(new TextMessage(jsonArrayString));

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("WebSocketHandler: handleTextMessage() startMethod: получен message: {}", message.getPayload());
        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        super.handleTransportError(session, exception);
        log.info("WebSocketHandler: handleTransportError() startMethod: получен exception: {}",
                exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        log.info("WebSocketHandler: afterConnectionClosed() startMethod: WS соединение завершено, status: {}",
                status.toString());
    }
    */
    //1

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //2
    /*
    private WebSocketSessionManager webSocketSessionManager;
    ObjectMapper mapper = new ObjectMapper();

    public WebSocketHandler(WebSocketSessionManager webSocketSessionManager) {
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: WS соединение установлено, session: {}",
                session);

        this.webSocketSessionManager.addWebSocketSessions(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("WebSocketHandler: handleTextMessage() startMethod: получен message: {}", message.getPayload());

        this.webSocketSessionManager
                .getWebSocketSessions() //  .getWebSocketSessionExcept(session)
                .forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        super.handleTransportError(session, exception);
        log.info("WebSocketHandler: handleTransportError() startMethod: получен exception: {}",
                exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        log.info("WebSocketHandler: afterConnectionClosed() startMethod: WS соединение завершено, status: {}",
                status.toString());
        this.webSocketSessionManager.removeWebSocketSessions(session);
    }
    */
    //2

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
