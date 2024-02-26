package org.wsd.app.payload;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Getter
@ToString
public class Payload<T> {

    private final UUID requestId = UUID.randomUUID();
    private final String message;
    private final T payload;
    private final Date timestamp;

    private Payload(String message, T payload) {
        this.message = message;
        this.payload = payload;
        this.timestamp = Date.from(Instant.now()
                .atZone(ZoneId.of("UTC"))
                .toInstant());
    }

    public static class Builder<T> {
        private String message;
        private T payload;

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public Payload<T> build() {
            return new Payload<>(message, payload);
        }
    }
}