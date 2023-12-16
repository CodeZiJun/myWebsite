package com.example;

import com.example.entity.dto.Account;
import com.example.entity.dto.Department;
import com.example.entity.vo.response.DepartmentVO;
import com.example.mapper.DepartmentMapper;
import com.example.service.AccountService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {
    @Resource
    AccountService service;
    @Resource
    DepartmentMapper departmentMapper;
    @Test
    void contextLoads() {
        Account account =service.query()
                .eq("binary username", "CodeZijun")
                .one();
        System.out.println(account);
    }

    @Test
    void encodePasswordUtil() {
        System.out.println(new BCryptPasswordEncoder().encode("codezijun"));
    }

    @Test
    void testDocx() throws IOException {

    }

    @Test
    void testMybatisPlus() {
//        List<DepartmentVO> vos = departmentMapper.selectJoinList(DepartmentVO.class,
//                new MPJQueryWrapper<Department>()
//                        .selectAll(Department.class)
//                        .select("acc.username")
//                        .leftJoin("db_account acc on t.minister_id = acc.id"));
//        vos.forEach(System.out::println);
        String detail = "";
        detail = "".equals(detail) ? null : detail;
        List<DepartmentVO> vos = departmentMapper.selectJoinList( DepartmentVO.class,
                new MPJQueryWrapper<Department>()
                        .selectAll(Department.class)
                        .eq("t.department_name", detail)
                        .or()
                        .eq("acc.username", detail)
                        .or()
                        .eq("t.id",detail)
                        .select("acc.username")
                        .leftJoin("db_account acc on t.minister_id = acc.id"));
        vos.forEach(System.out::println);
    }
}
