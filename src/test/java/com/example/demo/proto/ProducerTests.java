package com.example.demo.proto;

import com.example.demo.serialization.ProtobufSerializer;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class ProducerTests {

    @Test
    public void producer() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        // 生产者重试
        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        /**
         * acks = 1
         *  默认值即为1。生产者发送消息之后，只要分区的 leader 副本成功写入消息，那么它就会收到来自服务端的成功响应。
         * acks = 0
         *  生产者发送消息之后不需要等待任何服务端的响应。如果在消息从发送到写入 Kafka 的过程中出现某些异常，
         *  导致 Kafka 并没有收到这条消息，那么生产者也无从得知，消息也就丢失了。
         * acks = -1 或 acks = all
         *  生产者在消息发送之后，需要等待 ISR 中的所有副本都成功写入消息之后才能够收到来自服务端的成功响应。
         *  在其他配置环境相同的情况下，acks 设置为 -1（all） 可以达到最强的可靠性。
         */
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        KafkaProducer<String, User> producer = new KafkaProducer<>(props);
        User user = new User();
        user.setId(1);
        user.setName("Li Hua");
        user.setAge(25);
        ProducerRecord<String, User> record = new ProducerRecord<>("test_topic", user);
        Future<RecordMetadata> metadataFuture = producer.send(record);
        RecordMetadata recordMetadata = metadataFuture.get();
        log.info("发送消息结果:topic={}, partition={}, offset={}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());

        Thread.sleep(5000);
    }

}
