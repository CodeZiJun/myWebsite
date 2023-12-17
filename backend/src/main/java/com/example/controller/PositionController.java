package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Position;
import com.example.entity.dto.Salary;
import com.example.entity.vo.request.PositionAddVO;
import com.example.entity.vo.request.PositionUpdateVO;
import com.example.entity.vo.response.PositionVO;
import com.example.mapper.SalaryMapper;
import com.example.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/position")
public class PositionController {
    @Resource
    PositionService service;

    @Resource
    SalaryMapper salaryMapper;

    @GetMapping(value = "/selectSalaryById/{salaryId}")
    public String selectSalaryById(@PathVariable("salaryId") Integer id) {
        Salary salary = salaryMapper.selectById(id);
        return salary == null ? RestBean.failure(400, "此等级不存在").toJsonString()
                : RestBean.success(salary).toJsonString();
    }

    @GetMapping(value = "/selectPage/{pageNum}/{pageSize}")
    public String selectPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             @RequestParam(required = false) String detail) {
        Page<PositionVO> page = new Page<>(pageNum, pageSize);
        IPage<PositionVO> iPage;
        if (detail == null) {
            iPage = service.selectPositionPage(page);
        } else {
            iPage = service.selectPositionByDetailPage(page, detail);
        }
        return RestBean.success(iPage).toJsonString();
    }

    @PostMapping(value = "/addPosition")
    public String addDepartment(@RequestBody @Validated PositionAddVO vo) {
        String message = service.addPosition(vo);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deletePositionById(@PathVariable Integer id) {
        if (service.removeById(id))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @DeleteMapping(value = "/deleteBatch")
    public String deletePostionByids(@RequestBody List<Integer> ids) {
        if (service.removeBatchByIds(ids))
            return RestBean.success().toJsonString();
        else
            return RestBean.failure(400, "删除失败").toJsonString();
    }

    @PostMapping(value = "/updateBatch")
    public String updatePositionBatch(@RequestBody List<PositionUpdateVO> vos) {
        String message;
        List<Position> positions = new ArrayList<>();
        for(PositionUpdateVO vo : vos) {
            Position position = new Position();
            position.setId(vo.getId());
            position.setPositionName(vo.getPositionName());
            position.setSalaryId(vo.getSalaryId());
            positions.add(position);
        }
        try{
            if(service.updateBatchById(positions))
                message = null;
            else
                message = "内部错误，请联系管理员";
        } catch (Exception e) {
            message = "无法更改";
        }
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }
}
