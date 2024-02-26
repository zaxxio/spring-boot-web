package org.wsd.app.config;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Log4j2
@EnableKafka
@Configuration
@EnableKafkaStreams
@EnableKafkaRetryTopic
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        final Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "spring-boot-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.UUID().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        // props.put(StreamsConfig.STATE_DIR_CONFIG, "/to/state-store");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024L); // 10MB
        props.put(StreamsConfig.producerPrefix(ProducerConfig.BUFFER_MEMORY_CONFIG), 32 * 1024 * 1024L); // 32MB
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4); // Adjust based on your CPU cores
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KafkaTemplate<UUID, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<UUID, Object> producerFactory() {
        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Correcting the serializer for value to JsonSerializer for consistency and correctness
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-1");
        configProps.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 15000);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "pid");
        // Additional configurations for enhanced performance and reliability
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024);
        // Configurations for handling connections and timeouts
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        configProps.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, 50);
        configProps.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 1000);
        // Monitoring and management
        configProps.put(ProducerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG, 30000);
        configProps.put(ProducerConfig.METRICS_NUM_SAMPLES_CONFIG, 2);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy(); //
        backOffPolicy.setInitialInterval(1000); // Initial backoff interval in milliseconds
        backOffPolicy.setMultiplier(2.0); // Multiplier to use for the next backoff interval
        backOffPolicy.setMaxInterval(10000); // Maximum backoff interval
        retryTemplate.setBackOffPolicy(backOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5); // Maximum number of retry attempts
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        final Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        // Specify the actual deserializer classes as properties of ErrorHandlingDeserializer
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, UUIDDeserializer.class.getName());
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // Trusted package
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "org.wsd.app.event");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "300");
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");

        // Additional configurations
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "500");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
        configProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "cid");
        configProps.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "1000");
        configProps.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "10000");
        configProps.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "100");
        configProps.put(ConsumerConfig.CHECK_CRCS_CONFIG, "true");
        configProps.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "300000");
        configProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "305000");
        configProps.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "60000");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory,
                                                                                                 KafkaTransactionManager<UUID, Object> transactionManager) {
        final ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(false);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.getContainerProperties().setTransactionManager(transactionManager);
        factory.setCommonErrorHandler(commonErrorHandler());
        return factory;
    }

    @Bean
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        return (message, exception) -> {
            log.error("Error Handler " + message);
            return null;
        };
    }


    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new CustomCommonErrorHandler();
    }


    public class CustomCommonErrorHandler implements CommonErrorHandler {

        @Override
        public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer,
                                 MessageListenerContainer container) {
            log.error(thrownException.getMessage());
            return CommonErrorHandler.super.handleOne(thrownException, record, consumer, container);
        }

    }


}
