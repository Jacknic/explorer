package com.jacknic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * json操作工具类
 *
 * @author Jacknic
 */
public class JSON {
    public final static ObjectMapper JSON_MAPPER = new ObjectMapper();

    private JSON() {
    }

    /**
     * 返回错误信息
     */
    private static String error(String msg) {
        return "{\"code\":500,\"msg\":\"发生错误：" + msg + "\"}";
    }

    /**
     * 对象转JSON字符
     */
    public static String string(Object object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 向输出流写入JSON数据
     */
    public static void write(OutputStream outputStream, Object object) {
        try {
            JSON_MAPPER.writeValue(outputStream, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
