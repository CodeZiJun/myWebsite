package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Salary;
import com.example.service.SalaryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Resource
    SalaryService salaryService;
    @GetMapping(value = "/selectSalaryPage/{pageNum}/{pageSize}")
    public String selectAccountPage(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @RequestParam(required = false) String detail) {
        Page<Salary> page = new Page<>(pageNum, pageSize);
        IPage<Salary> iPage;
        if (detail == null){
            iPage = salaryService.selectSalaryPage(page, null);
        } else {
            iPage = salaryService.selectSalaryByDetailPage(page, detail);
        }
        return RestBean.success(iPage).toJsonString();
    }
}
