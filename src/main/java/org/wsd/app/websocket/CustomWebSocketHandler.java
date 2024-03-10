/*
 * Copyright (c) of Partha Sutradhar 2024.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
