package com.netease.focusmonk.utils;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

/**
 * @ClassName RedisObjectSerializer
 * @Author konghaifeng
 * @Date 2019/5/8 15:28
 **/
public class RedisObjectSerializer implements RedisSerializer<Object> {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    @Override
    public byte[] serialize(Object object) throws SerializationException {

        if (object == null) return EMPTY_ARRAY;

        ObjectOutputStream obi;
        ByteArrayOutputStream bai;
        try {
            bai = new ByteArrayOutputStream();
            obi = new ObjectOutputStream(bai);
            obi.writeObject(object);
            return bai.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {

        if (isEmpty(bytes))return null;

        ObjectInputStream oii;
        ByteArrayInputStream bis;
        bis = new ByteArrayInputStream(bytes);
        try {
            oii = new ObjectInputStream(bis);
            return  oii.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
