package com.example.kafkaexamplev2.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/*
*  Class responsible for creating topics
* */
@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic amigoscodeTopic(){
        return TopicBuilder.name("amigoscode")
                .build();
    }
}
