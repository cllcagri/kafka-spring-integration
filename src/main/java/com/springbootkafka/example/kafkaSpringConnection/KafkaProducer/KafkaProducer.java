package com.springbootkafka.example.kafkaSpringConnection.KafkaProducer;

import com.springbootkafka.example.kafkaSpringConnection.model.Book;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


@Component
public class KafkaProducer {

    @Autowired
    KafkaTemplate<String, Book> kafkaTemplate;

    public void send(String topicName,Book book){
        kafkaTemplate.send(topicName, book);
    }

    public ListenableFuture<SendResult<String,Book>> sendDefault(String topicName, Book book){
        return kafkaTemplate.send(topicName, book);
    }

    public SendResult<String, Book> sendSync(String topicName,Book book) throws ExecutionException, InterruptedException {
        try {
            return kafkaTemplate.send(topicName, book).get();
        } catch (ExecutionException | InterruptedException ex) {
            throw ex;
        } catch (Exception ex) {
           throw ex;
        }
    }

    public ListenableFuture<SendResult<String,Book>> sendProducer(ProducerRecord producerRecord){
        return kafkaTemplate.send(producerRecord);
    }
}
