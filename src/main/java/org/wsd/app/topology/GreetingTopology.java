package org.wsd.app.topology;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.wsd.app.event.SensorEvent;

import java.util.UUID;

@Configuration
public class GreetingTopology {

    @Bean
    public Serde<SensorEvent> sensorEventSerde() {
        return new JsonSerde<>(SensorEvent.class);
    }

    @Bean
    public KStream<UUID, SensorEvent> kStream(StreamsBuilder streamsBuilder, Serde<SensorEvent> sensorEventSerde) {
        KStream<UUID, SensorEvent> stream = streamsBuilder.stream("SENSOR_INPUT", Consumed.with(Serdes.UUID(), sensorEventSerde));
        stream.print(Printed.<UUID, SensorEvent>toSysOut().withLabel("SENSOR-INPUT"));

        KStream<UUID, SensorEvent> changedStream = stream.mapValues(value -> new SensorEvent(UUID.randomUUID().toString(), value.getX(), value.getY()));
        changedStream.print(Printed.<UUID, SensorEvent>toSysOut().withLabel("SENSOR-OUTPUT"));

        changedStream.to("SENSOR_OUTPUT", Produced.with(Serdes.UUID(), sensorEventSerde));
        return changedStream;
    }
}
