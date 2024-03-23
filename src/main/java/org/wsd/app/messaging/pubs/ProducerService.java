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

package org.wsd.app.messaging.pubs;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.event.SensorEventAvro;
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.repository.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final UserRepository userRepository;
    private final KafkaTemplate<UUID, SensorEventAvro> sensorEventAvroKafkaTemplate;

    @Bean
    public CommandLineRunner commandLineRunner(ProducerService producerService) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                try {
                    producerService.process(i);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void process(int i) throws TransactionException {


        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setX(Math.random())
                .setY(Math.random())
                .setZ(Math.random())
                .build();

        final Message<SensorEventAvro> message = MessageBuilder
                .withPayload(eventAvro)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID())
                .setHeader("schema.version","V2")
                .setHeader(KafkaHeaders.TOPIC, TopicConfiguration.SENSOR)
                .setHeader(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();

        CompletableFuture<? extends SendResult<UUID, ?>> future = sensorEventAvroKafkaTemplate.send(message);
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
        //userRepository.findAll();
    }

    @Recover
    public void recover(Exception e) {
        log.error("Recover : " + e.getMessage());
    }
}
