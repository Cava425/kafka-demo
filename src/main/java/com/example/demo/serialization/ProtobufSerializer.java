package com.example.demo.serialization;


import com.example.demo.serialization.IProtoBuf;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ProtobufSerializer implements Serializer<IProtoBuf> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, IProtoBuf data) {
        return data.encode();
    }

    @Override
    public void close() {

    }
}
