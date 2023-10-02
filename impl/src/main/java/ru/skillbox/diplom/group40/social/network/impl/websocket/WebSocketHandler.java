package ru.skillbox.diplom.group40.social.network.impl.websocket;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.DialogService;

import java.util.*;

@Data
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final DialogService dialogService;
    Map<UUID, List<WebSocketSession>> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketHandler: afterConnectionEstablished() startMethod: sessionId: {}, sessionMap: {}", session.getId(), sessionMap);

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
            sessionMap.remove(uuid, list);
        }

        log.info("WebSocketHandler: afterConnectionEstablished(): итоговый для id: {} sessionMap: {}", session.getId(), sessionMap);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {     // private
        log.info("WebSocketHandler: handleTextMessage() startMethod: получен message: {}", message.getPayload());
        dialogService.handleSocketMessage(message);

        List<WebSocketSession> sendingList = sessionMap.getOrDefault(getRecipientId(message), new ArrayList<>());
        for (WebSocketSession registerSession : sendingList) {
            registerSession.sendMessage(message);
        }

        log.info("\nWebSocketHandler: handleTextMessage() endMethod: message: {} send to sessions:\n{}", message.getPayload(), sendingList);

    }

    private UUID getRecipientId(TextMessage message) {
        return UUID.fromString(new JSONObject(message.getPayload()).getString("recipientId"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocketHandler: afterConnectionClosed() startMethod: status: {}, для id: {}, sessionMap: {}", status.toString(), session.getId(), sessionMap);
        UUID uuid = getId(session);
        List<WebSocketSession> list = sessionMap.getOrDefault(uuid, new ArrayList<>());
        list.remove(session);
        sessionMap.remove(uuid, list);
        log.info("WebSocketHandler: afterConnectionClosed(): итоговый для id: {} sessionMap: {}", uuid, sessionMap);
    }

    public UUID getId(WebSocketSession session) throws Exception {
        JwtAuthenticationToken token = (JwtAuthenticationToken) session.getPrincipal();
        Jwt jwt = (Jwt) token.getCredentials();
        UUID uuid = UUID.fromString(jwt.getClaim("user_id"));
        log.info("WebSocketHandler: getId() id: {}", uuid);
        return uuid;
    }
}
