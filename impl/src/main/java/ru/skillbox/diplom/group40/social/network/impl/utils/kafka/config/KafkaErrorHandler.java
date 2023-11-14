package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaErrorHandler implements ContainerAwareErrorHandler {

    @Override
    public void handle(Exception exception, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
                       MessageListenerContainer messageListenerContainer) {
        if (!records.isEmpty()) {
            doSeeks(records, consumer);
            Optional<ConsumerRecord<?, ?>> optionalRecord = records.stream().findFirst();
            ConsumerRecord<?, ?> record;
            if (optionalRecord.isPresent()) {
                record = optionalRecord.get();
                String topic = record.topic();
                long offset = record.offset();
                int partition = record.partition();
                if (exception.getClass().equals(DeserializationException.class)) {
                    DeserializationException deserializationException = (DeserializationException) exception;
                    log.error("Malformed Message Deserialization Exception: {}, {},{},{}", topic, offset,
                            String.valueOf(deserializationException.getData()),
                            deserializationException.getLocalizedMessage());
                } else {
                    log.error("An Exception has occurred: {}, {},{},{}", topic, offset, partition,
                            exception.getLocalizedMessage());
                }
            }
        } else {
            log.error("KafkaErrorHandler: An Exception has occurred at Kafka Consumer: {}",
                    exception.getLocalizedMessage());

            String error = exception.fillInStackTrace().toString()
                                .split("key/value for partition")[1]
                                .split("If needed")[0];

            String[] components = error.split("at offset");

            int offsetInt = Integer.valueOf(components[1].replaceAll("\\D", ""));

            String[] topicComponents = components[0].split("-");

            String topic = topicComponents[0].trim();

            int partitionInt = Integer.valueOf(topicComponents[1].trim());


            log.info("KafkaErrorHandler: An Exception has occurred at Kafka Consumer: Получен offset = {}, " +
                            "partition = {}, topic = {}", offsetInt, partitionInt, topic);

            TopicPartition topicPartition = new TopicPartition(topic, partitionInt);
            consumer.seek(topicPartition, offsetInt+1);
            log.info("KafkaErrorHandler: setOffset() - Выполнена установка offset = {},  partition = {}, topic = {}",
                    offsetInt+1, partitionInt, topic);
        }
    }


    private static void doSeeks(List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer) {
        Map<TopicPartition, Long> partitions = new LinkedHashMap<>();
        AtomicBoolean first = new AtomicBoolean(true);
        records.forEach((ConsumerRecord<?, ?> record) -> {
            if (first.get()) {
                partitions.put(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
            } else {
                partitions.computeIfAbsent(new TopicPartition(record.topic(), record.partition()),
                        offset -> record.offset());
            }
            first.set(false);
        });
        partitions.forEach(consumer::seek);
    }
}