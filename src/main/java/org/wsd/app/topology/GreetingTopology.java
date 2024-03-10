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
