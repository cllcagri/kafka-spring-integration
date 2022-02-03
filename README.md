# Setting Up Kafka

## Mac

- Make sure you are navigated inside the bin directory.

### Start Zookeeper and Kafka Broker

-   Start up the Zookeeper.

```
./zookeeper-server-start.sh ../config/zookeeper.properties
```

- Add the below properties in the server.properties

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```

-   Start up the Kafka Broker

```
./kafka-server-start.sh ../config/server.properties
```

### Create a topic

```
kafka-topics.sh --create --topic NewTopic --bootstrap-server 127.0.0.1:9092 --replication-factor 1 --partitions 2
```

### Instantiate a Console Producer

#### Without Key

```
./kafka-console-producer.sh --bootstrap-server localhost:9092 --topic NewTopic
```

#### With Key

```
./kafka-console-producer.sh --bootstrap-server localhost:9092 --topic NewTopic--property "key.separator=-" --property "parse.key=true"
```

### Instantiate a Console Consumer

#### Without Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NewTopic --from-beginning
```

#### With Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NewTopic --from-beginning -property "key.separator= - " --property "print.key=true"
```
