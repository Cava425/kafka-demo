package com.example.demo.proto;

import com.example.demo.serialization.ProtobufDeSerializer;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class ConsumerTests {

    @Test
    public void consumer(){

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProtobufDeSerializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group.demo");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer.client.id.demo");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        /**
         * - earliest 从开始处消费
         * - latest   从结尾开始消费
         */
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, User> consumer = null;
        try{
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("test_topic"));
            while (true){
                ConsumerRecords<String, User> records = consumer.poll(Duration.of(1, ChronoUnit.SECONDS));
                for(ConsumerRecord<String, User> record : records){
                    log.info("收到消息: topic={},  partition={},  offset={},  key={},  value={}", record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
                    //User user = new User(record.value());
                    log.info("序列化为User对象：{}", record.value());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(consumer != null){
                consumer.close();
            }
        }

    }

}
