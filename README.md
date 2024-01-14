# kafka-service
A service that runs Kafka, configures topics, Producers and Consumers using Spring Boot and Java 

Features: 
- A kafka broker will be set up locally
- Confurations of a producer
- Configurations of a consumer
- Producers can send streams of records to a topic
- Consumers can consume those records via the topic 

## Starting Broker 
Run the following commands in order to start all services in the correct order:
```
# Start the ZooKeeper service
# Note: Soon, ZooKeeper will no longer be required by Apache Kafka.
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```
Then 
```
# Start the Kafka broker service
$ bin/kafka-server-start.sh config/server.properties
```
Once all services have successfully launched, you will have a basic Kafka environment running and ready to use.

## Using REST to test 
We can call an endpoint "api/v1/messages" to test sending a topic using curl: 
```
curl -X POST http://localhost:8080/api/v1/messages \
     -H "Content-Type: application/json" \
     -d '{"message": "Api With Kafka"}'
```

## Topic 
```
/*
	config -> KafkaTopicConfig.java 
*/

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic amigoscodeTopic() {

        return TopicBuilder.name("kafkatopic")
            .build();
    }
}
```
## Producers and Consumers 
```
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
```

The prodcuer itself will ideally have its own service to services but we can test via Main 
```
@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			kafkaTemplate.send("amigoscode", "hello, kafka");
		};
	}
```

We will use the ConsumerFactory as the main engine to support our consumer config 

```
@Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }
```

## Creating Listener 
To consume the events from our topic 
```
// consume the events from our topic
@Component
public class KafkaListeners {

    // specify group id so that when we scale we can listen to the same partition and topic
    @KafkaListener(topics = "amigoscode", groupId = "groupId")
    void listener(String data) {
        // do anything with data
        System.out.println("Received Data " + data);
    }
}
```
