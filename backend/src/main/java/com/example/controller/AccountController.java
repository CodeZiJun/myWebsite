package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.service.impl.AccountServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping(value = "/selectAccountPage/{pageNum}/{pageSize}")
    public String selectAccountPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               @RequestParam(required = false) String detail) {
        Page<Account> page = new Page<>(pageNum, pageSize);
        IPage<Account> iPage;
        if (detail == null){
            iPage = accountService.selectAccountPage(page, null);
        } else {
            iPage = accountService.selectAccountByDetailPage(page, detail);
        }
        return RestBean.success(iPage).toJsonString();
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {
        if (accountService.removeById(id))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @DeleteMapping(value = "/deleteBatch")
    public String deleAccountBatchByids(@RequestBody(required = true) List<Integer> ids) {
        if (accountService.removeBatchByIds(ids))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }
}
