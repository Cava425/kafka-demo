package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;
import java.util.concurrent.Future;

@Slf4j
@SpringBootTest
public class ProducerTests {

    @Test
    public void test(){
        System.out.println(123);
    }

    /**
     * 同步发送方式，发送结束后调用Feature方法，阻塞等待Kafka响应，直到发送成功或者发生异常
     * @throws Exception
     */
    @Test
    public void kafkaProducerSync() throws Exception{
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
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

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>("test_topic", "Hello Kafka!");
        Future<RecordMetadata> metadataFuture = producer.send(record);
        RecordMetadata recordMetadata = metadataFuture.get();
        log.info("发送消息结果:topic={}, partition={}, offset={}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
    }

    @Test
    public void kafkaProducerAsync() throws Exception{
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>("test_topic", "Hello Kafka!");
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e != null){
                    // 发送失败
                    e.printStackTrace();
                }else {
                    log.info("发送消息结果:topic={}, partition={}, offset={}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                }
            }
        });
        // 等待回调
        Thread.sleep(5000);
    }
}
