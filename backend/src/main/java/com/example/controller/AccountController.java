package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.service.impl.AccountServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/selectAccountPage/{pageNum}/{pageSize}")
    public String selectAccountPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize) {
        Page<Account> page = new Page<>(pageNum, pageSize);
        IPage<Account> iPage = accountService.selectAccountPage(page);
        return RestBean.success(iPage).toJsonString();
    }


    @GetMapping("/charts")
    public RestBean charts() {
        //以下是将所有用户按照角色分类可视化的数据封装
        List<Account> accountList=accountService.list();
        Set<String> roles=accountList.stream().map(Account::getRole).collect(Collectors.toSet());
        List<String> roleList= CollUtil.newArrayList(roles);//角色列表



        List<Dict> linelist =new ArrayList<>();//折线图
        for(String role:roleList){
            Integer value=0;
            for(Account u : accountList){
                if(u.getRole().equals(role)){ value+=1;};
            }
            Dict dict=Dict.create();
            Dict line=dict.set("role",role).set("value",value);

            linelist.add(line);
        }


        List<Dict> barList = new ArrayList<>();//条形图

        for(String role:roleList){
            Integer value=0;
            for(Account u : accountList){
                if(u.getRole().equals(role)){ value+=1;};
            }
            Dict dict=Dict.create();
            Dict bar=dict.set("role",role).set("value",value);

            barList.add(bar);
        }





        Dict res = Dict.create().set("line", linelist).set("bar", barList);
        System.out.println(res);
        return RestBean.successDict(res);
    }
}
