package com.springbootkafka.example.kafkaSpringConnection;

import com.springbootkafka.example.kafkaSpringConnection.KafkaProducer.KafkaProducer;
import com.springbootkafka.example.kafkaSpringConnection.model.Book;
import com.springbootkafka.example.kafkaSpringConnection.model.BookType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    private static final String KAFKA_TOPIC = "NewTopic";

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Book book) {
        book.setBookType(BookType.NEW);
        kafkaProducer.send(KAFKA_TOPIC,book);
        return "Publish successfully !!";
    }

    @PostMapping("/publishAsync")
    public void publishMessageCallback(@RequestBody Book book) {
        book.setBookType(BookType.NEW);
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
        book.setBookType(BookType.NEW);
        SendResult<String, Book> result = kafkaProducer.sendSync(KAFKA_TOPIC, book);
        return "Publish successfully !!  Result : " + result.toString();
    }

    @PostMapping("/publishRec")
    public void publishMessageRecord(@RequestBody Book book){
        book.setBookType(BookType.NEW);
        ProducerRecord<String, Book> producerRecord = buildProducerRecord(book.getIsbn(), book, KAFKA_TOPIC);
        ListenableFuture<SendResult<String,Book>> listenableFuture = kafkaProducer.sendProducer(producerRecord);
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

    private ProducerRecord<String, Book> buildProducerRecord(String key, Book value, String topic) {
        List<Header> recordHeader = List.of(new RecordHeader("event","scanner".getBytes()));
        return new ProducerRecord<>(KAFKA_TOPIC, null, key, value, recordHeader);
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
