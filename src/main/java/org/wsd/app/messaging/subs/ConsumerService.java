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
