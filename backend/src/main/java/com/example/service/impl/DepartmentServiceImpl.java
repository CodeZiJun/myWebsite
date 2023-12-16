package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Department;
import com.example.entity.vo.request.DepartmentAddVO;
import com.example.entity.vo.response.DepartmentVO;
import com.example.mapper.DepartmentMapper;
import com.example.service.AccountService;
import com.example.service.DepartmentService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Resource
    DepartmentMapper mapper;
    @Resource
    AccountService accountService;
    @Override
    public IPage<DepartmentVO> selectDepartmentByDetailPage(Page<DepartmentVO> page, String detail) {
        if(!"".equals(detail))
            return mapper.selectJoinPage(page, DepartmentVO.class,
                new MPJQueryWrapper<Department>()
                        .selectAll(Department.class)
                        .like("t.department_name", detail).or()
                        .like("t.id",detail).or()
                        .like("acc.username", detail).or()
                        .like("acc.email", detail)
                        .select("acc.username", "acc.email")
                        .leftJoin("db_account acc on t.minister_id = acc.id"));
        else
            return selectDepartmentPage(page);

    }

    @Override
    public IPage<DepartmentVO> selectDepartmentPage(Page<DepartmentVO> page) {
        return mapper.selectJoinPage(page, DepartmentVO.class,
                new MPJQueryWrapper<Department>()
                        .selectAll(Department.class)
                        .select("acc.username", "acc.email")
                        .leftJoin("db_account acc on t.minister_id = acc.id"));
    }

    @Override
    public String addDepartment(DepartmentAddVO vo) {
        Integer id = accountService.selectOneByEmail(vo.getEmail()).getId();
        Department department = new Department();
        department.setDepartmentName(vo.getDepartmentName());
        department.setMinisterId(id);
        if (this.save(department))
            return null;
        else
            return "内部错误，请联系管理员";
    }
}
