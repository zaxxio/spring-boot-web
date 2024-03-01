package org.wsd.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.wsd.app.websocket.CustomWebSocketHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(perConnectionWebSocketHandler(), "/websocket");
        registry.addHandler(perConnectionWebSocketHandler(), "/websocket")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Bean
    public PerConnectionWebSocketHandler perConnectionWebSocketHandler() {
        return new PerConnectionWebSocketHandler(CustomWebSocketHandler.class);
    }


}