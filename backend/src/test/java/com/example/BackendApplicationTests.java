package com.example;

import com.example.entity.dto.Account;
import com.example.entity.vo.response.ArchivesVO;
import com.example.mapper.DepartmentMapper;
import com.example.service.AccountService;
import com.example.service.ArchivesService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@SpringBootTest
class BackendApplicationTests {
    @Resource
    ArchivesService archivesService;

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
        ArchivesVO vo = archivesService.selectDetailArchivesByEmail("1391923299@qq.com");
        System.out.println(vo);
    }
}
