package com.jacknic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

public class JSON {
    public final static ObjectMapper objectMapper = new ObjectMapper();

    private JSON() {
    }

    /**
     * 返回错误信息
     */
    private static String error(Exception e) {
        return "{\"code\":500,\"msg\":\"发生错误：" + e.getMessage() + "\"}";
    }

    /**
     * 对象转JSON字符
     */
    public static String string(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return error(e);
        }
    }

    /**
     * 向输出流写入JSON数据
     */
    public static void write(OutputStream outputStream, Object object) {
        try {
            objectMapper.writeValue(outputStream, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
