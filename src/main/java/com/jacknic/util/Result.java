package com.jacknic.util;

public class Result {

    private Result() {
    }

    /**
     * 响应ok状态
     */
    public static ResultBody ok() {
        return new ResultBody();
    }

    /**
     * 响应数据
     */
    public static ResultBody data(Object data) {
        ResultBody result = ok();
        result.setData(data);
        return result;
    }

    /**
     * @param code 状态码
     * @param msg  提示信息
     */
    public static ResultBody fail(int code, String msg) {
        ResultBody result = data(null);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
