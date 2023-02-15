package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@SpringBootTest
public class ConsumerTests {

    /**
     * - 配置消费者客户端参数，创建消费者实例
     * - 订阅主题
     * - 拉取消息并消费
     * - 提交消费位移
     * - 关闭消费者实例
     */

    @Test
    public void test(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group.demo");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer.client.id.demo");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        /**
         * - earliest 从开始处消费
         * - latest   从结尾开始消费
         */
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = null;
        try{
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("test_topic"));
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.SECONDS));
                for(ConsumerRecord<String, String> record : records){
                    log.info("收到消息: topic={},  partition={},  offset={},  key={},  value={}", record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
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
