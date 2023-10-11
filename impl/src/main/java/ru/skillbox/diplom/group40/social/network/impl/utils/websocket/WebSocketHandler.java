package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
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
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final DialogService dialogService;
    Map<UUID, List<WebSocketSession>> sessionMap = new HashMap<UUID, List<WebSocketSession>>();
    public static final String TYPE_NOTIFICATION = "NOTIFICATION";
    public static final String TYPE_MESSAGE = "MESSAGE";
    @Autowired
    private NotificationsMapper notificationsMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("\nWebSocketHandler: afterConnectionEstablished() startMethod: sessionId: {}, sessionMap: {}",
                session.getId(), sessionMap);

        UUID uuid = getId(session);
        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));

        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());

        boolean isNew = false;
        if(list.isEmpty()) {isNew = true;}

        list.add(session);

        if(isNew) {
            sessionMap.put(uuid, list);
        } else {
            sessionMap.remove(uuid, list);
        }

        log.info("\nWebSocketHandler: afterConnectionEstablished(): итоговый для id: {} sessionMap: {}",
                session.getId(), sessionMap);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: handleTextMessage() startMethod: получен TextMessage: {}", message.getPayload());

        if (isNotification(message)) {
            log.info("\nWebSocketHandler: handleTextMessage() : получен TextMessage c type Notification: {}",
                    message.getPayload());
            sendTextMessage(getId(session), message);
        };

        if (isMessage(message)) {
            log.info("\nWebSocketHandler: handleTextMessage() : получен TextMessage c type Message: {}",
                    message.getPayload());
            TextMessage newMessage = dialogService.handleSocketMessage(message);
            sendTextMessage(getRecipientId(message), newMessage);
        };
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("\nWebSocketHandler: afterConnectionClosed() startMethod: status: {}, для id: {}, sessionMap: {}",
                status.toString(), session.getId(), sessionMap);

        UUID uuid = getId(session);
        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());
        list.remove(session);
        sessionMap.replace(uuid, list);
        log.info("\nWebSocketHandler: afterConnectionClosed(): итоговый для id: {} sessionMap: {}", uuid, sessionMap);
    }

    private UUID getRecipientId(TextMessage message) {
        return UUID.fromString(new JSONObject(message.getPayload()).getString("recipientId"));
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
        log.info("\nWebSocketHandler: sendMessage(): TextMessage отправлен users: {}", sendingList);
    }

    public boolean isNotification(TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: isNotification(): проверка типа TextMessage: {}", message.getPayload());
        return  TYPE_NOTIFICATION.equals(getType(message));                                                             //        return TYPE_NOTIFICATION.equals(notificationsMapper.getSocketNotificationDTO(message.getPayload()).getType());
    }

    public boolean isMessage(TextMessage message) throws Exception {
        log.info("\nWebSocketHandler: isMessage(): проверка типа TextMessage: {}", message.getPayload());
        JSONObject jsonSocketMessage = new JSONObject(message.getPayload());
        return TYPE_MESSAGE.equals(jsonSocketMessage.getString("type"));
    }

    public String getType(TextMessage message) {
        log.info("\nWebSocketHandler: getType(): получение type TextMessage: {}", message.getPayload());
        JSONObject jsonSocketMessage = new JSONObject(message.getPayload());
        return jsonSocketMessage.getString("type");
    }
}
