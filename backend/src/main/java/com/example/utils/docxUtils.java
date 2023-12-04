package com.example.utils;

import com.deepoove.poi.XWPFTemplate;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class docxUtils {
    public XWPFTemplate docxInject(String templateClassPath, Map<String, Objects> params) throws IOException {
        ClassPathResource resource = new ClassPathResource(templateClassPath);
        File file = resource.getFile();
        return XWPFTemplate.compile(file).render(params);
//        OutputStream outputStream = new FileOutputStream("./testdoc.docx");
//        template.write(outputStream);
    }

}
