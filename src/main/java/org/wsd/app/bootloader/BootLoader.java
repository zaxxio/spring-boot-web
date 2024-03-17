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

package org.wsd.app.bootloader;

import com.github.javafaker.Faker;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.avro.SensorEventAvro;
import org.wsd.app.config.TopicConfiguration;
import org.wsd.app.domain.PhotoEntity;
import org.wsd.app.mongo.Address;
import org.wsd.app.mongo.Gender;
import org.wsd.app.mongo.Person;
import org.wsd.app.mongo.PersonRepository;
import org.wsd.app.repository.PhotoRepository;
import org.wsd.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BootLoader implements CommandLineRunner {
    private final PhotoRepository photoRepository;
    private final PersonRepository personRepository;

    private final KafkaTemplate<UUID, SensorEventAvro> sensorEventAvroKafkaTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setId(1L);
        String name = new Faker().file().fileName();
        photoEntity.setName(name);
        PhotoEntity photo = photoRepository.save(photoEntity);


        Person person = new Person();
        person.setUsername("username1");
        person.setPassword("password");
        Address address = new Address();
        address.setName("Mirpur");
        person.setAddress(address);
        person.setGender(Gender.MALE);

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setX(1)
                .setY(Math.random())
                .build();

        sensorEventAvroKafkaTemplate.send(TopicConfiguration.SENSOR, UUID.randomUUID(), eventAvro);
        System.out.println("Okay");

        // personRepository.save(person);

    }
}
