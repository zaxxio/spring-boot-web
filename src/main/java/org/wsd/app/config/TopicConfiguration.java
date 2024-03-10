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

package org.wsd.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.record.CompressionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    public static final String SENSOR = "SENSOR_TOPIC";
    public static final String SENSOR_INPUT = "SENSOR_INPUT";
    public static final String SENSOR_OUTPUT = "SENSOR_OUTPUT";

    @Bean
    public NewTopic sensorTopic() {
        return TopicBuilder.name(TopicConfiguration.SENSOR)
                .partitions(3)
                .replicas(3)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.toString())
                .build();
    }

    @Bean
    public NewTopic sourceGreetingTopic() {
        return TopicBuilder.name(TopicConfiguration.SENSOR_INPUT)
                .partitions(3)
                .replicas(3)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.toString())
                .build();
    }

    @Bean
    public NewTopic destionationGreetingTopic() {
        return TopicBuilder.name(TopicConfiguration.SENSOR_OUTPUT)
                .partitions(3)
                .replicas(3)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.DELETE_RETENTION_MS_CONFIG, "86400000")
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.toString())
                .build();
    }
}
