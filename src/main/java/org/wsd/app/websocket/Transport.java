package org.wsd.app.websocket;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Transport<T> {
    private String sessionId;
    private T payload;
    private Connection connection;
    private String type;
    private Timestamp timestamp;
    private Integer index;
}
