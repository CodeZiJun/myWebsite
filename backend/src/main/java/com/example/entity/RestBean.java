package com.example.entity;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

//这里是为了给前端的请求返回更标准的json响应
public record RestBean<T>(int code, T data, String message) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功");
    }

    public static <T> RestBean<T> success(int code, String message) {
        return new RestBean<>(200,null, message);
    }

    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    //401
    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }

    //403
    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message);
    }

    //封装一个方法 转化为json 使用alibaba的fastJson2
    public String toJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls); //writeNulls指对空数据也会返回json 避免出问题
    }
}
