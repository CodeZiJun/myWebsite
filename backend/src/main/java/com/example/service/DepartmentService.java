package com.example.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.dto.Department;
import com.example.entity.vo.request.DepartmentAddVO;
import com.example.entity.vo.response.DepartmentVO;

public interface DepartmentService extends IService<Department> {
    IPage<DepartmentVO> selectDepartmentByDetailPage(Page<DepartmentVO> page, String detail);
    IPage<DepartmentVO> selectDepartmentPage(Page<DepartmentVO> page);
    String addDepartment(DepartmentAddVO vo);
}
