package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.vo.request.DepartmentAddVO;
import com.example.entity.vo.response.DepartmentVO;
import com.example.service.DepartmentService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Resource
    DepartmentService service;

    @GetMapping(value = "/selectPage/{pageNum}/{pageSize}")
    public String selectPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             @RequestParam(required = false) String detail) {
        Page<DepartmentVO> page = new Page<>(pageNum, pageSize);
        IPage<DepartmentVO> iPage;
        if (detail == null) {
            iPage = service.selectDepartmentPage(page);
        } else {
            iPage = service.selectDepartmentByDetailPage(page, detail);
        }
        return RestBean.success(iPage).toJsonString();
    }

    @PostMapping(value = "/addDepartment")
    public String addDepartment(@RequestBody @Validated DepartmentAddVO vo) {
        String message = service.addDepartment(vo);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteDepartmentById(@PathVariable Integer id) {
        if (service.removeById(id))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @DeleteMapping(value = "/deleteBatch")
    public String deleAccountBatchByids(@RequestBody List<Integer> ids) {
        if (service.removeBatchByIds(ids))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }
}
