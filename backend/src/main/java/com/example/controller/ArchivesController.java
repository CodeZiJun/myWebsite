package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ArchivesAddVO;
import com.example.entity.vo.response.ArchivesVO;
import com.example.service.ArchivesService;
import com.example.service.CommissionService;
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

    @Resource
    CommissionService commissionService;

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

    @GetMapping(value = "/commissionDepartment/{email}/{departmentId}")
    String commissionDepartment(@PathVariable @Email String email, @PathVariable Integer departmentId) {
        String message = commissionService.commissionDepartment(email, departmentId);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }

    @GetMapping(value = "/commissionPosition/{email}/{positionId}")
    String commissionPosition(@PathVariable @Email String email, @PathVariable Integer positionId) {
        String message = commissionService.commissionPosition(email, positionId);
        return message == null ? RestBean.success().toJsonString() : RestBean.failure(400, message).toJsonString();
    }
}
