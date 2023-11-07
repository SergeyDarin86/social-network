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
            log.error("333An Exception has occurred at Kafka Consumer: {}", exception.getLocalizedMessage());
//            doSeeks(records, consumer);
            log.error("334An Exception has occurred at Kafka Consumer: {}", exception.getClass());
            log.error("335An Exception has occurred at Kafka Consumer: {}", exception.getCause().toString());
            log.error("336An Exception has occurred at Kafka Consumer: {}", exception.getMessage());
            log.error("337An Exception has occurred at Kafka Consumer: {}", exception.fillInStackTrace().toString());
            log.error("338An Exception has occurred at Kafka Consumer: {}", exception.getStackTrace().toString());
            log.error("339An Exception has occurred at Kafka Consumer: {}", Arrays.stream(exception.getSuppressed()).toArray());

//            List n = Arrays.stream(exception.getStackTrace()).toList();
//            for (Object o : n){
//                System.out.println( "111" + o);
//            }

//            List nx = Arrays.stream(exception.fillInStackTrace().getStackTrace()).toList();
//            for (Object o : nx){
//                System.out.println( "112" + o);
//            }

//            List ngx = Arrays.stream(Objects.requireNonNull(exception).fillInStackTrace()).toList();
//            Object og = Objects.requireNonNull(exception).fillInStackTrace();
//            System.out.println("113" + og);
//            log.error("340An Exception has occurred at Kafka Consumer: {}", n) ;

//            exception.printStackTrace();

            //
            String error = exception.fillInStackTrace().toString();
            String[] components = error.split("partition");
            String[] components2 = components[1].split("If needed");
            String find = components2[0];

            for(String s : components) {
                System.out.println("115 " + s);
            }

            for(String s : components2) {
                System.out.println("116 " + s);
            }

            System.out.println("117 " + find);
            //

            //1
            String error2 = exception.fillInStackTrace().toString().split("key/value for partition")[1].split("If needed")[0];
            System.out.println("\n\t118 " + error2);

            String[] components3 = error2.split("at offset");
            for(String s : components3) {
                System.out.println("119 " + s);
            }

            /** Блок с получением offset */
            String offset = components3[1];
            offset = offset.replaceAll("\\D", "");
            System.out.println("120 offset = " + offset);
            int offsetInt = Integer.valueOf(offset);
            System.out.println("121 offsetInt = " + offsetInt);
            /** */

            /** Блок с получением partition и topic */
            String topPart = components3[0];
            String[] components4 = topPart.split("-");
            String topic = components4[0].trim();
            String partition = components4[1].trim();
            int partitionInt = Integer.valueOf(partition);
            System.out.println("122 topic = " + topic + " , partitionInt = " + partitionInt);
            /** */
            //1

//            /*
            TopicPartition topicPartition = new TopicPartition(topic, partitionInt);
            consumer.seek(topicPartition, offsetInt+1);                                                               //            consumer.assign(List.of(topicPartition));
            log.info("5KafkaErrorHandler: setOffset() - Выполнена установка offset = {},  partition = {}, topic = {}",
                    offsetInt, partitionInt, topic);
//            */

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

    private static void doSeek(List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer) {
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

         /*
            String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
            String topics = s.split("-")[0];
            int offset = Integer.valueOf(s.split("offset ")[1]);
            int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);
            */


//            /*
//            Optional<ConsumerRecord<?, ?>> optionalRecord = records.stream().findFirst();
//            ConsumerRecord<?, ?> record;
//            record = optionalRecord.get();
//            String topic = record.topic();
//            long offset = record.offset();
//            int partition = record.partition();
//            */
