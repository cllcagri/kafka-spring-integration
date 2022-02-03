package com.springbootkafka.example.kafkaSpringConnection;

import com.springbootkafka.example.kafkaSpringConnection.KafkaProducer.KafkaProducer;
import com.springbootkafka.example.kafkaSpringConnection.model.Book;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    private static final String KAFKA_TOPIC = "NewTopic";

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Book book) {
        kafkaProducer.send(KAFKA_TOPIC,book);
        return "Publish successfully !!";
    }
}
