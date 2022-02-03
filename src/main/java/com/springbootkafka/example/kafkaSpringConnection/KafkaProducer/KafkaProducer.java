package com.springbootkafka.example.kafkaSpringConnection.KafkaProducer;

import com.springbootkafka.example.kafkaSpringConnection.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    KafkaTemplate<String, Book> kafkaTemplate;

    public void send(String topicName,Book book){
        kafkaTemplate.send(topicName, book);
    }
}
