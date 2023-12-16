package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Archives;
import com.example.entity.vo.response.ArchivesVO;

public interface ArchivesService extends IService<Archives> {
    ArchivesVO selectDetailArchivesByEmail(String email);
}
