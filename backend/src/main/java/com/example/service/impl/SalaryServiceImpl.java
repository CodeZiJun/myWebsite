package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Salary;
import com.example.mapper.SalaryMapper;
import com.example.service.SalaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {
    @Resource
    SalaryMapper salaryMapper;
    @Override
    public IPage<Salary> selectSalaryByDetailPage(Page<Salary> page, String detail) {
        QueryWrapper<Salary> wrapper = new QueryWrapper<>();
        if (!"".equals(detail)) {
            wrapper.like("id", detail).or()
                    .like("amount", detail);
        }
        return selectSalaryPage(page, wrapper);
    }

    @Override
    public IPage<Salary> selectSalaryPage(Page<Salary> page, Wrapper<Salary> wrapper) {
        return salaryMapper.selectPage(page, wrapper);
    }
}
