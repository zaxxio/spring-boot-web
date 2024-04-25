/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.cqrs.infrastructure;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.core.events.BaseEvent;
import org.wsd.app.core.producer.EventProducer;
import org.wsd.app.cqrs.events.AccountCreatedEvent;
import org.wsd.app.event.AccountCreatedEventAvro;
import org.wsd.app.event.SensorEventAvro;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class AccountEventProducer implements EventProducer {

    @Autowired
    private KafkaTemplate<UUID, AccountCreatedEventAvro> kafkaTemplate;

    @Override
    @Transactional
    public void produce(String topic, BaseEvent baseEvent) {
        AccountCreatedEvent event = (AccountCreatedEvent) baseEvent;


        AccountCreatedEventAvro eventAvro = AccountCreatedEventAvro.newBuilder()
                .setId(event.getId())
                .setAccountHolder(event.getAccountHolder())
                .setAccountType(event.getAccountType())
                .setVersion(event.getVersion())
                .setBalance(event.getBalance())
                .setCreatedAt(event.getCreatedAt())
                .build();

        final Message<AccountCreatedEventAvro> message = MessageBuilder
                .withPayload(eventAvro)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID())
                .setHeader("schema.version", "v1")
                .setHeader(KafkaHeaders.TOPIC, event.getClass().getSimpleName())
                .setHeader(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();

        CompletableFuture<? extends SendResult<UUID, ?>> future = kafkaTemplate.send(message);
        future.thenAccept(uuidSendResult -> {
            try {
                log.info("Produced : " + future.get().getProducerRecord().value());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(exception -> {
            log.error(exception.getMessage());
            return null;
        });

        this.kafkaTemplate.send(topic, eventAvro);
    }
}
