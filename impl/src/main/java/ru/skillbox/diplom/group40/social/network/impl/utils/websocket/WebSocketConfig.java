package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.DialogService;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final DialogService dialogService;
//    /*
    //1
    @Bean
    public WebSocketHandler getWebSocketHandler() {
        return new WebSocketHandler(dialogService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(), "/api/v1/streaming/ws")
//                .setAllowedOrigins("*");
        .setAllowedOriginPatterns("*");
//                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    //1
//    */

    /*
    //2
    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(this.webSocketSessionManager), "/api/v1/streaming/ws")
                .setAllowedOrigins("*");
    }
    //2
    */

}