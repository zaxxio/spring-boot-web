package org.wsd.app.messaging.pubs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.event.SensorEvent;
import org.wsd.app.repository.SensorRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<UUID, ?> kafkaTemplate;
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    @Scheduled(fixedRate = 1000)
    @Transactional("kafkaTransactionManager")
    public void process() throws IOException {
        //sendMessage();
        final SensorEvent sensorEvent = new SensorEvent();
        sensorEvent.setX(Math.random());
        sensorEvent.setY(Math.random());

//        sensorRepository.insert(sensorEvent);
//        String dummyContent = "This is a dummy file content. " + UUID.randomUUID();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(dummyContent.getBytes());
//
//        DBObject metaData = new BasicDBObject();
//        metaData.put("type", "txt");
//        metaData.put("title", "My Title " + UUID.randomUUID());
//
//        ObjectId fileId = gridFsTemplate.store(
//                inputStream, UUID.randomUUID() + ".txt", "text/plain", metaData);
//
//        log.info("Stored with file id: " + fileId);
//        //retrieveDummyFile(fileId);
//        getFile(String.valueOf(fileId));
    }

    // Example of querying the dummy file
    public void getFile(String id) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        InputStream inputStream = operations.getResource(file).getInputStream();
        String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        System.out.println(string);
    }

    public void sendMessage() {
        final SensorEvent sensorEvent = new SensorEvent();
        sensorEvent.setX(Math.random());
        sensorEvent.setY(Math.random());

        final Message<SensorEvent> message = MessageBuilder
                .withPayload(sensorEvent)
                .setHeader(KafkaHeaders.KEY, UUID.randomUUID())
                .setHeader(KafkaHeaders.TOPIC, "sensor")
                //.setHeader(KafkaHeaders.PARTITION, 0)
                .setHeader(KafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();

        CompletableFuture<? extends SendResult<UUID, ?>> executed = kafkaTemplate.executeInTransaction(tx -> tx.send(message));

        if (executed != null) {
            executed.thenAccept(tx -> log.info("Message sent successfully" + tx.getProducerRecord()))
                    .exceptionally(throwable -> {
                        log.error("Failed message : " + throwable.getMessage());
                        return null;
                    });
        }
    }

}
