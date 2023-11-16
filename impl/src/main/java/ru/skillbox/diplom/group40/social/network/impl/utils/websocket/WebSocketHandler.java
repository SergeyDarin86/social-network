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
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final DialogService dialogService;
    ConcurrentMap<UUID, List<WebSocketSession>> sessionMap= new ConcurrentHashMap();
    public static final String TYPE_NOTIFICATION = "NOTIFICATION";
    public static final String TYPE_MESSAGE = "MESSAGE";
    @Autowired
    private NotificationsMapper notificationsMapper;
    @Autowired
    private KafkaService kafkaService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: sessionId: {}, sessionMap: {}",
                session.getId(), sessionMap);

        JwtAuthenticationToken token = (JwtAuthenticationToken) session.getPrincipal();
        Jwt jwt = (Jwt) token.getCredentials();
        UUID uuid = UUID.fromString(jwt.getClaim("user_id"));

        session.sendMessage(new TextMessage("{ \"connection\": \"established\"}"));

        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());

        boolean isNew = false;
        if (list.isEmpty()) {
            isNew = true;
        }

        list.add(session);

        if (isNew) {
            sessionMap.put(uuid, list);
        } else {
            sessionMap.replace(uuid, list);
        }

        kafkaService.sendAccountDTO(notificationsMapper.getAccountOnlineDto(uuid, true));

        log.info("WebSocketHandler: afterConnectionEstablished(): итоговый для id: {} sessionMap: {}",
                session.getId(), sessionMap);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info("WebSocketHandler: handleTextMessage() startMethod: получен TextMessage: {}", message.getPayload());

        if (isNotification(message)) {
            log.info("WebSocketHandler: handleTextMessage() : получен TextMessage c type Notification: {}",
                    message.getPayload());
            sendTextMessage(getId(session), message);
        };

        if (isMessage(message)) {
            log.info("WebSocketHandler: handleTextMessage() startMethodAnton: получен message: {}",
                    message.getPayload());
            dialogService.handleSocketMessage(message);

            List<WebSocketSession> sendingList = sessionMap.getOrDefault(getRecipientId(message), new ArrayList<>());
            for (WebSocketSession registerSession : sendingList) {
                registerSession.sendMessage(message);
            }

            log.info("WebSocketHandler: handleTextMessage() endMethodAnton: message: {} send to sessions:{}",
                    message.getPayload(), sendingList);
        };


    }

    private UUID getRecipientId(TextMessage message) {
        return UUID.fromString(new JSONObject(message.getPayload()).getString("recipientId"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocketHandler: afterConnectionClosed() startMethod: status: {}, для id: {}, sessionMap: {}",
                status.toString(), session.getId(), sessionMap);
        UUID uuid = getId(session);
        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());
        list.remove(session);
        sessionMap.replace(uuid, list);

        kafkaService.sendAccountDTO(notificationsMapper.getAccountOnlineDto(uuid, false));

        log.info("WebSocketHandler: afterConnectionClosed(): итоговая для id: {} sessionMap: {}", uuid, sessionMap);
    }

    public UUID getId(WebSocketSession session) throws Exception {
        JwtAuthenticationToken token = (JwtAuthenticationToken) session.getPrincipal();
        Jwt jwt = (Jwt) token.getCredentials();
        UUID uuid = UUID.fromString(jwt.getClaim("user_id"));
        log.info("WebSocketHandler: getId() id: {}", uuid);
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
        return  TYPE_NOTIFICATION.equals(getType(message));
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
