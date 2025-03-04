package com.ycx.net.rpc.serializer;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.ycx.pojo.PersonOuterClass;
import com.ycx.test.pojo.PatRegisterOuterClass;

public class ProtobufSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        if (obj instanceof GeneratedMessage) {
            GeneratedMessage message = (GeneratedMessage) obj;
            return message.toByteArray();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return (T) PatRegisterOuterClass.PatRegisterList.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
