package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ArchivesAddVO;
import com.example.entity.vo.response.ArchivesVO;
import com.example.service.ArchivesService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/addArchives")
    String addArchives(@RequestBody @Validated ArchivesAddVO vo) {
        String message = archivesService.addArchives(vo);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }
}
