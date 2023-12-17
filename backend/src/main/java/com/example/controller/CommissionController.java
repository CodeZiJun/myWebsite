package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.vo.response.CommissionVO;
import com.example.service.CommissionService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/commission")
public class CommissionController {
    @Resource
    CommissionService service;
    @GetMapping(value = "/selectPage/{pageNum}/{pageSize}")
    public String selectAllcommissionStatus(@PathVariable Integer pageNum,
                                            @PathVariable Integer pageSize,
                                            @RequestParam(required = false) String detail) {
        Page<CommissionVO> page = new Page<>(pageNum, pageSize);
        IPage<CommissionVO> iPage;
        if (detail == null) {
            iPage = service.selectCommissionPage(page);
        } else {
            iPage = service.selectCommissionByDetailPage(page, detail);
        }
        return RestBean.success(iPage).toJsonString();
    }

}
