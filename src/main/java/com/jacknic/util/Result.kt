package com.jacknic.util

/**
 * 请求响应结果工具类
 *
 * @author Jacknic
 */
object Result {

    /**
     * 响应ok状态
     */
    fun ok(): ResultBody {
        return ResultBody()
    }

    /**
     * 响应数据
     */
    fun data(data: Any?): ResultBody {
        val result = ok()
        result.data = data
        return result
    }

    /**
     * @param code 状态码
     * @param msg  提示信息
     */
    fun fail(code: Int, msg: String): ResultBody {
        val result = data(null)
        result.code = code
        result.msg = msg
        return result
    }
}
