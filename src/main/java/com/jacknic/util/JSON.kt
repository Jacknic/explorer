package com.jacknic.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

import java.io.IOException
import java.io.OutputStream

/**
 * json操作工具类
 *
 * @author Jacknic
 */
object JSON {
    private val JSON_MAPPER = ObjectMapper()

    /**
     * 返回错误信息
     */
    private fun error(msg: String): String {
        return "{\"code\":500,\"msg\":\"$msg\"}"
    }

    /**
     * 对象转JSON字符
     */
    fun string(`object`: Any): String {
        return try {
            JSON_MAPPER.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            error("发生错误：${e.message}")
        }

    }

    /**
     * 向输出流写入JSON数据
     */
    fun write(outputStream: OutputStream, `object`: Any) {
        try {
            JSON_MAPPER.writeValue(outputStream, `object`)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
