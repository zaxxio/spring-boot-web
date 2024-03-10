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

package org.wsd.app.messaging.subs;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.NetworkClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.event.SensorEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class ConsumerService {

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            attempts = "4",
            backoff = @Backoff(delay = 10, multiplier = 1.5, maxDelay = 2000)
    )
    @KafkaListener(topics = TopicConfiguration.SENSOR, groupId = "sensor-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumerGroup1(@Payload SensorEvent sensorEvent, Acknowledgment acknowledgment) {
        log.info(sensorEvent);
        if (sensorEvent.getX() == 50) {
            throw new RuntimeException("Expected.");
        }
        acknowledgment.acknowledge();
    }


    @DltHandler
    public void processMessage(SensorEvent message) {
        log.error("DltHandler processMessage = {}", message);
    }

    //@KafkaListener(topicPartitions = @TopicPartition(topic = "sensor", partitions = {"1"}), groupId = "sensor-group", clientIdPrefix = "cg2", concurrency = "5", errorHandler = "kafkaErrorHandler")
    public void consumerGroup2(List<SensorEvent> sensorEvent, Acknowledgment acknowledgment) {
        log.info(sensorEvent);
        acknowledgment.acknowledge();
    }
}
