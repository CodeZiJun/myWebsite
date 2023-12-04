package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

//vo.response 存放对前端响应的实体类 也就是响应的views
//这里封装的是对前端认证响应的实体类
@Data
public class AuthorizeVO {
    String username;
    String role;
    String token;
    Date expire;
}
