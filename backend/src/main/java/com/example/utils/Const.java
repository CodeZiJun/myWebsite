package com.example.utils;

//存储常量
public class Const {
    //redis前缀JWT黑名单
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    //跨域拦截器优先级
    public static final int ORDER_CORS = -102 ;

    public static final int ORDER_LIMIT = -101;

    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    public static final String FLOW_LIMIT_COUNTER = "flow:counter:";

    public static final String FLOW_LIMIT_BLOCK = "flow:block:";
}
