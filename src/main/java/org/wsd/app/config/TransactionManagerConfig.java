package org.wsd.app.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class TransactionManagerConfig {


    private final ProducerFactory<UUID, Object> producerFactory;

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager<UUID, Object> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public ChainedTransactionManager chainedTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new ChainedTransactionManager(transactionManager(entityManagerFactory), kafkaTransactionManager());
    }


    @TransactionalEventListener(classes = RollbackException.class)
    public void consume(ApplicationEvent applicationEvent) {
        log.info("Custom : " + applicationEvent);
    }

}
