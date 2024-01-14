package com.example.kafkaexamplev2;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    // specify group id so that when we scale we can listen to the same partition and topic
    @KafkaListener(topics = "amigoscode", groupId = "groupId")
    void listener(String data) {
        // do anything with data
        System.out.println("Received Data: " + data);
    }
}
