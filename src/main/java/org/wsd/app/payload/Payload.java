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