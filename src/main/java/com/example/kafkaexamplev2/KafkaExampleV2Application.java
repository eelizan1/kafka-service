package com.example.kafkaexamplev2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaExampleV2Application {

	public static void main(String[] args) {
		SpringApplication.run(KafkaExampleV2Application.class, args);
	}

	// test send messages as soon as we start the application
	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			kafkaTemplate.send("amigoscode", "Hello From Kafka again");
		};
	}
}
