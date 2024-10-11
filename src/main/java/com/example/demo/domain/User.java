package com.example.demo.domain;

import com.example.demo.serialization.IProtoBuf;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User implements IProtoBuf {

    private static Logger logger = LoggerFactory.getLogger(User.class);

    long id;
    String name;
    int age;

    //有参构造及无参构造
    public User(){}

    public User(int id, String name, int age){
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    //重写toString
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }


    @Override
    public byte[] encode() {
        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setId(id);
        builder.setName(name);
        builder.setAge(age);
        return builder.build().toByteArray();
    }

    public User(byte[] bytes){
        try {
            UserProto.User user = UserProto.User.parseFrom(bytes);
            this.id = user.getId();
            this.name = user.getName();
            this.age = user.getAge();
        } catch (InvalidProtocolBufferException e) {
            logger.error("User反序列化失败:{}",e.getMessage());
        }
    }

    //set get 方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
