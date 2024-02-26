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
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.event.SensorEvent;
import org.wsd.app.repository.UserRepository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<UUID, ?> kafkaTemplate;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner commandLineRunner(ProducerService producerService) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                try {
                    //producerService.process(i);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }


    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void process(int i) throws TransactionException {
        final SensorEvent sensorEvent = new SensorEvent();
        sensorEvent.setX(i);
        sensorEvent.setY(Math.random());

        final Message<SensorEvent> message = MessageBuilder
                .withPayload(sensorEvent)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID())
                .setHeader(KafkaHeaders.TOPIC, TopicConfiguration.SENSOR)
                .setHeader(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();

        CompletableFuture<? extends SendResult<UUID, ?>> future = kafkaTemplate.send(message);
        future.thenAccept(uuidSendResult -> {
            log.info("Message sent successfully.");
        }).exceptionally(exception -> {
            log.error(exception.getMessage());
            return null;
        });
        userRepository.findAll();
    }

    @Recover
    public void recover(Exception e) {
        log.error("Recover : " + e.getMessage());
    }
}
