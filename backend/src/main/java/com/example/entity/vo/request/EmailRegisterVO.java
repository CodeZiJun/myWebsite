package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class EmailRegisterVO {
    @Email
    String email;
    @Length(max = 6, min = 6)
    String code;
    //用户名不可包含非法字符
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
            @Length(min = 1, max = 20)
    String username;
    @Length(min = 6, max = 20)
    String password;
}
