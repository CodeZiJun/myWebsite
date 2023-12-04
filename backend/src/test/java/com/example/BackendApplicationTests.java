package com.example;

import com.deepoove.poi.XWPFTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import com.example.utils.docxUtils;
import org.springframework.util.ResourceUtils;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void encodePasswordUtil() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    @Test
    void testDocx() throws IOException {

    }
}
