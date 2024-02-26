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
