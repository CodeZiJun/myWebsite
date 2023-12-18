package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Department;
import com.example.entity.dto.Position;
import com.example.entity.dto.Salary;
import com.example.entity.vo.response.CommissionVO;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.PositionMapper;
import com.example.service.CommissionService;
import com.example.service.DepartmentService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/commission")
public class CommissionController {
    @Resource
    CommissionService service;
    @Resource
    DepartmentMapper departmentMapper;
    @Resource
    PositionMapper positionMapper;

    @GetMapping(value = "/selectPage/{pageNum}/{pageSize}/{flag}")
    public String selectAllcommissionStatus(@PathVariable Integer pageNum,
                                            @PathVariable Integer pageSize,
                                            @PathVariable Integer flag,
                                            @RequestParam(required = false) String detail) {
        Page<CommissionVO> page = new Page<>(pageNum, pageSize);
        IPage<CommissionVO> iPage;
        if (detail == null) {
            iPage = service.selectCommissionPage(page,flag);
        } else {
            iPage = service.selectCommissionByDetailPage(page, detail,flag);
        }
        return RestBean.success(iPage).toJsonString();
    }

    @GetMapping(value = "/selectDepartmentById/{departmentId}")
    public String selectDepartmentById(@PathVariable("departmentId") Integer id) {
        Department department = departmentMapper.selectById(id);
        return department == null ? RestBean.failure(400, "编号错误").toJsonString()
                : RestBean.success(department).toJsonString();
    }

    @GetMapping(value = "/selectPositionById/{positionId}")
    public String selectPositionById(@PathVariable("positionId") Integer id) {
        Position position = positionMapper.selectById(id);
        return position == null ? RestBean.failure(400, "编号错误").toJsonString()
                : RestBean.success(position).toJsonString();
    }
}
