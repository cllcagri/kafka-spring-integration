package com.springbootkafka.example.kafkaSpringConnection;

import com.springbootkafka.example.kafkaSpringConnection.KafkaProducer.KafkaProducer;
import com.springbootkafka.example.kafkaSpringConnection.model.Book;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

    @PostMapping("/publishV2")
    public void publishMessageCallback(@RequestBody Book book) {

        ListenableFuture<SendResult<String,Book>> listenableFuture = kafkaProducer.sendDefault(KAFKA_TOPIC, book);
        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(KAFKA_TOPIC, book.toString(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, Book> result) {
                handleSuccess(KAFKA_TOPIC, book.toString());
            }
        });

    }

    @PostMapping("/publishSync")
    public String publishMessageSync(@RequestBody Book book) throws ExecutionException, InterruptedException {
        SendResult<String, Book> result = kafkaProducer.sendSync(KAFKA_TOPIC, book);
        return "Publish successfully !!  Result : " + result.toString();
    }

    private void handleSuccess(String key, String value){
        System.out.println("Message Sent Successfully for the key : " + key + " and value is :" + value);
    }

    private void handleFailure(String key, String value, Throwable ex) {
        try {
            throw new Exception ("Error sending the message and ex: " + ex + " for the key : " + key+ " and value is :" + value);
        } catch (Exception e) {
            System.out.println("Error sending the message and ex: " + ex + " for the key : " + key+ " and value is :" + value);
        }
    }
}
