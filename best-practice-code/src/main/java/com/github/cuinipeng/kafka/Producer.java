package com.github.cuinipeng.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class Producer {
    private static Logger logger = LoggerFactory.getLogger(Producer.class);
    private Properties settings = new Properties();
    private KafkaProducer producer = null;
    private String brokers = "localhost:9092";
    private String topic = "test";

    public Producer() {}

    public Producer(String brokers, String topic) {
        this.brokers = brokers;
        this.topic = topic;
    }

    public void run() {
        settings.put("bootstrap.servers", "localhost:9092");
        settings.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        settings.put("acks", "all");
        settings.put("buffer.memory", 64 * 1024 * 1024);
        settings.put("compression.type", "lz4");   // gzip/lz4/snappy
        settings.put("retries", 5);
        producer = new KafkaProducer<String, String>(settings);

        try {
            long idx = 0;
            while (true) {
                String key = "k" + idx;
                String value = "v" + idx;
                idx++;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
                // Fire-and-forget
                // producer.send(record);
                // Synchronous send
                // RecordMetadata meta = (RecordMetadata)producer.send(record).get();
                // logger.info(String.format("topic: %s, partition: %s, offset: %s",
                //         meta.topic(), meta.partition(), meta.offset()));
                // Asynchronous send
                producer.send(record, (recordMetadata, ex) -> {
                    if (ex != null) ex.printStackTrace();
                    logger.info(String.format("topic: %s, partition: %s, offset: %s",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset()));
                });
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}

