package org.wsd.app.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.wsd.app.websocket.CustomWebSocketHandler;

public class DelegatingWebSocketHandler extends WebSocketHandlerDecorator {

    @Autowired
    public DelegatingWebSocketHandler(ApplicationContext applicationContext) {
        super(applicationContext.getBean(CustomWebSocketHandler.class));
    }
}
