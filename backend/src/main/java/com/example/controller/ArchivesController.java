package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.ArchivesVO;
import com.example.service.ArchivesService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/archives")
public class ArchivesController {
    @Resource
    ArchivesService archivesService;

    @GetMapping(value = "/selectDetailByEmail/{email}")
    String selectDetailArchivesByEmail(@PathVariable @Email String email) {
        ArchivesVO vo = archivesService.selectDetailArchivesByEmail(email);
        return vo == null ? RestBean.failure(400,"账户无档案").toJsonString()
                          : RestBean.success(vo).toJsonString();

    }
}
