package com.example.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Salary;

public interface SalaryService extends IService<Salary> {
    IPage<Salary> selectSalaryByDetailPage(Page<Salary> page, String detail);
    IPage<Salary> selectSalaryPage(Page<Salary> page, Wrapper<Salary> wrapper);
}
