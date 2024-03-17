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
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.avro.SensorEventAvro;
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.repository.UserRepository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<UUID, SensorEventAvro> kafkaTemplate;


//    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void process(int i) throws TransactionException {
        final SensorEventAvro sensorEvent = new SensorEventAvro();
        sensorEvent.setX(i);
        sensorEvent.setY(Math.random());


        CompletableFuture<? extends SendResult<UUID, ?>> future = kafkaTemplate.send(TopicConfiguration.SENSOR, UUID.randomUUID(), sensorEvent);
        future.thenAccept(uuidSendResult -> {
            log.info("Message sent successfully.");
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
