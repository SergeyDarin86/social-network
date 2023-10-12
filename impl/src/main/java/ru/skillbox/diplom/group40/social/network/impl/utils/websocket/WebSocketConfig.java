package ru.skillbox.diplom.group40.social.network.impl.utils.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

//    /*
    //1
    @Bean
    public WebSocketHandler getWebSocketHandler() {
        return new WebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(), "/api/v1/streaming/ws")
                .setAllowedOrigins("*");   //.setAllowedOriginPatterns("*");
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