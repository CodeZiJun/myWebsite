package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Salary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalaryMapper extends BaseMapper<Salary> {
}
