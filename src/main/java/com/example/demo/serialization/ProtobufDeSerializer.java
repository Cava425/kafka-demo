package com.example.demo.serialization;

import com.example.demo.domain.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ProtobufDeSerializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public User deserialize(String topic, byte[] data) {
        return new User(data);
    }

    @Override
    public void close() {

    }
}
