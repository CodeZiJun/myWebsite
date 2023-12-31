package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.AccountAddVO;
import com.example.entity.vo.request.AccountUpdateVO;
import com.example.service.impl.AccountServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(value = "/selectUsernameByEmail/{email}")
    public String selectUsernameByEmail(@PathVariable("email") String email) {
        if(accountService.selectOneByEmail(email) == null)
            return RestBean.failure(400, "未查询到此邮箱用户").toJsonString();
        else
            return RestBean.success(new voUsername(accountService.selectOneByEmail(email).getUsername())).toJsonString();
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {
        if (accountService.removeById(id))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @DeleteMapping(value = "/deleteBatch")
    public String deleAccountBatchByids(@RequestBody List<Integer> ids) {
        if (accountService.removeBatchByIds(ids))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @PostMapping(value = "/addAccount")
    public String addAccount(@RequestBody @Valid AccountAddVO vo) {
        String message = accountService.addAccount(vo);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }

    @PostMapping(value = "/updateAccountBatch")
    public String updateAccountBatch(@RequestBody List<AccountUpdateVO> vos) {
        String message;
        List<Account> accounts = new ArrayList<>();
        for(AccountUpdateVO vo : vos) {
            Account account = new Account();
            account.setRole(vo.getRole());
            account.setEmail(vo.getEmail());
            account.setUsername(vo.getUsername());
            account.setId(vo.getId());
            accounts.add(account);
        }
        try{
            if(accountService.updateBatchById(accounts))
                message = null;
            else
                message = "内部错误，请联系管理员";
        } catch (Exception e) {
            message = "无法更改";
        }

        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }

    @GetMapping("/charts")
    public String charts() {
        //以下是将所有用户按照角色分类可视化的数据封装
        List<Account> accountList=accountService.list();
        Set<String> roles=accountList.stream().map(Account::getRole).collect(Collectors.toSet());
        List<String> roleList= CollUtil.newArrayList(roles);//角色列表
        List<Dict> linelist =new ArrayList<>();//折线图
        for(String role:roleList){
            Integer value=0;
            for(Account u : accountList){
                if(u.getRole().equals(role)){ value+=1;}
            }
            Dict dict=Dict.create();
            Dict line=dict.set("role",role).set("value",value);

            linelist.add(line);
        }
        List<Dict> barList = new ArrayList<>();//条形图

        for(String role:roleList){
            Integer value=0;
            for(Account u : accountList){
                if(u.getRole().equals(role)){ value+=1;}
            }
            Dict dict=Dict.create();
            Dict bar=dict.set("role",role).set("value",value);

            barList.add(bar);
        }

        Dict res = Dict.create().set("line", linelist).set("bar", barList);
        System.out.println(res);
        return RestBean.success(res).toJsonString();
    }
}
