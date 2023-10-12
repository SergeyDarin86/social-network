package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaErrorHandler implements ContainerAwareErrorHandler {

    /**
     * @param exception
     * @param records
     * @param consumer
     * @param messageListenerContainer
     */
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
                    log.error("An Exception has occurred: {}, {},{},{}", topic, offset, partition, exception.getLocalizedMessage());
                }
            }
        } else {
            log.error("An Exception has occurred at Kafka Consumer: {}", exception.getLocalizedMessage());
        }
    }

    /**
     * Seeks/Checks up to which offset Kafka message was consumed
     * @param records
     * @param consumer
     */
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