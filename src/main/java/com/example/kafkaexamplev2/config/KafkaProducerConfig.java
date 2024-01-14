package com.example.kafkaexamplev2.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    // hold bootstrap server url from application properties
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    // producer config
    // These configurations are essential for the Kafka producer to understand how to connect to the Kafka cluster
    // and how to serialize the messages it sends.
    public Map<String, Object> producerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    // producer factory responsible for creating producer instances
    // note - second param would be the type of object we're sending (ex. Customer, Notification etc)
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    // template allows us to send the messages
    // provides high-level operations to send messages to Kafka topics.
    // This template encapsulates a producer and provides a convenient way to send data to Kafka topics.
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
