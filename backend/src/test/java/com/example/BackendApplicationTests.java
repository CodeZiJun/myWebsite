package com.example;

import com.example.entity.dto.Account;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@SpringBootTest
class BackendApplicationTests {
    @Resource
    AccountService service;
    @Test
    void contextLoads() {
        Account account =service.query()
                .eq("binary username", "CodeZijun")
                .one();
        System.out.println(account);
    }

    @Test
    void encodePasswordUtil() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    @Test
    void testDocx() throws IOException {

    }
}
