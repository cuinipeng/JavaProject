package com.github.cuinipeng.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class Consumer {
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
    private Properties settings = new Properties();
    private KafkaConsumer consumer = null;
    private String brokers = "localhost:9092";
    private String topic = "test";
    private String group = "test_group";

    public Consumer() {}

    public Consumer(String brokers, String topic) {
        this.brokers = brokers;
        this.topic = topic;
    }

    public void run() {
        settings.put("bootstrap.servers", brokers);
        settings.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put("group.id", group);
        consumer = new KafkaConsumer<String, String>(settings);

        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.SECONDS));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info(String.format("topic = %s, partition = %s, offset = %d, key = %s, value = %s",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
