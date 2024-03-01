package org.wsd.app.websocket;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PingTask {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void registerSession(WebSocketSession session) {
        sessions.addIfAbsent(session);
    }

    public void unregisterSession(WebSocketSession session) {
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 1000) // Ping interval of 1 seconds
    public void sendPings() {
        TextMessage pingMessage = new TextMessage("Ping: ");
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(pingMessage);
                } else {
                    unregisterSession(session);
                }
            } catch (IOException e) {
                // Handle the exception, possibly removing the session
                unregisterSession(session);
            }
        }
    }
}
