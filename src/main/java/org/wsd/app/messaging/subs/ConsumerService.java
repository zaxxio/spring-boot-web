package org.wsd.app.messaging.subs;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.wsd.app.event.SensorEvent;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class ConsumerService {
    @KafkaListener(topicPartitions = @TopicPartition(topic = "sensor", partitions = {"0"}), groupId = "sensor-group", clientIdPrefix = "cg1", concurrency = "5", errorHandler = "kafkaErrorHandler")
    public void consumerGroup1(@Payload List<SensorEvent> sensorEvent,
                               @Headers Map<Object, Object> map, Acknowledgment acknowledgment) {
        try {
            log.info(sensorEvent);
            // processed
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            //throw ex;
        }
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "sensor", partitions = {"1"}), groupId = "sensor-group", clientIdPrefix = "cg2", concurrency = "5", errorHandler = "kafkaErrorHandler")
    public void consumerGroup2(List<SensorEvent> sensorEvent, Acknowledgment acknowledgment) {
        log.info(sensorEvent);
        acknowledgment.acknowledge();
    }
}
