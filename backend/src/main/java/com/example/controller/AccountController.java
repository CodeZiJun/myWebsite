package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.service.impl.AccountServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Resource
    AccountServiceImpl accountService;

    public record voAvatar(String avatar){}
    public record voUsername(String username){}
    @PostMapping(value = "/updateAvatar")
    public String updateAvatar(@RequestParam MultipartFile avatar) {
        if(accountService.updateAvatar(avatar) == null){
            Account currentLoginUser = accountService.getCurrentLoginUser();
            voAvatar vo = new voAvatar(currentLoginUser.getAvatar());
            return RestBean.success(vo).toJsonString();
        }
        else
            return RestBean.failure(400,"服务器内部错误").toJsonString();
    }

    @PostMapping(value = "/updateUsername")
    public String updateUsername(@RequestParam String username) {
        if(accountService.updateUsername(username) == null) {
            Account currentLoginUser = accountService.getCurrentLoginUser();
            voUsername vo = new voUsername(currentLoginUser.getUsername());
            return RestBean.success(vo).toJsonString();
        }
        else
            return RestBean.failure(400, "服务器内部错误").toJsonString();
    }
}
