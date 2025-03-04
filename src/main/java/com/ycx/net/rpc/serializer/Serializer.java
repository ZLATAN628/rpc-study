package com.ycx.net.rpc.serializer;

public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
