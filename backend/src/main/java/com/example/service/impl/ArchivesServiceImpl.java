package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.Archives;
import com.example.entity.vo.response.ArchivesVO;
import com.example.mapper.ArchivesMapper;
import com.example.service.AccountService;
import com.example.service.ArchivesService;
import com.github.yulichang.query.MPJQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArchivesServiceImpl extends ServiceImpl<ArchivesMapper, Archives> implements ArchivesService {
    @Resource
    AccountService accountService;
    @Resource
    ArchivesMapper mapper;
    @Override
    public ArchivesVO selectDetailArchivesByEmail(String email) {
        Account account = accountService.selectOneByEmail(email);
        if (account != null) {
            return mapper.selectJoinOne(ArchivesVO.class,
                    new MPJQueryWrapper<Archives>()
                            .selectAll(Archives.class)
                            .eq("t.id", account.getArchivesId())
                            .select("dep.department_name")
                            .select("pos.position_name")
                            .leftJoin("db_department dep on t.department_id = dep.id")
                            .leftJoin("db_position pos on t.position_id = pos.id")
            );
        }
        else
            return null;
    }
}

