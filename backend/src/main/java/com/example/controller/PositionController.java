package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/charts")
    public String charts() {
        //以下是将所有用户按照角色分类可视化的数据封装
        List<Position> positionList=service.list();
        Set<String> names=positionList.stream().map(Position::getPositionName).collect(Collectors.toSet());
        List<String> nameList= CollUtil.newArrayList(names);//角色列表
        List<Dict> linelist =new ArrayList<>();//折线图
        for(String name:nameList){
            Integer s=0;
            for(Position p : positionList){
                if(p.getPositionName().equals(name)){

                    Integer id=p.getSalaryId();
                    Salary salary=salaryMapper.selectById(id);
                    s =salary.getAmount();

                }
            }
            Dict dict=Dict.create();
            Dict line=dict.set("name",name).set("salary",s);

            linelist.add(line);
        }



        Dict res = Dict.create().set("positionLine", linelist);
        System.out.println(res);
        return RestBean.success(res).toJsonString();
    }
}
