package org.wsd.app.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.wsd.app.domain.PhotoEntity;
import org.wsd.app.payload.Payload;
import org.wsd.app.repository.PhotoRepository;
import org.wsd.app.security.auth.resquest.SignInRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Component
@Scope(scopeName = "prototype")
@RequiredArgsConstructor
public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final PhotoRepository photoRepository;
    private final Gson gson = new Gson();
    private Integer index = 0;
    private final PingTask pingTask;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("Id " + session.getId() + " is  open.");
        pingTask.registerSession(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message : " + Objects.requireNonNull(session.getPrincipal()).getName()); // get principle to route message
        log.info("Started Processing : " + session.getId());
        Thread.sleep(1000); // to do the work
        A a = gson.fromJson((String) message.getPayload(), A.class);
        a.setPassword("Dumb!");
        if (session.isOpen()) {
            final Transport<String> transport = new Transport<>();
            transport.setSessionId(session.getId());
            transport.setConnection(Connection.OPEN);
            transport.setTimestamp(Timestamp.from(Instant.now()));
            transport.setIndex(index++);
            if (message instanceof TextMessage textMessage) {
                transport.setPayload(gson.toJson(a));
            }
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(transport)));
        }
        log.info("Finished Processing : " + session.getId());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class A {
        public String username;
        public String password;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        final Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", "error");
        errorDetails.put("message", exception.getMessage());
        String errorMessage = objectMapper.writeValueAsString(errorDetails);
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(errorMessage));
        }
        pingTask.unregisterSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Id " + session.getId() + " is  closed.");
        pingTask.unregisterSession(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
